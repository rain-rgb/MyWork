package com.trtm.iot.wztaizhang.vo;

import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * wztaizhang标段
 */
@Data
public class WztaizhangBdVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private Integer count;
    private List<WztaizhangThreeVo> records;

}
