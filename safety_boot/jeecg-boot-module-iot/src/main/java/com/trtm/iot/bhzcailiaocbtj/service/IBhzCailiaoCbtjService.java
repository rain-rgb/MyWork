package com.trtm.iot.bhzcailiaocbtj.service;

import com.trtm.iot.bhzcailiaocbtj.entity.BhzCailiaoCbtj;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: bhz_cailiao_cbtj
 * @Author: jeecg-boot
 * @Date:   2022-12-06
 * @Version: V1.0
 */
public interface IBhzCailiaoCbtjService extends IService<BhzCailiaoCbtj> {

    BhzCailiaoCbtj selectclOne(String shebeiNo, Integer materialeType, String materialeName);
}
