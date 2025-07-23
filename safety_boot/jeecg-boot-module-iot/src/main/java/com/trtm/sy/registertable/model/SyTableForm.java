package com.trtm.sy.registertable.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("sy_table")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sy_table对象", description = "sy_table")
public class SyTableForm implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer parentId;
    private String jlbmc;
    private String jlbbm;
    private Integer type;
    private String gnks;
    private String bz;
    @Version
    private Integer version;
    private String fjid;
    @TableLogic(value = "N", delval = "Y")
    private String delFlag;
    private String createTime;
    private String createUser;
    private String updateTime;
    private String updateUser;
    private String config;
    private String verify;
    private String configUser;
    private Integer hv;

}
