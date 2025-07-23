package org.jeecg.modules.job.RCJob.unifiedProcessReqVO;

import lombok.Data;

import java.io.Serializable;

@Data
public class unifiedTaskUpdateReqVO implements Serializable {
//    private String appId;//应用ID
    private String appRedirectUrl;//app跳转网址
    private Integer assigneeId;//候选人id
    private Integer assigneeName;//候选人姓名
    private String batchId;//批次Id
    private String bussId;//业务id
    private String comment;//任务评论
    private String endTime;//任务完成时间
    private Integer id;//主键
    private String pcRedirectUrl;//系统跳转网址
    private String processId;//流程id
    private String processName;//流程名称
    private String projectId;//项目id
    private String projectName;//项目名称
    private String sectionId;//标段id
    private String sectionName;//标段名称
    private Integer sourceId;//来源id
    private String sourceName;//来源名称
    private String startTime;//任务创建时间
    private String taskId;//任务id
    private String taskName;//任务名称
    private Integer taskStatus;//任务状态 0 未完成；1 已完成；2 已删除
}
