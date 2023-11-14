package com.at8.flow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Activiti BPMN Config
 *
 * @author Aaric
 * @version 0.10.0-SNAPSHOT
 */
@Configuration
public class BpmnConfig {

//    @Autowired
//    DataSource dataSource;

//    @Bean
//    SpringProcessEngineConfiguration processEngineConfiguration() {
//        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
//        config.setDataSource(dataSource);
//        config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
//        return config;
//    }

//    @Bean
//    ProcessEngine processEngine() {
//        return processEngineConfiguration().buildProcessEngine();
//    }

    /**
     * 禁用SecuritySecurity，提供UserDetailsService定义，解决启动报错
     */
    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return null;
            }
        };
    }
}
