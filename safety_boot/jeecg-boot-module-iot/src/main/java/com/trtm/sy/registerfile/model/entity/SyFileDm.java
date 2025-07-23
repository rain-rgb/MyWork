package com.trtm.sy.registerfile.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SyFileDm {
    /**
     * 文件目录映射ID
     */
    @ApiModelProperty(value = "主键ID")
    @TableId(value = "fdmid", type = IdType.AUTO)
    private String fdmid;

    /**
     * 文件目录ID
     */
    @TableField("wjmlid")
    private String wjmlid;

    /**
     * 附件ID
     */
    @TableField("fjid")
    private String fjid;

    /**
     * 删除标记：Y-已删除，N-未删除
     */
    @TableField("del_flag")
    private String delFlag;

    /**
     * 版本号
     */
    @TableField("version")
    private Integer version;

    // 规程文件相关
    /**
     * 规程名称
     */
    @ApiModelProperty(value = "规程名称")
    @TableField("gcmc")
    private String gcmc;

    /**
     * 颁布号
     */
    @ApiModelProperty(value = "颁布号")
    @TableField("bbh")
    private String bbh;

    /**
     * 规程名称
     */
    @ApiModelProperty(value = "执行日期")
    @TableField("zxrq")
    private String zxrq;

    /**
     * 规程名称
     */
    @ApiModelProperty(value = "过期日期")
    @TableField("gqrq")
    private String gqrq;

    /**
     * 附件类型
     */
    @ApiModelProperty(value = "附件类型")
    @TableField("fj_type")
    private String fjType;

    @TableField(exist = false)
    private List<SyFile> syFileList;
}
