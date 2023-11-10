package com.at8.flow;

import com.at8.flow.pojo.Form;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

/**
 * BpmnUelVariableTests
 *
 * @author Aaric
 * @version 0.6.0-SNAPSHOT
 */
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BpmnUelVariableTests {

    @Autowired
    private StandaloneProcessEngineConfiguration standaloneProcessEngineConfiguration;

    private String bizKey = "custom008";

    @Disabled
    @Test
    public void testSetGlobalVariableWithRuntime() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        String executionId = "test";
        Form form = new Form()
                .setBizKey(bizKey)
                .setNum(5D)
                .setRemark("出差5天");

        // runtimeService.setVariable(executionId, "form", form);
        Map<String, Object> variables = new HashMap<>() {{
            put("form", form);
        }};

        runtimeService.setVariables(executionId, variables);
    }

    @Disabled
    @Test
    public void testSetGlobalVariableWithTask() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();

        String taskId = "test";
        Form form = new Form()
                .setBizKey(bizKey)
                .setNum(5D)
                .setRemark("出差5天");

        // taskService.setVariable(taskId, "form", form);
        taskService.setVariables(taskId, new HashMap<>() {{
            put("form", form);
        }});
    }
}
