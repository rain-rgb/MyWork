package org.jeecg.modules.job.RCJob.unifiedProcessReqVO;

import lombok.Data;

import java.io.Serializable;

@Data
public class unifiedProcessUpdateReqVO implements Serializable {
    private String appUrl;//app流程图url
    private String bussId;//业务id
    private String endTime;//流程完成时间
    private String pcUrl;//pc流程图url
    private Integer postId;//发起人岗位id
    private String postName;//发起人岗位名称
    private String processId;//流程id(和bussId至少存在一个)
    private String processName;//流程名称
    private Integer processStatus;//流程状态 0 未完成；1 已完成；2 已删除
    private String projectId;//项目id
    private String projectName;//项目名称
    private String sectionId;//标段id
    private String sectionName;//标段名称
    private Integer sourceId;//来源id
    private String sourceName;//来源名称
    private String startTime;//流程创建时间
    private Integer startUserId;//发起人id
    private String startUserName;//发起人姓名

}
