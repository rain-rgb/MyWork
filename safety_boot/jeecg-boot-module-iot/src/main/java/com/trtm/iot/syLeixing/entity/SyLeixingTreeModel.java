package com.trtm.iot.syLeixing.entity;


import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Description: 试验类型
 * @Author: jeecg-boot
 * @Date:   2022-03-09
 * @Version: V1.0
 */
@Data
@TableName("sy_leixing")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_leixing对象", description="试验类型")
public class SyLeixingTreeModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 对应SysDepart中的id字段,前端数据树中的key*/
    private String key;

    /** 对应SysDepart中的id字段,前端数据树中的value*/
    private String value;

    /** 对应depart_name字段,前端数据树中的title*/
    private String title;


    private boolean isLeaf;
    /**id*/
    private java.lang.Integer id;
    /**名称*/
    private java.lang.String cailiaoname;
    /**编号*/
    private java.lang.String cailiaono;
    /**上级编号*/
    private java.lang.String parentno;
    /**节点类型*/
    private Integer nodetype;
    /**是否删除*/
    private java.lang.String isdel;
    /**创建人*/
    private java.lang.String creatpersom;
    /**创建时间*/
    private java.util.Date creattime;
    /**修改人*/
    private java.lang.String updateperson;
    /**修改时间*/
    private java.util.Date updatetime;

    private List<SyLeixingTreeModel> children;

}
