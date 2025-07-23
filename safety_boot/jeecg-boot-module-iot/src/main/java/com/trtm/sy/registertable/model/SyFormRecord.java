package com.trtm.sy.registertable.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 原始记录表单实体类
 * @Author Liupei
 * @Date 2023-06-29
 */
@Data
@TableName(value = "sy_form_record")
public class SyFormRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 排序号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 属性的组件类型
     */
    @TableField(value = "input_type")
    private String inputType;

    /**
     * 属性的字段 例如 name
     */
    @TableField(value = "`key`")
    private String key;

    /**
     * 表格具体的值
     */
    @TableField(value = "`style`")
    private String style;

    /**
     * 表格具体的值
     */
    @TableField(value = "`value`")
    private String value;

    /**
     * 列跨度
     */
    @TableField(value = "`colspan`")
    private Integer colspan;

    /**
     * 行跨度
     */
    @TableField(value = "`rowspan`")
    private Integer rowspan;

    /**
     * 行组
     */
    @TableField(value = "`group`")
    private String group;

    /**
     * 记录单号
     */
    @TableField(value = "`report_number`")
    private String reportNumber;

    /**
     * 字典类型
     */
    @TableField(value = "`dict_type`")
    private String dictType;

    @TableField(value = "`required`")
    private String required;

    /**
     * 长度
     */
    @TableField(value = "`length`")
    private int length;

    private int abeam;

    private int empty;
    @TableField(value = "`bgkey`")
    private String bgkey;
}
