package com.trtm.iot.wzcailiaonamedict.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.trtm.iot.wzcailiaonamedictman.entity.AddDictBatch;
import com.trtm.iot.wzcailiaonamedictman.entity.WzcailiaonamedictMan;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;

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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

import static org.jeecg.common.util.DateUtils.getTimestampten;

/**
 * @Description: 材料配置主表
 * @Author: jeecg-boot
 * @Date: 2021-05-07
 * @Version: V1.0
 */
@Api(tags = "材料配置主表")
@RestController
@RequestMapping("/wzcailiaonamedict/wzcailiaonamedict")
@Slf4j
public class WzcailiaonamedictController extends JeecgController<Wzcailiaonamedict, IWzcailiaonamedictService> {
    @Autowired
    private IWzcailiaonamedictService wzcailiaonamedictService;

    /**
     * 分页列表查询
     *
     * @param wzcailiaonamedict
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "材料配置主表-分页列表查询")
    @ApiOperation(value = "材料配置主表-分页列表查询", notes = "材料配置主表-分页列表查询")
    @GetMapping(value = "/list")
    @PermissionData(pageComponent = "wzgl/wzcailiaonamedict/WzcailiaonamedictList")
    public Result<?> queryPageList(Wzcailiaonamedict wzcailiaonamedict,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                   HttpServletRequest req, Integer ne,
                                   Integer nodetypeCL) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            wzcailiaonamedict.setSysOrgCode("*" + sys_depart_orgcode + "*");
        } else {
            wzcailiaonamedict.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        wzcailiaonamedict.setCailiaoname("*" + wzcailiaonamedict.getCailiaoname() + "*");
        QueryWrapper<Wzcailiaonamedict> queryWrapper = QueryGenerator.initQueryWrapper(wzcailiaonamedict, req.getParameterMap());
        if (ne != null) {
            queryWrapper.ne("lmcailiaolx", ne);
        }
        queryWrapper.eq(nodetypeCL != null, "nodetype", nodetypeCL);
        Page<Wzcailiaonamedict> page = new Page<Wzcailiaonamedict>(pageNo, pageSize);
        IPage<Wzcailiaonamedict> pageList = wzcailiaonamedictService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "材料配置主表-分页列表查询")
    @ApiOperation(value = "材料配置主表-分页列表查询", notes = "材料配置主表-分页列表查询")
    @GetMapping(value = "/list2")
    public Result<?> queryPageList2(Wzcailiaonamedict wzcailiaonamedict,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        wzcailiaonamedict.setSysOrgCode(loginUser.getOrgCode() + "*");
        if (StringUtils.isEmpty(wzcailiaonamedict.getIselocks())) {
            wzcailiaonamedict.setIselocks(1);
        } else {
            wzcailiaonamedict.setIselocks(null);
        }
        QueryWrapper<Wzcailiaonamedict> queryWrapper = QueryGenerator.initQueryWrapper(wzcailiaonamedict, req.getParameterMap());
        List<Wzcailiaonamedict> list = wzcailiaonamedictService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 当前用户获取当前材料配置列表
     *
     * @param Wzcailiaonamedict
     * @param req
     * @return
     */
    @RequestMapping(value = "/list1", method = RequestMethod.GET)
    public Result<List<Wzcailiaonamedict>> queryPageList3(String sys_depart_orgcode, Wzcailiaonamedict Wzcailiaonamedict, HttpServletRequest req) {
        Result<List<Wzcailiaonamedict>> result = new Result<List<Wzcailiaonamedict>>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        List<Wzcailiaonamedict> pageList = wzcailiaonamedictService.usercailiaoList(loginUser.getOrgCode());
        result.setSuccess(true);
        result.setResult(pageList);
        log.info(pageList.toString());
        return result;
    }

    /**
     * 当前用户获取当前材料配置列表
     *
     * @param Wzcailiaonamedict
     * @param req
     * @return
     */
    @RequestMapping(value = "/list3", method = RequestMethod.GET)
    public Result<List<Wzcailiaonamedict>> queryPageList4(String sys_depart_orgcode, Wzcailiaonamedict Wzcailiaonamedict, HttpServletRequest req) {
        Result<List<Wzcailiaonamedict>> result = new Result<List<Wzcailiaonamedict>>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String sysOrgCode = null;
        if (!StringUtils.isEmpty(sys_depart_orgcode)) {
            sysOrgCode = sys_depart_orgcode;
        } else {
            sysOrgCode = loginUser.getOrgCode();
        }
        List<Wzcailiaonamedict> pageList = wzcailiaonamedictService.usercailiaoList(sysOrgCode);
        result.setSuccess(true);
        result.setResult(pageList);
        log.info(pageList.toString());
        return result;
    }

    /**
     * 添加
     *
     * @param wzcailiaonamedict
     * @return
     */
    @AutoLog(value = "材料配置主表-添加")
    @ApiOperation(value = "材料配置主表-添加", notes = "材料配置主表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Wzcailiaonamedict wzcailiaonamedict) {
        /**下面是根据id直接拼接材料编号**/
        List<Wzcailiaonamedict> wzcailiaonamedictList = wzcailiaonamedictService.arrayOnecailiaos();
        Integer id = 0;
        for (Wzcailiaonamedict cailiao : wzcailiaonamedictList) {
            id = cailiao.getTestid();
        }
        id = id + 1;
        wzcailiaonamedict.setTestid(id);
        String cailiaono = "";
        String format = String.format("%05d", id);
        String prefix = "DB-";
        String cailiaonos = prefix + cailiaono + format;
        wzcailiaonamedict.setCailiaono(cailiaonos);
        /**上面是根据id直接拼接材料编号**/
        String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
        wzcailiaonamedict.setGuid(uuid);
        Integer ts = getTimestampten();//获取当前系统的时间戳（10位）
        wzcailiaonamedict.setTs(ts);
        wzcailiaonamedictService.save(wzcailiaonamedict);
        return Result.OK("添加成功！");
    }


