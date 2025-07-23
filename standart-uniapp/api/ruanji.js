import {http} from '@/common/service/service.js'
const apiService = {
	// 软基工单列表查询
	workorderlist(params) {
		return http.get('/devicemixpilerwd/deviceMixpileRwd/list',params)
		// return http.get('/devicemixpilerwdsta/deviceMixpileRwdsta/list',params)
	},
	// 软基工单数据详细信息
	workorderdetail(params) {
		return http.get('/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/listAll', params)
	},
	// 软基工单新增
	workorderadd(params) {
		return http.post('/sys/sysDepartproject/rjrwdadd', params)
	},
	// 软基工单修改
	workorderedit(params) {
		return http.put('/sys/sysDepartproject/rjrwdadd/rjrwdedit', params)
	},
	// 软基工单删除
	workorderdel(data,params) {
		return http.delete('/devicemixpilerwd/deviceMixpileRwd/delete',data, params)
	},
	// 搅拌桩成桩记录
	pilerecordlist(params) {
		return http.get('/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/list', params)
	},
	// 搅拌桩数据查询
	mixpilelist(params) {
		return http.get('/devicemixpilerealdata/deviceMixpileRealdata/list', params)
	},
	// 灌注桩数据查询
	fillingPilelist(params) {
		return http.get('/devicemixgrouted/deviceMixpileGroutedReal/list', params)
	},
	//桩记录
	pilenotelist(params) {
		return http.get('/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/listCX', params)
	},
	// 软基工单下发
	workorderxf(params) {
		return http.get('/devicemixpilerwd/deviceMixpileRwd/issuedlist', params)
	},
	// 软基数据超标统计
	mixpileoverlist(params) {
		return http.get('/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/countList', params)
	},
	// 软基数据今日生产统计
	mixpiledevToday(params) {
		return http.get('/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/queryToday', params)
	},
};

export default apiService;
