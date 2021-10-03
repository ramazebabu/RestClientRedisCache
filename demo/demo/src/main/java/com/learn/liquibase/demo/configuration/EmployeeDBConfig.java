package com.learn.liquibase.demo.configuration;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory",basePackages = {"com.learn.liquibase.demo.repository.employee"},transactionManagerRef ="transactionManager")
public class EmployeeDBConfig {

    @Primary
    @Bean(name = "datasource")
    @ConfigurationProperties(prefix = "spring.employee.datasource")
    public DataSource dataSource(){
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder, @Qualifier("datasource") DataSource dataSource){
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
        return builder.dataSource(dataSource).properties(properties).packages("com.learn.liquibase.demo.model.employee").persistenceUnit("Employee").build();
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory){
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.employee.datasource.liquibase")
    public LiquibaseProperties employeeLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean("liquibase")
    public SpringLiquibase employeeLiquibase() {
        return springLiquibase(dataSource(), employeeLiquibaseProperties());
    }


    private static SpringLiquibase springLiquibase(DataSource dataSource, LiquibaseProperties properties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(properties.getChangeLog());
        liquibase.setContexts(properties.getContexts());
        liquibase.setDefaultSchema(properties.getDefaultSchema());
        liquibase.setDropFirst(properties.isDropFirst());
        liquibase.setShouldRun(properties.isEnabled());
        liquibase.setLabels(properties.getLabels());
        liquibase.setChangeLogParameters(properties.getParameters());
        liquibase.setRollbackFile(properties.getRollbackFile());
        return liquibase;
    }

}
