package com.trtm.iot.ycltd.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.api.utils.StringUtils;
import com.trtm.iot.testroom.entity.SyTestroom;
import com.trtm.iot.testroom.service.ISyTestroomService;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import com.trtm.iot.ycltd.entity.YclTestDetailVo;
import com.trtm.iot.ycltesttaizhang.entity.YclTestTaizhang;
import com.trtm.iot.ycltesttaizhang.service.IYclTestTaizhangService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;

import com.trtm.iot.ycltd.entity.YclTestDetail;
import com.trtm.iot.ycltd.service.IYclTestDetailService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 原材料试验详情
 * @Author: lis1
 * @Date: 2022-11-18
 * @Version: V1.0
 */
@Api(tags = "原材料试验详情")
@RestController
@RequestMapping("/ycltd/yclTestDetail")
@Slf4j
public class YclTestDetailController extends JeecgController<YclTestDetail, IYclTestDetailService> {
    @Autowired
    private IYclTestDetailService yclTestDetailService;
    @Autowired
    private IWztaizhangService wztaizhangService;
    @Autowired
    private IYclTestTaizhangService yclTestTaizhangService;

    @Autowired
    private ISyTestroomService syTestroomService;
    /**
     * 分页列表查询
     *
     * @param yclTestDetail
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "原材料试验详情-分页列表查询")
    @ApiOperation(value = "原材料试验详情-分页列表查询", notes = "原材料试验详情-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(YclTestDetail yclTestDetail,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        // 接口为云检
        if(ObjectUtil.isNotEmpty(yclTestDetail.getNotedd()) && "yunjian".equals(yclTestDetail.getNotedd()) ){
            return Result.OK(getTestRoomInfo(yclTestDetail.getStorageId(),1));
        }
        QueryWrapper<YclTestDetail> queryWrapper = QueryGenerator.initQueryWrapper(yclTestDetail, req.getParameterMap());
        Page<YclTestDetail> page = new Page<YclTestDetail>(pageNo, pageSize);
        IPage<YclTestDetail> pageList = yclTestDetailService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "原材料试验详情-分页列表查询")
    @ApiOperation(value = "原材料试验详情-分页列表查询", notes = "原材料试验详情-分页列表查询")
    @PermissionData(pageComponent = "sy/testroom/YclTestDetailList")
    @GetMapping(value = "/listss")
    public Result<?> queryPageListss(YclTestDetail yclTestDetail,String sys_depart_orgcode,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if(StringUtils.isNotBlank(sys_depart_orgcode)){
            yclTestDetail.setSysOrgCode(sys_depart_orgcode+"*");
        }else{
            yclTestDetail.setSysOrgCode(loginUser.getOrgCode()+"*");
        }
        QueryWrapper<YclTestDetail> queryWrapper = QueryGenerator.initQueryWrapper(yclTestDetail, req.getParameterMap());
        Page<YclTestDetail> page = new Page<YclTestDetail>(pageNo, pageSize);
        IPage<YclTestDetail> pageList = yclTestDetailService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "原材料试验详情-分页列表查询")
    @ApiOperation(value = "原材料试验详情-分页列表查询", notes = "原材料试验详情-分页列表查询")
    @GetMapping(value = "/listTj")
    public Result<?> queryPageListTj(YclTestDetail yclTestDetail,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        // 接口为云检
        if(ObjectUtil.isNotEmpty(yclTestDetail.getNotedd()) && "yunjian".equals(yclTestDetail.getNotedd()) ){
            return Result.OK(getTestRoomInfo(yclTestDetail.getStorageId(),0));
        }

        QueryWrapper<YclTestDetail> queryWrapper = QueryGenerator.initQueryWrapper(yclTestDetail, req.getParameterMap());
        queryWrapper.select("SUM( CASE WHEN test_status = 0 OR test_status = 3 THEN 1 ELSE 0 END ) AS daiwanc, " +
                " SUM( CASE WHEN  test_status > 0 AND  test_status < 3 THEN 1 ELSE 0 END ) AS tian, " +
                " SUM( CASE WHEN MONTH ( test_time ) = MONTH ( CURDATE()) AND test_status > 0 AND  test_status < 3 THEN 1 ELSE 0 END ) AS yue, " +
                " SUM( CASE WHEN WEEK ( test_time ) = WEEK ( CURDATE()) AND test_status > 0 AND  test_status < 3 THEN 1 ELSE 0 END ) AS zhou ");
//        queryWrapper.eq("1","1");
//        queryWrapper.last(" and test_time >= DATE_FORMAT(NOW(), '%Y-%m')" );
        List<Map<String, Object>> maps = yclTestDetailService.listMaps(queryWrapper);
        return Result.OK(maps);
    }

    // 获取云检关于检测室的任务和统计信息
    List<Map<String, Object>> getTestRoomInfo (String testroom,Integer a){
        QueryWrapper<SyTestroom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_type",testroom);
        SyTestroom one = syTestroomService.getOne(queryWrapper);
        if(ObjectUtil.isEmpty(one)){
            return new ArrayList<>();
        }
        JSONObject sendObject = JSONUtil.createObj();
        sendObject.set("fTestroomid",one.getYunjianid());
        String body = HttpRequest.post("http://1.14.103.201:8300/IOTManagement/SXApi/getSampleWorkScreen.do")
                .header("Content-Type", "application/json")
                .body(String.valueOf(sendObject))
                .execute()
                .body();

        JSONObject jsonObject1 = new JSONObject(body);
        JSONObject data = (JSONObject) jsonObject1.get("data");
        List<Map<String, Object>> returnlist = new ArrayList<>();
        if(a == 0){
            JSONObject task = (JSONObject) data.get("task");
            Map<String, Object> tj = new HashMap<>();
            tj.put("daiwanc",task.get("waitcount"));
            tj.put("tian",task.get("endcount"));
            tj.put("zhou",task.get("weekcount"));
            tj.put("yue",task.get("monthcount"));
            returnlist.add(tj);

        }else{
            JSONArray wtlist = data.getJSONArray("list");
            for(Object dan : wtlist ){
                JSONObject wt = (JSONObject) dan;
                Map<String, Object> info = new HashMap<>();
                info.put("sampleNumber",wt.get("fSampleassignnostr"));//样品编号
                info.put("testName",wt.get("fSamplereportname"));//试验名称
                info.put("tester",wt.get("fSampleperson"));//试验员
                info.put("testTime",wt.get("fReportdates"));//试验日期
                info.put("samplingTime",wt.get("fReportdates"));//试验日期
                info.put("conclusion",wt.get("fReportresult"));//试验结论
                info.put("testStatus",wt.get("fTeststatus"));//完成状态
                returnlist.add(info);

            }
        }

        return returnlist;
    }

    /**
     * 分页列表查询
     *
     * @param yclTestDetail
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "原材料试验详情-分页列表查询")
    @ApiOperation(value = "原材料试验详情-分页列表查询", notes = "原材料试验详情-分页列表查询")
    @GetMapping(value = "/lists")
    public Result<?> queryPageLists(YclTestDetail yclTestDetail,
                                    String pici,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        QueryWrapper<YclTestDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("inspection_lot_number", pici);
        List<YclTestDetail> list = yclTestDetailService.list(queryWrapper);
        return Result.OK(list);
    }

    @AutoLog(value = "原材料试验-修改状态")
    @ApiOperation(value = "原材料试验-修改状态", notes = "原材料试验-修改状态")
    @Transactional
    @PostMapping(value = "/list1")
    public Result<?> updateStatus(@RequestBody YclTestDetailVo yclTestDetailVo,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                  HttpServletRequest req) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Wztaizhang wztaizhang = wztaizhangService.getByPici(yclTestDetailVo.getInspectionLotNumber());
        //根据批次号查wztaizhang数据
        if (ObjectUtil.isNotEmpty(wztaizhang)) {
            Integer testStatus = yclTestDetailVo.getTestStatus();
            //检验状态 =1 or =2（说明完成检验）
            if (testStatus == 1 || testStatus == 2) {
                YclTestTaizhang yclTestTaizhang = new YclTestTaizhang();
                String cailiaoName = yclTestTaizhangService.getCailiaoName(wztaizhang.getCailiaono());
                yclTestTaizhang.setCailiaoname(cailiaoName);
                yclTestTaizhang.setGuige(wztaizhang.getGuigexinghao());
                yclTestTaizhang.setNodetype(wztaizhang.getNodetype());
                String gongyingshangName = yclTestTaizhangService.getGongYSName(wztaizhang.getGongyingshangdanweibianma());
                yclTestTaizhang.setGongyingshang(gongyingshangName);
                yclTestTaizhang.setPici(wztaizhang.getPici());
                try {
                    yclTestTaizhang.setJinchangtime(sdf.parse(wztaizhang.getJinchangshijian()));
                } catch (ParseException e) {
                    yclTestTaizhang.setJinchangtime(yclTestDetailVo.getTestTime());
                }
                yclTestTaizhang.setCunfangplace(wztaizhang.getStoragePlace());
                yclTestTaizhang.setShuliang(wztaizhang.getJingzhongt());
                yclTestTaizhang.setUsepart(wztaizhang.getUsePart());
                yclTestTaizhang.setSysOrgCode(wztaizhang.getSysOrgCode().substring(0, 21));
                yclTestTaizhang.setCreateTime(wztaizhang.getCreateTime());
                if (yclTestDetailVo.getJianyanType() == 1) {
                    yclTestTaizhang.setReslut(testStatus == 1 ? "合格" : "不合格");
                    String report = yclTestDetailVo.getReport();
                    if (report != null) {
                        report = report.replaceAll("'", "");//去掉报告中的单引号’
                        report = report.replaceAll("\\s+", "");//去掉单引号后去点空格，\\s+：正则表达式，匹配一个或多个空格替换为空字符
                    }
                    yclTestTaizhang.setZjpdf(report);
                }
                if (yclTestDetailVo.getJianyanType() == 2) {
                    yclTestTaizhang.setCjreslut(testStatus == 1 ? "合格" : "不合格");
                    String report = yclTestDetailVo.getReport();
                    if (report != null) {
                        report = report.replaceAll("'", "");//去掉报告中的单引号’
                        report = report.replaceAll("\\s+", "");//去掉单引号后去点空格，\\s+：正则表达式，匹配一个或多个空格替换为空字符
                    }
                    yclTestTaizhang.setCjpdf(report);
                }
                YclTestTaizhang yclTestTaizhangs = yclTestTaizhangService.getOne(new QueryWrapper<YclTestTaizhang>().eq("pici", wztaizhang.getPici()));
                if (ObjectUtil.isNotEmpty(yclTestTaizhangs)) {
                    yclTestTaizhangService.updateByPici(yclTestTaizhang);
                } else {
                    yclTestTaizhangService.save(yclTestTaizhang);
                }
            }
        }


        YclTestDetail one = yclTestDetailService.selectBySampleNumber(yclTestDetailVo.getInspectionLotNumber(), yclTestDetailVo.getSampleNumber());
        YclTestDetail yclTestDetail = new YclTestDetail();
        if (one != null) {//如果数据库表有相同的批次号和样品编号，就修改状态
            String report = yclTestDetailVo.getReport();
            if (report != null) {
                report = report.replaceAll("'", "");//去掉报告中的单引号’
                report = report.replaceAll("\\s+", "");//去掉单引号后去点空格，\\s+：正则表达式，匹配一个或多个空格替换为空字符
            }

            boolean update = yclTestDetailService.updateBySampleNumber(yclTestDetailVo.getStorageId(), yclTestDetailVo.getInspectionLotNumber(), yclTestDetailVo.getTestName(),
                    yclTestDetailVo.getSamplingTime(), yclTestDetailVo.getTestTime(), yclTestDetailVo.getJianyanType(), yclTestDetailVo.getConclusion(), yclTestDetailVo.getTestStatus(),
                    yclTestDetailVo.getTester(), report, yclTestDetailVo.getCreateBy(), yclTestDetailVo.getCreateTime(), yclTestDetailVo.getUpdateTime(), yclTestDetailVo.getSampleNumber());

            if (yclTestDetailVo.getJianyanType() == 1) {//自检
                yclTestDetailService.updateJYStateByPici(yclTestDetailVo.getTestStatus(), yclTestDetailVo.getInspectionLotNumber());
            }
            if (yclTestDetailVo.getJianyanType() == 2) {//抽检
                yclTestDetailService.updateCJStateByPici(yclTestDetailVo.getTestStatus(), yclTestDetailVo.getInspectionLotNumber());
            }
            if (update) {
                return Result.OK("更新成功",yclTestDetail);
            } else {
                return Result.error(201, "更新失败");
            }
        } else {//如果数据库没有样品编号，新增数据，修改状态
            yclTestDetail.setStorageId(yclTestDetailVo.getStorageId());// 料仓id
            yclTestDetail.setInspectionLotNumber(yclTestDetailVo.getInspectionLotNumber());// 批次号
            yclTestDetail.setSampleNumber(yclTestDetailVo.getSampleNumber());// 样品编号
            yclTestDetail.setTestName(yclTestDetailVo.getTestName());// 试验名称
            yclTestDetail.setSamplingTime(yclTestDetailVo.getSamplingTime());// 取样时间
            yclTestDetail.setTestTime(yclTestDetailVo.getTestTime()); // 实验时间
            yclTestDetail.setJianyanType(yclTestDetailVo.getJianyanType());// 检验类型 (1:自检,2:抽检)
            yclTestDetail.setConclusion(yclTestDetailVo.getConclusion());// 试验结论
            yclTestDetail.setTestStatus(yclTestDetailVo.getTestStatus());// 试验状态 0:未检验 1:合格 2:不合格 3:检验中
            yclTestDetail.setTester(yclTestDetailVo.getTester());// 试验员
            String report = yclTestDetailVo.getReport();
            if (report != null) {
                report = report.replaceAll("'", "");//去掉报告中的单引号’
                report = report.replaceAll("\\s+", "");//去掉单引号后去点空格，\\s+：正则表达式，匹配一个或多个空格替换为空字符
            }
            yclTestDetail.setReport(report);// 报告详情
            yclTestDetail.setCreateBy(yclTestDetailVo.getCreateBy());
            yclTestDetail.setCreateTime(yclTestDetailVo.getCreateTime());
            yclTestDetail.setUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            if (yclTestDetailVo.getJianyanType() == 1) {//自检
                yclTestDetailService.updateJYStateByPici(yclTestDetailVo.getTestStatus(), yclTestDetailVo.getInspectionLotNumber());
            }
            if (yclTestDetailVo.getJianyanType() == 2) {//抽检
                yclTestDetailService.updateCJStateByPici(yclTestDetailVo.getTestStatus(), yclTestDetailVo.getInspectionLotNumber());
            }
            boolean save = yclTestDetailService.save(yclTestDetail);
            if (save) {
                return Result.OK("添加成功!", yclTestDetail);
            } else {
                return Result.error(201, "添加失败！");
            }
        }

    }

    /**
     * 添加
     *
     * @param yclTestDetail
     * @return
     */
    @AutoLog(value = "原材料试验详情-添加")
    @ApiOperation(value = "原材料试验详情-添加", notes = "原材料试验详情-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody YclTestDetail yclTestDetail) {
        yclTestDetailService.save(yclTestDetail);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param yclTestDetail
     * @return
     */
    @AutoLog(value = "原材料试验详情-编辑")
    @ApiOperation(value = "原材料试验详情-编辑", notes = "原材料试验详情-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody YclTestDetail yclTestDetail) {
        yclTestDetailService.updateById(yclTestDetail);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "原材料试验详情-通过id删除")
    @ApiOperation(value = "原材料试验详情-通过id删除", notes = "原材料试验详情-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        yclTestDetailService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "原材料试验详情-批量删除")
    @ApiOperation(value = "原材料试验详情-批量删除", notes = "原材料试验详情-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.yclTestDetailService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "原材料试验详情-通过id查询")
    @ApiOperation(value = "原材料试验详情-通过id查询", notes = "原材料试验详情-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        YclTestDetail yclTestDetail = yclTestDetailService.getById(id);
        if (yclTestDetail == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(yclTestDetail);
    }

    /**
     * 通过检验批编号查询原材料试验详情
     *
     * @param iLN
     * @return
     */
    @AutoLog(value = "原材料试验详情")
    @ApiOperation(value = "原材料试验详情", notes = "通过检验批编号查询原材料试验详情")
    @GetMapping(value = "/queryByILN")
    public Result<?> queryByILN(@RequestParam(name = "inspectionLotNumber") String iLN) {
        YclTestDetail data = yclTestDetailService.getByILN(iLN);
        if (data == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(data);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param yclTestDetail
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, YclTestDetail yclTestDetail) {
        return super.exportXls(request, yclTestDetail, YclTestDetail.class, "原材料试验详情");
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
        return super.importExcel(request, response, YclTestDetail.class);
    }

}
