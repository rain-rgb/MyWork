package com.trtm.iot.hntbhz.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lis1
 * @date 2022/11/8
 * @time 10:43
 */
@ApiModel(value="混凝土用量统计", description="Excel导出")
@Data
public class CementUsageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private String shebeiNo;

    @Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private String projectName;

    @Excel(name = "强度等级", width = 15)
    @ApiModelProperty(value = "强度等级")
    private String strength;

    @Excel(name = "总方量")
    @ApiModelProperty(value = "总方量")
    private Double totalCount;

    @Excel(name = "出料日期", width = 15)
    @ApiModelProperty(value = "出料时间")
    private String productTime;
}
