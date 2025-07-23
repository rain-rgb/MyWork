package com.trtm.iot.wztaizhang.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * wztaizhang标段
 */
@Data
public class WztaizhangKCVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**id*/
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**进场时间*/
    @ApiModelProperty(value = "进场时间")
    private java.lang.String jinchangshijian;
    /**设备编号*/
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeibianhao;
    /**料仓编号*/
    @ApiModelProperty(value = "料仓编号")
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private java.lang.String lcbianhao;
    /**材料编号*/
    @ApiModelProperty(value = "材料编号")
    @Dict(dictTable = "ycl_cailiaodict", dicText = "cailiaoname", dicCode = "cailiaono")
    private java.lang.String cailiaono;
    /**毛重(吨)*/
    @ApiModelProperty(value = "毛重(吨)")
    private java.lang.String maozhongt;
    /**皮重(吨)*/
    @ApiModelProperty(value = "皮重(吨)")
    private java.lang.String pizhongt;
    /**净重(吨)*/
    @ApiModelProperty(value = "净重(吨)")
    private java.lang.String jingzhongt;
    /**批次*/
    @ApiModelProperty(value = "批次")
    private java.lang.String pici;
    /**规格类型*/
    @ApiModelProperty(value = "规格类型")
    private java.lang.String guigexinghao;
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**检验状态*/
    @ApiModelProperty(value = "检验状态")
    @Dict(dicCode = "jianyanstate")
    private java.lang.Integer jianyanstate;
    /**抽检状态*/
    @ApiModelProperty(value = "抽检状态")
    @Dict(dicCode = "choujianstate")
    private java.lang.Integer choujianstate;
    /**供应商*/
    @ApiModelProperty(value = "供应商")
    @Dict(dictTable = "wzgongyingshang", dicText = "gongyingshangname", dicCode = "guid")
    private java.lang.String gongyingshangdanweibianma;
    /**形成检验批状态*/
    @ApiModelProperty(value = "形成检验批状态")
    private java.lang.Integer isfinish;
    /**是否已取样*/
    @ApiModelProperty(value = "是否已取样")
    private java.lang.Integer isquyang;
    /**报告附件*/
    @ApiModelProperty(value = "报告附件")
    private java.lang.String baogaofile;
    /**数据推送义东高速状态（0 未推送 1已推送 2 推送失败）*/
    @ApiModelProperty(value = "数据推送义东高速状态（0 未推送 1已推送 2 推送失败）")
    private java.lang.Integer yidongstatus;
    /**所属部门*/
    @ApiModelProperty(value = "所属部门")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
    /**闭合状态*/
    @ApiModelProperty(value = "闭合状态(0:未处置,10:待审核,20已闭合)")
    private java.lang.Integer overproofStatus;
    /**存放地点*/
    @ApiModelProperty(value = "存放地点")
    private java.lang.String storagePlace;
    /**使用部位*/
    @ApiModelProperty(value = "使用部位")
    private java.lang.String usePart;
    /**委托状态（0：未委托，1：已委托）*/
    @ApiModelProperty(value = "委托状态（0：未委托，1：已委托）")
    private java.lang.Integer delegateState;
    /**过磅类型: 0:无人过磅 1:人工过磅*/
    @ApiModelProperty(value = "过磅类型: 0:无人过磅 1:人工过磅")
    private java.lang.Integer gblx;
    /**计算规则方式 0：根据地磅等计算 1：根据项目等计算*/
    @ApiModelProperty(value = "计算规则方式 0：根据地磅等计算 1：根据项目等计算")
    private java.lang.Integer ruleway;
    /**0:未处置；10:待审核，20已闭合*/
    /**检验批数据推送（0 未推送 1已推送 2 推送失败）*/
    @ApiModelProperty(value = "检验批数据推送（0 未推送 1已推送 40 推送失败）")
    @Dict(dicCode = "taizhangtj")
    private java.lang.Integer jystatus;
    /**出厂时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "出厂时间")
    private java.util.Date chuchangshijian;
    /**质保单*/
    @ApiModelProperty(value = "质保单")
    private java.lang.String zhibaodan;

    private java.lang.String nodetype;
    private java.lang.Integer quyangzhuangtai;
    private java.lang.String danwei;
    private java.lang.String sampleNo;
    private java.lang.String titCode;
    private java.lang.Integer waiweizhuangtai;
    /**标段是否自检（0：未自检，1：已自检）*/
    @ApiModelProperty(value = "标段是否自检（0：未自检，1：已自检）")
    private java.lang.Integer bdzj;
    @TableField(exist = false)
    private java.lang.String zjcsl;
    // 质保单号
    private java.lang.String shengchanpihao;
    private java.lang.Double usenum;



}
