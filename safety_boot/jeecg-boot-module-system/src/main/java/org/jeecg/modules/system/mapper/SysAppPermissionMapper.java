package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.system.entity.SysAppPermission;

import java.util.List;
import java.util.Map;

/**
 * @Description: app菜单数据
 * @Author: jeecg-boot
 * @Date:   2022-02-16
 * @Version: V1.0
 */
public interface SysAppPermissionMapper extends BaseMapper<SysAppPermission> {

    @Update("update sys_app_permission set is_leaf=#{leaf} where id = #{id}")
    public int setMenuLeaf(@Param("id") String id, @Param("leaf") int leaf);

    public List<SysAppPermission> queryByUser(@Param("username") String username);
    public List<Map> queryByUserHead(@Param("username") String username,@Param("parentId") String parentId);

}
