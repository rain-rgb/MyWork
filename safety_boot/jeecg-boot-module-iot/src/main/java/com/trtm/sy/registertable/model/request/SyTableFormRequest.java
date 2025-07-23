package com.trtm.sy.registertable.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SyTableFormRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer pageNo;
    private Integer pageSize;
    private String jlbbm;
    private String jlbmc;
    private String type;
}
