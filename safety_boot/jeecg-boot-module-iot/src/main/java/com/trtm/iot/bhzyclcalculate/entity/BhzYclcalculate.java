package com.trtm.iot.bhzyclcalculate.entity;

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
 * @Description: bhz_yclcalculate
 * @Author: jeecg-boot
 * @Date: 2023-10-24
 * @Version: V1.0
 */
@Data
@TableName("bhz_yclcalculate")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "bhz_yclcalculate对象", description = "bhz_yclcalculate")
public class BhzYclcalculate implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 拌和站原材料核算表id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "拌和站原材料核算表id")
    private java.lang.Integer id;
    /**设备编号*/
    @Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String shebeiNo;
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
    private java.lang.String fuzhuGuige;
    /**
     * 单位
     */
    @Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    private java.lang.String danwei;
    /*
     * 统计时间
     */
    @Excel(name = "统计时间", width = 15)
    @ApiModelProperty(value = "统计时间，以天为单位")
    private String statisticsTime;
    /**
     * 上期末物资支出
     */
    @Excel(name = "上期末物资支出", width = 15)
    @ApiModelProperty(value = "上期末物资支出")
    private java.lang.String lastWzzc;
    /**
     * 上期末系统消耗
     */
    @Excel(name = "上期末系统消耗", width = 15)
    @ApiModelProperty(value = "上期末系统消耗")
    private java.lang.String lastSysxh;
    /**
     * 上期末节超(节+超-)
     */
    @Excel(name = "上期末节超(节+超-)", width = 15)
    @ApiModelProperty(value = "上期末节超(节+超-)")
    private java.lang.String lastJc;
    /**
     * 本期物资支出
     */
    @Excel(name = "本期物资支出", width = 15)
    @ApiModelProperty(value = "本期物资支出")
    private java.lang.String thisWzzc;
    /**
     * 本期系统消耗
     */
    @Excel(name = "本期系统消耗", width = 15)
    @ApiModelProperty(value = "本期系统消耗")
    private java.lang.String thisSysxh;
    /**
     * 本期节超(节+超-)
     */
    @Excel(name = "本期节超(节+超-)", width = 15)
    @ApiModelProperty(value = "本期节超(节+超-)")
    private java.lang.String thisJc;
    /**
     * 开累物资支出
     */
    @Excel(name = "开累物资支出", width = 15)
    @ApiModelProperty(value = "开累物资支出")
    private java.lang.String klWzzc;
    /**
     * 开累系统消耗
     */
    @Excel(name = "开累系统消耗", width = 15)
    @ApiModelProperty(value = "开累系统消耗")
    private java.lang.String klSysxh;
    /**
     * 开累节超(节+超-)
     */
    @Excel(name = "开累节超(节+超-)", width = 15)
    @ApiModelProperty(value = "开累节超(节+超-)")
    private java.lang.String klJc;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String beizhu;
    /**
     * 登记人
     */
    @ApiModelProperty(value = "登记人")
    private java.lang.String createBy;
    /**
     * 登记时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "登记时间")
    private java.util.Date createTime;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateBy;
    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
}
