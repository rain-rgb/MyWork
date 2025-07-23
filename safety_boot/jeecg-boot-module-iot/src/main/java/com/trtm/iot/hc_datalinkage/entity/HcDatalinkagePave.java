package com.trtm.iot.hc_datalinkage.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 数据联动（摊铺数据）
 * @Author: jeecg-boot
 * @Date:   2023-06-13
 * @Version: V1.0
 */
@ApiModel(value="hc_datalinkage_pave对象", description="数据联动（摊铺数据）")
@Data
@TableName("hc_datalinkage_pave")
public class HcDatalinkagePave implements Serializable {
    private static final long serialVersionUID = 1L;

	/**记录id，唯一递增*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "记录id，唯一递增")
    private java.lang.Integer id;
	/**机械id*/
	@Excel(name = "机械id", width = 15)
    @ApiModelProperty(value = "机械id")
    private java.lang.String machineid;
	/**机械名称*/
	@Excel(name = "机械名称", width = 15)
    @ApiModelProperty(value = "机械名称")
    private java.lang.String machinename;
	/**开始摊铺时间*/
	@Excel(name = "开始摊铺时间", width = 15)
    @ApiModelProperty(value = "开始摊铺时间")
    private java.lang.String startpavetime;
	/**结束摊铺时间*/
	@Excel(name = "结束摊铺时间", width = 15)
    @ApiModelProperty(value = "结束摊铺时间")
    private java.lang.String endpavetime;
	/**摊铺时长*/
	@Excel(name = "摊铺时长", width = 15)
    @ApiModelProperty(value = "摊铺时长")
    private java.lang.String pavetime;
	/**开始摊铺桩号*/
	@Excel(name = "开始摊铺桩号", width = 15)
    @ApiModelProperty(value = "开始摊铺桩号")
    private java.lang.String startpavestation;
	/**结束摊铺桩号*/
	@Excel(name = "结束摊铺桩号", width = 15)
    @ApiModelProperty(value = "结束摊铺桩号")
    private java.lang.String endpavestation;
	/**主表id*/
    @ApiModelProperty(value = "主表id")
    private java.lang.Integer zbid;
}
