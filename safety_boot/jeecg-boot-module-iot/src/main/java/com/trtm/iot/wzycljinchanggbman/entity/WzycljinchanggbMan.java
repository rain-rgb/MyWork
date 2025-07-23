package com.trtm.iot.wzycljinchanggbman.entity;

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
 * @Description: wzycljinchanggb_man
 * @Author: jeecg-boot
 * @Date:   2022-08-08
 * @Version: V1.0
 */
@Data
@TableName("wzycljinchanggb_man")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wzycljinchanggb_man对象", description="wzycljinchanggb_man")
public class WzycljinchanggbMan implements Serializable {
    private static final long serialVersionUID = 1L;

	/**进场过磅id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "进场过磅id")
    private java.lang.Integer id;
	/**进出材料单*/
	@Excel(name = "进出材料单", width = 15)
    @ApiModelProperty(value = "进出材料单")
        private java.lang.String jinchuliaodanno;
	/**材料编号*/
	@Excel(name = "材料编号", width = 15)
    @ApiModelProperty(value = "材料编号")
    @Dict(dictTable = "wzcailiaonamedict_man", dicText = "cailiaoname", dicCode = "cailiaono")
    private java.lang.String cailiaono;
	/**进场时间*/
	@Excel(name = "进场时间", width = 15)
    @ApiModelProperty(value = "进场时间")
    private java.lang.String jinchangshijian;
	/**出场时间*/
	@Excel(name = "出场时间", width = 15)
    @ApiModelProperty(value = "出场时间")
    private java.lang.String chuchangshijian;
	/**生产批号*/
	@Excel(name = "生产批号", width = 15)
    @ApiModelProperty(value = "生产批号")
    private java.lang.String pici;
	/**规格型号*/
	@Excel(name = "规格型号", width = 15)
    @ApiModelProperty(value = "规格型号")
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
	/**材料数量*/
	@Excel(name = "材料数量", width = 15)
    @ApiModelProperty(value = "材料数量")
    private java.lang.String jingzhong;
	/**材料单位*/
	@Excel(name = "材料单位", width = 15)
    @ApiModelProperty(value = "材料单位")
    @Dict(dicCode = "WZCaiLiaoJiLiangDanWei")
    private java.lang.String kouzhong;
	/**koulv*/
	@Excel(name = "koulv", width = 15)
    @ApiModelProperty(value = "koulv")
    private java.lang.String koulv;
	/**材料厂家*/
	@Excel(name = "材料厂家", width = 15)
    @ApiModelProperty(value = "材料厂家")
    private java.lang.String chengzhongpiancha;
	/**材料名称*/
	@Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")

