package com.trtm.sy.sycspz.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.sy.sycspz.entity.SyMParamBoxqty;
import com.trtm.sy.sycspz.mapper.SyMParamBoxqtyMapper;
import com.trtm.sy.sycspz.service.ISyMParamBoxqtyService;
import com.trtm.sy.sycspz.utils.ExcelRead;
import com.trtm.sy.sycspz.utils.ExcelUtil;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import com.xkcoding.http.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: sy_m_param_boxqty
 * @Author: jeecg-boot
 * @Date: 2023-12-07
 * @Version: V1.0
 */
@Service
public class SyMParamBoxqtyServiceImpl extends ServiceImpl<SyMParamBoxqtyMapper, SyMParamBoxqty> implements ISyMParamBoxqtyService {


    @Override
    public IPage<Map<String, Object>> mparamraboxqtyList(Integer offset, Integer limit, String boxno, HttpServletRequest request, HttpServletResponse response) {
        Page Page = new Page(offset, limit);
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        IPage<Map<String, Object>> result = this.baseMapper.mparamraboxqtyList(Page, boxno, loginUser.getOrgCode());
        return result;
    }

    @Override
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response, MultipartFile file) throws IOException {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        boolean flag = true;
        StringBuffer sbmsg = new StringBuffer();
        //判断文件是否为空
        if (file == null) {
            throw new JeecgBootException("文件为空");
        }
        String name = file.getOriginalFilename();
        long size = file.getSize();
        if (name == null || ExcelUtil.EMPTY.equals(name) && size == 0) {
            throw new JeecgBootException("文件错误");
        }
        String[] list = {"boxno", "boxqty", "departId"};
        String[] noEmpty = {"boxqty", "boxno", "departId"};
        JSONObject obj = new JSONObject();
//        SysDepart sysDepart = null;
//        obj.put("departId", sysDepart.getId());
        obj.put("departId", loginUser.getDepartIds());
        List<ArrayList<String>> listArr = new ExcelRead().readExcel(file);
        if (flag) {
            if (listArr.size() > 1) {
                for (int i = 0; i < listArr.size(); i++) {
                    ArrayList arrlist = listArr.get(i);
                    if (!StringUtil.isNotEmpty(arrlist.get(0).toString())) {
                        flag = false;
                        sbmsg.append("行号 " + (i + 3) + " 盒号为空。<br>");
                    }
                    if (!StringUtil.isNotEmpty(arrlist.get(1).toString())) {
                        flag = false;
                        sbmsg.append("行号 " + (i + 3) + " 盒质量为空。<br>");
                    }
                }

            } else {
                throw new JeecgBootException("文件内没有数据");
            }
            if (flag) {
                JSONArray json = ExcelUtil.ExcelToJson(listArr, 2, list, obj, noEmpty);
                for (int i = 0; i < json.size(); i++) {
                    obj = json.getJSONObject(i);
                    List<Map<String, Object>> BoxNolist = this.baseMapper.importExcel(loginUser.getDepartIds());
                    for (int j = 0; j < BoxNolist.size(); j++) {
                        if (obj.get("boxno").equals(BoxNolist.get(j).get("Boxno"))) {
                            flag = false;
                            sbmsg.append("行号 " + (i + 3) + " 盒号重复。<br>");
                        }
                    }
                }
                if (flag) {
                    for (int i = 0; i < json.size(); i++) {
                        obj = json.getJSONObject(i);
                        SyMParamBoxqty e = (SyMParamBoxqty) JSONObject.toBean(obj, SyMParamBoxqty.class);
                        this.save(e);
                    }
                    return Result.OK();
                } else {
                    return Result.error(sbmsg.toString());
                }
            }
        }
        return null;
    }
}

