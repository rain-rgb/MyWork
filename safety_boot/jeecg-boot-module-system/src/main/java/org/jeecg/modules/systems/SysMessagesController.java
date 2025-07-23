package org.jeecg.modules.systems;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.api.SignController;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.bhzStatistics.service.IBhzCementStatisticsService;
import com.trtm.iot.bhzSupervisionOrder.entity.BhzSupervisionOrder;
import com.trtm.iot.bhzSupervisionOrder.service.IBhzSupervisionOrderService;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.bys.entity.BysReal;
import com.trtm.iot.bys.service.IBysRealService;
import com.trtm.iot.chaoshengbo.entity.ChaoshengboSyjbsj;
import com.trtm.iot.chaoshengbo.service.IChaoshengboSyjbsjService;
import com.trtm.iot.cunliangprocedureconfig.entity.CunliangProcedureConfig;
import com.trtm.iot.cunliangprocedureconfig.service.ICunliangProcedureConfigService;
import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.service.IDeviceMixpileHistorydataOneService;
import com.trtm.iot.deviceMixpileHistorydataOne.vo.DeviceMixpileHistorydataOneXls;
import com.trtm.iot.devicehistorystatic.entity.DevicehistoryStatic;
import com.trtm.iot.devicehistorystatic.service.IDevicehistoryStaticService;
import com.trtm.iot.entityprogresscheck.entity.EntityCheckDetial;
import com.trtm.iot.entityprogresscheck.entity.EntityProgresscheck;
import com.trtm.iot.entityprogresscheck.service.IEntityCheckDetialService;
import com.trtm.iot.entityprogresscheck.service.IEntityProgresscheckService;
import com.trtm.iot.hc_token.entity.HcToken;
import com.trtm.iot.hc_token.service.IHcTokenService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntconsigncode.entity.HntConsignCode;
import com.trtm.iot.hntconsigncode.service.IHntConsignCodeService;
import com.trtm.iot.hntconsignsamplepos.entity.HntConsignSamplePos;
import com.trtm.iot.hntconsignsamplepos.service.IHntConsignSamplePosService;
import com.trtm.iot.hntconsignsamplepos.vo.HntConsignSamplePosPage;
import com.trtm.iot.lqbhzStatistics.service.IBhzLqStatisticsService;
import com.trtm.iot.monitor.entity.Monitor;
import com.trtm.iot.monitor.service.IMonitorService;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.trgangjinbhcm.entity.TrGangjinbhcM;
import com.trtm.iot.trgangjinbhcm.service.ITrGangjinbhcMService;
import com.trtm.iot.wbshebeidetail.entity.WbshebeiDetail;
import com.trtm.iot.wbshebeidetail.service.IWbshebeiDetailService;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import com.trtm.iot.wzcailiaonamedictman.entity.WzcailiaonamedictMan;
import com.trtm.iot.wzcailiaonamedictman.service.IWzcailiaonamedictManService;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;
import com.trtm.iot.wzgongyingshangman.entity.WzgongyingshangMan;
import com.trtm.iot.wzgongyingshangman.service.IWzgongyingshangManService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.service.IWztaizhangLcService;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import com.trtm.iot.wzycljinchanggbman.entity.WzycljinchanggbMan;
import com.trtm.iot.wzycljinchanggbman.service.IWzycljinchanggbManService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import com.trtm.iot.yclsl.vo.WzycljinchanggbPush;
import com.trtm.iot.zhilianggongxu.entity.ZhiliangGongxu;
import com.trtm.iot.zhilianggongxu.service.IZhiliangGongxuService;
import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import com.trtm.iot.zhiliangrenwudan.service.IZhiliangrenwudanService;
import com.trtm.iot.zhiliangrenwudan.vo.ZhiliangRWD;
import com.xkcoding.http.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.job.jobutil.HttpRequestUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.util.HttpUtils;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.*;
import java.util.*;

import static org.jeecg.common.util.DateUtils.date2Str;
import static org.jeecg.common.util.DateUtils.dateformat;

/**
 * @Description: 消息
 * @author: jeecg-boot
 * @date: 2019-04-09
 * @version: V1.0
 */
@Slf4j
@RestController
@RequestMapping("/sys/systems/sysMessages")
public class SysMessagesController extends JeecgController<SysMessage, ISysMessageService> {
    @Autowired
    private ISysMessageService sysMessageService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Autowired
    private IZhiliangrenwudanService zhiliangrenwudanService;
    @Autowired
    private IZhiliangGongxuService zhiliangGongxuService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private ISysDictItemService sysDictItemService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IDeviceMixpileHistorydataOneService deviceMixpileHistorydataOneService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private IHntConsignSamplePosService hntConsignSamplePosService;
    @Autowired
    private IHntConsignCodeService hntConsignCodeService;
    @Autowired
    private IBysRealService bysRealService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private IWbshebeiDetailService wbshebeiDetailService;
    @Autowired
    private ITrGangjinbhcMService trGangjinbhcMService;
    @Autowired
    private IBhzCementStatisticsService bhzCementStatisticsService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IWztaizhangService wztaizhangService;
    @Autowired
    private ITSyjzbService tSyjzbService;
    @Autowired
    private IDevicehistoryStaticService devicehistoryStaticService;
    @Autowired
    private IEntityProgresscheckService entityProgresscheckService;
    @Autowired
    private IEntityCheckDetialService entityCheckDetialService;
    @Autowired
    private IChaoshengboSyjbsjService chaoshengboSyjbsjService;
    @Autowired
    private IBhzLqStatisticsService bhzLqStatisticsService;
    @Autowired
    private ICunliangProcedureConfigService cunliangProcedureConfigService;

    @Autowired
    private IMonitorService monitorService;

    @Autowired
    private IWzycljinchanggbService wzycljinchanggbService;
    @Autowired
    private IWzliaocangService wzliaocangService;
    @Autowired
    private IWzgongyingshangService wzgongyingshangService;
    @Autowired
    private IWzcailiaonamedictService wzcailiaonamedictService;

    @Autowired
    private IWzycljinchanggbManService wzycljinchanggbManService;

    @Autowired
    private IWzgongyingshangManService wzgongyingshangManService;
    @Autowired
    private IWzcailiaonamedictManService wzcailiaonamedictManService;

    @Autowired
    private IBhzCallCfgService bhzCallCfgService;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Autowired
    private IBhzSupervisionOrderService bhzSupervisionOrderService;
    @Autowired
    private IHcTokenService hcTokenService;

    @Autowired
    private IWztaizhangLcService tzlcService;


    @RequestMapping("/ljlm")
    public void ljlm(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String orgCode = loginUser.getOrgCode();
        String sys_depart_orgcode =  request.getParameter("sys_depart_orgcode");
        QueryWrapper<HcToken> queryWrapper = new QueryWrapper<>();
        String code = "";
        if(StringUtils.isNotBlank(sys_depart_orgcode)){
            code = sys_depart_orgcode;
        }else {
            code = orgCode;
        }
        queryWrapper.eq("orgcode", code);
        HcToken  one = hcTokenService.getOne(queryWrapper);
        if(one == null){
            QueryWrapper<HcToken> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.likeRight("orgcode", code);
            queryWrapper1.orderByAsc("orgcode");
            List<HcToken> list = hcTokenService.list(queryWrapper1);
            if(list.size()>0){
                one = list.get(0);
            }
        }
        //路基路面
        resp.sendRedirect("https://dp.huace.cn/digitalPlatform/module/vehicleLocation/vehicleOnlineRecord.shtml?v=2&sid=" + one.getSid());
    }

    @RequestMapping("/jxdw")
    public void jxdw(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String orgCode = loginUser.getOrgCode();
        String sys_depart_orgcode =  request.getParameter("sys_depart_orgcode");
        QueryWrapper<HcToken> queryWrapper = new QueryWrapper<>();
        String code = "";
        if(StringUtils.isNotBlank(sys_depart_orgcode)){
            code = sys_depart_orgcode;
        }else {
            code = orgCode;
        }
        queryWrapper.eq("orgcode", code);
        HcToken  one = hcTokenService.getOne(queryWrapper);
        if(one == null){
            QueryWrapper<HcToken> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.likeRight("orgcode", code);
            queryWrapper1.orderByAsc("orgcode");
            List<HcToken> list = hcTokenService.list(queryWrapper1);
            if(list.size()>0){
                one = list.get(0);
            }
        }
        //机械定位
        resp.sendRedirect("https://dp.huace.cn/digitalPlatform/module/vehicleLocation/listUI.shtml?v=2&sid=" + one.getSid());
    }

    @RequestMapping("/ysrb")
    public void ysrb(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String orgCode = loginUser.getOrgCode();
        String sys_depart_orgcode =  request.getParameter("sys_depart_orgcode");
        QueryWrapper<HcToken> queryWrapper = new QueryWrapper<>();
        String code = "";
        if(StringUtils.isNotBlank(sys_depart_orgcode)){
            code = sys_depart_orgcode;
        }else {
            code = orgCode;
        }
        queryWrapper.eq("orgcode", code);
        HcToken  one = hcTokenService.getOne(queryWrapper);
        if(one == null){
            QueryWrapper<HcToken> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.likeRight("orgcode", code);
            queryWrapper1.orderByAsc("orgcode");
            List<HcToken> list = hcTokenService.list(queryWrapper1);
            if(list.size()>0){
                one = list.get(0);
            }
        }
        //压实日报
        resp.sendRedirect("https://dp.huace.cn/digitalPlatform/module/dailyReport/list.shtml?v=2&sid=" + one.getSid());
    }

    @AutoLog(value = "bhz_supervision_order-指令单确认")
    @ApiOperation(value = "bhz_supervision_order-指令单确认", notes = "bhz_supervision_order-指令单确认")
    @PutMapping(value = "/editOk")
    public Result<?> editOk(@RequestBody BhzSupervisionOrder bhzSupervisionOrder) throws Exception {
        if(bhzSupervisionOrder.getPenaltyAmount() == null){
            return Result.error("未选择惩罚金额");
        }
        // 通过工具类生成pdf并上传服务器
        String url = bhzSupervisionOrder.getZldurl();
        // 第一步：创建签署文档，调用接口【上传本地文件创建签署文档】，获取文档 ID。
        String documentId = SignController.createbyfile(url);
        bhzSupervisionOrder.setDocumentid(documentId);
        // 第二步：使用第一步生成的文档 ID 创建并发起电子签约，调用接口【创建或发起电子签约】，获取【电子签约】ID。 mapList 为签署
        BhzCallCfg cfg = bhzCallCfgService.selectbhzcallone(bhzSupervisionOrder.getShebeiNo());
        if(cfg != null && StringUtils.isNotBlank(cfg.getShperson())){
            // 专监签字
            LoginUser zj = sysBaseAPI.getUserByName(cfg.getShperson());
            Map<String, String> map = new HashMap<String, String>() {{
                put("operatorName", zj.getRealname());
                put("operatorContact", zj.getPhone());
            }};

            // 总监签字
            LoginUser zjl = sysBaseAPI.getUserByName(cfg.getSupervisorPerson());
            Map<String, String> map1 = new HashMap<String, String>() {{
                put("operatorName", zjl.getRealname());
                put("operatorContact", zjl.getPhone());
            }};
            // 施工单位
            LoginUser sgdw = sysBaseAPI.getUserByName(cfg.getCzperson());
            Map<String, String> map3 = new HashMap<String, String>() {{
                put("operatorName", sgdw.getRealname());
                put("operatorContact", sgdw.getPhone());
            }};
            List<Map<String,String>> mapList = Arrays.asList(map,map1,map3);

            String contractId = SignController.createbycategory(documentId,mapList);
            bhzSupervisionOrder.setContractid(contractId);
        }else{
            return Result.error("该设备未配置预警处置人员，请联系实施管理员进行配置！");
        }





        bhzSupervisionOrderService.updateById(bhzSupervisionOrder);


        return Result.OK("编辑成功!");
    }


