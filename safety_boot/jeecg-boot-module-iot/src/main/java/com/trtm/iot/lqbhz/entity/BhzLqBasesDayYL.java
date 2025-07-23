package com.trtm.iot.lqbhz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 沥青主表
 * @Author: jeecg-boot
 * @Date:   2021-02-22
 * @Version: V1.0
 */
@ApiModel(value="bhz_lq_bases对象", description="沥青主表")
@Data
@TableName("bhz_lq_bases")
public class BhzLqBasesDayYL implements Serializable {
    private static final long serialVersionUID = 1L;


	/**出料时间*/
    private String chuliaoshijian;
	/**配比编号*/
    private String formulaNo;
    /**配比名*/
    private String formulaName;
    /**材料名*/
    private String materialName;
    /**实际用量*/
    private String actualDosage;
    /**实际配比*/
    private String actualScale;
    private String gglx;
    private String phb;

    public String getGglx() {
        return gglx;
    }

    public void setGglx(String gglx) {
        this.gglx = gglx;
    }

    public String getPhb() {
        return phb;
    }

    public void setPhb(String phb) {
        this.phb = phb;
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

    public String getFormulaNo() {
        return formulaNo;
    }

    public void setFormulaNo(String formulaNo) {
        this.formulaNo = formulaNo;
    }

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getActualDosage() {
        return actualDosage;
    }

    public void setActualDosage(String actualDosage) {
        this.actualDosage = actualDosage;
    }

    public String getActualScale() {
        return actualScale;
    }

    public void setActualScale(String actualScale) {
        this.actualScale = actualScale;
    }
}
