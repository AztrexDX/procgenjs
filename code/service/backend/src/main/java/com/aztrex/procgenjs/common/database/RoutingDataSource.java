package com.aztrex.procgenjs.common.database;

import com.aztrex.procgenjs.common.utility.constant.PathConstant;
import com.aztrex.procgenjs.common.utility.constant.ProcgenjsConstant;
import com.aztrex.procgenjs.common.utility.database.DBUtil;
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