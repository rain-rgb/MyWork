package org.jeecg.modules.systems;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.internal.LinkedTreeMap;
import com.trtm.iot.car.entity.CarMiles;
import com.trtm.iot.carmiles.entity.Carmiles;
import com.trtm.iot.carmiles.service.ICarmilesService;
import com.trtm.iot.clgl.entity.ClxxRealdata;
import com.trtm.iot.clgl.service.IClxxRealdataService;
import com.trtm.iot.devicecranerealdata.entity.DeviceCraneRealdata;
import com.trtm.iot.devicecranerealdata.service.IDeviceCraneRealdataService;
import com.trtm.iot.devicemixgrouted.entity.DeviceMixpileGroutedOne;
import com.trtm.iot.devicemixgrouted.entity.DeviceMixpileGroutedPart;
import com.trtm.iot.devicemixgrouted.service.IDeviceMixpileGroutedOneService;
import com.trtm.iot.devicemixgrouted.service.IDeviceMixpileGroutedPartService;
import com.trtm.iot.devicepiplewall.entity.DevicePiplewallOne;
import com.trtm.iot.devicepiplewall.service.IDevicePiplewallOneService;
import com.trtm.iot.lmd.entity.DeviceCraneTb;
import com.trtm.iot.lmd.service.IDeviceCraneTbService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.tokenrecode.entity.Tokenrecode;
import com.trtm.iot.tokenrecode.service.ITokenrecodeService;
import com.trtm.iot.wzcailiaonamedictall.entity.WzcailiaonamedictAll;
import com.trtm.iot.wzcailiaonamedictall.service.IWzcailiaonamedictAllService;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.entity.WztaizhangLc;
import com.trtm.iot.wztaizhang.service.IWztaizhangLcService;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import com.znlh.kgl.openapi.client.GenericClient;
import com.znlh.kgl.openapi.client.req.message.AuthTokenRequest;
import com.znlh.kgl.openapi.constant.Options;
import com.znlh.kgl.openapi.model.generic.OAuth2Token;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.job.jobutil.HttpRequestUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.jobutil.WeiLanUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.service.ISysPermissionService;
import org.jeecg.modules.systems.service.APICodeResp;
import org.jeecg.modules.systems.service.SaasClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.trtm.iot.util.GPSUtil.gps84_To_Gcj02;

/**
 * @Description: 消息
 * @author: jeecg-boot
 * @date: 2019-04-09
 * @version: V1.0
 */
@Slf4j
@RestController
@RequestMapping("/sys/systems/ssoredirect")
public class Ssoredirect extends JeecgController<SysMessage, ISysMessageService> {

    @Autowired
    private IClxxRealdataService clxxRealdataService;
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private WeiLanUtil weiLanUtil;

    @Autowired
    private ITokenrecodeService tokenrecodeService;

    @Autowired
    private ISysPermissionService permissionService;
    @Autowired
    private IWztaizhangLcService wztaizhangLcService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IWztaizhangService wztaizhangService;
    @Autowired
    private IWzcailiaonamedictAllService wzcailiaonamedictAllService;
    @Autowired
    private IWzliaocangService wzliaocangService;

    @Autowired
    private ICarmilesService carmilesService;
    public static final String REST_HOST = "https://open.znlhzl.cn";
    public static final String APP_ID = "KGL202407170001";
    public static final String SECRET_KEY = "c509d1e174f1dfd751a20717b04851c0f0fea4cfb5c26ad4038759b37378e125";

    @Autowired
    private IDeviceMixpileGroutedOneService deviceMixpileGroutedOneService;
    @Autowired
    private IDeviceMixpileGroutedPartService deviceMixpileGroutedPartService;

    @Autowired
    private IDevicePiplewallOneService devicePiplewallOneService;
    @Autowired
    private IDeviceCraneRealdataService deviceCraneRealdataService;

    @Autowired
    private IDeviceCraneTbService deviceCraneTbService;

