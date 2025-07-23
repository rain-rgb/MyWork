package com.trtm.iot.devicepipepilerealdata.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
 * @Description: device_pipepile_realdata
 * @Author: jeecg-boot
 * @Date:   2022-07-21
 * @Version: V1.0
 */
@Data
@TableName("device_pipepile_realdata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_pipepile_realdata对象", description="device_pipepile_realdata")
public class DevicePipepileRealdata implements Serializable {
    private static final long serialVersionUID = 1L;

	/**序号*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "序号")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**设计桩长(m)*/
	@Excel(name = "设计桩长(m)", width = 15)
    @ApiModelProperty(value = "设计桩长(m)")
    private java.lang.String designdep;
	/**当前桩长度(m)*/
	@Excel(name = "当前桩长度(m)", width = 15)
    @ApiModelProperty(value = "当前桩长度(m)")
    private java.lang.String curdep;
	/**离地桩长(m)*/
	@Excel(name = "离地桩长(m)", width = 15)
    @ApiModelProperty(value = "离地桩长(m)")
    private java.lang.String grounddep;
	/**最大垂直度(%)*/
	@Excel(name = "最大垂直度(%)", width = 15)
    @ApiModelProperty(value = "最大垂直度(%)")
    private java.lang.String pileY;
	/**最大压桩力(KN)*/
	@Excel(name = "最大压桩力(KN)", width = 15)
    @ApiModelProperty(value = "最大压桩力(KN)")
    private java.lang.String pileUpress;
	/**最大夹持力(KN)*/
	@Excel(name = "最大夹持力(KN)", width = 15)
    @ApiModelProperty(value = "最大夹持力(KN)")
    private java.lang.String pileDpress;
	/**平均桩压力(KN)*/
	@Excel(name = "平均桩压力(KN)", width = 15)
    @ApiModelProperty(value = "平均桩压力(KN)")
    private java.lang.String pileAveupress;
	/**平均夹持力(KN)*/
	@Excel(name = "平均夹持力(KN)", width = 15)
    @ApiModelProperty(value = "平均夹持力(KN)")
    private java.lang.String pileAvedpress;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15)
    @ApiModelProperty(value = "开始时间")
    private java.lang.String pileStarttime;
	/**数据时间*/
	@Excel(name = "数据时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据时间")
    private java.util.Date datatime;
	/**速度*/
	@Excel(name = "速度", width = 15)
    @ApiModelProperty(value = "速度")
    private java.lang.String speed;
	/**接桩时间*/
	@Excel(name = "接桩时间", width = 15)
    @ApiModelProperty(value = "接桩时间")
    private java.lang.String piletime;
	/**有效桩长(m)*/
	@Excel(name = "有效桩长(m)", width = 15)
    @ApiModelProperty(value = "有效桩长(m)")
    private java.lang.String pileEffdep;
	/**时长*/
	@Excel(name = "时长", width = 15)
    @ApiModelProperty(value = "时长")
    private java.lang.String pileWorktime;
	/**打桩次数*/
	@Excel(name = "打桩次数", width = 15)
    @ApiModelProperty(value = "打桩次数")
    private java.lang.Integer times;
	/**工作状态*/
	@Excel(name = "工作状态", width = 15)
    @ApiModelProperty(value = "工作状态")
    private java.lang.String worksta;
	/**时间戳*/
	@Excel(name = "时间戳", width = 15)
    @ApiModelProperty(value = "时间戳")
    private java.lang.String ts;
	/**桩号*/
	@Excel(name = "桩号", width = 15)
    @ApiModelProperty(value = "桩号")
    private java.lang.String pileno;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String ltdfloat;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String lgdfloat;
	/**里程*/
	@Excel(name = "里程", width = 15)
    @ApiModelProperty(value = "里程")
    private java.lang.String mileage;
    /**电流*/
    @Excel(name = "电流", width = 15)
    @ApiModelProperty(value = "电流")
    private String elml;
    private java.lang.String rjrwd;

    public void setWorksta(String worksta){
        Date datatime = this.getDatatime();
//            Date parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(pileTime);
        // 当前时间
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = dateFormat.format(date);
        String pileTime = dateFormat.format(datatime);

        long diff = 0;
        try {
            Date d1 = dateFormat.parse(pileTime);//毫秒ms
            Date d2 = dateFormat.parse(format);//毫秒ms
            diff = d2.getTime() - d1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 如果大于5分钟，设备离线，否则在线
        if (diff > 1000*60*30){
            this.worksta = ("离线");
            this.pileno = ("无");
        }else {
            if (worksta.equals("1")){
                this.worksta = ("上钻");
            }else {
                this.worksta = ("下钻");
            }
        }
    }

//    public void setWorksta(String worksta) {
//        this.worksta = worksta;
//    }
}
