<template>
	<view id="ruanji">
		<!-- 次级页面标签 -->
		<view style="height: 130upx;" class=""></view>
		<!-- <view class="top">
			<view class="top-item" @click="handleNav(0)" v-has="'fcsb:add'">
				<view class="top-item-img" style="background-image: url(../../static/rawmaterial/six.png);"></view>
				<view class="top-item-name">发车申报</view>
			</view>
			<view class="top-item" @click="handleNav(1)">
				<view class="top-item-img" style="background-image: url(../../static/rawmaterial/two.png);"></view>
				<view class="top-item-name">运输实时追踪</view>
			</view>
			<view class="top-item" @click="handleNav(2)" v-has="'dcsh:sh'">
				<view class="top-item-img" style="background-image: url(../../static/rawmaterial/one.png);"></view>
				<view class="top-item-name">到场审核</view>
			</view>
			<view class="top-item" @click="handleNav(3)">
				<view class="top-item-img" style="background-image: url(../../static/rawmaterial/three.png);"></view>
				<view class="top-item-name">发车单查询</view>
			</view>
			<view class="top-item" @click="handleNav(4)">
				<view class="top-item-img" style="background-image: url(../../static/rawmaterial/four.png);"></view>
				<view class="top-item-name">统计分析</view>
			</view>
			<view class="top-item" @click="handleNav(5)">
				<view class="top-item-img" style="background-image: url(../../static/rawmaterial/five.png);"></view>
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
		<!-- 消息中心 -->
		<view class="news">
			<view class="news-title">
				<view class="news-title-name">消息中心</view>
				<view class="news-title-more"> <span>查看更多</span>
					<u-icon color="#999999" size="12" name="arrow-right"></u-icon>
				</view>
			</view>
			<view class="news-con" v-if="messagenum.length>0">
				<view class="news-con-item" v-for="(k,index) in messagenum" :key="index">
					<view class="news-con-item-but">通知</view>
					<!-- <view class="news-con-item-but">精选</view> -->
					<view class="news-con-item-des">{{k.content}}</view>
				</view>
			</view>
			<view class="news-con" v-else>
				<view class="news-con-item">
					<u-notice-bar bgColor="#fff" fontSize=16 color="#91CB74" text="暂无新消息提醒"></u-notice-bar>
				</view>
				<view class="tiptitle"></view>
			</view>
		</view>
		<!-- 今日产能 -->
		<view class="daycn">
			<view class="daycn-title">
				<view class="daycn-title-name">统计数据</view>

			</view>

			<view class="daycn-bp">
				<view class="daycn-bp-item">
					<view class="daycn-bp-item-title">到场总车次</view>
					<view class="daycn-bp-item-title-font">{{countlist.arrived}}
					</view>
					<p style="border-bottom:1px solid #D0D2DA ;width:20px;height: .1px;padding: 7px 0;"></p>
				</view>
				<p style="float:left;width: 1px;height: 30px; background: #D0D2DA;margin: 24px 0;"></p>
				<view class="daycn-bp-item">
					<view class="daycn-bp-item-title">发车在途 </view>
					<view class="daycn-bp-item-title-font">{{countlist.noarrive}}</view>
					<p style="border-bottom:1px solid #D0D2DA ;width:20px;height: .1px;padding: 7px 0;"></p>
				</view>
				<p style="float:left;width: 1px;height: 30px; background: #D0D2DA;margin: 24px 0;"></p>
				<view class="daycn-bp-item">
					<view class="daycn-bp-item-title">抵达中转站 </view>
					<view class="daycn-bp-item-title-font">{{countlist.zzarrived}}</view>
					<p style="border-bottom:1px solid #D0D2DA ;width:20px;height: .1px;padding: 7px 0;"></p>
				</view>
				<view class="daycn-bp-item">
					<view class="daycn-bp-item-title">待解锁 </view>
					<view class="daycn-bp-item-title-font">{{countlist.nojiesuo}}</view>
				</view>
				<p style="float:left;width: 1px;height: 30px; background: #D0D2DA;margin: 24px 0;"></p>
				<view class="daycn-bp-item">
					<view class="daycn-bp-item-title">到场待审核 </view>
					<view class="daycn-bp-item-title-font">{{countlist.nosharrived}}</view>
				</view>
				<p style="float:left;width: 1px;height: 30px; background: #D0D2DA;margin: 24px 0;"></p>
				<view class="daycn-bp-item">
					<view class="daycn-bp-item-title">车辆退场 </view>
					<view class="daycn-bp-item-title-font">{{countlist.failed}}</view>
				</view>

			</view>
			<u-divider style="width: 90%; margin: 30upx auto 0;"></u-divider>
		</view>
		<view class="poursta">
			<view class="poursta-title">单位生产统计</view>
			<view class="poursta-pic">
				<view class="charts-box1">
					<qiun-data-charts type="mix" :opts="opts" :chartData="mix" />
				</view>
			</view>
			<view class="poursta-title"></view>
		</view>
		<!-- 产能统计 -->
		<view class="pourstas">
			<view class="poursta-title">材料使用情况</view>
			<view class="borderlist">
				<uni-table stripe emptyText="暂无更多数据">
					<!-- 表头行 -->
					<uni-tr>
						<uni-th width="90" align="center">材料</uni-th>
						<uni-th align="center">供应商</uni-th>
						<uni-th width="110" align="center">运输数量(吨)</uni-th>
					</uni-tr>
					<!-- 表格数据行 -->
					<uni-tr v-for="(tablelist,index) in materilstable" :key="index">
						<uni-td align="center">{{tablelist.cailiao}}</uni-td>
						<uni-td align="center">{{tablelist.ghdw}}</uni-td>
						<uni-td align="center">{{Number(tablelist.jcsl).toFixed(2)}}</uni-td>
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
					<view class="news-activity-con-time" >
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
	import api from '../../api/rawmaterial.js';
	export default {
		data() {
			return {
				menu: [],
				messagelist: [],
				messagevideo: [],
				chartsDataRing1: {
					"series": [{
						"data": [{
							"name": "一班",
							"value": 50
						}, {
							"name": "二班",
							"value": 30
						}, {
							"name": "三班",
							"value": 20
						}, {
							"name": "四班",
							"value": 18
						}, {
							"name": "五班",
							"value": 8
						}]
					}]
				},
				lablist: [{
						name: '粉煤灰',
						number: 200,
						bai: 10,
					},
					{
						name: '粉煤灰',
						number: 200,
						bai: 10,
					},
					{
						name: '粉煤灰',
						number: 200,
						bai: 10,
					},
					{
						name: '粉煤灰',
						number: 200,
						bai: 10,
					},
					{
						name: '粉煤灰',
						number: 200,
						bai: 10,
					},
					{
						name: '粉煤灰',
						number: 200,
						bai: 10,
					},
					{
						name: '粉煤灰',
						number: 200,
						bai: 10,
					},

				],
				mix: {},
				opts: {
					legend: {
						position: 'top',
						float: 'right',
					},
					yAxis: {
						data: [{
							position: 'left',
							title: '运输量(吨)',
							titleOffsetY: -20,
							titleOffsetX: 5
						}]
					},
				},

				messagenum: [],
				countlist: {},
				materilstable: []
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
		created() {
			this.messagedata()
			this.countdata()
			this.countchart()
			this.materils()
			this.Clmessage()
		},
		methods: {
			handlerMenu(){
				this.menu = this.menuList.mate.filter((e)=>{
					return e.indexpic.indexOf("http") != -1;
					// return e.indexpic.indexOf("null") == -1 && e.indexpic.indexOf("url()") == -1;
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
			// handleNav(e) {
			// 	if (e == 0) {
			// 		uni.navigateTo({
			// 			url: '/pages/Rawmaterial/depart/departadd'
			// 		})
			// 	}
			// 	if (e == 1) {
			// 		uni.navigateTo({
			// 			url: '/pages/Rawmaterial/Realtime/realtimedata'
			// 		})
			// 	}

			// 	if (e == 2) {
			// 		uni.navigateTo({
			// 			url: '/pages/Rawmaterial/check/checklist'
			// 		})
			// 	}
			// 	if (e == 3) {
			// 		uni.navigateTo({
			// 			url: '/pages/Rawmaterial/depart/departlist'
			// 		})
			// 	}
			// 	if (e == 4) {
			// 		uni.navigateTo({
			// 			url: '/pages/Rawmaterial/statistics/statistics'
			// 		})
			// 	}
			// 	if (e == 5) {
			// 		uni.navigateTo({
			// 			url: '/pages/Labellist/Labellist'
			// 		})
			// 	}
			// },
			messagedata() {
				this.$http.get(`/wbshebeidetail/wbshebeiDetail/newlist`).then(res => {
					console.log(res.data.result, '消息中心')
					let data = res.data.result
					if (data.length > 0) {
						data.forEach(item => {
							if (item.isjiesuo == 0) {
								this.messagenum.push({
									content: item.cph + `~已发车`
								})
							} else {
								this.messagenum.push({
									content: item.cph + `~已解锁`
								})
							}
						})
						if (this.messagenum.length >=3) {
							this.messagenum.length = 3
						}
					}
				})
			},
			countdata() {
				this.$http.get(`/wbshebeidetail/wbshebeiDetail/stalist`).then(res => {
					console.log(res.data.result, '统计信息')
					this.countlist = res.data.result || {}
				})
			},
			//统计图
			countchart() {
				this.$http.get(`/wbshebeidetail/wbshebeiDetail/cailiaolist`).then(res => {
					console.log(res.data.result, '柱线统计图')
					let charts = res.data.result
					let mixData = {
						categories: [],
						series: [{
							name: "材料类型",
							data: [],
							type: "column",
						}]
					}
					charts.forEach(item => {
						mixData.categories.push(item.cailiao)
						mixData.series[0].data.push(Number(item.jcsl).toFixed(2))
					})
					this.mix = mixData
				})
			},
			//材料用量
			materils() {
				api.materlist().then(res => {
					console.log(res.data.result, 'hhhhhhh')
					this.materilstable = res.data.result
				})
			},
			// 底部消息中心
			Clmessage() {
				let params = {
					column: 'id',
					order: 'desc',
					topictype: 3,
				}
				this.$http.get(`/officialfile/officialFile/list7`, {
					params
				}).then(res => {
					this.messagelist = []
					this.messagevideo = []
					console.log(res, '材料追溯消息中心')
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
				})
			},
			// 消息中心
			messageclick(m) {
				console.log(m, '信息中心')
				uni.navigateTo({
					url: '/pages/smartbh/messageDetails?item=' + JSON.stringify(m)
				})
			}
		},
	}
</script>

<style lang="scss" scoped>
	#ruanji {
		background-image: url(../../static/home/bg-header.png);
		background-repeat: no-repeat;
		background-size: 100%;

		.top {
			width: 690upx;
			height: 334upx;
			margin: 10px auto;
			// margin-top: 30upx;
			border-radius: 16upx;
			background-color: white;
			display: flex;
			flex-wrap: wrap;

			// justify-content:left;
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
					// margin-left: 30upx;
					// margin-right: 18upx;
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

		.daycn {
			width: 690upx;
			height: 500upx;
			margin: 0 auto;
			margin-top: 30upx;
			background-image: url(../../static/rawmaterial/title.png);
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
				flex-wrap: wrap;

				&-item {
					width: 190upx;
					height: 145upx;
					border-radius: 19upx;
					// background-color: rgba(208, 210, 218, .5);
					display: flex;
					flex-direction: column;
					justify-content: center;
					align-items: center;

					&-title {
						margin-bottom: 15upx;
						color: #4C5971;
						font-size: 24upx;
						font-weight: bold;
					}

					&-font {
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
					height: 320upx;
				}

				.charts-box1 {
					width: 100%;
					height: 420upx;
				}
			}

			&-pro {
				width: 94%;
				margin: 0 auto;
			}
		}
	}

	.borderlist {
		border-radius: 10px;
		width: 94%;
		margin: 0 auto;
		// border: 1px solid deepskyblue;
	}

	.pourstas {
		width: 690upx;
		height: auto;
		margin: 0 auto;
		margin-top: 30upx;
		border-radius: 16upx;
		background-color: #FFFFFF;
	}

	.tiptitle {
		color: #91CB74;
		font-weight: bold;
		height: 20px;
		font-size: 17px;
		text-align: center;
	}
</style>
