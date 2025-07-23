package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysRoleAppPermission;

/**
 * @Description: app菜单角色授权
 * @Author: jeecg-boot
 * @Date:   2022-02-16
 * @Version: V1.0
 */
public interface ISysRoleAppPermissionService extends IService<SysRoleAppPermission> {

    /**
     * 保存授权/先删后增
     * @param roleId
     * @param permissionIds
     */
    public void saveRolePermission(String roleId,String permissionIds);

    /**
     * 保存授权 将上次的权限和这次作比较 差异处理提高效率
     * @param roleId
     * @param permissionIds
     * @param lastPermissionIds
     */
    public void saveRolePermission(String roleId,String permissionIds,String lastPermissionIds);

}
