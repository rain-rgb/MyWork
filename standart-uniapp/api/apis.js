import {
	http
} from '@/common/service/service.js'
import configService from '@/common/service/config.service.js';
const apiService = {

	/**
	 * 登录
	 */
	login(params) {
		return http.post('/sys/mLogin', params)
	},
	/**
	 * 手机号码登录
	 */
	phoneNoLogin(params) {
		return http.post('/sys/phoneLogin', params);
	},
	/**
	 * 退出
	 */
	logout(params) {
		return http.post('/sys/logout', params);
	},
	/**
	 * 智慧用电-分页列表查询
	 */
	zhydList(params) {
		return http.get('/zhydreal/deviceElectricRealdata/list', params)
	},
	/**
	 * 智慧用电历史记录表
	 */
	zhydHistory(params) {
		return http.get('/zhydhistory/deviceElectricHistorydata/list', params)
	},
	/**
	 * 原材料收料主表-分页列表查询
	 */
	rMaterialList(params) {
		return http.get('/yclsl/wzycljinchanggb/list', params)
	},
	// 拌合站数据列表查询
	bhzList(params) {
		return http.get('/hntbhz/bhzCementBase/list', params)
	},
	//派单列表查询
	facarList(params) {
		return http.get('/car/carDispatch/list', params)
	},
	//派单接单统计查询
	facartongji(params) {
		return http.get('/car/carDispatch/list4', params)
	},
	//派单接单统计月统计
	facartongjiyue(params) {
		return http.get('/car/carDispatch/list5', params)
	},
	qiangdanmessage(params) {
		return http.post('/sys/message/sysMessage/addmessage', params)
	},
	//派单接单统计周统计
	facartongjizhou(params) {
		return http.get('/car/carDispatch/list6', params)
	},
	//添加派单信息
	facaradd(params) {
		return http.post('/car/carDispatch/add', params)
	},
	//强单
	facaredit(params) {
		return http.put('/car/carDispatch/edit', params)
	},
	//我的订单列表查询
	myorder(params) {
		return http.get('/car/carDispatch/list1', params)
	},
	// 拌合站用料信息
	bhzdetail(params) {
		return http.get('/hntbhz/bhzCementBase/queryBhzCementDetailByMainId', params)
	},
	// 任务单查询
	taskListNo(params) {
		return http.get('/system/bhzrenwudan/list', params)
	},
	// 根据施工配合比查询施工配合比信息
	constructionMixNo(params) {
		return http.get('/system/shigongphb/list', params)
	},
	/**
	 * 设备信息列表
	 */
	deviceList(params) {
		return http.get('/sys/user/list2', params)
	},
	/**
	 * 根据设备类型查看设备信息
	 */
	deviceType(params) {
		return http.get('/sys/user/list3', params)
	},
	// 调度车辆查询
	dispatchCarList(params) {
		return http.get('/car/schedulingCar/list', params)
	},
	// 通过id查询调度车辆
	dispatchCarId(params) {
		return http.get('/car/schedulingCar/queryById', params)
	},
	// 料仓查询
	// /wzliaocang/wzliaocang/list4
	siloQuery(params) {
		return http.get('/wzliaocang/wzliaocang/list4', params)
	},
	// 料仓配置表-开门
	siloOpen(params) {
		return http.put('/wzliaocang/wzliaocang/list3', params)
	},
	// 料仓配置表-关门
	siloClose(params) {
		return http.put('/wzliaocang/wzliaocang/list2', params)
	},
	// 见证取样配比通知单
	samplingMixList(params) {
		return http.get('/system/shigongphb/list1', params)
	},
	// 见证取样信息添加
	samplingAdd(params) {
		return http.post('/hntconsign/hntConsign/add1', params)
	},
	// 见证取样图片信息添加
	samplingPicAdd(params) {
		return http.post('/hntconsignpic/hntConsignPic/add', params)
	},
	// 见证取样信息列表查询
	samplingList(params) {
		return http.get('/hntconsign/hntConsign/list', params)
	},
	// 养护查询架号
	curingNo(params) {
		return http.get('/hntconsignsamplepos/hntConsignSamplePos/list1', params)
	},
	// 养护上架
	startCuring(params) {
		return http.put('/hntconsigncode/hntConsignCode/edityhup', params)
	},
	// 根据委托id查询取样图片
	samplingPic(params) {
		return http.get('/hntconsignpic/hntConsignPic/list1', params)
	},
	/**
	 * 组织机构树
	 */
	orgTree(params) {
		return http.get('/sys/sysDepart/queryMyDeptTreeList', params)
	},
	//当前用户分部分项
	projectTree(params) {
		return http.get('/sys/sysDepartproject/queryTreeList', params)
	},
	projectTreeSon(params) {
		return http.get('/sys/sysDepartproject/queryTreeListSon', params)
	},
	//张拉任务单新增
	zhanghlarwdadd(params) {
		return http.post('/sys/sysDepartproject/Zlrwdadd', params)
	},
	//压浆任务单新增
	yajiangrwdadd(params) {
		return http.post('/sys/sysDepartproject/yjrenwudanadd', params)
	},
	//浇筑令新增
	bhzrenwudanadd(params) {
		return http.post('/sys/sysDepartproject/rewudanadd', params)
	},
	//浇筑令查询
	bhzrwdList(params) {
		return http.get('/system/bhzrenwudan/bhzrwdlist', params)
	},
	//浇筑令编辑
	bhzrwdEdit(params) {
		return http.put('/sys/sysDepartproject/renwudanedit', params)
	},
	//浇筑令审核
	bhzrwdsh(params) {
		return http.put('/system/bhzrenwudan/edit', params)
	},
	//设备查询
	shebeiList(params) {
		return http.get('/system/shebeiInfo/list', params)
	},
	//运输车油耗添加
	fuelList(params) {
		return http.post('/carconsumption/carConsumption/add', params)
	},
	//运输车油耗查询
	fueldataList(params) {
		return http.get('/carconsumption/carConsumption/list', params)
	},
	//运输车油耗油量/金额统计
	fueldstaList(params) {
		return http.get('/carconsumption/carConsumption/list1', params)
	},
	//运输车油耗油量/金额按月统计
	fueldstaMList(params) {
		return http.get('/carconsumption/carConsumption/list2', params)
	},
	//运输车油耗油量/金额按天统计
	fueldstaDList(params) {
		return http.get('/carconsumption/carConsumption/list3', params)
	},
	//配料单数据列表
	shigongphbLists(params) {
		return http.get('/system/shigongphb/sphblist', params)
	},
	//砼理论配合比编号查询
	llphbLists(params) {
		return http.get('/bhzrecipe/bhzRecipe/list1', params)
	},
	//砼理论配合比查询
	llphbdataLists(params) {
		return http.get('/bhzrecipe/bhzRecipe/list', params)
	},
	//砼理论配合比材料信息查询
	llphbdetailLists(params) {
		return http.get('/bhzrecipe/bhzRecipe/queryBhzRecipedetailByMainId', params)
	},
	//任务单编号列表查询
	bhzrwdcodeList(params) {
		return http.get('/system/bhzrenwudan/bhzrwdlists', params)
	},
	//料仓列表查询
	liaocangChangeList(params) {
		return http.get('/wzliaocang/wzliaocang/list5', params)
	},
	//配料单添加
	shigongphbadd(params) {
		return http.post('/system/shigongphb/add1', params)
	},
	//制梁任务单编号列表查询
	zhilaingList(params) {
		return http.get('/zhiliangrenwudan/zhiliangrenwudan/list9', params)
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
