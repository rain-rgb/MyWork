const routes = [{
	path: "/pages/login/login",
	name: 'login',
	meta: {
		title: '登录',
	},
},
{
 	path: "/pages/login/ssologin",
 	name: 'ssologin',
 	meta: {
 		title: '单点地址',
 	},
 },
 {
 	path: "/pages/login/JTlogin",
 	name: 'JTlogin',
 	meta: {
 		title: '物联网',
 	},
 },
{
	//注意：path必须跟pages.json中的地址对应，最前面别忘了加'/'哦
	path: '/pages/index/index',
	name: 'index',
	meta: {
		title: '主页',
	},
},
{
	//注意：path必须跟pages.json中的地址对应，最前面别忘了加'/'哦
	path: '/pages/home/home',
	//aliasPath:'/',  //对于h5端你必须在首页加上aliasPath并设置为/
	name: 'home',
	meta: {
		title: '首页',
	},
},
{
	path: '/pages/home/homes',
	name: 'homes',
	meta: {
		title: '首页',
	},
},
{
	path: '/pages/user/people',
	name: 'people',
	meta: {
		title: '个人中心',
	},
},
{
	path: '/pages/user/userdetail',
	name: 'userdetail',
	meta: {
		title: '个人详情',
	},
},
{
	path: '/pages/user/useredit',
	name: 'useredit',
	meta: {
		title: '个人编辑',
	},
},
{
	path: '/pages/user/userexit',
	name: 'userexit',
	meta: {
		title: '退出',
	},
},
{
	path: '/pages/common/exit',
	name: 'exit',
	meta: {
		title: '退出',
	},
},
{
	path: '/pages/common/success',
	name: 'success',
	meta: {
		title: 'success',
	},
},
{
	path: '/pages/common/helloWorld',
	name: 'helloWorld',
	meta: {
		title: 'helloWorld',
	},
},
{
	path: '/pages/smartbh/pourorder/pourManage',
	name: 'pourManage',
	meta: {
		title: '浇筑令管理',
	},
},
{
	path: '/pages/smartbh/pourorder/pourAdd',
	name: 'pourAdd',
	meta: {
		title: '浇筑令新增',
	},
},
{
	path: '/pages/Labellist/Labellist',
	// name: 'Labellist',
	meta: {
		title: '全部服务',
	},
},
{
	path: '/pages/smartbh/pourorder/pourDetail',
	name: 'pourDetail',
	meta: {
		title: '浇筑令详情',
	},
},
{
	path: '/pages/smartbh/mix/mixoverview',
	name: 'mixoverview',
	meta: {
		title: '拌合站总览',
	},
},
{
	path: '/pages/smartbh/mix/mixzlfx',
	name: 'mixzlfx',
	meta: {
		title: '拌合站总览',
	},
},
{
	path: '/pages/smartbh/mix/mixdatadetail',
	name: 'mixdatadetail',
	meta: {
		title: '拌合站数据详情',
	},
},
{
	path: '/pages/smartbh/mix/mixdatalist',
	name: 'mixdatalist',
	meta: {
		title: '拌合站数据列表',
	},
},
{
	path: '/pages/ruanji/ruanji',
	name: 'ruanji',
	meta: {
		title: '软基首页',
	},
},
{
	path: '/pages/ruanji/workorder',
	name: 'workorder',
	meta: {
		title: '工单管理',
	},
},
{
	path: '/pages/ruanji/workorderdetail',
	name: 'workorderdetail',
	meta: {
		title: '工单详情',
	},
},
{
	path: '/pages/ruanji/workorderadd',
	name: 'workorderadd',
	meta: {
		title: '工单新增',
	},
},
{
	path: '/pages/ruanji/pilerecord',
	name: 'pilerecord',
	meta: {
		title: '成桩记录',
	},
},
{
	path: '/pages/ruanji/pilerecorddetail',
	name: 'pilerecorddetail',
	meta: {
		title: '成桩记录详情',
	},
},
{
	path: '/pages/ruanji/pilenote',
	name: 'pilenote',
	meta: {
		title: '桩记录',
	},
},
{
	path: '/pages/ruanji/pilenotedetail',
	name: 'pilenotedetail',
	meta: {
		title: '桩记录详情',
	},
},
{
	path: '/pages/ruanji/mixpile',
	name: 'mixpile',
	meta: {
		title: '桩基实时监测列表',
	},
},
{
	path: '/pages/ruanji/mixpilerealtime',
	name: 'mixpilerealtime',
	meta: {
		title: '桩基实时监测',
	},
},
{
	path: '/pages/ruanji/fillingPile',
	name: 'fillingPile',
	meta: {
		title: '灌注桩检测列表',
	},
},
{
	path: '/pages/ruanji/fillingPileDetail',
	name: 'fillingPile',
	meta: {
		title: '灌注桩检测详情',
	},
},
{
	path: '/pages/ruanji/pileefficiency',
	name: 'pileefficiency',
	meta: {
		title: '桩基功效',
	},
},
{
	path: '/pages/Rawmaterial/Rawmaterial',
	name: 'Rawmaterial',
	meta: {
		title: '原材料首页',
	},
},
{
	path: '/pages/Rawmaterial/Statistics',
	// name: 'Rawmaterial',
	meta: {
		title: '原材料首页',
	},
},
{
	path: '/pages/Rawmaterial/Statisticdetails',
	// name: 'Rawmaterial',
	meta: {
		title: '原材料首页',
	},
},

{
	path: '/pages/Entitydetection/Entitydetection',
	// name: 'Entitydetection',
	meta: {
		title: '实体检测',
	},
},
{
	path: '/pages/Entitydetection/Entitydetectionadd',
	// name: 'Entitydetectionadd',
	meta: {
		title: '新增检测委托',
	},
},
{
	path: '/pages/Entitydetection/Entitydetectiondetils',
	// name: 'Entitydetectiondetils',
	meta: {
		title: '新增检测委托详情',
	},
},
{
	path: '/pages/theory/theory',
	name: 'theory',
	meta: {
		title: '理论配合比',
	},
},
{
	path: '/pages/theory/theoryadd',
	name: 'theoryadd',
	meta: {
		title: '理论配合比新增页面',
	},
},
{
	path: '/pages/theory/theorydetails',
	name: 'theorydetails',
	meta: {
		title: '理论配合比详情',
	},
},
{
	path: '/pages/theory/theoryedit',
	name: 'theoryedit',
	meta: {
		title: '理论配合比编辑',
	},
},
{
	path: '/pages/smartsy/shigongphb/shigongphbManage',
	name: 'shigongphbManage',
	meta: {
		title: '配料单管理',
	},
},
{
	path: '/pages/smartsy/shigongphb/shigongphbAdd',
	name: 'shigongphbAdd',
	meta: {
		title: '配料单新增',
	},
},
{
	path: '/pages/smartsy/shigongphb/shigongphbAdd47',
	name: 'shigongphbAdd47',
	meta: {
		title: '配料单新增',
	},
},
{
	path: '/pages/smartsy/shigongphb/shigongphbDetail',
	name: 'shigongphbDetail',
	meta: {
		title: '配料单详情',
	},
},
{
	path: '/pages/Testdetection/Testdetection',
	// name: 'Testdetection',
	meta: {
		title: '标养室',
	},
},
{
	path: '/pages/Testdetection/Testwnj',
	// name: 'Testwnj',
	meta: {
		title: '万能机',
	},
},
{
	path: '/pages/Testdetection/Testwnjdetails',
	// name: 'Testwnj',
	meta: {
		title: '万能机详情',
	},
},
{
	path: '/pages/smartsy/smartsy',
	// name: 'smartsy',
	meta: {
		title: '智慧试验首页',
	},
},
{
	path: '/pages/Testdetection/Testylj',
	// name: 'Testylj',
	meta: {
		title: '压力机',
	},
},
{
	path: '/pages/Testdetection/Testyljdetails',
	// name: 'Testyljdetails',
	meta: {
		title: '压力机详情',
	},
}, {
	path: '/pages/smartsy/testingmachine/testingmachinehome',
	// name: 'testingmachinehome',
	meta: {
		title: '试验机',
	},
},
{
	path: '/pages/Testdetection/Testkykz',
	// name: 'Testkykz',
	meta: {
		title: '抗压抗折'
	}
},
{
	path: '/pages/Testdetection/Testkykzdetails',
	// name: 'Testkykzdetails',
	meta: {
		title: '抗压抗折'
	}
},
{
	path: '/pages/smartsy/sampling/samplinglist',
	// name: 'samplinglist',
	meta: {
		title: '试块管理',
	},
},
{
	path: '/pages/smartsy/sampling/samplingadd',
	// name: 'samplingadd',
	meta: {
		title: '见证取样',
	},
},
{
	path: '/pages/smartsy/sampling/samplingdetail',
	// name: 'samplingdetail',
	meta: {
		title: '见证取样详情',
	},
},
{
	path: '/pages/smartsy/sampling/testblocksave',
	// name: 'testblocksave',
	meta: {
		title: '试块上架',
	},
},
{
	path: '/pages/smartsy/sampling/testblockdown',
	// name: 'testblockdown',
	meta: {
		title: '试块下架',
	},
},
{
	path: '/pages/smartbh/Entitytracing',
	// name: 'Entitytracing',
	meta: {
		title: '实体追溯'
	}
},
{
	path: '/pages/Rawmaterial/depart/departlist',
	// name:'departlist',
	meta: {
		title: '发车单'
	}
},
{
	path: '/pages/Rawmaterial/depart/departadd',
	// name:'departadd',
	meta: {
		title: '发车单申报'
	}

},
{
	path: '/pages/Rawmaterial/depart/departdetails',
	// name:'departdetails',
	meta: {
		title: '发车单详情'
	}

},
{
	path: '/pages/Rawmaterial/check/checklist',
	// name:'checklist',
	meta: {
		title: '到场待审核'
	}

},
{
	path: '/pages/Rawmaterial/Realtime/realtimedata',
	// name:'checklist',
	meta: {
		title: '发车单实时数据'
	}

},
{
	path: '/pages/Rawmaterial/statistics/statistics',
	name: 'statistics',
	meta: {
		title: '统计分析'
	}

},
{
	path: '/pages/ruanji/pilequality',
	name: 'pilequality',
	meta: {
		title: '桩质量分析'
	}

},
{
	path: '/pages/ruanji/maprealtime',
	name: 'maprealtime',
	meta: {
		title: '地图'
	}

},
{
	path: '/pages/smartbh/pourorder/pourAudit',
	name: 'pourAudit',
	meta: {
		title: '浇筑令审核'
	}

},
{
	path: '/pages/smartbh/concreteorder/concreteorder',
	meta: {
		title: '混凝土订单'
	}
},
{
	path: '/pages/Rawmaterial/check/arrivaledlist',
	name: 'arrivaledlist',
	meta: {
		title: '到场确认'
	}

},
{
	path: '/pages/Rawmaterial/check/jiesuo',
	name: 'jiesuo',
	meta: {
		title: '到场解锁'
	}
},
{
	path: '/pages/smartsy/Concreteiron/concreteiron',
	// name:'concreteiron',
	meta: {
		title: '钢筋保护层'
	}
},
{
	path: '/pages/smartsy/Concreteiron/concrete',
	name: 'concreteiron',
	meta: {
		title: '混凝土回弹'
	}

},
{
	path: '/pages/smartsy/Concreteiron/Cktesting',
	// name:'concreteiron',
	meta: {
		title: '成孔质量检测'
	}
},
{
	path: '/pages/smartsy/Concreteiron/Zjtesting',
	// name:'concreteiron',
	meta: {
		title: '桩基超声波检测'
	}
},
{
	path: '/pages/smartlc/infolist/infolist',
	meta: {
		title: '梁信息列表'
	}
},
{
	path: '/pages/smartlc/infolist/infodetails',
	meta: {
		title: '梁详情列表'
	}
},
{
	path: '/pages/smartlc/steplist/steplist',
	meta: {
		title: '梁信息工序列表'
	}
},
{
	path: '/pages/smartlc/steplist/stepdetails',
	meta: {
		title: '梁详情信息'
	}
},
{
	path: '/pages/smartlc/steplist/stepTake',
	meta: {
		title: '梁信息工序确认'
	}
},
{
	path: '/pages/smartlc/steplist/stepSave',
	meta: {
		title: '梁信息工序确认'
	}
},
{
	path: '/pages/smartlc/steplist/stepConfirm',
	meta: {
		title: '梁信息工序确认'
	}
},
{
	path: '/pages/smartlc/Storagebeam/Storagebeam',
	meta: {
		title: '存梁区'
	}
},
{
	path: '/pages/smartlc/Storagebeam/Storagedetails',
	meta: {
		title: '存梁详情'
	}
},
{
	path: '/pages/smartlc/Gantrycrane/Gantrycrane',
	meta: {
		title: '龙门吊监测'
	}
},
{
	path: '/pages/smartlc/Zllist/Zllist',
	meta: {
		title: '张拉数据监测'
	}
},
{
	path: '/pages/smartlc/Zllist/Zldetails',
	meta: {
		title: '张拉详情信息'
	}
},
{
	path: '/pages/smartlc/Yjlist/Yjlist',
	meta: {
		title: '压浆数据监测'
	}

},
{
	path: '/pages/smartlc/Yjlist/Yjdetails',
	meta: {
		title: '压浆详情数据'
	}
},
{
	path: '/pages/smartlc/selfhelp/selfhelp',
	meta: {
		title: '自助下单'
	}
},
{
	path: '/pages/smartlc/orderTracking/orderTracking',
	meta: {
		title: '订单追踪'
	}
},
{
	path: '/pages/smartlc/orderTracking/orderlist',
	mata: {
		title: '订单详情'
	}
},
{
	path: '/pages/smartlc/beamInfo/beamInfo',
	mata: {
		title: '梁板信息'
	}
},
{
	path: '/pages/smartlc/orderTracking/orderadd',
	mata: {
		title: '订单新增'
	}
},
{
	path: '/pages/smartlc/orderTracking/Zlcheck',
	mata: {
		title: '制梁审核'
	}
},
{
	path: '/pages/SmartYd/SmartYd',
	meta: {
		title: '智慧用电'
	}
},
{
	path: '/pages/smartlc/orderTracking/jzllist',
	meta: {
		title: '浇筑令新增'
	}
},
{
	path: '/pages/smartbh/messageDetails',
	meta: {
		title: '首页消息中心详情',
	}
},
{
	path: '/pages/smartbh/Seemorelist',
	meta: {
		title: '热门资讯查看更多'
	}
},
{
	path: '/pages/smartbh/messagelook',
	meta: {
		title: '首页消息中心查看更多'
	}
},
{
	path: '/pages/smartlc/customerPay/customerPay',
	meta: {
		title: '交付进度'
	}
},
{
	path: '/pages/smartlc/customerPay/customerList',
	meta: {
		title: '交付进度详情'
	}
},
{
	path: '/pages/SmartPavement/asphalt/asphalt',
	meta: {
		title: '沥青管控',
	},
},
{
	path: '/pages/SmartPavement/waterControl/waterControl',
	meta: {
		title: '水稳管控'
	}
},
{
	path: '/pages/SmartPavement/Paving/Paving',
	meta: {
		title: '摊铺碾压'
	}
},
{
	path: '/pages/SmartPavement/asphalt/asphaltmix',
	meta: {
		title: '沥青拌合列表'
	}
},
{
	path: '/pages/SmartPavement/asphalt/asphaltmixdetails',
	meta: {
		title: '沥青拌合详情'
	}
},
{
	path: '/pages/SmartPavement/AsphaltOverproof/AsphaltOverproof',
	meta: {
		title: '沥青超标'
	}
},
{
	path: '/pages/SmartPavement/AsphaltOverproof/AsphaltOverDetail',
	meta: {
		title: '沥青超标详情'
	}
},
{
	path: '/pages/SmartPavement/WaterOverproof/WaterOverproof',
	meta: {
		title: '水稳超标'
	}
},
{
	path: '/pages/SmartPavement/WaterOverproof/WaterOverDetail',
	meta: {
		title: '水稳超标详情'
	}
},
{



	path: '/pages/home/message',
	meta: {
		title: '首页消息提示'
	}
},
{
	path: "/pages/smartbh/mix/Silomonitor",
	meta: {
		title: '料仓动态监测'
	}
},
{
	path: "/pages/smartbh/overHandle/overHandle",
	meta: {
		title: '超标处理'
	}
},
{
	path: "/pages/Rawmaterial/Tasklist/Tasklist",
	meta: {
		title: '送货任务单'
	}
},
{
	path: "/pages/Rawmaterial/Tasklist/Tasklistdetails",
	meta: {
		title: '送货任务单详情'
	}
},
{
	path: "/pages/MaterialProcurement/DeliveryDeclaration/DeliveryDeclaration",
	meta: {
		title: '发货申报'
	}
},
{
	path: "/pages/MaterialProcurement/DeliveryManagement/DeliveryManagement",
	meta: {
		title: '发货管理'
	}
},
{
	path: "/pages/MaterialProcurement/check/arrivaledlist",
	meta: {
		title: '到场确认'
	}
},
{
	path: "/pages/MaterialProcurement/check/checklist",
	meta: {
		title: '到场审核'
	}
},
{
	path: "/pages/MaterialProcurement/check/checkdetails",
	meta: {
		title: '到场审核详情'
	}
},
{
	path: "/pages/MaterialInspection/sampling/sampling",
	meta: {
		title: '取样委托'
	}
},
{
	path: "/pages/MaterialProcurement/check/spotCheck",
	meta: {
		title: '车辆抽检'
	}
},

{
	path: "/pages/MaterialProcurement/check/spotCheckAudit",
	meta: {
		title: '抽检审核'
	}
},
{
	path: "/pages/MaterialProcurement/check/spotCheckAuditDetails",
	meta: {
		title: '抽检审核详情'
	}
},
{
	path: "/pages/CarDispatch/CarInfo/CarInfo",
	meta: {
		title: '车辆调度'
	}
},
{
	path: "/pages/CarDispatch/CarInfo/CarDispatch",
	meta: {
		title: '车辆调度管理'
	}
},
{
	path: "/pages/CarDispatch/CarInfo/CarTrack",
	meta: {
		title: '车辆轨迹'
	}
},
{
	path: "/pages/CarDispatch/CarInfo/CarTrack1",
	meta: {
		title: '车辆轨迹回放'
	}
},
{
	path: "/pages/MaterialInspection/sampling/entrustDetails",
	meta: {
		title: '委托详情'
	}
},
{
	path: "/pages/smartlc/orderTracking/zlTaskList",
	meta: {
		title: '张拉任务单新增'
	}
},
{
	path: "/pages/smartlc/orderTracking/yjTaskList",
	meta: {
		title: '压浆任务单新增'
	}
},
{
	path: "/pages/smartlc/ZlOverproof/ZlOverproof",
	meta: {
		title: '张拉超标'
	}
},
{
	path: "/pages/smartlc/ZlOverproof/ZlOverDetail",
	meta: {
		title: '张拉超标详情'
	}
},
{
	path: "/pages/smartlc/YjOverproof/YjOverproof",
	meta: {
		title: '压浆超标'
	}
},
{
	path: "/pages/smartlc/YjOverproof/YjOverDetail",
	meta: {
		title: '压浆超标详情'
	}
},
{
	path: "/pages/smartgb/smartgb",
	meta: {
		title: '智慧过磅'
	}
},
{
	path: "/pages/smartgb/Smartgb/smartgblist",
	meta: {
		title: '智慧过磅列表查询'
	}
},
{
	path: "/pages/smartgb/Smartgb/smartgbdetails",
	meta: {
		title: '智慧过磅详情'
	}

},
{
	path: "/pages/smartgb/Smartgb/smartgbjc",
	meta: {
		title: '智慧过磅进场材料'
	}
},
{
	path: "/pages/smartgb/Smartgb/smartgbcc",
	meta: {
		title: '智慧过磅出场材料'
	}

},
{
	path: "/pages/smartgb/Smartenv/smartenv",
	meta: {
		title: '环保监测'
	}

},
{
	path: "/pages/smartgb/SafeCheck/SafeCheck",
	meta: {
		title: '安全检查'
	}

},
{
	path: "/pages/smartgb/SafeCheck/handle/AddList",
	meta: {
		title: '新增'
	}

},
{
	path: "/pages/smartgb/SafeCheck/handle/edit",
	meta: {
		title: '整改'
	}

},
{
	path: "/pages/smartgb/VideoMonitor/VideoMonitorlist",
	meta: {
		title: '视频监控'
	}

},
{
	path: "/pages/smartgb/VideoMonitor/VideoMonitorDetail",
	meta: {
		title: '视频监控详情'
	}

},
{
	path: "/pages/smartgb/lmdrealdata/DeviceCraneRealdataList",
	meta: {
		title: '龙门吊'
	}

},
{
	path: "/pages/smartgb/lmdrealdata/DeviceCraneRealdataDetail",
	meta: {
		title: '龙门吊详情'
	}

},
{
	path: "/pages/smartgb/bridgerealdata/DeviceCraneRealdataList",
	meta: {
		title: '架桥机'
	}

},
{
	path: "/pages/smartgb/bridgerealdata/DeviceCraneRealdataDetail",
	meta: {
		title: '架桥机详情'
	}

},
{
	path: "/pages/UpApp/UpApp",
	meta: {
		title: '更新'
	}
},
{
	path: "/pages/smartbh/mix/handle/ChuZhi",
	meta: {
		title: '施工处置'
	}
},
{
	path: "/pages/smartbh/mix/handle/ShenHe",
	meta: {
		title: '监理审核'
	}
},
{
	path: "/pages/smartbh/mix/handle/ShenPi",
	meta: {
		title: '指挥部审批'
	}
},
{
	path: "/pages/smartbh/mix/handle/DealDetail",
	meta: {
		title: '处置详情'
	}
},
{
	path: "/pages/smartlc/ZlOverproof/handle/ZlChuZhi",
	meta: {
		title: '张拉施工处置'
	}
},
{
	path: "/pages/smartlc/ZlOverproof/handle/ZlShenHe",
	meta: {
		title: '张拉监理审核'
	}
},
{
	path: "/pages/smartlc/ZlOverproof/handle/ZlShenPi",
	meta: {
		title: '张拉指挥部审批'
	}
},
{
	path: "/pages/smartlc/YjOverproof/handle/YjChuZhi",
	meta: {
		title: '压浆施工处置'
	}
},
{
	path: "/pages/smartlc/YjOverproof/handle/YjShenHe",
	meta: {
		title: '压浆监理审核'
	}
},
{
	path: "/pages/smartlc/YjOverproof/handle/YjShenPi",
	meta: {
		title: '压浆指挥部审批'
	}
},
{
	path: "/pages/Rawmaterial/SiloManagement/SiloManagement",
	meta: {
		title: '料仓管理'
	}
},
{
	path: "/pages/Rawmaterial/SiloManagement/SiloManageDetail",
	meta: {
		title: '料仓详情'
	}
},
{
	path: "/pages/WebView/Iroadw",
	meta: {
		title: '摊铺碾压'
	}
},
{
	path: "/pages/ruanji/PipeProduction",
	meta: {
		title: '管桩生产记录'
	}
},
{
	path: "/pages/ruanji/PipeProductionDetail",
	meta: {
		title: '管桩生产记录详情'
	}
},
{
	path: "/pages/ruanji/PipeWarning",
	meta: {
		title: '管桩预警信息'
	}
},
{
	path: "/pages/ruanji/PipeMonitoring",
	meta: {
		title: '管桩设备监控'
	}
},
{
	path: "/pages/ruanji/PipeMonitoringDetail",
	meta: {
		title: '管桩设备监控详情'
	}
},
{
	path: "/pages/smartbh/Instruct/InstructList",
	meta: {
		title: '物联网指令单'
	}
},
{
	path: "/pages/CarDispatch/CarInfo/carlist",
	meta: {
		title: '混凝土发车单'
	}
},
{
	path: "/pages/zlyj/zl/zlTaskList",
	meta: {
		title: '张拉任务单'
	}
},
{
	path: "/pages/zlyj/zl/zlAdd",
	meta: {
		title: '张拉任务单'
	}
},
{
	path: "/pages/zlyj/yj/yjTaskList",
	meta: {
		title: '张拉任务单'
	}
},
{
	path: "/pages/zlyj/yj/yjAdd",
	meta: {
		title: '张拉任务单'
	}
},
{
	path: "/pages/personManage/StaffBaseInfoListM",
	meta: {
		title: '人员信息'
	}
},
{
	path: "/pages/personManage/StaffInfo",
	meta: {
		title: '人员信息详情'
	}
},
{
	path: "/pages/personManage/StaffInfo1",
	meta: {
		title: '人员信息详情'
	}
},
{
	path: "/pages/MonthHour/MonthHourListM",
	meta: {
		title: '考勤统计'
	}
},
{
	path: "/pages/personManage/EntranceGuardRecordList",
	meta: {
		title: '人员门禁'
	}
},
{
	path: "/pages/DeviceManage/MobileDevice",
	meta: {
		title: '移动设备'
	}
},
{
	path: "/pages/DeviceManage/DeviceCheck",
	meta: {
		title: '设备巡检'
	}
},
{
	path: "/pages/DeviceManage/DeviceCheckInfo",
	meta: {
		title: '设备巡检详情'
	}
},
{
	path: "/pages/DeviceManage/AddDeviceCheck",
	meta: {
		title: '添加设备巡检记录'
	}
},
{
	path: "/pages/smartgb/DeviceCheck/DeviceCheck",
	meta: {
		title: '设备监测'
	}
},
{
	path: "/pages/smartgb/DeviceCheck/DeviceCurve",
	meta: {
		title: '曲线数据详情'
	}
}


]
export default routes
