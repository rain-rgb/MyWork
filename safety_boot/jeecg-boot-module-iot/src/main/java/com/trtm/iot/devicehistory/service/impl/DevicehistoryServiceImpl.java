package com.trtm.iot.devicehistory.service.impl;

import com.alibaba.fastjson.JSON;
import com.trtm.iot.devicehistory.entity.DeviceTunnelEnvironmentdataRealPO;
import com.trtm.iot.devicehistory.entity.Devicehistory;
import com.trtm.iot.devicehistory.mapper.DevicehistoryMapper;
import com.trtm.iot.devicehistory.service.IDevicehistoryService;
import com.trtm.iot.system.mapper.ShebeiInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Description: 环境监测历史记录
 * @Author: jeecg-boot
 * @Date:   2021-04-15
 * @Version: V1.0
 */
@Service
public class DevicehistoryServiceImpl extends ServiceImpl<DevicehistoryMapper, Devicehistory> implements IDevicehistoryService {
    @Autowired
    private DevicehistoryMapper devicehistoryMapper;
    @Autowired
    private ShebeiInfoMapper shebeiInfoMapper;

    @Override
    public List<Devicehistory> selectListes(String DevAddr, Integer id) {

        return devicehistoryMapper.selectListes(DevAddr, id);
    }

    @Override
    public List<Devicehistory> selectdeviceone(Integer curid, int alertstate) {
        return devicehistoryMapper.selectdeviceone(curid,alertstate);
    }

    @Override
    public Devicehistory queryone(String shebeiNo) {
        return devicehistoryMapper.queryone(shebeiNo);
    }

    @Override
    public List<Devicehistory> selectlistdata(String strsToList1, Integer curid) {
        return devicehistoryMapper.selectlistdata(strsToList1,curid);
    }

    @Override
    public List<Devicehistory> selectlistToSHYJ(String shebeiNo, Integer id) {
        return devicehistoryMapper.selectlistToSHYJ(shebeiNo,id);
    }

    @Override
    public List<Devicehistory> selectlistYJQS(String shebeiNo, Integer id) {
        return devicehistoryMapper.selectlistYJQS(shebeiNo,id);
    }
    @Override
    public List<Devicehistory> selectlistYLQ(String shebeiNo, Integer id) {
        return devicehistoryMapper.selectlistYLQ(shebeiNo,id);
    }

    @Override
    public List<Devicehistory> selectlistdatatop1(String strsToList1, Integer curid) {
        return devicehistoryMapper.selectlistdatatop1(strsToList1,curid);
    }

    @Override
    public List<Devicehistory> selectListByDev(String DevAddr) {
        return devicehistoryMapper.selectListByDev(DevAddr);

    }

    @Override
    public Devicehistory selectNewByDev(String shebeino) {
        return devicehistoryMapper.selectNewByDev(shebeino);
    }

    @Override
    public int upadteIstuisong(Integer id, int i) {
        Devicehistory devicehistory = new Devicehistory();
        devicehistory.setId(id);
        devicehistory.setIstuisong(i);
        return devicehistoryMapper.updateById(devicehistory);
    }

    @Override
    public List<Devicehistory> selectyjList(String shebeilist, Integer curid) {
        return devicehistoryMapper.selectyjList(shebeilist,curid);
    }

    @Override
    public void push(Devicehistory devicehistory)  throws IOException {
        DeviceTunnelEnvironmentdataRealPO deviceTunnelEnvironmentdataRealPO = new DeviceTunnelEnvironmentdataRealPO();
        deviceTunnelEnvironmentdataRealPO.setDataTime(devicehistory.getTimevalue());
        deviceTunnelEnvironmentdataRealPO.setPmTen(devicehistory.getPm10());
        deviceTunnelEnvironmentdataRealPO.setPmTwo(devicehistory.getPm25());
        deviceTunnelEnvironmentdataRealPO.setNoise(devicehistory.getNoise());
        deviceTunnelEnvironmentdataRealPO.setTemperature(devicehistory.getTemperature());
        deviceTunnelEnvironmentdataRealPO.setHumidity(devicehistory.getHumidity());
        deviceTunnelEnvironmentdataRealPO.setWindspeed(devicehistory.getWindspeed());
        deviceTunnelEnvironmentdataRealPO.setShebeino(devicehistory.getDevaddr());
        String shebeiName = shebeiInfoMapper.querySheBeiMC(devicehistory.getDevaddr());
        deviceTunnelEnvironmentdataRealPO.setSite(shebeiName);
//        deviceTunnelEnvironmentdataRealPO.setTunnelid("10010142");
        String shebeiNo = devicehistory.getDevaddr();

        // 构造请求体
        String json = JSON.toJSONString(deviceTunnelEnvironmentdataRealPO);

        //创建HttpPost对象，设置url访问地址
//        URL url = new URL("http://122.96.239.81:9100/api/tunnel/tunnelEnvironmentdataReals/tunnelEnvironmentRealDataUpload");
        URL url = new URL("http://122.96.239.81:9089/api/tunnel/tunnelEnvironmentdataReals/tunnelEnvironmentRealDataUpload");
        //本地测试
//        URL url = new URL("http://192.168.1.245:8087/tunnel/tunnelEnvironmentdataReals/tunnelEnvironmentRealDataUpload");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 设置请求方法和头
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // 发送请求
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = json.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // 读取响应
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);
    }
}
