//package com.aztrex.procgenjs.common.configuration;//package com.aztrex.procgenjs.configuration;
//
//import com.aztrex.procgenjs.common.service.ConfigService;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//
//@Configuration
//@PropertySource("classpath:config.properties")
//public class AppConfig {
//
//    @Value("${database.file}")
//    private String databaseFile;
//
//    @Bean
//    public ConfigService configService() {
//        return new ConfigService(databaseFile);
//    }
//}