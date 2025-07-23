import {http} from '@/common/service/service.js'
const apiService = {
	// 任务单列表
	pourlist(params) {
		return http.get('/bhzrwdxx/bhzrwdxx/list',params)
	},
	// 任务单列表2
	pourlist2(params) {
		return http.get('/system/bhzrenwudan/bhzrwdlistss',params)
	},
	// 任务单列表3
	pourlist3(params) {
		return http.get('/bhzrwdxx/bhzrwdxx/listjzl',params)
	},
	// 拌合站数据列表
	mixlist(params) {
		return http.get('/hntbhz/bhzCementBase/list', params)
	},
	// 拌合站数据详细信息
	mixdetail(params) {
		return http.get('/hntbhz/bhzCementBase/queryBhzCementDetailByMainId', params)
	},
	// 设备列表
	deviceList(params) {
		return http.get('/sys/user/list3',params)
	},
	//浇筑令新增
	bhzrenwudanadd(params) {
		return http.post('/sys/sysDepartproject/rewudanadd', params)
	},
	//浇筑令统计
	bhzrwdsta(params){
		return http.get('/bhzrwdxx/bhzrwdxx/listtj',params)
	},
	//浇筑令统计2
	bhzrwdsta2(params){
		return http.get('/system/bhzrenwudan/taskSta2',params)
	},
	//浇筑令流程
	bhzrwdSte(params){
		return http.get('/bhzrwdxx/bhzrwdxx/rwdprocess',params)
	},
	//浇筑令流程2
	bhzrwdSte2(params){
		return http.get('bhzrwdxx/bhzrwdxx/rwdprocess2',params)
	},
	//浇筑令审核
	bhzrwdedit(params){
		return http.put('/system/bhzrenwudan/edit',params)
	},
	//浇筑令编辑
	bhzrenwudanedit(params) {
		return http.put('/sys/sysDepartproject/renwudanedit', params)
	},
	// 拌合站超标率(图表接口)
	bhzcbv(params) {
		return http.get('/hntbhz/bhzCementBase/bhzcbv', params)
	},
	// 砼拌合站超标统计(图表接口)
	materials(params) {
		return http.get('/hntbhz/bhzCementBase/bhzcailiaoCount', params)
	},
	// 单位生产统计(图表接口)
	bhzcnCount(params) {
		return http.get('/bhzStatistics/bhzCementStatistics/bhzCNCount', params)
	},
	// 拌合站产能
	bhzcapacity(params) {
		return http.get('/bhzStatistics/bhzCementStatistics/ljfllist', params)
	},
	// 超标处理查询
	bhzOverHandle(params) {
		return http.get('/hntbhz/bhzCementBase/listczsh', params)
	},
	// 张拉任务单
	zhanglaRenwudan(params) {
		return http.get('/trzhanglarenwudan/trZhanglaRenwudan/list', params)
	},
	// 张拉任务单新增
	zhanglaRenwudanadd(params) {
		return http.post('/sys/sysDepartproject/Zlrwdadd', params)
	},
	// 张拉任务单
	zhanglaRenwudanedit(params) {
		return http.put('/sys/sysDepartproject/Zlrwdedit', params)
	},
	// 压浆任务单
	yajiangRenwudan(params) {
		return http.get('/yajiangrenwudan/trYajiangRenwudan/list', params)
	},
	// 压浆任务单新增
	yajiangRenwudanadd(params) {
		return http.post('/sys/sysDepartproject/yjrenwudanadd', params)
	},
	// 压浆任务单新增
	yajiangRenwudanedit(params) {
		return http.put('/sys/sysDepartproject/yjrenwudanedit', params)
	},
};

export default apiService;
