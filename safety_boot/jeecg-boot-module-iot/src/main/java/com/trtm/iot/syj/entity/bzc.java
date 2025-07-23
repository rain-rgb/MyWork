package com.trtm.iot.syj.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @ClassName bzc：
 * @Description TODO
 * @Author 55314
 * @Date 2024/3/4 16:00
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class bzc {
    private String sbbh;
    private String sbname;
    private String lq;
    private String SJQD;
    private String qddbzavg;
    private String qddbzbzc;
    private String num;
    private String stadate;
    private String enddate;
}
