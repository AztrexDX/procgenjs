package com.aztrex.procgenjs.common.database;

import com.aztrex.procgenjs.common.util.database.DBUtil;
import com.aztrex.procgenjs.common.util.constant.PathConstant;
import com.aztrex.procgenjs.common.util.constant.TrackerConstant;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class RoutingDataSource extends AbstractRoutingDataSource {

    @Autowired
    private DataSourceContextHolder dataSourceContextHolder;

//    @Autowired
//    @Qualifier("trackerDataSource")
//    private DataSource trackerDataSource;

//    @Autowired
//    private WorkspaceDataSourceRegistry workspaceDataSourceRegistry;

//    public RoutingDataSource() {
//        Map<Object, Object> dataSourceMap = new HashMap<>();
//        dataSourceMap.put(TrackerConstant.TRACKER,
//                DBUtil.createDataSource(PathConstant.TRACKER_DATABASE_PATH));
//        setTargetDataSources(dataSourceMap);
//        setDefaultTargetDataSource(dataSourceMap.get(TrackerConstant.TRACKER));
//        dataSourceContextHolder.setBranchContext(TrackerConstant.TRACKER);
//        super.afterPropertiesSet();
//    }

    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceContextHolder.getBranchContext();
    }

    @PostConstruct
    public void initializePostConstruct() {
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(TrackerConstant.TRACKER,
                DBUtil.createDataSource(PathConstant.TRACKER_DATABASE_PATH));
        setTargetDataSources(dataSourceMap);
        setDefaultTargetDataSource(dataSourceMap.get(TrackerConstant.TRACKER));
        dataSourceContextHolder.setBranchContext(TrackerConstant.TRACKER);
        super.afterPropertiesSet();
    }

//    @PostConstruct
//    public void initialize() {
//        Map<Object, Object> dataSourceMap = new HashMap<>();
//        dataSourceMap.put(TrackerConstant.TRACKER, trackerDataSource); // Correctly add trackerDataSource
//        dataSourceMap.putAll(workspaceDataSourceRegistry.getDataSources());
//        this.setTargetDataSources(dataSourceMap);
//        this.setDefaultTargetDataSource(trackerDataSource);
//        super.afterPropertiesSet(); // Important to call after setting targetDataSources
//    }
}