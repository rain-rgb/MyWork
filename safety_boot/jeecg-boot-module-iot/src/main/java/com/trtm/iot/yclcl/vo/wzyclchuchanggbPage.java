package com.trtm.iot.yclcl.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * \* @author: Xx
 * \* Date: 2021/6/22
 * \* Time: 15:55
 * \* Description:
 * \
 */
@Data
@ApiModel(value="wzyclchuchanggbPage对象", description="物资出场主表")
public class wzyclchuchanggbPage {
    @ApiModelProperty(value = "进场过磅id")
    private Integer id;
    /**进出材料单*/
    @Excel(name = "进出材料单", width = 15)
    @ApiModelProperty(value = "进出材料单")
    private String jinchuliaodanno;
    /**材料编号*/
    @Excel(name = "材料编号", width = 15)
    @ApiModelProperty(value = "材料编号")
    private String cailiaono;

    private String cailiaoName;

    private String cailiaoguige;
    /**进场时间*/
    @Excel(name = "进场时间", width = 15)
    @ApiModelProperty(value = "进场时间")
    private String jinchangshijian;
    /**出场时间*/
    @Excel(name = "出场时间", width = 15)
    @ApiModelProperty(value = "出场时间")
    private String chuchangshijian;
    /**批次*/
    @Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private String pici;
    /**船号*/
    @Excel(name = "船号", width = 15)
    @ApiModelProperty(value = "船号")
    private String cheliangbianhao;
    /**前车牌*/
    @Excel(name = "前车牌", width = 15)
    @ApiModelProperty(value = "前车牌")
    private String qianchepai;
    /**后车牌*/
    @Excel(name = "后车牌", width = 15)
    @ApiModelProperty(value = "后车牌")
    private String houchepai;
    /**毛重*/
    @Excel(name = "毛重", width = 15)
    @ApiModelProperty(value = "毛重")
    private String maozhong;
    /**皮重*/
    @Excel(name = "皮重", width = 15)
    @ApiModelProperty(value = "皮重")
    private String pizhong;
    /**净重*/
    @Excel(name = "净重", width = 15)
    @ApiModelProperty(value = "净重")
    private String jingzhong;
    /**扣重*/
    @Excel(name = "扣重", width = 15)
    @ApiModelProperty(value = "扣重")
    private String kouzhong;
    /**扣率*/
    @Excel(name = "扣率", width = 15)
    @ApiModelProperty(value = "扣率")
    private String koulv;
    /**称重偏差*/
    @Excel(name = "称重偏差", width = 15)
    @ApiModelProperty(value = "称重偏差")
    private String chengzhongpiancha;
    /**累计*/
    @Excel(name = "累计", width = 15)
    @ApiModelProperty(value = "累计")
    private String liaocang;
    /**司磅员*/
    @Excel(name = "司磅员", width = 15)
    @ApiModelProperty(value = "司磅员")
    private String sibangyuan;
    /**remark*/
    @Excel(name = "remark", width = 15)
    @ApiModelProperty(value = "remark")
    private String remark;
    /**设备编号*/
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String shebeibianhao;


    private String sbname;
    /**供应商单位编码*/
    @Excel(name = "供应商单位编码", width = 15)
    @ApiModelProperty(value = "供应商单位编码")
    private String gongyingshangdanweibianma;

