package com.trtm.iot.syj.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.attachlist.entity.Attachlist;
import com.trtm.iot.syj.entity.*;
import com.trtm.iot.syj.mapper.FWangnjMapper;
import com.trtm.iot.syj.mapper.FYalijiMapper;
import com.trtm.iot.syj.mapper.TSyjzbMapper;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import lombok.SneakyThrows;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: t_syjzb
 * @Author: jeecg-boot
 * @Date: 2021-03-12
 * @Version: V1.0
 */
@Service
public class TSyjzbServiceImpl extends ServiceImpl<TSyjzbMapper, TSyjzb> implements ITSyjzbService {
    @Resource
    private TSyjzbMapper tSyjzbMapper;
    @Resource
    private FYalijiMapper fYalijiMapper;
    @Resource
    private FWangnjMapper fWangnjMapper;

    /**
     * 对压力机进行数据查询
     *
     * @param pageNo     起始页
     * @param pageSize   每页所显示的数据量
     * @param sbbh       设备名称
     * @param pdjg       是否合格
     * @param syrq_begin 实验日期范围
     * @param syrq_end   结束实验日期范围
     * @return
     */
    @Override
    public IPage<TSyjzb> queryMapperAnd(Integer pageNo, Integer pageSize, String sbbh,
                                        String pdjg, Date syrq_begin, Date syrq_end, String shebeis, String sylx) {
        Page<TSyjzb> page = new Page<>(pageNo, pageSize);
        return tSyjzbMapper.queryMapperAnd(page, sbbh, pdjg, syrq_begin, syrq_end, shebeis, sylx);
    }

    /**
     * 对压力机超标数据查询
     *
     * @param pageNo        起始页
     * @param pageSize      每页所显示的数据量
     * @param sbbh_dictText 设备名称
     * @param pdjg          是否合格
     * @param syrq_begin    实验日期范围
     * @param syrq_end      结束实验日期范围
     * @return
     */
    @Override
    public IPage<TSyjzb> queryMapperService(Integer pageNo, Integer pageSize, String sbbh_dictText, String pdjg,
                                            Date syrq_begin, Date syrq_end, String shebei, String shebeis) {
        Page<TSyjzb> page = new Page<>(pageNo, pageSize);
        return tSyjzbMapper.queryMapperService(page, sbbh_dictText, pdjg, syrq_begin, syrq_end, shebei, shebeis);
    }

    /**
     * 对压力机超标数据处理
     *
     * @param pageNo        起始页
     * @param pageSize      每页所显示的数据量
     * @param sbbh_dictText 设备名称
     * @param pdjg          是否合格
     * @param syrq_begin    实验日期范围
     * @param syrq_end      结束实验日期范围
     * @return
     */
    @Override
    public IPage<TSyjzb> queryMapperServicecl(Integer pageNo, Integer pageSize, String sbbh_dictText, String pdjg,
                                              Date syrq_begin, Date syrq_end, String shebei, String shebeis) {
        Page<TSyjzb> page = new Page<>(pageNo, pageSize);
        return tSyjzbMapper.queryMapperServicecl(page, sbbh_dictText, pdjg, syrq_begin, syrq_end, shebei, shebeis);
    }

    /**
     * 对抗压抗折超标数据查询
     *
     * @param pageNo        起始页
     * @param pageSize      每页所显示的数据量
     * @param sbbh_dictText 设备名称
     * @param syrq_begin    实验日期范围
     * @param syrq_end      结束实验日期范围
     * @return
     */
    @Override
    public IPage<TSyjzb> defaultBtester(Integer pageNo, Integer pageSize, String sbbh_dictText, String pdjg, Date syrq_begin, Date syrq_end, String shebeis, String sylx) {
        Page<TSyjzb> page = new Page<>(pageNo, pageSize);
        return tSyjzbMapper.defaultBtester(page, sbbh_dictText, pdjg, syrq_begin, syrq_end, shebeis, sylx);
    }

    /**
     * 查询万能机的数据
     *
     * @param
     * @return
     */
    @Override
    public IPage<TSyjzb> queryWnjPageList(Integer pageNo, Integer pageSize, String sbbh, String pdjg, Date syrq_begin, Date syrq_end, String shebeis, String sylx) {
        Page<TSyjzb> page1 = new Page<>(pageNo, pageSize);
        return tSyjzbMapper.queryWnjPageList(page1, sbbh, pdjg, syrq_begin, syrq_end, shebeis, sylx);
    }

