package com.trtm.iot.devicepipepilehistorydataone.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: device_pipepile_historydata_one
 * @Author: jeecg-boot
 * @Date: 2022-07-21
 * @Version: V1.0
 */
@Data
@TableName("device_pipepile_historydata_one")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "device_pipepile_historydata_one对象", description = "device_pipepile_historydata_one")
public class ExportDataVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Integer id;
    /**
     * 项目名称
     */
    @Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private String departName;
    /**
     * 数据导出时间
     */
    @Excel(name = "数据导出时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据导出时间")
    private Date dctime;
    /**
     * 设备编号
     */
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String shebeino;
    /**
     * 设备名称
     */
    @Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private String shebeiname;
    /**
     * 桩号
     */
    @Excel(name = "桩号", width = 15)
    @ApiModelProperty(value = "桩号")
    private String pileNo;
    /**
     * 施工长度(m)
     */
    @Excel(name = "施工长度(m)", width = 15)
    @ApiModelProperty(value = "施工长度(m)")
    private String pileRealdep;
    /**
     * 预警误差
     */
    @Excel(name = "预警误差", width = 15)
    @ApiModelProperty(value = "预警误差")
    private String yjwc;
    /**
     * 成桩时间
     */
    @Excel(name = "成桩时间", width = 15)
    @ApiModelProperty(value = "成桩时间")
    private String pileWorktime;
    /**
     * 最大垂直度(%)
     */
    @Excel(name = "最大垂直度(%)", width = 15)
    @ApiModelProperty(value = "最大垂直度(%)")
    private String pileY;
    /**
     * 结束时间
     */
    @Excel(name = "结束时间", width = 15)
    @ApiModelProperty(value = "结束时间")
    private String pileTime;
    /**
     * 开始时间
     */
    @Excel(name = "开始时间", width = 15)
    @ApiModelProperty(value = "开始时间")
    private String pileStarttime;
    /**
     * 最大压桩力(KN)
     */
    @Excel(name = "最大压桩力(KN)", width = 15)
    @ApiModelProperty(value = "最大压桩力(KN)")
    private String pileUpress;
    /**
     * 最大夹持力(KN)
     */
    @Excel(name = "最大夹持力(KN)", width = 15)
    @ApiModelProperty(value = "最大夹持力(KN)")
    private String pileDpress;
    /**
     * 平均速度(cm/min)
     */
    @Excel(name = "平均速度(cm/min)", width = 15)
    @ApiModelProperty(value = "平均速度(cm/min)")
    private String pileSpeed;
    /**
     * 接桩次数
     */
    @Excel(name = "接桩次数", width = 15)
    @ApiModelProperty(value = "接桩次数")
    private Integer times;
    /**
     * 设计桩长
     */
    @Excel(name = "设计桩长", width = 15)
    @ApiModelProperty(value = "设计桩长")
    private Integer pileDesigndep;
    /**
     * 里程
     */
    @Excel(name = "里程", width = 15)
    @ApiModelProperty(value = "里程")
    private String pileMileage;

    /**
     * 桩经度
     */
    @Excel(name = "桩经度", width = 15)
    @ApiModelProperty(value = "桩经度")
    private String pileLgd;
    /**
     * 桩纬度
     */
    @Excel(name = "桩纬度", width = 15)
    @ApiModelProperty(value = "桩纬度")
    private String pileLtd;
    /**
     * 超标等级
     */
    @Excel(name = "超标等级", width = 15)
    @ApiModelProperty(value = "超标等级")
    private String chaobiaodengji;//超标等级0：合格；1不合格
    /**
     * 吨位
     */
    @Excel(name = "吨位", width = 15)
    @ApiModelProperty(value = "吨位")
    private String tonnage;
}
