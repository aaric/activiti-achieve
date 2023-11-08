package com.at8.flow.config;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Activiti BPMN Config
 *
 * @author Aaric
 * @version 0.2.0-SNAPSHOT
 */
@Slf4j
@Configuration
public class BpmnConfig {

    @Autowired
    DataSource dataSource;

    @Bean
    StandaloneProcessEngineConfiguration standaloneProcessEngineConfiguration() {
        StandaloneProcessEngineConfiguration config = new StandaloneProcessEngineConfiguration();
        config.setDataSource(dataSource);
        config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        return config;
    }
}
