package org.jeecg.modules.systems;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.anquanfxgk.entity.AnquanFxaqjcInfo;
import com.trtm.iot.anquanfxgk.service.IAnquanFxaqjcInfoService;
import com.trtm.iot.bhzrwdxx.entity.Bhzrwdxx;
import com.trtm.iot.bhzrwdxx.service.IBhzrwdxxService;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.renwudan.entity.RenwudanSchedule;
import com.trtm.iot.renwudan.service.IRenwudanScheduleService;
import com.trtm.iot.system.entity.*;
import com.trtm.iot.system.service.IBhzrenwudanService;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.system.service.IShigongphbService;
import com.trtm.iot.wbshebeidetail.entity.WbshebeiDetail;
import com.trtm.iot.wbshebeidetail.service.IWbshebeiDetailService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.xkcoding.http.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.jeecg.common.util.DateUtils.date2Str;

/**
 * @Description: 消息
 * @author: jeecg-boot
 * @date: 2019-04-09
 * @version: V1.0
 */
@Slf4j
@RestController
@RequestMapping("/sys/systems/api")
public class ApiSystemController extends JeecgController<SysMessage, ISysMessageService> {
    @Autowired
    private ISysMessageService sysMessageService;

    @Autowired
    private IBhzrwdxxService bhzrwdxxService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IBhzrenwudanService bhzrenwudanService;
    @Autowired
    private IRenwudanScheduleService renwudanScheduleService;
    @Autowired
    private IShigongphbService shigongphbService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Autowired
    private IWbshebeiDetailService wbshebeiDetailService;

    @Autowired
    private ISysDepartService sysDepartService;

    @Autowired
    private IAnquanFxaqjcInfoService anquanFxaqjcInfoService;

    @Autowired
    private IWzliaocangService wzliaocangService;
    /**
     * 编辑
     *
     * @param shebeiInfo
     * @return
     */
    @AutoLog(value = "设备审核表-编辑")
    @ApiOperation(value = "设备审核表-编辑", notes = "设备审核表-编辑")
    @PutMapping(value = "/shebeiedit")
    public Result<?> edit(@RequestBody ShebeiInfo shebeiInfo) {
        if(StringUtils.isBlank(shebeiInfo.getProjectid())){
            SysDepart depart = sysDepartService.queryone(shebeiInfo.getSysOrgCode());
            shebeiInfo.setProjectid(depart.getProjectid());
            shebeiInfo.setSectionid(depart.getSectionid());
        }
        shebeiInfoService.updateById(shebeiInfo);
        return Result.OK("编辑成功!");
    }

    @AutoLog(value = "设备审核表-添加")
    @ApiOperation(value = "设备审核表-添加", notes = "设备审核表-添加")
    @PostMapping(value = "/shebeiadd")
    public Result<?> add(@RequestBody ShebeiInfo shebeiInfo) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前登录人信息不在前端页面去选择
        if(StringUtils.isBlank(shebeiInfo.getProjectid())){
            SysDepart depart = sysDepartService.queryone(shebeiInfo.getSysOrgCode());
            shebeiInfo.setProjectid(depart.getProjectid());
            shebeiInfo.setSectionid(depart.getSectionid());
        }
        shebeiInfo.setCreateBy(sysUser.getUsername());
        shebeiInfo.setCreateTime(new Date());
        String sysOrgCode = shebeiInfo.getSysOrgCode();
        Integer sbtype = shebeiInfo.getSbtype();
        /**下面是根据id直接拼接设备编号**/
        String shesbjno = null;
        if (shebeiInfo.getSbjno() == null || shebeiInfo.getSbjno().equals("默认不写会自动生成编号")) {
            String shebeisbjno = String.valueOf(FillRuleUtil.shebeisbjno(sysOrgCode, sbtype));
            List<ShebeiInfo> shebeiInfos = shebeiInfoService.arrayOneshebeis();
            Integer id = 0;
            for (ShebeiInfo info : shebeiInfos) {
                id = info.getTestid();
            }
            id = id + 1;
            shebeiInfo.setTestid(id);
            String format = String.format("%04d", id);
            shesbjno = shebeisbjno + "_" + format;
        } else {
            shesbjno = shebeiInfo.getSbjno();
        }

