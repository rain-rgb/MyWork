package com.trtm.iot.wbshebeidetail.entity;

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
 * @Description: 电子锁详情数据表
 * @Author: jeecg-boot
 * @Date:   2022-02-22
 * @Version: V1.0
 */
@Data
@TableName("wbshebei_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wbshebei_detail对象", description="电子锁详情数据表")
public class WbshebeiDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String sbbh;
	/**到达时间*/
	@Excel(name = "到达时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "到达时间")
    private java.util.Date ddtime;
	/**出发时间*/
	@Excel(name = "出发时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出发时间")
    private java.util.Date cftime;
	/**单号*/
	@Excel(name = "单号", width = 15)
    @ApiModelProperty(value = "单号")
    private java.lang.String danhao;
	/**车牌号*/
	@Excel(name = "车牌号", width = 15)
    @ApiModelProperty(value = "车牌号")
    private java.lang.String cph;
	/**是否到达0为未到达，1为到达*/
	@Excel(name = "是否到达0为未到达，1为到达", width = 15)
    @ApiModelProperty(value = "是否到达0为未到达，1为到达")
    private java.lang.Integer zhuangtai;
	/**是否合格*/
	@Excel(name = "是否合格", width = 15)
    @ApiModelProperty(value = "是否合格")
    private java.lang.String sfhg;
	/**0-安徽环宇公路沥青材料有限公司;1-上海海太工程科技有限公司;2-湖南路安达沥青技术有限公司*/
	@Excel(name = "0-安徽环宇公路沥青材料有限公司;1-上海海太工程科技有限公司;2-湖南路安达沥青技术有限公司", width = 15)
    @ApiModelProperty(value = "0-安徽环宇公路沥青材料有限公司;1-上海海太工程科技有限公司;2-湖南路安达沥青技术有限公司")
    private java.lang.String ghdw;
	/**材料*/
	@Excel(name = "材料", width = 15)
    @ApiModelProperty(value = "材料")
    private java.lang.String cailiao;
	/**规格*/
	@Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
    private java.lang.String guige;
	/**发车纬度*/
	@Excel(name = "发车纬度", width = 15)
    @ApiModelProperty(value = "发车纬度")
    private java.lang.String fclat;
	/**发车经度*/
	@Excel(name = "发车经度", width = 15)
    @ApiModelProperty(value = "发车经度")
    private java.lang.String fclng;
	/**到达纬度*/
	@Excel(name = "到达纬度", width = 15)
    @ApiModelProperty(value = "到达纬度")
    private java.lang.String ddlat;
	/**到达经度*/
	@Excel(name = "到达经度", width = 15)
    @ApiModelProperty(value = "到达经度")
    private java.lang.String ddlng;
	/**负责人*/
	@Excel(name = "负责人", width = 15)
    @ApiModelProperty(value = "负责人")
    private java.lang.String fzr;
	/**0为报警 1为不报警*/
	@Excel(name = "0为报警 1为不报警", width = 15)
    @ApiModelProperty(value = "0为报警 1为不报警")
    private java.lang.String sfbj;
	/**0为未发送1为已发送 发送到达信息*/
	@Excel(name = "0为未发送1为已发送 发送到达信息", width = 15)
    @ApiModelProperty(value = "0为未发送1为已发送 发送到达信息")
    private java.lang.String fsxx;
	/**发车数量*/
	@Excel(name = "发车数量", width = 15)
    @ApiModelProperty(value = "发车数量")
    private java.lang.String jcsl;
	/**进场过磅数量*/
	@Excel(name = "进场过磅数量", width = 15)
    @ApiModelProperty(value = "进场过磅数量")
    private java.lang.String jcgbl;
	/**出场过磅数量*/
	@Excel(name = "出场过磅数量", width = 15)
    @ApiModelProperty(value = "出场过磅数量")
    private java.lang.String ccgbl;
	/**当前库存*/
	@Excel(name = "当前库存", width = 15)
    @ApiModelProperty(value = "当前库存")
    private java.lang.String dqkc;
	/**实际消耗数量*/
	@Excel(name = "实际消耗数量", width = 15)
    @ApiModelProperty(value = "实际消耗数量")
    private java.lang.String sjxhsl;
	/**理论消耗数量由理论配合比和工程任务单计算得出*/
	@Excel(name = "理论消耗数量由理论配合比和工程任务单计算得出", width = 15)
    @ApiModelProperty(value = "理论消耗数量由理论配合比和工程任务单计算得出")
    private java.lang.String llxhsl;
	/**理论库存 = 实际进场 - 理论消耗*/
	@Excel(name = "理论库存 = 实际进场 - 理论消耗", width = 15)
    @ApiModelProperty(value = "理论库存 = 实际进场 - 理论消耗")
    private java.lang.String llkc;
	/**理论消耗*/
	@Excel(name = "理论消耗", width = 15)
    @ApiModelProperty(value = "理论消耗")
    private java.lang.String llxh;
	/**理论配合比*/
	@Excel(name = "理论配合比", width = 15)
    @ApiModelProperty(value = "理论配合比")
    private java.lang.String llphb;
	/**0为已拿到地磅数据1为未拿到*/
	@Excel(name = "1为已拿到地磅数据0为未拿到", width = 15)
    @ApiModelProperty(value = "1为已拿到地磅数据0为未拿到")
    private java.lang.String sfcssj;
	/**目的地*/
	@Excel(name = "目的地", width = 15)
    @ApiModelProperty(value = "目的地")
    private java.lang.String mdd;
	/**照片地址*/
	@Excel(name = "照片地址(上锁图片)", width = 15)
    @ApiModelProperty(value = "照片地址(上锁图片)")
    private java.lang.String imgfile;
	/**铅封号*/
	@Excel(name = "铅封号", width = 15)
    @ApiModelProperty(value = "铅封号")
    private java.lang.String qianfenghao;
	/**所属标段*/
	@Excel(name = "所属标段", width = 15)
    @ApiModelProperty(value = "所属标段")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String userdepartid;
	/**0 未到达  1已到达 2已解锁*/
	@Excel(name = "0 未到达  1已到达 2已解锁", width = 15)
    @ApiModelProperty(value = "0 未到达  1已到达 2已解锁")
    private java.lang.Integer isjiesuo;
	/**解锁时间*/
	@Excel(name = "解锁时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "解锁时间")
    private java.util.Date timestatime;
	/**解锁人*/
	@Excel(name = "解锁人", width = 15)
    @ApiModelProperty(value = "解锁人")
    private java.lang.String name;
    private java.lang.String jyimgfile;//检验报告图片
    private java.lang.String reviewer;//审核人
    private java.util.Date reviewtime;//审核时间
    private java.lang.String zzmdd;//中转目的地
    private java.lang.Integer zzstatus;//中转状态


    private java.lang.String shrwdh;//送货任务单号
    private java.lang.String gjtp;//轨迹图
    private java.lang.Integer fctype;//发车单类型（1：无电子锁）
    private java.lang.String rejection;//拒收原因（快检不合格）
    /**厂商预计到货时间*/
    @Excel(name = "厂商预计到货时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "厂商预计到货时间")
    private java.util.Date estimatetime;

    private java.lang.String upsbbh;//上出料口设备编号，由审核人解锁检查材料
    private java.lang.String uppic;//审核时上传审核图片
    private java.lang.Integer isjiesuo2;


}
