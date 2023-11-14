package com.at8.flow;

import com.at8.flow.pojo.Form;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;

/**
 * BpmnGoBackTests
 *
 * @author Aaric
 * @version 0.8.0-SNAPSHOT
 */
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BpmnGoBackTests {

    @Autowired
    private StandaloneProcessEngineConfiguration standaloneProcessEngineConfiguration;

    //private String bizKey = "custom012";
    private String bizKey = "custom013";

    @Disabled
    @Test
    public void testFlowDeployZip() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();

//        repositoryService.deleteDeployment("5001", true);

        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/Process10.bpmn20.xml")
                .addClasspathResource("processes/Process10.png")
                .name("测试流程10")
                .deploy();
        log.info("id = {}, name = {}", deployment.getId(), deployment.getName());
    }

    @Disabled
    @Test
    public void testFlowStart() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        Form form = new Form()
                .setBizKey(bizKey)
                .setState(0)
                .setRemark("审核不通过");
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("Process10", bizKey, new HashMap<>() {{
            put("form", form);
        }});
        log.info("pdId={}, id={}", instance.getProcessDefinitionId(), instance.getId());
    }

    @Disabled
    @Test
    public void testTaskCompleteWithStateFailure() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("Process10")
                .taskAssignee("bb")
                .singleResult();

        if (null != task) {
            taskService.complete(task.getId());
        }
    }

//    @Disabled
    @Test
    public void testTaskCompleteWithStateOk() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("Process10")
                .taskAssignee("cc")
                .singleResult();

        if (null != task) {
            Form form = new Form()
                    .setBizKey(bizKey)
                    .setState(1)
                    .setRemark("审核通过");
            taskService.complete(task.getId(), new HashMap<>() {{
                put("form", form);
            }});
        }
    }
}
