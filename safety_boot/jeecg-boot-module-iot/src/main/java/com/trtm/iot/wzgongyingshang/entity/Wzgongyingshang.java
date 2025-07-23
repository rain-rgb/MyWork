package com.trtm.iot.wzgongyingshang.entity;

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
 * @Description: 物资供应商主表
 * @Author: jeecg-boot
 * @Date:   2021-05-07
 * @Version: V1.0
 */
@Data
@TableName("wzgongyingshang")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wzgongyingshang对象", description="物资供应商主表")
public class Wzgongyingshang implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private java.lang.Integer id;
	/**供应商名*/
	@Excel(name = "供应商名", width = 15)
    @ApiModelProperty(value = "供应商名")
    private java.lang.String gongyingshangname;
	/**供应商名字母缩写*/
	@Excel(name = "供应商名字母缩写", width = 15)
    @ApiModelProperty(value = "供应商名字母缩写")
    private java.lang.String pinyin;
	/**单位编码*/
	@Excel(name = "单位编码", width = 15)
    @ApiModelProperty(value = "单位编码")
    private java.lang.String danweibianma;
	/**供应材料*/
	@Excel(name = "供应材料", width = 15)
    @ApiModelProperty(value = "供应材料")
    private java.lang.String gongyingcailiao;
	/**所属地区*/
	@Excel(name = "所属地区", width = 15)
    @ApiModelProperty(value = "所属地区")
    private java.lang.String suoshudiqu;
	/**联系人*/
	@Excel(name = "联系人", width = 15)
    @ApiModelProperty(value = "联系人")
    private java.lang.String lianxiren;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
    private java.lang.String lianxidianhua;
	/**手机*/
	@Excel(name = "手机", width = 15)
    @ApiModelProperty(value = "手机")
    private java.lang.String shouji;
	/**传真*/
	@Excel(name = "传真", width = 15)
    @ApiModelProperty(value = "传真")
    private java.lang.String chuanzhen;
	/**邮编*/
	@Excel(name = "邮编", width = 15)
    @ApiModelProperty(value = "邮编")
    private java.lang.String youbian;
	/**通讯地址*/
	@Excel(name = "通讯地址", width = 15)
    @ApiModelProperty(value = "通讯地址")
    private java.lang.String tongxundizhi;
	/**网址*/
	@Excel(name = "网址", width = 15)
    @ApiModelProperty(value = "网址")
    private java.lang.String wangzhi;
	/**证书名称*/
	@Excel(name = "证书名称", width = 15)
    @ApiModelProperty(value = "证书名称")
    private java.lang.String zhengshumingcheng;
	/**证书编号*/
	@Excel(name = "证书编号", width = 15)
    @ApiModelProperty(value = "证书编号")
    private java.lang.String zhengshubianhao;
	/**发证时间*/
	@Excel(name = "发证时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "发证时间")
    private java.util.Date fazhengshijian;
	/**有效期限*/
	@Excel(name = "有效期限", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "有效期限")
    private java.util.Date youxiaoqixian;
	/**发证机关*/
	@Excel(name = "发证机关", width = 15)
    @ApiModelProperty(value = "发证机关")
    private java.lang.String fazhengjiguan;
	/**证件图片*/
	@Excel(name = "证件图片", width = 15)
    @ApiModelProperty(value = "证件图片")
    private java.lang.String zhengjiantupian;
	/**开户银行*/
	@Excel(name = "开户银行", width = 15)
    @ApiModelProperty(value = "开户银行")
    private java.lang.String kaihuyinhang;
	/**银行账号*/
	@Excel(name = "银行账号", width = 15)
    @ApiModelProperty(value = "银行账号")
    private java.lang.String yinhangzhanghao;
	/**企业税号*/
	@Excel(name = "企业税号", width = 15)
    @ApiModelProperty(value = "企业税号")
    private java.lang.String qiyeshuihao;
	/**邮箱*/
	@Excel(name = "邮箱", width = 15)
    @ApiModelProperty(value = "邮箱")
    private java.lang.String youxiang;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**企业状态:0=正常,1=异常*/
	@Excel(name = "企业状态:0=正常,1=异常", width = 15)
    @ApiModelProperty(value = "企业状态:0=正常,1=异常")
    private java.lang.String states;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**组织机构ID*/
    @ApiModelProperty(value = "组织机构ID")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**供应材料名*/
	@Excel(name = "供应材料名", width = 15)
    @ApiModelProperty(value = "供应材料名")
    private java.lang.String gongyingcailiaoname;
	/**UUID*/
	@Excel(name = "UUID", width = 15)
    @ApiModelProperty(value = "UUID")
    private java.lang.String guid;
	/**时间戳*/
	@Excel(name = "时间戳", width = 15)
    @ApiModelProperty(value = "时间戳")
    private java.lang.Integer ts;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.Integer status;
	/**是否删除:0=否,1=是*/
	@Excel(name = "是否删除:0=否,1=是", width = 15)
    @ApiModelProperty(value = "是否删除:0=否,1=是")
    private java.lang.Integer isdel;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String updateBy;
    /**departid*/
    @Excel(name = "departid", width = 15)
    @ApiModelProperty(value = "departid")
    @Dict(dictTable = "sys_depart", dicText = "id", dicCode = "org_code")
    private java.lang.String departid;
    private java.lang.Integer iselocks;//是否使用电子锁
}
