package com.trtm.iot.devicepipepilehistorydatapart.controller;

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

import com.trtm.iot.devicepipepilehistorydataone.entity.DevicePipepileHistorydataOne;
import com.trtm.iot.devicepipepilehistorydataone.service.IDevicePipepileHistorydataOneService;
import com.trtm.iot.devicepipepilerealdata.entity.DevicePipepileRealdata;
import com.xkcoding.http.util.StringUtil;
import lombok.Data;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicepipepilehistorydatapart.entity.DevicePipepileHistorydataPart;
import com.trtm.iot.devicepipepilehistorydatapart.service.IDevicePipepileHistorydataPartService;

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
 * @Description: device_pipepile_historydata_part
 * @Author: jeecg-boot
 * @Date: 2022-07-21
 * @Version: V1.0
 */
@Api(tags = "device_pipepile_historydata_part")
@RestController
@RequestMapping("/devicepipepilehistorydatapart/devicePipepileHistorydataPart")
@Slf4j
public class DevicePipepileHistorydataPartController extends JeecgController<DevicePipepileHistorydataPart, IDevicePipepileHistorydataPartService> {
    @Autowired
    private IDevicePipepileHistorydataPartService devicePipepileHistorydataPartService;
    @Autowired
    private IDevicePipepileHistorydataOneService devicePipepileHistorydataOneService;
    @Autowired
    RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param devicePipepileHistorydataPart
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_part-分页列表查询")
    @ApiOperation(value = "device_pipepile_historydata_part-分页列表查询", notes = "device_pipepile_historydata_part-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DevicePipepileHistorydataPart devicePipepileHistorydataPart,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<DevicePipepileHistorydataPart> queryWrapper = QueryGenerator.initQueryWrapper(devicePipepileHistorydataPart, req.getParameterMap());
        Page<DevicePipepileHistorydataPart> page = new Page<DevicePipepileHistorydataPart>(pageNo, pageSize);
        IPage<DevicePipepileHistorydataPart> pageList = devicePipepileHistorydataPartService.page(page, queryWrapper);
        List<DevicePipepileHistorydataPart> records = pageList.getRecords();
        for (DevicePipepileHistorydataPart record : records) {
            record.setPartDep(String.format("%.2f", Double.parseDouble(record.getPartDep())));
            record.setPartSpeed(String.format("%.2f", Double.parseDouble(record.getPartSpeed())));
            record.setPartY(String.format("%.3f", Double.parseDouble(record.getPartY())));
            record.setPartUpress(String.format("%.2f", Double.parseDouble(record.getPartUpress())));
            record.setPartDpress(String.format("%.2f", Double.parseDouble(record.getPartDpress())));
        }
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @return
     */
    @AutoLog(value = "device_pipepile_realdata-分页列表查询")
    @ApiOperation(value = "device_pipepile_realdata-分页列表查询", notes = "device_pipepile_realdata-分页列表查询")
    @GetMapping(value = "/listqxt")
    public Result<?> queryPageListqxt(String shebeino, String pileNo) throws ParseException {
        QueryWrapper<DevicePipepileHistorydataPart> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id,shebeino,pile_no,part_pilec,part_y,part_state,part_endtime,datatime,part_ts,uuid,round(part_dep, 2) part_dep,round(part_speed, 2) part_speed,round(part_upress, 2) part_upress,round(part_dpress, 2) part_dpress");
        queryWrapper.eq("shebeino", shebeino);
        queryWrapper.eq("pile_no", pileNo);
        List<DevicePipepileHistorydataPart> list = devicePipepileHistorydataPartService.list(queryWrapper);
        Date datatime1 = null;
        for (DevicePipepileHistorydataPart l : list) {
            Date datatime = l.getDatatime();
            if (datatime1 == null) {
                l.setPartPilec("0.0");
            } else {
                long l1 = datatime.getTime() - datatime1.getTime();
                long l2 = l1 / 1000;
                l.setPartPilec(String.valueOf(l2));
            }
            datatime1 = datatime;
        }
        return Result.OK(list);
    }

    /**
     * 分页列表查询
     *
     * @param devicePipepileHistorydataPart
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_part-分页列表查询")
    @ApiOperation(value = "device_pipepile_historydata_part-分页列表查询", notes = "device_pipepile_historydata_part-分页列表查询")
    @GetMapping(value = "/listGz")
    public Result<?> queryPageListGz(DevicePipepileHistorydataPart devicePipepileHistorydataPart,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        Page<DevicePipepileHistorydataPart> lists = null;
        if (devicePipepileHistorydataPart.getShebeino() != null && devicePipepileHistorydataPart.getPileNo() != null) {
            try {
                QueryWrapper<DevicePipepileHistorydataOne> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("shebeino", devicePipepileHistorydataPart.getShebeino());
                queryWrapper1.eq("pile_no", devicePipepileHistorydataPart.getPileNo());
                DevicePipepileHistorydataOne one = devicePipepileHistorydataOneService.getOne(queryWrapper1);
                QueryWrapper<DevicePipepileHistorydataPart> queryWrapper = QueryGenerator.initQueryWrapper(devicePipepileHistorydataPart, req.getParameterMap());
                Page<DevicePipepileHistorydataPart> page = new Page<>();
                lists = devicePipepileHistorydataPartService.page(page, queryWrapper);
                List<DevicePipepileHistorydataPart> list = lists.getRecords();
//				 list = devicePipepileHistorydataPartService.list(queryWrapper);
                if (list.size() > 0) {
                    for (DevicePipepileHistorydataPart l : list) {
                        l.setPartPilec(one.getPileWorktime());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            QueryWrapper<DevicePipepileHistorydataPart> queryWrapper = QueryGenerator.initQueryWrapper(devicePipepileHistorydataPart, req.getParameterMap());
            Page<DevicePipepileHistorydataPart> page = new Page<>();
            lists = devicePipepileHistorydataPartService.page(page, queryWrapper);
            List<DevicePipepileHistorydataPart> list = lists.getRecords();
            if (list.size() > 0) {
                for (DevicePipepileHistorydataPart l : list) {
                    Date date = new Date(l.getPartTs());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String format = dateFormat.format(date);
                    l.setPartY(format);
                    l.setPartPilec("");
                }
            }
        }
        return Result.OK(lists);
    }

    /**
     * 添加
     *
     * @param devicePipepileHistorydataPart
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_part-添加")
    @ApiOperation(value = "device_pipepile_historydata_part-添加", notes = "device_pipepile_historydata_part-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DevicePipepileHistorydataPart devicePipepileHistorydataPart) {
        devicePipepileHistorydataPartService.save(devicePipepileHistorydataPart);
        return Result.OK("添加成功！");
    }

    /**
     * 添加
     *
     * @param devicePipepileHistorydataPart
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_part-添加")
    @ApiOperation(value = "device_pipepile_historydata_part-添加", notes = "device_pipepile_historydata_part-添加")
    @PostMapping(value = "/addOther")
    public Result<?> addOther(@RequestBody DevicePipepileHistorydataPart devicePipepileHistorydataPart) {
        if (StringUtil.isNotEmpty(devicePipepileHistorydataPart.getShebeino()) && StringUtil.isNotEmpty(devicePipepileHistorydataPart.getPileNo())) {
            QueryWrapper<DevicePipepileHistorydataPart> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uuid", devicePipepileHistorydataPart.getUuid());
            DevicePipepileHistorydataPart one = devicePipepileHistorydataPartService.getOne(queryWrapper);
            if (one != null) {
                devicePipepileHistorydataPart.setId(one.getId());
                devicePipepileHistorydataPartService.updateById(devicePipepileHistorydataPart);
            } else {
                devicePipepileHistorydataPartService.save(devicePipepileHistorydataPart);
            }
            return Result.OK("添加成功！");
        } else {
            return Result.error("记录上传失败！请填入设备编号和桩号");
        }
    }

    /**
     * 编辑
     *
     * @param devicePipepileHistorydataPart
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_part-编辑")
    @ApiOperation(value = "device_pipepile_historydata_part-编辑", notes = "device_pipepile_historydata_part-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody DevicePipepileHistorydataPart devicePipepileHistorydataPart) {
        devicePipepileHistorydataPartService.updateById(devicePipepileHistorydataPart);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_part-通过id删除")
    @ApiOperation(value = "device_pipepile_historydata_part-通过id删除", notes = "device_pipepile_historydata_part-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        devicePipepileHistorydataPartService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_part-批量删除")
    @ApiOperation(value = "device_pipepile_historydata_part-批量删除", notes = "device_pipepile_historydata_part-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.devicePipepileHistorydataPartService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_part-通过id查询")
    @ApiOperation(value = "device_pipepile_historydata_part-通过id查询", notes = "device_pipepile_historydata_part-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        DevicePipepileHistorydataPart devicePipepileHistorydataPart = devicePipepileHistorydataPartService.getById(id);
        if (devicePipepileHistorydataPart == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(devicePipepileHistorydataPart);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param devicePipepileHistorydataPart
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DevicePipepileHistorydataPart devicePipepileHistorydataPart) {
        return super.exportXls(request, devicePipepileHistorydataPart, DevicePipepileHistorydataPart.class, "device_pipepile_historydata_part");
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
        return super.importExcel(request, response, DevicePipepileHistorydataPart.class);
    }

    /**
     * 添加(免登录版)
     *
     * @param devicePipepileHistorydataPart
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata_part-添加")
    @ApiOperation(value = "device_pipepile_historydata_part-添加", notes = "device_pipepile_historydata_part-添加")
    @PostMapping(value = "/addOther1")
    public Result<?> addOther1(@RequestBody DevicePipepileHistorydataPart devicePipepileHistorydataPart) {
        if (StringUtil.isNotEmpty(devicePipepileHistorydataPart.getShebeino()) && StringUtil.isNotEmpty(devicePipepileHistorydataPart.getPileNo())) {
            QueryWrapper<DevicePipepileHistorydataPart> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uuid", devicePipepileHistorydataPart.getUuid());
            DevicePipepileHistorydataPart one = devicePipepileHistorydataPartService.getOne(queryWrapper);
            if (one != null) {
                devicePipepileHistorydataPart.setId(one.getId());
                devicePipepileHistorydataPartService.updateById(devicePipepileHistorydataPart);
            } else {
                devicePipepileHistorydataPartService.save(devicePipepileHistorydataPart);
            }
            return Result.OK("添加成功！");
        } else {
            return Result.error("记录上传失败！请填入设备编号和桩号");
        }
    }

}
