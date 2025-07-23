package com.trtm.iot.openapigpsdatavo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.tpny.entity.FrontDeviceRealdata;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.openapigpsdatavo.entity.Openapigpsdatavo;
import com.trtm.iot.openapigpsdatavo.service.IOpenapigpsdatavoService;

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
 * @Description: 摊铺碾压数据
 * @Author: jeecg-boot
 * @Date: 2023-04-21
 * @Version: V1.0
 */
@Api(tags = "摊铺碾压数据")
@RestController
@RequestMapping("/openapigpsdatavo/openapigpsdatavo")
@Slf4j
public class OpenapigpsdatavoController extends JeecgController<Openapigpsdatavo, IOpenapigpsdatavoService> {
    @Autowired
    private IOpenapigpsdatavoService openapigpsdatavoService;
    @Autowired
    private IBhzLqBasesService bhzLqBasesService;

    /**
     * 分页列表查询
     *
     * @param openapigpsdatavo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "摊铺碾压数据-分页列表查询")
    @ApiOperation(value = "摊铺碾压数据-分页列表查询", notes = "摊铺碾压数据-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Openapigpsdatavo openapigpsdatavo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Openapigpsdatavo> queryWrapper = QueryGenerator.initQueryWrapper(openapigpsdatavo, req.getParameterMap());
        Page<Openapigpsdatavo> page = new Page<Openapigpsdatavo>(pageNo, pageSize);
        IPage<Openapigpsdatavo> pageList = openapigpsdatavoService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param openapigpsdatavo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "摊铺碾压数据-分页列表查询")
    @ApiOperation(value = "摊铺碾压数据-分页列表查询", notes = "摊铺碾压数据-分页列表查询")
    @GetMapping(value = "/listqx")
    public Result<?> queryPageListqx(Openapigpsdatavo openapigpsdatavo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Openapigpsdatavo> queryWrapper = QueryGenerator.initQueryWrapper(openapigpsdatavo, req.getParameterMap());
        queryWrapper.orderByDesc("gps_time");
//        queryWrapper.last("limit "+ pageSize);
        Page<Openapigpsdatavo> page = new Page<Openapigpsdatavo>(pageNo, pageSize);
        IPage<Openapigpsdatavo> pageList = openapigpsdatavoService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

//    /**
//     * 分页列表曲线查询
//     *
//
//     * @return
//     */
//    @AutoLog(value = "摊铺碾压数据-分页列表查询")
//    @ApiOperation(value = "摊铺碾压数据-分页列表查询", notes = "摊铺碾压数据-分页列表查询")
//    @GetMapping(value = "/listqx")
//    public Result<?> queryPageListqx(String machineId) throws ParseException {
//        QueryWrapper<Openapigpsdatavo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("machine_id",machineId);
//        queryWrapper.orderByDesc("gps_time");
//        queryWrapper.last("limit 1");
//        Openapigpsdatavo one = openapigpsdatavoService.getOne(queryWrapper);
//
//        if(one != null){
//            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
//            QueryWrapper<Openapigpsdatavo> queryWrapper1 = new QueryWrapper<>();
//            queryWrapper1.eq("machine_id",machineId);
//            queryWrapper1.likeRight("gps_time",sdf.format(one.getGpsTime()));
//            List<Openapigpsdatavo> list = openapigpsdatavoService.list(queryWrapper1);
//            return Result.OK(list);
//        }else {
//            return Result.error("该设备暂无震动曲线");
//        }
//    }

