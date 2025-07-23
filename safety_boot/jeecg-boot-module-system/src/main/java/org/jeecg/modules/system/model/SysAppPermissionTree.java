package org.jeecg.modules.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.modules.system.entity.SysAppPermission;
import org.jeecg.modules.system.entity.SysPermission;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: app菜单数据
 * @Author: jeecg-boot
 * @Date:   2022-02-16
 * @Version: V1.0
 */

public class SysAppPermissionTree implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键id*/
    private String id;

    private String key;
    private String title;

    /**父id*/
    private String parentId;
    /**菜单标题*/
    private String name;
    /**菜单权限编码*/
    private String menuPerms;
    /**菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)*/
    private Integer menuType;
    /**按钮权限编码*/
    private String btnPerms;
    /**权限策略1显示2禁用*/
    private String permsType;
    /**菜单排序*/
    private Double sortNo;
    /**菜单图标*/
    private String icon;
    /**是否叶子节点:    1:是   0:不是*/
    private Boolean isLeaf;
    /**是否隐藏路由: 0否,1是*/
    private Boolean hidden;
    /**描述*/
    private String description;
    /**创建人*/
    private String createBy;
    /**创建时间*/
    private Date createTime;
    /**更新人*/
    private String updateBy;
    /**更新时间*/
    private Date updateTime;
    /**删除状态 0正常 1已删除*/
    private Integer delFlag;
    /**是否添加数据权限1是0否*/
    private Integer ruleFlag;
    /**按钮权限状态(0无效1有效)*/
    private String status;
    private Boolean isLabel;
    private String imgurl;

    private List<SysAppPermissionTree> children;

    public SysAppPermissionTree() {
    }

    public SysAppPermissionTree(SysAppPermission permission) {
        this.key = permission.getId();
        this.id = permission.getId();
        this.menuPerms = permission.getMenuPerms();
        this.btnPerms = permission.getBtnPerms();
        this.permsType = permission.getPermsType();

        this.createBy = permission.getCreateBy();
        this.createTime = permission.getCreateTime();
        this.delFlag = permission.getDelFlag();
        this.description = permission.getDescription();
        this.icon = permission.getIcon();
        this.isLeaf = permission.getIsLeaf();
        this.menuType = permission.getMenuType();
        this.name = permission.getName();
        this.parentId = permission.getParentId();
        this.sortNo = permission.getSortNo();
        this.updateBy = permission.getUpdateBy();
        this.updateTime = permission.getUpdateTime();

        this.hidden = permission.getHidden();
        this.isLabel = permission.getIsLabel();
        this.imgurl = permission.getImgurl();

        this.title=permission.getName();
        if (!permission.getIsLeaf() ) {
            this.children = new ArrayList<SysAppPermissionTree>();
        }
        this.status = permission.getStatus();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenuPerms() {
        return menuPerms;
    }

    public void setMenuPerms(String menuPerms) {
        this.menuPerms = menuPerms;
    }

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public String getBtnPerms() {
        return btnPerms;
    }

    public void setBtnPerms(String btnPerms) {
        this.btnPerms = btnPerms;
    }

    public String getPermsType() {
        return permsType;
    }

    public void setPermsType(String permsType) {
        this.permsType = permsType;
    }

    public Double getSortNo() {
        return sortNo;
    }

    public void setSortNo(Double sortNo) {
        this.sortNo = sortNo;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getRuleFlag() {
        return ruleFlag;
    }

    public void setRuleFlag(Integer ruleFlag) {
        this.ruleFlag = ruleFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsLabel() {
        return isLabel;
    }

    public void setIsLabel(Boolean label) {
        this.isLabel = label;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public List<SysAppPermissionTree> getChildren() {
        return children;
    }

    public void setChildren(List<SysAppPermissionTree> children) {
        this.children = children;
    }
}
