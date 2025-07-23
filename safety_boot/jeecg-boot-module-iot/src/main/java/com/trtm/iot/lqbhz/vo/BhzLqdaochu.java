package com.trtm.iot.lqbhz.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class BhzLqdaochu {

    @ApiModelProperty(value = "序号")
    private Integer id;
    @ApiModelProperty(value = "设备名称")
    private String shebeiName;
    @ApiModelProperty(value = "混合料编号")
    private java.lang.String hunheliaoid;
    @ApiModelProperty(value = "总产量")
    private java.lang.Double zongchanliang;
    @ApiModelProperty(value = "工程名称")
    private java.lang.String projectName;
    @ApiModelProperty(value = "材料名称")
    private String cailiaoNames1;
    @ApiModelProperty(value = "理论用量")
    private Double liluns1;
    @ApiModelProperty(value = "实际用量")
    private Double reals1;
    private String cailiaoNames2;
    private Double liluns2;
    private Double reals2;
    private String cailiaoNames3;
    private Double liluns3;
    private Double reals3;
    private String cailiaoNames4;
    private Double liluns4;
    private Double reals4;
    private String cailiaoNames5;
    private Double liluns5;
    private Double reals5;
    private String cailiaoNames6;
    private Double liluns6;
    private Double reals6;
    private String cailiaoNamef1;
    private Double lilunf1;
    private Double realf1;
    private String cailiaoNamef2;
    private Double lilunf2;
    private Double realf2;
    private String cailiaoNamef3;
    private Double lilunf3;
    private Double realf3;
    private String cailiaoNamelq;
    private Double lilunlq;
    private Double reallq;
    private String cailiaoNamecl;
    private Double liluncl;
    private Double realcl;
    private String cailiaoNamexl;
    private Double lilunxl;
    private Double realxl;


}
