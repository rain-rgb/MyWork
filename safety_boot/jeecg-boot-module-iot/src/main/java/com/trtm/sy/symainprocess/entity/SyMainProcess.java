package com.trtm.sy.symainprocess.entity;

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
 * @Description: sy_main_process
 * @Author: jeecg-boot
 * @Date: 2022-09-08
 * @Version: V1.0
 */
@Data
@TableName("sy_main_process")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sy_main_process对象", description = "sy_main_process")
public class SyMainProcess implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private Integer id;
    /**
     * 组织结构代码
     */
    @Excel(name = "组织结构代码", width = 15)
    @ApiModelProperty(value = "组织结构代码")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private String sysorgcode;
    /**
     * 唯一id
     */
    @Excel(name = "唯一id", width = 15)
    @ApiModelProperty(value = "唯一id")
    private String uuid;
    /**
     * 试验id，关联与sy_main表uuid关联
     */
    @Excel(name = "试验id，关联与sy_main表uuid关联", width = 15)
    @ApiModelProperty(value = "试验id，关联与sy_main表uuid关联")
    private String testId;
    /**
     * 工序编号
     */
    @Excel(name = "工序编号", width = 15)
    @ApiModelProperty(value = "工序编号")
    private String processId;
    /**
     * 工序名称
     */
    @Excel(name = "工序名称", width = 15)
    @ApiModelProperty(value = "工序名称")
    private String processName;
    /**
     * 试验员id
     */
    @Excel(name = "试验员id", width = 15)
    @ApiModelProperty(value = "试验员id")
    private String testBy;
    /**
     * 试验状态（0：未开始，1：已完成，默认为0）
     */
    @Excel(name = "试验状态（0：未开始，1：已完成，默认为0）", width = 15)
    @ApiModelProperty(value = "试验状态（0：未开始，1：已完成，默认为0）")
    private Integer status;
    /**
     * 试验时间
     */
    @Excel(name = "试验时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "试验时间")
    private Date testTime;
    /**
     * 设备编号
     */
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String deviceId;
    /**
     * 试验记录(PDF)
     */
    @Excel(name = "试验记录(PDF)", width = 15)
    @ApiModelProperty(value = "试验记录(PDF)")
    private String testRecord;
    /**
     * 试验图片
     */
    @Excel(name = "试验图片", width = 15)
    @ApiModelProperty(value = "试验图片")
    private String testPhoto;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
}
