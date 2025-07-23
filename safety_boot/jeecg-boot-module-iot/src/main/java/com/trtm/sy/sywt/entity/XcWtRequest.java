package com.trtm.sy.sywt.entity;

import lombok.Data;

@Data
public class XcWtRequest {

    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private Integer quyangzhuangtai;
    private Integer ypzt;
    private String shouyangzhuangtai;
    private String quyangren;
    private String quyangshijian;
    private String shouyangren;
    private String shouyangshijian;
    private String sys_depart_orgcode;
    private String titCode;
    private String cailiaomingcheng;
    private String xcjc;

}
