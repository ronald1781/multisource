package com.r2g.ms.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "rrhhEntityManagerFactorty", transactionManagerRef = "rrhhTransactionManager", basePackages = {"com.r2g.ms.repo.rrhh"})
public class PostgresConfig {

    @Autowired
    private Environment env;


    @Bean(name = "rrhhDataSource")
    public DataSource rrhhDatasource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("bdpg.datasource.url"));
        dataSource.setUsername(env.getProperty("bdpg.datasource.username"));
        dataSource.setPassword(env.getProperty("bdpg.datasource.password"));
        dataSource.setDriverClassName(env.getProperty("bdpg.datasource.driverClassName"));
        return dataSource;
    }


    @Bean(name = "rrhhEntityManagerFactorty")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(rrhhDatasource());
        em.setPackagesToScan("com.r2g.ms.model.rrhh");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.show-sql", env.getProperty("bdmq.jpa.show-sql"));
        properties.put("hibernate.dialect", env.getProperty("bdmq.jpa.database-platform"));

        em.setJpaPropertyMap(properties);
        return em;
    }


    @Bean(name = "rrhhTransactionManager")
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return transactionManager;
    }

}
