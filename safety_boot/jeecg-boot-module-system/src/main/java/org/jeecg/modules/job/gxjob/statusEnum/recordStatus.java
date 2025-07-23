package org.jeecg.modules.job.gxjob.statusEnum;

/**
 * 打卡状态枚举类
 */
public enum recordStatus {
    //正常
    Normal("正常",0),
    //早退
    Early("早退",1),
    //迟到
    Late("迟到",2),
    //严重迟到
    SeriousLate("严重迟到",3),
    //旷工
    Absenteeism("旷工",4),
    //未打卡
    NotSigned("未打卡",5);

    private String recordType;
    private Integer status;

    recordStatus(String recordType, Integer status) {
        this.recordType = recordType;
        this.status = status;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
