package com.trtm.iot.anquanfxgk.entity;

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
 * @Description: anquan_fxyh_checks
 * @Author: jeecg-boot
 * @Date:   2024-06-11
 * @Version: V1.0
 */
@Data
@TableName("anquan_fxyh_checks")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="anquan_fxyh_checks对象", description="anquan_fxyh_checks")
public class AnquanFxyhChecks implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
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
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String note;
	/**预留（关联表）*/
	@Excel(name = "预留（关联表）", width = 15)
    @ApiModelProperty(value = "预留（关联表）")
    private java.lang.String nodeid;
	/**隐患内容*/
	@Excel(name = "隐患内容", width = 15)
    @ApiModelProperty(value = "隐患内容")
    private java.lang.String yhContent;
	/**隐患级别，填写一般隐患或重大隐患；*/
	@Excel(name = "隐患级别，填写一般隐患或重大隐患；", width = 15)
    @ApiModelProperty(value = "隐患级别，填写一般隐患或重大隐患；")
    private java.lang.String yhType;
	/**整改时间（立即整改）整改时间:立即整改是指马上能整改的隐患；限期整改指不能立即整改，而需要一定时间来完成整改的隐患。*/
	@Excel(name = "整改时间（立即整改）整改时间:立即整改是指马上能整改的隐患；限期整改指不能立即整改，而需要一定时间来完成整改的隐患。", width = 15)
    @ApiModelProperty(value = "整改时间（立即整改）整改时间:立即整改是指马上能整改的隐患；限期整改指不能立即整改，而需要一定时间来完成整改的隐患。")
    private java.lang.String zhenggaishijian;
	/**期限整改*/
	@Excel(name = "期限整改", width = 15)
    @ApiModelProperty(value = "期限整改")
    private java.lang.String zhenggaiqixian;
	/**整改措施*/
	@Excel(name = "整改措施", width = 15)
    @ApiModelProperty(value = "整改措施")
    private java.lang.String zhenggaicuoshi;
	/**责任人*/
	@Excel(name = "责任人", width = 15)
    @ApiModelProperty(value = "责任人")
    private java.lang.String responPerson;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**排查时间*/
	@Excel(name = "排查时间", width = 15)
    @ApiModelProperty(value = "排查时间")
    private java.lang.String paichashijian;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
}
