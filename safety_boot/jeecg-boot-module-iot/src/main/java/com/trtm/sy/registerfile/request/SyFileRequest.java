package com.trtm.sy.registerfile.request;

import com.trtm.sy.registermodules.core.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class SyFileRequest extends BaseRequest {

    /**
     * 附件
     */
    @ApiModelProperty(value = "主键ID")
    private String fjid;

    @ApiModelProperty(value = "附件名称")
    private String fjmc;

    @ApiModelProperty(value = "附件路径")
    private String fjlj;

    @ApiModelProperty(value = "附件类型")
    private String fjlx;

    @ApiModelProperty(value = "附件标识")
    private String fjbs;

    @ApiModelProperty(value = "附件所属业务ID")
    private String fjfjdid;

    @ApiModelProperty(value = "版本号")
    private String version;

    /**
     * 目录
     */
    @ApiModelProperty(value = "文件目录ID")
    private String wjmlid;

}
