package com.aztrex.procgenjs.modules.serviceModules.workspace.service.interceptor;

import com.aztrex.procgenjs.common.database.DataSourceContextHolder;
import com.aztrex.procgenjs.common.util.constant.PathConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;
import java.util.Objects;

@Component
public class WorkspaceContextInterceptor implements HandlerInterceptor {

    @Autowired
    private DataSourceContextHolder dataSourceContextHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // Try to get workspaceId from request param
        String workspaceId = request.getParameter(PathConstant.WSID);

        if (Objects.nonNull(workspaceId)) {
            dataSourceContextHolder.setBranchContext(workspaceId);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        dataSourceContextHolder.clearBranchContext();
    }
}