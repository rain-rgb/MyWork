package org.jeecg.modules.system.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.enums.RoleIndexConfigEnum;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.MD5Util;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysAppPermission;
import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.entity.SysRoleAppPermission;
import org.jeecg.modules.system.entity.SysRolePermission;
import org.jeecg.modules.system.model.SysAppPermissionTree;
import org.jeecg.modules.system.model.SysPermissionTree;
import org.jeecg.modules.system.model.TreeModel;
import org.jeecg.modules.system.service.ISysAppPermissionService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.service.ISysRoleAppPermissionService;
import org.jeecg.modules.system.util.PermissionDataUtil;
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
 * @Description: app菜单数据
 * @Author: jeecg-boot
 * @Date: 2022-02-16
 * @Version: V1.0
 */
@Api(tags = "app菜单数据")
@RestController
@RequestMapping("/system/sysAppPermission")
@Slf4j
public class SysAppPermissionController extends JeecgController<SysAppPermission, ISysAppPermissionService> {
    @Autowired
    private ISysAppPermissionService sysAppPermissionService;
    @Autowired
    private ISysRoleAppPermissionService sysRoleAppPermissionService;


    /**
     * 查询加载数据节点
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<List<SysAppPermissionTree>> list() {
        long start = System.currentTimeMillis();
        Result<List<SysAppPermissionTree>> result = new Result<>();
        try {
            LambdaQueryWrapper<SysAppPermission> query = new LambdaQueryWrapper<SysAppPermission>();
            query.eq(SysAppPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
            query.orderByAsc(SysAppPermission::getSortNo);
            List<SysAppPermission> list = sysAppPermissionService.list(query);
            List<SysAppPermissionTree> treeList = new ArrayList<>();
            getTreeList(treeList, list, null);
            result.setResult(treeList);
            result.setSuccess(true);
            log.info("======获取全部菜单数据=====耗时:" + (System.currentTimeMillis() - start) + "毫秒");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    private void getTreeList(List<SysAppPermissionTree> treeList, List<SysAppPermission> metaList, SysAppPermissionTree temp) {
        for (SysAppPermission permission : metaList) {
            String tempPid = permission.getParentId();
            SysAppPermissionTree tree = new SysAppPermissionTree(permission);
            if (temp == null && oConvertUtils.isEmpty(tempPid)) {
                treeList.add(tree);
                if (!tree.getLeaf()) {
                    getTreeList(treeList, metaList, tree);
                }
            } else if (temp != null && tempPid != null && tempPid.equals(temp.getId())) {
                temp.getChildren().add(tree);
                if (!tree.getLeaf()) {
                    getTreeList(treeList, metaList, tree);
                }
            }

        }
    }


    /**
     * 添加菜单
     *
     * @param permission
     * @return
     */
    //@RequiresRoles({ "admin" })
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<SysAppPermission> add(@RequestBody SysAppPermission permission) {
        Result<SysAppPermission> result = new Result<SysAppPermission>();
        try {
            // permission = PermissionDataUtil.intelligentProcessData(permission);
            sysAppPermissionService.addPermission(permission);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 获取全部的权限树
     *
     * @return
     */
    @RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
    public Result<Map<String, Object>> queryTreeList() {
        Result<Map<String, Object>> result = new Result<>();
        // 全部权限ids
        List<String> ids = new ArrayList<>();
        try {
            LambdaQueryWrapper<SysAppPermission> query = new LambdaQueryWrapper<SysAppPermission>();
            query.eq(SysAppPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
            query.orderByAsc(SysAppPermission::getSortNo);
            List<SysAppPermission> list = sysAppPermissionService.list(query);
            for (SysAppPermission sysPer : list) {
                ids.add(sysPer.getId());
            }
            List<TreeModel> treeList = new ArrayList<>();
            getTreeModelList(treeList, list, null);

            Map<String, Object> resMap = new HashMap<String, Object>();
            resMap.put("treeList", treeList); // 全部树节点数据
            resMap.put("ids", ids);// 全部树ids
            result.setResult(resMap);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }


    /**
     * 编辑app权限
     *
     * @param permission
     * @return
     */
    //@RequiresRoles({ "admin" })
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<SysAppPermission> edit(@RequestBody SysAppPermission permission) {
        Result<SysAppPermission> result = new Result<>();
        try {
            //permission = PermissionDataUtil.intelligentProcessData(permission);
            sysAppPermissionService.editPermission(permission);
            result.success("修改成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }


    /**
     * 查询角色授权
     *
     * @return
     */
    @RequestMapping(value = "/queryRolePermission", method = RequestMethod.GET)
    public Result<List<String>> queryRolePermission(@RequestParam(name = "roleId", required = true) String roleId) {
        Result<List<String>> result = new Result<>();
        try {
            List<SysRoleAppPermission> list = sysRoleAppPermissionService.list(new QueryWrapper<SysRoleAppPermission>().lambda().eq(SysRoleAppPermission::getRoleId, roleId));
            result.setResult(list.stream().map(SysRolePermission -> String.valueOf(SysRolePermission.getPermissionId())).collect(Collectors.toList()));
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }


    /**
     * 保存角色授权
     *
     * @return
     */
    @RequestMapping(value = "/saveRolePermission", method = RequestMethod.POST)
    //@RequiresRoles({ "admin" })
    public Result<String> saveRolePermission(@RequestBody JSONObject json) {
        long start = System.currentTimeMillis();
        Result<String> result = new Result<>();
        try {
            String roleId = json.getString("roleId");
            String permissionIds = json.getString("permissionIds");
            String lastPermissionIds = json.getString("lastpermissionIds");
            this.sysRoleAppPermissionService.saveRolePermission(roleId, permissionIds, lastPermissionIds);
            result.success("保存成功！");
            log.info("======角色授权成功=====耗时:" + (System.currentTimeMillis() - start) + "毫秒");
        } catch (Exception e) {
            result.error500("授权失败！");
            log.error(e.getMessage(), e);
        }
        return result;
    }


    /**
     * 查询用户拥有的菜单权限和按钮权限
     *
     * @return
     */
    @RequestMapping(value = "/getUserAppPermissionByToken", method = RequestMethod.GET)
    public Result<?> getUserPermissionByToken() {
        Result<JSONObject> result = new Result<JSONObject>();
        try {
            //直接获取当前用户不适用前端token
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            if (oConvertUtils.isEmpty(loginUser)) {
                return Result.error("请登录系统！");
            }
            List<SysAppPermission> metaList = sysAppPermissionService.queryByUser(loginUser.getUsername());
            JSONObject json = new JSONObject();
            JSONArray menujsonArray = new JSONArray();
            this.getPermissionJsonArray(menujsonArray, metaList, null);
            JSONArray authjsonArray = new JSONArray();
            this.getAuthJsonArray(authjsonArray, metaList);
            JSONArray lableAuthjsonArray = new JSONArray();
            this.lableAuthjsonArray(lableAuthjsonArray, metaList, null);
            //查询所有的权限
            LambdaQueryWrapper<SysAppPermission> query = new LambdaQueryWrapper<SysAppPermission>();
            query.eq(SysAppPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
            query.eq(SysAppPermission::getMenuType, CommonConstant.MENU_TYPE_2);
            //query.eq(SysPermission::getStatus, "1");
            List<SysAppPermission> allAuthList = sysAppPermissionService.list(query);
            JSONArray allauthjsonArray = new JSONArray();
            this.getAllAuthJsonArray(allauthjsonArray, allAuthList);
            //路由菜单
            json.put("menu", menujsonArray);
            //按钮权限（用户拥有的权限集合）
            json.put("auth", authjsonArray);
            //全部权限配置集合（按钮权限，访问权限）
            json.put("allAuth", allauthjsonArray);
            json.put("lableAuth", lableAuthjsonArray);
            result.setResult(json);
            result.success("查询成功");
        } catch (Exception e) {
            result.error500("查询失败:" + e.getMessage());
            log.error(e.getMessage(), e);
        }
        return result;
    }


    /**
     * 查询用户拥有的菜单权限和按钮权限
     *
     * @return
     */
    @RequestMapping(value = "/getUserHeadPermissionByToken", method = RequestMethod.GET)
    public Result<?> getUserHeadPermissionByToken(String parentId) {
        Result<JSONObject> result = new Result<JSONObject>();
        try {
            //直接获取当前用户不适用前端token
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            if (oConvertUtils.isEmpty(loginUser)) {
                return Result.error("请登录系统！");
            }
            List<Map> metaList = sysAppPermissionService.queryByUserHead(loginUser.getUsername(),parentId);
            JSONObject json = new JSONObject();
            json.put("lableAuth", metaList);
            result.setResult(json);
            result.success("查询成功");
        } catch (Exception e) {
            result.error500("查询失败:" + e.getMessage());
            log.error(e.getMessage(), e);
        }
        return result;
    }



    private void lableAuthjsonArray(JSONArray lableAuthjsonArray, List<SysAppPermission> metaList, JSONObject parentJson) {
        for (SysAppPermission permission : metaList) {
            if (permission.getMenuType() == null) {
                continue;
            }
            String tempPid = permission.getParentId();
            if (parentJson == null && oConvertUtils.isEmpty(tempPid) && !permission.getIsLeaf()) {
                JSONObject json = getPermissionJsonObject(permission);
                lableAuthjsonArray.add(json);
                lableAuthjsonArray(lableAuthjsonArray, metaList, json);
            } else if (parentJson != null && oConvertUtils.isNotEmpty(tempPid) && permission.getIsLabel() && tempPid.equals(parentJson.getString("id"))) {
                JSONObject json = getLableJsonObject(permission);
                if (parentJson.containsKey("mate")) {
                    parentJson.getJSONArray("mate").add(json);
                } else {
                    JSONArray mate = new JSONArray();
                    mate.add(json);
                    parentJson.put("mate", mate);
                }
            }
        }
    }

    private JSONObject getLableJsonObject(SysAppPermission permission) {
        JSONObject json = new JSONObject();
        if (CommonConstant.STATUS_1.equals(permission.getStatus()) && permission.getIsLabel()) {
            json.put("title", permission.getName());
            json.put("url", permission.getMenuPerms());
            json.put("style", "background-image:url(" + permission.getImgurl() + ")");
            json.put("indexpic", "background-image:url(" + permission.getIcon() + ")");
        }
        return json;
    }



    /**
     * 获取菜单JSON数组
     *
     * @param jsonArray
     * @param metaList
     * @param parentJson
     */
    private void getPermissionJsonArray(JSONArray jsonArray, List<SysAppPermission> metaList, JSONObject parentJson) {
        for (SysAppPermission permission : metaList) {
            if (permission.getMenuType() == null) {
                continue;
            }
            String tempPid = permission.getParentId();
            JSONObject json = getPermissionJsonObject(permission);
            if (json == null) {
                continue;
            }
            if (parentJson == null && oConvertUtils.isEmpty(tempPid)) {
                jsonArray.add(json);
                if (!permission.getIsLeaf()) {
                    getPermissionJsonArray(jsonArray, metaList, json);
                }
            } else if (parentJson != null && oConvertUtils.isNotEmpty(tempPid) && tempPid.equals(parentJson.getString("id"))) {
                // 类型( 0：一级菜单 1：子菜单 2：按钮 )
                if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_2) && permission.getIsLabel()) {
//                    JSONObject metaJson = parentJson.getJSONObject("meta");
//                    if (metaJson.containsKey("permissionList")) {
//                        metaJson.getJSONArray("permissionList").add(json);
//                    } else {
//                        JSONArray permissionList = new JSONArray();
//                        permissionList.add(json);
//                        metaJson.put("permissionList", permissionList);
//                    }
                    // 类型( 0：一级菜单 1：子菜单 2：按钮 )
                } else if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_1) || permission.getMenuType().equals(CommonConstant.MENU_TYPE_0)) {
                    if (parentJson.containsKey("children")) {
                        parentJson.getJSONArray("children").add(json);
                    } else {
                        JSONArray children = new JSONArray();
                        children.add(json);
                        parentJson.put("children", children);
                    }

                    if (!permission.getIsLeaf()) {
                        getPermissionJsonArray(jsonArray, metaList, json);
                    }
                }
            }

        }
    }

    /**
     * 获取权限JSON数组
     *
     * @param jsonArray
     * @param metaList
     */
    private void getAuthJsonArray(JSONArray jsonArray, List<SysAppPermission> metaList) {
        for (SysAppPermission permission : metaList) {
            if (permission.getMenuType() == null) {
                continue;
            }
            JSONObject json = null;
            if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_2) && CommonConstant.STATUS_1.equals(permission.getStatus())) {
                json = new JSONObject();
                json.put("action", permission.getBtnPerms());
                json.put("type", permission.getPermsType());
                json.put("describe", permission.getName());
                jsonArray.add(json);
            }
        }
    }

    /**
     * 获取权限JSON数组
     *
     * @param jsonArray
     * @param allList
     */
    private void getAllAuthJsonArray(JSONArray jsonArray, List<SysAppPermission> allList) {
        JSONObject json = null;
        for (SysAppPermission permission : allList) {
            json = new JSONObject();
            json.put("action", permission.getBtnPerms());
//            json.put("actions", permission.getMenuPerms());
            json.put("status", permission.getStatus());
            //1显示2禁用
            json.put("type", permission.getPermsType());
            json.put("describe", permission.getName());
            jsonArray.add(json);
        }
    }

    /**
     * 根据菜单配置生成路由json
     *
     * @param permission
     * @return
     */
    private JSONObject getPermissionJsonObject(SysAppPermission permission) {
        JSONObject json = new JSONObject();
        // 类型(0：一级菜单 1：子菜单 2：按钮)
        if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_2) && CommonConstant.STATUS_1.equals(permission.getStatus()) && permission.getIsLabel()) {
//            json.put("action", permission.getBtnPerms());
//            json.put("type", permission.getPermsType());
//            json.put("describe", permission.getName());
//            json.put("url", permission.getMenuPerms());
//            json.put("imgurl", permission.getImgurl());
        } else if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_0) || permission.getMenuType().equals(CommonConstant.MENU_TYPE_1)) {
            json.put("id", permission.getId());
            json.put("title", permission.getName());
            json.put("url", permission.getMenuPerms());
        }

