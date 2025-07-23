package com.trtm.iot.entranceGuardRecord.controller;

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

import com.trtm.iot.entranceGuardRecord.entity.EntranceGuardType;
import com.trtm.iot.entranceGuardRecord.service.IEntranceGuardTypeService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.entranceGuardRecord.entity.EntranceGuardRecord;
import com.trtm.iot.entranceGuardRecord.service.IEntranceGuardRecordService;

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
 * @Description: 门禁考勤表信息
 * @Author: jeecg-boot
 * @Date: 2021-06-24
 * @Version: V1.0
 */
@Api(tags = "门禁考勤表信息")
@RestController
@RequestMapping("/entranceGuardRecord/entranceGuardRecord")
@Slf4j
public class EntranceGuardRecordController extends JeecgController<EntranceGuardRecord, IEntranceGuardRecordService> {
    @Autowired
    private IEntranceGuardRecordService entranceGuardRecordService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IEntranceGuardTypeService entranceGuardTypeService;

    /**
     * 分页列表查询
     *
     * @param entranceGuardRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "门禁考勤表信息-分页列表查询")
    @ApiOperation(value = "门禁考勤表信息-分页列表查询", notes = "门禁考勤表信息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(EntranceGuardRecord entranceGuardRecord, EntranceGuardType entranceGuardType,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req, Integer type) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (entranceGuardRecord.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                entranceGuardRecord.setShebeino(shebei);
            } else {
                entranceGuardRecord.setShebeino("空");
            }
        }
        String doorid = null;
        if (type != null) {
            if (!"null".equals(shebei)) {
                entranceGuardType.setShebeiNo(shebei);
            } else {
                entranceGuardType.setShebeiNo("空");
            }
            entranceGuardType.setType(type);
            QueryWrapper<EntranceGuardType> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardType, req.getParameterMap());
            List<EntranceGuardType> querylists = entranceGuardTypeService.list(queryWrapper);
            if (querylists.size() > 0) {
                doorid = "";
                for (EntranceGuardType querylist : querylists) {
                    if (doorid.equals("")) {
                        doorid = "" + querylist.getDoorid() + "";
                    } else {
                        doorid = doorid + "," + querylist.getDoorid() + "";
                    }
                }
            }
        }
        if (doorid != null) {
            entranceGuardRecord.setDoorid(doorid);
        }
        entranceGuardRecord.setNames("*" + entranceGuardRecord.getNames() + "*");
        QueryWrapper<EntranceGuardRecord> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardRecord, req.getParameterMap());
        Page<EntranceGuardRecord> page = new Page<EntranceGuardRecord>(pageNo, pageSize);
        IPage<EntranceGuardRecord> pageList = entranceGuardRecordService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 分页列表查询
     *
     * @param entranceGuardRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "门禁考勤表信息-分页列表查询")
    @ApiOperation(value = "门禁考勤表信息-分页列表查询", notes = "门禁考勤表信息-分页列表查询")
    @GetMapping(value = "/list2")
    @PermissionData(pageComponent = "anquan/mjkq/EntranceGuardRecordListDingTalk")
    public Result<?> queryPageList2(EntranceGuardRecord entranceGuardRecord, EntranceGuardType entranceGuardType,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req, Integer type) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (entranceGuardRecord.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                entranceGuardRecord.setShebeino(shebei);
            } else {
                entranceGuardRecord.setShebeino("空");
            }
        }
        entranceGuardRecord.setNames("*" + entranceGuardRecord.getNames() + "*");
        QueryWrapper<EntranceGuardRecord> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardRecord, req.getParameterMap());
        Page<EntranceGuardRecord> page = new Page<>(pageNo, pageSize);
        queryWrapper.select("count(DISTINCT(names)) as isopen");
        IPage<EntranceGuardRecord> pageList = entranceGuardRecordService.page(page, queryWrapper);
        List<EntranceGuardRecord> list = pageList.getRecords();
        if (list != null && list.size() > 0) {
            for (EntranceGuardRecord item : list) {
                Date offTime = item.getAdddate();
                Date openTime = item.getOpentime();
                if (offTime != null && openTime != null) {
                    if (offTime.before(openTime)){
                        item.setAdddate(null);
                    }
                }
            }
        }
        return Result.OK(pageList);
    }


    /**
     * 根据条件查询列表
     * <p>
     * * @param entranceGuardRecord
     *
     * @param req
     * @return
     */
    @AutoLog(value = "门禁考勤表信息-list信息")
    @ApiOperation(value = "门禁考勤表信息-list信息", notes = "门禁考勤表信息-list信息")
    @GetMapping(value = "/querylist")
    public Result<?> queryPageList1(EntranceGuardRecord entranceGuardRecord, EntranceGuardType entranceGuardType, HttpServletRequest req, Integer type) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (entranceGuardRecord.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                entranceGuardRecord.setShebeino(shebei);
            } else {
                entranceGuardRecord.setShebeino("空");
            }
        }
        String doorid = null;
        if (type != null) {
            if (!"null".equals(shebei)) {
                entranceGuardType.setShebeiNo(shebei);
            } else {
                entranceGuardType.setShebeiNo("空");
            }
            entranceGuardType.setType(type);
            QueryWrapper<EntranceGuardType> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardType, req.getParameterMap());
            List<EntranceGuardType> querylists = entranceGuardTypeService.list(queryWrapper);
            if (querylists.size() > 0) {
                doorid = "";
                for (EntranceGuardType querylist : querylists) {
                    if (doorid.equals("")) {
                        doorid = "" + querylist.getDoorid() + "";
                    } else {
                        doorid = doorid + "," + querylist.getDoorid() + "";
                    }
                }
            }
        }
        if (doorid != null) {
            entranceGuardRecord.setDoorid(doorid);
        }
        QueryWrapper<EntranceGuardRecord> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardRecord, req.getParameterMap());
        List<EntranceGuardRecord> list = entranceGuardRecordService.list(queryWrapper);

        return Result.OK(list);
    }


    /**
     * 根据条件查询列表
     * <p>
     * * @param entranceGuardRecord
     *
     * @param req
     * @return
     */
    @AutoLog(value = "门禁考勤表信息-班组")
    @ApiOperation(value = "门禁考勤表信息-班组", notes = "门禁考勤表信息-班组")
    @GetMapping(value = "/querylistDepart")
    public Result<?> querylistDepart(EntranceGuardRecord entranceGuardRecord, HttpServletRequest req, EntranceGuardType entranceGuardType, Integer type) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (entranceGuardRecord.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                entranceGuardRecord.setShebeino(shebei);
            } else {
                entranceGuardRecord.setShebeino("空");
            }
        }
        String doorid = null;
        if (type != null) {
            if (!"null".equals(shebei)) {
                entranceGuardType.setShebeiNo(shebei);
            } else {
                entranceGuardType.setShebeiNo("空");
            }
            entranceGuardType.setType(type);
            QueryWrapper<EntranceGuardType> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardType, req.getParameterMap());
            List<EntranceGuardType> querylists = entranceGuardTypeService.list(queryWrapper);
            if (querylists.size() > 0) {
                doorid = "";
                for (EntranceGuardType querylist : querylists) {
                    if (doorid.equals("")) {
                        doorid = "" + querylist.getDoorid() + "";
                    } else {
                        doorid = doorid + "," + querylist.getDoorid() + "";
                    }
                }
            }
        }
        if (doorid != null) {
            entranceGuardRecord.setDoorid(doorid);
        }
        QueryWrapper<EntranceGuardRecord> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardRecord, req.getParameterMap());
        queryWrapper.select("count( DISTINCT (Names)) as isopen", "DepartName");
        queryWrapper.select().groupBy("DepartName");
