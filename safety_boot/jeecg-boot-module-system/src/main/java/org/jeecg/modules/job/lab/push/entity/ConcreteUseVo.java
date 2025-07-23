package org.jeecg.modules.job.lab.push.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author lis1
 * @date 2022/12/15
 * @time 15:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConcreteUseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    // 主键id
    private String useId;

    // 任务单编号
    private String taskId;

    // 拌合站ID
    private String mixId;

    // 设备名称
    private String deviceName;

    // 浇筑部位
    private String position;

    // 生产方量
    private Double capacity;

    // 强度等级
    private String strength;

    // 出料时间
    private String dischargeTime;

    // 创建人
    private String createId;
}
