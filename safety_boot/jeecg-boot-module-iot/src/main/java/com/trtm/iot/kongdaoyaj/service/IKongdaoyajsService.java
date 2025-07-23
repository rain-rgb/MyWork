package com.trtm.iot.kongdaoyaj.service;

import com.trtm.iot.kongdaoyaj.entity.Kongdaoyajs;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 孔道灌浆子表
 * @Author: jeecg-boot
 * @Date:   2024-06-25
 * @Version: V1.0
 */
public interface IKongdaoyajsService extends IService<Kongdaoyajs> {

	public List<Kongdaoyajs> selectByMainId(String mainId);
}
