package com.trtm.iot.outsource.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 委外试验
 * @Author: jeecg-boot
 * @Date: 2022-12-09
 * @Version: V1.0
 */
@Data
@TableName("f_trial")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "委外试验对象", description = "委外试验")
@ToString
public class FTrial implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;

    /**
     * 实验ID,唯一键
     */
    @Excel(name = "实验ID,唯一键", width = 15)
    @ApiModelProperty(value = "实验ID,唯一键")
    private java.lang.String trialId;

    /**
     * 指挥部实验室ID
     */
    @Excel(name = "指挥部实验室ID", width = 15)
    @ApiModelProperty(value = "指挥部实验室ID")
    private java.lang.String labId;

    /**
     * 设备名称
     */
    @Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private java.lang.String deviceName;

    /**
     * 第三方名称
     */
    @Excel(name = "第三方名称", width = 15)
    @ApiModelProperty(value = "第三方名称")
    private java.lang.String name;

    /**
     * 实验类型
     */
    @Excel(name = "实验类型", width = 15)
    @ApiModelProperty(value = "实验类型")
    private java.lang.String trialType;

    /**
     * 工程名称
     */
    @Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private java.lang.String engineeringName;

    /**
     * 试件编号
     */
    @Excel(name = "试件编号", width = 15)
    @ApiModelProperty(value = "试件编号")
    private java.lang.String reagentNo;

    /**
     * 龄期
     */
    @Excel(name = "龄期", width = 15)
    @ApiModelProperty(value = "龄期")
    private java.lang.Integer age;

    /**
     * 试剂尺寸
     */
    @Excel(name = "试剂尺寸", width = 15)
    @ApiModelProperty(value = "试剂尺寸")
    @TableField("trial_size")
    private java.lang.String reagentSize;

    /**
     * 试件数量
     */
    @Excel(name = "试件数量", width = 15)
    @ApiModelProperty(value = "试剂数量")
    @TableField("trial_num")
    private java.lang.Integer reagentNum;

    /**
     * 设计强度
     */
    @Excel(name = "设计强度", width = 15)
    @ApiModelProperty(value = "设计强度")
    private java.lang.String designStrength;

    /**
     * 设计特征值
     */
    @Excel(name = "设计特征值", width = 15)
    @ApiModelProperty(value = "设计特征值")
    private java.lang.String designValue;

    /**
     * 实验时间
     */
    @Excel(name = "实验时间", width = 15)
    @ApiModelProperty(value = "实验时间")
    private Date trialTime;

    /**
     * 实验结果0合格1不合格2未出报告
     */
    @Excel(name = "实验结果0合格1不合格2未出报告", width = 15)
    @ApiModelProperty(value = "实验结果0合格1不合格2未出报告")
    private java.lang.Integer trialResult;

    /**
     * 创建人
     */
    @Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private java.lang.String createId;
}
