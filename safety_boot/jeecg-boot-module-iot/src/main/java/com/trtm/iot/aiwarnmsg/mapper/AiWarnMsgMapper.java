package com.trtm.iot.aiwarnmsg.mapper;

import java.util.List;

import com.trtm.iot.aiwarnmsg.entity.AiWarnMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: AI识别预警
 * @Author: jeecg-boot
 * @Date:   2022-02-16
 * @Version: V1.0
 */
public interface AiWarnMsgMapper extends BaseMapper<AiWarnMsg> {

    List<AiWarnMsg> selectByShebeiNo(String sbList, Integer curid);

    List<AiWarnMsg> selectWarnData(Integer curid, String shebeilist);
}
