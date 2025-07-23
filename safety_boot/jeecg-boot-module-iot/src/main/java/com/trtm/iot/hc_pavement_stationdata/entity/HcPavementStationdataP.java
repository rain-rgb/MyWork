package com.trtm.iot.hc_pavement_stationdata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: 获取逐桩数据
 * @Author: jeecg-boot
 * @Date:   2023-04-24
 * @Version: V1.0
 */
public class HcPavementStationdataP implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "桩号")
    private String station;
    @ApiModelProperty(value = " 摊铺温度（°C）")
    private String pavingtemperature;
    @ApiModelProperty(value = "初压温度（°C）钢轮最高温度")
    private String temperaturefirst;
    @ApiModelProperty(value = "复压温度（°C）胶轮最高温度")
    private String temperaturerepeat;
    @ApiModelProperty(value = "初压遍数（遍）钢轮遍数")
    private String timesfirst;
    @ApiModelProperty(value = "复压遍数（遍）胶轮遍数")
    private String timesrepeat;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getPavingtemperature() {
        return pavingtemperature;
    }

    public void setPavingtemperature(String pavingtemperature) {
        this.pavingtemperature = pavingtemperature;
    }

    public String getTemperaturefirst() {
        return temperaturefirst;
    }

    public void setTemperaturefirst(String temperaturefirst) {
        this.temperaturefirst = temperaturefirst;
    }

    public String getTemperaturerepeat() {
        return temperaturerepeat;
    }

    public void setTemperaturerepeat(String temperaturerepeat) {
        this.temperaturerepeat = temperaturerepeat;
    }

    public String getTimesfirst() {
        return timesfirst;
    }

    public void setTimesfirst(String timesfirst) {
        this.timesfirst = timesfirst;
    }

    public String getTimesrepeat() {
        return timesrepeat;
    }

    public void setTimesrepeat(String timesrepeat) {
        this.timesrepeat = timesrepeat;
    }
}
