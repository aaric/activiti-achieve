package com.at8.flow;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
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
 * com.at8.flow
 *
 * @author Aaric
 * @version 0.5.0-SNAPSHOT
 */
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BpmnUelTests {

    @Autowired
    private StandaloneProcessEngineConfiguration standaloneProcessEngineConfiguration;

    private String bizKey = "custom005";

    @Disabled
    @Test
    public void testFlowDeployZip() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();

//        repositoryService.deleteDeployment("5001", true);

        ClassPathResource classPathResource = new ClassPathResource("processes/Process02.zip");
        ZipInputStream zipInputStream = new ZipInputStream(classPathResource.getInputStream());
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .name("测试流程02")
                .deploy();
        log.info("id = {}, name = {}", deployment.getId(), deployment.getName());
    }

    @Disabled
    @Test
    public void testFlowStartWithUel() throws Exception {
        // ACT_RU_VARIABLE
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> variables = new HashMap<>() {{
            put("aa", "zhangsan");
            put("bb", "lisi");
            put("cc", "wangwu");
        }};
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("Process02", bizKey, variables);
        log.info("pdId={}, id={}", instance.getProcessDefinitionId(), instance.getId());
    }

    @Disabled
    @Test
    public void testFlowStartWithTaskListener() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("Process03", bizKey);
        log.info("pdId={}, id={}", instance.getProcessDefinitionId(), instance.getId());
    }
}
