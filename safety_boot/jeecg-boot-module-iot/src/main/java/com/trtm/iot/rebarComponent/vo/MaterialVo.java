package com.trtm.iot.rebarComponent.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.trtm.iot.yclud.entity.YclUsageDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhangqi
 * @date 2023/6/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialVo implements Serializable {

    /**领用表 */
    @ApiModelProperty(value = "领用表 ")
    private List<YclUsageDetail> yclUsageDetail;
    /**主键id*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private java.lang.String id;
    /**构件编号*/
//    @Excel(name = "构件编号", width = 15)
    @ApiModelProperty(value = "构件编号")
    private java.lang.String componentId;
    /**任务编号*/
//    @Excel(name = "构件编号", width = 15)
    @ApiModelProperty(value = "任务编号")
    private java.lang.String taskId;
    /**料仓主键id*/
    @Excel(name = "料仓主键id", width = 15)
    @ApiModelProperty(value = "料仓主键id")
    private java.lang.String liaocangId;
    /**台账主键id*/
    @Excel(name = "台账主键id", width = 15)
    @ApiModelProperty(value = "台账主键id")
    private java.lang.String taizhangId;
    /**材料名称*/
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private java.lang.String materialName;
    /**材料编号*/
    @Excel(name = "材料编号", width = 15)
    @ApiModelProperty(value = "材料编号")
    private java.lang.String materialId;
    /**规格类型*/
    @Excel(name = "规格类型", width = 15)
    @ApiModelProperty(value = "规格类型")
    private java.lang.String materialModel;
    /**规格类型*/
    @Excel(name = "规格类型", width = 15)
    @ApiModelProperty(value = "规格类型")
    private java.lang.String guigexinghao;
    /**材料字典的唯一id*/
    @Excel(name = "材料字典的唯一id", width = 15)
    @ApiModelProperty(value = "材料字典的唯一id")
    private java.lang.String guid;
    /**材料类型*/
    @Excel(name = "材料类型", width = 15)
    @ApiModelProperty(value = "材料类型")
    private java.lang.String nodeType;
    /**材料数量*/
    @Excel(name = "材料数量", width = 15)
    @ApiModelProperty(value = "材料数量")
    private java.lang.String materialNumber;
    /**单个材料重量*/
    @Excel(name = "单个材料重量", width = 15)
    @ApiModelProperty(value = "单个材料重量")
    private java.lang.Double weight;
    /**材料总重量*/
    @Excel(name = "材料总重量", width = 15)
    @ApiModelProperty(value = "材料总重量")
    private java.lang.Double totalWeight;
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
    /**所属单位*/
    @Excel(name = "所属单位", width = 15)
    @ApiModelProperty(value = "所属单位")
    private java.lang.String orgName;
    @ApiModelProperty(value = "开始时间", required = true, dataType = "String")
    private String startDate;

    @ApiModelProperty(value = "结束时间", required = true, dataType = "String")
    private String endDate;
}
