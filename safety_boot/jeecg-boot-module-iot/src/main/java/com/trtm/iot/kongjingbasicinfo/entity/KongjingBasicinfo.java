package com.trtm.iot.kongjingbasicinfo.entity;

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
 * @Description: 孔径基本信息数据
 * @Author: jeecg-boot
 * @Date:   2022-03-01
 * @Version: V1.0
 */
@Data
@TableName("kongjing_basicinfo")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="kongjing_basicinfo对象", description="孔径基本信息数据")
public class KongjingBasicinfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**设备商标识*/
	@Excel(name = "设备商标识", width = 15)
    @ApiModelProperty(value = "设备商标识")
    private java.lang.String vendorid;
	/**流水号*/
	@Excel(name = "流水号", width = 15)
    @ApiModelProperty(value = "流水号")
    private java.lang.String serialno;
	/**测试仪编号*/
	@Excel(name = "测试仪编号", width = 15)
    @ApiModelProperty(value = "测试仪编号")
    private java.lang.String machineid;
	/**试桩编号*/
	@Excel(name = "试桩编号", width = 15)
    @ApiModelProperty(value = "试桩编号")
    private java.lang.String pileno;
	/**检测日期(格式：yyyy-MM-dd)*/
	@Excel(name = "检测日期(格式：yyyy-MM-dd)", width = 15)
    @ApiModelProperty(value = "检测日期(格式：yyyy-MM-dd)")
    private java.lang.String testpiledate;
	/**检测时间(格式：HH:mm:ss)*/
	@Excel(name = "检测时间(格式：HH:mm:ss)", width = 15)
    @ApiModelProperty(value = "检测时间(格式：HH:mm:ss)")
    private java.lang.String testpiletime;
	/**检测人员*/
	@Excel(name = "检测人员", width = 15)
    @ApiModelProperty(value = "检测人员")
    private java.lang.String worker;
	/**检测单位*/
	@Excel(name = "检测单位", width = 15)
    @ApiModelProperty(value = "检测单位")
    private java.lang.String testunit;
	/**施工单位*/
	@Excel(name = "施工单位", width = 15)
    @ApiModelProperty(value = "施工单位")
    private java.lang.String constructionunit;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private java.lang.String projectname;
	/**孔深(单位：m)*/
	@Excel(name = "孔深(单位：m)", width = 15)
    @ApiModelProperty(value = "孔深(单位：m)")
    private java.lang.Double depth;
	/**孔径(单位：mm)*/
	@Excel(name = "孔径(单位：mm)", width = 15)
    @ApiModelProperty(value = "孔径(单位：mm)")
    private java.lang.Integer diameter;
	/**始测深度(单位：m)*/
	@Excel(name = "始测深度(单位：m)", width = 15)
    @ApiModelProperty(value = "始测深度(单位：m)")
    private java.lang.Double begindepth;
	/**标高(单位：m)*/
	@Excel(name = "标高(单位：m)", width = 15)
    @ApiModelProperty(value = "标高(单位：m)")
    private java.lang.Double height;
	/**管数*/
	@Excel(name = "管数", width = 15)
    @ApiModelProperty(value = "管数")
    private java.lang.Integer pipenums;
	/**移距(单位：mm)*/
	@Excel(name = "移距(单位：mm)", width = 15)
    @ApiModelProperty(value = "移距(单位：mm)")
    private java.lang.Integer shift;
	/**偏移角*/
	@Excel(name = "偏移角", width = 15)
    @ApiModelProperty(value = "偏移角")
    private java.lang.Integer angle;
	/**采集方式  0：自动提升 1：手动提升 2：手动(4s/m) 3：手动(6s/m) 4：手动(8s/m)*/
	@Excel(name = "采集方式  0：自动提升 1：手动提升 2：手动(4s/m) 3：手动(6s/m) 4：手动(8s/m)", width = 15)
    @ApiModelProperty(value = "采集方式  0：自动提升 1：手动提升 2：手动(4s/m) 3：手动(6s/m) 4：手动(8s/m)")
    @Dict(dicCode = "method")
    private java.lang.Integer method;
	/**数字滤波 0：未启动 1：启动*/
	@Excel(name = "数字滤波 0：未启动 1：启动", width = 15)
    @ApiModelProperty(value = "数字滤波 0：未启动 1：启动")
    @Dict(dicCode = "filter")
    private java.lang.Integer filter;
	/**泥浆补偿 泥浆密度补偿 0:未启动 大于0：启动*/
	@Excel(name = "泥浆补偿 泥浆密度补偿 0:未启动 大于0：启动", width = 15)
    @ApiModelProperty(value = "泥浆补偿 泥浆密度补偿 0:未启动 大于0：启动")
    private java.lang.Integer density;
	/**波形增强 0：未启动 1：启动*/
	@Excel(name = "波形增强 0：未启动 1：启动", width = 15)
    @ApiModelProperty(value = "波形增强 0：未启动 1：启动")
    @Dict(dicCode = "filter")
    private java.lang.Integer waveenhance;
	/**判读算法 0：自动判读 1：幅值判读 2：方差判读*/
	@Excel(name = "判读算法 0：自动判读 1：幅值判读 2：方差判读", width = 15)
    @ApiModelProperty(value = "判读算法 0：自动判读 1：幅值判读 2：方差判读")
    @Dict(dicCode = "algorithm")
    private java.lang.Integer algorithm;
	/**幅值参数 */
	@Excel(name = "幅值参数 ", width = 15)
    @ApiModelProperty(value = "幅值参数 ")
    private java.lang.Double amplitude;
	/**方差*/
	@Excel(name = "方差", width = 15)
    @ApiModelProperty(value = "方差")
    private java.lang.Double variance;
	/**主频*/
	@Excel(name = "主频", width = 15)
    @ApiModelProperty(value = "主频")
    private java.lang.Double frequency;
	/**起点 0：负 1：正*/
	@Excel(name = "起点 0：负 1：正", width = 15)
    @ApiModelProperty(value = "起点 0：负 1：正")
    @Dict(dicCode = "origin")
    private java.lang.Integer origin;
	/**首波方式 0：负 1：正*/
	@Excel(name = "首波方式 0：负 1：正", width = 15)
    @ApiModelProperty(value = "首波方式 0：负 1：正")
    @Dict(dicCode = "origin")
    private java.lang.Integer mode;
	/**检测标准 公路、铁路、 建筑、广东规范等*/
	@Excel(name = "检测标准 公路、铁路、 建筑、广东规范等", width = 15)
    @ApiModelProperty(value = "检测标准 公路、铁路、 建筑、广东规范等")
    private java.lang.String standard;
	/**采集方向 0：下放 1：上拉*/
	@Excel(name = "采集方向 0：下放 1：上拉", width = 15)
    @ApiModelProperty(value = "采集方向 0：下放 1：上拉")
    @Dict(dicCode = "direction")
    private java.lang.Integer direction;
	/**罗盘修正 0：未修正 1：已修 正*/
	@Excel(name = "罗盘修正 0：未修正 1：已修 正", width = 15)
    @ApiModelProperty(value = "罗盘修正 0：未修正 1：已修 正")
    @Dict(dicCode = "compass")
    private java.lang.Integer compass;
	/**罗盘基准角 范围[0-360]*/
	@Excel(name = "罗盘基准角 范围[0-360]", width = 15)
    @ApiModelProperty(value = "罗盘基准角 范围[0-360]")
    private java.lang.Double compassangle;
	/**采样间隔 单位us*/
	@Excel(name = "采样间隔 单位us", width = 15)
    @ApiModelProperty(value = "采样间隔 单位us")
    private java.lang.Double sampleinterval;
	/**采样长度 1024、2048*/
	@Excel(name = "采样长度 1024、2048", width = 15)
    @ApiModelProperty(value = "采样长度 1024、2048")
    private java.lang.Integer samplelength;
	/**高通 单位kHz*/
	@Excel(name = "高通 单位kHz", width = 15)
    @ApiModelProperty(value = "高通 单位kHz")
    private java.lang.Double highfilter;
	/**低通 单位kHz*/
	@Excel(name = "低通 单位kHz", width = 15)
    @ApiModelProperty(value = "低通 单位kHz")
    private java.lang.Double lowfilter;
	/**发射脉宽 单位us*/
	@Excel(name = "发射脉宽 单位us", width = 15)
    @ApiModelProperty(value = "发射脉宽 单位us")
    private java.lang.Integer pulsewidth;
	/**发射电压 0：低压 1：高压*/
	@Excel(name = "发射电压 0：低压 1：高压", width = 15)
    @ApiModelProperty(value = "发射电压 0：低压 1：高压")
    @Dict(dicCode = "voltage")
    private java.lang.Integer voltage;
	/** 滑轮直径 单位mm*/
	@Excel(name = " 滑轮直径 单位mm", width = 15)
    @ApiModelProperty(value = " 滑轮直径 单位mm")
    private java.lang.Double pulleydiameter;
	/**电缆直径 单位mm*/
	@Excel(name = "电缆直径 单位mm", width = 15)
    @ApiModelProperty(value = "电缆直径 单位mm")
    private java.lang.Double cablediameter;
	/**绞车速度 1~8（八个等级）*/
	@Excel(name = "绞车速度 1~8（八个等级）", width = 15)
    @ApiModelProperty(value = "绞车速度 1~8（八个等级）")
    @Dict(dicCode = "winchspeed")
    private java.lang.Integer winchspeed;
	/**是否校系 0:不启用；1:启用*/
	@Excel(name = "是否校系 0:不启用；1:启用", width = 15)
    @ApiModelProperty(value = "是否校系 0:不启用；1:启用")
    @Dict(dicCode = "filter")
    private java.lang.Integer iscorrection;
	/**波幅校系*/
	@Excel(name = "波幅校系", width = 15)
    @ApiModelProperty(value = "波幅校系")
    private java.lang.Double amplitudecorrection;
	/**基本信息id*/
	@Excel(name = "基本信息id", width = 15)
    @ApiModelProperty(value = "基本信息id")
    private java.lang.String basicinfoid;
	/**关联表kongjing_tongdao的guid*/
	@Excel(name = "关联表kongjing_tongdao的guid", width = 15)
    @ApiModelProperty(value = "关联表kongjing_tongdao的guid")
    private java.lang.String guid;

    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
	private java.lang.String shebeino;//设备编号

    /**GPS 状态 0 无效 1 有效*/
    @Excel(name = "GPS 状态 0 无效 1 有效", width = 15)
    @ApiModelProperty(value = "GPS 状态 0 无效 1 有效")
    @Dict(dicCode = "valid_status")
    private java.lang.Integer gpsvalid;
    /**经度*/
    @Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.Double gpslongitude;
    /**纬度*/
    @Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.Double gpslatitude;
    /**检测位置*/
    @Excel(name = "检测位置", width = 15)
    @ApiModelProperty(value = "检测位置")
    private java.lang.String detloc;
    /**评级*/
    @ApiModelProperty(value = "评级")
    private java.lang.Integer level;
    /**附件*/
    @ApiModelProperty(value = "附件")
    private java.lang.String file;
    /**附件说明*/
    @ApiModelProperty(value = "附件说明")
    private java.lang.String fileInfo;
}
