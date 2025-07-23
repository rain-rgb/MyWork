package com.trtm.iot.zhiliangtaizuocfg.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

@Data
public class ZhiLiangTjVO {
    /**
     * 制梁总数
     */
     private int sum;
    /**
     * 空闲制梁
     */
    private int zhiliang;
    /**
     * 出场统计
     */
    private int chuchangTJ;

    private int gangjingTJ;
    private int hunningtuTJ;
    private int yangshengTJ;
    private int zhanglaTJ;
    private int yajiangTJ;
    private int fengduanTJ;
    private int yanshouTJ;

    /**强度等级*/
    @Excel(name = "强度等级", width = 15)
    @ApiModelProperty(value = "强度等级")
    private java.lang.String betlev;

}
