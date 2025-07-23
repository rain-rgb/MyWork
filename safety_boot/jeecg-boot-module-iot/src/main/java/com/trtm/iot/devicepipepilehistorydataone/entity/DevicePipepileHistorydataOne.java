package com.trtm.iot.devicepipepilehistorydataone.entity;

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
 * @Description: device_pipepile_historydata_one
 * @Author: jeecg-boot
 * @Date:   2022-07-21
 * @Version: V1.0
 */
@Data
@TableName("device_pipepile_historydata_one")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_pipepile_historydata_one对象", description="device_pipepile_historydata_one")
public class DevicePipepileHistorydataOne implements Serializable {
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
	/**最大压桩力(KN)*/
	@Excel(name = "最大压桩力(KN)", width = 15)
    @ApiModelProperty(value = "最大压桩力(KN)")
    private java.lang.String pileUpress;
	/**最大夹持力(KN)*/
	@Excel(name = "最大夹持力(KN)", width = 15)
    @ApiModelProperty(value = "最大夹持力(KN)")
    private java.lang.String pileDpress;
	/**平均速度(cm/min)*/
	@Excel(name = "平均速度(cm/min)", width = 15)
    @ApiModelProperty(value = "平均速度(cm/min)")
    private java.lang.String pileSpeed;
	/**接桩次数*/
	@Excel(name = "接桩次数", width = 15)
    @ApiModelProperty(value = "接桩次数")
    private java.lang.Integer times;
	/**设计桩长*/
	@Excel(name = "设计桩长", width = 15)
    @ApiModelProperty(value = "设计桩长")
    private java.lang.Integer pileDesigndep;
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
	/**时间戳*/
	@Excel(name = "时间戳", width = 15)
    @ApiModelProperty(value = "时间戳")
    private java.lang.String ts;
	/**桩经度*/
	@Excel(name = "桩经度", width = 15)
    @ApiModelProperty(value = "桩经度")
    private java.lang.String pileLgd;
	/**桩纬度*/
	@Excel(name = "桩纬度", width = 15)
    @ApiModelProperty(value = "桩纬度")
    private java.lang.String pileLtd;
    private Integer istongji;//是否统计
    private Integer chaobiaodengji;//超标等级0：合格；1不合格
    private java.lang.String address;//图片地址
    private Integer overproofStatus;
    private java.lang.String ycyy;//预警原因 桩长异常，倾角异常
    private java.lang.String spurl;//视频地址
    private Integer zbzl;//质保资料数据推送0未推送1推送成功40推送失败
    private java.lang.String gzpj;//管桩评级
    private Double csarea; //横截面积
    private Double tonnage; //吨位
}
