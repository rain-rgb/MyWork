package com.trtm.iot.devicemixpilehistorydataonehb.entity;

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
 * @Description: device_mixpile_historydata_one_hb
 * @Author: jeecg-boot
 * @Date:   2024-01-19
 * @Version: V1.0
 */
@Data
@TableName("device_mixpile_historydata_one_hb")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_mixpile_historydata_one_hb对象", description="device_mixpile_historydata_one_hb")
public class DeviceMixpileHistorydataOneHb implements Serializable {
    private static final long serialVersionUID = 1L;

	/**序号*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "序号")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**里程*/
	@Excel(name = "里程", width = 15)
    @ApiModelProperty(value = "里程")
    private java.lang.String pileMileage;
	/**桩号*/
	@Excel(name = "桩号", width = 15)
    @ApiModelProperty(value = "桩号")
    private java.lang.String pileNo;
	/**桩设计深度*/
	@Excel(name = "桩设计深度", width = 15)
    @ApiModelProperty(value = "桩设计深度")
    private java.lang.String pileDesigndep;
	/**桩钻深*/
	@Excel(name = "桩钻深", width = 15)
    @ApiModelProperty(value = "桩钻深")
    private java.lang.String pileRealdep;
	/**每米水泥用量(平均灰量)*/
	@Excel(name = "每米水泥用量(平均灰量)", width = 15)
    @ApiModelProperty(value = "每米水泥用量(平均灰量)")
    private java.lang.String usagePm;
	/**桩x角度(垂直度)*/
	@Excel(name = "桩x角度(垂直度)", width = 15)
    @ApiModelProperty(value = "桩x角度(垂直度)")
    private java.lang.String pileX;
	/**平均提桩速度*/
	@Excel(name = "平均提桩速度", width = 15)
    @ApiModelProperty(value = "平均提桩速度")
    private java.lang.String pileUspeed;
	/**平均下桩速度*/
	@Excel(name = "平均下桩速度", width = 15)
    @ApiModelProperty(value = "平均下桩速度")
    private java.lang.String pileDspeed;
	/**水泥浆比重(平均密度)*/
	@Excel(name = "水泥浆比重(平均密度)", width = 15)
    @ApiModelProperty(value = "水泥浆比重(平均密度)")
    private java.lang.String pileDensity;
	/**状态(0合格；1不合格)*/
	@Excel(name = "状态(0合格；1不合格)", width = 15)
    @ApiModelProperty(value = "状态(0合格；1不合格)")
    private java.lang.Integer status;
	/**超标描述*/
	@Excel(name = "超标描述", width = 15)
    @ApiModelProperty(value = "超标描述")
    private java.lang.String problem;
	/**处置状态*/
	@Excel(name = "处置状态", width = 15)
    @ApiModelProperty(value = "处置状态")
    private java.lang.Integer handleStatus;
	/**峰值电流*/
	@Excel(name = "峰值电流", width = 15)
    @ApiModelProperty(value = "峰值电流")
    private java.lang.String pileMaxelec;
	/**搅拌次数*/
	@Excel(name = "搅拌次数", width = 15)
    @ApiModelProperty(value = "搅拌次数")
    private java.lang.Integer stirCount;
	/**搅拌时长*/
	@Excel(name = "搅拌时长", width = 15)
    @ApiModelProperty(value = "搅拌时长")
    private java.lang.String stirTime;
	/**注浆量*/
	@Excel(name = "注浆量", width = 15)
    @ApiModelProperty(value = "注浆量")
    private java.lang.String slurryVolume;
}
