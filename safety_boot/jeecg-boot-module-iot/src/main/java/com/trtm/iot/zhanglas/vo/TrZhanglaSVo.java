package com.trtm.iot.zhanglas.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: 张拉信息子表
 * @Author: jeecg-boot
 * @Date:   2021-08-31
 * @Version: V1.0
 */
@Data
@TableName("tr_zhangla_s")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tr_zhangla_s对象", description="张拉信息子表")
public class TrZhanglaSVo implements Serializable ,Cloneable {
    private static final long serialVersionUID = 1L;


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
    private String overReason;// 预警原因
    private Integer overLevel;//超标等级

    /**sid*/
	@Excel(name = "sid", width = 15)
    @ApiModelProperty(value = "sid")
    private String sid;
	/**张拉主表主键*/
	@Excel(name = "张拉主表主键", width = 15)
    @ApiModelProperty(value = "张拉主表主键")
    private String fguid;
	/**张拉信息表主键*/
	@Excel(name = "张拉信息表主键", width = 15)
    @ApiModelProperty(value = "张拉信息表主键")
    private String syjid;
	/**主表的钢束编号*/
	@Excel(name = "主表的钢束编号", width = 15)
    @ApiModelProperty(value = "主表的钢束编号")
    private String gsbh;
    /**钢束股数*/
    @Excel(name = "钢束股数", width = 15)
    @ApiModelProperty(value = "钢束股数")
    private java.lang.String gsgs;
    /**是否合格*/
    @Excel(name = "是否合格", width = 15)
    @ApiModelProperty(value = "是否合格")
    private java.lang.String hege;
	/**阶段行程百分比%*/
	@Excel(name = "阶段行程百分比%", width = 15)
    @ApiModelProperty(value = "阶段行程百分比%")
    private String jdbfb10;
    private String jdbfb20;
    private String jdbfb50I;
    private String jdbfb50II;
    private String jdbfb100;

    // 检测类型(张拉力(KN)，油压(Mpa)，伸长量(mm))
    private String recodePoint;

//	/**油表读数*/
//	@Excel(name = "油表读数", width = 15)
//    @ApiModelProperty(value = "油表读数")
//    private String ybds;
//	/**阶段张拉力KN*/
//	@Excel(name = "阶段张拉力KN", width = 15)
//    @ApiModelProperty(value = "阶段张拉力KN")
//    private String jdzll;
//	/**阶段伸长量mm*/
//	@Excel(name = "阶段伸长量mm", width = 15)
//    @ApiModelProperty(value = "阶段伸长量mm")
//    private String jdscl;
	/**张拉断面*/
	@Excel(name = "张拉断面", width = 15)
    @ApiModelProperty(value = "张拉断面")
    private String dh;


	/**力筋回缩量*/
	@Excel(name = "力筋回缩量", width = 15)
    @ApiModelProperty(value = "力筋回缩量")
    private String ljhsl;
	/**单侧伸长量*/
	@Excel(name = "单侧伸长量", width = 15)
    @ApiModelProperty(value = "单侧伸长量")
    private String dcscl;
	/**完成状态(0为未完成,状态为1代表*/
	@Excel(name = "完成状态(0为未完成,状态为1代表", width = 15)
    @ApiModelProperty(value = "完成状态(0为未完成,状态为1代表")
    private String status;
	/**持荷时间*/
	@Excel(name = "持荷时间", width = 15)
    @ApiModelProperty(value = "持荷时间")
    private String chsj;

    @Excel(name = "holeid", width = 15)
    @ApiModelProperty(value = "holeid")
    private java.lang.String holeid;

    private java.lang.String shebeibianhao;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    


}
