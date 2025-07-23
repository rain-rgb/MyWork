package com.trtm.iot.bhzSwJipeiFanwei.mapper;

import java.util.List;

import com.trtm.iot.bhzSwJipeiFanwei.entity.BhzSwJipeiFanwei;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: bhz_sw_jipei_fanwei
 * @Author: jeecg-boot
 * @Date:   2023-06-14
 * @Version: V1.0
 */
public interface BhzSwJipeiFanweiMapper extends BaseMapper<BhzSwJipeiFanwei> {

    List<BhzSwJipeiFanwei> selectlist(String shebeilist, Integer curid);
}
