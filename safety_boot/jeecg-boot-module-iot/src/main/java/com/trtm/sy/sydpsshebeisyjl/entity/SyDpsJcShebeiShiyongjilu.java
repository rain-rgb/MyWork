package com.trtm.sy.sydpsshebeisyjl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: dps_jc_shebei_shiyongjilu
 * @Author: jeecg-boot
 * @Date: 2023-02-27
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_jc_shebei_shiyongjilu")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sy_dps_jc_shebei_shiyongjilu对象", description = "sy_dps_jc_shebei_shiyongjilu")
public class SyDpsJcShebeiShiyongjilu implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * shiyanriqi
     */
    @Excel(name = "shiyanriqi", width = 15)
    @ApiModelProperty(value = "shiyanriqi")
    private java.lang.String shiyanriqi;
    /**
     * shebeiid
     */
    @Excel(name = "shebeiid", width = 15)
    @ApiModelProperty(value = "shebeiid")
    private java.lang.String shebeiid;
    /**
     * shiyongren
     */
    @Excel(name = "shiyongren", width = 15)
    @ApiModelProperty(value = "shiyongren")
    private java.lang.String shiyongren;
    /**
     * sampleno
     */
    @Excel(name = "sampleno", width = 15)
    @ApiModelProperty(value = "sampleno")
    private java.lang.String sampleno;
    /**
     * tino
     */
    @Excel(name = "tino", width = 15)
    @ApiModelProperty(value = "tino")
    private java.lang.String tino;
}
