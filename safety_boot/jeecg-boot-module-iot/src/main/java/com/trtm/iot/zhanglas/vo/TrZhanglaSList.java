package com.trtm.iot.zhanglas.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * \* @author: Xx
 * \* Date: 2021/10/21
 * \* Time: 14:05
 * \* Description:
 * \
 */
@Data
@ApiModel(value="tr_zhangla_s对象", description="张拉信息子表")
public class TrZhanglaSList {

    private List<TrZhanglaS> trZhanglaS;
}
