package com.trtm.iot.trmaoxiayuyinglim.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.swbhz.entity.BhzSwBases;
import com.trtm.iot.tr_maoxiayuyingli_over_handler.entity.TrMaoxiayuyingliOverHandler;
import com.trtm.iot.tr_maoxiayuyingli_over_handler.service.ITrMaoxiayuyingliOverHandlerService;
import com.trtm.iot.trmaoxiayuyinglim.entity.TrMaoxiayuyingliM;
import com.trtm.iot.trmaoxiayuyinglim.entity.TrMaoxiayuyingliMCZ;
import com.trtm.iot.trmaoxiayuyinglim.entity.TrMaoxiayuyingliS;
import com.trtm.iot.trmaoxiayuyinglim.service.ITrMaoxiayuyingliMService;
import com.trtm.iot.trmaoxiayuyinglim.service.ITrMaoxiayuyingliSService;
import com.trtm.iot.trmaoxiayuyinglim.vo.TrMaoxiayuyingliMPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 锚下预应力张拉主表
 * @Author: jeecg-boot
 * @Date: 2024-03-12
 * @Version: V1.0
 */
@Api(tags = "锚下预应力张拉主表")
@RestController
@RequestMapping("/trmaoxiayuyinglim/trMaoxiayuyingliM")
@Slf4j
public class TrMaoxiayuyingliMController {
    @Autowired
    private ITrMaoxiayuyingliMService trMaoxiayuyingliMService;
    @Autowired
    private ITrMaoxiayuyingliSService trMaoxiayuyingliSService;
    @Autowired
    private ITrMaoxiayuyingliOverHandlerService trMaoxiayuyingliOverHandlerService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param trMaoxiayuyingliM
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "锚下预应力张拉主表-分页列表查询")
    @ApiOperation(value = "锚下预应力张拉主表-分页列表查询", notes = "锚下预应力张拉主表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrMaoxiayuyingliM trMaoxiayuyingliM,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trMaoxiayuyingliM.getSbbh() == null) {
            if (!"null".equals(shebei)) {
                trMaoxiayuyingliM.setSbbh(shebei);
            } else {
                trMaoxiayuyingliM.setSbbh("空");
            }

        }
        QueryWrapper<TrMaoxiayuyingliM> queryWrapper = QueryGenerator.initQueryWrapper(trMaoxiayuyingliM, req.getParameterMap());
        Page<TrMaoxiayuyingliM> page = new Page<TrMaoxiayuyingliM>(pageNo, pageSize);
        IPage<TrMaoxiayuyingliM> pageList = trMaoxiayuyingliMService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "锚下预应力张拉主表-分页列表查询")
    @ApiOperation(value = "锚下预应力张拉超标主表-分页列表查询", notes = "锚下预应力张拉超标主表-分页列表查询")
    @GetMapping(value = "/cblist")
    public Result<?> queryPagecbList(TrMaoxiayuyingliM trMaoxiayuyingliM,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trMaoxiayuyingliM.getSbbh() == null) {
            if (!"null".equals(shebei)) {
                trMaoxiayuyingliM.setSbbh(shebei);
            } else {
                trMaoxiayuyingliM.setSbbh("空");
            }
        }
        List<Object> records = new ArrayList<>();
        Map<Object, Object> map = new HashMap<>();
        QueryWrapper<TrMaoxiayuyingliM> queryWrapper = QueryGenerator.initQueryWrapper(trMaoxiayuyingliM, req.getParameterMap());
        queryWrapper.gt("over_level", 0);
        Page<TrMaoxiayuyingliM> page = new Page<TrMaoxiayuyingliM>(pageNo, pageSize);
        IPage<TrMaoxiayuyingliM> pageList = trMaoxiayuyingliMService.page(page, queryWrapper);
        for (TrMaoxiayuyingliM record : pageList.getRecords()) {
            String uuid = record.getUuid();
            TrMaoxiayuyingliMCZ trMaoxiayuyingliMCZ = new TrMaoxiayuyingliMCZ();
            BeanUtils.copyProperties(record, trMaoxiayuyingliMCZ);

            LambdaQueryWrapper<TrMaoxiayuyingliOverHandler> queryWrapperover = new LambdaQueryWrapper<>();
            queryWrapperover.eq(TrMaoxiayuyingliOverHandler::getBaseid, uuid);
            TrMaoxiayuyingliOverHandler setTrMaoxiayuyingliOverHandler = trMaoxiayuyingliOverHandlerService.getOne(queryWrapperover);
            trMaoxiayuyingliMCZ.setTrMaoxiayuyingliOverHandler(setTrMaoxiayuyingliOverHandler);

            records.add(trMaoxiayuyingliMCZ);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", records);
        return Result.OK(map);
    }


    /**
     * 根据id添加或者修改处置信息
     */
    @AutoLog(value = "锚下预应力张拉处置信息添加或者修改")
    @ApiOperation(value = "锚下预应力张拉处置信息添加或者修改", notes = "锚下预应力张拉处置信息添加或者修改")
    @GetMapping(value = "/CZAddOrUpdate")
    public Result<?> chuZhi(BhzCementOverHandler bhzCementOverHandler,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String chuzhiren = String.valueOf(loginUser.getRealname());
        String wtyy = request.getParameter("wtyy");  //问题原因
        String clfs = request.getParameter("clfs");  //处理方式
        String cljg = request.getParameter("cljg");  //处理结果
        String uuid = request.getParameter("uuid");   //id

        LambdaQueryWrapper<TrMaoxiayuyingliM> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(TrMaoxiayuyingliM::getUuid, uuid);
        List<TrMaoxiayuyingliM> list = trMaoxiayuyingliMService.list(lambdaQueryWrapper);
        for (TrMaoxiayuyingliM trmaoxiayuyingliM : list) {
            TrMaoxiayuyingliM maoxiayuyingliM = new TrMaoxiayuyingliM();
            maoxiayuyingliM.setId(trmaoxiayuyingliM.getId());
            maoxiayuyingliM.setOverproofStatus(10);
            trMaoxiayuyingliMService.updateById(maoxiayuyingliM);
        }

        String bizPath = request.getParameter("fileList");  //图片
        int i = trMaoxiayuyingliOverHandlerService.chuZhiAddOrUpDate(wtyy, clfs, cljg, uuid, bizPath, chuzhiren);
        return Result.OK(i);
    }

    /**
     * 添加
     *
     * @param trMaoxiayuyingliMPage
     * @return
     */
    @AutoLog(value = "锚下预应力张拉主表-添加")
    @ApiOperation(value = "锚下预应力张拉主表-添加", notes = "锚下预应力张拉主表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrMaoxiayuyingliMPage trMaoxiayuyingliMPage) {
        TrMaoxiayuyingliM trMaoxiayuyingliM = new TrMaoxiayuyingliM();
        BeanUtils.copyProperties(trMaoxiayuyingliMPage, trMaoxiayuyingliM);
        trMaoxiayuyingliMService.saveMain(trMaoxiayuyingliM, trMaoxiayuyingliMPage.getTrMaoxiayuyingliSList());
        return Result.OK("添加成功！");
    }

    /**
     * 添加对外开放
     *
     * @param trMaoxiayuyingliMPage
     * @return
     */
    @AutoLog(value = "锚下预应力张拉主表-添加开放")
    @ApiOperation(value = "锚下预应力张拉主表-添加开放", notes = "锚下预应力张拉主表-添加开放")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen(@RequestBody TrMaoxiayuyingliMPage trMaoxiayuyingliMPage) {
        TrMaoxiayuyingliM trMaoxiayuyingliM = new TrMaoxiayuyingliM();
        BeanUtils.copyProperties(trMaoxiayuyingliMPage, trMaoxiayuyingliM);
        trMaoxiayuyingliMService.saveMain(trMaoxiayuyingliM, trMaoxiayuyingliMPage.getTrMaoxiayuyingliSList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param trMaoxiayuyingliMPage
     * @return
     */
    @AutoLog(value = "锚下预应力张拉主表-编辑")
    @ApiOperation(value = "锚下预应力张拉主表-编辑", notes = "锚下预应力张拉主表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrMaoxiayuyingliMPage trMaoxiayuyingliMPage) {
        TrMaoxiayuyingliM trMaoxiayuyingliM = new TrMaoxiayuyingliM();
        BeanUtils.copyProperties(trMaoxiayuyingliMPage, trMaoxiayuyingliM);
        TrMaoxiayuyingliM trMaoxiayuyingliMEntity = trMaoxiayuyingliMService.getById(trMaoxiayuyingliM.getId());
        if (trMaoxiayuyingliMEntity == null) {
            return Result.error("未找到对应数据");
        }
        trMaoxiayuyingliMService.updateMain(trMaoxiayuyingliM, trMaoxiayuyingliMPage.getTrMaoxiayuyingliSList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "锚下预应力张拉主表-通过id删除")
    @ApiOperation(value = "锚下预应力张拉主表-通过id删除", notes = "锚下预应力张拉主表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trMaoxiayuyingliMService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "锚下预应力张拉主表-批量删除")
    @ApiOperation(value = "锚下预应力张拉主表-批量删除", notes = "锚下预应力张拉主表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trMaoxiayuyingliMService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "锚下预应力张拉主表-通过id查询")
    @ApiOperation(value = "锚下预应力张拉主表-通过id查询", notes = "锚下预应力张拉主表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrMaoxiayuyingliM trMaoxiayuyingliM = trMaoxiayuyingliMService.getById(id);
        if (trMaoxiayuyingliM == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(trMaoxiayuyingliM);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "锚下预应力张拉子表通过主表ID查询")
    @ApiOperation(value = "锚下预应力张拉子表主表ID查询", notes = "锚下预应力张拉子表-通主表ID查询")
    @GetMapping(value = "/queryTrMaoxiayuyingliSByMainId")
    public Result<?> queryTrMaoxiayuyingliSListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<TrMaoxiayuyingliS> trMaoxiayuyingliSList = trMaoxiayuyingliSService.selectByMainId(id);
        int size = trMaoxiayuyingliSList.size();
        //添加总条数
        Map map = new HashMap();
        map.put("trMaoxiayuyingliSList", trMaoxiayuyingliSList);
        map.put("size", size);
        return Result.OK(map);
    }
    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "锚下预应力张拉子表通过主表ID查询")
    @ApiOperation(value = "锚下预应力张拉子表主表ID查询", notes = "锚下预应力张拉子表-通主表ID查询")
    @GetMapping(value = "/queryTrMaoxiayuyingliSByMainId1")
    public Result<?> queryTrMaoxiayuyingliSListByMainId1(@RequestParam(name = "id", required = true) String id) {
        List<TrMaoxiayuyingliS> trMaoxiayuyingliSList = trMaoxiayuyingliSService.selectByMainId(id);
        return Result.OK(trMaoxiayuyingliSList);
    }
    /**
     * 导出excel
     *
     * @param request
     * @param trMaoxiayuyingliM
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrMaoxiayuyingliM trMaoxiayuyingliM) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<TrMaoxiayuyingliM> queryWrapper = QueryGenerator.initQueryWrapper(trMaoxiayuyingliM, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<TrMaoxiayuyingliM> queryList = trMaoxiayuyingliMService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<TrMaoxiayuyingliM> trMaoxiayuyingliMList = new ArrayList<TrMaoxiayuyingliM>();
        if (oConvertUtils.isEmpty(selections)) {
            trMaoxiayuyingliMList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            trMaoxiayuyingliMList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<TrMaoxiayuyingliMPage> pageList = new ArrayList<TrMaoxiayuyingliMPage>();
        for (TrMaoxiayuyingliM main : trMaoxiayuyingliMList) {
            TrMaoxiayuyingliMPage vo = new TrMaoxiayuyingliMPage();
            BeanUtils.copyProperties(main, vo);
            List<TrMaoxiayuyingliS> trMaoxiayuyingliSList = trMaoxiayuyingliSService.selectByMainId(String.valueOf(main.getId()));
            vo.setTrMaoxiayuyingliSList(trMaoxiayuyingliSList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "锚下预应力张拉主表列表");
        mv.addObject(NormalExcelConstants.CLASS, TrMaoxiayuyingliMPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("锚下预应力张拉主表数据", "导出人:" + sysUser.getRealname(), "锚下预应力张拉主表"));
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
                List<TrMaoxiayuyingliMPage> list = ExcelImportUtil.importExcel(file.getInputStream(), TrMaoxiayuyingliMPage.class, params);
                for (TrMaoxiayuyingliMPage page : list) {
                    TrMaoxiayuyingliM po = new TrMaoxiayuyingliM();
                    BeanUtils.copyProperties(page, po);
                    trMaoxiayuyingliMService.saveMain(po, page.getTrMaoxiayuyingliSList());
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
