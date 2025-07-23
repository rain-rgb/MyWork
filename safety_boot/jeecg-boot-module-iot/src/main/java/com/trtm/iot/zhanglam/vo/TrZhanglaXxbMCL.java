package com.trtm.iot.zhanglam.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.entity.ZhanglaYajiangOverHandler;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
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
public class TrZhanglaXxbMCL {
    /**主键*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;
    /**唯一码*/
    @Excel(name = "唯一码", width = 15)
    @ApiModelProperty(value = "唯一码")
    private String fguid;
    private String gsbh;
    /**主键*/
    @Excel(name = "主键", width = 15)
    @ApiModelProperty(value = "主键")
    private String syjid;
    /**设备编号*/
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String shebeibianhao;
    /**张拉时间*/
    @Excel(name = "张拉时间", width = 15)
    @ApiModelProperty(value = "张拉时间")
    private String zlsj;
    @Excel(name = "设计张拉力 KN", width = 15)
    @ApiModelProperty(value = "设计张拉力 KN")
    private String sjzll;
    /**回弹量mm*/
    @Excel(name = "回弹量mm", width = 15)
    @ApiModelProperty(value = "回弹量mm")
    private String htl;
    /**总伸长量mm*/
    @Excel(name = "总伸长量mm", width = 15)
    @ApiModelProperty(value = "总伸长量mm")
    private String zscl;
    /**理论伸长量mm*/
    @Excel(name = "理论伸长量mm", width = 15)
    @ApiModelProperty(value = "理论伸长量mm")
    private String llscl;
    /**延伸量误差%*/
    @Excel(name = "延伸量误差%", width = 15)
    @ApiModelProperty(value = "延伸量误差%")
    private String sclper;
    @Excel(name = "超张百分比", width = 15)
    @ApiModelProperty(value = "超张百分比")
    private String zzlb;
    /**记录时间*/
    @Excel(name = "记录时间", width = 15)
    @ApiModelProperty(value = "记录时间")
    private String jlsj;
    /**是否合格*/
    @Excel(name = "是否合格", width = 15)
    @ApiModelProperty(value = "是否合格")
    private Integer hege;
    /**钢束股数*/
    @Excel(name = "钢束股数", width = 15)
    @ApiModelProperty(value = "钢束股数")
    private String gsgs;
    /**弹性模量*/
    @Excel(name = "弹性模量", width = 15)
    @ApiModelProperty(value = "弹性模量")
    private String txml;
    /**允许偏差值*/
    @Excel(name = "允许偏差值", width = 15)
    @ApiModelProperty(value = "允许偏差值")
    private String yxpc;
    /**预张拉百分比*/
    @Excel(name = "预张拉百分比", width = 15)
    @ApiModelProperty(value = "预张拉百分比")
    private String yzlb;
    /**初张拉百分比*/
    @Excel(name = "初张拉百分比", width = 15)
    @ApiModelProperty(value = "初张拉百分比")
    private String czlb;
    private java.lang.Integer overproofStatus;
    private List<TrZhanglaXxb> trZhanglaXxbList;
    private List<TrZhanglaM> zhanglaMList;
    private ZhanglaYajiangOverHandler zhanglaYajiangOverHandler;
}
