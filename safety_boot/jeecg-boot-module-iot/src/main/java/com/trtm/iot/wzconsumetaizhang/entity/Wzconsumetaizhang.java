package com.trtm.iot.wzconsumetaizhang.entity;

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
 * @Description: 物资原材料消耗台账主表信息
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
@Data
@TableName("wzconsumetaizhang")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wzconsumetaizhang对象", description="物资原材料消耗台账主表信息")
public class Wzconsumetaizhang implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private java.lang.Integer id;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private java.lang.String projectName;
	/**材料消耗部位*/
	@Excel(name = "材料消耗部位", width = 15)
    @ApiModelProperty(value = "材料消耗部位")
    private java.lang.String poureLocation;
	/**砼消耗数量(m³)*/
	@Excel(name = "砼消耗数量(m³)", width = 15)
    @ApiModelProperty(value = "砼消耗数量(m³)")
    private java.lang.Double estimateNumber;
	/**砼标记*/
	@Excel(name = "砼标记", width = 15)
    @ApiModelProperty(value = "砼标记")
    private java.lang.String strengthRank;
	/**控制权限*/
    @ApiModelProperty(value = "控制权限")
    private java.lang.String sysOrgCode;
    /**开始统计时间*/
    @Excel(name = "开始统计时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始统计时间")
    private java.util.Date starttim;
    /**当前统计时间*/
    @Excel(name = "当前统计时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "当前统计时间")
    private java.util.Date endtim;
}