    /**
     * 添加
     *
     * @param openapigpsdatavo
     * @return
     */
    @AutoLog(value = "摊铺碾压数据-添加")
    @ApiOperation(value = "摊铺碾压数据-添加", notes = "摊铺碾压数据-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Openapigpsdatavo openapigpsdatavo) {
        openapigpsdatavoService.save(openapigpsdatavo);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param openapigpsdatavo
     * @return
     */
    @AutoLog(value = "摊铺碾压数据-编辑")
    @ApiOperation(value = "摊铺碾压数据-编辑", notes = "摊铺碾压数据-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Openapigpsdatavo openapigpsdatavo) {
        openapigpsdatavoService.updateById(openapigpsdatavo);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "摊铺碾压数据-通过id删除")
    @ApiOperation(value = "摊铺碾压数据-通过id删除", notes = "摊铺碾压数据-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        openapigpsdatavoService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "摊铺碾压数据-批量删除")
    @ApiOperation(value = "摊铺碾压数据-批量删除", notes = "摊铺碾压数据-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.openapigpsdatavoService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "摊铺碾压数据-通过id查询")
    @ApiOperation(value = "摊铺碾压数据-通过id查询", notes = "摊铺碾压数据-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Openapigpsdatavo openapigpsdatavo = openapigpsdatavoService.getById(id);
        if (openapigpsdatavo == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(openapigpsdatavo);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param openapigpsdatavo
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Openapigpsdatavo openapigpsdatavo) {
        return super.exportXls(request, openapigpsdatavo, Openapigpsdatavo.class, "摊铺碾压数据");
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
        return super.importExcel(request, response, Openapigpsdatavo.class);
    }

    /**
     * 将度分格式的经纬度转换为ddmm.mmmmm格式
     * @param degree 分离度分格式的度部分
     * @param minute 分离度分格式的分部分
     * @return 转换后的ddmm.mmmmm格式的经纬度
     */
    public static String convertDMS2DDMM(double degree, double minute) {
        // 计算度分部分对应的十进制度数
        double decimalDegree = convertDMS2Decimal(degree, minute);
        // 将十进制度数转换为ddmm.mmmmm格式
        int dd = (int) decimalDegree;                  // 取整数部分为度数部分
        double mm = (decimalDegree - dd) * 60.0;       // 将小数部分转换为分数部分
        String result = String.format("%02d%.11f", dd, mm);
        return result;
    }

    /**
     * 将度分格式的经纬度转换为十进制度数
     * @param degree 分离度分格式的度部分
     * @param minute 分离度分格式的分部分
     * @return 十进制度数
     */
    public static double convertDMS2Decimal(double degree, double minute) {
        return degree + minute / 60.0;
    }

    @PostMapping(value = "/recvGpsData")
    public Result<?> recvGpsData(@Valid @RequestBody List<Openapigpsdatavo> list) {
        //业务操作
        try {
            for (Openapigpsdatavo openapigpsdatavo : list) {
                Double velocity = Double.parseDouble(openapigpsdatavo.getVelocity());
                openapigpsdatavo.setVelocity(String.valueOf(velocity*60));
                openapigpsdatavoService.saveOrUpdate(openapigpsdatavo);
            }
        } catch (Exception e) {
            return Result.error("接收异常！");
        }

        return Result.OK();
    }

    @PostMapping(value = "/recvGpsData1")
    public Result<?> recvGpsData1(@Valid @RequestBody List<Openapigpsdatavo> list) {
        //业务操作
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 时间格式化器
        try {
            for (Openapigpsdatavo openapigpsdatavo : list) {
                Double velocity = Double.parseDouble(openapigpsdatavo.getVelocity());
                openapigpsdatavo.setVelocity(String.valueOf(velocity*60));
                openapigpsdatavoService.saveOrUpdate(openapigpsdatavo);

                JSONObject sendDate = new JSONObject();

                String machineid = openapigpsdatavo.getMachineId();

                sendDate.set("XT",machineid);  //心跳(设备编号)
                String temperature = openapigpsdatavo.getTemperature();
                String[] split = temperature.split(";");
                String T = split[0];
                double v = Double.parseDouble(openapigpsdatavo.getVelocity());
                sendDate.set("T",T);  //温度
                sendDate.set("V",v);  //速度

                String longitude = openapigpsdatavo.getLon();
                String latitude = openapigpsdatavo.getLat();
                int longitudeDegree = (int) Double.parseDouble(longitude);// 取整数部分为度数部分
                double longitudeMinute = (Double.parseDouble(longitude) - longitudeDegree) * 60.0;  // 将小数部分转换为分数部分
                String longitudeDDMM = convertDMS2DDMM(longitudeDegree, longitudeMinute);    // 转换为ddmm.mmmmmmmmmmm格式

                int latitudeDegree = (int) Double.parseDouble(latitude);                  // 取整数部分为度数部分
                double latitudeMinute = (Double.parseDouble(latitude) - latitudeDegree) * 60.0;    // 将小数部分转换为分数部分
                String latitudeDDMM = convertDMS2DDMM(latitudeDegree, latitudeMinute);   // 转换为ddmm.mmmmmmmmmmm格式

                sendDate.set("JD",longitudeDDMM);  //经度
                sendDate.set("WD",latitudeDDMM);  //纬度
                String roadstation = openapigpsdatavo.getRoadStation();

                //取整
                int station = (int)Double.parseDouble(roadstation);
                String s = String.valueOf(station);
                int len = s.length();

                if (len > 3) {
                    // 截取后三位并进行强制类型转换
                    String endtring = s.substring(len - 3);

                    // 获取前面的字符串
                    String firstPart = s.substring(0, len - 3);

                    roadstation = "K"+firstPart+"+"+endtring;
                }else if (len == 3) {
                    roadstation = "K0"+"+"+station;
                }else {
                    roadstation = "K0+000";
                }
                sendDate.set("PILE",roadstation);  //桩号

                String gpstime = sdf.format(openapigpsdatavo.getGpsTime());

                int newOffset = 0;
                double offset = Double.parseDouble(openapigpsdatavo.getOffset());
                if (offset > 0) {
                    newOffset = 1;
                }

                QueryWrapper<BhzLqBases> bhzLqBasesQueryWrapper = new QueryWrapper<>();
                bhzLqBasesQueryWrapper.eq("shebeibianhao", "ydlm1blq_01")
                        .isNotNull("chuliaoshijian")
                        .orderByAsc("ABS(TIMESTAMPDIFF(SECOND, chuliaoshijian, '"+ gpstime+"'))")
                        .last("LIMIT 1");
                BhzLqBases one = bhzLqBasesService.getOne(bhzLqBasesQueryWrapper);
                String formulaNo = one.getFormulaNo();
                String itemText = "";
                if ("36".equals(formulaNo)){
                    itemText = "SMA-16";
                }else {
                    itemText = "SMA-13";
                }
                sendDate.set("LEFTRIGHT", newOffset);  //左右幅
                sendDate.set("surfaceCourse", itemText);  //上面层
                sendDate.set("SJ", gpstime);  //时间

                String body = HttpRequest.post("http://123.60.37.16:443/receive/qcdata/dtu")
                        .form("data",sendDate)
                        .execute()
                        .body();

                if (body.contains("success")) {
                    log.info(String.format("义东摊铺碾压推送成功!"  +"body: "+ body));
                    openapigpsdatavo.setIsdj(1);
                } else {
                    log.info(String.format("义东摊铺碾压推送失败!"  + "Json数据" + sendDate));
                    openapigpsdatavo.setIsdj(2);
                }
            }
        } catch (Exception e) {
            return Result.error("接收异常！");
        }

        return Result.OK();
    }
}
