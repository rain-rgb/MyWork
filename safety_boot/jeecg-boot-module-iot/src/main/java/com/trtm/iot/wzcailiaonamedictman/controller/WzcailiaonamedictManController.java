package com.trtm.iot.wzcailiaonamedictman.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedict.mapper.WzcailiaonamedictMapper;
import com.trtm.iot.wzcailiaonamedictman.entity.AddDictBatch;
import com.trtm.iot.wzcailiaonamedictman.mapper.WzcailiaonamedictManMapper;
import com.trtm.iot.wzgongyingshangman.entity.WzgongyingshangMan;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.shiro.SecurityUtils;
import org.checkerframework.checker.units.qual.A;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.wzcailiaonamedictman.entity.WzcailiaonamedictMan;
import com.trtm.iot.wzcailiaonamedictman.service.IWzcailiaonamedictManService;

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

import static org.jeecg.common.util.DateUtils.getTimestampten;

/**
 * @Description: wzcailiaonamedict_man
 * @Author: jeecg-boot
 * @Date: 2022-08-08
 * @Version: V1.0
 */
@Api(tags = "wzcailiaonamedict_man")
@RestController
@RequestMapping("/wzcailiaonamedictman/wzcailiaonamedictMan")
@Slf4j
public class WzcailiaonamedictManController extends JeecgController<WzcailiaonamedictMan, IWzcailiaonamedictManService> {
    @Autowired
    private IWzcailiaonamedictManService wzcailiaonamedictManService;

    /**
     * 分页列表查询
     *
     * @param wzcailiaonamedictMan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wzcailiaonamedict_man-分页列表查询")
    @ApiOperation(value = "wzcailiaonamedict_man-分页列表查询", notes = "wzcailiaonamedict_man-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WzcailiaonamedictMan wzcailiaonamedictMan,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                   HttpServletRequest req,
                                   Integer nodetypeCP) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            wzcailiaonamedictMan.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            wzcailiaonamedictMan.setSysOrgCode("*" + sys_depart_orgcode + "*");
        }
        QueryWrapper<WzcailiaonamedictMan> queryWrapper = QueryGenerator.initQueryWrapper(wzcailiaonamedictMan, req.getParameterMap());
        queryWrapper.eq(nodetypeCP != null, "nodetype", nodetypeCP);
        Page<WzcailiaonamedictMan> page = new Page<WzcailiaonamedictMan>(pageNo, pageSize);
        IPage<WzcailiaonamedictMan> pageList = wzcailiaonamedictManService.page(page, queryWrapper);
        return Result.OK(pageList);
    }
    /**
     * 分页列表查询
     *
     * @param wzcailiaonamedictMan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wzcailiaonamedict_man-分页列表查询")
    @ApiOperation(value = "wzcailiaonamedict_man-分页列表查询", notes = "wzcailiaonamedict_man-分页列表查询")
    @GetMapping(value = "/rebarList")
    public Result<?> queryPagerebarList(WzcailiaonamedictMan wzcailiaonamedictMan,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                   HttpServletRequest req,
                                   Integer nodetypeCP) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            wzcailiaonamedictMan.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            wzcailiaonamedictMan.setSysOrgCode("*" + sys_depart_orgcode + "*");
        }
        QueryWrapper<WzcailiaonamedictMan> queryWrapper = QueryGenerator.initQueryWrapper(wzcailiaonamedictMan, req.getParameterMap());
        queryWrapper.eq(nodetypeCP != null, "nodetype", nodetypeCP);
        queryWrapper.groupBy("guigexinghao");
        Page<WzcailiaonamedictMan> page = new Page<WzcailiaonamedictMan>(pageNo, pageSize);
        IPage<WzcailiaonamedictMan> pageList = wzcailiaonamedictManService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param wzcailiaonamedictMan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wzcailiaonamedict_man-分页列表查询")
    @ApiOperation(value = "wzcailiaonamedict_man-分页列表查询", notes = "wzcailiaonamedict_man-分页列表查询")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(WzcailiaonamedictMan wzcailiaonamedictMan,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        QueryWrapper<WzcailiaonamedictMan> queryWrapper = QueryGenerator.initQueryWrapper(wzcailiaonamedictMan, req.getParameterMap());
        Page<WzcailiaonamedictMan> page = new Page<WzcailiaonamedictMan>(pageNo, pageSize);
        List<WzcailiaonamedictMan> list = wzcailiaonamedictManService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 添加
     *
     * @param wzcailiaonamedictMan
     * @return
     */
    @AutoLog(value = "wzcailiaonamedict_man-添加")
    @ApiOperation(value = "wzcailiaonamedict_man-添加", notes = "wzcailiaonamedict_man-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WzcailiaonamedictMan wzcailiaonamedictMan) {
        /**下面是根据id直接拼接材料编号**/
        List<WzcailiaonamedictMan> wzcailiaonamedictList = wzcailiaonamedictManService.arrayOnecailiaos();
        Integer id = 0;
        for (WzcailiaonamedictMan cailiao : wzcailiaonamedictList) {
            id = cailiao.getId();
        }
        id = id + 1;
        wzcailiaonamedictMan.setTestid(id);
        String cailiaono = "";
        String format = String.format("%05d", id);
        String prefix = "CC-";
        String cailiaonos = prefix + cailiaono + format;
        wzcailiaonamedictMan.setCailiaono(cailiaonos);
        /**上面是根据id直接拼接材料编号**/
        String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
        wzcailiaonamedictMan.setGuid(uuid);
        Integer ts = getTimestampten();//获取当前系统的时间戳（10位）
        wzcailiaonamedictMan.setTs(ts);
        wzcailiaonamedictManService.save(wzcailiaonamedictMan);
        return Result.OK("添加成功！");

    }