        return json;
    }

    /**
     * 删除菜单
     *
     * @param id
     * @return
     */
    //@RequiresRoles({ "admin" })
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<SysPermission> delete(@RequestParam(name = "id", required = true) String id) {
        Result<SysPermission> result = new Result<>();
        try {
            sysAppPermissionService.deletePermission(id);
            result.success("删除成功!");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500(e.getMessage());
        }
        return result;
    }


    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "app菜单数据-批量删除")
    @ApiOperation(value = "app菜单数据-批量删除", notes = "app菜单数据-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.sysAppPermissionService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "app菜单数据-通过id查询")
    @ApiOperation(value = "app菜单数据-通过id查询", notes = "app菜单数据-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SysAppPermission sysAppPermission = sysAppPermissionService.getById(id);
        if (sysAppPermission == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(sysAppPermission);
    }

    private void getTreeModelList(List<TreeModel> treeList, List<SysAppPermission> metaList, TreeModel temp) {
        for (SysAppPermission permission : metaList) {
            String tempPid = permission.getParentId();
            TreeModel tree = new TreeModel(permission);
            if (temp == null && oConvertUtils.isEmpty(tempPid)) {
                treeList.add(tree);
                if (!tree.getIsLeaf()) {
                    getTreeModelList(treeList, metaList, tree);
                }
            } else if (temp != null && tempPid != null && tempPid.equals(temp.getKey())) {
                temp.getChildren().add(tree);
                if (!tree.getIsLeaf()) {
                    getTreeModelList(treeList, metaList, tree);
                }
            }

        }
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
        return super.importExcel(request, response, SysAppPermission.class);
    }

}
