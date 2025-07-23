package com.trtm.iot.aiwarnmsg.service;

import com.trtm.iot.aiwarnmsg.entity.AiWarnMsg;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: AI识别预警
 * @Author: jeecg-boot
 * @Date:   2022-02-16
 * @Version: V1.0
 */
public interface IAiWarnMsgService extends IService<AiWarnMsg> {

    List<AiWarnMsg> selectByShebeiNo(String sbList, Integer curid);

    List<AiWarnMsg> selectWarnData(Integer curid, String shebeilist);

    String selectCfgByCid(String cid);
}
