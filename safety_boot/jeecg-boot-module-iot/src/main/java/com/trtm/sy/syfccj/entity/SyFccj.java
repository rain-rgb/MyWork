package com.trtm.sy.syfccj.entity;

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
 * @Description: 发车抽检
 * @Author: jeecg-boot
 * @Date:   2022-09-20
 * @Version: V1.0
 */
@Data
@TableName("sy_fccj")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_fccj对象", description="发车抽检")
public class SyFccj implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**关联发车单号*/
	@Excel(name = "关联发车单号", width = 15)
    @ApiModelProperty(value = "关联发车单号")
    private java.lang.String fcid;
	/**state 0：确认抽检；1：抽检合格；2抽检不合格*/
	@Excel(name = "state 0：确认抽检；1：抽检合格；2抽检不合格", width = 15)
    @ApiModelProperty(value = "state 0：确认抽检；1：抽检合格；2抽检不合格")
    private java.lang.Integer state;
	/**确认时间*/
	@Excel(name = "确认时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "确认时间")
    private java.util.Date qrtime;
	/**确认抽检人（用户id）*/
	@Excel(name = "确认抽检人（用户id）", width = 15)
    @ApiModelProperty(value = "确认抽检人（用户id）")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String qrp;
	/**抽检人所处单位（0施工单位，1实验室，2监理，3指挥部, 4外委）*/
	@Excel(name = "抽检人所处单位（0施工单位，1实验室，2监理，3指挥部, 4外委）", width = 15)
    @ApiModelProperty(value = "抽检人所处单位（0施工单位，1实验室，2监理，3指挥部, 4外委）")
    private java.lang.String cjdw;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String beizhu;
	/**文件*/
	@Excel(name = "文件", width = 15)
    @ApiModelProperty(value = "文件")
    private java.lang.String file;
	/**不合格原因*/
	@Excel(name = "不合格原因", width = 15)
    @ApiModelProperty(value = "不合格原因")
    private java.lang.String reason;
	/**抽检人*/
	@Excel(name = "抽检人", width = 15)
    @ApiModelProperty(value = "抽检人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String cjp;
	/**抽检时间*/
	@Excel(name = "抽检时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "抽检时间")
    private java.util.Date cjtime;

    /**材料编号*/
    @Excel(name = "送货任务单号", width = 15)
    @ApiModelProperty(value = "送货任务单号")
//    @Dict(dictTable = "wzcailiaonamedict", dicText = "cailiaoName", dicCode = "cailiaoNo")
    private java.lang.String cailiaono;
}
