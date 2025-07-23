package com.trtm.iot.devicehammeringhistorydataone.entity;

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
 * @Description: device_hammering_historydata_one
 * @Author: jeecg-boot
 * @Date:   2024-03-08
 * @Version: V1.0
 */
@Data
@TableName("device_hammering_historydata_one")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_hammering_historydata_one对象", description="device_hammering_historydata_one")
public class DeviceHammeringHistorydataOne implements Serializable {
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
	/**桩号*/
	@Excel(name = "桩号", width = 15)
    @ApiModelProperty(value = "桩号")
    private java.lang.String pileNo;
	/**施工长度(m)*/
	@Excel(name = "施工长度(m)", width = 15)
    @ApiModelProperty(value = "施工长度(m)")
    private java.lang.String pileRealdep;
	/**成桩时间*/
	@Excel(name = "成桩时间", width = 15)
    @ApiModelProperty(value = "成桩时间")
    private java.lang.String pileWorktime;
	/**最大垂直度(%)*/
	@Excel(name = "最大垂直度(%)", width = 15)
    @ApiModelProperty(value = "最大垂直度(%)")
    private java.lang.String pileY;
	/**结束时间*/
	@Excel(name = "结束时间", width = 15)
    @ApiModelProperty(value = "结束时间")
    private java.lang.String pileTime;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15)
    @ApiModelProperty(value = "开始时间")
    private java.lang.String pileStarttime;
	/**锤击次数*/
	@Excel(name = "锤击次数", width = 15)
    @ApiModelProperty(value = "锤击次数")
    private java.lang.Integer pileSecond;
	/**设计桩长*/
	@Excel(name = "设计桩长", width = 15)
    @ApiModelProperty(value = "设计桩长")
    private java.lang.Double pileDesigndep;
	/**关联软基任务单 device_mixpile_rwd*/
	@Excel(name = "关联软基任务单 device_mixpile_rwd", width = 15)
    @ApiModelProperty(value = "关联软基任务单 device_mixpile_rwd")
    private java.lang.String rjrwd;
	/**唯一码*/
	@Excel(name = "唯一码", width = 15)
    @ApiModelProperty(value = "唯一码")
    private java.lang.String uuid;
	/**里程*/
	@Excel(name = "里程", width = 15)
    @ApiModelProperty(value = "里程")
    private java.lang.String pileMileage;
	/**数据时间*/
	@Excel(name = "数据时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据时间")
    private java.util.Date datatime;
	/**桩经度*/
	@Excel(name = "桩经度", width = 15)
    @ApiModelProperty(value = "桩经度")
    private java.lang.String pileLgd;
	/**桩纬度*/
	@Excel(name = "桩纬度", width = 15)
    @ApiModelProperty(value = "桩纬度")
    private java.lang.String pileLtd;
	/**是否统计 0 未统计 1 已统计*/
	@Excel(name = "是否统计 0 未统计 1 已统计", width = 15)
    @ApiModelProperty(value = "是否统计 0 未统计 1 已统计")
    private java.lang.Integer istongji;
	/**超标等级0：合格；1不合格*/
	@Excel(name = "超标等级0：合格；1不合格", width = 15)
    @ApiModelProperty(value = "超标等级0：合格；1不合格")
    private java.lang.Integer chaobiaodengji;

	/**图片地址*/
	@Excel(name = "图片地址", width = 15)
    @ApiModelProperty(value = "图片地址")
    private java.lang.String address;
	/**0未处置；10：处置；20：闭合，30驳回*/
	@Excel(name = "0未处置；10：处置；20：闭合，30驳回", width = 15)
    @ApiModelProperty(value = "0未处置；10：处置；20：闭合，30驳回")
    private java.lang.Integer overproofStatus;
	/**异常原因*/
	@Excel(name = "异常原因", width = 15)
    @ApiModelProperty(value = "异常原因")
    private java.lang.String ycyy;
	/**视频地址*/
	@Excel(name = "视频地址", width = 15)
    @ApiModelProperty(value = "视频地址")
    private java.lang.String spurl;
	/**质保资料数据推送0未推送1推送成功40推送失败*/
	@Excel(name = "质保资料数据推送0未推送1推送成功40推送失败", width = 15)
    @ApiModelProperty(value = "质保资料数据推送0未推送1推送成功40推送失败")
    private java.lang.Integer zbzl;
	/**管桩评级*/
	@Excel(name = "管桩评级", width = 15)
    @ApiModelProperty(value = "管桩评级")
    private java.lang.String gzpj;
	/**横截面积*/
	@Excel(name = "横截面积", width = 15)
    @ApiModelProperty(value = "横截面积")
    private java.lang.Double csarea;
	/**吨位*/
	@Excel(name = "吨位", width = 15)
    @ApiModelProperty(value = "吨位")
    private java.lang.Double tonnage;
    private java.lang.Integer alertstate;// 是否进行超标判断，0：未判断；1判断完成；40判断异常
    private java.lang.String problem;// 问题描述
}
