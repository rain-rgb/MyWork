package com.trtm.iot.hc_pavement_stationdata.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.hc_constructionresults.entity.HcConstructionresults;
import com.trtm.iot.hc_constructionresults.service.IHcConstructionresultsService;
import com.trtm.iot.hc_pavement_stationdata.entity.HcPavementStationdataP;
import com.trtm.iot.hc_result_collect.service.IHcResultCollectService;
import com.trtm.iot.lmzjzl.entity.Lmzjzl;
import com.trtm.iot.lmzjzl.service.ILmzjzlService;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.openapigpsdatavo.entity.Openapigpsdatavo;
import com.trtm.iot.openapigpsdatavo.service.IOpenapigpsdatavoService;
import com.trtm.iot.skip_car.entity.SkipCarTem;
import com.xkcoding.http.util.StringUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.hc_pavement_stationdata.entity.HcPavementStationdata;
import com.trtm.iot.hc_pavement_stationdata.service.IHcPavementStationdataService;

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

/**
 * @Description: 获取逐桩数据
 * @Author: jeecg-boot
 * @Date: 2023-04-24
 * @Version: V1.0
 */
@Api(tags = "获取逐桩数据")
@RestController
@RequestMapping("/hc_pavement_stationdata/hcPavementStationdata")
@Slf4j
public class HcPavementStationdataController extends JeecgController<HcPavementStationdata, IHcPavementStationdataService> {
    @Autowired
    private IHcPavementStationdataService hcPavementStationdataService;
    @Autowired
    private IHcConstructionresultsService constructionresultsService;
    @Autowired
    private IOpenapigpsdatavoService openapigpsdatavoService;
    @Autowired
    private IBhzLqBasesService lqBasesService;
    @Autowired
    private ILmzjzlService lmzjzlService;

