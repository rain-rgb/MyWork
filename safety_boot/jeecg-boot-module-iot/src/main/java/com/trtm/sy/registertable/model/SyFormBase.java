package com.trtm.sy.registertable.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName(value = "sy_form_base")
public class SyFormBase implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 排序号
     */
    @TableField(value = "id")
    private Integer id;

    /**
     * 属性的组件类型
     */
    @TableField(value = "input_type")
    private String inputType;

    /**
     * 属性对应值的组件类型
     */
    @TableField(value = "value_input_type")
    private String valueInputType;

    /**
     * 属性的字段 例如 name
     */
    @TableField(value = "`key`")
    private String key;

    /**
     * 显示值
     */
    @TableField(value = "label")
    private String label;

    /**
     * 表格具体的值
     */
    @TableField(value = "`value`")
    private String value;

    /**
     * 属性样式
     */
    @TableField(value = "`style`")
    private String style;

    /**
     * 值样式样式
     */
    @TableField(value = "value_style")
    private String valueStyle;

    /**
     * 是否需要生成Value表格 0是 1否
     */
    @TableField(value = "`select`")
    private String select;

    @TableField(value = "colspan")
    private Integer colspan;

    @TableField(value = "`rowspan`")
    private Integer rowspan;

    @TableField(value = "value_colspan")
    private Integer valueColspan;

    @TableField(value = "`value_rowspan`")
    private Integer valueRowspan;

    @TableField(value = "`group`")
    private String group;

    @TableField(value = "`dict_type`")
    private String dictType;

}
