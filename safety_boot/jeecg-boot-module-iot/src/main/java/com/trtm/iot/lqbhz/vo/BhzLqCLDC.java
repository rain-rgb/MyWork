package com.trtm.iot.lqbhz.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class BhzLqCLDC {


    private Integer id;
    private String sbname;
    private String cailiaoming;
    private String hunheliaoid;
    private String zongchanliang;
    private String projectName;
    private String theoryNumber;
    private String shijiyongliang;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSbname() {
        return sbname;
    }

    public void setSbname(String sbname) {
        this.sbname = sbname;
    }

    public String getCailiaoming() {
        return cailiaoming;
    }

    public void setCailiaoming(String cailiaoming) {
        this.cailiaoming = cailiaoming;
    }

    public String getHunheliaoid() {
        return hunheliaoid;
    }

    public void setHunheliaoid(String hunheliaoid) {
        this.hunheliaoid = hunheliaoid;
    }

    public String getZongchanliang() {
        return zongchanliang;
    }

    public void setZongchanliang(String zongchanliang) {
        this.zongchanliang = zongchanliang;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTheoryNumber() {
        return theoryNumber;
    }

    public void setTheoryNumber(String theoryNumber) {
        this.theoryNumber = theoryNumber;
    }

    public String getShijiyongliang() {
        return shijiyongliang;
    }

    public void setShijiyongliang(String shijiyongliang) {
        this.shijiyongliang = shijiyongliang;
    }
}
