package com.trtm.iot.trhnthtm.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.trhnthts.entity.TrHnthtS;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * \* @author: Xx
 * \* Date: 2021/9/14
 * \* Time: 10:36
 * \* Description:
 * \
 */
@Data
public class TrHnthtMPage {
    /**主键*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
    /**构件序号*/
    @Excel(name = "构件序号", width = 15)
    @ApiModelProperty(value = "构件序号")
    private java.lang.String point;
    /**检测时间*/
    @Excel(name = "检测时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "检测时间")
    private java.util.Date checktime;
    /**检测位置*/
    @Excel(name = "检测位置", width = 15)
    @ApiModelProperty(value = "检测位置")
    private java.lang.String checklocation;
    /**检测ID*/
    @Excel(name = "检测ID", width = 15)
    @ApiModelProperty(value = "检测ID")
    private java.lang.String testid;
    /**检测人*/
    @Excel(name = "检测人", width = 15)
    @ApiModelProperty(value = "检测人")
    private java.lang.String testerpeople;
    /**构件部位*/
    @Excel(name = "构件部位", width = 15)
    @ApiModelProperty(value = "构件部位")
    private java.lang.String place;
    /**强度等级*/
    @Excel(name = "强度等级", width = 15)
    @ApiModelProperty(value = "强度等级")
    private java.lang.String strength;
    /**测区数量*/
    @Excel(name = "测区数量", width = 15)
    @ApiModelProperty(value = "测区数量")
    private java.lang.Integer testingcount;
    /**检测面*/
    @Excel(name = "检测面", width = 15)
    @ApiModelProperty(value = "检测面")
    private java.lang.String detectionsurface;
    /**浇筑面 0：浇筑侧面 1：浇筑底面*/
    @Excel(name = "浇筑面 0：浇筑侧面 1：浇筑底面", width = 15)
    @ApiModelProperty(value = "浇筑面 0：浇筑侧面 1：浇筑底面")
    private java.lang.Integer pouringsurface;
    /**标准碳化深度值*/
    @Excel(name = "标准碳化深度值", width = 15)
    @ApiModelProperty(value = "标准碳化深度值")
    private java.lang.Double carbonizedepth;
    /**检测角度*/
    @Excel(name = "检测角度", width = 15)
    @ApiModelProperty(value = "检测角度")
    private java.lang.String detectionangle;
    /**是否泵送   是/否*/
    @Excel(name = "是否泵送   是/否", width = 15)
    @ApiModelProperty(value = "是否泵送   是/否")
    private java.lang.String ispumping;
    /**检测依据*/
    @Excel(name = "检测依据", width = 15)
    @ApiModelProperty(value = "检测依据")
    private java.lang.String detectionstandard;
    /**浇筑日期*/
    @Excel(name = "浇筑日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "浇筑日期")
    private java.util.Date pouringdate;
    /**测试最小数值*/
    @Excel(name = "测试最小数值", width = 15)
    @ApiModelProperty(value = "测试最小数值")
    private java.lang.Double testingmin;
    /**测区平均数值*/
    @Excel(name = "测区平均数值", width = 15)
    @ApiModelProperty(value = "测区平均数值")
    private java.lang.Double testingaverage;
    /**标准差*/
    @Excel(name = "标准差", width = 15)
    @ApiModelProperty(value = "标准差")
    private java.lang.Double standarddeviation;
    /**推定值*/
    @Excel(name = "推定值", width = 15)
    @ApiModelProperty(value = "推定值")
    private java.lang.Double estimatedvalue;
    /**测区最小换算值*/
    @Excel(name = "测区最小换算值", width = 15)
    @ApiModelProperty(value = "测区最小换算值")
    private java.lang.String showmin;
    /**测区平均换算值*/
    @Excel(name = "测区平均换算值", width = 15)
    @ApiModelProperty(value = "测区平均换算值")
    private java.lang.String showaverage;
    /**标准差*/
    @Excel(name = "标准差", width = 15)
    @ApiModelProperty(value = "标准差")
    private java.lang.String showstandarddev;
    /**推定值*/
    @Excel(name = "推定值", width = 15)
    @ApiModelProperty(value = "推定值")
    private java.lang.String showestimatedval;
    /**任务单编号*/
    @Excel(name = "任务单编号", width = 15)
    @ApiModelProperty(value = "任务单编号")
    private java.lang.String code;
    /**试验设备编号*/
    @Excel(name = "试验设备编号", width = 15)
    @ApiModelProperty(value = "试验设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeiNo;
    /**项目名称*/
    @Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projectname;
    /**施工部位*/
    @Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    private java.lang.String sgbw;

    private List<TrHnthtS> trHnthtS;
}
