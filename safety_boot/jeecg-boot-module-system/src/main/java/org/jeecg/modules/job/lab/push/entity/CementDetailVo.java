package org.jeecg.modules.job.lab.push.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author lis1
 * @date 2022/12/15
 * @time 11:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CementDetailVo implements Serializable {

    private static final long serialVersionUID = 1L;

    // 任务详情主键
    private String taskdatailId;

    // 混凝土主表batch_no
    private String useId;

    // 拌合站id
    private String mixId;

    // 原材料名称
    private String material;

    // 理论用量
    private Double theoreticalDosage;

    // 理论用量
    private Double actualDosage;

    // 误差值
    private Double errorValue;

    // 超标值
    private Double exceedanceValue;

    // 超标等级 ：1合格 2初级超标 3中级超标 4高级超标
    private String exceedanceLevel;

    // 创建人
    private String createId;
}
