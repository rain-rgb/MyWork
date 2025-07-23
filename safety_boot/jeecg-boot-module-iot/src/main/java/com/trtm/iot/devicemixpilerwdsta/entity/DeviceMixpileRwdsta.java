package com.trtm.iot.devicemixpilerwdsta.entity;

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
 * @Description: device_mixpile_rwdsta
 * @Author: jeecg-boot
 * @Date:   2022-04-19
 * @Version: V1.0
 */
@Data
@TableName("device_mixpile_rwdsta")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_mixpile_rwdsta对象", description="device_mixpile_rwdsta")
public class DeviceMixpileRwdsta implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**软基工单号*/
	@Excel(name = "软基工单号", width = 15)
    @ApiModelProperty(value = "软基工单号")
    private java.lang.String rjrwd;
	/**组织机构*/
	@Excel(name = "组织机构", width = 15)
    @ApiModelProperty(value = "组织机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String orgcode;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**里程*/
	@Excel(name = "里程", width = 15)
    @ApiModelProperty(value = "里程")
    private java.lang.String mileage;
	/**设计桩基数*/
	@Excel(name = "设计桩基数", width = 15)
    @ApiModelProperty(value = "设计桩基数")
    private java.lang.Integer descount;
	/**开工日期*/
	@Excel(name = "开工日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开工日期")
    private java.util.Date starttime;
	/**截止日期*/
	@Excel(name = "截止日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "截止日期")
    private java.util.Date endtime;
	/**工单状态*/
	@Excel(name = "rjstatus", width = 15)
    @ApiModelProperty(value = "rjstatus")
    private java.lang.Integer rjstatus;
	/**工单进度*/
	@Excel(name = "totalpro", width = 15)
    @ApiModelProperty(value = "totalpro")
    private java.math.BigDecimal totalpro;
	/**完成里程数*/
	@Excel(name = "完成里程数", width = 15)
    @ApiModelProperty(value = "完成里程数")
    private java.lang.Double mileagetotal;
	/**超标数*/
	@Excel(name = "超标数", width = 15)
    @ApiModelProperty(value = "超标数")
    private java.math.BigDecimal chaobiaototal;
	/**处置数*/
	@Excel(name = "处置数", width = 15)
    @ApiModelProperty(value = "处置数")
    private java.math.BigDecimal handled;
	/**checked*/
	@Excel(name = "闭合数", width = 15)
    @ApiModelProperty(value = "闭合数")
    private java.math.BigDecimal checked;
	/**超标率*/
	@Excel(name = "超标率", width = 15)
    @ApiModelProperty(value = "超标率")
    private java.math.BigDecimal chaobiaolv;
    /**完成桩基数*/
    @Excel(name = "完成桩基数", width = 15)
    @ApiModelProperty(value = "完成桩基数")
    private java.lang.Double piletotal;
}
