package com.trtm.iot.devicemixpilehistorydatapart.controller;

import java.text.DecimalFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.mixpileanalysis.entity.MixpileAnalysis;
import com.trtm.iot.mixpileanalysis.service.IMixpileAnalysisService;
import com.trtm.iot.mixpileshebeicfg.entity.MixpileShebeicfg;
import com.trtm.iot.mixpileshebeicfg.service.IMixpileShebeicfgService;
import com.xkcoding.http.util.StringUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.iot.devicemixpilehistorydatapart.entity.DeviceMixpileHistorydataPart;
import com.trtm.iot.devicemixpilehistorydatapart.service.IDeviceMixpileHistorydataPartService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

import static org.jeecg.modules.online.cgform.util.b.e;
import static org.jeecg.modules.online.cgform.util.b.w;

/**
 * @Description: 水泥搅拌桩成桩记录每段数据信息表
 * @Author: jeecg-boot
 * @Date: 2021-11-19
 * @Version: V1.0
 */
@Api(tags = "水泥搅拌桩成桩记录每段数据信息表")
@RestController
@RequestMapping("/devicemixpilehistorydatapart/deviceMixpileHistorydataPart")
@Slf4j
public class DeviceMixpileHistorydataPartController extends JeecgController<DeviceMixpileHistorydataPart, IDeviceMixpileHistorydataPartService> {
    @Autowired
    private IDeviceMixpileHistorydataPartService deviceMixpileHistorydataPartService;
    @Autowired
    private IMixpileShebeicfgService mixpileShebeicfgService;
    @Autowired
    private IMixpileAnalysisService mixpileAnalysisService;