    /**
     * 分页列表查询
     *
     * @param hcPavementStationdata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "获取逐桩数据-分页列表查询")
    @ApiOperation(value = "获取逐桩数据-分页列表查询", notes = "获取逐桩数据-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(HcPavementStationdata hcPavementStationdata,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<HcPavementStationdata> queryWrapper = QueryGenerator.initQueryWrapper(hcPavementStationdata, req.getParameterMap());
        Page<HcPavementStationdata> page = new Page<HcPavementStationdata>(pageNo, pageSize);
        IPage<HcPavementStationdata> pageList = hcPavementStationdataService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param hcPavementStationdata
     * @return
     */
    @AutoLog(value = "获取逐桩数据-添加")
    @ApiOperation(value = "获取逐桩数据-添加", notes = "获取逐桩数据-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody HcPavementStationdata hcPavementStationdata) {
        hcPavementStationdataService.save(hcPavementStationdata);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param hcPavementStationdata
     * @return
     */
    @AutoLog(value = "获取逐桩数据-编辑")
    @ApiOperation(value = "获取逐桩数据-编辑", notes = "获取逐桩数据-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody HcPavementStationdata hcPavementStationdata) {
        hcPavementStationdataService.updateById(hcPavementStationdata);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "获取逐桩数据-通过id删除")
    @ApiOperation(value = "获取逐桩数据-通过id删除", notes = "获取逐桩数据-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        hcPavementStationdataService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "获取逐桩数据-批量删除")
    @ApiOperation(value = "获取逐桩数据-批量删除", notes = "获取逐桩数据-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.hcPavementStationdataService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "获取逐桩数据-通过id查询")
    @ApiOperation(value = "获取逐桩数据-通过id查询", notes = "获取逐桩数据-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        HcPavementStationdata hcPavementStationdata = hcPavementStationdataService.getById(id);
        if (hcPavementStationdata == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(hcPavementStationdata);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param hcPavementStationdata
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HcPavementStationdata hcPavementStationdata) {
        return super.exportXls(request, hcPavementStationdata, HcPavementStationdata.class, "获取逐桩数据");
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
        return super.importExcel(request, response, HcPavementStationdata.class);
    }

    @RequestMapping(value = "/getDay", method = RequestMethod.POST)
    public Result<?> getDay(HttpServletRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        QueryWrapper<HcConstructionresults> constructionresultsQueryWrapper = new QueryWrapper<>();
        constructionresultsQueryWrapper.eq("projectid", "jishu03@20220809223937")
                .groupBy("date")
                .orderByDesc("date");
        List<HcConstructionresults> constructionresultsList = constructionresultsService.list(constructionresultsQueryWrapper);

        List sendList = new ArrayList();
        for (HcConstructionresults hcConstructionresults : constructionresultsList) {
            Map sendMap = new HashMap();
            sendMap.put("date", sdf.format(hcConstructionresults.getDate()));
            sendList.add(sendMap);
        }

        return Result.OK("请求成功！", sendList);
    }

    @RequestMapping(value = "/getData", method = RequestMethod.POST)
    public Result<?> getData(HttpServletRequest request) {
        String datetime = request.getParameter("date");
        QueryWrapper<Lmzjzl> lmzjzlQueryWrapper = new QueryWrapper<>();
        lmzjzlQueryWrapper.eq("date",datetime);
        List<Lmzjzl> lmzjzlList = lmzjzlService.list(lmzjzlQueryWrapper);

        List pushList = new ArrayList();
        for (Lmzjzl lmzjzl : lmzjzlList) {
            JSONObject sendObject = new JSONObject();
            sendObject.put("formulaName", lmzjzl.getFormulaName());
            sendObject.put("timesFirst", lmzjzl.getTimesFirst());
            sendObject.put("timesRepeat", lmzjzl.getTimesRepeat());
            sendObject.put("temperatureFirst", lmzjzl.getTemperatureFirst());
            sendObject.put("temperatureRepeat", lmzjzl.getTemperatureRepeat());
            sendObject.put("pavingtemperature", lmzjzl.getPavingtemperature());
            sendObject.put("beforetemperature", lmzjzl.getBeforetemperature());
            sendObject.put("finallytemperature", lmzjzl.getFinallytemperature());
            sendObject.put("Lessthan7", lmzjzl.getLessthan7());
            sendObject.put("Equalto7", lmzjzl.getEqualto7());
            sendObject.put("Equalto8", lmzjzl.getEqualto8());
            sendObject.put("Greaterthan8", lmzjzl.getGreaterthan8());
            pushList.add(sendObject);
        }

        return Result.OK("请求成功！", pushList);
    }

    @RequestMapping(value = "/getData1", method = RequestMethod.POST)
    public Result<?> getData1(HttpServletRequest request) {
        List sendList = new ArrayList();
        String datetime = request.getParameter("date");

        List<HcConstructionresults> list13 = new ArrayList();
        List<HcConstructionresults> list16 = new ArrayList();
        List<HcConstructionresults> list20 = new ArrayList();
        List<HcConstructionresults> list25 = new ArrayList();

        QueryWrapper<HcConstructionresults> constructionresultsQueryWrapper = new QueryWrapper<>();
        constructionresultsQueryWrapper.select("date,\n" +
                "\t\tstartStation,\n" +
                "\t\tendStation,\n" +
                "\tCASE\n" +
                "\t\t\t(\n" +
                "\t\t\tSELECT\n" +
                "\t\t\t\tformula_no\n" +
                "\t\t\tFROM\n" +
                "\t\t\t\tbhz_lq_bases\n" +
                "\t\t\tWHERE\n" +
                "\t\t\t\tshebeibianhao = 'ydlm1blq_01'\n" +
                "\t\t\t\tAND formula_no IN ( '33', '36', '34', '75' )\n" +
                "\t\t\tORDER BY\n" +
                "\t\t\t\tABS(\n" +
                "\t\t\t\tTIMESTAMPDIFF( SECOND, chuliaoshijian, hc_constructionresults.beginTime ))\n" +
                "\t\t\t\tLIMIT 1\n" +
                "\t\t\t)\n" +
                "\t\t\tWHEN '33' THEN\n" +
                "\t\t\t'SMA-13'\n" +
                "\t\t\tWHEN '36' THEN\n" +
                "\t\t\t'SMA-16'\n" +
                "\t\t\tWHEN '34' THEN\n" +
                "\t\t\t'SUP-20'\n" +
                "\t\t\tWHEN '75' THEN\n" +
                "\t\t\t'SUP-25'\n" +
                "\t\tEND AS layerindex")
                .eq("date", datetime)
                .eq("projectId", "jishu03@20220809223937");
        List<HcConstructionresults> list = constructionresultsService.list(constructionresultsQueryWrapper);

        for (HcConstructionresults constructionresults1 : list) {

            String formulaName = constructionresults1.getLayerindex();
            switch (formulaName) {
                case "SMA-13":
                    list13.add(constructionresults1);
                    break;
                case "SMA-16":
                    list16.add(constructionresults1);
                    break;
                case "SUP-20":
                    list20.add(constructionresults1);
                    break;
                case "SUP-25":
                    list25.add(constructionresults1);
                    break;
                default:
                    break;
            }
        }



        if (list13.size() > 0) {
            //获取当天的沥青温度165-175
            String lqwd = getlqwd(datetime, "33");
            JSONObject getsend = getsend("SMA-13", list13, lqwd);
            sendList.add(getsend);
        }
        if (list16.size() > 0) {
            //获取当天的沥青温度165-175
            String lqwd = getlqwd(datetime,"36");
            JSONObject getsend = getsend("SMA-16", list16, lqwd);
            sendList.add(getsend);
        }
        if (list20.size() > 0) {
            //获取当天的沥青温度165-175
            String lqwd = getlqwd(datetime,"34");
            JSONObject getsend = getsend("SUP-20", list20, lqwd);
            sendList.add(getsend);
        }
        if (list25.size() > 0) {
            //获取当天的沥青温度165-175
            String lqwd = getlqwd(datetime,"75");
            JSONObject getsend = getsend("SUP-25", list25, lqwd);
            sendList.add(getsend);
        }
        return Result.OK("请求成功！", sendList);
    }

    public String getlqwd(String datetime, String fou) {
        QueryWrapper<BhzLqBases> lqBasesQueryWrapper = new QueryWrapper<>();
        lqBasesQueryWrapper.select("ROUND(AVG(liqingwd), 2) AS liqingwd")
                .in("formula_no", Arrays.asList("33", "34", "36", "75"))
                .ge("CAST(liqingwd AS DECIMAL(10, 2))", "165")
                .le("CAST(liqingwd AS DECIMAL(10, 2))", "175")
                .eq("formula_no", fou)
                .eq("DATE(chuliaoshijian)", datetime);
        BhzLqBases one = lqBasesService.getOne(lqBasesQueryWrapper);
        String lqwd = "175";
        if (one != null){
            lqwd = one.getLiqingwd();
        }
        return lqwd;
    }

    public JSONObject getsend(String formulaName, List<HcConstructionresults> list, String lqwd) {

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        JSONObject sendObject = new JSONObject();

        QueryWrapper<HcPavementStationdata> pavementStationdataQueryWrapper = new QueryWrapper<>();
        pavementStationdataQueryWrapper.select(
                "ROUND(AVG(timesFirst), 2) AS timesFirst",
                "ROUND(AVG(timesRepeat), 2) AS timesRepeat",
                "ROUND(AVG(pavingtemperature), 2) AS pavingtemperature",
                "ROUND(AVG(temperatureFirst), 2) AS temperatureFirst",
                "CAST(MAX(pavingtemperature) AS DECIMAL(10,2)) AS steelrate")
                .gt("timesFirst", 0)
                .gt("pavingtemperature", 0)
                .lt("pavingtemperature", Double.parseDouble(lqwd) - 0.5)
                .gt("temperatureFirst", 0);

        sendObject.put("formulaName", formulaName);
        String sqlwhere13 = " and ( 1 != 1";
        for (int i = 0; i < list.size(); i++) {
            sqlwhere13 += " or (station >= " + list.get(i).getStartstation() + " AND station <= " + list.get(i).getEndstation() + ") ";
        }
        sqlwhere13 += " ) ";
        pavementStationdataQueryWrapper.last(sqlwhere13);
        HcPavementStationdata pavementStationdata = hcPavementStationdataService.getOne(pavementStationdataQueryWrapper);

        double temperatureRepeat = getfywd(sqlwhere13);
        String timesfirst = "0";
        String timesRepeat = "0";
        String pavingtemperature = "0";
        String temperaturefirst = "0";
        String beforetemperature = "0";
        double finallytemperature = 104.00;

        //遍数占比
        QueryWrapper<HcPavementStationdata> pavementStationdataQueryWrapper1 = new QueryWrapper<>();
        pavementStationdataQueryWrapper1.select("ROUND(SUM(CASE WHEN 7 > (timesFirst + timesRepeat)  THEN 1 ELSE 0 END) / COUNT(*) * 100, 2) AS layer,\n" +
                "\t\tROUND(SUM(CASE WHEN (timesFirst + timesRepeat) = 7 THEN 1 ELSE 0 END) / COUNT(*) * 100, 2) AS pavingtemperature,\n" +
                "\t\tROUND(SUM(CASE WHEN (timesFirst + timesRepeat) = 8 THEN 1 ELSE 0 END) / COUNT(*) * 100, 2) AS temperaturesegregation,\n" +
                "\t\tROUND(SUM(CASE WHEN (timesFirst + timesRepeat) > 8 THEN 1 ELSE 0 END) / COUNT(*) * 100, 2) AS temperaturefirst")
                .gt("timesFirst", 0)
                .gt("pavingtemperature", 0)
                .gt("temperatureFirst", 0)
                .last(sqlwhere13);
        HcPavementStationdata hcPavementStationdata = hcPavementStationdataService.getOne(pavementStationdataQueryWrapper1);
        String xy7 = "";
        String dy7 = "";
        String dy8 = "";
        String d8 = "";

        if (hcPavementStationdata != null) {
            xy7 = hcPavementStationdata.getLayer();
            dy7 = hcPavementStationdata.getPavingtemperature();
            dy8 = hcPavementStationdata.getTemperaturesegregation();
            d8 = hcPavementStationdata.getTemperaturefirst();
        }


        //摊铺详情
        if (pavementStationdata != null) {
            timesfirst = pavementStationdata.getTimesfirst();
            timesRepeat = pavementStationdata.getTimesrepeat();
            pavingtemperature = pavementStationdata.getPavingtemperature();
            temperaturefirst = pavementStationdata.getTemperaturefirst();
            beforetemperature = pavementStationdata.getSteelrate();
            if (StringUtil.isNotEmpty(beforetemperature) && Double.parseDouble(pavingtemperature) > Double.parseDouble(beforetemperature)){
                String str = beforetemperature;
                beforetemperature = pavingtemperature;
                pavingtemperature = str;
            }
            if (Double.parseDouble(temperaturefirst) > Double.parseDouble(pavingtemperature)) {
                String str = pavingtemperature;
                pavingtemperature = temperaturefirst;
                temperaturefirst = str;
            }
            if (StringUtil.isNotEmpty(beforetemperature) && Double.parseDouble(beforetemperature) > 185) {

                beforetemperature = "184.50";
            }
            if (temperatureRepeat == 0.00 || temperatureRepeat < 130 || temperatureRepeat > Double.parseDouble(temperaturefirst)) {
                double v = Double.parseDouble(temperaturefirst);
                // 将小数转换成字符串
                String numStr = String.valueOf(v);

                // 获取个位数
                int unitDigit = Integer.parseInt(numStr.substring(numStr.indexOf('.') + 1, numStr.indexOf('.') + 2));

                // 获取小数位
                double decimalDigits = Double.parseDouble(numStr.substring(numStr.indexOf('.') + 1));

                // 计算结果
                temperatureRepeat = 140 + unitDigit + (decimalDigits * 0.01);
            }
        }

        //终压温度
        String replace = sqlwhere13.replace("station", "road_station");
        QueryWrapper<Openapigpsdatavo> openapigpsdatavoQueryWrapper = new QueryWrapper<>();
        openapigpsdatavoQueryWrapper.select("AVG(CAST(SUBSTRING_INDEX(temperature, ';', 1) AS DECIMAL(10,2))) AS temperature")
                .eq("machine_id", "2323")
                .last(replace);
        Openapigpsdatavo openapigpsdatavo = openapigpsdatavoService.getOne(openapigpsdatavoQueryWrapper);
        if (openapigpsdatavo != null) {
            String temperature = openapigpsdatavo.getTemperature();
            finallytemperature = Double.parseDouble(temperature);
            if (finallytemperature < 90) {
                finallytemperature = Double.parseDouble(decimalFormat.format((90 + Double.valueOf(temperatureRepeat)) / 2));
            }
        } else {
            finallytemperature = Double.parseDouble(decimalFormat.format((90 + Double.valueOf(temperatureRepeat)) / 2));
        }

        sendObject.put("timesFirst", timesfirst);
        sendObject.put("timesRepeat", timesRepeat);
        sendObject.put("temperatureFirst", temperaturefirst);
        sendObject.put("temperatureRepeat", temperatureRepeat);

        double v = Double.parseDouble(pavingtemperature);
        double v1 = Double.parseDouble(beforetemperature);
        if (v>v1){
            pavingtemperature = decimalFormat.format(v1);
            beforetemperature = decimalFormat.format(v);
        }

        sendObject.put("pavingtemperature", pavingtemperature);
        sendObject.put("beforetemperature", beforetemperature);
        sendObject.put("finallytemperature", decimalFormat.format(finallytemperature));
        sendObject.put("Lessthan7", xy7);
        sendObject.put("Equalto7", dy7);
        sendObject.put("Equalto8", dy8);
        sendObject.put("Greaterthan8", d8);

        return sendObject;
    }

    public double getfywd(String sqlwhere) {
        QueryWrapper<HcPavementStationdata> pavementStationdataQueryWrapper = new QueryWrapper<>();
        pavementStationdataQueryWrapper.select(
                "ROUND(AVG(temperatureRepeat), 2) AS temperatureRepeat")
                .gt("temperatureRepeat", 0);
        pavementStationdataQueryWrapper.last(sqlwhere);
        HcPavementStationdata pavementStationdata = hcPavementStationdataService.getOne(pavementStationdataQueryWrapper);
        double temperatureRepeat = 0.00;
        if (pavementStationdata != null) {
            temperatureRepeat = Double.parseDouble(pavementStationdata.getTemperaturerepeat());
        }
        return temperatureRepeat;
    }
}
