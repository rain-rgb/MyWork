package com.trtm.iot.testroom.entity;

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
 * @Description: sy_testroom_detail
 * @Author: jeecg-boot
 * @Date:   2024-09-24
 * @Version: V1.0
 */
@Data
@TableName("sy_testroom_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_testroom_detail对象", description="sy_testroom_detail")
public class SyTestroomDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**试验室*/
	@Excel(name = "试验室", width = 15)
    @ApiModelProperty(value = "试验室")
    private java.lang.String testroom;
	/**试验室编号*/
	@Excel(name = "试验室编号", width = 15)
    @ApiModelProperty(value = "试验室编号")
    private java.lang.String testroomid;
	/**危险源名称*/
	@Excel(name = "危险源名称", width = 15)
    @ApiModelProperty(value = "危险源名称")
    private java.lang.String weixianname;
	/**危险编号*/
	@Excel(name = "危险编号", width = 15)
    @ApiModelProperty(value = "危险编号")
    private java.lang.String wxid;
	/**危险级别*/
	@Excel(name = "危险级别", width = 15)
    @ApiModelProperty(value = "危险级别")
    private java.lang.String wxlevel;
	/**易发生事故伤害种类*/
	@Excel(name = "易发生事故伤害种类", width = 15)
    @ApiModelProperty(value = "易发生事故伤害种类")
    private java.lang.String miaoshu;
	/**控制要点*/
	@Excel(name = "控制要点", width = 15)
    @ApiModelProperty(value = "控制要点")
    private java.lang.String kongzhipoint;
	/**责任人*/
	@Excel(name = "责任人", width = 15)
    @ApiModelProperty(value = "责任人")
    private java.lang.String zerenr;
	/**检查周期*/
	@Excel(name = "检查周期", width = 15)
    @ApiModelProperty(value = "检查周期")
    private java.lang.String jianchazhouqi;
	/**公司名称*/
	@Excel(name = "公司名称", width = 15)
    @ApiModelProperty(value = "公司名称")
    private java.lang.String company;
}
