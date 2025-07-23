package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.service.IDeviceMixpileHistorydataOneService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import com.trtm.iot.wzconsumetaizhang.entity.Wzconsumetaizhang;
import com.trtm.iot.wzconsumetaizhang.service.IWzconsumetaizhangService;
import com.trtm.iot.wzconsumetaizhang.vo.WzconsumetaizhangPage;
import com.trtm.iot.wzconsumetaizhangdetail.entity.WzconsumetaizhangDetail;
import com.trtm.iot.wzconsumetaizhangdetail.service.IWzconsumetaizhangDetailService;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.wzshouhuodanwei.entity.Wzshouhuodanwei;
import com.trtm.iot.wzshouhuodanwei.service.IWzshouhuodanweiService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.ImportExcelUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.YouBianCodeUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.DepartIdModel;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门表 前端控制器
 * <p>
 *
 * @Author: Steve @Since： 2019-01-22
 */
@RestController
@Api(tags = "sys-sysDepart")
@RequestMapping("/sys/sysDepart")
@Slf4j
public class SysDepartController {

    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    public RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysUserDepartService sysUserDepartService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IWzgongyingshangService wzgongyingshangService;
    @Autowired
    private IWzliaocangService wzliaocangService;
    @Autowired
    private IWzcailiaonamedictService wzcailiaonamedictService;
    @Autowired
    private IWzconsumetaizhangService wzconsumetaizhangService;
    @Autowired
    private IWzconsumetaizhangDetailService wzconsumetaizhangDetailService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IDeviceMixpileHistorydataOneService deviceMixpileHistorydataOneService;
    @Autowired
    private IWzshouhuodanweiService wzshouhuodanweiService;

