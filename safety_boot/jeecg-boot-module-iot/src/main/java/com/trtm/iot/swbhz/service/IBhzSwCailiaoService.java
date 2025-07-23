package com.trtm.iot.swbhz.service;

import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.swbhz.entity.BhzSwCailiao;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 水稳材料表信息
 * @Author: jeecg-boot
 * @Date:   2021-02-24
 * @Version: V1.0
 */
public interface IBhzSwCailiaoService extends IService<BhzSwCailiao> {

	public List<BhzSwCailiao> selectByMainId(String mainId);

	List<BhzSwCailiao> selectswbhzcailiao(String baseGuid);

    List<BhzCementDetail> selectdetail(String id);
}
