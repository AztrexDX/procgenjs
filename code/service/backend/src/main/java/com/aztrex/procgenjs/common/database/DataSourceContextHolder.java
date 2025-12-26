package com.aztrex.procgenjs.common.database;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DataSourceContextHolder {
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public void setBranchContext(String dbId) {
        threadLocal.set(dbId);
    }

    public String getBranchContext() {
        return threadLocal.get();
    }

    public void clearBranchContext() {
        threadLocal.remove();
    }
}