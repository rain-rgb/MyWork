import { getAction, deleteAction, putAction, postAction, httpAction } from '@/api/manage'
import Vue from 'vue'
import {UI_CACHE_DB_DICT_DATA } from "@/store/mutation-types"

//规格选择,材料
const guigeList = (params)=>getAction("/wzcailiaonamedict/wzcailiaonamedict/list2",params);
//有电子锁的供应商选择
const gysList = ()=>getAction("/wzgongyingshang/wzgongyingshang/list1");
//目的地选择
const mudidiList = ()=>getAction("/wbdestination/wbDestination/list1");

//原材料收料和出料数据供应商
const gongyingshangList = (params)=>getAction("/wzgongyingshang/wzgongyingshang/list",params);
//原材料进场仓储数据供应商
const gongyingshangmanList = (params)=>getAction("/wzgongyingshangman/wzgongyingshangMan/list",params);
//原材料收料和出料数据材料规格
const cailiaoList = (params)=>getAction("/wzcailiaonamedict/wzcailiaonamedict/list",params);

//登录初始根据当前用户获取所有设备列表
const initialarryOne = ()=>postAction("/sys/user/list1");
const changearryOne = (params)=>getAction("/sys/user/list2",params);
const usershebeiList = (params)=>getAction("/sys/user/list3",params);
const zlpzxiangmuList = (params)=>getAction("/zlpz/zlpzProject/list1",params);
const carshebeiList = (params)=>getAction("/system/shebeiInfo/list4",params);//当前用户运输车设备列表以及离线在线状态
const userliaocangList = (params) => getAction("/wzliaocang/wzliaocang/list7" , params)//当前组织机构下料仓列表
const usercailiaoList = (params) => getAction("/wzcailiaonamedict/wzcailiaonamedict/list3" , params)//当前组织机构下材料配置列表
//角色管理
const addRole = (params)=>postAction("/sys/role/add",params);
const editRole = (params)=>putAction("/sys/role/edit",params);
const checkRoleCode = (params)=>getAction("/sys/role/checkRoleCode",params);
const queryall = (params)=>getAction("/sys/role/queryall",params);
const selectroleMessage=(params)=>getAction("/sys/role/list1",params);//根据当前用户信息获取当前角色名称用来显示hander 的显示

//用户管理
const addUser = (params)=>postAction("/sys/user/add",params);
const editUser = (params)=>putAction("/sys/user/edit",params);
const queryUserRole = (params)=>getAction("/sys/user/queryUserRole",params);
const getUserList = (params)=>getAction("/sys/user/list",params);
const frozenBatch = (params)=>putAction("/sys/user/frozenBatch",params);

//获取当前用户下的所有用户
const getMyUserList = (params)=>getAction("/sys/sysDepartRole/list",params);

//验证用户是否存在
const checkOnlyUser = (params)=>getAction("/sys/user/checkOnlyUser",params);
//改变密码
const changePassword = (params)=>putAction("/sys/user/changePassword",params);
//权限管理
const addPermission= (params)=>postAction("/sys/permission/add",params);
const editPermission= (params)=>putAction("/sys/permission/edit",params);
const getPermissionList = (params)=>getAction("/sys/permission/list",params);
const addAppPermission= (params)=>postAction("/system/sysAppPermission/add",params);//app菜单添加
const editAppPermission= (params)=>putAction("/system/sysAppPermission/edit",params);//app菜单编辑
const getAppPermissionList = (params)=>getAction("/system/sysAppPermission/list",params);//app菜单查询
const getSystemMenuList = (params)=>getAction("/sys/permission/getSystemMenuList",params);
const getSystemSubmenu = (params)=>getAction("/sys/permission/getSystemSubmenu",params);
const getSystemSubmenuBatch = (params) => getAction('/sys/permission/getSystemSubmenuBatch', params)

