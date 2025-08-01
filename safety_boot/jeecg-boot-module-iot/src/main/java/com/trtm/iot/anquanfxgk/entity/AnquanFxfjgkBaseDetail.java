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
 * @Description: anquan_fxfjgk_base_detail
 * @Author: jeecg-boot
 * @Date:   2024-06-11
 * @Version: V1.0
 */
@Data
@TableName("anquan_fxfjgk_base_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="anquan_fxfjgk_base_detail对象", description="anquan_fxfjgk_base_detail")
public class AnquanFxfjgkBaseDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
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
	/**主表guid*/
	@Excel(name = "主表guid", width = 15)
    @ApiModelProperty(value = "主表guid")
    private java.lang.String baseGuid;
	/**设备/设施/物料/作业活动*/
	@Excel(name = "设备/设施/物料/作业活动", width = 15)
    @ApiModelProperty(value = "设备/设施/物料/作业活动")
    private java.lang.String soCode;
	/**危险源*/
	@Excel(name = "危险源", width = 15)
    @ApiModelProperty(value = "危险源")
    private java.lang.String soWeixian;
	/**危险源类别*/
	@Excel(name = "危险源类别", width = 15)
    @ApiModelProperty(value = "危险源类别")
    private java.lang.String soType;
	/**可能产生的影响及后果*/
	@Excel(name = "可能产生的影响及后果", width = 15)
    @ApiModelProperty(value = "可能产生的影响及后果")
    private java.lang.String soYingxiang;
	/**概率*/
	@Excel(name = "概率", width = 15)
    @ApiModelProperty(value = "概率")
    private java.lang.String riskGailv;
	/**后果严重度*/
	@Excel(name = "后果严重度", width = 15)
    @ApiModelProperty(value = "后果严重度")
    private java.lang.String riskReslut;
	/**风险程度*/
	@Excel(name = "风险程度", width = 15)
    @ApiModelProperty(value = "风险程度")
    private java.lang.String riskDegree;
	/**风险等级*/
	@Excel(name = "风险等级", width = 15)
    @ApiModelProperty(value = "风险等级")
    private java.lang.String riskLevel;
	/**技术措施*/
	@Excel(name = "技术措施", width = 15)
    @ApiModelProperty(value = "技术措施")
    private java.lang.String measureJishu;
	/**管理措施*/
	@Excel(name = "管理措施", width = 15)
    @ApiModelProperty(value = "管理措施")
    private java.lang.String measureGuanli;
	/**教育措施*/
	@Excel(name = "教育措施", width = 15)
    @ApiModelProperty(value = "教育措施")
    private java.lang.String measureJiaoyu;
	/**个人防护*/
	@Excel(name = "个人防护", width = 15)
    @ApiModelProperty(value = "个人防护")
    private java.lang.String measureFanghu;
	/**应急措施*/
	@Excel(name = "应急措施", width = 15)
    @ApiModelProperty(value = "应急措施")
    private java.lang.String measureYingji;
	/**管控措施*/
	@Excel(name = "管控措施", width = 15)
    @ApiModelProperty(value = "管控措施")
    private java.lang.String measureGuankong;
	/**管理单位*/
	@Excel(name = "管理单位", width = 15)
    @ApiModelProperty(value = "管理单位")
    private java.lang.String management;
	/**唯一键*/
	@Excel(name = "唯一键", width = 15)
    @ApiModelProperty(value = "唯一键")
    private java.lang.String baseid;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
}
