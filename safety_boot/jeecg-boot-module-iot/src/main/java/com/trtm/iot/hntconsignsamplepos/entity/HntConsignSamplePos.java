package com.trtm.iot.hntconsignsamplepos.entity;

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
 * @Description: 混凝土见证取样货架表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-02
 * @Version: V1.0
 */
@Data
@TableName("hnt_consign_sample_pos")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hnt_consign_sample_pos对象", description="混凝土见证取样货架表信息")
public class HntConsignSamplePos implements Serializable {
    private static final long serialVersionUID = 1L;

	/**货架id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "货架id")
    private java.lang.Integer id;
	/**外键，组织机构id,只能是试验类型的组织机构*/
	@Excel(name = "外键，组织机构id,只能是试验类型的组织机构", width = 15)
    @ApiModelProperty(value = "外键，组织机构id,只能是试验类型的组织机构")
    private java.lang.String departid;
	/**外键，组织机构编码*/
	@Excel(name = "外键，组织机构编码", width = 15)
    @ApiModelProperty(value = "外键，组织机构编码")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**货架名称或者编号*/
	@Excel(name = "货架名称或者编号", width = 15)
    @ApiModelProperty(value = "货架名称或者编号")
    private java.lang.String huojianame;
	/**货架排行*/
	@Excel(name = "货架排行", width = 15)
    @ApiModelProperty(value = "货架排行")
    private java.lang.String huojiahang;
	/**货架排列*/
	@Excel(name = "货架排列", width = 15)
    @ApiModelProperty(value = "货架排列")
    private java.lang.String huojialie;
	/**货架从下到上总层数*/
	@Excel(name = "货架从下到上总层数", width = 15)
    @ApiModelProperty(value = "货架从下到上总层数")
    private java.lang.String huojiacenshu;
	/**外键，标养室id*/
	@Excel(name = "外键，标养室id", width = 15)
    @ApiModelProperty(value = "外键，标养室id")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String temperatureid;
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String createBy;
    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
}
