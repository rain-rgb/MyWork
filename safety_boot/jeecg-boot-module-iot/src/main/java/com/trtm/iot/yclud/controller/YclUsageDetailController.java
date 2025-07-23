package com.trtm.iot.yclud.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.hntconsign.entity.HntConsign;
import com.trtm.iot.hntconsign.service.IHntConsignService;
import com.trtm.iot.hnthtconsign.entity.HnthtConsign;
import com.trtm.iot.hnthtconsign.service.IHnthtConsignService;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.entity.Shigongphb;
import com.trtm.iot.system.service.IBhzrenwudanService;
import com.trtm.iot.system.service.IShigongphbService;
import com.trtm.iot.trgangjinbhcm.entity.TrGangjinbhcM;
import com.trtm.iot.trgangjinbhcm.service.ITrGangjinbhcMService;
import com.trtm.iot.trhnthtm.entity.TrHnthtM;
import com.trtm.iot.trhnthtm.service.ITrHnthtMService;
import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.trtm.iot.trzhanglarenwudan.service.ITrZhanglaRenwudanService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangrenwudan.entity.TrYajiangRenwudan;
import com.trtm.iot.yajiangrenwudan.service.ITrYajiangRenwudanService;
import com.trtm.iot.yclud.entity.NodeTypePc;
import com.trtm.iot.yclud.entity.NodeTypePcGg;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import com.trtm.iot.yclud.entity.YclUsageDetail;
import com.trtm.iot.yclud.service.IYclUsageDetailService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 原材料使用详情
 * @Author: lis1
 * @Date: 2022-11-18
 * @Version: V1.0
 */
@Api(tags = "原材料使用详情")
@RestController
@RequestMapping("/yclud/yclUsageDetail")
@Slf4j
public class YclUsageDetailController extends JeecgController<YclUsageDetail, IYclUsageDetailService> {
    @Autowired
    private IYclUsageDetailService yclUsageDetailService;
    @Autowired
    private ITrZhanglaRenwudanService zhanglaRenwudanService;
    @Autowired
    private ITrZhanglaXxbService zhanglaXxbService;
    @Autowired
    private ITrYajiangRenwudanService yajiangRenwudanService;
    @Autowired
    private ITrYajiangMService yajiangMService;
    @Autowired
    private IHnthtConsignService hnthtConsignService;
    @Autowired
    private ITrHnthtMService hnthtMService;
    @Autowired
    private ITrGangjinbhcMService gangjinbhcMService;

    @Autowired
    private IBhzrenwudanService rwdService;
    @Autowired
    private IShigongphbService phbService;

