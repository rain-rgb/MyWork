package com.trtm.iot.wzgbStatistics.entity;

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
 * @Description: 原材料过磅进出场数据统计表
 * @Author: jeecg-boot
 * @Date:   2022-09-19
 * @Version: V1.0
 */
@Data
@TableName("wzgb_statistics")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wzgb_statistics对象", description="原材料过磅进出场数据统计表")
public class WzgbStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

	/**进场过磅id*/
	@TableId(type = IdType.AUTO)
    @Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "进场过磅id")
    private java.lang.Integer id;
	/**设备编号*/
//	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String shebeibianhao;
	/**供应商*/
	@Excel(name = "供货单位", width = 15)
    @ApiModelProperty(value = "供应商")
    private java.lang.String gongyingshangdanweibianma;
	/**收货单位*/
	@Excel(name = "收货单位", width = 15)
    @ApiModelProperty(value = "收货单位")
    private java.lang.String liaocangid;
    /**材料单号*/
    @Excel(name = "货物名称", width = 15)
    @ApiModelProperty(value = "材料单号")
    private java.lang.String cailiaono;
	/**规格类型*/
	@Excel(name = "规格类型", width = 15)
    @ApiModelProperty(value = "规格类型")
    private java.lang.String guigexinghao;
	/**车数*/
	@Excel(name = "车数", width = 15)
    @ApiModelProperty(value = "车数")
    private java.lang.Integer carsNumber;
    /**统计时间，以天为单位*/
//	@Excel(name = "统计时间，以天为单位", width = 15, format = "yyyy-MM-dd")
    @Excel(name = "进出场日期", width = 15)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "统计时间，以天为单位")
    private String statisticsTime;
    /**毛重(吨)*/
    @Excel(name = "毛重(吨)", width = 15)
    @ApiModelProperty(value = "毛重(吨)")
    private java.lang.String maozhongt;
    /**皮重(吨)*/
    @Excel(name = "皮重(吨)", width = 15)
    @ApiModelProperty(value = "皮重(吨)")
    private java.lang.String pizhongt;
    /**净重(吨)*/
    @Excel(name = "净重(吨)", width = 15)
    @ApiModelProperty(value = "净重(吨)")
    private java.lang.String jingzhongt;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;

}
