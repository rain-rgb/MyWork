package com.trtm.iot.hctfysworkareaoverhandler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hctfysworkareaoverhandler.entity.HcTfysworkareaOverHandler;
import com.trtm.iot.hctfysworkareaoverhandler.mapper.HcTfysworkareaOverHandlerMapper;
import com.trtm.iot.hctfysworkareaoverhandler.service.IHcTfysworkareaOverHandlerService;
import com.trtm.iot.syjoverhandler.entity.SyjOverHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: hc_tfysworkarea_over_handler
 * @Author: jeecg-boot
 * @Date:   2023-12-06
 * @Version: V1.0
 */
@Service
public class HcTfysworkareaOverHandlerServiceImpl extends ServiceImpl<HcTfysworkareaOverHandlerMapper, HcTfysworkareaOverHandler> implements IHcTfysworkareaOverHandlerService {

    @Autowired
    HcTfysworkareaOverHandlerMapper hcTfysworkareaOverHandlerMapper;

    @Override
    public HcTfysworkareaOverHandler selectone(String baseid) {
        try {
            QueryWrapper<HcTfysworkareaOverHandler> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("baseId",baseid);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
