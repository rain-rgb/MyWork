package com.trtm.sy.registertable.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "合并单元格")
public class MergeCellModel {

    private Integer id;
    private String name;
    private Integer currentRow;
    private Integer currentCos;
    private Integer cutoffLine;
    private Integer group;
    private Boolean status;
}
