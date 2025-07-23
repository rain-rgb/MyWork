package com.trtm.iot.mxesy.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.StabilityDetail.entity.StabilityDetail;
import com.trtm.iot.StabilityDetail.service.IStabilityDetailService;
import com.trtm.iot.mxesy.entity.WendingD;
import com.trtm.iot.mxesy.service.IWendingDService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
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
 * @Description: 稳定度主表
 * @Author: jeecg-boot
 * @Date: 2021-04-28
 * @Version: V1.0
 */
@Api(tags = "稳定度主表")
@RestController
@RequestMapping("/mxe/wendingD")
@Slf4j
public class WendingDController extends JeecgController<WendingD, IWendingDService> {
    @Autowired
    private IWendingDService wendingDService;
    @Autowired
    private IStabilityDetailService stabilityDetailService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param wendingD
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "稳定度主表-分页列表查询")
    @ApiOperation(value = "稳定度主表-分页列表查询", notes = "稳定度主表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WendingD wendingD,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (wendingD.getFsbbh() == null) {
            if (!"null".equals(shebei)) {
                wendingD.setFsbbh(shebei);
            }else{
                wendingD.setFsbbh("空");
            }
        }
        QueryWrapper<WendingD> queryWrapper = QueryGenerator.initQueryWrapper(wendingD, req.getParameterMap());
        Page<WendingD> page = new Page<WendingD>(pageNo, pageSize);
        IPage<WendingD> pageList = wendingDService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param wendingD
     * @return
     */
    @AutoLog(value = "稳定度主表-添加")
    @ApiOperation(value = "稳定度主表-添加", notes = "稳定度主表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WendingD wendingD) {
        wendingDService.save(wendingD);
        return Result.OK("添加成功！");
    }

    /**
     * 添加
     *
     * @param wendingD
     * @return
     */
    @AutoLog(value = "稳定度主表-添加")
    @ApiOperation(value = "稳定度主表-添加", notes = "稳定度主表-添加")
    @PostMapping(value = "/addOpen2")
    public Result<?> addOpen(@RequestBody WendingD wendingD) {
        boolean save = wendingDService.save(wendingD);
        if (save) {
            return Result.OK("添加成功！");
        } else {
            return Result.error("添加失败！");
        }
    }

    /**
     * 编辑
     *
     * @param wendingD
     * @return
     */
    @AutoLog(value = "稳定度主表-编辑")
    @ApiOperation(value = "稳定度主表-编辑", notes = "稳定度主表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody WendingD wendingD) {
        wendingDService.updateById(wendingD);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "稳定度主表-通过id删除")
    @ApiOperation(value = "稳定度主表-通过id删除", notes = "稳定度主表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        wendingDService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "稳定度主表-批量删除")
    @ApiOperation(value = "稳定度主表-批量删除", notes = "稳定度主表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.wendingDService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "稳定度主表-通过id查询")
    @ApiOperation(value = "稳定度主表-通过id查询", notes = "稳定度主表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WendingD wendingD = wendingDService.getById(id);
        if (wendingD == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(wendingD);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param wendingD
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WendingD wendingD) {
        return super.exportXls(request, wendingD, WendingD.class, "稳定度主表");
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
        return super.importExcel(request, response, WendingD.class);
    }


    /**
     * 稳定度接收接口
     *
     * @param args
     * @return
     */
    @AutoLog(value = "稳定度接收接口")
    @ApiOperation(value = "稳定度接收接口", notes = "稳定度接收接口")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen2(@RequestBody String args) {
        JSONObject jsonObject = JSONObject.parseObject(args);
        WendingD t = JSONObject.toJavaObject(jsonObject, WendingD.class);
        t.setSubmittime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        List<StabilityDetail> list = t.getStabilityDetails();
        boolean save = wendingDService.save(t);
        boolean save1 = false;
        if (list != null && list.size() > 0) {
            for (StabilityDetail item : list) {
                item.setSubmittime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                try {
                    save1 = stabilityDetailService.save(item);
                } catch (Exception exception) {
                    continue;
                }
            }
        }
        if (save1) {
            return Result.OK("路强仪数据上传成功");
        } else if (save){
            return Result.OK("稳定度数据上传成功");
        } else {
            return Result.error("稳定度数据上传失败");
        }
    }
}
