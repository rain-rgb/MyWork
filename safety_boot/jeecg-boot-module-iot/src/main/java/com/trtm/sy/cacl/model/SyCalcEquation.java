package com.trtm.sy.cacl.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName("sy_calc_equation")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sy_calc_equation对象", description = "试验计算公式")
public class SyCalcEquation {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "org_code")
    private String orgCode;

    @TableField(value = "ti_no")
    private String tiNo;

    @TableField(value = "infix")
    private String infix;

    @TableField(value = "suffix")
    private String suffix;

    @TableField(value = "res")
    private String res;

    @TableField(value = "bol")
    private String bol;

    @TableField(value = "remark")
    private String remark;

    @TableField(value = "order")
    private String order;

    @TableField(value = "xy")
    private String xy;

}
