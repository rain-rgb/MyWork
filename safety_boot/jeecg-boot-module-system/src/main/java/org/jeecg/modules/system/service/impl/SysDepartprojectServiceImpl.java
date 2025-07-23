package org.jeecg.modules.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.iot.system.entity.ShebeiInfo;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.FillRuleConstant;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.common.util.YouBianCodeUtil;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.*;
import org.jeecg.modules.system.model.DepartIdModel;
import org.jeecg.modules.system.model.DepartIdprojectModel;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.model.SysDepartTreeprojectModel;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysDepartprojectService;
import org.jeecg.modules.system.util.FindsDepartsChildrenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 部门表 服务实现类
 * <p>
 *
 * @Author Steve
 * @Since 2019-01-22
 */
@Service
public class SysDepartprojectServiceImpl extends ServiceImpl<SysDepartprojectMapper, SysDepartproject> implements ISysDepartprojectService {

    @Autowired
    private SysUserDepartMapper userDepartMapper;
    @Autowired
    private SysDepartRoleMapper sysDepartRoleMapper;
    @Autowired
    private SysDepartPermissionMapper departPermissionMapper;
    @Autowired
    private SysDepartRolePermissionMapper departRolePermissionMapper;
    @Autowired
    private SysDepartRoleUserMapper departRoleUserMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysDepartprojectMapper sysDepartprojectMapper;

