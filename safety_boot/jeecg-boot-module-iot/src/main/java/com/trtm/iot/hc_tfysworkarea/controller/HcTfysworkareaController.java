package com.trtm.iot.hc_tfysworkarea.controller;

import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.czsh.vo.BhzCementOverHandlerPage;
import com.trtm.iot.hc_project.entity.HcProject;
import com.trtm.iot.hc_project.service.IHcProjectService;
import com.trtm.iot.hc_section.entity.HcSection;
import com.trtm.iot.hc_section.service.IHcSectionService;
import com.trtm.iot.hc_tfstationdetail.entity.HcTfstationdetail;
import com.trtm.iot.hc_tfstationdetail.service.IHcTfstationdetailService;
import com.trtm.iot.hctfysworkareaoverhandler.entity.HcTfysworkareaOverHandler;
import com.trtm.iot.hctfysworkareaoverhandler.service.IHcTfysworkareaOverHandlerService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.hc_tfysworkarea.entity.HcTfysworkarea;
import com.trtm.iot.hc_tfysworkarea.service.IHcTfysworkareaService;

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
import org.springframework.beans.BeanUtils;
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
 * @Description: 土方工作区施工成果
 * @Author: jeecg-boot
 * @Date: 2023-10-10
 * @Version: V1.0
 */
@Api(tags = "土方工作区施工成果")
@RestController
@RequestMapping("/hc_tfysworkarea/hcTfysworkarea")
@Slf4j
public class HcTfysworkareaController extends JeecgController<HcTfysworkarea, IHcTfysworkareaService> {
    @Autowired
    private IHcTfysworkareaService hcTfysworkareaService;
    @Autowired
    private IHcTfstationdetailService hcTfstationdetailService;
    @Autowired
    private IHcProjectService projectService;
    @Autowired
    private IHcSectionService sectionService;
    @Autowired
    private IHcTfysworkareaOverHandlerService hcTfysworkareaOverHandlerServiceService;

