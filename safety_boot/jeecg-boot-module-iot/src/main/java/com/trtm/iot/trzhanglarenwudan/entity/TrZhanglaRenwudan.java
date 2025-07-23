package com.trtm.iot.trzhanglarenwudan.entity;

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
 * @Description: 张拉任务单
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
@Data
@TableName("tr_zhangla_renwudan")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tr_zhangla_renwudan对象", description="张拉任务单")
public class TrZhanglaRenwudan implements Serializable {
    private static final long serialVersionUID = 1L;

	/**自增主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "自增主键")
    private java.lang.Integer id;
	/**任务单号*/
	@Excel(name = "任务单号", width = 15)
    @ApiModelProperty(value = "任务单号")
    private java.lang.String uuid;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private java.lang.String projectname;
	/**梁场*/
	@Excel(name = "梁场", width = 15)
    @ApiModelProperty(value = "梁场")
    private java.lang.String girderplace;
	/**构件名称（梁名称）*/
	@Excel(name = "构件名称（梁名称）", width = 15)
    @ApiModelProperty(value = "构件名称（梁名称）")
    private java.lang.String component;
	/**施工部位*/
	@Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    private java.lang.String sgbwuuid;
	/**施工部位名称*/
	@Excel(name = "施工部位名称", width = 15)
    @ApiModelProperty(value = "施工部位名称")
    private java.lang.String sgbwname;
	/**台座*/
	@Excel(name = "台座", width = 15)
    @ApiModelProperty(value = "台座")
    private java.lang.String pedestal;
	/**一次张拉任务状态码：0：未使用  1：已使用*/
	@Excel(name = "一次张拉任务状态码：0：未使用  1：已使用", width = 15)
    @ApiModelProperty(value = "一次张拉任务状态码：0：未使用  1：已使用")
    private java.lang.String status;
	/**张拉日期*/
	@Excel(name = "张拉日期", width = 15)
    @ApiModelProperty(value = "张拉日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date zldate;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
	/**修改时间*/
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private  java.util.Date updateTime;
	/**组织机构*/
	@Excel(name = "组织机构", width = 15)
    @ApiModelProperty(value = "组织机构")
    private java.lang.String departid;
	/**设计张拉力*/
	@Excel(name = "设计张拉力", width = 15)
    @ApiModelProperty(value = "设计张拉力")
    private java.lang.String sjzll;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String createBy;
	/**sysOrgCode*/
    @ApiModelProperty(value = "sysOrgCode")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**二次张拉日期*/
	@Excel(name = "二次张拉日期", width = 15)
    @ApiModelProperty(value = "二次张拉日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private  java.util.Date zldate2;
	/**二次张拉力*/
	@Excel(name = "二次张拉力", width = 15)
    @ApiModelProperty(value = "二次张拉力")
    private java.lang.String sjzll2;
	/**二次张拉任务状态码：0：未使用  1：已使用*/
	@Excel(name = "二次张拉任务状态码：0：未使用  1：已使用", width = 15)
    @ApiModelProperty(value = "二次张拉任务状态码：0：未使用  1：已使用")
    private java.lang.String status2;
	/**departname*/
	@Excel(name = "departname", width = 15)
    @ApiModelProperty(value = "departname")
    private java.lang.String departname;
	/**shebeibianh*/
	@Excel(name = "shebeibianh", width = 15)
    @ApiModelProperty(value = "shebeibianh")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeibianh;
	/**预应力钢材种类规格*/
	@Excel(name = "预应力钢材种类规格", width = 15)
    @ApiModelProperty(value = "预应力钢材种类规格")
    private java.lang.String yylgczlgg;
	/**预应力钢材产地品牌*/
	@Excel(name = "预应力钢材产地品牌", width = 15)
    @ApiModelProperty(value = "预应力钢材产地品牌")
    private java.lang.String yylgccdpp;
	/**预应力钢材检验单编号*/
	@Excel(name = "预应力钢材检验单编号", width = 15)
    @ApiModelProperty(value = "预应力钢材检验单编号")
    private java.lang.String yylgcbh;
	/**锚夹具种类规格*/
	@Excel(name = "锚夹具种类规格", width = 15)
    @ApiModelProperty(value = "锚夹具种类规格")
    private java.lang.String mjjzlgg;
	/**锚夹具产地品牌*/
	@Excel(name = "锚夹具产地品牌", width = 15)
    @ApiModelProperty(value = "锚夹具产地品牌")
    private java.lang.String mjjcdpp;
	/**锚夹具检验单编号*/
	@Excel(name = "锚夹具检验单编号", width = 15)
    @ApiModelProperty(value = "锚夹具检验单编号")
    private java.lang.String mjjbh;
	/**锚垫板种类规格*/
	@Excel(name = "锚垫板种类规格", width = 15)
    @ApiModelProperty(value = "锚垫板种类规格")
    private java.lang.String mdbzlgg;
	/**锚垫板产地品牌*/
	@Excel(name = "锚垫板产地品牌", width = 15)
    @ApiModelProperty(value = "锚垫板产地品牌")
    private java.lang.String mdbcdpp;
	/**锚垫板检验单编号*/
	@Excel(name = "锚垫板检验单编号", width = 15)
    @ApiModelProperty(value = "锚垫板检验单编号")
    private java.lang.String mdbbh;
	/**梁板理论上拱度*/
	@Excel(name = "梁板理论上拱度", width = 15)
    @ApiModelProperty(value = "梁板理论上拱度")
    private java.lang.String lbgd;
	/**混凝土的设计强度*/
	@Excel(name = "混凝土的设计强度", width = 15)
    @Dict(dicCode = "betlev")
    @ApiModelProperty(value = "混凝土的设计强度")
    private java.lang.String hntsjqd;
	/**两端千斤顶的张拉力误差指标*/
	@Excel(name = "两端千斤顶的张拉力误差指标", width = 15)
    @ApiModelProperty(value = "两端千斤顶的张拉力误差指标")
    private java.lang.String zllwc;

	private java.lang.Integer wcstatus;
	private java.lang.String treeid;//施工部位节点id
    private java.lang.Integer isruicang;//瑞苍内网数据是否推送 0 未推送 1 已推送
    /**制梁任务单编号*/
    @Excel(name = "制梁任务单编号", width = 15)
    @ApiModelProperty(value = "制梁任务单编号")
    private java.lang.String code;
    /**梁型(0：箱型梁；1：T型梁；2：槽形梁；3：空心板梁 ；10：其他)*/
    @Excel(name = "梁型(0：箱型梁；1：T型梁；2：槽形梁；3：空心板梁 ；10：其他)", width = 15)
    @ApiModelProperty(value = "梁型(0：箱型梁；1：T型梁；2：槽形梁；3：空心板梁 ；10：其他)")
    @Dict(dicCode = "lx")
    private java.lang.Integer lx;
    /**孔道数*/
    @Excel(name = "孔道数", width = 15)
    @ApiModelProperty(value = "孔道数")
    private java.lang.String kds;

    private String gxwbs;
}
