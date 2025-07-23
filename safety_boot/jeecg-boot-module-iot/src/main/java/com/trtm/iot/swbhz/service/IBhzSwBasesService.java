package com.trtm.iot.swbhz.service;

import com.trtm.iot.hntbhz.entity.BhzCementWarnVO;
import com.trtm.iot.swbhz.entity.BhzSwCailiao;
import com.trtm.iot.swbhz.entity.BhzSwBases;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.swbhz.entity.BhzSwWarnVO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 水稳主表
 * @Author: jeecg-boot
 * @Date:   2021-02-24
 * @Version: V1.0
 */
public interface IBhzSwBasesService extends IService<BhzSwBases> {


	public void updateByGuid(BhzSwBases bhzSwBases);
	/**
	 * 添加一对多
	 *
	 */
	public void saveMain(BhzSwBases bhzSwBases,List<BhzSwCailiao> bhzSwCailiaoList) ;

	/**
	 * 修改一对多
	 *
	 */
	public void updateMain(BhzSwBases bhzSwBases,List<BhzSwCailiao> bhzSwCailiaoList);

	/**
	 * 删除一对多
	 */
	public void delMain (String id);

	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	/**
	 * 根据条件查出相对应的数据
	 * @param id
	 * @param alertstate
	 * @return
	 */
	List<BhzSwBases> selectswbhzone(Integer id,Integer alertstate);

	/**
	 * 根据条件查出相对应的数据 查询一条
	 * @param id
	 * @param alertstate
	 * @return
	 */
	List<BhzSwBases> selectswbhzones(Integer id,Integer alertstate);


	/**
	 * 根据条件查出相对应的数据
	 * @param id
	 * @param alertstate
	 * @return
	 */
	List<BhzSwBases> selectswbhzlist(Integer id,Integer alertstate,String shebeibianhao);

	/**
	 * 根据条件查出相对应的数据
	 * @param id
	 * @param alertstate
	 * @return
	 */
	List<BhzSwBases> selectswbhzcblist(Integer id,Integer alertstate,String shebeibianhao,Integer chaobiaodengji);
	/**
	 * 根据唯一标识去修改状态
	 * @param id
	 * @param alertstate
	 * @return
	 */
	int updateswbhzone(String id, Integer alertstate);

	/**
	 * 根据唯一标识去修改总量
	 * @param id
	 * @param zongchanliang
	 * @return
	 */
	int updateswbhzongliang(String id, Float zongchanliang);

	/**
	 * 根据唯一标识去修改超标等级
	 * @param id
	 * @param chaobiaodengji
	 * @return
	 */
	int updateswbhzdengji(String id, Integer chaobiaodengji);

    BhzSwWarnVO selectWranCount(String orgCode);

    List<BhzCementWarnVO> selectWranCountByorgcde(String orgCode);

    List<BhzCementWarnVO> selectWranCountByshebeino(String sys_org_code, int i);

    List<BhzCementWarnVO> selectBiaoduanByshebeino(String sysOrgCode);

    Integer selectBiheCount(String orgCode);

	List<BhzSwBases> selectList1(String shebeiNo, Integer id);

	List<BhzSwBases> selectByGuid(String guid);

	String getUnit(String shebeibianhao);

	List<BhzSwBases> selectListToJTJT(String shebeilist, Integer curid);

	List<BhzSwBases> selectListToDJ(String shebeilist, Integer curid);

    List<BhzSwBases> selectBHZUnifiedProcess(Integer curid, Integer alertstate, List<String> shebeiList, Integer overLevel);

    List<BhzSwBases> selectcbList(String shebeilist, Integer curid);

	List<BhzSwBases> selectkzlist(String shebeilist, Integer curid);

	List<BhzSwBases> selectListToTCP(String shebeilist, Integer curid);
}
