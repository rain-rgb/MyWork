package com.trtm.iot.hntbhz.service;

import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.hntbhz.entity.BhzCementWarnVO;
import com.trtm.iot.hntbhz.vo.BhzCementBaseCbTongji;
import com.trtm.iot.hntbhz.vo.BhzCementBaseRC;
import com.trtm.iot.hntbhz.vo.BhzCementTongJi;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 拌合站主表
 * @Author: jeecg-boot
 * @Date: 2021-02-05
 * @Version: V1.0
 */
public interface IBhzCementBaseService extends IService<BhzCementBase> {

    /**
     * 添加一对多
     */
    public void saveMain(BhzCementBase bhzCementBase, List<BhzCementDetail> bhzCementDetailList);

    /**
     * 添加一对多
     */
    public void saveMains(BhzCementBase bhzCementBase, List<BhzCementDetail> bhzCementDetailList, BhzCementOverHandler bhzCementOverHandler);

    /**
     * 修改一对多
     */
    public void updateMain(BhzCementBase bhzCementBase, List<BhzCementDetail> bhzCementDetailList);

    /**
     * 删除一对多
     */
    public void delMain(String id);

    /**
     * 批量删除一对多
     */
    public void delBatchMain(Collection<? extends Serializable> idList);

    /**
     * 根据条件查出相对应的数据
     *
     * @param id
     * @param alertstate
     * @return
     */
    List<BhzCementBase> selecthntbhzone(Integer id, Integer alertstate);


    /**
     * 根据条件查出相对应的数据  修改成一次查询一条
     *
     * @param id
     * @param alertstate
     * @return
     */
    List<BhzCementBase> selecthntbhzones(Integer id, Integer alertstate);

    List<BhzCementBase> selecthntbhzonesx(Integer id, Integer alertstate);


    List<BhzCementBase> selecthntbhzonesstatistics(Integer id, Integer statistics);

    /**
     * 根据条件查出相对应的数据
     *
     * @param id
     * @param
     * @return
     */
    List<BhzCementBase> selecthntbhzList1(Integer id, Integer renwudanstatus);

    /**
     * 根据条件查出相对应的数据
     *
     * @param id
     * @param alertstate
     * @return
     */
    List<BhzCementBase> selecthntbhzList(Integer id, Integer alertstate, List<String> shebeiNo);


    /**
     * 根据条件查出相对应的数据
     *
     * @param
     * @param alertstate
     * @return
     */
    List<BhzCementBase> selecthntbhzChaobiaoList(Integer alertstate, Integer overLevel);

    /**
     * 根据条件查出相对应的数据
     *
     * @param id
     * @param alertstate
     * @return
     */
    List<BhzCementBase> selecthntbhzcbList(Integer id, Integer alertstate, String shebeiNo, Integer overLevel);

    /**
     * 根据唯一标识去修改状态
     *
     * @param batch_no
     * @param alertstate
     * @return
     */
    int updatehntbhzone(String batch_no, Integer alertstate);

    int updatehntbhzonestatics(String batch_no, Integer statistics);

    /**
     * 根据唯一标识去修改状态
     *
     * @param batch_no
     * @param renwudanstatus
     * @return
     */
    int updatehntbhzrenwustatus(String batch_no, Integer renwudanstatus);

    /**
     * 砼拌合站原材料消耗统计
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<Map> ycltjlist(Integer pageNo, Integer pageSize);


    List<Map> ycltjlists(String shebeilist, Integer pageNo, Integer pageSize);

    List<Map> ycltjliststatic(String shebeilist, Integer pageNo, Integer pageSize);

    List<BhzCementBase> selectbhzList(Integer curid);

    BhzCementBase queryone(String shebeiNo);

    Map<String, Object> bulletin();

    List<Map> bhzcbv(Integer result, List<String> shebeilist);

    List<Map> bhzcailiaoCount(Integer result, List<String> shebeilist);

    /**
     * @param shebeiNo
     * @param id
     * @return
     */
    List<BhzCementBase> selectLists(List<String> shebeiNo, Integer id);

    List<BhzCementBase> selectLists1(String shebeiNo, Integer id);

    List<BhzCementBase> selectListszt(String shebeiNo, Integer id);

    List<BhzCementBase> selectLists2(String shebeiNo, Integer id);

    List<BhzCementBase> selectListskz4(String shebeiNo, Integer id);

    List<BhzCementBase> selectListsyb(String shebeiNo, Integer id);

    List<BhzCementBase> selectListsrcsms(String shebeiNo);

    List<BhzCementBase> selectLists1b(String shebeiNo, Integer id);

    List<BhzCementBase> selectListssutai(String shebeiNo, Integer id);

