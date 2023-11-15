package com.at8.flow;

import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * BpmnProcessDiagramTests
 *
 * @author Aaric
 * @version 0.10.0-SNAPSHOT
 */
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BpmnProcessDiagramTests {

    public static final String DESKTOP_PATH = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Disabled
    @Test
    public void testProcessDiagram() throws Exception {
        String taskId = "dcf24df3-8355-11ee-99ed-84fdd1ba564e";
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        if (null != task) {
            HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());

            String fontName = "Simsun";  // 宋体
            ProcessDiagramGenerator processDiagramGenerator = new DefaultProcessDiagramGenerator();
            //InputStream inputStream = processDiagramGenerator.generateDiagram(bpmnModel, fontName, fontName, fontName);
            List<String> highLightedActivityIdList = runtimeService.getActiveActivityIds(task.getExecutionId());
            List<String> highLightedFlowIdList = new ArrayList<>();
            InputStream inputStream = processDiagramGenerator.generateDiagram(bpmnModel,
                    highLightedActivityIdList, highLightedFlowIdList,
                    fontName, fontName, fontName);

            IOUtils.copy(inputStream, new FileOutputStream(new File(DESKTOP_PATH, "ProcessDiagram.svg")));
        }
    }
}
