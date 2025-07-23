package com.trtm.iot.czsh.entity;

import java.io.Serializable;
import java.util.Date;
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

/**
 * @Description: 拌合站处置审核信息
 * @Author: jeecg-boot
 * @Date:   2021-03-19
 * @Version: V1.0
 */
@ApiModel(value="bhz_cement_over_handler对象", description="拌合站处置审核信息")
@Data
@TableName("bhz_cement_over_handler")
public class BhzCementOverHandler implements Serializable {
    private static final long serialVersionUID = 1L;
	/**主键id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Integer id;
	/**对应拌和站主表batch_no*/
	@Excel(name = "对应拌和站主表batch_no", width = 15)
    @ApiModelProperty(value = "对应拌和站主表batch_no")
    private String baseid;
	/**问题原因*/
	@Excel(name = "问题原因", width = 15)
    @ApiModelProperty(value = "问题原因")
    private String problemReasons;
	/**处理方式*/
	@Excel(name = "处理方式", width = 15)
    @ApiModelProperty(value = "处理方式")
    private String handleWay;
	/**处理结果*/
	@Excel(name = "处理结果", width = 15)
    @ApiModelProperty(value = "处理结果")
    private String handleResult;
	/**处理时间*/
	@Excel(name = "处理时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "处理时间")
    private Date handleTime;
	/**处理人*/
	@Excel(name = "处理人", width = 15)
    @ApiModelProperty(value = "处理人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String handlePerson;
	/**监理审批*/
	@Excel(name = "监理审批", width = 15)
    @ApiModelProperty(value = "监理审批")
    private String supervisorApproval;
	/**监理结果*/
	@Excel(name = "监理结果", width = 15)
    @ApiModelProperty(value = "监理结果")
    private String supervisorResult;
	/**监理处理时间*/
	@Excel(name = "监理处理时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "监理处理时间")
    private Date supervisorHandleTime;
	/**审批人*/
	@Excel(name = "审批人", width = 15)
    @ApiModelProperty(value = "审批人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String approvalPerson;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
	/**审核附件*/
	@Excel(name = "审核附件", width = 15)
    @ApiModelProperty(value = "审核附件")
    private String filePath;
    /**处置附件*/
    @Excel(name = "处置附件", width = 15)
    @ApiModelProperty(value = "处置附件")
    private String filePath2;
	/**超标状态：0为未处理，10为施工方已处理，20为监理方已处理*/
	@Excel(name = "超标状态：0为未处理，10为施工方已处理，20为监理方已处理", width = 15)
    @Dict(dicCode = "overproof_status")
    @ApiModelProperty(value = "超标状态：0为未处理，10为施工方已处理，20为监理方已处理")
    private Integer overproofStatus;

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
    private Integer handletype;

}



