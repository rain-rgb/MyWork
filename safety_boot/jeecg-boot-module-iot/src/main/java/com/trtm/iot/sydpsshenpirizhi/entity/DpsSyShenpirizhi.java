package com.trtm.iot.sydpsshenpirizhi.entity;

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
 * @Description: dps_sy_shenpirizhi
 * @Author: jeecg-boot
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Data
@TableName("dps_sy_shenpirizhi")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="dps_sy_shenpirizhi对象", description="dps_sy_shenpirizhi")
public class DpsSyShenpirizhi implements Serializable {
    private static final long serialVersionUID = 1L;

	/**编号，uuid*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "编号，uuid")
    private java.lang.String id;
	/**外键、样品表id，sy_dps_sy_sample*/
	@Excel(name = "外键、样品表id，sy_dps_sy_sample", width = 15)
    @ApiModelProperty(value = "外键、样品表id，sy_dps_sy_sample")
    private java.lang.String sampleid;
	/**审批意见*/
	@Excel(name = "审批意见", width = 15)
    @ApiModelProperty(value = "审批意见")
    private java.lang.String shenpiyijian;
	/**审批人，当前登录用户名*/
	@Excel(name = "审批人，当前登录用户名", width = 15)
    @ApiModelProperty(value = "审批人，当前登录用户名")
    private java.lang.String shenpiren;
	/**审批时间*/
	@Excel(name = "审批时间", width = 15)
    @ApiModelProperty(value = "审批时间")
    private java.lang.String shenpishijian;
	/**状态 1：提交 2：审核通过 3：审核退回*/
	@Excel(name = "状态 1：提交 2：审核通过 3：审核退回", width = 15)
    @ApiModelProperty(value = "状态 1：提交 2：审核通过 3：审核退回")
    private java.lang.Integer shenpizhuangtai;
	/**evaluateid*/
	@Excel(name = "evaluateid", width = 15)
    @ApiModelProperty(value = "evaluateid")
    private java.lang.String evaluateid;
	/**img*/
	@Excel(name = "img", width = 15)
    @ApiModelProperty(value = "img")
    private java.lang.String img;
}
