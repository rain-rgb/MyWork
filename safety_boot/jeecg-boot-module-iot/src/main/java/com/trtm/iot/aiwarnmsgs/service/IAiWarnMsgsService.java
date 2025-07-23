package com.trtm.iot.aiwarnmsgs.service;

import com.trtm.iot.aiwarnmsgs.entity.AiWarnMsgs;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: ai_warn_msgs
 * @Author: jeecg-boot
 * @Date:   2022-03-24
 * @Version: V1.0
 */
public interface IAiWarnMsgsService extends IService<AiWarnMsgs> {

    List<AiWarnMsgs> getlists(List<String> strsToList1, Integer curid);
    List<AiWarnMsgs> getlists1(String strsToList1, Integer curid);
}
