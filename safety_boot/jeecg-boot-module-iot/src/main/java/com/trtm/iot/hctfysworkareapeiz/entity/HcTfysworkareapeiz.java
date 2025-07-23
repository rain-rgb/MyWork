package com.trtm.iot.hctfysworkareapeiz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 土方压实配置表
 * @Author: jeecg-boot
 * @Date:   2024-04-09
 * @Version: V1.0
 */
@Data
@TableName("hc_tfysworkareapeiz")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hc_tfysworkareapeiz对象", description="土方压实配置表")
public class HcTfysworkareapeiz implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**项目*/
	@Excel(name = "项目", width = 15)
    @ApiModelProperty(value = "项目")
    @Dict(dictTable = "hc_project", dicText = "pjName", dicCode = "pjId")
    private java.lang.String projectid;
	/**标段*/
	@Excel(name = "标段", width = 15)
    @ApiModelProperty(value = "标段")
    @Dict(dictTable = "hc_section", dicText = "sectionName", dicCode = "sectionId")
    private java.lang.String sectionid;
	/**开始桩号 面型工程没有桩号*/
	@Excel(name = "开始桩号 面型工程没有桩号", width = 15)
    @ApiModelProperty(value = "开始桩号 面型工程没有桩号")
    private java.lang.String startstation;
	/**结束桩号 面型工程没有桩号*/
	@Excel(name = "结束桩号 面型工程没有桩号", width = 15)
    @ApiModelProperty(value = "结束桩号 面型工程没有桩号")
    private java.lang.String endstation;
	/**碾压层数*/
	@Excel(name = "碾压层数", width = 15)
    @ApiModelProperty(value = "碾压层数")
    private java.lang.String layername;
	/**施工高程*/
	@Excel(name = "施工高程", width = 15)
    @ApiModelProperty(value = "施工高程")
    private java.lang.String avgheights;
	/**设计宽度*/
	@Excel(name = "设计宽度", width = 15)
    @ApiModelProperty(value = "设计宽度")
    private java.lang.String thickavgs;
	/**施工日期*/
	@Excel(name = "施工日期", width = 15)
    @ApiModelProperty(value = "施工日期")
    private java.lang.String starttimes;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;

    private java.lang.String mileageid;
    private java.lang.String mileage;
    private java.lang.String xiangmu;
    private java.lang.String biaoduan;
    private java.lang.String danwei;
    private java.lang.Integer ismt;
    private java.lang.String treeid;
    private java.lang.String gxwbs;
    @Dict(dictTable = "bhz_phone", dicText = "names", dicCode = "uid")
    private java.lang.String construction;
    @Dict(dictTable = "bhz_phone", dicText = "names", dicCode = "uid")
    private java.lang.String supervisory;
}
