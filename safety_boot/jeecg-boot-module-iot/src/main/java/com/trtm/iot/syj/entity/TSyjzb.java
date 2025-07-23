package com.trtm.iot.syj.entity;

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
 * @Description: t_syjzb
 * @Author: jeecg-boot
 * @Date:   2021-03-12
 * @Version: V1.0
 */
@Data
@TableName("t_syjzb")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="t_syjzb对象", description="t_syjzb")
public class TSyjzb implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**version*/
	@Excel(name = "version", width = 15)
    @ApiModelProperty(value = "version")
    private Integer version;
	/**唯一码*/
	@Excel(name = "唯一码", width = 15)
    @ApiModelProperty(value = "唯一码")
    private String syjid;
	/**委托ID*/
	@Excel(name = "委托ID", width = 15)
    @ApiModelProperty(value = "委托ID")
    private String wtid;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String sbbh;
	/**试验类型*/
	@Excel(name = "试验类型", width = 15)
    @Dict(dicCode = "SYLX")
    @ApiModelProperty(value = "试验类型")
    private String sylx;
	/**委托编号*/
	@Excel(name = "委托编号", width = 15)
    @ApiModelProperty(value = "委托编号")
    private String wtbh;
	/**试件编号*/
	@Excel(name = "试件编号", width = 15)
    @ApiModelProperty(value = "试件编号")
    private String sjbh;
	/**制件日期*/
	@Excel(name = "制件日期", width = 15)
    @ApiModelProperty(value = "制件日期")
    private String zzrq;
	/**试验日期*/
	@Excel(name = "试验日期", width = 15)
    @ApiModelProperty(value = "试验日期")
    private String syrq;
	/**试验完成时间*/
	@Excel(name = "试验完成时间", width = 15)
    @ApiModelProperty(value = "试验完成时间")
    private String sywcsj;
	/**龄期*/
	@Excel(name = "龄期", width = 15)
    @ApiModelProperty(value = "龄期")
    private Integer lq;
	/**试件尺寸*/
	@Excel(name = "试件尺寸", width = 15)
    @ApiModelProperty(value = "试件尺寸")
    private String sjcc;
	/**试件面积*/
	@Excel(name = "试件面积", width = 15)
    @ApiModelProperty(value = "试件面积")
    private String sjmj;
	/**试件数量*/
	@Excel(name = "试件数量", width = 15)
    @ApiModelProperty(value = "试件数量")
    private String sjsl;
	/**设计强度*/
	@Excel(name = "设计强度", width = 15)
    @ApiModelProperty(value = "设计强度")
    private String sjqd;
	/**折算系数*/
	@Excel(name = "折算系数", width = 15)
    @ApiModelProperty(value = "折算系数")
    private String zsxs;
	/**强度代表值*/
	@Excel(name = "强度代表值", width = 15)
    @ApiModelProperty(value = "强度代表值")
    private String qddbz;
	/**判定结果*/
	@Excel(name = "判定结果", width = 15)
    @ApiModelProperty(value = "判定结果")
    private String pdjg;
	/**操作人员*/
	@Excel(name = "操作人员", width = 15)
    @ApiModelProperty(value = "操作人员")
    private String czry;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private String cjmc;
	/**品种编码*/
	@Excel(name = "品种编码", width = 15)
    @ApiModelProperty(value = "品种编码")
    private String pzbm;
	/**公称直径*/
	@Excel(name = "公称直径", width = 15)
    @ApiModelProperty(value = "公称直径")
    private String gczj;
	/**承压面积*/
	@Excel(name = "承压面积", width = 15)
    @ApiModelProperty(value = "承压面积")
    private String area;
	/**iswjj*/
	@Excel(name = "iswjj", width = 15)
    @ApiModelProperty(value = "iswjj")
    private Integer iswjj;
	/**rtcode*/
	@Excel(name = "rtcode", width = 15)
    @ApiModelProperty(value = "rtcode")
    private String rtcode;
	/**szfw*/
	@Excel(name = "szfw", width = 15)
    @ApiModelProperty(value = "szfw")
    private String szfw;
	/**生产厂家*/
	@Excel(name = "生产厂家", width = 15)
    @ApiModelProperty(value = "生产厂家")
    private String fbl;
	/**status*/
	@Excel(name = "status", width = 15)
    @ApiModelProperty(value = "status")
    private Integer status;
	/**wtzs*/
	@Excel(name = "wtzs", width = 15)
    @ApiModelProperty(value = "wtzs")
        private String wtzs;
	/**recGuid*/
	@Excel(name = "recGuid", width = 15)
    @ApiModelProperty(value = "recGuid")
    private String recGuid;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String beizhu;
	/**tjstate*/
	@Excel(name = "tjstate", width = 15)
    @ApiModelProperty(value = "tjstate")
    private Integer tjstate;
	/**judgestate*/
	@Excel(name = "judgestate", width = 15)
    @ApiModelProperty(value = "judgestate")
    private Integer judgestate;
	/**数据上传时间*/
	@Excel(name = "数据上传时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据上传时间")
    private Date sjscsj;
    private String sgbw;
    private  String gcmc;
	private Integer overproofStatus;//审核状态
    private Integer statistics;//统计状态
    private Integer isbhkykz;//滨淮抗压抗折机是否推送 0 未推送 1 已推送
    private Integer iszlpz;
    private Integer issend;
    private String orgcode;
    private String projectname;
    private String sksjqd;
    private String sjqfqd;
    private String sjklqd;
    private String sjscl;
    private String sjzdlzysl;
    private String bhgyy;
    private String facepic;
}
