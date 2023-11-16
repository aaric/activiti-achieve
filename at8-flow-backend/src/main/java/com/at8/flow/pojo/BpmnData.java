package com.at8.flow.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Bpmn数据
 *
 * @author Aaric
 * @version 0.12.0-SNAPSHOT
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Tag(name = "Bpmn数据")
public class BpmnData {

    @Schema(description = "ID")
    private String id;

    @Schema(description = "流程ID")
    private String pId;

    @Schema(description = "流程Key")
    private String pKey;

    @Schema(description = "流程实例ID")
    private String piId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "状态")
    private Boolean state;

    public BpmnData(Boolean state) {
        this.state = state;
    }

    public BpmnData(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public BpmnData(String pId, String pKey, String name) {
        this.pId = pId;
        this.pKey = pKey;
        this.name = name;
    }

    public BpmnData(String pId, String pKey, String piId, String name) {
        this.pId = pId;
        this.pKey = pKey;
        this.piId = piId;
        this.name = name;
    }
}
