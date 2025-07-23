package com.trtm.sy.sylxqxgl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("sy_dps_yy_userastestitemtype")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sy_dps_yy_userastestitemtype", description = "sy_dps_yy_userastestitemtype")
public class SyDpsYyUserAsTestItemType implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id; //主键
    @TableField(value = "titCode")
    private String titCode; //试验类型
    @TableField(value = "role_id")
    private String roleId;  //角色id
    @TableField(value = "ti_nos")
    private String tiNos; //表号

}
