package com.trtm.iot.yajiangss.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 压浆过程表
 * @Author: jeecg-boot
 * @Date:   2021-09-06
 * @Version: V1.0
 */
@Data
@TableName("tr_yajiang_s_s")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tr_yajiang_s_s对象", description="压浆过程表")
public class TrYajiangSS implements Serializable {
    private static final long serialVersionUID = 1L;

	/**全球唯一码，主键*/
	@Excel(name = "全球唯一码，主键", width = 15)
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "全球唯一码，主键")
    private java.lang.String sid;
	/**孔号，和主表kongdao相同*/
	@Excel(name = "孔号，和主表kongdao相同", width = 15)
    @ApiModelProperty(value = "孔号，和主表kongdao相同")
    private java.lang.String holeid;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String shebeibianh;
	/**记录时间*/
	@Excel(name = "记录时间", width = 15)
    @ApiModelProperty(value = "记录时间")
    private java.lang.String jlsj;
	/**持压时间*/
	@Excel(name = "持压时间", width = 15)
    @ApiModelProperty(value = "持压时间")
    private java.lang.String cysj;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.String zt;
	/**进浆压力MPa*/
	@Excel(name = "进浆压力MPa", width = 15)
    @ApiModelProperty(value = "进浆压力MPa")
    private java.lang.String jjyl;
	/**返浆压力MPa*/
	@Excel(name = "返浆压力MPa", width = 15)
    @ApiModelProperty(value = "返浆压力MPa")
    private java.lang.String fjyl;
	/**进浆量L*/
	@Excel(name = "进浆量L", width = 15)
    @ApiModelProperty(value = "进浆量L")
    private java.lang.String jjl;
	/**返浆量L*/
	@Excel(name = "返浆量L", width = 15)
    @ApiModelProperty(value = "返浆量L")
    private java.lang.String fjl;
    /**环境温度*/
    @Excel(name = "环境温度", width = 15)
    @ApiModelProperty(value = "环境温度")
    private java.lang.String hjwd;
    /**水温度*/
    @Excel(name = "水温度", width = 15)
    @ApiModelProperty(value = "水温度")
    private java.lang.String swd;
    /**粉料温度*/
    @Excel(name = "粉料温度", width = 15)
    @ApiModelProperty(value = "粉料温度")
    private java.lang.String flwd;
    /**浆液温度*/
    @Excel(name = "浆液温度", width = 15)
    @ApiModelProperty(value = "浆液温度")
    private java.lang.String jywd;
}
