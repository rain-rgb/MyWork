package com.trtm.iot.trgangjinbhcm.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.trgangjinbhcs.entity.TrGangjinbhcS;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * \* @author: Xx
 * \* Date: 2021/9/14
 * \* Time: 9:45
 * \* Description:
 * \
 */
@Data
public class TrGangjinbhcMPage {
    /**主键*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
    /**构件序号*/
    @Excel(name = "构件序号", width = 15)
    @ApiModelProperty(value = "构件序号")
    private java.lang.String point;
    /**1:厚度、2:波形、3:剖面、4:网格、5:规范*/
    @Excel(name = "1:厚度、2:波形、3:剖面、4:网格、5:规范", width = 15)
    @ApiModelProperty(value = "1:厚度、2:波形、3:剖面、4:网格、5:规范")
    private java.lang.Integer type;
    /**检测唯一ID*/
    @Excel(name = "检测唯一ID", width = 15)
    @ApiModelProperty(value = "检测唯一ID")
    private java.lang.String testid;
    /**现浇板, 现浇墙, 现浇梁, 现浇柱, 预制板, 预制墙板, 预制梁, 预制柱, 预制桁架, 其他*/
    @Excel(name = "现浇板, 现浇墙, 现浇梁, 现浇柱, 预制板, 预制墙板, 预制梁, 预制柱, 预制桁架, 其他", width = 15)
    @ApiModelProperty(value = "现浇板, 现浇墙, 现浇梁, 现浇柱, 预制板, 预制墙板, 预制梁, 预制柱, 预制桁架, 其他")
    private java.lang.String targettype;
    /**测点总数*/
    @Excel(name = "测点总数", width = 15)
    @ApiModelProperty(value = "测点总数")
    private java.lang.Integer zonecount;
    /**合格率*/
    @Excel(name = "合格率", width = 15)
    @ApiModelProperty(value = "合格率")
    private java.lang.Double passrate;
    /**检测位置*/
    @Excel(name = "检测位置", width = 15)
    @ApiModelProperty(value = "检测位置")
    private java.lang.String checklocation;
    /**检测人*/
    @Excel(name = "检测人", width = 15)
    @ApiModelProperty(value = "检测人")
    private java.lang.String testerid;
    /**检测时间*/
    @Excel(name = "检测时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "检测时间")
    private java.util.Date checktime;
    /**主筋直径mm*/
    @Excel(name = "主筋直径mm", width = 15)
    @ApiModelProperty(value = "主筋直径mm")
    private java.lang.Integer masterdiameter;
    /**主筋间距mm*/
    @Excel(name = "主筋间距mm", width = 15)
    @ApiModelProperty(value = "主筋间距mm")
    private java.lang.Integer masterspacing;
    /**设计厚度mm*/
    @Excel(name = "设计厚度mm", width = 15)
    @ApiModelProperty(value = "设计厚度mm")
    private java.lang.Integer designthickness;
    /**纵向设计厚度*/
    @Excel(name = "纵向设计厚度", width = 15)
    @ApiModelProperty(value = "纵向设计厚度")
    private java.lang.Integer zdesignthickness;
    /**箍筋直径mm*/
    @Excel(name = "箍筋直径mm", width = 15)
    @ApiModelProperty(value = "箍筋直径mm")
    private java.lang.Integer subdiameter;
    /**箍筋间距mm*/
    @Excel(name = "箍筋间距mm", width = 15)
    @ApiModelProperty(value = "箍筋间距mm")
    private java.lang.Integer subspacing;
    /**曲面直径*/
    @Excel(name = "曲面直径", width = 15)
    @ApiModelProperty(value = "曲面直径")
    private java.lang.Integer curveddiameter;
    /**任务单NO*/
    @Excel(name = "任务单NO", width = 15)
    @ApiModelProperty(value = "任务单NO")
    private java.lang.String code;
    /**设备编号*/
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeiNo;

    private java.lang.String projectname;

    private java.lang.String sgbw;

    private List<TrGangjinbhcS> trGangjinbhcS;
}
