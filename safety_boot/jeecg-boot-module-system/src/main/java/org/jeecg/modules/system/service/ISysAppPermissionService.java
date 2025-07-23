package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.system.entity.SysAppPermission;
import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.model.TreeModel;

import java.util.List;
import java.util.Map;

/**
 * @Description: app菜单数据
 * @Author: jeecg-boot
 * @Date:   2022-02-16
 * @Version: V1.0
 */
public interface ISysAppPermissionService extends IService<SysAppPermission> {

    public List<TreeModel> queryListByParentId(String parentId);

    /**真实删除*/
    public void deletePermission(String id) throws JeecgBootException;
    /**逻辑删除*/
    public void deletePermissionLogical(String id) throws JeecgBootException;

    public void addPermission(SysAppPermission sysPermission) throws JeecgBootException;

    public void editPermission(SysAppPermission sysPermission) throws JeecgBootException;

    public List<SysAppPermission> queryByUser(String username);

    public List<Map> queryByUserHead(String username, String parentId);

    /**
     * 根据permissionId删除其关联的SysPermissionDataRule表中的数据
     *
     * @param id
     * @return
     */
    public void deletePermRuleByPermId(String id);

    /**
     * 查询出带有特殊符号的菜单地址的集合
     * @return
     */
    public List<String> queryPermissionUrlWithStar();

    /**
     * 判断用户否拥有权限
     * @param username
     * @param sysPermission
     * @return
     */
    public boolean hasPermission(String username, SysAppPermission sysPermission);

    /**
     * 根据用户和请求地址判断是否有此权限
     * @param username
     * @param url
     * @return
     */
    public boolean hasPermission(String username, String url);
}
