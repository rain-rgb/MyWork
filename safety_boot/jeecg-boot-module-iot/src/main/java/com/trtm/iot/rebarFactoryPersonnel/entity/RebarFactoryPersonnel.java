package com.trtm.iot.rebarFactoryPersonnel.entity;

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
 * @Description: rebar_factory_personnel
 * @Author: jeecg-boot
 * @Date:   2024-11-14
 * @Version: V1.0
 */
@Data
@TableName("rebar_factory_personnel")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="rebar_factory_personnel对象", description="rebar_factory_personnel")
public class RebarFactoryPersonnel implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private java.lang.String id;
	/**任务编号*/
	@Excel(name = "任务编号", width = 15)
    @ApiModelProperty(value = "任务编号")
    private java.lang.String factoryId;
	/**班组长*/
	@Excel(name = "班组长", width = 15)
    @ApiModelProperty(value = "班组长")
    private java.lang.String teamLeaderId;
	/**组织机构编码*/
	@Excel(name = "组织机构编码", width = 15)
    @ApiModelProperty(value = "组织机构编码")
    private java.lang.String orgCode;
	/**分部分项组织机构编码*/
	@Excel(name = "分部分项组织机构编码", width = 15)
    @ApiModelProperty(value = "分部分项组织机构编码")
    private java.lang.String orgCodes;
}
