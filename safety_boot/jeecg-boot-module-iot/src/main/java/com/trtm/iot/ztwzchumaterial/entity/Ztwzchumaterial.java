package com.trtm.iot.ztwzchumaterial.entity;

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
 * @Description: 中铁出厂子表
 * @Author: jeecg-boot
 * @Date:   2024-04-07
 * @Version: V1.0
 */
@Data
@TableName("ztwzchumaterial")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ztwzchumaterial对象", description="中铁出厂子表")
public class Ztwzchumaterial implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**组织机构*/
	@Excel(name = "组织机构", width = 15)
    @ApiModelProperty(value = "组织机构")
    private java.lang.String orgid;
	/**主表主键*/
	@Excel(name = "主表主键", width = 15)
    @ApiModelProperty(value = "主表主键")
    private java.lang.String orderid;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**版本号*/
	@Excel(name = "版本号", width = 15)
    @ApiModelProperty(value = "版本号")
    private java.lang.String version;
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
	/**STRING	主单位*/
	@Excel(name = "STRING	主单位", width = 15)
    @ApiModelProperty(value = "STRING	主单位")
    private java.lang.String materialunit;
	/**辅单位*/
	@Excel(name = "辅单位", width = 15)
    @ApiModelProperty(value = "辅单位")
    private java.lang.String auxiliaryunit;
	/**材料类别id*/
	@Excel(name = "材料类别id", width = 15)
    @ApiModelProperty(value = "材料类别id")
    private java.lang.String classid;
	/**材料类别主键链*/
	@Excel(name = "材料类别主键链", width = 15)
    @ApiModelProperty(value = "材料类别主键链")
    private java.lang.String classfullid;
	/**第三方组织机构*/
	@Excel(name = "第三方组织机构", width = 15)
    @ApiModelProperty(value = "第三方组织机构")
    private java.lang.String oriorgid;
	/**条码*/
	@Excel(name = "条码", width = 15)
    @ApiModelProperty(value = "条码")
    private java.lang.String itembarcode;
	/**确认数量（核算数量）*/
	@Excel(name = "确认数量（核算数量）", width = 15)
    @ApiModelProperty(value = "确认数量（核算数量）")
    private java.lang.String netquantity;
	/**转化率*/
	@Excel(name = "转化率", width = 15)
    @ApiModelProperty(value = "转化率")
    private java.lang.String conversionrate;
	/**扣率*/
	@Excel(name = "扣率", width = 15)
    @ApiModelProperty(value = "扣率")
    private java.lang.String deductrate;
	/**扣吨*/
	@Excel(name = "扣吨", width = 15)
    @ApiModelProperty(value = "扣吨")
    private java.lang.String deductquantity;
	/**辅单位净重*/
	@Excel(name = "辅单位净重", width = 15)
    @ApiModelProperty(value = "辅单位净重")
    private java.lang.String auxiliarynetquantity;
	/**净重主单位*/
	@Excel(name = "净重主单位", width = 15)
    @ApiModelProperty(value = "净重主单位")
    private java.lang.String mainnetquantity;
	/**上传前id*/
	@Excel(name = "上传前id", width = 15)
    @ApiModelProperty(value = "上传前id")
    private java.lang.String oriitemid;
	/**上传之前的磅单主键*/
	@Excel(name = "上传之前的磅单主键", width = 15)
    @ApiModelProperty(value = "上传之前的磅单主键")
    private java.lang.String oriorderid;
	/**添加时间*/
	@Excel(name = "添加时间", width = 15)
    @ApiModelProperty(value = "添加时间")
    private java.lang.String createdat;
	/**删除状态*/
	@Excel(name = "删除状态", width = 15)
    @ApiModelProperty(value = "删除状态")
    private java.lang.String isremoved;
	/**添加人*/
	@Excel(name = "添加人", width = 15)
    @ApiModelProperty(value = "添加人")
    private java.lang.String creatorid;
	/**添加人*/
	@Excel(name = "添加人", width = 15)
    @ApiModelProperty(value = "添加人")
    private java.lang.String creatorname;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15)
    @ApiModelProperty(value = "修改时间")
    private java.lang.String updatedat;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private java.lang.String modifierid;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private java.lang.String modifiername;
	/**业务类型  1代表发料（10） 内调（20） 外调（30） 报废 （40） 发退（-11） 内退（-21） 外退（-31）*/
	@Excel(name = "业务类型  1代表发料（10） 内调（20） 外调（30） 报废 （40） 发退（-11） 内退（-21） 外退（-31）", width = 15)
    @ApiModelProperty(value = "业务类型  1代表发料（10） 内调（20） 外调（30） 报废 （40） 发退（-11） 内退（-21） 外退（-31）")
    private java.lang.String servicetype;
	/**操作类型默认值  1冲红（-1）2补录4正常*/
	@Excel(name = "操作类型默认值  1冲红（-1）2补录4正常", width = 15)
    @ApiModelProperty(value = "操作类型默认值  1冲红（-1）2补录4正常")
    private java.lang.String ordertype;
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
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
}
