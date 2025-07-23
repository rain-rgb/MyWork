package com.trtm.iot.wztaizhang.entity;

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
 * @Description: wzycl_config
 * @Author: jeecg-boot
 * @Date:   2024-08-12
 * @Version: V1.0
 */
@Data
@TableName("wzycl_config")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wzycl_config对象", description="wzycl_config")
public class WzyclConfig implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String sbjno;
	/**生产批次时间（h）*/
	@Excel(name = "生产批次时间（h）", width = 15)
    @ApiModelProperty(value = "生产批次时间（h）")
    private java.lang.Integer hour;
	/**粗集料(KG)*/
	@Excel(name = "粗集料(KG)", width = 15)
    @ApiModelProperty(value = "粗集料(KG)")
    private java.lang.Integer cujiliao;
	/**细集料(KG)*/
	@Excel(name = "细集料(KG)", width = 15)
    @ApiModelProperty(value = "细集料(KG)")
    private java.lang.Integer xijiliao;
	/**水泥(KG)*/
	@Excel(name = "水泥(KG)", width = 15)
    @ApiModelProperty(value = "水泥(KG)")
    private java.lang.Integer shuini;
	/**矿粉(KG)*/
	@Excel(name = "矿粉(KG)", width = 15)
    @ApiModelProperty(value = "矿粉(KG)")
    private java.lang.Integer kuangfeng;
	/**粉煤灰(KG)*/
	@Excel(name = "粉煤灰(KG)", width = 15)
    @ApiModelProperty(value = "粉煤灰(KG)")
    private java.lang.Integer fenmeihui;
	/**减水剂(KG)*/
	@Excel(name = "减水剂(KG)", width = 15)
    @ApiModelProperty(value = "减水剂(KG)")
    private java.lang.Integer jianshuiji;
	/**其他(KG)*/
	@Excel(name = "其他(KG)", width = 15)
    @ApiModelProperty(value = "其他(KG)")
    private java.lang.Integer other;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
}
