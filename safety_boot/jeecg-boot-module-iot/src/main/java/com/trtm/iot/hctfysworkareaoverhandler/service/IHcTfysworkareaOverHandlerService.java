package com.trtm.iot.hctfysworkareaoverhandler.service;

import com.trtm.iot.hctfysworkareaoverhandler.entity.HcTfysworkareaOverHandler;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: hc_tfysworkarea_over_handler
 * @Author: jeecg-boot
 * @Date:   2023-12-06
 * @Version: V1.0
 */
public interface IHcTfysworkareaOverHandlerService extends IService<HcTfysworkareaOverHandler> {

    HcTfysworkareaOverHandler selectone(String baseid);
}
