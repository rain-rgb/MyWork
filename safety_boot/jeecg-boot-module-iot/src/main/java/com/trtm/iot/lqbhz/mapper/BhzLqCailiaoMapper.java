package com.trtm.iot.lqbhz.mapper;

import java.util.List;

import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.lqbhz.entity.BhzLqCailiao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 沥青子表信息
 * @Author: jeecg-boot
 * @Date:   2021-02-22
 * @Version: V1.0
 */
public interface BhzLqCailiaoMapper extends BaseMapper<BhzLqCailiao> {

	public boolean deleteByMainId(@Param("mainId") String mainId);

	public List<BhzLqCailiao> selectByMainId(@Param("mainId") String mainId);
	/**
	 * 修改理论配比,根据id
	 */
	@Update("update bhz_lq_cailiao set lilunpb=#{lilunpb} where id=#{id}")
	void updateLilunpb(Double lilunpb , int id);
	/**
	 * 修改拌合机base表中的每盘数据的超标等级
	 */
	@Update("update bhz_lq_bases set chaobiaodengji =#{chaobiaodengji} where id=#{id}")
	void updateOver_level(Integer id , int chaobiaodengji);


    List<BhzCementDetail> selectdetail(String id);

	List<BhzLqCailiao> getcailiaoList(String chuliaoshijian_begin, String chuliaoshijian_end, String hunheliaoid, String projectName, String shebeibianhao);
}
