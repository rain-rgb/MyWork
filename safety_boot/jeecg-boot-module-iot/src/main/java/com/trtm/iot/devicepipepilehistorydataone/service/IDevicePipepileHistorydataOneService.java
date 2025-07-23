package com.trtm.iot.devicepipepilehistorydataone.service;

import com.trtm.iot.devicepipepilehistorydataone.controller.DevicePipepileHistorydataOneController;
import com.trtm.iot.devicepipepilehistorydataone.entity.DevicePipepileHistorydataOne;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.devicepipepilehistorydataone.entity.GongYiDto;
import com.trtm.iot.devicepipepilehistorydataone.vo.DevicePipepileReportVo;
import com.trtm.iot.devicepipepilehistorydataone.vo.PartVo;
import com.trtm.iot.devicepipepilehistorydatapart.entity.DevicePipepileHistorydataPart;

import java.util.List;
import java.util.Map;

/**
 * @Description: device_pipepile_historydata_one
 * @Author: jeecg-boot
 * @Date: 2022-07-21
 * @Version: V1.0
 */
public interface IDevicePipepileHistorydataOneService extends IService<DevicePipepileHistorydataOne> {

    List<PartVo> selectWorksta(String shebeino, String pileNo);

    List<DevicePipepileReportVo> selectDeviceVo(String shebeino, String pileNo);

    List<DevicePipepileHistorydataPart> selectbysp(String shebeino, String pileNo);

    List<DevicePipepileHistorydataOne> selectLists(String shebeino, Integer id);

    List<DevicePipepileHistorydataOne> selectjbzzones(Integer curid, int i);

    List<Map<String, Object>> queryWarningStatus(String orgCode);

    List<Map<String, Object>> findWarningStatus(Integer type,String orgCode);


    List<GongYiDto> queryWarningStatusByType(String orgCode, Integer type);

    Map<String, Object> queryRateOfPass(String orgCode);

    List<DevicePipepileHistorydataOne> stadate(String stadate);

    List<Object> findgxStatus(String dateNowStr,String orgCode);

    List<Object> findhntStatus(String dateNowStr,String orgCode);

    List<Object> findgxyears(String dateNowStr,String orgCode);

    List<Map<String, Object>> findgxtypeyears(Integer type, String dateNowStr, String orgCode);

    List<Map<String, Object>> findgxtypeyearss(Integer type, String orgCode, String beginTime, String endTime);

    Integer findXiangMuZSsb(List<String> querySheBeiList, String time);

    Integer findYuJingSsb(List<String> querySheBeiList, String time);

    Integer findBiHeSsb(List<String> querySheBeiList, String time);

    List<Map<String, Object>> findgxYearxmtj(String orgCode, String dateNowStr);

    List<Map<String, Object>> findgxYearxmbhztj(String orgCode, String dateNowStr);

    List<Map<String, Object>> findgxYearxmbhzyjbgt(String orgCode, Integer detatime, Integer grade);

    List<Map<String, Object>> findgxYearxmxq(String orgCode, String dateNowStr,Integer type);

    List<Map<String, Object>> soqyorgcode(String orgCode);

    List<Map<String, Object>> findgxYearxmtjzg(String orgCode, String dateNowStr);

    List<Map<String, Object>> findWarningSta(String orgCode);

    String getSbjName(String sheBeiNo);

    String getDepartName(String orgCode);

    List<DevicePipepileHistorydataOne> selectUnifiedProcess(Integer curid, Integer alertstate, List<String> shebeiList, Integer overLevel);

    List<Map<String, Object>> findjhtjStatus(String dateNowStr, String orgCode);

    List<Map<String, Object>> findjhtjzlyjStatus(String dateNowStr, String orgCode);

    List<Map<String, Object>> findjhtjsybyStatus(String dateNowStr, String orgCode);

    List<Map<String, Object>> findjhtyyStatus(String dateNowStr, String orgCode);

    List<Map<String, Object>> findjhtyysStatus(String dateNowStr, String orgCode);

    List<Object> findhntczgStatus(String orgCode);

    abstract List<Map<String, Object>> findgxtypeyearssex(Integer type, String orgCode, String beginTime, String
            endTime);
}
