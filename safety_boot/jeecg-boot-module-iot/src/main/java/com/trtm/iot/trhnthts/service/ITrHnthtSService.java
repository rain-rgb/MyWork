package com.trtm.iot.trhnthts.service;

import com.trtm.iot.trhnthtm.entity.TrHnthtM;
import com.trtm.iot.trhnthts.entity.TrHnthtS;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 混凝土回弹子表
 * @Author: jeecg-boot
 * @Date:   2021-09-13
 * @Version: V1.0
 */
public interface ITrHnthtSService extends IService<TrHnthtS> {

    public List<TrHnthtS> selectHntHtList(String testid);

}
