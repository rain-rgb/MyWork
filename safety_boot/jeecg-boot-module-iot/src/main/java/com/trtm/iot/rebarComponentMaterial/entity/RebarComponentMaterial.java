package com.trtm.iot.rebarComponentMaterial.entity;

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
 * @Description: rebar_component_material
 * @Author: jeecg-boot
 * @Date:   2023-06-15
 * @Version: V1.0
 */
@Data
@TableName("rebar_component_material")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="rebar_component_material对象", description="rebar_component_material")
public class RebarComponentMaterial implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private java.lang.String id;
	/**构件id*/
	@Excel(name = "构件id", width = 15)
    @ApiModelProperty(value = "构件id")
    private java.lang.String componentId;
	/**原材id*/
	@Excel(name = "原材id", width = 15)
    @ApiModelProperty(value = "原材id")
    private java.lang.String materialId;
	/**原材对应的数量*/
	@Excel(name = "原材对应的数量", width = 15)
    @ApiModelProperty(value = "原材对应的数量")
    private java.lang.String materialNumber;
    /**原材名称*/
    @Excel(name = "原材名称", width = 15)
    @ApiModelProperty(value = "原材名称")
    private java.lang.String materialName;
    /**规格型号*/
    @Excel(name = "规格型号", width = 15)
    @ApiModelProperty(value = "规格型号")
    private java.lang.String materialType;
    /**所属单位编码*/
    @Excel(name = "所属单位编码", width = 15)
    @ApiModelProperty(value = "所属单位编码")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String orgCode;
    /**分部分项组织机构编码*/
    @Excel(name = "分部分项组织机构编码", width = 15)
    @ApiModelProperty(value = "分部分项组织机构编码")
    @Dict(dictTable = "sys_depart_project", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String orgCodes;
    @ApiModelProperty(value = "任务编号")
    private java.lang.String taskId;
    @ApiModelProperty(value = "材料唯一编号")
    private java.lang.String guid;
}
