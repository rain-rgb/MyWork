package com.trtm.iot.devicemixpileoneoverhandler.service;

import com.trtm.iot.devicemixpileoneoverhandler.entity.MixpileOneOverHandler;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 软基超标处理
 * @Author: jeecg-boot
 * @Date:   2021-12-31
 * @Version: V1.0
 */
public interface IMixpileOneOverHandlerService extends IService<MixpileOneOverHandler> {
    Integer GZCZAddOrUpDate(String wtyy, String clfs, String cljg, String hntbatch, String bizPath, String chuzhiren, int status);
    Integer supervisorAddOrUpdate(String spyj, String spjg, String hntbatch, String bizPath, String shenpiren, int status);
    Integer headquartersAddOrUpdate(String zhbyj, String zhbjg, String hntbatch, String bizPath, String chuzhiren, int status);

    Integer supervisorBohui(String jlbh, String hntbatch, String people, int i);

    Integer headquartersBohui(String zhbbh, String hntbatch,  String people, int i);
}
