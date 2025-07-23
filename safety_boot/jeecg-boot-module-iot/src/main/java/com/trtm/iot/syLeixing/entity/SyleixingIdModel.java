package com.trtm.iot.syLeixing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SyleixingIdModel：
 * @Description TODO
 * @Author 55314
 * @Date 2022/3/9 14:37
 * @Version 1.0
 **/
public class SyleixingIdModel {
    private static final long serialVersionUID = 1L;
    private java.lang.Integer id;
    private java.lang.String cailiaoname;
    private java.lang.String cailiaono;
    private java.lang.String parentno;
    private java.lang.String nodetype;
    private java.lang.String isdel;
    @ApiModelProperty(value = "创建人")
    private java.lang.String creatpersom;
    private java.util.Date creattime;
    private java.lang.String updateperson;
    /**修改时间*/
    private java.util.Date updatetime;
    private List<SyLeixing> list;
}