    @AutoLog(value = "device_crane_tb-分页列表查询")
    @ApiOperation(value="device_crane_tb-分页列表查询", notes="device_crane_tb-分页列表查询")
    @GetMapping(value = "/getTbMap")
    public Result<?> getTbMap(DeviceCraneTb deviceCraneTb,
                              @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                              @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                              HttpServletRequest req) {
        QueryWrapper<DeviceCraneTb> queryWrapper = QueryGenerator.initQueryWrapper(deviceCraneTb, req.getParameterMap());
        Page<DeviceCraneTb> page = new Page<DeviceCraneTb>(pageNo, pageSize);
        IPage<DeviceCraneTb> pageList = deviceCraneTbService.page(page, queryWrapper);
//		 Map<String, DeviceCraneTb> deviceMap = pageList.getRecords().stream()
//				 .collect(Collectors.toMap(
//						 DeviceCraneTb::getTb,  // 作为 key
//						 device -> device       // 作为 value
//				 ));
        Map<String, String> deviceMap = pageList.getRecords().stream()
                .collect(Collectors.toMap(
                        DeviceCraneTb::getTb,      // 作为 key
                        DeviceCraneTb::getName       // 作为 value
                ));

        return Result.OK(deviceMap);
    }

    @AutoLog(value = "device_piplewall_one-分页列表查询")
    @ApiOperation(value="device_piplewall_one-分页列表查询", notes="device_piplewall_one-分页列表查询")
    @GetMapping(value = "/wallOpen")
    public Result<?> wallOpen(DevicePiplewallOne devicePiplewallOne,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
//        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
//        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//        //查询到他的设备编号
//        if (devicePiplewallOne.getShebeino() == null) {
//            if (!"null".equals(shebei)) {
//                devicePiplewallOne.setShebeino(shebei);
//            } else {
//                devicePiplewallOne.setShebeino("空");
//            }
//        }
        QueryWrapper<DevicePiplewallOne> queryWrapper = QueryGenerator.initQueryWrapper(devicePiplewallOne, req.getParameterMap());
        Page<DevicePiplewallOne> page = new Page<DevicePiplewallOne>(pageNo, pageSize);
        IPage<DevicePiplewallOne> pageList = devicePiplewallOneService.page(page, queryWrapper);
        return Result.OK(pageList);
    }
    @AutoLog(value = "灌注桩详情-分页列表查询")
    @ApiOperation(value="灌注桩详情-分页列表查询", notes="灌注桩详情-分页列表查询")
    @GetMapping(value = "/listopen")
    public Result<?> queryPageListopen(DeviceMixpileGroutedOne deviceMixpileGroutedOne,
                                       @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                       @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                       HttpServletRequest req) {

        QueryWrapper<DeviceMixpileGroutedOne> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileGroutedOne, req.getParameterMap());
        Page<DeviceMixpileGroutedOne> page = new Page<DeviceMixpileGroutedOne>(pageNo, pageSize);
        IPage<DeviceMixpileGroutedOne> pageList = deviceMixpileGroutedOneService.page(page, queryWrapper);

