package com.trtm.iot.monitor.entity;

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
 * @Description: monitor
 * @Author: jeecg-boot
 * @Date:   2021-12-16
 * @Version: V1.0
 */
@Data
@TableName("monitor")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="monitor对象", description="monitor")
public class Monitor implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.Long id;
	/**设备名称*/
	@Excel(name = "摄像头名称", width = 15)
    @ApiModelProperty(value = "摄像头名称")
    private java.lang.String monitorname;
	/**序列号*/
	@Excel(name = "序列号", width = 15)
    @ApiModelProperty(value = "序列号")
    private java.lang.String serialnumber;
	/**验证码*/
	@Excel(name = "验证码", width = 15)
    @ApiModelProperty(value = "验证码")
    private java.lang.String verificationcode;
	/**备注*/
	@Excel(name = "监视设备", width = 15)
    @ApiModelProperty(value = "监视设备")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String remark;
	/**排序号*/
	@Excel(name = "排序号", width = 15)
    @ApiModelProperty(value = "排序号")
    private java.lang.Integer ordernum;
	/**通道*/
	@Excel(name = "通道", width = 15)
    @ApiModelProperty(value = "通道")
    private java.lang.Integer channel;
	/**1.实验室；2.拌合站；3.钢筋场;4.梁场；5、现场*/
	@Excel(name = "1.实验室；2.拌合站；3.钢筋场;4.梁场；5、现场", width = 15)
    @ApiModelProperty(value = "1.实验室；2.拌合站；3.钢筋场;4.梁场；5、现场")
    @Dict(dicCode="usetype")
    private java.lang.Integer usetype;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String longitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String latitude;
    /** 更新时间 */
    @ApiModelProperty(value = "更新时间")
    @Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;

	/**组织机构*/
	@Excel(name = "组织机构", width = 15)
    @ApiModelProperty(value = "组织机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String orgcode;
    private java.lang.Integer workstate;
    private java.lang.String vaddress;
    private java.lang.Integer isdel;
    private java.lang.Integer camtype;


}