    /**
     * 分页列表查询
     *
     * @param yclUsageDetail
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "原材料使用详情-分页列表查询")
    @ApiOperation(value = "原材料使用详情-分页列表查询", notes = "原材料使用详情-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(YclUsageDetail yclUsageDetail,
                                   String nodetype,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req, String sys_depart_orgcode) {
        if (oConvertUtils.isNotEmpty(yclUsageDetail.getCode())) {
            yclUsageDetail.setCode(yclUsageDetail.getCode() + "*");
        }
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            yclUsageDetail.setSysOrgCode(sys_depart_orgcode + "*");
        }
        if (oConvertUtils.isNotEmpty(yclUsageDetail.getProjectPart())) {
            yclUsageDetail.setProjectPart("*" + yclUsageDetail.getProjectPart() + "*");
        }
        List<String> cailiaoList = null;
        if (oConvertUtils.isNotEmpty(nodetype)) {
            cailiaoList = yclUsageDetailService.getCailiaoByNodetype(nodetype);
        }
        QueryWrapper<YclUsageDetail> queryWrapper = QueryGenerator.initQueryWrapper(yclUsageDetail, req.getParameterMap());
        queryWrapper.in(oConvertUtils.isNotEmpty(cailiaoList) && cailiaoList.size() > 0, "cailiaono", cailiaoList);
        Page<YclUsageDetail> page = new Page<>(pageNo, pageSize);
        IPage<YclUsageDetail> pageList = yclUsageDetailService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param yclUsageDetail
     * @return
     */
    @AutoLog(value = "原材料使用详情-添加")
    @ApiOperation(value = "原材料使用详情-添加", notes = "原材料使用详情-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody YclUsageDetail yclUsageDetail) {
//        yclUsageDetail.setDosingOrderNumber("LY-" + DateUtils.getDate("yyyyMMdd-HHmmss"));
//        yclUsageDetailService.save(yclUsageDetail);
        yclUsageDetailService.add(yclUsageDetail);
        return Result.OK("添加成功！");
    }
    @AutoLog(value = "原材料使用详情-添加-钢筋加工厂")
    @ApiOperation(value = "原材料使用详情-添加", notes = "原材料使用详情-添加")
    @PostMapping(value = "/addRebarCaiLiao")
    public Result<?> addRebarCaiLiao(@RequestBody YclUsageDetail yclUsageDetail) {
//        yclUsageDetail.setDosingOrderNumber("LY-" + DateUtils.getDate("yyyyMMdd-HHmmss"));
//        yclUsageDetailService.save(yclUsageDetail);
        yclUsageDetailService.addRebarCaiLiao(yclUsageDetail);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param yclUsageDetail
     * @return
     */
    @AutoLog(value = "原材料使用详情-编辑")
    @ApiOperation(value = "原材料使用详情-编辑", notes = "原材料使用详情-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody YclUsageDetail yclUsageDetail) {
        yclUsageDetailService.updateById(yclUsageDetail);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "原材料使用详情-通过id删除")
    @ApiOperation(value = "原材料使用详情-通过id删除", notes = "原材料使用详情-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        yclUsageDetailService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "原材料使用详情-批量删除")
    @ApiOperation(value = "原材料使用详情-批量删除", notes = "原材料使用详情-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.yclUsageDetailService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "原材料使用详情-通过id查询")
    @ApiOperation(value = "原材料使用详情-通过id查询", notes = "原材料使用详情-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        YclUsageDetail yclUsageDetail = yclUsageDetailService.getById(id);
        if (yclUsageDetail == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(yclUsageDetail);
    }

    /**
     * 跟据检验批编号查询使用详情
     *
     * @param iLN 检验批编号
     * @return YclUsageDetail
     */
    @AutoLog(value = "原材料使用详情")
    @ApiOperation(value = "原材料使用详情", notes = "跟据检验批编号查询使用详情")
    @GetMapping(value = "/queryByILN")
    public Result<?> queryByILN(@RequestParam(name = "inspectionLotNumber") String iLN) {
        YclUsageDetail date = yclUsageDetailService.getByILN(iLN);
        if (date == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(date);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param yclUsageDetail
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, YclUsageDetail yclUsageDetail) {
        return super.exportXls(request, yclUsageDetail, YclUsageDetail.class, "原材料使用详情");
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
        return super.importExcel(request, response, YclUsageDetail.class);
    }


    /**
     * 根据wbsid查询原材领用表
     *
     * @param treeId
     * @return
     */
    @GetMapping("/getYclUsa")
    @ApiOperation(value = "", notes = "")
    public Result<?> getYclUsa(@RequestParam String treeId, String nodeType) {
        treeId = getTreeId(treeId);
        List<Map> mapList = yclUsageDetailService.getYclUsa(treeId, nodeType);
        return Result.OK(mapList);
    }

    /**
     * 获取材料批次
     *
     * @param treeId
     * @return
     */
    @GetMapping("/getInspection")
    @ApiOperation(value = "", notes = "")
    public Result<?> getInspection(String treeId, String orgCode, String nodeType) {
        treeId = getTreeId(treeId);
        List<String> list = yclUsageDetailService.getInspection(treeId, orgCode, nodeType);
        return Result.OK(list);
    }

    /**
     * 根据材料批次查询对应的wbsid
     */
    @GetMapping("/getWIdBYC")
    @ApiOperation(value = "", notes = "")
    public Result<?> getWbsIdByPiCi(String pici, String orgCode) {
        List<String> list = yclUsageDetailService.getWbsIdByPiCi(pici, orgCode);
        return Result.OK(list);
    }

    /**
     * 根据wbsid查询材料批次
     */
    @GetMapping("/getPic")
    @ApiOperation(value = "", notes = "")
    public Result<?> getPic(String treeId) {
        List<String> pc = yclUsageDetailService.getPic(treeId);
        return Result.OK(pc);
    }


    /**
     * 获取材料使用表详情
     */
    @GetMapping("/getAllSy")
    @ApiOperation(value = "", notes = "")
    public Result<?> getAllSy(String treeId) {
        treeId = getTreeId(treeId);
        List list = yclUsageDetailService.getAllSy(treeId);
        return Result.OK(list);
    }

    /**
     * 获取水泥使用表详情
     */
    @GetMapping("/getSNSy")
    @ApiOperation(value = "", notes = "")
    public Result<?> getSNSy(String treeId, String nodeType, String guigexinghao, String pici) {
        treeId = getTreeId(treeId);
        List list = yclUsageDetailService.getSNSy(treeId, nodeType, guigexinghao, pici);
        return Result.OK(list);
    }

    /**
     * 获取水泥使用表详情
     */
    @GetMapping("/getSNSys")
    @ApiOperation(value = "", notes = "")
    public Result<?> getSNSys(String treeId, String nodeType, String guigexinghao, String pici, String orgCode, String startTime, String endTime, String type,
                              @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        treeId = getTreeId(treeId);
        IPage iPage = yclUsageDetailService.getSNSys(treeId, nodeType, guigexinghao, pici, orgCode, pageNo, pageSize, startTime, endTime, type);
        return Result.OK(iPage);
    }

    @GetMapping("/getDetails")
    @ApiOperation(value = "", notes = "")
    public Result<?> getDetails(String treeId, String nodeType, String guigexinghao, String pici, String orgCode, String startTime, String endTime, @RequestParam String type,
                              @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {

        if (oConvertUtils.isNotEmpty(treeId)) {
            orgCode = yclUsageDetailService.getTreeIdOrgCode(treeId);
        }
        IPage iPage = yclUsageDetailService.getDetails(treeId, nodeType, guigexinghao, pici, orgCode, pageNo, pageSize, startTime, endTime, type);
        return Result.OK(iPage);
    }

    @GetMapping("/getDetailsLc")
    @ApiOperation(value = "", notes = "")
    public Result<?> getDetailsLc(String treeId, String nodeType, String guigexinghao, String pici, String orgCode, String startTime, String endTime, @RequestParam String type,
                                @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {

        if (oConvertUtils.isNotEmpty(treeId)) {
            orgCode = yclUsageDetailService.getTreeIdOrgCode(treeId);
        }
        IPage iPage = yclUsageDetailService.getDetailsLc(treeId, nodeType, guigexinghao, pici, orgCode, pageNo, pageSize, startTime, endTime, type);
        return Result.OK(iPage);
    }

    private String getTreeId(String treeId) {
        if (oConvertUtils.isNotEmpty(treeId)) {
            if (treeId.contains("|")) {
                String[] split = treeId.split("\\|");
                int length = split.length;
                return split[length - 1];
            } else {
                return treeId;
            }
        }
        return null;
    }

    /**
     * 根据nodeType查询guigexinghao
     *
     * @param nodeType
     * @return
     * @throws ParseException
     */
    @GetMapping("/getGgXh")
    @ApiOperation(value = "", notes = "")
    public Result<?> getGgXh(String nodeType, String orgCode) {
        List<String> list = yclUsageDetailService.getGgXh(nodeType, orgCode);
        return Result.OK(list);
    }

    /**
     * 查询使用表的原材类型
     */
    @GetMapping("/getNodeType")
    public Result<?> getNodeTypeByUsa(String orgCode) {
        List<Map> list = yclUsageDetailService.getNodeTypeByUsa(orgCode);
        return Result.OK(list);
    }


    /**
     * 查询材料类型和批次，联动
     *
     * @return
     */
    @GetMapping("/getNodeTypePc")
    @ApiOperation(value = "", notes = "")
    public Result<?> getNodeTypePc(String orgCode, String treeId, String type) {
        if (oConvertUtils.isNotEmpty(treeId)) {
            orgCode = yclUsageDetailService.getTreeIdOrgCode(treeId);
        }
        List<NodeTypePc> mapList = yclUsageDetailService.getNodeTypePc(orgCode, treeId, type);
        return Result.OK(mapList);
    }


    /**
     * 查询材料类型和批次规格型号，联动
     *
     * @return
     */
    @GetMapping("/getNodeTypePcGg")
    @ApiOperation(value = "", notes = "")
    public Result<?> getNodeTypePcGg(String orgCode, String treeId, String type) {
        if (oConvertUtils.isNotEmpty(treeId)) {
            orgCode = yclUsageDetailService.getTreeIdOrgCode(treeId);
        }
        List<NodeTypePcGg> mapList = yclUsageDetailService.getNodeTypePcGg(orgCode, treeId, type);
        return Result.OK(mapList);
    }


    @AutoLog(value = "原材料使用详情-分页列表查询")
    @ApiOperation(value = "原材料使用详情-分页列表查询", notes = "原材料使用详情-分页列表查询")
    @GetMapping(value = "/productList")
    public Result<?> productList(String treeid) throws ParseException {
        List<Map> result = new ArrayList<>();
        QueryWrapper<Bhzrenwudan> queryWrapperq = new QueryWrapper<>();
        queryWrapperq.eq("treeid", treeid);
        List<Bhzrenwudan> list = rwdService.list(queryWrapperq);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map begin = new HashMap();
        begin.put("name", "混凝土浇筑");
        begin.put("time", null);
        begin.put("result1", null);//施工配比
        begin.put("result2", null);//结论
        begin.put("result3", null);//钢保护层
        begin.put("result4", null);//回弹强皮
        begin.put("syjid", null);
        if (list.size() > 0) {
            Bhzrenwudan rwd = list.get(0);
            begin.put("time", simpleDateFormat.format(rwd.getDattim()));
            QueryWrapper<Shigongphb> shigongphbQW = new QueryWrapper<>();
            shigongphbQW.eq("renwudan", rwd.getCode());
            List<Shigongphb> phblist = phbService.list(shigongphbQW);
            if(phblist.size()>0){
                begin.put("result1", phblist.get(0).getCode());
            }

        }
//        else {
//            return Result.error("未查询到数据");
//        }


        Map second = new HashMap();
        second.put("name", "预应力筋的加工和张拉");
        second.put("time", null);
        second.put("result1", null);//施工配比
        second.put("result2", null);//结论
        second.put("result3", null);//钢保护层
        second.put("result4", null);//回弹强皮
        second.put("syjid", null);
        QueryWrapper<TrZhanglaRenwudan> zhangla = new QueryWrapper<>();
        zhangla.eq("treeid", treeid);
        List<TrZhanglaRenwudan> zhanglaRenwudans = zhanglaRenwudanService.list(zhangla);
        if (zhanglaRenwudans.size() > 0) {
            TrZhanglaRenwudan zhanglaRenwudan = zhanglaRenwudans.get(0);
            QueryWrapper<TrZhanglaXxb> zhanglaXxbQueryWrapper = new QueryWrapper<>();
            zhanglaXxbQueryWrapper.eq("uuid", zhanglaRenwudan.getUuid());
            List<TrZhanglaXxb> list1 = zhanglaXxbService.list(zhanglaXxbQueryWrapper);
            if (list1.size() > 0) {
                TrZhanglaXxb trZhanglaXxb = list1.get(0);
                if (trZhanglaXxb.getOverLevel() == 0) {
                    second.put("result2", "符合设计及规范要求");
                    second.put("syjid", trZhanglaXxb.getSyjid());
                    second.put("time", simpleDateFormat.format(dateFormat.parse(trZhanglaXxb.getSgsj())));

                } else if (trZhanglaXxb.getOverLevel() != 0) {
                    second.put("result2", trZhanglaXxb.getOverReason());
                    second.put("syjid", trZhanglaXxb.getSyjid());
                    second.put("time", simpleDateFormat.format(dateFormat.parse(trZhanglaXxb.getSgsj())));
                }
            }
        }

        Map then = new HashMap();
        then.put("name", "预应力孔道压浆封锚");
        then.put("time", null);
        then.put("result1", null);//施工配比
        then.put("result2", null);//结论
        then.put("result3", null);//钢保护层
        then.put("result4", null);//回弹强皮
        then.put("syjid", null);
        QueryWrapper<TrYajiangRenwudan> yajiang = new QueryWrapper<>();
        yajiang.eq("treeid", treeid);
        List<TrYajiangRenwudan> yajiangRenwudans = yajiangRenwudanService.list(yajiang);
        if (yajiangRenwudans.size() > 0) {
            TrYajiangRenwudan yajiangRenwudan = yajiangRenwudans.get(0);
            QueryWrapper<TrYajiangM> yajiangMQueryWrapper = new QueryWrapper<>();
            yajiangMQueryWrapper.eq("uuid", yajiangRenwudan.getUuid());
            List<TrYajiangM> list1 = yajiangMService.list(yajiangMQueryWrapper);
            if (list1.size() > 0) {
                TrYajiangM trYajiangM = list1.get(0);
                if (trYajiangM.getIsOverLevel() != null && trYajiangM.getIsOverLevel() == 0 ) {
                    then.put("result2", "符合设计及规范要求");
                    then.put("syjid", trYajiangM.getSyjid());
                    then.put("time", simpleDateFormat.format(dateFormat.parse(trYajiangM.getYjsj())));
                } else if (trYajiangM.getIsOverLevel() != null && trYajiangM.getIsOverLevel() == 1) {
                    then.put("result2", trYajiangM.getOverReason());
                    then.put("syjid", trYajiangM.getSyjid());
                    then.put("time", simpleDateFormat.format(dateFormat.parse(trYajiangM.getYjsj())));
                }
            }

        }

        Map end = new HashMap();
        end.put("name", "实体检测");
        end.put("time", null);
        end.put("result1", null);//施工配比
        end.put("result2", null);//结论
        end.put("result3", null);//钢保护层
        end.put("result4", null);//回弹强皮
        end.put("syjid", null);
        QueryWrapper<HnthtConsign> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("treeid", treeid);
        List<HnthtConsign> HnthtConsigns = hnthtConsignService.list(queryWrapper);
        if (HnthtConsigns.size() > 0) {
            for (HnthtConsign hnthtConsign : HnthtConsigns) {
                if (hnthtConsign.getComponent().equals("钢筋保护层")) {
                    QueryWrapper<TrGangjinbhcM> mQueryWrapper = new QueryWrapper<>();
                    mQueryWrapper.eq("Code", hnthtConsign.getUuid());
                    List<TrGangjinbhcM> list1 = gangjinbhcMService.list();
                    if (list1.size() > 0) {
                        TrGangjinbhcM gangjinbhcM = list1.get(0);
                        end.put("result4", "合格率" + gangjinbhcM.getPassrate() + "%");
//                        end.put("time", gangjinbhcM.getChecktime());
                    }

                } else if (hnthtConsign.getComponent().equals("混凝土回弹")) {
                    QueryWrapper<TrHnthtM> hnthtMQueryWrapper = new QueryWrapper<>();
                    hnthtMQueryWrapper.eq("code", hnthtConsign.getUuid());
                    List<TrHnthtM> list1 = hnthtMService.list(hnthtMQueryWrapper);
                    if (list1.size() > 0) {
                        TrHnthtM trHnthtM = list1.get(0);
                        end.put("result3", trHnthtM.getTestingaverage() + "mpa");
                        end.put("time", trHnthtM.getPouringdate());
                    }
                }
            }
        }

        result.add(begin);
        result.add(second);
        result.add(then);
        result.add(end);
        return Result.OK(result);
    }
}
