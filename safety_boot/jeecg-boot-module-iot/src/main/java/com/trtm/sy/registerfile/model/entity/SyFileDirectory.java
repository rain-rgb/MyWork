package com.trtm.sy.registerfile.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.trtm.sy.registermodules.system.vo.BaseEntity;
import lombok.Data;


import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author Liupei
 * @Date 2023-08-09
 */

@Data
@TableName("sy_file_directory")
public class SyFileDirectory extends BaseEntity {

    /**
     * 文件目录ID
     */
    @TableId(value = "wjmlid", type = IdType.AUTO)
    private String wjmlid;

    /**
     * 目录名称
     */
    @TableField("mlmc")
    private String mlmc;

    /**
     * 目录编码
     */
    @TableField("mlcode")
    private String mlcode;

    /**
     * 父目录ID
     */
    @TableField("fwjmlid")
    private String fwjmlid;

    /**
     * 目录链条
     */
    @TableField("ml_chain")
    private String mlChain;

    /**
     * 目录类型
     */
    @TableField("ml_class")
    private String mlClass;

    /**
     * 删除标记：Y-已删除，N-未删除
     */
    @TableField("del_flag")
    private String delFlag;

    /**
     * 修改标记：1-允许修改，0-不可修改(包括删除)
     */
    @TableField("edit_flag")
    private String editFlag;

    /**
     * 版本号
     */
    @TableField("version")
    private Integer version;

    @TableField(exist = false)
    private List<SyFileDirectory> children = new ArrayList<>();

}
