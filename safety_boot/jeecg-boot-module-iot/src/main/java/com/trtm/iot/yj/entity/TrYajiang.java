package com.trtm.iot.yj.entity;

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
 * @Description: tr_yajiang
 * @Author: jeecg-boot
 * @Date: 2021-03-17
 * @Version: V1.0
 */
@Data
@TableName("tr_yajiang")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "tr_yajiang对象", description = "tr_yajiang")
public class TrYajiang implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private Integer id;
    /**
     * 唯一码
     */
    @Excel(name = "唯一码", width = 15)
    @ApiModelProperty(value = "唯一码")
    private String uuid;
    /**
     * 鉴权ID,通过鉴权接口获取
     */
    @Excel(name = "鉴权ID,通过鉴权接口获取", width = 15)
    @ApiModelProperty(value = "鉴权ID,通过鉴权接口获取")
    private String token;
    /**
     * 预留字段，项目唯一标识，用于判断设备所属项目
     */
    @Excel(name = "预留字段，项目唯一标识，用于判断设备所属项目", width = 15)
    @ApiModelProperty(value = "预留字段，项目唯一标识，用于判断设备所属项目")
    private String projectid;
    /**
     * 平台设备识别码根据厂家编码和厂家设备编号从平台获取
     */
    @Excel(name = "平台设备识别码根据厂家编码和厂家设备编号从平台获取", width = 15)
    @ApiModelProperty(value = "平台设备识别码根据厂家编码和厂家设备编号从平台获取")
    private String platformdeviceid;
    /**
     * 厂家编码
     * 根据厂家编码和厂家设备编号从平台获取
     */
    @Excel(name = "厂家编码根据厂家编码和厂家设备编号从平台获取 ", width = 15)
            @ApiModelProperty(value = "厂家编码根据厂家编码和厂家设备编号从平台获取 ")
            private String vendorno;
            /**厂家的设备唯一编号*/
            @Excel(name = "厂家的设备唯一编号", width = 15)
            @ApiModelProperty(value = "厂家的设备唯一编号")
            @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
            private String deviceno;
            /**梁识别码每片梁只能对应一个UUID，由厂家生成*/
            @Excel(name = "梁识别码每片梁只能对应一个UUID，由厂家生成", width = 15)
            @ApiModelProperty(value = "梁识别码每片梁只能对应一个UUID，由厂家生成")
            private String componentid;
            /**梁（构件）名称或编号*/
            @Excel(name = "梁（构件）名称或编号", width = 15)
            @ApiModelProperty(value = "梁（构件）名称或编号")
            private String componentparts;
            /**梁型*/
            @Excel(name = "梁型", width = 15)
            @ApiModelProperty(value = "梁型")
            private String beamtype;
            /**砼设计强度*/
            @Excel(name = "砼设计强度", width = 15)
            @ApiModelProperty(value = "砼设计强度")
            private String concretestrength;
            /**弹性模量*/
            @Excel(name = "弹性模量", width = 15)
            @ApiModelProperty(value = "弹性模量")
            private String modulusofelasticity;
            /**钢束编号*/
            @Excel(name = "钢束编号", width = 15)
            @ApiModelProperty(value = "钢束编号")
            private String steelbeamno;
            /**钢绞线根数*/
            @Excel(name = "钢绞线根数", width = 15)
            @ApiModelProperty(value = "钢绞线根数")
            private String steelstrand;
            /**压浆方向*/
            @Excel(name = "压浆方向", width = 15)
            @ApiModelProperty(value = "压浆方向")
            private String pulpingorientation;
            /**压浆顺序*/
            @Excel(name = "压浆顺序", width = 15)
            @ApiModelProperty(value = "压浆顺序")
            private String pulpingorder;
            /**初始流动速度*/
            @Excel(name = "初始流动速度", width = 15)
            @ApiModelProperty(value = "初始流动速度")
            private String flowvelocity;
            /**流动度*/
            @Excel(name = "流动度", width = 15)
            @ApiModelProperty(value = "流动度")
            private String fluidity;
            /**配合比*/
            @Excel(name = "配合比", width = 15)
            @ApiModelProperty(value = "配合比")
            private String mixproportion;
            /**水胶比*/
            @Excel(name = "水胶比", width = 15)
            @ApiModelProperty(value = "水胶比")
            private String waterratio;
            /**搅拌时间*/
            @Excel(name = "搅拌时间", width = 15)
            @ApiModelProperty(value = "搅拌时间")
            private String stirringtime;
            /**开始时间*/
            @Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd")
            @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private Date startdate;
    /**
     * 结束时间
     */
    @Excel(name = "结束时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    private Date enddate;
    /**
     * 进浆量
     */
    @Excel(name = "进浆量", width = 15)
    @ApiModelProperty(value = "进浆量")
    private String pulpingvolume;
    /**
     * 理论浆量
     */
    @Excel(name = "理论浆量", width = 15)
    @ApiModelProperty(value = "理论浆量")
    private String standardvolume;
    /**
     * 进浆压力
     */
    @Excel(name = "进浆压力", width = 15)
    @ApiModelProperty(value = "进浆压力")
    private String pulpingpressurein;
    /**
     * 出浆压力
     */
    @Excel(name = "出浆压力", width = 15)
    @ApiModelProperty(value = "出浆压力")
    private String pulpingpressureout;
    /**
     * 持压时间
     */
    @Excel(name = "持压时间", width = 15)
    @ApiModelProperty(value = "持压时间")
    private String holdingtime;
    /**
     * 是否合格缺省为0；0代表合格，1代表不合格
     */
    @Excel(name = "是否合格缺省为0；0代表合格，1代表不合格", width = 15)
    @ApiModelProperty(value = "是否合格缺省为0；0代表合格，1代表不合格")
    private Integer result;
    /**
     * 操作员
     */
    @Excel(name = "操作员", width = 15)
    @ApiModelProperty(value = "操作员")
    private String userid;
    /**
     * 其他信息
     */
    @Excel(name = "其他信息", width = 15)
    @ApiModelProperty(value = "其他信息")
    private String otherinformation;
    /**
     * 进浆量曲线
     */
    @Excel(name = "进浆量曲线", width = 15)
    @ApiModelProperty(value = "进浆量曲线")
    private String volumecurve;
    /**
     * 进浆压力曲线
     */
    @Excel(name = "进浆压力曲线", width = 15)
    @ApiModelProperty(value = "进浆压力曲线")
    private String pressureincurve;
    /**
     * 出浆压力曲线
     */
    @Excel(name = "出浆压力曲线", width = 15)
    @ApiModelProperty(value = "出浆压力曲线")
    private String pressureoutcurve;
    /**
     * 工程名称
     */
    @Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private String engineeringname;
    /**
     * 工程（施工）部位
     */
    @Excel(name = "工程（施工）部位", width = 15)
    @ApiModelProperty(value = "工程（施工）部位")
    private String engineeringsite;
    /**
     * 状态位 是否判断超标并报警0为没有判断过1为已判断并且进行过了生产统计  20为已经处理的数据   30为已经进行了超标处理统计   40为数据异常
     */
    @Excel(name = "状态位 是否判断超标并报警0为没有判断过1为已判断并且进行过了生产统计  20为已经处理的数据   30为已经进行了超标处理统计   40为数据异常", width = 15)
    @ApiModelProperty(value = "状态位 是否判断超标并报警0为没有判断过1为已判断并且进行过了生产统计  20为已经处理的数据   30为已经进行了超标处理统计   40为数据异常")
    private Integer status;


}
