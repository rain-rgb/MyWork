package com.trtm.iot.lmjob.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: lm_sw_files
 * @Author: jeecg-boot
 * @Date:   2023-11-15
 * @Version: V1.0
 */
@Data
@TableName("upload_file")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="upload_file", description="upload_file")
public class LmUploadFiles implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**施工阶段*/
	@Excel(name = "施工阶段", width = 15)
    @ApiModelProperty(value = "施工阶段")
    private String sgjd;
    /**施工阶段*/
    @Excel(name = "施工阶段", width = 15)
    @ApiModelProperty(value = "施工阶段")
    private String sgjdValue;
	/**文件类型*/
	@Excel(name = "文件类型", width = 15)
    @ApiModelProperty(value = "文件类型")
    private String filetype;
    /**文件类型*/
    @Excel(name = "文件类型", width = 15)
    @ApiModelProperty(value = "文件类型")
    private String typeValue;
}
