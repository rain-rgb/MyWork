package com.trtm.sy.sydpssysample.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.sy.sydpssysample.entity.*;
import com.trtm.sy.sylxdps.entity.*;
import io.swagger.models.auth.In;
import net.sf.json.JSONArray;
import org.jeecg.common.api.vo.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: sy_dps_sy_sample
 * @Author: jeecg-boot
 * @Date: 2023-01-12
 * @Version: V1.0
 */
public interface ISyDpsSySampleService extends IService<SyDpsSySample> {

    String selectProjNames(String orgCode);

    SyDpsSySample getSampleByWtbh(String wtbh);

    void insert(HashMap map);

    void add(HashMap map);

    Map getSList(String id);

    Map getBgSList(String id);

    IPage getGrid(Integer pageNo, Integer pageSize, String orgCode, String titCode, String sampleState, String sampleNo, String sampleName, String sampleGcbw, String titType, String sampleDate, String reportNo, String tiNo, String userName, Boolean lookself, String signature, String shenpizhuangtai, String qianzhangzhuangtai, String lq);

    void deleteBySampleNo(String sampleNo);

    List<Map<String, Object>> selectTinoBySampleNo(String sampleNo);

    List<Map<String, Object>> selectDate(String sampleNo, String tino);

    SysDepart queryDepartByDepartid(String id);

    List<Map<String, Object>> selectByLeixing(int qianzhangleixing, String titcode, int liuchengleixing);

    List<Map<String, Object>> selectByIdAndType(String id, Integer type);

    String selectOneTinoBySampleno(String sampleno);

    String selectBgdateBySampleno(String sampleno, String tino);

    Map<String, Object> selectByRSId(String id);

    Map<String, Object> selectBysampleNoAndtiNoTemp(String tiNo, String sampleno, String tinotemp);

    SysDepart getDepartById(String id);

    Map<String, Object> selectId(String departId, String titCode);

    SyDpsJcTestitemtypeCodingrules selectTCById(String id);

    SysDepart selectByDepartId(String departId);

    SyDpsJcTestitemtype selectTtByTitcode(String titCode);

    Map<String, Object> selectTinoBySampleno(String sampleNo);

    Map<String, Object> selectByNoFlowNumber(String NoFlowNumber);

    Long selectCount(String str);

    List<Map<String, Object>> selectSampleNoLike(String str);

    void updateCurrentCodeById(String currentCode, String id);

    void addCodingFlowNumber(String currentCode, String str);

    SyDpsSyReportM selectRMBySampleNo(String sampleNo);

    List<SyDpsSyReportS> selectRSBySampleNo(String sampleNo);

    List<SyDpsSyTableheader> selectTHBySampleNo(String sampleNo);

    SyDpsJcTestitem selectTIByTino(String tiNo);

    Map<String, Object> selectBySamplenoFromTino(String tableName, String sampleNo, Integer tiNoTemp);

    void insert1(String tino, String str, Integer tiNoTemp, String yp, String jl, String bg, String departId);

    void update1(String tino, String str, String key, String value);

    Long selectCountBySampleNo(String sampleNo);

    void updateCFN(String sampleNo2, String sampleNo1, String sampleNoNoSuffix);

    void updateRM(String sampleNo, String reportNo, String tableNumber, String sampleNoOld);

    void updateRS(String sampleNo, String reportNo, String sampleNoOld);

    List<String> selectTiNoList(String sampleNo);

    void updateTable(String table, String sampleNo, String reportNo, String tableNumber, String sampleNoOld);

    void updateNo1(String sampleNox, String reportNo, String tableNumber, String reportingSheetNo, String approvalTableNo, String sampleNoNew, String reportNoNew, String tableNumberNew, String reportingSheetNoNew, String approvalTableNoNew, String sampleNo);

    void updateNo2(String table, String sampleNo1, String sampleNo2);

    void deleteNo3(String table, String sampleId);

    void delSample(String id);

    void getSampleId(String id);

    void returnSample(String sampleNo, String xcjc);

    IPage getGrid(Page<Map> page, String orgCode, String titCode, String sampleNo, String sampleName, String sampleGcbw, String date, String reportNo, Integer liuchengleixing, String type, String state, String id, String reportDate, String s);

    IPage getGrid3(Page<Map> page, String orgCode, String titCode, String sampleNo, String sampleName, String sampleGcbw, String date, String reportNo, String tiNo, String username, String state, String self, String shenpizhuangtai);

    String approval(HttpServletRequest request, HttpServletResponse response, String id, String type) throws Exception;

    List<Map<String, Object>> get2(String biaoming, JSONArray json, String orderby);

    List<Map<String, Object>> get1(String biaoming, JSONArray json, String orderby, String groupby);

    List<Map<String, Object>> getTable(String table, HttpServletRequest request);

    Map<String, Object> searchOneReturn(String biaoming, JSONArray json);

    List<Map<String, Object>> dushuget(HttpServletRequest request);

    Object getSyjData(HashMap<String, Object> map);

    IPage<SyResponse> getGrids(SyRequest syRequest);

    Map<String, Object> tqdata(String titCode, String sampleNo);

    Map<String, Object> getSampleById(String id);

    List<Map> getSyRenWu(String orgCode);

    void copy(HttpServletRequest request, String id, String sampleDate, String type, String inspection);

    List<Map> getTypeListByCode(String code);

    IPage<Map> searchPhbData(Map map);

    Map ycjcExtract(String wtdbh);
}
