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
 * @Description: lm_lq_files
 * @Author: jeecg-boot
 * @Date:   2023-11-15
 * @Version: V1.0
 */
@Data
@TableName("lm_lq_files")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="lm_lq_files对象", description="lm_lq_files")
public class LmLqFiles implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**施工阶段*/
    @Excel(name = "施工阶段", width = 15)
    @ApiModelProperty(value = "施工阶段")
    @Dict(dicCode = "lmlqsgjd")
    private java.lang.String sgjd;
	/**文件类型*/
	@Excel(name = "文件类型", width = 15)
    @ApiModelProperty(value = "文件类型")
    @Dict(dicCode = "lmlqfiles")
    private java.lang.String filetype;
	/**文件路径*/
	@Excel(name = "文件路径", width = 15)
    @ApiModelProperty(value = "文件路径")
    private java.lang.String fileurl;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    private java.lang.String sysOrgCode;
    /**是否有上传文件 0：有  1：没有*/
    @Excel(name = "是否有上传文件", width = 15)
    @ApiModelProperty(value = "是否有上传文件 0：有  1：没有")
    private java.lang.Integer isfile;
    /**文件上传状态 0：未上传,1：已上传,2：无文件*/
    @Excel(name = "文件上传状态 0：未上传,1：已上传,2：无文件", width = 15)
    @ApiModelProperty(value = "文件上传状态 0：未上传,1：已上传,2：无文件")
    @Dict(dicCode = "filestatus")
    private java.lang.Integer filestatus;
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
	/**主键id*/
	@Excel(name = "主键id", width = 15)
    @ApiModelProperty(value = "主键id")
    private java.lang.Integer infoid;
	/**文件数量*/
	@Excel(name = "文件数量", width = 15)
    @ApiModelProperty(value = "文件数量")
    private java.lang.Integer filecount;
}
