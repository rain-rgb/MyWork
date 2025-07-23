package com.trtm.iot.devicepipepilehistorydata.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangm.vo.YaJiangmsss;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import com.trtm.iot.zhangla.entity.TrZhangla;
import com.trtm.iot.zhangla.service.ITrZhanglaService;
import com.trtm.iot.zhanglam.entity.ZhanglaYajiangOverHandler;
import com.trtm.iot.zhanglam.service.IZhanglaYajiangOverHandlerService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.devicepipepilehistorydata.entity.DevicePipepileHistorydata;
import com.trtm.iot.devicepipepilehistorydata.service.IDevicePipepileHistorydataService;

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
 * @Description: device_pipepile_historydata
 * @Author: jeecg-boot
 * @Date: 2022-07-21
 * @Version: V1.0
 */
@Api(tags = "device_pipepile_historydata")
@RestController
@RequestMapping("/devicepipepilehistorydata/devicePipepileHistorydata")
@Slf4j
public class DevicePipepileHistorydataController extends JeecgController<DevicePipepileHistorydata, IDevicePipepileHistorydataService> {
    @Autowired
    private IDevicePipepileHistorydataService devicePipepileHistorydataService;
    @Autowired
    private ITrZhanglaService trZhanglaService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ITrYajiangSService trYajiangSService;
    @Autowired
    private ITrYajiangMService trYajiangMService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IZhanglaYajiangOverHandlerService zhanglaYajiangOverHandlerService;


