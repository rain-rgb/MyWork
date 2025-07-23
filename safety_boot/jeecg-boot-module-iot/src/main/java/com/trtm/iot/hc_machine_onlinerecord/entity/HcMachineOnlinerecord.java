package com.trtm.iot.hc_machine_onlinerecord.entity;

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
 * @Description: 设备上下线
 * @Author: jeecg-boot
 * @Date:   2023-04-26
 * @Version: V1.0
 */
@Data
@TableName("hc_machine_onlinerecord")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hc_machine_onlinerecord对象", description="设备上下线")
public class HcMachineOnlinerecord implements Serializable {
    private static final long serialVersionUID = 1L;

	/**机械id*/
	@Excel(name = "机械id", width = 15)
    @ApiModelProperty(value = "机械id")
    private java.lang.String machineid;
	/**机械名称*/
	@Excel(name = " 机械名称", width = 15)
    @ApiModelProperty(value = " 机械名称")
    private java.lang.String machinename;
	/**机械类型名称*/
	@Excel(name = "机械类型名称", width = 15)
    @ApiModelProperty(value = "机械类型名称")
    private java.lang.String sncode;
	/**机械类型名称*/
	@Excel(name = "机械类型名称", width = 15)
    @ApiModelProperty(value = "机械类型名称")
    private java.lang.String machinetype;
	/**上线时间*/
	@Excel(name = "上线时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "上线时间")
    private java.util.Date logintime;
	/**下线时间*/
	@Excel(name = " 下线时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = " 下线时间")
    private java.util.Date logofftime;
	/**projectid*/
	@Excel(name = "projectid", width = 15)
    @ApiModelProperty(value = "projectid")
    private java.lang.String projectid;
	/**sectionid*/
	@Excel(name = "sectionid", width = 15)
    @ApiModelProperty(value = "sectionid")
    private java.lang.String sectionid;
}
