package com.trtm.iot.lqbhz.service;

import com.trtm.iot.hntbhz.entity.BhzCementWarnVO;
import com.trtm.iot.hntbhz.vo.BhzCementBaseRC;
import com.trtm.iot.lqbhz.entity.BhzLqBasesDayYL;
import com.trtm.iot.lqbhz.entity.BhzLqCailiao;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.lqbhz.entity.BhzLqWarnVO;
import com.trtm.iot.lqbhz.vo.BhzLqCLDC;
import com.trtm.iot.lqbhz.vo.BhzLqTongJi;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 沥青主表
 * @Author: jeecg-boot
 * @Date:   2021-02-22
 * @Version: V1.0
 */
public interface IBhzLqBasesService extends IService<BhzLqBases> {

	/**
	 * 添加一对多
	 *
	 */
	public void saveMain(BhzLqBases bhzLqBases,List<BhzLqCailiao> bhzLqCailiaoList) ;

	/**
	 * 修改一对多
	 *
	 */
	public void updateMain(BhzLqBases bhzLqBases,List<BhzLqCailiao> bhzLqCailiaoList);

	/**
	 * 删除一对多
	 */
	public void delMain (String id);

	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

	/**
	 * 根据条件查出对应的数据
	 */
	List<BhzLqBases> selectlqbhzone(Integer id , Integer alertstate);

	/**
	 * 根据条件查出对应的数据  查询一条数据
	 */
	List<BhzLqBases> selectlqbhzones(Integer id , Integer alertstate);
	/**
	 * 疏港
	 */
	List<BhzLqBases> selectlqbhzonesg(Integer id , Integer alertstate, String shebeiNo);

	int updatelqbhzone(String guid, Integer alertstate);
	/**
	 * 根据信息修改沥青搅拌时间
	 */
	void updateLqOverTime(int time , String guid);

	/**
	 * 根据主表guid查询子表材料信息
	 */
     List <BhzLqCailiao> selectCailiaoList(String guid);

	/**
	 * 根据guid修改总产量
	 */
	void updateZclByGuid(String guid,Double zongchanliang);

	/**
	 * 根据guid修改油石比
	 */
	void updateYsbByGuid(String guid,String youshibi);

	/**
	 * 根据guid将理论油石比写入数据库
	 * @param lilunpb
	 * @param guid
	 */
	void updateLlysbByGuid(String lilunpb , String guid);

	void updateGcmcByGuid(String gcmc , String guid);

	void updatehunheliaoidByGuid(String hunheliaoid , String guid);
	/**
	 * 修改bhz_lq_bases的数据的状态
	 *
	 * @param id
	 * @param alertstate
	 */
	void updateBaseStatus(Integer id , int alertstate);
	/**
	 * 根据条件查出相对应的数据
	 * @param id
	 * @param alertstate
	 * @return
	 */
	List<BhzLqBases> selectlqbhzList(Integer id,Integer alertstate,String shebeiNo);

	List<BhzLqBases> selectList1(String shebeiNo,Integer id);

	List<BhzLqBases> selectListTY(String shebeiNo,Integer id);

	List<BhzLqBases> selectListSG(String shebeiNo,Integer id);

	List<BhzLqBases> selectList2(String shebeiNo,Integer id);

	List<BhzLqBases> selectListtoDJ(String shebeiNo,Integer id);

	List<BhzLqBases> selectUpdateList(String shebeiNo);

	List<BhzLqBases> selectListtoHC(String shebeiNo,Integer id);

	List<BhzLqBases> selectcbList(String shebeiNo,Integer id);

	List<BhzLqBases> selectlqbhzBysb(Integer id,  List<String> shebeiNo);

	List<BhzLqBases> selectlqbhzchaobiaoList(Integer curid, Integer alertstate, String sbjnolist);

    BhzLqBases queryone(String shebeiNo);

	BhzLqBases selectBysbno(List<String>  SBList);

    BhzLqWarnVO selectWranCount(String orgCode);

    List<BhzCementWarnVO> selectWranCountByorgcde(String orgCode);

    List<BhzCementWarnVO> selectWranCountByshebeino(String sys_org_code, int i);

	List<String> getPitchGraphData(BhzLqBases pitchBase,Integer code);

    List<BhzCementWarnVO> selectBiaoduanByshebeino(String sysOrgCode);

    Integer selectBiheCount(String orgCode);

	Map selectwarnContent(String tableName, String sbjno);

	List<Map> selectProjectList(String sys_org_code, int i);

	Double selectOverCount(List<String> shebeiList, String tableName, String sql);

	List<BhzLqBases> selectLQBHZ(Integer curid, String shebeilist);

    List<BhzLqBases> selectBHZUnifiedProcess(Integer curid, Integer alertstate, List<String> shebeiList, Integer overLevel);

	List<BhzLqBases> selectLQBHZRoad(Integer curid, String shebeilist);

    List<BhzLqBasesDayYL> getDosage(String formulaNo, String date);

    List<BhzCementBaseRC> selectTongjiData(String shebeilist);

    List<BhzLqTongJi> selectCailiaoUse(String shebei, String start, String end);

	List<BhzLqCLDC> selectdcdata(String shebeibianhao, String chuliaoshijian_begin, String chuliaoshijian_end, String projectName, String hunheliaoid);
}
