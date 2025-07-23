package com.trtm.iot.hc_tfysworkarea.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.hc_tfstationdetail.entity.HcTfstationdetail;
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
 * @Description: 土方工作区施工成果
 * @Author: jeecg-boot
 * @Date: 2023-10-10
 * @Version: V1.0
 */
@Data
public class HcTfysworkareaxq implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
    /**
     * 工作区id 唯一
     */
//	@Excel(name = "工作区id 唯一", width = 15)
    @ApiModelProperty(value = "工作区id 唯一")
    private String blockid;
    /**
     * 工作区名称
     */
//	@Excel(name = "工作区名称", width = 15)
    @ApiModelProperty(value = "工作区名称")
    private String blockname;
    /**
     * 所属层
     */
//	@Excel(name = "所属层", width = 15)
    @ApiModelProperty(value = "所属层")
    private String layername;
    /**
     * 开始桩号 面型工程没有桩号
     */
    @Excel(name = "施工桩号", width = 15)
    @ApiModelProperty(value = "开始桩号 面型工程没有桩号")
    private String startstation;
    /**
     * 结束桩号 面型工程没有桩号
     */
//	@Excel(name = "结束桩号", width = 15)
    @ApiModelProperty(value = "结束桩号 面型工程没有桩号")
    private String endstation;
    /**
     * 开始时间
     */
    @Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private Date starttime;
    /**
     * 结束时间
     */
    @Excel(name = "结束时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
    private Date endtime;
    /**
     * 工作时长（h）
     */
    @Excel(name = "工作时长(h)", width = 15)
    @ApiModelProperty(value = "工作时长（h）")
    private String worktime;
    /**
     * 施工长度（m） 面型工程没有长度
     */
    @Excel(name = "施工长度(m)", width = 15)
    @ApiModelProperty(value = "施工长度（m） 面型工程没有长度")
    private String workmile;
    /**
     * 施工面积（㎡）
     */
    @Excel(name = "施工面积(㎡)", width = 15)
    @ApiModelProperty(value = " 施工面积（㎡）")
    private String workarea;
    /**
     * 工作区边界坐标，格式：[[北,东]]
     */
//	@Excel(name = "工作区边界坐标，格式：[[北,东]]", width = 15)
    @ApiModelProperty(value = "工作区边界坐标，格式：[[北,东]]")
    private String border;
    /**
     * 开振占比（%）
     */
    @Excel(name = "开振占比(%)", width = 15)
    @ApiModelProperty(value = "开振占比（%）")
    private String vibrateratio;
    /**
     * 平均碾压遍数
     */
    @Excel(name = "平均碾压遍数", width = 15)
    @ApiModelProperty(value = "平均碾压遍数")
    private String timesavg;
    /**
     * 平均振动遍数
     */
    @Excel(name = "平均振动遍数", width = 15)
    @ApiModelProperty(value = " 平均振动遍数")
    private String timesvibrateavg;
    /**
     * 平均碾压速度（km/h）
     */
    @Excel(name = "平均碾压速度(km/h)", width = 15)
    @ApiModelProperty(value = "平均碾压速度（km/h）")
    private String speedavg;
    /**
     * 平均厚度（m）
     */
    @Excel(name = "平均厚度(cm)", width = 15)
    @ApiModelProperty(value = "平均厚度（m）")
    private String thickavg;
    /**
     * 告警条数
     */
//	@Excel(name = "告警条数", width = 15)
    @ApiModelProperty(value = "告警条数")
    private String alarmnum;
    /**
     * 施工质量，0正常，1异常，2异常已处理
     */
//	@Excel(name = "施工质量", width = 15)
    @ApiModelProperty(value = "施工质量，0正常，1异常，2异常已处理")
    private String qualitystat;
    /**
     * 施工状态，0施工中，1已完工
     */
//	@Excel(name = "施工状态，0施工中，1已完工", width = 15)
    @ApiModelProperty(value = "施工状态，0施工中，1已完工")
    private String workstat;
    /**
     * 是否报检，0未报检，1已报检
     */
//	@Excel(name = "是否报检，0未报检，1已报检", width = 15)
    @ApiModelProperty(value = "是否报检，0未报检，1已报检")
    private String inspectstat;
    /**
     * projectid
     */
//	@Excel(name = "projectid", width = 15)
    @ApiModelProperty(value = "projectid")
    private String projectid;
    /**
     * 标段
     */
    @Excel(name = "标段", width = 15)
    @ApiModelProperty(value = "sectionid")
    @Dict(dictTable = "hc_section", dicText = "sectionName", dicCode = "sectionId")
    private String sectionid;
    @Excel(name = "预警状态", width = 15)
    private String standard;
    private String reason;
    /**超标状态：0为未处理，10为施工方已处理，20为闭合，30监理驳回，40监理审核，50指挥部审核，60指挥部驳回*/
    @Excel(name = "超标状态：0为未处理，10为施工方已处理，20为闭合，30监理驳回，40监理审核，50指挥部审核，60指挥部驳回", width = 15)
    @ApiModelProperty(value = "超标状态：0为未处理，10为施工方已处理，20为闭合，30监理驳回，40监理审核，50指挥部审核，60指挥部驳回")
    private Integer overproofStatus;

    private Double i;
    private Double j;
    private Double n;

    private Double x;
    private Double y;
    private Double z;

    List<HcTfstationdetail> HcTfstationdetail;
}
