<template>
	<view id="ruanji">
		<!-- 次级页面标签 -->
		<view style="height: 150rpx;"></view>
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
		<view class="top" v-else>
			<u-loading-icon mode="circle"></u-loading-icon>
		</view>
		<!-- 今日产能 -->
		<view class="devcntoday">
			<picker @change="Changesbname" :range="listdata" :range-key="'shebeino_dictText'">
				<view class="devcntoday-title">{{sbname==''?'选择设备':sbname}}</view>
			</picker>
			<view class="devcntoday-con" v-if="devcntoday!=''">
				<view class="devcntoday-con-item">
					<view class="devcntoday-con-item-title">总根数</view>
					<view style=" font-size: 30rpx;">{{ devcntoday.finishcount }}</view>
				</view>
				<view class="devcntoday-con-item">
					<view class="devcntoday-con-item-title">总桩长</view>
					<view style=" font-size: 30rpx;">{{ devcntoday.longlen }}m</view>
				</view>
				<view class="devcntoday-con-item">
					<view class="devcntoday-con-item-title">总浆量</view>
					<view style=" font-size: 30rpx;">
						{{ (devcntoday.downcement + devcntoday.upcement).toFixed(2) }}L
					</view>
				</view>
				<view class="devcntoday-con-item">
					<view class="devcntoday-con-item-title">总灰量</view>
					<view style=" font-size: 30rpx;">{{ devcntoday.pilecement }}KG</view>
				</view>
			</view>
			<view style="height: 20rpx;"></view>
		</view>
		<!-- 消息中心 -->
		<view class="news">
			<view class="news-title">
				<view class="news-title-name">消息中心</view>
				<!-- <view class="news-title-more"> <span>查看更多</span>
					<u-icon color="#999999" size="12" name="arrow-right"></u-icon>
				</view> -->
			</view>
			<view class="news-con" v-if="messagenum.length > 0">
				<scroll-view scroll-y style="height: 300rpx">
					<view class="news-con-item" @click="checkWarn(item)" v-for="item in messagenum" :key="item.id">
						<view class="news-con-item-but">预警</view>
						<view class="news-con-item-des">{{item.shebeino_dictText + item.problem}}</view>
					</view>
				</scroll-view>
			</view>
			<view class="news-con" v-else>
				<view class="news-con-item">
					<u-notice-bar bgColor="#fff" fontSize=16 color="#91CB74" text="暂无新消息提醒"></u-notice-bar>
				</view>
				<view class="tiptitle"></view>
			</view>
		</view>
		<view class="daycn">
			<view class="daycn-title">
				<view class="daycn-title-name">搅拌桩总体数据</view>
				<view class="daycn-title-time">
					<u-icon color="#FFFFFF" size="28" name="calendar"></u-icon><span>{{nowdate}}</span>
				</view>
			</view>
			<view class="daycn-bp">
				<view class="daycn-bp-item">
					<view class="daycn-bp-item-title">完成桩基数</view>
					<view style=" font-size: 30rpx;color: rgba(57, 206, 136, 1);">{{ statistical.size }}</view>
				</view>
				<view class="daycn-bp-item">
					<view class="daycn-bp-item-title">累计注浆量</view>
					<view style="font-size: 30rpx;color: rgba(255, 164, 48, 1);">{{ (parseFloat(statistical.sumzjl || 0)).toFixed(2) }} 方</view>
				</view>
			</view>
			<view class="daycn-bp">
				<view class="daycn-bp-item">
					<view class="daycn-bp-item-title">总钻深度</view>
					<view style=" font-size: 30rpx;color: rgba(70, 139, 238, 1);">{{ (parseFloat(statistical.zonglong || 0)).toFixed(2) }} 米
					</view>
				</view>
				<view class="daycn-bp-item">
					<view class="daycn-bp-item-title">质量预警</view>
					<view style=" font-size: 30rpx;color: rgba(237, 87, 86, 1);">{{ statistical.chaobiaocount }}</view>
				</view>
			</view>
			<u-divider style="width: 90%; margin: 30rpx auto 0;"></u-divider>
		</view>
		<view class="daycn" style="height: 320rpx;background-image: url(../../static/pour/daycn2.png);">
			<view class="daycn-title">
				<view class="daycn-title-name">管桩统计</view>
				<view class="daycn-title-time">
					<u-icon color="#FFFFFF" size="28" name="calendar"></u-icon><span>{{nowdate}}</span>
				</view>
			</view>
			<view class="daycn-bp">
				<view class="daycn-bp-item" style="width: 180rpx;">
					<view class="daycn-bp-item-title">完成桩基数</view>
					<view style=" font-size: 30rpx;color: rgba(57, 206, 136, 1);">{{ pipeDate.size }}</view>
				</view>
				<view class="daycn-bp-item" style="width: 180rpx;">
					<view class="daycn-bp-item-title">总钻深度</view>
					<view style=" font-size: 30rpx;color: rgba(70, 139, 238, 1);">{{ (parseFloat(pipeDate.zonglong || 0)).toFixed(2) }} 米
					</view>
				</view>
				<view class="daycn-bp-item" style="width: 180rpx;">
					<view class="daycn-bp-item-title">质量预警</view>
					<view style=" font-size: 30rpx;color: rgba(237, 87, 86, 1);">{{ pipeDate.chaobiaocount }}</view>
				</view>
			</view>
		</view>
		<!-- 工单统计标签 -->
		<view class="poursta">
			<view class="poursta-title">工单统计标签</view>
			<view class="poursta-pic">
				<view class="charts-box">
					<qiun-data-charts type="ring"
						:opts="{legend:{position: 'right'},dataLabel: false,extra:{ring:{ringWidth: 25,linearType:'custom'}}}"
						:chartData="chartsDataRing1" />
				</view>
			</view>
			<u-divider style="width: 90%; margin: 30rpx auto;"></u-divider>
			<view class="poursta-pro">
				<u-line-progress height="18" :percentage="30"></u-line-progress>
			</view>

		</view>
		<!-- 单位生产统计 -->
		<view class="poursta">
			<view class="poursta-title">单位生产统计</view>
			<view class="poursta-pic">
				<view class="charts-box1">
					<qiun-data-charts type="mix"
						:opts="{yAxis:{data:[{position: 'left',title: '超标率'},{position: 'right',title: '总数',textAlign: 'left'}]},xAxis:{disableGrid: true,rotateLabel: true,fontSize: 10}}"
						:chartData="mix" />
				</view>
			</view>
		</view>
		<!-- 产能统计 -->
		<view class="poursta">
			<view class="poursta-title">产能统计
				<view class="poursta-title-item" :class="date==0?'item1':'item2'" @click="checkdate(0)">年</view>
				<view class="poursta-title-item" :class="date==1?'item1':'item2'" @click="checkdate(1)">月</view>
				<view class="poursta-title-item" :class="date==2?'item1':'item2'" @click="checkdate(2)">日</view>
			</view>
			<view class="poursta-pic">
				<view class="charts-box1">
					<qiun-data-charts type="column" :opts="opts" :chartData="chartsDataRing2" />
				</view>
			</view>
		</view>
		<!-- 底部消息中心 -->
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
					<text
						style="float: right; margin-right: 14rpx;padding:10px 0;padding:10px 0;">{{video.createTime}}</text>
				</view>
			</view>

		</view>
		<view style="height: 150rpx;"></view>
	</view>