        /**上面是根据id直接拼接设备编号**/
        shebeiInfo.setSbjno(shesbjno);
        shebeiInfoService.save(shebeiInfo);
        return Result.OK("添加成功！");
    }

    /**
     * 分页列表查询
     *
     * @param shigongphb
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "施工配合比-分页列表查询")
    @ApiOperation(value = "施工配合比-分页列表查询", notes = "施工配合比-分页列表查询")
    @GetMapping(value = "/list3")
    public Result<?> queryPageList3(Shigongphb shigongphb,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req, Integer id) {
        if (id != null) {
            shigongphb.setId(id);
        }
        shigongphb.setIsdel(0);
        QueryWrapper<Shigongphb> queryWrapper = QueryGenerator.initQueryWrapper(shigongphb, req.getParameterMap());
        Page<Shigongphb> page = new Page<Shigongphb>(pageNo, pageSize);
        IPage<Shigongphb> pageList = shigongphbService.page(page, queryWrapper);
        List<Shigongphb> shigongphbs = pageList.getRecords();
        List data = new ArrayList<>();
        for (Shigongphb shigongphb1 : shigongphbs) {
            String shebeibianhao = shigongphb1.getShebeibianhao();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeibianhao);
            Bhzrenwudan bhzrenwudan = bhzrenwudanService.selectstation(shigongphb1.getRenwudan());
            shigongphb1.setShebeibianhao(selectshebeione.getSbname());
            LoginUser userByName = sysBaseAPI.getUserByName(shigongphb1.getCreateBy());
            shigongphb1.setCreateBy(userByName.getRealname());
            if (bhzrenwudan != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                shigongphb1.setM32(!ObjectUtil.isNull(bhzrenwudan.getBegtim()) ?simpleDateFormat.format(bhzrenwudan.getBegtim()):"");
                LoginUser userByNamejzl = sysBaseAPI.getUserByName(bhzrenwudan.getCreateBy());
                shigongphb1.setM31(userByNamejzl.getRealname());
            }
            if(StringUtils.isNotBlank(shigongphb1.getShren())){
                LoginUser userByNameshr = sysBaseAPI.getUserByName(shigongphb1.getShren());
                shigongphb1.setShren(userByNameshr.getRealname());
            }
            data.add(shigongphb1);
        }
        return Result.OKs(data);
    }


    @AutoLog(value = "电子锁详情数据表-分页列表查询 - 地磅")
    @ApiOperation(value = "电子锁详情数据表-分页列表查询", notes = "电子锁详情数据表-分页列表查询")
    @GetMapping(value = "/wbshebeiList")
    public Result<?> queryPageList(WbshebeiDetail wbshebeiDetail,String departid,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {

        List<JSONObject> jsonObjects = sysBaseAPI.queryDepartsByIds(departid);
        String scode ="-";
        if(jsonObjects.size()>0){
            scode = (String) jsonObjects.get(0).get("orgCode");
        }
     //   wbshebeiDetail.setUserdepartid(scode+"*");
        List<String> selecegcsb = shebeiInfoService.selectSbjnoListLikeOrgcode(scode,35 );
        QueryWrapper<WbshebeiDetail> queryWrapper = QueryGenerator.initQueryWrapper(wbshebeiDetail, req.getParameterMap());
         queryWrapper.in("sbbh",selecegcsb);

        Page<WbshebeiDetail> page = new Page<WbshebeiDetail>(pageNo, pageSize);
        IPage<WbshebeiDetail> pageList = wbshebeiDetailService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    @AutoLog(value = "地磅端 过磅 电子锁详情数据表-编辑")
    @ApiOperation(value = "电子锁详情数据表-编辑", notes = "电子锁详情数据表-编辑")
    @PostMapping(value = "/wbshebeiEdit")
    public Result<?> edit(@RequestBody WbshebeiDetail wbshebeiDetail) {
        // 收获任务单改成 shrwdh  地磅数据；修改 sfcssj 电子锁关联地磅状态；自动到达 isjiesuo:1
        wbshebeiDetailService.updateById(wbshebeiDetail);
        return Result.OK("编辑成功!");
    }

    @AutoLog(value = "地磅端 过磅 电子锁详情数据表-编辑")
    @ApiOperation(value = "电子锁详情数据表-编辑", notes = "电子锁详情数据表-编辑")
    @GetMapping(value = "/wbshebeiliaocang")
    public Result<?> wbshebeiliaocang(String orgcode, String name) {
        // 获取最新生产中的配合比
        List<String> strings = shebeiInfoService.selectSbjnoListLikeOrgcode(orgcode, 0);// 查询对应拌合站信息
        String lastPhb = bhzCementBaseService.getLastPhb(strings);
        if(StringUtils.isNotBlank(lastPhb)){
            Shigongphb shigongphb = shigongphbService.queryoneCode(lastPhb);
            // 对比查询骨料仓列表
            // 匹配料斗名称并返回 料斗号、料仓名称、批次编号
            WZLiaodou cailiao = getCailiao(shigongphb, name);
            return Result.OK(cailiao);
        }else{
            return Result.OK("未获取到最新生产配比");
        }

    }

    public WZLiaodou getCailiao(Shigongphb phb, String name){
        if(phb != null){
           if (phb.getLc4() != null) {
               WZLiaodou m4 = new WZLiaodou();
                Wzliaocang loudouM4 = wzliaocangService.queryselectone(phb.getLc4());
                m4.setName(loudouM4.getName());// 料仓名称
                // m4.setGuige(loudouM4.getGuigexinghao());// 规格
                m4.setLcbh(loudouM4.getCailiaoname()); // 料斗号
                if (StringUtils.isNotBlank(phb.getPici1())) { m4.setPici(phb.getPici4()); }
                else { m4.setPici(loudouM4.getPici()); }//批次

                if(name.equals(loudouM4.getCailiaoname())){
                    return m4;
                }
            } if (phb.getLc5() != null) {
                WZLiaodou m5 = new WZLiaodou();
                Wzliaocang loudouM5 = wzliaocangService.queryselectone(phb.getLc5());
                m5.setName(loudouM5.getName());// 料仓名称
                m5.setLcbh(loudouM5.getCailiaoname()); // 料斗号
                if (StringUtils.isNotBlank(phb.getPici5())) { m5.setPici(phb.getPici5()); }
                else { m5.setPici(loudouM5.getPici()); }//批次

                if(name.equals(loudouM5.getCailiaoname())){
                    return m5;
                }
            } if (phb.getLc6() != null) {
                WZLiaodou m6 = new WZLiaodou();
                Wzliaocang loudouM6 = wzliaocangService.queryselectone(phb.getLc6());
                m6.setName(loudouM6.getName());// 料仓名称
                m6.setLcbh(loudouM6.getCailiaoname()); // 料斗号
                if (StringUtils.isNotBlank(phb.getPici6())) { m6.setPici(phb.getPici6()); }
                else { m6.setPici(loudouM6.getPici()); }//批次

                if(name.equals(loudouM6.getCailiaoname())){
                    return m6;
                }
            } if (phb.getLc7() != null) {
                WZLiaodou m7 = new WZLiaodou();
                Wzliaocang loudouM7 = wzliaocangService.queryselectone(phb.getLc7());
                m7.setName(loudouM7.getName());// 料仓名称
             //   m7.setGuige(loudouM7.getGuigexinghao());// 规格
                m7.setLcbh(loudouM7.getCailiaoname()); // 料斗号
                if (StringUtils.isNotBlank(phb.getPici7())) { m7.setPici(phb.getPici7()); }
                else { m7.setPici(loudouM7.getPici()); }//批次

                if(name.equals(loudouM7.getCailiaoname())){
                    return m7;
                }
            } if (phb.getLc12() != null) {
                WZLiaodou m12 = new WZLiaodou();
                Wzliaocang loudouM12 = wzliaocangService.queryselectone(phb.getLc12());
                m12.setName(loudouM12.getName());// 料仓名称
              //  m12.setGuige(loudouM12.getGuigexinghao());// 规格
                m12.setLcbh(loudouM12.getCailiaoname()); // 料斗号
                if (StringUtils.isNotBlank(phb.getPici12())) { m12.setPici(phb.getPici12()); }
                else { m12.setPici(loudouM12.getPici()); }//批次

                if(name.equals(loudouM12.getCailiaoname())){
                    return m12;
                }
            }   if (phb.getLc1() != null) {
                WZLiaodou m1 = new WZLiaodou();
                Wzliaocang loudouM1 = wzliaocangService.queryselectone(phb.getLc1());
                m1.setName(loudouM1.getName());// 料仓名称
               // m1.setGuige(loudouM1.getGuigexinghao());// 规格
                m1.setLcbh(loudouM1.getCailiaoname()); // 料斗号
                if (StringUtils.isNotBlank(phb.getPici1())) { m1.setPici(phb.getPici1()); }
                else { m1.setPici(loudouM1.getPici()); }//批次

                if(name.equals(loudouM1.getCailiaoname())){
                    return m1;
                }
            }


        }
        WZLiaodou liaodou =   new WZLiaodou();
        liaodou.setLcbh(name);
        liaodou.setPici("当前未进行生产");
        return liaodou;
    }

    @AutoLog(value = "bhzrwdxx-分页列表查询")
    @ApiOperation(value = "bhzrwdxx-分页列表查询", notes = "bhzrwdxx-分页列表查询")
    @GetMapping(value = "/rwdprocess3")
    public Result<?> rwdprocess3(Bhzrenwudan bhzrenwudan,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                 String sys_depart_orgcode,
                                 HttpServletRequest req) {
        ShebeiInfo selectshebeione = new ShebeiInfo();
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            bhzrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
        bhzrenwudan.setConspos("*" + bhzrenwudan.getConspos() + "*");
        bhzrenwudan.setIsdel(0);
        QueryWrapper<Bhzrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(bhzrenwudan, req.getParameterMap());
        Page<Bhzrenwudan> page = new Page<Bhzrenwudan>(pageNo, pageSize);
        IPage<Bhzrenwudan> pageList = bhzrenwudanService.page(page, queryWrapper);
        List<Map<String, Object>> rwdprosess = new ArrayList<>();
        if (pageList.getSize() > 0) {
            Bhzrenwudan rwd = pageList.getRecords().get(0);
            Bhzrwdxx bhzrwdxx = new Bhzrwdxx();
            QueryWrapper<RenwudanSchedule> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.select("Code,EndTim,Starttim,shebei_no,sum( Metes ) AS Metes,Mete");
            queryWrapper1.eq("Code", rwd.getCode());
            queryWrapper1.eq("sys_org_code", rwd.getSysOrgCode());
//            queryWrapper1.last("group by Code,sys_org_code");
            RenwudanSchedule one = renwudanScheduleService.getOne(queryWrapper1);
            BeanUtils.copyProperties(rwd,bhzrwdxx);
            bhzrwdxx.setRwdcode(rwd.getCode());
            bhzrwdxx.setJzlsts(rwd.getStatus());
            if(one!=null){
                bhzrwdxx.setShebeiNo(one.getShebeiNo());
                bhzrwdxx.setStarttim(one.getStarttim());
                bhzrwdxx.setEndtim(one.getEndtim());
                bhzrwdxx.setMetes(one.getMetes());
                bhzrwdxx.setMete(one.getMete());
            }
            Map<String, Object> qita = new HashMap<>();
            Map<String, Object> create = new HashMap<>();
            create.put("tile", "创建");
            Map<String, Object> shenhe = new HashMap<>();
            shenhe.put("tile", "审核");
            Map<String, Object> peiliao = new HashMap<>();
            peiliao.put("tile", "配料");
            Map<String, Object> jlsh = new HashMap<>();
            jlsh.put("status", 0);
            jlsh.put("tile", "监理确认");

            Map<String, Object> shenchan = new HashMap<>();
            if (rwd.getStatus() == 6) {
                shenchan.put("tile", "生产已滞后");
            } else {
                shenchan.put("tile", "生产中");
            }
            Map<String, Object> wancheng = new HashMap<>();
            wancheng.put("tile", "已完成");

            create.put("time", bhzrwdxx.getDattim());
            LoginUser user1 = sysBaseAPI.getUserByName(bhzrwdxx.getCreateBy());
            create.put("person", user1.getRealname());
            create.put("content", "");
            create.put("status", 1);


            if (rwd.getStatus() > 0) {

                if(StringUtils.isNotBlank(rwd.getShren())){
                    LoginUser user2 = sysBaseAPI.getUserByName(rwd.getShren());
                    shenhe.put("person", user2.getRealname());
                    shenhe.put("time", rwd.getShshijian());
                    shenhe.put("content", "已通过审核");
                }else{
                    shenhe.put("person", "/");
                    shenhe.put("time", "/");
                }
                shenhe.put("status", 1);
            } else {
                shenhe.put("status", 0);
            }
            if (rwd.getStatus() > 1) {
                QueryWrapper<Shigongphb> phb = new QueryWrapper<>();
                phb.eq("renwudan", rwd.getCode());
                phb.eq("isdel", 0);
                List<Shigongphb> phblist = shigongphbService.list(phb);
                if (phblist.size() > 0) {
                    Shigongphb shigongphb = phblist.get(0);
                    peiliao.put("time", shigongphb.getDattim());
                    LoginUser user3 = sysBaseAPI.getUserByName(shigongphb.getCreateBy());
                    peiliao.put("person", user3.getRealname());
                    peiliao.put("content", shigongphb.getCode());
                    peiliao.put("status", 1);
                    if(shigongphb.getCheckStatus()!=null &&  shigongphb.getCheckStatus() == 0 ){
                        jlsh.put("content", "审核通过");
                        jlsh.put("status", 1);
                        jlsh.put("time", shigongphb.getShshijian());
                        LoginUser user4 = sysBaseAPI.getUserByName(shigongphb.getShren());
                        jlsh.put("person", user4.getRealname());
                    }else {
                        jlsh.put("content", "待监理确认配料单");
                        jlsh.put("status", 0);
                        jlsh.put("person","/");
                    }


                }
            } else {
                peiliao.put("status", 0);
            }

            if (null != bhzrwdxx.getShebeiNo()) {
                selectshebeione = shebeiInfoService.selectshebeione(bhzrwdxx.getShebeiNo());
            }
            if (bhzrwdxx.getJzlsts() > 3) {
                shenchan.put("time", bhzrwdxx.getStarttim());
                shenchan.put("person", selectshebeione.getSbname());
                shenchan.put("content", "计划方量" + rwd.getMete() + "方,已完成" + bhzrwdxx.getMetes() + "方");
                shenchan.put("status", 1);

            } else {
                shenchan.put("status", 0);
            }
            if (bhzrwdxx.getJzlsts() == 5) {
                wancheng.put("time", bhzrwdxx.getEndtim());
                wancheng.put("person", selectshebeione.getSbname());
                wancheng.put("content", "");
                wancheng.put("status", 1);

            } else {
                wancheng.put("status", 0);
            }

            rwdprosess.add(wancheng);
            rwdprosess.add(shenchan);
            rwdprosess.add(jlsh);
            rwdprosess.add(peiliao);
            rwdprosess.add(shenhe);
            rwdprosess.add(create);
        }

        return Result.OK(rwdprosess);
    }
    @AutoLog(value = "bhzrwdxx-分页列表查询")
    @ApiOperation(value = "bhzrwdxx-分页列表查询", notes = "bhzrwdxx-分页列表查询")
    @GetMapping(value = "/rwdprocess4")
    public Result<?> rwdprocess4(Bhzrenwudan bhzrenwudan,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                 String sys_depart_orgcode,
                                 HttpServletRequest req) {
        ShebeiInfo selectshebeione = new ShebeiInfo();
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            bhzrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
        bhzrenwudan.setConspos("*" + bhzrenwudan.getConspos() + "*");
        bhzrenwudan.setIsdel(0);
        QueryWrapper<Bhzrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(bhzrenwudan, req.getParameterMap());
        Page<Bhzrenwudan> page = new Page<Bhzrenwudan>(pageNo, pageSize);
        IPage<Bhzrenwudan> pageList = bhzrenwudanService.page(page, queryWrapper);
        List<Map<String, Object>> rwdprosess = new ArrayList<>();
        if (pageList.getSize() > 0) {
            Bhzrenwudan rwd = pageList.getRecords().get(0);
            Bhzrwdxx bhzrwdxx = new Bhzrwdxx();
            QueryWrapper<RenwudanSchedule> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.select("Code,EndTim,Starttim,shebei_no,sum( Metes ) AS Metes,Mete");
            queryWrapper1.eq("Code", rwd.getCode());
            queryWrapper1.eq("sys_org_code", rwd.getSysOrgCode());
//            queryWrapper1.last("group by Code,sys_org_code");
            RenwudanSchedule one = renwudanScheduleService.getOne(queryWrapper1);
            BeanUtils.copyProperties(rwd,bhzrwdxx);
            bhzrwdxx.setRwdcode(rwd.getCode());
            bhzrwdxx.setJzlsts(rwd.getStatus());
            if(one!=null){
                bhzrwdxx.setShebeiNo(one.getShebeiNo());
                bhzrwdxx.setStarttim(one.getStarttim());
                bhzrwdxx.setEndtim(one.getEndtim());
                bhzrwdxx.setMetes(one.getMetes());
                bhzrwdxx.setMete(one.getMete());
            }
            Map<String, Object> qita = new HashMap<>();
            Map<String, Object> create = new HashMap<>();
            create.put("tile", "创建");
//            Map<String, Object> shenhe = new HashMap<>();
//            shenhe.put("tile", "审核");
            Map<String, Object> peiliao = new HashMap<>();
            peiliao.put("tile", "配料");
//            Map<String, Object> jlsh = new HashMap<>();
//            jlsh.put("status", 0);
//            jlsh.put("tile", "监理确认");

            Map<String, Object> shenchan = new HashMap<>();
            if (rwd.getStatus() == 6) {
                shenchan.put("tile", "生产已滞后");
            } else {
                shenchan.put("tile", "生产中");
            }
            Map<String, Object> wancheng = new HashMap<>();
            wancheng.put("tile", "已完成");

            create.put("time", bhzrwdxx.getDattim());
            LoginUser user1 = sysBaseAPI.getUserByName(bhzrwdxx.getCreateBy());
            create.put("person", user1.getRealname());
            create.put("content", "");
            create.put("status", 1);



            if (rwd.getStatus() > 1) {
                QueryWrapper<Shigongphb> phb = new QueryWrapper<>();
                phb.eq("renwudan", rwd.getCode());
                phb.eq("isdel", 0);
                List<Shigongphb> phblist = shigongphbService.list(phb);
                if (phblist.size() > 0) {
                    Shigongphb shigongphb = phblist.get(0);
                    peiliao.put("time", shigongphb.getDattim());
                    LoginUser user3 = sysBaseAPI.getUserByName(shigongphb.getCreateBy());
                    peiliao.put("person", user3.getRealname());
                    peiliao.put("content", shigongphb.getCode());
                    peiliao.put("status", 1);
                }
            } else {
                peiliao.put("status", 0);
            }

            if (null != bhzrwdxx.getShebeiNo()) {
                selectshebeione = shebeiInfoService.selectshebeione(bhzrwdxx.getShebeiNo());
            }
            if (bhzrwdxx.getJzlsts() > 3) {
                shenchan.put("time", bhzrwdxx.getStarttim());
                shenchan.put("person", selectshebeione.getSbname());
                shenchan.put("content", "计划方量" + rwd.getMete() + "方,已完成" + bhzrwdxx.getMetes() + "方");
                shenchan.put("status", 1);

            } else {
                shenchan.put("status", 0);
            }
            if (bhzrwdxx.getJzlsts() == 5) {
                wancheng.put("time", bhzrwdxx.getEndtim());
                wancheng.put("person", selectshebeione.getSbname());
                wancheng.put("content", "");
                wancheng.put("status", 1);

            } else {
                wancheng.put("status", 0);
            }

            rwdprosess.add(wancheng);
            rwdprosess.add(shenchan);
            rwdprosess.add(peiliao);
            rwdprosess.add(create);
        }

        return Result.OK(rwdprosess);
    }

    @AutoLog(value = "安全检查-流程流转查询")
    @ApiOperation(value = "安全检查-流程流转查询", notes = "安全检查-流程流转查询")
    @GetMapping(value = "/checkprocess")
    public Result<?> checkprocess(AnquanFxaqjcInfo anquanFxaqjcInfo,
                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                String sys_depart_orgcode,
                                HttpServletRequest req) {
        List<Map<String, Object>> prosessall = new ArrayList<>();
        AnquanFxaqjcInfo byId = anquanFxaqjcInfoService.getById(anquanFxaqjcInfo.getId());
        Map<String, Object> process1 = new HashMap<>();
        process1.put("tile", "发现隐患");
        process1.put("status", 1);
        process1.put("people",byId.getCheckPeople());
        process1.put("time",byId.getCheckTime());
        process1.put("info","");

        Map<String, Object> process2 = new HashMap<>();
        process2.put("tile", "整改");
        if(byId.getHandlestatus()>0){
            process2.put("status", 1);
        }else{
            process2.put("status", 0);
        }
        process2.put("people",byId.getProcedure2People());
        process2.put("time",byId.getProcedure2Time());
        process2.put("info",byId.getProcedure2Reslut());

        prosessall.add(process1);
        prosessall.add(process2);
        if(byId.getProblemType().contains("隐患")){
            Map<String, Object> process3 = new HashMap<>();
            process3.put("tile", "督办审核");
            if(byId.getHandlestatus()>20){
                process3.put("status", 1);
            }else{
                process3.put("status", 0);
            }
            process3.put("people",byId.getProcedure1People());
            process3.put("time",byId.getProcedure1Time());
            process3.put("info",byId.getProcedure1Reslut());
            prosessall.add(process3);
            if(byId.getProblemType().contains("重⼤隐患")){
                Map<String, Object> process4 = new HashMap<>();
                process4.put("tile", "销号");
                if(byId.getHandlestatus()>30){
                    process4.put("status", 1);

                }else{
                    process4.put("status", 0);
                }
                process4.put("people",byId.getProcedure3People());
                process4.put("time",byId.getProcedure3Time());
                process4.put("info",byId.getProcedure3Reslut());
                prosessall.add(process4);
            }

        }


        return Result.OK(prosessall);
    }
}
