package com.trtm.iot.inventory.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @Description: wzinventory
 * @Author: jeecg-boot
 * @Date: 2023-10-20
 * @Version: V1.0
 */
@Data
@TableName("wzinventory")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "wzinventory对象", description = "wzinventory")
public class Wzinventory implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 物资盘点记录id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "物资盘点记录id")
    private java.lang.Integer id;
    /**
     * 库别
     */
    @Excel(name = "库别", width = 15)
    @ApiModelProperty(value = "库别")
    private java.lang.String kubie;
    /**
     * 类别
     */
    @Excel(name = "类别", width = 15)
    @ApiModelProperty(value = "类别")
    private java.lang.String leibie;
    /**
     * 物资名称
     */
    @Excel(name = "物资名称", width = 15)
    @ApiModelProperty(value = "物资名称")
    private java.lang.String wzName;
    /**
     * 型号
     */
    @Excel(name = "型号", width = 15)
    @ApiModelProperty(value = "型号")
    private java.lang.String xinghao;
    /**
     * 规格
     */
    @Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
    private java.lang.String guige;
    /**
     * 辅助规格
     */
    @Excel(name = "辅助规格", width = 15)
    @ApiModelProperty(value = "辅助规格")
    private java.lang.String fuzhuguige;
    /**
     * 存料地点
     */
    @Excel(name = "存料地点", width = 15)
    @ApiModelProperty(value = "存料地点")
    private java.lang.String cunliaodi;
    /**
     * 单位
     */
    @Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    private java.lang.String danwei;
    /**
     * 盘点数量
     */
    @Excel(name = "盘点数量", width = 15)
    @ApiModelProperty(value = "盘点数量")
    private java.lang.String pdnum;
    /**
     * 账期
     */
    @Excel(name = "账期", width = 15)
    @ApiModelProperty(value = "账期")
    private java.lang.String zhangqi;
    /**
     * 计算依据
     */
    @Excel(name = "计算依据", width = 15)
    @ApiModelProperty(value = "计算依据")
    private java.lang.String jisuanyiju;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String beizhu;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateBy;
    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
}