</template>

<script>
	import nowtime from '../../common/util/nowtime.js'
	import ruanjiapi from '../../api/ruanji.js'
	import api from "@/api/api.js"
	export default {
		data() {
			return {
				messagenum: [],
				messagelist: [],
				messagevideo: [],
				listdata: [],
				menu: [],
				date: 2,
				nowdate: nowtime.date(),
				datasta: '',
				chartsDataRing1: {
					series: [{
						data: []
					}]
				},
				mix: {
					categories: [],
					series: [{
						name: "总数",
						index: 1,
						data: [],
						type: "column"
					}, {
						name: "曲线",
						data: [],
						type: "line",
						style: "curve",
						// color: "#1890ff",
						disableLegend: true
					}]
				},
				opts: {
					legend: {
						show: false
					},
					padding: [25, 5, 15, 5],
					dataLabel: false,
					extra: {
						ring: {
							ringWidth: 10,
							linearType: 'custom'
						}
					},
					xAxis: {
						rotateLabel: true,
					}
				},
				chartsDataRing2: {
					categories: [],
					series: [{
							name: "产能(m)",
							data: []
						},
						{
							name: "数量(根)",
							data: []
						}
					]
				},
				statistical: {},
				devcntoday: '',
				sbname: '',
				shebeino: '',
				status: '',
				pipeDate: {}
			}
		},
		props:['parentId','menuList'],
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
			// this.getMenuList()
			this.messagedata()
			this.staticdata()
			// this.staticdata1()
			this.getloadlist()
			this.getdwsctj()
			this.getMixcntj()
			this.getloadlist()
			this.Rjmessage()
			// this.getmixoverlist()
		},
		methods: {
			handlerMenu(){
				this.menu = this.menuList.mate.filter((e)=>{
					return e.indexpic.indexOf("http") != -1;
				});
				if(this.menu.length > 5 ){
					this.menu.length = 5;
				};
				console.log(this.menu,this.menuList.mate,'handlerMenu----------------------------------------------软基检测');
			},
			handleNav(url) {
				if (url) {
					uni.navigateTo({
						url: url
					})
				} else {
					uni.navigateTo({
						url: '/pages/Labellist/Labellist'
					})
				}
			},
			//获取菜单
			getMenuList() {
				let parentId = this.parentId
				let params = {
					// parentId,
					parentId: '1496738460326305793',
				}
				api.getMenu({
					params
				}).then(res => {
					if (res.data.success) {
						this.menu = res.data.result.lableAuth
						console.log(this.menu,res.data,"获取菜单-------------------子组件");
					}
				})
			},
			//消息中心
			messagedata() {
				this.messagenum = []
				let params = {
					shebeino: this.shebeino,
					handlestate: 0
				}
				this.$http.get('/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/cbcxlist', {
					params
				}).then(res => {
					if (res.data.success) {
						this.messagenum = res.data.result.records
					}
				})
			},
			checkWarn(msg) {
				uni.navigateTo({
					url: `/pages/ruanji/pilerecorddetail?pileno=${msg.pileNo}&shebeino=${msg.shebeino}&id=${msg.id}`,
				})
			},
			Changesbname(e) {
				this.sbname = this.listdata[e.detail.value].shebeino_dictText
				this.shebeino = this.listdata[e.detail.value].shebeino
				this.mixpiledevToday()
				this.messagedata()
			},
			mixpiledevToday() {
				let params = {
					shebeino: this.shebeino
				}
				ruanjiapi.mixpiledevToday({
					params
				}).then(res => {
					this.devcntoday = res.data.result
				})
			},
			// 选择状态
			Changestatus(e) {
				this.statusname = this.statusnames[e.detail.value]
				this.status = e.detail.value
			},
			handleNav(url) {
				if (url) {
					uni.navigateTo({
						url: url
					})
				} else {
					uni.navigateTo({
						url: '/pages/Labellist/Labellist'
					})
				}
			},
			checkdate(e) {
				this.date = e
				this.getMixcntj()
			},
			deWeightThree ( arr3 ){
				var map = new Map();
				for (let item of arr3) {
				        if (!map.has(item.shebeino)) {
				            map.set(item.shebeino, item);
				        }
				    }
				    return [...map.values()];
			},
			// 单位生产统计
			getdwsctj() {
				this.$http.get('/sys/systems/sysMessages/mixStalist').then(res => {
					// console.log(res)
					let datas = res.data.result
					this.mix.categories = []
					this.mix.series[0].data = []
					this.mix.series[1].data = []
					datas.forEach(e => {
						// console.log(e)
						this.mix.categories.push(e.departName)
						this.mix.series[0].data.push(e.zong)
						let cbl = parseFloat(((e.buhege / e.zong) * 100).toFixed(2)) || 0
						// console.log(cbl,typeof(cbl))
						this.mix.series[1].data.push(cbl)
					})
				})
			},
			// 功效统计
			getMixcntj() {
				let params = {
					date: this.date
				}
				this.$http.get('/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/stalists', {
					params
				}).then(res => {
					// console.log(res)
					let datas = res.data.result
					this.chartsDataRing2.categories = []
					this.chartsDataRing2.series[0].data = []
					this.chartsDataRing2.series[1].data = []
					datas.forEach(e => {
						// console.log(e)
						this.chartsDataRing2.categories.push(e.pileTime)
						this.chartsDataRing2.series[0].data.push(e.pileLength)
						this.chartsDataRing2.series[1].data.push(e.pileNum)
					})
				})
			},
			// 获取统计信息
			staticdata1() {
				this.$http.get('/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/list1', {
					params: {
						column: 'id',
						order: 'desc',
						pileTime: this.nowdate
					}
				}).then(res => {
					this.statisticalnowday = res.data.result
				})
			},
			// 获取统计信息
			staticdata() {
				this.$http.get('/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/list1', {
					params: {
						column: 'id',
						order: 'desc',
					}
				}).then(res => {
					this.statistical = res.data.result
				})
				this.$http.get('/devicepipepilehistorydataone/devicePipepileHistorydataOne/listshz').then(res => {
					this.pipeDate = res.data.result
				})
			},
			// 获取软基数据列表
			getloadlist() {
				ruanjiapi.workorderlist().then(res => {
					// console.log(res, "软基工单列表")
					if (res.data.success) {
						let datas = res.data.data
						this.chartsDataRing1.series[0].data = [{
								name: '未开始',
								value: datas.ready || 0
							},
							{
								name: '已完成',
								value: datas.finish || 0
							},
							{
								name: '生产中',
								value: datas.doing || 0
							},
							{
								name: '滞后',
								value: datas.timeout || 0
							},
						]
					}
				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			// 超标统计
			getmixoverlist() {
				ruanjiapi.mixpileoverlist().then(res => {
					// console.log(res)
				})
			},
			// 获取搅拌桩数据列表
			getloadlist() {
				let params = {
					pageSize: 100,
					sys_depart_orgcode: this.$store.getters.orgcode
				}
				ruanjiapi.mixpilelist({
					params
				}).then(res => {
					// console.log(res, "搅拌桩数据列表")
					this.listdata = this.deWeightThree(res.data.result.records) 
				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			// 底部消息中心
			Rjmessage() {
				let params = {
					column: 'id',
					order: 'desc',
					topictype: 2,
				}
				this.$http.get(`/officialfile/officialFile/list7`, {
					params
				}).then(res => {
					this.messagelist = []
					this.messagevideo = []
					let data = res.data.result
					// this.messagelist = res.data.result
					data.forEach(msg => {
						// console.log(msg)
						if (msg.filetype == 0) {
							this.messagelist.push(msg)
						} else if (msg.filetype == 1) {
							this.messagevideo.push(msg)
						}
					})
				})
			},
			// 消息中心
			messageclick(m) {
				// console.log(m, '信息中心')
				uni.navigateTo({
					url: '/pages/smartbh/messageDetails?item=' + JSON.stringify(m)
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	#ruanji {
		background-image: url(../../static/home/bg-header.png);
		background-repeat: no-repeat;
		background-size: 100%;

		.top {
			width: 690rpx;
			height: 334rpx;
			margin: 0 auto;
			// margin-top: 30rpx;
			border-radius: 16rpx;
			background-color: white;
			display: flex;
			flex-wrap: wrap;
			justify-content: space-around;
			align-items: center;


			&-item {
				width: 210rpx;
				height: 150rpx;
				border-radius: 16rpx;
				background-color: white;
				display: flex;
				flex-direction: column;
				justify-content: center;
				align-items: center;
				margin: 0 5px;

				&-img {
					width: 75rpx;
					height: 75rpx;
					background-repeat: no-repeat;
					background-size: 100% 100%;
					// margin-left: 30rpx;
					// margin-right: 18rpx;
					margin-bottom: 24rpx;
				}

				&-num {
					font-size: 38rpx;
					color: #333333;
					margin-bottom: 10rpx;

					span {
						color: #4C5971;
						font-size: 28rpx;
					}
				}

				&-name {
					font-size: 28rpx;
					color: #333333;
					// margin-top: 24rpx;
				}
			}
		}

		.news {
			width: 690rpx;
			margin: 0 auto;
			margin-top: 30rpx;

			&-title {
				display: flex;
				justify-content: space-between;
				align-items: center;

				&-name {
					color: #333333;
					font-size: 36rpx;
					font-weight: 600;
				}

				&-more {
					display: flex;
					align-items: center;
					font-size: 24rpx;
					color: #999999;
				}
			}

			&-con {
				margin-top: 30rpx;

				&-item {
					margin-top: 30rpx;
					display: flex;

					&-but {
						width: 130rpx;
						padding: 0 10rpx;
						height: 35rpx;
						background-color: rgba(250, 53, 52, 0.1);
						font-size: 22rpx;
						color: #fa3534;
						text-align: center;
					}

					&-des {
						word-break: break-all;
						width: 500rpx;
						margin-left: 18rpx;
						font-size: 26rpx;
						color: #333333;
					}
				}
			}

			&-activity {
				height: 230rpx;
				border-radius: 16rpx;
				margin-top: 30rpx;
				background-color: white;
				display: flex;
				align-items: center;

				&-img {
					width: 260rpx;
					height: 200rpx;
					margin-left: 16rpx;
					// background-color: #1CBBB4;
					border-radius: 16rpx;
				}

				&-con {
					height: 200rpx;
					margin-left: 20rpx;
					color: #333333;

					// background-color: red;
					&-title {
						font-size: 28rpx;
						font-weight: 600;
						text-align: left;
						width: 239px;
						height: auto;
					}

					&-text {
						margin-top: 16rpx;
						font-size: 24rpx;
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
				height: 500rpx;
				border-radius: 16rpx;
				margin-top: 30rpx;
				background-color: white;
				color: #333333;

				&-img {
					height: 280rpx;
					// background-color: #1CBBB4;
					border-radius: 16rpx;
					// border: 1px solid navajowhite;
				}

				&-title {
					margin-top: 20rpx;
					font-size: 28rpx;
					font-weight: 600;
				}

				&-text {
					margin-top: 16rpx;
					font-size: 24rpx;
				}
			}
		}

		.daycn {
			width: 690rpx;
			height: 500rpx;
			margin: 0 auto;
			margin-top: 30rpx;
			background-image: url(../../static/pour/daycn.png);
			background-repeat: no-repeat;
			background-size: 100% 100%;

			&-title {
				width: 620rpx;
				margin: 0 auto;
				padding-top: 22rpx;
				font-size: 36rpx;
				color: #FFFFFF;
				font-weight: 500;
				display: flex;
				justify-content: space-between;

				&-time {
					display: flex;
					align-items: center;

					span {
						margin-left: 18rpx;
					}
				}
			}

			&-bp {
				width: 620rpx;
				margin: 0 auto;
				margin-top: 35rpx;
				display: flex;
				justify-content: space-around;

				&-item {
					width: 250rpx;
					height: 145rpx;
					border-radius: 19rpx;
					background-color: rgba(208, 210, 218, .5);
					display: flex;
					flex-direction: column;
					justify-content: center;
					align-items: center;

					&-title {
						margin-bottom: 15rpx;
						color: #4C5971;
						font-size: 28rpx;
					}
				}
			}
		}

		.poursta {
			width: 690rpx;
			height: 520rpx;
			margin: 0 auto;
			margin-top: 30rpx;
			border-radius: 16rpx;
			background-color: #FFFFFF;

			&-title {
				padding-top: 25rpx;
				margin-left: 30rpx;
				font-size: 36rpx;
				font-weight: 600;
				color: #333333;

				&-item {
					width: 60rpx;
					margin-right: 10rpx;
					text-align: center;
					float: right;
					font-weight: normal;
					font-size: 30rpx;
				}

				.item1 {
					background-color: rgba(56, 127, 253, .5);
					border-radius: 8rpx;
				}

				.item2 {
					background-color: rgba(51, 51, 51, .5);
					border-radius: 8rpx;
				}

			}

			&-pic {
				width: 100%;
				// height: ;
				// background-color: red;

				.charts-box {
					width: 90%;
					height: 320rpx;
				}

				.charts-box1 {
					width: 100%;
					height: 420rpx;
				}
			}

			&-pro {
				width: 94%;
				margin: 0 auto;
			}
		}

		.devcntoday {
			width: 690rpx;
			margin: 0 auto;
			margin-top: 30rpx;
			border-radius: 16rpx;
			background-color: #FFFFFF;

			&-title {
				padding-top: 25rpx;
				margin-left: 30rpx;
				font-size: 36rpx;
				font-weight: 600;
				color: #333333;
			}

			&-con {
				width: 620rpx;
				margin: 0 auto;
				margin-top: 35rpx;
				display: flex;
				flex-wrap: wrap;
				justify-content: space-around;

				&-item {
					width: 280rpx;
					height: 145rpx;
					margin-bottom: 10rpx;
					border-radius: 19rpx;
					background-color: rgba(208, 210, 218, .5);
					display: flex;
					flex-direction: column;
					justify-content: center;
					align-items: center;

					&-title {
						margin-bottom: 15rpx;
						color: #4C5971;
						font-size: 28rpx;
					}
				}
			}
		}
	}
</style>