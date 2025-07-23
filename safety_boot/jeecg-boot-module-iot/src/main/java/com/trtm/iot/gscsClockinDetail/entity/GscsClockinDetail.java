package com.trtm.iot.gscsClockinDetail.entity;

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
 * @Description: 班组安全管控系统打卡详情表
 * @Author: jeecg-boot
 * @Date:   2022-01-25
 * @Version: V1.0
 */
@Data
@TableName("gscs_clockin_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="gscs_clockin_detail对象", description="班组安全管控系统打卡详情表")
public class GscsClockinDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
    @ApiModelProperty(value = "主键id")
    private String id;
	/**用户名*/
	@Excel(name = "用户名", width = 15)
    @ApiModelProperty(value = "用户名")
    private String username;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
    private String phone;
	/**打卡时间*/
	@Excel(name = "打卡时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "打卡时间")
    private java.util.Date clockInTime;
	/**在岗情况（0不在岗，1在岗）*/
	@Excel(name = "在岗情况（0不在岗，1在岗）", width = 15)
    @ApiModelProperty(value = "在岗情况（0不在岗，1在岗）")
    private Integer isOnline;
	/**身体状况（0健康，1有发烧咳嗽，2其他）*/
	@Excel(name = "身体状况（0健康，1有发烧咳嗽，2其他）", width = 15)
    @ApiModelProperty(value = "身体状况（0健康，1有发烧咳嗽，2其他）")
    private Integer healthStatus;
	/**是否接触病例（0否，1是）*/
	@Excel(name = "是否接触病例（0否，1是）", width = 15)
    @ApiModelProperty(value = "是否接触病例（0否，1是）")
    private Integer isContactCases;
	/**健康码颜色（0绿色，1黄色，2红色，3所在地尚未开通二维码）*/
	@Excel(name = "健康码颜色（0绿色，1黄色，2红色，3所在地尚未开通二维码）", width = 15)
    @ApiModelProperty(value = "健康码颜色（0绿色，1黄色，2红色，3所在地尚未开通二维码）")
    private Integer qrCodeColor;
	/**是否被要求隔离观察（0否，1是）*/
	@Excel(name = "是否被要求隔离观察（0否，1是）", width = 15)
    @ApiModelProperty(value = "是否被要求隔离观察（0否，1是）")
    private Integer isAskQuarantine;
	/**是否做过核酸检测（0否，1是）*/
	@Excel(name = "是否做过核酸检测（0否，1是）", width = 15)
    @ApiModelProperty(value = "是否做过核酸检测（0否，1是）")
    private Integer isNat;
	/**家庭成员是否14日内入境或者拟入境（0没有，1有）*/
	@Excel(name = "家庭成员是否14日内入境或者拟入境（0没有，1有）", width = 15)
    @ApiModelProperty(value = "家庭成员是否14日内入境或者拟入境（0没有，1有）")
    private Integer isFamilyEnter;
	/**14日内是否有出入境计划（0没有，1有）*/
	@Excel(name = "14日内是否有出入境计划（0没有，1有）", width = 15)
    @ApiModelProperty(value = "14日内是否有出入境计划（0没有，1有）")
    private Integer isEnterPlan;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
}