    /**
     * 查询万能机超标数据
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public IPage<TSyjzb> queryWnjChaoBiaoPageList(Integer pageNo, Integer pageSize, String sbbh, String pdjg, Date syrq_begin, Date syrq_end, String shebeis) {
        Page<TSyjzb> page1 = new Page<>(pageNo, pageSize);
        return tSyjzbMapper.queryWnjChaoBiaoPageList(page1, sbbh, pdjg, syrq_begin, syrq_end, shebeis);
    }

    /**
     * 查询万能机超标数据
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public IPage<TSyjzb> queryWnjChaoBiaoCLPageList(Integer pageNo, Integer pageSize, String sbbh, String pdjg, Date syrq_begin, Date syrq_end, String shebeis) {
        Page<TSyjzb> page1 = new Page<>(pageNo, pageSize);
        return tSyjzbMapper.queryWnjChaoBiaoCLPageList(page1, sbbh, pdjg, syrq_begin, syrq_end, shebeis);
    }

    /**
     * 对抗压抗折超标数据查询
     *
     * @param pageNo        起始页
     * @param pageSize      每页所显示的数据量
     * @param sbbh_dictText 设备名称
     * @param syrq_begin    实验日期范围
     * @param syrq_end      结束实验日期范围
     * @return
     */
    @Override
    public IPage<TSyjzb> defaultBtester2(Integer pageNo, Integer pageSize, String sbbh_dictText, Date syrq_begin, Date syrq_end, String shebeis) {
        Page<TSyjzb> page = new Page<>(pageNo, pageSize);
        return tSyjzbMapper.defaultBtester2(page, sbbh_dictText, syrq_begin, syrq_end, shebeis);
    }

    /**
     * 对抗压抗折超标数据查询
     *
     * @param pageNo        起始页
     * @param pageSize      每页所显示的数据量
     * @param sbbh_dictText 设备名称
     * @param syrq_begin    实验日期范围
     * @param syrq_end      结束实验日期范围
     * @return
     */
    @Override
    public IPage<TSyjzb> defaultBtesterdeal(Integer pageNo, Integer pageSize, String sbbh_dictText, Date syrq_begin, Date syrq_end, String shebeis) {
        Page<TSyjzb> page = new Page<>(pageNo, pageSize);
        return tSyjzbMapper.defaultBtesterdeal(page, sbbh_dictText, syrq_begin, syrq_end, shebeis);
    }

    /**
     * 对恒应力一体机机进行数据查询
     *
     * @param pageNo     起始页
     * @param pageSize   每页所显示的数据量
     * @param sbbh       设备名称
     * @param pdjg       是否合格
     * @param syrq_begin 实验日期范围
     * @param syrq_end   结束实验日期范围
     * @return
     */
    @Override
    public IPage<TSyjzb> queryMapperhylytjList(Integer pageNo, Integer pageSize, String sbbh,
                                               String pdjg, Date syrq_begin, Date syrq_end, String shebeis) {
        Page<TSyjzb> page = new Page<>(pageNo, pageSize);
        return tSyjzbMapper.queryMapperhylytjList(page, sbbh, pdjg, syrq_begin, syrq_end, shebeis);

    }

    /**
     * 对压力机超标数据查询
     *
     * @param pageNo        起始页
     * @param pageSize      每页所显示的数据量
     * @param sbbh_dictText 设备名称
     * @param pdjg          是否合格
     * @param syrq_begin    实验日期范围
     * @param syrq_end      结束实验日期范围
     * @return
     */
    @Override
    public IPage<TSyjzb> queryMapperhylytjList1(Integer pageNo, Integer pageSize, String sbbh_dictText, String pdjg,
                                                Date syrq_begin, Date syrq_end, String shebeis) {
        Page<TSyjzb> page = new Page<>(pageNo, pageSize);
        return tSyjzbMapper.queryMapperhylytjList1(page, sbbh_dictText, pdjg, syrq_begin, syrq_end, shebeis);
    }

    /**
     * 对压力机超标数据查询
     *
     * @param pageNo        起始页
     * @param pageSize      每页所显示的数据量
     * @param sbbh_dictText 设备名称
     * @param pdjg          是否合格
     * @param syrq_begin    实验日期范围
     * @param syrq_end      结束实验日期范围
     * @return
     */
    @Override
    public IPage<TSyjzb> queryMapperhylytjdealList(Integer pageNo, Integer pageSize, String sbbh_dictText, String pdjg,
                                                   Date syrq_begin, Date syrq_end, String shebeis) {
        Page<TSyjzb> page = new Page<>(pageNo, pageSize);
        return tSyjzbMapper.queryMapperhylytjdealList(page, sbbh_dictText, pdjg, syrq_begin, syrq_end, shebeis);
    }

