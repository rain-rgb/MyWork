package com.trtm.iot.StabilityDetail.entity;

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
 * @Description: 稳定度子表
 * @Author: jeecg-boot
 * @Date:   2022-12-07
 * @Version: V1.0
 */
@Data
@TableName("w_wendingdu_s")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="稳定度子表", description="稳定度子表")
public class StabilityDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**唯一id*/
	@Excel(name = "唯一id", width = 15)
    @ApiModelProperty(value = "唯一id")
    private java.lang.String fGuid;
	/**试验id*/
	@Excel(name = "试验id", width = 15)
    @ApiModelProperty(value = "试验id")
    private java.lang.String syjid;
	/**fXh*/
	@Excel(name = "fXh", width = 15)
    @ApiModelProperty(value = "fXh")
    private java.lang.Integer fXh;
	/**流值*/
	@Excel(name = "流值", width = 15)
    @ApiModelProperty(value = "流值")
    private java.math.BigDecimal liuzhi;
	/**稳定度*/
	@Excel(name = "稳定度", width = 15)
    @ApiModelProperty(value = "稳定度")
    private java.math.BigDecimal wendingdu;
	/**测试时间*/
	@Excel(name = "测试时间", width = 15)
    @ApiModelProperty(value = "测试时间")
    private java.lang.String testtime;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String fSbbh;
	/**isvalid*/
	@Excel(name = "isvalid", width = 15)
    @ApiModelProperty(value = "isvalid")
    private java.lang.String isvalid;
	/**抗压力值*/
	@Excel(name = "抗压力值", width = 15)
    @ApiModelProperty(value = "抗压力值")
    private java.lang.String fYskylz;
	/**fYskyxb*/
	@Excel(name = "fYskyxb", width = 15)
    @ApiModelProperty(value = "fYskyxb")
    private java.lang.String fYskyxb;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.Integer status;
	/**提交时间*/
	@Excel(name = "提交时间", width = 15)
    @ApiModelProperty(value = "提交时间")
    private java.lang.String submittime;
}