    /**
     * 分页列表查询
     *
     * @param devicePipepileHistorydata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata-分页列表查询")
    @ApiOperation(value = "device_pipepile_historydata-分页列表查询", notes = "device_pipepile_historydata-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DevicePipepileHistorydata devicePipepileHistorydata,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<DevicePipepileHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(devicePipepileHistorydata, req.getParameterMap());
        Page<DevicePipepileHistorydata> page = new Page<DevicePipepileHistorydata>(pageNo, pageSize);
        IPage<DevicePipepileHistorydata> pageList = devicePipepileHistorydataService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 超标分页列表查询
     *
     * @param devicePipepileHistorydata
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata-超标分页列表查询")
    @ApiOperation(value = "device_pipepile_historydata-超标分页列表查询", notes = "device_pipepile_historydata-分页列表查询")
    @GetMapping(value = "/queryExcessivePageList")
    public Result<?> queryExcessivePageList(DevicePipepileHistorydata devicePipepileHistorydata, TrYajiangS trYajiangS, TrZhangla trZhangla, @RequestParam Integer type,
                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                            HttpServletRequest req) {
        if (null != type && 1 == type) {
            QueryWrapper<DevicePipepileHistorydata> queryWrapper = QueryGenerator.initQueryWrapper(devicePipepileHistorydata, req.getParameterMap());
            Page<DevicePipepileHistorydata> page = new Page<DevicePipepileHistorydata>(pageNo, pageSize);
            IPage<DevicePipepileHistorydata> pageList = devicePipepileHistorydataService.page(page, queryWrapper);
            return Result.OK(pageList);
        } else if (null != type && 2 == type) {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
            String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
            if (trZhangla.getDeviceno() == null) {
                if (shebei != null) {
                    trZhangla.setDeviceno(shebei);
                }
            }
            QueryWrapper<TrZhangla> queryWrapper = QueryGenerator.initQueryWrapper(trZhangla, req.getParameterMap());
            Page<TrZhangla> page = new Page<TrZhangla>(pageNo, pageSize);
            IPage<TrZhangla> pageList = trZhanglaService.page(page, queryWrapper);
            return Result.OK(pageList);
        } else if (null != type && 3 == type) {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
            String[] split = shebei.split(",");
            String shebeis = "'" + StringUtils.join(split, "','") + "'";//数据格式   'A','B','C'
            if ("null".equals(shebeis)) {
                shebeis = "空";
            }
            String shebeibianhao = req.getParameter("shebeibianhao");
            IPage<TrYajiangS> pageList = trYajiangSService.selectChaobiaoListPage(pageNo, pageSize, shebeis, shebeibianhao);
            List<TrYajiangS> records = pageList.getRecords();
            List list = new ArrayList();
            Map map = new HashMap();
            for (TrYajiangS record : records) {
                YaJiangmsss yaJiangmsss = new YaJiangmsss();
                String syjid = record.getSyjid();
                TrYajiangM trYajiangM = trYajiangMService.selectgetOne(syjid);
                QueryWrapper<TrYajiangS> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("syjid", syjid);
                List<TrYajiangS> list1 = trYajiangSService.list(queryWrapper);
                String yjsbbh = trYajiangM.getYjsbbh();
                ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(yjsbbh);
                ZhanglaYajiangOverHandler zhanglaYajiangOverHandler = zhanglaYajiangOverHandlerService.selectoneMessage(syjid);
                if (zhanglaYajiangOverHandler == null) {
                    ZhanglaYajiangOverHandler zhanglaYajiangOverHandler1 = new ZhanglaYajiangOverHandler();
                    zhanglaYajiangOverHandler1.setOverproofStatus(0);
                    yaJiangmsss.setOverproofStatus(0);
                    yaJiangmsss.setZhanglaYajiangOverHandler(zhanglaYajiangOverHandler1);
                } else {
                    yaJiangmsss.setOverproofStatus(zhanglaYajiangOverHandler.getOverproofStatus());
                    yaJiangmsss.setZhanglaYajiangOverHandler(zhanglaYajiangOverHandler);
                }
                yaJiangmsss.setSyjid(syjid);
                yaJiangmsss.setSgdw(trYajiangM.getSgdw());
                yaJiangmsss.setGcmc(trYajiangM.getGcmc());
                yaJiangmsss.setYjsj(trYajiangM.getYjsj());
                yaJiangmsss.setSgbw(trYajiangM.getSgbw());
                yaJiangmsss.setGjjg(trYajiangM.getGjjg());
                yaJiangmsss.setQw(trYajiangM.getQw());
                yaJiangmsss.setSw(trYajiangM.getSw());
                yaJiangmsss.setShuijiaobi(trYajiangM.getShuijiaobi());
                yaJiangmsss.setYjwd(trYajiangM.getYjwd());
                yaJiangmsss.setYjsbbh(shebeiInfo.getSbname());
                yaJiangmsss.setZlsj(trYajiangM.getZlsj());
                yaJiangmsss.setKongdaoshu(trYajiangM.getKongdaoshu());
                yaJiangmsss.setLianghao(trYajiangM.getLianghao());
                yaJiangmsss.setYajiangs(list1);
                list.add(yaJiangmsss);
            }
            map.put("current", pageList.getCurrent());
            map.put("pages", pageList.getPages());
            map.put("size", pageList.getSize());
            map.put("total", pageList.getTotal());
            map.put("records", list);
            return Result.OK(map);
        } else {
            return Result.error("暂无数据");
        }
    }


    /**
     * 添加
     *
     * @param devicePipepileHistorydata
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata-添加")
    @ApiOperation(value = "device_pipepile_historydata-添加", notes = "device_pipepile_historydata-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DevicePipepileHistorydata devicePipepileHistorydata) {
        devicePipepileHistorydataService.save(devicePipepileHistorydata);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param devicePipepileHistorydata
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata-编辑")
    @ApiOperation(value = "device_pipepile_historydata-编辑", notes = "device_pipepile_historydata-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody DevicePipepileHistorydata devicePipepileHistorydata) {
        devicePipepileHistorydataService.updateById(devicePipepileHistorydata);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata-通过id删除")
    @ApiOperation(value = "device_pipepile_historydata-通过id删除", notes = "device_pipepile_historydata-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        devicePipepileHistorydataService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata-批量删除")
    @ApiOperation(value = "device_pipepile_historydata-批量删除", notes = "device_pipepile_historydata-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.devicePipepileHistorydataService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "device_pipepile_historydata-通过id查询")
    @ApiOperation(value = "device_pipepile_historydata-通过id查询", notes = "device_pipepile_historydata-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        DevicePipepileHistorydata devicePipepileHistorydata = devicePipepileHistorydataService.getById(id);
        if (devicePipepileHistorydata == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(devicePipepileHistorydata);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param devicePipepileHistorydata
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DevicePipepileHistorydata devicePipepileHistorydata) {
        return super.exportXls(request, devicePipepileHistorydata, DevicePipepileHistorydata.class, "device_pipepile_historydata");
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
        return super.importExcel(request, response, DevicePipepileHistorydata.class);
    }

}
