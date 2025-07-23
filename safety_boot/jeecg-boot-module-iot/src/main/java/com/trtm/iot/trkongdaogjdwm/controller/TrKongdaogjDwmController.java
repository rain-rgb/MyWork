package com.trtm.iot.trkongdaogjdwm.controller;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.kongdaoyaj.entity.Kongdaoyaj;
import com.trtm.iot.kongdaoyaj.entity.Kongdaoyajs;
import com.trtm.iot.kongdaoyaj.service.IKongdaoyajService;
import com.trtm.iot.kongdaoyaj.service.IKongdaoyajsService;
import com.trtm.iot.kongdaoyaj.vo.KongdaoyajPage;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.jeecg.common.util.RedisUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.trkongdaogjdwm.entity.TrKongdaogjDws;
import com.trtm.iot.trkongdaogjdwm.entity.TrKongdaogjDwm;
import com.trtm.iot.trkongdaogjdwm.vo.TrKongdaogjDwmPage;
import com.trtm.iot.trkongdaogjdwm.service.ITrKongdaogjDwmService;
import com.trtm.iot.trkongdaogjdwm.service.ITrKongdaogjDwsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 孔道灌浆（定位检测）主表
 * @Author: jeecg-boot
 * @Date: 2024-03-15
 * @Version: V1.0
 */
@Api(tags = "孔道灌浆（定位检测）主表")
@RestController
@RequestMapping("/trkongdaogjdwm/trKongdaogjDwm")
@Slf4j
public class TrKongdaogjDwmController {
    @Autowired
    private ITrKongdaogjDwmService trKongdaogjDwmService;
    @Autowired
    private ITrKongdaogjDwsService trKongdaogjDwsService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IKongdaoyajService kongdaoyajService;
    @Autowired
    private IKongdaoyajsService kongdaoyajsService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    /**
     * 分页列表查询
     *
     * @param trKongdaogjDwm
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "孔道灌浆（定位检测）主表-分页列表查询")
    @ApiOperation(value = "孔道灌浆（定位检测）主表-分页列表查询", notes = "孔道灌浆（定位检测）主表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrKongdaogjDwm trKongdaogjDwm,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trKongdaogjDwm.getMachineid() == null) {
            if (!"null".equals(shebei)) {
                trKongdaogjDwm.setMachineid(shebei);
            } else {
                trKongdaogjDwm.setMachineid("空");
            }

        }
        QueryWrapper<TrKongdaogjDwm> queryWrapper = QueryGenerator.initQueryWrapper(trKongdaogjDwm, req.getParameterMap());
        Page<TrKongdaogjDwm> page = new Page<TrKongdaogjDwm>(pageNo, pageSize);
        IPage<TrKongdaogjDwm> pageList = trKongdaogjDwmService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param trKongdaogjDwmPage
     * @return
     */
    @AutoLog(value = "孔道灌浆（定位检测）主表-添加")
    @ApiOperation(value = "孔道灌浆（定位检测）主表-添加", notes = "孔道灌浆（定位检测）主表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrKongdaogjDwmPage trKongdaogjDwmPage) {
        TrKongdaogjDwm trKongdaogjDwm = new TrKongdaogjDwm();
        BeanUtils.copyProperties(trKongdaogjDwmPage, trKongdaogjDwm);
        trKongdaogjDwmService.saveMain(trKongdaogjDwm, trKongdaogjDwmPage.getTrKongdaogjDwsList());
        return Result.OK("添加成功！");
    }

