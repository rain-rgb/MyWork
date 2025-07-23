package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysAppPermission;
import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.mapper.SysAppPermissionMapper;
import org.jeecg.modules.system.mapper.SysPermissionMapper;
import org.jeecg.modules.system.mapper.SysRoleAppPermissionMapper;
import org.jeecg.modules.system.model.TreeModel;
import org.jeecg.modules.system.service.ISysAppPermissionService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: app菜单数据
 * @Author: jeecg-boot
 * @Date:   2022-02-16
 * @Version: V1.0
 */
@Service
public class SysAppPermissionServiceImpl extends ServiceImpl<SysAppPermissionMapper, SysAppPermission> implements ISysAppPermissionService {
    @Resource
    private SysAppPermissionMapper sysAppPermissionMapper;
    @Resource
    private SysRoleAppPermissionMapper sysRoleAppPermissionMapper;

    @Override
    public List<TreeModel> queryListByParentId(String parentId) {
        return null;
    }

    /**
     * 真实删除
     */
    @Override
    @Transactional
    //@CacheEvict(value = CacheConstant.SYS_DATA_PERMISSIONS_CACHE,allEntries=true)
    public void deletePermission(String id) throws JeecgBootException {
        SysAppPermission sysPermission = this.getById(id);
        if(sysPermission==null) {
            throw new JeecgBootException("未找到菜单信息");
        }
        String pid = sysPermission.getParentId();
        if(oConvertUtils.isNotEmpty(pid)) {
            int count = this.count(new QueryWrapper<SysAppPermission>().lambda().eq(SysAppPermission::getParentId, pid));
            if(count==1) {
                //若父节点无其他子节点，则该父节点是叶子节点
                this.sysAppPermissionMapper.setMenuLeaf(pid, 1);
            }
        }
        sysAppPermissionMapper.deleteById(id);
        // 该节点可能是子节点但也可能是其它节点的父节点,所以需要级联删除
        this.removeChildrenBy(sysPermission.getId());
        //关联删除
        Map map = new HashMap<>();
        map.put("permission_id",id);

        //删除角色授权表
        sysRoleAppPermissionMapper.deleteByMap(map);
        //删除部门权限表
//        sysDepartPermissionMapper.deleteByMap(map);
        //删除部门角色授权
//        sysDepartRolePermissionMapper.deleteByMap(map);
    }

    private void removeChildrenBy(String parentId) {
        LambdaQueryWrapper<SysAppPermission> query = new LambdaQueryWrapper<>();
        // 封装查询条件parentId为主键,
        query.eq(SysAppPermission::getParentId, parentId);
        // 查出该主键下的所有子级
        List<SysAppPermission> permissionList = this.list(query);
        if (permissionList != null && permissionList.size() > 0) {
            String id = ""; // id
            int num = 0; // 查出的子级数量
            // 如果查出的集合不为空, 则先删除所有
            this.remove(query);
            // 再遍历刚才查出的集合, 根据每个对象,查找其是否仍有子级
            for (int i = 0, len = permissionList.size(); i < len; i++) {
                id = permissionList.get(i).getId();
                Map map = new HashMap<>();
                map.put("permission_id",id);
                //删除数据规则
                this.deletePermRuleByPermId(id);
                //删除角色授权表
                sysRoleAppPermissionMapper.deleteByMap(map);
                //删除部门权限表
//                sysDepartPermissionMapper.deleteByMap(map);
                //删除部门角色授权
//                sysDepartRolePermissionMapper.deleteByMap(map);
                num = this.count(new LambdaQueryWrapper<SysAppPermission>().eq(SysAppPermission::getParentId, id));
                // 如果有, 则递归
                if (num > 0) {
                    this.removeChildrenBy(id);
                }
            }
        }
    }

    @Override
    public void deletePermissionLogical(String id) throws JeecgBootException {

    }

    @Override
   //  @CacheEvict(value = CacheConstant.SYS_DATA_PERMISSIONS_CACHE,allEntries=true)
    public void addPermission(SysAppPermission sysPermission) throws JeecgBootException {
        //----------------------------------------------------------------------
        //判断是否是一级菜单，是的话清空父菜单
        if(CommonConstant.MENU_TYPE_0.equals(sysPermission.getMenuType())) {
            sysPermission.setParentId(null);
        }
        //----------------------------------------------------------------------
        String pid = sysPermission.getParentId();
        if(oConvertUtils.isNotEmpty(pid)) {
            //设置父节点不为叶子节点
            this.sysAppPermissionMapper.setMenuLeaf(pid, 0);
        }
        sysPermission.setCreateTime(new Date());
        sysPermission.setDelFlag(0);
        sysPermission.setIsLeaf(true);
        this.save(sysPermission);
    }

    @Override
   // @CacheEvict(value = CacheConstant.SYS_DATA_PERMISSIONS_CACHE,allEntries=true)
    public void editPermission(SysAppPermission sysPermission) throws JeecgBootException {
        SysAppPermission p = this.getById(sysPermission.getId());
        //TODO 该节点判断是否还有子节点
        if(p==null) {
            throw new JeecgBootException("未找到菜单信息");
        }else {
            sysPermission.setUpdateTime(new Date());
            //----------------------------------------------------------------------
            //Step1.判断是否是一级菜单，是的话清空父菜单ID
            if(CommonConstant.MENU_TYPE_0.equals(sysPermission.getMenuType())) {
                sysPermission.setParentId("");
            }
            //Step2.判断菜单下级是否有菜单，无则设置为叶子节点
            int count = this.count(new QueryWrapper<SysAppPermission>().lambda().eq(SysAppPermission::getParentId, sysPermission.getId()));
            if(count==0) {
                sysPermission.setIsLeaf(true);
            }
            //----------------------------------------------------------------------
            this.updateById(sysPermission);

            //如果当前菜单的父菜单变了，则需要修改新父菜单和老父菜单的，叶子节点状态
            String pid = sysPermission.getParentId();
            if((oConvertUtils.isNotEmpty(pid) && !pid.equals(p.getParentId())) || oConvertUtils.isEmpty(pid)&&oConvertUtils.isNotEmpty(p.getParentId())) {
                //a.设置新的父菜单不为叶子节点
                this.sysAppPermissionMapper.setMenuLeaf(pid, 0);
                //b.判断老的菜单下是否还有其他子菜单，没有的话则设置为叶子节点
                int cc = this.count(new QueryWrapper<SysAppPermission>().lambda().eq(SysAppPermission::getParentId, p.getParentId()));
                if(cc==0) {
                    if(oConvertUtils.isNotEmpty(p.getParentId())) {
                        this.sysAppPermissionMapper.setMenuLeaf(p.getParentId(), 1);
                    }
                }

            }
        }

    }

    @Override
    public List<SysAppPermission> queryByUser(String username) {
        return sysAppPermissionMapper.queryByUser(username);
    }

    @Override
    public List<Map> queryByUserHead(String username, String parentId) {
        return sysAppPermissionMapper.queryByUserHead(username,parentId);
    }

    @Override
    public void deletePermRuleByPermId(String id) {

    }

    @Override
    public List<String> queryPermissionUrlWithStar() {
        return null;
    }

    @Override
    public boolean hasPermission(String username, SysAppPermission sysPermission) {
        return false;
    }

    @Override
    public boolean hasPermission(String username, String url) {
        return false;
    }
}
