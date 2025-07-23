package com.trtm.iot.weiyan.vo;

public class IotWeiYanNormCfg {
    private Integer id;

    private Integer wallRockLevel;

    private String superviseType;

    private Integer detectionType;

    private Integer callLevel;

    private Double upNorm;

    private Double downNorm;

    private Integer successionDay;

    private Integer isSuccessionCall;

    private String birthTime;

    private String uploadTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWallRockLevel() {
        return wallRockLevel;
    }

    public void setWallRockLevel(Integer wallRockLevel) {
        this.wallRockLevel = wallRockLevel;
    }

    public String getSuperviseType() {
        return superviseType;
    }

    public void setSuperviseType(String superviseType) {
        this.superviseType = superviseType;
    }

    public Integer getDetectionType() {
        return detectionType;
    }

    public void setDetectionType(Integer detectionType) {
        this.detectionType = detectionType;
    }

    public Integer getCallLevel() {
        return callLevel;
    }

    public void setCallLevel(Integer callLevel) {
        this.callLevel = callLevel;
    }

    public Double getUpNorm() {
        return upNorm;
    }

    public void setUpNorm(Double upNorm) {
        this.upNorm = upNorm;
    }

    public Double getDownNorm() {
        return downNorm;
    }

    public void setDownNorm(Double downNorm) {
        this.downNorm = downNorm;
    }

    public Integer getSuccessionDay() {
        return successionDay;
    }

    public void setSuccessionDay(Integer successionDay) {
        this.successionDay = successionDay;
    }

    public Integer getIsSuccessionCall() {
        return isSuccessionCall;
    }

    public void setIsSuccessionCall(Integer isSuccessionCall) {
        this.isSuccessionCall = isSuccessionCall;
    }

    public String getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(String birthTime) {
        this.birthTime = birthTime == null ? null : birthTime.trim();
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime == null ? null : uploadTime.trim();
    }
}