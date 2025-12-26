//package com.aztrex.procgenjs.common.configuration;
//
//import com.aztrex.procgenjs.modules.serviceModules.workspace.service.interceptor.WorkspaceContextInterceptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Autowired
//    private WorkspaceContextInterceptor workspaceContextInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(workspaceContextInterceptor);
//    }
//}