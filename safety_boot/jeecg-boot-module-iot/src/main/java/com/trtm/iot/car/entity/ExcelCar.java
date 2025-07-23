package com.trtm.iot.car.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExcelCar implements Serializable {
    private String sbName;// 设备名称
    private String sbType; // 设备类型
    private Integer runDay; // 启用天数
    private double runTime; // 运转时间（h）
    private Double runKm; // 运转公里（km）
    private String chuQinLv; // 出勤率
    private String liYongLv; // 利用率
    private String start; //
    private String end; //




}
