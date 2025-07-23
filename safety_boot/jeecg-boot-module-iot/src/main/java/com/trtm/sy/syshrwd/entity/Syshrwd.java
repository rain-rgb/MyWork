package com.trtm.sy.syshrwd.entity;

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
 * @Description: 试验收货任务单
 * @Author: jeecg-boot
 * @Date:   2022-09-08
 * @Version: V1.0
 */
@Data
@TableName("sy_shrwd")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_shrwd对象", description="试验收货任务单")
public class Syshrwd implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**送货任务单号*/
	@Excel(name = "送货任务单号", width = 15)
    @ApiModelProperty(value = "送货任务单号")
    private String shrwd;
	/**guid*/
	@Excel(name = "guid", width = 15)
    @ApiModelProperty(value = "guid")
    private String guid;
	/**材料类型*/
	@Excel(name = "材料类型", width = 15)
    @ApiModelProperty(value = "材料类型")
    @Dict(dicCode = "nodeType")
    private String cailiaotype;
	/**材料规格型号*/
	@Excel(name = "材料规格型号", width = 15)
    @ApiModelProperty(value = "材料规格型号")
    private String ggxh;
	/**材料编号*/
	@Excel(name = "材料编号", width = 15)
    @ApiModelProperty(value = "材料编号")
    private String cailaiobh;
	/**批次号*/
	@Excel(name = "批次号", width = 15)
    @ApiModelProperty(value = "批次号")
    private String picihao;
	/**要求到货时间*/
	@Excel(name = "要求到货时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "要求到货时间")
    private Date endtime;
	/**预计车次数*/
	@Excel(name = "预计车次数", width = 15)
    @ApiModelProperty(value = "预计车次数")
    private Integer cartimes;
	/**状态*/
	@Excel(name = "状态（0厂家未确认；1厂家确认回函；2已发货；3已签收）", width = 15)
    @ApiModelProperty(value = "状态（0厂家未确认；1厂家确认回函；2已发货；3已签收）")
    @Dict(dicCode = "shStatus")
    private String status;
	/**送货任务单创建人*/
	@Excel(name = "送货任务单创建人", width = 15)
    @ApiModelProperty(value = "送货任务单创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String createby;
	/**送货任务单创建时间*/
	@Excel(name = "送货任务单创建时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "送货任务单创建时间")
    private Date caeatetime;
	/**厂家确认时间*/
	@Excel(name = "厂家确认时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "厂家确认时间")
    private Date oktime;
	/**厂家确认人*/
	@Excel(name = "厂家确认人", width = 15)
    @ApiModelProperty(value = "厂家确认人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String okby;
	/**数量（单位吨）*/
	@Excel(name = "数量（单位吨）", width = 15)
    @ApiModelProperty(value = "数量（单位吨）")
    private String shuliang;
	/**目的地*/
	@Excel(name = "目的地", width = 15)
    @ApiModelProperty(value = "目的地")
    @Dict(dictTable = "wb_destination", dicText = "departname", dicCode = "sys_org_code")
    private String mudidi;
	/**单位（吨，kg）*/
	@Excel(name = "单位（吨，kg）", width = 15)
    @ApiModelProperty(value = "单位（吨，kg）")
    @Dict(dicCode = "danwei")
    private String danwei;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String beizhu;
	/**质保单*/
	@Excel(name = "质保单", width = 15)
    @ApiModelProperty(value = "质保单")
    private String zhibaodan;
	/**厂家联系人*/
	@Excel(name = "厂家联系人", width = 15)
    @ApiModelProperty(value = "厂家联系人")
    private String lianxr;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private String jd;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private String wd;
	/**收货部门*/
	@Excel(name = "收货部门", width = 15)
    @ApiModelProperty(value = "收货部门")
    private String shouhuodp;
	/**订单所属组织机构*/
	@Excel(name = "订单所属组织机构", width = 15)
    @ApiModelProperty(value = "订单所属组织机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private String sysorgcode;
	/**收货时间*/
	@Excel(name = "收货时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "收货时间")
    private Date shouhuotime;
	/**收料检验员*/
	@Excel(name = "收料检验员", width = 15)
    @ApiModelProperty(value = "收料检验员")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String shouliaojy;

    @Excel(name = "预计到货时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "厂家预计全部到货时间")
    private Date endtime1;

    private String phone;

    @Dict(dictTable = "wzgongyingshang", dicText = "gongyingshangName", dicCode = "guid")
    private String gysguid;

    private  Boolean ismsg;// 是否发送短信

    private Integer cars;// 目前车次数

    // 施工单位电话
    private String createphone;
    // 监理人
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String jlcheckp;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "监理审核时间")
    private Date jlchecktime;
    // 监理电话
    private String jlphone;

    private Integer ischaoshiqueren;//是否超时确认

}
