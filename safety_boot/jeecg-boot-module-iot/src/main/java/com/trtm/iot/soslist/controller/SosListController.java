package com.trtm.iot.soslist.controller;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trtm.iot.soslist.common.MyWebSocketClient;
import com.trtm.iot.system.service.IShebeiInfoService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.soslist.entity.SosList;
import com.trtm.iot.soslist.service.ISosListService;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

import static org.java_websocket.enums.ReadyState.OPEN;

/**
 * @Description: sos_list
 * @Author: jeecg-boot
 * @Date: 2024-12-17
 * @Version: V1.0
 */
@Api(tags = "sos_list")
@RestController
@RequestMapping("/soslist/sosList")
@Slf4j
public class SosListController extends JeecgController<SosList, ISosListService> {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISosListService sosListService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;

    /**
     * 分页列表查询
     *
     * @param sosList
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "sos_list-分页列表查询")
    @ApiOperation(value = "sos_list-分页列表查询", notes = "sos_list-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SosList sosList,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (sosList.getDeviceId() == null) {
            if (!"null".equals(shebei)) {
                sosList.setDeviceId(shebei);
            } else {
                sosList.setDeviceId("空");
            }
        }
        List<String> strings = new ArrayList<>();
        List<String> list = shebeiInfoService.selectSbjnoListLikeOrgcode(loginUser.getOrgCode(), 40);
        List<String> list1 = shebeiInfoService.selectSbjnoListLikeOrgcode(loginUser.getOrgCode(), 84);
        strings.addAll(list);
        strings.addAll(list1);
        QueryWrapper<SosList> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(sosList.getUserName() != null, "user_name", sosList.getUserName());
        queryWrapper.like(sosList.getSosType() != null, "sos_type", sosList.getSosType());
        queryWrapper.in("device_id", strings);
        queryWrapper.orderByDesc("c_time");
        Page<SosList> page = new Page<SosList>(pageNo, pageSize);
        IPage<SosList> pageList = sosListService.page(page, queryWrapper);
        List<SosList> records = pageList.getRecords();
        if (ObjectUtil.isNotEmpty(records)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            for (SosList record : records) {
                Date date = new Date(Long.parseLong(record.getCTime()) * 1000);  // 毫秒为单位
                record.setCTime(sdf.format(date));
            }
        }
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param sosList
     * @return
     */
    @AutoLog(value = "sos_list-添加")
    @ApiOperation(value = "sos_list-添加", notes = "sos_list-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SosList sosList) {
        sosListService.save(sosList);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param sosList
     * @return
     */
    @AutoLog(value = "sos_list-编辑")
    @ApiOperation(value = "sos_list-编辑", notes = "sos_list-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SosList sosList) {
        sosListService.updateById(sosList);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "sos_list-通过id删除")
    @ApiOperation(value = "sos_list-通过id删除", notes = "sos_list-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sosListService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "sos_list-批量删除")
    @ApiOperation(value = "sos_list-批量删除", notes = "sos_list-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.sosListService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "sos_list-通过id查询")
    @ApiOperation(value = "sos_list-通过id查询", notes = "sos_list-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SosList sosList = sosListService.getById(id);
        if (sosList == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(sosList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param sosList
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SosList sosList) {
        return super.exportXls(request, sosList, SosList.class, "sos_list");
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
        return super.importExcel(request, response, SosList.class);
    }

    @AutoLog(value = "指挥调度平台-获取实时、状态等心跳包")
    @ApiOperation(value = "指挥调度平台-获取实时、状态等心跳包", notes = "指挥调度平台-获取实时、状态等心跳包")
    @PostMapping(value = "/caps")
    public Result<?> AnQuanMao() throws JsonProcessingException {
        Map loginMap = getLoginMap();
        // 创建 JsonParser 实例
        JsonParser parser = new JsonParser();
        // 解析字符串为 JsonObject
        JsonObject loginMessage = parser.parse(loginMap.get("loginMessage").toString()).getAsJsonObject();
        // 获取对象
        JsonObject adminInfo = loginMessage.getAsJsonObject("admin_info");
        // 获取字段
        String adminId = adminInfo.get("admin_id").getAsString();

        // 创建 ObjectMapper 实例
        ObjectMapper objectMapper = new ObjectMapper();
        // 解析 JSON 字符串为 JsonNode 对象
        JsonNode rootNode = objectMapper.readTree(loginMap.get("deviceMessage").toString());
        // 获取 "data" 数组
        JsonNode dataNode = rootNode.get("data");
        if (ObjectUtil.isNotEmpty(dataNode)) {
            // 遍历 "data" 数组中的每个元素
            Iterator<JsonNode> elements = dataNode.elements();
            while (elements.hasNext()) {
                JsonNode deviceNode = elements.next();
                // 获取 user_info 子节点
                JsonNode userInfoNode = deviceNode.get("user_info");
                // 获取 user_id 值
                String userId = userInfoNode.get("user_id").asText();
                System.out.println("User ID: " + userId);
            }
        }
        return Result.OK(dataNode);
    }

    @AutoLog(value = "指挥调度平台-获取轨迹")
    @ApiOperation(value = "指挥调度平台-获取轨迹", notes = "指挥调度平台-获取轨迹")
    @GetMapping(value = "/getUserPath")
    public Result<?> getUserPath(String startTime,
                                 String endTime,
                                 String userId, String shebeiNo) {
        if (ObjectUtil.isEmpty(userId)) {
            String data = "";
            if ("肖国锋".equals(shebeiNo)) {
                data = "[{\"y_point\": \"119.015073\",\"c_time\": 1734565264,\"x_point\": \"31.673855\"},{\"y_point\": \"119.015191\",\"c_time\": 1734564980,\"x_point\": \"31.674660\"},{\"y_point\": \"119.015099\",\"c_time\": 1734565308,\"x_point\": \"31.675834\"},{\"y_point\": \"119.015322\",\"c_time\": 1734565341,\"x_point\": \"31.676538\"}]";
            }
            if ("张文军".equals(shebeiNo)) {
                data = "[{\"y_point\": \"119.013989\",\"c_time\": 1734565264,\"x_point\": \"31.675341\"},{\"y_point\": \"119.015106\",\"c_time\": 1734564980,\"x_point\": \"31.675240\"},{\"y_point\": \"119.015644\",\"c_time\": 1734565308,\"x_point\": \"31.674860\"},{\"y_point\": \"119.015749\",\"c_time\": 1734565341,\"x_point\": \"31.674178\"}]";
            }
            if ("周庭军".equals(shebeiNo)) {
                data = "[{\"y_point\": \"119.014291\",\"c_time\": 1734565264,\"x_point\": \"31.674446\"},{\"y_point\": \"119.014567\",\"c_time\": 1734564980,\"x_point\": \"31.675475\"},{\"y_point\": \"119.015802\",\"c_time\": 1734565308,\"x_point\": \"31.675542\"},{\"y_point\": \"119.015579\",\"c_time\": 1734565341,\"x_point\": \"31.676369\"}]";
            }
            try {
                ObjectMapper objectMapper1 = new ObjectMapper();
                JsonNode dataObj = objectMapper1.readTree(data);
                return Result.OK(dataObj);
            } catch (Exception e) {
                e.printStackTrace();
                return Result.error("请求失败！");
            }
        } else {
            String pkey = getPkey();
            String token = getToken(pkey);
            Map loginMap = getLoginMap();
            // 创建 JsonParser 实例
            JsonParser parser = new JsonParser();
            // 解析字符串为 JsonObject
            JsonObject loginMessage = parser.parse(loginMap.get("loginMessage").toString()).getAsJsonObject();
            // 获取对象
            JsonObject adminInfo = loginMessage.getAsJsonObject("admin_info");
            // 获取字段
            String adminId = adminInfo.get("admin_id").getAsString();
            try {
                // 定义日期时间格式，确保与输入字符串的格式匹配
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // 设置时区为 UTC
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                // 将字符串解析为 Date 对象
                Date startTimes = sdf.parse(startTime);
                Date endTimes = sdf.parse(endTime);
                // 获取时间戳（以毫秒为单位）
                long startTimesTamp = startTimes.getTime() / 1000;
                long endTimesTamp = endTimes.getTime() / 1000;
                Map map = new HashMap();
                map.put("admin_id", adminId);
                map.put("user_id", userId);
                map.put("start", startTimesTamp);
                map.put("end", endTimesTamp);
                map.put("token", token);
                String post = HttpRequest.post("https://caps.runde.pro/api/index.php??ctl=location&act=get_user_path_web")
                        .form(map)
                        .execute()
                        .body();
                JSONObject jsonObjects = new JSONObject(post);
                String data1 = jsonObjects.getStr("data");
                System.out.println(data1);
//            JSONArray data = (JSONArray) jsonObjects.get("data");
//            Map map1 = new HashMap<>();
//            map1.put("data", data);
//            System.out.println(data);
//            return Result.OK(map1);
                ObjectMapper objectMapper1 = new ObjectMapper();
                JsonNode dataObj = objectMapper1.readTree(data1);
                System.out.println(dataObj);
                return Result.OK(dataObj);
            } catch (Exception e) {
                e.printStackTrace();
                return Result.error("请求失败!");
            }
        }
    }

    @AutoLog(value = "指挥调度平台-设备报警记录")
    @ApiOperation(value = "指挥调度平台-设备报警记录", notes = "指挥调度平台-设备报警记录")
    @PostMapping(value = "/getSosList")
    public Result<?> getSosList() throws JsonProcessingException {
        String pkey = getPkey();
        String token = getToken(pkey);
        Map loginMap = getLoginMap();
        // 创建 JsonParser 实例
        JsonParser parser = new JsonParser();
        // 解析字符串为 JsonObject
        JsonObject loginMessage = parser.parse(loginMap.get("loginMessage").toString()).getAsJsonObject();
        // 获取对象
        JsonObject adminInfo = loginMessage.getAsJsonObject("admin_info");
        // 获取字段
        String adminId = adminInfo.get("admin_id").getAsString();

        // 创建 ObjectMapper 实例
        ObjectMapper objectMapper = new ObjectMapper();
        // 解析 JSON 字符串为 JsonNode 对象
        JsonNode rootNode = objectMapper.readTree(loginMap.get("deviceMessage").toString());
        // 获取 "data" 数组
        JsonNode dataNode = rootNode.get("data");
        // 遍历 "data" 数组中的每个元素
        Iterator<JsonNode> elements = dataNode.elements();
        List<String> list = new ArrayList<>();
        while (elements.hasNext()) {
            JsonNode deviceNode = elements.next();
            // 获取 user_info 子节点
            JsonNode userInfoNode = deviceNode.get("user_info");
            // 获取 user_id 值
            String userId = userInfoNode.get("user_id").asText();
            list.add(userId);
        }
        List<Object> objList = new ArrayList<>();
        for (String userId : list) {
            Map map = new HashMap();
            map.put("admin_id", adminId);
            map.put("user_id", userId);
            map.put("p", 1);
            map.put("sos_type", "");
            map.put("token", token);
            String post = HttpRequest.post("https://caps.runde.pro/api/index.php??ctl=task&act=get_sos_list")
                    .form(map)
                    .execute()
                    .body();
            JSONObject jsonObjects = new JSONObject(post);
            String data1 = jsonObjects.getStr("data");
            ObjectMapper objectMapper1 = new ObjectMapper();
            JsonNode dataObj = objectMapper1.readTree(data1);
            sosListService.saveOrUpdateBySid(dataObj);
            objList.add(dataObj);
        }
        return Result.OK(objList);
    }

    private Map getLoginMap() {
        String loginMessage = null;
        String deviceMessage = null;
        Map map1 = new HashMap<>();
        try {
            //登录
            String pkey = getPkey();
            //获取token
            String token = getToken(pkey);
            URI serverUri = new URI("wss://caps.runde.pro/wss");
            MyWebSocketClient client = new MyWebSocketClient(serverUri);
            client.connect();
            System.out.println(client.getReadyState());
            while (!client.getReadyState().equals(OPEN)) {
                System.out.println("连接中···");
                Thread.sleep(2000);
            }
            String a = "{\n" +
                    "\"act\": \"ma_login\",\n" +
                    "\"user_name\": \"入海水道\",\n" +
                    "\"access_token\": \"" + token + "\"\n" +
                    "}";
            client.sendMessage(a);
            //等待WebSocket服务端响应
            while ((loginMessage = client.getLoginMessage()) == null) {
                System.out.println("等待中...");
                Thread.sleep(1000);
            }
            System.out.println("成功获取管理员登录数据：" + loginMessage);
            map1.put("loginMessage", loginMessage);
            String b = "{\n" +
                    "\"act\": \"ma_get_active_devices\"\n" +
                    "}";
            client.sendMessage(b);
            //等待WebSocket服务端响应
            while ((deviceMessage = client.getDeviceMessage()) == null) {
                System.out.println("等待中...");
                Thread.sleep(1000);
            }
            //打印服务端返回的数据
            System.out.println("成功获取心跳包数据：" + deviceMessage);
            map1.put("deviceMessage", deviceMessage);
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map1;
    }

    private static String getPkey() {
        Map map = new HashMap();
        map.put("user_name", "入海水道");
        map.put("pwd", "Ab@123456");
        String post = HttpRequest.post("https://caps.runde.pro/api/index.php?ctl=tool&act=get_pkey")
                .form(map)
                .execute()
                .body();
        JSONObject jsonObjects = new JSONObject(post);
        String data = jsonObjects.getStr("data");
        return data;
    }

    private static String getToken(String pkey) {
        Map map = new HashMap();
        map.put("user_name", "入海水道");
        map.put("pkey", pkey);
        String post = HttpRequest.post("https://caps.runde.pro/api/index.php?ctl=tool&act=get_token")
                .form(map)
                .execute()
                .body();
        JSONObject jsonObjects = new JSONObject(post);
        String token = jsonObjects.getStr("token");
        return token;
    }


}
