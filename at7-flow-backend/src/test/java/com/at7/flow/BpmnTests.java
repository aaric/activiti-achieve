package com.at7.flow;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * BpmnTests
 *
 * @author Aaric
 * @version 0.2.0-SNAPSHOT
 */
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BpmnTests {

    @Autowired
    private StandaloneProcessEngineConfiguration standaloneProcessEngineConfiguration;

    @Disabled
    @Test
    public void testInitDb() throws Exception {
        standaloneProcessEngineConfiguration.buildProcessEngine();
    }

    @Disabled
    @Test
    public void testGetServices() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        TaskService taskService = processEngine.getTaskService();
        HistoryService historyService = processEngine.getHistoryService();
        ManagementService managementService = processEngine.getManagementService();
    }

    @Test
    public void testDeploy() throws Exception {
        // ACT_GE_PROPERTY    更新版本
        // ACT_RE_DEPLOYMENT  流程信息
        // ACT_RE_PROCDEF     流程定义
        // ACT_GE_BYTEARRAY   流程资源
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();

        repositoryService.deleteDeployment("1");

        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/Test01.bpmn20.xml")
                .addClasspathResource("processes/Test01.png")
                .name("Test01")
                .deploy();
        log.info("id = {}, name = {}", deployment.getId(), deployment.getName());
    }
}
