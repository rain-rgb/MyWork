package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysDepartproject;
import org.jeecg.modules.system.model.DepartIdModel;
import org.jeecg.modules.system.model.DepartIdprojectModel;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.model.SysDepartTreeprojectModel;

import java.util.List;

/**
 * <p>
 * 分部分项 服务实现类
 * <p>
 *
 * @Author:Steve
 * @Since：   2019-01-22
 */
public interface ISysDepartprojectService extends IService<SysDepartproject>{

    /**
     * 查询我的部门信息,并分节点进行显示
     * @return
     */
    List<SysDepartTreeprojectModel> queryMyDeptTreeList(String departIds,String parentId);

    /**
     * 查询所有部门信息,并分节点进行显示
     * @return
     */
    List<SysDepartTreeprojectModel> queryTreeList(String parentId,String orgCode);

    /**
     * 查询所有部门信息,并分节点进行显示
     * @return
     */
    List<SysDepartTreeprojectModel> queryTreeLists(String parentId);

    /**
     * 查询所有部门DepartId信息,并分节点进行显示
     * @return
     */
    public List<DepartIdprojectModel> queryDepartIdTreeList();

    /**
     * 保存部门数据
     * @param sysDepart
     */
    void saveDepartData(SysDepartproject sysDepart,String username);

    /**
     * 更新depart数据
     * @param sysDepart
     * @return
     */
    Boolean updateDepartDataById(SysDepartproject sysDepart,String username);

    /**
     * 根据关联的组织机构修改当前的分部分项所属权限 如果当前分部分项没有分配过权限则直接分配 如果修改过则会新增一份分部分项
     * @param sysDepart
     * @param username
     * @return
     */
    Boolean updateDepartDataByIds(SysDepartproject sysDepart,String username,String sysOrgCodes);
    /**
     * 根据关联的组织机构修改当前的分部分项所属权限 如果当前分部分项没有分配过权限则直接分配 如果修改过则会新增一份分部分项
     * @return
     */
	/* boolean removeDepartDataById(String id); */
    Boolean insertsave(SysDepartproject sysDepartproject,String sysOrgCodes);
    /**
     * 根据关键字搜索相关的部门数据
     * @param keyWord
     * @return
     */
    List<SysDepartTreeprojectModel> searhBy(String keyWord,String myDeptSearch,String departIds);

    /**
     * 根据部门id删除并删除其可能存在的子级部门
     * @param id
     * @return
     */
    boolean delete(String id);

    /**
     * 查询SysDepart集合
     * @param userId
     * @return
     */
	public List<SysDepartproject> queryUserDeparts(String userId);

    /**
     * 根据用户名查询部门
     *
     * @param username
     * @return
     */
    List<SysDepartproject> queryDepartsByUsername(String username);

	 /**
     * 根据部门id批量删除并删除其可能存在的子级部门
     * @param id
     * @return
     */
	void deleteBatchWithChildren(List<String> ids);

    /**
     *  根据部门Id查询,当前和下级所有部门IDS
     * @param departId
     * @return
     */
    List<String> getSubDepIdsByDepId(String departId);

    /**
     * 获取我的部门下级所有部门IDS
     * @return
     */
    List<String> getMySubDepIdsByDepId(String departIds);
    /**
     * 根据关键字获取部门信息（通讯录）
     * @return
     */
    List<SysDepartTreeprojectModel> queryTreeByKeyWord(String keyWord);
    /**
     * 根据关键字获取部门信息（项目编号——单位工程/具体部位）
     * @return
     */
    SysDepartproject selectprojname(String projgrade);

    /**
     * 根据备注查询数据
     * @param memo
     * @return
     */
    SysDepartproject selectmemo(String memo,String description );
    /**
     * 根据备注查询数据
     * @param memo
     * @return
     */
    SysDepartproject selectmemos(String memo);

    List<SysDepartproject> querylistidList(String id);

    SysDepartproject selectprojnames(String treeid);

    List<SysDepartTreeprojectModel> queryTreeListgml(String orgCode,String beiz);
}
