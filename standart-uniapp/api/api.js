import { http } from '@/common/service/service.js' 
import configService from '@/common/service/config.service.js';
const apiService = {
	 
	 /**
	  * 登录
	  */
	login(params) {
		return http.post('/sys/mLogins',params)	
	},
	/**
	 	  * 集团单点登录
	 	  */
	 	login12(params) {
	 		return http.post('/sys/mLoginss',params)	
	 	},
	/**
	  * 手机号码登录
	  */
	phoneNoLogin(params) {
		return http.post('/sys/phoneLogin',params);
	},
	/**
	  * 退出
	  */
	logout(params) {
		return http.post('/sys/logout',params);
	},
	/**
	 * 组织机构树
	 */
	orgTree(params) {
		return http.get('/sys/sysDepart/queryMyDeptTreeList', params)
	},
	/**
	 * 根据设备类型查看设备信息
	 */
	deviceType(params) {
		return http.get('/sys/user/list3', params)
	},
	/**
	 * 设备信息列表
	 */
	deviceList(params) {
		return http.get('/sys/user/list2', params)
	},
	//当前用户分部分项
	projectTree(params) {
		return http.get('/sys/sysDepartproject/queryTreeList', params)
	},
	projectTreeSon(params) {
		return http.get('/sys/sysDepartproject/queryTreeListSon', params)
	},
	// 获取组织机构列表
	getOrgList(params) {
		return http.get('/sys/sysDepart/querySysDepartInfoList',params)
	},
	permission(params){
		return http.get('/system/sysAppPermission/getUserAppPermissionByToken',params)
	},
	//登陆成功选择用户当前部门
	departlogin(params){
		return http.put('/sys/selectDeparts',params)
	},
	//获取菜单
	getMenu(params){
		return http.get('/system/sysAppPermission/getUserHeadPermissionByToken',params)
	},
	/**
	 * 获取文件访问路径
	 * @param avatar
	 * @param subStr
	 * @returns {*}
	 */
	getFileAccessHttpUrl(avatar,subStr){
	    if(!subStr) subStr = 'http'
	    if(avatar && avatar.startsWith(subStr)){
	        return avatar;
	    }else{
	        return configService.staticDomainURL + "/" + avatar;
	    }
	}
};

export default apiService;
