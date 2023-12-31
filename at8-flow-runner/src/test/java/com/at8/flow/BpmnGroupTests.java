package com.at8.flow;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.zip.ZipInputStream;

/**
 * BpmnGroupTests
 *
 * @author Aaric
 * @version 0.6.0-SNAPSHOT
 */
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BpmnGroupTests {

    @Autowired
    private StandaloneProcessEngineConfiguration standaloneProcessEngineConfiguration;

    private String bizKey = "custom009";

    @Disabled
    @Test
    public void testFlowDeployZip() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();

//        repositoryService.deleteDeployment("5001", true);

        ClassPathResource classPathResource = new ClassPathResource("processes/Process05.zip");
        ZipInputStream zipInputStream = new ZipInputStream(classPathResource.getInputStream());
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .name("测试流程05")
                .deploy();
        log.info("id = {}, name = {}", deployment.getId(), deployment.getName());
    }

    @Disabled
    @Test
    public void testFlowStart() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        ProcessInstance instance = runtimeService.startProcessInstanceByKey("Process05", bizKey);
        log.info("pdId={}, id={}", instance.getProcessDefinitionId(), instance.getId());
    }

    @Disabled
    @Test
    public void testTaskClaim() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();

        String userId = "cc";
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("Process05")
                .taskCandidateUser(userId)
                .singleResult();
        if (null != task) {
            taskService.claim(task.getId(), userId);
            log.info("id={}, claim={}", task.getId(), userId);
        }

        /*task = taskService.createTaskQuery()
                .processDefinitionKey("Process05")
                .taskAssignee(userId)
                .singleResult();
        if (null != task) {
            taskService.setAssignee(task.getId(), null);
        }*/
    }

    @Disabled
    @Test
    public void testTaskComplete() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("Process05")
                .taskAssignee("cc")
                .singleResult();

        if (null != task) {
            taskService.complete(task.getId());
        }
    }
}
