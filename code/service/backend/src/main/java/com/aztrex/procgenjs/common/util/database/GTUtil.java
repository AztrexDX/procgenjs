package com.aztrex.procgenjs.common.util.database;

import com.aztrex.procgenjs.common.database.DataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class GTUtil {

    private final JdbcTemplate jdbcTemplate; // Autowire the main JdbcTemplate
    private final DataSourceContextHolder dataSourceContextHolder;

    // Common columns for every dynamic table
    public static final String COMMON_TABLE_SCHEMA_SQL_SQLITE =
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "itemId TEXT UNIQUE NOT NULL, " + // itemId is already unique, so an index is implicitly created by SQLite for UNIQUE constraint
            "title TEXT, " +
            "description TEXT, " +
            "details TEXT, " +
            "notes TEXT, " +
            "category TEXT, " +
            "subcategory TEXT, " +
            "group_name TEXT, " + // Renamed from "group"
            "subgroup TEXT, " +
            "section TEXT, " +
            "tags TEXT, " +
            "content TEXT, " +         // For JSON data
            "configuration TEXT, " +   // For JSON config
            "reference TEXT, " +
            "from_node_id INTEGER, " + // For hierarchy
            "dateCreated TEXT NOT NULL, " +
            "dateUpdated TEXT NOT NULL, " +
            "FOREIGN KEY (from_node_id) REFERENCES %s(id) ON DELETE SET NULL"; // Self-referencing FK

    /**
     * Creates a new dynamic table if it doesn't already exist in the
     * SQLite database associated with the current wsid.
     *
     * @param wsid      The workspace ID, determining the SQLite database.
     * @param tableName The name of the table to create.
     * @return true if the table was created or already existed, false on error.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW) // Ensure this runs in its own transaction
    public boolean createTableIfNotExists(String wsid, String tableName) {
        if (tableName == null || tableName.trim().isEmpty() || !tableName.matches("^[a-zA-Z0-9_]+$")) {
            log.error("Invalid table name provided: {}", tableName);
            throw new IllegalArgumentException("Table name must be alphanumeric with underscores.");
        }

        String originalContext = dataSourceContextHolder.getBranchContext();
        try {
            dataSourceContextHolder.setBranchContext(wsid); // Switch to the target wsid database
            log.info("Attempting to create table '{}' in wsid '{}'", tableName, wsid);

            // Format the self-referencing foreign key constraint with the actual table name
            String formattedSchema = String.format(COMMON_TABLE_SCHEMA_SQL_SQLITE, tableName);
            String createTableSql = String.format("CREATE TABLE IF NOT EXISTS %s (%s);", tableName, formattedSchema);

            jdbcTemplate.execute(createTableSql);

            // Create indexes for commonly queried fields
            // itemId already has an implicit index due to UNIQUE constraint, but explicitly creating one doesn't hurt
            // and makes intent clear. SQLite will typically use the existing one.
            createIndexIfNotExists(tableName, "idx_" + tableName + "_itemId", "itemId");
            createIndexIfNotExists(tableName, "idx_" + tableName + "_category", "category"); // <<< ADDED INDEX FOR CATEGORY
            createIndexIfNotExists(tableName, "idx_" + tableName + "_from_node_id", "from_node_id");
            createIndexIfNotExists(tableName, "idx_" + tableName + "_title", "title");


            log.info("Table '{}' ensured in wsid '{}'", tableName, wsid);
            return true;
        } catch (Exception e) {
            log.error("Error creating table '{}' in wsid '{}': {}", tableName, wsid, e.getMessage(), e);
            return false; // Or throw a custom exception
        } finally {
            dataSourceContextHolder.setBranchContext(originalContext); // Switch back
        }
    }

    private void createIndexIfNotExists(String tableName, String indexName, String columnName) {
         if (indexName == null || indexName.trim().isEmpty() || !indexName.matches("^[a-zA-Z0-9_]+$")) {
            log.warn("Invalid index name for table {}: {}", tableName, indexName);
            return;
        }
        if (columnName == null || columnName.trim().isEmpty() || !columnName.matches("^[a-zA-Z0-9_]+$")) {
            log.warn("Invalid column name for index {} on table {}: {}", indexName, tableName, columnName);
            return;
        }
        try {
            // For SQLite, you can specify multiple columns for a composite index if needed: (column1, column2)
            String createIndexSql = String.format("CREATE INDEX IF NOT EXISTS %s ON %s (%s);", indexName, tableName, columnName);
            jdbcTemplate.execute(createIndexSql);
            log.debug("Index '{}' on table '{}' for column '{}' ensured.", indexName, tableName, columnName);
        } catch (Exception e) {
            log.warn("Could not create index {} on table {} for column {}: {}", indexName, tableName, columnName, e.getMessage());
            // Don't let index creation failure stop table creation
        }
    }

    /**
     * Drops a dynamic table if it exists in the SQLite database
     * associated with the current wsid.
     *
     * @param wsid      The workspace ID, determining the SQLite database.
     * @param tableName The name of the table to drop.
     * @return true if the table was dropped or did not exist, false on error.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW) // Ensure this runs in its own transaction
    public boolean dropTable(String wsid, String tableName) {
        if (tableName == null || tableName.trim().isEmpty() || !tableName.matches("^[a-zA-Z0-9_]+$")) {
            log.error("Invalid table name provided for drop operation: {}", tableName);
            throw new IllegalArgumentException("Table name must be alphanumeric with underscores.");
        }

        String originalContext = dataSourceContextHolder.getBranchContext();
        try {
            dataSourceContextHolder.setBranchContext(wsid); // Switch to the target wsid database
            log.info("Attempting to drop table '{}' in wsid '{}'", tableName, wsid);

            String dropTableSql = String.format("DROP TABLE IF EXISTS %s;", tableName);
            jdbcTemplate.execute(dropTableSql);

            log.info("Table '{}' dropped (if it existed) in wsid '{}'", tableName, wsid);
            return true;
        } catch (Exception e) {
            log.error("Error dropping table '{}' in wsid '{}': {}", tableName, wsid, e.getMessage(), e);
            return false; // Or throw a custom exception
        } finally {
            dataSourceContextHolder.setBranchContext(originalContext); // Switch back
        }
    }
}