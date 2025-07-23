package com.trtm.iot.wztaizhang.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="JypWztz", description="JypWztz")
public class JypWztz implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;//wztaizhang表的id
    private String sysOrgCode;//标段
    private String pici;//系统检验批次编号
    private String cailiaoName;//材料名称
    private String guigexinghao;//规格类型
    private String gongyingshangName;//供应商单位
    private String jingzhongT;//净重
    private String jinchangshijian;//进场时间
    private String cunfangdidian;//存放地点
    private String sampleGcbw;//使用部位
    private  String zhibaodan;
    private  String shengchanpihao;// 手填生成批号
    private String danwei;
    private String lcbh;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date chuchangshijian; // 出厂时间




}
