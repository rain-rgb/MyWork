package com.trtm.iot.zhanglaxxb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 张拉信息表
 * @Author: jeecg-boot
 * @Date:   2021-09-01
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TrZhanglaXxbS implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;
	/**唯一码*/
	@Excel(name = "唯一码", width = 15)
    @ApiModelProperty(value = "唯一码")
    private String syjid;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private String gcmc;
	/**预制梁场*/
	@Excel(name = "预制梁场", width = 15)
    @ApiModelProperty(value = "预制梁场")
    private String yzlc;
	/**构件编号*/
	@Excel(name = "构件编号", width = 15)
    @ApiModelProperty(value = "构件编号")
    private String gjbh;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String shebeibianhao;
	/**构件名称*/
	@Excel(name = "构件名称", width = 15)
    @ApiModelProperty(value = "构件名称")
    private String gjmc;
	/**砼设计强度*/
	@Excel(name = "砼设计强度", width = 15)
    @ApiModelProperty(value = "砼设计强度")
    private String snsjqd;
	/**施工时间*/
	@Excel(name = "施工时间", width = 15)
    @ApiModelProperty(value = "施工时间")
    private String sgsj;
	/**砼试块强度*/
	@Excel(name = "砼试块强度", width = 15)
    @ApiModelProperty(value = "砼试块强度")
    private String snskqd;
	/**张拉仪1编号*/
	@Excel(name = "张拉仪1编号", width = 15)
    @ApiModelProperty(value = "张拉仪1编号")
    private String zly1;
	/**油表编号1*/
	@Excel(name = "油表编号1", width = 15)
    @ApiModelProperty(value = "油表编号1")
    private String ybbh1;
	/**标定日期1*/
	@Excel(name = "标定日期1", width = 15)
    @ApiModelProperty(value = "标定日期1")
    private String bdrq1;
	/**张拉仪2编号*/
	@Excel(name = "张拉仪2编号", width = 15)
    @ApiModelProperty(value = "张拉仪2编号")
    private String zly2;
	/**油表编号2*/
	@Excel(name = "油表编号2", width = 15)
    @ApiModelProperty(value = "油表编号2")
    private String ybbh2;
	/**标定日期2*/
	@Excel(name = "标定日期2", width = 15)
    @ApiModelProperty(value = "标定日期2")
    private String bdrq2;
	/**承包单位,施工单位*/
	@Excel(name = "承包单位,施工单位", width = 15)
    @ApiModelProperty(value = "承包单位,施工单位")
    private String cbunit;
	/**监理单位*/
	@Excel(name = "监理单位", width = 15)
    @ApiModelProperty(value = "监理单位")
    private String jlunit;
	/**土建合同号*/
	@Excel(name = "土建合同号", width = 15)
    @ApiModelProperty(value = "土建合同号")
    private String htbh;
	/**张拉参数u*/
	@Excel(name = "张拉参数u", width = 15)
    @ApiModelProperty(value = "张拉参数u")
    private String zlcsu;
	/**张拉参数k*/
	@Excel(name = "张拉参数k", width = 15)
    @ApiModelProperty(value = "张拉参数k")
    private String zlcsk;
	/**张拉参数ep*/
	@Excel(name = "张拉参数ep", width = 15)
    @ApiModelProperty(value = "张拉参数ep")
    private String zlcsep;
	/**张拉部位图片*/
	@Excel(name = "张拉部位图片", width = 15)
    @ApiModelProperty(value = "张拉部位图片")
    private String zlbwpic;
	/**跨梁*/
	@Excel(name = "跨梁", width = 15)
    @ApiModelProperty(value = "跨梁")
    private String kualiang;
	/**伸长量计算式*/
	@Excel(name = "伸长量计算式", width = 15)
    @ApiModelProperty(value = "伸长量计算式")
    private String scljss;
	/**封锚情况描述*/
	@Excel(name = "封锚情况描述", width = 15)
    @ApiModelProperty(value = "封锚情况描述")
    private String fmqkms;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String memo;
	/**完成状态(0为未完成,状态为1代表*/
	@Excel(name = "完成状态(0为未完成,状态为1代表", width = 15)
    @ApiModelProperty(value = "完成状态(0为未完成,状态为1代表")
    private String status;
	/**张拉任务单号*/
	@Excel(name = "张拉任务单号", width = 15)
    @ApiModelProperty(value = "张拉任务单号")
    private String uuid;
    /**质检推送*/
    @Excel(name = "质检推送", width = 15)
    @ApiModelProperty(value = "质检推送")
    private Integer issend;
    /**统计*/
    @Excel(name = "张拉统计", width = 15)
    @ApiModelProperty(value = "张拉统计")
    private Integer statistics;
    private Integer isruicang;//瑞苍内网数据是否推送 0 未推送 1 已推送
    private List<TrZhanglaS> zhanglaSList;
}