    /**
     * 分页列表查询
     *
     * @param hcTfysworkarea
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "土方工作区施工成果-分页列表查询")
    @ApiOperation(value = "土方工作区施工成果-分页列表查询", notes = "土方工作区施工成果-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(HcTfysworkarea hcTfysworkarea,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        if (hcTfysworkarea.getStartstation() != null){
            hcTfysworkarea.setStartstation(hcTfysworkarea.getStartstation() + "*");
        }
        if (hcTfysworkarea.getEndstation() != null){
            hcTfysworkarea.setEndstation(hcTfysworkarea.getEndstation() + "*");
        }
        QueryWrapper<HcTfysworkarea> queryWrapper = QueryGenerator.initQueryWrapper(hcTfysworkarea, req.getParameterMap());
        Page<HcTfysworkarea> page = new Page<HcTfysworkarea>(pageNo, pageSize);
        IPage<HcTfysworkarea> pageList = hcTfysworkareaService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param hcTfysworkarea
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "土方工作区施工成果-分页列表查询")
    @ApiOperation(value = "土方工作区施工成果-分页列表查询", notes = "土方工作区施工成果-分页列表查询")
    @GetMapping(value = "/listsyjyj")
    public Result<?> queryPagelistsyjyj(HcTfysworkarea hcTfysworkarea,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String orgCode = loginUser.getOrgCode();

        List<HcProject> list = projectService.list();
        List<String> object = new ArrayList<>();
        for (HcProject l :list){
            if (orgCode.contains(l.getOrgcode()) || l.getOrgcode().contains(orgCode)){
                object.add(l.getPjid());
            }
        }

        //不合格数
        QueryWrapper<HcTfysworkarea> queryWrapper = QueryGenerator.initQueryWrapper(hcTfysworkarea, req.getParameterMap());
        queryWrapper.eq("standard",1);
        queryWrapper.in("projectid",object);
        List<HcTfysworkarea> list1 = hcTfysworkareaService.list(queryWrapper);

        //闭合数
        QueryWrapper<HcTfysworkarea> queryWrapper1 = QueryGenerator.initQueryWrapper(hcTfysworkarea, req.getParameterMap());
        queryWrapper1.eq("standard",1);
        queryWrapper1.in("projectid",object);
        queryWrapper1.eq("overproof_status",20);
        List<HcTfysworkarea> list2 = hcTfysworkareaService.list(queryWrapper1);

        QueryWrapper<HcTfysworkarea> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("standard",1);
        queryWrapper2.in("projectid",object);
        queryWrapper2.last("ORDER BY id DESC limit 5");
        List<HcTfysworkarea> list3 = hcTfysworkareaService.list(queryWrapper2);

        HashMap<String, Object> map = new HashMap<>();
        map.put("over",list1.size());
        map.put("overproof",list2.size());
        map.put("detail",list3);
        return Result.OKs(map);
    }

    /**
     * 半月统计
     *
     * @param hcTfysworkarea
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "土方工作区施工成果-分页列表查询")
    @ApiOperation(value = "土方工作区施工成果-分页列表查询", notes = "土方工作区施工成果-分页列表查询")
    @GetMapping(value = "/listbytj")
    public Result<?> queryPageListbytj(HcTfysworkarea hcTfysworkarea,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        List<HcTfysworkarea> pageList = hcTfysworkareaService.listbytj();
        String s = null;
        List<HcTfysworkarea> pageList1 = new ArrayList<>();
        for (HcTfysworkarea page :pageList){
            String layername = page.getLayername();

            String s1 = layername + "-01";
            String s16 = layername + "-16";
            if (s != null){
                List<HcTfysworkarea> hcTfysworkarea1 = hcTfysworkareaService.listbytjs(s,s1);
                List<HcTfysworkarea> hcTfysworkarea2 = hcTfysworkareaService.listbytjs(s1,s16);
                if (hcTfysworkarea1.size() > 0){
                    for (HcTfysworkarea hcTfysworkarea1s :hcTfysworkarea1){
                        String sectionName = hcTfysworkareaService.getSectionName(hcTfysworkarea1s.getSectionid());
                        hcTfysworkarea1s.setSectionid(sectionName);
                        hcTfysworkarea1s.setBlockid(s + "——" + s1);
                        pageList1.add(hcTfysworkarea1s);
                    }
                }

                if (hcTfysworkarea2.size() > 0){
                    for (HcTfysworkarea hcTfysworkarea1s :hcTfysworkarea2){
                        String sectionName = hcTfysworkareaService.getSectionName(hcTfysworkarea1s.getSectionid());
                        hcTfysworkarea1s.setSectionid(sectionName);
                        hcTfysworkarea1s.setBlockid(s1 + "——" + s16);
                        pageList1.add(hcTfysworkarea1s);
                    }
                }
            }else {
                List<HcTfysworkarea> hcTfysworkarea1 = hcTfysworkareaService.listbytjs(s1,s16);
                if (hcTfysworkarea1.size() > 0){
                    for (HcTfysworkarea hcTfysworkarea1s :hcTfysworkarea1){
                        String sectionName = hcTfysworkareaService.getSectionName(hcTfysworkarea1s.getSectionid());
                        hcTfysworkarea1s.setSectionid(sectionName);
                        hcTfysworkarea1s.setBlockid(s1 + "——" + s1);
                        pageList1.add(hcTfysworkarea1s);
                    }
                }
            }
            s = s16;
        }
        return Result.OK(pageList1);
    }

    /**
     * 分页列表查询详情
     *
     * @param hcTfysworkarea
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "土方工作区施工成果-分页列表查询")
    @ApiOperation(value = "土方工作区施工成果-分页列表查询", notes = "土方工作区施工成果-分页列表查询")
    @GetMapping(value = "/listxq")
    public Result<?> queryPageListxq(HcTfysworkarea hcTfysworkarea,String stast,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
//        QueryWrapper<HcTfysworkarea> queryWrapper = QueryGenerator.initQueryWrapper(hcTfysworkarea, req.getParameterMap());
        QueryWrapper<HcSection> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("sectionId",stast);
        HcSection one = sectionService.getOne(queryWrapper1);
        QueryWrapper<HcTfysworkarea> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("startstation",hcTfysworkarea.getStartstation());
        queryWrapper.le("endstation",hcTfysworkarea.getEndstation());
        Page<HcTfysworkarea> page = new Page<HcTfysworkarea>(pageNo, pageSize);
        IPage<HcTfysworkarea> pageList = hcTfysworkareaService.page(page, queryWrapper);
        for (HcTfysworkarea pag :pageList.getRecords()){
            pag.setProjectid(one.getSectionbuildercompanyname());
            pag.setSectionid(one.getSectionsupcompanyname());
        }
        return Result.OK(pageList);
    }
    /**
     * 土方压实大屏
     *
     * @param hcTfysworkarea
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "土方工作区施工成果-分页列表查询")
    @ApiOperation(value = "土方工作区施工成果-分页列表查询", notes = "土方工作区施工成果-分页列表查询")
    @GetMapping(value = "/listtuf")
    public Result<?> queryPageListtuf(HcTfysworkarea hcTfysworkarea,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        if (hcTfysworkarea.getStartstation() != null){
            hcTfysworkarea.setStartstation(hcTfysworkarea.getStartstation() + "*");
        }
        if (hcTfysworkarea.getEndstation() != null){
            hcTfysworkarea.setEndstation(hcTfysworkarea.getEndstation() + "*");
        }
        QueryWrapper<HcTfysworkarea> queryWrapper = QueryGenerator.initQueryWrapper(hcTfysworkarea, req.getParameterMap());
        Page<HcTfysworkarea> page = new Page<HcTfysworkarea>(pageNo, pageSize);
        IPage<HcTfysworkarea> pageList = hcTfysworkareaService.page(page, queryWrapper);
        List<HcTfysworkarea> records = pageList.getRecords();
        if (records.size() > 0){
            for (HcTfysworkarea tfysworkarea :records){
                List<HcTfstationdetail> hcTfstationdetails = hcTfstationdetailService.selectlistbybaserid(tfysworkarea.getBlockid());
                if (hcTfstationdetails.size() > 0){
                    HcTfstationdetail hcTfstationdetail = hcTfstationdetails.get(0);
                    tfysworkarea.setAlarmnum(hcTfstationdetail.getAvgheight());
                }
            }
        }
        return Result.OK(pageList);
    }

    /**
     * 土方压实大屏高层曲线
     * @return
     */
    @AutoLog(value = "土方工作区施工成果-分页列表查询")
    @ApiOperation(value = "土方工作区施工成果-分页列表查询", notes = "土方工作区施工成果-分页列表查询")
    @GetMapping(value = "/listtufgcqx")
    public Result<?> queryPageListtufgcqx(String blockid) {
        List<HcTfstationdetail> hcTfstationdetails = hcTfstationdetailService.selectlistbybaserid(blockid);
        return Result.OK(hcTfstationdetails);
    }

