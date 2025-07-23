package com.trtm.iot.hntbhz.vo;

import lombok.Data;

import java.util.List;

@Data
public class BhzCementDetailCbTongji {
    private String shebeiName;
    private Double zongchangliang;
    private Integer allDish;
    private Integer allOverDish;
    private Double chaobiaolv;
    private Integer level;
    private Integer xiguliao;//细骨料
    private Integer xiguliao_advanced;//粗骨料(大石)
    private Integer xiguliao_middle;//粗骨料(中石)
    private Integer xiguliao_primary;//粗骨料(小石)
    private Integer water;//水
    private Integer shuini;//水泥
    private Integer kuangfen;//矿粉
    private Integer fenmeihui;//粉煤灰
    private Integer waijiaji;//外加剂
    private Integer qita;//其他



}
