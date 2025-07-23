package com.trtm.iot.gongyistatistic.entity;

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
 * @Description: gongyi_statistic
 * @Author: jeecg-boot
 * @Date:   2023-03-06
 * @Version: V1.0
 */
@Data
@TableName("gongyi_statistic")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="gongyi_statistic对象", description="gongyi_statistic")
public class GongyiStatistic implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**所属部门名称*/
	@Excel(name = "所属部门名称", width = 15)
    @ApiModelProperty(value = "所属部门名称")
    private java.lang.String orgcodeName;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeiNo;
	/**字典值*/
	@Excel(name = "字典值", width = 15)
    @ApiModelProperty(value = "字典值")
    private java.lang.Integer prontzhi;
	/**统计时间月数*/
	@Excel(name = "统计时间月数", width = 15)
    @ApiModelProperty(value = "统计时间月数")
    private java.lang.String prontTime;
	/**总数*/
	@Excel(name = "总数", width = 15)
    @ApiModelProperty(value = "总数")
    private java.lang.String zongshu;
	/**预警数*/
	@Excel(name = "预警数", width = 15)
    @ApiModelProperty(value = "预警数")
    private java.lang.String yujings;
	/**闭合数*/
	@Excel(name = "闭合数", width = 15)
    @ApiModelProperty(value = "闭合数")
    private java.lang.String bihes;
}
