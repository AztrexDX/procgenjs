package com.aztrex.procgenjs.common.database;//package com.aztrex.procgenjs.common.database;
//
//import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//import org.springframework.stereotype.Component;
//
//@Component
//public class RoutingDataSource extends AbstractRoutingDataSource {
//
//    public static final ThreadLocal<String> CURRENT_WORKSPACE = new ThreadLocal<>();
//
//    @Override
//    protected Object determineCurrentLookupKey() {
//        return CURRENT_WORKSPACE.get();
//    }
//}