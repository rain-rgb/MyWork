package com.trtm.iot.zhangla.entity;

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
 * @Description: tr_zhangla
 * @Author: jeecg-boot
 * @Date:   2021-03-16
 * @Version: V1.0
 */
@Data
@TableName("tr_zhangla")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tr_zhangla对象", description="tr_zhangla")
public class TrZhangla implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**唯一码*/
	@Excel(name = "唯一码", width = 15)
    @ApiModelProperty(value = "唯一码")
    private java.lang.String uuid;
	/**鉴权ID,通过鉴权接口获取*/
	@Excel(name = "鉴权ID,通过鉴权接口获取", width = 15)
    @ApiModelProperty(value = "鉴权ID,通过鉴权接口获取")
        private java.lang.String token;
	/**预留字段，项目唯一标识，用于判断设备所属项目*/
	@Excel(name = "预留字段，项目唯一标识，用于判断设备所属项目", width = 15)
    @ApiModelProperty(value = "预留字段，项目唯一标识，用于判断设备所属项目")
    private java.lang.String projectid;
	/**平台设备识别码根据厂家编码和厂家设备编号从平台获取*/
	@Excel(name = "平台设备识别码根据厂家编码和厂家设备编号从平台获取", width = 15)
    @ApiModelProperty(value = "平台设备识别码根据厂家编码和厂家设备编号从平台获取")
    private java.lang.String platformdeviceid;
	/**厂家编码
根据厂家编码和厂家设备编号从平台获取
*/
	@Excel(name = "厂家编码 根据厂家编码和厂家设备编号从平台获取", width = 15)
    @ApiModelProperty(value = "厂家编码根据厂家编码和厂家设备编号从平台获取")
    private java.lang.String vendorno;
	/**厂家的设备唯一编号*/
	@Excel(name = "厂家的设备唯一编号", width = 15)
    @ApiModelProperty(value = "厂家的设备唯一编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String deviceno;
	/**梁识别码每片梁只能对应一个UUID，由厂家生成*/
	@Excel(name = "梁识别码每片梁只能对应一个UUID，由厂家生成", width = 15)
    @ApiModelProperty(value = "梁识别码每片梁只能对应一个UUID，由厂家生成")
    private java.lang.String componentid;
	/**梁（构件）名称或编号*/
	@Excel(name = "梁（构件）名称或编号", width = 15)
    @ApiModelProperty(value = "梁（构件）名称或编号")
    private java.lang.String componentparts;
	/**梁型*/
	@Excel(name = "梁型", width = 15)
    @ApiModelProperty(value = "梁型")
    private java.lang.String beamtype;
	/**砼设计强度*/
	@Excel(name = "砼设计强度", width = 15)
    @ApiModelProperty(value = "砼设计强度")
    private java.lang.String concretestrength;
	/**弹性模量*/
	@Excel(name = "弹性模量", width = 15)
    @ApiModelProperty(value = "弹性模量")
    private java.lang.String modulusofelasticity;
	/**钢束编号*/
	@Excel(name = "钢束编号", width = 15)
    @ApiModelProperty(value = "钢束编号")
    private java.lang.String steelbeamno;
	/**钢绞线根数*/
	@Excel(name = "钢绞线根数", width = 15)
    @ApiModelProperty(value = "钢绞线根数")
    private java.lang.String steelstrand;
	/**张拉断面*/
	@Excel(name = "张拉断面", width = 15)
    @ApiModelProperty(value = "张拉断面")
    private java.lang.String tensioningsection;
	/**张拉时间*/
	@Excel(name = "张拉时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "张拉时间")
    private java.util.Date tensioningdate;
	/**张拉力0*/
	@Excel(name = "张拉力0", width = 15)
    @ApiModelProperty(value = "张拉力0")
    private java.lang.String tensioningforce0;
	/**张拉力1*/
	@Excel(name = "张拉力1", width = 15)
    @ApiModelProperty(value = "张拉力1")
    private java.lang.String tensioningforce1;
	/**张拉力2*/
	@Excel(name = "张拉力2", width = 15)
    @ApiModelProperty(value = "张拉力2")
    private java.lang.String tensioningforce2;
	/**张拉力3*/
	@Excel(name = "张拉力3", width = 15)
    @ApiModelProperty(value = "张拉力3")
    private java.lang.String tensioningforce3;
	/**张拉力4*/
	@Excel(name = "张拉力4", width = 15)
    @ApiModelProperty(value = "张拉力4")
    private java.lang.String tensioningforce4;
	/**伸长量0*/
	@Excel(name = "伸长量0", width = 15)
    @ApiModelProperty(value = "伸长量0")
    private java.lang.String elongation0;
	/**伸长量1*/
	@Excel(name = "伸长量1", width = 15)
    @ApiModelProperty(value = "伸长量1")
    private java.lang.String elongation1;
	/**伸长量2*/
	@Excel(name = "伸长量2", width = 15)
    @ApiModelProperty(value = "伸长量2")
    private java.lang.String elongation2;
	/**伸长量3*/
	@Excel(name = "伸长量3", width = 15)
    @ApiModelProperty(value = "伸长量3")
    private java.lang.String elongation3;
	/**伸长量4*/
	@Excel(name = "伸长量4", width = 15)
    @ApiModelProperty(value = "伸长量4")
    private java.lang.String elongation4;
	/**油压0*/
	@Excel(name = "油压0", width = 15)
    @ApiModelProperty(value = "油压0")
    private java.lang.String oilpressure0;
	/**油压1*/
	@Excel(name = "油压1", width = 15)
    @ApiModelProperty(value = "油压1")
    private java.lang.String oilpressure1;
	/**油压2*/
	@Excel(name = "油压2", width = 15)
    @ApiModelProperty(value = "油压2")
    private java.lang.String oilpressure2;
	/**油压3*/
	@Excel(name = "油压3", width = 15)
    @ApiModelProperty(value = "油压3")
    private java.lang.String oilpressure3;
	/**油压4*/
	@Excel(name = "油压4", width = 15)
    @ApiModelProperty(value = "油压4")
    private java.lang.String oilpressure4;
	/**设计张拉控制力*/
	@Excel(name = "设计张拉控制力", width = 15)
    @ApiModelProperty(value = "设计张拉控制力")
    private java.lang.String tensioncontrol;
	/**总伸长量*/
	@Excel(name = "总伸长量", width = 15)
    @ApiModelProperty(value = "总伸长量")
    private java.lang.String totalelongation;
	/**理论伸长量*/
	@Excel(name = "理论伸长量", width = 15)
    @ApiModelProperty(value = "理论伸长量")
    private java.lang.String theoreticalelongation;
	/**延伸误差量*/
	@Excel(name = "延伸误差量", width = 15)
    @ApiModelProperty(value = "延伸误差量")
    private java.lang.String extenderror;
	/**是否合格缺省为0；0代表合格，1代表不合格*/
	@Excel(name = "是否合格缺省为0；0代表合格，1代表不合格", width = 15)
    @ApiModelProperty(value = "是否合格缺省为0；0代表合格，1代表不合格")
    @Dict( dicCode = "result")
    private java.lang.Integer result;
	/**操作员*/
	@Excel(name = "操作员", width = 15)
    @ApiModelProperty(value = "操作员")
    private java.lang.String userid;
	/**其他信息*/
	@Excel(name = "其他信息", width = 15)
    @ApiModelProperty(value = "其他信息")
    private java.lang.String otherinformation;
	/**张拉力曲线*/
	@Excel(name = "张拉力曲线", width = 15)
    @ApiModelProperty(value = "张拉力曲线")
    private java.lang.String forcecurve;
	/**伸长量曲线*/
	@Excel(name = "伸长量曲线", width = 15)
    @ApiModelProperty(value = "伸长量曲线")
    private java.lang.String elongationcurve;
	/**油压曲线*/
	@Excel(name = "油压曲线", width = 15)
    @ApiModelProperty(value = "油压曲线")
    private java.lang.String oilpressurecurve;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private java.lang.String engineeringname;
	/**工程（施工）部位*/
	@Excel(name = "工程（施工）部位", width = 15)
    @ApiModelProperty(value = "工程（施工）部位")
    private java.lang.String engineeringsite;
	/**是否判断超标并报警0为没有判断过1为已判断并且进行过了生产统计  20为已经处理的数据   30为已经进行了超标处理统计   40为数据异常*/
	@Excel(name = "是否判断超标并报警0为没有判断过1为已判断并且进行过了生产统计  20为已经处理的数据   30为已经进行了超标处理统计   40为数据异常", width = 15)
    @ApiModelProperty(value = "是否判断超标并报警0为没有判断过1为已判断并且进行过了生产统计  20为已经处理的数据   30为已经进行了超标处理统计   40为数据异常")
    private java.lang.Integer status;
}
