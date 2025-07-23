package com.trtm.sy.sydpssysample.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trtm.sy.sydpssysample.entity.*;
import com.trtm.sy.sydpssysample.service.ISyDpsSySampleService;
import com.trtm.sy.sydpssysample.utils.FileUtil;
import com.trtm.sy.sylxdps.entity.*;
import com.trtm.sy.sylxdps.service.ISyDpsSyReportMService;
import com.trtm.sy.sylxdps.service.ISyDpsSyReportSService;
import com.trtm.sy.sylxdps.service.ISyDpsSyTableheaderService;
import com.xkcoding.http.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.ShtooneUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: sy_dps_sy_sample
 * @Author: jeecg-boot
 * @Date: 2023-01-12
 * @Version: V1.0
 */
@Api(tags = "sy_dps_sy_sample")
@RestController
@RequestMapping("/sydpssysample/syDpsSySample")
@Slf4j
public class SyDpsSySampleController extends JeecgController<SyDpsSySample, ISyDpsSySampleService> {
    @Autowired
    private ISyDpsSySampleService syDpsSySampleService;
    @Autowired
    private ISyDpsSyReportMService syDpsSyReportMService;
    @Autowired
    private ISyDpsSyReportSService syDpsSyReportSService;
    @Autowired
    private ISyDpsSyTableheaderService syDpsSyTableheaderService;

    /**
     * 分页列表查询
     *
     * @param syDpsSySample
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "sy_dps_sy_sample-分页列表查询")
    @ApiOperation(value = "sy_dps_sy_sample-分页列表查询", notes = "sy_dps_sy_sample-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SyDpsSySample syDpsSySample,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        syDpsSySample.setSamplename("*" + syDpsSySample.getSamplename() + "*");
        syDpsSySample.setSampleno("*" + syDpsSySample.getSampleno() + "*");
        syDpsSySample.setSamplegcbw("*" + syDpsSySample.getSamplegcbw() + "*");
        syDpsSySample.setReportnonew("*" + syDpsSySample.getReportnonew() + "*");
        QueryWrapper<SyDpsSySample> queryWrapper = QueryGenerator.initQueryWrapper(syDpsSySample, req.getParameterMap());
        Page<SyDpsSySample> page = new Page<SyDpsSySample>(pageNo, pageSize);
        IPage<SyDpsSySample> pageList = syDpsSySampleService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param syDpsSySample
     * @return
     */
    @AutoLog(value = "sy_dps_sy_sample-添加")
    @ApiOperation(value = "sy_dps_sy_sample-添加", notes = "sy_dps_sy_sample-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SyDpsSySample syDpsSySample) {
        syDpsSySampleService.save(syDpsSySample);
        return Result.OK("添加成功！");
    }

