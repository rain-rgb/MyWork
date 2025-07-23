package com.trtm.iot.gqpx_training_plan.entity;

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
 * @Description: 岗前培训计划表
 * @Author: jeecg-boot
 * @Date:   2024-11-26
 * @Version: V1.0
 */
@Data
@TableName("gqpx_training_plan")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="gqpx_training_plan对象", description="岗前培训计划表")
public class GqpxTrainingPlan implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**唯一id*/
	@Excel(name = "唯一id", width = 15)
    @ApiModelProperty(value = "唯一id")
    private java.lang.String guid;
	/**培训计划名称*/
	@Excel(name = "培训计划名称", width = 15)
    @ApiModelProperty(value = "培训计划名称")
    private java.lang.String name;
	/**培训类型*/
	@Excel(name = "培训类型", width = 15)
    @ApiModelProperty(value = "培训类型")
    private java.lang.Integer type;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private java.util.Date startTime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
    private java.util.Date endTime;
	/**培训地点*/
	@Excel(name = "培训地点", width = 15)
    @ApiModelProperty(value = "培训地点")
    private java.lang.String location;
	/**参与工种*/
	@Excel(name = "参与工种", width = 15)
    @ApiModelProperty(value = "参与工种")
    private java.lang.String worktype;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private java.lang.String trniningdescribe;
	/**拟参与人员*/
	@Excel(name = "拟参与人员", width = 15)
    @ApiModelProperty(value = "拟参与人员")
    private java.lang.String staff;
	/**审核状态(0待审核；10监理同意；15监理已驳回；20指挥部同意；25指挥部已驳回；30交警同意；35：交警已驳回)*/
	@Excel(name = "审核状态(0待审核；10监理同意；15监理已驳回；20指挥部同意；25指挥部已驳回；30交警同意；35：交警已驳回)", width = 15)
    @ApiModelProperty(value = "审核状态(0待审核；10监理同意；15监理已驳回；20指挥部同意；25指挥部已驳回；30交警同意；35：交警已驳回)")
    private java.lang.Integer status;
	/**监理审核意见*/
	@Excel(name = "监理审核意见", width = 15)
    @ApiModelProperty(value = "监理审核意见")
    private java.lang.String supervisorApproval;
	/**监理审核时间*/
	@Excel(name = "监理审核时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "监理审核时间")
    private java.util.Date supervisorHandleTime;
	/**指挥部审核意见*/
	@Excel(name = "指挥部审核意见", width = 15)
    @ApiModelProperty(value = "指挥部审核意见")
    private java.lang.String headquartersApproval;
	/**指挥部审核时间*/
	@Excel(name = "指挥部审核时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "指挥部审核时间")
    private java.util.Date headquartersHandleTime;
	/**交警审核意见*/
	@Excel(name = "交警审核意见", width = 15)
    @ApiModelProperty(value = "交警审核意见")
    private java.lang.String trafficPoliceApproval;
	/**交警审核时间*/
	@Excel(name = "交警审核时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "交警审核时间")
    private java.util.Date trafficPoliceHandleTime;
	/**完成状态（10监理批准；20指挥部批准；30运营公司批准；40交警批准）*/
	@Excel(name = "完成状态（10监理批准；20指挥部批准；30运营公司批准；40交警批准）", width = 15)
    @ApiModelProperty(value = "完成状态（10监理批准；20指挥部批准；30运营公司批准；40交警批准）")
    private java.lang.Integer completeStatus;
	/**现场图片*/
	@Excel(name = "现场图片", width = 15)
    @ApiModelProperty(value = "现场图片")
    private java.lang.String pictureurl1;
	/**现场图片*/
	@Excel(name = "现场图片", width = 15)
    @ApiModelProperty(value = "现场图片")
    private java.lang.String pictureurl2;
	/**现场图片*/
	@Excel(name = "现场图片", width = 15)
    @ApiModelProperty(value = "现场图片")
    private java.lang.String pictureurl3;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
    private java.lang.String createPerson;
    /**创建时间*/
    @Excel(name = "绿码到期时间", width = 15)
    @ApiModelProperty(value = "绿码到期时间")
    private java.lang.String expirationTime;

    private java.lang.String supervisorPerson;
    private java.lang.String headquartersPerson;
    private java.lang.String trafficPolicePerson;
    private java.lang.String supervisorConfirmApproval;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date supervisorConfirmTime;
    private java.lang.String supervisorConfirmPerson;
    private java.lang.String headquartersConfirmApproval;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date headquartersConfirmTime;
    private java.lang.String headquartersConfirmPerson;
    private java.lang.String trafficPoliceConfirmApproval;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date trafficPoliceConfirmTime;
    private java.lang.String trafficPoliceConfirmPerson;
    private java.lang.String operationsConfirmApproval;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date operationsConfirmTime;
    private java.lang.String operationsConfirmPerson;


    private java.lang.String lecturer; //讲师
    private java.lang.String instructor; //教官
    private java.lang.String banzu; //班组
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String orgcode; //标段
    private java.lang.String orgname; //标段
    private java.lang.Integer completionStatus; //完成状态
    private java.lang.String danwei;//单位
    private java.lang.String renshu;//人数
    private java.lang.String sgcontent;//施工内容
    private java.lang.String startpile;//开始桩号
    private java.lang.String endpile;//结束桩号
    private java.lang.String direction;//施工方向
    private java.lang.String effectmeasure;//影响及措施
    private java.lang.String person;//负责人
    private java.util.Date sgstartTime;//施工开始时间
    private java.util.Date sgendTime;//施工结束时间
    private java.lang.String changereason;//变更原因
}
