package com.trtm.sy.registertable.model;


import lombok.Data;

import java.io.Serializable;


@Data
public class BaseForm implements Serializable {
    private static final long serialVersionUID = 1L;


    public BaseForm() {
    }

    public BaseForm(Integer id,
                     String key,
                     String value,
                     String type,
                     Integer colspan,
                     Integer rowspan,
                     String style,
                     String dictType,
                     String required,
                     int length,
                     int abeam,
                     int empty
    ) {
        this.id = id;
        this.key = key;
        this.value = value;
        this.type = type;
        this.colspan = colspan;
        this.rowspan = rowspan;
        this.style = style;
        this.dictType = dictType;
        this.required = required;
        this.length = length;
        this.abeam = abeam;
        this.empty = empty;
    }

    /**
     * 组件ID 需求确保唯一性
     */
    private Integer id;

    /**
     * 表格的类型
     */
    private String type;

    /**
     * 字段名称
     */
    private String key;

    /**
     * 表格的值
     */
    private String value;

    /**
     * 夸几列
     */
    private Integer colspan;

    /**
     * 夸几行
     */
    private Integer rowspan;

    /**
     * 是否显示表格
     */
    private Boolean hidden;

    private String style;

    private String dictType;

    private String required;

    private int length;

    private int abeam;

    private int empty;
}
