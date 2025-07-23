package com.trtm.iot.hntbhz.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class BhzCementJhdcVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String shebeiNo;
    private String projectName;
    private String poureLocation;
    private String strengthRank;
    private Double estimateNumber;

    @Excel(name = "出料时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "出料时间")
    private Date productDatetime;

    private String materialeName;
    /**实际用量*/
    private Double realityNumber;
    /**理论用量*/
    private Double theoryNumber;
}
