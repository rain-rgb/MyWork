package com.trtm.iot.trmaoxiayuyinglim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.trtm.iot.tr_maoxiayuyingli_over_handler.entity.TrMaoxiayuyingliOverHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 锚下预应力张拉主表
 * @Author: jeecg-boot
 * @Date:   2024-03-12
 * @Version: V1.0
 */
@ApiModel(value="tr_maoxiayuyingli_m对象", description="锚下预应力张拉主表")
@Data
@TableName("tr_maoxiayuyingli_m")
public class TrMaoxiayuyingliMCZ implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**设备厂家*/
	@Excel(name = "设备厂家", width = 15)
    @ApiModelProperty(value = "设备厂家")
    private String shebeichangjia;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    @ApiModelProperty(value = "设备编号")
    private String sbbh;
	/**单位工程*/
	@Excel(name = "单位工程", width = 15)
    @ApiModelProperty(value = "单位工程")
    private String dwgc;
	/**单位工程*/
	/**分部工程*/
	@Excel(name = "分部工程", width = 15)
    @ApiModelProperty(value = "分部工程")
    private String fbgc;
	/**分部工程*/
	/**分项工程*/
	@Excel(name = "分项工程", width = 15)
    @ApiModelProperty(value = "分项工程")
    private String fxgc;
	/**构件编号*/
	@Excel(name = "构件编号", width = 15)
    @ApiModelProperty(value = "构件编号")
    private String gjbh;
	/**检测日期*/
	@Excel(name = "检测日期", width = 15)
    @ApiModelProperty(value = "检测日期")
    private String jcrq;
	/**检测内容*/
	@Excel(name = "检测内容", width = 15)
    @ApiModelProperty(value = "检测内容")
    private String jcnr;
	/**设计力值*/
	@Excel(name = "设计力值", width = 15)
    @ApiModelProperty(value = "设计力值")
    private String sjlz;
	/**孔道钢绞线束数*/
	@Excel(name = "孔道钢绞线束数", width = 15)
    @ApiModelProperty(value = "孔道钢绞线束数")
    private String kdgs;
	/**设计砼强度（强度等级）*/
	@Excel(name = "设计砼强度（强度等级）", width = 15)
    @ApiModelProperty(value = "设计砼强度（强度等级）")
    private String sjqd;
	/**超标等级(0合格，123，初中高  默认为0）*/
	@Excel(name = "超标等级(0合格，123，初中高  默认为0）", width = 15)
    @ApiModelProperty(value = "超标等级(0合格，123，初中高  默认为0）")
//    @Dict(dicCode = "over_level")
    private Integer overLevel;
	/**唯一id*/
	@Excel(name = "唯一id", width = 15)
    @ApiModelProperty(value = "唯一id")
    private String uuid;
	/**梁号*/
	@Excel(name = "梁号", width = 15)
    @ApiModelProperty(value = "梁号")
    private String lh;
    private String type;
    private String serialno;
    private java.lang.Integer overproofStatus;
    @ApiModelProperty(value = "拌合站处置信息表")
    private TrMaoxiayuyingliOverHandler trMaoxiayuyingliOverHandler;
}
