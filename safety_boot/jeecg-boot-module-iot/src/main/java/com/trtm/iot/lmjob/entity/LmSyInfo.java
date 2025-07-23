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
 * @Description: lm_sy_info
 * @Author: jeecg-boot
 * @Date:   2024-07-04
 * @Version: V1.0
 */
@Data
@TableName("lm_sy_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="lm_sy_info对象", description="lm_sy_info")
public class LmSyInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    private java.lang.String sysOrgCode;
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
	/**试验类型：施工检测；监理检测；业主检测；竣工检测*/
	@Excel(name = "试验类型：施工检测；监理检测；业主检测；竣工检测", width = 15)
    @ApiModelProperty(value = "试验类型：施工检测；监理检测；业主检测；竣工检测")
    private java.lang.String syType;
	/**结论描述*/
	@Excel(name = "结论描述", width = 15)
    @ApiModelProperty(value = "结论描述")
    private java.lang.String resultinfo;
	/**0：合格；1不合格*/
	@Excel(name = "0：合格；1不合格", width = 15)
    @ApiModelProperty(value = "0：合格；1不合格")
    private java.lang.Integer reslut;
	/**试验日期*/
	@Excel(name = "试验日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "试验日期")
    private java.util.Date jctime;
	/**部位*/
	@Excel(name = "部位", width = 15)
    @ApiModelProperty(value = "部位")
    private java.lang.String usewbs;
	/**附件上传*/
	@Excel(name = "附件上传", width = 15)
    @ApiModelProperty(value = "附件上传")
    private java.lang.String file;
	/**闭合状态：0：未闭合；20已闭合*/
	@Excel(name = "闭合状态：0：未闭合；20已闭合", width = 15)
    @ApiModelProperty(value = "闭合状态：0：未闭合；20已闭合")
    private java.lang.String bihe;
	/**试验名称*/
	@Excel(name = "试验名称", width = 15)
    @ApiModelProperty(value = "试验名称")
    private java.lang.String syname;
	/**beizhu*/
	@Excel(name = "beizhu", width = 15)
    @ApiModelProperty(value = "beizhu")
    private java.lang.String beizhu;
}
