package com.trtm.iot.devicepipepilehistorydataone.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class DevicePipepileDto implements Serializable {

    private static final long serialVersionUID = 1L;


    private String syjid;
    private String departName;
    private String pileMileage;
    private String pileNo;
    private String pileWorktime;
    private String pileUpress;
    private String pileDpress;
    private String pileSpeed;
    private String overproofStatus;
    private String approvalPerson;
    private String sbname;
    private String chaobiaodengji;
    private String pileRealdep;
    private String pileY;
    private Integer pileDesigndep;
    private String pileStarttime;
    private String pileTime;
}
