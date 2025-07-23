package com.trtm.iot.lmjob.entity;

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
 * @Description: lm_job_info
 * @Author: jeecg-boot
 * @Date:   2023-08-31
 * @Version: V1.0
 */
@Data
@TableName("lm_job_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="lm_job_info对象", description="lm_job_info")
public class LmJobInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**桩号里程*/
	@Excel(name = "桩号里程", width = 15)
    @ApiModelProperty(value = "桩号里程")
    private java.lang.String zhlc;
	/**路面结构类型*/
	@Excel(name = "路面结构类型", width = 15)
    @ApiModelProperty(value = "路面结构类型")
    private java.lang.String wbstype;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**分部分项code*/
	@Excel(name = "分部分项code", width = 15)
    @ApiModelProperty(value = "分部分项code")
    @Dict(dictTable = "sys_depart_project", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String wbsOrgCode;
	/**底基层混合料总方量*/
	@Excel(name = "底基层混合料总方量", width = 15)
    @ApiModelProperty(value = "底基层混合料总方量")
    private java.lang.String djcInfo1;
	/**基层混合料总方量*/
	@Excel(name = "基层混合料总方量", width = 15)
    @ApiModelProperty(value = "基层混合料总方量")
    private java.lang.String jcInfo1;
	/**底基层供应商*/
	@Excel(name = "底基层供应商", width = 15)
    @ApiModelProperty(value = "底基层供应商")
    private java.lang.String djcInfo2;
	/**基层供应商*/
	@Excel(name = "基层供应商", width = 15)
    @ApiModelProperty(value = "基层供应商")
    private java.lang.String jcInfo2;
	/**下面层混合料总方量*/
	@Excel(name = "下面层混合料总方量", width = 15)
    @ApiModelProperty(value = "下面层混合料总方量")
    private java.lang.String xmcInfo1;
	/**下面层供应商*/
	@Excel(name = "下面层供应商", width = 15)
    @ApiModelProperty(value = "下面层供应商")
    private java.lang.String xmcInfo2;
	/**中面层混合料总方量*/
	@Excel(name = "中面层混合料总方量", width = 15)
    @ApiModelProperty(value = "中面层混合料总方量")
    private java.lang.String zmcInfo1;
	/**中面层供应商*/
	@Excel(name = "中面层供应商", width = 15)
    @ApiModelProperty(value = "中面层供应商")
    private java.lang.String zmcInfo2;
	/**上面层混合料总方量*/
	@Excel(name = "上面层混合料总方量", width = 15)
    @ApiModelProperty(value = "上面层混合料总方量")
    private java.lang.String smcInfo1;
	/**上面层供应商*/
	@Excel(name = "上面层供应商", width = 15)
    @ApiModelProperty(value = "上面层供应商")
    private java.lang.String smcInfo2;
	/**文件数量*/
	@Excel(name = "文件数量", width = 15)
    @ApiModelProperty(value = "文件数量")
    private java.lang.Integer files;
	/**guid*/
	@Excel(name = "guid", width = 15)
    @ApiModelProperty(value = "guid")
    private java.lang.String guid;
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
    private java.lang.String proname;
    private java.lang.String lmname;
    private java.lang.String sgdw;
}
