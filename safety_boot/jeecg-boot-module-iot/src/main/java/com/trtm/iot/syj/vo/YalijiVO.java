package com.trtm.iot.syj.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

/**
 * @Description: t_syjzb
 * @Author: jeecg-boot
 * @Date: 2021-03-12
 * @Version: V1.0
 */
@Data
@ApiModel(value = "YalijiVO", description = "YalijiVO")
public class YalijiVO {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
    /**
     * 唯一码
     */
    @Excel(name = "唯一码", width = 15)
    @ApiModelProperty(value = "唯一码")
    private String syjid;
    /**
     * 设备编号
     */
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String sbbh;
    /**
     * 设备名称
     */
    @Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private String sbname;
    /**
     * 试验类型
     */
    @Excel(name = "试验类型", width = 15)
    @ApiModelProperty(value = "试验类型")
    private String sylx;
    /**
     * 试验类型名称
     */
    @Excel(name = "试验类型名称", width = 15)
    @ApiModelProperty(value = "试验类型名称")
    private String sylxName;
    /**
     * 试件编号
     */
    @Excel(name = "试件编号", width = 15)
    @ApiModelProperty(value = "试件编号")
    private String sjbh;
    /**
     * 试件尺寸
     */
    @Excel(name = "试件尺寸", width = 15)
    @ApiModelProperty(value = "试件尺寸")
    private String sjcc;
    /**
     * 试验日期
     */
    @Excel(name = "试验日期", width = 15)
    @ApiModelProperty(value = "试验日期")
    private String syrq;
    /**
     * 试验完成时间
     */
    @Excel(name = "试验完成时间", width = 15)
    @ApiModelProperty(value = "试验完成时间")
    private String sywcsj;
    /**
     * 当前时间
     */
    @Excel(name = "当前时间", width = 15)
    @ApiModelProperty(value = "当前时间")
    private Date dqsj;
    /**
     * 判定结果
     */
    @Excel(name = "判定结果", width = 15)
    @ApiModelProperty(value = "判定结果")
    private String pdjg;
    /**
     * 龄期
     */
    @Excel(name = "龄期", width = 15)
    @ApiModelProperty(value = "龄期")
    private Integer lq;
    /**
     * 设计强度
     */
    @Excel(name = "设计强度", width = 15)
    @ApiModelProperty(value = "设计强度")
    private String sjqd;
    /**
     * 操作人员
     */
    @Excel(name = "操作人员", width = 15)
    @ApiModelProperty(value = "操作人员")
    private String czry;
    /**
     * 工程名称
     */
    @Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private String cjmc;
    /**
     * 试件序号
     */
    @Excel(name = "试件序号", width = 15)
    @ApiModelProperty(value = "试件序号")
    private String sjxh;
    /**
     * 抗压力值
     */
    @Excel(name = "抗压力值", width = 15)
    @ApiModelProperty(value = "抗压力值")
    private String kylz;
    /**
     * 抗压强度
     */
    @Excel(name = "抗压强度", width = 15)
    @ApiModelProperty(value = "抗压强度")
    private String kyqd;

}