        return Result.OK(pageList);
    }
    @AutoLog(value = "灌注桩子表-分页列表查询")
    @ApiOperation(value="灌注桩子表-分页列表查询", notes="灌注桩子表-分页列表查询")
    @GetMapping(value = "/listpart")
    public Result<?> queryPageListpart(DeviceMixpileGroutedPart deviceMixpileGroutedPart,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<DeviceMixpileGroutedPart> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileGroutedPart, req.getParameterMap());
        Page<DeviceMixpileGroutedPart> page = new Page<DeviceMixpileGroutedPart>(pageNo, pageSize);
        IPage<DeviceMixpileGroutedPart> pageList = deviceMixpileGroutedPartService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param wztaizhang
     * @return
     */
    @AutoLog(value = "wztaizhang-添加")
    @ApiOperation(value = "wztaizhang-添加", notes = "wztaizhang-添加")
    @PostMapping(value = "/addPiciOpen")
    public Result<?> addPiciOpen(@RequestBody Wztaizhang wztaizhang) {
        // 查询批次是否存在，存在则更新数据
        Wztaizhang byPici = wztaizhangService.getByPici(wztaizhang.getPici());
        if(ObjectUtil.isNotNull(byPici)){
            return Result.OK("批次已存在！");
        }
        // 根据设备编号获取组织机构code
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(wztaizhang.getShebeibianhao());
        if(ObjectUtil.isNull(selectshebeione)){
            return Result.OK(wztaizhang.getShebeibianhao()+"设备未注册请联系管理员进行注册！");
        }
        // 根据材编号获取材料类型
        WzcailiaonamedictAll wzcailiaonamedictAll = wzcailiaonamedictAllService.getcailiaoInfo(wztaizhang.getCailiaono());
        if(ObjectUtil.isNull(wzcailiaonamedictAll)){
            return Result.OK(wztaizhang.getCailiaono()+"材料编号不存在，请联系管理员登记");
        }


        wztaizhang.setNodetype(wzcailiaonamedictAll.getNodetype());
        wztaizhang.setGuigexinghao(wzcailiaonamedictAll.getGuigexinghao());
        if(StringUtils.isNotBlank(wztaizhang.getLcbianhao())){
            Wzliaocang queryselectone = wzliaocangService.queryselectone(wztaizhang.getLcbianhao());
            if(ObjectUtil.isNull(queryselectone)){
                return Result.OK(wztaizhang.getLcbianhao()+"料仓编号不存在，请联系管理员登记");
            }
            // 进料时同步数据到wztaizhang_lc
            WztaizhangLc wztaizhangLc = new WztaizhangLc();
            wztaizhangLc.setLiaocangno(wztaizhang.getLcbianhao());
            wztaizhangLc.setShebeino(wztaizhang.getShebeibianhao());
            wztaizhangLc.setPici(wztaizhang.getPici());
            wztaizhangLc.setCailiaono(wztaizhang.getCailiaono());
            wztaizhangLc.setGongyingshang(wztaizhang.getGongyingshangdanweibianma());
            wztaizhangLc.setIncount( wztaizhang.getJingzhongt());
            wztaizhangLc.setSysOrgCode(selectshebeione.getSysOrgCode());
            wztaizhangLcService.save(wztaizhangLc);

        }
        wztaizhang.setSysOrgCode(selectshebeione.getSysOrgCode());
        wztaizhangService.save(wztaizhang);
        return Result.OK("添加成功！");
    }

    @AutoLog(value = "wztaizhang-不合格处置状态")
    @ApiOperation(value = "wztaizhang-不合格处置状态", notes = "wztaizhang-添加")
    @PostMapping(value = "/handlePiciOpen")
    public Result<?> handlePiciOpen(@RequestBody Wztaizhang wztaizhang) {
        // 查询批次是否存在，存在则更新数据
        Wztaizhang byPici = wztaizhangService.getByPici(wztaizhang.getPici());
        if(ObjectUtil.isNull(byPici)){
            return Result.OK("批次不存在！");
        }
        wztaizhang.setId(byPici.getId());
        wztaizhangService.updateById(wztaizhang);
        return Result.OK("处置成功！");
    }

    @RequestMapping("/loginSs/shuzishiyan")
    public void shuzishiyan(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        // 通用跳转集团标准版物联网
        resp.sendRedirect("http://121.40.163.88:8084/CATDPS_Html/webpage/login.html");
    }
    @RequestMapping("/loginSs/zhongdiangongyi")
    public void zhongdiangongyi(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        // 通用跳转集团标准版物联网
        resp.sendRedirect("http://47.96.161.157/user/login");
    }

    @RequestMapping("/loginSs/sbgl204")
    public void sbgl204(HttpServletRequest request, HttpServletResponse resp) throws IOException {

        String code ="";
        SaasClient saasClient = SaasClient.create(getOptions());
        APICodeResp apiCodeResp = new APICodeResp();
        Map jsonObject = saasClient.execute(
                "saas.authority.login.sso",apiCodeResp,
                HashMap.class);
        code = (String) ((LinkedTreeMap) jsonObject.get("data")).get("code");

        // 通用跳转集团标准版物联网
        resp.sendRedirect("https://skydome.znlhzl.cn/singleSignOn?auth_code="+code+"&redirect_url=%2FcustomerSaas%2Fcustomer%2Fdashboard");
    }
    public static Options getOptions() {
        AuthTokenRequest tokenReq = AuthTokenRequest.builder()
                .clientId(APP_ID)
                .clientSecret(SECRET_KEY)
                .build();

        OAuth2Token token = GenericClient.create(REST_HOST).getOAuth2Token(tokenReq);

        // 2. 使用第一次获取的token，调用业务接口，也就是发送短信接口
        Options options = Options.builder()
                .restHost(REST_HOST)
                .appId(APP_ID)
                .secretKey(SECRET_KEY)
                .token("Bearer " + token.getAccessToken())
                .build();
        return options;
    }
    @RequestMapping("/loginSs/{pathid}")
    public void loginSs(HttpServletRequest request, HttpServletResponse resp, String username,@PathVariable(name="pathid") String pathid, String content) throws IOException {

        QueryWrapper<Tokenrecode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Tokenrecode one = tokenrecodeService.getOne(queryWrapper);
        SysPermission byId = permissionService.getById(pathid);

        // 通用跳转集团标准版物联网
        resp.sendRedirect("http://47.96.161.157"+byId.getUrl()+"?token=" + one.getRedisvalue()+"&batchNo="+content);
    }

    @RequestMapping("/loginSz/{pathid}")
    public void loginSz(HttpServletRequest request, HttpServletResponse resp, String username,@PathVariable(name="pathid") String pathid, String content) throws IOException {

        QueryWrapper<Tokenrecode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Tokenrecode one = tokenrecodeService.getOne(queryWrapper);
        SysPermission byId = permissionService.getById(pathid);
        String urlhttp = StringUtils.isNotBlank(one.getHttpurl())?one.getHttpurl():"http://z.traiot.cn";
        String params =StringUtils.isNotBlank(one.getHttpurl())?one.getParams():"";;

        // 通用跳转标准物联网z
        resp.sendRedirect(urlhttp+byId.getUrl()+"?token=" + one.getRedisvalue()+params);
    }


    @RequestMapping("/aimsgviews")
    public void aimsgviews(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {

        QueryWrapper<Tokenrecode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Tokenrecode one = tokenrecodeService.getOne(queryWrapper);
        // 预警
        resp.sendRedirect("https://app.traiot.cn:8447/anquan/aiwarn/aiwarnmsg/AiWarnMsgList?token=" + one.getRedisvalue());
    }
    @RequestMapping("/zhyd")
    public void zhyd(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {

        QueryWrapper<Tokenrecode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Tokenrecode one = tokenrecodeService.getOne(queryWrapper);
        // 智慧用电
        resp.sendRedirect("https://app.traiot.cn:8447/snjbz/devicepower/devicepowerhistorydata/DevicePowerHistorydataList?token=" + one.getRedisvalue());
    }

    @RequestMapping("/wrsl")
    public void wrsl(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {

        QueryWrapper<Tokenrecode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Tokenrecode one = tokenrecodeService.getOne(queryWrapper);
        // 无人收料
        resp.sendRedirect("https://app.traiot.cn:8447/zlgl/yclsl/WzycljinchanggbList?token=" + one.getRedisvalue());
    }

    @RequestMapping("/lmds")
    public void lmds(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {

        QueryWrapper<Tokenrecode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Tokenrecode one = tokenrecodeService.getOne(queryWrapper);
        // 龙门吊
        resp.sendRedirect("https://app.traiot.cn:8447/lmd/DeviceCraneHistorydataList?token=" + one.getRedisvalue());
    }
    @RequestMapping("/lmd")
    public void lmd(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {

        QueryWrapper<Tokenrecode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Tokenrecode one = tokenrecodeService.getOne(queryWrapper);
         // 龙门吊
        resp.sendRedirect("http://z.traiot.cn/lmd/DeviceCraneHistorydataList?token=" + one.getRedisvalue());
    }

    @RequestMapping("/tower")
    public void tower(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {

        QueryWrapper<Tokenrecode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Tokenrecode one = tokenrecodeService.getOne(queryWrapper);

        resp.sendRedirect("http://z.traiot.cn/td/DeviceTowerHistorydataList?token=" + one.getRedisvalue());
    }


    @RequestMapping("/aimsgview")
    public void aimsgview(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {

        QueryWrapper<Tokenrecode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Tokenrecode one = tokenrecodeService.getOne(queryWrapper);

//        HttpSession session = request.getSession();
//        String token = String.valueOf(session.getAttribute("token"));
//        if (token.equals("null")){
//            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
//            token = selectsysconfigone.getToken() ;
//            session.setAttribute("token",token);
//        }
        //       resp.sendRedirect("http://47.96.http://127.0.0.1:8118s/systems/ssoredirect/yjcbcxkb?username=admin161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/dashboard/CMindex?token="+token;

        resp.sendRedirect("http://z.traiot.cn/anquan/aiwarn/aiwarnmsg/AiWarnMsgList?token=" + one.getRedisvalue());
    }


    // 超标查询

    //拌合站
    @RequestMapping("/hntbhzcbcx")
    public void hntbhzcbcx(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {

//        QueryWrapper<Tokenrecode> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("username", username);
//        Tokenrecode one = tokenrecodeService.getOne(queryWrapper);
        String byUsernameToken = getByUsernameToken(username);
        resp.sendRedirect("http://47.96.161.157/bhz/hntbhz/BhzCementBaseCBList?token=" + byUsernameToken);
    }

    // 张拉
    @RequestMapping("/zlcbcxkb")
    public void zlcbcxkb(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {

//        QueryWrapper<Tokenrecode> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("username", username);
//        Tokenrecode one = tokenrecodeService.getOne(queryWrapper);
        String byUsernameToken = getByUsernameToken(username);
        resp.sendRedirect("http://47.96.161.157/zl/TrZhanglachaobiaoList?token=" + byUsernameToken);
    }

    // 压浆
    @RequestMapping("/yjcbcxkb")
    public void yjcbcxkb(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {

//        QueryWrapper<Tokenrecode> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("username", username);
//        Tokenrecode one = tokenrecodeService.getOne(queryWrapper);
        String byUsernameToken = getByUsernameToken(username);
        resp.sendRedirect("http://47.96.161.157/yj/TrYajiangchaobiaoList?token=" + byUsernameToken);
    }

    // 预应力管桩
    @RequestMapping("/yylgzcxkb")
    public void yylgzcxkb(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {

//        QueryWrapper<Tokenrecode> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("username", username);
//        Tokenrecode one = tokenrecodeService.getOne(queryWrapper);
        String byUsernameToken = getByUsernameToken(username);
        resp.sendRedirect("http://47.96.161.157/snjbz/devicepipepilehistorydataone/DevicePipepileHistorydataOneCBCXList?token=" + byUsernameToken);
    }


    // 拌合站

    @RequestMapping("/tbhzkb")
    public void tbhzkb(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {

        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/dashboard/CMindex?token="+token;

        resp.sendRedirect("http://47.96.161.157/dashboard/CMindex?token=" + token);
    }


    @RequestMapping("/tbhz")
    public void tbhz(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/bhz/hntbhz/BhzCementBaseYLList?token="+token;

        resp.sendRedirect("http://47.96.161.157/bhz/hntbhz/BhzCementBaseYLList?token=" + token);
    }

    @RequestMapping("/tbhzcb")
    public void tbhzcb(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/bhz/hntbhz/BhzCementBaseCBList?token="+token;

        resp.sendRedirect("http://47.96.161.157/bhz/hntbhz/BhzCementBaseCBList?token=" + token);
    }

    @RequestMapping("/tbhzcbcl")
    public void tbhzcbcl(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/bhz/hntbhz/BhzHntCbCxtBaseList?token="+token;

        resp.sendRedirect("http://47.96.161.157/bhz/hntbhz/BhzHntCbCxtBaseList?token=" + token);
    }


    // 张拉压浆
    @RequestMapping("/zlcx")
    public void zlcx(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/zlyj/TrZhanglaXxbList?token="+token;

        resp.sendRedirect("http://47.96.161.157/zlyj/TrZhanglaXxbList?token=" + token);
    }

    @RequestMapping("/zlcbcx")
    public void zlcbcx(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/zlyj/TrZhanglachaobiaoList?token="+token;

        resp.sendRedirect("http://47.96.161.157/zlyj/TrZhanglachaobiaoList?token=" + token);
    }

    @RequestMapping("/zlcbcl")
    public void zlcbcl(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/zlyj/TrZhanglachaobiaoCLList?token="+token;

        resp.sendRedirect("http://47.96.161.157/zlyj/TrZhanglachaobiaoCLList?token=" + token);
    }

    @RequestMapping("/yjcx")
    public void yjcx(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/zlyj/TrYajiangMList?token="+token;

        resp.sendRedirect("http://47.96.161.157/zlyj/TrYajiangMList?token=" + token);
    }

    @RequestMapping("/yjcbcx")
    public void yjcbcx(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/zlyj/TrYajiangchaobiaoList?token="+token;

        resp.sendRedirect("http://47.96.161.157/zlyj/TrYajiangchaobiaoList?token=" + token);
    }

    @RequestMapping("/yjcbcl")
    public void yjcbcl(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/zlyj/TrYajiangchaobiaoCLList?token="+token;

        resp.sendRedirect("http://47.96.161.157/zlyj/TrYajiangchaobiaoCLList?token=" + token);
    }


    // 试验检测
    @RequestMapping("/ycjyp")
    public void ycjyp(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/zlgl/wztaizhang/wztaizhangnew/WztaizhangLists?token="+token;

        resp.sendRedirect("http://47.96.161.157/zlgl/wztaizhang/wztaizhangnew/WztaizhangLists?token=" + token);
    }


    // 智慧用电
    @RequestMapping("/zhydcx")
    public void zhydcx(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/anquan/zhyd/historydata/DeviceElectricHistorydataList?token="+token;

        resp.sendRedirect("http://47.96.161.157/anquan/zhyd/historydata/DeviceElectricHistorydataList?token=" + token);
    }

    @RequestMapping("/zhydcb")
    public void zhydcb(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/anquan/zhyd/historydata/DeviceElectricHistorydataListCB?token="+token;

        resp.sendRedirect("http://47.96.161.157/anquan/zhyd/historydata/DeviceElectricHistorydataListCB?token=" + token);
    }

    // 试验机
    @RequestMapping("/wnjcx")
    public void wnjcx(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/syj/wnj/WnjList?token="+token;

        resp.sendRedirect("http://47.96.161.157/syj/wnj/WnjList?token=" + token);
    }


    @RequestMapping("/wnjcb")
    public void wnjcb(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/syj/wnj/WnjCBList?token="+token;

        resp.sendRedirect("http://47.96.161.157/syj/wnj/WnjCBList?token=" + token);
    }

    @RequestMapping("/wnjcbcl")
    public void wnjcbcl(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/syj/wnj/WnjCBCZList?token="+token;

        resp.sendRedirect("http://47.96.161.157/syj/wnj/WnjCBCZList?token=" + token);
    }

    @RequestMapping("/yljcx")
    public void yljcx(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/syj/ylj/SyyLjBasesList?token="+token;

        resp.sendRedirect("http://47.96.161.157/syj/ylj/SyyLjBasesList?token=" + token);
    }

    @RequestMapping("/yljcb")
    public void yljcb(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/syj/ylj/SyyLjBasesCBList?token="+token;

        resp.sendRedirect("http://47.96.161.157/syj/ylj/SyyLjBasesCBList?token=" + token);
    }

    @RequestMapping("/yljcbcl")
    public void yljcbcl(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/syj/ylj/SyyLjBasesCBCZList?token="+token;

        resp.sendRedirect("http://47.96.161.157/syj/ylj/SyyLjBasesCBCZList?token=" + token);
    }

    @RequestMapping("/kykzcx")
    public void kykzcx(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/kykzj/kykz/KyKzJBasesList?token="+token;

        resp.sendRedirect("http://47.96.161.157/kykzj/kykz/KyKzJBasesList?token=" + token);
    }

    @RequestMapping("/kykzcb")
    public void kykzcb(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/kykzj/kykz/KyKzJBasesCBList?token="+token;

        resp.sendRedirect("http://47.96.161.157/kykzj/kykz/KyKzJBasesCBList?token=" + token);
    }

    @RequestMapping("/kykzcbcl")
    public void kykzcbcl(HttpServletRequest request, HttpServletResponse resp, String username) throws IOException {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("token"));
        if (token.equals("null")) {
            SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
            token = selectsysconfigone.getToken();
            session.setAttribute("token", token);
        }
        //       resp.sendRedirect("http://47.96.161.157/bhz/lqbhz/BhzLqBasesList?token="+token+"");
        // return "redirect:http://47.96.161.157/kykzj/kykz/kykzBaseCBCZList?token="+token;

        resp.sendRedirect("http://47.96.161.157/kykzj/kykz/kykzBaseCBCZList?token=" + token);
    }
    @RequestMapping("/gjpwqj")
    public void gjpwqj(HttpServletRequest request, HttpServletResponse resp, String id,String begintime ,String endtime) throws IOException {
        String token = getToken();
        if(StringUtils.isNotBlank(begintime) && StringUtils.isNotBlank(endtime)){
            resp.sendRedirect("http://47.97.173.113:9271/VideoMonitor?id="+id+"&begintime="+begintime+"&endtime="+endtime+"&token="+token);
        }else{
            resp.sendRedirect("http://47.97.173.113:9271/VideoMonitor?id="+id+"&token="+token);
        }

    }

    private String getToken() {
        Map<String, String> param = new HashMap<>();
        param.put("apiid", "1");
        param.put("apisceret", "CB093DD1D932456C9D33B2E25CD9CFF5");
        com.alibaba.fastjson.JSONObject sr = HttpRequestUtil.sendPost("http://js.traiot.cn:8081/dataprovider/gettoken", param);
        com.alibaba.fastjson.JSONObject data = com.alibaba.fastjson.JSONObject.parseObject(sr.getString("data"));
        String token = data.getString("token");
        return token;
    }

    private String getByUsernameToken( String username) {
        JSONObject JsonObj = JSONUtil.createObj();
        JsonObj.put("username", username);
        String url = "http://47.96.161.157/jeecg-boot/sys/loginsso";// 获取请求地址
        String body = HttpRequest.post(url)
                .body(String.valueOf(JsonObj))
                .timeout(20000)
                .execute().body();
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(body);
        cn.hutool.json.JSONObject jsarray = jsonObject.getJSONObject("result");
        String token = String.valueOf(jsarray.get("token"));
        return token;
    }

    /**
     * 分页列表查询
     *
     * @param deviceCraneRealdata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "龙门吊实时数据-分页列表查询")
    @ApiOperation(value="龙门吊实时数据-分页列表查询", notes="龙门吊实时数据-分页列表查询")
    @GetMapping(value = "/listreallmd")
    public Result<?> listreallmd(DeviceCraneRealdata deviceCraneRealdata,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {

        QueryWrapper<DeviceCraneRealdata> queryWrapper = QueryGenerator.initQueryWrapper(deviceCraneRealdata, req.getParameterMap());
        Page<DeviceCraneRealdata> page = new Page<DeviceCraneRealdata>(pageNo, pageSize);
        IPage<DeviceCraneRealdata> pageList = deviceCraneRealdataService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "龙门吊实时数据-分页列表查询")
    @ApiOperation(value="龙门吊实时数据-分页列表查询", notes="龙门吊实时数据-分页列表查询")
    @GetMapping(value = "/checkLocation")
    public Result<?> checkLocation( @RequestParam(name="peolocation", defaultValue="0.0,0.0") String peolocation,
                                    @RequestParam(name="lmdlocation", defaultValue="0.0,0.0") String lmdlocation,
                                 HttpServletRequest req) {
        // double[] doubles = gps84_To_Gcj02(latitude, longitude);
       // 判断经纬度在指定位置范围内
        String[] doubles = peolocation.split(",");
        String[] split = lmdlocation.split(",");
        double latitude1 = Double.valueOf(doubles[1]) ;
        double longitude1 = Double.valueOf(doubles[0]) ;

        Double lng2= Double.valueOf(split[0]);
        Double lat2= Double.valueOf(split[1]);
        Double distance = weiLanUtil.getDistances( latitude1,longitude1,  lat2,lng2);
        if(distance>500){
            return Result.OK().error500("超出控制范围,距离龙门吊"+String.format("%.2f",distance)+"米");
        }else{
            return Result.OK("距离验证通过，距离龙门吊"+String.format("%.2f",distance)+"米");
        }


    }

    @AutoLog(value = "车辆信息实时数据表-添加")
    @ApiOperation(value = "车辆信息实时数据表-添加", notes = "车辆信息实时数据表-添加")
    @PostMapping(value = "/updateRealCarInfo")
    public Result<?> updateRealCarInfo(@RequestBody ClxxRealdata clxxRealdata) {
        if( StringUtils.isNotBlank(clxxRealdata.getShebeiNo())){
            ClxxRealdata queryone = clxxRealdataService.queryone(clxxRealdata.getShebeiNo());
            if(ObjectUtil.isNull(queryone)){
                clxxRealdataService.save(clxxRealdata);
            }else{
                clxxRealdata.setId(queryone.getId());
                clxxRealdataService.updateById(clxxRealdata);
            }
            return Result.OK("更新成功");
        }else{
            return Result.error("请填入设备编号！");
        }

    }

    /**
     *   添加
     *
     * @param carMiles
     * @return
     */
    @AutoLog(value = "car_miles-添加")
    @ApiOperation(value="car_miles-添加", notes="car_miles-添加")
    @PostMapping(value = "/addCarRunDay")
    public Result<?> addCarRunDay(@RequestBody Carmiles carMiles) {
        carmilesService.save(carMiles);
        return Result.OK("添加成功！");
    }

}
