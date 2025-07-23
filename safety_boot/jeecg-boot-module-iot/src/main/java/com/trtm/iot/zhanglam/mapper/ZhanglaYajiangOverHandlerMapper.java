package com.trtm.iot.zhanglam.mapper;

import java.util.List;

import com.trtm.iot.zhanglam.entity.ZhanglaYajiangOverHandler;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: zhangla_yajiang_over_handler
 * @Author: jeecg-boot
 * @Date:   2021-12-27
 * @Version: V1.0
 */
public interface ZhanglaYajiangOverHandlerMapper extends BaseMapper<ZhanglaYajiangOverHandler> {

    List<ZhanglaYajiangOverHandler> listToday(String shebeilist);

    String dataById(String baseid);
    String dataById2(String baseid, String holeid);

    int BhzchuZhiAddById(String wtyy, String clfs, String cljg, String baseid, String bizPath, String chuzhiren, int status, String holeid);

    int BhzchuZhiUpdateById(String wtyy, String clfs, String cljg, String baseid, String bizPath, String chuzhiren, int status, String holeid);

    int BhzShBoHuiAddById(String jlbh, String baseid, String people, int status, String holeid);

    int BhzShBoHuiUpdateById(String jlbh, String baseid, String people, int status, String holeid);

    int BhzSHAddById(String spyj, String spjg, String baseid, String bizPath, String shenpiren, int status, String holeid);

    int BhzSHUpdateById(String spyj, String spjg, String baseid, String bizPath, String shenpiren, int status, String holeid);

    int BhzZHBSHAddById(String zhbyj, String zhbjg, String baseid, String bizPath, String shenpiren, int status, String holeid);

    int BhzZHBSHUpdateById(String zhbyj, String zhbjg, String baseid, String bizPath, String shenpiren, int status, String holeid);

    int BhzZHBBoHuiAddById(String zhbbh, String baseid, String shenpiren, int status, String holeid);

    int BhzZHBBoHuiUpdateById(String zhbbh, String baseid, String shenpiren, int status, String holeid);
}
