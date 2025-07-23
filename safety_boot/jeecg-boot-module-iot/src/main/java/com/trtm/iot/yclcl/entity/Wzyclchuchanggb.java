package com.trtm.iot.yclcl.entity;

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
 * @Description: wzyclchuchanggb
 * @Author: jeecg-boot
 * @Date:   2021-05-26
 * @Version: V1.0
 */
@Data
@TableName("wzyclchuchanggb")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wzyclchuchanggb对象", description="wzyclchuchanggb")
public class Wzyclchuchanggb implements Serializable {
    private static final long serialVersionUID = 1L;

	/**进场过磅id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "进场过磅id")
    private java.lang.Integer id;
	/**进出料单号*/
	@Excel(name = "进出料单号", width = 15)
    @ApiModelProperty(value = "进出料单号")
    private java.lang.String jinchuliaodanno;
	/**材料单号*/
	@Excel(name = "材料单号", width = 15)
    @ApiModelProperty(value = "材料单号")
    @Dict(dictTable = "wzcailiaonamedict", dicText = "cailiaoname", dicCode = "cailiaono")
    private java.lang.String cailiaono;
	/**进场时间*/
	@Excel(name = "进场时间", width = 15)
    @ApiModelProperty(value = "进场时间")
    private java.lang.String jinchangshijian;
	/**出场时间*/
	@Excel(name = "出场时间", width = 15)
    @ApiModelProperty(value = "出场时间")
    private java.lang.String chuchangshijian;
	/**批次*/
	@Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private java.lang.String pici;
	/**车辆编号*/
	@Excel(name = "车辆编号", width = 15)
    @ApiModelProperty(value = "车辆编号")
    private java.lang.String cheliangbianhao;
	/**前车牌*/
	@Excel(name = "前车牌", width = 15)
    @ApiModelProperty(value = "前车牌")
    private java.lang.String qianchepai;
	/**后车牌*/
	@Excel(name = "后车牌", width = 15)
    @ApiModelProperty(value = "后车牌")
    private java.lang.String houchepai;
	/**毛重*/
	@Excel(name = "毛重", width = 15)
    @ApiModelProperty(value = "毛重")
    private java.lang.String maozhong;
	/**皮重*/
	@Excel(name = "皮重", width = 15)
    @ApiModelProperty(value = "皮重")
    private java.lang.String pizhong;
	/**净重*/
	@Excel(name = "净重", width = 15)
    @ApiModelProperty(value = "净重")
    private java.lang.String jingzhong;
	/**扣重*/
	@Excel(name = "扣重", width = 15)
    @ApiModelProperty(value = "扣重")
    private java.lang.String kouzhong;
	/**扣率*/
	@Excel(name = "扣率", width = 15)
    @ApiModelProperty(value = "扣率")
    private java.lang.String koulv;
	/**称重偏差*/
	@Excel(name = "称重偏差", width = 15)
    @ApiModelProperty(value = "称重偏差")
    private java.lang.String chengzhongpiancha;
	/**料仓*/
	@Excel(name = "料仓", width = 15)
    @ApiModelProperty(value = "料仓")
    private java.lang.String liaocang;
	/**司磅员*/
	@Excel(name = "司磅员", width = 15)
    @ApiModelProperty(value = "司磅员")
    private java.lang.String sibangyuan;
	/**出场原因*/
	@Excel(name = "出场原因", width = 15)
    @ApiModelProperty(value = "出场原因")
    private java.lang.String remark;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeibianhao;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    @Dict(dictTable = "wzgongyingshang", dicText = "gongyingshangname", dicCode = "guid")
    private java.lang.String gongyingshangdanweibianma;
	/**过磅类别*/
	@Excel(name = "过磅类别", width = 15)
    @ApiModelProperty(value = "过磅类别")
    private java.lang.String guobangleibie;
	/**车辆类型*/
	@Excel(name = "车辆类型", width = 15)
    @ApiModelProperty(value = "车辆类型")
    private java.lang.String cheliangleixing;
	/**STATUS*/
	@Excel(name = "STATUS", width = 15)
    @ApiModelProperty(value = "STATUS")
    private java.lang.String status;
	/**ISDEL*/
	@Excel(name = "ISDEL", width = 15)
    @ApiModelProperty(value = "ISDEL")
    private java.lang.String isdel;
	/**TS*/
	@Excel(name = "TS", width = 15)
    @ApiModelProperty(value = "TS")
    private java.lang.Integer ts;
	/**GUID*/
	@Excel(name = "GUID", width = 15)
    @ApiModelProperty(value = "GUID")
    private java.lang.String guid;
	/**毛重(吨)*/
	@Excel(name = "毛重(吨)", width = 15)
    @ApiModelProperty(value = "毛重(吨)")
    private java.lang.String maozhongt;
	/**皮重(吨)*/
	@Excel(name = "皮重(吨)", width = 15)
    @ApiModelProperty(value = "皮重(吨)")
    private java.lang.String pizhongt;
	/**candi*/
	@Excel(name = "candi", width = 15)
    @ApiModelProperty(value = "candi")
    private java.lang.String candi;
	/**运输单位*/
	@Excel(name = "运输单位", width = 15)
    @ApiModelProperty(value = "运输单位")
    private java.lang.String yunshudanwei;
	/**料仓*/
	@Excel(name = "料仓", width = 15)
    @ApiModelProperty(value = "料仓")
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private java.lang.String lcbianhao;
	/**ISSHOULIAO*/
	@Excel(name = "ISSHOULIAO", width = 15)
    @ApiModelProperty(value = "ISSHOULIAO")
    private java.lang.String isshouliao;
	/**净重(吨)*/
	@Excel(name = "净重(吨)", width = 15)
    @ApiModelProperty(value = "净重(吨)")
    private java.lang.String jingzhongt;
	/**ISTONGJI*/
	@Excel(name = "ISTONGJI", width = 15)
    @ApiModelProperty(value = "ISTONGJI")
    private java.lang.Integer istongji;
	/**进场高空*/
	@Excel(name = "进场高空", width = 15)
    @ApiModelProperty(value = "进场高空")
    private java.lang.String jcgkpic;
	/**进场车牌1*/
	@Excel(name = "进场车牌1", width = 15)
    @ApiModelProperty(value = "进场车牌1")
    private java.lang.String jccppic;
	/**进场车牌2*/
	@Excel(name = "进场车牌2", width = 15)
    @ApiModelProperty(value = "进场车牌2")
    private java.lang.String jchcppic;
	/**进场磅房*/
	@Excel(name = "进场磅房", width = 15)
    @ApiModelProperty(value = "进场磅房")
    private java.lang.String jcbfpic;
	/**出场高空*/
	@Excel(name = "出场高空", width = 15)
    @ApiModelProperty(value = "出场高空")
    private java.lang.String ccgkpic;
	/**出场车牌1*/
	@Excel(name = "出场车牌1", width = 15)
    @ApiModelProperty(value = "出场车牌1")
    private java.lang.String cccppic;
	/**出场车牌2*/
	@Excel(name = "出场车牌2", width = 15)
    @ApiModelProperty(value = "出场车牌2")
    private java.lang.String cchcppic;
	/**出场磅房*/
	@Excel(name = "出场磅房", width = 15)
    @ApiModelProperty(value = "出场磅房")
    private java.lang.String ccbfpic;
	/**料仓id*/
	@Excel(name = "料仓id", width = 15)
    @ApiModelProperty(value = "料仓id")
    private java.lang.String liaocangid;
	/**SERIALNO*/
	@Excel(name = "SERIALNO", width = 15)
    @ApiModelProperty(value = "SERIALNO")
    private java.lang.String serialno;
	/**原因*/
	@Excel(name = "原因", width = 15)
    @ApiModelProperty(value = "原因")
    private java.lang.String reason;
	/**文件上传*/
	@Excel(name = "文件上传", width = 15)
    @ApiModelProperty(value = "文件上传")
    private java.lang.String fileUpload;
	/**beizhu*/
	@Excel(name = "beizhu", width = 15)
    @ApiModelProperty(value = "beizhu")
    private java.lang.String beizhu;
	/**项目id*/
	@Excel(name = "项目id", width = 15)
    @ApiModelProperty(value = "项目id")
    private java.lang.String projectid;
	/**施工地点*/
	@Excel(name = "施工地点", width = 15)
    @ApiModelProperty(value = "施工地点")
    private java.lang.String constructionsite;
    private java.lang.Integer statuscode;
}
