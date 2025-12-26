package com.aztrex.procgenjs.common.configuration;//package com.aztrex.procgenjs.common.configuration;
//
//import com.aztrex.procgenjs.common.database.RoutingDataSource;
//import com.aztrex.procgenjs.common.util.database.DBUtil;
//import com.aztrex.procgenjs.modules.serviceModules.workspace.database.WorkspaceDataSourceRegistry;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//
//@Configuration
//@EnableJpaRepositories(basePackages = "com.aztrex.procgenjs.modules.*.database.repository",
//        entityManagerFactoryRef = "entityManagerFactory",
//        transactionManagerRef = "transactionManager")
//public class DataSourceConfig {
//
//    @Autowired
//    private WorkspaceDataSourceRegistry workspaceDataSourceRegistry;
//
//    @Value("${spring.datasource.url}")
//    private String trackerDatabaseUrl;
//
//    @Value("${spring.datasource.driver-class-name}")
//    private String trackerDatabaseDriverClassName;
//
//    @Value("${spring.jpa.database-platform}")
//    private String databasePlatform;
//
//    @Value("${spring.jpa.hibernate.ddl-auto}")
//    private String hibernateDdlAuto;
//
//    @Bean
//    public DataSource trackerDataSource() {
//        return DBUtil.createDataSource(trackerDatabaseUrl, trackerDatabaseDriverClassName);
//    }
//
//    @Bean
//    @Primary
//    public RoutingDataSource routingDataSource() {
//        RoutingDataSource routingDataSource = new RoutingDataSource();
//        Map<Object, Object> targetDataSources = new HashMap<>();
//        targetDataSources.put("tracker", trackerDataSource());
//        // Add workspace data sources here AFTER they are created by WorkspaceDataSourceRegistry
//        targetDataSources.putAll(workspaceDataSourceRegistry.getDataSources());
//        routingDataSource.setTargetDataSources(targetDataSources);
//        routingDataSource.afterPropertiesSet();
//        return routingDataSource;
//    }
//
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(routingDataSource());
//        em.setPackagesToScan("com.aztrex.procgenjs.modules.*.model");
//
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        em.setJpaVendorAdapter(vendorAdapter);
//        em.setJpaProperties(additionalProperties());
//
//        return em;
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager() {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
//        return transactionManager;
//    }
//
//    Properties additionalProperties() {
//        Properties properties = new Properties();
//        properties.setProperty("hibernate.dialect", databasePlatform);
//        properties.setProperty("hibernate.hbm2ddl.auto", hibernateDdlAuto);
//        return properties;
//    }
//}