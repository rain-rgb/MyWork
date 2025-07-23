<template>
	<view id="smartbh">
		<view style="height: 150upx;" class=""></view>
		<!-- 次级页面标签 -->
		<!-- <view class="top">
			<view class="top-item" @click="handleNav(0)">
				<view class="top-item-img" style="background-image: url(../../static/pour/tag1.png);"></view>
				<view class="top-item-name">拌合总览</view>
			</view>
			<view class="top-item" @click="handleNav(1)">
				<view class="top-item-img" style="background-image: url(../../static/pour/tag2.png);"></view>
				<view class="top-item-name">浇筑令管理</view>
			</view>
			<view class="top-item" @click="handleNav(2)">
				<view class="top-item-img" style="background-image: url(../../static/pour/tag3.png);"></view>
				<view class="top-item-name">拌合数据详情</view>
			</view>
			<view class="top-item" @click="handleNav(3)">
				<view class="top-item-img" style="background-image: url(../../static/pour/tag4.png);"></view>
				<view class="top-item-name">拌合质量分析</view>
			</view>
			<view class="top-item" @click="handleNav(4)">
				<view class="top-item-img" style="background-image: url(../../static/pour/tag5.png);"></view>
				<view class="top-item-name">运输详情</view>
			</view>
			<view class="top-item" @click="handleNav(5)">
				<view class="
				top-item-img" style="background-image: url(../../static/pour/tag6.png);"></view>
				<view class="top-item-name">配料单管理</view>
			</view>
		</view> -->
		<view class="top">
			<view class="top-item" v-for="(item,i) in menu" :key="i" @click="handleNav(item.url)">
				<view class="top-item-img" :style="item.indexpic"></view>
				<view class="top-item-name">{{ item.title }}</view>
			</view>
		</view>
		<!-- 消息中心 -->
		<view class="news">
			<view class="news-title">
				<view class="news-title-name">消息中心</view>
				<view class="news-title-more" @click="messagelook"> <span>查看更多</span>
					<u-icon color="#999999" size="12" name="arrow-right"></u-icon>
				</view>
			</view>
			<view class="news-con">
				<view class="news-con-item"  @click="checkWarn(item)" v-for="(item,index) in newslist" :key="index">
					<view class="news-con-item-but">{{ item.overLevel_dictText }}</view>
					<view class="news-con-item-des">{{ item.overReason }}{{ item.poureLocation }}</view>
				</view>
			</view>
		</view>
		<!-- 今日产能 -->
		<view class="daycn" v-if="todaycn != ''">
			<view class="daycn-title">
				<view class="daycn-title-name">今日产能</view>
				<view class="daycn-title-time">
					<u-icon color="#FFFFFF" size="28" name="calendar"></u-icon><span>{{nowdate}}</span>
				</view>
			</view>
			<view class="daycn-sc">
				<view class="daycn-sc-item">
					<view class="daycn-sc-item-img">
						<image class="" src="../../static/pour/pang-img.png" mode=""></image>
						<span>生产方量</span>
					</view>
					<view class="daycn-sc-item-num">{{ (todaycn.fangliang || 0).toFixed() }}m³</view>
				</view>
				<view class="daycn-sc-item">
					<view class="daycn-sc-item-img">
						<image class="" src="../../static/pour/pang-img.png" mode=""></image>
						<span>生产盘数</span>
					</view>
					<view class="daycn-sc-item-num">{{ todaycn.shengchanpan || 0 }}盘</view>
				</view>
			</view>
			<view class="daycn-bp">
				<view class="daycn-bp-item">
					<view class="daycn-bp-item-title">初级超标</view>
					<view class="" style="color: #387FFD; font-size: 30upx;">{{ todaycn.chu || 0 }}盘</view>
				</view>
				<view class="daycn-bp-item">
					<view class="daycn-bp-item-title">中级超标</view>
					<view class="" style="color: #F08322; font-size: 30upx;">{{ todaycn.zhong || 0 }}盘</view>
				</view>
				<view class="daycn-bp-item">
					<view class="daycn-bp-item-title">高级超标</view>
					<view class="" style="color: #CB5D60; font-size: 30upx;">{{ todaycn.gao || 0 }}盘</view>
				</view>
			</view>
			<u-divider style="width: 90%; margin: 30upx auto 0;"></u-divider>
		</view>
		<!-- 浇筑令统计 -->
		<view class="poursta">
			<view class="poursta-title">浇筑令统计 <text class="total">总数:{{pourtjtotal}}</text> </view>
			<view class="poursta-pic">
				<view class="charts-box">
					<qiun-data-charts type="pie"
						:opts="{legend:{position: 'right',lineHeight:'25'},title:{color:'#ffffff'},subtitle:{color:'#ffffff'}, dataLabel: false}"
						:chartData="chartsDataRing1" @getIndex="getpourtotal" />
				</view>
			</view>
			<u-divider style="width: 90%; margin: 30upx auto;"></u-divider>
			<view class="poursta-pro">
				<u-line-progress height="18" :percentage="pourtjjd"></u-line-progress>
			</view>
			<view style="height: 30upx;"></view>
		</view>
		<!-- 单位生产统计 -->
		<view class="poursta">
			<view class="poursta-title">单位生产统计</view>
			<view class="poursta-pic">
				<view class="charts-box">
					<qiun-data-charts type="mix" :opts="opts" :chartData="mix" />
				</view>
			</view>
		</view>
		<!-- 产能统计 -->
		<view class="poursta">
			<view class="poursta-title">产能统计</view>
			<view class="poursta-pic">
				<view class="charts-box1">
					<!-- <qiun-data-charts type="pie"
						:opts="{legend:{show:false},dataLabel: false,extra:{ring:{ringWidth: 20,linearType:'custom'}}}"
						:chartData="chartsDataRing2" /> -->
					<qiun-data-charts type="pie"
						:opts="{legend:{show:false},extra:{ring:{ringWidth: 20,linearType:'custom'}}}"
						:chartData="chartsDataRing2" />
				</view>
			</view>
		</view>
		<!-- 统计信息 -->
		<view class="top" style="margin-top: 30upx;">
			<view class="top-item">
				<view class="top-item-num">50 <span>单</span></view>
				<view class="top-item-name">发车单总数</view>
			</view>
			<view class="top-item">
				<view class="top-item-num">200 <span>盘</span></view>
				<view class="top-item-name">总盘数</view>
			</view>
			<view class="top-item">
				<view class="top-item-num">50 <span>量</span></view>
				<view class="top-item-name">总方量</view>
			</view>
			<view class="top-item">
				<view class="top-item-num">150 <span>h</span></view>
				<view class="top-item-name">平均运输时长</view>
			</view>
			<view class="top-item">
				<view class="top-item-num">20 <span>h</span></view>
				<view class="top-item-name">最大时长</view>
			</view>
			<view class="top-item">
				<view class="top-item-num">50 <span>辆</span></view>
				<view class="top-item-name">超时车辆数</view>
			</view>
		</view>
		<!-- 热门活动 -->
		<view class="news">
			<view class="news-title">
				<view class="news-title-name">热门资讯</view>
				<view class="news-title-more" @click="seemore"><span>查看更多</span>
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
					<!-- {{num}} -->
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
					<text style="float: right; margin-right: 14upx;">{{video.createTime}}</text>
				</view>
			</view>

		</view>
		<view style="height: 150upx;" class=""></view>
	</view>
