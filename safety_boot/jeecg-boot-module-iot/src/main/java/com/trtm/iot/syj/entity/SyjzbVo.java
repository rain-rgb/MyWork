package com.trtm.iot.syj.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="syjzb对象", description="syjzb")
public class SyjzbVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String SYJID;
    private String SJQD;
    private String SBBH;
    private String SYLX;
    private String SJBH;
    private String GCMC;
    private String SJCC;
    private String SJSL;
    private String QDDBZ;
    private String SYRQ;
    private String PDJG;
    private String CZRY;
    private String sbname;
    private String sysOrgCode;
    private Integer LQ;
    private String CJMC;
    private String PZBM;
    private String GCZJ;
}
