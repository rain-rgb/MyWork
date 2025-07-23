package com.trtm.iot.hc_pavement_stationdata.entity;

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
 * @Description: 获取逐桩数据
 * @Author: jeecg-boot
 * @Date:   2023-04-24
 * @Version: V1.0
 */
@Data
@TableName("hc_pavement_stationdata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hc_pavement_stationdata对象", description="获取逐桩数据")
public class HcPavementStationdata implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**桩号*/
	@Excel(name = "桩号", width = 15)
    @ApiModelProperty(value = "桩号")
    private java.lang.String station;
	/**结构层，数值越大为越高层*/
	@Excel(name = "结构层，数值越大为越高层", width = 15)
    @ApiModelProperty(value = "结构层，数值越大为越高层")
    private java.lang.String layer;
	/**左右幅，0为左幅，1为右幅*/
	@Excel(name = "左右幅，0为左幅，1为右幅", width = 15)
    @ApiModelProperty(value = "左右幅，0为左幅，1为右幅")
    private java.lang.Integer offset;
	/**摊铺温度（°C）*/
	@Excel(name = " 摊铺温度（°C）", width = 15)
    @ApiModelProperty(value = " 摊铺温度（°C）")
    private java.lang.String pavingtemperature;
	/**温度离析（°C）*/
	@Excel(name = " 温度离析（°C）", width = 15)
    @ApiModelProperty(value = " 温度离析（°C）")
    private java.lang.String temperaturesegregation;
	/**摊铺速度（m/min）*/
	@Excel(name = " 摊铺速度（m/min）", width = 15)
    @ApiModelProperty(value = " 摊铺速度（m/min）")
    private java.lang.Double pavingspeed;
	/**初压温度（°C）钢轮最高温度*/
	@Excel(name = "初压温度（°C）钢轮最高温度", width = 15)
    @ApiModelProperty(value = "初压温度（°C）钢轮最高温度")
    private java.lang.String temperaturefirst;
	/**复压温度（°C）胶轮最高温度*/
	@Excel(name = "复压温度（°C）胶轮最高温度", width = 15)
    @ApiModelProperty(value = "复压温度（°C）胶轮最高温度")
    private java.lang.String temperaturerepeat;
	/**初压遍数（遍）钢轮遍数*/
	@Excel(name = "初压遍数（遍）钢轮遍数", width = 15)
    @ApiModelProperty(value = "初压遍数（遍）钢轮遍数")
    private java.lang.String timesfirst;
	/**复压遍数（遍）胶轮遍数*/
	@Excel(name = "复压遍数（遍）胶轮遍数", width = 15)
    @ApiModelProperty(value = "复压遍数（遍）胶轮遍数")
    private java.lang.String timesrepeat;
	/**钢轮欠压率*/
	@Excel(name = " 钢轮欠压率", width = 15)
    @ApiModelProperty(value = " 钢轮欠压率")
    private java.lang.String steelrate;
	/**胶轮欠压率*/
	@Excel(name = " 胶轮欠压率", width = 15)
    @ApiModelProperty(value = " 胶轮欠压率")
    private java.lang.String rubberrate;
	/**projectid*/
	@Excel(name = "projectid", width = 15)
    @ApiModelProperty(value = "projectid")
    private java.lang.String projectid;
	/**sectionid*/
	@Excel(name = "sectionid", width = 15)
    @ApiModelProperty(value = "sectionid")
    private java.lang.String sectionid;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date baocundate;
}
