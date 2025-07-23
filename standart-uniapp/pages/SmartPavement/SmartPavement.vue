<template>
	<view id="smartbh">
		<view style="height: 150upx;" class=""></view>
		<!-- 次级页面标签 -->
		<!-- <view class="top">
			<view class="top-item" @click="handleNav(0)">
				<view class="top-item-img" style="background-image: url(../../static/pour/tag2.png);"></view>
				<view class="top-item-name">水稳施工</view>
			</view>
			<view class="top-item" @click="handleNav(1)">
				<view class="top-item-img" style="background-image: url(../../static/pour/tag2.png);"></view>
				<view class="top-item-name">水稳原材</view>
			</view>
			<view class="top-item" @click="handleNav(2)">
				<view class="top-item-img" style="background-image: url(../../static/pour/tag3.png);"></view>
				<view class="top-item-name">水稳拌和</view>
			</view>
			<view class="top-item" @click="handleNav(3)">
				<view class="top-item-img" style="background-image: url(../../static/pour/tag4.png);"></view>
				<view class="top-item-name">水稳检测</view>
			</view>
			<view class="top-item" @click="handleNav(4)">
				<view class="top-item-img" style="background-image: url(../../static/pour/tag5.png);"></view>
				<view class="top-item-name">水稳运输</view>
			</view>
			<view class="top-item" @click="handleNav(5)">
				<view class="top-item-img" style="background-image: url(../../static/pour/tag6.png);"></view>
				<view class="top-item-name">全部</view>
			</view>
		</view> -->
		<view class="tabcard">
			<!-- <view class="tabcard-left" @click="clickJump(0)">
				<view class="tabcard-left-img" style="background-image: url(../../static/label/shuiwen.png);"></view>
				<view class="tabcard-left-title">
					水稳管控
				</view>
			</view> -->
			 <!-- <view class="tabcard-left" @click="clickJump(1)"> -->
				<!-- <img src="../../../static/label/liqing.png" style="padding: 0 15px;" alt=""> -->
				<!-- <view class="tabcard-left-img" style="background-image: url(../../static/label/liqing.png);"></view>
				<view class="tabcard-left-title">
					沥青管控
				</view> -->
			<!-- </view> --> 
			<!-- <view class="tabcard-left" @click="clickJump(2)"> -->
				<!-- <img src="../../../static/label/liqing.png" style="padding: 0 15px;" alt=""> -->
				<!-- <view class="tabcard-left-img" style="background-image: url(../../static/label/lm.png);"></view>
				<view class="tabcard-left-title">
					摊铺碾压
				</view> -->
			<!-- </view> -->
			<view class="tabcard-left" v-for="(item,i) in menu" :key="i" @click="clickJump(item.url)">
				<view class="tabcard-left-img" :style="item.indexpic"></view>
				<view class="tabcard-left-title">{{ item.title }}</view>
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
					<view class="news-con-item-but">精选</view>
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
		<view class="poursta">
			<view class="poursta-title">路面原材料检验批</view>
			<view class="borderlist">
				<uni-table stripe emptyText="暂无更多数据">
					<!-- 表头行 -->
					<uni-tr>
						<uni-th width="90" align="center">材料</uni-th>
						<uni-th align="center">供应商</uni-th>
						<uni-th width="110" align="center">运输数量(吨)</uni-th>
					</uni-tr>
					<!-- 表格数据行 -->
					<uni-tr>
						<uni-td align="center">{{4}}</uni-td>
						<uni-td align="center">{{6}}</uni-td>
						<uni-td align="center">{{2}}</uni-td>
					</uni-tr>


				</uni-table>
			</view>
		</view>
		<!-- 产能 -->
		<view class="poursta">
			<view class="poursta-title">产能</view>
			<view class="poursta-pic">
				<view style="display: flex;">
					<!-- <view class="charts-box1" style="width: 50%;">
						<qiun-data-charts type="ring"
							:opts="{legend:{position: 'bottom'},dataLabel: false,extra:{ring:{ringWidth: 15,linearType:'custom'}}}"
							:chartData="chartsDataRing1" />
					</view> -->
					<view class="charts-box1" style="width: 50%;">
						<qiun-data-charts type="ring"
							:opts="{legend:{position: 'bottom',float:'bottom'},dataLabel: false,extra:{ring:{ringWidth: 15,linearType:'custom'}}}"
							:chartData="chartsDataRing2" />
					</view>
				</view>
			</view>
		</view>
		<!-- 现场施工 -->
		<view class="Nowwork">
			<view class="Nowwork-title">现场施工</view>
			<view class="Nowwork-article">
				<view class="Nowwork-article-font">
					温度预警数:0
				</view>
				<view class="Nowwork-article-font">
					速度预警数:9
				</view>
			</view>
			<!-- <view class="progresst">
				<view class="progresst-protitle">水稳摊铺碾压:</view>
				<view class="progresst-prgressline">
					<u-line-progress :percentage="30" height=20 activeColor="#55aaff"></u-line-progress>
				</view>
			</view> -->
			<view class="progresst">
				<view class="progresst-protitle">沥青摊铺碾压:</view>
				<view class="progresst-prgressline">
					<u-line-progress :percentage="30" height=20 activeColor="#55aaff"></u-line-progress>
				</view>
			</view>
		</view>
		<!-- 路面检测试验指标 -->
		<view class="poursta">
			<view class="poursta-title">路面检测试验指标</view>
			<view class="borderlist">
				<uni-table stripe emptyText="暂无更多数据">
					<!-- 表头行 -->
					<uni-tr>
						<uni-th align="center">试验名称</uni-th>
						<uni-th align="center">试验次数</uni-th>
						<uni-th align="center">合格数</uni-th>
						<uni-th align="center">不合格数</uni-th>
						<uni-th align="center">合格率</uni-th>
					</uni-tr>
					<!-- 表格数据行 -->
					<uni-tr>
						<uni-td align="center">{{4}}</uni-td>
						<uni-td align="center">{{5}}</uni-td>
						<uni-td align="center">{{3}}</uni-td>
						<uni-td align="center">{{21}}</uni-td>
					</uni-tr>


				</uni-table>
			</view>
		</view>
		<!-- 热门活动 -->
		<view class="news">
			<view class="news-title">
				<view class="news-title-name">消息中心</view>
				<view class="news-title-more"> <span>查看更多</span>
					<u-icon color="#999999" size="12" name="arrow-right"></u-icon>
				</view>
			</view>
			<view class="news-activity">
				<view class="news-activity-img"></view>
				<view class="news-activity-con">
					<view class="news-activity-con-title">多层面多维度谋划未来产展</view>
					<view class="news-activity-con-text">未来产业是以新一代信息技术、新材料、新能源、新装备等与现...</view>
					<view class="news-activity-con-time">
						<text>200</text>
						<text style="float: right; margin-right: 14upx;">2020-10-20</text>
					</view>
				</view>

			</view>
			<view class="news-activity">
				<view class="news-activity-img"></view>
				<view class="news-activity-con">
					<view class="news-activity-con-title">多层面多维度谋划未来产展</view>
					<view class="news-activity-con-text">未来产业是以新一代信息技术、新材料、新能源、新装备等与现...</view>
					<view class="news-activity-con-time">
						<text>200</text>
						<text style="float: right; margin-right: 14upx;">2020-10-20</text>
					</view>
				</view>

			</view>

			<view class="news-video">
				<view class="news-video-img"></view>
				<view class="news-video-title">多层面多维度谋划未来产展</view>
				<view class="news-video-time">
					<text>200</text>
					<text style="float: right; margin-right: 14upx;">2020-10-20</text>
				</view>
			</view>

		</view>
		<!--  -->
		<view style="height: 150upx;" class=""></view>
	</view>
