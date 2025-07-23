package com.trtm.iot.yclud.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ycl_usage_detail对象", description="ycl_usage_detail")
public class NodeTypePc implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nodeTypeName;
    private String nodeType;
    private List<String> pici;

}