    /**
     * 查询数据 查出我的部门,并以树结构数据格式响应给前端
     *
     * @return
     */
    @ApiOperation(value = "部门-分页列表查询", notes = "部门-分页列表查询")
    @RequestMapping(value = "/queryMyDeptTreeList", method = RequestMethod.GET)
    public Result<List<SysDepartTreeModel>> queryMyDeptTreeList() {
        Result<List<SysDepartTreeModel>> result = new Result<>();
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        try {
            if (oConvertUtils.isNotEmpty(user.getUserIdentity()) && user.getUserIdentity().equals(CommonConstant.USER_IDENTITY_2)) {
                //update-begin--Author:liusq  Date:20210624  for:部门查询ids为空后的前端显示问题 issues/I3UD06
                String departIds = user.getDepartIds();
                if (StringUtils.isNotBlank(departIds)) {
                    List<SysDepartTreeModel> list = sysDepartService.queryMyDeptTreeList(departIds);
                    result.setResult(list);
                }
                //update-end--Author:liusq  Date:20210624  for:部门查询ids为空后的前端显示问题 issues/I3UD06
                result.setMessage(CommonConstant.USER_IDENTITY_2.toString());
                result.setSuccess(true);
            } else {
                result.setMessage(CommonConstant.USER_IDENTITY_1.toString());
                result.setSuccess(true);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 根据组织机构类型查询看板监理标: 项目数量: 施工标:
     *
     * @return
     */
    @RequestMapping(value = "/querList", method = RequestMethod.GET)
    public Result<?> querList(String sysorgcode) {
        ArrayList<Map<String, Integer>> list1 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            QueryWrapper<SysDepart> queryWrapper = new QueryWrapper<>();
            queryWrapper.likeRight("org_code", sysorgcode);
            Map<String, Integer> map = new HashMap<>();
            if (i == 0) {
                queryWrapper.like("depart_name", "指挥部");
            } else if (i == 1) {
                queryWrapper.like("depart_name", "监");
            } else {
                queryWrapper.notLike("depart_name", "监");
                queryWrapper.like("depart_name", "标");
            }
            List<SysDepart> list = sysDepartService.list(queryWrapper);
            map.put("type", i);
            map.put("departs", list.size());
            list1.add(map);
        }
        return Result.OK(list1);
    }

    /**
     * 中铁十五局三公司智慧拌合站
     *
     * @return
     */
    @RequestMapping(value = "/querList15", method = RequestMethod.GET)
    public Result<?> querList15(String sysorgcode) {
        ArrayList<Map<String, Integer>> list1 = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        QueryWrapper<ShebeiInfo> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.likeRight("sys_org_code", sysorgcode);
        List<ShebeiInfo> list2 = shebeiInfoService.list(queryWrapper1);

        QueryWrapper<ShebeiInfo> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.likeRight("sys_org_code", sysorgcode);
        queryWrapper3.ne("status", 0);
        List<ShebeiInfo> list3 = shebeiInfoService.list(queryWrapper3);

        QueryWrapper<SysDepart> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("org_code", sysorgcode);
        queryWrapper.likeRight("org_type", 4);
        List<SysDepart> list = sysDepartService.list(queryWrapper);

        map.put("xms", list.size());
        map.put("sbs", list2.size());
        map.put("sbszx", list3.size());
        list1.add(map);
        return Result.OK(list1);
    }

    /**
     * 根据组织机构类型查询出所有的组织机构()
     *
     * @return
     */
    @RequestMapping(value = "/querySysDepartInfoList", method = RequestMethod.GET)
    public Result<?> querySysDepartInfoList(String orgtype, String sysorgcode) {
        LambdaQueryWrapper<SysDepart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String[] split = orgtype.split(",");
        lambdaQueryWrapper.likeRight(SysDepart::getOrgCode, sysorgcode);
        lambdaQueryWrapper.and(i -> {
            if (split.length > 0) {
                i.eq(SysDepart::getOrgCategory, split[0]);
                for (int a = 1; a < split.length; a++) {
                    i.or().eq(SysDepart::getOrgCategory, split[a]);
                }
            }
        });
        lambdaQueryWrapper.orderByAsc(SysDepart::getOrgCode);
        List<SysDepart> list = sysDepartService.list(lambdaQueryWrapper);
        return Result.OK(list);
    }

    /**
     * 根据组织机构类型查询出所有的组织机构(项目名称)
     *
     * @return
     */
    @RequestMapping(value = "/querySysDepartList", method = RequestMethod.GET)
    public Result<?> querySysDepartList() {
        LambdaQueryWrapper<SysDepart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysDepart::getOrgCategory, 3);
        List<SysDepart> list = sysDepartService.list(lambdaQueryWrapper);
        return Result.OK(list);
    }

    /**
     * 根据组织机构id 查询出org_code
     *
     * @return
     */
    @RequestMapping(value = "/querySysDepartOrgCode", method = RequestMethod.GET)
    public Result<?> querySysDepartOrgCode(String id) {
        LambdaQueryWrapper<SysDepart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (id != null) {
            String[] split = id.split(",");
            lambdaQueryWrapper.in(SysDepart::getId, split);
        }
        List<SysDepart> list = sysDepartService.list(lambdaQueryWrapper);
        return Result.OK(list);
    }

    /**
     * 查询数据 查出所有部门,并以树结构数据格式响应给前端
     *
     * @return
     */
    @RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
    public Result<List<SysDepartTreeModel>> queryTreeList() {
        Result<List<SysDepartTreeModel>> result = new Result<>();
        try {
            // 从内存中读取
//			List<SysDepartTreeModel> list =FindsDepartsChildrenUtil.getSysDepartTreeList();
//			if (CollectionUtils.isEmpty(list)) {
//				list = sysDepartService.queryTreeList();
//			}
            List<SysDepartTreeModel> list = sysDepartService.queryTreeList();
            result.setResult(list);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 查询数据 查出所有部门,并以树结构数据格式响应给前端
     *
     * @return
     */
    @RequestMapping(value = "/queryTreeListByUserOrgCode", method = RequestMethod.GET)
    public Result<List<SysDepartTreeModel>> queryTreeListByOrgCode() {
        Result<List<SysDepartTreeModel>> result = new Result<>();
        try {
            // 从内存中读取
//			List<SysDepartTreeModel> list =FindsDepartsChildrenUtil.getSysDepartTreeList();
//			if (CollectionUtils.isEmpty(list)) {
//				list = sysDepartService.queryTreeList();
//			}
            List<SysDepartTreeModel> list = sysDepartService.queryTreeListByOrgCode();
            result.setResult(list);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 异步查询部门list
     *
     * @return
     */
    @RequestMapping(value = "/queryDepartTreeSync", method = RequestMethod.GET)
    public Result<List<SysDepartTreeModel>> queryDepartTreeSync(@RequestParam(name = "pid", required = false) String parentId) {
        Result<List<SysDepartTreeModel>> result = new Result<>();
        try {
            List<SysDepartTreeModel> list = sysDepartService.queryTreeListByPid(parentId);
            result.setResult(list);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 获取某个部门的所有父级部门的ID
     *
     * @param departId 根据departId查
     * @param orgCode  根据orgCode查，departId和orgCode必须有一个不为空
     */
    @GetMapping("/queryAllParentId")
    public Result queryParentIds(
            @RequestParam(name = "departId", required = false) String departId,
            @RequestParam(name = "orgCode", required = false) String orgCode
    ) {
        try {
            JSONObject data;
            if (oConvertUtils.isNotEmpty(departId)) {
                data = sysDepartService.queryAllParentIdByDepartId(departId);
            } else if (oConvertUtils.isNotEmpty(orgCode)) {
                data = sysDepartService.queryAllParentIdByOrgCode(orgCode);
            } else {
                return Result.error("departId 和 orgCode 不能都为空！");
            }
            return Result.OK(data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 添加新数据 添加用户新建的部门对象数据,并保存到数据库
     *
     * @param sysDepart
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @CacheEvict(value = {CacheConstant.SYS_DEPARTS_CACHE, CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries = true)
    public Result<SysDepart> add(@RequestBody SysDepart sysDepart, HttpServletRequest request) {
        Result<SysDepart> result = new Result<SysDepart>();
        String username = JwtUtil.getUserNameByToken(request);
        try {
            sysDepart.setCreateBy(username);
            sysDepartService.saveDepartData(sysDepart, username);
            //清除部门树内存
            // FindsDepartsChildrenUtil.clearSysDepartTreeList();
            // FindsDepartsChildrenUtil.clearDepartIdModel();
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑数据 编辑部门的部分数据,并保存到数据库
     *
     * @param sysDepart
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    @CacheEvict(value = {CacheConstant.SYS_DEPARTS_CACHE, CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries = true)
    public Result<SysDepart> edit(@RequestBody SysDepart sysDepart, HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        sysDepart.setUpdateBy(username);
        Result<SysDepart> result = new Result<SysDepart>();
        SysDepart sysDepartEntity = sysDepartService.getById(sysDepart.getId());
        if (sysDepartEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysDepartService.updateDepartDataById(sysDepart, username);
            // TODO 返回false说明什么？
            if (ok) {
                //清除部门树内存
                //FindsDepartsChildrenUtil.clearSysDepartTreeList();
                //FindsDepartsChildrenUtil.clearDepartIdModel();
                result.success("修改成功!");
            }
        }
        return result;
    }

    @AutoLog(value = "原材料收料主表-料仓查询")
    @ApiOperation(value = "原材料收料主表-料仓查询", notes = "原材料收料主表-料仓查询")
    @GetMapping(value = "/lclist")
    public Result<?> lcPageList(Wzycljinchanggb wzycljinchanggb, String shebeibianhao) {
//		String shebeino = wzycljinchanggb.getShebeibianhao();
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeibianhao);
        String sysorgcode = selectshebeione.getSysOrgCode();
        SysDepart queryone = sysDepartService.queryone(sysorgcode);
        String departid = queryone.getId();
        List<Wzliaocang> lcList = wzliaocangService.lclist(departid);
        return Result.OK(lcList);
    }

    /**
     * 分页列表查询
     *
     * @param
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "原材料收料主表-收货单位查询")
    @ApiOperation(value = "原材料收料主表-收货单位查询", notes = "原材料收料主表-收货单位查询")
    @GetMapping(value = "/shouhuolist")
    public Result<?> queryPageshouhuoList(String shebeibianhao,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                          HttpServletRequest req) {
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeibianhao);
        String sysorgcode = selectshebeione.getSysOrgCode();
        SysDepart queryone = sysDepartService.queryone(sysorgcode);
        String departid = queryone.getId();
        List<Wzshouhuodanwei> pageList = wzshouhuodanweiService.getdatalist(departid);
        return Result.OK(pageList);
    }

    @AutoLog(value = "原材料收料主表-材料查询")
    @ApiOperation(value = "原材料收料主表-材料查询", notes = "原材料收料主表-材料查询")
    @GetMapping(value = "/cailiaolist")
    public Result<?> cailiaoPageList(String shebeibianhao) {
//		String shebeino = wzycljinchanggb.getShebeibianhao();
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeibianhao);
        String sysorgcode = selectshebeione.getSysOrgCode();
        SysDepart queryone = sysDepartService.queryone(sysorgcode);
        String departid = queryone.getId();
        List<Wzcailiaonamedict> cailiaoList = wzcailiaonamedictService.cailiaolist(departid);
        return Result.OK(cailiaoList);
    }

    @AutoLog(value = "原材料收料主表-材料查询")
    @ApiOperation(value = "原材料收料主表-材料查询", notes = "原材料收料主表-材料查询")
    @GetMapping(value = "/cailiaolist1")
    public Result<?> cailiaoPageList1(String shebeibianhao, String lmcailiaolx) {
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeibianhao);
        String sysorgcode = selectshebeione.getSysOrgCode();
        SysDepart queryone = sysDepartService.queryone(sysorgcode);
        String departid = queryone.getId();
        String[] split = lmcailiaolx.split(",");
        List<Integer> list = new ArrayList<>();
        for (String s : split) {
            list.add(Integer.valueOf(s));
        }
        List<Wzcailiaonamedict> cailiaoList = wzcailiaonamedictService.cailiaolists(departid, list);
        return Result.OK(cailiaoList);
    }

    @AutoLog(value = "原材料收料主表-供应商查询")
    @ApiOperation(value = "原材料收料主表-供应商查询", notes = "原材料收料主表-供应商查询")
    @GetMapping(value = "/gongyingshanglist")
    public Result<?> gongyingshangPageList(Wzycljinchanggb wzycljinchanggb, String shebeibianhao) {
//		String shebeino = wzycljinchanggb.getShebeibianhao();
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeibianhao);
        String sysorgcode = selectshebeione.getSysOrgCode();
        SysDepart queryone = sysDepartService.queryone(sysorgcode);
        String departid = queryone.getId();
        List<Wzgongyingshang> gysList = wzgongyingshangService.gyslist(departid);
        return Result.OK(gysList);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @CacheEvict(value = {CacheConstant.SYS_DEPARTS_CACHE, CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries = true)
    public Result<SysDepart> delete(@RequestParam(name = "id", required = true) String id) {

        Result<SysDepart> result = new Result<SysDepart>();
        SysDepart sysDepart = sysDepartService.getById(id);
        if (sysDepart == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysDepartService.delete(id);
            if (ok) {
                //清除部门树内存
                //FindsDepartsChildrenUtil.clearSysDepartTreeList();
                // FindsDepartsChildrenUtil.clearDepartIdModel();
                result.success("删除成功!");
            }
        }
        return result;
    }


    /**
     * 批量删除 根据前端请求的多个ID,对数据库执行删除相关部门数据的操作
     *
     * @param ids
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    @CacheEvict(value = {CacheConstant.SYS_DEPARTS_CACHE, CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries = true)
    public Result<SysDepart> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {

        Result<SysDepart> result = new Result<SysDepart>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.sysDepartService.deleteBatchWithChildren(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 查询数据 添加或编辑页面对该方法发起请求,以树结构形式加载所有部门的名称,方便用户的操作
     *
     * @return
     */
    @RequestMapping(value = "/queryIdTree", method = RequestMethod.GET)
    public Result<List<DepartIdModel>> queryIdTree() {
//		Result<List<DepartIdModel>> result = new Result<List<DepartIdModel>>();
//		List<DepartIdModel> idList;
//		try {
//			idList = FindsDepartsChildrenUtil.wrapDepartIdModel();
//			if (idList != null && idList.size() > 0) {
//				result.setResult(idList);
//				result.setSuccess(true);
//			} else {
//				sysDepartService.queryTreeList();
//				idList = FindsDepartsChildrenUtil.wrapDepartIdModel();
//				result.setResult(idList);
//				result.setSuccess(true);
//			}
//			return result;
//		} catch (Exception e) {
//			log.error(e.getMessage(),e);
//			result.setSuccess(false);
//			return result;
//		}
        Result<List<DepartIdModel>> result = new Result<>();
        try {
            List<DepartIdModel> list = sysDepartService.queryDepartIdTreeList();
            result.setResult(list);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * <p>
     * 部门搜索功能方法,根据关键字模糊搜索相关部门
     * </p>
     *
     * @param keyWord
     * @return
     */
    @RequestMapping(value = "/searchBy", method = RequestMethod.GET)
    public Result<List<SysDepartTreeModel>> searchBy(@RequestParam(name = "keyWord", required = true) String keyWord, @RequestParam(name = "myDeptSearch", required = false) String myDeptSearch) {
        Result<List<SysDepartTreeModel>> result = new Result<List<SysDepartTreeModel>>();
        //部门查询，myDeptSearch为1时为我的部门查询，登录用户为上级时查只查负责部门下数据
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String departIds = null;
        if (oConvertUtils.isNotEmpty(user.getUserIdentity()) && user.getUserIdentity().equals(CommonConstant.USER_IDENTITY_2)) {
            departIds = user.getDepartIds();
        }
        List<SysDepartTreeModel> treeList = this.sysDepartService.searhBy(keyWord, myDeptSearch, departIds);
        if (treeList == null || treeList.size() == 0) {
            result.setSuccess(false);
            result.setMessage("未查询匹配数据！");
            return result;
        }
        result.setResult(treeList);
        return result;
    }


    /**
     * 导出excel
     *
     * @param request
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(SysDepart sysDepart, HttpServletRequest request) {
        // Step.1 组装查询条件
        QueryWrapper<SysDepart> queryWrapper = QueryGenerator.initQueryWrapper(sysDepart, request.getParameterMap());
        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<SysDepart> pageList = sysDepartService.list(queryWrapper);
        //按字典排序
        Collections.sort(pageList, new Comparator<SysDepart>() {
            @Override
            public int compare(SysDepart arg0, SysDepart arg1) {
                return arg0.getOrgCode().compareTo(arg1.getOrgCode());
            }
        });
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "部门列表");
        mv.addObject(NormalExcelConstants.CLASS, SysDepart.class);
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("部门列表数据", "导出人:" + user.getRealname(), "导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    @CacheEvict(value = {CacheConstant.SYS_DEPARTS_CACHE, CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries = true)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<String> errorMessageList = new ArrayList<>();
        List<SysDepart> listSysDeparts = null;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                // orgCode编码长度
                int codeLength = YouBianCodeUtil.zhanweiLength;
                listSysDeparts = ExcelImportUtil.importExcel(file.getInputStream(), SysDepart.class, params);
				// 筛选不为空的记录
				listSysDeparts = listSysDeparts.stream()
						.filter(depart -> depart.getDepartName() != null && !depart.getDepartName().isEmpty())
						.collect(Collectors.toList());
                //按长度排序
                Collections.sort(listSysDeparts, new Comparator<SysDepart>() {
                    @Override
                    public int compare(SysDepart arg0, SysDepart arg1) {

                        return arg0.getOrgCode().length() - arg1.getOrgCode().length();
                    }
                });

                int num = 0;
                for (SysDepart sysDepart : listSysDeparts) {
                    String orgCode = sysDepart.getOrgCode();
                    if (orgCode.length() > codeLength) {
                        String parentCode = orgCode.substring(0, orgCode.length() - codeLength);
                        QueryWrapper<SysDepart> queryWrapper = new QueryWrapper<SysDepart>();
                        queryWrapper.eq("org_code", parentCode);
                        try {
                            SysDepart parentDept = sysDepartService.getOne(queryWrapper);
                            if (!parentDept.equals(null)) {
                                sysDepart.setParentId(parentDept.getId());
                            } else {
                                sysDepart.setParentId("");
                            }
                        } catch (Exception e) {
                            //没有查找到parentDept
                        }
                    } else {
                        sysDepart.setParentId("");
                    }
                    //update-begin---author:liusq   Date:20210223  for：批量导入部门以后，不能追加下一级部门 #2245------------
                    sysDepart.setOrgType(sysDepart.getOrgCode().length() / codeLength + "");
                    //update-end---author:liusq   Date:20210223  for：批量导入部门以后，不能追加下一级部门 #2245------------
                    sysDepart.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
                    ImportExcelUtil.importDateSaveOne(sysDepart, ISysDepartService.class, errorMessageList, num, CommonConstant.SQL_INDEX_UNIQ_DEPART_ORG_CODE);
                    num++;
                }
                //清空部门缓存
                Set keys3 = redisTemplate.keys(CacheConstant.SYS_DEPARTS_CACHE + "*");
                Set keys4 = redisTemplate.keys(CacheConstant.SYS_DEPART_IDS_CACHE + "*");
                redisTemplate.delete(keys3);
                redisTemplate.delete(keys4);
                return ImportExcelUtil.imporReturnRes(errorMessageList.size(), listSysDeparts.size() - errorMessageList.size(), errorMessageList);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }


    /**
     * 查询所有部门信息
     *
     * @return
     */
    @GetMapping("listAll")
    public Result<List<SysDepart>> listAll(@RequestParam(name = "id", required = false) String id) {
        Result<List<SysDepart>> result = new Result<>();
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        query.orderByAsc(SysDepart::getOrgCode);
        if (oConvertUtils.isNotEmpty(id)) {
            String arr[] = id.split(",");
            query.in(SysDepart::getId, arr);
        }
        List<SysDepart> ls = this.sysDepartService.list(query);
        result.setSuccess(true);
        result.setResult(ls);
        return result;
    }

    /**
     * 查询数据 查出所有部门,并以树结构数据格式响应给前端
     *
     * @return
     */
    @RequestMapping(value = "/queryTreeByKeyWord", method = RequestMethod.GET)
    public Result<Map<String, Object>> queryTreeByKeyWord(@RequestParam(name = "keyWord", required = false) String keyWord) {
        Result<Map<String, Object>> result = new Result<>();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            List<SysDepartTreeModel> list = sysDepartService.queryTreeByKeyWord(keyWord);
            //根据keyWord获取用户信息
            LambdaQueryWrapper<SysUser> queryUser = new LambdaQueryWrapper<SysUser>();
            queryUser.eq(SysUser::getDelFlag, CommonConstant.DEL_FLAG_0);
            queryUser.and(i -> i.like(SysUser::getUsername, keyWord).or().like(SysUser::getRealname, keyWord));
            List<SysUser> sysUsers = this.sysUserService.list(queryUser);
            map.put("userList", sysUsers);
            map.put("departList", list);
            result.setResult(map);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 根据部门编码获取部门信息
     *
     * @param orgCode
     * @return
     */
    @GetMapping("/getDepartName")
    public Result<SysDepart> getDepartName(@RequestParam(name = "orgCode") String orgCode) {
        Result<SysDepart> result = new Result<>();
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        query.eq(SysDepart::getOrgCode, orgCode);
        SysDepart sysDepart = sysDepartService.getOne(query);
        result.setSuccess(true);
        result.setResult(sysDepart);
        return result;
    }

    /**
     * 根据部门id获取用户信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getUsersByDepartId")
    public Result<List<SysUser>> getUsersByDepartId(@RequestParam(name = "id") String id) {
        Result<List<SysUser>> result = new Result<>();
        List<SysUser> sysUsers = sysUserDepartService.queryUserByDepId(id);
        result.setSuccess(true);
        result.setResult(sysUsers);
        return result;
    }

    /**
     * 分页列表查询
     *
     * @param wzconsumetaizhang
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "物资原材料消耗台账主表信息-分页列表查询")
    @ApiOperation(value = "物资原材料消耗台账主表信息-分页列表查询", notes = "物资原材料消耗台账主表信息-分页列表查询")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(Wzconsumetaizhang wzconsumetaizhang,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            wzconsumetaizhang.setSysOrgCode("*" + sys_depart_orgcode + "*");
        }
        QueryWrapper<Wzconsumetaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wzconsumetaizhang, req.getParameterMap());
        Page<Wzconsumetaizhang> page = new Page<Wzconsumetaizhang>(pageNo, pageSize);
        IPage<Wzconsumetaizhang> pageList = wzconsumetaizhangService.page(page, queryWrapper);
        List<Wzconsumetaizhang> records = pageList.getRecords();
        List records1 = new ArrayList<>();
        Map map = new HashMap();
        for (Wzconsumetaizhang wzconsumetaizhang1 : records) {
            WzconsumetaizhangPage wzconsumetaizhangPage = new WzconsumetaizhangPage();
            Integer id = wzconsumetaizhang1.getId();
            String poureLocation = wzconsumetaizhang1.getPoureLocation();
            String projectName = wzconsumetaizhang1.getProjectName();
            String strengthRank = wzconsumetaizhang1.getStrengthRank();
            Double estimateNumber = wzconsumetaizhang1.getEstimateNumber();
            String sysOrgCode = wzconsumetaizhang1.getSysOrgCode();
            Date Starttim = wzconsumetaizhang1.getStarttim();
            Date EndTim = wzconsumetaizhang1.getEndtim();
            List<WzconsumetaizhangDetail> selectdetaillist = wzconsumetaizhangDetailService.selectdetaillist(id);
            if (selectdetaillist.size() > 0) {
                wzconsumetaizhangPage.setWzconsumetaizhangDetailList(selectdetaillist);
            }
            SysDepart sysDepart = sysDepartService.queryone(sysOrgCode);
            wzconsumetaizhangPage.setId(id);
            wzconsumetaizhangPage.setPoureLocation(poureLocation);
            wzconsumetaizhangPage.setProjectName(projectName);
            wzconsumetaizhangPage.setStrengthRank(strengthRank);
            wzconsumetaizhangPage.setEstimateNumber(estimateNumber);
            wzconsumetaizhangPage.setSysOrgCode(sysDepart.getDepartName());
            wzconsumetaizhangPage.setStarttim(Starttim);
            wzconsumetaizhangPage.setEndtim(EndTim);
            records1.add(wzconsumetaizhangPage);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", records1);
        return Result.OK(map);
    }

    /**
     * 软基当月超标统计
     *
     * @return
     */
    @GetMapping(value = "/countList")
    public Result<?> countList(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne, HttpServletRequest req, String sbtypes, String org_category, String org_code) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = sbtypes.split(",");
        List list = new ArrayList();
        for (int i = 0; i < split.length; i++) {
            list.add(split[i]);
        }
        QueryWrapper<SysDepart> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("depart_name,org_code");
        queryWrapper.eq("org_category", org_category);
        queryWrapper.likeRight("org_code", org_code);
        List<SysDepart> lists1 = sysDepartService.list(queryWrapper);
        List lists = new ArrayList();
        for (SysDepart sysDepart : lists1) {
            Map map = new HashMap();
            String orgCode = sysDepart.getOrgCode();
            String departName = sysDepart.getDepartName();
            List<ShebeiInfo> shebeiInfos = shebeiInfoService.usershebeiList(orgCode, list);
            String shebeis = null;
            if (shebeiInfos.size() > 0) {
                shebeis = org.apache.commons.lang.StringUtils.join(shebeiInfos.stream().map(ShebeiInfo::getSbjno).toArray(), ",");
            }
            if (!"null".equals(shebeis)) {
                deviceMixpileHistorydataOne.setShebeino(shebei);
            } else {
                deviceMixpileHistorydataOne.setShebeino("空");
            }
            // 超标条数
            QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper1 = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
            queryWrapper1.select("count(chaobiaodengji) as chaobiaodengji");
            queryWrapper1.gt("chaobiaodengji", 0);
            DeviceMixpileHistorydataOne one = deviceMixpileHistorydataOneService.getOne(queryWrapper1);
            //总条数
            QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper2 = QueryGenerator.initQueryWrapper(deviceMixpileHistorydataOne, req.getParameterMap());
            queryWrapper2.select("count(chaobiaodengji) as chaobiaodengji");
            DeviceMixpileHistorydataOne two = deviceMixpileHistorydataOneService.getOne(queryWrapper2);
            if (one != null) {
                Integer chaobiaodengji = one.getChaobiaodengji();
                Integer chaobiaodengji1 = two.getChaobiaodengji();
                if (chaobiaodengji == 0) {
                    map.put("chaobiaolv", 0);
                    map.put("depart_name", departName);
                    map.put("chaobiao", chaobiaodengji);
                    map.put("jbzcount", chaobiaodengji1);
                } else {
                    double chaobiaolv = chaobiaodengji.doubleValue() / chaobiaodengji1.doubleValue() * 100;
                    DecimalFormat decimalFormat = new DecimalFormat("#.00");
                    String str = decimalFormat.format(chaobiaolv);
                    map.put("chaobiaolv", str);
                    map.put("depart_name", departName);
                    map.put("chaobiao", chaobiaodengji);
                    map.put("jbzcount", chaobiaodengji1);
                }
            } else {
                map.put("chaobiaolv", 0);
                map.put("depart_name", departName);
                map.put("chaobiao", 0);
                map.put("jbzcount", 0);
            }
            lists.add(map);
        }
        return Result.OK(lists);//deviceMixpileHistorydataOneService.countList()
    }

    @AutoLog(value = "wztaizhang-项目级原材及产品组织机构")
    @ApiOperation(value = "wztaizhang-项目级原材及产品组织机构", notes = "wztaizhang-项目级原材及产品组织机构")
    @GetMapping(value = "/queryList13")
    public Result<?> queryList13(String orgtype, String sysorgcode) {
        LambdaQueryWrapper<SysDepart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String[] split = orgtype.split(",");
        lambdaQueryWrapper.likeRight(SysDepart::getOrgCode, sysorgcode);
        lambdaQueryWrapper.and(i -> {
            if (split.length > 0) {
                i.eq(SysDepart::getOrgCategory, split[0]);
                for (int a = 1; a < split.length; a++) {
                    i.or().eq(SysDepart::getOrgCategory, split[a]);
                }
            }
        });
        List<SysDepart> list = sysDepartService.list(lambdaQueryWrapper);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDepartName() != null) {
                String cailiaoName = list.get(i).getDepartName().toString();
                if (cailiaoName.equals("瑞苍项目")) {
                    SysDepart sysDepart = new SysDepart();
                    sysDepart = list.get(0);
                    list.set(0, list.get(i));
                    list.set(i, sysDepart);
                    break;
                }
            }
        }
        return Result.OK(list);
    }

    /**
     * 物料管理出库的项目,根据组织机构类型查询出所有的组织机构()
     *
     * @return
     */
    @AutoLog(value = "物料管理出库的项目")
    @ApiOperation(value = "物料管理出库的项目", notes = "物料管理出库的项目")
    @RequestMapping(value = "/queryCaiLiaoSysDepartInfoList", method = RequestMethod.GET)
    public Result<?> queryCaiLiaoSysDepartInfoList(String orgCategory, String sysorgcode) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String orgCode = loginUser.getOrgCode();
        LambdaQueryWrapper<SysDepart> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.likeRight(SysDepart::getOrgCode, orgCode);
        //项目用sys_depart表 org_category=4表示项目
        lambdaQueryWrapper.eq(SysDepart::getOrgCategory, orgCategory);

        List<SysDepart> list = sysDepartService.list(lambdaQueryWrapper);
        return Result.OK(list);
    }
}















