package com.trtm.iot.lqbhz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 沥青主表
 * @Author: jeecg-boot
 * @Date:   2021-02-22
 * @Version: V1.0
 */
@ApiModel(value="bhz_lq_bases对象", description="沥青主表")
@Data
@TableName("bhz_lq_bases")
public class BhzLqBasesYSB implements Serializable {
    private static final long serialVersionUID = 1L;


	/**出料时间*/
    private String chuliaoshijian;
	/**油石比*/
    private String youshibi;
    private String guliaowd;
    private String chuliaowd;
    private String liqingwd;
    private String formulaName;

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getChuliaoshijian() {
        return chuliaoshijian;
    }

    public void setChuliaoshijian(String chuliaoshijian) {
        this.chuliaoshijian = chuliaoshijian;
    }

    public String getYoushibi() {
        return youshibi;
    }

    public void setYoushibi(String youshibi) {
        this.youshibi = youshibi;
    }

    public String getGuliaowd() {
        return guliaowd;
    }

    public void setGuliaowd(String guliaowd) {
        this.guliaowd = guliaowd;
    }

    public String getChuliaowd() {
        return chuliaowd;
    }

    public void setChuliaowd(String chuliaowd) {
        this.chuliaowd = chuliaowd;
    }

    public String getLiqingwd() {
        return liqingwd;
    }

    public void setLiqingwd(String liqingwd) {
        this.liqingwd = liqingwd;
    }
}