    @Override
    public List<SysDepartTreeprojectModel> queryMyDeptTreeList(String departIds,String parentId) {
        //根据部门id获取所负责部门
        LambdaQueryWrapper<SysDepartproject> query = new LambdaQueryWrapper<SysDepartproject>();
        query.eq(SysDepartproject::getParentId,parentId);
        query.eq(SysDepartproject::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
        String[] codeArr = this.getMyDeptParentOrgCodes(departIds,parentId);
        query.and(wr->{
            Arrays.stream(codeArr).forEach(e->{
                wr.likeRight(SysDepartproject::getOrgCode, e).or();
            });
        });
//        for (int i = 0; i < codeArr.length; i++) {
//            query.or().likeRight(SysDepartproject::getOrgCode, codeArr[i]);
//        }
        //query.and(wrapper -> wrapper.eq(SysDepartproject::getParentId, parentId));
        query.orderByAsc(SysDepartproject::getDepartOrder);
        //将父节点ParentId设为null
        List<SysDepartproject> listDepts = this.list(query);
        for (int i = 0; i < codeArr.length; i++) {
            for (SysDepartproject dept : listDepts) {
                if (dept.getOrgCode().equals(codeArr[i])) {
                    dept.setParentId(null);
                }
            }
        }
        // 调用wrapTreeDataToTreeList方法生成树状数据
        List<SysDepartTreeprojectModel> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeLists(listDepts);
        return listResult;
    }

    /**
     * queryTreeList 对应 queryTreeList 查询所有的部门数据,以树结构形式响应给前端
     */
    @Cacheable(value = CacheConstant.SYS_DEPARTS_CACHE_PROJECT)
    @Override
    public List<SysDepartTreeprojectModel> queryTreeList(String parentId,String orgCode) {
        LambdaQueryWrapper<SysDepartproject> query = new LambdaQueryWrapper<SysDepartproject>();
        query.eq(SysDepartproject::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
        //query.eq(SysDepartproject::getParentstatus, CommonConstant.DEL_FLAG_1);
        query.eq(SysDepartproject::getParentId,parentId);
//        query.eq(StringUtils.isNotBlank(parentId)
//                , SysDepartproject::getParentId
//                ,parentId);
        query.likeRight(SysDepartproject::getOrgCodes,orgCode);
        query.orderByAsc(SysDepartproject::getDepartOrder);
        List<SysDepartproject> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
        List<SysDepartTreeprojectModel> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeLists(list);
        return listResult;
    }

    /**
     * queryTreeList 对应 queryTreeList 查询所有的部门数据,以树结构形式响应给前端
     */
//    @Cacheable(value = CacheConstant.SYS_DEPARTS_CACHE_PROJECT)
    @Override
    public List<SysDepartTreeprojectModel> queryTreeLists(String parentId) {
        LambdaQueryWrapper<SysDepartproject> query = new LambdaQueryWrapper<SysDepartproject>();
        query.eq(SysDepartproject::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
        //query.eq(SysDepartproject::getParentstatus, CommonConstant.DEL_FLAG_1);
        query.eq(SysDepartproject::getParentId,parentId);
        query.orderByAsc(SysDepartproject::getDepartOrder);
        List<SysDepartproject> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
        List<SysDepartTreeprojectModel> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeLists1(list);
        return listResult;
    }

    @Override
    public List<SysDepartTreeprojectModel> queryTreeListgml(String orgCode,String beiz) {
        LambdaQueryWrapper<SysDepartproject> query = new LambdaQueryWrapper<SysDepartproject>();
        query.eq(SysDepartproject::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
        //query.eq(SysDepartproject::getParentstatus, CommonConstant.DEL_FLAG_1);
        query.eq(SysDepartproject::getWbsStructureType, beiz);
        query.likeRight(SysDepartproject::getOrgCode,orgCode);
        query.orderByAsc(SysDepartproject::getDepartOrder);
        List<SysDepartproject> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
        List<SysDepartTreeprojectModel> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeLists1(list);
        return listResult;
    }
    @Cacheable(value = CacheConstant.SYS_DEPART_IDS_CACHE_PROJECT)
    @Override
    public List<DepartIdprojectModel> queryDepartIdTreeList() {
        LambdaQueryWrapper<SysDepartproject> query = new LambdaQueryWrapper<SysDepartproject>();
        query.eq(SysDepartproject::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
        query.orderByAsc(SysDepartproject::getDepartOrder);
        List<SysDepartproject> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
        List<DepartIdprojectModel> listResult = FindsDepartsChildrenUtil.wrapTreeDataToDepartIdTreeLists(list);
        return listResult;
    }

    /**
     * saveDepartData 对应 add 保存用户在页面添加的新的部门对象数据
     */
    @Override
    @Transactional
    public void saveDepartData(SysDepartproject sysDepart, String username) {
        if (sysDepart != null && username != null) {
            if (sysDepart.getParentId() == null) {
                sysDepart.setParentId("");
            }
            String s = UUID.randomUUID().toString().replace("-", "");
            sysDepart.setId(s);
            // 先判断该对象有无父级ID,有则意味着不是最高级,否则意味着是最高级
            // 获取父级ID
            String parentId = sysDepart.getParentId();
            SysDepartproject parentDepartId = sysDepartprojectMapper.getParentDepartId(parentId);
            //update-begin--Author:baihailong  Date:20191209 for：部门编码规则生成器做成公用配置
            JSONObject formData = new JSONObject();
            formData.put("parentId", parentId);
            String[] codeArray = (String[]) FillRuleUtil.executeRules(FillRuleConstant.DEPART, formData);
            //update-end--Author:baihailong  Date:20191209 for：部门编码规则生成器做成公用配置
            sysDepart.setOrgCode(codeArray[0]);
            String orgType = codeArray[1];
            sysDepart.setOrgType(String.valueOf(orgType));
            sysDepart.setCreateTime(new Date());
            sysDepart.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
            sysDepart.setPici(1);
            if(parentDepartId!=null){
                String orgCodes = parentDepartId.getOrgCodes();
                if(!orgCodes.equals(null)){
                    sysDepart.setOrgCodes(parentDepartId.getOrgCodes());
                }

            }

            this.save(sysDepart);
        }

    }

    /**
     * saveDepartData 的调用方法,生成部门编码和部门类型（作废逻辑）
     *
     * @param parentId
     * @return
     * @deprecated
     */
    private String[] generateOrgCode(String parentId) {
        //update-begin--Author:Steve  Date:20190201 for：组织机构添加数据代码调整
        LambdaQueryWrapper<SysDepartproject> query = new LambdaQueryWrapper<SysDepartproject>();
        LambdaQueryWrapper<SysDepartproject> query1 = new LambdaQueryWrapper<SysDepartproject>();
        String[] strArray = new String[2];
        // 创建一个List集合,存储查询返回的所有SysDepart对象
        List<SysDepartproject> departList = new ArrayList<>();
        // 定义新编码字符串
        String newOrgCode = "";
        // 定义旧编码字符串
        String oldOrgCode = "";
        // 定义部门类型
        String orgType = "";
        // 如果是最高级,则查询出同级的org_code, 调用工具类生成编码并返回
        if (StringUtil.isNullOrEmpty(parentId)) {
            // 线判断数据库中的表是否为空,空则直接返回初始编码
            query1.eq(SysDepartproject::getParentId, "").or().isNull(SysDepartproject::getParentId);
            query1.orderByDesc(SysDepartproject::getOrgCode);
            departList = this.list(query1);
            if (departList == null || departList.size() == 0) {
                strArray[0] = YouBianCodeUtil.getNextYouBianCode(null);
                strArray[1] = "1";
                return strArray;
            } else {
                SysDepartproject depart = departList.get(0);
                oldOrgCode = depart.getOrgCode();
                orgType = depart.getOrgType();
                newOrgCode = YouBianCodeUtil.getNextYouBianCode(oldOrgCode);
            }
        } else { // 反之则查询出所有同级的部门,获取结果后有两种情况,有同级和没有同级
            // 封装查询同级的条件
            query.eq(SysDepartproject::getParentId, parentId);
            // 降序排序
            query.orderByDesc(SysDepartproject::getOrgCode);
            // 查询出同级部门的集合
            List<SysDepartproject> parentList = this.list(query);
            // 查询出父级部门
            SysDepartproject depart = this.getById(parentId);
            // 获取父级部门的Code
            String parentCode = depart.getOrgCode();
            // 根据父级部门类型算出当前部门的类型
            orgType = String.valueOf(Integer.valueOf(depart.getOrgType()) + 1);
            // 处理同级部门为null的情况
            if (parentList == null || parentList.size() == 0) {
                // 直接生成当前的部门编码并返回
                newOrgCode = YouBianCodeUtil.getSubYouBianCode(parentCode, null);
            } else { //处理有同级部门的情况
                // 获取同级部门的编码,利用工具类
                String subCode = parentList.get(0).getOrgCode();
                // 返回生成的当前部门编码
                newOrgCode = YouBianCodeUtil.getSubYouBianCode(parentCode, subCode);
            }
        }
        // 返回最终封装了部门编码和部门类型的数组
        strArray[0] = newOrgCode;
        strArray[1] = orgType;
        return strArray;
        //update-end--Author:Steve  Date:20190201 for：组织机构添加数据代码调整
    }


    /**
     * removeDepartDataById 对应 delete方法 根据ID删除相关部门数据
     *
     */
    /*
     * @Override
     *
     * @Transactional public boolean removeDepartDataById(String id) {
     * System.out.println("要删除的ID 为=============================>>>>>"+id); boolean
     * flag = this.removeById(id); return flag; }
     */

    /**
     * updateDepartDataById 对应 edit 根据部门主键来更新对应的部门数据
     */
    @Override
    @Transactional
    public Boolean updateDepartDataById(SysDepartproject sysDepart, String username) {
        if (sysDepart != null && username != null) {
            sysDepart.setUpdateTime(new Date());
            sysDepart.setUpdateBy(username);
            this.updateById(sysDepart);
            return true;
        } else {
            return false;
        }

    }

    /**
     * 根据关联的组织机构修改当前的分部分项所属权限 如果当前分部分项没有分配过权限则直接分配 如果修改过则会新增一份分部分项（修改）
     *
     * @param sysDepart
     * @param username
     * @return
     */
    @Override
    public Boolean updateDepartDataByIds(SysDepartproject sysDepart, String username, String sysOrgCodes) {
        if (sysDepart != null) {// && username != null
            LambdaQueryWrapper<SysDepartproject> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            //lambdaQueryWrapper.like(SysDepartproject::getOrgCode,sysDepart.getOrgCode());
            lambdaQueryWrapper.apply("org_code" + " like{0}", sysDepart.getOrgCode() + "%");
            List<SysDepartproject> list = this.list(lambdaQueryWrapper);
            for (SysDepartproject sysDepartproject : list) {
                sysDepartproject.setOrgCodes(sysOrgCodes);
                sysDepartproject.setPici(sysDepartproject.getPici()+1);
                this.updateById(sysDepartproject);
            }
//			sysDepart.setUpdateTime(new Date());
//			sysDepart.setUpdateBy(username);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据关联的组织机构修改当前的分部分项所属权限 如果当前分部分项没有分配过权限则直接分配 如果修改过则会新增一份分部分项（新增）
     *
     * @param
     * @param
     * @return
     */
    @Override
    public Boolean insertsave(SysDepartproject sysDepart, String sysOrgCodes) {
        if (sysDepart != null) {// && username != null
            LambdaQueryWrapper<SysDepartproject> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            //lambdaQueryWrapper.like(SysDepartproject::getOrgCode,sysDepart.getOrgCode());
            lambdaQueryWrapper.apply("org_code" + " like{0}", sysDepart.getOrgCode() + "%");
            lambdaQueryWrapper.orderBy(true,true,SysDepartproject::getOrgCode);
            List<SysDepartproject> list = this.list(lambdaQueryWrapper);
            for (SysDepartproject sysDepartproject : list) {
                sysDepartproject.setOrgCodes(sysOrgCodes);
                //String s = UUID.randomUUID().toString().replace("-", "");
                sysDepartproject.setId(sysDepartproject.getId()+sysDepartproject.getPici());
                String parentId=null;
                if (sysDepartproject.getParentId().equals("")) {
                    sysDepartproject.setParentId("");
                    parentId= sysDepartproject.getParentId();
                }else{
                    parentId=sysDepartproject.getParentId()+sysDepartproject.getPici();
                    sysDepartproject.setParentId(sysDepartproject.getParentId()+sysDepartproject.getPici());
                }
                sysDepartproject.setPici(sysDepartproject.getPici()+1);
                // 先判断该对象有无父级ID,有则意味着不是最高级,否则意味着是最高级
                // 获取父级ID

                //update-begin--Author:baihailong  Date:20191209 for：部门编码规则生成器做成公用配置
                JSONObject formData = new JSONObject();
                formData.put("parentId", parentId);
                String[] codeArray = (String[]) FillRuleUtil.executeRules(FillRuleConstant.DEPART, formData);
                //update-end--Author:baihailong  Date:20191209 for：部门编码规则生成器做成公用配置
                sysDepartproject.setOrgCode(codeArray[0]);
                String orgType = codeArray[1];
                sysDepartproject.setOrgType(String.valueOf(orgType));
                sysDepartproject.setCreateTime(new Date());
                sysDepartproject.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
                this.save(sysDepartproject);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchWithChildren(List<String> ids) {
        List<String> idList = new ArrayList<String>();
        for (String id : ids) {
            idList.add(id);
            this.checkChildrenExists(id, idList);
        }
        this.removeByIds(idList);
        //根据部门id获取部门角色id
//		List<String> roleIdList = new ArrayList<>();
//		LambdaQueryWrapper<SysDepartRole> query = new LambdaQueryWrapper<>();
//		query.select(SysDepartRole::getId).in(SysDepartRole::getDepartId, idList);
//		List<SysDepartRole> depRoleList = sysDepartRoleMapper.selectList(query);
//		for(SysDepartRole deptRole : depRoleList){
//			roleIdList.add(deptRole.getId());
//		}
//		//根据部门id删除用户与部门关系
//		userDepartMapper.delete(new LambdaQueryWrapper<SysUserDepart>().in(SysUserDepart::getDepId,idList));
//		//根据部门id删除部门授权
//		departPermissionMapper.delete(new LambdaQueryWrapper<SysDepartPermission>().in(SysDepartPermission::getDepartId,idList));
//		//根据部门id删除部门角色
//		sysDepartRoleMapper.delete(new LambdaQueryWrapper<SysDepartRole>().in(SysDepartRole::getDepartId,idList));
//		if(roleIdList != null && roleIdList.size()>0){
//			//根据角色id删除部门角色授权
//			departRolePermissionMapper.delete(new LambdaQueryWrapper<SysDepartRolePermission>().in(SysDepartRolePermission::getRoleId,roleIdList));
//			//根据角色id删除部门角色用户信息
//			departRoleUserMapper.delete(new LambdaQueryWrapper<SysDepartRoleUser>().in(SysDepartRoleUser::getDroleId,roleIdList));
//		}
    }

    @Override
    public List<String> getSubDepIdsByDepId(String departId) {
        return this.baseMapper.getSubDepIdsByDepId(departId);
    }

    @Override
    public List<String> getMySubDepIdsByDepId(String departIds) {
        //根据部门id获取所负责部门
        String[] codeArr = this.getMyDeptParentOrgCode(departIds);
        return this.baseMapper.getSubDepIdsByOrgCodes(codeArr);
    }

    /**
     * <p>
     * 根据关键字搜索相关的部门数据
     * </p>
     */
    @Override
    public List<SysDepartTreeprojectModel> searhBy(String keyWord, String myDeptSearch, String departIds) {
        LambdaQueryWrapper<SysDepartproject> query = new LambdaQueryWrapper<SysDepartproject>();
        List<SysDepartTreeprojectModel> newList = new ArrayList<>();
        //myDeptSearch不为空时为我的部门搜索，只搜索所负责部门
        if (!StringUtil.isNullOrEmpty(myDeptSearch)) {
            //departIds 为空普通用户或没有管理部门
            if (StringUtil.isNullOrEmpty(departIds)) {
                return newList;
            }
            //根据部门id获取所负责部门
            String[] codeArr = this.getMyDeptParentOrgCode(departIds);
            for (int i = 0; i < codeArr.length; i++) {
                query.or().likeRight(SysDepartproject::getOrgCode, codeArr[i]);
            }
            query.eq(SysDepartproject::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
        }
        query.like(SysDepartproject::getDepartName, keyWord);
        //update-begin--Author:huangzhilin  Date:20140417 for：[bugfree号]组织机构搜索回显优化--------------------
        SysDepartTreeprojectModel model = new SysDepartTreeprojectModel();
        List<SysDepartproject> departList = this.list(query);
        if (departList.size() > 0) {
            for (SysDepartproject depart : departList) {
                model = new SysDepartTreeprojectModel(depart);
                model.setChildren(null);
                //update-end--Author:huangzhilin  Date:20140417 for：[bugfree号]组织机构搜索功回显优化----------------------
                newList.add(model);
            }
            return newList;
        }
        return null;
    }

    /**
     * 根据部门id删除并且删除其可能存在的子级任何部门
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String id) {
        List<String> idList = new ArrayList<>();
        idList.add(id);
        this.checkChildrenExists(id, idList);
        //清空部门树内存
        //FindsDepartsChildrenUtil.clearDepartIdModel();
        boolean ok = this.removeByIds(idList);
        //根据部门id获取部门角色id
        List<String> roleIdList = new ArrayList<>();
        LambdaQueryWrapper<SysDepartRole> query = new LambdaQueryWrapper<>();
        query.select(SysDepartRole::getId).in(SysDepartRole::getDepartId, idList);
        List<SysDepartRole> depRoleList = sysDepartRoleMapper.selectList(query);
        for (SysDepartRole deptRole : depRoleList) {
            roleIdList.add(deptRole.getId());
        }
        //根据部门id删除用户与部门关系
        userDepartMapper.delete(new LambdaQueryWrapper<SysUserDepart>().in(SysUserDepart::getDepId, idList));
        //根据部门id删除部门授权
        departPermissionMapper.delete(new LambdaQueryWrapper<SysDepartPermission>().in(SysDepartPermission::getDepartId, idList));
        //根据部门id删除部门角色
        sysDepartRoleMapper.delete(new LambdaQueryWrapper<SysDepartRole>().in(SysDepartRole::getDepartId, idList));
        if (roleIdList != null && roleIdList.size() > 0) {
            //根据角色id删除部门角色授权
            departRolePermissionMapper.delete(new LambdaQueryWrapper<SysDepartRolePermission>().in(SysDepartRolePermission::getRoleId, roleIdList));
            //根据角色id删除部门角色用户信息
            departRoleUserMapper.delete(new LambdaQueryWrapper<SysDepartRoleUser>().in(SysDepartRoleUser::getDroleId, roleIdList));
        }
        return ok;
    }

    /**
     * delete 方法调用
     *
     * @param id
     * @param idList
     */
    private void checkChildrenExists(String id, List<String> idList) {
        LambdaQueryWrapper<SysDepartproject> query = new LambdaQueryWrapper<SysDepartproject>();
        query.eq(SysDepartproject::getParentId, id);
        List<SysDepartproject> departList = this.list(query);
        if (departList != null && departList.size() > 0) {
            for (SysDepartproject depart : departList) {
                idList.add(depart.getId());
                this.checkChildrenExists(depart.getId(), idList);
            }
        }
    }

    @Override
    public List<SysDepartproject> queryUserDeparts(String userId) {
        return baseMapper.queryUserDeparts(userId);
    }

    @Override
    public List<SysDepartproject> queryDepartsByUsername(String username) {
        return baseMapper.queryDepartsByUsername(username);
    }

    /**
     * 根据用户所负责部门ids获取父级部门编码
     *
     * @param departIds
     * @return
     */
    private String[] getMyDeptParentOrgCodes(String departIds,String parentId) {
        //根据部门id查询所负责部门
        LambdaQueryWrapper<SysDepartproject> query = new LambdaQueryWrapper<SysDepartproject>();

        //query.in(SysDepartproject::getId, Arrays.asList(departIds.split(",")));
        String[] split = departIds.split(",");
        for (int i = 0; i < split.length; i++) {
            query.or().likeRight(SysDepartproject::getOrgCodes, split[i]);
        }
        //query.in(SysDepartproject::getOrgCodes, Arrays.asList(departIds.split(",")));
        query.eq(SysDepartproject::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
        query.eq(SysDepartproject::getParentId,parentId);
        query.orderByAsc(SysDepartproject::getOrgCode);
        List<SysDepartproject> list = this.list(query);
        //查找根部门
        if (list == null || list.size() == 0) {
            return null;
        }
        String orgCode = this.getMyDeptParentNode(list);
        String[] codeArr = orgCode.split(",");
        return codeArr;
    }
    private String[] getMyDeptParentOrgCode(String departIds) {
        //根据部门id查询所负责部门
        LambdaQueryWrapper<SysDepartproject> query = new LambdaQueryWrapper<SysDepartproject>();

        //query.in(SysDepartproject::getId, Arrays.asList(departIds.split(",")));
        String[] split = departIds.split(",");
        for (int i = 0; i < split.length; i++) {
            query.or().likeRight(SysDepartproject::getOrgCodes, split[i]);
        }
        //query.in(SysDepartproject::getOrgCodes, Arrays.asList(departIds.split(",")));
        query.eq(SysDepartproject::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
        query.orderByAsc(SysDepartproject::getOrgCode);
        List<SysDepartproject> list = this.list(query);
        //查找根部门
        if (list == null || list.size() == 0) {
            return null;
        }
        String orgCode = this.getMyDeptParentNode(list);
        String[] codeArr = orgCode.split(",");
        return codeArr;
    }
    //根据当前用户的所负责的部门去查询所拥有的分部分项
//    private String[] getorgCodes(String departIds){
//        LambdaQueryWrapper<SysDepart> queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.in(SysDepart::getId,Arrays.asList(departIds.split(",")));
//        this.list(queryWrapper);
//        return null;
//    }

    /**
     * 获取负责部门父节点
     *
     * @param list
     * @return
     */
    private String getMyDeptParentNode(List<SysDepartproject> list) {
        Map<String, String> map = new HashMap<>();
        //1.先将同一公司归类
        for (SysDepartproject dept : list) {
            String code = dept.getOrgCode().substring(0, 3);
            if (map.containsKey(code)) {
                String mapCode = map.get(code) + "," + dept.getOrgCode();
                map.put(code, mapCode);
            } else {
                map.put(code, dept.getOrgCode());
            }
        }
        StringBuffer parentOrgCode = new StringBuffer();
        //2.获取同一公司的根节点
        for (String str : map.values()) {
            String[] arrStr = str.split(",");
            parentOrgCode.append(",").append(this.getMinLengthNode(arrStr));
        }
        return parentOrgCode.substring(1);
    }

    /**
     * 获取同一公司中部门编码长度最小的部门
     *
     * @param str
     * @return
     */
    private String getMinLengthNode(String[] str) {
        int min = str[0].length();
        String orgCode = str[0];
        for (int i = 1; i < str.length; i++) {
            if (str[i].length() <= min) {
                min = str[i].length();
                orgCode = orgCode + "," + str[i];
            }
        }
        return orgCode;
    }

    /**
     * 获取部门树信息根据关键字
     *
     * @param keyWord
     * @return
     */
    @Override
    public List<SysDepartTreeprojectModel> queryTreeByKeyWord(String keyWord) {
        LambdaQueryWrapper<SysDepartproject> query = new LambdaQueryWrapper<SysDepartproject>();
        query.eq(SysDepartproject::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
        query.orderByAsc(SysDepartproject::getDepartOrder);
        List<SysDepartproject> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
        List<SysDepartTreeprojectModel> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeLists(list);
        List<SysDepartTreeprojectModel> treelist = new ArrayList<>();
        if (StringUtils.isNotBlank(keyWord)) {
            this.getTreeByKeyWord(keyWord, listResult, treelist);
        } else {
            return listResult;
        }
        return treelist;
    }

    /**
     * 根据关键字筛选部门信息
     *
     * @param keyWord
     * @return
     */
    public void getTreeByKeyWord(String keyWord, List<SysDepartTreeprojectModel> allResult, List<SysDepartTreeprojectModel> newResult) {
        for (SysDepartTreeprojectModel model : allResult) {
            if (model.getDepartName().contains(keyWord)) {
                newResult.add(model);
                continue;
            } else if (model.getChildren() != null) {
                getTreeByKeyWord(keyWord, model.getChildren(), newResult);
            }
        }
    }
    @Override
    public SysDepartproject selectprojname(String projgrade){
        try {
            QueryWrapper<SysDepartproject> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("org_code",projgrade);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SysDepartproject selectmemo(String memo,String description ) {
        try {
            QueryWrapper<SysDepartproject> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("memo",memo);
            queryWrapper.eq("description",description);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public SysDepartproject selectmemos(String memo) {
        try {
            QueryWrapper<SysDepartproject> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("memo",memo);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SysDepartproject> querylistidList(String id) {
        try {
            QueryWrapper<SysDepartproject> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("id",id);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SysDepartproject selectprojnames(String treeid) {
        try{
            QueryWrapper<SysDepartproject> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("id",treeid);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
