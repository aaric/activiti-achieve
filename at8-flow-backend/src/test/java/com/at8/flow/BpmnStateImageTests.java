package com.at8.flow;

import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * BpmnStateImageTests
 *
 * @author Aaric
 * @version 0.10.0-SNAPSHOT
 */
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BpmnStateImageTests {

    public static final String DESKTOP_PATH = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    @Disabled
    @Test
    public void testTaskStateImage() throws Exception {
        String taskId = "034abcbb-82cd-11ee-b457-84fdd1ba564e";
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        if (null != task) {
            HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());

            ProcessDiagramGenerator processDiagramGenerator = new DefaultProcessDiagramGenerator();
            InputStream inputStream = processDiagramGenerator.generateDiagram(bpmnModel, "宋体", "宋体", "宋体");

            IOUtils.copy(inputStream, new FileOutputStream(new File(DESKTOP_PATH, taskId + ".svg")));
        }
    }
}
