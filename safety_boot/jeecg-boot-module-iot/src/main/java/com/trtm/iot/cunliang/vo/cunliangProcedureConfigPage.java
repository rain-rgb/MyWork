package com.trtm.iot.cunliang.vo;

import com.trtm.iot.cunliang.entity.BeammanagementProcedure;
import com.trtm.iot.zhilianggongxu.entity.ZhiliangGongxu;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * \* @author: Xx
 * \* Date: 2021/8/23
 * \* Time: 15:16
 * \* Description:
 * \
 */
@Data
@ApiModel(value="cunliangProcedureConfigPage", description="存梁区信息")
public class cunliangProcedureConfigPage {

    private java.lang.String liangzuoname;

    private java.lang.String shebeino;
    private java.lang.Integer switchsta;
    private java.lang.String uuid;

    private List<ZhiliangGongxu> zhiliangGongxuList;
}
