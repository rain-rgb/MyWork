package com.trtm.sy.syztgl.entity;

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
 * @Description: 试验设备管理
 * @Author: jeecg-boot
 * @Date:   2022-09-23
 * @Version: V1.0
 */
@Data
@TableName("sy_shebeiguanli")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_shebeiguanli对象", description="试验设备管理")
public class SyShebeiguanli implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String sbjno;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private java.lang.String sbname;
	/**设备所属机构*/
	@Excel(name = "设备所属机构", width = 15)
    @ApiModelProperty(value = "设备所属机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysorgcode;
	/**设备可做实验类型*/
	@Excel(name = "设备可做实验类型", width = 15)
    @ApiModelProperty(value = "设备可做实验类型")
    private java.lang.String exptype;
	/**所属单位（id）*/
	@Excel(name = "所属单位（id）", width = 15)
    @ApiModelProperty(value = "所属单位（id）")
    private java.lang.String ssdw;
	/**进场时间*/
	@Excel(name = "进场时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "进场时间")
    private java.util.Date jingchangtime;
	/**标定时间*/
	@Excel(name = "标定时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "标定时间")
    private java.util.Date biaodingtime;
	/**检验周期（单位天）*/
	@Excel(name = "检验周期（单位天）", width = 15)
    @ApiModelProperty(value = "检验周期（单位天）")
    private java.lang.String jyzq;
	/**管理员（id）*/
	@Excel(name = "管理员（id）", width = 15)
    @ApiModelProperty(value = "管理员（id）")
    private java.lang.String guanliyuan;
	/**放置地点*/
	@Excel(name = "放置地点", width = 15)
    @ApiModelProperty(value = "放置地点")
    private java.lang.String adr;
	/**是否完好（0合格；1不合格）*/
	@Excel(name = "是否完好（0合格；1不合格）", width = 15)
    @ApiModelProperty(value = "是否完好（0合格；1不合格）")
    private java.lang.Integer isok;
	/**是否物联（0：是；1否）*/
	@Excel(name = "是否物联（0：是；1否）", width = 15)
    @ApiModelProperty(value = "是否物联（0：是；1否）")
    private java.lang.Integer iswulian;
	/**设备类型*/
	@Excel(name = "设备类型", width = 15)
    @ApiModelProperty(value = "设备类型")
    private java.lang.String sbtype;
	/**型号*/
	@Excel(name = "型号", width = 15)
    @ApiModelProperty(value = "型号")
    private java.lang.String xinghao;

    private java.lang.Integer staus;//0已关闭；1启动中


}
