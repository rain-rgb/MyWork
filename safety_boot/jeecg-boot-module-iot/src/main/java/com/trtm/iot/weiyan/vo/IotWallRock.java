package com.trtm.iot.weiyan.vo;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class IotWallRock {

    private List<String> officeIdList;//传入的标段id值组，
    private List<String> overProofStatusList;//传入的处置状态值组，未处理['10','15']，已处理未审核[10]
    private String id;
    private String sectionNum;
    private String batchNo;
    private String unitName;
    private String sectionId;
    private String shebeiNo;

    private String jobLocation;

    private String sectionName;

    private String sectionLength;

    private String sectionWidth;

    private String excavationType;

    private String wallRockLevel;

    private String measuringPoint;

    private Double measuringNumber;

    private Double onceDeformation;

    private Double intervalTime;

    private Integer intervalFirstTime;

    private Double deformationRate;

    private Integer onceStatus;

    private Integer accumulatedStatus;

    private Double accumulatedDeformation;

    private Integer alertstate;
    private Integer isUse;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;
    private String measuringStatus; // 测量状态（0：正常 ,1：重新布设）

    private Integer callLevel;
    private Integer superviseType;
    private Integer sortStatus;//0位正序，1为倒序
    private  Integer lastOrNextData;//根据数据采集时间，以此属性判断 0：为临近时间的一条数据 1：为最早的一条数据
    private String problemRecord;
    private String longName;

    private String hId;
    private String problemReasons;
    private String handleWay;
    private String handleResult;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime;
    private String handlePerson;
    private String supervisorApproval;
    private String  supercisorResult;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date supervisorHandleTime;
    private String approvalPerson;

    private String zhbApproval;
    private String  zhbResult;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date zhbHandleTime;
    private String zhbPerson;
    private String zhbOpinion;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date detectionTime;//检测方原因录入时间
    private String detectionReasons;//检测方原因分析
    private String detectionPerson;//检测方人员姓名
    private String detectionFilePath;//检测方附件上传
    private String remark;
    private String  filePath;
    private Integer overProofStatus;
    private String  byTheOpinion;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date conStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date conEndDate;
    private Integer quantityall;
    private Integer quantity;
    private Integer  callSum;
    private  Integer order;  //0为asc，1为desc
    private String nexthandlePerson;

    public Integer getQuantityall() {
        return quantityall;
    }

    public void setQuantityall(Integer quantityall) {
        this.quantityall = quantityall;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getCallSum() {
        return callSum;
    }

    public void setCallSum(Integer callSum) {
        this.callSum = callSum;
    }

    public String getMeasuringStatus() {
        return measuringStatus;
    }

    public void setMeasuringStatus(String measuringStatus) {
        this.measuringStatus = measuringStatus;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getZhbApproval() {
        return zhbApproval;
    }

    public void setZhbApproval(String zhbApproval) {
        this.zhbApproval = zhbApproval;
    }

    public String getZhbResult() {
        return zhbResult;
    }

    public void setZhbResult(String zhbResult) {
        this.zhbResult = zhbResult;
    }

    public Date getZhbHandleTime() {
        return zhbHandleTime;
    }

    public void setZhbHandleTime(Date zhbHandleTime) {
        this.zhbHandleTime = zhbHandleTime;
    }

    public String getZhbPerson() {
        return zhbPerson;
    }

    public void setZhbPerson(String zhbPerson) {
        this.zhbPerson = zhbPerson;
    }

    public String getZhbOpinion() {
        return zhbOpinion;
    }

    public void setZhbOpinion(String zhbOpinion) {
        this.zhbOpinion = zhbOpinion;
    }

    @Override
    public String toString() {
        return "IotWallRock{" +
                "officeIdList=" + officeIdList +
                ", overProofStatusList=" + overProofStatusList +
                ", id=" + id +
                ", unitName='" + unitName + '\'' +
                ", sectionId='" + sectionId + '\'' +
                ", shebeiNo='" + shebeiNo + '\'' +
                ", jobLocation='" + jobLocation + '\'' +
                ", sectionName='" + sectionName + '\'' +
                ", sectionLength='" + sectionLength + '\'' +
                ", sectionWidth='" + sectionWidth + '\'' +
                ", excavationType='" + excavationType + '\'' +
                ", wallRockLevel='" + wallRockLevel + '\'' +
                ", measuringPoint='" + measuringPoint + '\'' +
                ", measuringNumber=" + measuringNumber +
                ", onceDeformation=" + onceDeformation +
                ", intervalTime=" + intervalTime +
                ", deformationRate=" + deformationRate +
                ", onceStatus=" + onceStatus +
                ", accumulatedStatus=" + accumulatedStatus +
                ", accumulatedDeformation=" + accumulatedDeformation +
                ", alertstate=" + alertstate +
                ", isUse=" + isUse +
                ", birthTime=" + birthTime +
                ", uploadTime=" + uploadTime +
                ", callLevel=" + callLevel +
                ", superviseType=" + superviseType +
                ", sortStatus=" + sortStatus +
                ", hId='" + hId + '\'' +
                ", problemReasons='" + problemReasons + '\'' +
                ", handleWay='" + handleWay + '\'' +
                ", handleResult='" + handleResult + '\'' +
                ", handleTime=" + handleTime +
                ", handlePerson='" + handlePerson + '\'' +
                ", supervisorApproval='" + supervisorApproval + '\'' +
                ", supercisorResult='" + supercisorResult + '\'' +
                ", supervisorHandleTime=" + supervisorHandleTime +
                ", approvalPerson='" + approvalPerson + '\'' +
                ", remark='" + remark + '\'' +
                ", filePath='" + filePath + '\'' +
                ", overProofStatus=" + overProofStatus +
                ", byTheOpinion='" + byTheOpinion + '\'' +
                ", conStartDate=" + conStartDate +
                ", conEndDate=" + conEndDate +
                '}';
    }


    public String getSectionNum() {
        return sectionNum;
    }

    public void setSectionNum(String sectionNum) {
        this.sectionNum = sectionNum;
    }

    public Integer getIntervalFirstTime() {
        return intervalFirstTime;
    }

    public void setIntervalFirstTime(Integer intervalFirstTime) {
        this.intervalFirstTime = intervalFirstTime;
    }

    public List<String> getOverProofStatusList() {
        return overProofStatusList;
    }

    public void setOverProofStatusList(List<String> overProofStatusList) {
        this.overProofStatusList = overProofStatusList;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getSortStatus() {
        return sortStatus;
    }

    public void setSortStatus(Integer sortStatus) {
        this.sortStatus = sortStatus;
    }
    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
    }

    public List<String> getOfficeIdList() {
        return officeIdList;
    }

    public void setOfficeIdList(List<String> officeIdList) {
        this.officeIdList = officeIdList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShebeiNo() {
        return shebeiNo;
    }

    public void setShebeiNo(String shebeiNo) {
        this.shebeiNo = shebeiNo;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionLength() {
        return sectionLength;
    }

    public void setSectionLength(String sectionLength) {
        this.sectionLength = sectionLength;
    }

    public String getSectionWidth() {
        return sectionWidth;
    }

    public void setSectionWidth(String sectionWidth) {
        this.sectionWidth = sectionWidth;
    }

    public String getExcavationType() {
        return excavationType;
    }

    public void setExcavationType(String excavationType) {
        this.excavationType = excavationType;
    }

    public String getWallRockLevel() {
        return wallRockLevel;
    }

    public void setWallRockLevel(String wallRockLevel) {
        this.wallRockLevel = wallRockLevel;
    }

    public String getMeasuringPoint() {
        return measuringPoint;
    }

    public void setMeasuringPoint(String measuringPoint) {
        this.measuringPoint = measuringPoint;
    }

    public Double getMeasuringNumber() {
        return measuringNumber;
    }

    public void setMeasuringNumber(Double measuringNumber) {
        this.measuringNumber = measuringNumber;
    }

    public Double getOnceDeformation() {
        return onceDeformation;
    }

    public void setOnceDeformation(Double onceDeformation) {
        this.onceDeformation = onceDeformation;
    }

    public Double getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Double intervalTime) {
        this.intervalTime = intervalTime;
    }

    public Double getDeformationRate() {
        return deformationRate;
    }

    public void setDeformationRate(Double deformationRate) {
        this.deformationRate = deformationRate;
    }

    public Integer getOnceStatus() {
        return onceStatus;
    }

    public void setOnceStatus(Integer onceStatus) {
        this.onceStatus = onceStatus;
    }

    public Integer getAccumulatedStatus() {
        return accumulatedStatus;
    }

    public void setAccumulatedStatus(Integer accumulatedStatus) {
        this.accumulatedStatus = accumulatedStatus;
    }

    public Double getAccumulatedDeformation() {
        return accumulatedDeformation;
    }

    public void setAccumulatedDeformation(Double accumulatedDeformation) {
        this.accumulatedDeformation = accumulatedDeformation;
    }

    public Integer getAlertstate() {
        return alertstate;
    }

    public void setAlertstate(Integer alertstate) {
        this.alertstate = alertstate;
    }

    public Date getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(Date birthTime) {
        this.birthTime = birthTime;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getSectionId() {
        return sectionId;
    }
    
    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public Integer getCallLevel() {
        return callLevel;
    }

    public void setCallLevel(Integer callLevel) {
        this.callLevel = callLevel;
    }

    public Integer getSuperviseType() {
        return superviseType;
    }

    public void setSuperviseType(Integer superviseType) {
        this.superviseType = superviseType;
    }

    public String gethId() {
        return hId;
    }

    public void sethId(String hId) {
        this.hId = hId;
    }

    public String getProblemReasons() {
        return problemReasons;
    }

    public void setProblemReasons(String problemReasons) {
        this.problemReasons = problemReasons;
    }

    public String getHandleWay() {
        return handleWay;
    }

    public void setHandleWay(String handleWay) {
        this.handleWay = handleWay;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public String getHandlePerson() {
        return handlePerson;
    }

    public void setHandlePerson(String handlePerson) {
        this.handlePerson = handlePerson;
    }

    public String getSupervisorApproval() {
        return supervisorApproval;
    }

    public void setSupervisorApproval(String supervisorApproval) {
        this.supervisorApproval = supervisorApproval;
    }

    public String getSupercisorResult() {
        return supercisorResult;
    }

    public void setSupercisorResult(String supercisorResult) {
        this.supercisorResult = supercisorResult;
    }

    public Date getSupervisorHandleTime() {
        return supervisorHandleTime;
    }

    public void setSupervisorHandleTime(Date supervisorHandleTime) {
        this.supervisorHandleTime = supervisorHandleTime;
    }

    public String getApprovalPerson() {
        return approvalPerson;
    }

    public void setApprovalPerson(String approvalPerson) {
        this.approvalPerson = approvalPerson;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getOverProofStatus() {
        return overProofStatus;
    }

    public void setOverProofStatus(Integer overProofStatus) {
        this.overProofStatus = overProofStatus;
    }

    public String getByTheOpinion() {
        return byTheOpinion;
    }

    public void setByTheOpinion(String byTheOpinion) {
        this.byTheOpinion = byTheOpinion;
    }

    public Date getConStartDate() {
        return conStartDate;
    }

    public void setConStartDate(Date conStartDate) {
        this.conStartDate = conStartDate;
    }

    public Date getConEndDate() {
        return conEndDate;
    }

    public void setConEndDate(Date conEndDate) {
        this.conEndDate = conEndDate;
    }

    public Integer getLastOrNextData() {
        return lastOrNextData;
    }

    public void setLastOrNextData(Integer lastOrNextData) {
        this.lastOrNextData = lastOrNextData;
    }

    public String getProblemRecord() {
        return problemRecord;
    }

    public void setProblemRecord(String problemRecord) {
        this.problemRecord = problemRecord;
    }

    public String getLongName() {



       return longName;
    }

    public Date getDetectionTime() {
        return detectionTime;
    }

    public void setDetectionTime(Date detectionTime) {
        this.detectionTime = detectionTime;
    }

    public String getDetectionReasons() {
        return detectionReasons;
    }

    public void setDetectionReasons(String detectionReasons) {
        this.detectionReasons = detectionReasons;
    }

    public String getDetectionPerson() {
        return detectionPerson;
    }

    public void setDetectionPerson(String detectionPerson) {
        this.detectionPerson = detectionPerson;
    }

    public String getDetectionFilePath() {
        return detectionFilePath;
    }

    public void setDetectionFilePath(String detectionFilePath) {
        this.detectionFilePath = detectionFilePath;
    }

    public void setLongName(String longName) {

        String a=   this.sectionNum;
String  tunnelName="";
        if(a.isEmpty()){
            tunnelName=longName;
        }
         else  if(a.equals("TJ01")||a.equals("TJ02")||a.equals("TJ03")){
            String substring = longName.substring(0, longName.indexOf("/"));
            tunnelName=substring;

        }else if(a.equals("TJ04")||a.equals("TJ05")||a.equals("TJ06")){
            String substring = longName.substring(longName.indexOf("/")+1 );
            String substring1 = substring.substring(0, substring.indexOf("/"));
            tunnelName=substring1;
        }
        this.longName = tunnelName;
    }

    public String getNexthandlePerson() {
        return nexthandlePerson;
    }

    public void setNexthandlePerson(String nexthandlePerson) {
        this.nexthandlePerson = nexthandlePerson;
    }
}
