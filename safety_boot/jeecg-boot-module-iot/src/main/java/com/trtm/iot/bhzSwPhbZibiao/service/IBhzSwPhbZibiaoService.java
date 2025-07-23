package com.trtm.iot.bhzSwPhbZibiao.service;

import com.trtm.iot.bhzSwPhbZibiao.entity.BhzSwPhbZibiao;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 水稳理论配合比子表
 * @Author: jeecg-boot
 * @Date:   2021-11-23
 * @Version: V1.0
 */
public interface IBhzSwPhbZibiaoService extends IService<BhzSwPhbZibiao> {

    BhzSwPhbZibiao selectTianjiaji(String shebeibianhao, String formulaNo, String cailiaoming);

    BhzSwPhbZibiao selectTianjiajis(String shebeibianhao, String cailiaoming);
}
