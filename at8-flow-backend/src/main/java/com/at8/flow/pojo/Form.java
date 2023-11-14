package com.at8.flow.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Form
 *
 * @author Aaric
 * @version 0.6.0-SNAPSHOT
 */
@Data
@Accessors(chain = true)
public class Form implements Serializable {

    private String bizKey;

    private Integer state;

    private Double num;

    private String remark;
}
