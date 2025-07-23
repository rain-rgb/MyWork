package com.trtm.iot.tr_maoxiayuyingli_over_handler.entity;

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
 * @Description: 锚下预应力张拉处置
 * @Author: jeecg-boot
 * @Date:   2024-06-06
 * @Version: V1.0
 */
@Data
@TableName("tr_maoxiayuyingli_over_handler")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tr_maoxiayuyingli_over_handler对象", description="锚下预应力张拉处置")
public class TrMaoxiayuyingliOverHandler implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private java.lang.Integer id;
	/**工作区id 唯一*/
	@Excel(name = "工作区id 唯一", width = 15)
    @ApiModelProperty(value = "工作区id 唯一")
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
	@Excel(name = "处理时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
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
	@Excel(name = "监理处理时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
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
	/**超标状态：0为未处理，10为施工方已处理，20为闭合，30监理驳回，40监理审核，50指挥部审核，60指挥部驳回*/
	@Excel(name = "超标状态：0为未处理，10为施工方已处理，20为闭合，30监理驳回，40监理审核，50指挥部审核，60指挥部驳回", width = 15)
    @ApiModelProperty(value = "超标状态：0为未处理，10为施工方已处理，20为闭合，30监理驳回，40监理审核，50指挥部审核，60指挥部驳回")
    private java.lang.Integer overproofStatus;
	/**施工桩号*/
	@Excel(name = "施工桩号", width = 15)
    @ApiModelProperty(value = "施工桩号")
    private java.lang.String startstation;
	/**审核附件文件*/
	@Excel(name = "审核附件文件", width = 15)
    @ApiModelProperty(value = "审核附件文件")
    private java.lang.String filePathwj;
	/**处置附件文件*/
	@Excel(name = "处置附件文件", width = 15)
    @ApiModelProperty(value = "处置附件文件")
    private java.lang.String filePath2wj;
	/**指挥部审批*/
	@Excel(name = "指挥部审批", width = 15)
    @ApiModelProperty(value = "指挥部审批")
    private java.lang.String headquartersApproval;
	/**指挥部结果*/
	@Excel(name = "指挥部结果", width = 15)
    @ApiModelProperty(value = "指挥部结果")
    private java.lang.String headquartersResult;
	/**指挥部处理时间*/
	@Excel(name = "指挥部处理时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "指挥部处理时间")
    private java.util.Date headquartersHandleTime;
	/**指挥部审批人*/
	@Excel(name = "指挥部审批人", width = 15)
    @ApiModelProperty(value = "指挥部审批人")
    private java.lang.String headquartersPerson;
	/**指挥部驳回原因*/
	@Excel(name = "指挥部驳回原因", width = 15)
    @ApiModelProperty(value = "指挥部驳回原因")
    private java.lang.String headquartersRemark;
	/**指挥部审批附件*/
	@Excel(name = "指挥部审批附件", width = 15)
    @ApiModelProperty(value = "指挥部审批附件")
    private java.lang.String filePath3;
}
