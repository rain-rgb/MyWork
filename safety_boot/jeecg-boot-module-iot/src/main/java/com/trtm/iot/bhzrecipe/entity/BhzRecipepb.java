package com.trtm.iot.bhzrecipe.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: 砼拌合站理论配合比主表
 * @Author: jeecg-boot
 * @Date:   2021-06-29
 * @Version: V1.0
 */
public class BhzRecipepb implements Serializable {
    private static final long serialVersionUID = 1L;


    private String phbname;
    private String cailiaono;
    private String phb;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPhbname() {
        return phbname;
    }

    public void setPhbname(String phbname) {
        this.phbname = phbname;
    }

    public String getCailiaono() {
        return cailiaono;
    }

    public void setCailiaono(String cailiaono) {
        this.cailiaono = cailiaono;
    }

    public String getPhb() {
        return phb;
    }

    public void setPhb(String phb) {
        this.phb = phb;
    }
}