const queryTreeList = (params)=>getAction("/sys/permission/queryTreeList",params);
const queryTreeListForRole = (params)=>getAction("/sys/role/queryTreeList",params);
const queryTreeAppList = (params)=>getAction("/system/sysAppPermission/queryTreeList",params);//app菜单树查询
const queryTreeListAppForRole = (params)=>getAction("/sys/role/queryTreeListApp",params);
const queryListAsync = (params)=>getAction("/sys/permission/queryListAsync",params);
const queryRolePermission = (params)=>getAction("/sys/permission/queryRolePermission",params);
const saveRolePermission = (params)=>postAction("/sys/permission/saveRolePermission",params);
const queryRoleAppPermission = (params)=>getAction("/system/sysAppPermission/queryRolePermission",params);//app根据角色查询菜单权限
const saveRoleAppPermission = (params)=>postAction("/system/sysAppPermission/saveRolePermission",params);//app根据角色添加菜单权限
const queryPermissionsByUser = ()=>getAction("/sys/permission/getUserPermissionByToken");
const loadAllRoleIds = (params)=>getAction("/sys/permission/loadAllRoleIds",params);
const getPermissionRuleList = (params)=>getAction("/sys/permission/getPermRuleListByPermId",params);
const queryPermissionRule = (params)=>getAction("/sys/permission/queryPermissionRule",params);

// 部门管理
const queryDepartTreeList = (params)=>getAction("/sys/sysDepart/queryTreeList",params);//组织机构树
const queryDepartTreeSync = (params)=>getAction("/sys/sysDepart/queryDepartTreeSync",params);
const queryPartialBasic = ()=>getAction("/partialBasic/queryPartialBasic");
const queryIdTree = (params)=>getAction("/sys/sysDepart/queryIdTree",params);
const queryParentName   = (params)=>getAction("/sys/sysDepart/queryParentName",params);
const searchByKeywords   = (params)=>getAction("/sys/sysDepart/searchBy",params);
const deleteByDepartId   = (params)=>deleteAction("/sys/sysDepart/delete",params);
//分部分项
const queryDepartTreeprojectList = (params)=>getAction("/sys/sysDepartproject/queryTreeList",params);
const queryDepartTreeprojectListSon = (params)=>getAction("/sys/sysDepartproject/queryTreeListSon",params);

const queryIdprojectTree = (params)=>getAction("/sys/sysDepartproject/queryIdTree",params);
const queryParentprojectName   = (params)=>getAction("/sys/sysDepartproject/queryParentName",params);
const searchByKeyprojectwords   = (params)=>getAction("/sys/sysDepartproject/searchBy",params);
const deleteByDepartprojectId   = (params)=>deleteAction("/sys/sysDepartproject/delete",params);

//二级部门管理
const queryDepartPermission = (params)=>getAction("/sys/permission/queryDepartPermission",params);
const saveDepartPermission = (params)=>postAction("/sys/permission/saveDepartPermission",params);
const queryTreeListForDeptRole = (params)=>getAction("/sys/sysDepartPermission/queryTreeListForDeptRole",params);
const queryDeptRolePermission = (params)=>getAction("/sys/sysDepartPermission/queryDeptRolePermission",params);
const saveDeptRolePermission = (params)=>postAction("/sys/sysDepartPermission/saveDeptRolePermission",params);
const queryMyDepartTreeList  = (params)=>getAction("/sys/sysDepart/queryMyDeptTreeList",params);//当前用户的组织机构
const queryMyDepartprojectTreeList =(params)=>getAction("/sys/sysDepartproject/queryMyDeptTreeList",params);//当前用户分部分项

//日志管理
const deleteLog = (params)=>deleteAction("/sys/log/delete",params);
const deleteLogList = (params)=>deleteAction("/sys/log/deleteBatch",params);

//数据字典
const addDict = (params)=>postAction("/sys/dict/add",params);
const editDict = (params)=>putAction("/sys/dict/edit",params);
const treeList = (params)=>getAction("/sys/dict/treeList",params);
const addDictItem = (params)=>postAction("/sys/dictItem/add",params);
const editDictItem = (params)=>putAction("/sys/dictItem/edit",params);

//app权限

const addappfunction=(params)=>postAction("/appfunction/appfunction/add",params);

const selectAPPmessage=(params)=>getAction("/appmessage/appmessage/list1",params);




//字典标签专用（通过code获取字典数组）
export const ajaxGetDictItems = (code, params)=>getAction(`/sys/dict/getDictItems/${code}`,params);
//从缓存中获取字典配置
function getDictItemsFromCache(dictCode) {
  if (Vue.ls.get(UI_CACHE_DB_DICT_DATA) && Vue.ls.get(UI_CACHE_DB_DICT_DATA)[dictCode]) {
    let dictItems = Vue.ls.get(UI_CACHE_DB_DICT_DATA)[dictCode];
    console.log("-----------getDictItemsFromCache----------dictCode="+dictCode+"---- dictItems=",dictItems)
    return dictItems;
  }
}

