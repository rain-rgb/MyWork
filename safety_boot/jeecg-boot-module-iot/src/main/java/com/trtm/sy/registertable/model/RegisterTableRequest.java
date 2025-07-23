package com.trtm.sy.registertable.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "注册上传表格请求实体")
public class RegisterTableRequest implements Serializable {

    private Integer id;
    private String row;
    private String col;

}
