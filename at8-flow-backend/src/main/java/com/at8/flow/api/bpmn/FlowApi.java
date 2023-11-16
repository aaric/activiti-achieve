package com.at8.flow.api.bpmn;

import com.at8.flow.core.data.ApiData;
import com.at8.flow.pojo.BpmnData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * 工作流模块API接口
 *
 * @author Aaric
 * @version 0.12.0-SNAPSHOT
 */
@Tag(name = "工作流模块API")
public interface FlowApi {

    @Operation(summary = "【流程】分页查询数据")
    ApiData<Page<BpmnData>> processPage(@Parameter(description = "分页大小", example = "10") Integer pageSize,
                                        @Parameter(description = "查询页码", example = "1") Integer pageNum);

    @Operation(summary = "【流程】发布")
    ApiData<BpmnData> processDeploy(@Parameter(description = "流程名称", example = "示例流程") String bpmnName,
                                    @Parameter(description = "流程设计文件（xml）") MultipartFile bpmnFile,
                                    @Parameter(description = "流程图片文件（svg）") MultipartFile imageFile) throws Exception;

    @Operation(summary = "【流程】启动实例")
    ApiData<BpmnData> processStart(@Parameter(description = "流程Key", example = "Demo01") String processKey,
                                   @Parameter(description = "业务Key", example = "custom001") String bizKey);

    @Operation(summary = "【任务】分页查询数据")
    ApiData<Page<BpmnData>> taskPage(@Parameter(description = "分页大小", example = "10") Integer pageSize,
                                     @Parameter(description = "查询页码", example = "1") Integer pageNum,
                                     @Parameter(description = "受理人", example = "aa") String assignee);

    @Operation(summary = "【任务】受理人处理")
    ApiData<BpmnData> taskComplete(@Parameter(description = "任务ID") String taskId,
                                   @Parameter(description = "受理人", example = "aa") String assignee);

    @Operation(summary = "【任务】查看跟踪图")
    ResponseEntity<byte[]> taskDiagram(@Parameter(description = "任务ID") String taskId) throws Exception;
}
