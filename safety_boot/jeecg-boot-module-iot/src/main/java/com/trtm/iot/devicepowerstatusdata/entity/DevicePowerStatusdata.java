package com.trtm.iot.devicepowerstatusdata.entity;

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
 * @Description: device_power_statusdata
 * @Author: jeecg-boot
 * @Date:   2022-12-29
 * @Version: V1.0
 */
@Data
@TableName("device_power_statusdata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_power_statusdata对象", description="device_power_statusdata")
public class DevicePowerStatusdata implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备id*/
	@Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String imei;
	/**数据上传时间*/
	@Excel(name = "数据上传时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "数据上传时间")
    private java.util.Date powerdate;
	/**设备状态*/
	@Excel(name = "设备状态", width = 15)
    @ApiModelProperty(value = "设备状态")
    private java.lang.Integer devicestatus;
	/**信号强度*/
	@Excel(name = "信号强度", width = 15)
    @ApiModelProperty(value = "信号强度")
    private java.lang.Integer signal;
	/**通道1漏电mA*/
	@Excel(name = "通道1漏电mA", width = 15)
    @ApiModelProperty(value = "通道1漏电mA")
    private java.lang.Integer t1;
	/**通道2A相温度℃*/
	@Excel(name = "通道2A相温度℃", width = 15)
    @ApiModelProperty(value = "通道2A相温度℃")
    private java.lang.Integer t2;
	/**通道B相温度℃*/
	@Excel(name = "通道B相温度℃", width = 15)
    @ApiModelProperty(value = "通道B相温度℃")
    private java.lang.Integer t3;
	/**通道4C相温度℃*/
	@Excel(name = "通道4C相温度℃", width = 15)
    @ApiModelProperty(value = "通道4C相温度℃")
    private java.lang.Integer t4;
	/**通道5N相温度℃*/
	@Excel(name = "通道5N相温度℃", width = 15)
    @ApiModelProperty(value = "通道5N相温度℃")
    private java.lang.Integer t5;
	/**通道6A相电流A*/
	@Excel(name = "通道6A相电流A", width = 15)
    @ApiModelProperty(value = "通道6A相电流A")
    private java.lang.Integer t6;
	/**通道7B相电流A*/
	@Excel(name = "通道7B相电流A", width = 15)
    @ApiModelProperty(value = "通道7B相电流A")
    private java.lang.Integer t7;
	/**通道8C相电流A*/
	@Excel(name = "通道8C相电流A", width = 15)
    @ApiModelProperty(value = "通道8C相电流A")
    private java.lang.Integer t8;
	/**通道9A相电压V*/
	@Excel(name = "通道9A相电压V", width = 15)
    @ApiModelProperty(value = "通道9A相电压V")
    private java.lang.Integer t9;
	/**通道10B相电压V*/
	@Excel(name = "通道10B相电压V", width = 15)
    @ApiModelProperty(value = "通道10B相电压V")
    private java.lang.Integer t10;
	/**通道11C相电压V*/
	@Excel(name = "通道11C相电压V", width = 15)
    @ApiModelProperty(value = "通道11C相电压V")
    private java.lang.Integer t11;
	/**通道12A相频率Hz*/
	@Excel(name = "通道12A相频率Hz", width = 15)
    @ApiModelProperty(value = "通道12A相频率Hz")
    private java.lang.Integer t12;
	/**通道13B相频率Hz*/
	@Excel(name = "通道13B相频率Hz", width = 15)
    @ApiModelProperty(value = "通道13B相频率Hz")
    private java.lang.Integer t13;
	/**通道14C相频率Hz*/
	@Excel(name = "通道14C相频率Hz", width = 15)
    @ApiModelProperty(value = "通道14C相频率Hz")
    private java.lang.Integer t14;
	/**通道15A相电压相位角°*/
	@Excel(name = "通道15A相电压相位角°", width = 15)
    @ApiModelProperty(value = "通道15A相电压相位角°")
    private java.lang.Integer t15;
	/**通道16B相电压相位角°*/
	@Excel(name = "通道16B相电压相位角°", width = 15)
    @ApiModelProperty(value = "通道16B相电压相位角°")
    private java.lang.Integer t16;
	/**通道17C相电压相位角°*/
	@Excel(name = "通道17C相电压相位角°", width = 15)
    @ApiModelProperty(value = "通道17C相电压相位角°")
    private java.lang.Integer t17;
	/**通道18A相电流相位角°*/
	@Excel(name = "通道18A相电流相位角°", width = 15)
    @ApiModelProperty(value = "通道18A相电流相位角°")
    private java.lang.Integer t18;
	/**通道19B相电流相位角°*/
	@Excel(name = "通道19B相电流相位角°", width = 15)
    @ApiModelProperty(value = "通道19B相电流相位角°")
    private java.lang.Integer t19;
	/**通道20C相电流相位角°*/
	@Excel(name = "通道20C相电流相位角°", width = 15)
    @ApiModelProperty(value = "通道20C相电流相位角°")
    private java.lang.Integer t20;
	/**通道21A相功率因素%*/
	@Excel(name = "通道21A相功率因素%", width = 15)
    @ApiModelProperty(value = "通道21A相功率因素%")
    private java.lang.Integer t21;
	/**通道22B相功率因素%*/
	@Excel(name = "通道22B相功率因素%", width = 15)
    @ApiModelProperty(value = "通道22B相功率因素%")
    private java.lang.Integer t22;
	/**通道23C相功率因素%*/
	@Excel(name = "通道23C相功率因素%", width = 15)
    @ApiModelProperty(value = "通道23C相功率因素%")
    private java.lang.Integer t23;
	/**通道24合相功率因素%*/
	@Excel(name = "通道24合相功率因素%", width = 15)
    @ApiModelProperty(value = "通道24合相功率因素%")
    private java.lang.Integer t24;
	/**通道25A相有功功率KW*/
	@Excel(name = "通道25A相有功功率KW", width = 15)
    @ApiModelProperty(value = "通道25A相有功功率KW")
    private java.lang.Integer t25;
	/**通道26B相有功功率KW*/
	@Excel(name = "通道26B相有功功率KW", width = 15)
    @ApiModelProperty(value = "通道26B相有功功率KW")
    private java.lang.Integer t26;
	/**通道27C相有功功率KW*/
	@Excel(name = "通道27C相有功功率KW", width = 15)
    @ApiModelProperty(value = "通道27C相有功功率KW")
    private java.lang.Integer t27;
	/**通道28合相有功功率KW*/
	@Excel(name = "通道28合相有功功率KW", width = 15)
    @ApiModelProperty(value = "通道28合相有功功率KW")
    private java.lang.Integer t28;
	/**通道29A相无功功率KVar*/
	@Excel(name = "通道29A相无功功率KVar", width = 15)
    @ApiModelProperty(value = "通道29A相无功功率KVar")
    private java.lang.Integer t29;
	/**通道30B相无功功率KVar*/
	@Excel(name = "通道30B相无功功率KVar", width = 15)
    @ApiModelProperty(value = "通道30B相无功功率KVar")
    private java.lang.Integer t30;
	/**通道31C相无功功率KVar*/
	@Excel(name = "通道31C相无功功率KVar", width = 15)
    @ApiModelProperty(value = "通道31C相无功功率KVar")
    private java.lang.Integer t31;
	/**通道32合相无功功率KVar*/
	@Excel(name = "通道32合相无功功率KVar", width = 15)
    @ApiModelProperty(value = "通道32合相无功功率KVar")
    private java.lang.Integer t32;
	/**通道33A相视在功率KVA*/
	@Excel(name = "通道33A相视在功率KVA", width = 15)
    @ApiModelProperty(value = "通道33A相视在功率KVA")
    private java.lang.Integer t33;
	/**通道34B相视在功率KVA*/
	@Excel(name = "通道34B相视在功率KVA", width = 15)
    @ApiModelProperty(value = "通道34B相视在功率KVA")
    private java.lang.Integer t34;
	/**通道35C相视在功率KVA*/
	@Excel(name = "通道35C相视在功率KVA", width = 15)
    @ApiModelProperty(value = "通道35C相视在功率KVA")
    private java.lang.Integer t35;
	/**通道36合相视在功率KVA*/
	@Excel(name = "通道36合相视在功率KVA", width = 15)
    @ApiModelProperty(value = "通道36合相视在功率KVA")
    private java.lang.Integer t36;
	/**通道37A相有功电能高位W KWH*/
	@Excel(name = "通道37A相有功电能高位W KWH", width = 15)
    @ApiModelProperty(value = "通道37A相有功电能高位W KWH")
    private java.lang.Integer t37;
	/**通道37A相有功电能低位KWH*/
	@Excel(name = "通道37A相有功电能低位KWH", width = 15)
    @ApiModelProperty(value = "通道37A相有功电能低位KWH")
    private java.lang.Integer t38;
	/**通道39B相有功电能高位W KWH*/
	@Excel(name = "通道39B相有功电能高位W KWH", width = 15)
    @ApiModelProperty(value = "通道39B相有功电能高位W KWH")
    private java.lang.Integer t39;
	/**通道40B相有功电能低位KWH*/
	@Excel(name = "通道40B相有功电能低位KWH", width = 15)
    @ApiModelProperty(value = "通道40B相有功电能低位KWH")
    private java.lang.Integer t40;
	/**通道41C相有功电能高位W KWH*/
	@Excel(name = "通道41C相有功电能高位W KWH", width = 15)
    @ApiModelProperty(value = "通道41C相有功电能高位W KWH")
    private java.lang.Integer t41;
	/**通道42C相有功电能低位KWH*/
	@Excel(name = "通道42C相有功电能低位KWH", width = 15)
    @ApiModelProperty(value = "通道42C相有功电能低位KWH")
    private java.lang.Integer t42;
	/**通道43合相有功电能高位W KWH*/
	@Excel(name = "通道43合相有功电能高位W KWH", width = 15)
    @ApiModelProperty(value = "通道43合相有功电能高位W KWH")
    private java.lang.Integer t43;
	/**通道44合相有功电能低位KWH*/
	@Excel(name = "通道44合相有功电能低位KWH", width = 15)
    @ApiModelProperty(value = "通道44合相有功电能低位KWH")
    private java.lang.Integer t44;
	/**通道45A相无功电能高位W KVarH*/
	@Excel(name = "通道45A相无功电能高位W KVarH", width = 15)
    @ApiModelProperty(value = "通道45A相无功电能高位W KVarH")
    private java.lang.Integer t45;
	/**通道46A相无功电能低位KVarH*/
	@Excel(name = "通道46A相无功电能低位KVarH", width = 15)
    @ApiModelProperty(value = "通道46A相无功电能低位KVarH")
    private java.lang.Integer t46;
	/**通道47B相无功电能高位W KVarH*/
	@Excel(name = "通道47B相无功电能高位W KVarH", width = 15)
    @ApiModelProperty(value = "通道47B相无功电能高位W KVarH")
    private java.lang.Integer t47;
	/**通道48B相无功电能低位KVarH*/
	@Excel(name = "通道48B相无功电能低位KVarH", width = 15)
    @ApiModelProperty(value = "通道48B相无功电能低位KVarH")
    private java.lang.Integer t48;
	/**通道49C相无功电能高位W KVarH*/
	@Excel(name = "通道49C相无功电能高位W KVarH", width = 15)
    @ApiModelProperty(value = "通道49C相无功电能高位W KVarH")
    private java.lang.Integer t49;
	/**通道50C相无功电能低位KVarH*/
	@Excel(name = "通道50C相无功电能低位KVarH", width = 15)
    @ApiModelProperty(value = "通道50C相无功电能低位KVarH")
    private java.lang.Integer t50;
	/**通道51合相无功电能高位W KVarH*/
	@Excel(name = "通道51合相无功电能高位W KVarH", width = 15)
    @ApiModelProperty(value = "通道51合相无功电能高位W KVarH")
    private java.lang.Integer t51;
	/**通道52合相无功电能低位KVarH*/
	@Excel(name = "通道52合相无功电能低位KVarH", width = 15)
    @ApiModelProperty(value = "通道52合相无功电能低位KVarH")
    private java.lang.Integer t52;
	/**通道53A相视在电能高位W KVAH*/
	@Excel(name = "通道53A相视在电能高位W KVAH", width = 15)
    @ApiModelProperty(value = "通道53A相视在电能高位W KVAH")
    private java.lang.Integer t53;
	/**通道54A相视在电能低位KVAH*/
	@Excel(name = "通道54A相视在电能低位KVAH", width = 15)
    @ApiModelProperty(value = "通道54A相视在电能低位KVAH")
    private java.lang.Integer t54;
	/**通道55B相视在电能高位W KVAH*/
	@Excel(name = "通道55B相视在电能高位W KVAH", width = 15)
    @ApiModelProperty(value = "通道55B相视在电能高位W KVAH")
    private java.lang.Integer t55;
	/**通道56B相视在电能低位KVAH*/
	@Excel(name = "通道56B相视在电能低位KVAH", width = 15)
    @ApiModelProperty(value = "通道56B相视在电能低位KVAH")
    private java.lang.Integer t56;
	/**通道57C相视在电能高位W KVAH*/
	@Excel(name = "通道57C相视在电能高位W KVAH", width = 15)
    @ApiModelProperty(value = "通道57C相视在电能高位W KVAH")
    private java.lang.Integer t57;
	/**通道58C相视在电能低位KVAH*/
	@Excel(name = "通道58C相视在电能低位KVAH", width = 15)
    @ApiModelProperty(value = "通道58C相视在电能低位KVAH")
    private java.lang.Integer t58;
	/**通道59合相视在电能高位W KVAH*/
	@Excel(name = "通道59合相视在电能高位W KVAH", width = 15)
    @ApiModelProperty(value = "通道59合相视在电能高位W KVAH")
    private java.lang.Integer t59;
	/**通道60合相视在电能低位KVAH*/
	@Excel(name = "通道60合相视在电能低位KVAH", width = 15)
    @ApiModelProperty(value = "通道60合相视在电能低位KVAH")
    private java.lang.Integer t60;
	/**通道61三相电流平衡度%*/
	@Excel(name = "通道61三相电流平衡度%", width = 15)
    @ApiModelProperty(value = "通道61三相电流平衡度%")
    private java.lang.Integer t61;
	/** 通道62三相电压平衡度%*/
	@Excel(name = " 通道62三相电压平衡度%", width = 15)
    @ApiModelProperty(value = " 通道62三相电压平衡度%")
    private java.lang.Integer t62;
}