    /**
     * 分页列表查询
     *
     * @param deviceMixpileHistorydataPart
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "水泥搅拌桩成桩记录每段数据信息表-分页列表查询")
    @ApiOperation(value = "水泥搅拌桩成桩记录每段数据信息表-分页列表查询", notes = "水泥搅拌桩成桩记录每段数据信息表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DeviceMixpileHistorydataPart deviceMixpileHistorydataPart,String uuids,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        if(deviceMixpileHistorydataPart.getPartEndtime() != null){
            String partEndtime = deviceMixpileHistorydataPart.getPartEndtime();
            int i = partEndtime.indexOf(" ");
            String substring = partEndtime.substring(0,i);
            deviceMixpileHistorydataPart.setPartEndtime("*"+substring+"*");
        }
        QueryWrapper<DeviceMixpileHistorydataPart> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataPart, req.getParameterMap());
        queryWrapper.orderByAsc("part_endtime");
        Page<DeviceMixpileHistorydataPart> page = new Page<DeviceMixpileHistorydataPart>(pageNo, pageSize);
        IPage<DeviceMixpileHistorydataPart> pageList = deviceMixpileHistorydataPartService.page(page, queryWrapper);
        List<DeviceMixpileHistorydataPart> records = pageList.getRecords();
        for (DeviceMixpileHistorydataPart record : records) {
            record.setPartDep(String.format("%.2f", Double.parseDouble(record.getPartDep())));
            record.setPartBeton(String.format("%.2f", Double.parseDouble(record.getPartBeton())));
            if (record.getPileDensity() == null){
                record.setPileDensity("1.7999999523162842");
            }
            record.setPileDensity(String.format("%.2f", Double.parseDouble(record.getPileDensity())));
            if (record.getPartSpeed().equals("inf.0")){
                record.setPartSpeed("0.0");
            }else {
                record.setPartSpeed(String.format("%.2f", Double.parseDouble(record.getPartSpeed())));
            }
            if (record.getPartX() != null){
                record.setPartX(String.format("%.2f", Double.parseDouble(record.getPartX())));
            }else {
                record.setPartX("0");
            }
            if (record.getPartY() != null){
                record.setPartY(String.format("%.2f", Double.parseDouble(record.getPartY())));
            }else {
                record.setPartY("0");
            }
        }
        return Result.OK(pageList);
    }

    /**
     * 评级方法
     * @return
     */
    public void queryPageListpj(String shebeino,String pileno,String date,String pileMileage,String pileRatio,String pileDensity,String pileDesigndep) {
        QueryWrapper<MixpileAnalysis> queryWrapper8 = new QueryWrapper<>();
        queryWrapper8.eq("shebeino",shebeino);
        queryWrapper8.eq("pileno",pileno);
        if (pileMileage != null){
            queryWrapper8.eq("pile_mileage",pileMileage);
        }else {
            queryWrapper8.likeRight("part_endtime",date);
        }
        List<MixpileAnalysis> list = mixpileAnalysisService.list(queryWrapper8);
        if (list.size() == 0){
            QueryWrapper<MixpileShebeicfg> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("shebeino",shebeino);
            MixpileShebeicfg one1 = mixpileShebeicfgService.getOne(queryWrapper2);

            if (one1 != null){
                double historydataPart = 0.0;
                if (pileMileage != null){
                    historydataPart = deviceMixpileHistorydataPartService.selecmaxpile(shebeino,pileno,pileMileage);
                }
                if (historydataPart == 0.0){
                    historydataPart = deviceMixpileHistorydataPartService.selecmaxpiles(shebeino,pileno,date);
                }

                if (historydataPart != 0.0){
                    int j = 0;
                    double mixwbfsum = 0.0;
                    double mixmfsum = 0.0;
                    MixpileAnalysis mixpileAnalysis1 = new MixpileAnalysis();

                    double v = historydataPart;
                    double i1 = 0.0;
                    for (int i = 0; i < v; i++) {
                        j = i+1;
                        MixpileAnalysis mixpileAnalysis = new MixpileAnalysis();

                        DeviceMixpileHistorydataPart one = new DeviceMixpileHistorydataPart();
                        if(pileMileage != null){
                            QueryWrapper<DeviceMixpileHistorydataPart> queryWrapper = new QueryWrapper<>();
                            queryWrapper.select("sum(part_beton) part_beton,avg(part_speed) part_speed,part_endtime");
                            queryWrapper.eq("shebeino",shebeino);
                            queryWrapper.eq("pile_no",pileno);
                            queryWrapper.eq("pile_mileage",pileMileage);
                            queryWrapper.ge("part_dep",i);
                            queryWrapper.lt("part_dep",i+1);
                            one = deviceMixpileHistorydataPartService.getOne(queryWrapper);
                            if (one == null){
                                QueryWrapper<DeviceMixpileHistorydataPart> queryWrapper1 = new QueryWrapper<>();
                                queryWrapper1.select("sum(part_beton) part_beton,avg(part_speed) part_speed,part_endtime");
                                queryWrapper1.eq("shebeino",shebeino);
                                queryWrapper1.eq("pile_no",pileno);
                                queryWrapper1.likeRight("part_endtime",date);
                                queryWrapper1.ge("part_dep",i);
                                queryWrapper1.lt("part_dep",i+1);
                                one = deviceMixpileHistorydataPartService.getOne(queryWrapper1);
                            }
                        }

                        if(one == null){
                            continue;
                        }
                        //计算Wb
                        double mixwb = mixwb(one.getPartBeton(), pileDensity, pileRatio);
                        //计算Wb分
                        double mixwbf = mixwbf(mixwb);
                        //计算M
                        double mixm = mixm(one1, one.getPartSpeed());
                        //计算M分
                        double mixmf = mixmf(mixm);
                        //去掉最后一段
                        try {
                            i1 = Double.parseDouble(pileDesigndep);
                            if (i1 >= i){
                                mixwbfsum = mixwbfsum + mixwbf;
                                mixmfsum = mixmfsum + mixmf;
                            }
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();
                        }
                        //分段记分
                        double v1 = (mixwbf * 0.6) + (mixmf * 0.4);

                        mixpileAnalysis.setShebeino(shebeino);
                        mixpileAnalysis.setPileno(pileno);
                        mixpileAnalysis.setPileMileage(pileMileage);
                        mixpileAnalysis.setPartEndtime(one.getPartEndtime());
                        mixpileAnalysis.setLamination(i+1);
                        mixpileAnalysis.setMixpileWb(mixwb);
                        // 这三个设备不需要分数
//                        if (!shebeino.equals("2024032003") && !shebeino.equals("2024032002") && !shebeino.equals("2024032001")){
//                            mixpileAnalysis.setMixpileWbscore(mixwbf);
//                            mixpileAnalysis.setMixpileM(mixm);
//                            mixpileAnalysis.setMixpileMscore(mixmf);
//                            mixpileAnalysis.setSegmentedScoring(v1);
//                        }
                        mixpileAnalysis.setMixpileWbscore(mixwbf);
                        mixpileAnalysis.setMixpileM(mixm);
                        mixpileAnalysis.setMixpileMscore(mixmf);
                        mixpileAnalysis.setSegmentedScoring(v1);
                        mixpileAnalysis.setStuts(0);
                        mixpileAnalysisService.save(mixpileAnalysis);

                        mixpileAnalysis1.setPartEndtime(one.getPartEndtime());
                    }
//                    // 这三个设备不需要分数
//                    if (!shebeino.equals("2024032003") && !shebeino.equals("2024032002") && !shebeino.equals("2024032001")){
//
//                    }
                    mixpileAnalysis1.setShebeino(shebeino);
                    mixpileAnalysis1.setPileno(pileno);
                    mixpileAnalysis1.setPileMileage(pileMileage);
                    if (mixwbfsum > 0){
                        mixwbfsum = mixwbfsum / i1;
                    }
                    mixpileAnalysis1.setMixpileWbscore(mixwbfsum);
                    if (mixmfsum > 0){
                        mixmfsum = mixmfsum / i1;
                    }
                    mixpileAnalysis1.setMixpileMscore(mixmfsum);

                    double v2 = (mixwbfsum * 0.6) + (mixmfsum * 0.4);
                    mixpileAnalysis1.setSegmentedScoring(v2);
                    if (mixwbfsum >= 90){
                        mixpileAnalysis1.setHege("A");
                    }else if (mixwbfsum >= 70){
                        mixpileAnalysis1.setHege("B");
                    }else{
                        mixpileAnalysis1.setHege("C");
                    }

                    if (v2 >= 90){
                        mixpileAnalysis1.setMination("A");
                    }else if (v2 >= 70){
                        mixpileAnalysis1.setMination("B");
                    }else{
                        mixpileAnalysis1.setMination("C");
                    }
                    mixpileAnalysis1.setStuts(1);
                    mixpileAnalysisService.save(mixpileAnalysis1);
                }
            }
        }

    }
    //计算Wb
    public double mixwb(String partBeton, String pileDensity, String pileRatio) {
        double w = Double.parseDouble(partBeton);
        double g = Double.parseDouble(pileDensity);
        double a = Double.parseDouble(pileRatio);
        if (a > 1){
            a = a / 100;
        }
        double wb = (w * g)/(1 + a);
        return wb;
    }
    //计算Wb分
    public double mixwbf(double mixwb) {
        double mixwbfs = 0.0;
        if (mixwb >= 55){
            mixwbfs = 100.0;
        }else if (41.25 >= mixwb){
            mixwbfs = 0.0;
        }else {
            double v1 =100/(55 - 41.25);
            mixwbfs = (mixwb - 41.25)* v1;
        }
        return mixwbfs;
    }
    //计算M
    public double mixm(MixpileShebeicfg one1,String partSpeed) {
        double angle = Math.toRadians(one1.getMixpileB()); // 将角度转换为弧度
        double cosB = Math.cos(angle);//cosB

        double v = Double.parseDouble(partSpeed);
        int z = 0; // ∑Z
        for (int i = 1; i <= one1.getMixpileZ(); i++) {
            z += i; // 将i添加到z上
        }

        double m = (one1.getMixpileH() * cosB * z * one1.getMixpileN()) / v;
        return m;
    }
    //计算Wb分
    public double mixmf(double mixm) {
        double mixmfs = 0.0;
        if (mixm >= 20){
            mixmfs = 100.0;
        }else if (mixm >= 15){
            double v1 =20.0 /(20 - 15);
            mixmfs = 80 + v1 * (mixm - 15);
        }else if (mixm >= 12){
            double v1 =10.0 /(15 - 12);
            mixmfs = 70 + v1 * (mixm - 15);
        }else if (mixm >= 8){
            double v1 =20.0 /(12 - 8);
            mixmfs = 50 + v1 * (mixm - 8);
        }else if (mixm >= 0){
            double v1 =50.0 /8;
            mixmfs = 0 + v1 * (mixm - 0);
        }
        return mixmfs;
    }

