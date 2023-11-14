package com.at8.flow;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * BpmnSpringTests
 *
 * @author Aaric
 * @version 0.10.0-SNAPSHOT
 */
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BpmnSpringTests {

    @Autowired
    private RepositoryService repositoryService;

    @Test
    public void testInitDb() throws Exception {
        System.err.println(repositoryService);
    }
}