    /**
     * 分页列表查询不合格数据
     *
     * @param hcTfysworkarea
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "土方工作区施工成果-分页列表查询")
    @ApiOperation(value = "土方工作区施工成果-分页列表查询", notes = "土方工作区施工成果-分页列表查询")
    @GetMapping(value = "/listcb")
    public Result<?> queryPageListcb(HcTfysworkarea hcTfysworkarea,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        if (hcTfysworkarea.getStartstation() != null){
            hcTfysworkarea.setStartstation(hcTfysworkarea.getStartstation() + "*");
        }
        if (hcTfysworkarea.getEndstation() != null){
            hcTfysworkarea.setEndstation(hcTfysworkarea.getEndstation() + "*");
        }
        QueryWrapper<HcTfysworkarea> queryWrapper = QueryGenerator.initQueryWrapper(hcTfysworkarea, req.getParameterMap());
        queryWrapper.gt("standard", 0);
        Page<HcTfysworkarea> page = new Page<HcTfysworkarea>(pageNo, pageSize);
        IPage<HcTfysworkarea> pageList = hcTfysworkareaService.page(page, queryWrapper);
        List<HcTfysworkarea> records = pageList.getRecords();
//        if (records.size() > 0) {
//            for (HcTfysworkarea l : records) {
//                HcTfysworkareaOverHandler selectone = hcTfysworkareaOverHandlerServiceService.selectone(l.getBlockid());
//                if (selectone == null) {
//                    l.setQualitystat("0");
//                } else {
//                    l.setQualitystat(String.valueOf(selectone.getOverproofStatus()));
//                }
//            }
//        }
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param hcTfysworkarea
     * @return
     */
    @AutoLog(value = "土方工作区施工成果-添加")
    @ApiOperation(value = "土方工作区施工成果-添加", notes = "土方工作区施工成果-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody HcTfysworkarea hcTfysworkarea) {
        hcTfysworkareaService.save(hcTfysworkarea);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param hcTfysworkarea
     * @return
     */
    @AutoLog(value = "土方工作区施工成果-编辑")
    @ApiOperation(value = "土方工作区施工成果-编辑", notes = "土方工作区施工成果-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody HcTfysworkarea hcTfysworkarea) {
        hcTfysworkareaService.updateById(hcTfysworkarea);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "土方工作区施工成果-通过id删除")
    @ApiOperation(value = "土方工作区施工成果-通过id删除", notes = "土方工作区施工成果-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        hcTfysworkareaService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "土方工作区施工成果-批量删除")
    @ApiOperation(value = "土方工作区施工成果-批量删除", notes = "土方工作区施工成果-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.hcTfysworkareaService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "土方工作区施工成果-通过id查询")
    @ApiOperation(value = "土方工作区施工成果-通过id查询", notes = "土方工作区施工成果-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        HcTfysworkarea hcTfysworkarea = hcTfysworkareaService.getById(id);
        if (hcTfysworkarea == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(hcTfysworkarea);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param hcTfysworkarea
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HcTfysworkarea hcTfysworkarea) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<HcTfysworkarea> queryWrapper = QueryGenerator.initQueryWrapper(hcTfysworkarea, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<HcTfysworkarea> queryList = hcTfysworkareaService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<HcTfysworkarea> hcTfysworkareaList = new ArrayList<HcTfysworkarea>();
        if (oConvertUtils.isEmpty(selections)) {
            hcTfysworkareaList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            hcTfysworkareaList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<HcTfysworkarea> pageList = new ArrayList<HcTfysworkarea>();
        for (HcTfysworkarea one : hcTfysworkareaList) {
            String startSta = "";
            if (ObjectUtil.isNotEmpty(one.getStartstation())) {
                if (one.getStartstation().length() == 2) {
                    startSta = "K0+0" + one.getStartstation();
                } else if (one.getStartstation().length() == 3) {
                    startSta = "K0+" + one.getStartstation();
                } else if (one.getStartstation().length() > 3) {
                    String k1 = one.getStartstation().substring(0, one.getStartstation().length() - 3);
                    String k2 = one.getStartstation().substring(one.getStartstation().length() - 3);
                    startSta = "K" + k1 + "+" + k2;
                }
            }
            String endSta = "";
            if (ObjectUtil.isNotEmpty(one.getEndstation())) {
                if (one.getEndstation().length() == 2) {
                    endSta = "K0+0" + one.getEndstation();
                } else if (one.getEndstation().length() == 3) {
                    endSta = "K0+" + one.getEndstation();
                } else if (one.getEndstation().length() > 3) {
                    String k1 = one.getEndstation().substring(0, one.getEndstation().length() - 3);
                    String k2 = one.getEndstation().substring(one.getEndstation().length() - 3);
                    endSta = "K" + k1 + "+" + k2;
                }
            }
            one.setStartstation(startSta + "-" + endSta);
            HcTfysworkarea hctfys = new HcTfysworkarea();
            BeanUtil.copyProperties(one, hctfys);
            hctfys.setSectionid(hcTfysworkareaService.getSectionName(one.getSectionid()));
            hctfys.setStandard("0".equals(one.getStandard()) ? "合格" : "不合格");
            pageList.add(hctfys);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "土方压实数据列表");
        mv.addObject(NormalExcelConstants.CLASS, HcTfysworkarea.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("土方压实数据", "导出人:" + sysUser.getRealname(), "土方压实数据列表"));
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
        return super.importExcel(request, response, HcTfysworkarea.class);
    }

}