    @AutoLog(value = "wzcailiaonamedict_man-批量添加")
    @ApiOperation(value = "wzcailiaonamedict_man-批量添加", notes = "wzcailiaonamedict_man-批量添加")
    @PostMapping(value = "/addBatch")
    public Result<?> addBatch(@RequestBody AddDictBatch addDictBatch ) {
        /**下面是根据id直接拼接材料编号**/
        List<Wzcailiaonamedict> wzcailiaonamedictList = wzcailiaonamedictService.arrayOnecailiaos();
        Integer id = 0;
        for (Wzcailiaonamedict cailiao : wzcailiaonamedictList) {
            id = cailiao.getTestid();
        }
        Map selectqueryone = wzcailiaonamedictService.selectqueryone(addDictBatch.getSysOrgCode());
        for(Wzcailiaonamedict wzcailiaonamedictMan : addDictBatch.getWzcailiaonamedictList()  ){
            if (selectqueryone != null) {
                wzcailiaonamedictMan.setWzcailiaodanweihuansuan(String.valueOf(selectqueryone.get("id")));
            }

            QueryWrapper<Wzcailiaonamedict> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("allguid",wzcailiaonamedictMan.getGuid());
            queryWrapper.eq("sys_org_code",addDictBatch.getSysOrgCode());
            Wzcailiaonamedict one = wzcailiaonamedictService.getOne(queryWrapper);
            wzcailiaonamedictMan.setSysOrgCode(addDictBatch.getSysOrgCode());
            if( one != null){
                BeanUtils.copyProperties(one,wzcailiaonamedictMan);

            }else{
                wzcailiaonamedictMan.setId(null);
                wzcailiaonamedictMan.setAllguid(wzcailiaonamedictMan.getGuid());
                wzcailiaonamedictMan.setIselocks(addDictBatch.getIselocks());
                wzcailiaonamedictMan.setLmcailiaolx(addDictBatch.getLmcailiaolx());
                id = id + 1;
                wzcailiaonamedictMan.setTestid(id);
                String format = String.format("%03d", id);
                String prefix = "DB-";
                String cailiaonos = addDictBatch.getSysOrgCode() +"-"+ prefix +  format;
                wzcailiaonamedictMan.setCailiaono(cailiaonos);
                /**上面是根据id直接拼接材料编号**/
                String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
                wzcailiaonamedictMan.setGuid(uuid);
                Integer ts = getTimestampten();//获取当前系统的时间戳（10位）
                wzcailiaonamedictMan.setTs(ts);
            }


        }

        wzcailiaonamedictService.saveOrUpdateBatch(addDictBatch.getWzcailiaonamedictList());
        return Result.OK("添加成功！");

    }

    /**
     * 材料类型对外开放添加数据接口
     *
     * @param wzcailiaonamedict
     * @return
     */
    @AutoLog(value = "材料配置主表-添加")
    @ApiOperation(value = "材料配置主表-添加", notes = "材料配置主表-添加")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen(@RequestBody Wzcailiaonamedict wzcailiaonamedict) {
        QueryWrapper<Wzcailiaonamedict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cailiaoNo", wzcailiaonamedict.getCailiaono());
        Wzcailiaonamedict one = wzcailiaonamedictService.getOne(queryWrapper);
        if (one == null) {
            wzcailiaonamedictService.save(wzcailiaonamedict);
            return Result.OK("添加成功！");
        } else {
            return Result.error("添加失败！");
        }
    }

    /**
     * 编辑
     *
     * @param wzcailiaonamedict
     * @return
     */
    @AutoLog(value = "材料配置主表-编辑")
    @ApiOperation(value = "材料配置主表-编辑", notes = "材料配置主表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Wzcailiaonamedict wzcailiaonamedict) {
        Map selectqueryone = wzcailiaonamedictService.selectqueryone(wzcailiaonamedict.getSysOrgCode());
        if (selectqueryone != null) {
            wzcailiaonamedict.setWzcailiaodanweihuansuan(String.valueOf(selectqueryone.get("id")));
        }
        Integer ts = getTimestampten();//获取当前系统的时间戳（10位）
        wzcailiaonamedict.setTs(ts);
        wzcailiaonamedictService.updateById(wzcailiaonamedict);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "材料配置主表-通过id删除")
    @ApiOperation(value = "材料配置主表-通过id删除", notes = "材料配置主表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        wzcailiaonamedictService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "材料配置主表-批量删除")
    @ApiOperation(value = "材料配置主表-批量删除", notes = "材料配置主表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.wzcailiaonamedictService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "材料配置主表-通过id查询")
    @ApiOperation(value = "材料配置主表-通过id查询", notes = "材料配置主表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.getById(id);
        if (wzcailiaonamedict == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(wzcailiaonamedict);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param wzcailiaonamedict
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Wzcailiaonamedict wzcailiaonamedict) {
        return super.exportXls(request, wzcailiaonamedict, Wzcailiaonamedict.class, "材料配置主表");
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
        return super.importExcel(request, response, Wzcailiaonamedict.class);
    }

}
