package com.trtm.iot.lqbhz.service;

import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.lqbhz.entity.BhzLqCailiao;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 沥青子表信息
 * @Author: jeecg-boot
 * @Date:   2021-02-22
 * @Version: V1.0
 */
public interface IBhzLqCailiaoService extends IService<BhzLqCailiao> {

	public List<BhzLqCailiao> selectByMainId(String mainId);

	/**
	 * 根据唯一id查询砼拌合站的材料
	 * @param guid
	 * @return
	 */
	List<BhzLqCailiao> selectcailiaolist(String guid);
	/**
	 * 根据id修改理论配比
	 */
	void updateLilunpb(Double liunpb , int id);
	/**
	 * 修改拌合机base表中的每盘数据的超标等级
	 */
	void updateOver_level(Integer id , int chaobiaodengji);


    List<BhzCementDetail> selectdetail(String id);

    List<BhzLqCailiao> selectByGuid(String guid);

	List<BhzLqCailiao>  getcailiaoList(String chuliaoshijian_begin, String chuliaoshijian_end, String hunheliaoid, String projectName, String shebeibianhao);
}