	/**
	 *   添加
	 *
	 * @param
	 * @return
	 */
	@AutoLog(value = "孔道灌浆（定位检测）主表-添加")
	@ApiOperation(value="孔道灌浆（定位检测）主表-添加", notes="孔道灌浆（定位检测）主表-添加")
	@PostMapping(value = "/addOpen")
    public Map<String, Object> extractJsonFromRarDirectly(@RequestParam("file") MultipartFile uploadedFile) {
        Map<String, Object> resultMap = new HashMap<>();
        try (InputStream inputStream = uploadedFile.getInputStream();) {
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            pushandreturnService.saveData(0, "上传文件开始", "kongdaoyaj", "上传文件开始");
            Map jsonObjectmap = processZipFile(zipInputStream);
            int count = 0;
            pushandreturnService.saveData(0, String.valueOf(jsonObjectmap), "kongdaoyaj", "上传文件开始");
            if (jsonObjectmap != null) {
                count = Integer.valueOf(jsonObjectmap.get("count").toString());
                String jsonString = String.valueOf(jsonObjectmap.get("jsonObject"));
                cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(jsonString);
                Map<String, Object> lowerCaseMap = new HashMap<>();
                for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                    lowerCaseMap.put(entry.getKey().toLowerCase(), entry.getValue());
                }
                JSONObject lowerCaseJsonObject = new JSONObject(lowerCaseMap);

                KongdaoyajPage kongdaoyajVo = JSON.toJavaObject(lowerCaseJsonObject, KongdaoyajPage.class);
                Kongdaoyaj kongdaoyaj = new Kongdaoyaj();
                BeanUtils.copyProperties(kongdaoyajVo, kongdaoyaj);
                kongdaoyajService.save(kongdaoyaj);

                List<Kongdaoyajs> detectData = kongdaoyajVo.getDetectData();
                for (Kongdaoyajs kongdaoyajs : detectData) {
                    kongdaoyajs.setUuid(kongdaoyaj.getUuid());
                    kongdaoyajsService.save(kongdaoyajs);
                }
            }
            Map datas = new HashMap();
            List list = new ArrayList();
            //根据count个数添加list.add("完成")
            for (int i = 0; i < count; i++) {
                list.add("完成");
            }
            datas.put("values",list);

            resultMap.put("status", 200);
            resultMap.put("message", "成功");
            resultMap.put("datas",datas);
        } catch (IOException e) {
            resultMap.put("status", 0);
            resultMap.put("message", "失败");
            pushandreturnService.saveData(0, "异常", "kongdaoyaj", e.getMessage());
        }
        return resultMap;
    }
    public Map processZipFile(ZipInputStream zipInputStream) {
        cn.hutool.json.JSONObject jsonObject = null;
        int count = 0;
        try {
            // 遍历ZIP流中的条目
            ZipEntry entry;
            pushandreturnService.saveData(0, "分析文件开始", "kongdaoyaj", "分析文件开始");
            while ((entry = zipInputStream.getNextEntry()) != null) {
                //统计有多少个bin文件
                if (entry.getName().endsWith(".bin") || entry.getName().contains(".bin")) {
                    count ++;
                }
                if (entry.getName().endsWith(".json") || entry.getName().contains(".json")) {
                    // 读取.json文件内容
                    StringBuilder jsonContent = new StringBuilder();
                    pushandreturnService.saveData(0, entry.getName(), "kongdaoyaj", "提取json文件");
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(zipInputStream, StandardCharsets.UTF_8))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            jsonContent.append(line);
                        }
                        // 解析JSON内容
                        jsonObject = new cn.hutool.json.JSONObject(jsonContent.toString());

                        pushandreturnService.saveData(0, String.valueOf(jsonObject), "kongdaoyaj", "提取json文件成功");
                    } catch (Exception e) {
                        pushandreturnService.saveData(0, "异常", "kongdaoyaj", e.getMessage());
                    }
                }
                // 关闭当前条目，准备处理下一个
                zipInputStream.closeEntry();
            }
        } catch (IOException e) {
            pushandreturnService.saveData(0, "异常", "kongdaoyaj", e.getMessage());
        }
        Map map = new HashMap();
        map.put("count", count);
        map.put("jsonObject", jsonObject);
        return map;
    }
    /**
     * 编辑
     *
     * @param trKongdaogjDwmPage
     * @return
     */
    @AutoLog(value = "孔道灌浆（定位检测）主表-编辑")
    @ApiOperation(value = "孔道灌浆（定位检测）主表-编辑", notes = "孔道灌浆（定位检测）主表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrKongdaogjDwmPage trKongdaogjDwmPage) {
        TrKongdaogjDwm trKongdaogjDwm = new TrKongdaogjDwm();
        BeanUtils.copyProperties(trKongdaogjDwmPage, trKongdaogjDwm);
        TrKongdaogjDwm trKongdaogjDwmEntity = trKongdaogjDwmService.getById(trKongdaogjDwm.getId());
        if (trKongdaogjDwmEntity == null) {
            return Result.error("未找到对应数据");
        }
        trKongdaogjDwmService.updateMain(trKongdaogjDwm, trKongdaogjDwmPage.getTrKongdaogjDwsList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "孔道灌浆（定位检测）主表-通过id删除")
    @ApiOperation(value = "孔道灌浆（定位检测）主表-通过id删除", notes = "孔道灌浆（定位检测）主表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trKongdaogjDwmService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "孔道灌浆（定位检测）主表-批量删除")
    @ApiOperation(value = "孔道灌浆（定位检测）主表-批量删除", notes = "孔道灌浆（定位检测）主表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trKongdaogjDwmService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "孔道灌浆（定位检测）主表-通过id查询")
    @ApiOperation(value = "孔道灌浆（定位检测）主表-通过id查询", notes = "孔道灌浆（定位检测）主表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrKongdaogjDwm trKongdaogjDwm = trKongdaogjDwmService.getById(id);
        if (trKongdaogjDwm == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(trKongdaogjDwm);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "孔道灌浆（定位检测）子表通过主表ID查询")
    @ApiOperation(value = "孔道灌浆（定位检测）子表主表ID查询", notes = "孔道灌浆（定位检测）子表-通主表ID查询")
    @GetMapping(value = "/queryTrKongdaogjDwsByMainId")
    public Result<?> queryTrKongdaogjDwsListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<TrKongdaogjDws> trKongdaogjDwsList = trKongdaogjDwsService.selectByMainId(id);
        return Result.OK(trKongdaogjDwsList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trKongdaogjDwm
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrKongdaogjDwm trKongdaogjDwm) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<TrKongdaogjDwm> queryWrapper = QueryGenerator.initQueryWrapper(trKongdaogjDwm, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<TrKongdaogjDwm> queryList = trKongdaogjDwmService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<TrKongdaogjDwm> trKongdaogjDwmList = new ArrayList<TrKongdaogjDwm>();
        if (oConvertUtils.isEmpty(selections)) {
            trKongdaogjDwmList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            trKongdaogjDwmList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<TrKongdaogjDwmPage> pageList = new ArrayList<TrKongdaogjDwmPage>();
        for (TrKongdaogjDwm main : trKongdaogjDwmList) {
            TrKongdaogjDwmPage vo = new TrKongdaogjDwmPage();
            BeanUtils.copyProperties(main, vo);
            List<TrKongdaogjDws> trKongdaogjDwsList = trKongdaogjDwsService.selectByMainId(String.valueOf(main.getId()));
            vo.setTrKongdaogjDwsList(trKongdaogjDwsList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "孔道灌浆（定位检测）主表列表");
        mv.addObject(NormalExcelConstants.CLASS, TrKongdaogjDwmPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("孔道灌浆（定位检测）主表数据", "导出人:" + sysUser.getRealname(), "孔道灌浆（定位检测）主表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
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
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<TrKongdaogjDwmPage> list = ExcelImportUtil.importExcel(file.getInputStream(), TrKongdaogjDwmPage.class, params);
                for (TrKongdaogjDwmPage page : list) {
                    TrKongdaogjDwm po = new TrKongdaogjDwm();
                    BeanUtils.copyProperties(page, po);
                    trKongdaogjDwmService.saveMain(po, page.getTrKongdaogjDwsList());
                }
                return Result.OK("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.OK("文件导入失败！");
    }

}
