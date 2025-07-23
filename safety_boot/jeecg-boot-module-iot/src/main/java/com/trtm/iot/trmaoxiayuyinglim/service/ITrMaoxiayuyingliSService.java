package com.trtm.iot.trmaoxiayuyinglim.service;

import com.trtm.iot.trmaoxiayuyinglim.entity.TrMaoxiayuyingliS;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 锚下预应力张拉子表
 * @Author: jeecg-boot
 * @Date:   2024-03-12
 * @Version: V1.0
 */
public interface ITrMaoxiayuyingliSService extends IService<TrMaoxiayuyingliS> {

	public List<TrMaoxiayuyingliS> selectByMainId(String mainId);
}
