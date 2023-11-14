package com.at8.flow;

import com.at8.flow.pojo.Form;
import com.at8.flow.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.model.payloads.StartProcessPayload;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;

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

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskService taskService;

    private String bizKey = "demo01";

    @Autowired
    private SecurityUtil securityUtil;

    @Disabled
    @Test
    public void testFlowDeploy() throws Exception {
//        repositoryService.deleteDeployment("5001", true);

        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/Demo01.bpmn20.xml")
                .addClasspathResource("processes/Demo01.bpmn20.png")
                .name("示例流程01")
                .deploy();
        log.info("id = {}, name = {}", deployment.getId(), deployment.getName());
    }

    @Disabled
    @Test
    public void testProcessList() throws Exception {
        securityUtil.loginAs("admin");

        Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 100));
        for (ProcessDefinition processDefinition : processDefinitionPage.getContent()) {
            log.info("id={}, name={}", processDefinition.getId(), processDefinition.getName());
        }
    }

    @Disabled
    @Test
    public void testFlowStart() throws Exception {
        Form form = new Form()
                .setBizKey(bizKey)
                .setState(0)
                .setRemark("审核不通过");
//        ProcessInstance instance = runtimeService.startProcessInstanceByKey("Demo01", bizKey, new HashMap<>() {{
//            put("form", form);
//        }});
        securityUtil.loginAs("aa");

        StartProcessPayload payload = ProcessPayloadBuilder.start()
                .withProcessDefinitionId("Demo01")
                .withVariables(new HashMap<>() {{
                    put("form", form);
                }}).build();
        ProcessInstance instance = processRuntime.start(payload);
        log.info("pdId={}, id={}", instance.getProcessDefinitionId(), instance.getId());
    }

    @Disabled
    @Test
    public void testTaskCompleteWithStateFailure() throws Exception {
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("Demo01")
                .taskAssignee("bb")
                .singleResult();

        if (null != task) {
            taskService.complete(task.getId());
        }
    }

    @Disabled
    @Test
    public void testTaskCompleteWithStateOk() throws Exception {
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("Demo01")
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
