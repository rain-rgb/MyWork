package com.trtm.iot.safetyTunnelCarHistory.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: 车辆门禁考勤历史数据表
 * @Author: jeecg-boot
 * @Date:   2022-08-01
 * @Version: V1.0
 */
@Data
@TableName("safety_tunnel_car_history")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="safety_tunnel_car_history对象", description="车辆门禁考勤历史数据表")
public class SafetyTunnelCarHistory implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键(隧道车辆)*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键(隧道车辆)")
    private java.lang.Integer id;
	/**车牌号*/
	@Excel(name = "车牌号", width = 15)
    @ApiModelProperty(value = "车牌号")
    private java.lang.String card;
	/**进入时间*/
	@Excel(name = "进入时间", width = 15)
    @ApiModelProperty(value = "进入时间")
    private java.lang.String enterTime;
	/**出去时间*/
	@Excel(name = "出去时间", width = 15)
    @ApiModelProperty(value = "出去时间")
    private java.lang.String outTime;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**唯一标识*/
	@Excel(name = "唯一标识", width = 15)
    @ApiModelProperty(value = "唯一标识")
    private java.lang.String guid;
	/**左右隧道标志 1 左洞  2右洞*/
	@Excel(name = "左右隧道标志 1 左洞  2右洞", width = 15)
    @ApiModelProperty(value = "左右隧道标志 1 左洞  2右洞")
    private java.lang.Integer lorr;
    @Excel(name = "车辆数", width = 15)
    @ApiModelProperty(value = "车辆数")
    @TableField(exist = false)
    private java.lang.Integer vehiclesTypeNumber;

	/**隧道进出标志 1 进洞  2 出洞*/
	@Excel(name = "隧道进出标志 1 进洞  2 出洞", width = 15)
    @ApiModelProperty(value = "隧道进出标志 1 进洞  2 出洞")
    private java.lang.Integer ioflag;
    private java.lang.String tagid;//卡号
    @Excel(name = "类型", width = 15)
    @ApiModelProperty(value = "类型")
    private java.lang.String type;//类型
    private java.lang.String owner;//司机
    private java.lang.String tunnelname;//隧道名称
    @ApiModelProperty(value = "洞内在洞时常", dataType = "String")
    private String caveOften;

    @ApiModelProperty(value = "定位时间", dataType = "Date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date positionTime;

    @ApiModelProperty(value = "距离洞口(米)", dataType = "BigDecimal")
    private BigDecimal openTunnelDistance;

    @ApiModelProperty(value = "逻辑删除标识", dataType = "Integer")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建人", dataType = "String")
    private String createPerson;

    @ApiModelProperty(value = "创建时间", dataType = "Date")
    private Date createTime;


    @ApiModelProperty(value = "项目id", dataType = "String")
    private String projectId;

    @ApiModelProperty(value = "标段id", dataType = "String")
    private String sectionId;


    @ApiModelProperty(value = "隧道id", dataType = "String")
    private String tunnelId;


}
