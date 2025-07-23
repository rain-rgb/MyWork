package com.trtm.iot.hc_tfstationdetail.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 土方工作区逐桩详情
 * @Author: jeecg-boot
 * @Date:   2023-10-10
 * @Version: V1.0
 */
@Data
@TableName("hc_tfstationdetail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hc_tfstationdetail对象", description="土方工作区逐桩详情")
public class HcTfstationdetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**
桩号*/
	@Excel(name = " 桩号", width = 15)
    @ApiModelProperty(value = " 桩号")
    private java.lang.String station;
	/**平均高程（m）*/
	@Excel(name = "平均高程（m）", width = 15)
    @ApiModelProperty(value = "平均高程（m）")
    private java.lang.String avgheight;
	/**平均厚度（m）*/
	@Excel(name = "平均厚度（m）", width = 15)
    @ApiModelProperty(value = "平均厚度（m）")
    private java.lang.String avgthick;
	/**平均碾压遍数*/
	@Excel(name = "平均碾压遍数", width = 15)
    @ApiModelProperty(value = "平均碾压遍数")
    private java.lang.String avgtimes;
	/**平均碾压速度（km/h）*/
	@Excel(name = "平均碾压速度（km/h）", width = 15)
    @ApiModelProperty(value = "平均碾压速度（km/h）")
    private java.lang.String avgspeed;
	/**平均振碾遍数*/
	@Excel(name = "平均振碾遍数", width = 15)
    @ApiModelProperty(value = "平均振碾遍数")
    private java.lang.String avgvibratetimes;
    private java.lang.String blockid;
    private java.lang.String sectionid;
    private java.lang.String hetong;
}
