package com.aztrex.procgenjs.common.database;

import com.aztrex.procgenjs.common.utility.constant.PathConstant;
import com.aztrex.procgenjs.common.utility.constant.ProcgenjsConstant;
import com.aztrex.procgenjs.common.utility.database.DBUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class RoutingDataSource extends AbstractRoutingDataSource {

    @Autowired
    private DataSourceContextHolder dataSourceContextHolder;

    private final Map<Object, Object> currentDataSources = new ConcurrentHashMap<>();
//    @Autowired
//    @Qualifier("trackerDataSource")
//    private DataSource trackerDataSource;

//    @Autowired
//    private WorkspaceDataSourceRegistry workspaceDataSourceRegistry;

//    public RoutingDataSource() {
//        Map<Object, Object> dataSourceMap = new HashMap<>();
//        dataSourceMap.put(ProcgenjsConstant.TRACKER,
//                DBUtil.createDataSource(PathConstant.PROCGENJS_DATABASE_PATH));
//        setTargetDataSources(dataSourceMap);
//        setDefaultTargetDataSource(dataSourceMap.get(ProcgenjsConstant.TRACKER));
//        dataSourceContextHolder.setBranchContext(ProcgenjsConstant.TRACKER);
//        super.afterPropertiesSet();
//    }

    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceContextHolder.getBranchContext();
    }

    @PostConstruct
    public void initializePostConstruct() {
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(ProcgenjsConstant.PROCGENJS,
                DBUtil.createDataSource(PathConstant.PROCGENJS_DATABASE_PATH));
        setTargetDataSources(dataSourceMap);
        setDefaultTargetDataSource(dataSourceMap.get(ProcgenjsConstant.PROCGENJS));
        dataSourceContextHolder.setBranchContext(ProcgenjsConstant.PROCGENJS);
        super.afterPropertiesSet();
    }

    /**
     * Call this method dynamically to add a new Workspace DB
     */
    public void addDataSource(String key, DataSource dataSource) {
        log.info("Dynamically adding DataSource for key: {}", key);

        // 1. Add to our local map
        this.currentDataSources.put(key, dataSource);

        // 2. Pass the updated map to Spring
        this.setTargetDataSources(this.currentDataSources);

        // 3. Trigger a refresh of the internal resolvedDataSources
        // This parses the map and makes the new DataSource active immediately
        super.afterPropertiesSet();
    }
//    @PostConstruct
//    public void initialize() {
//        Map<Object, Object> dataSourceMap = new HashMap<>();
//        dataSourceMap.put(ProcgenjsConstant.TRACKER, trackerDataSource); // Correctly add trackerDataSource
//        dataSourceMap.putAll(workspaceDataSourceRegistry.getDataSources());
//        this.setTargetDataSources(dataSourceMap);
//        this.setDefaultTargetDataSource(trackerDataSource);
//        super.afterPropertiesSet(); // Important to call after setting targetDataSources
//    }
}