package com.trtm.iot.yclsl.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
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
 * @Description: 原材料收料主表
 * @Author: jeecg-boot
 * @Date:   2021-04-21
 * @Version: V1.0
 */
@Data
@TableName("wzycljinchanggb")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wzycljinchanggb对象", description="原材料收料主表")
public class Wzycljinchanggb implements Serializable {
    private static final long serialVersionUID = 1L;

	/**进场过磅id*/
	@TableId(type = IdType.AUTO)
    @Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "进场过磅id")
    private java.lang.Integer id;
	/**进出材料单*/
	@Excel(name = "进出材料单", width = 15)
    @ApiModelProperty(value = "进出材料单")
    private java.lang.String jinchuliaodanno;
	/**材料编号*/
	@Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    @Dict(dictTable = "wzcailiaonamedict", dicText = "cailiaoname", dicCode = "cailiaono")
    private java.lang.String cailiaono;
    /**beizhu*/
    @Excel(name = "规格型号", width = 15)
    @ApiModelProperty(value = "beizhu")
    private java.lang.String beizhu;
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
	/**船号*/
    @ApiModelProperty(value = "船号")
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
	/**累计*/
	@Excel(name = "累计", width = 15)
    @ApiModelProperty(value = "累计")
    private java.lang.String liaocang;
	/**司磅员*/
	@Excel(name = "司磅员", width = 15)
    @ApiModelProperty(value = "司磅员")
    private java.lang.String sibangyuan;
	/**remark*/
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
    @Dict(dictTable = "wzgongyingshang", dicText = "gongyingshangname", dicCode = "guid")
    private java.lang.String gongyingshangdanweibianma;
	/**过磅类别*/
    @ApiModelProperty(value = "过磅类别")
    private java.lang.String guobangleibie;
	/**车辆类型*/
    @ApiModelProperty(value = "车辆类型")
    private java.lang.String cheliangleixing;
	/**唯一标识*/
    @ApiModelProperty(value = "唯一标识")
    private java.lang.String guid;
	/**时间戳*/
    @ApiModelProperty(value = "时间戳")
    private java.lang.Integer ts;
	/**是否删除*/
    @ApiModelProperty(value = "是否删除")
//    @TableLogic(value = "0", delval = "1")
    private java.lang.String isdel;
	/**status*/
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
    @ApiModelProperty(value = "candi")
    private java.lang.String candi;
	/**运输单位*/
    @ApiModelProperty(value = "运输单位")
    private java.lang.String yunshudanwei;
	/**料仓编号*/
	@Excel(name = "料仓编号", width = 15)
    @ApiModelProperty(value = "料仓编号")
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private java.lang.String lcbianhao;
	/**isshouliao*/
    @ApiModelProperty(value = "isshouliao")
    private java.lang.String isshouliao;
	/**净重(吨)*/
	@Excel(name = "净重(吨)", width = 15)
    @ApiModelProperty(value = "净重(吨)")
    private java.lang.String jingzhongt;
	/**是否统计 默认为0:未统计，1:已统计,15:统计出错*/
    @Dict(dicCode = "istongji")
    @ApiModelProperty(value = "是否统计 默认为0:未统计，1:已统计,15:统计出错")
    private java.lang.Integer istongji;
	/**jcgkpic*/
    @ApiModelProperty(value = "jcgkpic")
    private java.lang.String jcgkpic;
	/**jccppic*/
    @ApiModelProperty(value = "jccppic")
    private java.lang.String jccppic;
	/**jchcppic*/
    @ApiModelProperty(value = "jchcppic")
    private java.lang.String jchcppic;
	/**jcbfpic*/
    @ApiModelProperty(value = "jcbfpic")
    private java.lang.String jcbfpic;
	/**ccgkpic*/
    @ApiModelProperty(value = "ccgkpic")
    private java.lang.String ccgkpic;
	/**cccppic*/
    @ApiModelProperty(value = "cccppic")
    private java.lang.String cccppic;
	/**cchcppic*/
    @ApiModelProperty(value = "cchcppic")
    private java.lang.String cchcppic;
	/**ccbfpic*/
    @ApiModelProperty(value = "ccbfpic")
    private java.lang.String ccbfpic;
	/**卸货地点*/
    @ApiModelProperty(value = "卸货地点")
    private java.lang.String liaocangid;
	/**serialno*/
    @ApiModelProperty(value = "serialno")
    private java.lang.String serialno;
	/**reason*/
    @ApiModelProperty(value = "reason")
    private java.lang.String reason;
	/**fileUpload*/
    @ApiModelProperty(value = "fileUpload")
    private java.lang.String fileUpload;
	/**istaizhangtj*/
    @ApiModelProperty(value = "istaizhangtj")
    private java.lang.String istaizhangtj;
	/**送货单*/
    @ApiModelProperty(value = "送货单")
    private java.lang.String songhuodanpic;
	/**是否合格*/
    @ApiModelProperty(value = "是否合格")
    private java.lang.String shifouhege;
	/**原材描述*/
    @ApiModelProperty(value = "原材描述")
    private java.lang.String yuancaimiaoshu;

	/**jianlipic*/
    @ApiModelProperty(value = "jianlipic")
    private java.lang.String jianlipic;
	/**sibanpic*/
    @ApiModelProperty(value = "sibanpic")
    private java.lang.String sibanpic;
    /**台账是否统计*/
    @ApiModelProperty(value = "台账是否统计")
    @Dict(dicCode = "taizhangtj")
    private java.lang.Integer taizhangtj;
    /**台账id*/
    @ApiModelProperty(value = "台账id")
    @Dict(dictTable = "wztaizhang", dicText = "jystatus", dicCode = "id")
    private java.lang.Integer taizhangid;
    /**净重T统计*/
    @ApiModelProperty(value = "净重T统计")
    private java.lang.Integer jingzhongttj;
    private java.lang.Integer statuscode;

    @TableField(exist = false)
    private String rengon;

}
