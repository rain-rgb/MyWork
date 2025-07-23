package com.trtm.iot.zhangla.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.iot.devicepipepilehistorydataone.entity.DevicePipepileDto;
import com.trtm.iot.devicepipepilehistorydataone.mapper.DevicePipepileHistorydataOneMapper;
import com.trtm.iot.pippileOneOverHandler.entity.PippileOneOverHandler;
import com.trtm.iot.system.mapper.ShebeiInfoMapper;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.mapper.TrYajiangSMapper;
import com.trtm.iot.zhangla.entity.*;
import com.trtm.iot.zhangla.mapper.TrZhanglaMapper;
import com.trtm.iot.zhangla.service.ITrZhanglaService;
import com.trtm.iot.zhanglam.entity.ZhanglaYajiangOverHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description: tr_zhangla
 * @Author: jeecg-boot
 * @Date: 2021-03-16
 * @Version: V1.0
 */
@Service
@Slf4j
public class TrZhanglaServiceImpl extends ServiceImpl<TrZhanglaMapper, TrZhangla> implements ITrZhanglaService {

    @Autowired
    private TrZhanglaMapper trZhanglaMapper;
    @Autowired
    private TrYajiangSMapper trYajiangSMapper;
    @Autowired
    private DevicePipepileHistorydataOneMapper devicePipepileHistorydataOneMapper;
    @Autowired
    private ShebeiInfoMapper shebeiInfoMapper;


    @Override
    public Map queryDetails(Integer type,String shebeiNo, Integer pageNo, Integer pageSize) {
        Map map = new HashMap();
        List<ClomnData> clomnData = new ArrayList<>();
        if (null != type && type == 1) {

            clomnData.add(new ClomnData("departName", "项目名称"));
            clomnData.add(new ClomnData("sbname", "设备名称"));
            clomnData.add(new ClomnData("pileMileage", "里程"));
            clomnData.add(new ClomnData("pileNo", "桩号"));
            clomnData.add(new ClomnData("pileWorktime", "成桩时间"));
            clomnData.add(new ClomnData("pileUpress", "最大压桩力(兆帕)"));
            clomnData.add(new ClomnData("pileDpress", "最大夹持力(兆帕)"));
            clomnData.add(new ClomnData("pileSpeed", "平均速度(cm/min)"));

            clomnData.add(new ClomnData("pileRealdep", "施工长度(m)"));
            clomnData.add(new ClomnData("pileY", "最大垂直度(%)"));
            clomnData.add(new ClomnData("pileDesigndep", "设计桩长"));
            clomnData.add(new ClomnData("pileStarttime", "开始时间"));
            clomnData.add(new ClomnData("pileTime", "结束时间"));

            clomnData.add(new ClomnData("overproofStatus", "处理状态"));
            clomnData.add(new ClomnData("approvalPerson", "审核人"));
            clomnData.add(new ClomnData("chaobiaodengji", "是否合格"));
            map.put("clomnData", clomnData);
            Page<DevicePipepileDto> pageQuery = new Page<>(pageNo, pageSize);
            IPage<DevicePipepileDto> tableData = devicePipepileHistorydataOneMapper.queryDeatilss(shebeiNo,pageQuery);
            map.put("tableData", tableData);
        } else if (null != type && type == 2) {

            clomnData.add(new ClomnData("gcmc", "工程名称"));
            clomnData.add(new ClomnData("kualiang", "张拉梁型"));
            clomnData.add(new ClomnData("sbname", "设备名称"));
            clomnData.add(new ClomnData("sclper", "延伸量误差"));
            clomnData.add(new ClomnData("zlsj", "张拉时间"));
            clomnData.add(new ClomnData("yxpc", "允许偏差值"));
            clomnData.add(new ClomnData("gsbh", "钢束编号"));

            clomnData.add(new ClomnData("txml", "弹性模量"));
            clomnData.add(new ClomnData("sjzll", "设计张拉力 KN"));
            clomnData.add(new ClomnData("htl", "回弹量mm"));
            clomnData.add(new ClomnData("zscl", "总伸长量mm"));
            clomnData.add(new ClomnData("llscl", "理论伸长量mm"));
            clomnData.add(new ClomnData("yzlb", "预张拉百分比"));
            clomnData.add(new ClomnData("czlb", "初张拉百分比"));
            clomnData.add(new ClomnData("zzlb", "终张拉百分比"));

            clomnData.add(new ClomnData("hege", "是否合格"));
            clomnData.add(new ClomnData("overproofStatus", "处理状态"));
            clomnData.add(new ClomnData("handlePerson", "处置人"));
            clomnData.add(new ClomnData("approvalPerson", "审核人"));
            map.put("clomnData", clomnData);
            Page<ZhangLaDto> pageQuery = new Page<>(pageNo, pageSize);
            IPage<ZhangLaDto> tableData = trZhanglaMapper.queryDeatilss(shebeiNo,pageQuery);
            map.put("tableData", tableData);
        } else if (null != type && type == 3) {

            clomnData.add(new ClomnData("yajiangsj", "压浆时间"));
            clomnData.add(new ClomnData("kongdao", "孔道"));
            clomnData.add(new ClomnData("yajiangmosh", "压浆模式"));
            clomnData.add(new ClomnData("peihebi", "配合比"));
            clomnData.add(new ClomnData("shuijiaobi", "水胶比"));
            clomnData.add(new ClomnData("jiaobansj", "搅拌时间"));
            clomnData.add(new ClomnData("starttime", "开始时间"));
            clomnData.add(new ClomnData("endtime", "结束时间"));
            clomnData.add(new ClomnData("jinjiangyal", "进浆压力MPa"));
            clomnData.add(new ClomnData("fanjiangyal", "返浆压力MPa"));
            clomnData.add(new ClomnData("chixushijia", "持续时间"));
            clomnData.add(new ClomnData("jinjiangshu", "进浆量L"));
            clomnData.add(new ClomnData("fanjianglia", "返浆量L"));
            clomnData.add(new ClomnData("zkyl", "真空泵压力MPa"));
            clomnData.add(new ClomnData("yjcs", "压浆次数"));
            clomnData.add(new ClomnData("hege", "是否合格"));
            map.put("clomnData", clomnData);
            Page<YaJiangVo> pageQuery = new Page<>(pageNo, pageSize);
            IPage<YaJiangVo> tableData = trYajiangSMapper.queryDeatilss(shebeiNo,pageQuery);

            map.put("tableData", tableData);
        }
        return map;
    }

