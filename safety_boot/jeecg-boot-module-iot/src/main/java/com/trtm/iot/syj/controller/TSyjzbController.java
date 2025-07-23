package com.trtm.iot.syj.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.bhzStatistics.service.IBhzCementStatisticsService;
import com.trtm.iot.bys.entity.BysReal;
import com.trtm.iot.bys.service.IBysRealService;
import com.trtm.iot.lqbhzStatistics.entity.BhzLqStatistics;
import com.trtm.iot.lqbhzStatistics.service.IBhzLqStatisticsService;
import com.trtm.iot.qrcode.entity.Qrcode;
import com.trtm.iot.swbhzStatistics.entity.BhzSwStatistics;
import com.trtm.iot.swbhzStatistics.service.IBhzSwStatisticsService;
import com.trtm.iot.syj.entity.*;
import com.trtm.iot.syj.service.IFWangnjService;
import com.trtm.iot.syj.service.IFYalijiService;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.syj.vo.*;
import com.trtm.iot.syjoverhandler.entity.SyjOverHandler;
import com.trtm.iot.syjoverhandler.service.ISyjOverHandlerService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.xkcoding.http.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.mathUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Description: t_syjzb
 * @Author: jeecg-boot
 * @Date: 2021-03-12
 * @Version: V1.0
 */
@Api(tags = "试验机主表")
@RestController
@RequestMapping("/syj/tSyjzb")
@Slf4j
public class TSyjzbController extends JeecgController<TSyjzb, ITSyjzbService> {
    @Autowired
    private ITSyjzbService tSyjzbService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IFYalijiService ifYalijiService;
    @Autowired
    private IFWangnjService ifWangnjService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ISyjOverHandlerService syjOverHandlerService;
    @Autowired
    private IBysRealService bysRealService;
    @Autowired
    private IBhzLqStatisticsService iBhzLqStatisticsService;
    @Autowired
    private IBhzSwStatisticsService bhzSwStatisticsService;
    @Autowired
    private IBhzCementStatisticsService bhzCementStatisticsService;


