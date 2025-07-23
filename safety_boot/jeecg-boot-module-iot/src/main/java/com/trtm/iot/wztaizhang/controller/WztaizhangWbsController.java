package com.trtm.iot.wztaizhang.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.devicepipepilehistorydataone.mapper.DevicePipepileHistorydataOneMapper;
import com.trtm.iot.hntbhz.mapper.BhzCementBaseMapper;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import com.trtm.iot.wzcailiaonamedictman.entity.WzcailiaonamedictMan;
import com.trtm.iot.wzcailiaonamedictman.service.IWzcailiaonamedictManService;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;
import com.trtm.iot.wzgongyingshangman.entity.WzgongyingshangMan;
import com.trtm.iot.wzgongyingshangman.service.IWzgongyingshangManService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import com.trtm.iot.wztaizhang.vo.*;
import com.trtm.iot.ycltd.entity.YclTestDetail;
import com.trtm.iot.ycltd.service.IYclTestDetailService;
import com.trtm.iot.ycltesttaizhang.service.IYclTestTaizhangService;
import com.trtm.iot.yclud.entity.NodeTypePcGg;
import com.trtm.iot.yclud.entity.YclUsageDetail;
import com.trtm.iot.yclud.service.IYclUsageDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * @Description: wztaizhang
 * @Author: jeecg-boot
 * @Date: 2021-06-18
 * @Version: V1.0
 */
@Api(tags = "wztaizhang")
@RestController
@RequestMapping("/wztaizhang/wztaizhangwbs")
@Slf4j
public class WztaizhangWbsController extends JeecgController<Wztaizhang, IWztaizhangService> {
    @Autowired
    private IWztaizhangService wztaizhangService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IWzcailiaonamedictService wzcailiaonamedictService;
    @Autowired
    private IWzcailiaonamedictManService wzcailiaonamedictManService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private BhzCementBaseMapper bhzCementBaseMapper;
    @Autowired
    private IYclTestDetailService yclTestDetailService;
    @Autowired
    private IWzgongyingshangService wzgongyingshangService;
    @Autowired
    private IWzgongyingshangManService wzgongyingshangManService;
    @Autowired
    private IYclUsageDetailService yclUsageDetailService;
    @Autowired
    private IWzliaocangService wzliaocangService;
    @Autowired
    DevicePipepileHistorydataOneMapper devicePipepileHistorydataOneMapper;
    @Autowired
    private IYclTestTaizhangService yclTestTaizhangService;

    /**
     * 分页列表查询(按设备等计算)
     *
     * @param wztaizhang
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wztaizhang-分页列表查询")
    @ApiOperation(value = "wztaizhang-分页列表查询", notes = "wztaizhang-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Wztaizhang wztaizhang,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   String sys_depart_orgcode,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        wztaizhang.setRuleway(0);
        //查询到他的设备编号
        if (wztaizhang.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wztaizhang.setShebeibianhao(shebei);
            } else {
                wztaizhang.setShebeibianhao("空");
            }
        }
        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());

        Page<Wztaizhang> page = new Page<Wztaizhang>(pageNo, pageSize);
        IPage<Wztaizhang> pageList = wztaizhangService.page(page, queryWrapper);
        return Result.OK(pageList);
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
        List<NodeTypePcGg> mapList = yclUsageDetailService.getNodeTypePcGgwbs(orgCode, treeId, type);
        return Result.OK(mapList);
    }



    /**
     * 瑞苍隧道看板-原材产品统计
     *
     * @param
     * @param type    原材类型(1：原材 2：产品)
     * @return
     */
    @AutoLog(value = "wztaizhang-瑞苍隧道看板-原材产品统计")
    @ApiOperation(value = "wztaizhang-瑞苍隧道看板-原材产品统计", notes = "wztaizhang-瑞苍隧道看板-原材产品统计")
    @GetMapping(value = "/getTj")
    public Result<?> getTjwbs(  String wbsId, Integer type) {
        String wbsOrgCode = wztaizhangService.getWbsOrgCode(wbsId);
        QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();
        String lastsql = " where  pici IN ( SELECT DISTINCT inspection_lot_number FROM `ycl_usage_detail` WHERE inspection_lot_number != '' AND `code` LIKE '"+wbsOrgCode+"%' )  ";

        Map<Object, Object> map = new HashMap<>();
        List<Wztaizhang> listPici = null;
        List<Wztaizhang> listBhg = null;
        if (type == 1) {//原材
            lastsql = lastsql + " AND CONVERT ( nodetype, UNSIGNED INTEGER ) < 100";
            queryWrapper.last(lastsql);
            listPici = wztaizhangService.list(queryWrapper);//筛选进场批次
            lastsql = lastsql+ " AND (jianyanstate = 2 or choujianstate = 2) ";
            queryWrapper.last(lastsql);
            listBhg = wztaizhangService.list(queryWrapper);//筛选不合格批次
        } else {//产品
            lastsql = lastsql + " AND CONVERT ( nodetype, UNSIGNED INTEGER ) > 100";
            queryWrapper.last(lastsql);
            listPici = wztaizhangService.list(queryWrapper);//筛选进场批次
            lastsql = lastsql+ " AND (jianyanstate = 2 or choujianstate = 2) ";
            queryWrapper.last(lastsql);
            listBhg = wztaizhangService.list(queryWrapper);//筛选不合格批次
        }
        if (listPici.size() == 0) {
            map.put("hglv", "-");
        } else {
            String percentStr = wztaizhangService.getPercentStr((listPici.size() - listBhg.size()), listPici.size());
            map.put("hglv", percentStr);
        }
        map.put("jcpc", listPici.size());
        map.put("bhgpc", listBhg.size());
        map.put("bhgInfo", listBhg);
        return Result.OK(map);
    }



