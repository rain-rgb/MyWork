package com.trtm.iot.rebarTaskMaterialLiaocangTaizhang.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.trtm.iot.rebarComponent.vo.MaterialVo;
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
 * @Description: rebar_task_material_liaocang_taizhang
 * @Author: jeecg-boot
 * @Date:   2023-07-17
 * @Version: V1.0
 */
@Data
@TableName("rebar_task_material_liaocang_taizhang")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="rebar_task_material_liaocang_taizhang对象", description="rebar_task_material_liaocang_taizhang")
public class RebarTaskMaterialLiaocangTaizhang implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private java.lang.String id;
	/**任务清单主键id*/
	@Excel(name = "任务清单主键id", width = 15)
    @ApiModelProperty(value = "任务清单主键id")
    private java.lang.String taskId;
	/**材料主键id*/
	@Excel(name = "材料主键id", width = 15)
    @ApiModelProperty(value = "材料主键id")
    private java.lang.String materialId;
	/**料仓主键id*/
	@Excel(name = "料仓主键id", width = 15)
    @ApiModelProperty(value = "料仓主键id")
    private java.lang.String liaocangId;
	/**台账主键id*/
	@Excel(name = "台账主键id", width = 15)
    @ApiModelProperty(value = "台账主键id")
    private java.lang.String taizhangId;
}