    private String gongyingshangname;
    /**过磅类别*/
    @Excel(name = "过磅类别", width = 15)
    @ApiModelProperty(value = "过磅类别")
    private String guobangleibie;
    /**车辆类型*/
    @Excel(name = "车辆类型", width = 15)
    @ApiModelProperty(value = "车辆类型")
    private String cheliangleixing;
    /**唯一标识*/
    @Excel(name = "唯一标识", width = 15)
    @ApiModelProperty(value = "唯一标识")
    private String guid;
    /**时间戳*/
    @Excel(name = "时间戳", width = 15)
    @ApiModelProperty(value = "时间戳")
    private Integer ts;
    /**是否删除*/
    @Excel(name = "是否删除", width = 15)
    @ApiModelProperty(value = "是否删除")
    private String isdel;
    /**status*/
    @Excel(name = "status", width = 15)
    @ApiModelProperty(value = "status")
    private String status;
    /**毛重(吨)*/
    @Excel(name = "毛重(吨)", width = 15)
    @ApiModelProperty(value = "毛重(吨)")
    private String maozhongt;
    /**皮重(吨)*/
    @Excel(name = "皮重(吨)", width = 15)
    @ApiModelProperty(value = "皮重(吨)")
    private String pizhongt;
    /**candi*/
    @Excel(name = "candi", width = 15)
    @ApiModelProperty(value = "candi")
    private String candi;
    /**运输单位*/
    @Excel(name = "运输单位", width = 15)
    @ApiModelProperty(value = "运输单位")
    private String yunshudanwei;
    /**料仓编号*/
    @Excel(name = "料仓编号", width = 15)
    @ApiModelProperty(value = "料仓编号")
    private String lcbianhao;
    private String lcname;
    /**isshouliao*/
    @Excel(name = "isshouliao", width = 15)
    @ApiModelProperty(value = "isshouliao")
    private String isshouliao;
    /**净重(吨)*/
    @Excel(name = "净重(吨)", width = 15)
    @ApiModelProperty(value = "净重(吨)")
    private String jingzhongt;
    /**是否统计 默认为0:未统计，1:已统计,15:统计出错*/
    @Excel(name = "是否统计 默认为0:未统计，1:已统计,15:统计出错", width = 15)
    @Dict(dicCode = "istongji")
    @ApiModelProperty(value = "是否统计 默认为0:未统计，1:已统计,15:统计出错")
    private Integer istongji;
    /**jcgkpic*/
    @Excel(name = "jcgkpic", width = 15)
    @ApiModelProperty(value = "jcgkpic")
    private String jcgkpic;
    /**jccppic*/
    @Excel(name = "jccppic", width = 15)
    @ApiModelProperty(value = "jccppic")
    private String jccppic;
    /**jchcppic*/
    @Excel(name = "jchcppic", width = 15)
    @ApiModelProperty(value = "jchcppic")
    private String jchcppic;
    /**jcbfpic*/
    @Excel(name = "jcbfpic", width = 15)
    @ApiModelProperty(value = "jcbfpic")
    private String jcbfpic;
    /**ccgkpic*/
    @Excel(name = "ccgkpic", width = 15)
    @ApiModelProperty(value = "ccgkpic")
    private String ccgkpic;
    /**cccppic*/
    @Excel(name = "cccppic", width = 15)
    @ApiModelProperty(value = "cccppic")
    private String cccppic;
    /**cchcppic*/
    @Excel(name = "cchcppic", width = 15)
    @ApiModelProperty(value = "cchcppic")
    private String cchcppic;
    /**ccbfpic*/
    @Excel(name = "ccbfpic", width = 15)
    @ApiModelProperty(value = "ccbfpic")
    private String ccbfpic;
    /**卸货地点*/
    @Excel(name = "卸货地点", width = 15)
    @ApiModelProperty(value = "卸货地点")
    private String liaocangid;
    /**serialno*/
    @Excel(name = "serialno", width = 15)
    @ApiModelProperty(value = "serialno")
    private String serialno;
    /**reason*/
    @Excel(name = "reason", width = 15)
    @ApiModelProperty(value = "reason")
    private String reason;
    /**fileUpload*/
    @Excel(name = "fileUpload", width = 15)
    @ApiModelProperty(value = "fileUpload")
    private String fileUpload;
    /**istaizhangtj*/
    @Excel(name = "istaizhangtj", width = 15)
    @ApiModelProperty(value = "istaizhangtj")
    private String istaizhangtj;
    /**送货单*/
    @Excel(name = "送货单", width = 15)
    @ApiModelProperty(value = "送货单")
    private String songhuodanpic;
    /**是否合格*/
    @Excel(name = "是否合格", width = 15)
    @ApiModelProperty(value = "是否合格")
    private String shifouhege;
    /**原材描述*/
    @Excel(name = "原材描述", width = 15)
    @ApiModelProperty(value = "原材描述")
    private String yuancaimiaoshu;
    /**beizhu*/
    @Excel(name = "beizhu", width = 15)
    @ApiModelProperty(value = "beizhu")
    private String beizhu;
    /**jianlipic*/
    @Excel(name = "jianlipic", width = 15)
    @ApiModelProperty(value = "jianlipic")
    private String jianlipic;
    /**sibanpic*/
    @Excel(name = "sibanpic", width = 15)
    @ApiModelProperty(value = "sibanpic")
    private String sibanpic;
}