    /**
     * 瑞苍智慧梁场看板-材料进场量、检验量、使用量、库存量
     *
     * @param orgCode 组织机构编码
     * @param type    原材类型(1：原材 2：产品)
     * @return
     */
    @AutoLog(value = "wztaizhang-瑞苍智慧梁场看板-材料进场量、检验量、使用量、库存量")
    @ApiOperation(value = "wztaizhang-瑞苍智慧梁场看板-材料进场量、检验量、使用量、库存量", notes = "wztaizhang-瑞苍智慧梁场看板-材料进场量、检验量、使用量、库存量")
    @GetMapping(value = "/getTjInfo")
    public Result<?> getTjInfowbs(String orgCode, String wbsId, Integer type) {
        try {

            String wbsOrgCode = wztaizhangService.getWbsOrgCode(wbsId);
            QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();

            List<Map> mapList = new ArrayList<>();

            queryWrapper.select(
                    " t.nodetype cllxbh, " +
                            " t.danwei dw, " +
                            " sum(t.jingzhongT)  jcl, " +
                            " sum( case WHEN t.jianyanstate>0  THEN t.jingzhongT ELSE 0  END) jyl, " +
                            " sum(u.uses) syl ");

            if (type == 1) {//原材
            queryWrapper.last(
                    " t JOIN `ycl_usage_detail` u ON u.inspection_lot_number = t.pici  " +
                            " AND inspection_lot_number != '' " +
                            " AND u.`code` LIKE '"+wbsOrgCode+"%'  " +
                            " AND t.nodetype != ''  " +
                            " WHERE " +
                            " CONVERT ( t.nodetype, UNSIGNED INTEGER ) < 100  " +
                            " GROUP BY nodetype ");
            }else{ // 产品
                queryWrapper.last(
                        " JOIN `ycl_usage_detail` u ON u.inspection_lot_number = t.pici  " +
                                " AND inspection_lot_number != '' " +
                                " AND u.`code` LIKE '"+wbsOrgCode+"%'  " +
                                " AND t.nodetype != ''  " +
                                " WHERE " +
                                " CONVERT ( t.nodetype, UNSIGNED INTEGER ) > 100  " +
                                " GROUP BY nodetype ");
            }

            List<Map<String, Object>> maps = wztaizhangService.listMaps(queryWrapper);

            for (Map<String, Object> one : maps) {

                Map<Object, Object> map = new LinkedHashMap<>();


                map.put("cllx", cailiao.get(one.get("cllxbh")) );
                map.put("cllxbh", one.get("cllxbh"));
                map.put("dw", "吨");
                map.put("jcl", String.format("%.1f", one.get("jcl")));
                map.put("jyl", String.format("%.1f", one.get("jyl")));

                map.put("syl", String.format("%.1f", one.get("syl")));
                map.put("kcl", String.format("%.1f", (Double.parseDouble(one.get("jcl").toString())  - Double.parseDouble(one.get("syl").toString()))));

                mapList.add(map);
            }
            return Result.OK(mapList);


        } catch (Exception e) {
            return Result.error("暂无数据");
        }
    }


    @GetMapping("/getDetails")
    @ApiOperation(value = "", notes = "")
    public Result<?> getDetails(String treeId, String nodeType, String guigexinghao, String pici, String orgCode, String startTime, String endTime, @RequestParam String type,
                                @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {

        if (oConvertUtils.isNotEmpty(treeId)) {
            orgCode = yclUsageDetailService.getTreeIdOrgCode(treeId);
        }
        IPage iPage = yclUsageDetailService.getDetailsWbs(treeId, nodeType, guigexinghao, pici, orgCode, pageNo, pageSize, startTime, endTime, type);
        return Result.OK(iPage);
    }

    // 材料
    private static final HashMap<String, String> cailiao = new HashMap<String, String>() {{

        put("6", "水泥");
        put("9", "外加剂");
        put("1", "细集料");
        put("8", "粉煤灰");
        put("2", "粗集料");
        put("5", "水");
        put("7", "矿粉");
        put("10", "其他原材");
        put("11", "沥青");
        put("102", "钢绞线");
        put("108", "锚夹具");
        put("109", "中空锚杆");
        put("106", "支座");
        put("107", "护栏");
        put("105", "钢立柱");
        put("104", "钢筋机械连接接头");
        put("103", "钢管");
        put("110", "波纹管");
        put("111", "防水板");
        put("112", "止水带");
        put("113", "土工布");
        put("12", "钢筋原材");
        put("114", "群锚组装件");
        put("115", "热轧光圆钢筋");
        put("116", "金属波纹管");
        put("117", "声测管");
        put("101", "其他产品");
        put("13", "水稳材料");
        put("14", "土");
        put("118", "热轧带肋钢筋");
        put("31", "矿渣粉");

    }};

}
















