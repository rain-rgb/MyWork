package com.trtm.iot.yclcl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.yclcl.entity.Wzyclchuchanggb;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: wzyclchuchanggb
 * @Author: jeecg-boot
 * @Date:   2021-05-26
 * @Version: V1.0
 */
public interface WzyclchuchanggbMapper extends BaseMapper<Wzyclchuchanggb> {

    List<Wzyclchuchanggb> selecones(int istongji, Integer curid);

    Integer updateWzliaocangljshiyongByGuid(@Param("num") Double num,@Param("guid") String guid);

     List<Wzyclchuchanggb> slistrqid(String shebeilist, Integer curid);
}
