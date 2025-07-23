package com.trtm.iot.devicemixpilerwdsta.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicemixpilerwdsta.entity.DeviceMixpileRwdsta;
import com.trtm.iot.devicemixpilerwdsta.service.IDeviceMixpileRwdstaService;

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
 * @Description: device_mixpile_rwdsta
 * @Author: jeecg-boot
 * @Date: 2022-04-19
 * @Version: V1.0
 */
@Api(tags = "device_mixpile_rwdsta")
@RestController
@RequestMapping("/devicemixpilerwdsta/deviceMixpileRwdsta")
@Slf4j
public class DeviceMixpileRwdstaController extends JeecgController<DeviceMixpileRwdsta, IDeviceMixpileRwdstaService> {
    @Autowired
    private IDeviceMixpileRwdstaService deviceMixpileRwdstaService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param deviceMixpileRwdsta
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_mixpile_rwdsta-分页列表查询")
    @ApiOperation(value = "device_mixpile_rwdsta-分页列表查询", notes = "device_mixpile_rwdsta-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DeviceMixpileRwdsta deviceMixpileRwdsta,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (deviceMixpileRwdsta.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                deviceMixpileRwdsta.setShebeino(shebei);
            } else {
                deviceMixpileRwdsta.setShebeino("空");
            }
        }
        QueryWrapper<DeviceMixpileRwdsta> queryWrapper = QueryGenerator.initQueryWrapper(deviceMixpileRwdsta, req.getParameterMap());
        Page<DeviceMixpileRwdsta> page = new Page<DeviceMixpileRwdsta>(pageNo, pageSize);
        IPage<DeviceMixpileRwdsta> pageList = deviceMixpileRwdstaService.page(page, queryWrapper);
        queryWrapper.select(" sum( CASE WHEN rjstatus = 0 THEN 1 else 0 END) as ready, " +
                "SUM( CASE WHEN rjstatus = 1 THEN 1 else 0  END) as doing, " +
                "SUM( CASE WHEN rjstatus = 2 THEN 1 else 0 END)  as finish, " +
                "SUM( CASE WHEN rjstatus = 3 THEN 1 else 0 END) as timeout ");
        Map<String, Object> map = deviceMixpileRwdstaService.getMap(queryWrapper);
        Result<Object> r = new Result<>();
        r.setSuccess(true);
        r.setCode(CommonConstant.SC_OK_200);
        r.setResult(pageList);
        r.setData(map);
        return r;
    }

    /**
     * 添加
     *
     * @param deviceMixpileRwdsta
     * @return
     */
    @AutoLog(value = "device_mixpile_rwdsta-添加")
    @ApiOperation(value = "device_mixpile_rwdsta-添加", notes = "device_mixpile_rwdsta-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DeviceMixpileRwdsta deviceMixpileRwdsta) {
        deviceMixpileRwdstaService.save(deviceMixpileRwdsta);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param deviceMixpileRwdsta
     * @return
     */
    @AutoLog(value = "device_mixpile_rwdsta-编辑")
    @ApiOperation(value = "device_mixpile_rwdsta-编辑", notes = "device_mixpile_rwdsta-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody DeviceMixpileRwdsta deviceMixpileRwdsta) {
        deviceMixpileRwdstaService.updateById(deviceMixpileRwdsta);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "device_mixpile_rwdsta-通过id删除")
    @ApiOperation(value = "device_mixpile_rwdsta-通过id删除", notes = "device_mixpile_rwdsta-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        deviceMixpileRwdstaService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "device_mixpile_rwdsta-批量删除")
    @ApiOperation(value = "device_mixpile_rwdsta-批量删除", notes = "device_mixpile_rwdsta-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.deviceMixpileRwdstaService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "device_mixpile_rwdsta-通过id查询")
    @ApiOperation(value = "device_mixpile_rwdsta-通过id查询", notes = "device_mixpile_rwdsta-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        DeviceMixpileRwdsta deviceMixpileRwdsta = deviceMixpileRwdstaService.getById(id);
        if (deviceMixpileRwdsta == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(deviceMixpileRwdsta);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param deviceMixpileRwdsta
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceMixpileRwdsta deviceMixpileRwdsta) {
        return super.exportXls(request, deviceMixpileRwdsta, DeviceMixpileRwdsta.class, "device_mixpile_rwdsta");
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
        return super.importExcel(request, response, DeviceMixpileRwdsta.class);
    }

}
