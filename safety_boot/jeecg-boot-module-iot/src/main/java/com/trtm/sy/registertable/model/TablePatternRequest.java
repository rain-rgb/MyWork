package com.trtm.sy.registertable.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "表样式数据集合")
public class TablePatternRequest implements Serializable {
    private static final long serialVersionUID = 1L;


    private String id;
    private String row;
    private String col;
    private String style;
    private String inputType;
    private String key;

}

