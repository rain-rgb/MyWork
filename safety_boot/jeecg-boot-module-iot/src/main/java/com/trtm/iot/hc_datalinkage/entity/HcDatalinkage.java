package com.trtm.iot.hc_datalinkage.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
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

/**
 * @Description: 数据联动（运输信息）
 * @Author: jeecg-boot
 * @Date:   2023-06-13
 * @Version: V1.0
 */
@ApiModel(value="hc_datalinkage对象", description="数据联动（运输信息）")
@Data
@TableName("hc_datalinkage")
public class HcDatalinkage implements Serializable {
    private static final long serialVersionUID = 1L;

	/**记录id，唯一递增*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "记录id，唯一递增")
    private java.lang.Integer id;
	/**运输车id*/
	@Excel(name = "运输车id", width = 15)
    @ApiModelProperty(value = "运输车id")
    private java.lang.String truckid;
	/**出厂时间*/
	@Excel(name = "出厂时间", width = 15)
    @ApiModelProperty(value = "出厂时间")
    private java.lang.String outstationtime;
	/**卸料时间*/
	@Excel(name = "卸料时间", width = 15)
    @ApiModelProperty(value = "卸料时间")
    private java.lang.String loadoffmixturetime;
	/**接料时长*/
	@Excel(name = "接料时长", width = 15)
    @ApiModelProperty(value = "接料时长")
    private java.lang.String loadmixturetime;
	/**摊铺记录id数组，逗号分隔*/
	@Excel(name = "摊铺记录id数组，逗号分隔", width = 15)
    @ApiModelProperty(value = "摊铺记录id数组，逗号分隔")
    private java.lang.String pavemixtureinfoids;
	/**混合料生产详情id数组，逗号分隔*/
	@Excel(name = "混合料生产详情id数组，逗号分隔", width = 15)
    @ApiModelProperty(value = "混合料生产详情id数组，逗号分隔")
    private java.lang.String pavemixturematerialinfoids;
	/**混合料类型，1是水稳，2是沥青*/
	@Excel(name = "混合料类型，1是水稳，2是沥青", width = 15)
    @ApiModelProperty(value = "混合料类型，1是水稳，2是沥青")
    private java.lang.Integer materialtype;
    private java.lang.Integer isdj;
}
