package com.trtm.iot.kongdaoyaj.vo;

import java.util.List;

import com.trtm.iot.kongdaoyaj.entity.Kongdaoyajs;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 孔道灌浆主表
 * @Author: jeecg-boot
 * @Date:   2024-06-25
 * @Version: V1.0
 */
@Data
@ApiModel(value="kongdaoyajPage对象", description="孔道灌浆主表")
public class KongdaoyajPage {

	/**id*/
	@ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**json文件唯一标识*/
	@Excel(name = "json文件唯一标识", width = 15)
	@ApiModelProperty(value = "json文件唯一标识")
    private java.lang.String uuid;
	/**项目所在省份*/
	@Excel(name = "项目所在省份", width = 15)
	@ApiModelProperty(value = "项目所在省份")
    private java.lang.String province;
	/**项目所属行业*/
	@Excel(name = "项目所属行业", width = 15)
	@ApiModelProperty(value = "项目所属行业")
    private java.lang.String detectindustry;
	/**检测性质*/
	@Excel(name = "检测性质", width = 15)
	@ApiModelProperty(value = "检测性质")
    private java.lang.String detectnaturenum;
	/**检测类型*/
	@Excel(name = "检测类型", width = 15)
	@ApiModelProperty(value = "检测类型")
    private java.lang.String detecttype;
	/**检测单位*/
	@Excel(name = "检测单位", width = 15)
	@ApiModelProperty(value = "检测单位")
    private java.lang.String detectunit;
	/**设备厂商*/
	@Excel(name = "设备厂商", width = 15)
	@ApiModelProperty(value = "设备厂商")
    private java.lang.String devicecompany;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private java.lang.String remarks;
	/**工程地点*/
	@Excel(name = "工程地点", width = 15)
	@ApiModelProperty(value = "工程地点")
    private java.lang.String projplace;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
	@ApiModelProperty(value = "工程名称")
    private java.lang.String workname;
	/**检测人员*/
	@Excel(name = "检测人员", width = 15)
	@ApiModelProperty(value = "检测人员")
    private java.lang.String detectperson;
	/**保留字段*/
	@Excel(name = "保留字段", width = 15)
	@ApiModelProperty(value = "保留字段")
    private java.lang.String detectparamno;
	/**保留字段*/
	@Excel(name = "保留字段", width = 15)
	@ApiModelProperty(value = "保留字段")
    private java.lang.String programname;
	/**检测大类*/
	@Excel(name = "检测大类", width = 15)
	@ApiModelProperty(value = "检测大类")
    private java.lang.String detectparam;
	/**检测小类*/
	@Excel(name = "检测小类", width = 15)
	@ApiModelProperty(value = "检测小类")
    private java.lang.String detectclass;
	/**施工单位*/
	@Excel(name = "施工单位", width = 15)
	@ApiModelProperty(value = "施工单位")
    private java.lang.String consunit;
	/**监理单位*/
	@Excel(name = "监理单位", width = 15)
	@ApiModelProperty(value = "监理单位")
    private java.lang.String supervisionunit;
	/**建设单位*/
	@Excel(name = "建设单位", width = 15)
	@ApiModelProperty(value = "建设单位")
    private java.lang.String employer;
	/**试验编号*/
	@Excel(name = "试验编号", width = 15)
	@ApiModelProperty(value = "试验编号")
    private java.lang.String detectno;
	/**保留字段*/
	@Excel(name = "保留字段", width = 15)
	@ApiModelProperty(value = "保留字段")
    private java.lang.String pushprojectname;

	@ExcelCollection(name="孔道灌浆子表")
	@ApiModelProperty(value = "孔道灌浆子表")
	private List<Kongdaoyajs> DetectData;
	//波形图文件 文件格式
	@Excel(name = "波形图文件 文件格式", width = 15)
	@ApiModelProperty(value = "波形图文件 文件格式")
	private java.lang.String waveformfiletype;


}