    List<BhzCementBase> selectListyjqs(String shebeiNo, Integer id);

    List<BhzCementBase> selectListsbhg(String shebeiNo, Integer id);

    List<BhzCementBase> selectListsbhgty(String shebeiNo, Integer id);

    List<BhzCementBase> selectListsbhgbhty(String shebeiNo, Integer id);

    List<BhzCementBase> selectListsbhgsg(String shebeiNo, Integer id);

    List<BhzCementBase> selecthntbhzlists(Integer curid, int i, String strsToList);

    BhzCementBase queryones(String shebeiNo);

    List<BhzCementBase> selectListdata(String shebeilist, Integer curid);

    List<BhzCementBase> selectListstbim(String shebeilist, Integer curid);

    List<BhzCementBase> selectListToSHYJ(String shebeilist, Integer curid);

    List<BhzCementBase> selectHntbhzList(Integer curid, int i, String strsToList1);

    List<BhzCementBase> selecthntbhzonesstatistics1(Integer curid, int statistics, Integer curdate);

    List<Map<String, Object>> getList(String start, String end, List<String> list,String strengthRank);

    BhzCementWarnVO selectWranCount(String orgCode);

    List<BhzCementWarnVO>  selectWranCountByorgcde(String orgCode);

    List<BhzCementWarnVO> selectWranCountByshebeino(String sys_org_code, int i);

    List<BhzCementWarnVO> selectBiaoduanByshebeino(String sysOrgCode);

    List<String> selectByCode(String orgCode);

    Integer selectBiheCount(String orgCode);

    List<BhzCementBaseRC> selectTongjiData(String shebeiList);

    List<BhzCementBase> selectcailiao(Integer id, Integer statistics);

    void selectcailiaostatics(String batchNo, int i);

    String selectName(String handlePerson);

    BhzCementBaseCbTongji selectCbTongji(String shebeilist);

    List<BhzCementBaseCbTongji> selectshebeiBybiaoduan(String sys_depart_orgcode);

    List<String> selectshebeiByCode(String sys_depart_orgcode);

    List<Map> getDataToInitPM(String code,Integer lastId);

    List<BhzCementBase> getBatchNoByOrgCode(String code, Integer lastId);

    List<BhzCementBase> selectHntbhzList2(Integer curid, int i, String shebeilist);

    List<BhzCementBase> getBHAllDataToUpload(Integer lastId,List<String> list);

    List<BhzCementBase> selecthntbhzRC(Integer curid, int i);

    List<BhzCementBase> selecthntbhzbhRC(int cbStatistics, int bhStatus);

    String selectNameByCode(String orgCode);

    List<BhzCementBase> selectBhzChao(int id,int overproofStatus, String format);

    List<BhzCementBase> selectBhzChao2( int overproofStatus, String format, int isorder, int alertstate);

    String selectbyorgcode(String sysOrgCode, int i);

    List<BhzCementBase> selectByClientNo(String danhao);

    Integer findXiangMuZSs(List<String> querySheBeiList, String time);

    Integer findYuJingSs(List<String> querySheBeiList, String time);

    Integer findBiHeSs(List<String> querySheBeiList, String time);

    List<BhzCementBase> selectByCheFlag(String shebeilist, Integer cheflag, Integer alertstate, Integer curid);

    List<BhzCementBase> selectRCJHJD(Integer curid, String shebeilist);

    String getSbname(String sbjno);

    List<BhzCementBase> selectBHZUnifiedProcess(Integer curid, Integer alertstate, List<String> shebeilist, Integer overLevel);

    Map selectSYSdepartId(String sysOrgCode);

    List<BhzCementBase> selectHntbhzListBySTFour(Integer curid, int i, String shebeilist);

    Integer getCarCount(BhzCementBase bhzCementBase);

    List<BhzCementBase> selectSCData(Integer curid, String shebeilist);

//    List<Map<Object, Object>> selectCaiLiaoUse(String shebeiNo);
    List<BhzCementTongJi> selectCaiLiaoUse(String shebeiNo,String start,String end);

    List<BhzCementBase> queryByPhb(String code);

    Map selectwarnContent(String bhz_cement_base, String sbjno);

    List<Map<String, Object>> getQddj(String sysDepartOrgcode);

    List<BhzCementBase> selecerenwudanpsgx(List<String> strings, String productDatetime_begin, String productDatetime_end);

    List<String> selecerenw(String shebeiNo, String productDatetime_begin, String productDatetime_end);

    List<String> selecerenws(List<String> strings, String productDatetime_begin, String productDatetime_end);

    List<BhzCementBase> selectListjhjd(String shebeilist);

    String  getLastPhb(List<String> shebeiNolist);
}
