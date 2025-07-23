package com.trtm.iot.pmscomwbs.entity;

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
 * @Description: 中交象山疏港高速(原海德 wbs数据)
 * @Author: jeecg-boot
 * @Date:   2021-07-16
 * @Version: V1.0
 */
@Data
@TableName("pms_com_wbs")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pms_com_wbs对象", description="中交象山疏港高速(原海德 wbs数据)")
public class PmsComWbs implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**sectid*/
	@Excel(name = "sectid", width = 15)
    @ApiModelProperty(value = "sectid")
    private java.lang.String sectid;
	/**wbsIsbn*/
	@Excel(name = "wbsIsbn", width = 15)
    @ApiModelProperty(value = "wbsIsbn")
    private java.lang.String wbsIsbn;
	/**wbsParent*/
	@Excel(name = "wbsParent", width = 15)
    @ApiModelProperty(value = "wbsParent")
    private java.lang.String wbsParent;
	/**wbsName*/
	@Excel(name = "wbsName", width = 15)
    @ApiModelProperty(value = "wbsName")
    private java.lang.String wbsName;
	/**wbsMoney*/
	@Excel(name = "wbsMoney", width = 15)
    @ApiModelProperty(value = "wbsMoney")
    private java.lang.Integer wbsMoney;
	/**wbsLevel*/
	@Excel(name = "wbsLevel", width = 15)
    @ApiModelProperty(value = "wbsLevel")
    private java.lang.Integer wbsLevel;
	/**hasChild*/
	@Excel(name = "hasChild", width = 15)
    @ApiModelProperty(value = "hasChild")
    private java.lang.Integer hasChild;
	/**deleFlag*/
	@Excel(name = "deleFlag", width = 15)
    @ApiModelProperty(value = "deleFlag")
    private java.lang.Integer deleFlag;
	/**wbsUnit*/
	@Excel(name = "wbsUnit", width = 15)
    @ApiModelProperty(value = "wbsUnit")
    private java.lang.String wbsUnit;
	/**wbsAmount*/
	@Excel(name = "wbsAmount", width = 15)
    @ApiModelProperty(value = "wbsAmount")
    private java.lang.String wbsAmount;
	/**wbsAmount2*/
	@Excel(name = "wbsAmount2", width = 15)
    @ApiModelProperty(value = "wbsAmount2")
    private java.lang.String wbsAmount2;
	/**wbsZhBegi*/
	@Excel(name = "wbsZhBegi", width = 15)
    @ApiModelProperty(value = "wbsZhBegi")
    private java.lang.String wbsZhBegi;
	/**wbsZhEnd*/
	@Excel(name = "wbsZhEnd", width = 15)
    @ApiModelProperty(value = "wbsZhEnd")
    private java.lang.String wbsZhEnd;
	/**wbsMapno*/
	@Excel(name = "wbsMapno", width = 15)
    @ApiModelProperty(value = "wbsMapno")
    private java.lang.String wbsMapno;
	/**wbsType1*/
	@Excel(name = "wbsType1", width = 15)
    @ApiModelProperty(value = "wbsType1")
    private java.lang.String wbsType1;
	/**wbsType2*/
	@Excel(name = "wbsType2", width = 15)
    @ApiModelProperty(value = "wbsType2")
    private java.lang.String wbsType2;
	/**wbsType3*/
	@Excel(name = "wbsType3", width = 15)
    @ApiModelProperty(value = "wbsType3")
    private java.lang.String wbsType3;
	/**sortorder*/
	@Excel(name = "sortorder", width = 15)
    @ApiModelProperty(value = "sortorder")
    private java.lang.Integer sortorder;
	/**1 代表已经更新 0 代表未更新*/
	@Excel(name = "1 代表已经更新 0 代表未更新", width = 15)
    @ApiModelProperty(value = "1 代表已经更新 0 代表未更新")
    private java.lang.Integer status;
	/**ids*/
	@Excel(name = "ids", width = 15)
    @ApiModelProperty(value = "ids")
    private java.lang.Integer ids;
}
