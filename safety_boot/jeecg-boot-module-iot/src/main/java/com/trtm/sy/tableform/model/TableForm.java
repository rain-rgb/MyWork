package com.trtm.sy.tableform.model;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName("sy_dps_jc_testitem")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sy_dps_jc_testitem对象", description = "试验表格")
public class TableForm {

    private Integer id;
    private String uuid;
    private String titCode;
    private String ruleNo;
    private String tiNo;
    private String tiName;
    private String tiParameterNum;
    private String tiTableNum;
    private String tiTableName;
    private Integer tiDefaultGroupNum;
    private Integer tiSort;
    private String tiRemark;
    private String tiCustomTableNum;
    private Integer tiType;
    @TableLogic(value = "0", delval = "1")
    private Integer tiIsDel;
    private String tiJiShuZhiBiao;
    private String tiZhongYaoZhiBiao;
    private String tiZhongYaoZhiBiaoName;
    private String tiBaoGaoTaiZhangName;
    private String tiBaoGaoTaiZhangNo;
    private Integer plusMark;
    private String tip;
    private String tiPanDingYiJu;
    private String tiShiYanYiJu;

}
