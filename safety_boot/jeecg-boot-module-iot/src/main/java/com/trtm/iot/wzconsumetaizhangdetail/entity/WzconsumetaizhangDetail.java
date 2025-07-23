package com.trtm.iot.wzconsumetaizhangdetail.entity;

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
 * @Description: 物资原材料消耗台账材料子表
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
@Data
@TableName("wzconsumetaizhang_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wzconsumetaizhang_detail对象", description="物资原材料消耗台账材料子表")
public class WzconsumetaizhangDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private java.lang.Integer id;
	/**料仓编号*/
	@Excel(name = "料仓编号", width = 15)
    @ApiModelProperty(value = "料仓编号")
    private java.lang.String lcbianhao;
    /**料仓名称*/
    @Excel(name = "料仓名称", width = 15)
    @ApiModelProperty(value = "料仓名称")
    private java.lang.String name;
	/**（wzconsumetaizhang表）id*/
	@Excel(name = "（wzconsumetaizhang表）id", width = 15)
    @ApiModelProperty(value = "（wzconsumetaizhang表）id")
    private java.lang.Integer xhId;
	/**材料剩余(吨)*/
	@Excel(name = "材料剩余(吨)", width = 15)
    @ApiModelProperty(value = "材料剩余(吨)")
    private java.lang.Double cailiaoshengyut;
	/**批次库存(吨)*/
	@Excel(name = "批次库存(吨)", width = 15)
    @ApiModelProperty(value = "批次库存(吨)")
    private java.lang.Double picizhong;
	/**材料消耗(吨)*/
	@Excel(name = "材料消耗(吨)", width = 15)
    @ApiModelProperty(value = "材料消耗(吨)")
    private java.lang.Double realityNumber;
    /**材料名*/
    @Excel(name = "材料名", width = 15)
    @ApiModelProperty(value = "材料名")
    private java.lang.String materialeName;
    /**批次*/
    @Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private java.lang.String pici;
}
