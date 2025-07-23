import {
	http
} from '@/common/service/service.js'
const apiService = {
	// 配料单列表
	shigongphblist(params) {
		return http.get('/system/shigongphb/sphblist', params)
	},
	//任务单编号列表查询
	bhzrwdcodeList(params) {
		return http.get('/system/bhzrenwudan/bhzrwdlistss', params)
	},
	//砼理论配合比查询
	llphbdataLists(params) {
		return http.get('/bhzrecipe/bhzRecipe/list', params)
	},
	//砼理论配合比编号查询
	llphbLists(params) {
		return http.get('/bhzrecipe/bhzRecipe/list1', params)
	},
	//砼理论配合比材料信息查询
	llphbdetailLists(params) {
		return http.get('/bhzrecipe/bhzRecipe/queryBhzRecipedetailByMainId', params)
	},
	//制梁任务单编号列表查询
	zhilaingList(params) {
		return http.get('/zhiliangrenwudan/zhiliangrenwudan/list9', params)
	},
	//料仓列表查询
	liaocangChangeList(params) {
		return http.get('/wzliaocang/wzliaocang/list5', params)
	},
	//料仓号查询检验批
	listpld(params) {
		return http.get('/system/shigongphb/listpld', params)
	},
	//配料单添加
	shigongphbadd(params) {
		return http.post('/system/shigongphb/add1', params)
	},
	//配料单编辑
	shigongphbedit(params) {
		return http.put('/system/shigongphb/edit', params)
	},
	//试块智管
	hntconsignsta(params) {
		return http.get('/hntconsign/hntConsign/list3', params)
	},
	//理论配合比数量统计
	bhzrecipesta(params) {
		return http.get('/bhzrecipe/bhzRecipe/list', params)
	},
	//配料单数量统计
	sgphbsta(params) {
		return http.get('/system/shigongphb/list', params)
	},
	//钢筋保护层厚度合格率
	gangjinbhcSta(params) {
		return http.get('/sys/systems/sysMessages/gblist', params)
	},
	// 试块信息查询
	samplinglist(params) {
		return http.get('/hntconsign/hntConsign/list2', params)
	},
	// 见证取样配比通知单
	samplingMixList(params) {
		return http.get('/system/shigongphb/lists', params)
	},
	// 任务单查询
	taskListNo(params) {
		return http.get('/system/bhzrenwudan/list', params)
	},
	// 见证取样信息添加
	samplingAdd(params) {
		return http.post('/hntconsign/hntConsign/add1', params)
	},
	// 见证取样图片信息添加
	samplingPicAdd(params) {
		return http.post('/hntconsignpic/hntConsignPic/add', params)
	},
	// 根据委托id查询取样图片
	samplingPic(params) {
		return http.get('/hntconsignpic/hntConsignPic/list1', params)
	},
	//检验批统计
	wztaizhangSta(params) {
		return http.get('/wztaizhang/wztaizhang/list5', params)
	},
};

export default apiService;
