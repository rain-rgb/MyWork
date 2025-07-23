package com.trtm.sy.wtgl.qywtd.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class QuYangVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer wtid;
    private Integer wztzid;
    private String cailiaoNo;
    private String gongyingshangdanweibianma;
    private String guigexinghao;
    private String pici;
    private String jinchangshijian;
    private String zhipairen;
    private String zhipaishijian;
    private Integer delegateState;
    private Integer zhipaizhuangtai;
}
