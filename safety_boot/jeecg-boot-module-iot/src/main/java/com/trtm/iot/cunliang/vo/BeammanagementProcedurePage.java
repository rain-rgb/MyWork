package com.trtm.iot.cunliang.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.cunliang.entity.BeammanagementProcedure;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * \* @author: Xx
 * \* Date: 2021/8/20
 * \* Time: 16:09
 * \* Description:
 * \
 */
@Data
@ApiModel(value="BeammanagementProcedurePage对象", description="存梁表信息")
public class BeammanagementProcedurePage {
    /**主键*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
    /**任务单编号*/
    @Excel(name = "任务单编号", width = 15)
    @ApiModelProperty(value = "任务单编号")
    private java.lang.String code;
    @Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private java.lang.String projname;
    @Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    private java.lang.String conspos;
    @Excel(name = "唯一id", width = 15)
    @ApiModelProperty(value = "唯一id")
    private java.lang.String uuid;

    private List<BeammanagementProcedure> beammanagementProcedure;
}
