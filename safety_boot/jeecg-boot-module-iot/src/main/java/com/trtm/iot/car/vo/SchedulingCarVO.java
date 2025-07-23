package com.trtm.iot.car.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "运输车发车单详情")
public class SchedulingCarVO implements Serializable {

    /**
     * 项目发车单名称
     */
    private String fcdname;
    /**
     * 搅拌站号
     */
    private String zhanno;
    /**
     * 生产日期
     */
    private String dattim;
    /**
     * 任务单号
     */
    private String code;
    /**
     * 客户名称
     */
    private String customer;
    /**
     * 工程名称
     */
    private String projname;
    /**
     * 强度等级
     */
    private String betlev;
    /**
     * 坍落度
     */
    private String lands;
    /**
     * 施工部位
     */
    private String conspos;
    /**
     * 砂浆方量（计划方量）
     */
    private Double mormete;
    /**
     * 生产方量（本车方量）
     */
    private Double prodmete;
    /**
     * 本车方量（累计方量）
     */
    private Double totmete;
    /**
     * 累计车次
     */
    private Integer totvehs;
    /**
     * 驾驶员
     */
    private String driver;
    /**
     * 运输车号
     */
    private String vehicle;
//    /**
//     * 发货时间
//     */
//    private String XX;
//    /**
//     * 返回时间
//     */
//    private String XX1;
    /**
     * 运输距离
     */
    private Double distance;
//    /**
//     * 到达时间
//     */
//    private Date etatime;
//    /**
//     * 开始浇筑时间
//     */
//    private Date starttim;
//    /**
//     * 完成浇筑时间
//     */
//    private Date endtim;
    /**
     * 操作员
     */
    private String handler;
//    /**
//     * 调度
//     */
//    private String YY1;

    private List<Map<String, String>> info;

}
