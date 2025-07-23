package com.trtm.iot.rhdcx.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trtm.iot.rhdcx.entity.WRuanhuadianM;
import com.trtm.iot.rhdcx.service.IWRuanhuadianMService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description: 软化点主表
 * @Author: jeecg-boot
 * @Date: 2021-04-26
 * @Version: V1.0
 */
@Api(tags = "软化点主表")
@RestController
@RequestMapping("/ruanhuadu/wRuanhuadianM")
@Slf4j
public class WRuanhuadianMController extends JeecgController<WRuanhuadianM, IWRuanhuadianMService> {
    @Autowired
    private IWRuanhuadianMService wRuanhuadianMService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param wRuanhuadianM
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "软化点主表-分页列表查询")
    @ApiOperation(value = "软化点主表-分页列表查询", notes = "软化点主表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WRuanhuadianM wRuanhuadianM,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (wRuanhuadianM.getFsbbh() == null) {
            if (!"null".equals(shebei)) {
                wRuanhuadianM.setFsbbh(shebei);
            }else{
                wRuanhuadianM.setFsbbh("空");
            }
        }
        QueryWrapper<WRuanhuadianM> queryWrapper = QueryGenerator.initQueryWrapper(wRuanhuadianM, req.getParameterMap());
        Page<WRuanhuadianM> page = new Page<WRuanhuadianM>(pageNo, pageSize);
        IPage<WRuanhuadianM> pageList = wRuanhuadianMService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param wRuanhuadianM
     * @return
     */
    @AutoLog(value = "软化点主表-添加")
    @ApiOperation(value = "软化点主表-添加", notes = "软化点主表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WRuanhuadianM wRuanhuadianM) {
        wRuanhuadianMService.save(wRuanhuadianM);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param wRuanhuadianM
     * @return
     */
    @AutoLog(value = "软化点主表-编辑")
    @ApiOperation(value = "软化点主表-编辑", notes = "软化点主表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody WRuanhuadianM wRuanhuadianM) {
        wRuanhuadianMService.updateById(wRuanhuadianM);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "软化点主表-通过id删除")
    @ApiOperation(value = "软化点主表-通过id删除", notes = "软化点主表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        wRuanhuadianMService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "软化点主表-批量删除")
    @ApiOperation(value = "软化点主表-批量删除", notes = "软化点主表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.wRuanhuadianMService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "软化点主表-通过id查询")
    @ApiOperation(value = "软化点主表-通过id查询", notes = "软化点主表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WRuanhuadianM wRuanhuadianM = wRuanhuadianMService.getById(id);
        if (wRuanhuadianM == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(wRuanhuadianM);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param wRuanhuadianM
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WRuanhuadianM wRuanhuadianM) {
        return super.exportXls(request, wRuanhuadianM, WRuanhuadianM.class, "软化点主表");
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
        return super.importExcel(request, response, WRuanhuadianM.class);
    }


    /**
     * 软化点接收接口
     *
     * @param
     * @return
     */
    @AutoLog(value = "软化点接收接口")
    @ApiOperation(value = "软化点接收接口", notes = "软化点接收接口")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen(@RequestBody String args) {
        JSONObject jsonObject = JSONObject.parseObject(args);
        WRuanhuadianM t = JSONObject.toJavaObject(jsonObject, WRuanhuadianM.class);
        t.setSubmittime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        if(StringUtils.isNotBlank(t.getSyjid())){
            QueryWrapper<WRuanhuadianM> queryWrapper = new QueryWrapper<WRuanhuadianM>();
            queryWrapper.eq("syjid",t.getSyjid());
            List<WRuanhuadianM> list = wRuanhuadianMService.list(queryWrapper);
            if(list.size()>0){
                boolean update = wRuanhuadianMService.update(t, queryWrapper);
                if (update) {
                    return Result.OK("更新成功！");
                } else {
                    return Result.error(201,"更新失败！");
                }
            }else{
                boolean save = wRuanhuadianMService.save(t);
                if (save) {
                    return Result.OK("添加成功！");
                } else {
                    return Result.error(201,"添加失败！");
                }
            }



        }else{
            return Result.error(201,"syjid不可为空！");
        }

    }
}