    /**
     * 根据权限查询质量预警短信
     *
     * @param sysMessage
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SysMessage sysMessage, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (sysMessage.getRemark() == null) {
            if (!"null".equals(shebei)) {
                sysMessage.setRemark(shebei);
            } else {
                sysMessage.setRemark("空");
            }
        }
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format = ft.format(new Date());
        QueryWrapper<SysMessage> queryWrapper = QueryGenerator.initQueryWrapper(sysMessage, req.getParameterMap());
        queryWrapper.likeRight("create_time", format);
        List<SysMessage> pageList = sysMessageService.list(queryWrapper);
        for (SysMessage sysMessage1 : pageList) {
            JSONObject jsonObject = JSONUtil.parseObj(sysMessage1.getEsContent());
            if (jsonObject != null) {
                sysMessage1.setEsParam(jsonObject.getStr("sbname"));
                sysMessage1.setRemark(jsonObject.getStr("content"));
            }
        }
        return Result.ok(pageList);
    }

    /**
     * 疏港梁场大屏车间驾驶数据
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-疏港梁场大屏车间驾驶数据")
    @ApiOperation(value = "任务单（制梁）表信息-疏港梁场大屏车间驾驶数据", notes = "任务单（制梁）表信息-疏港梁场大屏车间驾驶数据")
    @GetMapping(value = "/list13")
    public Result<?> queryPageList14(Zhiliangrenwudan zhiliangrenwudan,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sysOrgCode,
                                     HttpServletRequest req) {
        List list = new ArrayList();
        QueryWrapper<SysDict> queryWrapper4 = new QueryWrapper<>();
        queryWrapper4.eq("dict_code", "stations");
        SysDict sysDict = sysDictService.getOne(queryWrapper4);
        if (sysDict != null) {
            QueryWrapper<SysDictItem> queryWrapper5 = new QueryWrapper<>();
            queryWrapper5.eq("dict_id", sysDict.getId());
            List<SysDictItem> sysDictItemList = sysDictItemService.list(queryWrapper5);
            if (sysDictItemList.size() > 0) {
                for (SysDictItem sysDictItem : sysDictItemList) {
                    ZhiliangRWD zhiliangRWD = new ZhiliangRWD();
                    zhiliangRWD.setStation(Integer.valueOf(sysDictItem.getItemValue()));
                    zhiliangRWD.setStationname(sysDictItem.getItemText());
                    List<Zhiliangrenwudan> zhiliangrenwudanList = zhiliangrenwudanService.selectones(Integer.valueOf(sysDictItem.getItemValue()), sysOrgCode);
                    if (zhiliangrenwudanList.size() > 0) {
                        for (Zhiliangrenwudan zhiliangrenwudan1 : zhiliangrenwudanList) {
                            LambdaQueryWrapper<ZhiliangGongxu> queryWrapper1 = new LambdaQueryWrapper<>();
                            queryWrapper1.eq(ZhiliangGongxu::getUuid, zhiliangrenwudan1.getUuid());
                            queryWrapper1.eq(ZhiliangGongxu::getXuhao, 7);
                            queryWrapper1.eq(ZhiliangGongxu::getStatus, 0);
                            queryWrapper1.eq(ZhiliangGongxu::getIsdel, 0);
                            ZhiliangGongxu one = zhiliangGongxuService.getOne(queryWrapper1);
                            if (one != null) {
                                LambdaQueryWrapper<ZhiliangGongxu> queryWrapper2 = new LambdaQueryWrapper<>();
                                queryWrapper2.eq(ZhiliangGongxu::getUuid, zhiliangrenwudan1.getUuid());
                                queryWrapper2.eq(ZhiliangGongxu::getXuhao, 1);
                                queryWrapper2.eq(ZhiliangGongxu::getStatus, 2);
                                queryWrapper2.eq(ZhiliangGongxu::getIsdel, 0);
                                ZhiliangGongxu one1 = zhiliangGongxuService.getOne(queryWrapper2);
                                if (one1 != null) {
                                    LambdaQueryWrapper<ZhiliangGongxu> queryWrapper3 = new LambdaQueryWrapper<>();
                                    queryWrapper3.eq(ZhiliangGongxu::getUuid, one1.getUuid());
                                    queryWrapper3.eq(ZhiliangGongxu::getIsdel, 0);
                                    List<ZhiliangGongxu> one3 = zhiliangGongxuService.list(queryWrapper3);
                                    zhiliangRWD.setZhiliangrenwudan(zhiliangrenwudan1);
                                    zhiliangRWD.setZhiliangGongxuList(one3);
                                }
                            }
                        }
                    }
                    list.add(zhiliangRWD);
                }
            }
        }
        return Result.OK(list);
    }

    /**
     * 疏港梁场大屏车间驾驶数据
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-疏港梁场大屏车间驾驶数据")
    @ApiOperation(value = "任务单（制梁）表信息-疏港梁场大屏车间驾驶数据", notes = "任务单（制梁）表信息-疏港梁场大屏车间驾驶数据")
    @GetMapping(value = "/list14")
    public Result<?> queryPageList15(Zhiliangrenwudan zhiliangrenwudan,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sysOrgCode,
                                     HttpServletRequest req) {
        List list = new ArrayList();
        QueryWrapper<SysDict> queryWrapper4 = new QueryWrapper<>();
        queryWrapper4.eq("dict_code", "stations");
        SysDict sysDict = sysDictService.getOne(queryWrapper4);
        if (sysDict != null) {
            QueryWrapper<SysDictItem> queryWrapper5 = new QueryWrapper<>();
            queryWrapper5.eq("dict_id", sysDict.getId());
            List<SysDictItem> sysDictItemList = sysDictItemService.list(queryWrapper5);
            if (sysDictItemList.size() > 0) {
                for (SysDictItem sysDictItem : sysDictItemList) {
                    ZhiliangRWD zhiliangRWD = new ZhiliangRWD();
                    zhiliangRWD.setStation(Integer.valueOf(sysDictItem.getItemValue()));
                    zhiliangRWD.setStationname(sysDictItem.getItemText());
                    zhiliangRWD.setStationstatus("空闲");
                    List<Zhiliangrenwudan> zhiliangrenwudanList = zhiliangrenwudanService.selectones(Integer.valueOf(sysDictItem.getItemValue()), sysOrgCode);
                    if (zhiliangrenwudanList.size() > 0) {
                        for (Zhiliangrenwudan zhiliangrenwudan1 : zhiliangrenwudanList) {
                            LambdaQueryWrapper<ZhiliangGongxu> queryWrapper1 = new LambdaQueryWrapper<>();
                            queryWrapper1.eq(ZhiliangGongxu::getUuid, zhiliangrenwudan1.getUuid());
                            queryWrapper1.eq(ZhiliangGongxu::getXuhao, 7);
                            queryWrapper1.eq(ZhiliangGongxu::getStatus, 0);
                            queryWrapper1.eq(ZhiliangGongxu::getIsdel, 0);
                            ZhiliangGongxu one = zhiliangGongxuService.getOne(queryWrapper1);
                            if (one != null) {
                                LambdaQueryWrapper<ZhiliangGongxu> queryWrapper2 = new LambdaQueryWrapper<>();
                                queryWrapper2.eq(ZhiliangGongxu::getUuid, zhiliangrenwudan1.getUuid());
                                queryWrapper2.eq(ZhiliangGongxu::getXuhao, 1);
                                queryWrapper2.eq(ZhiliangGongxu::getStatus, 2);
                                queryWrapper2.eq(ZhiliangGongxu::getIsdel, 0);
                                ZhiliangGongxu one1 = zhiliangGongxuService.getOne(queryWrapper2);
                                if (one1 != null) {
                                    zhiliangRWD.setStationstatus("生产中");
                                }
                            }
                        }
                    }
                    list.add(zhiliangRWD);
                }
            }
        }
        return Result.OK(list);
    }

    /**
     * 导出水泥搅拌桩成桩记录信息表 excel
     *
     * @param request
     * @param deviceMixpileHistorydataOne
     */
    @GetMapping(value = "/mixpileexportapiXls")
    public Result<?> exportapiXls(HttpServletRequest request, String start, String end, String piletype, String shebeino, String mileage, DeviceMixpileHistorydataOne deviceMixpileHistorydataOne) throws ParseException, UnsupportedEncodingException {
        Map map = new HashMap();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        if (mileage != null && mileage.contains("%7E")){
            mileage = URLEncoder.encode(mileage, "UTF-8");
            mileage = mileage.replace("%7E", "~");
        }
        if (mileage != null && mileage.contains(" ")){
            mileage = mileage.replace(" ", "+");
        }
        //查询到他的设备编号
        String[] split = shebei.split(",");
        List<String> shebeilist = new ArrayList<>();
        Collections.addAll(shebeilist, split);
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new QueryWrapper<>();
        if (!"undefined".equals(piletype) && !"".equals(piletype) && piletype != null) {
            if ("0".equals(piletype)) {
                queryWrapper.in("piletype", 0, 2);
            } else if ("1".equals(piletype)) {
                queryWrapper.in("piletype", 1, 3);
            }
        }
        if (!"undefined".equals(shebeino) && !"".equals(shebeino) && shebeino != null) {
            queryWrapper.eq("shebeino", shebeino);
        } else {
            queryWrapper.in("shebeino", shebeilist);
        }
        if (!"undefined".equals(mileage) && !"".equals(mileage) && mileage != null) {
            queryWrapper.like("pile_mileage", mileage);
//            queryWrapper.like("pile_mileage", "K175+480");
//            queryWrapper.like("pile_mileage", "K175+700");
        }


        List data = new ArrayList<>();

        if (!StringUtils.isEmpty(start) && !StringUtils.isEmpty(end) && !"undefined".equals(start) && !"undefined".equals(end)) {
            queryWrapper.between("pile_time", start, end);
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date zero = calendar.getTime();
            queryWrapper.between("pile_time", sdf.format(zero), sdf.format(new Date()));
        }
        if (loginUser.getOrgCode().contains("A05A01A08A16A03")){
            queryWrapper.orderByDesc("pile_mileage").orderByDesc("CAST(pile_no AS SIGNED)");
        }else {
            queryWrapper.orderByDesc("pile_time");
        }
        List<DeviceMixpileHistorydataOne> pageList = deviceMixpileHistorydataOneService.list(queryWrapper);
        Integer id = 0;
        for (DeviceMixpileHistorydataOne one : pageList) {
            //如果大于一除1000
            double v = Double.parseDouble(one.getPileUobeton());
//            if (v > 10){
//                one.setPileUobeton(String.valueOf(v/1000));
//            }
            DeviceMixpileHistorydataOneXls xls = new DeviceMixpileHistorydataOneXls();
            id = id + 1;
            xls.setId(id);
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(one.getShebeino());
            xls.setShebeino(selectshebeione.getSbname());
            SysDepart queryone = sysDepartService.queryone(selectshebeione.getSysOrgCode());
            xls.setSupervisionUnit(queryone.getSupervisionUnit());
            xls.setConstructionUnit(queryone.getConstructionUnit());
            xls.setDepartNameAbbr(queryone.getDepartNameAbbr());
            xls.setPileWorktime(one.getPileWorktime());
            xls.setPileDowntime(one.getPileDowntime());
            xls.setPileUptime(one.getPileUptime());
            xls.setPileTime(one.getPileTime());
            xls.setDatatime(one.getDatatime());
            xls.setPileDesigndep(one.getPileDesigndep());
            xls.setGzcount(one.getGzcount());

            try {
                if (!StringUtils.isEmpty(one.getPileDownbeton())) {
                    xls.setPileDownbeton(String.format("%.2f", Double.valueOf(one.getPileDownbeton())));
                } else {
                    xls.setPileDownbeton("0");
                }
            } catch (NumberFormatException e) {
                xls.setPileDownbeton("0");
                e.printStackTrace();
            }
            try {
                if (!StringUtils.isEmpty(one.getPileRealdep())) {
                    xls.setPileRealdep(String.format("%.2f", Double.valueOf(one.getPileRealdep())));
                } else {
                    xls.setPileRealdep("0");
                }
            } catch (NumberFormatException e) {
                xls.setPileRealdep("0");
                e.printStackTrace();
            }
            try {
                if (!StringUtils.isEmpty(one.getPileMaxelectr())) {
                    xls.setPileMaxelectr(String.format("%.2f", Double.valueOf(one.getPileMaxelectr())));
                } else {
                    xls.setPileMaxelectr("0");
                }
            } catch (NumberFormatException e) {
                xls.setPileMaxelectr("0");
                e.printStackTrace();
            }
            try {
                if (!StringUtils.isEmpty(one.getPileSpeed())) {
                    xls.setPileSpeed(String.format("%.2f", Double.valueOf(one.getPileSpeed())));
                } else {
                    xls.setPileSpeed("0");
                }
            } catch (NumberFormatException e) {
                xls.setPileSpeed("0");
                e.printStackTrace();
            }
            try {
                xls.setPileNo(one.getPileNo());
                if (!StringUtils.isEmpty(one.getPileUobeton())) {
                    xls.setPileUobeton(String.format("%.2f", Double.valueOf(one.getPileUobeton())));
                } else {
                    xls.setPileUobeton("0");
                }
            } catch (NumberFormatException e) {
                xls.setPileUobeton("0");
                e.printStackTrace();
            }
            try {
                if (!StringUtils.isEmpty(one.getPileX())) {
                    xls.setPileX(String.format("%.2f", Double.valueOf(one.getPileX())));
                } else {
                    xls.setPileX("0");
                }
            } catch (NumberFormatException e) {
                xls.setPileX("0");
                e.printStackTrace();
            }
            try {
                if (!StringUtils.isEmpty(one.getPileUelectr())) {
                    xls.setPileUelectr(String.format("%.2f", Double.valueOf(one.getPileUelectr())));
                } else {
                    xls.setPileUelectr("0");
                }
            } catch (NumberFormatException e) {
                xls.setPileUelectr("0");
                e.printStackTrace();
            }
            try {
                if (!StringUtils.isEmpty(one.getPileUspeed())) {
                    xls.setPileUspeed(String.format("%.2f", Double.valueOf(one.getPileUspeed())));
                } else {
                    xls.setPileUspeed("0");
                }
            } catch (NumberFormatException e) {
                xls.setPileUspeed("0");
                e.printStackTrace();
            }
            try {
                if (!StringUtils.isEmpty(one.getPileDelectr())) {
                    xls.setPileDelectr(String.format("%.2f", Double.valueOf(one.getPileDelectr())));
                } else {
                    xls.setPileDelectr("0");
                }
            } catch (NumberFormatException e) {
                xls.setPileDelectr("0");
                e.printStackTrace();
            }

            try {
                if (!StringUtils.isEmpty(one.getPileUpress())) {
                    xls.setPileUpress(String.format("%.2f", Double.valueOf(one.getPileUpress())));
                } else {
                    xls.setPileUpress("0");
                }
            } catch (NumberFormatException e) {
                xls.setPileUpress("0");
                e.printStackTrace();
            }
            try {
                if (!StringUtils.isEmpty(one.getPileDpress())) {
                    xls.setPileDpress(String.format("%.2f", Double.valueOf(one.getPileDpress())));
                } else {
                    xls.setPileDpress("0");
                }
            } catch (NumberFormatException e) {
                xls.setPileDpress("0");
                e.printStackTrace();
            }
            try {
                if (!StringUtils.isEmpty(one.getPileMaxelec())) {
                    xls.setPileMaxelec(String.format("%.2f", Double.valueOf(one.getPileMaxelec())));
                } else {
                    xls.setPileMaxelec("0");
                }
            } catch (NumberFormatException e) {
                xls.setPileMaxelec("0");
                e.printStackTrace();
            }
            try {
                if (!StringUtils.isEmpty(one.getPileDspeed())) {
                    xls.setPileDspeed(String.format("%.2f", Double.valueOf(one.getPileDspeed())));
                } else {
                    xls.setPileDspeed("0");
                }
            } catch (NumberFormatException e) {
                xls.setPileDspeed("0");
                e.printStackTrace();
            }
            // 水泥浆比重*体积/（1+水灰比）=> 改为每米水泥用量（水泥用量/(实际桩长-0.25m）
            if (!StringUtils.isEmpty(one.getPileCement())) {
                xls.setPileCement(String.format("%.2f", Double.valueOf(one.getPileCement())));
                if (!StringUtils.isEmpty(one.getPileMinelec()) && !"0".equals(one.getPileMinelec())) {
                    if (Double.parseDouble(one.getPileMinelec()) >= 55) {
                        xls.setPermCement(one.getPileMinelec());
                    } else if (!StringUtils.isEmpty(one.getPileRealdep())) {
                        Double pc = Double.parseDouble(xls.getPileCement()) / (Double.parseDouble(one.getPileRealdep()) - 0.25);
                        xls.setPermCement(String.format("%.2f", pc));
                    }
                } else if (!StringUtils.isEmpty(one.getPileRealdep())) {
                    Double pc = Double.parseDouble(xls.getPileCement()) / (Double.parseDouble(one.getPileRealdep()) - 0.25);
                    xls.setPermCement(String.format("%.2f", pc));
                }
            } else {
                xls.setPileCement("0");
            }
            xls.setPileDensity(one.getPileDensity());

            try {
                if (!StringUtils.isEmpty(one.getPileDensity())) {
                    xls.setPileDensity(String.format("%.2f", Double.valueOf(one.getPileDensity())));
                } else {
                    xls.setPileDensity("1.78");
                }
            } catch (NumberFormatException e) {
                xls.setPileDensity("1.78");
                e.printStackTrace();
            }
            try {
                if (!StringUtils.isEmpty(one.getPileDesigndep())) {
                    xls.setPileDesigndep(String.format("%.2f", Double.valueOf(one.getPileDesigndep())));
                } else {
                    xls.setPileDesigndep("10");
                }
            } catch (NumberFormatException e) {
                xls.setPileDesigndep("10");
                e.printStackTrace();
            }
            xls.setPileMileage(one.getPileMileage());
            if (!StringUtils.isEmpty(start) && !StringUtils.isEmpty(end) && !"undefined".equals(start) && !"undefined".equals(end)) {
                xls.setJilustarttime(dateformat(start, "yyyy-MM-dd"));
                xls.setJiluendtime(dateformat(end, "yyyy-MM-dd"));
            } else {
                xls.setJilustarttime(date2Str(sdf1));
                xls.setJiluendtime(date2Str(sdf1));
            }
            //计算总浆量
            try {
                if (one.getPileRatio() == null){
                    one.setPileRatio("50");
                }
                double i = 1 + Double.parseDouble(one.getPileRatio())/100;
                double pileDensity = Double.parseDouble(one.getPileDensity());//水泥浆比重
                double pileCement = Double.parseDouble(xls.getPileCement());//水泥用量
                double zjl = (pileCement * i) / pileDensity;
                xls.setPileFlowtotal(String.format("%.2f",zjl));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            data.add(xls);
        }

        return Result.OKs(data);
    }

    /**
     * 导出水泥搅拌桩成桩记录信息表 excel
     *
     * @param request
     * @param deviceMixpileHistorydataOne
     */
    @GetMapping(value = "/mixpileexportapiXlspileTimeend")
    public Result<?> exportapiXlspileTimeend(HttpServletRequest request, String start, String endtime, String piletype, String shebeino, String mileage, DeviceMixpileHistorydataOne deviceMixpileHistorydataOne) throws ParseException, UnsupportedEncodingException {
        Map map = new HashMap();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        if (mileage != null && mileage.contains("%7E")){
            mileage = URLEncoder.encode(mileage, "UTF-8");
            mileage = mileage.replace("%7E", "~");
        }
        //查询到他的设备编号
        String[] split = shebei.split(",");
        List<String> shebeilist = new ArrayList<>();
        Collections.addAll(shebeilist, split);
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new QueryWrapper<>();
        if (!"undefined".equals(piletype) && !"".equals(piletype) && piletype != null) {
            if ("0".equals(piletype)) {
                queryWrapper.in("piletype", 0, 2);
            } else if ("1".equals(piletype)) {
                queryWrapper.in("piletype", 1, 3);
            }
        }
        if (!"undefined".equals(shebeino) && !"".equals(shebeino) && shebeino != null) {
            queryWrapper.eq("shebeino", shebeino);
        } else {
            queryWrapper.in("shebeino", shebeilist);
        }
        if (!"undefined".equals(mileage) && !"".equals(mileage) && mileage != null) {
            queryWrapper.like("pile_mileage", mileage);
        }


        List data = new ArrayList<>();

        if (!StringUtils.isEmpty(start) && !StringUtils.isEmpty(endtime) && !"undefined".equals(start) && !"undefined".equals(endtime)) {
            queryWrapper.between("pile_time", start, endtime);
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date zero = calendar.getTime();
            queryWrapper.between("pile_time", sdf.format(zero), sdf.format(new Date()));
        }
        queryWrapper.orderByDesc("pile_time");
        List<DeviceMixpileHistorydataOne> pageList = deviceMixpileHistorydataOneService.list(queryWrapper);
        Integer id = 0;
        for (DeviceMixpileHistorydataOne one : pageList) {
            //如果大于一除1000
            double v = Double.parseDouble(one.getPileUobeton());
            if (v > 10){
                one.setPileUobeton(String.valueOf(v/1000));
            }
            DeviceMixpileHistorydataOneXls xls = new DeviceMixpileHistorydataOneXls();
            id = id + 1;
            xls.setId(id);
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(one.getShebeino());
            xls.setShebeino(selectshebeione.getSbname());
            SysDepart queryone = sysDepartService.queryone(selectshebeione.getSysOrgCode());
            xls.setSupervisionUnit(queryone.getSupervisionUnit());
            xls.setConstructionUnit(queryone.getConstructionUnit());
            xls.setDepartNameAbbr(queryone.getDepartNameAbbr());
            xls.setPileWorktime(one.getPileWorktime());
            xls.setPileDowntime(one.getPileDowntime());
            xls.setPileUptime(one.getPileUptime());
            xls.setPileTime(one.getPileTime());
            xls.setDatatime(one.getDatatime());
            xls.setPileDesigndep(one.getPileDesigndep());

            if (!StringUtils.isEmpty(one.getPileDownbeton())) {
                xls.setPileDownbeton(String.format("%.2f", Double.valueOf(one.getPileDownbeton())));
            } else {
                xls.setPileDownbeton("0");
            }
            if (!StringUtils.isEmpty(one.getPileRealdep())) {
                xls.setPileRealdep(String.format("%.2f", Double.valueOf(one.getPileRealdep())));
            } else {
                xls.setPileRealdep("0");
            }
            xls.setPileNo(one.getPileNo());
            if (!StringUtils.isEmpty(one.getPileUobeton())) {
                xls.setPileUobeton(String.format("%.2f", Double.valueOf(one.getPileUobeton())));
            } else {
                xls.setPileUobeton("0");
            }
            if (!StringUtils.isEmpty(one.getPileX())) {
                xls.setPileX(String.format("%.2f", Double.valueOf(one.getPileX())));
            } else {
                xls.setPileX("0");
            }
            if (!StringUtils.isEmpty(one.getPileUelectr())) {
                xls.setPileUelectr(String.format("%.2f", Double.valueOf(one.getPileUelectr())));
            } else {
                xls.setPileUelectr("0");
            }
            if (!StringUtils.isEmpty(one.getPileUspeed())) {
                xls.setPileUspeed(String.format("%.2f", Double.valueOf(one.getPileUspeed())));
            } else {
                xls.setPileUspeed("0");
            }
            if (!StringUtils.isEmpty(one.getPileDelectr())) {
                xls.setPileDelectr(String.format("%.2f", Double.valueOf(one.getPileDelectr())));
            } else {
                xls.setPileDelectr("0");
            }

            if (!StringUtils.isEmpty(one.getPileUpress())) {
                xls.setPileUpress(String.format("%.2f", Double.valueOf(one.getPileUpress())));
            } else {
                xls.setPileUpress("0");
            }
            if (!StringUtils.isEmpty(one.getPileDpress())) {
                xls.setPileDpress(String.format("%.2f", Double.valueOf(one.getPileDpress())));
            } else {
                xls.setPileDpress("0");
            }
            if (!StringUtils.isEmpty(one.getPileMaxelec())) {
                xls.setPileMaxelec(String.format("%.2f", Double.valueOf(one.getPileMaxelec())));
            } else {
                xls.setPileMaxelec("0");
            }
            if (!StringUtils.isEmpty(one.getPileDspeed())) {
                xls.setPileDspeed(String.format("%.2f", Double.valueOf(one.getPileDspeed())));
            } else {
                xls.setPileDspeed("0");
            }
            // 水泥浆比重*体积/（1+水灰比）=> 改为每米水泥用量（水泥用量/(实际桩长-0.25m）
            if (!StringUtils.isEmpty(one.getPileCement())) {
                xls.setPileCement(String.format("%.2f", Double.valueOf(one.getPileCement())));
                if (!StringUtils.isEmpty(one.getPileMinelec()) && !"0".equals(one.getPileMinelec())) {
                    if (Double.parseDouble(one.getPileMinelec()) >= 55) {
                        xls.setPermCement(one.getPileMinelec());
                    } else if (!StringUtils.isEmpty(one.getPileRealdep())) {
                        Double pc = Double.parseDouble(xls.getPileCement()) / (Double.parseDouble(one.getPileRealdep()) - 0.25);
                        xls.setPermCement(String.format("%.2f", pc));
                    }
                } else if (!StringUtils.isEmpty(one.getPileRealdep())) {
                    Double pc = Double.parseDouble(xls.getPileCement()) / (Double.parseDouble(one.getPileRealdep()) - 0.25);
                    xls.setPermCement(String.format("%.2f", pc));
                }
            } else {
                xls.setPileCement("0");
            }
            xls.setPileDensity(one.getPileDensity());

            if (!StringUtils.isEmpty(one.getPileDensity())) {
                xls.setPileDensity(String.format("%.2f", Double.valueOf(one.getPileDensity())));
            } else {
                xls.setPileDensity("1.78");
            }
            if (!StringUtils.isEmpty(one.getPileDesigndep())) {
                xls.setPileDesigndep(String.format("%.2f", Double.valueOf(one.getPileDesigndep())));
            } else {
                xls.setPileDesigndep("10");
            }
            xls.setPileMileage(one.getPileMileage());
            if (!StringUtils.isEmpty(start) && !StringUtils.isEmpty(endtime) && !"undefined".equals(start) && !"undefined".equals(endtime)) {
                xls.setJilustarttime(dateformat(start, "yyyy-MM-dd"));
                xls.setJiluendtime(dateformat(endtime, "yyyy-MM-dd"));
            } else {
                xls.setJilustarttime(date2Str(sdf1));
                xls.setJiluendtime(date2Str(sdf1));
            }
            //计算总浆量
            try {
                double i = 1 + Double.parseDouble(one.getPileRatio())/100;
                double pileDensity = Double.parseDouble(one.getPileDensity());//水泥浆比重
                double pileCement = Double.parseDouble(xls.getPileCement());//水泥用量
                double zjl = (pileCement * i) / pileDensity;
                xls.setPileUobeton(String.format("%.2f",zjl));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            data.add(xls);
        }

        return Result.OKs(data);
    }
    /**
     * 养护大屏
     *
     * @param hntConsignSamplePos
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "混凝土见证取样货架表信息-养护大屏")
    @ApiOperation(value = "混凝土见证取样货架表信息-养护大屏", notes = "混凝土见证取样货架表信息-养护大屏")
    @GetMapping(value = "/yhlist")
    public Result<?> queryPageyhList(HntConsignSamplePos hntConsignSamplePos, String sys_depart_orgcode,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (hntConsignSamplePos.getTemperatureid() == null) {
            if (!"null".equals(shebei)) {
                hntConsignSamplePos.setTemperatureid(shebei);
            } else {
                hntConsignSamplePos.setTemperatureid("空");
            }
        }
        QueryWrapper<HntConsignSamplePos> queryWrapper = QueryGenerator.initQueryWrapper(hntConsignSamplePos, req.getParameterMap());
        List<HntConsignSamplePos> pageList = hntConsignSamplePosService.list(queryWrapper);
        ArrayList<Object> list = new ArrayList<>();
        for (HntConsignSamplePos hntConsignSamplePos1 : pageList) {
            HntConsignSamplePosPage hntConsignSamplePosPage = new HntConsignSamplePosPage();
            hntConsignSamplePosPage.setId(hntConsignSamplePos1.getId());
            hntConsignSamplePosPage.setTemperatureid(hntConsignSamplePos1.getTemperatureid());
            hntConsignSamplePosPage.setHuojialie(hntConsignSamplePos1.getHuojialie());
            hntConsignSamplePosPage.setSysOrgCode(hntConsignSamplePos1.getSysOrgCode());
            QueryWrapper<BysReal> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("shebeiNo", hntConsignSamplePos1.getTemperatureid());
            queryWrapper2.orderByDesc("dataTime");
            queryWrapper2.last("limit 1");
            BysReal one = bysRealService.getOne(queryWrapper2);
            if (one != null) {
                hntConsignSamplePosPage.setTemperature(one.getTemperature());
                hntConsignSamplePosPage.setHumidity(one.getHumidity());
            }
            SysDepart queryone = sysDepartService.queryone(hntConsignSamplePos1.getSysOrgCode());
            if (queryone != null) {
                hntConsignSamplePosPage.setDepartname(queryone.getDepartName());
            }
            hntConsignSamplePosPage.setHuojianame(hntConsignSamplePos1.getHuojianame());
            hntConsignSamplePosPage.setHuojiahang(hntConsignSamplePos1.getHuojiahang());
            hntConsignSamplePosPage.setHuojiacenshu(hntConsignSamplePos1.getHuojiacenshu());
            ArrayList<Object> list2 = new ArrayList<>();
            for (int i = 0; i < Integer.parseInt(hntConsignSamplePos1.getHuojiacenshu()); i++) {
                char huojiaceng = (char) (65 + i);
                QueryWrapper<HntConsignCode> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.select("a.id,a.codeno,a.wtid,a.wtbh,a.cstatus,a.jyhdate,a.jyhr,a.cyhdate,a.cyhr,a.qyr,a.qydate,a.huojianname,a.huojiacenshu,a.code,a.no,b.yplq");
                queryWrapper1.last("a left join hnt_consign b on a.wtid = b.wtid where a.huojianname = '" + hntConsignSamplePos1.getId() + "' and a.huojiacenshu = '" + huojiaceng + "'");
                List<Map<String, Object>> list1 = hntConsignCodeService.listMaps(queryWrapper1);
                list2.add(list1);
            }
            hntConsignSamplePosPage.setListceng(list2);
            list.add(hntConsignSamplePosPage);
        }
        return Result.OK(list);
    }

    //视频播放token获取
    @GetMapping(value = "/tokenlist")
    public Result<?> queryPageList2(Monitor monitor,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        Map<String, String> param = new HashMap<>();
        param.put("apiid", "1");
        param.put("apisceret", "CB093DD1D932456C9D33B2E25CD9CFF5");
        com.alibaba.fastjson.JSONObject sr = HttpRequestUtil.sendPost("http://js.traiot.cn:8081/dataprovider/gettoken", param);
        com.alibaba.fastjson.JSONObject data = com.alibaba.fastjson.JSONObject.parseObject(sr.getString("data"));
        String token = data.getString("token");
        return Result.OK(token);
    }

    //视频播放token获取
    @GetMapping(value = "/tokenid")
    public Result<?> queryID2(Monitor monitor,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        Map<String, String> param = new HashMap<>();
        param.put("apiid", "1");
        param.put("apisceret", "CB093DD1D932456C9D33B2E25CD9CFF5");
        com.alibaba.fastjson.JSONObject sr = HttpRequestUtil.sendPost("http://js.traiot.cn:8081/dataprovider/gettoken", param);
        com.alibaba.fastjson.JSONObject data = com.alibaba.fastjson.JSONObject.parseObject(sr.getString("data"));
        String token = data.getString("token");

        QueryWrapper<Monitor> queryWrapper = QueryGenerator.initQueryWrapper(monitor, req.getParameterMap());
        List<Monitor> list = monitorService.list(queryWrapper);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token",token);
        if(list.size()>0){
            map1.put("devid",list.get(0).getId());
        }else{
            map1.put("devid","无");
        }

        return Result.OK(map1
        );
    }



    @AutoLog(value = "上口审核时电子锁解封")
    @ApiOperation(value = "上口审核时电子锁解封", notes = "上口审核时电子锁解封")
    @GetMapping(value = "/jfElocksUp")
    public Result<?> queryPageList2up(@RequestParam(name = "teid") String teid, Integer id) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息

        ShebeiInfo sbjwd = shebeiInfoService.SBJWD(teid);
        // 根据设备厂家字段区分，不为空则请求厂家新接口
        if(StringUtils.isNotBlank(sbjwd.getMillname())){

            Map<String, String> param = new HashMap<>();
            param.put("password", "dc483e80a7a0bd9ef71d8cf973673924");
            param.put("username", "hzgx");
            param.put("sn", teid);
            param.put("issue_type", "openPut");
            com.alibaba.fastjson.JSONObject sr = HttpRequestUtil.sendPost("http://www.iotlock.vip/mt-api/device/issure/apiOpenPut", param);
            com.alibaba.fastjson.JSONObject code = (com.alibaba.fastjson.JSONObject) sr.get("verify");
            if (code.get("sign").equals("success")) {
                WbshebeiDetail wbshebeiDetail = new WbshebeiDetail();
                wbshebeiDetail.setId(id);
                wbshebeiDetail.setIsjiesuo2(2);
                wbshebeiDetail.setReviewer(loginUser.getUsername());
                wbshebeiDetail.setReviewtime(new Date());
                wbshebeiDetailService.updateById(wbshebeiDetail);
                return Result.OK("解封成功，请审核！");
            } else {
                return Result.error("解封失败！");
            }
        }else{
            String token = jobUtil.getWbshebeiToken();
            if (token != null) {
                Map<String, String> param = new HashMap<>();
                param.put("token", token);
                param.put("te_type", "hhd808");
                param.put("teid", teid);
                com.alibaba.fastjson.JSONObject sr = HttpRequestUtil.sendPost("http://gpselock.vip:80//cmdtransfer/unseal", param);
                Integer code = (Integer) sr.get("code");
                if (code == 0) {
                    WbshebeiDetail wbshebeiDetail = new WbshebeiDetail();
                    wbshebeiDetail.setId(id);
                    wbshebeiDetail.setIsjiesuo2(2);
                    wbshebeiDetail.setReviewer(loginUser.getUsername());
                    wbshebeiDetail.setReviewtime(new Date());
                    wbshebeiDetailService.updateById(wbshebeiDetail);
                    return Result.OK("解封成功，请审核！");
                } else {
                    return Result.error("解封失败！");
                }
            } else {
                return Result.error("获取token失败！");
            }
        }

    }


    @AutoLog(value = "电子锁解封")
    @ApiOperation(value = "电子锁解封", notes = "电子锁解封")
    @GetMapping(value = "/jfElocks")
    public Result<?> queryPageList2(@RequestParam(name = "teid") String teid, Integer id) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息

        ShebeiInfo sbjwd = shebeiInfoService.SBJWD(teid);
        // 根据设备厂家字段区分，不为空则请求厂家新接口
        if(StringUtils.isNotBlank(sbjwd.getMillname())){

            Map<String, String> param = new HashMap<>();
            param.put("password", "dc483e80a7a0bd9ef71d8cf973673924");
            param.put("username", "hzgx");
            param.put("sn", teid);
            param.put("issue_type", "openPut");
            com.alibaba.fastjson.JSONObject sr = HttpRequestUtil.sendPost("http://www.iotlock.vip/mt-api/device/issure/apiOpenPut", param);
            com.alibaba.fastjson.JSONObject code = (com.alibaba.fastjson.JSONObject) sr.get("verify");
            if (code.get("sign").equals("success")) {
                WbshebeiDetail wbshebeiDetail = new WbshebeiDetail();
                wbshebeiDetail.setId(id);
                wbshebeiDetail.setIsjiesuo(2);
                wbshebeiDetail.setReviewer(loginUser.getUsername());
                wbshebeiDetail.setReviewtime(new Date());
                wbshebeiDetailService.updateById(wbshebeiDetail);
                return Result.OK("解封成功，请审核！");
            } else {
                return Result.error("解封失败！");
            }
        }else{
            String token = jobUtil.getWbshebeiToken();
            if (token != null) {
                Map<String, String> param = new HashMap<>();
                param.put("token", token);
                param.put("te_type", "hhd808");
                param.put("teid", teid);
                com.alibaba.fastjson.JSONObject sr = HttpRequestUtil.sendPost("http://gpselock.vip:80//cmdtransfer/unseal", param);
                Integer code = (Integer) sr.get("code");
                if (code == 0) {
                    WbshebeiDetail wbshebeiDetail = new WbshebeiDetail();
                    wbshebeiDetail.setId(id);
                    wbshebeiDetail.setIsjiesuo(2);
                    wbshebeiDetail.setReviewer(loginUser.getUsername());
                    wbshebeiDetail.setReviewtime(new Date());
                    wbshebeiDetailService.updateById(wbshebeiDetail);
                    return Result.OK("解封成功，请审核！");
                } else {
                    return Result.error("解封失败！");
                }
            } else {
                return Result.error("获取token失败！");
            }
        }
    }

    /**
     * 分页列表查询
     *
     * @param trGangjinbhcM
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "钢保数据检测主表-分页列表查询")
    @ApiOperation(value = "钢保数据检测主表-分页列表查询", notes = "钢保数据检测主表-分页列表查询")
    @GetMapping(value = "/gblists")
    public Result<?> queryPagegbLists(TrGangjinbhcM trGangjinbhcM,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        SysDepart queryone = sysDepartService.queryone(loginUser.getOrgCode());
        List<List<Map<String, Object>>> list = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        if (queryone != null) {
            List<SysDepart> sysDepartlist = sysDepartService.getsysDepartlist(queryone.getId());
            if (sysDepartlist.size() == 0) {
                sysDepartlist.add(queryone);
            }
            if (sysDepartlist.size() > 0) {
                for (SysDepart sysDepart : sysDepartlist) {
                    QueryWrapper<TrGangjinbhcM> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.select("DISTINCT(targettype) as targettype");
                    queryWrapper1.eq("type", 1);
                    List<TrGangjinbhcM> list2 = trGangjinbhcMService.list(queryWrapper1);
                    if (list2.size() > 0) {
                        List<Map<String, Object>> list3 = new ArrayList<>();
                        for (TrGangjinbhcM trGangjinbhcM1 : list2) {
                            Map<String, Object> map = new HashMap<>();
                            QueryWrapper<TrGangjinbhcM> queryWrapper = new QueryWrapper<>();
                            queryWrapper.select("ifnull(sum((passRate*zoneCount)/100),0) as passRate,ifnull(sum(zoneCount),0) as zoneCount,'" + sysDepart.getOrgCode() + "' as checklocation,'" + sysDepart.getDepartName() + "' as testerid,'" + trGangjinbhcM1.getTargettype() + "' as targettype");
                            queryWrapper.last("a join shebei_info b on a.shebei_no = b.sbjno where b.sys_org_code like '" + sysDepart.getOrgCode() + "%' and a.targetType = '" + trGangjinbhcM1.getTargettype() + "'");
                            TrGangjinbhcM list1 = trGangjinbhcMService.getOne(queryWrapper);
                            map.put("data", list1);
                            map.put("name", trGangjinbhcM1.getTargettype());
                            list3.add(map);
                        }
                        list.add(list3);
                    }
                }
            }
            map1.put("data", list);
            map1.put("orgCode", sysDepartlist);
        }
        return Result.OK(map1);
    }


    /**
     * 分页列表查询
     *
     * @param trGangjinbhcM
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "钢保数据检测主表-分页列表查询")
    @ApiOperation(value = "钢保数据检测主表-分页列表查询", notes = "钢保数据检测主表-分页列表查询")
    @GetMapping(value = "/gblist")
    public Result<?> queryPagegbList(TrGangjinbhcM trGangjinbhcM,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        SysDepart queryone = sysDepartService.queryone(loginUser.getOrgCode());
        List<List<Map<String, Object>>> list = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        if (queryone != null) {
            QueryWrapper<TrGangjinbhcM> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.select("DISTINCT(targettype) as targettype");
            queryWrapper1.eq("type", 1);
            List<TrGangjinbhcM> list2 = trGangjinbhcMService.list(queryWrapper1);
            List<Map<String, Object>> list4 = new ArrayList<>();
            if (list2.size() > 0) {
                for (TrGangjinbhcM trGangjinbhcM1 : list2) {
                    Map<String, Object> map = new HashMap<>();
                    List<SysDepart> sysDepartlist = sysDepartService.getsysDepartlist(queryone.getId());
                    if (sysDepartlist.size() == 0) {
                        sysDepartlist.add(queryone);
                    }
                    if (sysDepartlist.size() > 0) {
                        List list3 = new ArrayList();
                        for (SysDepart sysDepart : sysDepartlist) {
                            List<ShebeiInfo> lists = shebeiInfoService.shebeilist(42, sysDepart.getOrgCode());
                            List<String> shebeis = new ArrayList<>();
                            if (lists.size() > 0) {
                                for (ShebeiInfo shebeiInfo : lists) {
                                    shebeis.add(shebeiInfo.getSbjno());
                                }
                            }
                            QueryWrapper<TrGangjinbhcM> queryWrapper = new QueryWrapper<>();
                            queryWrapper.select("ifnull(sum((passRate*zoneCount)/100),0) as passRate,ifnull(sum(zoneCount),0) as zoneCount,'" + sysDepart.getOrgCode() + "' as checklocation," +
                                    "'" + sysDepart.getDepartName() + "' as testerid,'" + trGangjinbhcM1.getTargettype() + "' as targettype");
                            if (shebeis.size() > 0) {
                                queryWrapper.in("shebei_no", shebeis);
                            } else {
                                queryWrapper.eq("shebei_no", "空");
                            }
                            queryWrapper.last(" and targetType = '" + trGangjinbhcM1.getTargettype() + "'");
                            TrGangjinbhcM list1 = trGangjinbhcMService.getOne(queryWrapper);
                            list3.add(list1);
                        }
                        list.add(list3);
                        map.put("data", list3);
                    }
                    map.put("name", trGangjinbhcM1.getTargettype());
                    map1.put("orgCode", sysDepartlist);
                    list4.add(map);
                }
            }
            map1.put("data", list4);
        }
        return Result.OK(map1);
    }

    /**
     * 按部门统计水泥搅拌桩超差率
     *
     * @param deviceMixpileHistorydataOne
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "按部门统计水泥搅拌桩超差率")
    @ApiOperation(value = "按部门统计水泥搅拌桩超差率", notes = "按部门统计水泥搅拌桩超差率")
    @GetMapping(value = "/mixStalist")
    public Result<?> queryPageixStaList(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne,
                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        SysDepart queryone = sysDepartService.queryone(loginUser.getOrgCode());
        List list = new ArrayList();
        List<SysDepart> sysDepartlist = sysDepartService.getsysDepartlist(queryone.getId());
        if (sysDepartlist.size() == 0) {
            sysDepartlist.add(queryone);
        }
        if (sysDepartlist.size() > 0) {
            for (SysDepart sysDepart : sysDepartlist) {
                int[] stype = {16, 19, 53, 54};
                List<Integer> stypelist = new ArrayList<>();
                for (int j : stype) {
                    stypelist.add(j);
                }
                List<ShebeiInfo> list1 = shebeiInfoService.usershebeiList(sysDepart.getOrgCode(), stypelist);
                List<String> shebeis = new ArrayList<>();
                if (list1.size() > 0) {
                    for (ShebeiInfo shebeiInfo : list1) {
                        shebeis.add(shebeiInfo.getSbjno());
                    }
                }
                QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new QueryWrapper<>();
                queryWrapper.select("ifnull(sum(case when chaobiaodengji=1 then 1 end),0) as buhege,ifnull(count(*),0) as zong," +
                        "'" + sysDepart.getOrgCode() + "' as orgCode,'" + sysDepart.getDepartName() + "' as departName");
                if (shebeis.size() > 0) {
                    queryWrapper.in("shebeino", shebeis);
                } else {
                    queryWrapper.eq("shebeino", "空");
                }
                Map<String, Object> map = deviceMixpileHistorydataOneService.getMap(queryWrapper);
                Map<String, Object> map1 = new HashMap<>();
                if (map == null) {
                    map1.put("buhege", 0);
                    map1.put("zong", 0);
                    map1.put("orgCode", sysDepart.getOrgCode());
                    map1.put("departName", sysDepart.getDepartName());
                    list.add(map1);
                } else {
                    list.add(map);
                }
            }
        }
        return Result.OK(list);
    }

    /**
     * 按部门统计拌合站数据
     *
     * @param bhzCementStatistics
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "按部门统计拌合站数据")
    @ApiOperation(value = "按部门统计拌合站数据", notes = "按部门统计拌合站数据")
    @GetMapping(value = "/bhzcountSta")
    public Result<?> queryPagebhzcountSta(BhzCementStatistics bhzCementStatistics,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          HttpServletRequest req, String orgCategory, Integer date) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        List list = new ArrayList();
        List<SysDepart> sysDepartlist = sysDepartService.getsysDepartlists(loginUser.getOrgCode(), orgCategory);
        if (sysDepartlist.size() > 0) {
            for (SysDepart sysDepart : sysDepartlist) {
                QueryWrapper<BhzCementStatistics> queryWrapper = new QueryWrapper<>();
                List<ShebeiInfo> list1 = shebeiInfoService.shebeilist(0, sysDepart.getOrgCode());
                List<String> shebeis = new ArrayList<>();
                if (list1.size() > 0) {
                    for (ShebeiInfo shebeiInfo : list1) {
                        shebeis.add(shebeiInfo.getSbjno());
                    }
                }
                queryWrapper.select("sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(all_dish) as all_dish,sum(primary_dish) as primary_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish");
                if (shebeis.size() > 0) {
                    queryWrapper.in("shebei_no", shebeis);
                } else {
                    queryWrapper.eq("shebei_no", "空");
                }
                if (date == null) {

                } else if (date == 0) {
                    queryWrapper.last(" and YEAR(statistics_time)=YEAR(NOW())");
                } else if (date == 1) {
                    queryWrapper.last(" and date_format(statistics_time,'%Y-%m') = date_format(now(),'%Y-%m')");
                } else if (date == 2) {
                    queryWrapper.last(" and to_days(statistics_time) = to_days(now())");
                } else if (date == 3) {
                    queryWrapper.last(" and QUARTER(statistics_time)=QUARTER(now()) AND YEAR (statistics_time) = YEAR (NOW())");
                } else if (date == 4) {
                    queryWrapper.last(" and YEARWEEK(date_format(statistics_time,'%Y-%m-%d')) = YEARWEEK(now())");
                }
                BhzCementStatistics bhzCementStatistics1 = bhzCementStatisticsService.getOne(queryWrapper);
                QueryWrapper<BhzCementBase> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.select("SUM(CASE WHEN overproof_status = 20 THEN 1 ELSE 0 END) as cement_variety ");
                if (shebeis.size() > 0) {
                    queryWrapper1.in("shebei_no", shebeis);
                } else {
                    queryWrapper1.eq("shebei_no", "空");
                }
                if (date == null) {
                } else if (date == 0) {
                    queryWrapper1.last(" and YEAR(product_datetime)=YEAR(NOW())");
                } else if (date == 1) {
                    queryWrapper1.last(" and date_format(product_datetime,'%Y-%m') = date_format(now(),'%Y-%m')");
                } else if (date == 2) {
                    queryWrapper1.last(" and to_days(product_datetime) = to_days(now())");
                } else if (date == 3) {
                    queryWrapper1.last(" and QUARTER(product_datetime)=QUARTER(now()) AND YEAR(product_datetime) = YEAR(NOW())");
                } else if (date == 4) {
                    queryWrapper1.last(" and YEARWEEK(date_format(product_datetime,'%Y-%m-%d')) = YEARWEEK(now())");
                }
                BhzCementBase bhzCementBase = bhzCementBaseService.getOne(queryWrapper1);
                Map<String, Object> map = new HashMap<>();
                double handlelv = 0.0;
                double chaobiao1lv = 0;
                double chaobiao2lv = 0;
                double chaobiao3lv = 0;
                double bhzcount = 0.0;
                double chaobiao1 = 0.0;
                double chaobiao2 = 0.0;
                double chaobiao3 = 0.0;
                double handle = 0.0;
                double zong = 0.0;
                double chaobiao = 0.0;
                if (bhzCementStatistics1 != null) {
                    bhzcount = bhzCementStatistics1.getAllDish();
                    chaobiao1 = bhzCementStatistics1.getPrimaryDish();
                    chaobiao2 = bhzCementStatistics1.getMiddleDish();
                    chaobiao3 = bhzCementStatistics1.getAdvancedDish();
                    zong = bhzCementStatistics1.getAllDish();
                    chaobiao = bhzCementStatistics1.getAllOverproofDish();
                }
                if (bhzCementBase != null) {
                    handle = Double.parseDouble(bhzCementBase.getCementVariety());
                }
                if (zong > 0) {
                    chaobiao1lv = (chaobiao1 / zong) * 100;
                    chaobiao2lv = (chaobiao2 / zong) * 100;
                    chaobiao3lv = (chaobiao3 / zong) * 100;
                } else {
                    chaobiao1lv = 100;
                    chaobiao2lv = 100;
                    chaobiao3lv = 100;
                }
                if (chaobiao > 0) {
                    handlelv = handle / chaobiao * 100;
                }
                map.put("depart_name", sysDepart.getDepartName());
                map.put("bhzcount", bhzcount);
                map.put("chaobiao1", chaobiao1);
                map.put("chaobiao2", chaobiao2);
                map.put("chaobiao3", chaobiao3);
                map.put("handle", handle);
                map.put("chaobiao1lv", String.format("%.2f", chaobiao1lv));
                map.put("chaobiao2lv", String.format("%.2f", chaobiao2lv));
                map.put("chaobiao3lv", String.format("%.2f", chaobiao3lv));
                map.put("handlelv", String.format("%.2f", handlelv));
                list.add(map);
            }
        }
        return Result.OK(list);
    }

    /**
     * 分页列表查询
     *
     * @param trGangjinbhcM
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "钢保数据检测主表-分页列表查询")
    @ApiOperation(value = "钢保数据检测主表-分页列表查询", notes = "钢保数据检测主表-分页列表查询")
    @GetMapping(value = "/gblistData")
    public Result<?> queryPagegbListData(TrGangjinbhcM trGangjinbhcM,
                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                         HttpServletRequest req, String orgCategory) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        List<List<Map<String, Object>>> list = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        QueryWrapper<TrGangjinbhcM> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT(targettype) as targettype");
        queryWrapper.eq("type", 1);
        List<TrGangjinbhcM> list2 = trGangjinbhcMService.list(queryWrapper);
        List<Map<String, Object>> list4 = new ArrayList<>();
        if (list2.size() > 0) {
            for (TrGangjinbhcM trGangjinbhcM1 : list2) {
                Map<String, Object> map = new HashMap<>();
                List<SysDepart> sysDepartlist = sysDepartService.getsysDepartlists(loginUser.getOrgCode(), orgCategory);
                if (sysDepartlist.size() > 0) {
                    List list3 = new ArrayList();
                    for (SysDepart sysDepart : sysDepartlist) {
                        List<ShebeiInfo> lists = shebeiInfoService.shebeilist(42, sysDepart.getOrgCode());
                        List<String> shebeis = new ArrayList<>();
                        if (lists.size() > 0) {
                            for (ShebeiInfo shebeiInfo : lists) {
                                shebeis.add(shebeiInfo.getSbjno());
                            }
                        }
                        QueryWrapper<TrGangjinbhcM> queryWrapper1 = new QueryWrapper<>();
                        queryWrapper1.select("ifnull(sum((passRate*zoneCount)/100),0) as passRate,ifnull(sum(zoneCount),0) as zoneCount,'" + trGangjinbhcM1.getTargettype() + "' as targettype");
                        if (shebeis.size() > 0) {
                            queryWrapper1.in("shebei_no", shebeis);
                        } else {
                            queryWrapper1.eq("shebei_no", "空");
                        }
                        queryWrapper1.last(" and targetType = '" + trGangjinbhcM1.getTargettype() + "'");
                        TrGangjinbhcM list1 = trGangjinbhcMService.getOne(queryWrapper1);
                        list3.add(list1);
                    }
                    list.add(list3);
                    map.put("data", list3);
                }
                map.put("name", trGangjinbhcM1.getTargettype());
                map1.put("orgCode", sysDepartlist);
                list4.add(map);
            }
        }
        map1.put("data", list4);
        return Result.OK(map1);
    }

    @GetMapping(value = "/piciStalist")//根据组织机构统计进场、合格、退场批次
    public Result<?> queryPagepiciStaList(Wztaizhang wztaizhang,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          HttpServletRequest req, String orgCategory) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        List<SysDepart> sysDepartlist = new ArrayList<>();
        if (!StringUtil.isEmpty(orgCategory)) {
            sysDepartlist = sysDepartService.getsysDepartlists(loginUser.getOrgCode(), orgCategory);
        } else {
            SysDepart queryone = sysDepartService.queryone(loginUser.getOrgCode());
            sysDepartlist = sysDepartService.getsysDepartlist(queryone.getId());
            if (sysDepartlist.size() == 0) {
                sysDepartlist.add(queryone);
            }
        }
        List list = new ArrayList();
        if (sysDepartlist.size() > 0) {
            for (SysDepart sysDepart : sysDepartlist) {
                Map map = new HashMap();
                List<ShebeiInfo> lists = shebeiInfoService.shebeilist(27, sysDepart.getOrgCode());
                List<String> shebeis = new ArrayList<>();
                if (lists.size() > 0) {
                    for (ShebeiInfo shebeiInfo : lists) {
                        shebeis.add(shebeiInfo.getSbjno());
                    }
                }
                QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();
                queryWrapper.select("count(pici) as pici");
                if (shebeis.size() > 0) {
                    queryWrapper.in("shebeibianhao", shebeis);
                } else {
                    queryWrapper.eq("shebeibianhao", "空");
                }
                double hegesum = 0.0;
                double zongsum = 0.0;
                double tuichangsum = 0.0;
                double tuichanglv = 0.0;
                Wztaizhang one = wztaizhangService.getOne(queryWrapper);
                if (one != null) {
                    if (!StringUtil.isEmpty(one.getPici())) {
                        zongsum = Double.parseDouble(one.getPici());
                    }
                }
                QueryWrapper<Wztaizhang> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.select("count(pici) as pici");
                if (shebeis.size() > 0) {
                    queryWrapper1.in("shebeibianhao", shebeis);
                } else {
                    queryWrapper1.eq("shebeibianhao", "空");
                }
                queryWrapper1.eq("jianyanstate", 1);
                Wztaizhang one1 = wztaizhangService.getOne(queryWrapper1);
                if (one1 != null) {
                    if (!StringUtil.isEmpty(one1.getPici())) {
                        hegesum = Double.parseDouble(one1.getPici());
                    }
                }
                QueryWrapper<Wztaizhang> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.select("count(pici) as pici");
                if (shebeis.size() > 0) {
                    queryWrapper2.in("shebeibianhao", shebeis);
                } else {
                    queryWrapper2.eq("shebeibianhao", "空");
                }
                queryWrapper1.eq("jianyanstate", 2);
                Wztaizhang one2 = wztaizhangService.getOne(queryWrapper1);
                if (one2 != null) {
                    if (!StringUtil.isEmpty(one2.getPici())) {
                        tuichangsum = Double.parseDouble(one2.getPici());
                    }
                }
                if (zongsum != 0) {
                    tuichanglv = tuichangsum / zongsum * 100;
                }
                map.put("orgCode", sysDepart.getOrgCode());
                map.put("departName", sysDepart.getDepartName());
                map.put("hegesum", hegesum);
                map.put("zongsum", zongsum);
                map.put("tuichangsum", tuichangsum);
                map.put("tuichanglv", tuichanglv);
                list.add(map);
            }
        }
        return Result.OK(list);
    }

    /**
     * 统计超标数(根据部门)
     *
     * @param devicehistoryStatic
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "环境监测数据统计表-统计超标数")
    @ApiOperation(value = "环境监测数据统计表-统计超标数", notes = "环境监测数据统计表-统计超标数")
    @GetMapping(value = "/devicelist")
    public Result<?> queryPagedeviceLis(DevicehistoryStatic devicehistoryStatic,
                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        HttpServletRequest req, String orgCategory) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        List<SysDepart> sysDepartlist = new ArrayList<>();
        if (!StringUtil.isEmpty(orgCategory)) {
            sysDepartlist = sysDepartService.getsysDepartlists(loginUser.getOrgCode(), orgCategory);
        } else {
            SysDepart queryone = sysDepartService.queryone(loginUser.getOrgCode());
            sysDepartlist = sysDepartService.getsysDepartlist(queryone.getId());
            if (sysDepartlist.size() == 0) {
                sysDepartlist.add(queryone);
            }
        }
        List list = new ArrayList();
        if (sysDepartlist.size() > 0) {
            for (SysDepart sysDepart : sysDepartlist) {
                Map map = new HashMap();
                List<ShebeiInfo> lists = shebeiInfoService.shebeilist(15, sysDepart.getOrgCode());
                List<String> shebeis = new ArrayList<>();
                if (lists.size() > 0) {
                    for (ShebeiInfo shebeiInfo : lists) {
                        shebeis.add(shebeiInfo.getSbjno());
                    }
                }
                int chaototal = 0;
                QueryWrapper<DevicehistoryStatic> queryWrapper = new QueryWrapper<>();
                queryWrapper.select("sum(chaototal) chaototal");
                if (shebeis.size() > 0) {
                    queryWrapper.in("DevAddr", shebeis);
                } else {
                    queryWrapper.in("DevAddr", "空");
                }
                DevicehistoryStatic pageList = devicehistoryStaticService.getOne(queryWrapper);
                if (pageList != null) {
                    if (pageList.getChaototal() != null) {
                        chaototal = pageList.getChaototal();
                    }
                }
                map.put("chaototal", chaototal);
                map.put("departName", sysDepart.getDepartName());
                list.add(map);
            }
        }

        return Result.OK(list);
    }

    /**
     * 分页列表查询
     *
     * @param entityProgresscheck
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "实体进度清单数据表-分页列表查询")
    @ApiOperation(value = "实体进度清单数据表-分页列表查询", notes = "实体进度清单数据表-分页列表查询")
    @GetMapping(value = "/entitylist")
    public Result<?> queryPageList(EntityProgresscheck entityProgresscheck,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            entityProgresscheck.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            entityProgresscheck.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String format1 = format.format(new Date());
        String dictCode = "projectType";
        SysDict selectdict = sysDictService.selectdict(dictCode);
        List<Map<String, Object>> list1 = new ArrayList<>();
        if (selectdict != null) {
            List<SysDictItem> selectcailiaoname = sysDictItemService.selectcailiaoname(selectdict.getId());
            if (selectcailiaoname.size() > 0) {
                for (SysDictItem sysDictItem : selectcailiaoname) {
                    Map<String, Object> map = new HashMap<>();
                    double designQuantity = 0.0;
                    double finishedAmount = 0.0;
                    double remainingAmount = 0.0;
                    double schedule = 0.0;
                    double planAmount = 0;
                    double plannedAmount = 0;
                    QueryWrapper<EntityProgresscheck> queryWrapper = QueryGenerator.initQueryWrapper(entityProgresscheck, req.getParameterMap());
                    queryWrapper.select("sum(design_quantity) design_quantity,sum(finished_amount) finished_amount,sum(remaining_amount) remaining_amount");
                    queryWrapper.eq("project_type", sysDictItem.getItemValue());
                    EntityProgresscheck one = entityProgresscheckService.getOne(queryWrapper);
                    if (one != null) {
                        if (one.getDesignQuantity() != null) {
                            designQuantity = Double.parseDouble(one.getDesignQuantity());
                        }
                        if (one.getFinishedAmount() != null) {
                            finishedAmount = Double.parseDouble(one.getFinishedAmount());
                        }
                        if (one.getRemainingAmount() != null) {
                            remainingAmount = Double.parseDouble(one.getRemainingAmount());
                        }
                    }
                    if (designQuantity != 0) {
                        schedule = finishedAmount / designQuantity * 100;
                    }
                    QueryWrapper<EntityProgresscheck> queryWrapper1 = QueryGenerator.initQueryWrapper(entityProgresscheck, req.getParameterMap());
                    queryWrapper1.eq("project_type", sysDictItem.getItemValue());
                    List<EntityProgresscheck> progresscheckList = entityProgresscheckService.list(queryWrapper1);
                    List<String> list = new ArrayList<>();
                    if (progresscheckList.size() > 0) {
                        for (EntityProgresscheck entityProgresscheck1 : progresscheckList) {
                            list.add(entityProgresscheck1.getUuid());
                        }
                    }
                    QueryWrapper<EntityCheckDetial> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.select("sum(plan_amount) plan_amount,sum(planned_amount) planned_amount");
                    if (list.size() > 0) {
                        queryWrapper2.in("uuid", list);
                    } else {
                        queryWrapper2.in("uuid", "空");
                    }
                    queryWrapper2.last(" and time like '" + format1 + "%' and QUARTER(time) = QUARTER(now())");
                    EntityCheckDetial one1 = entityCheckDetialService.getOne(queryWrapper2);
                    if (one1 != null) {
                        if (one1.getPlanAmount() != null) {
                            planAmount = Double.parseDouble(one1.getPlanAmount());
                        }
                        if (one1.getPlannedAmount() != null) {
                            plannedAmount = Double.parseDouble(one1.getPlannedAmount());
                        }
                    }
                    map.put("designQuantity", String.format("%.0f", designQuantity));
                    map.put("finishedAmount", String.format("%.0f", finishedAmount));
                    map.put("remainingAmount", String.format("%.0f", remainingAmount));
                    map.put("schedule", String.format("%.1f", schedule));
                    map.put("planAmount", String.format("%.0f", planAmount));
                    map.put("plannedAmount", String.format("%.0f", plannedAmount));
                    map.put("projectType", sysDictItem.getItemText());
                    list1.add(map);
                }
            }
        }
        return Result.OK(list1);
    }

    /**
     * 桩基统计
     *
     * @param chaoshengboSyjbsj
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "chaoshengbo_syjbsj-桩基统计")
    @ApiOperation(value = "chaoshengbo_syjbsj-桩基统计", notes = "chaoshengbo_syjbsj-桩基统计")
    @GetMapping(value = "/chaoshenglist")
    public Result<?> queryPagechaoshengList(ChaoshengboSyjbsj chaoshengboSyjbsj,
                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                            HttpServletRequest req, String orgCategory) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        List list = new ArrayList();
        Map map1 = new HashMap();
        String[] listStr = {"一类桩", "二类桩", "三类桩"};
        List<Map<String, Object>> list4 = new ArrayList<>();
        for (Object o : listStr) {
            Map<String, Object> map = new HashMap<>();
            List<SysDepart> sysDepartlist = sysDepartService.getsysDepartlists(loginUser.getOrgCode(), orgCategory);
            if (sysDepartlist.size() > 0) {
                List list3 = new ArrayList();
                for (SysDepart sysDepart : sysDepartlist) {
                    QueryWrapper<ChaoshengboSyjbsj> queryWrapper = new QueryWrapper<>();
                    List<ShebeiInfo> list1 = shebeiInfoService.shebeilist(14, sysDepart.getOrgCode());
                    List<String> shebeis = new ArrayList<>();
                    if (list1.size() > 0) {
                        for (ShebeiInfo shebeiInfo : list1) {
                            shebeis.add(shebeiInfo.getSbjno());
                        }
                    }
                    queryWrapper.select("count(1) baoxuhao");
                    if (shebeis.size() > 0) {
                        queryWrapper.in("shebeino", shebeis);
                    } else {
                        queryWrapper.eq("shebeino", "空");
                    }
                    if (o.equals("一类桩")) {
                        queryWrapper.le("shizhuangleixing", 21);
                    } else {
                        queryWrapper.eq("shizhuangleixing", 0);
                    }
                    ChaoshengboSyjbsj one = chaoshengboSyjbsjService.getOne(queryWrapper);
                    int num = 0;
                    if (one != null) {
                        if (one.getBaoxuhao() != null) {
                            num = one.getBaoxuhao();
                        }
                    }
                    list3.add(num);
                }
                list.add(list3);
                map.put("data", list3);
            }
            map.put("name", o);
            map1.put("orgCode", sysDepartlist);
            list4.add(map);
        }
        map1.put("data", list4);
        return Result.OK(map1);
    }

    /**
     * 分页列表查询
     *
     * @param bhzCementStatistics
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "bhz_lq_statistics-分页列表查询")
    @ApiOperation(value = "bhz_lq_statistics-分页列表查询", notes = "bhz_lq_statistics-分页列表查询")
    @GetMapping(value = "/hntstalist")
    public Result<?> queryPagehntstaList(BhzCementStatistics bhzCementStatistics,
                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                         HttpServletRequest req, String orgCategory) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        List<SysDepart> sysDepartlist = new ArrayList<>();
        if (!StringUtil.isEmpty(orgCategory)) {
            sysDepartlist = sysDepartService.getsysDepartlists(loginUser.getOrgCode(), orgCategory);
        } else {
            SysDepart queryone = sysDepartService.queryone(loginUser.getOrgCode());
            sysDepartlist = sysDepartService.getsysDepartlist(queryone.getId());
            if (sysDepartlist.size() == 0) {
                sysDepartlist.add(queryone);
            }
        }
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        String format2 = format1.format(new Date());
        List list = new ArrayList();
        if (sysDepartlist.size() > 0) {
            for (SysDepart sysDepart : sysDepartlist) {
                Map map = new HashMap();
                List<ShebeiInfo> lists = shebeiInfoService.shebeilist(0, sysDepart.getOrgCode());
                List<String> shebeis = new ArrayList<>();
                if (lists.size() > 0) {
                    for (ShebeiInfo shebeiInfo : lists) {
                        shebeis.add(shebeiInfo.getSbjno());
                    }
                }
                QueryWrapper<BhzCementStatistics> queryWrapper = new QueryWrapper<>();
                queryWrapper.select("sum(all_overproof_dish) all_overproof_dish", "date_format(statistics_time,'%Y-%m') as dates");
                if (shebeis.size() > 0) {
                    queryWrapper.in("shebei_no", shebeis);
                } else {
                    queryWrapper.in("shebei_no", "空");
                }
                queryWrapper.likeRight("statistics_time", format2);
                queryWrapper.groupBy("MONTH(statistics_time)");
                List<Map<String, Object>> pageList = bhzCementStatisticsService.listMaps(queryWrapper);
                map.put("chaototaldata", pageList);
                map.put("departName", sysDepart.getDepartName());
                list.add(map);
            }
        }
        return Result.OK(list);
    }

    @AutoLog(value = "梁场台座管理表信息-继电器开关")
    @ApiOperation(value = "梁场台座管理表信息-继电器开关", notes = "梁场台座管理表信息-继电器开关")
    @PutMapping(value = "/switchEdit")
    public Result<?> queryPageList(@RequestBody CunliangProcedureConfig cunliangProcedureConfig) {
        String token = jobUtil.getDeviceToken();
        QueryWrapper<CunliangProcedureConfig> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("uuid", cunliangProcedureConfig.getUuid());
        List<CunliangProcedureConfig> list = cunliangProcedureConfigService.list(queryWrapper1);
        int codes = jobUtil.getcode(token, cunliangProcedureConfig.getUuid(), cunliangProcedureConfig.getSwitchsta());
        if (cunliangProcedureConfig.getSwitchsta() == 0) {
            if (list.size() > 0) {
                jobUtil.getupdate(list, 1);
            }
            if (codes == 0) {
                return Result.OK("开灯成功!");
            }else {
                return Result.error("开灯失败!");
            }
        } else {
            if (list.size() > 0) {
                jobUtil.getupdate(list, 0);
            }
            if (codes == 0) {
                return Result.OK("关灯成功!");
            }else {
                return Result.error("关灯失败!");
            }
        }
    }



    @PutMapping(value = "/pushSyDataO")
    public Result<?> pushSyDataO(@RequestBody Wztaizhang pushDate) {

        // 存放
        String cunfnag = pushDate.getStoragePlace();
        // 质保单
        String zhibaodan =pushDate.getZhibaodan();
        // 生产批号
        String shengchanpihao = org.apache.commons.lang3.StringUtils.isNotBlank( pushDate.getShengchanpihao()) ?  pushDate.getShengchanpihao() : "/";


        Wzcailiaonamedict queryselectone1 = wzcailiaonamedictService.queryselectone1(pushDate.getCailiaono());
        if (queryselectone1 == null) {
//            wzycljinchanggbService.updateistongji(pushDate.getId(), 20);
            return Result.error("推送失败,没有找到相关材料信息");
        }

        Wzliaocang queryselectone = wzliaocangService.queryselectone(pushDate.getLcbianhao());
        String lcbh = "";
        if (queryselectone == null) {
//            wzycljinchanggbService.updateistongji(pushDate.getId(), 10);
            return Result.error("推送失败,没有找到相关料仓信息");
        }else{
          //  cunfnag = cunfnag+queryselectone.getName();
            lcbh = queryselectone.getName();
        }

        Wzgongyingshang queryselectone2 = wzgongyingshangService.selectnameone(pushDate.getGongyingshangdanweibianma());
        if (queryselectone2 == null) {
//            wzycljinchanggbService.updateistongji(pushDate.getId(), 15);
            return Result.error("推送失败,没有找到对应供应商信息");
        }
        if (StringUtils.isEmpty(pushDate.getJingzhongt())) {
//            wzycljinchanggbService.updateistongji(pushDate.getId(), 25);
            return Result.error("推送失败,无净重信息");
        }


        ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(pushDate.getShebeibianhao());
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.set("departId", shebeiInfo.getSysOrgCode());
        jsonObject1.set("cailiaoName", queryselectone1.getCailiaoname());
        jsonObject1.set("guigexinghao", queryselectone1.getGuigexinghao());
        jsonObject1.set("SupplierUnit", queryselectone2.getGongyingshangname());
        jsonObject1.set("jinchangshijian", pushDate.getJinchangshijian());
        jsonObject1.set("cunfangdidian", cunfnag);
        jsonObject1.set("lcbh", lcbh);
        jsonObject1.set("url", zhibaodan);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        jsonObject1.set("chuchangriqi", sdf.format(pushDate.getChuchangshijian()));

        Format fm = new DecimalFormat("000");
        String ss= String.valueOf(pushDate.getId());//fm.format(Integer.valueOf(pushDate.getId()>1000?pushDate.getId()%1000:pushDate.getId()));
        jsonObject1.set("jingzhong", pushDate.getJingzhongt() + 't');
        jsonObject1.set("pici", pushDate.getPici());
        jsonObject1.set("shengchanpihao", shengchanpihao);

        String back = HttpUtils.httpPost("http://121.40.163.88:8084/CATDPS/WzReceivingController.do?InspectionLotData",
                String.valueOf(jsonObject1));

        JSONObject jsonObject2 = new JSONObject(back);
        String success = jsonObject2.get("success").toString();
        if ("0".equals(success)) {
            pushDate.setJystatus(1);
            boolean b = wztaizhangService.saveOrUpdate(pushDate);
            //  wztaizhangService.update(new UpdateWrapper<Wztaizhang>().lambda().set(Wztaizhang::getJystatus, 1).eq(Wztaizhang::getId, jypWztz.getId()));//推送成功，修改jystatus值为1
            return Result.OK("推送成功",jsonObject1);
        } else {
            pushDate.setJystatus(2);
            boolean b = wztaizhangService.saveOrUpdate(pushDate);
            // wztaizhangService.update(new UpdateWrapper<Wztaizhang>().lambda().set(Wztaizhang::getJystatus, 2).eq(Wztaizhang::getId, jypWztz.getId()));//推送成功，修改jystatus值为2
            return Result.error("推送失败",jsonObject1);
        }
    }


    @PutMapping(value = "/pushSyData")
    public Result<?> pushSyData(@RequestBody WzycljinchanggbPush pushDate) throws ParseException {

        String cunfnag = StringUtils.isNotBlank(pushDate.getLiaocangid()) ? pushDate.getLiaocangid() : "";
        String zhibaodan = StringUtils.isNotBlank(pushDate.getSonghuodanpic())?("http://web.traiot.cn/docs/wz/" + pushDate.getSonghuodanpic()):"" ;// 质保单
       // 生产批号
        String shengchanpihao = org.apache.commons.lang3.StringUtils.isNotBlank( pushDate.getYuancaimiaoshu()) ?  pushDate.getYuancaimiaoshu() : "/";
        Wztaizhang wztaizhang = new Wztaizhang();
        wztaizhang.setShengchanpihao(shengchanpihao);
        Wzcailiaonamedict queryselectone1 = wzcailiaonamedictService.queryselectone1(pushDate.getCailiaono());
        if (queryselectone1 == null) {
            wzycljinchanggbService.updateistongji(pushDate.getId(), 20);
            return Result.error("推送失败,没有找到相关材料信息");
        }

        Wzliaocang queryselectone = wzliaocangService.queryselectone(pushDate.getLcbianhao());
        String lcbh = "";
        if (queryselectone == null) {
            log.info("暂无相匹配的料仓配置数据" + DateUtils.now());
            wzycljinchanggbService.updateistongji(pushDate.getId(), 10);
            return Result.error("推送失败,没有找到相关料仓信息");
        }else{
          //  cunfnag = cunfnag+queryselectone.getName();
            lcbh = queryselectone.getName();
        }

        Wzgongyingshang queryselectone2 = wzgongyingshangService.selectnameone(pushDate.getGongyingshangdanweibianma());
        if (queryselectone2 == null) {
            log.info("暂无相匹配的供应商单位数据" + DateUtils.now());
            wzycljinchanggbService.updateistongji(pushDate.getId(), 15);
            return Result.error("推送失败,没有找到对应供应商信息");
        }
        if (StringUtils.isEmpty(pushDate.getJingzhongt())) {
            wzycljinchanggbService.updateistongji(pushDate.getId(), 25);
            return Result.error("推送失败,无净重信息");
        }


        ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(pushDate.getShebeibianhao());
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.set("departId", shebeiInfo.getSysOrgCode());
        jsonObject1.set("cailiaoName", queryselectone1.getCailiaoname());
        jsonObject1.set("guigexinghao", queryselectone1.getGuigexinghao());
        jsonObject1.set("SupplierUnit", queryselectone2.getGongyingshangname());
        jsonObject1.set("jinchangshijian", pushDate.getJinchangshijian());
        jsonObject1.set("cunfangdidian", cunfnag);
        jsonObject1.set("lcbh", lcbh);
        jsonObject1.set("url", zhibaodan);
        jsonObject1.set("chuchangriqi", pushDate.getChuchangshijian());
        jsonObject1.set("shengchanpihao", shengchanpihao);

        if(StringUtils.isNotBlank(pushDate.getSerialno())){ // 地磅补录
            Format fm = new DecimalFormat("000");
            String ss= String.valueOf(pushDate.getId());//fm.format(Integer.valueOf(pushDate.getId()>1000?pushDate.getId()%1000:pushDate.getId()));
            jsonObject1.set("jingzhong", pushDate.getJingzhongt() + 't');
            jsonObject1.set("pici", ( StringUtils.isNotBlank(pushDate.getPici())?pushDate.getPici():"/")+ "-" + ss);

            wztaizhang.setGblx(0);
            wztaizhang.setJingzhongt(pushDate.getJingzhongt());
        }else{// 仓储补录
            jsonObject1.set("sampleGcbw", pushDate.getHouchepai());
            jsonObject1.set("jingzhong", pushDate.getJingzhong() + pushDate.getGuobangleibie());
            jsonObject1.set("pici", pushDate.getPici());
            wztaizhang.setGblx(1);
            wztaizhang.setJingzhongt(pushDate.getJingzhong());
        }


        wztaizhang.setShebeibianhao(pushDate.getShebeibianhao());
        wztaizhang.setJinchangshijian(pushDate.getJinchangshijian());
        wztaizhang.setStoragePlace(cunfnag);
        wztaizhang.setNodetype(queryselectone1.getNodetype());
        wztaizhang.setCailiaono(pushDate.getCailiaono());
        wztaizhang.setDanwei(org.apache.commons.lang3.StringUtils.isNotBlank(pushDate.getGuobangleibie())?pushDate.getGuobangleibie():"t");
        wztaizhang.setZhibaodan(zhibaodan);
        wztaizhang.setGongyingshangdanweibianma(pushDate.getGongyingshangdanweibianma());
        wztaizhang.setGuigexinghao(queryselectone1.getGuigexinghao());
        wztaizhang.setLcbianhao(pushDate.getLcbianhao());
        wztaizhang.setPici(jsonObject1.getStr("pici"));
        wztaizhang.setIsfinish(1);
        wztaizhang.setSysOrgCode(shebeiInfo.getSysOrgCode());
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        wztaizhang.setChuchangshijian(format1.parse(pushDate.getChuchangshijian()));
        Wzycljinchanggb wzycljinchanggb = new Wzycljinchanggb();
        BeanUtils.copyProperties(pushDate,wzycljinchanggb);
        wzycljinchanggb.setBeizhu(null);

        if(pushDate.getTaizhangid() != null){
            Wztaizhang byId = wztaizhangService.getById(pushDate.getTaizhangid());
            wztaizhang = byId;
            jsonObject1.set("pici",byId.getPici());
            jsonObject1.set("jingzhong",byId.getJingzhongt()+"t" );

        }else{
            // 料仓和台账的中间表
            tzlcService.saveOrUpdateLC(wztaizhang.getPici(), pushDate.getLcbianhao(),Double.valueOf(pushDate.getJingzhong()) ,pushDate.getJinchangshijian(),pushDate.getCailiaono(),pushDate.getGongyingshangdanweibianma(),pushDate.getShebeibianhao(),shebeiInfo.getSysOrgCode());
        }

        String back = HttpUtils.httpPost("http://121.40.163.88:8084/CATDPS/WzReceivingController.do?InspectionLotData",
                String.valueOf(jsonObject1));

        JSONObject jsonObject2 = new JSONObject(back);
        String success = jsonObject2.get("success").toString();
        if ("0".equals(success)) {
            wztaizhang.setJystatus(1);
            boolean b = wztaizhangService.saveOrUpdate(wztaizhang);
            //  wztaizhangService.update(new UpdateWrapper<Wztaizhang>().lambda().set(Wztaizhang::getJystatus, 1).eq(Wztaizhang::getId, jypWztz.getId()));//推送成功，修改jystatus值为1
            wzycljinchanggb.setTaizhangid(wztaizhang.getId());
            wzycljinchanggb.setTaizhangtj(1);
            wzycljinchanggbService.updateById(wzycljinchanggb);
            return Result.OK("推送成功",jsonObject1);
        } else {
            wztaizhang.setJystatus(2);
            boolean b = wztaizhangService.saveOrUpdate(wztaizhang);
            wzycljinchanggb.setTaizhangid(wztaizhang.getId());
            wzycljinchanggb.setTaizhangtj(40);
            wzycljinchanggbService.updateById(wzycljinchanggb);
            // wztaizhangService.update(new UpdateWrapper<Wztaizhang>().lambda().set(Wztaizhang::getJystatus, 2).eq(Wztaizhang::getId, jypWztz.getId()));//推送成功，修改jystatus值为2
            return Result.error("推送失败",jsonObject1);
        }
    }

    @PutMapping(value = "/pushSyDataMan")
    public Result<?> pushSyDataMan(@RequestBody WzycljinchanggbPush pushDate) throws ParseException {

        String cunfnag = StringUtils.isNotBlank(pushDate.getLiaocangid()) ? pushDate.getLiaocangid() : "";
        String zhibaodan = StringUtils.isNotBlank(pushDate.getSonghuodanpic())?("http://web.traiot.cn/docs/wz/" + pushDate.getSonghuodanpic()):"" ;// 质保单
        // 生产批号
        String shengchanpihao = org.apache.commons.lang3.StringUtils.isNotBlank( pushDate.getYuancaimiaoshu()) ?  pushDate.getYuancaimiaoshu() : "/";
        Wztaizhang wztaizhang = new Wztaizhang();
        wztaizhang.setShengchanpihao(shengchanpihao);
        WzcailiaonamedictMan queryselectone1 = wzcailiaonamedictManService.queryselectone1(pushDate.getCailiaono());
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        if (queryselectone1 == null) {
            wzycljinchanggbManService.updateistongji(pushDate.getId(), 20);
            return Result.error("推送失败,未匹配材料信息");
        }

        Wzliaocang queryselectone = wzliaocangService.queryselectone(pushDate.getLcbianhao());
        String lcbh = "";
        if (queryselectone == null) {
            log.info("暂无相匹配的料仓配置数据" + DateUtils.now());
            wzycljinchanggbManService.updateistongji(pushDate.getId(), 10);
            return Result.error("推送失败,未匹配料仓信息");
        }else{
         //   cunfnag = cunfnag+queryselectone.getName();
            lcbh = queryselectone.getName();
        }

        WzgongyingshangMan queryselectone2 = wzgongyingshangManService.selectnameone1(pushDate.getGongyingshangdanweibianma());
        if (queryselectone2 == null) {
            log.info("暂无相匹配的供应商单位数据" + DateUtils.now());
            wzycljinchanggbManService.updateistongji(pushDate.getId(), 15);
            return Result.error("推送失败,未匹配供应商信息");
        }
        if (StringUtils.isEmpty(pushDate.getJingzhongt())) {
            wzycljinchanggbManService.updateistongji(pushDate.getId(), 25);
            return Result.error("推送失败,无净重信息");
        }

        ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(pushDate.getShebeibianhao());
        if(pushDate.getTaizhangid() != null){
            wztaizhang.setId(pushDate.getTaizhangid());
            Wztaizhang byId = wztaizhangService.getById(pushDate.getTaizhangid());
            if(byId != null){
                wztaizhang.setJingzhongt(byId.getJingzhongt());
            }else{
                wztaizhang.setJingzhongt(pushDate.getJingzhong());
            }
        }else{
            QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("pici",pushDate.getPici());
            queryWrapper.orderByDesc("id");
            List<Wztaizhang> wz = wztaizhangService.list(queryWrapper);
            // 未进行检验批累加时进行累加
            if(wz.size()>0){
//            wzycljinchanggbManService.updateistongji(pushDate.getId(), 35);
                wztaizhang.setId(wz.get(0).getId());
                wztaizhang.setJingzhongt(  String.format("%.2f",  Double.parseDouble(wz.get(0).getJingzhongt()) + Double.parseDouble(pushDate.getJingzhong())) );
//            return Result.error("推送失败,批次号重复");
            }else{
                wztaizhang.setJingzhongt(pushDate.getJingzhong());
            }
            // 料仓和台账的中间表
            tzlcService.saveOrUpdateLC(pushDate.getPici(), pushDate.getLcbianhao(),Double.valueOf(pushDate.getJingzhong()) ,pushDate.getJinchangshijian(),pushDate.getCailiaono(),pushDate.getGongyingshangdanweibianma(),pushDate.getShebeibianhao(),shebeiInfo.getSysOrgCode());

        }



        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.set("departId", shebeiInfo.getSysOrgCode());
        jsonObject1.set("cailiaoName", queryselectone1.getCailiaoname());
        jsonObject1.set("guigexinghao", queryselectone1.getGuigexinghao());
        jsonObject1.set("SupplierUnit", queryselectone2.getGongyingshangname());
        jsonObject1.set("jinchangshijian", pushDate.getJinchangshijian());
        jsonObject1.set("cunfangdidian", cunfnag);
        jsonObject1.set("lcbh", lcbh);
        jsonObject1.set("url", zhibaodan);
        jsonObject1.set("chuchangriqi", pushDate.getChuchangshijian());

        jsonObject1.set("shengchanpihao", shengchanpihao);

        // 仓储补录
        jsonObject1.set("sampleGcbw", pushDate.getHouchepai());
        jsonObject1.set("jingzhong", wztaizhang.getJingzhongt() + pushDate.getGuobangleibie());
        jsonObject1.set("pici", pushDate.getPici());
        wztaizhang.setGblx(1);

        wztaizhang.setShebeibianhao(pushDate.getShebeibianhao());
        wztaizhang.setJinchangshijian(pushDate.getJinchangshijian());
        wztaizhang.setStoragePlace(cunfnag);
        wztaizhang.setNodetype(queryselectone1.getNodetype());
        wztaizhang.setCailiaono(pushDate.getCailiaono());
        wztaizhang.setDanwei(StringUtils.isNotBlank(pushDate.getGuobangleibie())?pushDate.getGuobangleibie():"t");
        wztaizhang.setZhibaodan(zhibaodan);
        wztaizhang.setGongyingshangdanweibianma(pushDate.getGongyingshangdanweibianma());
        wztaizhang.setGuigexinghao(queryselectone1.getGuigexinghao());
        wztaizhang.setLcbianhao(pushDate.getLcbianhao());
        wztaizhang.setPici(jsonObject1.getStr("pici"));
        wztaizhang.setChuchangshijian(format1.parse(pushDate.getChuchangshijian()));
        wztaizhang.setIsfinish(1);
        wztaizhang.setSysOrgCode(shebeiInfo.getSysOrgCode());
        WzycljinchanggbMan wzycljinchanggb = new WzycljinchanggbMan();
        BeanUtils.copyProperties(pushDate,wzycljinchanggb);



        String back = HttpUtils.httpPost("http://121.40.163.88:8084/CATDPS/WzReceivingController.do?InspectionLotData",
                String.valueOf(jsonObject1));

        JSONObject jsonObject2 = new JSONObject(back);
        String success = jsonObject2.get("success").toString();
        if ("0".equals(success)) {
            wztaizhang.setJystatus(1);
            boolean b = wztaizhangService.saveOrUpdate(wztaizhang);
            //  wztaizhangService.update(new UpdateWrapper<Wztaizhang>().lambda().set(Wztaizhang::getJystatus, 1).eq(Wztaizhang::getId, jypWztz.getId()));//推送成功，修改jystatus值为1
            wzycljinchanggb.setTaizhangid(wztaizhang.getId());
            wzycljinchanggb.setTaizhangtj(1);
            wzycljinchanggbManService.updateById(wzycljinchanggb);
            return Result.OK("推送成功",jsonObject1);
        } else {
            wztaizhang.setJystatus(2);
            boolean b = wztaizhangService.saveOrUpdate(wztaizhang);
            wzycljinchanggb.setTaizhangid(wztaizhang.getId());
            wzycljinchanggb.setTaizhangtj(40);
            wzycljinchanggbManService.updateById(wzycljinchanggb);
            // wztaizhangService.update(new UpdateWrapper<Wztaizhang>().lambda().set(Wztaizhang::getJystatus, 2).eq(Wztaizhang::getId, jypWztz.getId()));//推送成功，修改jystatus值为2
            return Result.error("推送失败",jsonObject1);
        }
    }

}
