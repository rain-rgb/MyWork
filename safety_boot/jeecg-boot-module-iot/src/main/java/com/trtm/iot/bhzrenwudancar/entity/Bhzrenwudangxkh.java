package com.trtm.iot.bhzrenwudancar.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 中铁一局拌合站生产流向
 * @Author: jeecg-boot
 * @Date:   2023-10-24
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Bhzrenwudangxkh implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;

    /**使用单位*/
    @Excel(name = "分包单位", width = 15)
    @ApiModelProperty(value = "分包单位")
    private java.lang.String customer;
	/**单位工程*/
	@Excel(name = "单位工程", width = 15)
    @ApiModelProperty(value = "单位工程")
    private String projname;
	/**分部工程*/
	@Excel(name = "分部工程", width = 15)
    @ApiModelProperty(value = "分部工程")
    private String conspos;
	/**分项工程*/
	@Excel(name = "分项工程", width = 15)
    @ApiModelProperty(value = "分项工程")
    private String projadr;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String note;

    private java.lang.String danwei;
    private long psjc;
    private long jfje;
}
