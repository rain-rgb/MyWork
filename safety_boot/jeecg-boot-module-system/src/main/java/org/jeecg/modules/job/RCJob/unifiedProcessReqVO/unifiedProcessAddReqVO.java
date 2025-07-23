package org.jeecg.modules.job.RCJob.unifiedProcessReqVO;

import lombok.Data;

import java.io.Serializable;

@Data
public class unifiedProcessAddReqVO implements Serializable {
    private String appUrl;//app流程图url
    private String endTime;//流程完成时间
    private String pcUrl;//pc流程图url
    private Integer postId;//发起人岗位id
    private String postName;//发起人岗位名称
    private String processId;//流程id
    private String processName;//流程名称
    private Integer processStatus;//流程状态
    private String processType;//流程类型
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
