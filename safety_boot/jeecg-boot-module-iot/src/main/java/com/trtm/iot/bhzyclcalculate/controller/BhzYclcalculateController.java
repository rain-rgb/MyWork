package com.trtm.iot.bhzyclcalculate.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.iot.bhzyclcalculate.entity.BhzYclcalculate;
import com.trtm.iot.bhzyclcalculate.service.IBhzYclcalculateService;

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
 * @Description: bhz_yclcalculate
 * @Author: jeecg-boot
 * @Date: 2023-10-24
 * @Version: V1.0
 */
@Api(tags = "bhz_yclcalculate")
@RestController
@RequestMapping("/bhzyclcalculate/bhzYclcalculate")
@Slf4j
public class BhzYclcalculateController extends JeecgController<BhzYclcalculate, IBhzYclcalculateService> {
    @Autowired
    private IBhzYclcalculateService bhzYclcalculateService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;

    /**
     * 分页列表查询
     *
     * @param bhzYclcalculate
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "bhz_yclcalculate-分页列表查询")
    @ApiOperation(value = "bhz_yclcalculate-分页列表查询", notes = "bhz_yclcalculate-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BhzYclcalculate bhzYclcalculate,
                                   String shebeiNo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BhzYclcalculate> queryWrapper = QueryGenerator.initQueryWrapper(bhzYclcalculate, req.getParameterMap());
        Page<BhzYclcalculate> page = new Page<BhzYclcalculate>(pageNo, pageSize);
        IPage<BhzYclcalculate> pageList = bhzYclcalculateService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "bhz_yclcalculate")
    @ApiOperation(value = "bhz_yclcalculate", notes = "bhz_yclcalculate")
    @GetMapping(value = "/listTongJi")
    public Result<?> listTongJi(BhzYclcalculate bhzYclcalculate,
                                String shebeiNo,
                                @RequestParam(name = "statisticsTime_begin", required = false) String statisticsTime_begin,
                                @RequestParam(name = "statisticsTime_end", required = false) String statisticsTime_end,
                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        String time = timeFrame();
        String[] split = time.split("/");
        if (statisticsTime_begin == null || "undefined".equals(statisticsTime_begin) || "".equals(statisticsTime_begin)) {
            statisticsTime_begin = split[0];
        }
        if (statisticsTime_end == null || "undefined".equals(statisticsTime_end) || "".equals(statisticsTime_end)) {
            statisticsTime_end = split[1];
        }

        List<Map<String, Object>> mapList = bhzYclcalculateService.getTJInfoBySbjno(shebeiNo, statisticsTime_begin, statisticsTime_end);
        List<BhzYclcalculate> bhzYclcalculates = new ArrayList<>();
        int i = 0;
        for (Map<String, Object> map : mapList) {
            i++;
            BhzYclcalculate bhzYclcalculate1 = new BhzYclcalculate();
            bhzYclcalculate1.setId(i);
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(String.valueOf(map.get("shebeiNo")));
            if (ObjectUtil.isNotEmpty(selectshebeione)) {
                bhzYclcalculate1.setShebeiNo(selectshebeione.getSbname());
            }
            bhzYclcalculate1.setWzName(String.valueOf(map.get("materialeName")));
            bhzYclcalculate1.setThisSysxh(String.valueOf(map.get("realityNumber")));
            bhzYclcalculate1.setStatisticsTime(String.valueOf(map.get("statisticsTime")));
            bhzYclcalculates.add(bhzYclcalculate1);
        }
        return Result.OK(bhzYclcalculates);
    }

    /**
     * 添加
     *
     * @param bhzYclcalculate
     * @return
     */
    @AutoLog(value = "bhz_yclcalculate-添加")
    @ApiOperation(value = "bhz_yclcalculate-添加", notes = "bhz_yclcalculate-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BhzYclcalculate bhzYclcalculate) {
        bhzYclcalculateService.save(bhzYclcalculate);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bhzYclcalculate
     * @return
     */
    @AutoLog(value = "bhz_yclcalculate-编辑")
    @ApiOperation(value = "bhz_yclcalculate-编辑", notes = "bhz_yclcalculate-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BhzYclcalculate bhzYclcalculate) {
        bhzYclcalculateService.updateById(bhzYclcalculate);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "bhz_yclcalculate-通过id删除")
    @ApiOperation(value = "bhz_yclcalculate-通过id删除", notes = "bhz_yclcalculate-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bhzYclcalculateService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "bhz_yclcalculate-批量删除")
    @ApiOperation(value = "bhz_yclcalculate-批量删除", notes = "bhz_yclcalculate-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bhzYclcalculateService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "bhz_yclcalculate-通过id查询")
    @ApiOperation(value = "bhz_yclcalculate-通过id查询", notes = "bhz_yclcalculate-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BhzYclcalculate bhzYclcalculate = bhzYclcalculateService.getById(id);
        if (bhzYclcalculate == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bhzYclcalculate);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bhzYclcalculate
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzYclcalculate bhzYclcalculate) {
        return super.exportXls(request, bhzYclcalculate, BhzYclcalculate.class, "bhz_yclcalculate");
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
        return super.importExcel(request, response, BhzYclcalculate.class);
    }


    public String timeFrame() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String now = sdf.format(date);
        String[] split = now.split("-", 3);

        String time_begin = "";
        String time_end = "";
        if (Integer.parseInt(split[2]) >= 21) {
            time_begin = split[0] + "-" + split[1] + "-" + "21";
            Integer month = null;
            if ("12".equals(split[1])) {
                month = 1;
            } else {
                month = Integer.parseInt(split[1]) + 1;
            }
            time_end = split[0] + "-" + String.valueOf(month) + "-" + "20";
        } else {
            Integer month = null;
            if ("1".equals(split[1])) {
                month = 12;
            } else {
                month = Integer.parseInt(split[1]) - 1;
            }
            time_begin = split[0] + "-" + String.valueOf(month) + "-" + "21";
            time_end = split[0] + "-" + split[1] + "-" + "20";
        }
        return time_begin + "/" + time_end;
    }

}
