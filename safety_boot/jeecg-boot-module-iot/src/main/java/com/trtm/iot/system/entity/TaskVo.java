package com.trtm.iot.system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lis1
 * @date 2022/12/13
 * @time 16:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String taskId;

    private String mixId;

    private String projectName;

    private String constructionPosition;

    private String pouringType;

    private String startTime;

    private String endTime;

    private String strengthGrade;

    private String slump;

    private Double taskVolume;

    private Double productionVolume;

    private Double rate;

    private String createId;
}
