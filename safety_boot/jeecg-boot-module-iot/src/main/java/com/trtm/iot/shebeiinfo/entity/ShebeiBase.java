package com.trtm.iot.shebeiinfo.entity;

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
 * @Description: shebei_base
 * @Author: jeecg-boot
 * @Date:   2024-11-13
 * @Version: V1.0
 */
@Data
@TableName("shebei_base")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="shebei_base对象", description="shebei_base")
public class ShebeiBase implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String shebeino;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private java.lang.String shebeiname;
	/**设备类型*/
	@Excel(name = "设备类型", width = 15)
    @ApiModelProperty(value = "设备类型")
    @Dict(dicCode = "sbtype")
    private java.lang.Integer sbtype;
	/**合格证附件资料*/
	@Excel(name = "合格证附件资料", width = 15)
    @ApiModelProperty(value = "合格证附件资料")
    private java.lang.String hegezheng;
	/**规格*/
	@Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
    private java.lang.String guige;
	/**设备厂家*/
	@Excel(name = "设备厂家", width = 15)
    @ApiModelProperty(value = "设备厂家")
    private java.lang.String changjia;
	/**出厂编号*/
	@Excel(name = "出厂编号", width = 15)
    @ApiModelProperty(value = "出厂编号")
    private java.lang.String chuchangbianhao;
	/**出厂日期*/
	@Excel(name = "出厂日期", width = 15)
    @ApiModelProperty(value = "出厂日期")
    private java.lang.String chuchangriqi;
	/**责任人*/
	@Excel(name = "责任人", width = 15)
    @ApiModelProperty(value = "责任人")
    private java.lang.String zerenren;
	/**验收状态*/
	@Excel(name = "验收状态", width = 15)
    @ApiModelProperty(value = "验收状态")
    @Dict(dicCode = "yanshouzhuangtai")
    private java.lang.String yanshouzhuangtai;
	/**进场时间*/
	@Excel(name = "进场时间", width = 15)
    @ApiModelProperty(value = "进场时间")
    private java.lang.String jingchangshijian;
	/**退场时间*/
	@Excel(name = "退场时间", width = 15)
    @ApiModelProperty(value = "退场时间")
    private java.lang.String tuichangshijian;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**0 没有运行轨迹；1 有运行轨迹*/
	@Excel(name = "0 没有运行轨迹；1 有运行轨迹", width = 15)
    @ApiModelProperty(value = "0 没有运行轨迹；1 有运行轨迹")
    private java.lang.Integer isgps;
	/**gps设备编号*/
	@Excel(name = "gps设备编号", width = 15)
    @ApiModelProperty(value = "gps设备编号")
    private java.lang.String gpsshebeino;
	/**备用*/
	@Excel(name = "备用", width = 15)
    @ApiModelProperty(value = "备用")
    private java.lang.String note;

    @Excel(name = "设备状态", width = 15)
    @ApiModelProperty(value = "设备状态")
    @Dict(dicCode ="shebeistatus")
    private java.lang.Integer shebeistatus;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最近检查时间")
    private java.util.Date lasttime;
    private java.lang.String jiancharen;
    // 使用地点
    private java.lang.String useadd;

}
