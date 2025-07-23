package com.trtm.iot.hnthtconsign.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.hnthtconsign.entity.HnthtConsign;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 检测试验任务单下发信息表
 * @Author: jeecg-boot
 * @Date:   2021-07-09
 * @Version: V1.0
 */
public interface HnthtConsignMapper extends BaseMapper<HnthtConsign> {

    List<HnthtConsign> selectPushList(String shebeiNo, Integer pushStatus);
}
