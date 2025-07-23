package com.trtm.iot.DeviceEmplocation.entity;

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
 * @Description: 人员定位表信息
 * @Author: jeecg-boot
 * @Date:   2021-06-22
 * @Version: V1.0
 */
@Data
@TableName("device_emplocation")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_emplocation对象", description="人员定位表信息")
public class DeviceEmplocation implements Serializable {
    private static final long serialVersionUID = 1L;

	/**用户id*/
	@Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
    private java.lang.Integer empid;
	/**部门id*/
	@Excel(name = "部门id", width = 15)
    @ApiModelProperty(value = "部门id")
    private java.lang.Integer devid;
	/**进入时间*/
	@Excel(name = "进入时间", width = 15)
    @ApiModelProperty(value = "进入时间")
    private java.lang.String entersitetime;
	/**进入隧道时间*/
	@Excel(name = "进入隧道时间", width = 15)
    @ApiModelProperty(value = "进入隧道时间")
    private java.lang.String entersdtime;
	/**other1*/
	@Excel(name = "other1", width = 15)
    @ApiModelProperty(value = "other1")
    private java.lang.String other1;
	/**other2*/
	@Excel(name = "other2", width = 15)
    @ApiModelProperty(value = "other2")
    private java.lang.String other2;
	/**other3*/
	@Excel(name = "other3", width = 15)
    @ApiModelProperty(value = "other3")
    private java.lang.String other3;
	/**tagid*/
	@Excel(name = "tagid", width = 15)
    @ApiModelProperty(value = "tagid")
    private java.lang.String tagid;
	/**用户名*/
	@Excel(name = "用户名", width = 15)
    @ApiModelProperty(value = "用户名")
    private java.lang.String empname;
	/**隧道名称*/
	@Excel(name = "隧道名称", width = 15)
    @ApiModelProperty(value = "隧道名称")
    private java.lang.String deptname;
	/**班组*/
	@Excel(name = "班组", width = 15)
    @ApiModelProperty(value = "班组")
    private java.lang.String jobname;
	/**位置名称*/
	@Excel(name = "位置名称", width = 15)
    @ApiModelProperty(value = "位置名称")
    private java.lang.String devname;
	/**isinside*/
	@Excel(name = "isinside", width = 15)
    @ApiModelProperty(value = "isinside")
    private java.lang.String isinside;
	/**路线名称*/
	@Excel(name = "路线名称", width = 15)
    @ApiModelProperty(value = "路线名称")
    private java.lang.Integer tunnelid;
	/**deptid*/
	@Excel(name = "deptid", width = 15)
    @ApiModelProperty(value = "deptid")
    private java.lang.Integer deptid;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**路线名称*/
	@Excel(name = "路线名称", width = 15)
    @ApiModelProperty(value = "路线名称")
    private java.lang.String tunnelname;
}
