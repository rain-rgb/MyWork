package com.trtm.iot.wzyclpidaichen.entity;

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
 * @Description: wzyclpidaichen
 * @Author: jeecg-boot
 * @Date:   2024-04-18
 * @Version: V1.0
 */
@Data
@TableName("wzyclpidaichen")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wzyclpidaichen对象", description="wzyclpidaichen")
public class Wzyclpidaichen implements Serializable {
    private static final long serialVersionUID = 1L;

	/**进场过磅id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "进场过磅id")
    private java.lang.Integer id;
	/**进出材料单*/
	@Excel(name = "进出材料单", width = 15)
    @ApiModelProperty(value = "进出材料单")
    private java.lang.String jinchuliaodanno;
	/**材料编号*/
	@Excel(name = "材料编号", width = 15)
    @ApiModelProperty(value = "材料编号")
    private java.lang.String cailiaono;
	/**进场时间*/
	@Excel(name = "进场时间", width = 15)
    @ApiModelProperty(value = "进场时间")
    private java.lang.String jinchangshijian;
	/**出场时间*/
	@Excel(name = "出场时间", width = 15)
    @ApiModelProperty(value = "出场时间")
    private java.lang.String chuchangshijian;
	/**净重*/
	@Excel(name = "净重", width = 15)
    @ApiModelProperty(value = "净重")
    private java.lang.String jingzhong;
	/**材料规格*/
	@Excel(name = "材料规格", width = 15)
    @ApiModelProperty(value = "材料规格")
    private java.lang.String cailiaoguige;
	/**remark*/
	@Excel(name = "remark", width = 15)
    @ApiModelProperty(value = "remark")
    private java.lang.String remark;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeibianhao;
	/**材料名称*/
	@Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private java.lang.String cailiaoname;
	/**料仓编号*/
	@Excel(name = "料仓编号", width = 15)
    @ApiModelProperty(value = "料仓编号")
    private java.lang.String lcbianhao;
	/**料仓名称*/
	@Excel(name = "料仓名称", width = 15)
    @ApiModelProperty(value = "料仓名称")
    private java.lang.String liaocangname;
	/**唯一标识*/
	@Excel(name = "唯一标识", width = 15)
    @ApiModelProperty(value = "唯一标识")
    private java.lang.String guid;
	/**时间戳*/
	@Excel(name = "时间戳", width = 15)
    @ApiModelProperty(value = "时间戳")
    private java.lang.Integer ts;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
    @ApiModelProperty(value = "是否删除")
    private java.lang.String isdel;
	/**status*/
	@Excel(name = "status", width = 15)
    @ApiModelProperty(value = "status")
    private java.lang.String status;
	/**是否统计 默认为0:未统计，1:已统计,15:统计出错*/
	@Excel(name = "是否统计 默认为0:未统计，1:已统计,15:统计出错", width = 15)
    @ApiModelProperty(value = "是否统计 默认为0:未统计，1:已统计,15:统计出错")
    private java.lang.Integer istongji;
    private java.lang.Integer statuscode;
}
