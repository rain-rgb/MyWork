package com.trtm.iot.zhangla.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class OverHandler implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String baseId;
    private String problemReasons;
    private String handleWay;
    private String handleResult;
    private String handleTime;
    private String handlePerson;
    private String supervisorApproval;
    private String supervisorResult;
    private String supervisorHandleTime;
    private String approvalPerson;
    private String remark;
    private String filePath;
    private String filePath2;
    private String overproofStatus;



}
