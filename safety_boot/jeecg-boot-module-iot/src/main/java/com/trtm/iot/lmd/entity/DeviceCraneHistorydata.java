package com.trtm.iot.lmd.entity;

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
 * @Description: 龙门吊表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-28
 * @Version: V1.0
 */
@Data
@TableName("device_crane_historydata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_crane_historydata对象", description="龙门吊表信息")
public class DeviceCraneHistorydata implements Serializable {
    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**设备编号*/
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String deviceCode;
    /**设备类型2桥门机3架桥机*/
    @Excel(name = "设备类型2桥门机3架桥机", width = 15)
    @ApiModelProperty(value = "设备类型2桥门机3架桥机")
    @Dict(dicCode = "device_type")
    private java.lang.Integer deviceType;
    /**大车行程*/
    @Excel(name = "大车行程", width = 15)
    @ApiModelProperty(value = "大车行程")
    private java.lang.Double bigCarroute;
    /**数据上传时间*/
    @Excel(name = "数据上传时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "数据上传时间")
    private java.util.Date cranedate;
    /**主钩高度*/
    @Excel(name = "主钩高度", width = 15)
    @ApiModelProperty(value = "主钩高度")
    private java.lang.Double mainHookheight;
    /**预留副钩高度*/
    @Excel(name = "预留副钩高度", width = 15)
    @ApiModelProperty(value = "预留副钩高度")
    private java.lang.Double reservedVicehookheight;
    /**主钩吊重*/
    @Excel(name = "主钩吊重", width = 15)
    @ApiModelProperty(value = "主钩吊重")
    private java.lang.Double mainHookload;
    /**预留副钩吊重*/
    @Excel(name = "预留副钩吊重", width = 15)
    @ApiModelProperty(value = "预留副钩吊重")
    private java.lang.Double reservedVicehookload;
    /**小车行程*/
    @Excel(name = "小车行程", width = 15)
    @ApiModelProperty(value = "小车行程")
    private java.lang.Double smallCarroute;
    /**预留小车2行程*/
    @Excel(name = "预留小车2行程", width = 15)
    @ApiModelProperty(value = "预留小车2行程")
    private java.lang.Double reservedSmallcarroute;
    /**风速*/
    @Excel(name = "风速", width = 15)
    @ApiModelProperty(value = "风速")
    private java.lang.Double windSpeed;
    /**预留模拟量*/
    @Excel(name = "预留模拟量", width = 15)
    @ApiModelProperty(value = "预留模拟量")
    private java.lang.Double reservedAnalogquantityone;
    /**预留模拟量*/
    @Excel(name = "预留模拟量", width = 15)
    @ApiModelProperty(value = "预留模拟量")
    private java.lang.Double reservedAnalogquantitytwo;
    /**主钩载荷状态*/
    @Excel(name = "主钩载荷状态", width = 15)
    @ApiModelProperty(value = "主钩载荷状态")
    private java.lang.Integer mainHookstatus;
    /**预留副钩载荷状态*/
    @Excel(name = "预留副钩载荷状态", width = 15)
    @ApiModelProperty(value = "预留副钩载荷状态")
    private java.lang.Integer reservedVicehookstatus;
    /**风速报警*/
    @Excel(name = "风速报警", width = 15)
    @ApiModelProperty(value = "风速报警")
    private java.lang.Integer windSpeedalarm;
    /**大车左限位*/
    @Excel(name = "大车左限位", width = 15)
    @ApiModelProperty(value = "大车左限位")
    private java.lang.Integer bigCarleftlimit;
    /**大车右限位*/
    @Excel(name = "大车右限位", width = 15)
    @ApiModelProperty(value = "大车右限位")
    private java.lang.Integer bigCarrightlimit;
    /**小车前限位*/
    @Excel(name = "小车前限位", width = 15)
    @ApiModelProperty(value = "小车前限位")
    private java.lang.Integer smallCarfrontlimit;
    /**小车后限位*/
    @Excel(name = "小车后限位", width = 15)
    @ApiModelProperty(value = "小车后限位")
    private java.lang.Integer smallCarbacklimit;
    /**预留小车2前限位*/
    @Excel(name = "预留小车2前限位", width = 15)
    @ApiModelProperty(value = "预留小车2前限位")
    private java.lang.Integer reservedSmallcarfrontlimit;
    /**预留小车2后限位*/
    @Excel(name = "预留小车2后限位", width = 15)
    @ApiModelProperty(value = "预留小车2后限位")
    private java.lang.Integer reservedSmallcarbacklimit;
    /**吊钩上限位*/
    @Excel(name = "吊钩上限位", width = 15)
    @ApiModelProperty(value = "吊钩上限位")
    private java.lang.Integer hookUplimit;
    /**预留副吊钩上限位*/
    @Excel(name = "预留副吊钩上限位", width = 15)
    @ApiModelProperty(value = "预留副吊钩上限位")
    private java.lang.Integer reservedVicehookuplimit;
    /**门限位*/
    @Excel(name = "门限位", width = 15)
    @ApiModelProperty(value = "门限位")
    private java.lang.Integer doorLimit;
    /**抗风防滑状态*/
    @Excel(name = "抗风防滑状态", width = 15)
    @ApiModelProperty(value = "抗风防滑状态")
    private java.lang.Integer windAntiskidstatus;
    /**电缆卷筒状态*/
    @Excel(name = "电缆卷筒状态", width = 15)
    @ApiModelProperty(value = "电缆卷筒状态")
    private java.lang.Integer wireDrumstatus;
    /**小车1制动器1*/
    @Excel(name = "小车1制动器1", width = 15)
    @ApiModelProperty(value = "小车1制动器1")
    private java.lang.Integer smallCar1brake1;
    /**小车1制动器2*/
    @Excel(name = "小车1制动器2", width = 15)
    @ApiModelProperty(value = "小车1制动器2")
    private java.lang.Integer smallCar1brake2;
    /**小车2制动器1*/
    @Excel(name = "小车2制动器1", width = 15)
    @ApiModelProperty(value = "小车2制动器1")
    private java.lang.Integer smallCar2brake1;
    /**小车2制动器2*/
    @Excel(name = "小车2制动器2", width = 15)
    @ApiModelProperty(value = "小车2制动器2")
    private java.lang.Integer smallCar2brake2;
    /**大车支腿偏差*/
    @Excel(name = "大车支腿偏差", width = 15)
    @ApiModelProperty(value = "大车支腿偏差")
    private java.lang.Double bigCarlegdeviation;
    /**累计时间*/
    @Excel(name = "累计时间", width = 15)
    @ApiModelProperty(value = "累计时间")
    private java.lang.Double allTime;
    /**循环使用次数*/
    @Excel(name = "循环使用次数", width = 15)
    @ApiModelProperty(value = "循环使用次数")
    private java.lang.Integer allTimes;
    /**支腿偏差报警*/
    @Excel(name = "支腿偏差报警", width = 15)
    @ApiModelProperty(value = "支腿偏差报警")
    private java.lang.Integer bigcarAlm;
    /**预留*/
    @Excel(name = "主钩额重(T)", width = 15)
    @ApiModelProperty(value = "主钩额重(T)")
    private java.lang.Integer reservedOne;
    /**预留*/
    @Excel(name = "副钩额重(T)", width = 15)
    @ApiModelProperty(value = "副钩额重(T)")
    private java.lang.Integer reservedTwo;

    /**大车水平角度X*/
    @Excel(name = "大车水平角度X", width = 15)
    @ApiModelProperty(value = "大车水平角度X")
    private java.lang.String bigx;
    /**大车水平角度Y*/
    @Excel(name = "大车水平角度Y", width = 15)
    @ApiModelProperty(value = "大车水平角度Y")
    private java.lang.String bigy;
    /**垂直角度X*/
    @Excel(name = "垂直角度X", width = 15)
    @ApiModelProperty(value = "垂直角度X")
    private java.lang.String verticalx;
    /**垂直角度Y*/
    @Excel(name = "垂直角度Y", width = 15)
    @ApiModelProperty(value = "垂直角度Y")
    private java.lang.String verticaly;
    /**力臂角度*/
    @Excel(name = "力臂角度", width = 15)
    @ApiModelProperty(value = "力臂角度")
    private java.lang.Float armangle;
    /**力臂长度*/
    @Excel(name = "力臂长度", width = 15)
    @ApiModelProperty(value = "力臂长度")
    private java.lang.Float armlength;
    /**主勾力矩*/
    @Excel(name = "主勾力矩", width = 15)
    @ApiModelProperty(value = "主勾力矩")
    private java.lang.Float mainmoment;
    /**副钩力矩*/
    @Excel(name = "副钩力矩", width = 15)
    @ApiModelProperty(value = "副钩力矩")
    private java.lang.Float vicemoment;
    /**激光测距1 单位：m 100倍放大*/
    @Excel(name = "激光测距1 单位：m 100倍放大", width = 15)
    @ApiModelProperty(value = "激光测距1 单位：m 100倍放大")
    private java.lang.Float slr1;
    /**激光测距2 单位：m 100倍放大*/
    @Excel(name = "激光测距2 单位：m 100倍放大", width = 15)
    @ApiModelProperty(value = "激光测距2 单位：m 100倍放大")
    private java.lang.Float slr2;
    /**是否预警判断*/
    @Excel(name = "是否预警判断", width = 15)
    @ApiModelProperty(value = "是否预警判断")
    private java.lang.Integer alertstate;
    /**激光测距预警(0：不报警，1：报警)*/
    @Excel(name = "激光测距预警(0：不报警，1：报警)", width = 15)
    @ApiModelProperty(value = "激光测距预警(0：不报警，1：报警)")
    private java.lang.Integer slralarm;
    /**状态（0：未进行超标判断，1：合格，2：不合格）*/
    @Excel(name = "状态（0：未进行超标判断，1：合格，2：不合格）", width = 15)
    @ApiModelProperty(value = "状态（0：未进行超标判断，1：合格，2：不合格）")
    private java.lang.Integer status;
    /**主副钩吊重预警*/
    @Excel(name = "主副钩吊重预警", width = 15)
    @ApiModelProperty(value = "主副钩吊重预警")
    private java.lang.Integer mainVicealarm;
    /**审核状态*/
    @Excel(name = "审核状态", width = 15)
    @ApiModelProperty(value = "审核状态")
    private Integer overproofStatus;//审核状态
    /**主钩超高预警*/
    @Excel(name = "主钩超高预警", width = 15)
    @ApiModelProperty(value = "主钩超高预警")
    private java.lang.Integer overval1Height1;
    /**副钩超高预警*/
    @Excel(name = "副钩超高预警", width = 15)
    @ApiModelProperty(value = "副钩超高预警")
    private java.lang.Integer overval2Height2;
    /**主钩超速预警*/
    @Excel(name = "主钩超速预警", width = 15)
    @ApiModelProperty(value = "主钩超速预警")
    private java.lang.Integer overval3Speed1;
    /**副钩超速预警*/
    @Excel(name = "副钩超速预警", width = 15)
    @ApiModelProperty(value = "副钩超速预警")
    private java.lang.Integer overval4Speed2;
}
