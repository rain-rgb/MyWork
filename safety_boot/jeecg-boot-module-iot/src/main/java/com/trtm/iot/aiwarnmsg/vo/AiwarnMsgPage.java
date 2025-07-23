package com.trtm.iot.aiwarnmsg.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class AiwarnMsgPage {
    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**设备编号*/
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
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
    /**设备id 关联表shebei_info的id*/
    @Excel(name = "设备id 关联表shebei_info的id", width = 15)
    @ApiModelProperty(value = "设备id 关联表shebei_info的id")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeiid;
}
