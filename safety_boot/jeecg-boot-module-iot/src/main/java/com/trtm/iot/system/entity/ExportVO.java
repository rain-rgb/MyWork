package com.trtm.iot.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 施工配合比
 * @Author: jeecg-boot
 * @Date: 2021-05-19
 * @Version: V1.0
 */
@Data
@TableName("shigongphb")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "shigongphb对象", description = "施工配合比")
public class ExportVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 设备编号
     */
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String shebeibianhao;
    /**
     * 施工部位
     */
    @Excel(name = "施工部位", width = 140)
    @ApiModelProperty(value = "施工部位")
    private String conspos;
    /**
     * 配合比号
     */
    @Excel(name = "配料单号", width = 15)
    @ApiModelProperty(value = "配合比号")
    private String code;
    /**
     * 任务单号
     */
    @Excel(name = "任务单号", width = 15)
    @ApiModelProperty(value = "任务单号")
    private String renwudan;
    /**
     * 浇筑日期
     */
    @Excel(name = "浇筑日期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "浇筑日期")
    private Date jztime;
    /**
     * 强度等级
     */
    @Excel(name = "强度等级", width = 15)
    @ApiModelProperty(value = "强度等级")
    @Dict(dicCode = "betlev")
    private String betlev;
}
