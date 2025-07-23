package com.trtm.iot.trmaoxiayuyinglim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: 锚下预应力张拉主表
 * @Author: jeecg-boot
 * @Date:   2024-03-12
 * @Version: V1.0
 */
@ApiModel(value="tr_maoxiayuyingli_m对象", description="锚下预应力张拉主表")
@Data
@TableName("tr_maoxiayuyingli_m")
public class TrMaoxiayuyingliM implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备厂家*/
	@Excel(name = "设备厂家", width = 15)
    @ApiModelProperty(value = "设备厂家")
    private java.lang.String shebeichangjia;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    @ApiModelProperty(value = "设备编号")
    private java.lang.String sbbh;
	/**单位工程*/
	@Excel(name = "单位工程", width = 15)
    @ApiModelProperty(value = "单位工程")
    private java.lang.String dwgc;
	/**单位工程*/
	/**分部工程*/
	@Excel(name = "分部工程", width = 15)
    @ApiModelProperty(value = "分部工程")
    private java.lang.String fbgc;
	/**分部工程*/
	/**分项工程*/
	@Excel(name = "分项工程", width = 15)
    @ApiModelProperty(value = "分项工程")
    private java.lang.String fxgc;
	/**构件编号*/
	@Excel(name = "构件编号", width = 15)
    @ApiModelProperty(value = "构件编号")
    private java.lang.String gjbh;
	/**检测日期*/
	@Excel(name = "检测日期", width = 15)
    @ApiModelProperty(value = "检测日期")
    private java.lang.String jcrq;
	/**检测内容*/
	@Excel(name = "检测内容", width = 15)
    @ApiModelProperty(value = "检测内容")
    private java.lang.String jcnr;
	/**设计力值*/
	@Excel(name = "设计力值", width = 15)
    @ApiModelProperty(value = "设计力值")
    private java.lang.String sjlz;
	/**孔道钢绞线束数*/
	@Excel(name = "孔道钢绞线束数", width = 15)
    @ApiModelProperty(value = "孔道钢绞线束数")
    private java.lang.String kdgs;
	/**设计砼强度（强度等级）*/
	@Excel(name = "设计砼强度（强度等级）", width = 15)
    @ApiModelProperty(value = "设计砼强度（强度等级）")
    private java.lang.String sjqd;
	/**超标等级(0合格，123，初中高  默认为0）*/
	@Excel(name = "超标等级(0合格，123，初中高  默认为0）", width = 15)
    @ApiModelProperty(value = "超标等级(0合格，123，初中高  默认为0）")
//    @Dict(dicCode = "over_level")
    private java.lang.Integer overLevel;
	/**唯一id*/
	@Excel(name = "唯一id", width = 15)
    @ApiModelProperty(value = "唯一id")
    private java.lang.String uuid;
	/**梁号*/
	@Excel(name = "梁号", width = 15)
    @ApiModelProperty(value = "梁号")
    private java.lang.String lh;
    private java.lang.String type;
    private java.lang.String serialno;
    private java.lang.Integer overproofStatus;
}