    /**
     * 添加
     *
     * @param addDictBatch
     * @return
     */
    @AutoLog(value = "wzcailiaonamedict_man-批量添加")
    @ApiOperation(value = "wzcailiaonamedict_man-批量添加", notes = "wzcailiaonamedict_man-批量添加")
    @PostMapping(value = "/addBatch")
    public Result<?> addBatch(@RequestBody AddDictBatch addDictBatch ) {
        /**下面是根据id直接拼接材料编号**/
        List<WzcailiaonamedictMan> wzcailiaonamedictList = wzcailiaonamedictManService.arrayOnecailiaos();
        Integer id = 0;

        for (WzcailiaonamedictMan cailiao : wzcailiaonamedictList) {
            id = cailiao.getId();
        }
        Map selectqueryone = wzcailiaonamedictManService.selectqueryone(addDictBatch.getSysOrgCode());
        for(WzcailiaonamedictMan wzcailiaonamedictMan : addDictBatch.getWzcailiaonamedictManList()  ){

            if (selectqueryone != null) {
                wzcailiaonamedictMan.setWzcailiaodanweihuansuan(String.valueOf(selectqueryone.get("id")));
            }
            QueryWrapper<WzcailiaonamedictMan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("allguid",wzcailiaonamedictMan.getGuid());
            queryWrapper.eq("sys_org_code",addDictBatch.getSysOrgCode());
            WzcailiaonamedictMan one = null;
            try{
                one  = wzcailiaonamedictManService.getOne(queryWrapper);
            }catch (TooManyResultsException e){
                return Result.error("重复添加！"+wzcailiaonamedictMan.getCailiaoname()+wzcailiaonamedictMan.getGuigexinghao());
            }

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
                String prefix = "CC-";
                String cailiaonos = addDictBatch.getSysOrgCode() +"-"+ prefix +  format;
                wzcailiaonamedictMan.setCailiaono(cailiaonos);
                /**上面是根据id直接拼接材料编号**/
                String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
                wzcailiaonamedictMan.setGuid(uuid);
                Integer ts = getTimestampten();//获取当前系统的时间戳（10位）
                wzcailiaonamedictMan.setTs(ts);
            }


        }

        wzcailiaonamedictManService.saveOrUpdateBatch(addDictBatch.getWzcailiaonamedictManList());
        return Result.OK("添加成功！");

    }




    /**
     * 编辑
     *
     * @param wzcailiaonamedictMan
     * @return
     */
    @AutoLog(value = "wzcailiaonamedict_man-编辑")
    @ApiOperation(value = "wzcailiaonamedict_man-编辑", notes = "wzcailiaonamedict_man-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody WzcailiaonamedictMan wzcailiaonamedictMan) {
        Map selectqueryone = wzcailiaonamedictManService.selectqueryone(wzcailiaonamedictMan.getSysOrgCode());
        if (selectqueryone != null) {
            wzcailiaonamedictMan.setWzcailiaodanweihuansuan(String.valueOf(selectqueryone.get("id")));
        }
        Integer ts = getTimestampten();//获取当前系统的时间戳（10位）
        wzcailiaonamedictMan.setTs(ts);
        wzcailiaonamedictManService.updateById(wzcailiaonamedictMan);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wzcailiaonamedict_man-通过id删除")
    @ApiOperation(value = "wzcailiaonamedict_man-通过id删除", notes = "wzcailiaonamedict_man-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        wzcailiaonamedictManService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "wzcailiaonamedict_man-批量删除")
    @ApiOperation(value = "wzcailiaonamedict_man-批量删除", notes = "wzcailiaonamedict_man-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.wzcailiaonamedictManService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wzcailiaonamedict_man-通过id查询")
    @ApiOperation(value = "wzcailiaonamedict_man-通过id查询", notes = "wzcailiaonamedict_man-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WzcailiaonamedictMan wzcailiaonamedictMan = wzcailiaonamedictManService.getById(id);
        if (wzcailiaonamedictMan == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(wzcailiaonamedictMan);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param wzcailiaonamedictMan
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WzcailiaonamedictMan wzcailiaonamedictMan) {
        return super.exportXls(request, wzcailiaonamedictMan, WzcailiaonamedictMan.class, "wzcailiaonamedict_man");
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
        return super.importExcel(request, response, WzcailiaonamedictMan.class);
    }

    @Autowired
    WzcailiaonamedictManMapper wzcailiaonamedictManMapper;

    /**
     * 通过材料编码查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "仓储材料表通过材料编码查询")
    @ApiOperation(value = "仓储材料表通过材料编码查询", notes = "仓储材料表通过材料编码查询")
    @GetMapping(value = "/queryByCailiaoNo")
    public Result<?> queryByCailiaoNo(@RequestParam(name = "id", required = true) String id) {
        WzcailiaonamedictMan wzcailiaonamedictMan = wzcailiaonamedictManService.getWzcailiaonamedictManById(id);
        String item=wzcailiaonamedictManMapper.DanweiByDanweiType("3");
        System.out.println("wzcailiaonamedictMan = " + item);
        if (wzcailiaonamedictMan == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(wzcailiaonamedictMan);
    }


}
