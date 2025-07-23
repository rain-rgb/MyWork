package com.trtm.sy.sydpssysample.entity;

import lombok.Data;

@Data
public class SyRequest {

    private Integer pageNo;
    private Integer pageSize;
    private String orgCode;
    private String titCode;
    private String sampleState;
    private String sampleNo;
    private String sampleName;
    private String sampleGcbw;
    private String titType;
    private String sampleDate;
    private String reportNo;
    private String tiNo;
    private Boolean lookself;
    private String signature;
    private String shenpizhuangtai;
    private String qianzhangzhuangtai;
    private String lq;
    private String username;
    private String sys_depart_orgcode;//组织机构
}
