package com.trtm.iot.yclud.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "", description = "")
public class GuiGeXingHao implements Serializable {
    private static final long serialVersionUID = 1L;


    private String GuiGeXingHao;
    private List pici;

}
