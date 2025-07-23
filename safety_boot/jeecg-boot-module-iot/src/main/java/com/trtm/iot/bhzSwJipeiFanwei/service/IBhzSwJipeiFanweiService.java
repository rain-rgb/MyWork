package com.trtm.iot.bhzSwJipeiFanwei.service;

import com.trtm.iot.bhzSwJipeiFanwei.entity.BhzSwJipeiFanwei;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: bhz_sw_jipei_fanwei
 * @Author: jeecg-boot
 * @Date:   2023-06-14
 * @Version: V1.0
 */
public interface IBhzSwJipeiFanweiService extends IService<BhzSwJipeiFanwei> {

    BhzSwJipeiFanwei selectone(String shebeibianhao, String formulaNo);


    List<BhzSwJipeiFanwei> selectlist(String shebeilist, Integer curid);
}
