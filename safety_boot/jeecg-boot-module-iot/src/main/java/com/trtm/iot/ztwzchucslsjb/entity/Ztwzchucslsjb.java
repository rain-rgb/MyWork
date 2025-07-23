package com.trtm.iot.ztwzchucslsjb.entity;

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
 * @Description: 中铁现场发料单据
 * @Author: jeecg-boot
 * @Date:   2024-04-07
 * @Version: V1.0
 */
@Data
@TableName("ztwzchucslsjb")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ztwzchucslsjb对象", description="中铁现场发料单据")
public class Ztwzchucslsjb implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;

    private java.lang.String fid;
	/**制单人*/
	@Excel(name = "制单人", width = 15)
    @ApiModelProperty(value = "制单人")
    private java.lang.String maker;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**审核人*/
	@Excel(name = "审核人", width = 15)
    @ApiModelProperty(value = "审核人")
    private java.lang.String auditor;
	/**版本号*/
	@Excel(name = "版本号", width = 15)
    @ApiModelProperty(value = "版本号")
    private java.lang.String version;
	/**端上的组织机构名称*/
	@Excel(name = "端上的组织机构名称", width = 15)
    @ApiModelProperty(value = "端上的组织机构名称")
    private java.lang.String oriorgname;
	/**组织机构*/
	@Excel(name = "组织机构", width = 15)
    @ApiModelProperty(value = "组织机构")
    private java.lang.String orgid;
	/**账期*/
	@Excel(name = "账期", width = 15)
    @ApiModelProperty(value = "账期")
    private java.lang.String orderdate;
	/**过磅日期*/
	@Excel(name = "过磅日期", width = 15)
    @ApiModelProperty(value = "过磅日期")
    private java.lang.String recordeddate;
	/**磅单编号*/
	@Excel(name = "磅单编号", width = 15)
    @ApiModelProperty(value = "磅单编号")
    private java.lang.String ordercode;
	/**0	过磅系统 1	物料验收APP 2	PC*/
	@Excel(name = "0	过磅系统 1	物料验收APP 2	PC", width = 15)
    @ApiModelProperty(value = "0	过磅系统 1	物料验收APP 2	PC")
    private java.lang.Integer orderorigin;
	/**10	发料 -11	退发料 20	内调 -21	退内调 30	外调 -31	退外调 40	报废*/
	@Excel(name = "10	发料 -11	退发料 20	内调 -21	退内调 30	外调 -31	退外调 40	报废", width = 15)
    @ApiModelProperty(value = "10	发料 -11	退发料 20	内调 -21	退内调 30	外调 -31	退外调 40	报废")
    private java.lang.Integer servicetype;
	/**4	正常单据 2	补录单据 -1	冲红单据 -2	冲红补录单据*/
	@Excel(name = "4	正常单据 2	补录单据 -1	冲红单据 -2	冲红补录单据", width = 15)
    @ApiModelProperty(value = "4	正常单据 2	补录单据 -1	冲红单据 -2	冲红补录单据")
    private java.lang.Integer ordertype;
	/**制单时间*/
	@Excel(name = "制单时间", width = 15)
    @ApiModelProperty(value = "制单时间")
    private java.lang.String makerdate;
	/**打印次数*/
	@Excel(name = "打印次数", width = 15)
    @ApiModelProperty(value = "打印次数")
    private java.lang.Integer printtimes;
	/**车牌号*/
	@Excel(name = "车牌号", width = 15)
    @ApiModelProperty(value = "车牌号")
    private java.lang.String platenumber;
	/**用料单位主键*/
	@Excel(name = "用料单位主键", width = 15)
    @ApiModelProperty(value = "用料单位主键")
    private java.lang.String labourid;
	/**用料单位名称*/
	@Excel(name = "用料单位名称", width = 15)
    @ApiModelProperty(value = "用料单位名称")
    private java.lang.String labourname;
	/**工号ID*/
	@Excel(name = "工号ID", width = 15)
    @ApiModelProperty(value = "工号ID")
    private java.lang.String ghid;
	/**工号全主键*/
	@Excel(name = "工号全主键", width = 15)
    @ApiModelProperty(value = "工号全主键")
    private java.lang.String ghfullid;
	/**工号全称*/
	@Excel(name = "工号全称", width = 15)
    @ApiModelProperty(value = "工号全称")
    private java.lang.String ghfullname;
	/**工号名称*/
	@Excel(name = "工号名称", width = 15)
    @ApiModelProperty(value = "工号名称")
    private java.lang.String ghname;
	/**冲红状态*/
	@Excel(name = "冲红状态", width = 15)
    @ApiModelProperty(value = "冲红状态")
    private java.lang.String isred;
	/**审核状态*/
	@Excel(name = "审核状态", width = 15)
    @ApiModelProperty(value = "审核状态")
    private java.lang.String isaudit;
	/**审核日期*/
	@Excel(name = "审核日期", width = 15)
    @ApiModelProperty(value = "审核日期")
    private java.lang.String auditdate;
	/**第三方组织机构*/
	@Excel(name = "第三方组织机构", width = 15)
    @ApiModelProperty(value = "第三方组织机构")
    private java.lang.String oriorgid;
	/**过磅端主键*/
	@Excel(name = "过磅端主键", width = 15)
    @ApiModelProperty(value = "过磅端主键")
    private java.lang.String oriorderid;
	/**添加人*/
	@Excel(name = "添加人", width = 15)
    @ApiModelProperty(value = "添加人")
    private java.lang.String creatorid;
	/**添加人姓名*/
	@Excel(name = "添加人姓名", width = 15)
    @ApiModelProperty(value = "添加人姓名")
    private java.lang.String creatorname;
	/**添加时间*/
	@Excel(name = "添加时间", width = 15)
    @ApiModelProperty(value = "添加时间")
    private java.lang.String createdat;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private java.lang.String modifierid;
	/**修改人姓名*/
	@Excel(name = "修改人姓名", width = 15)
    @ApiModelProperty(value = "修改人姓名")
    private java.lang.String modifiername;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15)
    @ApiModelProperty(value = "修改时间")
    private java.lang.String updatedat;
	/**删除状态*/
	@Excel(name = "删除状态", width = 15)
    @ApiModelProperty(value = "删除状态")
    private java.lang.String isremoved;
	/**引用主键*/
	@Excel(name = "引用主键", width = 15)
    @ApiModelProperty(value = "引用主键")
    private java.lang.String orderdataid;
	/**识别车牌*/
	@Excel(name = "识别车牌", width = 15)
    @ApiModelProperty(value = "识别车牌")
    private java.lang.String discernplatenumber;
	/**入场时间*/
	@Excel(name = "入场时间", width = 15)
    @ApiModelProperty(value = "入场时间")
    private java.lang.String entertime;
	/**出场时间*/
	@Excel(name = "出场时间", width = 15)
    @ApiModelProperty(value = "出场时间")
    private java.lang.String exittime;
	/**排序时间*/
	@Excel(name = "排序时间", width = 15)
    @ApiModelProperty(value = "排序时间")
    private java.lang.String sorttime;
	/**是否退料*/
	@Excel(name = "是否退料", width = 15)
    @ApiModelProperty(value = "是否退料")
    private java.lang.String isreturn;
	/**出场状态*/
	@Excel(name = "出场状态", width = 15)
    @ApiModelProperty(value = "出场状态")
    private java.lang.String isexit;
	/**是否称皮重*/
	@Excel(name = "是否称皮重", width = 15)
    @ApiModelProperty(value = "是否称皮重")
    private java.lang.String istare;
	/**转换率计算方式 true乘法 false 除法*/
	@Excel(name = "转换率计算方式 true乘法 false 除法", width = 15)
    @ApiModelProperty(value = "转换率计算方式 true乘法 false 除法")
    private java.lang.String ismultiplication;
	/**毛重*/
	@Excel(name = "毛重", width = 15)
    @ApiModelProperty(value = "毛重")
    private java.lang.String roughquantity;
	/**皮重*/
	@Excel(name = "皮重", width = 15)
    @ApiModelProperty(value = "皮重")
    private java.lang.String tarequantity;
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
	/**单据条形码*/
	@Excel(name = "单据条形码", width = 15)
    @ApiModelProperty(value = "单据条形码")
    private java.lang.String orderbarcode;
	/**区分一车多料、直进直出、普通进料标识*/
	@Excel(name = "区分一车多料、直进直出、普通进料标识", width = 15)
    @ApiModelProperty(value = "区分一车多料、直进直出、普通进料标识")
    private java.lang.String weighttype;
	/**复称状态*/
	@Excel(name = "复称状态", width = 15)
    @ApiModelProperty(value = "复称状态")
    private java.lang.String isaffirm;
	/**混凝土材料*/
	@Excel(name = "混凝土材料", width = 15)
    @ApiModelProperty(value = "混凝土材料")
    private java.lang.String isconcrete;
	/**料仓全称*/
	@Excel(name = "料仓全称", width = 15)
    @ApiModelProperty(value = "料仓全称")
    private java.lang.String stockbinfullname;
	/**料仓名称*/
	@Excel(name = "料仓名称", width = 15)
    @ApiModelProperty(value = "料仓名称")
    private java.lang.String stockbinname;
	/**料仓主键*/
	@Excel(name = "料仓主键", width = 15)
    @ApiModelProperty(value = "料仓主键")
    private java.lang.String stockbinid;
	/**第三方料仓主键*/
	@Excel(name = "第三方料仓主键", width = 15)
    @ApiModelProperty(value = "第三方料仓主键")
    private java.lang.String oristockbinid;
	/**用料单位第三方主键*/
	@Excel(name = "用料单位第三方主键", width = 15)
    @ApiModelProperty(value = "用料单位第三方主键")
    private java.lang.String orilabourid;
	/**工号第三方主键*/
	@Excel(name = "工号第三方主键", width = 15)
    @ApiModelProperty(value = "工号第三方主键")
    private java.lang.String orighid;
	/**组织机构名称*/
	@Excel(name = "组织机构名称", width = 15)
    @ApiModelProperty(value = "组织机构名称")
    private java.lang.String orgname;
	/**项目部组织ID*/
	@Excel(name = "项目部组织ID", width = 15)
    @ApiModelProperty(value = "项目部组织ID")
    private java.lang.String realorgid;
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
