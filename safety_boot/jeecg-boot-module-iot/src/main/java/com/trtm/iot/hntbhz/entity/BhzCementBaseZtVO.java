package com.trtm.iot.hntbhz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 中铁
 * @Author: jeecg-boot
 * @Date: 2021-02-05
 * @Version: V1.0
 */
@Data
public class BhzCementBaseZtVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Integer id;
    /**
     * 设备编号
     */
    @Excel(name = "设备编号", width = 15)
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    @ApiModelProperty(value = "设备编号")
    private String shebeiNo;
    /**
     * 方量
     */
    @Excel(name = "方量", width = 15)
    @ApiModelProperty(value = "方量")
    private Double estimateNumber;
    /**
     * 出料时间
     */
    @Excel(name = "出料时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出料时间")
    private Date productDatetime;

    /**材料类别*/
    @Excel(name = "材料类别", width = 15)
    @ApiModelProperty(value = "材料类别")
    private Integer materialeType;
    /**材料uuid*/
    @Excel(name = "材料uuid", width = 15)
    @ApiModelProperty(value = "材料uuid")
    private String materialeId;
    /**材料名*/
    @Excel(name = "材料名", width = 15)
    @ApiModelProperty(value = "材料名")
    private String materialeName;
    /**实际用量*/
    @Excel(name = "实际用量", width = 15)
    @ApiModelProperty(value = "实际用量")
    private Double realityNumber;
    /**理论用量*/
    @Excel(name = "理论用量", width = 15)
    @ApiModelProperty(value = "理论用量")
    private Double theoryNumber;
    /**误差值*/
    @Excel(name = "误差值", width = 15)
    @ApiModelProperty(value = "误差值")
    private Double errorValue;

    //实际
    private Double realitysesuis;//10-20碎石
    private Double realitywssuis;//5-10碎石
    private Double realityhesha;//河沙
    private Double realityshuini;//水泥
    private Double realityfenmeihui;//粉煤灰
    private Double realityjianshuiji;//减水剂
    private Double realityshui;//水

    //理论
    private Double theorysesuis;//10-20碎石
    private Double theorywssuis;//5-10碎石
    private Double theoryhesha;//河沙
    private Double theoryshuini;//水泥
    private Double theoryfenmeihui;//粉煤灰
    private Double theoryjianshuiji;//减水剂
    private Double theoryshui;//水

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy年MM月dd日")
    @DateTimeFormat(pattern="yyyy年MM月dd日")
    private Date productDatetime_begin;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy年MM月dd日")
    @DateTimeFormat(pattern="yyyy年MM月dd日")
    private Date productDatetime_end;

    private String timepj;

    private Double realityt1;//水泥
    private Double realityt2;//粉煤灰
    private Double realityt3;//减水剂
    private Double realityt4;//水

    private Double theoryt1;//水泥
    private Double theoryt2;//粉煤灰
    private Double theoryt3;//减水剂
    private Double theoryt4;//水
}
