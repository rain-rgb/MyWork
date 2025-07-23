package com.trtm.iot.ycltd.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 原材料试验详情
 * @Author: jeecg-boot
 * @Date: 2022-11-18
 * @Version: V1.0
 */
@Data
@TableName("ycl_test_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ycl_test_detail对象", description = "原材料试验详情")
public class YclTestDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private java.lang.Integer id;

    /**
     * 料仓id
     */
    @Excel(name = "料仓id", width = 15)
    @ApiModelProperty(value = "料仓id")
    private java.lang.String storageId;

    /**
     * 检验批编号
     */
    @Excel(name = "检验批编号", width = 15)
    @ApiModelProperty(value = "检验批编号")
    private java.lang.String inspectionLotNumber;

    /**
     * 样品编号
     */
    @Excel(name = "样品编号", width = 15)
    @ApiModelProperty(value = "样品编号")
    private java.lang.String sampleNumber;

    /**
     * 试验名称
     */
    @Excel(name = "试验名称", width = 15)
    @ApiModelProperty(value = "试验名称")
    private java.lang.String testName;

    /**
     * 取样时间
     */
    @Excel(name = "取样时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "取样时间")
    private java.util.Date samplingTime;

    /**
     * 试验时间
     */
    @Excel(name = "试验时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "试验时间")
    private java.util.Date testTime;

    /**
     * 检验类型
     */
    @Excel(name = "检验类型 (1:自检,2:抽检)", width = 15)
    @ApiModelProperty(value = "检验类型 (1:自检,2:抽检)")
    private java.lang.Integer jianyanType;

    /**
     * 试验结论
     */
    @Excel(name = "试验结论", width = 15)
    @ApiModelProperty(value = "试验结论")
    private java.lang.String conclusion;

    /**
     * 试验状态
     */
    @Excel(name = "试验状态 0:未检验 1:检验中 2:合格 3:不合格", width = 15)
    @ApiModelProperty(value = "试验状态 0:未检验 1:检验中 2:合格 3:不合格")
    @Dict(dicCode = "syzhuangtai")
    private java.lang.Integer testStatus;

    /**
     * 试验员
     */
    @Excel(name = "试验员", width = 15)
    @ApiModelProperty(value = "试验员")
    private java.lang.String tester;

    /**
     * 报告详情
     */
    @Excel(name = "报告详情", width = 15)
    @ApiModelProperty(value = "报告详情")
    private java.lang.String report;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private java.lang.String updateTime;

    @ApiModelProperty(value = "权限")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;


    // 请求为云检任务接口数据 参数 传yunjian
    @TableField(exist = false)
    private String notedd ;

}
