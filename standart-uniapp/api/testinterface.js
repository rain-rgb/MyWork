/**
 * 试验室API接口
 */
import {
	http
} from '@/common/service/service.js'
import configService from '@/common/service/config.service.js';
const apiService = {

 /**
	  * 试验机首页统计 万能机 压力机 标养室 抗压抗折机 总数 不合格数  合格数 
	  */
	syjStatisc(params) {
		return http.get('/syj/tSyjzb/stalists',params)	
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
