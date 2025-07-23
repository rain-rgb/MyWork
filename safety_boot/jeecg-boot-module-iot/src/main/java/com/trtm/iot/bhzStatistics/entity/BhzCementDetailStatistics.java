package com.trtm.iot.bhzStatistics.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: bhz_cement_detail_statistics
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
@Data
@TableName("bhz_cement_detail_statistics")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bhz_cement_detail_statistics对象", description="bhz_cement_detail_statistics")
public class BhzCementDetailStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**与拌合机生产数据统计表（bhz_cement_statistics）的id关联*/
	@Excel(name = "与拌合机生产数据统计表（bhz_cement_statistics）的id关联", width = 15)
    @ApiModelProperty(value = "与拌合机生产数据统计表（bhz_cement_statistics）的id关联")
    private Integer csId;
	/**材料类别*/
	@Excel(name = "材料类别", width = 15)
    @ApiModelProperty(value = "材料类别")
    private Integer materialeType;
	/**材料名*/
	@Excel(name = "材料名", width = 15)
    @ApiModelProperty(value = "材料名")
    private String materialeName;
	/**这种材料真实用量总和*/
	@Excel(name = "这种材料真实用量总和", width = 15)
    @ApiModelProperty(value = "这种材料真实用量总和")
    private Double realityNumber;
	/**这种材料的理论用量总和*/
	@Excel(name = "这种材料的理论用量总和", width = 15)
    @ApiModelProperty(value = "这种材料的理论用量总和")
    private Double theoryNumber;
	/**初级超标次数*/
	@Excel(name = "初级超标次数", width = 15)
    @ApiModelProperty(value = "初级超标次数")
    private Integer primaryNumber;
	/**中级超标次数*/
	@Excel(name = "中级超标次数", width = 15)
    @ApiModelProperty(value = "中级超标次数")
    private Integer middleNumber;
	/**高级超标次数*/
	@Excel(name = "高级超标次数", width = 15)
    @ApiModelProperty(value = "高级超标次数")
    private Integer advancedNumber;
	/**cailiaoid*/
	@Excel(name = "cailiaoid", width = 15)
    @ApiModelProperty(value = "cailiaoid")
    private String cailiaoid;
    /**设备编号*/
    @Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String shebeiNo;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "统计时间，以天为单位")
    private Date statisticsTime;
}
