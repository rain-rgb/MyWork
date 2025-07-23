package com.trtm.iot.zhangla.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.zhangla.entity.OverHandler;
import com.trtm.iot.zhangla.entity.TrZhangla;
import com.trtm.iot.zhangla.entity.ZhangLaDto;
import com.trtm.iot.zhanglam.entity.ZhanglaYajiangOverHandler;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: tr_zhangla
 * @Author: jeecg-boot
 * @Date:   2021-03-16
 * @Version: V1.0
 */
public interface TrZhanglaMapper extends BaseMapper<TrZhangla> {




    List<OverHandler> findOverHandler(String syjid);

    IPage<ZhangLaDto> queryDeatilss(String shebeiNo, Page<ZhangLaDto> pageQuery);
}
