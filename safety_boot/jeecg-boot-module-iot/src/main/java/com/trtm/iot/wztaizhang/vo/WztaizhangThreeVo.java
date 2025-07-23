package com.trtm.iot.wztaizhang.vo;

import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

import java.io.Serializable;
import java.util.Date;

@Data
public class WztaizhangThreeVo implements Serializable {

    private Integer id;
    private String typeName;
    private String gsName;
    private String projName;
    private String bdName;
    private String cailiaono;
    private String guigexinghao;
    private String pici;
    private String jinchangshijian;
    private String jingzhongt;
    private Date createTime;
    private Integer overproofStatus;

}
