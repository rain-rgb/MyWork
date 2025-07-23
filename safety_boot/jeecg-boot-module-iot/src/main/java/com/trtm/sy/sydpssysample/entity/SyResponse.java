package com.trtm.sy.sydpssysample.entity;

import lombok.Data;

@Data
public class SyResponse {
    private String id;
    private String sampleNo;
    private String sampleName;
    private String sampleGcbw;
    private String sampleDate;
    private String sampleQuYangDiDian;
    private String sampleQuYangRen;
    private Integer sampleState;
    private String reportNoNew;
    private String reportCreateDate;
    private String reportEditPerson;
    private String sampleCreatePerson;
    private String sampleCreateDate;
    private Integer shenpizhuangtai;
    private Integer qianzhangzhuangtai;
    private String pdfurl;
}
