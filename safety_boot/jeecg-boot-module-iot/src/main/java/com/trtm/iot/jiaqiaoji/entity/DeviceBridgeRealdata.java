package com.trtm.iot.jiaqiaoji.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: device_bridge_realdata
 * @Author: jeecg-boot
 * @Date:   2023-03-13
 * @Version: V1.0
 */
@Data
@TableName("device_bridge_realdata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_bridge_realdata对象", description="device_bridge_realdata")
public class DeviceBridgeRealdata implements Serializable {
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
    private java.lang.Integer deviceType;
	/**大车横向行程*/
	@Excel(name = "大车横向行程", width = 15)
    @ApiModelProperty(value = "大车横向行程")
    private java.lang.Double bigTransverseCarroute;
	/**数据上传时间*/
	@Excel(name = "数据上传时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据上传时间")
    private java.util.Date bridgedate;
	/**大车纵向行程*/
	@Excel(name = "大车纵向行程", width = 15)
    @ApiModelProperty(value = "大车纵向行程")
    private java.lang.Double bigLongitudinalCarroute;
	/**支腿垂直度*/
	@Excel(name = "支腿垂直度", width = 15)
    @ApiModelProperty(value = "支腿垂直度")
    private java.lang.Double verticalityLeg;
	/**大车水平度*/
	@Excel(name = "大车水平度", width = 15)
    @ApiModelProperty(value = "大车水平度")
    private java.lang.Double carLevelness;
	/**1#天车高度*/
	@Excel(name = "1#天车高度", width = 15)
    @ApiModelProperty(value = "1#天车高度")
    @TableField(value = "crane_height_1")
    private java.lang.Double craneHeight1;
	/**1#天车吊重*/
	@Excel(name = "1#天车吊重", width = 15)
    @ApiModelProperty(value = "1#天车吊重")
    @TableField(value = "crane_crane_1")
    private java.lang.Double craneCrane1;
	/**1#天车横向行程*/
	@Excel(name = "1#天车横向行程", width = 15)
    @ApiModelProperty(value = "1#天车横向行程")
    @TableField(value = "transverse_carroute_1")
    private java.lang.Double transverseCarroute1;
	/**1#天车纵向行程*/
	@Excel(name = "1#天车纵向行程", width = 15)
    @ApiModelProperty(value = "1#天车纵向行程")
    @TableField(value = "longitudinal_carroute_1")
    private java.lang.Double longitudinalCarroute1;
	/**1#天车吊钩载荷状态*/
	@Excel(name = "1#天车吊钩载荷状态", width = 15)
    @ApiModelProperty(value = "1#天车吊钩载荷状态")
    @TableField(value = "hookstatus_1")
    private java.lang.Integer hookstatus1;
	/**1#天车吊钩制动器1状态*/
	@Excel(name = "1#天车吊钩制动器1状态", width = 15)
    @ApiModelProperty(value = "1#天车吊钩制动器1状态")
    @TableField(value = "hookbrake1status_1")
    private java.lang.Integer hookbrake1status1;
	/**1#天车吊钩制动器2状态*/
	@Excel(name = "1#天车吊钩制动器2状态", width = 15)
    @ApiModelProperty(value = "1#天车吊钩制动器2状态")
    @TableField(value = "hookbrake2status_1")
    private java.lang.Integer hookbrake2status1;
	/**1#天车左限位*/
	@Excel(name = "1#天车左限位", width = 15)
    @ApiModelProperty(value = "1#天车左限位")
    @TableField(value = "carleftlimit_1")
    private java.lang.Integer carleftlimit1;
	/**1#天车右限位*/
	@Excel(name = "1#天车右限位", width = 15)
    @ApiModelProperty(value = "1#天车右限位")
    @TableField(value = "carrightlimit_1")
    private java.lang.Integer carrightlimit1;
	/**1#天车前限位*/
	@Excel(name = "1#天车前限位", width = 15)
    @ApiModelProperty(value = "1#天车前限位")
    @TableField(value = "carfrontlimit_1")
    private java.lang.Integer carfrontlimit1;
	/**1#天车后限位*/
	@Excel(name = "1#天车后限位", width = 15)
    @ApiModelProperty(value = "1#天车后限位")
    @TableField(value = "carbacklimit_1")
    private java.lang.Integer carbacklimit1;
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
	/**累计时间*/
	@Excel(name = "累计时间", width = 15)
    @ApiModelProperty(value = "累计时间")
    private java.lang.Double allTime;
	/**循环使用次数*/
	@Excel(name = "循环使用次数", width = 15)
    @ApiModelProperty(value = "循环使用次数")
    private java.lang.Integer allTimes;
	/**预留*/
	@Excel(name = "预留", width = 15)
    @ApiModelProperty(value = "预留")
    private java.lang.Integer reservedOne;
	/**预留*/
	@Excel(name = "预留", width = 15)
    @ApiModelProperty(value = "预留")
    private java.lang.Integer reservedTwo;
	/**2#天车高度*/
	@Excel(name = "2#天车高度", width = 15)
    @ApiModelProperty(value = "2#天车高度")
    @TableField(value = "crane_height_2")
    private java.lang.Double craneHeight2;
	/**2#天车吊重*/
	@Excel(name = "2#天车吊重", width = 15)
    @ApiModelProperty(value = "2#天车吊重")
    @TableField(value = "crane_crane_2")
    private java.lang.Double craneCrane2;
	/**2#天车横向行程*/
	@Excel(name = "2#天车横向行程", width = 15)
    @ApiModelProperty(value = "2#天车横向行程")
    @TableField(value = "transverse_carroute_2")
    private java.lang.Double transverseCarroute2;
	/**2#天车纵向行程*/
	@Excel(name = "2#天车纵向行程", width = 15)
    @ApiModelProperty(value = "2#天车纵向行程")
    @TableField(value = "longitudinal_carroute_2")
    private java.lang.Double longitudinalCarroute2;
	/**2#天车吊钩载荷状态*/
	@Excel(name = "2#天车吊钩载荷状态", width = 15)
    @TableField(value = "hookstatus_2")
    @ApiModelProperty(value = "2#天车吊钩载荷状态")
    private java.lang.Integer hookstatus2;
	/**2#天车吊钩制动器1状态*/
	@Excel(name = "2#天车吊钩制动器1状态", width = 15)
    @ApiModelProperty(value = "2#天车吊钩制动器1状态")
    @TableField(value = "hookbrake1status_2")
    private java.lang.Integer hookbrake1status2;
	/**2#天车吊钩制动器2状态*/
	@Excel(name = "2#天车吊钩制动器2状态", width = 15)
    @ApiModelProperty(value = "2#天车吊钩制动器2状态")
    @TableField(value = "hookbrake2status_2")
    private java.lang.Integer hookbrake2status2;
	/**2#天车左限位*/
	@Excel(name = "2#天车左限位", width = 15)
    @ApiModelProperty(value = "2#天车左限位")
    @TableField(value = "carleftlimit_2")
    private java.lang.Integer carleftlimit2;
	/**2#天车右限位*/
	@Excel(name = "2#天车右限位", width = 15)
    @ApiModelProperty(value = "2#天车右限位")
    @TableField(value = "carrightlimit_2")
    private java.lang.Integer carrightlimit2;
	/**2#天车前限位*/
	@Excel(name = "2#天车前限位", width = 15)
    @ApiModelProperty(value = "2#天车前限位")
    @TableField(value = "carfrontlimit_2")
    private java.lang.Integer carfrontlimit2;
	/**2#天车后限位*/
	@Excel(name = "2#天车后限位", width = 15)
    @ApiModelProperty(value = "2#天车后限位")
    @TableField(value = "carbacklimit_2")
    private java.lang.Integer carbacklimit2;
    /**1#超高预警*/
    @Excel(name = "1#超高预警", width = 15)
    @ApiModelProperty(value = "1#超高预警")
    @TableField(value = "overval1_height1")
    private java.lang.Integer overval1Height1;
    /**2#超高预警*/
    @Excel(name = "2#超高预警", width = 15)
    @ApiModelProperty(value = "2#超高预警")
    @TableField(value = "overval2_height2")
    private java.lang.Integer overval2Height2;
    /**1#超速预警*/
    @Excel(name = "1#超速预警", width = 15)
    @ApiModelProperty(value = "1#超速预警")
    @TableField(value = "overval3_speed1")
    private java.lang.Integer overval3Speed1;
    /**2#超速预警*/
    @Excel(name = "2#超速预警", width = 15)
    @ApiModelProperty(value = "2#超速预警")
    @TableField(value = "overval4_speed2")
    private java.lang.Integer overval4Speed2;
    /**风速预警*/
    @Excel(name = "风速预警", width = 15)
    @ApiModelProperty(value = "风速预警")
    @TableField(value = "overval5_wind")
    private java.lang.Integer overval5Wind;
    /**前支腿垂直度预警*/
    @Excel(name = "前支腿垂直度预警", width = 15)
    @ApiModelProperty(value = "前支腿垂直度预警")
    @TableField(value = "overval6_verticality")
    private java.lang.Integer overval6Verticality;
    /**风速*/
    @Excel(name = "风速", width = 15)
    @ApiModelProperty(value = "风速")
    private java.lang.Double windSpeed;

    private java.lang.Integer overval7Fhight;
    private java.lang.Integer overval8Bhight;
    private java.lang.Integer overval9Flimit;
    private java.lang.Integer overval10Blimit;
    private java.lang.Integer overval11Distance;
    private java.lang.Integer overval12Horizontal;

    // 角度
    @TableField(exist = false)
    private java.lang.Double jiaodu;
}
