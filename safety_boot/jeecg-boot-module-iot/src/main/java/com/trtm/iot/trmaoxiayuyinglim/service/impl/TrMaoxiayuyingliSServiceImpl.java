package com.trtm.iot.trmaoxiayuyinglim.service.impl;

import com.trtm.iot.trmaoxiayuyinglim.entity.TrMaoxiayuyingliS;
import com.trtm.iot.trmaoxiayuyinglim.mapper.TrMaoxiayuyingliSMapper;
import com.trtm.iot.trmaoxiayuyinglim.service.ITrMaoxiayuyingliSService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 锚下预应力张拉子表
 * @Author: jeecg-boot
 * @Date:   2024-03-12
 * @Version: V1.0
 */
@Service
public class TrMaoxiayuyingliSServiceImpl extends ServiceImpl<TrMaoxiayuyingliSMapper, TrMaoxiayuyingliS> implements ITrMaoxiayuyingliSService {
	
	@Autowired
	private TrMaoxiayuyingliSMapper trMaoxiayuyingliSMapper;
	
	@Override
	public List<TrMaoxiayuyingliS> selectByMainId(String mainId) {
		return trMaoxiayuyingliSMapper.selectByMainId(mainId);
	}
}
