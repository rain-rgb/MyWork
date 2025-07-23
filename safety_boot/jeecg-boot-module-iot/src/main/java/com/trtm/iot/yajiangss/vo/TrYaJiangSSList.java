package com.trtm.iot.yajiangss.vo;

import com.trtm.iot.yajiangss.entity.TrYajiangSS;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * \* @author: Xx
 * \* Date: 2021/10/21
 * \* Time: 14:05
 * \* Description:
 * \
 */
@Data
@ApiModel(value="tr_yajiang_s_s对象", description="压浆子表过程值")
public class TrYaJiangSSList {
    private List<TrYajiangSS> trYajiangSS;
}
