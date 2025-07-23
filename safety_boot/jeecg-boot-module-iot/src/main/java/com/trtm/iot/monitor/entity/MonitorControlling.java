package com.trtm.iot.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: monitor
 * @Author: jeecg-boot
 * @Date:   2021-12-16
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="monitor对象", description="monitor")
public class MonitorControlling implements Serializable {
    private static final long serialVersionUID = 1L;

    private String cameraIndexCode;
    private String command; // 命令
    private Integer action; // 0-开始 1-停止 注：GOTO_PRESET命令下填任意值均可转到预置点,建议填0即可
//    private Integer speed; // 台速度，取值范围为1-100，默认50
//    private Integer presetIndex; // 预置点编号，整数，通常在300以内


    //    不区分大小写
//    command 命令:
//    LEFT 左转
//    RIGHT右转
//    UP 上转
//    DOWN 下转
//    ZOOM_IN 焦距变大
//    ZOOM_OUT 焦距变小
//    LEFT_UP 左上
//    LEFT_DOWN 左下
//    RIGHT_UP 右上
//    RIGHT_DOWN 右下 FOCUS_NEAR 焦点前移
//    FOCUS_FAR 焦点后移 IRIS_ENLARGE 光圈扩大
//    IRIS_REDUCE 光圈缩小
//    WIPER_SWITCH 接通雨刷开关
//    START_RECORD_TRACK开始记录轨迹
//    STOP_RECORD_TRACK 停止记录轨迹
//    START_TRACK 开始轨迹
//    STOP_TRACK 停止轨迹
//
//    以下命令presetIndex不可
//    为空：
//    GOTO_PRESET到预置点

}
