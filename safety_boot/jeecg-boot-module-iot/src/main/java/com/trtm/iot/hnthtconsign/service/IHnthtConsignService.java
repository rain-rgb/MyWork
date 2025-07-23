package com.trtm.iot.hnthtconsign.service;

import com.trtm.iot.hnthtconsign.entity.HnthtConsign;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 检测试验任务单下发信息表
 * @Author: jeecg-boot
 * @Date:   2021-07-09
 * @Version: V1.0
 */
public interface IHnthtConsignService extends IService<HnthtConsign> {

    List<HnthtConsign> queryones(String shebeichangjia, String shebeibianhao, String status);

    HnthtConsign queryone(String code);

    List<HnthtConsign> selectlist(List<String> shebeiNo,Integer id);

    List<HnthtConsign> selectPushList(String shebeiNo,Integer pushStatus);

    void updateStatus(String code);

    void updatePushStatus(HnthtConsign hnthtConsign);
}
