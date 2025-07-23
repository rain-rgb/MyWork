package com.trtm.sy.symain.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: sy_main
 * @Author: jeecg-boot
 * @Date: 2022-09-08
 * @Version: V1.0
 */
@Data
@TableName("sy_main")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sy_main对象", description = "sy_main")
public class SyMain implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private Integer id;
    /**
     * 唯一id
     */
    @Excel(name = "唯一id", width = 15)
    @ApiModelProperty(value = "唯一id")
    private String uuid;
    /**
     * 组织结构代码
     */
    @Excel(name = "组织结构代码", width = 15)
    @ApiModelProperty(value = "组织结构代码")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private String sysorgcode;
    /**
     * 设备编号
     */
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String deviceId;
    /**
     * 样品封签照片（图片）
     */
    @Excel(name = "样品封签照片（图片）", width = 15)
    @ApiModelProperty(value = "样品封签照片（图片）")
    private String fqzpstart;
    /**
     * 留样封签照片（图片）
     */
    @Excel(name = "留样封签照片（图片）", width = 15)
    @ApiModelProperty(value = "留样封签照片（图片）")
    private String fqzpend;
    /**
     * 仪器校准照片（图片）
     */
    @Excel(name = "仪器校准照片（图片）", width = 15)
    @ApiModelProperty(value = "仪器校准照片（图片）")
    private String yqjzzp;
    /**
     * 检测环境照片（图片）
     */
    @Excel(name = "检测环境照片（图片）", width = 15)
    @ApiModelProperty(value = "检测环境照片（图片）")
    private String jchjzp;
    /**
     * 试验类型，与dps_jc_testItemType表titCode关联
     */
    @Excel(name = "试验类型，与dps_jc_testItemType表titCode关联", width = 15)
    @ApiModelProperty(value = "试验类型，与dps_jc_testItemType表titCode关联")
    private String testType;
    /**
     * 检测标准
     */
    @Excel(name = "检测标准", width = 15)
    @ApiModelProperty(value = "检测标准")
    private String testStandard;
    /**
     * 检测状态（0：未开始，1：准备中，2：试验中，3：已结束，10：合格，11：不合格，默认为0）
     */
    @Excel(name = "检测状态（0：未开始，1：准备中，2：试验中，3：已结束，10：合格，11：不合格，默认为0）", width = 15)
    @ApiModelProperty(value = "检测状态（0：未开始，1：准备中，2：试验中，3：已结束，10：合格，11：不合格，默认为0）")
    @Dict(dicCode = "test_status")
    private Integer testStatus;
    /**
     * 试验结果（0：合格，1：不合格，暂不使用）
     */
    @Excel(name = "试验结果（0：合格，1：不合格，暂不使用）", width = 15)
    @ApiModelProperty(value = "试验结果（0：合格，1：不合格，暂不使用）")
    private Integer testResult;
    /**
     * 检测数据（PDF文件）
     */
    @Excel(name = "检测数据（PDF文件）", width = 15)
    @ApiModelProperty(value = "检测数据（PDF文件）")
    private String testData;
    /**
     * 摄像头id
     */
    @Excel(name = "摄像头id", width = 15)
    @ApiModelProperty(value = "摄像头id")
    private String cameraId;
    /**
     * 检验批
     */
    @Excel(name = "检验批", width = 15)
    @ApiModelProperty(value = "检验批")
    private String inspectionIot;
    /**
     * 创建人（id）
     */
    @ApiModelProperty(value = "创建人（id）")
    @Dict(dictTable = "sys_user",dicCode = "username",dicText = "realname")
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