    @Override
    @Transactional
    public void saveMainWNJ(TSyjzb tSyjzb, List<fswangnj> fsWangnjList) {
        tSyjzbMapper.insert(tSyjzb);
        if (fsWangnjList != null && fsWangnjList.size() > 0) {
            for (fswangnj fsWangnj : fsWangnjList) {
                fsWangnj.setSyjid(tSyjzb.getSyjid());
                fWangnjMapper.insert(fsWangnj);
            }
        }
    }

    @Override
    @Transactional
    public void saveMain(TSyjzb tSyjzb, List<FsYaliji> fYalijiList) {
        tSyjzbMapper.insert(tSyjzb);
        if (fYalijiList != null && fYalijiList.size() > 0) {
            for (FsYaliji entity : fYalijiList) {
                //外键设置
                entity.setSyjid(tSyjzb.getSyjid());
                fYalijiMapper.insert(entity);
            }
        }
    }

    /**
     * APP试验机首页统计  万能机/压力机/标养室/抗压抗折 总数/不合格数/合格数统计
     *
     * @return
     */
    @Override
    public Map stsPageLists(String shebeis) {

        return tSyjzbMapper.stsPageLists(shebeis);
    }

    /**
     * APP试验机首页统计  万能机/压力机/标养室/抗压抗折 总数/不合格数/合格数统计
     *
     * @return
     */
    @Override
    public Map stsPageLists1(String shebeis) {
        return tSyjzbMapper.stsPageLists1(shebeis);
    }

    /**
     * APP试验机首页统计  万能机/压力机/标养室/抗压抗折 总数/不合格数/合格数统计
     *
     * @return
     */
    @Override
    public Map stsPageLists2(String shebeis) {
        return tSyjzbMapper.stsPageLists2(shebeis);
    }


    /**
     * APP试验机首页统计  万能机/压力机/标养室/抗压抗折 总数/不合格数/合格数统计/处置数
     *
     * @return
     */
    @Override
    public Map stsPageLists3(String shebeis) {
        return tSyjzbMapper.stsPageLists3(shebeis);
    }

    @Override
    public Map stsPageLists4(String shebeis) {
        return tSyjzbMapper.stsPageLists4(shebeis);
    }

    @Override
    public Map stsPageLists5(String shebeis) {
        return tSyjzbMapper.stsPageLists5(shebeis);
    }

    @Override
    public List<TSyjzb> selectList(String shebei, Integer id) {
//        try {
//            QueryWrapper<TSyjzb> queryWrapper=new QueryWrapper<>();
//            queryWrapper.gt("id",id);
//            queryWrapper.in("SBBH",shebei);
//            queryWrapper.last("limit 100");
//            return this.list(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return tSyjzbMapper.selectSyjList(shebei, id);
    }

    @Override
    public List<TSyjzb> selectsyjonesstatistics(Integer id, Integer statistics) {
        return tSyjzbMapper.selectsyjonesstatistics(id, statistics);
    }

    @Override
    public int updateSyjzbOneStatistics(String syjid, int statistics) {
        return tSyjzbMapper.updateSyjzbOneStatistics(syjid, statistics);
    }

    @Override
    public TSyjzb queryone(String shebeiNo) {
        return tSyjzbMapper.queryone(shebeiNo);
    }


