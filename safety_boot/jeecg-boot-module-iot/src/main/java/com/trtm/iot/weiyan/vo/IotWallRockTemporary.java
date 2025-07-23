package com.trtm.iot.weiyan.vo;



public class IotWallRockTemporary {
    private String shebeiNo;

    private String jobLocation;

    private String sectionName;

    private String sectionLength;

    private String sectionWidth;

    private String excavationType;

    private String wallRockLevel;

    private String measuringPoint;

    private Integer successionDay;

    public String getShebeiNo() {
        return shebeiNo;
    }

    public void setShebeiNo(String shebeiNo) {
        this.shebeiNo = shebeiNo == null ? null : shebeiNo.trim();
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation == null ? null : jobLocation.trim();
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName == null ? null : sectionName.trim();
    }

    public String getSectionLength() {
        return sectionLength;
    }

    public void setSectionLength(String sectionLength) {
        this.sectionLength = sectionLength == null ? null : sectionLength.trim();
    }

    public String getSectionWidth() {
        return sectionWidth;
    }

    public void setSectionWidth(String sectionWidth) {
        this.sectionWidth = sectionWidth == null ? null : sectionWidth.trim();
    }

    public String getExcavationType() {
        return excavationType;
    }

    public void setExcavationType(String excavationType) {
        this.excavationType = excavationType == null ? null : excavationType.trim();
    }

    public String getWallRockLevel() {
        return wallRockLevel;
    }

    public void setWallRockLevel(String wallRockLevel) {
        this.wallRockLevel = wallRockLevel == null ? null : wallRockLevel.trim();
    }

    public String getMeasuringPoint() {
        return measuringPoint;
    }

    public void setMeasuringPoint(String measuringPoint) {
        this.measuringPoint = measuringPoint == null ? null : measuringPoint.trim();
    }

    public Integer getSuccessionDay() {
        return successionDay;
    }
    public void setSuccessionDay(Integer successionDay) {
        this.successionDay = successionDay;
    }
}