package com.aztrex.procgenjs.common.utility.database;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class DBUtil {
    public static String JDBC_SQLITE = "jdbc:sqlite:";
    public static String ORG_SQLITE_JDBC = "org.sqlite.JDBC";
    public static String CREATED = "created";

    public static HikariDataSource createDataSource(String jdbc, String dbPath, String driverClassName) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(jdbc + dbPath);
        hikariDataSource.setDriverClassName(driverClassName);
        hikariDataSource.setMaximumPoolSize(1);
//            hikariDataSource.setConnectionTimeout(30000);
//            hikariDataSource.setIdleTimeout(600000);
//            hikariDataSource.setMaxLifetime(1800000);
//            dataSourceMap.put(dbPath, hikariDataSource);
        return hikariDataSource;
    }

    public static HikariDataSource createDataSource(String dbPath) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(JDBC_SQLITE + dbPath);
        hikariDataSource.setDriverClassName(ORG_SQLITE_JDBC);
        hikariDataSource.setMaximumPoolSize(1);
        return hikariDataSource;
    }

    public static HikariDataSource createDataSource(String dbUrl, String driverClassName) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(dbUrl);
        hikariDataSource.setDriverClassName(driverClassName);
        hikariDataSource.setMaximumPoolSize(1);
        return hikariDataSource;
    }

    public static JdbcTemplate createSqliteJdbcTemplate(String dbPath) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DBUtil.ORG_SQLITE_JDBC);
        dataSource.setUrl(DBUtil.JDBC_SQLITE + dbPath);
        return new JdbcTemplate(dataSource);
    }

    public static HikariDataSource createSqliteDataSource(String dbPath) {
        return createDataSource(JDBC_SQLITE, dbPath, ORG_SQLITE_JDBC);
    }

    public static Connection createDatabase(String dbPath) {
        Connection conn = null;
        try {
            // Ensure the parent directory exists
            File dbFile = new File(dbPath);
            if (dbFile.exists()) {
                log.info("Database already exists at: " + dbPath);
                return null; // Don't recreate if it already exists
            }

            dbFile.getParentFile().mkdirs(); // Creates parent folders if they don't exist

            // SQLite connection string
            String url = JDBC_SQLITE + dbPath;

            // Connect to the database
            conn = DriverManager.getConnection(url);

            if (conn != null) {
                log.info(dbPath + CREATED);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return conn;
    }

    public static Integer tfTo10(boolean value) {
        return value ? 1 : 0;
    }

    public static boolean tfFrom10(int value) {
        return value > 0;
    }
}