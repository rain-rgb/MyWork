package com.trtm.sy.registertable.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName(value = "sy_form_report")
public class SyFormReport implements Serializable {
    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.AUTO)
    private Integer id;
    private String inputType;

    @TableField(value = "`key`")
    private String key;
    private String label;
    private String style;

    @TableField(value = "`select`")
    private String select;
    private Integer colspan;
    private Integer rowspan;

    @TableField(value = "`group`")
    private String group;
    private String reportNumber;
    private String sampleId;
    private String paramId;
    private String dictType;
    private String required;
    private String length;
    private String parent;

    @TableField(value = "`type`")
    private String type;

    @TableField(value = "`lv`")
    private String lv;

    @TableField(value = "`display`")
    private String display;

    @TableField(exist = false)
    private int cell;

}
