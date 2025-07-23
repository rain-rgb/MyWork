package com.trtm.iot.wztaizhang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.trtm.iot.wztaizhang.entity.JypWztz;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.wztaizhang.vo.WztaizhangKBVO;
import com.trtm.iot.yclud.entity.YclUsageDetail;
import com.trtm.sy.wtgl.qywtd.entity.QueryVo;

import java.util.List;
import java.util.Map;

/**
 * @Description: wztaizhang
 * @Author: jeecg-boot
 * @Date: 2021-06-18
 * @Version: V1.0
 */
public interface IWztaizhangService extends IService<Wztaizhang> {

    List<Map> selectPiciBylc(String lcguid);

    List<Map> selectPiciBylc2(String lcguid);

    Wztaizhang getWztaizhang(String sysOrgCode, String cailiaoNo, String gongyinsgangNo, String pici);

    int updateone(String maozhongT2, String pizhongT2, Integer id1, String jingzhongT2);

    Wztaizhang queryone(String shebeiNo, String lcNo, String cailiaoNo, String gongyinsgangNo);

    Wztaizhang selectwztaizhangone(Integer ids);

    List<Wztaizhang> selectlc(String lcNo);

    List<Wztaizhang> selectwzjypList(Integer curid, List<String> strsToList1);

    List<Map> zhongdianCailiao();

    Wztaizhang getselectlcone(String lc);

    List<Wztaizhang> selectLists(String strsToList1, Integer curid);

    Wztaizhang queryones(String sysOrgCode, String cailiaoNo, String gongyinsgangNo);

    Wztaizhang queryoness(String sysOrgCode, String cailiaoNo, String gongyinsgangNo, String pici);

    List<Wztaizhang> selectolddata(Integer ids);

    List<String> getOrgCodeList(String orgCategory);

    String getDepartName(String orgCode);

    String selectPici(String orgcode,String nodetype);

    String selectBhgPici(String orgcode,String nodetype);

    String getPercentStr(Integer diff, Integer sum);

    List<String> selectOrgcode(String orgCategory,String orgCode);

    List<Wztaizhang> selectBhgPiciByBd(String nodetype,String orgcode);

    String selectGsName(String orgcode);

    String selectCailiaoName(String cailiaono);

    List<JypWztz> queryJypList();

    String selectNodetypeByCailiaono(String cailiaono);

    List<Map> selectmapList(String jinchangshijian_begin, String jinchangshijian_end, Integer cprule, Integer ycrule);

    List<Map> selectByYearList(Integer ycrule, Integer cprule);

    List<Map> selectByProject(String code);

    WztaizhangKBVO selectNumByCode(String code, String beginTime, String endTime, String cailiaoNo);

    List<Map> selectBiaoduanBycode(String code);

    Double selectCountByPro(String sys_depart_orgcode, String beginTime, String endTime, Integer cprule, Integer ycrule, List<String> stringList);

    List<Map> selectMonthByCode(String code, String beginTime, String endTime, Integer cprule, Integer ycrule, List<String> stringList);

    List<Map> selectMLvByCode(String code, String beginTime, String endTime, Integer cprule, Integer ycrule, List<String> stringList);

    Double selectCountByCl(List<String>  cailiaoNo, String code, String beginTime, String endTime);

    List<JypWztz> queryJypList2(String curdate);

    IPage<Map> getWzTz(Integer pageNo, Integer pageSize, QueryVo queryVo);

    List<String> getNodetypeByOrgCode(String orgCode);

    List<String> getNodetypeByWbsid(String wbsId);

    String getNodetypeName(String nodetype);

    double getUses(String pici);

    String getWbsOrgCode(String wbsId);

    Wztaizhang getState(String inspectionLotNumber);

    List<String> getNode();

    List<YclUsageDetail> getUseageList(String nodeType,String wbsid);

    String selectBydict(String nodetype);

    List<String> selectByDictValue(String cailiaoName);

    List<Map> selectDictList(Integer cprule, Integer ycrule);

    List<String> selectByGongyingshang(String gongyingshangdanweibianma);

    Wztaizhang getByPici(String pici);

    String getBgfile(String pici);

    List<Wztaizhang> SXListYCSD(String ycStr,String sysOrgCode);

    List<Wztaizhang> SXBhgListYCSD(String ycStr,String sysOrgCode);

    List<Wztaizhang> SXListCPSD(String cpStr,String sysOrgCode);

    List<Wztaizhang> SXBhgListCPSD(String cpStr,String sysOrgCode);

    List<Wztaizhang> SXListYCLC(String ycStr, String sysOrgCode);

    List<Wztaizhang> SXBhgListYCLC(String ycStr,String sysOrgCode);

    List<Wztaizhang> SXListCPLC(String cpStr,String sysOrgCode);

    List<Wztaizhang> SXBhgListCPLC(String cpStr,String sysOrgCode);

    String selectJYL(String sysOrgCode, String nodetype, int i);

    String selectJCL(String sysOrgCode, String nodetype);

    String selectUSES(String sysOrgCode, String nodetype);

    List<Wztaizhang> getPiciByStateCailiaoLiaocangGongyingshangNo(Integer jianyanstate, String cailiaono, String lcbianhao, String gongyingshangdanweibianma,String orgCode);

    List<Wztaizhang> getListzlpz(String shebeilist);

    List<Wztaizhang> selectRWDList(String sysOrgCode, Integer curid);
}
