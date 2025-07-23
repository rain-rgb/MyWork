package com.trtm.sy.wtgl.qywtd.entity.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="返回前端选择框中的材料编号和名称", description="返回前端选择框中的材料和名称")
public class ClResponse {

    private String cailiaoNo;
    private String cailiaoName;
    private String guigexinghao;
    private String clNoAGgXH; //显示框中的数据是材料名称+规格型号

    public String getClNoAGgXH() {
        return cailiaoName + guigexinghao;
    }
}
