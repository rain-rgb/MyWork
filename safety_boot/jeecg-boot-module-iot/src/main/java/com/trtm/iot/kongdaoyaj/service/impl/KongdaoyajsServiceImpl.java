package com.trtm.iot.kongdaoyaj.service.impl;

import com.trtm.iot.kongdaoyaj.entity.Kongdaoyajs;
import com.trtm.iot.kongdaoyaj.mapper.KongdaoyajsMapper;
import com.trtm.iot.kongdaoyaj.service.IKongdaoyajsService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 孔道灌浆子表
 * @Author: jeecg-boot
 * @Date:   2024-06-25
 * @Version: V1.0
 */
@Service
public class KongdaoyajsServiceImpl extends ServiceImpl<KongdaoyajsMapper, Kongdaoyajs> implements IKongdaoyajsService {
	
	@Autowired
	private KongdaoyajsMapper kongdaoyajsMapper;
	
	@Override
	public List<Kongdaoyajs> selectByMainId(String mainId) {
		return kongdaoyajsMapper.selectByMainId(mainId);
	}
}
