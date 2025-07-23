package com.trtm.iot.aiwarnmsg.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: AI识别预警
 * @Author: jeecg-boot
 * @Date:   2022-02-16
 * @Version: V1.0
 */
@Data
@TableName("ai_warn_msg")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ai_warn_msg对象", description="AI识别预警")
public class AiWarnMsg implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
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

    private java.lang.String traceId;// 报警唯一id
    private java.lang.String warnent;// 报警产生者
    private java.lang.String enttype;// 报警产生者工种
//
//    private java.lang.String warmLocation;
//    private java.lang.String EventLevel;
//    private java.lang.String EventLevelValue;
//    private java.lang.String EventTypeName;



    public String getWarntime() {
        if(StringUtils.isNotBlank(warntime) && warntime.length() == 13 && StringUtils.isNumeric(warntime)){
            long timestamp = Long.parseLong(warntime);
            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(date);
            return formattedDate;
        }else{
            return warntime;
        }

    }
}
