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
 * @Description: anquan_fxfjgk_rwd
 * @Author: jeecg-boot
 * @Date:   2024-06-11
 * @Version: V1.0
 */
@Data
@TableName("anquan_fxfjgk_rwd")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="anquan_fxfjgk_rwd对象", description="anquan_fxfjgk_rwd")
public class AnquanFxfjgkRwd implements Serializable {
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
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String note;
	/**风险点/工点名称 工点名称应具体，例如某桥、某隧道，如为线路上某处工点应标明线路里程*/
	@Excel(name = "风险点/工点名称 工点名称应具体，例如某桥、某隧道，如为线路上某处工点应标明线路里程", width = 15)
    @ApiModelProperty(value = "风险点/工点名称 工点名称应具体，例如某桥、某隧道，如为线路上某处工点应标明线路里程")
    private java.lang.String riskPoint;
	/**风险主要内容/施工主要内容*/
	@Excel(name = "风险主要内容/施工主要内容", width = 15)
    @ApiModelProperty(value = "风险主要内容/施工主要内容")
    private java.lang.String riskContent;
	/**风险等级*/
	@Excel(name = "风险等级", width = 15)
    @ApiModelProperty(value = "风险等级")
    private java.lang.String riskLevel;
	/**风险因素 风险因素描述应具体扼要，按1.……；2.……；3.……逐个列出；对策措施中对应风险因素逐条明确应对措施；*/
	@Excel(name = "风险因素 风险因素描述应具体扼要，按1.……；2.……；3.……逐个列出；对策措施中对应风险因素逐条明确应对措施；", width = 15)
    @ApiModelProperty(value = "风险因素 风险因素描述应具体扼要，按1.……；2.……；3.……逐个列出；对策措施中对应风险因素逐条明确应对措施；")
    private java.lang.String riskFactor;
	/**对策措施*/
	@Excel(name = "对策措施", width = 15)
    @ApiModelProperty(value = "对策措施")
    private java.lang.String cuoshi;
	/**管控人*/
	@Excel(name = "管控人", width = 15)
    @ApiModelProperty(value = "管控人")
    private java.lang.String guankongren;
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
	/**1.非建设项目/2.建设项目*/
	@Excel(name = "1.非建设项目/2.建设项目", width = 15)
    @ApiModelProperty(value = "1.非建设项目/2.建设项目")
    private java.lang.String leixing;
	/**施工单位*/
	@Excel(name = "施工单位", width = 15)
    @ApiModelProperty(value = "施工单位")
    private java.lang.String shigongunit;
	/**监理单位*/
	@Excel(name = "监理单位", width = 15)
    @ApiModelProperty(value = "监理单位")
    private java.lang.String jianliunit;
	/**建设单位*/
	@Excel(name = "建设单位", width = 15)
    @ApiModelProperty(value = "建设单位")
    private java.lang.String jiansheunit;
	/**施工负责人*/
	@Excel(name = "施工负责人", width = 15)
    @ApiModelProperty(value = "施工负责人")
    private java.lang.String shigongpeo;
	/**监理负责人*/
	@Excel(name = "监理负责人", width = 15)
    @ApiModelProperty(value = "监理负责人")
    private java.lang.String jianlipeo;
	/**建设负责人*/
	@Excel(name = "建设负责人", width = 15)
    @ApiModelProperty(value = "建设负责人")
    private java.lang.String jianshepeo;
}
