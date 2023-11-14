package com.at8.flow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

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
     * 禁用SecuritySecurity，提供UserDetailsService定义，此处仅为解决启动报错
     */
    @Bean
    UserDetailsService inMemoryUserDetailsService() {
        /*return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return null;
            }
        };*/
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        userDetailsManager.createUser(new User("aa", passwordEncoder().encode("test123"),
                Arrays.asList("ROLE_ACTIVITI_USER", "GROUP_team01").stream().map(SimpleGrantedAuthority::new).toList()));
        userDetailsManager.createUser(new User("bb", passwordEncoder().encode("test123"),
                Arrays.asList("ROLE_ACTIVITI_USER", "GROUP_team01").stream().map(SimpleGrantedAuthority::new).toList()));
        userDetailsManager.createUser(new User("cc", passwordEncoder().encode("test123"),
                Arrays.asList("ROLE_ACTIVITI_USER", "GROUP_team02").stream().map(SimpleGrantedAuthority::new).toList()));
        userDetailsManager.createUser(new User("dd", passwordEncoder().encode("test123"),
                Arrays.asList("ROLE_ACTIVITI_USER").stream().map(SimpleGrantedAuthority::new).toList()));
        userDetailsManager.createUser(new User("admin", passwordEncoder().encode("test123"),
                Arrays.asList("ROLE_ACTIVITI_ADMIN").stream().map(SimpleGrantedAuthority::new).toList()));
        return userDetailsManager;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
