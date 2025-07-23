package com.trtm.iot.syj.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
@ApiModel(value = "YJDateVo", description = "预警数据返回字段")
public class YJDateVo {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;
    /**
     * 项目编号
     */
    @Excel(name = "项目编号", width = 15)
    @ApiModelProperty(value = "项目编号")
    private String sysOrgCode;
    /**
     * 项目名称
     */
    @Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private String orgCodeName;
    /**
     * 设备编号
     */
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String sbbh;
    /**
     * 试验日期
     */
    @Excel(name = "试验日期", width = 15)
    @ApiModelProperty(value = "试验日期")
    private String syrq;
    /**
     * 闭合状态
     */
    @Excel(name = "闭合状态", width = 15)
    @ApiModelProperty(value = "闭合状态")
    private String overproofStatus;
    /**
     * 预警内容
     */
    @Excel(name = "预警内容", width = 15)
    @ApiModelProperty(value = "预警内容")
    private String yjinfo;
}
