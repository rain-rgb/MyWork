package com.trtm.iot.bhzcfg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.api.utils.StringUtils;
import com.trtm.iot.bhzcfg.entity.BhzChaobiaoCfg;
import com.trtm.iot.bhzcfg.mapper.BhzChaobiaoCfgMapper;
import com.trtm.iot.bhzcfg.service.IBhzChaobiaoCfgService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 拌合站超标配置子表
 * @Author: jeecg-boot
 * @Date:   2021-03-17
 * @Version: V1.0
 */
@Service
public class BhzChaobiaoCfgServiceImpl extends ServiceImpl<BhzChaobiaoCfgMapper, BhzChaobiaoCfg> implements IBhzChaobiaoCfgService {

	@Autowired
	private BhzChaobiaoCfgMapper bhzChaobiaoCfgMapper;

	@Override
	public List<BhzChaobiaoCfg> selectByMainIds(String mainId) {
		return bhzChaobiaoCfgMapper.selectByMainIds(mainId);
	}

	@Override
	public List<BhzChaobiaoCfg> selectchaobiaolist(String bhjno,String uid){
		try {
			QueryWrapper<BhzChaobiaoCfg> queryWrapper=new QueryWrapper<>();
			queryWrapper.eq("bhjno",bhjno);
			queryWrapper.eq(StringUtils.isNotBlank(uid),"uid",uid);
			return this.list(queryWrapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
