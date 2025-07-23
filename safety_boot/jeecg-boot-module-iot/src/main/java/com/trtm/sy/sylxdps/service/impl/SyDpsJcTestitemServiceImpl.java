package com.trtm.sy.sylxdps.service.impl;

import com.trtm.sy.sylxdps.entity.SyDpsJcTestitem;
import com.trtm.sy.sylxdps.mapper.SyDpsJcTestitemMapper;
import com.trtm.sy.sylxdps.mapper.SyDpsJcTestitemtypeMapper;
import com.trtm.sy.sylxdps.service.ISyDpsJcTestitemService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.*;

/**
 * @Description: sy_dps_jc_testitem
 * @Author: jeecg-boot
 * @Date: 2023-01-10
 * @Version: V1.0
 */
@Service
public class SyDpsJcTestitemServiceImpl extends ServiceImpl<SyDpsJcTestitemMapper, SyDpsJcTestitem> implements ISyDpsJcTestitemService {

    @Autowired
    private SyDpsJcTestitemMapper testitemMapper;
    @Autowired
    private SyDpsJcTestitemtypeMapper dpsJcTestitemtypeMapper;

    @Override
    public List<Map> getList(String titCode) {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<String> roleIds = dpsJcTestitemtypeMapper.getRoleIds(user.getId());
        String tiNos = testitemMapper.selectTiNoByUserId(roleIds, titCode);
        List<String> tiNoList = Arrays.asList(tiNos.split(","));
        List<Map> plist = testitemMapper.getPList(titCode);
        List<Map> list = testitemMapper.getList(titCode, tiNoList);
        List<Map> l = new ArrayList();
        for (int i = 0; i < plist.size(); i++) {
            List a = new ArrayList();
            for (int i1 = 0; i1 < list.size(); i1++) {
                if (plist.get(i).get("tiNo").toString().equals(list.get(i1).get("tiParentNo").toString())) {
                    a.add(list.get(i1));
                }
            }
            if (a.size() > 0) {
                Map map = new HashMap();
                map.put("plist", plist.get(i));
                map.put("list",a);
                l.add(map);
            }
        }
        return l;
    }

    @Override
    public Map findById(String id) {
        Map<String, Object> map = testitemMapper.findById(id);
        String tiNo = map.get("tiNo").toString();
        String sampleNo = map.get("sampleNo").toString();
        String tiNoTemp = map.get("tiNoTemp").toString();
        List<String> columnName = testitemMapper.findColumnName(tiNo);
        Map<String, Object> data = testitemMapper.findByTiNos(columnName,tiNo,sampleNo,tiNoTemp);
        data.putAll(map);
        return data;
    }

    @Override
    public Map findSamp(String id) {
        Map<String, Object> data = testitemMapper.findSampById(id);
        List<Map> tiNos = testitemMapper.findReportSBySampleNo(data.get("sampleNo").toString());
        List<Map> QRCode = testitemMapper.findQRCodeBySampleNo(data.get("sampleNo").toString());
        List<Map> Pic = testitemMapper.findPicBySampleNo(data.get("sampleNo").toString());
        List<Map> yj = testitemMapper.findYjBySampleNo(data.get("sampleNo").toString());
        data.put("tiNos", tiNos);
        data.put("QRCode", QRCode);
        data.put("Pic", Pic);
        data.put("yj", yj);
        return data;
    }

    @Override
    public List<Map<String, Object>> typeList(String type, String bg, Boolean isAll) {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        List<Map<String, Object>> list = null;
        if(oConvertUtils.isNotEmpty(isAll) && isAll==true){
            list = testitemMapper.findSql(user.getId(), type);

        }else{
            list = testitemMapper.findSql1(type);

        }
        if (oConvertUtils.isNotEmpty(bg)) {
            for (int i = 0; i < list.size(); i++) {
                List<Map<String, Object>> children = testitemMapper.findSql2(list.get(i).get("value").toString());
                list.get(i).put("children", children);
            }
        }
        List<Map<String, Object>> newlist = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < list.size(); i++) {
            if (oConvertUtils.isEmpty(list.get(i).get("pvalue"))) {
                newlist.add(list.get(i));
            }
        }
        for (int i = 0; i < newlist.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (newlist.get(i).get("value").equals(list.get(j).get("pvalue"))) {
                    List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
                    if (newlist.get(i).containsKey("children")) {
                        children = (List<Map<String, Object>>) newlist.get(i).get("children");
                        children.add(list.get(j));
                    } else {
                        children.add(list.get(j));
                        newlist.get(i).put("children", children);
                    }
                }
            }
        }
        return newlist;
    }
}
