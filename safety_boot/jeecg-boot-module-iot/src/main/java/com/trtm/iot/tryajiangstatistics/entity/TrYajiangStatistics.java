package com.trtm.iot.tryajiangstatistics.entity;

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
 * @Description: 压浆统计表
 * @Author: jeecg-boot
 * @Date:   2022-06-10
 * @Version: V1.0
 */
@Data
@TableName("tr_yajiang_statistics")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tr_yajiang_statistics对象", description="压浆统计表")
public class TrYajiangStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private int id;
	/**合同号*/
	@Excel(name = "合同号", width = 15)
    @ApiModelProperty(value = "合同号")
    private String htbh;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private String gcmc;
    /**压浆时间*/
    @Excel(name = "压浆时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "压浆时间")
    private Date yjsj;
	/**桩号及部位*/
	@Excel(name = "桩号及部位", width = 15)
    @ApiModelProperty(value = "桩号及部位")
    private String zhbw;
	/**施工部位*/
	@Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    private String sgbw;
	/**构件结构*/
	@Excel(name = "构件结构", width = 15)
    @ApiModelProperty(value = "构件结构")
    private String gjjg;
	/**构件编号及长度*/
	@Excel(name = "构件编号及长度", width = 15)
    @ApiModelProperty(value = "构件编号及长度")
    private String gjbh;
	/**压浆设备编号*/
	@Excel(name = "压浆设备编号", width = 15)
    @ApiModelProperty(value = "压浆设备编号")
    private String yjsbbh;
	/**梁板类型*/
	@Excel(name = "梁板类型", width = 15)
    @ApiModelProperty(value = "梁板类型")
    private String lblx;
	/**部门名称*/
    @ApiModelProperty(value = "部门名称")
    private String sysOrgCode;
	/**总条数*/
	@Excel(name = "总条数", width = 15)
    @ApiModelProperty(value = "总条数")
    private Integer trYajiangSum;
	/**不合格数*/
	@Excel(name = "不合格数", width = 15)
    @ApiModelProperty(value = "不合格数")
    private Integer trYajaingOversum;
}
