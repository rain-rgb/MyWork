package com.trtm.iot.zhengyang.entity;

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
 * @Description: 蒸养工序表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-18
 * @Version: V1.0
 */
@Data
@TableName("zhengyang_procedure")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="zhengyang_procedure对象", description="蒸养工序表信息")
public class ZhengyangProcedure implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**唯一码(与主表(zhiliangrenwudan)uuid关联)*/
	@Excel(name = "唯一码(与主表(zhiliangrenwudan)uuid关联)", width = 15)
    @ApiModelProperty(value = "唯一码(与主表(zhiliangrenwudan)uuid关联)")
    private java.lang.String uuid;
	/**蒸养状态1*/
	@Excel(name = "蒸养状态1", width = 15)
    @ApiModelProperty(value = "蒸养状态1")
    private java.lang.Integer status1;
	/**蒸养状态2*/
	@Excel(name = "蒸养状态2", width = 15)
    @ApiModelProperty(value = "蒸养状态2")
    private java.lang.Integer status2;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd  HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd  HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd  HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private java.util.Date starttime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
    private java.util.Date endtime;
	/**温度*/
	@Excel(name = "温度", width = 15)
    @ApiModelProperty(value = "温度")
    private java.lang.Double temperature;
	/**湿度*/
	@Excel(name = "湿度", width = 15)
    @ApiModelProperty(value = "湿度")
    private java.lang.Double humidity;
	/**监管人*/
    @ApiModelProperty(value = "监管人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
}
