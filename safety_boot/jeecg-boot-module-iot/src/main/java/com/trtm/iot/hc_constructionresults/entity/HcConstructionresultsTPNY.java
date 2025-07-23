package com.trtm.iot.hc_constructionresults.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.hc_constructionresults_ny.entity.HcConstructionresultsNy;
import com.trtm.iot.hc_constructionresults_tp.entity.HcConstructionresultsTp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 施工成果
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Data
@TableName("hc_constructionresults")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hc_constructionresults对象", description="施工成果")
public class HcConstructionresultsTPNY{
    private static final long serialVersionUID = 1L;
	List<HcConstructionresultsTp> paver;
	List<HcConstructionresultsNy> rollerList;
    private String id;
	/**施工日期*/
    private Date date;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private Date begintime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    private Date endtime;
	/**结构层编号*/
	@Excel(name = " 结构层编号", width = 15)
    @ApiModelProperty(value = " 结构层编号")
    private java.lang.String layerindex;
	/**结构层*/
	@Excel(name = " 结构层", width = 15)
    @ApiModelProperty(value = " 结构层")
    private java.lang.String layername;
	/**开始桩号*/
	@Excel(name = "开始桩号", width = 15)
    @ApiModelProperty(value = "开始桩号")
    private java.lang.String startstation;
	/**结束桩号*/
	@Excel(name = " 结束桩号", width = 15)
    @ApiModelProperty(value = " 结束桩号")
    private java.lang.String endstation;
	/**左右幅，0：左幅，1：右幅*/
	@Excel(name = "左右幅，0：左幅，1：右幅", width = 15)
    @ApiModelProperty(value = "左右幅，0：左幅，1：右幅")
    private java.lang.String offset;
	/**摊铺里程(m)*/
	@Excel(name = "摊铺里程(m)", width = 15)
    @ApiModelProperty(value = "摊铺里程(m)")
    private java.lang.String workmileage;
	/**projectid*/
	@Excel(name = "projectid", width = 15)
    @ApiModelProperty(value = "projectid")
    private java.lang.String projectid;
	/**sectionid*/
	@Excel(name = "sectionid", width = 15)
    @ApiModelProperty(value = "sectionid")
    private java.lang.String sectionid;
    private java.lang.String totaltimesratio;
    /**工程名*/
    @Excel(name = "工程名", width = 15)
    @ApiModelProperty(value = "工程名")
    private java.lang.String pjname;
    /**标段名*/
    @Excel(name = "标段名", width = 15)
    @ApiModelProperty(value = "标段名")
    private java.lang.String sectionname;
}
