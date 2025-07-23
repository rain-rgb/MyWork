package com.trtm.iot.devicemixpilehistorydatapart.entity;

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
 * @Description: 水泥搅拌桩成桩记录每段数据信息表
 * @Author: jeecg-boot
 * @Date: 2021-11-19
 * @Version: V1.0
 */
@Data
@TableName("device_mixpile_historydata_part")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "device_mixpile_historydata_part对象", description = "水泥搅拌桩成桩记录每段数据信息表")
public class DeviceMixpileHistorydataPart implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "序号")
    private java.lang.Integer id;
    /**
     * 设备编号
     */
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
    /**
     * 桩号
     */
    @Excel(name = "桩号", width = 15)
    @ApiModelProperty(value = "桩号")
    private java.lang.String pileNo;
    @Excel(name = "里程桩号", width = 15)
    private java.lang.String pileMileage;// 里程桩号
    /**
     * 段号
     */
    @Excel(name = "段号", width = 15)
    @ApiModelProperty(value = "段号")
    private java.lang.String partPilec;
    /**
     * 段深度
     */
    @Excel(name = "段深度", width = 15)
    @ApiModelProperty(value = "段深度")
    private java.lang.String partDep;
    /**
     * 段浆量
     */
    @Excel(name = "段浆量", width = 15)
    @ApiModelProperty(value = "段浆量")
    private java.lang.String partBeton;
    /**
     * 段速度
     */
    @Excel(name = "段速度", width = 15)
    @ApiModelProperty(value = "段速度")
    private java.lang.String partSpeed;
    /**
     * 段电流
     */
    @Excel(name = "段电流", width = 15)
    @ApiModelProperty(value = "段电流")
    private java.lang.String partElectricity;
    /**
     * 段时长
     */
    @Excel(name = "段时长", width = 15)
    @ApiModelProperty(value = "段时长")
    private java.lang.String partTime;
    /**
     * 水泥浆比重（密度）
     */
    @Excel(name = "水泥浆比重（密度）", width = 15)
    @ApiModelProperty(value = "水泥浆比重（密度）")
    private java.lang.String pileDensity;
    /**
     * 段x
     */
    @Excel(name = "段x", width = 15)
    @ApiModelProperty(value = "段x")
    private java.lang.String partX;
    /**
     * 段y
     */
    @Excel(name = "段y", width = 15)
    @ApiModelProperty(value = "段y")
    private java.lang.String partY;
    /**
     * 段状态
     */
    @Excel(name = "段状态", width = 15)
    @ApiModelProperty(value = "段状态")
    @Dict(dicCode = "jbzpartstate")
    private java.lang.String partState;
    /**
     * 段结束时间
     */
    @Excel(name = "段结束时间", width = 15)
    @ApiModelProperty(value = "段结束时间")
    private java.lang.String partEndtime;
    /**
     * 数据时间
     */
    @Excel(name = "数据时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据时间")
    private java.util.Date datatime;
    private String partTs;
    private String uuid;
    private Integer gzcount;//1初搅；2复搅；
    private java.lang.String partPress;//压力
}
