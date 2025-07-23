package com.trtm.iot.jiaqiaoji.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.jiaqiaoji.entity.DeviceBridgeHistorydata;
import com.trtm.iot.jiaqiaoji.entity.DeviceBridgeRealdata;
import com.trtm.iot.jiaqiaoji.mapper.DeviceBridgeHistorydataMapper;
import com.trtm.iot.jiaqiaoji.service.IDeviceBridgeHistorydataService;
import com.trtm.iot.message.entity.SysMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Description: device_bridge_historydata
 * @Author: jeecg-boot
 * @Date: 2023-03-13
 * @Version: V1.0
 */
@Service
public class DeviceBridgeHistorydataServiceImpl extends ServiceImpl<DeviceBridgeHistorydataMapper, DeviceBridgeHistorydata> implements IDeviceBridgeHistorydataService {

    @Autowired
    private DeviceBridgeHistorydataMapper deviceBridgeHistorydataMapper;

    @Override
    public List<DeviceBridgeHistorydata> selectJQJList(String shebeilist, Integer curid) {
        return deviceBridgeHistorydataMapper.selectJQJList(shebeilist, curid);
    }

    @Override
    public List<DeviceBridgeHistorydata> selectListbim(String shebeilist, Integer curid) {
        return deviceBridgeHistorydataMapper.selectListbim(shebeilist, curid);
    }

    @Override
    public List<Map<String, Object>> getYjList() {
        return deviceBridgeHistorydataMapper.getYjList();
    }

    @Override
    public void cbpanduan(DeviceBridgeRealdata deviceBridgeRealdata) {
        double tanValue1 = Math.abs(deviceBridgeRealdata.getCraneHeight1() - deviceBridgeRealdata.getCraneHeight2());
        double tanValue2 = Math.abs(deviceBridgeRealdata.getLongitudinalCarroute1() - deviceBridgeRealdata.getLongitudinalCarroute2());
        double tanValue = tanValue1 / tanValue2;
        double atanValue = Math.atan(tanValue) * 180 / Math.PI;
        atanValue = deviceBridgeRealdata.getCraneHeight1() - deviceBridgeRealdata.getCraneHeight2() > 0 ? atanValue : -atanValue;
//        判断atanValue的绝对值大于15则超标
        if (Math.abs(atanValue) >= 15) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            JSONObject obj = new JSONObject();
            obj.put("sbname", "新老07线架桥机");
            obj.put("time", format.format(deviceBridgeRealdata.getBridgedate()));
            obj.put("content", "角度超标！当前角度为："+atanValue);
            SysMessage sysMessage = new SysMessage();
            sysMessage.setEsTitle("架桥机角度预警");
            sysMessage.setId(String.valueOf(System.currentTimeMillis()));
            sysMessage.setEsType("1");//短信类型  1短信
            sysMessage.setEsReceiver("18257319258");//手机号
            sysMessage.setEsContent(obj.toString());//短信内容
            sysMessage.setEsSendStatus("0");//推送状态0未推送
            sysMessage.setEsSendNum(0);//推送总次数
            sysMessage.setRemark(deviceBridgeRealdata.getDeviceCode());
                sysMessage.setCreateTime(new Date());

            deviceBridgeHistorydataMapper.saveyjdx(sysMessage);
        }
    }
}