    @Override
    public Map queryDe(String syjid, Integer type) {

        Map map = new HashMap();
        List<ClomnData> clomnData = new ArrayList<>();
        clomnData.add(new ClomnData("problemReasons", "问题原因"));
        clomnData.add(new ClomnData("handleWay", "处理方式"));
        clomnData.add(new ClomnData("handleResult", "处理结果"));
        clomnData.add(new ClomnData("handleTime", "处理时间"));
        clomnData.add(new ClomnData("handlePerson", "处理人"));
        clomnData.add(new ClomnData("supervisorApproval", "监理审批"));
        clomnData.add(new ClomnData("supervisorResult", "监理结果"));
        clomnData.add(new ClomnData("supervisorHandleTime", "监理处理时间"));
        clomnData.add(new ClomnData("approvalPerson", "审批人"));
        clomnData.add(new ClomnData("remark", "驳回原因"));
        clomnData.add(new ClomnData("filePath", "审核照片"));
        clomnData.add(new ClomnData("filePath2", "处置照片"));
        clomnData.add(new ClomnData("overproofStatus", "状态"));
        map.put("clomnData", clomnData);
        if (null != type && type == 1) {
            List<OverHandler> tableData = devicePipepileHistorydataOneMapper.findOverHandler(syjid);
            map.put("tableData", tableData);
        } else if (null != type && type == 2) {
            List<OverHandler> tableData = trZhanglaMapper.findOverHandler(syjid);
            map.put("tableData", tableData);
        } else if (null != type && type == 3) {
            List<OverHandler> tableData = trYajiangSMapper.findOverHandler(syjid);
            map.put("tableData", tableData);
        }
        return map;
    }


}