    /**
     * 添加
     *
     * @param syDpsSySample
     * @return
     */
    @AutoLog(value = "sy_dps_sy_sample-添加")
    @ApiOperation(value = "sy_dps_sy_sample-添加", notes = "sy_dps_sy_sample-添加")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen(@RequestBody SyDpsSySample syDpsSySample) {
        syDpsSySampleService.save(syDpsSySample);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param map
     * @return
     */
    @AutoLog(value = "sy_dps_sy_sample-编辑")
    @ApiOperation(value = "sy_dps_sy_sample-编辑", notes = "sy_dps_sy_sample-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Map map) {
        map.put("update", "1");
        syDpsSySampleService.add((HashMap) map);
        return Result.OK("更新成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "sy_dps_sy_sample-通过id删除")
    @ApiOperation(value = "sy_dps_sy_sample-通过id删除", notes = "sy_dps_sy_sample-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        syDpsSySampleService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "sy_dps_sy_sample-批量删除")
    @ApiOperation(value = "sy_dps_sy_sample-批量删除", notes = "sy_dps_sy_sample-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.syDpsSySampleService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "sy_dps_sy_sample-通过id查询")
    @ApiOperation(value = "sy_dps_sy_sample-通过id查询", notes = "sy_dps_sy_sample-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SyDpsSySample syDpsSySample = syDpsSySampleService.getById(id);
        if (syDpsSySample == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(syDpsSySample);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param syDpsSySample
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyDpsSySample syDpsSySample) {
        return super.exportXls(request, syDpsSySample, SyDpsSySample.class, "sy_dps_sy_sample");
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
        return super.importExcel(request, response, SyDpsSySample.class);
    }

    @AutoLog(value = "查询项目名称")
    @ApiOperation(value = "查询项目名称", notes = "查询项目名称")
    @GetMapping(value = "/getProjNames")
    public Result<?> getProjNames(@RequestParam("orgCode") String orgCode) {
        String projNames = syDpsSySampleService.selectProjNames(orgCode);
        return Result.OK(projNames);
    }

    /**
     * 试验-复制功能按钮接口
     *
     * @param request
     * @param response
     * @param id         id
     * @param sampleDate 取样日期
     * @param type       类型
     * @param inspection
     * @return
     */
    @Transactional
    @RequestMapping(value = "/copy/{id}/{date}/{type}", method = RequestMethod.GET)
    public Result<?> copy(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id,
                          @PathVariable("date") String sampleDate, @PathVariable("type") String type,
                          @RequestParam(value = "inspection", required = false) String inspection) {
        syDpsSySampleService.copy(request, id, sampleDate, type, inspection);
        return Result.OK();
    }

    /**
     * 试验-修改编号按钮接口
     *
     * @param request
     * @param response
     * @param map
     * @return
     */
    @AutoLog(value = "修改编号")
    @ApiOperation(value = "修改编号", notes = "修改编号")
    @Transactional
    @RequestMapping(value = "/updateNo", method = RequestMethod.POST)
    public Result<?> updateNo(HttpServletRequest request, HttpServletResponse response,
                              @RequestBody HashMap<String, Object> map) {
        try {
            String sampleNo = map.get("sampleNo").toString();//新输入的sampleNo
            Long count = syDpsSySampleService.selectCountBySampleNo(sampleNo);
            if (count == 0) {
                String tableNumber = sampleNo.replaceFirst("YP", "JL");
                String reportNo = sampleNo.replaceFirst("YP", "BG");
                String reportingSheetNo = sampleNo.replaceFirst("YP", "BY");
                String approvalTableNo = sampleNo.replaceFirst("YP", "SP");

                QueryWrapper<SyDpsSySample> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id", map.get("id").toString());
                SyDpsSySample sample = syDpsSySampleService.getOne(queryWrapper);
                String sampleNoOld = sample.getSampleno();//查找到修改之前的sampleNo

                sample.setShenpizhuangtai(0);
                sample.setShenpiren("");
                sample.setShenpishijian("");
                sample.setQianzhangzhuangtai(0);
                sample.setZhuanfazhuangtai(0);
                sample.setJilubiaoqianzhangzhuangtai("-1");
                sample.setBaogaoqianzhangzhuangtai("-1");
                sample.setBaoyandanqianzhangzhuangtai("-1");
                sample.setShenpibiaoqianzhangzhuangtai("-1");
                sample.setSampleno(sampleNo);
                sample.setSamplenonew(sampleNo);
                sample.setTablenumbernew(tableNumber);
                sample.setReportnonew(reportNo);
                sample.setReportingsheetnonew(reportingSheetNo);
                sample.setApprovaltablenonew(approvalTableNo);
                sample.setSamplestate(1);

                int length = sampleNoOld.split("-").length;
                String[] split = sampleNoOld.split("-");//YP-2022-01-GJJ-00001
                String sampleNo2 = split[length - 1];
                String sampleNoNoSuffix = "";
                for (int i = 0; i < split.length; i++) {
                    if (i == split.length - 2) {
                        sampleNoNoSuffix = sampleNoNoSuffix + split[i];
                        break;
                    } else {
                        sampleNoNoSuffix = sampleNoNoSuffix + split[i] + "-";
                    }
                }

                int length1 = sampleNo.split("-").length;
                String[] split1 = sampleNo.split("-");//YP-2022-01-GJJ-01-00001
                String sampleNo1 = split1[length1 - 1];

                syDpsSySampleService.saveOrUpdate(sample);
                syDpsSySampleService.updateCFN(sampleNo2, sampleNo1, sampleNoNoSuffix);//sampleNoOld是不含后缀的  sampleNo2是旧的后缀  sampleNo1是新的后缀
                syDpsSySampleService.updateRM(sampleNo, reportNo, tableNumber, sampleNoOld);
                syDpsSySampleService.updateRS(sampleNo, reportNo, sampleNoOld);

                List<String> list = syDpsSySampleService.selectTiNoList(sampleNoOld);
                for (int i = 0; i < list.size(); i++) {
                    syDpsSySampleService.updateTable(list.get(i), sampleNo, reportNo, tableNumber, sampleNoOld);
                }

                syDpsSySampleService.updateNo1(sampleNo, reportNo, tableNumber, reportingSheetNo, approvalTableNo, sampleNo, reportNo, tableNumber, reportingSheetNo, approvalTableNo, sampleNoOld);
                syDpsSySampleService.updateNo2("dps_jc_shebei_shiyongjilu", sampleNo, sampleNoOld);
                syDpsSySampleService.updateNo2("sy_dps_sy_SamplePic", sampleNo, sampleNoOld);
                syDpsSySampleService.updateNo2("sy_dps_sy_SampleQRCode", sampleNo, sampleNoOld);
                syDpsSySampleService.updateNo2("sy_dps_sy_Sample_WT", sampleNo, sampleNoOld);

                syDpsSySampleService.deleteNo3("dps_sy_qianzhangrizhi", sample.getId());//未添加表
                syDpsSySampleService.deleteNo3("dps_sy_shenpirizhi", sample.getId());
                syDpsSySampleService.deleteNo3("dps_sy_qianzhangliucheng", sample.getId());

                return Result.OK();
            } else {
                return Result.error("编号重复!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.error("出现异常!");
        }
    }


    /**
     * 表格保存接口
     *
     * @param
     * @return
     */
    @PostMapping("/save")
    public Result<?> save(@RequestBody HashMap map) {

        syDpsSySampleService.insert(map);
        return Result.OK();

    }


    /**
     * 保存接口
     *
     * @param map
     * @return
     */
    @PostMapping("/saveOrUpdate")
    public Result<?> saveOrUpdate(@RequestBody HashMap map) {

        syDpsSySampleService.add(map);
        return Result.OK();

    }


    /**
     * 退回样品
     *
     * @param request
     * @param response
     * @param sampleNo
     * @param xcjc
     * @return
     */
    @GetMapping("/returnSample")
    public Result<?> returnSample(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam(value = "sampleNo", required = false) String sampleNo,
                                  @RequestParam(value = "xcjc", required = false) String xcjc) {
        syDpsSySampleService.returnSample(sampleNo, xcjc);
        return Result.OK();
    }


    @GetMapping("/getSList/{id}")
    public Result<?> getSList(@PathVariable String id) {
        Map map = syDpsSySampleService.getSList(id);
        return Result.OK(map);
    }

    @GetMapping("/getBgSList/{id}")
    public Result<?> getBgSList(@PathVariable String id) {
        Map map = syDpsSySampleService.getBgSList(id);
        return Result.OK(map);
    }

//    /**
//     * 试验-查询接口
//     *
//     * @return
//     */
//    @GetMapping(value = "/grid")
//    public Result<?> grid(HttpServletRequest request, HttpServletResponse response,
//                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
//                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
//                          @RequestParam(value = "orgCode", required = false) String orgCode,
//                          @RequestParam(value = "titCode", required = false) String titCode,
//                          @RequestParam(value = "sampleState", required = false) String sampleState,
//                          @RequestParam(value = "sampleNo", required = false) String sampleNo,
//                          @RequestParam(value = "sampleName", required = false) String sampleName,
//                          @RequestParam(value = "sampleGcbw", required = false) String sampleGcbw,
//                          @RequestParam(value = "titType", required = false) String titType,
//                          @RequestParam(value = "sampleDate", required = false) String sampleDate,
//                          @RequestParam(value = "reportNo", required = false) String reportNo,
//                          @RequestParam(value = "tiNo", required = false) String tiNo,
//                          @RequestParam(value = "lookself", required = false) Boolean lookself,
//                          @RequestParam(value = "signature", required = false) String signature,
//                          @RequestParam(value = "shenpizhuangtai", required = false) String shenpizhuangtai,
//                          @RequestParam(value = "qianzhangzhuangtai", required = false) String qianzhangzhuangtai,
//                          @RequestParam(value = "lq", required = false) String lq) {
//        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
//        String userName = loginUser.getUsername();
//        IPage list = syDpsSySampleService.getGrid(pageNo, pageSize, orgCode, titCode, sampleState, sampleNo, sampleName, sampleGcbw,
//                titType, sampleDate, reportNo, tiNo, userName, lookself, signature,
//                shenpizhuangtai, qianzhangzhuangtai, lq);
//        return Result.OK(list);
//    }


    /**
     * 试验-查询接口
     *
     * @return
     */
    @GetMapping(value = "/grid")
    public Result<?> grid(SyRequest syRequest) {

        IPage<SyResponse> list = syDpsSySampleService.getGrids(syRequest);
        return Result.OK(list);
    }

    /**
     * 原材记录页面编辑数据，根据sample表的id查询数据
     *
     * @return
     */
    @GetMapping("/getSampleById")
    public Result<?> getSampleById(@RequestParam String id) {
        Map<String, Object> map = syDpsSySampleService.getSampleById(id);
        return Result.OK(map);
    }


    /**
     * 试验-提交审核按钮接口
     *
     * @param request
     * @param response
     * @param id
     * @return
     */
    @GetMapping(value = "/over/{id}")
    public Result<?> over(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id, @RequestParam Map<String, Object> map) {

        try {
            String type = map.get("type").toString();
//            ObjectMapper objectMapper = new ObjectMapper();
//            Map<String, String> personMap = objectMapper.readValue(map.get("personMap").toString(), Map.class);
            return Result.OK(syDpsSySampleService.approval(request, response, id, type));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}")
    public Result<?> get(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) {
        try {
            syDpsSySampleService.getSampleId(id);
            return Result.OK();
        } catch (Exception ex) {
            return Result.error("error");
        }
    }

    @GetMapping(value = "/del/{id}")
    public Result<?> del(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) {
        try {
            syDpsSySampleService.delSample(id);
            return Result.OK();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.error("出现异常,删除失败!");
        }
    }


    public String getYpxx(String ypxx, SyDpsSySample syDpsSySample) throws Exception {
        String xx = "";
        if (StringUtil.isNotEmpty(ypxx)) {
            String[] a = ypxx.split("\\|");
            for (int i = 0; i < a.length; i++) {
                String[] b = a[i].split(",");
                Method method = syDpsSySample.getClass().getMethod("get" + b[1].substring(0, 1).toUpperCase() + b[1].substring(1));
                Object field = method.invoke(syDpsSySample);
                if (StringUtil.isNotEmpty(String.valueOf(field))) {
                    xx += b[0] + ":" + field + ";";
                } else {
                    xx += b[0] + ":/;";
                }
            }
        }
        return xx;
    }

    /**
     * 级配类型
     */
    @RequestMapping(value = "/searchOrderby", method = RequestMethod.POST)
    public Result<?> jipeimc(HttpServletRequest request, HttpServletResponse response,
                             @RequestBody HashMap<String, Object> map) {
        String biaoming = map.get("biaoming").toString();
        String orderby = map.get("orderby").toString();
        JSONArray json = JSONArray.fromObject(map.get("Arr"));
        List<Map<String, Object>> data = syDpsSySampleService.get2(biaoming, json, orderby);
        return Result.OK(data);
    }

    @RequestMapping(value = "/searchGroupby", method = RequestMethod.POST)
    public Result<?> hunheliaomc(HttpServletRequest request, HttpServletResponse response,
                                 @RequestBody HashMap<String, Object> map) {
        String biaoming = map.get("biaoming").toString();
        String orderby = map.get("orderby").toString();
        String GROUPBY = map.get("groupby").toString();
        JSONArray json = JSONArray.fromObject(map.get("Arr"));
        List<Map<String, Object>> data = syDpsSySampleService.get1(biaoming, json, orderby, GROUPBY);
        return Result.OK(data);
    }

    // 新增试验项目
    @RequestMapping(value = "/get/{table}", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> getTable(HttpServletRequest request, HttpServletResponse response,
                              @PathVariable("table") String table) {
        List<Map<String, Object>> result = syDpsSySampleService.getTable(table, request);
        return Result.OK(result);
    }

    @RequestMapping(value = "/searchOneReturn", method = RequestMethod.POST)
    public Result<?> search(HttpServletRequest request, HttpServletResponse response,
                            @RequestBody HashMap<String, Object> map) {
        String biaoming = (String) map.get("biaoming");
        JSONArray json = JSONArray.fromObject(map.get("Arr"));
        Map<String, Object> result = syDpsSySampleService.searchOneReturn(biaoming, json);
        return Result.OK(result);
    }

    //提取cbr读数数据
    @RequestMapping(value = "/cbrdsget", method = RequestMethod.GET)
    public Result<?> cbrdsget(HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> result = syDpsSySampleService.dushuget(request);
        return Result.OK(result);
    }

    @PostMapping("/getSyjData")
    public Result<?> getSyjData(@RequestBody HashMap<String, Object> map) {
        Object obj = syDpsSySampleService.getSyjData(map);
        return Result.OK(obj);
    }

    @RequestMapping(value = "/tqdata", method = RequestMethod.GET)
    public Result<?> grid(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "titCode", required = false) String titCode,
                          @RequestParam(value = "sampleNo", required = false) String sampleNo) {
        Map<String, Object> result = syDpsSySampleService.tqdata(titCode, sampleNo);
        return Result.OK(result);
    }

    /**
     * 终端获取试验任务
     */
    @GetMapping("/getSyRenWu")
    public Result<?> getSyRenWu(@RequestParam String orgCode) {
        List<Map> list = syDpsSySampleService.getSyRenWu(orgCode);
        return Result.OK(list);
    }

    @RequestMapping(value = "/type/listByCode", method = RequestMethod.GET)
    public Result<?> getTypeGroupByCode(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");
        return Result.OK(syDpsSySampleService.getTypeListByCode(code));
    }


    @ApiOperation(value = "配合比数据同步接口", notes = "配合比数据同步接口")
    @PostMapping("/searchPbhData")
    public Result<?> searchPhbData(@RequestBody Map map) {
        IPage<Map> data = syDpsSySampleService.searchPhbData(map);
        return Result.OK(data);
    }
    //提取监理原材进场数据
    @RequestMapping(value = "/ycjcExtract", method = RequestMethod.GET)
    public Result<?> ycjcExtract(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "wtdbh", required = false) String wtdbh) {
        Map data = syDpsSySampleService.ycjcExtract(wtdbh);
        return Result.OK(data);
    }
    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> fileUpload(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam("file") MultipartFile file) {
        UUID uuid = UUID.randomUUID();
        try {
            FileUtil.upFile(file.getInputStream(), uuid + ".jpg",
                    request.getSession().getServletContext().getRealPath("/") + "\\file" + "\\");
            return  Result.OK("file/" + uuid + ".jpg");
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("");
        }
    }

}