    private java.lang.String liaocang;
	/**司磅员*/
	@Excel(name = "司磅员", width = 15)
    @ApiModelProperty(value = "司磅员")
    private java.lang.String sibangyuan;
	/**remark*/
	@Excel(name = "remark", width = 15)
    @ApiModelProperty(value = "remark")
    private java.lang.String remark;
    /**设备编号*/
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeibianhao;
	/**供应商单位编码*/
	@Excel(name = "供应商单位编码", width = 15)
    @ApiModelProperty(value = "供应商单位编码")
    @Dict(dictTable = "wzgongyingshang_man", dicText = "gongyingshangName", dicCode = "guid")
    private java.lang.String gongyingshangdanweibianma;
	/**过磅类别(材料单位）*/
	@Excel(name = "过磅类别(材料单位）", width = 15)
    @ApiModelProperty(value = "过磅类别(材料单位）")
    private java.lang.String guobangleibie;
	/**车辆类型*/
	@Excel(name = "车辆类型", width = 15)
    @ApiModelProperty(value = "车辆类型")
    private java.lang.String cheliangleixing;
	/**唯一标识*/
	@Excel(name = "唯一标识", width = 15)
    @ApiModelProperty(value = "唯一标识")
    private java.lang.String guid;
	/**时间戳*/
	@Excel(name = "时间戳", width = 15)
    @ApiModelProperty(value = "时间戳")
    private java.lang.Integer ts;
	/**状态0新增1已编辑过2已删除*/
	@Excel(name = "状态0新增1已编辑过2已删除", width = 15)
    @ApiModelProperty(value = "状态0新增1已编辑过2已删除")
    @Dict(dicCode = "isdel")
    private java.lang.String isdel;
	/**status*/
	@Excel(name = "status", width = 15)
    @ApiModelProperty(value = "status")
    private java.lang.String status;
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
	/**料仓编号*/
	@Excel(name = "料仓编号", width = 15)
    @ApiModelProperty(value = "料仓编号")
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private java.lang.String lcbianhao;
	/**isshouliao*/
	@Excel(name = "isshouliao", width = 15)
    @ApiModelProperty(value = "isshouliao")
    private java.lang.String isshouliao;
	/**净重(吨)*/
	@Excel(name = "净重(吨)", width = 15)
    @ApiModelProperty(value = "净重(吨)")
    private java.lang.String jingzhongt;
	/**是否统计 默认为0:未统计，1:已统计,15:统计出错*/
	@Excel(name = "是否统计 默认为0:未统计，1:已统计,15:统计出错", width = 15)
    @ApiModelProperty(value = "是否统计 默认为0:未统计，1:已统计,15:统计出错")
    @Dict(dicCode = "istongji")
    private java.lang.Integer istongji;
	/**存放地点*/
	@Excel(name = "存放地点", width = 15)
    @ApiModelProperty(value = "存放地点")
    private java.lang.String liaocangid;
	/**serialno*/
	@Excel(name = "serialno", width = 15)
    @ApiModelProperty(value = "serialno")
    private java.lang.String serialno;
	/**reason*/
	@Excel(name = "reason", width = 15)
    @ApiModelProperty(value = "reason")
    private java.lang.String reason;
	/**fileUpload*/
	@Excel(name = "fileUpload", width = 15)
    @ApiModelProperty(value = "fileUpload")
    private java.lang.String fileUpload;
	/**istaizhangtj*/
	@Excel(name = "istaizhangtj", width = 15)
    @ApiModelProperty(value = "istaizhangtj")
    private java.lang.String istaizhangtj;
	/**送货单*/
	@Excel(name = "送货单", width = 15)
    @ApiModelProperty(value = "送货单")
    private java.lang.String songhuodanpic;
	/**是否合格*/
	@Excel(name = "是否合格（0：未合格，1：合格）", width = 15)
    @ApiModelProperty(value = "是否合格（0：未合格，1：合格）")
    @Dict(dicCode = "shifouhege")
    private java.lang.String shifouhege;
	/**原材描述*/
	@Excel(name = "原材描述", width = 15)
    @ApiModelProperty(value = "原材描述")
    private java.lang.String yuancaimiaoshu;
	/**beizhu*/
	@Excel(name = "beizhu", width = 15)
    @ApiModelProperty(value = "beizhu")
    private java.lang.String beizhu;
	/**jianlipic*/
	@Excel(name = "jianlipic", width = 15)
    @ApiModelProperty(value = "jianlipic")
    private java.lang.String jianlipic;
	/**sibanpic*/
	@Excel(name = "sibanpic", width = 15)
    @ApiModelProperty(value = "sibanpic")
    private java.lang.String sibanpic;
	/**jcgkpic*/
	@Excel(name = "jcgkpic", width = 15)
    @ApiModelProperty(value = "jcgkpic")
    private java.lang.String jcgkpic;
	/**jccppic*/
	@Excel(name = "jccppic", width = 15)
    @ApiModelProperty(value = "jccppic")
    private java.lang.String jccppic;
	/**jchcppic*/
	@Excel(name = "jchcppic", width = 15)
    @ApiModelProperty(value = "jchcppic")
    private java.lang.String jchcppic;
	/**jcbfpic*/
	@Excel(name = "jcbfpic", width = 15)
    @ApiModelProperty(value = "jcbfpic")
    private java.lang.String jcbfpic;
	/**ccgkpic*/
	@Excel(name = "ccgkpic", width = 15)
    @ApiModelProperty(value = "ccgkpic")
    private java.lang.String ccgkpic;
	/**cccppic*/
	@Excel(name = "cccppic", width = 15)
    @ApiModelProperty(value = "cccppic")
    private java.lang.String cccppic;
	/**cchcppic*/
	@Excel(name = "cchcppic", width = 15)
    @ApiModelProperty(value = "cchcppic")
    private java.lang.String cchcppic;
	/**ccbfpic*/
	@Excel(name = "ccbfpic", width = 15)
    @ApiModelProperty(value = "ccbfpic")
    private java.lang.String ccbfpic;
	/**台账统计状态 0;未统计，1:已统计，10:没有料仓配置，15:没有供应商单位，20;没有材料字典，40:异常*/
	@Excel(name = "台账统计状态 0;未统计，1:已统计，10:没有料仓配置，15:没有供应商单位，20;没有材料字典，40:异常", width = 15)
    @ApiModelProperty(value = "台账统计状态 0;未统计，1:已统计，10:没有料仓配置，15:没有供应商单位，20;没有材料字典，40:异常")
    @Dict(dicCode = "taizhangtj")
    private java.lang.Integer taizhangtj;
	/**台账id*/
	@Excel(name = "台账id", width = 15)
    @ApiModelProperty(value = "台账id")
    @Dict(dictTable = "wztaizhang", dicText = "jystatus", dicCode = "id")
    private java.lang.Integer taizhangid;
}
