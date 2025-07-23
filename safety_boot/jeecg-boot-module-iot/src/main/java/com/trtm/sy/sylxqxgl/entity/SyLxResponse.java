package com.trtm.sy.sylxqxgl.entity;

import lombok.Data;

import java.util.List;

@Data
public class SyLxResponse implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


    private Integer id;
    private String uuid;
    private String titCode;
    private String titName;
    private String titSampleMark;
    private String titParentCode;
    private String titChildNodesNum;
    private String titRemark;
    private Integer titIsDel;
    private String tiPanDingYiJu;
    private String tiShiYanYiJu;
    private Integer titType;
    private String tiYangPinXinXiJL;
    private String tiYangPinXinXiBG;
    private String tiPushTableName;


    private List<SyLxResponse> children;
//    private List<SyJlbResponse> jlb;

}
