package com.trtm.iot.aiwarnmsg.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.google.gson.JsonArray;
import com.trtm.iot.aiwarnmsg.utils.ImageUtil;
import com.trtm.iot.aiwarnmsgcfg.entity.AiWarnMsgCfg;
import com.trtm.iot.aiwarnmsgcfg.service.IAiWarnMsgCfgService;
import com.trtm.iot.syjoverhandler.entity.SyjOverHandler;
import com.trtm.iot.syjoverhandler.service.ISyjOverHandlerService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysDepartModel;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.aiwarnmsg.entity.AiWarnMsg;
import com.trtm.iot.aiwarnmsg.service.IAiWarnMsgService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: AI识别预警
 * @Author: jeecg-boot
 * @Date: 2022-02-16
 * @Version: V1.0
 */
@Api(tags = "AI识别预警")
@RestController
@RequestMapping("/aiwarnmsg/aiWarnMsg")
@Slf4j
public class AiWarnMsgController extends JeecgController<AiWarnMsg, IAiWarnMsgService> {
    @Autowired
    private IAiWarnMsgService aiWarnMsgService;
    @Autowired
    private IAiWarnMsgCfgService aiWarnMsgCfgService;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISyjOverHandlerService syjOverHandlerService;

    /**
     * 分页列表查询
     *
     * @param aiWarnMsg
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "AI识别预警-分页列表查询")
    @ApiOperation(value = "AI识别预警-分页列表查询", notes = "AI识别预警-分页列表查询")
    @GetMapping(value = "/list")
    @PermissionData(pageComponent = "anquan/aiwarn/aiwarnmsg/AiWarnMsgList")
    public Result<?> queryPageList(AiWarnMsg aiWarnMsg,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<AiWarnMsg> queryWrapper = QueryGenerator.initQueryWrapper(aiWarnMsg, req.getParameterMap());
        Page<AiWarnMsg> page = new Page<AiWarnMsg>(pageNo, pageSize);
        IPage<AiWarnMsg> pageList = aiWarnMsgService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "AI识别预警-分页列表查询")
    @ApiOperation(value = "AI识别预警-分页列表查询", notes = "AI识别预警-分页列表查询")
    @GetMapping(value = "/list2")
    public Result<?> queryPageList2(AiWarnMsg aiWarnMsg,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        System.out.println(shebei);
        if (aiWarnMsg.getCid() == null) {
            if (!"null".equals(shebei)) {
                aiWarnMsg.setCid(shebei);
            } else {
                aiWarnMsg.setCid("空");
            }
        }

        QueryWrapper<AiWarnMsg> queryWrapper = QueryGenerator.initQueryWrapper(aiWarnMsg, req.getParameterMap());
        queryWrapper.orderByDesc("id");
        Page<AiWarnMsg> page = new Page<AiWarnMsg>(pageNo, pageSize);
        IPage<AiWarnMsg> pageList = aiWarnMsgService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param aiWarnMsg
     * @return
     */
    @AutoLog(value = "AI识别预警-添加")
    @ApiOperation(value = "AI识别预警-添加", notes = "AI识别预警-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody AiWarnMsg aiWarnMsg) {
        aiWarnMsgService.save(aiWarnMsg);
        return Result.OK("添加成功！");
    }

    /**
     * 数据接收
     *
     * @param aiWarnMsg
     * @return
     */
    @AutoLog(value = "AI识别预警-添加")
    @ApiOperation(value = "AI识别预警-添加", notes = "AI识别预警-添加")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen(@RequestBody AiWarnMsg aiWarnMsg) {
//		 AiWarnMsgCfg one = aiWarnMsgCfgService.getOne(new QueryWrapper<AiWarnMsgCfg>().eq("cid", aiWarnMsg.getCid()));
//		 if (one!=null){
        String traceId = aiWarnMsg.getTraceId();
        if (!StringUtils.isEmpty(traceId)) {
            AiWarnMsg msg = aiWarnMsgService.getOne(new QueryWrapper<AiWarnMsg>().eq("trace_id", traceId));
            if (msg != null) {
                return Result.error("数据重复");
            } else {
                aiWarnMsgService.save(aiWarnMsg);
                return Result.OK("添加成功！");
            }
        } else {
            return Result.error("traceId为空，上传失败");
        }
//		 }else {
//		 	return Result.error("该任务id没有配置，请联系开发人员或者实施人员配置任务id");
//		 }
    }

