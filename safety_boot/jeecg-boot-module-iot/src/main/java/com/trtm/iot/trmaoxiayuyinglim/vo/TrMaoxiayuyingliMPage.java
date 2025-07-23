package com.trtm.iot.trmaoxiayuyinglim.vo;

import java.util.List;
import com.trtm.iot.trmaoxiayuyinglim.entity.TrMaoxiayuyingliM;
import com.trtm.iot.trmaoxiayuyinglim.entity.TrMaoxiayuyingliS;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 锚下预应力张拉主表
 * @Author: jeecg-boot
 * @Date:   2024-03-12
 * @Version: V1.0
 */
@Data
@ApiModel(value="tr_maoxiayuyingli_mPage对象", description="锚下预应力张拉主表")
public class TrMaoxiayuyingliMPage {

	/**id*/
	@ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备厂家*/
	@Excel(name = "设备厂家", width = 15)
	@ApiModelProperty(value = "设备厂家")
    private java.lang.String shebeichangjia;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
	@ApiModelProperty(value = "设备编号")
    private java.lang.String sbbh;
	/**单位工程*/
	@Excel(name = "单位工程", width = 15)
	@ApiModelProperty(value = "单位工程")
    private java.lang.String dwgc;
	/**分部工程*/
	@Excel(name = "分部工程", width = 15)
	@ApiModelProperty(value = "分部工程")
    private java.lang.String fbgc;
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

	@ExcelCollection(name="锚下预应力张拉子表")
	@ApiModelProperty(value = "锚下预应力张拉子表")
	private List<TrMaoxiayuyingliS> trMaoxiayuyingliSList;

}
