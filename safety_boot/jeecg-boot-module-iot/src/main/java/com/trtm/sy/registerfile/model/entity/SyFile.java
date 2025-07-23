package com.trtm.sy.registerfile.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName(value = "sy_file")
public class SyFile {

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "fjid", type = IdType.AUTO)
    private String fjid;

    @TableField(value = "fjmc")
    @ApiModelProperty(value = "附件名称")
    private String fjmc;

    @TableField(value = "fjlj")
    @ApiModelProperty(value = "附件路径")
    private String fjlj;

    @ApiModelProperty(value = "附件预览路径")
    private String fjyllj;

    @TableField(value = "fjlx")
    @ApiModelProperty(value = "附件类型")
    private String fjlx;//退回文件：99

    @TableField(value = "fjbs")
    @ApiModelProperty(value = "附件标识")
    private String fjbs;

    /**
     * 删除标记：Y-已删除，N-未删除
     */
    @TableField(value = "del_flag")
    @ApiModelProperty(value = "删除标记：Y-已删除，N-未删除")
    private String delFlag;

    /**
     * 附件所属业务ID
     */
    @TableField(value = "fjfjdid")
    @ApiModelProperty(value = "附件所属业务ID")
    private String fjfjdid;

    @TableField(exist = false)
    @ApiModelProperty(value = "文件目录ID")
    private String wjmlid;

    /**
     * 版本号
     */
    @TableField("version" )
    private Integer version;

}
