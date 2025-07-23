package com.trtm.sy.wtgl.qywtd.entity.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="指派人员取样entity", description="指派人员取样entity")
public class ZpQyRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer wztzid; //wztaizhang id
    private String wtid;
    private String zhipaiquyangren; //指派取样人
    private String zhipaiquyangrenid; //指派取样人id
    private String titCode; //试验类型，材料类型

}
