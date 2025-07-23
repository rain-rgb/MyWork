package com.trtm.iot.bhzStatistics.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 拌合站统计表
 * @Author: jeecg-boot
 * @Date:   2021-03-22
 * @Version: V1.0
 */
@Data
@TableName("bhz_cement_statistics")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bhz_cement_statistics对象", description="拌合站统计表")
public class BhzCementStatisticsDC implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String shebeiNo;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private String projectName;
	/**施工地点*/
	@Excel(name = "施工地点", width = 15)
    @ApiModelProperty(value = "施工地点")
    private String jobLocation;
	/**浇筑部位*/
	@Excel(name = "浇筑部位", width = 15)
    @ApiModelProperty(value = "浇筑部位")
    private String poureLocation;
	/**配方号*/
	@Excel(name = "配方号", width = 15)
    @ApiModelProperty(value = "配方号")
    private String formulaNo;
	/**强度等级*/
	@Excel(name = "强度等级", width = 15)
    @ApiModelProperty(value = "强度等级")
    private String strengthRank;
	/**初级超标多少盘*/
	@Excel(name = "初级超标多少盘", width = 15)
    @ApiModelProperty(value = "初级超标多少盘")
    private Integer primaryDish;
	/**初级处置多少盘*/
	@Excel(name = "初级处置多少盘", width = 15)
    @ApiModelProperty(value = "初级处置多少盘")
    private Integer primaryHandle;
	/**初级处置百分点*/
	@Excel(name = "初级处置百分点", width = 15)
    @ApiModelProperty(value = "初级处置百分点")
    private Double primaryPercent;
	/**中级超标多少盘*/
	@Excel(name = "中级超标多少盘", width = 15)
    @ApiModelProperty(value = "中级超标多少盘")
    private Integer middleDish;
	/**中级处置多少盘*/
	@Excel(name = "中级处置多少盘", width = 15)
    @ApiModelProperty(value = "中级处置多少盘")
    private Integer middleHandle;
	/**中级处置百分点*/
	@Excel(name = "中级处置百分点", width = 15)
    @ApiModelProperty(value = "中级处置百分点")
    private Double middlePercent;
	/**高级超标多少盘*/
	@Excel(name = "高级超标多少盘", width = 15)
    @ApiModelProperty(value = "高级超标多少盘")
    private Integer advancedDish;
	/**高级处置多少盘*/
	@Excel(name = "高级处置多少盘", width = 15)
    @ApiModelProperty(value = "高级处置多少盘")
    private Integer advancedHandle;
	/**高级处置百分点*/
	@Excel(name = "高级处置百分点", width = 15)
    @ApiModelProperty(value = "高级处置百分点")
    private Double advancedPercent;
	/**总盘数*/
	@Excel(name = "总盘数", width = 15)
    @ApiModelProperty(value = "总盘数")
    private Integer allDish;
	/**总超标盘数*/
	@Excel(name = "总超标盘数", width = 15)
    @ApiModelProperty(value = "总超标盘数")
    private Integer allOverproofDish;
	/**总处理盘数*/
	@Excel(name = "总处理盘数", width = 15)
    @ApiModelProperty(value = "总处理盘数")
    private Integer allHandleDish;
	/**总体超标处置率*/
	@Excel(name = "总体超标处置率", width = 15)
    @ApiModelProperty(value = "总体超标处置率")
    private Double allPercent;
	/**方量*/
	@Excel(name = "方量", width = 15)
    @ApiModelProperty(value = "方量")
    private Double estimateNumber;
	/**统计时间，以天为单位*/
	@Excel(name = "统计时间，以天为单位", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "统计时间，以天为单位")
    private Date statisticsTime;
    /**是否进行原材料消耗统计*/
    @Excel(name = "是否进行原材料消耗统计", width = 15)
    @ApiModelProperty(value = "是否进行原材料消耗统计")
    private Integer istongji;

    private Double shi1rea;
    private Double shi1the;
    private Double shi2rea;
    private Double shi2the;
    private Double shi3rea;
    private Double shi3the;
    private Double shi4rea;
    private Double shi4the;
    private Double shi5rea;
    private Double shi5the;
    private Double shi6rea;
    private Double shi6the;
    private Double shini1rea;
    private Double shini1the;
    private Double shini2rea;
    private Double shini2the;
    private Double shini3rea;
    private Double shini3the;
    private Double kf1rea;
    private Double kf1the;
    private Double fmh1rea;
    private Double fmh1the;
    private Double shui1rea;
    private Double shui1the;
    private Double shui2rea;
    private Double shui2the;
    private Double shui3rea;
    private Double shui3the;
    private Double wjj1rea;
    private Double wjj1the;
    private Double wjj2rea;
    private Double wjj2the;
    private Double wjj3rea;
    private Double wjj3the;
    private Double wjj4rea;
    private Double wjj4the;

}
