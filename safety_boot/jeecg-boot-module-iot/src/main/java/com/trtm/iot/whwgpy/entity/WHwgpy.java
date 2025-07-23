package com.trtm.iot.whwgpy.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: w_hwgpy
 * @Author: jeecg-boot
 * @Date:   2023-04-26
 * @Version: V1.0
 */
@Data
@TableName("w_hwgpy")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="w_hwgpy对象", description="w_hwgpy")
public class WHwgpy implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备归属*/
	@Excel(name = "设备归属", width = 15)
    @ApiModelProperty(value = "设备归属")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String customer;
	/**项目id*/
	@Excel(name = "项目id", width = 15)
    @ApiModelProperty(value = "项目id")
    private java.lang.Integer projectid;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projectname;
	/**沥青种类*/
	@Excel(name = "沥青种类", width = 15)
    @ApiModelProperty(value = "沥青种类")
    private java.lang.String product;
	/**车号*/
	@Excel(name = "车号", width = 15)
    @ApiModelProperty(value = "车号")
    private java.lang.String grade;
	/**标段*/
	@Excel(name = "标段", width = 15)
    @ApiModelProperty(value = "标段")
    private java.lang.String source;
	/**0代表匹配，1代表不匹配*/
	@Excel(name = "0代表匹配，1代表不匹配", width = 15)
    @ApiModelProperty(value = "0代表匹配，1代表不匹配")
    private java.lang.Integer result;
	/**试验的时间yyyy-MM-dd HH:mm:ss*/
	@Excel(name = "试验的时间yyyy-MM-dd HH:mm:ss", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "试验的时间yyyy-MM-dd HH:mm:ss")
    private java.util.Date datetime;
	/**试验种类‘1’代表普通沥青;’2’改性沥青；*/
	@Excel(name = "试验种类‘1’代表普通沥青;’2’改性沥青；", width = 15)
    @ApiModelProperty(value = "试验种类‘1’代表普通沥青;’2’改性沥青；")
    private java.lang.Integer type;
	/**通过率*/
	@Excel(name = "通过率", width = 15)
    @ApiModelProperty(value = "通过率")
    private java.lang.String access;
	/**Sbs掺量*/
	@Excel(name = "Sbs掺量", width = 15)
    @ApiModelProperty(value = "Sbs掺量")
    private java.lang.String sbsaccess;
	/**光谱图文件 指定文件格式csv*/
	@Excel(name = "光谱图文件 指定文件格式csv", width = 15)
    private transient java.lang.String lighfileString;

    private byte[] lighfile;

//    public byte[] getLighfile(){
//        if(lighfileString==null){
//            return null;
//        }
//        try {
//            return lighfileString.getBytes("UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public String getLighfileString(){
//        if(lighfile==null || lighfile.length==0){
//            return "";
//        }
//        try {
//            return new String(lighfile,"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
	/**isdj*/
	@Excel(name = "isdj", width = 15)
    @ApiModelProperty(value = "isdj")
    private java.lang.Integer isdj;
	/**预警原因*/
	@Excel(name = "预警原因", width = 15)
    @ApiModelProperty(value = "预警原因")
    private java.lang.String overReason;
	/**审核状态*/
	@Excel(name = "审核状态", width = 15)
    @ApiModelProperty(value = "审核状态")
    private java.lang.Integer overproofStatus;
}
