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
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * BpmnUelConditionTests
 *
 * @author Aaric
 * @version 0.6.0-SNAPSHOT
 */
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BpmnUelConditionTests {

    @Autowired
    private StandaloneProcessEngineConfiguration standaloneProcessEngineConfiguration;

    private String bizKey = "custom007";

    @Disabled
    @Test
    public void testFlowDeployZip() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();

//        repositoryService.deleteDeployment("5001", true);

        ClassPathResource classPathResource = new ClassPathResource("processes/Process04.zip");
        ZipInputStream zipInputStream = new ZipInputStream(classPathResource.getInputStream());
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .name("测试流程04")
                .deploy();
        log.info("id = {}, name = {}", deployment.getId(), deployment.getName());
    }

    @Disabled
    @Test
    public void testFlowStartWithCondition() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        Form form = new Form()
                .setBizKey(bizKey)
                .setNum(3D)
                .setRemark("出差3天");
        Map<String, Object> variables = new HashMap<>() {{
            put("aa", "zhangsan");
            put("bb", "lisi");
            put("cc", "wangwu");
            put("form", form);
        }};
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("Process04", bizKey, variables);
        log.info("pdId={}, id={}", instance.getProcessDefinitionId(), instance.getId());
    }

    @Disabled
    @Test
    public void testTaskComplete() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("Process04")
                .taskAssignee("wangwu")
                .singleResult();
        if (null != task) {
            Form form = new Form()
                    .setBizKey(bizKey)
                    .setNum(2D)
                    .setRemark("只允许出差2天");

            taskService.complete(task.getId(), new HashMap<>() {{
                put("form", form);
            }});
        }
    }

    @Disabled
    @Test
    public void testTaskCompleteWithGlobalVariable() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("Process04")
                .taskAssignee("lisi")
                .singleResult();

        if (null != task) {
            Form form = new Form()
                    .setBizKey(bizKey)
                    .setNum(2D)
                    .setRemark("只允许出差2天");
            taskService.setVariable(task.getId(), "form", form);

            taskService.complete(task.getId());

        }
    }

    @Disabled
    @Test
    public void testTaskCompleteWithLocalVariable() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("Process04")
                .taskAssignee("lisi")
                .singleResult();

        if (null != task) {
            Form form = new Form()
                    .setBizKey(bizKey)
                    .setNum(2D)
                    .setRemark("只允许出差2天");
            taskService.setVariableLocal(task.getId(), "form", form);

            taskService.complete(task.getId());

        }
    }
}
