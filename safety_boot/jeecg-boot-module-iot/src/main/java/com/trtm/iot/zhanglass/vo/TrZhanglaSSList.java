package com.trtm.iot.zhanglass.vo;

import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglass.entity.TrZhanglaSS;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * \* @author: zml
 * \* Date: 2022/05/10
 * \* Time: 10:05
 * \* Description:
 * \
 */
@Data
@ApiModel(value="tr_zhangla_s_s对象", description="张拉信息子表过程数据")
public class TrZhanglaSSList {

    private List<TrZhanglaSS> trZhanglaSS;
}
