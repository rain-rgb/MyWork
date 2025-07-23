package com.trtm.iot.zlpzpeizhi.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.zlpzpeizhi.entity.Zlpzpeizhi;
import com.trtm.iot.zlpzpeizhi.mapper.ZlpzpeizhiMapper;
import com.trtm.iot.zlpzpeizhi.service.IZlpzpeizhiService;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Description: 浙路品质推送配置
 * @Author: jeecg-boot
 * @Date:   2023-12-12
 * @Version: V1.0
 */
@Service
public class ZlpzpeizhiServiceImpl extends ServiceImpl<ZlpzpeizhiMapper, Zlpzpeizhi> implements IZlpzpeizhiService {
    @Autowired
    private IShebeiInfoService iShebeiInfoService;
    @Override
    public void saveMain(Zlpzpeizhi zlpzpeizhi) {
        UUID uuid = UUID.randomUUID();
        zlpzpeizhi.setShebeiid(String.valueOf(uuid));
        ShebeiInfo sbjwd = iShebeiInfoService.SBJWD(zlpzpeizhi.getShebeino());
        if (sbjwd!=null) zlpzpeizhi.setShebeiname(sbjwd.getSbname());
        addShebeiInfo(zlpzpeizhi);
        this.save(zlpzpeizhi);
    }

    private void addShebeiInfo(Zlpzpeizhi zlpzpeizhi) {
        String projectId = zlpzpeizhi.getProject();
        String sectionId = zlpzpeizhi.getBiaoduan();
        String equipmentNumber = zlpzpeizhi.getShebeino();
        String id = zlpzpeizhi.getShebeiid();
        String equipType =iShebeiInfoService.getEquipType(zlpzpeizhi.getSbtype());
        String equipmentName = zlpzpeizhi.getShebeiname();

        JSONObject sendDate = new JSONObject();
        List sendList = new ArrayList();

        sendDate.set("id", id);
        sendDate.set("equipType", equipType);
        sendDate.set("equipmentNumber", equipmentNumber);
        sendDate.set("equipmentName", equipmentName);
        sendDate.set("projectId", projectId);
        sendDate.set("sectionId", sectionId);

        sendList.add(sendDate);

        JSONObject sendJsonObject = new JSONObject();
        sendJsonObject.set("serviceName", "ZLPZ_ZX_WLWDGSBZC");
        sendJsonObject.set("token", "93cd2c6567594107a16b51a65bcd5a37");
        sendJsonObject.set("updateNull", "true");
        sendJsonObject.set("param", sendList);

        String url = "https://sjsn.jtyst.zj.gov.cn:21086/dtas-server/api/service/push";
        HttpRequest.post(url)
                .body(String.valueOf(sendJsonObject))
                .timeout(20000)
                .execute().body();
    }
}
