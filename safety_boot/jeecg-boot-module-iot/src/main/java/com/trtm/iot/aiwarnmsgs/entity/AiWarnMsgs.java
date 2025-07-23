package com.trtm.iot.aiwarnmsgs.entity;

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
 * @Description: ai_warn_msgs
 * @Author: jeecg-boot
 * @Date:   2022-03-24
 * @Version: V1.0
 */
@Data
@TableName("ai_warn_msgs")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ai_warn_msgs对象", description="ai_warn_msgs")
public class AiWarnMsgs implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**报警唯一码*/
	@Excel(name = "报警唯一码", width = 15)
    @ApiModelProperty(value = "报警唯一码")
    private java.lang.String traceId;
	/**设备编号*/
	@Excel(name = "任务id", width = 15)
    @ApiModelProperty(value = "任务id")
    private java.lang.String cid;
	/**摄像头名称（报警工点）*/
	@Excel(name = "摄像头名称（报警工点）", width = 15)
    @ApiModelProperty(value = "摄像头名称（报警工点）")
    private java.lang.String cname;
	/**报警时间*/
	@Excel(name = "报警时间", width = 15)
    @ApiModelProperty(value = "报警时间")
    private java.lang.String warntime;
	/**报警内容*/
	@Excel(name = "报警内容", width = 15)
    @ApiModelProperty(value = "报警内容")
    private java.lang.String warnmsg;
	/**报警类别*/
	@Excel(name = "报警类别", width = 15)
    @ApiModelProperty(value = "报警类别")
    private java.lang.String algtype;
	/**图片，多张用，分隔*/
	@Excel(name = "图片，多张用，分隔", width = 15)
    @ApiModelProperty(value = "图片，多张用，分隔")
    private java.lang.String picurls;
	/**警报产生者*/
	@Excel(name = "警报产生者", width = 15)
    @ApiModelProperty(value = "警报产生者")
    private java.lang.String warnent;
	/**产生者类别*/
	@Excel(name = "产生者类别", width = 15)
    @ApiModelProperty(value = "产生者类别")
    private java.lang.String enttype;
	/**设备id 关联表shebei_info的id*/
	@Excel(name = "设备id 关联表shebei_info的id", width = 15)
    @ApiModelProperty(value = "设备id 关联表shebei_info的id")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeiid;
}
