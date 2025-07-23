package com.trtm.iot.hntbhz.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.iot.hntbhz.vo.BhzCementDetailRC;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.poi.hslf.record.Record;

/**
 * @Description: 拌合站子表材料信息
 * @Author: jeecg-boot
 * @Date:   2021-02-05
 * @Version: V1.0
 */
public interface BhzCementDetailMapper extends BaseMapper<BhzCementDetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);

    @Select("select * from bhz_cement_detail  where  bhz_cement_detail.batch_no=#{mainId}")
	public List<BhzCementDetail> selectByMainId(@Param("mainId") String mainId);

	@Select("select bhz_cement_base.*,bhz_cement_over_handler.overproof_status from " +
			"bhz_cement_base join bhz_cement_over_handler where bhz_cement_base.batch_no=bhz_cement_over_handler.baseId limit #{pageNo},#{pageSize}")
	public List querySysCementListByUserId(Integer pageNo,Integer pageSize);

	public List<BhzCementDetail> selectcementlist(String batchNo);

	@Select("select * from bhz_cement_detail where batch_no = #{batchNo} and materiale_over_level != 0")
	List<BhzCementDetail> selectcementlist1(String batchNo);

    List<BhzCementDetailRC> selectcementlists(String batchNo);

	List<Map> selectByBatchList(@Param("list") List<String> list);
}
