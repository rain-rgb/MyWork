package com.trtm.iot.gscsInformUser.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 安全班组会通知与用户关联表
 * @Author: jeecg-boot
 * @Date: 2022-02-14
 * @Version: V1.0
 */
@Data
@TableName("gscs_inform_user")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "gscs_inform_user对象", description = "安全班组会通知与用户关联表")
public class GscsInformUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private java.lang.String id;
    /**
     * 通知id
     */
    @Excel(name = "通知id", width = 15)
    @ApiModelProperty(value = "通知id")
    private java.lang.String informId;
    /**
     * 接收通知人
     */
    @Excel(name = "接收通知人", width = 15)
    @ApiModelProperty(value = "接收通知人")
    private java.lang.String informTo;
}