</template>

<script>
	import uCharts from '@/uni_modules/qiun-data-charts/js_sdk/u-charts/config-ucharts.js';
	import nowtime from '../../common/util/nowtime.js'
	import api from "../../api/smartbh.js"
	export default {
		// mixins: [nowtime],
		props:["menuList"],
		watch: {
			menuList: {
				handler() {
					this.handlerMenu();
					// console.log(this.menuList,'menuList----------------------------------------------智慧拌合');
				},
			}
		},
		data() {
			return {
				num: 0,
				menu: [],
				messagelist: [],
				messagevideo: [],
				opts: {
					yAxis: {
						data: [{
							position: 'left',
							min: 0,
							max: 200,
							title: '柱状图'
						}]
					}
				},
				// messagelist: [],
				messagevideo: [],
				newslist: [],
				todaycn: '',
				pourtjjd: 0, //浇筑令统计进度
				pourtjtotal: 0, //浇筑令总数
				nowdate: nowtime.date(),
				chartsDataRing1: {
					series: [{
						data: []
					}]
				},
				chartsDataRing2: {
					series: [{
						data: []
					}]
				},
				mix: {
					"categories": ["2016", "2017", "2018", "2019", "2020", "2021"],
					"series": [{
						"name": "",
						"index": 1,
						"data": [50, 20, 75, 60, 34, 38],
						"type": "column"
					}]
				}
			}
		},
		mounted() {
			// this.nowdate = nowtime.date()
			this.getMixcblist()
			this.getTodayCN()
			this.getpourtj()
			this.getDWCNCount()
			this.getbhzcapacity()
			this.bhmessage()
		},
		methods: {
			handlerMenu(){
				this.menu = this.menuList.mate.filter((e)=>{
					return e.indexpic.indexOf("http") != -1;
				});
				if(this.menu.length > 6 ){
					this.menu.length = 6;
				};
				// console.log(this.menu,'handlerMenu----------------------------------------------智慧拌合');
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
			// handleNav(e) {
			// 	if (e == 0) {
			// 		uni.navigateTo({
			// 			url: '/pages/smartbh/Entitytracing'
			// 		})
			// 	}
			// 	if (e == 1) {
			// 		uni.navigateTo({
			// 			url: '/pages/smartbh/pourorder/pourManage'
			// 		})
			// 	}
			// 	if (e == 2) {
			// 		uni.navigateTo({
			// 			url: '/pages/smartbh/mix/mixdatalist'
			// 		})
			// 	}
			// 	if (e == 3) {
			// 		uni.navigateTo({
			// 			url: '/pages/smartbh/mix/mixzlfx'
			// 		})
			// 	}
			// 	if (e == 4) {
			// 		uni.navigateTo({
			// 			url: '/pages/CarDispatch/CarInfo/CarInfo'
			// 		})
			// 	}
			// 	if (e == 5) {
			// 		uni.navigateTo({
			// 			url: '/pages/smartsy/shigongphb/shigongphbManage'
			// 		})
			// 	}
			// },
			getpourtotal(e) {
				// console.log(e)
				console.log(e.opts._series_)
				this.pourtjtotal = 0
				e.opts._series_.forEach(item => {
					this.pourtjtotal += item.value
				})
			},
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
					console.log(res,'yyyyyy')
					this.newslist = res.data.result.records
				})
			},
			checkWarn(msg) {
				uni.navigateTo({
					url: '/pages/smartbh/mix/mixdatadetail?item=' + JSON.stringify(msg)
				})
			},
			// 今日产能
			getTodayCN() {
				let params = {
					statisticsTime: this.nowdate
				}
				this.$http.get('/hntbhz/bhzCementBase/listtj', {
					params
				}).then(res => {
					this.todaycn = res.data.result
				})
			},
			// 浇筑令统计
			getpourtj() {
				let params = {
					sys_depart_orgcode: this.$store.getters.orgcode
				}
				this.$http.get('/bhzrwdxx/bhzrwdxx/listtj', {
					params
				}).then(res => {
					let datas = res.data.result
					// console.log(datas, typeof(datas))

					let arr = [{
							name: '配料',
							value: datas.peiliao || 0
						},
						{
							name: '生产',
							value: datas.shenchan || 0
						},
						{
							name: '审核',
							value: datas.shenhe || 0
						},
						{
							name: '完成',
							value: datas.wancheng || 0
						},
						{
							name: '未审核',
							value: datas.weishenhe || 0
						},
						{
							name: '滞后',
							value: datas.zhihou || 0
						}
					];
					this.chartsDataRing1.series[0].data = arr
					let zongshu = 0
					arr.forEach(e => {
						zongshu += e.value
					})
					this.pourtjjd = (datas.wancheng / zongshu) * 100
					this.pourtjtotal = zongshu
					// console.log(zongshu)
					// console.log(this.pourtjjd,"1111111")
				})
			},
			//单位产能统计
			getDWCNCount() {
				var that = this;
				that.mix = {
						categories: [],
						series: [{
							name: "方量",

							data: [],
							type: "column"
						}],
					},
					api.bhzcnCount({}).then(res => {
						if (res.data.result.length > 0) {
							//console.log("resM", res)
							let data = res.data.result;
							data.forEach(function(item, index) {
								that.mix.categories.push(item.shebeiNo);
								that.mix.series[0].data.push(item.estimateNumber);

							})
							//console.log("this.chartData++", that.chartData)
						} else {
							uni.showToast({
								title: "暂无数据",
								icon: 'none'
							})
							console.log("暂无数据")
						}
					})


			},
			// 拌合站产能统计
			getServerData(e) {
				setTimeout(() => {
					//饼图格式化示例
					//使用format属性指定config-ucharts.js里事先定义好的formatter的key值，组件会自动匹配与其对应的function 
					let pieFormatDemo = JSON.parse(JSON.stringify(e))
					// console.log(pieFormatDemo)
					for (var i = 0; i < pieFormatDemo.series.length; i++) {
						pieFormatDemo.series[i].format = "pieDemo"
					}
					// console.log(pieFormatDemo)
					this.chartsDataRing2 = pieFormatDemo

				}, 1500);
			},
			getbhzcapacity() {
				api.bhzcapacity().then(res => {
					if (res.data.result.length > 0) {
						// console.log("resM", res)
						let data = res.data.result;
						let datas = []
						data.forEach(function(item, index) {
							if (item.estimateNumber > 1000) {
								let num = parseFloat(item.estimateNumber.toFixed(2))
								datas.push({
									name: item.strengthRank,
									value: num
								})
							}
							// that.mix.categories.push(item.projectName);
							// that.mix.series[0].data.push(item.estimateNumber);
						})
						this.chartsDataRing2.series[0].data = datas
						// console.log(datas)
						this.getServerData(this.chartsDataRing2)
						//console.log("this.chartData++", that.chartData)
					} else {
						uni.showToast({
							title: "暂无数据",
							icon: 'none'
						})
					}
				})
			},
			bhmessage() {
				let params = {
					column: 'id',
					order: 'desc',
					topictype: 0,
				}
				this.$http.get(`/officialfile/officialFile/list6`, {
					params
				}).then(res => {
					this.messagelist = []
					this.messagevideo = []
					console.log(res, '智慧拌合消息中心')
					let data = res.data.result.records
					// this.messagelist = res.data.result
					data.forEach(msg => {
						console.log(msg)
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
				console.log(m.filetype, '信息中心')
				// this.num = m.filetype
				// this.num ++
				uni.navigateTo({
					url: '/pages/smartbh/messageDetails?item=' + JSON.stringify(m)
				})
			},
			seemore(){
				uni.navigateTo({
					url:'/pages/smartbh/Seemorelist'
				})
			},
			messagelook(){
				uni.navigateTo({
					url:'/pages/smartbh/overHandle/overHandle'
					// url:'/pages/smartbh/messagelook'
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
			margin: 0 auto;
			// margin-top: 30upx;
			border-radius: 16upx;
			background-color: white;
			display: flex;
			flex-wrap: wrap;
			// justify-content: space-between;
			align-items: center;


			&-item {
				width: 210upx;
				height: 150upx;
				border-radius: 16upx;
				background-color: white;
				display: flex;
				flex-direction: column;
				justify-content: center;
				align-items: center;
				margin: 0 5px;

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
				width: 700upx;
				border-radius: 16upx;
				margin-top: 30upx;
				background-color: white;
				display: flex;
				align-items: center;
				justify-content: space-around;

				&-img {
					width: 180upx;
					height: 200upx;
					// margin-left: 16upx;
					// background-color: #1CBBB4;
					border-radius: 16upx;
				}

				&-con {
					height: 200upx;
					// margin-left: 20upx;
					color: #333333;

					// background-color: red;
					&-title {
						font-size: 28upx;
						font-weight: 600;
						text-align: left;
						width: 239px;
						height: 70px;
					}

					&-time {
						width: 500upx;
						// border: 1px solid darkgoldenrod;
						display: flex;
						flex-direction: row;
						justify-content: start;
						align-items: center;

						.img {
							width: 100px;
							height: 30px;
							line-height: 30px;
							padding: 0 10px;
							// border: 1px solid forestgreen;
						}

						.wrap-font {
							width: 230px;
							height: 30px;
							line-height: 30px;
							padding: 0 30px;
							// border: 1px solid deeppink;
						}
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

		.daycn {
			width: 690upx;
			height: 500upx;
			margin: 0 auto;
			margin-top: 30upx;
			background-image: url(../../static/pour/daycn.png);
			background-repeat: no-repeat;
			background-size: 100% 100%;

			&-title {
				width: 620upx;
				margin: 0 auto;
				padding-top: 22upx;
				font-size: 36upx;
				color: #FFFFFF;
				font-weight: 500;
				display: flex;
				justify-content: space-between;

				&-time {
					display: flex;
					align-items: center;

					span {
						margin-left: 18upx;
					}
				}
			}

			&-sc {
				width: 620upx;
				margin: 0 auto;
				margin-top: 70upx;
				display: flex;
				justify-content: space-between;

				&-item {
					width: 290upx;
					height: 70upx;
					border-radius: 19upx;
					border: 1upx solid #D0D2DA;
					display: flex;
					justify-content: space-between;
					align-items: center;

					&-img {
						margin-left: 20upx;
						font-size: 24upx;

						image {
							width: 26upx;
							height: 26upx;
							margin-right: 16upx;
						}

						span {
							color: #4C5971;
						}
					}

					&-num {
						margin-right: 20upx;
					}
				}
			}

			&-bp {
				width: 620upx;
				margin: 0 auto;
				margin-top: 35upx;
				display: flex;
				justify-content: space-between;

				&-item {
					width: 190upx;
					height: 145upx;
					border-radius: 19upx;
					background-color: rgba(208, 210, 218, .5);
					display: flex;
					flex-direction: column;
					justify-content: center;
					align-items: center;

					&-title {
						margin-bottom: 15upx;
						color: #4C5971;
						font-size: 24upx;
					}
				}
			}
		}

		.poursta {
			width: 690upx;
			// height: 520upx;
			margin: 0 auto;
			margin-top: 30upx;
			border-radius: 16upx;
			background-color: #FFFFFF;

			&-title {
				padding-top: 25upx;
				margin-left: 30upx;
				font-size: 36upx;
				font-weight: 600;
				color: #333333;

			}

			&-pic {
				width: 100%;
				// height: ;
				// background-color: red;

				.charts-box {
					width: 100%;
					height: 400upx;
				}

				.charts-box1 {
					width: 100%;
					height: 900upx;
				}
			}

			&-pro {
				width: 94%;
				margin: 0 auto;
			}
		}
	}
</style>
