package com.trtm.iot.wzliaocang.entity;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 料仓配置表
 * @Author: jeecg-boot
 * @Date: 2021-05-07
 * @Version: V1.0
 */
@Data
@TableName("wzliaocang")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "wzliaocang对象", description = "料仓配置表")
public class Wzliaocang implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 料仓id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "料仓id")
    private java.lang.Integer id;
    /**
     * 料仓名称
     */
    @Excel(name = "料仓名称", width = 15)
    @ApiModelProperty(value = "料仓名称")
    private java.lang.String name;
    /**
     * 所属部门
     */
    @ApiModelProperty(value = "所属部门")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
    /**
     * 唯一标识
     */
    @Excel(name = "唯一标识", width = 15)
    @ApiModelProperty(value = "唯一标识")
    private java.lang.String guid;
    /**
     * isdel
     */
    @Excel(name = "isdel", width = 15)
    @ApiModelProperty(value = "isdel")
    private java.lang.Integer isdel;
    /**
     * 时间戳
     */
    @Excel(name = "时间戳", width = 15)
    @ApiModelProperty(value = "时间戳")
    private java.lang.Integer ts;
    /**
     * orgcode
     */
    @Excel(name = "orgcode", width = 15)
    @ApiModelProperty(value = "orgcode")
    private java.lang.String orgcode;
    /**
     * 材料类型
     */
    @Excel(name = "材料类型", width = 15)
    @ApiModelProperty(value = "材料类型")
    @Dict(dicCode = "nodeType")
    private java.lang.String cailiaono;
    /**
     * 计量单位
     */
    @Excel(name = "计量单位", width = 15)
    @ApiModelProperty(value = "计量单位")
    @Dict(dicCode = "danwei")
    private java.lang.String danwei;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**
     * departid
     */
    @Excel(name = "departid", width = 15)
    @Dict(dictTable = "sys_depart", dicText = "id", dicCode = "org_code")
    @ApiModelProperty(value = "departid")
    private java.lang.String departid;
    /**
     * 检验日期
     */
//    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
//    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Excel(name = "检验日期", width = 15)
    @ApiModelProperty(value = "检验日期")
    private java.lang.String jianyanTime;
//    /**材料状态*/
//    @Excel(name = "材料状态", width = 15)
//    @ApiModelProperty(value = "材料状态")
//    private java.lang.String cailiaoStatus;
    /**
     * 料仓状态
     */
    @Excel(name = "料仓状态", width = 15)
    @ApiModelProperty(value = "料仓状态")
    @Dict(dicCode = "liaocang_status")
    private java.lang.String liaocangStatus;
    /**
     * 规格类型
     */
    @Excel(name = "规格类型", width = 15)
    @ApiModelProperty(value = "规格类型")
    private java.lang.String guigexinghao;
    /**
     * 批次
     */
    @Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private java.lang.String pici;
    /**
     * 重量
     */
    @Excel(name = "重量", width = 15)
    @ApiModelProperty(value = "重量")
    private java.lang.String picizhong;
    /**
     * 进场日期
     */
    //    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
//    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Excel(name = "进场日期", width = 15)
    @ApiModelProperty(value = "进场日期")
    private java.lang.String jinchangTime;
    /**
     * 生产厂家
     */
    @Excel(name = "生产厂家", width = 15)
    @ApiModelProperty(value = "生产厂家")
    private java.lang.String changjia;
    /**
     * 材料名称
     */
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private java.lang.String cailiaoname;
    /**
     * 试验编号
     */
    @Excel(name = "试验编号", width = 15)
    @ApiModelProperty(value = "试验编号")
    private java.lang.String bgbianhao;
    /**
     * 库存
     */
    @Excel(name = "库存", width = 15)
    @ApiModelProperty(value = "库存")
    private java.lang.String kucun;
    /**
     * 通过门禁webserivce读到的重量
     */
    @Excel(name = "通过门禁webserivce读到的重量", width = 15)
    @ApiModelProperty(value = "通过门禁webserivce读到的重量")
    private java.lang.String weight;
    /**
     * 库存
     */
    @Excel(name = "通过门禁webserivce读到的Index", width = 15)
    @ApiModelProperty(value = "通过门禁webserivce读到的Index")
    private java.lang.Integer indexs;
    /**
     * 通过门禁webserivce读到的IC
     */
    @Excel(name = "通过门禁webserivce读到的IC", width = 15)
    @ApiModelProperty(value = "通过门禁webserivce读到的IC")
    private java.lang.String ic;
    /**
     * 通过门禁webserivce读到的State
     */
    @Excel(name = "通过门禁webserivce读到的State", width = 15)
    @ApiModelProperty(value = "通过门禁webserivce读到的State")
    private java.lang.String state;
    /**
     * 通过门禁webserivce读到的Code
     */
    @Excel(name = "通过门禁webserivce读到的Code", width = 15)
    @ApiModelProperty(value = "通过门禁webserivce读到的Code")
    private java.lang.String code;

    /**
     * 是否已安装门禁 0：未安装 1：已安装
     */
    @Excel(name = "是否已安装门禁 0：未安装 1：已安装", width = 15)
    @ApiModelProperty(value = "是否已安装门禁 0：未安装 1：已安装")
    private java.lang.Integer status;
    private java.lang.String liaoweino;//料位设备编码

//    public String getWeight() {
//        if(StringUtils.isNotBlank(picizhong) && picizhong != "/"){
//          return picizhong;
//        }
//        return weight;
//    }
    /**
     * 实时库存
     */
    @Excel(name = "实时库存", width = 15)
    @ApiModelProperty(value = "实时库存")
    private java.lang.String sskucun;

//    public void setSskucun() {
//         this.sskucun = new DecimalFormat("0.000").format(ljjinchang - ljshiyong + ljxiuzheng);
//    }


    /**
     * 库存消耗
     */
    @Excel(name = "库存消耗", width = 15)
    @ApiModelProperty(value = "库存消耗")
    private java.lang.Double kucunxh;

    public String getSskucun() {
        if (ljjinchang == null || ljshiyong == null || ljxiuzheng == null) {
            return null;
        } else {
            return new DecimalFormat("0.#####").format(ljjinchang - ljshiyong + ljxiuzheng);
        }
    }
    /**
     * 抽检状态
     */
    @Excel(name = "抽检状态", width = 15)
    @ApiModelProperty(value = "抽检状态")
    private java.lang.String choujianzt;
    /**
     * 累计进场
     */
    @Excel(name = "累计进场", width = 15)
    @ApiModelProperty(value = "累计进场")
    private java.lang.Double ljjinchang;
    /**
     * 累计使用
     */
    @Excel(name = "累计使用", width = 15)
    @ApiModelProperty(value = "累计使用")
    private java.lang.Double ljshiyong;
    /**
     * 库存消耗累计修正
     */
    @Excel(name = "库存消耗累计修正", width = 15)
    @ApiModelProperty(value = "库存消耗累计修正")
    private java.lang.Double ljxiuzheng;
    /**
     * 红外栅栏设备
     */
    @Excel(name = "红外栅栏设备", width = 15)
    @ApiModelProperty(value = "红外栅栏设备")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String infraredFence;
    /**
     * 红外栅栏状态1：布防 0：撤防
     */
    @Excel(name = "红外栅栏状态1：布防 0：撤防", width = 15)
    @ApiModelProperty(value = "红外栅栏状态1：布防 0：撤防")
    private java.lang.Integer ifStatus;

    private java.lang.String bhzbh;
    private java.lang.String picurls;
    private java.lang.String designcapacity;


}