    @AutoLog(value = "t_syjzb-分页列表查询")
    @ApiOperation(value = "t_syjzb-分页列表查询", notes = "t_syjzb-分页列表查询")
    @GetMapping(value = "/listss")
    public Result<?> queryMappereas(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest request, TSyjzb tSyjzb) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (tSyjzb.getSbbh() == null) {
            if ("null".equals(shebei)) {
                shebei = "空";
            }
            tSyjzb.setSbbh(shebei);
        }
        QueryWrapper<TSyjzb> queryWrapper = QueryGenerator.initQueryWrapper(tSyjzb, request.getParameterMap());
        Page<TSyjzb> page = new Page<TSyjzb>(pageNo, pageSize);
        IPage<TSyjzb> pageList = tSyjzbService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "t_syjzb-分页列表查询")
    @ApiOperation(value = "t_syjzb-分页列表查询", notes = "t_syjzb-分页列表查询")
    @GetMapping(value = "/listsyjyj")
    public Result<?> queryMappereasyjyj(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest request, TSyjzb tSyjzb) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (tSyjzb.getSbbh() == null) {
            if ("null".equals(shebei)) {
                shebei = "空";
            }
            tSyjzb.setSbbh(shebei);
        }
        Date day=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String format = df.format(day);

        QueryWrapper<TSyjzb> queryWrapper = QueryGenerator.initQueryWrapper(tSyjzb, request.getParameterMap());
        queryWrapper.eq("PDJG","不合格").or().eq("PDJG","无效");
        List<TSyjzb> list = tSyjzbService.list(queryWrapper);

        QueryWrapper<TSyjzb> queryWrapper1 = QueryGenerator.initQueryWrapper(tSyjzb, request.getParameterMap());
        queryWrapper1.eq("overproof_status",20);
        List<TSyjzb> list1 = tSyjzbService.list(queryWrapper1);

        QueryWrapper<TSyjzb> queryWrapper3 = QueryGenerator.initQueryWrapper(tSyjzb, request.getParameterMap());
        queryWrapper3.eq("PDJG","不合格").likeRight("SYWCSJ",format).or().eq("PDJG","无效").likeRight("SYWCSJ",format);
        queryWrapper3.isNotNull("bhgyy");
        List<TSyjzb> list3 = tSyjzbService.list(queryWrapper3);
        for (TSyjzb l :list3){
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(l.getSjbh());
            l.setSbbh(selectshebeione.getSbname());
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("over",list.size());
        map.put("overproof",list1.size());
        map.put("detail",list3);
        return Result.OKs(map);
    }

    @AutoLog(value = "t_syjzb-分页列表查询")
    @ApiOperation(value = "t_syjzb-分页列表查询", notes = "t_syjzb-分页列表查询")
    @GetMapping(value = "/anyue")
    public Result<?> anyue(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest request, TSyjzb tSyjzb) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (tSyjzb.getSbbh() == null) {
            if ("null".equals(shebei)) {
                shebei = "空";
            }
            tSyjzb.setSbbh(shebei);
        }
        QueryWrapper<TSyjzb> queryWrapper = QueryGenerator.initQueryWrapper(tSyjzb, request.getParameterMap());
        queryWrapper.select(" DATE_FORMAT( SYRQ, '%Y-%m' ) rq, " +
                " count( 1 )  zongshu , " +
                "(CASE WHEN pdjg = '合格' THEN count( 1 ) END) hege");
        queryWrapper.groupBy(" \t(\n" +
                "SELECT\n" +
                "\tDATE_FORMAT( SYRQ, '%Y-%m' )) ");
        List<Map<String, Object>> maps = tSyjzbService.listMaps(queryWrapper);
        return Result.OK(maps);
    }

    /**
     * 压力机数据查询
     *
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @AutoLog(value = "t_syjzb-分页列表查询")
    @ApiOperation(value = "t_syjzb-分页列表查询", notes = "t_syjzb-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryMapperea(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest request, TSyjzb tSyjzb) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        List<String> list = new ArrayList();
        for (int i = 0; i < split.length; i++) {
            list.add(split[i]);
        }
        List<ShebeiInfo> shebeiInfos = shebeiInfoService.arrayOneshebeis(list, 4);
        String shebeis = null;
        if (shebeiInfos.size() > 0) {
            shebeis = StringUtils.join(shebeiInfos.stream().map(ShebeiInfo::getSbjno).toArray(), ",");
        }
        if (tSyjzb.getSbbh() == null) {
            if ("null".equals(shebeis) || StringUtils.isEmpty(shebeis)) {
                shebeis = "空";
            }
            tSyjzb.setSbbh(shebeis);
        }
        QueryWrapper<TSyjzb> queryWrapper = QueryGenerator.initQueryWrapper(tSyjzb, request.getParameterMap());
        Page<TSyjzb> page = new Page<TSyjzb>(pageNo, pageSize);
        IPage<TSyjzb> pageList = tSyjzbService.page(page, queryWrapper);
        List<TSyjzb> records = pageList.getRecords();
        for (TSyjzb record : records) {
            String cjmc = record.getCjmc();
            if (StringUtil.isEmpty(cjmc)) {
                record.setCjmc(record.getSgbw());
            }
            String sjbh = record.getSjbh();
            if (StringUtil.isEmpty(sjbh)) {
                record.setSjbh(record.getWtbh());
            }
        }
        return Result.OK(pageList);
    }


    @AutoLog(value = "t_syjzb-分页列表查询")
    @ApiOperation(value = "t_syjzb-分页列表查询", notes = "t_syjzb-分页列表查询")
    @GetMapping(value = "/syjcblist")
    public Result<?> syjcblist(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest request, TSyjzb tSyjzbs) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebeis = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//        String[] split = shebei.split(",");
//        List<String> lists = new ArrayList();
//        for (int i = 0; i < split.length; i++) {
//            lists.add(split[i]);
//        }
//        List<ShebeiInfo> shebeiInfos = shebeiInfoService.arrayOneshebeis(lists, 4);
//        String shebeis = null;
//        if (shebeiInfos.size() > 0) {
//            shebeis = StringUtils.join(shebeiInfos.stream().map(ShebeiInfo::getSbjno).toArray(), ",");
//        }
        if (tSyjzbs.getSbbh() == null) {
            if ("null".equals(shebeis) || StringUtils.isEmpty(shebeis)) {
                shebeis = "空";
            }else{
                tSyjzbs.setSbbh(shebeis);
            }

        }
        QueryWrapper<TSyjzb> queryWrapper = QueryGenerator.initQueryWrapper(tSyjzbs, request.getParameterMap());
        queryWrapper.and(wrapper->{wrapper.eq("PDJG", "不合格").or().eq("PDJG", "无效");});
        //  queryWrapper.eq("PDJG", "不合格");
        Page<TSyjzb> page = new Page<TSyjzb>(pageNo, pageSize);
        IPage<TSyjzb> pageList = tSyjzbService.page(page, queryWrapper);
        List<Object> records1 = new ArrayList<>();
        Map<Object, Object> map = new HashMap<>();
        List<TSyjzb> records = pageList.getRecords();
        for (TSyjzb tSyjzb : records) {
            TSyjzbPage tSyjzbPage = new TSyjzbPage();
            SyjOverHandler syjOverHandler = syjOverHandlerService.selectone(tSyjzb.getSyjid());
            BeanUtils.copyProperties(tSyjzb, tSyjzbPage);
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(tSyjzb.getSbbh());
            if (selectshebeione != null) {
                tSyjzbPage.setSbbh(selectshebeione.getSbname());
            }
            if (syjOverHandler == null) {
                SyjOverHandler syjOverHandler1 = new SyjOverHandler();
                syjOverHandler1.setOverproofStatus(0);
                tSyjzbPage.setSyjOverHandler(syjOverHandler1);
            } else {
                tSyjzbPage.setSyjOverHandler(syjOverHandler);
            }
            records1.add(tSyjzbPage);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", records1);
        return Result.OK(map);
    }


    /**
     * 压力机超标查询
     *
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @AutoLog(value = "t_syjzb-分页列表查询")
    @ApiOperation(value = "t_syjzb-分页列表查询", notes = "t_syjzb-分页列表查询")
    @GetMapping(value = "/list2")
    public Result<?> queryMapperSle(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest request, TSyjzb tSyjzbs) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        List<String> lists = new ArrayList();
        for (int i = 0; i < split.length; i++) {
            lists.add(split[i]);
        }
        List<ShebeiInfo> shebeiInfos = shebeiInfoService.arrayOneshebeis(lists, 4);
        String shebeis = null;
        if (shebeiInfos.size() > 0) {
            shebeis = StringUtils.join(shebeiInfos.stream().map(ShebeiInfo::getSbjno).toArray(), ",");
        }
        if (tSyjzbs.getSbbh() == null) {
            if ("null".equals(shebeis) || StringUtils.isEmpty(shebeis)) {
                shebeis = "空";
            }
            tSyjzbs.setSbbh(shebeis);
        }
        QueryWrapper<TSyjzb> queryWrapper = QueryGenerator.initQueryWrapper(tSyjzbs, request.getParameterMap());
        queryWrapper.and(wrapper->{wrapper.eq("PDJG", "不合格").or().eq("PDJG", "无效");});
      //  queryWrapper.eq("PDJG", "不合格");
        Page<TSyjzb> page = new Page<TSyjzb>(pageNo, pageSize);
        IPage<TSyjzb> pageList = tSyjzbService.page(page, queryWrapper);
        List<Object> records1 = new ArrayList<>();
        Map<Object, Object> map = new HashMap<>();
        List<TSyjzb> records = pageList.getRecords();
        for (TSyjzb tSyjzb : records) {
            TSyjzbPage tSyjzbPage = new TSyjzbPage();
            SyjOverHandler syjOverHandler = syjOverHandlerService.selectone(tSyjzb.getSyjid());
            BeanUtils.copyProperties(tSyjzb, tSyjzbPage);
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(tSyjzb.getSbbh());
            if (selectshebeione != null) {
                tSyjzbPage.setSbbh(selectshebeione.getSbname());
            }
            if (syjOverHandler == null) {
                SyjOverHandler syjOverHandler1 = new SyjOverHandler();
                syjOverHandler1.setOverproofStatus(0);
                tSyjzbPage.setSyjOverHandler(syjOverHandler1);
            } else {
                tSyjzbPage.setSyjOverHandler(syjOverHandler);
            }
            records1.add(tSyjzbPage);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", records1);
        return Result.OK(map);
    }

    /**
     * 压力机超标处理
     *
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @AutoLog(value = "t_syjzb-压力机超标处理")
    @ApiOperation(value = "t_syjzb-压力机超标处理", notes = "t_syjzb-压力机超标处理")
    @GetMapping(value = "/czshlist")
    public Result<?> queryMapperSledeal(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        HttpServletRequest request, TSyjzb tSyjzbs) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        List<String> lists = new ArrayList();
        for (int i = 0; i < split.length; i++) {
            lists.add(split[i]);
        }
        List<ShebeiInfo> shebeiInfos = shebeiInfoService.arrayOneshebeis(lists, 4);
        String shebeis = null;
        if (shebeiInfos.size() > 0) {
            shebeis = StringUtils.join(shebeiInfos.stream().map(ShebeiInfo::getSbjno).toArray(), ",");
        }
        if (tSyjzbs.getSbbh() == null) {
            if ("null".equals(shebeis) || StringUtils.isEmpty(shebeis)) {
                shebeis = "空";
            }
            tSyjzbs.setSbbh(shebeis);
        }
        QueryWrapper<TSyjzb> queryWrapper = QueryGenerator.initQueryWrapper(tSyjzbs, request.getParameterMap());
        queryWrapper.and(wrapper->{wrapper.eq("PDJG", "不合格").or().eq("PDJG", "无效");});
       //  queryWrapper.eq("PDJG", "不合格").or().eq("PDJG", "无效");
        queryWrapper.ne("overproof_status", 40);
        Page<TSyjzb> page = new Page<TSyjzb>(pageNo, pageSize);
        IPage<TSyjzb> pageList = tSyjzbService.page(page, queryWrapper);
        List<Object> records1 = new ArrayList<>();
        Map<Object, Object> map = new HashMap<>();
        List<TSyjzb> records = pageList.getRecords();
        for (TSyjzb tSyjzb : records) {
            TSyjzbPage tSyjzbPage = new TSyjzbPage();
            SyjOverHandler syjOverHandler = syjOverHandlerService.selectone(tSyjzb.getSyjid());
            BeanUtils.copyProperties(tSyjzb, tSyjzbPage);
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(tSyjzb.getSbbh());
            if (selectshebeione != null) {
                tSyjzbPage.setSbbh(selectshebeione.getSbname());
            }
            if (syjOverHandler == null) {
                SyjOverHandler syjOverHandler1 = new SyjOverHandler();
                syjOverHandler1.setOverproofStatus(0);
                tSyjzbPage.setSyjOverHandler(syjOverHandler1);
            } else {
                tSyjzbPage.setSyjOverHandler(syjOverHandler);
            }
            records1.add(tSyjzbPage);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", records1);
        return Result.OK(map);
    }


    @AutoLog(value = "t_syjzb-万能机分页列表查询")
    @ApiOperation(value = "t_syjzb-万能机分页列表查询", notes = "t_syjzb-万能机分页列表查询")
    @GetMapping(value = "/list7")
    public Result<?> queryPageList2(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req, TSyjzb tSyjzb) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        List<String> lists = new ArrayList();
        for (int i = 0; i < split.length; i++) {
            lists.add(split[i]);
        }
        List<ShebeiInfo> shebeiInfos = shebeiInfoService.arrayOneshebeis(lists, 3);
        String shebeis = null;
        if (shebeiInfos.size() > 0) {
            shebeis = StringUtils.join(shebeiInfos.stream().map(ShebeiInfo::getSbjno).toArray(), ",");
        }
        if (tSyjzb.getSbbh() == null) {
            if ("null".equals(shebeis) || StringUtils.isEmpty(shebeis)) {
                shebeis = "空";
            }
            tSyjzb.setSbbh(shebeis);
        }

        QueryWrapper<TSyjzb> queryWrapper = QueryGenerator.initQueryWrapper(tSyjzb, req.getParameterMap());
        Page<TSyjzb> page = new Page<TSyjzb>(pageNo, pageSize);
        IPage<TSyjzb> pageList = tSyjzbService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 万能机超标查询
     *
     * @param pageNo
     * @param pageSize
     * @param
     * @return
     */
    @AutoLog(value = "t_syjzb-万能机超标分页列表查询")
    @ApiOperation(value = "t_syjzb-万能机超标分页列表查询", notes = "t_syjzb-万能机超标分页列表查询")
    @GetMapping(value = "/list6")
    public Result<?> queryPageList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req, TSyjzb tSyjzb) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        List<String> lists = new ArrayList();
        for (int i = 0; i < split.length; i++) {
            lists.add(split[i]);
        }
        List<ShebeiInfo> shebeiInfos = shebeiInfoService.arrayOneshebeis(lists, 3);
        String shebeis = null;
        if (shebeiInfos.size() > 0) {
            shebeis = StringUtils.join(shebeiInfos.stream().map(ShebeiInfo::getSbjno).toArray(), ",");
        }
        if (tSyjzb.getSbbh() == null) {
            if ("null".equals(shebeis) || StringUtils.isEmpty(shebeis)) {
                shebeis = "空";
            }
            tSyjzb.setSbbh(shebeis);
        }

        QueryWrapper<TSyjzb> queryWrapper = QueryGenerator.initQueryWrapper(tSyjzb, req.getParameterMap());
        queryWrapper.eq("PDJG", "不合格");
        Page<TSyjzb> page = new Page<TSyjzb>(pageNo, pageSize);
        IPage<TSyjzb> pageList = tSyjzbService.page(page, queryWrapper);
        List<TSyjzb> records = pageList.getRecords();
        List list = new ArrayList();
        Map map = new HashMap();
        if (records.size() > 0) {
            for (TSyjzb record : records) {
                TSyjzbPage tSyjzbPage = new TSyjzbPage();
                String syjid = record.getSyjid();
                String sbbh1 = record.getSbbh();
                ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(sbbh1);
                tSyjzbPage.setSbbh(shebeiInfo.getSbname());
                SyjOverHandler syjOverHandler = syjOverHandlerService.selectOneSyj(syjid);
                if (syjOverHandler == null) {
                    SyjOverHandler syjOverHandler1 = new SyjOverHandler();
                    syjOverHandler1.setOverproofStatus(0);
                    tSyjzbPage.setOverproofStatus(0);
                    tSyjzbPage.setSyjOverHandler(syjOverHandler1);
                } else {
                    tSyjzbPage.setOverproofStatus(syjOverHandler.getOverproofStatus());
                    tSyjzbPage.setSyjOverHandler(syjOverHandler);
                }
                tSyjzbPage.setSyjid(syjid);
                tSyjzbPage.setSylx(record.getSylx());
                tSyjzbPage.setWtbh(record.getWtbh());
                tSyjzbPage.setSjbh(record.getSjbh());
                tSyjzbPage.setZzrq(record.getZzrq());
                tSyjzbPage.setSyrq(record.getSyrq());
                tSyjzbPage.setLq(record.getLq());
                tSyjzbPage.setSjcc(record.getSjcc());
                tSyjzbPage.setSjmj(record.getSjmj());
                tSyjzbPage.setSjsl(record.getSjsl());
                tSyjzbPage.setSjqd(record.getSjqd());
                tSyjzbPage.setZsxs(record.getZsxs());
                tSyjzbPage.setQddbz(record.getQddbz());
                tSyjzbPage.setPdjg(record.getPdjg());
                tSyjzbPage.setCzry(record.getCzry());
                tSyjzbPage.setCjmc(record.getCjmc());
                tSyjzbPage.setStatus(record.getStatus());
                tSyjzbPage.setJudgestate(record.getJudgestate());
                tSyjzbPage.setSjscsj(record.getSjscsj());
                list.add(tSyjzbPage);
            }

        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", list);
        return Result.OK(map);
    }


    /**
     * 万能机超标查询
     *
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @AutoLog(value = "t_syjzb-万能机超标分页列表查询")
    @ApiOperation(value = "t_syjzb-万能机超标分页列表查询", notes = "t_syjzb-万能机超标分页列表查询")
    @GetMapping(value = "/list10")
    public Result<?> queryPageList10(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest request, TSyjzb tSyjzb) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        List<String> lists = new ArrayList();
        for (int i = 0; i < split.length; i++) {
            lists.add(split[i]);
        }
        List<ShebeiInfo> shebeiInfos = shebeiInfoService.arrayOneshebeis(lists, 3);
        String shebeis = null;
        if (shebeiInfos.size() > 0) {
            shebeis = StringUtils.join(shebeiInfos.stream().map(ShebeiInfo::getSbjno).toArray(), ",");
        }
        if (tSyjzb.getSbbh() == null) {
            if ("null".equals(shebeis) || StringUtils.isEmpty(shebeis)) {
                shebeis = "空";
            }
            tSyjzb.setSbbh(shebeis);
        }
        QueryWrapper<TSyjzb> queryWrapper = QueryGenerator.initQueryWrapper(tSyjzb, request.getParameterMap());
        queryWrapper.eq("PDJG", "不合格");
        queryWrapper.ne("overproof_status", 40);
        Page<TSyjzb> page = new Page<TSyjzb>(pageNo, pageSize);
        IPage<TSyjzb> pageList = tSyjzbService.page(page, queryWrapper);
        List<TSyjzb> records = pageList.getRecords();
        List list = new ArrayList();
        Map map = new HashMap();

        if (records.size() > 0) {
            for (TSyjzb record : records) {
                TSyjzbPage tSyjzbPage = new TSyjzbPage();
                String syjid = record.getSyjid();
                String sbbh1 = record.getSbbh();
                ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(sbbh1);
                tSyjzbPage.setSbbh(shebeiInfo.getSbname());
                SyjOverHandler syjOverHandler = syjOverHandlerService.selectOneSyj(syjid);
                if (syjOverHandler == null) {
                    SyjOverHandler syjOverHandler1 = new SyjOverHandler();
                    syjOverHandler1.setOverproofStatus(0);
                    tSyjzbPage.setOverproofStatus(0);
                    tSyjzbPage.setSyjOverHandler(syjOverHandler1);
                } else {
                    tSyjzbPage.setOverproofStatus(syjOverHandler.getOverproofStatus());
                    tSyjzbPage.setSyjOverHandler(syjOverHandler);
                }
                tSyjzbPage.setSyjid(syjid);
                tSyjzbPage.setSylx(record.getSylx());
                tSyjzbPage.setWtbh(record.getWtbh());
                tSyjzbPage.setSjbh(record.getSjbh());
                tSyjzbPage.setZzrq(record.getZzrq());
                tSyjzbPage.setSyrq(record.getSyrq());
                tSyjzbPage.setLq(record.getLq());
                tSyjzbPage.setSjcc(record.getSjcc());
                tSyjzbPage.setSjmj(record.getSjmj());
                tSyjzbPage.setSjsl(record.getSjsl());
                tSyjzbPage.setSjqd(record.getSjqd());
                tSyjzbPage.setZsxs(record.getZsxs());
                tSyjzbPage.setQddbz(record.getQddbz());
                tSyjzbPage.setPdjg(record.getPdjg());
                tSyjzbPage.setCzry(record.getCzry());
                tSyjzbPage.setCjmc(record.getCjmc());
                tSyjzbPage.setStatus(record.getStatus());
                tSyjzbPage.setJudgestate(record.getJudgestate());
                tSyjzbPage.setSjscsj(record.getSjscsj());
                list.add(tSyjzbPage);
            }
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", list);


        return Result.OK(map);
    }


    /**
     * 抗压抗折数据查询
     *
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @AutoLog(value = "t_syjzb-分页列表查询")
    @ApiOperation(value = "t_syjzb-分页列表查询", notes = "t_syjzb-分页列表查询")
    @GetMapping(value = "/list3")
    public Result<?> queryMapperBykykz(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest request) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        String shebeis = "'" + StringUtils.join(split, "','") + "'";//数据格式   'A','B','C'
        if ("null".equals(shebeis)) {
            shebeis = "空";
        }
        String sbbh = request.getParameter("sbbh");
        String pdjg = request.getParameter("pdjg");
        String syrq = request.getParameter("syrq_begin");
        String sywcsj = request.getParameter("syrq_end");
        String sylx = request.getParameter("sylx");
        Date syrqee = null;
        Date sywcsjs = null;
        if (syrq != null && sywcsj != null) {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            try {
                syrqee = DateUtils.parseDate(syrq, pattern);
                sywcsjs = DateUtils.parseDate(sywcsj, pattern);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        IPage<TSyjzb> tSyjzbIPage = tSyjzbService.defaultBtester(pageNo, pageSize, sbbh, pdjg, syrqee, sywcsjs, shebeis, sylx);
        return Result.OK(tSyjzbIPage);
    }


    /**
     * 抗压抗折超标查询
     *
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @AutoLog(value = "t_syjzb-分页列表查询")
    @ApiOperation(value = "t_syjzb-分页列表查询", notes = "t_syjzb-分页列表查询")
    @GetMapping(value = "/list4")
    public Result<?> queryMapperBykykzs(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        HttpServletRequest request) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        String shebeis = "'" + StringUtils.join(split, "','") + "'";//数据格式   'A','B','C'
        if ("null".equals(shebeis)) {
            shebeis = "空";
        }
        String sbbh = request.getParameter("sbbh");
        String syrq = request.getParameter("syrq_begin");
        String sywcsj = request.getParameter("syrq_end");
        Date syrqee = null;
        Date sywcsjs = null;
        if (syrq != null && sywcsj != null) {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            try {
                syrqee = DateUtils.parseDate(syrq, pattern);
                sywcsjs = DateUtils.parseDate(sywcsj, pattern);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        IPage<TSyjzb> tSyjzbIPage = tSyjzbService.defaultBtester2(pageNo, pageSize, sbbh, syrqee, sywcsjs, shebeis);
        List<Object> records1 = new ArrayList<>();
        Map<Object, Object> map = new HashMap<>();
        List<TSyjzb> records = tSyjzbIPage.getRecords();
        for (TSyjzb tSyjzb : records) {
            TSyjzbPage tSyjzbPage = new TSyjzbPage();
            SyjOverHandler syjOverHandler = syjOverHandlerService.selectone(tSyjzb.getSyjid());
            BeanUtils.copyProperties(tSyjzb, tSyjzbPage);
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(tSyjzb.getSbbh());
            if (selectshebeione != null) {
                tSyjzbPage.setSbbh(selectshebeione.getSbname());
            }
            if (syjOverHandler == null) {
                SyjOverHandler syjOverHandler1 = new SyjOverHandler();
                syjOverHandler1.setOverproofStatus(0);
                tSyjzbPage.setSyjOverHandler(syjOverHandler1);
            } else {
                tSyjzbPage.setSyjOverHandler(syjOverHandler);
            }
            records1.add(tSyjzbPage);
        }
        map.put("current", tSyjzbIPage.getCurrent());
        map.put("pages", tSyjzbIPage.getPages());
        map.put("size", tSyjzbIPage.getSize());
        map.put("total", tSyjzbIPage.getTotal());
        map.put("records", records1);
        return Result.OK(map);
    }

    /**
     * 抗压抗折超标处理
     *
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @AutoLog(value = "t_syjzb-抗压抗折超标处理")
    @ApiOperation(value = "t_syjzb-抗压抗折超标处理", notes = "t_syjzb-抗压抗折超标处理")
    @GetMapping(value = "/kykzdeallist")
    public Result<?> queryMapperBykykzsdeal(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                            HttpServletRequest request) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        String shebeis = "'" + StringUtils.join(split, "','") + "'";//数据格式   'A','B','C'
        if ("null".equals(shebeis)) {
            shebeis = "空";
        }
        String sbbh = request.getParameter("sbbh");
        String syrq = request.getParameter("syrq_begin");
        String sywcsj = request.getParameter("syrq_end");
        Date syrqee = null;
        Date sywcsjs = null;
        if (syrq != null && sywcsj != null) {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            try {
                syrqee = DateUtils.parseDate(syrq, pattern);
                sywcsjs = DateUtils.parseDate(sywcsj, pattern);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        IPage<TSyjzb> tSyjzbIPage = tSyjzbService.defaultBtesterdeal(pageNo, pageSize, sbbh, syrqee, sywcsjs, shebeis);
        List<Object> records1 = new ArrayList<>();
        Map<Object, Object> map = new HashMap<>();
        List<TSyjzb> records = tSyjzbIPage.getRecords();
        for (TSyjzb tSyjzb : records) {
            TSyjzbPage tSyjzbPage = new TSyjzbPage();
            SyjOverHandler syjOverHandler = syjOverHandlerService.selectone(tSyjzb.getSyjid());
            BeanUtils.copyProperties(tSyjzb, tSyjzbPage);
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(tSyjzb.getSbbh());
            if (selectshebeione != null) {
                tSyjzbPage.setSbbh(selectshebeione.getSbname());
            }
            if (syjOverHandler == null) {
                SyjOverHandler syjOverHandler1 = new SyjOverHandler();
                syjOverHandler1.setOverproofStatus(0);
                tSyjzbPage.setSyjOverHandler(syjOverHandler1);
            } else {
                tSyjzbPage.setSyjOverHandler(syjOverHandler);
            }
            records1.add(tSyjzbPage);
        }
        map.put("current", tSyjzbIPage.getCurrent());
        map.put("pages", tSyjzbIPage.getPages());
        map.put("size", tSyjzbIPage.getSize());
        map.put("total", tSyjzbIPage.getTotal());
        map.put("records", records1);
        return Result.OK(map);
    }

    /**
     * 恒应力一体机数据查询
     *
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @AutoLog(value = "t_syjzb-分页列表查询")
    @ApiOperation(value = "t_syjzb-分页列表查询", notes = "t_syjzb-分页列表查询")
    @GetMapping(value = "/list8")
    public Result<?> queryMapperhylytj(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest request) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        String shebeis = "'" + StringUtils.join(split, "','") + "'";//数据格式   'A','B','C'
        if ("null".equals(shebeis)) {
            shebeis = "空";
        }
        String sbbh = request.getParameter("sbbh");
        String pdjg = request.getParameter("pdjg");
        String syrq = request.getParameter("syrq_begin");
        String sywcsj = request.getParameter("syrq_end");
        Date syrqee = null;
        Date sywcsjs = null;
        if (syrq != null && sywcsj != null) {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            try {
                syrqee = DateUtils.parseDate(syrq, pattern);
                sywcsjs = DateUtils.parseDate(sywcsj, pattern);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        IPage<TSyjzb> tSyjzbIPage = tSyjzbService.queryMapperhylytjList(pageNo, pageSize, sbbh, pdjg, syrqee, sywcsjs, shebeis);
        return Result.OK(tSyjzbIPage);
    }

    /**
     * 恒应力一体机超标查询
     *
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @AutoLog(value = "t_syjzb-分页列表查询")
    @ApiOperation(value = "t_syjzb-分页列表查询", notes = "t_syjzb-分页列表查询")
    @GetMapping(value = "/list9")
    public Result<?> queryMapperhylytj1(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        HttpServletRequest request) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        String shebeis = "'" + StringUtils.join(split, "','") + "'";//数据格式   'A','B','C'
        if ("null".equals(shebeis)) {
            shebeis = "空";
        }
        String sbbh_dictText = request.getParameter("sbbh_dictText");
        String pdjg = request.getParameter("pdjg");
        String syrq = request.getParameter("syrq_begin");
        String sywcsj = request.getParameter("syrq_end");
        Date syrqee = null;
        Date sywcsjs = null;
        if (syrq != null && sywcsj != null) {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            try {
                syrqee = DateUtils.parseDate(syrq, pattern);
                sywcsjs = DateUtils.parseDate(sywcsj, pattern);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        IPage<TSyjzb> tSyjzbIPage = tSyjzbService.queryMapperhylytjList1(pageNo, pageSize, sbbh_dictText, pdjg, syrqee, sywcsjs, shebeis);
        List<Object> records1 = new ArrayList<>();
        Map<Object, Object> map = new HashMap<>();
        List<TSyjzb> records = tSyjzbIPage.getRecords();
        for (TSyjzb tSyjzb : records) {
            TSyjzbPage tSyjzbPage = new TSyjzbPage();
            SyjOverHandler syjOverHandler = syjOverHandlerService.selectone(tSyjzb.getSyjid());
            BeanUtils.copyProperties(tSyjzb, tSyjzbPage);
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(tSyjzb.getSbbh());
            if (selectshebeione != null) {
                tSyjzbPage.setSbbh(selectshebeione.getSbname());
            }
            if (syjOverHandler == null) {
                SyjOverHandler syjOverHandler1 = new SyjOverHandler();
                syjOverHandler1.setOverproofStatus(0);
                tSyjzbPage.setSyjOverHandler(syjOverHandler1);
            } else {
                tSyjzbPage.setSyjOverHandler(syjOverHandler);
            }
            records1.add(tSyjzbPage);
        }
        map.put("current", tSyjzbIPage.getCurrent());
        map.put("pages", tSyjzbIPage.getPages());
        map.put("size", tSyjzbIPage.getSize());
        map.put("total", tSyjzbIPage.getTotal());
        map.put("records", records1);
        return Result.OK(map);
    }

    /**
     * 恒应力一体机超标处理
     *
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @AutoLog(value = "t_syjzb-恒应力一体机超标处理")
    @ApiOperation(value = "t_syjzb-恒应力一体机超标处理", notes = "t_syjzb-恒应力一体机超标处理")
    @GetMapping(value = "/hylytjdeallist")
    public Result<?> queryMapperhylytjdeal(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           HttpServletRequest request) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        String shebeis = "'" + StringUtils.join(split, "','") + "'";//数据格式   'A','B','C'
        if ("null".equals(shebeis)) {
            shebeis = "空";
        }
        String sbbh_dictText = request.getParameter("sbbh_dictText");
        String pdjg = request.getParameter("pdjg");
        String syrq = request.getParameter("syrq_begin");
        String sywcsj = request.getParameter("syrq_end");
        Date syrqee = null;
        Date sywcsjs = null;
        if (syrq != null && sywcsj != null) {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            try {
                syrqee = DateUtils.parseDate(syrq, pattern);
                sywcsjs = DateUtils.parseDate(sywcsj, pattern);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        IPage<TSyjzb> tSyjzbIPage = tSyjzbService.queryMapperhylytjdealList(pageNo, pageSize, sbbh_dictText, pdjg, syrqee, sywcsjs, shebeis);
        List<Object> records1 = new ArrayList<>();
        Map<Object, Object> map = new HashMap<>();
        List<TSyjzb> records = tSyjzbIPage.getRecords();
        for (TSyjzb tSyjzb : records) {
            TSyjzbPage tSyjzbPage = new TSyjzbPage();
            SyjOverHandler syjOverHandler = syjOverHandlerService.selectone(tSyjzb.getSyjid());
            BeanUtils.copyProperties(tSyjzb, tSyjzbPage);
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(tSyjzb.getSbbh());
            if (selectshebeione != null) {
                tSyjzbPage.setSbbh(selectshebeione.getSbname());
            }
            if (syjOverHandler == null) {
                SyjOverHandler syjOverHandler1 = new SyjOverHandler();
                syjOverHandler1.setOverproofStatus(0);
                tSyjzbPage.setSyjOverHandler(syjOverHandler1);
            } else {
                tSyjzbPage.setSyjOverHandler(syjOverHandler);
            }
            records1.add(tSyjzbPage);
        }
        map.put("current", tSyjzbIPage.getCurrent());
        map.put("pages", tSyjzbIPage.getPages());
        map.put("size", tSyjzbIPage.getSize());
        map.put("total", tSyjzbIPage.getTotal());
        map.put("records", records1);
        return Result.OK(map);
    }


    /**
     * 实验室首页统计图
     *
     * @param syjzb
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "实验室首页统计图")
    @ApiOperation(value = "实验室首页统计图", notes = "实验室首页统计图")
    @GetMapping(value = "/list5")
    public Result<?> queryPageList6(TSyjzb syjzb, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        Map map = new HashMap();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//1:本月第一天
        String day1 = format.format(c.getTime());
        Date parse = null;//本月第一天
        Date parse1 = null;//本月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String day2 = format.format(ca.getTime());
        try {
            parse = format.parse(day1);
            parse1 = format.parse(day2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //查询到他的设备编号
        if (!"null".equals(shebei)) {
            syjzb.setSbbh(shebei);
        } else {
            syjzb.setSbbh("空");
        }
        QueryWrapper<TSyjzb> queryWrapper = QueryGenerator.initQueryWrapper(syjzb, req.getParameterMap());
        queryWrapper.select("count(*) as version");
        queryWrapper.ge("syrq", parse);
        queryWrapper.le("syrq", parse1);
        TSyjzb one = tSyjzbService.getOne(queryWrapper);

        QueryWrapper<TSyjzb> queryWrapper1 = QueryGenerator.initQueryWrapper(syjzb, req.getParameterMap());
        queryWrapper1.select("count(*) as version");
        queryWrapper1.in("PDJG", "不合格", "无效");
        queryWrapper1.ge("syrq", parse);
        queryWrapper1.le("syrq", parse1);
        TSyjzb one1 = tSyjzbService.getOne(queryWrapper1);

        QueryWrapper<TSyjzb> queryWrapper2 = QueryGenerator.initQueryWrapper(syjzb, req.getParameterMap());
        queryWrapper2.select("count(*) as version");
        TSyjzb one2 = tSyjzbService.getOne(queryWrapper2);

        QueryWrapper<TSyjzb> queryWrapper3 = QueryGenerator.initQueryWrapper(syjzb, req.getParameterMap());
        queryWrapper3.select("count(*) as version");
        queryWrapper3.in("PDJG", "不合格", "无效");
        TSyjzb one3 = tSyjzbService.getOne(queryWrapper3);

        QueryWrapper<TSyjzb> queryWrapper4 = QueryGenerator.initQueryWrapper(syjzb, req.getParameterMap());
        queryWrapper4.select("count(*) as version");
        queryWrapper4.in("PDJG", "不合格", "无效");
        queryWrapper4.eq("overproof_status", 20);
        queryWrapper4.ge("syrq", parse);
        queryWrapper4.le("syrq", parse1);
        TSyjzb one4 = tSyjzbService.getOne(queryWrapper4);
        //List<TSyjzb> bhzCementStatisticsList = tSyjzbService.list(queryWrapper);
        Double syjsum = 0.0;
        Double syjcbsj = 0.0;
        Double syjcblv = 0.0;
        Double syjysum = 0.0;
        Double syjycb = 0.0;
        Double syjycblv = 0.0;
        double sysbhlvY = 0.0;
        double sysbhY = 0.0;

        if (one2 != null) {
            syjsum = syjsum + one2.getVersion();
        }
        if (one3 != null) {
            syjcbsj = syjcbsj + one3.getVersion();
        }
        if (one != null) {
            syjysum = syjysum + one.getVersion();
        }
        if (one1 != null) {
            syjycb = syjycb + one1.getVersion();
        }
        if (one4 != null) {
            sysbhY = one4.getVersion();
        }
        Double huncblv = (syjcbsj / syjsum) * 100;//总的超标率
        Double hunylv = (syjycb / syjysum) * 100;//当前月的超标率
        if (syjycb > 0) {
            sysbhlvY = (sysbhY / syjycb) * 100;
        }
        if (huncblv > 0) {
            BigDecimal b = new BigDecimal(huncblv);
            syjcblv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        if (hunylv > 0) {
            BigDecimal b1 = new BigDecimal(hunylv);
            syjycblv = b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        map.put("syjsum", syjsum);
        map.put("syjcbsj", syjcbsj);
        map.put("syjcblv", syjcblv);
        map.put("syjycblv", syjycblv);
        map.put("sysbhlvY", sysbhlvY);
        return Result.OK(map);
    }

    /**
     * 强度试验次数
     *
     * @param syjzb
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "强度试验次数")
    @ApiOperation(value = "强度试验次数", notes = "强度试验次数")
    @GetMapping(value = "/strengthlist")
    public Result<?> strengthPageList(TSyjzb syjzb, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (syjzb.getSbbh() == null) {
            if (!"null".equals(shebei)) {
                syjzb.setSbbh(shebei);
            } else {
                syjzb.setSbbh("空");
            }
        }
        QueryWrapper<TSyjzb> queryWrapper = QueryGenerator.initQueryWrapper(syjzb, req.getParameterMap());
        queryWrapper.select("ifnull(count(*),0) as version");
        queryWrapper.eq("SYLX", "100014");
        List<TSyjzb> tSyjzbList = tSyjzbService.list(queryWrapper);
        return Result.OK(tSyjzbList);
    }

    /**
     * 试验机总数
     *
     * @param syjzb
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "试验机总数")
    @ApiOperation(value = "试验机总数", notes = "试验机总数")
    @GetMapping(value = "/totallist")
    public Result<?> totallistPageList(TSyjzb syjzb, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (syjzb.getSbbh() == null) {
            if (!"null".equals(shebei)) {
                syjzb.setSbbh(shebei);
            } else {
                syjzb.setSbbh("空");
            }
        }
        QueryWrapper<TSyjzb> queryWrapper = QueryGenerator.initQueryWrapper(syjzb, req.getParameterMap());
        queryWrapper.select("ifnull(count(*),0) as version");
        List<TSyjzb> tSyjzbList = tSyjzbService.list(queryWrapper);
        return Result.OK(tSyjzbList);
    }

    /**
     * 万能机/压力机/抗压抗折机 总数/不合格数/合格数统计
     *
     * @param shebeiInfo
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "万能机/压力机/抗压抗折机 总数/不合格数/合格数统计")
    @ApiOperation(value = "万能机/压力机/抗压抗折机 总数/不合格数/合格数统计", notes = "万能机/压力机/抗压抗折机 总数/不合格数/合格数统计")
    @GetMapping(value = "/stalist")
    public Result<?> stsPageList(ShebeiInfo shebeiInfo, HttpServletRequest req) {
//        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
//        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//        if(shebeiInfo.getSbjno()==null){
//            if(!"null".equals(shebei)){
//                shebeiInfo.setSbjno(shebei);
//            }else{
//                shebeiInfo.setSbjno("空");
//            }
//        }
        if (shebeiInfo.getSbtype() != null) {
            shebeiInfo.setSbtype(shebeiInfo.getSbtype());
        }
        if (shebeiInfo.getSysOrgCode() != null) {
            shebeiInfo.setSysOrgCode(shebeiInfo.getSysOrgCode() + "*");
        }
        QueryWrapper<ShebeiInfo> queryWrapper = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        List<ShebeiInfo> shebeiInfoList = shebeiInfoService.list(queryWrapper);
        List<String> shebeilist = new ArrayList<>();
        double totalsum = 0;
        double hegesum = 0;
        double buhegesum = 0;
        double bhhegelv = 0.0;
        double chuzhisum = 0;
        Map map = new HashMap();
        if (shebeiInfoList.size() > 0) {
            for (ShebeiInfo shebeiInfo1 : shebeiInfoList) {
                shebeilist.add(shebeiInfo1.getSbjno());
            }
            QueryWrapper<TSyjzb> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.select("ifnull(count(*),0) version");
            queryWrapper1.in("SBBH", shebeilist);
            TSyjzb tSyjzb = tSyjzbService.getOne(queryWrapper1);
            if (tSyjzb != null) {
                totalsum = tSyjzb.getVersion();
            }
            QueryWrapper<TSyjzb> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.select("ifnull(count(*),0) version");
            queryWrapper2.eq("PDJG", "合格");
            queryWrapper2.in("SBBH", shebeilist);
            TSyjzb tSyjzb1 = tSyjzbService.getOne(queryWrapper2);
            if (tSyjzb1 != null) {
                hegesum = tSyjzb1.getVersion();
            }
            QueryWrapper<TSyjzb> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.select("ifnull(count(*),0) version");
            queryWrapper3.eq("PDJG", "不合格");
            queryWrapper3.gt("overproof_status", 0);
            queryWrapper3.in("SBBH", shebeilist);
            TSyjzb tSyjzb3 = tSyjzbService.getOne(queryWrapper3);
            if (tSyjzb3 != null) {
                chuzhisum = tSyjzb3.getVersion();
            }
        }
        buhegesum = totalsum - hegesum;
        if (totalsum != 0) {
            bhhegelv = (buhegesum / totalsum) * 100;
        }
        map.put("totalsum", totalsum);
        map.put("hegesum", hegesum);
        map.put("buhegesum", buhegesum);
        map.put("bhhegelv", bhhegelv);
        map.put("chuzhisum", chuzhisum);
        return Result.OK(map);
    }

    /**
     * 万能机/压力机/抗压抗折机 总数/不合格数/合格数统计(351国道看板)
     *
     * @param shebeiInfo
     * @param req
     * @return
     */
    @AutoLog(value = "万能机/压力机/抗压抗折机 总数/不合格数/合格数统计")
    @ApiOperation(value = "万能机/压力机/抗压抗折机 总数/不合格数/合格数统计", notes = "万能机/压力机/抗压抗折机 总数/不合格数/合格数统计")
    @GetMapping(value = "/statistics")
    public Result<?> statistics(ShebeiInfo shebeiInfo, HttpServletRequest req) {
        if (shebeiInfo.getSbtype() != null) {
            shebeiInfo.setSbtype(shebeiInfo.getSbtype());
        }
        if (shebeiInfo.getSysOrgCode() != null) {
            shebeiInfo.setSysOrgCode(shebeiInfo.getSysOrgCode() + "*");
        }
        QueryWrapper<ShebeiInfo> queryWrapper = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        List<ShebeiInfo> shebeiInfoList = shebeiInfoService.list(queryWrapper);//根据条件查询到shebeiinfo的数据列表
        List<String> sbjnoList = new ArrayList<>();
        if (shebeiInfoList.size() > 0) {

            for (ShebeiInfo shebeiInfo1 : shebeiInfoList) {
                sbjnoList.add(shebeiInfo1.getSbjno());//将shebeiinfo数据列表的设备编号存入数组
            }
            String sysorgcode = shebeiInfo.getSysOrgCode().substring(0, shebeiInfo.getSysOrgCode().length() - 1);
//            sysOrgCode.Remove(sysOrgCode.Length-i,i);
            List<String> sylxList = tSyjzbService.querySylx(shebeiInfo.getSbtype(), sysorgcode);
            if (sylxList.size() != 0) {

                Map map = new HashMap();
                for (String sylx : sylxList) {
                    List<String> sumList = new ArrayList<>();
                    QueryWrapper<TSyjzb> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.select("ifnull(count(*),0) version");
                    queryWrapper1.eq("sylx", sylx);
                    queryWrapper1.in("sbbh", sbjnoList);
                    TSyjzb one = tSyjzbService.getOne(queryWrapper1);
                    String sum = "0";
                    if (one != null) {
                        sum = String.valueOf(one.getVersion());//总数
                    }
                    sumList.add(0, sum);

                    QueryWrapper<TSyjzb> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.select("ifnull(count(*),0) version");
                    queryWrapper2.eq("sylx", sylx);
                    queryWrapper2.eq("pdjg", "合格");
                    queryWrapper2.in("sbbh", sbjnoList);
                    TSyjzb two = tSyjzbService.getOne(queryWrapper2);
                    String hgsum = "0";
                    if (two != null) {
                        hgsum = String.valueOf(two.getVersion());//合格数
                    }
                    sumList.add(1, hgsum);

                    String bhgsum = String.valueOf(Integer.parseInt(sum) - Integer.parseInt(hgsum));//不合格数
                    sumList.add(2, bhgsum);

                    NumberFormat numberFormat = NumberFormat.getInstance();
                    numberFormat.setMaximumFractionDigits(2);
                    String hglv = "0.0";
                    hglv = numberFormat.format((float) Integer.parseInt(hgsum) / (float) Integer.parseInt(sum) * 100);//合格率
                    sumList.add(3, hglv + "%");

                    Object sylxname = redisUtil.get("sys:cache:dict::SYLX:" + sylx);
                    String sylxName = String.valueOf(sylxname);
                    if (sylxname == null) {
                        switch (sylx) {
                            case "100048":
                                sylxName = "钢筋焊接接头试验";
                                break;
                            case "100133":
                                sylxName = "水泥砂浆立方体抗压强度试验(建筑)";
                                break;
                            case "100793":
                                sylxName = "后张预应力孔道压浆抗压强度试验";
                                break;
                            case "100794":
                                sylxName = "后张预应力孔道压浆抗折强度试验";
                                break;
                        }
                    }
                    //100138  100133
                    if (sylx.equals("100133") && shebeiInfo.getSbtype() == 12 || sylx.equals("100138") && shebeiInfo.getSbtype() == 12) {
                        break;
                    }
                    map.put(sylxName, sumList);
                }

                return Result.OK(map);
            }
            return Result.error("暂无查询到的数据");
        }
        return Result.error("暂无查询到的数据");
    }

    /**
     * 万能机/压力机/抗压抗折机 总数/不合格数/合格数统计
     *
     * @param shebeiInfo
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "万能机/压力机/抗压抗折机 总数/不合格数/合格数统计")
    @ApiOperation(value = "万能机/压力机/抗压抗折机 总数/不合格数/合格数统计", notes = "万能机/压力机/抗压抗折机 总数/不合格数/合格数统计")
    @GetMapping(value = "/stalist2")
    public Result<?> stsPageList2(ShebeiInfo shebeiInfo, HttpServletRequest req) {

        if (shebeiInfo.getSbtype() != null) {
            shebeiInfo.setSbtype(shebeiInfo.getSbtype());
        }
        if (shebeiInfo.getSysOrgCode() != null) {
            shebeiInfo.setSysOrgCode(shebeiInfo.getSysOrgCode() + "*");
        }
        QueryWrapper<ShebeiInfo> queryWrapper = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        List<ShebeiInfo> shebeiInfoList = shebeiInfoService.list(queryWrapper);
        List<String> shebeilist = new ArrayList<>();

        Map map = new HashMap();
        if (shebeiInfoList.size() > 0) {
            for (ShebeiInfo shebeiInfo1 : shebeiInfoList) {
                shebeilist.add(shebeiInfo1.getSbjno());
            }
            QueryWrapper<TSyjzb> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.select(" ifnull( count(1), 0 ) totalsum , " +
                    " SUM(CASE WHEN PDJG='合格'  THEN 1  ELSE 0 END ) AS hegesum, " +
                    " SUM( CASE WHEN PDJG='不合格'  THEN 1  ELSE 0 END ) AS buhegesum," +
                    " SUM( CASE WHEN PDJG='不合格' AND overproof_status = 20 THEN 1 ELSE 0 END ) AS checked ");
            queryWrapper1.in("SBBH", shebeilist);
            map = tSyjzbService.getMap(queryWrapper1);
//            if(map.size()>0){
//                Double hegelv =Double.parseDouble((String) map.get("hegesum")) / Double.parseDouble((String) map.get("totalsum")) ;
//                Double buhegelv =Double.parseDouble((String) map.get("buhegesum")) / Double.parseDouble((String) map.get("totalsum")) ;
//                Double checkedlv =Double.parseDouble((String) map.get("checked")) / Double.parseDouble((String) map.get("buhegesum")) ;
//
//                map.put("hegelv", hegelv);
//                map.put("buhegelv", buhegelv);
//                map.put("checkedlv", checkedlv);
//            }


        }
        if (map.size() == 0) {
            map.put("totalsum", 0);
            map.put("hegesum", 0);
            map.put("buhegesum", 0);
            map.put("checked", 0);
            map.put("checkedlv", 0);
        }

        return Result.OK(map);
    }


    /**
     * 万能机/压力机/标养室/抗压抗折 总数/不合格数/合格数统计[集团标准版看板]
     *
     * @param projectid
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "万能机/压力机/标养室/抗压抗折 总数/不合格数/合格数统计")
    @ApiOperation(value = "万能机/压力机/标养室/抗压抗折 总数/不合格数/合格数统计", notes = "万能机/压力机/标养室/抗压抗折 总数/不合格数/合格数统计")
    @GetMapping(value = "/getstalists")
    public Result<?> stsPageLists(String projectid, HttpServletRequest req) {
        Map map = new HashMap();
//        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(projectid + "shebeilist"));
        String[] split = shebei.split(",");
        String shebeis = "'" + StringUtils.join(split, "','") + "'";//数据格式   'A','B','C'
        if ("null".equals(shebeis)) {
            shebeis = "空";
        }
        Map map1 = tSyjzbService.stsPageLists(shebeis);//压力机总数 合格数 不合格数
        Map map2 = tSyjzbService.stsPageLists1(shebeis);//万能机总数 合格数 不合格数
        Map map3 = tSyjzbService.stsPageLists2(shebeis);//抗压抗折机总数 合格数 不合格数
        //Map map4 = bysRealService.stsPageLists3(shebeis);//标养室总数 合格数 不合格数
        Map map4 = new HashMap();
        map4.put("sums", 0);
        map4.put("sumtrue", 0);
        map4.put("sumsfalse", 0);
        map.put("ylj", map1);
        map.put("wnj", map2);
        map.put("bys", map4);
        map.put("kykz", map3);
        return Result.OK(map);
    }


    /**
     * 万能机/压力机/标养室/抗压抗折 总数/不合格数/合格数统计
     *
     * @param syjzb
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "万能机/压力机/标养室/抗压抗折 总数/不合格数/合格数统计")
    @ApiOperation(value = "万能机/压力机/标养室/抗压抗折 总数/不合格数/合格数统计", notes = "万能机/压力机/标养室/抗压抗折 总数/不合格数/合格数统计")
    @GetMapping(value = "/stalists")
    public Result<?> stsPageLists(TSyjzb syjzb, BysReal bysReal, HttpServletRequest req) {
        Map map = new HashMap();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        String shebeis = "'" + StringUtils.join(split, "','") + "'";//数据格式   'A','B','C'
        if ("null".equals(shebeis)) {
            shebeis = "空";
        }
        Map map1 = tSyjzbService.stsPageLists(shebeis);//压力机总数 合格数 不合格数
        Map map2 = tSyjzbService.stsPageLists1(shebeis);//万能机总数 合格数 不合格数
        Map map3 = tSyjzbService.stsPageLists2(shebeis);//抗压抗折机总数 合格数 不合格数
        //Map map4 = bysRealService.stsPageLists3(shebeis);//标养室总数 合格数 不合格数
        Map map4 = new HashMap();
        map4.put("sums", 0);
        map4.put("sumtrue", 0);
        map4.put("sumsfalse", 0);
        map.put("ylj", map1);
        map.put("wnj", map2);
        map.put("bys", map4);
        map.put("kykz", map3);
        return Result.OK(map);
    }

    /**
     * 砼/沥青/水稳拌合站/万能机/压力机/标养室/抗压抗折 今日/累计预警统计
     *
     * @param syjzb
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "砼/沥青/水稳拌合站/万能机/压力机/标养室/抗压抗折 今日/累计预警统计")
    @ApiOperation(value = "砼/沥青/水稳拌合站/万能机/压力机/标养室/抗压抗折 今日/累计预警统计", notes = "砼/沥青/水稳拌合站/万能机/压力机/标养室/抗压抗折 今日/累计预警统计")
    @GetMapping(value = "/lists")
    public Result<?> PageLists(TSyjzb syjzb, BhzLqStatistics bhzLqStatistics, BhzSwStatistics bhzSwStatistics, BhzCementStatistics bhzCementStatistics, HttpServletRequest req) {
        Map map = new HashMap();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format = ft.format(new Date());
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        String shebeis = "'" + StringUtils.join(split, "','") + "'";//数据格式   'A','B','C'
        if ("null".equals(shebeis)) {
            shebeis = "空";
        }
        if (bhzCementStatistics.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementStatistics.setShebeiNo(shebei);
            } else {
                bhzCementStatistics.setShebeiNo(shebei);
            }
        }
        if (bhzLqStatistics.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                bhzLqStatistics.setShebeibianhao(shebei);
            } else {
                bhzLqStatistics.setShebeibianhao("空");
            }
        }
        if (bhzSwStatistics.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                bhzSwStatistics.setShebeibianhao(shebei);
            } else {
                bhzSwStatistics.setShebeibianhao("空");
            }
        }
        int yljDay = 0;
        int yljSum = 0;
        int wnjDay = 0;
        int wnjSum = 0;
        int lqyjdishDay = 0;
        int lqyjdishSum = 0;
        int swyjdishDay = 0;
        int swyjdishSum = 0;
        int yjdishDay = 0;
        int yjdishSum = 0;
        QueryWrapper<TSyjzb> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("sum(case when PDJG in ('不合格','无效') and (to_days(SJSCSJ) = to_days(now())) then 1 else 0 end) SJSL," +
                "sum(case when PDJG in ('不合格','无效') then 1 else 0 end) SJQD");
        queryWrapper.last("a join shebei_info on a.SBBH=shebei_info.sbjno where shebei_info.sbtype = '4' and a.SBBH in (" + shebeis + ")");
        TSyjzb one = tSyjzbService.getOne(queryWrapper);
        if (one != null) {
            yljDay = Integer.parseInt(one.getSjsl());
            yljSum = Integer.parseInt(one.getSjqd());
        }
        QueryWrapper<TSyjzb> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.select("sum(case when PDJG in ('不合格','无效') and (to_days(SJSCSJ) = to_days(now())) then 1 else 0 end) SJSL," +
                "sum(case when PDJG in ('不合格','无效') then 1 else 0 end) SJQD");
        queryWrapper.last("a join shebei_info on a.SBBH=shebei_info.sbjno where shebei_info.sbtype = '3' and a.SBBH in (" + shebeis + ")");
        TSyjzb one1 = tSyjzbService.getOne(queryWrapper1);
        if (one1 != null) {
            wnjDay = Integer.parseInt(one1.getSjsl());
            wnjSum = Integer.parseInt(one1.getSjqd());
        }
        QueryWrapper<BhzLqStatistics> queryWrapper4 = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper4.select("sum(all_overproof_dish) as all_overproof_dish");
        queryWrapper4.likeRight("statistics_time", format);
        BhzLqStatistics statistics = iBhzLqStatisticsService.getOne(queryWrapper4);
        if (statistics != null) {
            lqyjdishDay = statistics.getAllOverproofDish();//沥青今日超标总盘数
        }
        QueryWrapper<BhzLqStatistics> queryWrapper5 = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper5.select("sum(all_overproof_dish) as all_overproof_dish");
        BhzLqStatistics statisticss = iBhzLqStatisticsService.getOne(queryWrapper5);
        if (statisticss != null) {
            lqyjdishSum = statisticss.getAllOverproofDish();//沥青累计超标总盘数
        }
        QueryWrapper<BhzSwStatistics> queryWrapper2 = QueryGenerator.initQueryWrapper(bhzSwStatistics, req.getParameterMap());
        queryWrapper2.select("sum(all_overproof_dish) as all_overproof_dish");
        BhzSwStatistics statisticss1 = bhzSwStatisticsService.getOne(queryWrapper2);
        if (statisticss1 != null) {
            swyjdishSum = statisticss1.getAllOverproofDish();//水稳累计超标总盘数
        }
        QueryWrapper<BhzSwStatistics> queryWrapper3 = QueryGenerator.initQueryWrapper(bhzSwStatistics, req.getParameterMap());
        queryWrapper3.select("sum(all_overproof_dish) as all_overproof_dish");
        queryWrapper3.likeRight("statistics_time", format);
        BhzSwStatistics statistics1 = bhzSwStatisticsService.getOne(queryWrapper3);
        if (statistics1 != null) {
            swyjdishDay = statistics1.getAllOverproofDish();//水稳今日超标总盘数
        }
        QueryWrapper<BhzCementStatistics> queryWrapper6 = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper6.select("sum(all_overproof_dish) as all_overproof_dish");
        BhzCementStatistics statistics7 = bhzCementStatisticsService.getOne(queryWrapper6);
        if (statistics7 != null) {
            yjdishSum = statistics7.getAllOverproofDish();
        }
        QueryWrapper<BhzCementStatistics> queryWrapper7 = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper7.select("sum(all_overproof_dish) as all_overproof_dish");
        queryWrapper7.likeRight("statistics_time", format);
        BhzCementStatistics statisticss7 = bhzCementStatisticsService.getOne(queryWrapper7);
        if (statisticss7 != null) {
            yjdishDay = statisticss7.getAllOverproofDish();
        }
        map.put("yljDay", yljDay);//压力机今日超标总盘数
        map.put("yljSum", yljSum);//压力机累计超标总盘数
        map.put("wnjDay", wnjDay);//万能机今日超标总盘数
        map.put("wnjSum", wnjSum);//万能机今日超标总盘数
        map.put("lqyjdishDay", lqyjdishDay);
        map.put("lqyjdishSum", lqyjdishSum);
        map.put("swyjdishSum", swyjdishSum);
        map.put("swyjdishDay", swyjdishDay);
        map.put("tyjdishDay", yjdishDay);
        map.put("tyjdishSum", yjdishSum);
        return Result.OK(map);
    }


    /**
     * 试验机首页统计  万能机/压力机/标养室/抗压抗折 总数/不合格数/合格数统计/处置数
     *
     * @param syjzb
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "试验机首页统计  万能机/压力机/标养室/抗压抗折 总数/不合格数/合格数统计/处置数")
    @ApiOperation(value = "试验机首页统计  万能机/压力机/标养室/抗压抗折 总数/不合格数/合格数统计/处置数", notes = "试验机首页统计  万能机/压力机/标养室/抗压抗折 总数/不合格数/合格数统计/处置数")
    @GetMapping(value = "/stalistslv")
    public Result<?> stsPageLists1(TSyjzb syjzb, BysReal bysReal, HttpServletRequest req) {
        Map map = new HashMap();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        String shebeis = "'" + StringUtils.join(split, "','") + "'";//数据格式   'A','B','C'
        if ("null".equals(shebeis)) {
            shebeis = "空";
        }
        Map map1 = tSyjzbService.stsPageLists3(shebeis);//压力机总数 合格数 不合格数/处置数
        Map map2 = tSyjzbService.stsPageLists4(shebeis);//万能机总数 合格数 不合格数/处置数
        Map map3 = tSyjzbService.stsPageLists5(shebeis);//抗压抗折机总数 合格数 不合格数/处置数
        Map map4 = bysRealService.stsPageLists3(shebeis);//标养室总数 合格数 不合格数/处置数

        map.put("ylj", map1);
        map.put("wnj", map2);
        map.put("bys", map4);
        map.put("kykz", map3);
        return Result.OK(map);
    }


    /**
     * 统计强度标准差
     *
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @AutoLog(value = "t_syjzb-统计强度标准差")
    @ApiOperation(value = "t_syjzb-统计强度标准差", notes = "t_syjzb-统计强度标准差")
    @GetMapping(value = "/syjlist")
    public Result<?> querysyjlist(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                  HttpServletRequest request, TSyjzb tSyjzb, String orgCode) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//        if (tSyjzb.getSbbh() == null) {
//            if ("null".equals(shebei)) {
//                shebei = "空";
//            }
//            tSyjzb.setSbbh(shebei);
//        }
        int[] stype = {3, 4, 12};
        List<Integer> stypelist = new ArrayList<>();
        for (int j : stype) {
            stypelist.add(j);
        }
        List<ShebeiInfo> list1 = shebeiInfoService.usershebeiList(orgCode, stypelist);
        List<String> shebeis = new ArrayList<>();
        if (list1.size() > 0) {
            for (ShebeiInfo shebeiInfo : list1) {
                shebeis.add(shebeiInfo.getSbjno());
            }
        }
        QueryWrapper<TSyjzb> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT(SJQD) SJQD");
        queryWrapper.eq("SYLX", "100014");
        queryWrapper.in("SJQD", (Object[]) new String[]{"C30", "C35", "C40", "C50"});
        List<TSyjzb> pageList = tSyjzbService.list(queryWrapper);
        List<Double> list = new ArrayList<>();
        List<Map<String, Object>> list3 = new ArrayList<>();
        for (TSyjzb tSyjzb1 : pageList) {
            String sjqd = tSyjzb1.getSjqd();
            QueryWrapper<TSyjzb> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.select("QDDBZ");
            queryWrapper1.eq("SJQD", sjqd);
            queryWrapper.in("PDJG", (Object[]) new String[]{"合格", "有效"});
            queryWrapper1.isNotNull("QDDBZ");
            if (shebeis.size() > 0) {
                queryWrapper1.in("SBBH", shebeis);
            } else {
                queryWrapper1.in("SBBH", "空");
            }
            List<TSyjzb> tSyjzbList = tSyjzbService.list(queryWrapper1);
            Map<String, Object> map = new HashMap<>();
            List<String> list2 = new ArrayList<>();
            double num = 0;
            for (TSyjzb tSyjzb2 : tSyjzbList) {
                double sd = 0.0;
                if (list.size() % 100 == 0 && list.size() > 0) {
                    sd = mathUtil.StandardDiviation(list);
                    String sd1 = String.format("%.2f", sd);
                    list2.add(sd1);
                    num = num + sd;
                    list.clear();
                } else {
                    if (!StringUtil.isEmpty(tSyjzb2.getQddbz())) {
                        double qd = Double.parseDouble(tSyjzb2.getQddbz());
                        list.add(qd);
                    }
                }

            }
            double num1 = 0;
            num1 = num / list2.size();
            map.put("data", list2);
            map.put("sjqd", sjqd);
            map.put("num1", String.format("%.2f", num1));
            list3.add(map);
        }
        return Result.OK(list3);
    }

    @AutoLog(value = "t_syjzb-统计强度标准差")
    @ApiOperation(value = "t_syjzb-统计强度标准差", notes = "t_syjzb-统计强度标准差")
    @GetMapping(value = "/syjbzc")
    public Result<?> querysyjlistbzc(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize,
                                     HttpServletRequest request, TSyjzb tSyjzb, String orgCode, String sjqdz) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//        if (tSyjzb.getSbbh() == null) {
//            if ("null".equals(shebei)) {
//                shebei = "空";
//            }
//            tSyjzb.setSbbh(shebei);
//        }
        int[] stype = {3, 4, 12};
        List<Integer> stypelist = new ArrayList<>();
        for (int j : stype) {
            stypelist.add(j);
        }
        List<ShebeiInfo> list1 = shebeiInfoService.usershebeiList(orgCode, stypelist);
        List<String> shebeis = new ArrayList<>();
        if (list1.size() > 0) {
            for (ShebeiInfo shebeiInfo : list1) {
                shebeis.add(shebeiInfo.getSbjno());
            }
        }
        QueryWrapper<TSyjzb> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT(SJQD) SJQD");
        queryWrapper.eq("SYLX", "100014");
        if (StringUtils.isNotBlank(sjqdz)) {
            queryWrapper.eq("SJQD", sjqdz);
        } else {
            queryWrapper.in("SJQD", (Object[]) new String[]{"C30", "C35", "C40", "C50"});
        }


        List<TSyjzb> pageList = tSyjzbService.list(queryWrapper);
        List<Double> list = new ArrayList<>();
        List<Map<String, Object>> list3 = new ArrayList<>();
        for (TSyjzb tSyjzb1 : pageList) {
            String sjqd = tSyjzb1.getSjqd();
            QueryWrapper<TSyjzb> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.select("QDDBZ");
            queryWrapper1.eq("SJQD", sjqd);
            queryWrapper.in("PDJG", (Object[]) new String[]{"合格", "有效"});
            queryWrapper1.isNotNull("QDDBZ");
            if (shebeis.size() > 0) {
                queryWrapper1.in("SBBH", shebeis);
            } else {
                queryWrapper1.in("SBBH", "空");
            }
            List<TSyjzb> tSyjzbList = tSyjzbService.list(queryWrapper1);
            Map<String, Object> map = new HashMap<>();
            List<String> list2 = new ArrayList<>();
            double num = 0;
            for (TSyjzb tSyjzb2 : tSyjzbList) {
                double sd = 0.0;
                if (list.size() % pageSize == 0 && list.size() > 0) {
                    sd = mathUtil.StandardDiviation(list);
                    String sd1 = String.format("%.2f", sd);
                    list2.add(sd1);
                    num = num + sd;
                    list.clear();
                } else {
                    if (!StringUtil.isEmpty(tSyjzb2.getQddbz())) {
                        double qd = Double.parseDouble(tSyjzb2.getQddbz());
                        list.add(qd);
                    }
                }

            }
            double num1 = 0;
            num1 = num / list2.size();
            map.put("data", list2);
            map.put("sjqd", sjqd);
            map.put("num1", String.format("%.2f", num1));
            list3.add(map);
        }
        return Result.OK(list3);
    }


    /**
     * 添加
     *
     * @param tSyjzb
     * @return
     */
    @AutoLog(value = "t_syjzb-添加")
    @ApiOperation(value = "t_syjzb-添加", notes = "t_syjzb-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TSyjzb tSyjzb) {
        tSyjzbService.save(tSyjzb);
        return Result.OK("添加成功！");
    }

    /**
     * 压力机添加
     *
     * @param
     * @return
     */
    @AutoLog(value = "t_syjzb-添加")
    @ApiOperation(value = "t_syjzb-添加", notes = "t_syjzb-添加")
    @PostMapping(value = "/yljadd")
    public Result<?> yljadd(@RequestBody YljPage yljPage) {
        TSyjzb tSyjzb = new TSyjzb();
        BeanUtils.copyProperties(yljPage, tSyjzb);
        tSyjzbService.saveMain(tSyjzb, yljPage.getFsYalijiList());
        return Result.OK("添加成功！");
    }

    /**
     * 万能机添加
     *
     * @param
     * @return
     */
    @AutoLog(value = "t_syjzb-添加")
    @ApiOperation(value = "t_syjzb-添加", notes = "t_syjzb-添加")
    @PostMapping(value = "/addWNJOpen")
    public Result<?> addWNJOpen(@RequestBody TSyjzbWnjPage tSyjzbWnjPage) {
        TSyjzb tSyjzb = new TSyjzb();
        BeanUtils.copyProperties(tSyjzbWnjPage, tSyjzb);
        tSyjzbService.saveMainWNJ(tSyjzb, tSyjzbWnjPage.getFsWangnjList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param tSyjzb
     * @return
     */
    @AutoLog(value = "t_syjzb-编辑")
    @ApiOperation(value = "t_syjzb-编辑", notes = "t_syjzb-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TSyjzb tSyjzb) {
        tSyjzbService.updateById(tSyjzb);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "t_syjzb-通过id删除")
    @ApiOperation(value = "t_syjzb-通过id删除", notes = "t_syjzb-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        tSyjzbService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param
     * @return
     */
    @AutoLog(value = "压力机表信息通过主表ID查询")
    @ApiOperation(value = "压力机表信息主表ID查询", notes = "压力机表信息-通主表ID查询")
    @GetMapping(value = "/selectById")
    public Result<?> list(FsYaliji fYaliji, HttpServletRequest req) {
        QueryWrapper queryWrapper = QueryGenerator.initQueryWrapper(fYaliji, req.getParameterMap());
        queryWrapper.orderBy(true, true, "sjxh");
        List<FsYaliji> list = ifYalijiService.list(queryWrapper);
        for (FsYaliji fsYaliji : list) {
            String qrcode = fsYaliji.getQrcode();
            if (StringUtils.isNotBlank(qrcode)) {
                String[] split = qrcode.split("#");
                fsYaliji.setQrcode(split[0]);
            }
        }
        return Result.OK(list);
    }

    @AutoLog(value = "甬台温南端二维码信息")
    @ApiOperation(value = "压力机表信息主表ID查询", notes = "甬台温南端二维码信息-通主表qrcode查询")
    @GetMapping(value = "/getQrcode")
    public Result<?> getQrcode(@RequestParam(name = "qrcode", required = true)String qrcode) {
        if (qrcode == null){
            return Result.OK("参数为空");
        }
        String body = tSyjzbService.getQrcode(qrcode);
        QrcodeXx qrcodeXx = new QrcodeXx();
        if (body.contains("成功")) {
            JSONObject jsonObject = new JSONObject(body);
            JSONObject obj = jsonObject.getJSONObject("obj");
            String syzs = obj.getStr("sampleShiYanZuShu");
            String qyrq = obj.getStr("sampleDate");
            String sampleGcbw = obj.getStr("sampleGcbw");
            qrcodeXx.setSampleShiYanZuShu(syzs);
            qrcodeXx.setSampleDate(qyrq);
            qrcodeXx.setSampleGcbw(sampleGcbw);
            JSONArray qrCodeArray = obj.getJSONArray("QRCode");
            if (qrCodeArray.size() > 0) {
                JSONObject qrCodeObject = qrCodeArray.getJSONObject(0);
                String yanghuday = qrCodeObject.getStr("yanghuday");
                String rkrq = qyrq.trim();
                // 创建日期格式化器
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                // 将养护天数转换为整数
                int yanghuDay = Integer.parseInt(yanghuday);
                // 将取样日期字符串转换为 LocalDate 对象
                LocalDate sampleDate = LocalDate.parse(rkrq, dateFormatter);
                // 计算出库时间（取样日期 + 养护天数）
                LocalDate outDate = sampleDate.plusDays(yanghuDay);

                qrcodeXx.setYanghuday(yanghuday);
                qrcodeXx.setCYHDATE(String.valueOf(outDate));
                qrcodeXx.setJYHDATE(rkrq);
            }
        }
        return Result.OK(qrcodeXx);
    }


    /**
     * 通过id查询
     *
     * @param
     * @return
     */
    @AutoLog(value = "万能机表信息通过主表ID查询")
    @ApiOperation(value = "万能机表信息主表ID查询", notes = "万能机表信息-通主表ID查询")
    @GetMapping(value = "/selectwnjById")
    public Result<?> list(FWangnj fWangnj, HttpServletRequest req) {
        QueryWrapper queryWrapper = QueryGenerator.initQueryWrapper(fWangnj, req.getParameterMap());
        List<FWangnj> list = ifWangnjService.list(queryWrapper);
        return Result.OK(list);
    }


    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "t_syjzb-批量删除")
    @ApiOperation(value = "t_syjzb-批量删除", notes = "t_syjzb-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.tSyjzbService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "t_syjzb-通过id查询")
    @ApiOperation(value = "t_syjzb-通过id查询", notes = "t_syjzb-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TSyjzb tSyjzb = tSyjzbService.getById(id);
        if (tSyjzb == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(tSyjzb);
    }

    /**
     * 抗压抗折导出api1（报表接口）
     */
    @AutoLog(value = "t_syjzb-通过试验机id查询")
    @ApiOperation(value = "t_syjzb-通过试验机id查询", notes = "t_syjzb-通过试验机id查询")
    @GetMapping(value = "/kykzApi1")
    public Result<?> kykzApi1(@RequestParam(name = "syjid", required = true) String syjid) {
        List data = new ArrayList<>();
        TSyjzb syjzb = tSyjzbService.getOne(new QueryWrapper<TSyjzb>().eq("syjid", syjid));
        YalijiVO one = new YalijiVO();
        one.setSbbh(syjzb.getSbbh());//设备编号
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(syjzb.getSbbh());
        one.setSbname(selectshebeione.getSbname());//设备名称
        one.setSylx(syjzb.getSylx());//试验类型编号
        one.setSylxName(tSyjzbService.selectSylxName(syjzb.getSylx()));//试验类型名称
        one.setSjqd(syjzb.getSjqd());//设计强度
        one.setSjbh(syjzb.getSjbh());//试件编号
        one.setSyrq(syjzb.getSyrq());//试验日期
        one.setSywcsj(syjzb.getSywcsj());//试验完成时间
        one.setCzry(syjzb.getCzry());//操作人员
        one.setDqsj(new Date());//当前时间
        one.setCjmc(syjzb.getCjmc());//工程部位
        one.setPdjg(syjzb.getPdjg());//判定结果
        data.add(one);
        return Result.OKs(data);
    }

    /**
     * 抗压抗折导出api2（报表接口）
     */
    @AutoLog(value = "f_yaliji-通过试验机id查询")
    @ApiOperation(value = "f_yaliji-通过试验机id查询", notes = "f_yaliji-通过试验机id查询")
    @GetMapping(value = "/kykzApi2")
    public Result<?> kykzApi2(@RequestParam(name = "syjid", required = true) String syjid) {
        List data = new ArrayList<>();
        TSyjzb syjzb = tSyjzbService.getOne(new QueryWrapper<TSyjzb>().eq("syjid", syjid));
        List<FsYaliji> yalijis = ifYalijiService.list(new QueryWrapper<FsYaliji>().eq("SYJID", syjid).orderByDesc("SJXH"));
        for (FsYaliji yaliji : yalijis) {
            YalijiVO yalijiVO = new YalijiVO();
            yalijiVO.setLq(syjzb.getLq());
            yalijiVO.setKylz(yaliji.getKylz());
            yalijiVO.setKyqd(yaliji.getKyqd());
            yalijiVO.setSjqd(syjzb.getQddbz());
            data.add(yalijiVO);
        }
        return Result.OKs(data);
    }

    /**
     * 抗压抗折导出api3，过程值（报表接口）
     */
    @AutoLog(value = "f_yaliji-通过试验机id查询过程值")
    @ApiOperation(value = "f_yaliji-通过试验机id查询过程值", notes = "f_yaliji-通过试验机id查询过程值")
    @GetMapping(value = "/getProcessData")
    public Result<?> getProcessData(@RequestParam(name = "syjid", required = true) String syjid) {
        List<FsYaliji> list = ifYalijiService.list(new QueryWrapper<FsYaliji>().eq("SYJID", syjid).orderByDesc("SJXH"));
        if (list == null || list.size() == 0) {
            return Result.error("未找到对应数据");
        }
        List data = new ArrayList();
        for (FsYaliji fsYaliji : list) {
            String sjxh = fsYaliji.getSjxh();
            String[] ysks = fsYaliji.getYskylz().split(",");
            String[] sjs = fsYaliji.getSjgc().split(",");
            for (int i = 0; i < sjs.length; i++) {
                Map map = new HashMap();
                map.put("name", i);
                map.put("value", ysks[i]);
                map.put("type", sjxh);
                data.add(map);
            }
        }
        return Result.OKs(data);
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TSyjzb.class);
    }

    /**
     * 新老07线根据编号导入
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel1", method = RequestMethod.POST)
    public Result<?> importExcel1(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        Map<String, String> replaceMap = new HashMap<>();
        replaceMap.put("xl07100twn", "xlwnj02");
        replaceMap.put("xl0710twn", "xlwnj01");
        replaceMap.put("xl07yl", "xlylj01");
        replaceMap.put("xl07sn", "xlkzky01");

        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();
            if (!file.isEmpty()) {
                try (InputStream inputStream = file.getInputStream()) {
                    Workbook workbook = WorkbookFactory.create(inputStream);
                    Sheet sheet = workbook.getSheetAt(0);
                    Iterator<Row> rowIterator = sheet.iterator();
                    if (rowIterator.hasNext()) {
                        rowIterator.next(); // 跳过第一行表头
                    }
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();
                        String sjbh = row.getCell(0).getStringCellValue().trim();
                        if (sjbh != null && !sjbh.isEmpty()) {
                            TSyjzb tSyjzb = new TSyjzb();
                            tSyjzb.setSjbh(sjbh);
                            List<TSyjzb> list = tSyjzbService.list(new QueryWrapper<TSyjzb>().eq("SJBH", sjbh));
                            if (list != null && !list.isEmpty()) {
                                for (TSyjzb syjzb : list) {
                                    String sbbh = syjzb.getSbbh();
                                    if (replaceMap.containsKey(sbbh)) {
                                        syjzb.setSbbh(replaceMap.get(sbbh));
                                        tSyjzbService.updateById(syjzb);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    return Result.error("导入失败: " + e.getMessage());
                }
            }
        }
        return Result.OK("导入成功");
    }

    /**
     * 压力机导出1
     */
    @AutoLog(value = "压力机数据查询-根据id查询数据并打印")
    @ApiOperation(value = "压力机数据查询-根据id查询数据并打印", notes = "压力机数据查询-根据id查询数据并打印")
    @GetMapping(value = "/print1")
    public Result<?> print1(TSyjzb tSyjzb,
                            String syjid,
                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                            HttpServletRequest req) {
        List data = new ArrayList<>();
        QueryWrapper<TSyjzb> queryWrapper = QueryGenerator.initQueryWrapper(tSyjzb, req.getParameterMap());
        queryWrapper.eq("syjid", syjid);
        TSyjzb one = tSyjzbService.getOne(queryWrapper);
        YljPage yljPage = new YljPage();
        String sbjno = one.getSbbh();
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(sbjno);//根据设备编号查询设备中的数据
        if (selectshebeione != null) {
            String sbname = selectshebeione.getSbname();
            yljPage.setSbname(sbname);
        }
        yljPage.setSyjid(syjid);
        yljPage.setSbbh(one.getSbbh());
        yljPage.setSylx(one.getSylx());//试验类型
        if (one.getCjmc() == null || "".equals(one.getCjmc())) {
            yljPage.setCjmc(one.getSgbw());
        } else {
            yljPage.setCjmc(one.getCjmc());//工程名称
        }
        if (one.getSjbh() == null || "".equals(one.getSjbh())) {
            yljPage.setSjbh(one.getWtbh());
        } else {
            yljPage.setSjbh(one.getSjbh());//试件编号
        }
        yljPage.setLq(one.getLq());//龄期
        yljPage.setSjcc(one.getSjcc());//试件尺寸
        yljPage.setSjsl(one.getSjsl());//试件数量
        yljPage.setSjqd(one.getSjqd());//设计强度
        yljPage.setQddbz(one.getQddbz());//强度代表值
        yljPage.setSyrq(one.getSyrq());//试验日期
        yljPage.setPdjg(one.getPdjg());//判定结果
        yljPage.setCzry(one.getCzry());//操作人员

        List<FsYaliji> fsYaliji = new ArrayList<>();
        List<FYaliji> list = ifYalijiService.selectFYalijiList(syjid);
        for (FYaliji fylj : list) {
            FsYaliji fsYaliji1 = new FsYaliji();
            fsYaliji1.setSjxh(fylj.getSjxh());//试件序号
            fsYaliji1.setKylz(fylj.getKylz());//抗压力值
            fsYaliji1.setKyqd(fylj.getKyqd());//抗压强度
            fsYaliji1.setSysj(fylj.getSysj());//试验时间
            fsYaliji1.setWcsj(fylj.getWcsj());//完成时间
            fsYaliji.add(fsYaliji1);
            yljPage.setFsYalijiList(fsYaliji);
        }
        data.add(yljPage);
        return Result.OKs(data);
    }

    /**
     * 压力机导出2
     */
    @AutoLog(value = "压力机数据打印-积木报表api数据源1")
    @ApiOperation(value = "压力机数据打印-积木报表api数据源", notes = "压力机数据打印-积木报表api数据源")
    @GetMapping(value = "/dataSource")
    public Result<?> dataSource(TSyjzb tSyjzb,
                                String syjid,
                                HttpServletRequest req) {
        List data = new ArrayList<>();
        QueryWrapper<TSyjzb> queryWrapper = QueryGenerator.initQueryWrapper(tSyjzb, req.getParameterMap());
        queryWrapper.eq("syjid", syjid);
        TSyjzb one = tSyjzbService.getOne(queryWrapper);
        List<FYaliji> list = ifYalijiService.selectFYalijiList(syjid);
        for (FYaliji fylj : list) {
            YljPage yljPage = new YljPage();
            yljPage.setLq(one.getLq());
            yljPage.setSjxh(fylj.getSjxh());
            yljPage.setKylz(fylj.getKylz());
            yljPage.setKyqd(fylj.getKyqd());
            yljPage.setSysj(fylj.getSysj());
            yljPage.setWcsj(fylj.getWcsj());
            data.add(yljPage);
        }
        return Result.OKs(data);
    }

    /**
     * 压力机导出3，过程值
     */
    @AutoLog(value = "压力机数据打印-积木报表api数据源2")
    @ApiOperation(value = "压力机数据打印-积木报表api数据源", notes = "压力机数据打印-积木报表api数据源")
    @GetMapping(value = "/gcz")
    public Result<?> selectGcz(String syjid) {//报表折线图过程值
        ArrayList<Object> data = new ArrayList<>();

        QueryWrapper<FsYaliji> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("syjid", syjid);
        queryWrapper.orderByAsc("sjxh");
        List<FsYaliji> list = ifYalijiService.list(queryWrapper);

        for (FsYaliji ysliji : list) {
            String sjxh = ysliji.getSjxh();

            String yskylzgcz = ysliji.getYskylz();
            String[] kylzgc = yskylzgcz.split(",");
            for (int i = 0; i < kylzgc.length; i++) {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", String.valueOf(i));
                map.put("value", kylzgc[i]);
                map.put("type", sjxh);
                data.add(map);
            }
        }
        return Result.OKs(data);
    }

    /**
     * 将字符串转化为timestamp
     */
    public Date timeTransformation(String time) {
        if (time == null || time == "") {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            return date;
        }
//		 SimpleDateFormat sdf = new SimpleDateFormat(format);
//		 return String.valueOf(sdf.parse(date_str).getTime()/1000);

    }

    /**
     * 试验机统计当月超标
     *
     * @return
     */
    @RequestMapping(value = "/countList", method = RequestMethod.GET)
    public Result<?> countList(String category) {
        String[] categorys = category.split(",");
        return Result.OK(tSyjzbService.countList(categorys));
    }

    /**
     * 义东高建看板-统计试验检测近30天的预警数/闭合数/闭合率
     */
    @RequestMapping(value = "/tjWarning", method = RequestMethod.GET)
    public Result<?> tjWarningData(TSyjzb tSyjzb, String orgCode) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String shebei = String.valueOf(redisUtil.get(loginUser.getId()));

        Map map = new HashMap();
        NumberFormat nf = NumberFormat.getInstance();
        ArrayList<YJDateVo> yjDateVos = new ArrayList<>();
        if (orgCode != null) {//参数传的有项目编号
            List<TSyjzb> countWarningByOC = tSyjzbService.getCountWarningByOC(orgCode);//根据项目查近30天的预警数据
            Integer countWar = countWarningByOC.size();//根据项目查近30天的预警数
            for (TSyjzb syjzb : countWarningByOC) {
                YJDateVo yjDateVo = new YJDateVo();
                yjDateVo.setId(syjzb.getId());
                ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(syjzb.getSbbh());
                String departName = tSyjzbService.getDepartName(selectshebeione.getSysOrgCode());
                yjDateVo.setOrgCodeName(departName);
                yjDateVo.setSysOrgCode(selectshebeione.getSysOrgCode());
                yjDateVo.setSbbh(syjzb.getSbbh());
                String sbtypeName = tSyjzbService.getSbtypeName(selectshebeione.getSbtype());
                if ("不合格".equals(syjzb.getPdjg()) || "无效".equals(syjzb.getPdjg())) {
                    yjDateVo.setYjinfo(sbtypeName + "试验不合格");
                }
                yjDateVo.setSyrq(syjzb.getSyrq());
                yjDateVo.setOverproofStatus(syjzb.getOverproofStatus() == 20 ? "已闭合" : "未闭合");
                yjDateVos.add(yjDateVo);
            }

            List<TSyjzb> countBHByOC = tSyjzbService.getCountBHByOC(orgCode);//根据项目查近30天的预警闭合数据
            Integer countBH = countBHByOC.size();//根据项目查近30天的预警闭合数

            float result = (float) countBH / countWar;
            nf.setMaximumFractionDigits(1);
            String BHlv = nf.format(result * 100);//闭合率=闭合数/预警数
            map.put("YJnum", countWar);//预警数
            map.put("BHnum", countBH);//闭合数
            if (countWar == 0) {//判断分母等于0的情况，避免出现乱码
                map.put("BHlv", "0%");//闭合率
            } else {
                map.put("BHlv", BHlv + "%");//闭合率
            }
            map.put("YJdate", yjDateVos);//预警记录
        } else {//参数没有传项目编号
            List<TSyjzb> countWarning = tSyjzbService.getCountWarning();//近30天的所有预警数据
            Integer countWar = countWarning.size();//近30天的所有预警数
            for (TSyjzb syjzb : countWarning) {
                YJDateVo yjDateVo = new YJDateVo();
                yjDateVo.setId(syjzb.getId());
                ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(syjzb.getSbbh());
                String departName = tSyjzbService.getDepartName(selectshebeione.getSysOrgCode());
                yjDateVo.setOrgCodeName(departName);
                yjDateVo.setSysOrgCode(selectshebeione.getSysOrgCode());
                yjDateVo.setSbbh(syjzb.getSbbh());
                String sbtypeName = tSyjzbService.getSbtypeName(selectshebeione.getSbtype());
                if ("不合格".equals(syjzb.getPdjg()) || "无效".equals(syjzb.getPdjg())) {
                    yjDateVo.setYjinfo(sbtypeName + "试验不合格");
                }
                yjDateVo.setSyrq(syjzb.getSyrq());
                yjDateVo.setOverproofStatus(syjzb.getOverproofStatus() == 20 ? "已闭合" : "未闭合");
                yjDateVos.add(yjDateVo);
            }
            List<TSyjzb> countBH1 = tSyjzbService.getCountBH();//近30天的所有闭合数据
            Integer countBH = countBH1.size();//近30天的所有闭合数

            float result = (float) countBH / countWar;
            nf.setMaximumFractionDigits(1);
            String BHlv = nf.format(result * 100);//闭合率=闭合数/预警数
            map.put("YJnum", countWar);//预警数
            map.put("BHnum", countBH);//闭合数
            if (countWar == 0) {//判断分母等于0的情况，避免出现乱码
                map.put("BHlv", "0%");//闭合率
            } else {
                map.put("BHlv", BHlv + "%");//闭合率
            }
            map.put("YJdate", yjDateVos);//预警记录
        }
        return Result.OK(map);
    }

    /**
     * 万能机导出1
     */
    @AutoLog(value = "万能机数据查询-根据id查询数据并打印")
    @ApiOperation(value = "万能机数据查询-根据id查询数据并打印", notes = "万能机数据查询-根据id查询数据并打印")
    @GetMapping(value = "/printWnj")
    public Result<?> printWnj(String syjid) {
        ArrayList<Object> data = new ArrayList<>();

        QueryWrapper<TSyjzb> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("syjid", syjid);
        TSyjzb one = tSyjzbService.getOne(queryWrapper);
        QueryWrapper<FWangnj> wnjQuery = new QueryWrapper<>();
        wnjQuery.eq("syjid", syjid);
        wnjQuery.orderByAsc("sjxh");
        List<FWangnj> list = ifWangnjService.list(wnjQuery);
        for (FWangnj fWangnj : list) {
            FWangnj wnj = new FWangnj();
            wnj.setDkwz(one.getGczj());//试件尺寸/公称直径
            wnj.setSjxh(fWangnj.getSjxh());//试件序号
            wnj.setWsbz(fWangnj.getWsbz());//原始标距
            wnj.setDhbz(fWangnj.getDhbz());//断后标距
            wnj.setLz(fWangnj.getLz());//最大力值
            wnj.setLzqd(fWangnj.getLzqd());//抗拉强度
            wnj.setQflz(fWangnj.getQflz());//屈服力值
            wnj.setQfqd(fWangnj.getQfqd());//屈服强度
            wnj.setScl(fWangnj.getScl());//伸长率
            wnj.setZdlzscl(fWangnj.getZdlzscl());//最大力值伸长率
            String sylxName = tSyjzbService.selectSylxName(one.getSylx());
            wnj.setLdcms(sylxName);
            data.add(wnj);
        }
        return Result.OKs(data);
    }

    /**
     * 万能机导出2，过程值
     */
    @AutoLog(value = "万能机数据查询-根据id查询数据并打印")
    @ApiOperation(value = "万能机数据查询-根据id查询数据并打印", notes = "万能机数据查询-根据id查询数据并打印")
    @GetMapping(value = "/wnjGcz")
    public Result<?> wnjGcz(String syjid) {
        ArrayList<Object> data = new ArrayList<>();
        QueryWrapper<FWangnj> wnjQuery = new QueryWrapper<>();
        wnjQuery.eq("syjid", syjid);
        List<FWangnj> list = ifWangnjService.list(wnjQuery);
        for (FWangnj fWangnj : list) {
            String sjxh = fWangnj.getSjxh();
            String lzgcz = fWangnj.getLzgc();
            String[] gcz = lzgcz.split(",");
            for (int i = 0; i < gcz.length; i++) {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", String.valueOf(i));
                map.put("value", gcz[i]);
                map.put("type", sjxh);
                if (i % 2 == 0) {
                    data.add(map);
                }
            }
        }
        return Result.OKs(data);
    }

    /**
     * 压力机破碎照片
     */
    @AutoLog(value = "压力机破碎照片")
    @ApiOperation(value = "压力机破碎照片", notes = "压力机破碎照片")
    @GetMapping(value = "/yljPsPic")
    public Result<?> yljPsPic(Integer id) {
        Map map = tSyjzbService.getPsPicBySyjid(id);
        return Result.OK(map);
    }

    @AutoLog(value = "t_syjzb-标准差查询")
    @ApiOperation(value = "t_syjzb-分页列表查询", notes = "t_syjzb-标准差查询")
    @GetMapping(value = "/bzclist")
    public Result<?> bzclist(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest request, bzc bzc) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        //条件查询
        String sbbh1 = bzc.getSbbh();
        String lq = bzc.getLq();
        String stadate = bzc.getStadate();
        String enddate = bzc.getEnddate();
        String sjqd = bzc.getSJQD();
        if (StringUtil.isNotEmpty(sbbh1)) {
            StringBuilder inCondition = new StringBuilder();
            inCondition = inCondition.append("'").append(sbbh1).append("'");
            sbbh1 = inCondition.toString();
        }else {
            String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
            // 将字符串以逗号分隔成数组
            String[] sbArray = shebei.split(",");

            // 构造 SQL 中 IN 条件的字符串
            StringBuilder inCondition = new StringBuilder();
            for (int i = 0; i < sbArray.length; i++) {
                inCondition.append("'").append(sbArray[i]).append("'");
                if (i < sbArray.length - 1) {
                    inCondition.append(",");
                }
            }
            sbbh1 = inCondition.toString();
        }


        // 执行分页查询
        List<bzc> bzcList = tSyjzbService.getBzcList((pageNo - 1) * pageSize, pageSize, sbbh1, lq, sjqd, stadate, enddate);
        for (com.trtm.iot.syj.entity.bzc bzc1 : bzcList) {
            String sbbh = bzc1.getSbbh();
            ShebeiInfo sbjwd = shebeiInfoService.SBJWD(sbbh);
            bzc1.setSbname(sbjwd.getSbname());
        }

        IPage page = new Page();
        page.setRecords(bzcList);
        page.setSize(pageSize);
        page.setCurrent(pageNo);
        int bzcListcount = tSyjzbService.getBzcListcount(sbbh1, lq, sjqd, stadate, enddate);
        page.setTotal(bzcListcount);
        double result = Math.ceil(bzcListcount/pageSize);
        int roundedResult = (int) result;
        page.setPages(roundedResult);

        return Result.OK(page);
    }
}