    /**
     * 添加
     *
     * @param deviceMixpileHistorydataPart
     * @return
     */
    @AutoLog(value = "水泥搅拌桩成桩记录每段数据信息表-添加")
    @ApiOperation(value = "水泥搅拌桩成桩记录每段数据信息表-添加", notes = "水泥搅拌桩成桩记录每段数据信息表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DeviceMixpileHistorydataPart deviceMixpileHistorydataPart) {
        deviceMixpileHistorydataPartService.save(deviceMixpileHistorydataPart);
        return Result.OK("添加成功！");
    }

    /**
     * 添加
     *
     * @param deviceMixpileHistorydataPart
     * @return
     */
    @AutoLog(value = "水泥搅拌桩成桩记录每段数据信息表-数据上传")
    @ApiOperation(value = "水泥搅拌桩成桩记录每段数据信息表-数据上传", notes = "水泥搅拌桩成桩记录每段数据信息表-数据上传")
    @PostMapping(value = "/addOther")
    public Result<?> addOther(@RequestBody DeviceMixpileHistorydataPart deviceMixpileHistorydataPart) {
        if (StringUtil.isNotEmpty(deviceMixpileHistorydataPart.getShebeino()) && StringUtil.isNotEmpty(deviceMixpileHistorydataPart.getPileNo())) {
            deviceMixpileHistorydataPartService.save(deviceMixpileHistorydataPart);
            return Result.OK("添加成功！");
        } else {
            return Result.error("软基成桩记录上传失败！请填入设备编号和桩号");
        }

    }


