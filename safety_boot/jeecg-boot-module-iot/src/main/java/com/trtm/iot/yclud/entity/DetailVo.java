package com.trtm.iot.yclud.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.util.oConvertUtils;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "", description = "")
public class DetailVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Dict(dictTable = "ycl_cailiaodict", dicText = "cailiaoName", dicCode = "cailiaoNo")
    private String cailiaoNo;
    private String guigexinghao;
    private String jingzhongT;
    private String danwei;
    private String pici;
    private String jinchangshijian;
    @Dict(dictTable = "ycl_gongyingshang", dicText = "gongyingshangname", dicCode = "guid")
    private String gongyingshangdanweibianma;

    private String jianyanshijian;
    private String conclusion;
    private String teststatus;
    private String report;
    private String shuliang;
    private String nodeType;
    private String nodeTypeName;

    public String getShuliang() {
        if (oConvertUtils.isNotEmpty(danwei)) {
            return jingzhongT + danwei;
        } else {
            return jingzhongT;
        }
    }
}