</template>

<script>
	import uCharts from '@/uni_modules/qiun-data-charts/js_sdk/u-charts/config-ucharts.js';
	import nowtime from '../../common/util/nowtime.js'
	import api from "../../api/smartbh.js"
	export default {
		// mixins: [nowtime],
		data() {
			return {
				menu:[],
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
				todaycn: '',
				messagenum: [],
				pourtjjd: 0, //浇筑令统计进度
				pourtjtotal: 0, //浇筑令总数
				nowdate: nowtime.date(),
				chartsDataRing1: {
					"series": [{
						"data": [{
							"name": "水稳总产能",
							"value": 0
						}, {
							"name": "水稳超标量",
							"value": 0
						}]
					}]
				},
				chartsDataRing2: {
					"series": [{
						"data": [{
							"name": "沥青总产能",
							"value": 0
						}, {
							"name": "沥青超标量",
							"value": 0
						}]
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
				},
				nameid: '',
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
			// this.nowdate = nowtime.date()
			this.getDWCNCount()
			console.log(Math.random())
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
			liangdata() {
				this.$http.get(`/zhiliangrenwudan/zhiliangrenwudan/list`, {
					params: {
						code: this.nameid
					}
				}).then(res => {
					console.log(res, '梁信息')
				})
			},
			handleNav(e) {
				if (e == 5) {
					uni.navigateTo({
						url: '/pages/Labellist/Labellist'
					})
				}
			},
			getpourtotal(e) {
				// console.log(e)
				console.log(e.opts._series_)
				this.pourtjtotal = 0
				e.opts._series_.forEach(item => {
					this.pourtjtotal += item.value
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
			clickJump(e) {
				// if (e == 0) {
				// 	uni.navigateTo({
				// 		url: '/pages/SmartPavement/waterControl/waterControl'
				// 	})
				// }
				// if (e == 1) {
				// 	uni.navigateTo({
				// 		url: '/pages/SmartPavement/asphalt/asphalt'
				// 	})
				// }
				// if (e == 2) {
				// 	uni.navigateTo({
				// 		url: '/pages/SmartPavement/Paving/Paving'
				// 	})
				// }
				if (e) {
					uni.navigateTo({
						url: e
					})
				}
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
			justify-content: space-between;
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

		.tabcard {
			width: 690upx;
			background-color: #fff;
			border-radius: 10px;
			height: 70px;
			display: flex;
			margin: 15px auto;
			justify-content: space-around;
			align-items: center;

			&-left {
				width: 150upx;
				height: auto;

				// background-image: url(../../static/label/shuiwen.png);
				&-img {
					width: 70upx;
					height: 80upx;
					margin: 0 auto;
					background-repeat: no-repeat;
				}

				&-title {
					// width: 135px;
					// height: 35px;
					font-size: 13px;
					font-family: PingFang SC;
					font-weight: bold;
					color: #333333;
					text-align: center;
				}

				// border: 1px solid #ff0000;
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
					background-color: #1CBBB4;
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
					}

					&-text {
						margin-top: 16upx;
						font-size: 24upx;
					}
				}
			}

			&-video {
				height: 405upx;
				border-radius: 16upx;
				margin-top: 30upx;
				background-color: white;
				color: #333333;

				&-img {
					height: 280upx;
					background-color: #1CBBB4;
					border-radius: 16upx;
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
					height: 420upx;
				}
			}

			&-pro {
				width: 94%;
				margin: 0 auto;
			}

			&-titles {
				padding-top: 25upx;
				margin-left: 30upx;
				font-size: 30upx;
				font-weight: 400;
			}
		}

		.Nowwork {
			width: 690upx;
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

			&-article {
				width: 660upx;
				// border: 1px solid forestgreen;
				display: flex;
				margin: 10px auto;
				justify-content: space-between;

				&-font {
					color: #000000;
					font-size: 17px;
					width: 230upx;
					text-align: center;
					font-weight: bold;
					background-color: #eef7f9;
					border-radius: 5px;
					border-bottom-style: initial;
				}

			}

			.progresst {
				width: 100%;
				height: auto;
				// border: 1px solid chocolate;
				display: flex;
				padding: 10px 0;

				&-protitle {
					width: 120px;
					height: 30px;
					color: #000;
					font-size: 28upx;
					font-weight: bold;
					padding: 0 12px;
				}

				&-prgressline {
					width: 220px;
					height: 30px;
					margin: 1px 0;
				}
			}

		}
	}
</style>
