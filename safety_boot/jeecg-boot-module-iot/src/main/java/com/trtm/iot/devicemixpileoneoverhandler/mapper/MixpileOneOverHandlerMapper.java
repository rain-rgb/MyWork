package com.trtm.iot.devicemixpileoneoverhandler.mapper;

import com.trtm.iot.devicemixpileoneoverhandler.entity.MixpileOneOverHandler;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 软基超标处理
 * @Author: jeecg-boot
 * @Date:   2021-12-31
 * @Version: V1.0
 */
public interface MixpileOneOverHandlerMapper extends BaseMapper<MixpileOneOverHandler> {
    /**
     * 根据id查询判断是否存在审核或者处理数据
     */
    public String dataById(String hntbatch);

    int GZchuZhiAddById(String wtyy, String clfs, String cljg, String hntbatch, String bizPath, String chuzhiren, int status);

    int GZchuZhiUpdateById(String wtyy, String clfs, String cljg, String hntbatch, String bizPath, String chuzhiren, int status);

    int GZSHAddById(String spyj, String spjg, String hntbatch, String bizPath, String shenpiren, int status);

    int GZSHUpdateById(String spyj, String spjg, String hntbatch, String bizPath, String shenpiren, int status);

    int GZZHBSHAddById(String zhbyj, String zhbjg, String hntbatch, String bizPath, String shenpiren, int status);

    int GZZHBSHUpdateById(String zhbyj, String zhbjg, String hntbatch, String bizPath, String shenpiren, int status);

    int GZShBoHuiAddById(String jlbh, String hntbatch, String people, int status);

    int GZShBoHuiUpdateById(String jlbh, String hntbatch,  String people, int status);

    int GZZHBBoHuiAddById(String zhbbh, String hntbatch,   String shenpiren, int status);

    int GZZHBBoHuiUpdateById(String zhbbh, String hntbatch, String shenpiren, int status);
}
