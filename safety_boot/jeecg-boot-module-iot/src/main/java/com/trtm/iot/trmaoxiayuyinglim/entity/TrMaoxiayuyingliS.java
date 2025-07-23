package com.trtm.iot.trmaoxiayuyinglim.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 锚下预应力张拉子表
 * @Author: jeecg-boot
 * @Date:   2024-03-12
 * @Version: V1.0
 */
@ApiModel(value="tr_maoxiayuyingli_s对象", description="锚下预应力张拉子表")
@Data
@TableName("tr_maoxiayuyingli_s")
public class TrMaoxiayuyingliS implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**对应主表id*/
    @ApiModelProperty(value = "对应主表id")
    private java.lang.String uuid;
	/**束号*/
	@Excel(name = "束号", width = 15)
    @ApiModelProperty(value = "束号")
    private java.lang.String gsh;
	/**孔号*/
	@Excel(name = "孔号", width = 15)
    @ApiModelProperty(value = "孔号")
    private java.lang.String kh;
	/**设计力值*/
	@Excel(name = "设计力值", width = 15)
    @ApiModelProperty(value = "设计力值")
    private java.lang.String sjlz;
	/**张拉力拐点（实测有效应力）*/
	@Excel(name = "张拉力拐点（实测有效应力）", width = 15)
    @ApiModelProperty(value = "张拉力拐点（实测有效应力）")
    private java.lang.String yxlz;
	/**警戒值*/
	@Excel(name = "警戒值", width = 15)
    @ApiModelProperty(value = "警戒值")
    private java.lang.String jjz;
	/**标准有效应力*/
	@Excel(name = "标准有效应力", width = 15)
    @ApiModelProperty(value = "标准有效应力")
    private java.lang.String bzyxlz;
	/**同束平均值*/
	@Excel(name = "同束平均值", width = 15)
    @ApiModelProperty(value = "同束平均值")
    private java.lang.String avg;
	/**有效预应力相对偏差*/
	@Excel(name = "有效预应力相对偏差", width = 15)
    @ApiModelProperty(value = "有效预应力相对偏差")
    private java.lang.String piancha;
	/**同断面不均匀度*/
	@Excel(name = "同断面不均匀度", width = 15)
    @ApiModelProperty(value = "同断面不均匀度")
    private java.lang.String bjjd;
	/**千斤顶工作长度*/
	@Excel(name = "千斤顶工作长度", width = 15)
    @ApiModelProperty(value = "千斤顶工作长度")
    private java.lang.String qjdcd;
	/**钢绞线长度*/
	@Excel(name = "钢绞线长度", width = 15)
    @ApiModelProperty(value = "钢绞线长度")
    private java.lang.String gjxcd;
	/**弹性模量*/
	@Excel(name = "弹性模量", width = 15)
    @ApiModelProperty(value = "弹性模量")
    private java.lang.String txml;
	/**公称截面积*/
	@Excel(name = "公称截面积", width = 15)
    @ApiModelProperty(value = "公称截面积")
    private java.lang.String gcjmj;
	/**张拉日期*/
	@Excel(name = "张拉日期", width = 15)
    @ApiModelProperty(value = "张拉日期")
    private java.lang.String zlrq;
	/**曲线*/
	@Excel(name = "曲线", width = 15)
    @ApiModelProperty(value = "曲线")
    private java.lang.String qx;
	/**设计砼强度（强度等级）*/
	@Excel(name = "设计砼强度（强度等级）", width = 15)
    @ApiModelProperty(value = "设计砼强度（强度等级）")
    private java.lang.String qddj;
	/**超标等级(0合格，123，初中高  默认为0）*/
	@Excel(name = "超标等级(0合格，123，初中高  默认为0）", width = 15)
    @ApiModelProperty(value = "超标等级(0合格，123，初中高  默认为0）")
    private java.lang.Integer overLevel;
}
