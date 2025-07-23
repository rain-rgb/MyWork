package com.trtm.iot.wztaizhang.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "wztaizhangVo对象", description = "wztaizhang主表")
public class WztaizhangVo implements Serializable {

    /**
     * 编号
     */
    private String orgCode;
    /**
     * 公司名称
     */
    private String gsName;
    /**
     * 材料类型
     */
    private String nodetype;
    /**
     * 材料名称
     */
    private String typeName;
    /**
     * 材料编号
     */
    private String cailiaoNo;
    /**
     * 净重总量
     */
    private String jingzhongt;
    /**
     * 不合格的净重量
     */
    private String bhgJingzhong;
    /**
     * 不合格净重占比
     */
    private String bhgJingzhongZb;
    /**
     * 总批次数
     */
    private String pici;
    /**
     * 不合格批次数
     */
    private String bhgPici;
    /**
     * 不合格批次占比
     */
    private String bhgPiciZb;
    /**
     * 合格率占比
     */
    private String passrate;
    /**
     * 闭合率
     */
    private String bhl;//闭合率
    /**
     * 项目名称
     */
    private String departName;

}
