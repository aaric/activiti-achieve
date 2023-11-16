package com.at8.flow.api.bpmn.impl;

import com.at8.flow.api.bpmn.FlowApi;
import com.at8.flow.core.data.ApiData;
import com.at8.flow.pojo.BpmnData;
import com.at8.flow.pojo.Form;
import com.at8.flow.util.FilePathUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.model.payloads.StartProcessPayload;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 工作流模块API接口控制器
 *
 * @author Aaric
 * @version 0.12.0-SNAPSHOT
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/bpmn/flow")
public class FlowController implements FlowApi {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Override
    @GetMapping(value = "/process/page/{pageSize}/{pageNum}")
    public ApiData<Page<BpmnData>> processPage(@PathVariable Integer pageSize, @PathVariable Integer pageNum) {
        List<BpmnData> content = new ArrayList<>();
        Pageable pageable = PageRequest.of((pageNum - 1), pageSize);
        long pdTotal = repositoryService.createProcessDefinitionQuery()
                .count();
        List<ProcessDefinition> pdList = repositoryService
                .createProcessDefinitionQuery()
                .listPage((int) pageable.getOffset(), pageable.getPageSize());
        if (null != pdList && !pdList.isEmpty()) {
            for (ProcessDefinition processDefinition : pdList) {
                content.add(new BpmnData(processDefinition.getId(), processDefinition.getKey(), processDefinition.getName()));
            }
        }
        return ApiData.of(new PageImpl<>(content, pageable, pdTotal));
    }

    @Override
    @PostMapping(value = "/process/deploy")
    public ApiData<BpmnData> processDeploy(@RequestParam String bpmnName, @RequestPart MultipartFile bpmnFile, @RequestPart(required = false) MultipartFile imageFile) throws Exception {
        File tmpBpmnFile = null, tmpImageFile = null;
        if (null != bpmnFile && null != bpmnFile.getOriginalFilename()) {
            tmpBpmnFile = new File(FilePathUtil.getAppDateDir(), bpmnFile.getOriginalFilename());
            bpmnFile.transferTo(tmpBpmnFile);
        }
        if (null != imageFile && null != imageFile.getOriginalFilename()) {
            tmpImageFile = new File(FilePathUtil.getAppDateDir(), imageFile.getOriginalFilename());
            imageFile.transferTo(tmpImageFile);
        }
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        if (null != tmpBpmnFile) {
            deploymentBuilder.addInputStream(tmpBpmnFile.getName(), new FileInputStream(tmpBpmnFile));
        }
        if (null != tmpImageFile) {
            deploymentBuilder.addInputStream(tmpImageFile.getName(), new FileInputStream(tmpImageFile));
        }
        Deployment deployment = deploymentBuilder.name(bpmnName)
                .deploy();
        return ApiData.of(new BpmnData(deployment.getId(), deployment.getKey(), deployment.getName()));
    }

    @Override
    @PostMapping(value = "/process/start")
    public ApiData<BpmnData> processStart(@RequestParam String processKey, @RequestParam String bizKey) {
        Form form = new Form()
                .setBizKey(bizKey)
                .setState(1)
                .setRemark("默认审核通过");

        StartProcessPayload payload = ProcessPayloadBuilder.start()
                .withProcessDefinitionId("Demo01")
                .withVariables(new HashMap<>() {{
                    put("form", form);
                }}).build();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, bizKey);
        return ApiData.of(new BpmnData(processInstance.getProcessDefinitionId(),
                processInstance.getProcessDefinitionKey(), processInstance.getId(), processInstance.getName()));
    }

    @Override
    @GetMapping(value = "/task/page/{pageSize}/{pageNum}")
    public ApiData<Page<BpmnData>> taskPage(@PathVariable Integer pageSize, @PathVariable Integer pageNum, @RequestParam String assignee) {
        List<BpmnData> content = new ArrayList<>();
        Pageable pageable = PageRequest.of((pageNum - 1), pageSize);
        long taskTotal = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .count();
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .listPage((int) pageable.getOffset(), pageable.getPageSize());
        if (null != taskList && !taskList.isEmpty()) {
            BpmnData data;
            for (Task task : taskList) {
                data = new BpmnData(task.getId(), task.getName());
                data.setPId(task.getProcessDefinitionId())
                        .setPiId(task.getProcessInstanceId());
                content.add(data);
            }
        }
        return ApiData.of(new PageImpl<>(content, pageable, taskTotal));
    }

    @Override
    @PostMapping(value = "/task/complete")
    public ApiData<BpmnData> taskComplete(@RequestParam String taskId, @RequestParam String assignee) {
        boolean state = false;
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee(assignee)
                .singleResult();
        if (null != task) {
            taskService.complete(task.getId());
            state = true;
        }

        return ApiData.of(new BpmnData(state));
    }

    @Override
    @GetMapping(value = "/task/diagram/{taskId}")
    public ResponseEntity<byte[]> taskDiagram(@PathVariable String taskId) throws Exception {
        // 获取任务信息
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        if (null != task) {
            // 生成流程SVG跟踪图
            HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());

            // 统一字体：宋体
            String fontName = "Simsun";
            ProcessDiagramGenerator processDiagramGenerator = new DefaultProcessDiagramGenerator();
            List<String> highLightedActivityIdList = runtimeService.getActiveActivityIds(task.getExecutionId());
            List<String> highLightedFlowIdList = new ArrayList<>();
            InputStream inputStream = processDiagramGenerator.generateDiagram(bpmnModel,
                    highLightedActivityIdList, highLightedFlowIdList,
                    fontName, fontName, fontName);

            File downloadFile = new File(FilePathUtil.getAppDateDir(), MessageFormat.format("{0}.svg", taskId));
            IOUtils.copy(inputStream, new FileOutputStream(downloadFile));

            // 设置传输文件流数据
            String downloadFileName = downloadFile.getName().substring(7);
            byte[] downloadFileBytes = FileUtils.readFileToByteArray(downloadFile);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(URLConnection.guessContentTypeFromName(downloadFileName)));

            return new ResponseEntity<>(downloadFileBytes, headers, HttpStatus.OK);
        }
        return null;
    }
}
