package com.trtm.iot.ztwzjincslsjb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.ztwzjinmaterial.entity.Ztwzjinmaterial;
import com.trtm.iot.ztwzphoto.entity.Ztwzphoto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 中铁一局现场收料数据
 * @Author: jeecg-boot
 * @Date:   2024-04-03
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ZtwzjincslsjbVo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;
	/**制单人*/
	@Excel(name = "制单人", width = 15)
    @ApiModelProperty(value = "制单人")
    private String maker;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
	/**审核人*/
	@Excel(name = "审核人", width = 15)
    @ApiModelProperty(value = "审核人")
    private String auditor;
	/**版本号*/
	@Excel(name = "版本号", width = 15)
    @ApiModelProperty(value = "版本号")
    private String version;
	/**端上的组织机构名称*/
	@Excel(name = "端上的组织机构名称", width = 15)
    @ApiModelProperty(value = "端上的组织机构名称")
    private String oriorgname;
	/**组织机构*/
	@Excel(name = "组织机构", width = 15)
    @ApiModelProperty(value = "组织机构")
    private String orgid;
	/**账期*/
	@Excel(name = "账期", width = 15)
    @ApiModelProperty(value = "账期")
    private String orderdate;
	/**过磅日期*/
	@Excel(name = "过磅日期", width = 15)
    @ApiModelProperty(value = "过磅日期")
    private String recordeddate;
	/**磅单编号*/
	@Excel(name = "磅单编号", width = 15)
    @ApiModelProperty(value = "磅单编号")
    private String ordercode;
	/**单据来源*/
	@Excel(name = "单据来源", width = 15)
    @ApiModelProperty(value = "单据来源")
    private Integer orderorigin;
	/**业务类型*/
	@Excel(name = "业务类型", width = 15)
    @ApiModelProperty(value = "业务类型")
    private Integer servicetype;
	/**操作类型*/
	@Excel(name = "操作类型", width = 15)
    @ApiModelProperty(value = "操作类型")
    private Integer ordertype;
	/**制单时间*/
	@Excel(name = "制单时间", width = 15)
    @ApiModelProperty(value = "制单时间")
    private String makerdate;
	/**打印次数*/
	@Excel(name = "打印次数", width = 15)
    @ApiModelProperty(value = "打印次数")
    private Integer printtimes;
	/**车牌号*/
	@Excel(name = "车牌号", width = 15)
    @ApiModelProperty(value = "车牌号")
    private String platenumber;
	/**供应商ID*/
	@Excel(name = "供应商ID", width = 15)
    @ApiModelProperty(value = "供应商ID")
    private Integer supplierid;
	/**供应商名称*/
	@Excel(name = "供应商名称", width = 15)
    @ApiModelProperty(value = "供应商名称")
    private String suppliername;
	/**冲红状态*/
	@Excel(name = "冲红状态", width = 15)
    @ApiModelProperty(value = "冲红状态")
    private String isred;
	/**审核状态*/
	@Excel(name = "审核状态", width = 15)
    @ApiModelProperty(value = "审核状态")
    private String isaudit;
	/**审核日期*/
	@Excel(name = "审核日期", width = 15)
    @ApiModelProperty(value = "审核日期")
    private String auditdate;
	/**第三方组织机构*/
	@Excel(name = "第三方组织机构", width = 15)
    @ApiModelProperty(value = "第三方组织机构")
    private String oriorgid;
	/**过磅端主键*/
	@Excel(name = "过磅端主键", width = 15)
    @ApiModelProperty(value = "过磅端主键")
    private String oriorderid;
	/**添加人*/
	@Excel(name = "添加人", width = 15)
    @ApiModelProperty(value = "添加人")
    private String creatorid;
	/**添加人姓名*/
	@Excel(name = "添加人姓名", width = 15)
    @ApiModelProperty(value = "添加人姓名")
    private String creatorname;
	/**添加时间*/
	@Excel(name = "添加时间", width = 15)
    @ApiModelProperty(value = "添加时间")
    private String createdat;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private String modifierid;
	/**修改人姓名*/
	@Excel(name = "修改人姓名", width = 15)
    @ApiModelProperty(value = "修改人姓名")
    private String modifiername;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15)
    @ApiModelProperty(value = "修改时间")
    private String updatedat;
	/**删除状态*/
	@Excel(name = "删除状态", width = 15)
    @ApiModelProperty(value = "删除状态")
    private String isremoved;
	/**引用主键*/
	@Excel(name = "引用主键", width = 15)
    @ApiModelProperty(value = "引用主键")
    private String orderdataid;
	/**识别车牌*/
	@Excel(name = "识别车牌", width = 15)
    @ApiModelProperty(value = "识别车牌")
    private String discernplatenumber;
	/**料仓全称*/
	@Excel(name = "料仓全称", width = 15)
    @ApiModelProperty(value = "料仓全称")
    private String stockbinfullname;
	/**料仓名称*/
	@Excel(name = "料仓名称", width = 15)
    @ApiModelProperty(value = "料仓名称")
    private String stockbinname;
	/**料仓主键*/
	@Excel(name = "料仓主键", width = 15)
    @ApiModelProperty(value = "料仓主键")
    private String stockbinid;
	/**第三方料仓主键*/
	@Excel(name = "第三方料仓主键", width = 15)
    @ApiModelProperty(value = "第三方料仓主键")
    private String oristockbinid;
	/**入场时间*/
	@Excel(name = "入场时间", width = 15)
    @ApiModelProperty(value = "入场时间")
    private String entertime;
	/**出场时间*/
	@Excel(name = "出场时间", width = 15)
    @ApiModelProperty(value = "出场时间")
    private String exittime;
	/**排序时间*/
	@Excel(name = "排序时间", width = 15)
    @ApiModelProperty(value = "排序时间")
    private String sorttime;
	/**是否退料*/
	@Excel(name = "是否退料", width = 15)
    @ApiModelProperty(value = "是否退料")
    private String isreturn;
	/**出场状态*/
	@Excel(name = "出场状态", width = 15)
    @ApiModelProperty(value = "出场状态")
    private String isexit;
	/**是否称皮重*/
	@Excel(name = "是否称皮重", width = 15)
    @ApiModelProperty(value = "是否称皮重")
    private String istare;
	/**转换率计算方式 true乘法 false 除法*/
	@Excel(name = "转换率计算方式 true乘法 false 除法", width = 15)
    @ApiModelProperty(value = "转换率计算方式 true乘法 false 除法")
    private String ismultiplication;
	/**运单结算(true代表确认数量netQuantity 为运单数量，false则代表确认数量netQuantity 为称重数量)*/
	@Excel(name = "运单结算(true代表确认数量netQuantity 为运单数量，false则代表确认数量netQuantity 为称重数量)", width = 15)
    @ApiModelProperty(value = "运单结算(true代表确认数量netQuantity 为运单数量，false则代表确认数量netQuantity 为称重数量)")
    private String isuseorinetquantity;
	/**抵扣系数*/
	@Excel(name = "抵扣系数", width = 15)
    @ApiModelProperty(value = "抵扣系数")
    private String deductionrate;
	/**毛重*/
	@Excel(name = "毛重", width = 15)
    @ApiModelProperty(value = "毛重")
    private String roughquantity;
	/**皮重*/
	@Excel(name = "皮重", width = 15)
    @ApiModelProperty(value = "皮重")
    private String tarequantity;
	/**扣率*/
	@Excel(name = "扣率", width = 15)
    @ApiModelProperty(value = "扣率")
    private String deductrate;
	/**扣吨*/
	@Excel(name = "扣吨", width = 15)
    @ApiModelProperty(value = "扣吨")
    private String deductquantity;
	/**辅单位净重*/
	@Excel(name = "辅单位净重", width = 15)
    @ApiModelProperty(value = "辅单位净重")
    private String auxiliarynetquantity;
	/**运单重量*/
	@Excel(name = "运单重量", width = 15)
    @ApiModelProperty(value = "运单重量")
    private String waybillweight;
	/**单据条形码*/
	@Excel(name = "单据条形码", width = 15)
    @ApiModelProperty(value = "单据条形码")
    private String orderbarcode;
	/**区分一车多料、直进直出、普通进料标识*/
	@Excel(name = "区分一车多料、直进直出、普通进料标识", width = 15)
    @ApiModelProperty(value = "区分一车多料、直进直出、普通进料标识")
    private String weighttype;
	/**复称状态*/
	@Excel(name = "复称状态", width = 15)
    @ApiModelProperty(value = "复称状态")
    private String isaffirm;
	/**供应商第三方主键*/
	@Excel(name = "供应商第三方主键", width = 15)
    @ApiModelProperty(value = "供应商第三方主键")
    private String orisupplierid;
	/**组织机构名称*/
	@Excel(name = "组织机构名称", width = 15)
    @ApiModelProperty(value = "组织机构名称")
    private String orgname;
	/**项目部组织ID*/
	@Excel(name = "项目部组织ID", width = 15)
    @ApiModelProperty(value = "项目部组织ID")
    private String realorgid;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    /**主表主键*/
    @Excel(name = "主表主键", width = 15)
    @ApiModelProperty(value = "主表主键")
    private java.lang.String orderid;
    /**生产厂商*/
    @Excel(name = "生产厂商", width = 15)
    @ApiModelProperty(value = "生产厂商")
    private java.lang.String manufacturer;
    /**材料主键*/
    @Excel(name = "材料主键", width = 15)
    @ApiModelProperty(value = "材料主键")
    private java.lang.String materialid;
    /**材料编码*/
    @Excel(name = "材料编码", width = 15)
    @ApiModelProperty(value = "材料编码")
    private java.lang.String materialcode;
    /**材料名称*/
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private java.lang.String materialname;
    /**规格型号*/
    @Excel(name = "规格型号", width = 15)
    @ApiModelProperty(value = "规格型号")
    private java.lang.String materialmodel;
    /**主单位*/
    @Excel(name = "主单位", width = 15)
    @ApiModelProperty(value = "主单位")
    private java.lang.String materialunit;
    /**辅单位*/
    @Excel(name = "辅单位", width = 15)
    @ApiModelProperty(value = "辅单位")
    private java.lang.String auxiliaryunit;
    /**材料类别id（已废弃）*/
    @Excel(name = "材料类别id（已废弃）", width = 15)
    @ApiModelProperty(value = "材料类别id（已废弃）")
    private java.lang.String classid;
    /**材料类别主键链（已废弃）*/
    @Excel(name = "材料类别主键链（已废弃）", width = 15)
    @ApiModelProperty(value = "材料类别主键链（已废弃）")
    private java.lang.String classfullid;
    /**炉批号*/
    @Excel(name = "炉批号", width = 15)
    @ApiModelProperty(value = "炉批号")
    private java.lang.String batchno;
    /**实验报告号*/
    @Excel(name = "实验报告号", width = 15)
    @ApiModelProperty(value = "实验报告号")
    private java.lang.String testreportno;
    /**确认数量（核算数量）*/
    @Excel(name = "确认数量（核算数量）", width = 15)
    @ApiModelProperty(value = "确认数量（核算数量）")
    private java.lang.String netquantity;
    /**转化率*/
    @Excel(name = "转化率", width = 15)
    @ApiModelProperty(value = "转化率")
    private java.lang.String conversionrate;
    /**运单数量*/
    @Excel(name = "运单数量", width = 15)
    @ApiModelProperty(value = "运单数量")
    private java.lang.String orinetquantity;
    /**净重主单位*/
    @Excel(name = "净重主单位", width = 15)
    @ApiModelProperty(value = "净重主单位")
    private java.lang.String mainnetquantity;
    /**上传前id*/
    @Excel(name = "上传前id", width = 15)
    @ApiModelProperty(value = "上传前id")
    private java.lang.String oriitemid;
    /**条码*/
    @Excel(name = "条码", width = 15)
    @ApiModelProperty(value = "条码")
    private java.lang.String itembarcode;
    /**存放地*/
    @Excel(name = "存放地", width = 15)
    @ApiModelProperty(value = "存放地")
    private java.lang.String storageplace;
    /**技证号*/
    @Excel(name = "技证号", width = 15)
    @ApiModelProperty(value = "技证号")
    private java.lang.String skillcardno;
    /**单价*/
    @Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private java.lang.String receiveprice;
    /**上传状态*/
    @Excel(name = "上传状态", width = 15)
    @ApiModelProperty(value = "上传状态")
    private java.lang.String externaluploadstate;
    /**冲红材料的原始材料ID*/
    @Excel(name = "冲红材料的原始材料ID", width = 15)
    @ApiModelProperty(value = "冲红材料的原始材料ID")
    private java.lang.String oriitemredid;
    /**类别名称*/
    @Excel(name = "类别名称", width = 15)
    @ApiModelProperty(value = "类别名称")
    private java.lang.String classname;
    /**类别编码*/
    @Excel(name = "类别编码", width = 15)
    @ApiModelProperty(value = "类别编码")
    private java.lang.String classcode;
    /**类别全称*/
    @Excel(name = "类别全称", width = 15)
    @ApiModelProperty(value = "类别全称")
    private java.lang.String classfullname;
    /**第三方材料ID*/
    @Excel(name = "第三方材料ID", width = 15)
    @ApiModelProperty(value = "第三方材料ID")
    private java.lang.String orimaterialid;
    /**第三方材料类别ID*/
    @Excel(name = "第三方材料类别ID", width = 15)
    @ApiModelProperty(value = "第三方材料类别ID")
    private java.lang.String oriclassid;

}
