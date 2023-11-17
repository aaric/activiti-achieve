package com.at8.flow.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * FormData
 *
 * @author Aaric
 * @version 0.6.0-SNAPSHOT
 */
@Data
@Accessors(chain = true)
@Tag(name = "表单数据")
public class FormData implements Serializable {

    @Schema(description = "业务Key")
    private String bizKey;

    @Schema(description = "状态")
    private Integer state;

    @Schema(description = "数目")
    private Double num;

    @Schema(description = "备注")
    private String remark;
}
