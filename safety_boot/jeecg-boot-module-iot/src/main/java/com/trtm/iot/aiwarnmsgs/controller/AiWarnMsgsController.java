package com.trtm.iot.aiwarnmsgs.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import com.trtm.iot.aiwarnmsgs.entity.AiWarnMsgs;
import com.trtm.iot.aiwarnmsgs.service.IAiWarnMsgsService;

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

/**
 * @Description: ai_warn_msgs
 * @Author: jeecg-boot
 * @Date: 2022-03-24
 * @Version: V1.0
 */
@Api(tags = "ai_warn_msgs")
@RestController
@RequestMapping("/aiwarnmsgs/aiWarnMsgs")
@Slf4j
public class AiWarnMsgsController extends JeecgController<AiWarnMsgs, IAiWarnMsgsService> {
    @Autowired
    private IAiWarnMsgsService aiWarnMsgsService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param aiWarnMsgs
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "ai_warn_msgs-分页列表查询")
    @ApiOperation(value = "ai_warn_msgs-分页列表查询", notes = "ai_warn_msgs-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(AiWarnMsgs aiWarnMsgs,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (aiWarnMsgs.getShebeiid() == null) {
            if (!"null".equals(shebei)) {
                aiWarnMsgs.setShebeiid(shebei);
            } else {
                aiWarnMsgs.setShebeiid("空");
            }
        }
        QueryWrapper<AiWarnMsgs> queryWrapper = QueryGenerator.initQueryWrapper(aiWarnMsgs, req.getParameterMap());
        Page<AiWarnMsgs> page = new Page<AiWarnMsgs>(pageNo, pageSize);
        IPage<AiWarnMsgs> pageList = aiWarnMsgsService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "ai_warn_msgs-分页列表查询")
    @ApiOperation(value = "ai_warn_msgs-分页列表查询", notes = "ai_warn_msgs-分页列表查询")
    @GetMapping(value = "/listCount")
    public Result<?> listCount(AiWarnMsgs aiWarnMsgs,
                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                            HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (aiWarnMsgs.getShebeiid() == null) {
            if (!"null".equals(shebei)) {
                aiWarnMsgs.setShebeiid(shebei);
            } else {
                aiWarnMsgs.setShebeiid("空");
            }
        }
        QueryWrapper<AiWarnMsgs> queryWrapper = QueryGenerator.initQueryWrapper(aiWarnMsgs, req.getParameterMap());
        queryWrapper.select("  algType, " +
                "    COUNT(*) AS total_count, " +
                "    SUM(CASE WHEN DATE_FORMAT(warntime, '%Y-%m') = DATE_FORMAT(CURRENT_DATE, '%Y-%m') THEN 1 ELSE 0 END) AS current_month_count, " +
                "    SUM(CASE WHEN DATE(warntime) = CURRENT_DATE THEN 1 ELSE 0 END) AS today_count");
        queryWrapper.groupBy("algType");
        List<Map<String, Object>> list = aiWarnMsgsService.listMaps(queryWrapper);
        return Result.OK(list);
    }

    @AutoLog(value = "ai_warn_msgs-分页列表查询")
    @ApiOperation(value = "ai_warn_msgs-分页列表查询", notes = "ai_warn_msgs-分页列表查询")
    @GetMapping(value = "/listtj")
    public Result<?> listtj(AiWarnMsgs aiWarnMsgs,
                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                            HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (aiWarnMsgs.getShebeiid() == null) {
            if (!"null".equals(shebei)) {
                aiWarnMsgs.setShebeiid(shebei);
            } else {
                aiWarnMsgs.setShebeiid("空");
            }
        }
        QueryWrapper<AiWarnMsgs> queryWrapper = QueryGenerator.initQueryWrapper(aiWarnMsgs, req.getParameterMap());
        queryWrapper.select("count(*) as enttype,algType");
        queryWrapper.groupBy("algType");
        List<AiWarnMsgs> list = aiWarnMsgsService.list(queryWrapper);
        return Result.OK(list);
    }

    @AutoLog(value = "ai_warn_msgs-分页列表查询")
    @ApiOperation(value = "ai_warn_msgs-分页列表查询", notes = "ai_warn_msgs-分页列表查询")
    @GetMapping(value = "/listytj")
    public Result<?> listytj(AiWarnMsgs aiWarnMsgs,
                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                             HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (aiWarnMsgs.getShebeiid() == null) {
            if (!"null".equals(shebei)) {
                aiWarnMsgs.setShebeiid(shebei);
            } else {
                aiWarnMsgs.setShebeiid("空");
            }
        }
        QueryWrapper<AiWarnMsgs> queryWrapper = QueryGenerator.initQueryWrapper(aiWarnMsgs, req.getParameterMap());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-00 00:00:00");
        Date date = new Date();
        String format = sdf.format(date);

//		 queryWrapper.select("count(*) as enttype,algType  FROM ai_warn_msgs WHERE DATE_FORMAT( FROM_UNIXTIME(SUBSTRING(warntime,1,10),'%Y-%m-%d %h:%i:%S'), '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' )");
        queryWrapper.select("count(*) as enttype,algType");
        queryWrapper.ge("warntime", format);
        queryWrapper.groupBy("algType");
        List<AiWarnMsgs> list = aiWarnMsgsService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 添加
     *
     * @param aiWarnMsgs
     * @return
     */
    @AutoLog(value = "ai_warn_msgs-添加")
    @ApiOperation(value = "ai_warn_msgs-添加", notes = "ai_warn_msgs-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody AiWarnMsgs aiWarnMsgs) {
        aiWarnMsgsService.save(aiWarnMsgs);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param aiWarnMsgs
     * @return
     */
    @AutoLog(value = "ai_warn_msgs-编辑")
    @ApiOperation(value = "ai_warn_msgs-编辑", notes = "ai_warn_msgs-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody AiWarnMsgs aiWarnMsgs) {
        aiWarnMsgsService.updateById(aiWarnMsgs);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "ai_warn_msgs-通过id删除")
    @ApiOperation(value = "ai_warn_msgs-通过id删除", notes = "ai_warn_msgs-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        aiWarnMsgsService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "ai_warn_msgs-批量删除")
    @ApiOperation(value = "ai_warn_msgs-批量删除", notes = "ai_warn_msgs-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.aiWarnMsgsService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "ai_warn_msgs-通过id查询")
    @ApiOperation(value = "ai_warn_msgs-通过id查询", notes = "ai_warn_msgs-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        AiWarnMsgs aiWarnMsgs = aiWarnMsgsService.getById(id);
        if (aiWarnMsgs == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(aiWarnMsgs);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param aiWarnMsgs
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AiWarnMsgs aiWarnMsgs) {
        return super.exportXls(request, aiWarnMsgs, AiWarnMsgs.class, "ai_warn_msgs");
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
        return super.importExcel(request, response, AiWarnMsgs.class);
    }

}
