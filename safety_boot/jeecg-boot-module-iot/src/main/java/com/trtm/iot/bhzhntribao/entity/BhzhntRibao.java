package com.trtm.iot.bhzhntribao.entity;

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
 * @Description: bhzhnt_ribao
 * @Author: jeecg-boot
 * @Date: 2023-10-24
 * @Version: V1.0
 */
@Data
@TableName("bhzhnt_ribao")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "bhzhnt_ribao对象", description = "bhzhnt_ribao")
public class BhzhntRibao implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 拌和站混凝土生产调度日报id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "拌和站混凝土生产调度日报id")
    private java.lang.Integer id;
    /**
     * 单位工程
     */
    @Excel(name = "单位工程", width = 15)
    @ApiModelProperty(value = "单位工程")
    private java.lang.String danweiProj;
    /**
     * 分部工程
     */
    @Excel(name = "分部工程", width = 15)
    @ApiModelProperty(value = "分部工程")
    private java.lang.String fenbuProj;
    /**
     * 分项工程
     */
    @Excel(name = "分项工程", width = 15)
    @ApiModelProperty(value = "分项工程")
    private java.lang.String fenxiangProj;
    /**
     * 单位
     */
    @Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    private java.lang.String danwei;
    /**
     * 总控数量
     */
    @Excel(name = "总控数量", width = 15)
    @ApiModelProperty(value = "总控数量")
    private java.lang.String zongkongNum;
    /**
     * 日完成量
     */
    @Excel(name = "日完成量", width = 15)
    @ApiModelProperty(value = "日完成量")
    private java.lang.String dayFinish;
    /**
     * 月完成量
     */
    @Excel(name = "月完成量", width = 15)
    @ApiModelProperty(value = "月完成量")
    private java.lang.String monthFinish;
    /**
     * 年完成量
     */
    @Excel(name = "年完成量", width = 15)
    @ApiModelProperty(value = "年完成量")
    private java.lang.String yearFinish;
    /**
     * 开累
     */
    @Excel(name = "开累", width = 15)
    @ApiModelProperty(value = "开累")
    private java.lang.String allFinish;
    /**
     * 剩余量
     */
    @Excel(name = "剩余量", width = 15)
    @ApiModelProperty(value = "剩余量")
    private java.lang.String residue;
    /**
     * 1号完成量
     */
    @Excel(name = "1号完成量", width = 15)
    @ApiModelProperty(value = "1号完成量")
    private java.lang.Integer day1;
    /**
     * 2号完成量
     */
    @Excel(name = "2号完成量", width = 15)
    @ApiModelProperty(value = "2号完成量")
    private java.lang.Integer day2;
    /**
     * 3号完成量
     */
    @Excel(name = "3号完成量", width = 15)
    @ApiModelProperty(value = "3号完成量")
    private java.lang.Integer day3;
    /**
     * 4号完成量
     */
    @Excel(name = "4号完成量", width = 15)
    @ApiModelProperty(value = "4号完成量")
    private java.lang.Integer day4;
    /**
     * 5号完成量
     */
    @Excel(name = "5号完成量", width = 15)
    @ApiModelProperty(value = "5号完成量")
    private java.lang.Integer day5;
    /**
     * 6号完成量
     */
    @Excel(name = "6号完成量", width = 15)
    @ApiModelProperty(value = "6号完成量")
    private java.lang.Integer day6;
    /**
     * 7号完成量
     */
    @Excel(name = "7号完成量", width = 15)
    @ApiModelProperty(value = "7号完成量")
    private java.lang.Integer day7;
    /**
     * 8号完成量
     */
    @Excel(name = "8号完成量", width = 15)
    @ApiModelProperty(value = "8号完成量")
    private java.lang.Integer day8;
    /**
     * 9号完成量
     */
    @Excel(name = "9号完成量", width = 15)
    @ApiModelProperty(value = "9号完成量")
    private java.lang.Integer day9;
    /**
     * 10号完成量
     */
    @Excel(name = "10号完成量", width = 15)
    @ApiModelProperty(value = "10号完成量")
    private java.lang.Integer day10;
    /**
     * 11号完成量
     */
    @Excel(name = "11号完成量", width = 15)
    @ApiModelProperty(value = "11号完成量")
    private java.lang.Integer day11;
    /**
     * 12号完成量
     */
    @Excel(name = "12号完成量", width = 15)
    @ApiModelProperty(value = "12号完成量")
    private java.lang.Integer day12;
    /**
     * 13号完成量
     */
    @Excel(name = "13号完成量", width = 15)
    @ApiModelProperty(value = "13号完成量")
    private java.lang.Integer day13;
    /**
     * 14号完成量
     */
    @Excel(name = "14号完成量", width = 15)
    @ApiModelProperty(value = "14号完成量")
    private java.lang.Integer day14;
    /**
     * 15号完成量
     */
    @Excel(name = "15号完成量", width = 15)
    @ApiModelProperty(value = "15号完成量")
    private java.lang.Integer day15;
    /**
     * 16号完成量
     */
    @Excel(name = "16号完成量", width = 15)
    @ApiModelProperty(value = "16号完成量")
    private java.lang.Integer day16;
    /**
     * 17号完成量
     */
    @Excel(name = "17号完成量", width = 15)
    @ApiModelProperty(value = "17号完成量")
    private java.lang.Integer day17;
    /**
     * 18号完成量
     */
    @Excel(name = "18号完成量", width = 15)
    @ApiModelProperty(value = "18号完成量")
    private java.lang.Integer day18;
    /**
     * 19号完成量
     */
    @Excel(name = "19号完成量", width = 15)
    @ApiModelProperty(value = "19号完成量")
    private java.lang.Integer day19;
    /**
     * 20号完成量
     */
    @Excel(name = "20号完成量", width = 15)
    @ApiModelProperty(value = "20号完成量")
    private java.lang.Integer day20;
    /**
     * 21号完成量
     */
    @Excel(name = "21号完成量", width = 15)
    @ApiModelProperty(value = "21号完成量")
    private java.lang.Integer day21;
    /**
     * 22号完成量
     */
    @Excel(name = "22号完成量", width = 15)
    @ApiModelProperty(value = "22号完成量")
    private java.lang.Integer day22;
    /**
     * 23号完成量
     */
    @Excel(name = "23号完成量", width = 15)
    @ApiModelProperty(value = "23号完成量")
    private java.lang.Integer day23;
    /**
     * 24号完成量
     */
    @Excel(name = "24号完成量", width = 15)
    @ApiModelProperty(value = "24号完成量")
    private java.lang.Integer day24;
    /**
     * 25号完成量
     */
    @Excel(name = "25号完成量", width = 15)
    @ApiModelProperty(value = "25号完成量")
    private java.lang.Integer day25;
    /**
     * 26号完成量
     */
    @Excel(name = "26号完成量", width = 15)
    @ApiModelProperty(value = "26号完成量")
    private java.lang.Integer day26;
    /**
     * 27号完成量
     */
    @Excel(name = "27号完成量", width = 15)
    @ApiModelProperty(value = "27号完成量")
    private java.lang.Integer day27;
    /**
     * 28号完成量
     */
    @Excel(name = "28号完成量", width = 15)
    @ApiModelProperty(value = "28号完成量")
    private java.lang.Integer day28;
    /**
     * 29号完成量
     */
    @Excel(name = "29号完成量", width = 15)
    @ApiModelProperty(value = "29号完成量")
    private java.lang.Integer day29;
    /**
     * 30号完成量
     */
    @Excel(name = "30号完成量", width = 15)
    @ApiModelProperty(value = "30号完成量")
    private java.lang.Integer day30;
    /**
     * 31号完成量
     */
    @Excel(name = "31号完成量", width = 15)
    @ApiModelProperty(value = "31号完成量")
    private java.lang.Integer day31;
    /**
     * 上月年累
     */
    @Excel(name = "上月年累", width = 15)
    @ApiModelProperty(value = "上月年累")
    private java.lang.String lastmonthYearfinish;
    /**
     * 上月开累
     */
    @Excel(name = "上月开累", width = 15)
    @ApiModelProperty(value = "上月开累")
    private java.lang.String lastmonthKaifinish;
    /**
     * 登记人
     */
    @ApiModelProperty(value = "登记人")
    private java.lang.String createBy;
    /**
     * 登记时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "登记时间")
    private java.util.Date createTime;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateBy;
    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
}
