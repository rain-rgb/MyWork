package com.trtm.iot.czsh.vo;

import java.util.List;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 拌合站处置审核信息
 * @Author: jeecg-boot
 * @Date:   2021-03-19
 * @Version: V1.0
 */
@Data
@ApiModel(value="bhz_cement_over_handlerPage对象", description="拌合站处置审核信息")
public class BhzCementOverHandlerPage {

	/**主键id*/
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
	@Excel(name = "处理时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "处理时间")
	private Date handleTime;
	/**处理人*/
	@Excel(name = "处理人", width = 15)
	@ApiModelProperty(value = "处理人")
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
	@Excel(name = "监理处理时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "监理处理时间")
	private Date supervisorHandleTime;
	/**审批人*/
	@Excel(name = "审批人", width = 15)
	@ApiModelProperty(value = "审批人")
	private String approvalPerson;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private String remark;
	/**附件*/
	@Excel(name = "附件", width = 15)
	@ApiModelProperty(value = "附件")
	private String filePath;
	/**超标状态：0为未处理，10为施工方已处理，20为监理方已处理*/
	@Excel(name = "超标状态：0为未处理，10为施工方已处理，20为监理方已处理", width = 15)
	@ApiModelProperty(value = "超标状态：0为未处理，10为施工方已处理，20为监理方已处理")
	private Integer overproofStatus;


}
