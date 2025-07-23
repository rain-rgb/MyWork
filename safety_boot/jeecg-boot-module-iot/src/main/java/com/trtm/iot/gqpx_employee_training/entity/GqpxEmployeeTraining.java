package com.trtm.iot.gqpx_employee_training.entity;

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
 * @Description: 人员培训记录
 * @Author: jeecg-boot
 * @Date:   2024-11-29
 * @Version: V1.0
 */
@Data
@TableName("gqpx_employee_training")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="gqpx_employee_training对象", description="人员培训记录")
public class GqpxEmployeeTraining implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**人员id*/
	@Excel(name = "人员id", width = 15)
    @ApiModelProperty(value = "人员id")
    private java.lang.String userId;
	/**人员姓名*/
	@Excel(name = "人员姓名", width = 15)
    @ApiModelProperty(value = "人员姓名")
    private java.lang.String userName;
	/**培训计划id*/
	@Excel(name = "培训计划id", width = 15)
    @ApiModelProperty(value = "培训计划id")
    private java.lang.String traningGuid;
	/**培训名称*/
	@Excel(name = "培训名称", width = 15)
    @ApiModelProperty(value = "培训名称")
    private java.lang.String traningName;
	/**培训状态（0进行中；1已完成）*/
	@Excel(name = "培训状态（0进行中；1已完成）", width = 15)
    @ApiModelProperty(value = "培训状态（0进行中；1已完成）")
    private java.lang.Integer status;
    @ApiModelProperty(value = "二维码状态（0红码；1绿码）")
    private java.lang.Integer colourStatus;
	/**完成时间*/
	@Excel(name = "完成时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "培训完成时间")
    private java.util.Date completionTime;
    /**创建时间*/
    @Excel(name = "培训创建时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "培训创建时间")
    private java.util.Date createTime;
    /**创建时间*/
    @Excel(name = "绿码到期时间", width = 15)
    @ApiModelProperty(value = "绿码到期时间")
    private java.lang.String expirationTime;
    private java.lang.Integer traningType;
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String orgcode;

}
