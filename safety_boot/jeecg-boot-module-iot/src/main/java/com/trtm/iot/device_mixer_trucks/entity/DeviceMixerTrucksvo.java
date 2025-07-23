package com.trtm.iot.device_mixer_trucks.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: 搅拌车监控
 * @Author: jeecg-boot
 * @Date:   2025-05-06
 * @Version: V1.0
 */
@Data
@TableName("device_mixer_trucks")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_mixer_trucks对象", description="搅拌车监控")
public class DeviceMixerTrucksvo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String devicename;                    //设备名称
    private String deviceid;                      //设备id
    private double lat;                           //wgs84格式的纬度
    private double lon;                           //wgs84格式的经度
    private long updatetime;                      //到达时间 utc 时间
    private long validpoistiontime;               //最后有效的定位时间
    private double radius;                        //定位半径 单位米
    private double speed;                         //速度 单位米
    private double altitude;                      //高度 米
    private int course;                           //角度 方向 0~360度
    private int totaldistance;                    //服务器计算的总里程米 the server calculate the total distance
    private long status;                          //状态
    private String strstatus;                     //状态描述
    private long alarm;                           //报警
    private String stralarm;                      //报警描述
    private String gotsrc;                        //cell wifi gps 定位类型
    private int rxlevel;                          //接收信号强度 百分比 10 40 60 80 100 这个固定值
    private double voltagev;                      //电压V，有的设备没有电压
    private int voltagepercent;                   //电量百分比，有的设备百分比是通过上报次数计算百分比
    private int gpsvalidnum;                      //卫星个数
    int iostatus=-1;//IO 状态位
    short rotatestatus=-1;//0x52	1	正反转(0:未知；1：正转（空车）2:反转（重车）；3：停转)
    private short reportmode;
    //0x00 定时上报
    //0x01 定距上报
    //0x02 拐点上传
    //0x03 ACC 状态改变上传
    //0x04 从运动变为静止状态后，补传最后一个定位点
    //0x05 网络断开重连后，上报之前最后一个有效上传点
    //0X06 上报模式：星历更新强制上传 GPS 点
    //0X07 上报模式：按键上传定位点
    //0X08 上报模式：开机上报位置信息
    //0X09 上报模式：未使用
    //0X0A 上报模式：设备静止后上报最后的经纬度，但时间更新
    //0X0B WIFI 解析经纬度上传包
    //0X0C 上报模式：LJDW（立即定位）指令上报
    //0X0D 上报模式：设备静止后上报最后的经纬度
    //0X0E 上报模式：GPSDUP 上传（下静止状态定时上传）
    private int moving;//是否在运动 0代表没运动 1代表在运动 判断依据当前位置跟上一次的位置是否超过1米
    private long parkduration;//停留了多久,单位utc时间
}
