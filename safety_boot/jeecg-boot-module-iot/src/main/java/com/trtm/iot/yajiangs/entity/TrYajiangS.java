package com.trtm.iot.yajiangs.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 压浆实时数据表
 * @Author: jeecg-boot
 * @Date:   2021-09-06
 * @Version: V1.0
 */
@Data
@TableName("tr_yajiang_s")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tr_yajiang_s对象", description="压浆实时数据表")
public class TrYajiangS implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;
	/**全球唯一码，主键*/
	@Excel(name = "全球唯一码，主键", width = 15)
    @ApiModelProperty(value = "全球唯一码，主键")
    private String fguid;
	/**外键，压浆主表主键*/
	@Excel(name = "外键，压浆主表主键", width = 15)
    @ApiModelProperty(value = "外键，压浆主表主键")
    private String syjid;
	/**压浆时间*/
	@Excel(name = "压浆时间", width = 15)
    @ApiModelProperty(value = "压浆时间")
    private String yajiangsj;
	/**完成状态(0为未完成,状态为1代表*/
	@Excel(name = "完成状态(0为未完成,状态为1代表", width = 15)
    @ApiModelProperty(value = "完成状态(0为未完成,状态为1代表")
    private String status;
	/**孔道*/
	@Excel(name = "孔道", width = 15)
    @ApiModelProperty(value = "孔道")
    private String kongdao;
	/**holeid*/
	@Excel(name = "holeid", width = 15)
    @ApiModelProperty(value = "holeid")
    private String holeid;
	/**压浆模式*/
	@Excel(name = "压浆模式", width = 15)
    @ApiModelProperty(value = "压浆模式")
    private String yajiangmosh;
	/**配合比*/
	@Excel(name = "配合比", width = 15)
    @ApiModelProperty(value = "配合比")
    private String peihebi;
	/**水胶比*/
	@Excel(name = "水胶比", width = 15)
    @ApiModelProperty(value = "水胶比")
    private String shuijiaobi;
	/**搅拌时间*/
	@Excel(name = "搅拌时间", width = 15)
    @ApiModelProperty(value = "搅拌时间")
    private String jiaobansj;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15)
    @ApiModelProperty(value = "开始时间")
    private String starttime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 15)
    @ApiModelProperty(value = "结束时间")
    private String endtime;
	/**进浆压力MPa*/
	@Excel(name = "进浆压力MPa", width = 15)
    @ApiModelProperty(value = "进浆压力MPa")
    private String jinjiangyal;
	/**返浆压力MPa*/
	@Excel(name = "返浆压力MPa", width = 15)
    @ApiModelProperty(value = "返浆压力MPa")
    private String fanjiangyal;
	/**持续时间*/
	@Excel(name = "持续时间", width = 15)
    @ApiModelProperty(value = "持续时间")
    private String chixushijia;
	/**进浆量L*/
	@Excel(name = "进浆量L", width = 15)
    @ApiModelProperty(value = "进浆量L")
    private String jinjiangshu;
	/**返浆量L*/
	@Excel(name = "返浆量L", width = 15)
    @ApiModelProperty(value = "返浆量L")
    private String fanjianglia;
	/**真空泵压力MPa*/
	@Excel(name = "真空泵压力MPa", width = 15)
    @ApiModelProperty(value = "真空泵压力MPa")
    private String zkyl;
	/**是否合格*/
	@Excel(name = "是否合格", width = 15)
    @ApiModelProperty(value = "是否合格")
    private String hege;
	/**通过*/
	@Excel(name = "通过", width = 15)
    @ApiModelProperty(value = "通过")
    private String tongguo;
	/**压浆次数*/
	@Excel(name = "压浆次数", width = 15)
    @ApiModelProperty(value = "压浆次数")
    private String yjcs;
	/**冒浆情况*/
	@Excel(name = "冒浆情况", width = 15)
    @ApiModelProperty(value = "冒浆情况")
    private String mjqk;
    private Integer overproofStatus;
    private Integer isOverLevel;
    private String overReason;// 预警原因
    private Integer iszl;// 预警原因
}
