package com.trtm.iot.hc_constructionresults_ny.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.hc_constructionresults_ny.entity.HcConstructionresultsNy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 施工成果碾压
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
public interface HcConstructionresultsNyMapper extends BaseMapper<HcConstructionresultsNy> {

    Integer selectsum(List<String> querySheBeiList);
}
