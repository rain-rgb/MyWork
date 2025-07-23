package com.trtm.iot.anquanfxgk.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: anquan_fxaqjc_info
 * @Author: jeecg-boot
 * @Date:   2024-06-11
 * @Version: V1.0
 */
@Data
@TableName("anquan_fxaqjc_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="anquan_fxaqjc_info对象", description="anquan_fxaqjc_info")
public class AnquanFxaqjcInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**年度如 2024年度研判*/
	@Excel(name = "年度如 2024年度研判", width = 15)
    @ApiModelProperty(value = "年度如 2024年度研判")
    private java.lang.String niandu;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projectname;
	/**所属业务部门*/
	@Excel(name = "所属业务部门", width = 15)
    @ApiModelProperty(value = "所属业务部门")
    private java.lang.String departname;
	/**guid*/
	@Excel(name = "guid", width = 15)
    @ApiModelProperty(value = "guid")
    private java.lang.String guid;
	/**说明 备注一栏根据对问题进行归类，如工程质量、施工安全、文明施工、内业资料、综合管理等*/
	@Excel(name = "说明 备注一栏根据对问题进行归类，如工程质量、施工安全、文明施工、内业资料、综合管理等", width = 15)
    @ApiModelProperty(value = "说明 备注一栏根据对问题进行归类，如工程质量、施工安全、文明施工、内业资料、综合管理等")
    private java.lang.String note;
	/**整改单位（责任单位）*/
	@Excel(name = "整改单位（责任单位）", width = 15)
    @ApiModelProperty(value = "整改单位（责任单位）")
    private java.lang.String zhenggaiUnit;
	/**整改责任人【暂不使用】*/
	@Excel(name = "整改责任人【暂不使用】", width = 15)
    @ApiModelProperty(value = "整改责任人【暂不使用】")
    private java.lang.String zhengaiPeople;
	/**整改情况 整改情况应填写“已整改”或“整改中”，不得采用其他描述*/
	@Excel(name = "整改情况 整改情况应填写“已整改”或“整改中”，不得采用其他描述", width = 15)
    @ApiModelProperty(value = "整改情况 整改情况应填写“已整改”或“整改中”，不得采用其他描述")
    private java.lang.String zhenggaiInfo;
	/**工点名称*/
	@Excel(name = "工点名称", width = 15)
    @ApiModelProperty(value = "工点名称")
    private java.lang.String manageLocation;
    /**工点名称id绑定风险源*/
//    @Excel(name = "工点名称", width = 15)
    @ApiModelProperty(value = "工点名称id绑定风险源")
    private java.lang.String manageLocationid;
	/**验收单位*/
	@Excel(name = "验收单位", width = 15)
    @ApiModelProperty(value = "验收单位")
    private java.lang.String yanshouUnit;
	/**整改期限年月日（完成期限）*/
	@Excel(name = "整改期限年月日（完成期限）", width = 15)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "整改期限年月日（完成期限）")
    private java.util.Date zhenggaiTime;
	/**发现问题内容 问题简要描述应具体扼要，原则上一行一序号且只描述1个问题*/
	@Excel(name = "发现问题内容 问题简要描述应具体扼要，原则上一行一序号且只描述1个问题", width = 15)
    @ApiModelProperty(value = "发现问题内容 问题简要描述应具体扼要，原则上一行一序号且只描述1个问题")
    private java.lang.String problems;
	/**整改措施和要求*/
	@Excel(name = "整改措施和要求", width = 15)
    @ApiModelProperty(value = "整改措施和要求")
    private java.lang.String zhenggaiRequire;
	/**检查人*/
	@Excel(name = "检查人", width = 15)
    @ApiModelProperty(value = "检查人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String checkPeople;
	/**检查年月日*/
	@Excel(name = "检查年月日", width = 15)
    @ApiModelProperty(value = "检查年月日")
    private java.lang.String checkTime;
	/**问题类别： ⼀般问题；⼀般隐患；重⼤隐患*/
	@Excel(name = "问题类别： ⼀般问题；⼀般隐患；重⼤隐患", width = 15)
    @ApiModelProperty(value = "问题类别： ⼀般问题；⼀般隐患；重⼤隐患")
    private java.lang.String problemType;
	/**治理要求*/
	@Excel(name = "治理要求", width = 15)
    @ApiModelProperty(value = "治理要求")
    private java.lang.String procedure1Info;
	/**procedure1Reslut*/
	@Excel(name = "procedure1Reslut", width = 15)
    @ApiModelProperty(value = "procedure1Reslut")
    private java.lang.String procedure1Reslut;
	/**签发人*/
	@Excel(name = "签发人", width = 15)
    @ApiModelProperty(value = "签发人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String procedure1People;
	/**检查年月日*/
	@Excel(name = "检查年月日", width = 15)
    @ApiModelProperty(value = "检查年月日")
    private java.lang.String procedure1Time;
	/**procedure2Reslut*/
	@Excel(name = "procedure2Reslut", width = 15)
    @ApiModelProperty(value = "procedure2Reslut")
    private java.lang.String procedure2Reslut;
	/**接收人（整改责任人）*/
	@Excel(name = "接收人（整改责任人）", width = 15)
    @ApiModelProperty(value = "接收人（整改责任人）")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String procedure2People;
	/**接收时间年月日*/
	@Excel(name = "接收时间年月日", width = 15)
    @ApiModelProperty(value = "接收时间年月日")
    private java.lang.String procedure2Time;
	/**编号*/
	@Excel(name = "编号", width = 15)
    @ApiModelProperty(value = "编号")
    private java.lang.String zlsNo;
	/**是否销号应填写“是”或“否”，不得采用其他描述。*/
	@Excel(name = "是否销号应填写“是”或“否”，不得采用其他描述。", width = 15)
    @ApiModelProperty(value = "是否销号应填写“是”或“否”，不得采用其他描述。")
    private java.lang.Integer isover;
	/**隐患类别*/
	@Excel(name = "隐患类别", width = 15)
    @ApiModelProperty(value = "隐患类别")
    private java.lang.String yhlb;
    /**procedure2Reslut*/
    @Excel(name = "procedure2Reslut", width = 15)
    @ApiModelProperty(value = "procedure2Reslut")
    private java.lang.String procedure3Reslut;
    /**接收人（整改责任人）*/
    @Excel(name = "销号人", width = 15)
    @ApiModelProperty(value = "销号人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String procedure3People;
    /**接收时间年月日*/
    @Excel(name = "销号时间年月日", width = 15)
    @ApiModelProperty(value = "销号时间年月日")
    private java.lang.String procedure3Time;

    private java.lang.Integer handlestatus;
    private java.lang.String yhfile;
    private java.lang.String zgfile;

    /**风险源点*/
    @TableField(exist = false)
    @ApiModelProperty(value = "风险源点")
    private AnquanFxfjgkBase anquanFxfjgkBase;

    /**部位*/
    @Excel(name = "部位", width = 15)
    @ApiModelProperty(value = "部位")
    private java.lang.String part;

    /**检查人照片*/
    @Excel(name = "检查人照片", width = 15)
    @ApiModelProperty(value = "检查人照片")
    private java.lang.String checkImg;
    /**检查人照片*/
    @TableField(exist = false)
    @ApiModelProperty(value = "检查人照片Bae64")
    private java.lang.String isCheckImg;


}
