<template>
	<view id="smartbh">
		<view style="height: 150upx;" class=""></view>
		<!-- 次级页面标签 -->
		<!-- <view class="top">
			<view class="top-item" @click="handleNav(0)">
				<view class="top-item-img" style="background-image: url(../../static/pour/tag1.png);"></view>
				<view class="top-item-name">订单追踪</view>
			</view>
			<view class="top-item" @click="handleNav(1)">
				<view class="top-item-img" style="background-image: url(../../static/pour/tag2.png);"></view>
				<view class="top-item-name">自助下单</view>
			</view>
			<view class="top-item" @click="handleNav(2)">
				<view class="top-item-img" style="background-image: url(../../static/pour/tag3.png);"></view>
				<view class="top-item-name">一键收梁</view>
			</view>
			<view class="top-item" @click="handleNav(3)">
				<view class="top-item-img" style="background-image: url(../../static/pour/tag4.png);"></view>
				<view class="top-item-name">工序管理</view>
			</view>
			<view class="top-item" @click="handleNav(4)">
				<view class="top-item-img" style="background-image: url(../../static/pour/tag5.png);"></view>
				<view class="top-item-name">存找梁</view>
			</view>
			<view class="top-item" @click="handleNav(5)">
				<view class="top-item-img" style="background-image: url(../../static/pour/tag6.png);"></view>
				<view class="top-item-name">全部</view>
			</view>
		</view> -->
		<view class="top" v-if="menu.length > 0">
			<view class="top-item" v-for="(item,i) in menu" :key="i" @click="handleNav(item.url)">
				<view class="top-item-img" :style="item.indexpic"></view>
				<!-- <view class="top-item-img" :style="item.style"></view> -->
				<view class="top-item-name">{{ item.title }}</view>
			</view>
			<view class="top-item" @click="handleNav()">
				<view class="top-item-img" style="background-image: url(../../static/pour/tag6.png);"></view>
				<view class="top-item-name">全部</view>
			</view>
		</view>
		<view>

		</view>
		<!-- 消息中心 -->
		<view class="news">
			<view class="news-title">
				<view class="news-title-name">任务及工序消息播报</view>
				<view class="news-title-more"> <span>查看更多</span>
					<u-icon color="#999999" size="12" name="arrow-right"></u-icon>
				</view>
			</view>
			<view class="news-con" v-if="newslist.length > 0">
				<view class="news-con-item" v-for="(item,index) in newslist" :key="index">
					<view class="news-con-item-but">{{ item.overLevel_dictText }}</view>
					<view class="news-con-item-des">{{ item.overReason }}{{ item.poureLocation }}</view>
				</view>
			</view>
			<view class="news-con" v-else>
				<view class="news-con-item">
					<u-notice-bar bgColor="#fff" fontSize=16 color="#91CB74" text="暂无新消息提醒"></u-notice-bar>
				</view>
				<view class="tiptitle"></view>
			</view>
		</view>

		<!-- 订单进度 -->
		<view class="beam">
			<view class="beam-title">订单进度</view>
			<view class="beam-line"></view>

			<view class="beam-box">
				<view class="beam-box-content" @click="orderstatus(0)">
					<view>未开始</view>
					<view class="num">{{orderList.notstarted || 0}}</view>
				</view>
				<p></p>
				<view class="beam-box-content" @click="orderstatus(1)">
					<view>生产中</view>
					<view class="num">{{orderList.inproduction || 0}}</view>
				</view>
				<p></p>
				<view class="beam-box-content" @click="orderstatus(2)">
					<view>已交付</view>
					<view class="num">{{orderList.paid || 0}}</view>
				</view>
			</view>

		</view>
		<!-- 交付进度统计 -->
		<view class="tableStyle">
			<view class="tableStyle-title" style="display: flex;justify-content: space-between;align-items: center;">
				<view>交付进度统计</view>
				<navigator :url="'/pages/smartlc/customerPay/customerPay'">
					<view class="news-title-more"><span>查看更多</span>
						<u-icon color="#999999" size="12" name="arrow-right"></u-icon>
					</view>
				</navigator>
			</view>
			<view class="table">
				<uni-table stripe emptyText="暂无更多数据">
					<uni-tr>
						<uni-th width="50" align="center">序号</uni-th>
						<uni-th width="70" align="center">客户名称</uni-th>
						<uni-th width="60" align="center">计划供应量</uni-th>
						<uni-th width="50" align="center">已交付量</uni-th>
						<uni-th width="60" align="center">进度百分比</uni-th>
					</uni-tr>
					<uni-tr v-for="(item, index) in deliverTableData" :key="index">
						<uni-td align="center">{{item.xuaho}}</uni-td>
						<uni-td align="center">
							<view class="table-content">{{item.material}}</view>
						</uni-td>
						<uni-td align="center">{{item.type}}</uni-td>
						<uni-td align="center">{{item.amount}}</uni-td>
						<uni-td align="center">{{item.stock}}</uni-td>
					</uni-tr>
				</uni-table>
			</view>
		</view>
		<!-- 制梁总览 -->
		<!-- <view class="beam">
			<view class="beam-title">制梁总览</view>
			<u-scroll-list indicatorColor="#F0F6FC" indicatorActiveColor="#46ACF8 ">
				<view v-for="(item, index) in menuArr" :key="index">
					<view class="beamCon" v-for="(item1, index1) in item" :key="index1">
						<view class="beamCon-type">{{item1.type}}</view>
						<text class="beamCon-amount">{{item1.amount}}片</text>
					</view>
				</view>
			</u-scroll-list>
		</view> -->
		<!-- 台座使用率 -->
		<view class="pedestal">
			<view class="pedestal-title">台座使用率</view>
			<view class="pedestal-total">累计已出场交付：<text>{{zhiliangList.chuchangTJ || 0}}片</text></view>
			<view class="pedestal-body">
				<view>
					<view class="charts-box">
						<qiun-data-charts type="arcbar" :opts="opts1" :chartData="chartData1" />
					</view>
					<view class="circle-title">制梁台座</view>
				</view>
				<view>
					<view class="charts-box">
						<qiun-data-charts type="arcbar" :opts="opts2" :chartData="chartData2" />
					</view>
					<view class="circle-title">存梁台座</view>
				</view>
			</view>

		</view>
		<!-- 重点工序实时排班 -->
		<!-- <view class="tableStyle">
			<view class="tableStyle-title">重点工序实时排班</view>
			<view class="pedestal-total">生产中构件数：<text>{{schedulingTableData.production}}片</text></view>
			<view class="table">
				<uni-table stripe emptyText="暂无更多数据">
					<!-- 表头行 -->
					<!-- <uni-tr>
						<uni-th width="100" align="center">序号</uni-th>
						<uni-th width="100" align="center">工序名称</uni-th>
						<uni-th width="100" align="center">梁板数</uni-th>
					</uni-tr>
					<!-- 表格数据行 -->
					<!-- <uni-tr v-for="(item, index) in schedulingTableData.gongxu" :key="index">
						<uni-td align="center">{{index > 8 ? index + 1 : '0' + (index + 1)}}</uni-td>
						<uni-td align="center">{{item.mingchen}}</uni-td>
						<uni-td align="center">{{item.sum}}</uni-td>
					</uni-tr>
				</uni-table>
			</view>
		</view> -->
		<!-- 混凝土需求分析表 -->
		<view class="tableStyle">
			<view class="tableStyle-title">混凝土需求分析表</view>
			<view class="table">
				<uni-table stripe emptyText="暂无更多数据">
					<!-- 表头行 -->
					<uni-tr>
						<uni-th width="100" align="center">序号</uni-th>
						<uni-th width="100" align="center">强度等级</uni-th>
						<uni-th width="100" align="center">需求数量</uni-th>
					</uni-tr>
					<!-- 表格数据行 -->
					<uni-tr v-for="(item, index) in concreteTableData" :key="index">
						<uni-td align="center">{{index > 8 ? index + 1 : '0' + (index + 1)}}</uni-td>
						<uni-td align="center">{{item.betLev}}</uni-td>
						<uni-td align="center">{{item.betlevsum}}</uni-td>
					</uni-tr>
				</uni-table>
			</view>
		</view>
		<!-- 原材料需求分析表 -->
		<view class="tableStyle">
			<view class="tableStyle-title">原材料需求分析表</view>
			<view class="table">
				<uni-table stripe emptyText="暂无更多数据">
					<uni-tr>
						<uni-th width="50" align="center">序号</uni-th>
						<uni-th width="60" align="center">材料名称</uni-th>
						<uni-th width="60" align="center">规格类型</uni-th>
						<uni-th width="60" align="center">需求数量</uni-th>
						<uni-th width="60" align="center">当前库存</uni-th>
					</uni-tr>
					<uni-tr v-for="(item, index) in materialTableData" :key="index">
						<uni-td align="center">{{item.xuaho}}</uni-td>
						<uni-td align="center">{{item.material}}</uni-td>
						<uni-td align="center">{{item.type}}</uni-td>
						<uni-td align="center">{{item.amount}}</uni-td>
						<uni-td align="center">{{item.stock}}</uni-td>
					</uni-tr>
				</uni-table>
			</view>
		</view>
		<!-- 热门活动 -->
		<view class="news">
			<view class="news-title">
				<view class="news-title-name">热门资讯</view>
				<view class="news-title-more"> <span>查看更多</span>
					<u-icon color="#999999" size="12" name="arrow-right"></u-icon>
				</view>
			</view>
			<view class="news-activity" v-for="(item,index) in messagelist" :key="index" @click="messageclick(item)">
				<view class="news-activity-img">
					<u--image :showLoading="true" :src="item.url" width="90px" height="96px" shape="square"
						radius="10px"></u--image>
				</view>
				<view class="news-activity-con">
					<view class="news-activity-con-title">{{item.name}}</view>
					<!-- <view class="news-activity-con-text">
						<u-read-more :toggle="true" closeText="更多" openText="收起" :showHeight="50">
							<rich-text style="font-size: 13px;" :nodes="item.description"></rich-text>
						</u-read-more>
					</view> -->
					<view class="news-activity-con-time">
						<!-- <text>200</text> -->
						<view class="img">
							{{item.sysOrgCode_dictText}}
						</view>
						<view class="wrap-font">
							<view>{{item.createTime.substring(5)}}</view>
						</view>
					</view>
				</view>
			</view>
			<view class="news-video" v-for="(video,index) in messagevideo" :key="index">
				<view class="news-video-img">
					<video id="myVideo" object-fit="cover" muted autoplay
						style="width: 342px;height: 137px;border-radius: 10px;" :src="video.url"
						controls="true"></video>
				</view>
				<view class="news-video-title">{{video.name}}</view>
				<view class="news-activity-con-text">{{video.description}}</view>
				<view class="news-video-time">
					<!-- <text>200</text> -->
					<text style="float: right; margin-right: 14upx;padding:10px 0;">{{video.createTime}}</text>
				</view>
			</view>
		</view>
		<!--  -->
		<view style="height: 150upx;" class=""></view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				pageNo: 1,
				menu:[],
				messagelist: [],
				messagevideo: [],
				newslist: [],
				nameid: '',
				zhiliangList: {},
				cunliangList: {},
				orderList: {},
				menuArr: [
					[{
							type: 'T梁44MM',
							amount: 56
						},
						{
							type: 'T梁44MM',
							amount: 57
						},
						{
							type: 'T梁44MM',
							amount: 58
						}
					],
					[{
							type: 'T梁44MM',
							amount: 56
						},
						{
							type: 'T梁44MM',
							amount: 57
						},
						{
							type: 'T梁44MM',
							amount: 58
						}
					],
					[{
							type: 'T梁44MM',
							amount: 56
						},
						{
							type: 'T梁44MM',
							amount: 57
						},
						{
							type: 'T梁44MM',
							amount: 58
						}
					],
					[{
							type: 'T梁44MM',
							amount: 56
						},
						{
							type: 'T梁44MM',
							amount: 57
						},
						{
							type: 'T梁44MM',
							amount: 58
						}
					],
					[{
							type: 'T梁44MM',
							amount: 56
						},
						{
							type: 'T梁44MM',
							amount: 57
						},
						{
							type: 'T梁44MM',
							amount: 58
						}
					]
				],
				schedulingTableData: {
					production: 0,
					gongxu: []
				},
				concreteTableData: [],
				materialTableData: [],
				deliverTableData: [],
				chartData1: {
					series: [{
						name: '百分比',
						data: 0,
						color: '#FF5E5E'
					}]
				},
				chartData2: {
					series: [{
						name: '百分比',
						data: 0,
						color: '#387EFF'
					}]
				},
				opts1: {
					title: {
						name: "",
						fontSize: 20,
						color: "#333333"
					},
					subtitle: {
						name: "",
						fontSize: 20,
						color: "#666666"
					},
					extra: {
						arcbar: {
							type: "circle",
							width: 10,
							backgroundColor: "#E9E9E9",
							startAngle: 1.5,
							endAngle: 0.25,
							gap: 2,
							radius: 50
						}
					}
				},
				opts2: {
					title: {
						name: "",
						fontSize: 20,
						color: "#333333"
					},
					subtitle: {
						name: "",
						fontSize: 20,
						color: "#666666"
					},
					extra: {
						arcbar: {
							type: "circle",
							width: 10,
							backgroundColor: "#E9E9E9",
							startAngle: 1.5,
							endAngle: 0.25,
							gap: 2,
							radius: 50
						}
					}
				}
			}
		},
		props:['menuList'],
		watch: {
			menuList: {
				handler() {
					console.log(this.parentId,"parentId-------------------子组件");
					this.handlerMenu();
					// this.getMenuList();
				},
			}
		},
		mounted() {
			// this.getMixcblist();
			// this.pedestalData();
			// this.scheduling();
			this.concrete();
			this.order();
			this.lcmessage();
		},
		computed: {
			zhiliang() {
				return this.zhiliangList.sum ? this.zhiliangList.zhiliang / this.zhiliangList.sum : 0
			},
			cunliang() {
				return this.cunliangList.sum ? this.cunliangList.cunliang / this.cunliangList.sum : 0
			}
		},
		methods: {
			handlerMenu(){
				this.menu = this.menuList.mate.filter((e)=>{
					return e.indexpic.indexOf("http") != -1;
				});
				if(this.menu.length > 5 ){
					this.menu.length = 5;
				};
				console.log(this.menu,this.menuList.mate,'handlerMenu----------------------------------------------智慧梁场');
			},
			handleNav(url) {
				// console.log("test----------------------------------点击");
				if (url == "/pages/smartlc/infolist/infolist") {
					var that = this;
					that.nameid = ''
					// 允许从相机和相册扫码
					uni.scanCode({
						success: (res) => {
							let qcode = res.result.slice(41)
							console.log(qcode,'gggg')
							if (res.scanType == 'QR_CODE') {
								that.nameid = qcode;
								setTimeout(() => {
									uni.showToast({
										title: "正在加载中，请稍侯",
										icon: 'loading'
									})
									setTimeout(() => {
										if (that.nameid != '') {
											uni.navigateTo({
												url: '/pages/smartlc/infolist/infolist?id=' +
													that.nameid
											})
										} else {
											this.$tip.error('请重新扫码');
										}
									}, 500)

								}, 1000)
							} else {
								uni.showToast({
									title: "请重新扫描二维码",
									icon: 'none'
								})
							}
							console.log("条码类型：" + res.scanType);
							console.log("条码内容：" + res.result);
						}
						// ,
						// fail(err) {
						// 	if (that.nameid = "") {
						// 		uni.showToast({
						// 			title: "扫码失败,请重新扫码",
						// 			icon: 'none'
						// 		})
						// 	}
						// }


					});
					
				} else if(url){
					uni.navigateTo({
						url: url
					})
				}else {
					uni.navigateTo({
						url: '/pages/Labellist/Labellist'
					})
				}
			},
			// handleNav(e) {
			// 	if (e == 0) {
			// 		uni.navigateTo({
			// 			url: '/pages/smartlc/orderTracking/orderTracking'
			// 			// url: '/pages/smartbh/Entitytracing'
			// 		})
			// 	}
			// 	if (e == 1) {
			// 		uni.navigateTo({
			// 			url: '/pages/smartlc/selfhelp/selfhelp'
			// 		})
			// 	}
			// 	if (e == 2) {
			// 		var that = this;
			// 		that.nameid = ''
			// 		// 允许从相机和相册扫码
			// 		uni.scanCode({
			// 			success: (res) => {
			// 				let qcode = res.result.slice(41)
			// 				console.log(qcode,'gggg')
			// 				if (res.scanType == 'QR_CODE') {
			// 					that.nameid = qcode;
			// 					setTimeout(() => {
			// 						uni.showToast({
			// 							title: "正在加载中，请稍侯",
			// 							icon: 'loading'
			// 						})
			// 						setTimeout(() => {
			// 							if (that.nameid != '') {
			// 								uni.navigateTo({
			// 									url: '/pages/smartlc/infolist/infolist?id=' +
			// 										that.nameid
			// 								})
			// 							} else {
			// 								this.$tip.error('请重新扫码');
			// 							}
			// 						}, 500)

			// 					}, 1000)
			// 				} else {
			// 					uni.showToast({
			// 						title: "请重新扫描二维码",
			// 						icon: 'none'
			// 					})
			// 				}
			// 				console.log("条码类型：" + res.scanType);
			// 				console.log("条码内容：" + res.result);
			// 			}
			// 			// ,
			// 			// fail(err) {
			// 			// 	if (that.nameid = "") {
			// 			// 		uni.showToast({
			// 			// 			title: "扫码失败,请重新扫码",
			// 			// 			icon: 'none'
			// 			// 		})
			// 			// 	}
			// 			// }


			// 		});

			// 	}
			// 	if (e == 3) {
			// 		uni.navigateTo({
			// 			url: '/pages/smartlc/steplist/steplist'
			// 		})
			// 	}
			// 	if (e == 4) {
			// 		uni.navigateTo({
			// 			url: '/pages/smartlc/Storagebeam/Storagebeam'
			// 		})
			// 	}
			// 	if (e == 5) {
			// 		uni.navigateTo({
			// 			url: '/pages/Labellist/Labellist'
			// 		})
			// 	}
			// },
			liangdata() {
				this.$http.get(`/zhiliangrenwudan/zhiliangrenwudan/list`, {
					params: {
						code: this.nameid
					}
				}).then(res => {
					console.log(res, '梁信息')
				})
			},
			// 未开始，生产中,已交付
			orderstatus(e) {
				if (e == 0) {
					// let params = 'statuuss: 0'
					uni.navigateTo({
						url: '/pages/smartlc/orderTracking/orderTracking?statuuss=0',
					});

				}
				if (e == 1) {
					// console.log(e)
					// let params = {
					// 	statuuss: 1,
					// }
					uni.navigateTo({
						url: '/pages/smartlc/orderTracking/orderTracking?statuuss=1',
					});
				}
				if (e == 2) {
					console.log(e)
					// let params = {
					// 	statuuss: 2,
					// }
					uni.navigateTo({
						url: '/pages/smartlc/orderTracking/orderTracking?statuuss=2',
					});
				}

			},
			// 订单进度
			order() {
				this.$http.get(`/beamorder/beamOrder/list2`).then(res => {
					if (res.data.code == 200) {
						this.orderList = res.data.result;
					}
				})
			},
			//重点工序实时排班
			// scheduling() {
			// 	this.$http.get(`/zhiliangtaizuocfg/zhiliangTaizuoCfg/querytj1`).then(res => {
			// 		if (res.data.code == 200) {
			// 			this.schedulingTableData = res.data.result;
			// 		}
			// 	})
			// },
			//混凝土需求分析表
			concrete() {
				this.$http.get(`/zhiliangtaizuocfg/zhiliangTaizuoCfg/querytj2`).then(res => {
					if (res.data.code == 200) {
						this.concreteTableData = res.data.result;
					}
				})
			},
			// 台座使用率
			// pedestalData() {
			// 	this.$http.get(`/zhiliangtaizuocfg/zhiliangTaizuoCfg/querytj`).then(res => {
			// 		if (res.data.code == 200) {
			// 			this.zhiliangList = res.data.result;
			// 			this.chartData1.series[0].data = this.zhiliang;
			// 			this.opts1.title.name = (Math.round(this.zhiliang * 10000)) / 100 + '%';
			// 		}
			// 	})
			// 	this.$http.get(`/cunliangprocedureconfig/cunliangProcedureConfig/querytj`).then(res => {
			// 		if (res.data.code == 200) {
			// 			this.cunliangList = res.data.result;
			// 			this.chartData2.series[0].data = this.cunliang;
			// 			this.opts2.title.name = (Math.round(this.cunliang * 10000)) / 100 + '%';
			// 		}
			// 	})
			// },
			// 消息中心 拌合站预警查询
			getMixcblist() {
				let params = {
					column: 'id',
					order: 'desc',
					pageSize: 3,
					overLevel: 3
				}
				this.$http.get('/hntbhz/bhzCementBase/list', {
					params
				}).then(res => {
					this.newslist = res.data.result.records
				})
			},
			// 底部消息中心
			lcmessage() {
				let params = {
					column: 'id',
					order: 'desc',
					topictype: 4,
				}
				this.$http.get(`/officialfile/officialFile/list7`, {
					params
				}).then(res => {
					this.messagelist = []
					this.messagevideo = []
					console.log(res, '智慧梁场消息中心')
					let data = res.data.result
					// this.messagelist = res.data.result
					data.forEach(msg => {
						console.log(msg)
						if (msg.filetype == 0) {
							this.messagelist.push(msg)
						} else if (msg.filetype == 1) {
							this.messagevideo.push(msg)
						}
					})
					console.log(this.messagelist, '图片合集')
					console.log(this.messagevideo, '视频合集')

				})
			},
			messageclick(m) {
				uni.navigateTo({
					url: '/pages/smartbh/messageDetails?item=' + JSON.stringify(m)
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	#smartbh {
		background-image: url(../../static/home/bg-header.png);
		background-repeat: no-repeat;
		background-size: 100%;

		.total {
			float: right;
			margin-right: 20upx;
			font-weight: normal;
			font-size: 30upx;
		}

		.top {
			width: 690upx;
			height: 334upx;
			margin: 10px auto;
			// margin-top: 30upx;
			border-radius: 16upx;
			background-color: white;
			display: flex;
			flex-wrap: wrap;
			// justify-content: space-between;
			align-items: center;


			&-item {
				width: 230upx;
				height: 150upx;
				border-radius: 16upx;
				background-color: white;
				display: flex;
				flex-direction: column;
				justify-content: center;
				align-items: center;

				&-img {
					width: 75upx;
					height: 75upx;
					background-repeat: no-repeat;
					background-size: 100% 100%;
					margin-bottom: 24upx;
				}

				&-num {
					font-size: 38upx;
					color: #333333;
					margin-bottom: 10upx;

					span {
						color: #4C5971;
						font-size: 28upx;
					}
				}

				&-name {
					font-size: 28upx;
					color: #333333;
					// margin-top: 24upx;
				}
			}
		}

		.news {
			width: 690upx;
			margin: 0 auto;
			margin-top: 30upx;

			&-title {
				display: flex;
				justify-content: space-between;
				align-items: center;

				&-name {
					color: #333333;
					font-size: 36upx;
					font-weight: 600;
				}

				&-more {
					display: flex;
					align-items: center;
					font-size: 24upx;
					color: #999999;
				}
			}

			&-con {
				margin-top: 30upx;

				&-item {
					margin-top: 30upx;
					display: flex;

					&-but {
						width: 130upx;
						padding: 0 10upx;
						height: 35upx;
						background-color: rgba(74, 127, 255, .1);
						font-size: 22upx;
						color: #387FFD;
						text-align: center;
					}

					&-des {
						width: 500upx;
						margin-left: 18upx;
						font-size: 26upx;
						color: #333333;
					}
				}
			}

			&-activity {
				height: 230upx;
				border-radius: 16upx;
				margin-top: 30upx;
				background-color: white;
				display: flex;
				align-items: center;

				&-img {
					width: 260upx;
					height: 200upx;
					margin-left: 16upx;
					// background-color: #1CBBB4;
					border-radius: 16upx;
				}

				&-con {
					height: 200upx;
					margin-left: 20upx;
					color: #333333;

					// background-color: red;
					&-title {
						font-size: 28upx;
						font-weight: 600;
						text-align: left;
						width: 239px;
						height: auto;
					}

					&-text {
						margin-top: 16upx;
						font-size: 24upx;
						display: -webkit-box;
						overflow: hidden;
						/*超出隐藏*/
						text-overflow: ellipsis;
						/*隐藏后添加省略号*/
						-webkit-box-orient: vertical;
						-webkit-line-clamp: 2; //想显示多少行
					}
				}
			}

			&-video {
				height: 500upx;
				border-radius: 16upx;
				margin-top: 30upx;
				background-color: white;
				color: #333333;

				&-img {
					height: 280upx;
					// background-color: #1CBBB4;
					border-radius: 16upx;
					// border: 1px solid navajowhite;
				}

				&-title {
					margin-top: 20upx;
					font-size: 28upx;
					font-weight: 600;
				}

				&-text {
					margin-top: 16upx;
					font-size: 24upx;
				}
			}
		}

	}


	.beam {
		width: 690rpx;
		margin: 30rpx auto 0;
		padding-bottom: 30rpx;
		border-radius: 16rpx;
		background-color: #FFFFFF;

		&-title {
			padding-top: 25rpx;
			margin-left: 30rpx;
			font-size: 36rpx;
			font-weight: 600;
			color: #333333;
		}

		&-line {
			margin: 30rpx auto;
			width: 632rpx;
			height: 2rpx;
			border-top: 4rpx dashed #EEEEEE;
		}

		&-box {
			display: flex;
			justify-content: space-evenly;

			p {
				width: 1px;
				height: 60rpx;
				background: #D0D2DA;
				margin: auto 0;
			}

			&-content {
				text-align: center;
				width: 160rpx;
				font-size: 30rpx;
				font-family: PingFang SC;
				font-weight: bold;
				color: #4C5971;
				line-height: 55rpx;

				.num {
					color: #333333;
				}
			}
		}

	}

	.pedestal {
		width: 690rpx;
		margin: 30rpx auto 0;
		padding-bottom: 30rpx;
		border-radius: 16rpx;
		background-color: #FFFFFF;

		&-title {
			padding-top: 25rpx;
			margin-left: 30rpx;
			font-size: 36rpx;
			font-weight: 600;
			color: #333333;
		}

		&-body {
			display: flex;
			justify-content: space-around;
			align-items: center;
		}

		&-total {
			margin: 10rpx 30rpx;
			font-size: 26rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #999999;

			text {
				color: #4C5971;
				font-weight: bold;
			}
		}
	}

	.tableStyle {
		width: 690rpx;
		margin: 30rpx auto 0;
		padding-bottom: 30rpx;
		border-radius: 16rpx;
		background-color: #FFFFFF;

		&-title {
			padding-top: 25rpx;
			margin-left: 30rpx;
			font-size: 36rpx;
			font-weight: 600;
			color: #333333;
		}
	}

	.charts-box {
		width: 300rpx;
		height: 300rpx;
	}

	.circle-title {
		font-size: 28rpx;
		font-family: PingFang SC;
		font-weight: bold;
		color: #4C5971;
		text-align: center;
	}

	.table {
		width: 95%;
		margin: 10rpx auto;

		&-content {
			overflow: hidden;
			text-overflow: ellipsis;
			white-space: nowrap;
			width: 70px;
		}
	}

	.beamCon {
		box-sizing: border-box;
		background: #F0F6FC;
		border-radius: 6rpx;
		text-align: center;
		font-size: 28rpx;
		margin: 20rpx;
		padding: 20rpx 30rpx;

		&-type {
			white-space: nowrap;
			font-family: PingFang SC;
			font-weight: bold;
			color: #333333;
		}

		&-amount {
			color: #46ACF8;
		}
	}
</style>