//系统通告
const doReleaseData = (params)=>getAction("/sys/annountCement/doReleaseData",params);
const doReovkeData = (params)=>getAction("/sys/annountCement/doReovkeData",params);
//获取系统访问量
const getLoginfo = (params)=>getAction("/sys/loginfo",params);
const getVisitInfo = (params)=>getAction("/sys/visitInfo",params);
// 根据部门主键查询用户信息
const queryUserByDepId = (params)=>getAction("/sys/user/queryUserByDepId",params);
// 重复校验
const duplicateCheck = (params)=>getAction("/sys/duplicate/check",params);

// 养护货架配置重复校验
const duplicateCheck1 = (params)=>getAction("/sys/duplicate/maintenan",params);

// 梁列配置重复校验
const duplicateCheck2 = (params)=>getAction("/sys/duplicate/beamlie",params);

// 制梁任务单编号/施工部位重复校验
const duplicateCheck3 = (params)=>getAction("/sys/duplicate/beamrwdcode",params);

// 梁座名称配置重复校验
const duplicateCheck4 = (params)=>getAction("/sys/duplicate/beamseat",params);

// 制梁任务单编号重复校验
const duplicateCheck5 = (params)=>getAction("/sys/duplicate/beamcode",params);

// 沥青筛分试验重复校验
const duplicateCheck6 = (params)=>getAction("/sys/duplicate/shaifensy",params);

// 加载分类字典
const loadCategoryData = (params)=>getAction("/sys/category/loadAllData",params);
const checkRuleByCode = (params) => getAction('/sys/checkRule/checkByCode', params)
//加载我的通告信息
const getUserNoticeInfo= (params)=>getAction("/sys/sysAnnouncementSend/getMyAnnouncementSend",params);
const getTransitURL = url => `/sys/common/transitRESTful?url=${encodeURIComponent(url)}`
// 中转HTTP请求
export const transitRESTful = {
  get: (url, parameter) => getAction(getTransitURL(url), parameter),
  post: (url, parameter) => postAction(getTransitURL(url), parameter),
  put: (url, parameter) => putAction(getTransitURL(url), parameter),
  http: (url, parameter) => httpAction(getTransitURL(url), parameter),
}

export {
  addRole,
  editRole,
  checkRoleCode,
  addUser,
  editUser,
  queryUserRole,
  getUserList,
  queryall,
  frozenBatch,
  checkOnlyUser,
  changePassword,
  getPermissionList,
  addPermission,
  editPermission,
  queryTreeList,
  queryListAsync,
  queryRolePermission,
  saveRolePermission,
  queryPermissionsByUser,
  loadAllRoleIds,
  getPermissionRuleList,
  queryPermissionRule,
  queryDepartTreeList,
  queryDepartTreeSync,
  queryPartialBasic,
  queryIdTree,
  queryParentName,
  searchByKeywords,
  deleteByDepartId,
  deleteLog,
  deleteLogList,
  addDict,
  editDict,
  treeList,
  addDictItem,
  editDictItem,
  doReleaseData,
  doReovkeData,
  getLoginfo,
  getVisitInfo,
  queryUserByDepId,
  duplicateCheck,
  queryTreeListForRole,
  getSystemMenuList,
  getSystemSubmenu,
  getSystemSubmenuBatch,
  loadCategoryData,
  checkRuleByCode,
  queryDepartPermission,
  saveDepartPermission,
  queryTreeListForDeptRole,
  queryDeptRolePermission,
  saveDeptRolePermission,
  queryMyDepartTreeList,
  getUserNoticeInfo,
  getDictItemsFromCache,
  getMyUserList,
  initialarryOne,
  changearryOne,
  usershebeiList,
  zlpzxiangmuList,
  addappfunction,
  queryDepartTreeprojectList,
  queryIdprojectTree,
  queryParentprojectName,
  searchByKeyprojectwords,
  deleteByDepartprojectId,
  queryMyDepartprojectTreeList,
  carshebeiList,
  userliaocangList,
  usercailiaoList,
  duplicateCheck1,
  queryDepartTreeprojectListSon,
  duplicateCheck2,
  duplicateCheck4,
  duplicateCheck5,
  duplicateCheck3,
  selectAPPmessage,
  selectroleMessage,
  getAppPermissionList,
  addAppPermission,
  editAppPermission,
  queryRoleAppPermission,
  saveRoleAppPermission,
  queryTreeListAppForRole,
  queryTreeAppList,
  duplicateCheck6,
  mudidiList,
  gysList,
  guigeList,
  gongyingshangList,
  gongyingshangmanList,
  cailiaoList
}