//        queryWrapper.last("GROUP BY DepartName");
        queryWrapper.select().orderByDesc("AddDate");
        List<EntranceGuardRecord> list = entranceGuardRecordService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 根据条件查询列表
     * <p>
     * * @param entranceGuardRecord
     *
     * @param req
     * @return
     */
    @AutoLog(value = "门禁考勤表信息-出勤数-总人数")
    @ApiOperation(value = "门禁考勤表信息-出勤数-总人数", notes = "门禁考勤表信息-出勤数-总人数")
    @GetMapping(value = "/querylistAttendance")
    public Result<?> querylistAttendance(EntranceGuardRecord entranceGuardRecord, HttpServletRequest req, EntranceGuardType entranceGuardType, Integer type) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (entranceGuardRecord.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                entranceGuardRecord.setShebeino(shebei);
            } else {
                entranceGuardRecord.setShebeino("空");
            }
        }
        String doorid = null;
        if (type != null) {
            if (!"null".equals(shebei)) {
                entranceGuardType.setShebeiNo(shebei);
            } else {
                entranceGuardType.setShebeiNo("空");
            }
            entranceGuardType.setType(type);
            QueryWrapper<EntranceGuardType> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardType, req.getParameterMap());
            List<EntranceGuardType> querylists = entranceGuardTypeService.list(queryWrapper);
            if (querylists.size() > 0) {
                doorid = "";
                for (EntranceGuardType querylist : querylists) {
                    if (doorid.equals("")) {
                        doorid = "" + querylist.getDoorid() + "";
                    } else {
                        doorid = doorid + "," + querylist.getDoorid() + "";
                    }
                }
            }
        }
        if (doorid != null) {
            entranceGuardRecord.setDoorid(doorid);
        }
        QueryWrapper<EntranceGuardRecord> queryWrapper = QueryGenerator.initQueryWrapper(entranceGuardRecord, req.getParameterMap());
        queryWrapper.select("count( DISTINCT (Names)) as isopen");
        List<EntranceGuardRecord> list = entranceGuardRecordService.list(queryWrapper);
        return Result.OK(list);
    }


    /**
     * 添加
     *
     * @param entranceGuardRecord
     * @return
     */
    @AutoLog(value = "门禁考勤表信息-添加")
    @ApiOperation(value = "门禁考勤表信息-添加", notes = "门禁考勤表信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody EntranceGuardRecord entranceGuardRecord) {
        entranceGuardRecordService.save(entranceGuardRecord);
        return Result.OK("添加成功！");
    }

    /**
     * 添加
     *
     * @param entranceGuardRecord
     * @return
     */
    @AutoLog(value = "门禁考勤表信息-添加")
    @ApiOperation(value = "门禁考勤表信息-添加", notes = "门禁考勤表信息-添加")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen(@RequestBody EntranceGuardRecord entranceGuardRecord) {
        entranceGuardRecordService.save(entranceGuardRecord);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param entranceGuardRecord
     * @return
     */
    @AutoLog(value = "门禁考勤表信息-编辑")
    @ApiOperation(value = "门禁考勤表信息-编辑", notes = "门禁考勤表信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody EntranceGuardRecord entranceGuardRecord) {
        entranceGuardRecordService.updateById(entranceGuardRecord);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "门禁考勤表信息-通过id删除")
    @ApiOperation(value = "门禁考勤表信息-通过id删除", notes = "门禁考勤表信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        entranceGuardRecordService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "门禁考勤表信息-批量删除")
    @ApiOperation(value = "门禁考勤表信息-批量删除", notes = "门禁考勤表信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.entranceGuardRecordService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "门禁考勤表信息-通过id查询")
    @ApiOperation(value = "门禁考勤表信息-通过id查询", notes = "门禁考勤表信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        EntranceGuardRecord entranceGuardRecord = entranceGuardRecordService.getById(id);
        if (entranceGuardRecord == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(entranceGuardRecord);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param entranceGuardRecord
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EntranceGuardRecord entranceGuardRecord) {
        return super.exportXls(request, entranceGuardRecord, EntranceGuardRecord.class, "门禁考勤表信息");
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
        return super.importExcel(request, response, EntranceGuardRecord.class);
    }

}
