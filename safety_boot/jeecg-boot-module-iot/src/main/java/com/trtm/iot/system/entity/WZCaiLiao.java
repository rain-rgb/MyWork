package com.trtm.iot.system.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class WZCaiLiao implements Serializable {
    private String name;// 材料名
    private String guige; // 规格型号
    private String llyongliang; // 理论用量
    private String sjyongliang; // 实际用量
    private String hslv; // 含水率
    private String hsliang; // 含水量
    private String hshilv; // 含石率
    private String hshiliang; // 含石量
    private String lcbh;//料仓编号
    private String pici;//检验批编号


}
