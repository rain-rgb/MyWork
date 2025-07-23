package com.trtm.iot.zhanglam.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

/**
 * \* @author: Xx
 * \* Date: 2021/9/3
 * \* Time: 17:40
 * \* Description:
 * \
 */
@Data
@ApiModel(value="TrZhanglaMS对象", description="张拉孔号以及断面信息表")
public class TrZhanglaMS {
    /**主键*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
    /**唯一码*/
    @Excel(name = "唯一码", width = 15)
    @ApiModelProperty(value = "唯一码")
    private java.lang.String fguid;
    private java.lang.String gsbh;
    private java.lang.String uuid;
    private java.lang.String sgsj;
    private java.lang.String snsjqd;
    private java.lang.String snskqd;
    private java.lang.String gsgs;
    private Integer overproofStatus;
    /**张拉时间*/
    @Excel(name = "张拉时间", width = 15)
    @ApiModelProperty(value = "张拉时间")
    private java.lang.String zlsj;
    @Excel(name = "设计张拉力 KN", width = 15)
    @ApiModelProperty(value = "设计张拉力 KN")
    private java.lang.String sjzll;
    /**回弹量mm*/
    @Excel(name = "回弹量mm", width = 15)
    @ApiModelProperty(value = "回弹量mm")
    private java.lang.String htl;
    /**总伸长量mm*/
    @Excel(name = "总伸长量mm", width = 15)
    @ApiModelProperty(value = "总伸长量mm")
    private java.lang.String zscl;
    /**理论伸长量mm*/
    @Excel(name = "理论伸长量mm", width = 15)
    @ApiModelProperty(value = "理论伸长量mm")
    private java.lang.String llscl;
    /**延伸量误差%*/
    @Excel(name = "延伸量误差%", width = 15)
    @ApiModelProperty(value = "延伸量误差%")
    private java.lang.String sclper;
    @Excel(name = "超张百分比", width = 15)
    @ApiModelProperty(value = "超张百分比")
    private java.lang.String zzlb;
    /**记录时间*/
    @Excel(name = "记录时间", width = 15)
    @ApiModelProperty(value = "记录时间")
    private java.lang.String jlsj;

    private List<TrZhanglaS> trZhanglaSList;

    private TrZhanglaM trZhanglaMList;

    @Excel(name = "孔号", width = 15)
    @ApiModelProperty(value = "孔号")
    private String dh;
    @Excel(name = "阶段行程百分比%", width = 15)
    @ApiModelProperty(value = "阶段行程百分比%")
    private String jdbfb;
    @Excel(name = "阶段张拉力KN", width = 15)
    @ApiModelProperty(value = "阶段张拉力KN")
    private String jdzll;
    @Excel(name = "持荷时间", width = 15)
    @ApiModelProperty(value = "持荷时间")
    private String chsj;
    @Excel(name = "合格", width = 15)
    @ApiModelProperty(value = "hege")
    private String hege;

}
