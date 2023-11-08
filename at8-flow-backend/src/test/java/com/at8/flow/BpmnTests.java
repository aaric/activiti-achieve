package com.at8.flow;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

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

    public static final String DESKTOP_PATH = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();

    @Autowired
    private StandaloneProcessEngineConfiguration standaloneProcessEngineConfiguration;

    private String bizKey = "custom001";

    @Disabled
    @Test
    public void testInitDb() throws Exception {
        standaloneProcessEngineConfiguration.buildProcessEngine();
    }

    @Disabled
    @Test
    public void testFlowDeploy() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();

        repositoryService.deleteDeployment("1");

        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/Process01.bpmn20.xml")
                .addClasspathResource("processes/Process01.png")
                .name("测试流程01")
                .deploy();
        log.info("id = {}, name = {}", deployment.getId(), deployment.getName());
    }

    @Disabled
    @Test
    public void testFlowDeployZip() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();

        repositoryService.deleteDeployment("2501", true);

        ClassPathResource classPathResource = new ClassPathResource("processes/Process01.zip");
        ZipInputStream zipInputStream = new ZipInputStream(classPathResource.getInputStream());
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .name("测试流程01Zip")
                .deploy();
        log.info("id = {}, name = {}", deployment.getId(), deployment.getName());
    }

    @Disabled
    @Test
    public void testFlowStart() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        ProcessInstance instance = runtimeService.startProcessInstanceByKey("Test01");
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("Process01", bizKey);
        // pdId=Test01:1:2504, id=5001
        log.info("pdId={}, id={}", instance.getProcessDefinitionId(), instance.getId());
    }

    @Disabled
    @Test
    public void testTaskQuery() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("Process01")
                .taskAssignee("aa")
                .list();
        for (Task task : taskList) {
            log.info("pdId={}, id={}, name={}", task.getProcessDefinitionId(), task.getId(), task.getName(), task.getName());
        }
    }

    @Disabled
    @Test
    public void testTaskComplete() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("Process01")
                .taskAssignee("cc")
                .singleResult();
        taskService.complete(task.getId());
    }

    @Disabled
    @Test
    public void testFlowInfo() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("Process01")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            log.info("id={}, name={}, key={}, version={}, dId={}", processDefinition.getId(), processDefinition.getKey(),
                    processDefinition.getName(), processDefinition.getVersion(), processDefinition.getDeploymentId());
        }
    }

    @Disabled
    @Test
    public void testFlowDownload() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("Process01")
                .singleResult();
        try (InputStream xmlInputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName());
             FileOutputStream xmlOutputStream = new FileOutputStream(new File(DESKTOP_PATH, processDefinition.getResourceName()))) {
            IOUtils.copy(xmlInputStream, xmlOutputStream);
        }

        try (InputStream pngInputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName());
             FileOutputStream pngOutputStream = new FileOutputStream(new File(DESKTOP_PATH, processDefinition.getDiagramResourceName()))) {
            IOUtils.copy(pngInputStream, pngOutputStream);
        }
    }

    @Disabled
    @Test
    public void testActivityHistory() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricActivityInstance> activityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processDefinitionId("Process01:1:5004")
//                .processInstanceId("5001")
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();
        for (HistoricActivityInstance activityInstance : activityInstanceList) {
            log.info("id={}, name={}, pdId={}, piId={}", activityInstance.getActivityId(), activityInstance.getActivityName(),
                    activityInstance.getProcessDefinitionId(), activityInstance.getProcessInstanceId());
        }
    }

    @Disabled
    @Test
    public void testProcessQuery() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(bizKey)
                .list();
        for (ProcessInstance instance : processInstanceList) {
            log.info("iId={}, piId={}, isEnded={}, isSuspended={}", instance.getId(), instance.getProcessInstanceId(),
                    instance.isEnded(), instance.isSuspended());
        }
    }

    @Disabled
    @Test
    public void testProcessSuspend() throws Exception {
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("Process01")
                .singleResult();

        if (processDefinition.isSuspended()) {
            repositoryService.activateProcessDefinitionById(processDefinition.getId(), true, null);
            log.info("pdId={}, isSuspended={}, activate=true", processDefinition.getId(), processDefinition.isSuspended());
        } else {
            repositoryService.suspendProcessDefinitionById(processDefinition.getId(), true, null);
            log.info("pdId={}, isSuspended={}, activate=false", processDefinition.getId(), processDefinition.isSuspended());
        }
    }
}
