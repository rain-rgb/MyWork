package com.trtm.iot.hc_section.entity;

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
 * @Description: 华测获取标段
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
@Data
@TableName("hc_section")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hc_section对象", description="华测获取标段")
public class HcSection implements Serializable {
    private static final long serialVersionUID = 1L;

	/**唯一id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "唯一id")
    private java.lang.Integer id;
	/**标段ID*/
	@Excel(name = "标段ID", width = 15)
    @ApiModelProperty(value = "标段ID")
    private java.lang.String sectionid;
	/**标段名*/
	@Excel(name = "标段名", width = 15)
    @ApiModelProperty(value = "标段名")
    private java.lang.String sectionname;
	/**左右幅，0是其他，1是双幅，2是单幅*/
	@Excel(name = "左右幅，0是其他，1是双幅，2是单幅", width = 15)
    @ApiModelProperty(value = "左右幅，0是其他，1是双幅，2是单幅")
    private java.lang.String offsettype;
	/**设置单遍复遍，0为单遍，1为复遍*/
	@Excel(name = "设置单遍复遍，0为单遍，1为复遍", width = 15)
    @ApiModelProperty(value = "设置单遍复遍，0为单遍，1为复遍")
    private java.lang.String timestype;
	/**分层模式：0不分层，2路基块自动分层，5路面信息化自动分层，6土方压实自动分层，7土方压实手动分层(压路机手点击开始结束)*/
	@Excel(name = "分层模式：0不分层，2路基块自动分层，5路面信息化自动分层，6土方压实自动分层，7土方压实手动分层(压路机手点击开始结束)", width = 15)
    @ApiModelProperty(value = "分层模式：0不分层，2路基块自动分层，5路面信息化自动分层，6土方压实自动分层，7土方压实手动分层(压路机手点击开始结束)")
    private java.lang.String stratifymode;
	/**标段边界北东数据，格式：北坐标,东坐标 北坐标,东坐标*/
	@Excel(name = "标段边界北东数据，格式：北坐标,东坐标 北坐标,东坐标", width = 15)
    @ApiModelProperty(value = "标段边界北东数据，格式：北坐标,东坐标 北坐标,东坐标")
    private java.lang.String sectionboundary;
	/**标段边界经纬度数据，格式：[[经度,纬度],[经度,纬度]]*/
	@Excel(name = "标段边界经纬度数据，格式：[[经度,纬度],[经度,纬度]]", width = 15)
    @ApiModelProperty(value = "标段边界经纬度数据，格式：[[经度,纬度],[经度,纬度]]")
    private java.lang.String sectionwgsboundary;
	/**开始施工日期*/
	@Excel(name = "开始施工日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始施工日期")
    private java.util.Date sectionstartdate;
	/**监理单位id*/
	@Excel(name = "监理单位id", width = 15)
    @ApiModelProperty(value = "监理单位id")
    private java.lang.String sectionsupcompanyid;
	/**监理单位名称*/
	@Excel(name = "监理单位名称", width = 15)
    @ApiModelProperty(value = "监理单位名称")
    private java.lang.String sectionsupcompanyname;
	/**施工单位id*/
	@Excel(name = "施工单位id", width = 15)
    @ApiModelProperty(value = "施工单位id")
    private java.lang.String sectionbuildercompanyid;
	/**施工单位名称*/
	@Excel(name = "施工单位名称", width = 15)
    @ApiModelProperty(value = "施工单位名称")
    private java.lang.String sectionbuildercompanyname;
	/**对应的标段编码*/
	@Excel(name = "对应的标段编码", width = 15)
    @ApiModelProperty(value = "对应的标段编码")
    private java.lang.String orgcode;
    private java.lang.String pjid;
}
