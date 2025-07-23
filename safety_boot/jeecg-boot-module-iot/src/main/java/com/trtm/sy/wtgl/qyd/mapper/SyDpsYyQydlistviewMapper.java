package com.trtm.sy.wtgl.qyd.mapper;

import java.util.List;
import java.util.Map;

import com.trtm.sy.wtgl.qyd.entity.SyDpsYyQydlistview;
import com.trtm.sy.wtgl.qyd.entity.response.YpdEntity;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: sy_dps_yy_qydlistview
 * @Author: jeecg-boot
 * @Date:   2023-08-25
 * @Version: V1.0
 */
public interface SyDpsYyQydlistviewMapper extends BaseMapper<SyDpsYyQydlistview> {

    List<Map<String, Object>> queryQyPic(Integer id);

    List<Map<String, Object>> querySyPic(Integer id);

    YpdEntity getQyXxById(Integer id);
}
