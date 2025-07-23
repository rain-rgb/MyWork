package com.trtm.iot.zhiliangrenwudan.vo;

import com.trtm.iot.zhilianggongxu.entity.ZhiliangGongxu;
import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="ZhiliangRWD", description="制梁生产线信息")
public class ZhiliangRWD {
    private Integer station;
    private String xuhao;
    private String stationname;
    private String stationstatus;
    private Zhiliangrenwudan zhiliangrenwudan;
    private List<ZhiliangGongxu> zhiliangGongxuList;
}
