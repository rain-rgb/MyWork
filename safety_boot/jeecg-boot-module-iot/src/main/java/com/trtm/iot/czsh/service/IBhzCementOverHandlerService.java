package com.trtm.iot.czsh.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 拌合站处置审核信息
 * @Author: jeecg-boot
 * @Date:   2021-03-19
 * @Version: V1.0
 */
public interface IBhzCementOverHandlerService extends IService<BhzCementOverHandler> {

	/**
	 * 添加一对多
	 *
	 */
	public void saveMain(BhzCementOverHandler bhzCementOverHandler) ;

	/**
	 * 修改一对多
	 *
	 */
	public void updateMain(BhzCementOverHandler bhzCementOverHandler);

	/**
	 * 删除一对多
	 */
	public void delMain(String id);

	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);
	/**
	 * 根据id添加或者修改审核信息
	 */
	public int shenheAddOrUpdate(String spyj, String spjg, String hntbatch, String bizPath, String shenpiren);
	/**
	 * 根据id添加或者修改处置信息
	 */
	public int chuZhiAddOrUpDate(String wtyy, String clfs, String cljg, String hntbatch, String bizPath, String chuzhiren);

	public BhzCementOverHandler selectlist(String baseid);


	Page<BhzCementOverHandler> selectlist1(String baseid);

	List<BhzCementOverHandler> listToday(String shebeilist);

	Integer BhzCZAddOrUpDate(String wtyy, String clfs, String cljg, String hntbatch, String bizPath, String chuzhiren, int status);
	Integer supervisorAddOrUpdate(String spyj, String spjg, String hntbatch, String bizPath, String shenpiren, int status);
	Integer headquartersAddOrUpdate(String zhbyj, String zhbjg, String hntbatch, String bizPath, String chuzhiren, int status);

    Integer supervisorBohui(String jlbh, String hntbatch, String people, int i);

	Integer headquartersBohui(String zhbbh, String hntbatch,  String people, int i);
}
