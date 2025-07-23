package com.trtm.iot.aiwarnmsgs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.aiwarnmsgs.entity.AiWarnMsgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: ai_warn_msgs
 * @Author: jeecg-boot
 * @Date:   2022-03-24
 * @Version: V1.0
 */
public interface AiWarnMsgsMapper extends BaseMapper<AiWarnMsgs> {
    List getlists1(String strsToList1, Integer curid);
}
