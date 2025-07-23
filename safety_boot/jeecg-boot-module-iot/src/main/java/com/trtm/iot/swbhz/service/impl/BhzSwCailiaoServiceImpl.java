package com.trtm.iot.swbhz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.swbhz.entity.BhzSwCailiao;
import com.trtm.iot.swbhz.mapper.BhzSwCailiaoMapper;
import com.trtm.iot.swbhz.service.IBhzSwCailiaoService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 水稳材料表信息
 * @Author: jeecg-boot
 * @Date:   2021-02-24
 * @Version: V1.0
 */
@Service
public class BhzSwCailiaoServiceImpl extends ServiceImpl<BhzSwCailiaoMapper, BhzSwCailiao> implements IBhzSwCailiaoService {
	
	@Autowired
	private BhzSwCailiaoMapper bhzSwCailiaoMapper;
	
	@Override
	public List<BhzSwCailiao> selectByMainId(String mainId) {
		return bhzSwCailiaoMapper.selectByMainId(mainId);
	}

	@Override
	public List<BhzSwCailiao> selectswbhzcailiao(String baseGuid) {
		try {
			QueryWrapper<BhzSwCailiao> queryWrapper=new QueryWrapper<>();
			queryWrapper.eq("base_guid",baseGuid);
			return this.list(queryWrapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    @Override
    public List<BhzCementDetail> selectdetail(String id) {
		return bhzSwCailiaoMapper.selectdetail(id);
    }
}
