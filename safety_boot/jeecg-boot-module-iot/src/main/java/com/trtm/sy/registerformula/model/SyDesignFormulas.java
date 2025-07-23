package com.trtm.sy.registerformula.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("sy_design_formulas")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sy_design_formulas对象", description = "sy_design_formulas")
public class SyDesignFormulas implements Serializable {
    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.AUTO)
    private Integer id; //主键
    private String jlbbm;  //表号
    private String infix;  //中缀表达式
    private String suffix;  //后缀表达式
    private String res;  //公式计算结果字段
    private String bol;  //特殊处理字段的名称
    private Integer od;  //排序
    private String xy;  //修约规则
    private String gf;  //规范
    private String remark;  //备注

}
