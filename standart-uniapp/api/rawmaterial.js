/**
 * 材料追溯API接口
 */
import {
	http
} from '@/common/service/service.js'
import configService from '@/common/service/config.service.js';
const apiService = {

 /**
	  * 审核/确认到达
	  */
	elockEdit(params) {
		return http.put('/wbshebeidetail/wbshebeiDetail/edit',params)	
	},
	// 解锁
	iselock(params) {
		return http.get('/sys/systems/sysMessages/jfElocks',params)	
	},
	materlist(){
		return http.get(`/wbshebeidetail/wbshebeiDetail/liststa`)
	},



	/**
	 * 获取文件访问路径
	 * @param avatar
	 * @param subStr
	 * @returns {*}
	 */
	getFileAccessHttpUrl(avatar, subStr) {
		if (!subStr) subStr = 'http'
		if (avatar && avatar.startsWith(subStr)) {
			return avatar;
		} else {
			return configService.staticDomainURL + "/" + avatar;
		}
	}
};

export default apiService;
