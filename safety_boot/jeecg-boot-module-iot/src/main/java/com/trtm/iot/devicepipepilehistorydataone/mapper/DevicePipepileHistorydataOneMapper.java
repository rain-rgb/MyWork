package com.trtm.iot.devicepipepilehistorydataone.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.devicepipepilehistorydataone.controller.DevicePipepileHistorydataOneController;
import com.trtm.iot.devicepipepilehistorydataone.entity.DevicePipepileDto;
import com.trtm.iot.devicepipepilehistorydataone.entity.GongYiVo;
import com.trtm.iot.devicepipepilehistorydataone.vo.DevicePipepileReportVo;
import com.trtm.iot.devicepipepilehistorydataone.vo.PartVo;
import com.trtm.iot.devicepipepilehistorydatapart.entity.DevicePipepileHistorydataPart;
import com.trtm.iot.pippileOneOverHandler.entity.PippileOneOverHandler;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.zhangla.entity.OverHandler;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.devicepipepilehistorydataone.entity.DevicePipepileHistorydataOne;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: device_pipepile_historydata_one
 * @Author: jeecg-boot
 * @Date:   2022-07-21
 * @Version: V1.0
 */
public interface DevicePipepileHistorydataOneMapper extends BaseMapper<DevicePipepileHistorydataOne> {

    @Select("select * from device_pipepile_historydata_part where shebeino = #{shebeino} and pile_no = #{pileNo}")
    List<PartVo> selectWorksta(String shebeino, String pileNo);

    @Select("select * from device_pipepile_report where shebeino = #{shebeino} and pile_no = #{pileNo}")
    List<DevicePipepileReportVo> selectDeviceVo(String shebeino, String pileNo);

    @Select("select * from device_pipepile_historydata_part where shebeino = #{shebeino} and pile_no = #{pileNo}")
    List<DevicePipepileHistorydataPart> selectbysp(String shebeino, String pileNo);

    List<DevicePipepileHistorydataOne> selectLists(String shebeino, Integer id);

    @Select("select * from device_pipepile_historydata_one where istongji = #{i} limit 100")
    List<DevicePipepileHistorydataOne> selectjbzzones(Integer curid, int i);

//    @Select("select count(*) from device_pipepile_historydata_one")
    Integer count(List<String> sheBs);


    Integer findXiangMuZS(List<String> querySheBeiList);//

    Integer findYuJingS(List<String> querySheBeiList);//

    Integer findBiHeS(List<String> querySheBeiList);//


    Integer queryCount(String sheBeiNo);//

    Integer queryBuhe(String sheBeiNo);//

    Integer queryStatus(String sheBeiNo);//


    List<Map<String, Object>> selectSheBeiNo(String orgCode);//
    List<Map<String, Object>> selectSBNo();

    List<String> fingAllSheBeiNo(String orgCode);//
    List<String> fingAllSheBeiNos(String orgCode);//

    String findGsMc(String orgCode);//

    List<String> querySheBeiBHByOrgCode(String orgCode);//

    List<String> queryOrgCode(String orgCategory);

    List<GongYiVo> queryCountZy(Integer orgCategory, String orgCode);

    List<GongYiVo> queryCountBySheBei(Integer orgCategory, String orgCode);
    GongYiVo qyeryByXiangMu(String orgCode);



    List<OverHandler> findOverHandler(String syjid);

    IPage<DevicePipepileDto> queryDeatilss(String shebeiNo, Page<DevicePipepileDto> pageQuery);

    @Select("select * from device_pipepile_historydata_one where pile_time like #{stadate}")
    List<DevicePipepileHistorydataOne> stadate(String stadate);

    Integer findXiangMuZStime(List<String> querySheBeiList, String dateNowStr);

    Integer findYuJingStime(List<String> querySheBeiList, String dateNowStr);

    Integer findBiHeStime(List<String> querySheBeiList, String dateNowStr);

    List<Map<String, Object>> selectorgcodebyid(String orgCode1);

    Integer findXiangMuZStimes(List<String> querySheBeiList, String beginTime, String endTime);

    Integer findYuJingStimes(List<String> querySheBeiList, String beginTime, String endTime);

    Integer findBiHeStimes(List<String> querySheBeiList, String beginTime, String endTime);

    Integer findBiHeSsb(List<String> querySheBeiList, String time);

    Integer findYuJingSsb(List<String> querySheBeiList, String time);

    Integer findXiangMuZSsb(List<String> querySheBeiList, String time);

    Integer findzzyc(List<String> querySheBeiList);

    Integer findqjyc(List<String> querySheBeiList);

    @Select("select * from sys_depart where org_code = #{orgCode}")
    SysDepart getSysDepart(String orgCode);

    @Select("select * from sys_depart where id = #{departParentId}")
    SysDepart getDepart(String departParentId);

    List<DevicePipepileHistorydataOne> selectUnifiedProcess(Integer curid, Integer alertstate, List<String> shebeiList, Integer overLevel);

    List<String> fingbhzSheBeiNo(String orgCode, int sbtypes);

    List<Map<String,Object>> getrichaxun(@Param("shebeis") List<String> shebeis);
    List<Map<String,Object>> getzhouchaxun(@Param("shebeis") List<String> shebeis);
    List<Map<String,Object>> getyuechaxun(@Param("shebeis") List<String> shebeis);

    @Select("select * from sys_depart where org_code like #{orgCode} and org_type = #{i}")
    List<SysDepart> fandxmordcode(String orgCode, int i);
}
