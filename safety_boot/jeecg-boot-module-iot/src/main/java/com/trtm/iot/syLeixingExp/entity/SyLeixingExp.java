package com.trtm.iot.syLeixingExp.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 试验类型和试验关联表
 * @Author: jeecg-boot
 * @Date:   2022-03-22
 * @Version: V1.0
 */
@Data
@TableName("sy_leixing_exp")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_leixing_exp对象", description="试验类型和试验关联表")
public class SyLeixingExp implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private java.lang.Integer id;
	/**与sy_leixing表中cailiaono关联*/
	@Excel(name = "与sy_leixing表中cailiaono关联", width = 15)
    @ApiModelProperty(value = "与sy_leixing表中cailiaono关联")
    private java.lang.String parentid;
	/**实验ID*/
	@Excel(name = "实验ID", width = 15)
    @ApiModelProperty(value = "实验ID")
    private java.lang.String expid;
	/**试验名称*/
	@Excel(name = "试验名称", width = 15)
    @ApiModelProperty(value = "试验名称")
    private java.lang.String expname;
}