    /**
     * 编辑
     *
     * @param deviceMixpileHistorydataPart
     * @return
     */
    @AutoLog(value = "水泥搅拌桩成桩记录每段数据信息表-编辑")
    @ApiOperation(value = "水泥搅拌桩成桩记录每段数据信息表-编辑", notes = "水泥搅拌桩成桩记录每段数据信息表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody DeviceMixpileHistorydataPart deviceMixpileHistorydataPart) {
        deviceMixpileHistorydataPartService.updateById(deviceMixpileHistorydataPart);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "水泥搅拌桩成桩记录每段数据信息表-通过id删除")
    @ApiOperation(value = "水泥搅拌桩成桩记录每段数据信息表-通过id删除", notes = "水泥搅拌桩成桩记录每段数据信息表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        deviceMixpileHistorydataPartService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "水泥搅拌桩成桩记录每段数据信息表-批量删除")
    @ApiOperation(value = "水泥搅拌桩成桩记录每段数据信息表-批量删除", notes = "水泥搅拌桩成桩记录每段数据信息表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.deviceMixpileHistorydataPartService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "水泥搅拌桩成桩记录每段数据信息表-通过id查询")
    @ApiOperation(value = "水泥搅拌桩成桩记录每段数据信息表-通过id查询", notes = "水泥搅拌桩成桩记录每段数据信息表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        DeviceMixpileHistorydataPart deviceMixpileHistorydataPart = deviceMixpileHistorydataPartService.getById(id);
        if (deviceMixpileHistorydataPart == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(deviceMixpileHistorydataPart);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param deviceMixpileHistorydataPart
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceMixpileHistorydataPart deviceMixpileHistorydataPart) {
        return super.exportXls(request, deviceMixpileHistorydataPart, DeviceMixpileHistorydataPart.class, "水泥搅拌桩成桩记录每段数据信息表");
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
        return super.importExcel(request, response, DeviceMixpileHistorydataPart.class);
    }

    /**
     * 水泥搅拌桩成桩记录每段数据信息过程值
     *
     * @param deviceMixpileHistorydataOne
     * @return
     */
    @AutoLog(value = "水泥搅拌桩成桩记录每段数据信息过程值")
    @ApiOperation(value = "水泥搅拌桩成桩记录每段数据信息过程值", notes = "水泥搅拌桩成桩记录每段数据信息过程值")
    @GetMapping(value = "/getProcess")
    public Result<?> getProcess(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne) {
        QueryWrapper<DeviceMixpileHistorydataPart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shebeino", deviceMixpileHistorydataOne.getShebeino());
        queryWrapper.eq("pile_no", deviceMixpileHistorydataOne.getPileNo());
        List<DeviceMixpileHistorydataPart> list = deviceMixpileHistorydataPartService.list(queryWrapper);

        Map<Object, Map<Object, Object>> mapList = new HashMap();
        Map mapX = new LinkedHashMap<>();
        Map mapY = new LinkedHashMap<>();
        for (DeviceMixpileHistorydataPart one : list) {
            mapX.put(one.getDatatime(), one.getPartX());
            mapY.put(one.getDatatime(), one.getPartY());
        }
        mapList.put("x度", mapX);
        mapList.put("y度", mapY);
        return Result.OK(mapList);
    }

    /**
     * 水泥搅拌桩成桩记录每段数据信息过程值
     *
     * @param deviceMixpileHistorydataOne
     * @return
     */
    @AutoLog(value = "水泥搅拌桩成桩记录每段数据信息过程值")
    @ApiOperation(value = "水泥搅拌桩成桩记录每段数据信息过程值", notes = "水泥搅拌桩成桩记录每段数据信息过程值")
    @GetMapping(value = "/getDepProcess")
    public Result<?> getDepProcess(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne) {
        QueryWrapper<DeviceMixpileHistorydataPart> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("shebeino", deviceMixpileHistorydataOne.getShebeino());
        queryWrapper1.eq("pile_no", deviceMixpileHistorydataOne.getPileNo());
        queryWrapper1.eq("part_state", "1");
        queryWrapper1.orderByAsc("part_pilec");
        List<DeviceMixpileHistorydataPart> list1 = deviceMixpileHistorydataPartService.list(queryWrapper1);

        QueryWrapper<DeviceMixpileHistorydataPart> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("shebeino", deviceMixpileHistorydataOne.getShebeino());
        queryWrapper2.eq("pile_no", deviceMixpileHistorydataOne.getPileNo());
        queryWrapper2.eq("part_state", "2");
        queryWrapper2.orderByDesc("part_pilec");
        List<DeviceMixpileHistorydataPart> list2 = deviceMixpileHistorydataPartService.list(queryWrapper2);

        Map<Object, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < list1.size(); i++) {
            String partBeton1 = list1.get(i).getPartBeton();
            String partBeton2 = list2.get(i).getPartBeton();
            double partBeton = Double.parseDouble(partBeton1) + Double.parseDouble(partBeton2);
            DecimalFormat df = new DecimalFormat("0.0000");
            map.put("深度" + (i + 1) + "米", df.format(partBeton));
        }
        return Result.OK(map);
    }
}