    @Override
    public List<Map<String, Object>> countList(String[] categorys) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        return tSyjzbMapper.countList(loginUser.getOrgCode() + "%", categorys);
    }

    @Override
    public List<TSyjzb> selectLists(String shebeino, Integer id) {
        return tSyjzbMapper.selectLists(shebeino, id);
    }

    @Override
    public List<TSyjzb> selectSyjListzlpz(String shebeino) {
        return tSyjzbMapper.selectSyjListzlpz(shebeino);
    }

    @Override
    public List<TSyjzb> selectSyjListytwnd(String shebeino) {
        return tSyjzbMapper.selectSyjListytwnd(shebeino);
    }

    @Override
    public List<TSyjzb> selectListData(String shebeilist) {
        return tSyjzbMapper.selectListData(shebeilist);
    }

    @Override
    public TSyjzb getsjbhData(String sjbh, String syjid, String sbbh) {
        return tSyjzbMapper.getsjbhData(sjbh, syjid, sbbh);
    }

    @Override
    public List<TSyjzb> selectListone(String shebeilist) {
        return tSyjzbMapper.selectListone(shebeilist);
    }

    @Override
    public List<TSyjzb> selectSyjList(String split, Integer curid) {
        return tSyjzbMapper.selectSyjList(split, curid);
    }

    @Override
    public List<TSyjzb> selectSyjylList(String split, Integer curid) {
        return tSyjzbMapper.selectSyjylList(split, curid);
    }

    @Override
    public List<TSyjzb> selectSyjwnList(String split, Integer curid) {
        return tSyjzbMapper.selectSyjwnList(split, curid);
    }

    @Override
    public List<TSyjzb> selectSyjListbl(String split, Integer curid) {
        return tSyjzbMapper.selectSyjListbl(split, curid);
    }

    @Override
    public List<TSyjzb> selectStsqList(String split, Integer curid) {
        return tSyjzbMapper.selectStsqList(split, curid);
    }

    @Override
    public List<TSyjzb> selectStsqList1(String split, Integer curid) {
        return tSyjzbMapper.selectStsqList1(split, curid);
    }

    @Override
    public List<TSyjzb> selectSTlist(String shebeilist, Integer curid) {
        return tSyjzbMapper.selectSTlist(shebeilist, curid);
    }

    @Override
    public List<TSyjzb> selectYJQSlist(String shebeilist, Integer curid) {
        return tSyjzbMapper.selectYJQSlist(shebeilist, curid);
    }

    @Override
    public List<TSyjzb> selectzj(String shebeilist, Integer curid) {
        return tSyjzbMapper.selectzj(shebeilist, curid);
    }

    @Override
    public TSyjzb selectBySyjid(String syjid) {
        return tSyjzbMapper.selectBySyjid(syjid);
    }

    /**
     * 品茗初始化混凝土抗压实验数据推送
     *
     * @param shebeilist
     * @param curid
     * @return
     */
    @Override
    public List<SyjzbVo> queryLists(String sylx, String shebeilist, Integer curid) {
        return tSyjzbMapper.queryLists(sylx, shebeilist, curid);
    }


    @Override
    public List<String> querySylx(Integer sbtype, String sysorgcode) {
        return tSyjzbMapper.querySylx(sbtype, sysorgcode);
    }

    @Override
    public List<TSyjzb> getCountWarning() {
        return tSyjzbMapper.getCountWarning();
    }

    @Override
    public List<TSyjzb> getCountBH() {
        return tSyjzbMapper.getCountBH();
    }

    @Override
    public List<TSyjzb> getCountWarningByOC(String orgCode) {
        return tSyjzbMapper.getCountWarningByOC(orgCode);
    }

    @Override
    public List<TSyjzb> getCountBHByOC(String orgCode) {
        return tSyjzbMapper.getCountBHByOC(orgCode);
    }

    /**
     * 获取今日前number日的日期
     *
     * @return
     */
    public List<String> getDayFromToday() {
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            List<String> lsDate = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                c.setTime(new Date());
                c.add(Calendar.DATE, -i);
                Date d = c.getTime();
                lsDate.add(sdf.format(d));
                Collections.sort(lsDate);
            }
            return lsDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getDepartName(String sysOrgCode) {
        String departParentId = tSyjzbMapper.getDepartParentId(sysOrgCode);//通过部门编号获取到部门的父id
        SysDepart depart = tSyjzbMapper.getDepart(departParentId);//id=父id找到上一级部门信息
        while (!"4".equals(depart.getOrgCategory())) {//判断部门的机构类别是否为项目级(category=4)
            depart = tSyjzbMapper.getDepart(depart.getParentId());
        }
        return depart.getDepartName();
    }

    @Override
    public String getSbtypeName(Integer sbtype) {
        String sbtypeName = null;
        switch (sbtype) {
            case 3:
                sbtypeName = "万能试验机";
                break;
            case 4:
                sbtypeName = "压力试验机";
                break;
            case 12:
                sbtypeName = "抗压抗折试验机";
                break;
        }
        return sbtypeName;
    }

    @Override
    public List<TSyjzb> selectListSy(Integer curid, List<String> strsToList1) {
        return tSyjzbMapper.selectListSy(curid, strsToList1);
    }

    @Override
    public String selectSylxName(String sylx) {
        return tSyjzbMapper.selectSylxName(sylx);
    }

    @Override
    public List<TSyjzb> selectSYJData(Integer curid, String shebeilist) {
        return tSyjzbMapper.selectSYJData(curid, shebeilist);
    }

    @Override
    public List<fswangnj> selectWnjList(String syjid) {
        return tSyjzbMapper.selectWnjList(syjid);
    }

    @Override
    public List<FsYaliji> selectYljList(String syjid) {
        return tSyjzbMapper.selectYljList(syjid);
    }

    @Override
    public List<TSyjzb> selectSYJYuJingData(Integer curid, String shebeilist) {
        return tSyjzbMapper.selectSYJYuJingData(curid, shebeilist);
    }

    @Override
    public Map getPsPicBySyjid(Integer id) {
        FYaliji ylj = tSyjzbMapper.getYljById(id);
        Map<String, String> map = new HashMap<>();
        String faceid = tSyjzbMapper.getFacePic(ylj.getSyjid());
        if (ObjectUtil.isEmpty(faceid)) {
            map.put("facePic", "暂无图片");
        } else {
            Attachlist attach0 = tSyjzbMapper.getAttach(faceid);
           // String rlsb = "http://web.traiot.cn/docs/wz/" + tSyjzbMapper.getFacePic(ylj.getSyjid());//人脸识别
            String rlsb = "http://web.traiot.cn/docs" + attach0.getRelativepath();//人脸识别
            map.put("facePic", rlsb);
        }
        FsYaliji yaliji = tSyjzbMapper.selectYlj(ylj.getSyjid(), ylj.getSjxh());
        Attachlist attach1 = tSyjzbMapper.getAttach(yaliji.getSpic());
        if (ObjectUtil.isNotEmpty(attach1)) {
            if (ObjectUtil.isEmpty(attach1.getRelativepath())) {
                map.put("psqPic", "暂无图片");
            } else {
                String psqPic = "http://web.traiot.cn/docs" + attach1.getRelativepath();//破碎前图片
                map.put("psqPic", psqPic);
            }
        }else {
            map.put("psqPic", "暂无图片");
        }
        Attachlist attach2 = tSyjzbMapper.getAttach(yaliji.getPspic());
        if (ObjectUtil.isNotEmpty(attach2)) {
            if (ObjectUtil.isEmpty(attach2.getRelativepath())) {
                map.put("pshPic", "暂无图片");
            } else {
                String pshPic = "http://web.traiot.cn/docs" + attach2.getRelativepath();//破碎后图片
                map.put("pshPic", pshPic);
            }
        }else {
            map.put("pshPic", "暂无图片");
        }

        Attachlist attach3 = tSyjzbMapper.getAttach(yaliji.getVideopic());
        if (ObjectUtil.isNotEmpty(attach3)) {
            if (ObjectUtil.isEmpty(attach3.getRelativepath())) {
                map.put("VideoPic", "暂无视频");
            } else {
                String VideoPic = "http://web.traiot.cn/docs" + attach3.getRelativepath();//试验视频
                map.put("VideoPic", VideoPic);
            }
        }else {
            map.put("VideoPic", "暂无视频");
        }
        return map;
    }

    @Override
    public List<bzc> getBzcList(Integer pageNo, Integer pageSize,String shebei,String lq,String sjqd ,String stadate,String enddate) {
        return tSyjzbMapper.getBzcList(pageNo, pageSize, shebei, lq, sjqd, stadate, enddate);
    }

    @Override
    public int getBzcListcount(String shebei,String lq,String sjqd ,String stadate,String enddate) {
        return tSyjzbMapper.getBzcListcount(shebei, lq, sjqd, stadate, enddate);
    }

    @Override
    public List<TSyjzb> selectListToJTJT(String shebeilist, Integer curid) {
        return tSyjzbMapper.selectListToJTJT(shebeilist, curid);
    }

    @Override
    public List<TSyjzb> seletListPanan351(String shebeilist) {
        return tSyjzbMapper.seletListPanan351(shebeilist);
    }

    @SneakyThrows
    @Override
    public String getQrcode(String qrcode) {
        qrcode = URLEncoder.encode("9039-159", "UTF-8");
        String url = "http://121.40.163.88:8085/CATDPS/samp.do?getQrCode&qrcode=" + qrcode;
        String body = GET(url);
        return body;
    }
    public static String GET(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);

            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();

            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // 建立实际的连接
            connection.connect();

            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();

            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }

        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}