    /**
     * 编辑
     *
     * @param aiWarnMsg
     * @return
     */
    @AutoLog(value = "AI识别预警-编辑")
    @ApiOperation(value = "AI识别预警-编辑", notes = "AI识别预警-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody AiWarnMsg aiWarnMsg) {
        aiWarnMsgService.updateById(aiWarnMsg);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "AI识别预警-通过id删除")
    @ApiOperation(value = "AI识别预警-通过id删除", notes = "AI识别预警-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        aiWarnMsgService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "AI识别预警-批量删除")
    @ApiOperation(value = "AI识别预警-批量删除", notes = "AI识别预警-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.aiWarnMsgService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "AI识别预警-通过id查询")
    @ApiOperation(value = "AI识别预警-通过id查询", notes = "AI识别预警-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        AiWarnMsg aiWarnMsg = aiWarnMsgService.getById(id);
        if (aiWarnMsg == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(aiWarnMsg);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param aiWarnMsg
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AiWarnMsg aiWarnMsg) {
        return super.exportXls(request, aiWarnMsg, AiWarnMsg.class, "AI识别预警");
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
        return super.importExcel(request, response, AiWarnMsg.class);
    }

    @AutoLog(value = "AI识别摄像头返回实时播放地址")
    @ApiOperation(value = "AI识别摄像头返回实时播放地址", notes = "AI识别摄像头返回实时播放地址")
    @GetMapping(value = "/GetPushStreamUrl")
    public Result<?> GetPushStreamUrl(@RequestParam(name = "DeviceNo", required = true) String DeviceNo) {
        String resultUrl = "";
        String get = HttpRequest.get("http://api.uddgps.com/UserLogin?username=hangzhougaoxun&password=797919")
                .header("Content-Type", "application/json")
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(get);
        String result = jsonObject.get("Result").toString();
        String SessionId = "";
        if (result.equals("ok")) {
            SessionId = jsonObject.get("SessionId").toString();
        }

        String get2 = HttpRequest.get("http://api.uddgps.com/GetPushStreamUrl?DeviceNo=" + DeviceNo + "&SessionId=" + SessionId)
                .header("Content-Type", "application/json")
                .execute()
                .body();
        JSONObject jsonObject2 = new JSONObject(get2);
        String result2 = jsonObject2.get("Result").toString();
        if (result2.equals("ok")) {
            Map streamMap = JSONUtil.toBean((JSONObject) jsonObject2.get("StreamMap"), Map.class);
            Set<String> values = streamMap.keySet();
            if (values.size() > 1) {
                String get3 = HttpRequest.get("http://bova.createiot.com:8001/getChannelData?Channelname=IPCameraState_Response&DeviceNo=" + DeviceNo + "&SessionId=" + SessionId + "=false")
                        .header("Content-Type", "application/json")
                        .execute()
                        .body();
                JSONObject jsonObject3 = new JSONObject(get3);
                JSONArray data = (JSONArray) jsonObject3.get("Data");
                JSONObject object = (JSONObject) data.get(0);
                String serialNumber = object.get("SerialNumber").toString();
                for (String s : values
                ) {
                    if (s.equals(serialNumber)) {
                        System.out.println(s + ":" + streamMap.get(s));
                        JSONObject o = (JSONObject) streamMap.get(s);
                        String flvurl = o.get("Flvurl").toString();
                        resultUrl = flvurl + "?Token=" + SessionId + "&DeviceNo=" + DeviceNo;
                    }
                }

            } else {
                for (String s : values
                ) {
//				 System.out.println(s + ":" + streamMap.get(s));
                    JSONObject o = (JSONObject) streamMap.get(s);
                    String flvurl = o.get("Flvurl").toString();
//				 System.out.println(flvurl);
                    resultUrl = flvurl + "?Token=" + SessionId + "&DeviceNo=" + DeviceNo;
                }
            }
        }
        return Result.OK(resultUrl);
    }

    @AutoLog(value = "AI识别摄像头返回实时播放地址2")
    @ApiOperation(value = "AI识别摄像头返回实时播放地址2", notes = "AI识别摄像头返回实时播放地址2")
    @GetMapping(value = "/GetPushStreamUrl2")
    public Result<?> GetPushStreamUrl2(@RequestParam(name = "deviceSn", required = true) String deviceSn) {
        JSONObject body = new JSONObject();
        body.set("appKey", "e1e484bddd354219bd5b82ce2490bbac");
        body.set("secret", "02d00e314c2848269ae927b8777e4133");
        String tokenpost = HttpRequest.post("https://www.aiot-y.com/api/user-service/lapp/v1/token/get")
                .header("Content-Type", "application/json")
                .body(body.toString())
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(tokenpost);
        int code = (int) jsonObject.get("code");
        if (code == 200) {
            JSONObject data = (JSONObject) jsonObject.get("data");
            String accessToken = data.get("accessToken").toString();
            JSONObject body2 = new JSONObject();
            body2.set("deviceSn", deviceSn);
            body2.set("accessToken", accessToken);
            String post = HttpRequest.post("https://www.aiot-y.com/api/device-service/lapp/v1/live/address/get")
                    .header("Content-Type", "application/json")
                    .body(body2.toString())
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(post);
            int code2 = (int) jsonObject.get("code");
            if (code2 == 200) {
                JSONObject data2 = (JSONObject) jsonObject2.get("data");
                String url = data2.get("url").toString();
                return Result.OK(url);
            }
        }
        return Result.error("未成功获取播放地址");
    }

    @AutoLog(value = "博视AI识别摄像头接受数据接口")
    @ApiOperation(value = "博视AI识别摄像头接受数据接口", notes = "博视AI识别摄像头接受数据接口")
    @PostMapping(value = "/BoShiAiWarn")
    public Result<?> BoShiAiWarn(@RequestBody JSONObject json) throws InvalidKeyException, NoSuchAlgorithmException {
        try {
            log.info("博视AI识别摄像头接受数据:" + json);
            String secret = "02d00e314c2848269ae927b8777e4133";//签名密钥
//            String signature = json.get("signature").toString();
            String timestamp = json.get("timestamp").toString();
//            boolean equals = signature.equals(hmacSha1(secret, json + timestamp, StandardCharsets.UTF_8));
            boolean equals = true;
            if (equals) {

                String device_sn = json.get("device_sn").toString();
                String alarm_type = json.get("alarm_type").toString();
                String alarm_img = "";
                String url = "";
                if (oConvertUtils.isNotEmpty(json.get("alarm_img"))) {
                    alarm_img = json.get("alarm_img").toString();
                    Map map = new HashMap();
                    map.put("img",alarm_img);
                    String post = HttpRequest.post("http://z.traiot.cn/jeecg-boot/sys/common/uploadLockAI2")
                            .header("Content-Type", "multipart/form-data")
                            .form(map)
                            .execute()
                            .body();
                    JSONObject jsonObject1 = new JSONObject(post);
                    Boolean success = (Boolean) jsonObject1.get("success");
                    if(success){
                        url = jsonObject1.get("message").toString();
                    }
                }

                JSONObject extend = (JSONObject) json.get("extend");

                String alias = extend.get("alias").toString().substring(2,extend.get("alias").toString().length()-2);//报警信息
                String label = extend.get("label").toString().substring(2,extend.get("label").toString().length()-2);//报警信息
                AiWarnMsg aiWarnMsg = new AiWarnMsg();
                if (label.equals("clothes")) {
                    aiWarnMsg.setAlgtype("2");
                } else if (label.equals("head")) {
                    aiWarnMsg.setAlgtype("1");
                }

                aiWarnMsg.setPicurls(url);
                aiWarnMsg.setWarntime(timestamp);
                QueryWrapper<AiWarnMsgCfg> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("shebeiid", device_sn);
                AiWarnMsgCfg aiWarnMsgCfg = aiWarnMsgCfgService.getOne(queryWrapper);
                if (aiWarnMsgCfg != null) {
                    aiWarnMsg.setCid(aiWarnMsgCfg.getCid());
                    aiWarnMsg.setCname(aiWarnMsgCfg.getLocal());
                    aiWarnMsg.setWarnmsg(aiWarnMsgCfg.getLocal() + "发现有人" + alias);
                } else {
                    log.info("未找到摄像头配置");
                    return null;
                }
                QueryWrapper<AiWarnMsg> aiWarnMsgQueryWrapper = new QueryWrapper<>();
                aiWarnMsgQueryWrapper.eq("cid", aiWarnMsg.getCid());
                aiWarnMsgQueryWrapper.eq("cname", aiWarnMsgCfg.getLocal());
                aiWarnMsgQueryWrapper.eq("warntime", timestamp);
                aiWarnMsgQueryWrapper.eq("algType", aiWarnMsg.getAlgtype());
                aiWarnMsgQueryWrapper.eq("picurls", alarm_img);
                List<AiWarnMsg> list = aiWarnMsgService.list(aiWarnMsgQueryWrapper);
                if (list == null || list.size() == 0) {
                    boolean save = aiWarnMsgService.save(aiWarnMsg);
                    if (save) {
                        log.info("添加成功" + aiWarnMsg.toString());
                        return Result.OK("添加成功" + json);
                    } else {
                        log.info("添加失败" + aiWarnMsg.toString());
                        return Result.error("添加失败" + json);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("博视AI识别摄像头接受数据失败");
            return Result.error(e.getMessage());
        }
        return null;
    }

    public static String hmacSha1(String key, String data, Charset bytesEncode)
            throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(bytesEncode), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        //通用方法，自己实现即可（由 byte[]转 hex string）
        return byteArrayToHexStr(mac.doFinal(data.getBytes(bytesEncode)));
    }

    /**
     * 由 byte[]转 hex string
     */
    public static String byteArrayToHexStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        for (int j = 0; j < byteArray.length; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * 试验机处置审核驳回
     *
     * @param syjOverHandler
     * @return
     */
    @AutoLog(value = "试验机处置审核表-处置审核驳回")
    @ApiOperation(value = "试验机处置审核表-处置审核驳回", notes = "试验机处置审核表-处置审核驳回")
    @GetMapping(value = "/deallist")
    @Transactional
    public Result<?> dealList(SyjOverHandler syjOverHandler, Integer flag, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        SyjOverHandler selectone = syjOverHandlerService.selectone(syjOverHandler.getBaseid());
        SyjOverHandler syjOverHandler1 = new SyjOverHandler();
        BeanUtils.copyProperties(syjOverHandler, syjOverHandler1);
        if (selectone == null) {
            syjOverHandler1.setHandlePerson(loginUser.getRealname());
            syjOverHandler1.setHandleTime(new Date());
            syjOverHandler1.setOverproofStatus(10);
            syjOverHandlerService.save(syjOverHandler1);
            AiWarnMsg byId = new AiWarnMsg();
            byId.setId(Integer.valueOf(syjOverHandler.getBaseid()));
            byId.setEnttype("10");
            aiWarnMsgService.updateById(byId);
            return Result.OK("处置成功!");
        } else {
            return Result.OK("请勿重复处置!");
        }

    }

    /**
     * 通过id查询
     *
     * @param
     * @return
     */
    @AutoLog(value = "试验机处置审核表-通过id查询")
    @ApiOperation(value = "试验机处置审核表-通过id查询", notes = "试验机处置审核表-通过id查询")
    @GetMapping(value = "/queryBybaseId")
    public Result<?> queryBybaseId(@RequestParam(name = "baseid", required = true) String baseid) {
//        SyjOverHandler syjOverHandler = syjOverHandlerService.getById(id);
        QueryWrapper<SyjOverHandler> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("baseid",baseid);
        SyjOverHandler  syjOverHandler =    syjOverHandlerService.getOne(queryWrapper);
        if (syjOverHandler == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(syjOverHandler);
    }
}
