package com.trtm.iot.entityprogresscheck.service;

import com.trtm.iot.entityprogresscheck.entity.EntityCheckDetial;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 实体进度清单子表
 * @Author: jeecg-boot
 * @Date:   2022-07-01
 * @Version: V1.0
 */
public interface IEntityCheckDetialService extends IService<EntityCheckDetial> {

	public List<EntityCheckDetial> selectByMainId(String mainId);
}
