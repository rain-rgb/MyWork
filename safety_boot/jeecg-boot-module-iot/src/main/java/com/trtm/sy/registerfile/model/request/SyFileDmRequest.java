package com.trtm.sy.registerfile.model.request;

import com.trtm.sy.registermodules.core.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class SyFileDmRequest extends BaseRequest {

    @ApiModelProperty(value = "主键ID")
    private String fdmid;

    @ApiModelProperty(value = "文件目录ID")
    private String wjmlid;

    @ApiModelProperty(value = "附件id")
    private String fjid;


    @ApiModelProperty(value = "版本号")
    private String version;

    // 规程文件相关
    @ApiModelProperty(value = "规程名称")
    private String gcmc;

    @ApiModelProperty(value = "颁布号")
    private String bbh;

    @ApiModelProperty(value = "执行日期")
    private String zxrq;

    @ApiModelProperty(value = "过期日期")
    private String gqrq;
}
