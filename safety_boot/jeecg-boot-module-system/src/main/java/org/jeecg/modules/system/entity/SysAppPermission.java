package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: app菜单数据
 * @Author: jeecg-boot
 * @Date:   2022-02-16
 * @Version: V1.0
 */
@Data
@TableName("sys_app_permission")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sys_app_permission对象", description="app菜单数据")
public class SysAppPermission implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private String id;
	/**父id*/
	@Excel(name = "父id", width = 15)
    @ApiModelProperty(value = "父id")
    private String parentId;
	/**菜单标题*/
	@Excel(name = "菜单标题", width = 15)
    @ApiModelProperty(value = "菜单标题")
    private String name;
	/**菜单权限编码*/
	@Excel(name = "菜单权限编码", width = 15)
    @ApiModelProperty(value = "菜单权限编码")
    private String menuPerms;
	/**菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)*/
	@Excel(name = "菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)", width = 15)
    @ApiModelProperty(value = "菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)")
    private Integer menuType;
	/**按钮权限编码*/
	@Excel(name = "按钮权限编码", width = 15)
    @ApiModelProperty(value = "按钮权限编码")
    private String btnPerms;
	/**权限策略1显示2禁用*/
	@Excel(name = "权限策略1显示2禁用", width = 15)
    @ApiModelProperty(value = "权限策略1显示2禁用")
    private String permsType;
	/**菜单排序*/
	@Excel(name = "菜单排序", width = 15)
    @ApiModelProperty(value = "菜单排序")
    private Double sortNo;
	/**菜单图标*/
	@Excel(name = "菜单图标", width = 15)
    @ApiModelProperty(value = "菜单图标")
    private String icon;
	/**是否叶子节点:    1:是   0:不是*/
	@Excel(name = "是否叶子节点:    1:是   0:不是", width = 15)
    @ApiModelProperty(value = "是否叶子节点:    1:是   0:不是")
    private Boolean isLeaf;
	/**是否隐藏路由: 0否,1是*/
	@Excel(name = "是否隐藏路由: 0否,1是", width = 15)
    @ApiModelProperty(value = "是否隐藏路由: 0否,1是")
    private Boolean hidden;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private String description;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
	/**删除状态 0正常 1已删除*/
	@Excel(name = "删除状态 0正常 1已删除", width = 15)
    @ApiModelProperty(value = "删除状态 0正常 1已删除")
    private Integer delFlag;
	/**是否添加数据权限1是0否*/
	@Excel(name = "是否添加数据权限1是0否", width = 15)
    @ApiModelProperty(value = "是否添加数据权限1是0否")
    private Integer ruleFlag;
	/**按钮权限状态(0无效1有效)*/
	@Excel(name = "按钮权限状态(0无效1有效)", width = 15)
    @ApiModelProperty(value = "按钮权限状态(0无效1有效)")
    private String status;

    private Boolean isLabel;// 是否为app标签页(0:不是；1是)
    private String imgurl;
}
