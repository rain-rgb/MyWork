package com.trtm.iot.zlpz.entity;

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
 * @Description: 智慧用电历史数据统计表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-02
 * @Version: V1.0
 */
@Data
@TableName("zlpzpeizhi")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="zlpzpeizhi对象", description="浙路品质设备配置")
public class Zlpz implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String shebeino;
	/**电压超标总数*/
	@Excel(name = "项目", width = 15)
    @ApiModelProperty(value = "项目")
    private String project;
	/**频率超标总数*/
	@Excel(name = "标段", width = 15)
    @ApiModelProperty(value = "标段")
    private String biaoduan;
	@Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id")
    private String shebeiid;
    private String shebeiname;
    private String sbtype;
    private String orgcode;
}
