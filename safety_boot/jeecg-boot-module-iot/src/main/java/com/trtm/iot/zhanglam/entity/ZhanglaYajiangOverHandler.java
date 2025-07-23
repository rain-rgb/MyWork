package com.trtm.iot.zhanglam.entity;

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
 * @Description: zhangla_yajiang_over_handler
 * @Author: jeecg-boot
 * @Date:   2021-12-27
 * @Version: V1.0
 */
@Data
@TableName("zhangla_yajiang_over_handler")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="zhangla_yajiang_over_handler对象", description="zhangla_yajiang_over_handler")
public class ZhanglaYajiangOverHandler implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private java.lang.Integer id;
	/**对应张拉压浆主表的syjid*/
	@Excel(name = "对应张拉压浆主表的syjid", width = 15)
    @ApiModelProperty(value = "对应张拉压浆主表的syjid")
    private java.lang.String baseid;
	/**问题原因*/
	@Excel(name = "问题原因", width = 15)
    @ApiModelProperty(value = "问题原因")
    private java.lang.String problemReasons;
	/**处理方式*/
	@Excel(name = "处理方式", width = 15)
    @ApiModelProperty(value = "处理方式")
    private java.lang.String handleWay;
	/**处理结果*/
	@Excel(name = "处理结果", width = 15)
    @ApiModelProperty(value = "处理结果")
    private java.lang.String handleResult;
	/**处理时间*/
	@Excel(name = "处理时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "处理时间")
    private java.util.Date handleTime;
	/**处理人*/
	@Excel(name = "处理人", width = 15)
    @ApiModelProperty(value = "处理人")
    private java.lang.String handlePerson;
	/**监理审批*/
	@Excel(name = "监理审批", width = 15)
    @ApiModelProperty(value = "监理审批")
    private java.lang.String supervisorApproval;
	/**监理结果*/
	@Excel(name = "监理结果", width = 15)
    @ApiModelProperty(value = "监理结果")
    private java.lang.String supervisorResult;
	/**监理处理时间*/
	@Excel(name = "监理处理时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "监理处理时间")
    private java.util.Date supervisorHandleTime;
	/**审批人*/
	@Excel(name = "审批人", width = 15)
    @ApiModelProperty(value = "审批人")
    private java.lang.String approvalPerson;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**审核附件*/
	@Excel(name = "审核附件", width = 15)
    @ApiModelProperty(value = "审核附件")
    private java.lang.String filePath;
	/**处置附件*/
	@Excel(name = "处置附件", width = 15)
    @ApiModelProperty(value = "处置附件")
    private java.lang.String filePath2;
	/**超标状态：0为未处理，10为施工方已处理，20为监理方已处理*/
	@Excel(name = "超标状态：0为未处理，10为施工方已处理，20为监理方已处理", width = 15)
    @ApiModelProperty(value = "超标状态：0为未处理，10为施工方已处理，20为监理方已处理")
    private java.lang.Integer overproofStatus;

	private String shebeiNo;
    private String headquartersApproval;
    private String headquartersResult;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date headquartersHandleTime;
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String headquartersPerson;
    private String headquartersRemark;
    private String filePath3;
    private String holeid;
}
