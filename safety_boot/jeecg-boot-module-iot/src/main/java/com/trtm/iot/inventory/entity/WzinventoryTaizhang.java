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
 * @Description: wzinventory_taizhang
 * @Author: jeecg-boot
 * @Date: 2023-10-20
 * @Version: V1.0
 */
@Data
@TableName("wzinventory_taizhang")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "wzinventory_taizhang对象", description = "wzinventory_taizhang")
public class WzinventoryTaizhang implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 物资盘点台账id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "物资盘点台账id")
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
     * 单位
     */
    @Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    private java.lang.String danwei;
    /**
     * 本期盘点数量
     */
    @Excel(name = "本期盘点数量", width = 15)
    @ApiModelProperty(value = "本期盘点数量")
    private java.lang.String qizhangPdnum;
    /**
     * 存料地点
     */
    @Excel(name = "存料地点", width = 15)
    @ApiModelProperty(value = "存料地点")
    private java.lang.String cunliaodi;
    /**
     * 情况说明
     */
    @Excel(name = "情况说明", width = 15)
    @ApiModelProperty(value = "情况说明")
    private java.lang.String situation;
    /**
     * 账面单价
     */
    @Excel(name = "账面单价", width = 15)
    @ApiModelProperty(value = "账面单价")
    private java.lang.String zhangmianPrice;
    /**
     * 账面金额
     */
    @Excel(name = "账面金额", width = 15)
    @ApiModelProperty(value = "账面金额")
    private java.lang.String zhangmianJine;
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
