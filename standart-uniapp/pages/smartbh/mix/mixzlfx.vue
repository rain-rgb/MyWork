<template>
	<view id="mixzlfx">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">拌合质量分析</block>
		</cu-custom>
		<view class="top">
			<view class="top-item">
				<image class="top-item-imgclass" src="../../../static/pour/week.png" mode="" @click="checkW"></image>
			</view>
			<view class="top-item">
				<image class="top-item-imgclass" src="../../../static/pour/mon.png" mode="" @click="checkM"></image>
			</view>
			<view class="top-item">
				<image class="top-item-imgclass" src="../../../static/pour/sea.png" mode="" @click="checkQ"></image>
			</view>
			<view class="top-item">
				<image class="top-item-imgclass" src="../../../static/pour/year.png" mode="" @click="checkY"></image>
			</view>
		</view>
		<!-- 拌合站超标率 -->
		<view class="poursta">
			<view class="poursta-title"> <text class="biaoqian">1</text> 拌合站超标率</view>
			<view class="poursta-pic">
				<view class="charts-box1">
					<qiun-data-charts type="column" :opts="opts" :chartData="mix" :ontouch="true"/>
				</view>
			</view>
		</view>
		<!-- 混泥土标准差图 -->
		<view class="poursta">
			<view class="poursta-title"> <text class="biaoqian">1</text> 混泥土标准差图</view>
			<view class="poursta-pic">
				<view class="charts-box1">
					<qiun-data-charts type="demotype" :opts="optss" :chartData="Line" />
				</view>
			</view>
		</view>
		<!-- 硂材料超标统计 -->
		<view class="poursta">
			<view class="poursta-title"> <text class="biaoqian">1</text> 硂材料超标统计</view>
			<view class="">
			<uni-table stripe emptyText="暂无更多数据">
				<uni-tr>
					<uni-th width="60" align="center">材料名</uni-th>
					<uni-th width="60" align="center">初级超标</uni-th>
					<uni-th width="60" align="center">中级超标</uni-th>
					<uni-th width="60" align="center">高级超标</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in materialsList" :key="index">
					<uni-td width="73" align="center">{{item.cailiaoming}}</uni-td>
					<uni-td width="60" align="center">{{item.chu}}</uni-td>
					<uni-td width="60" align="center">{{item.zhong}}</uni-td>
					<uni-td width="60" align="center">{{item.gao}}</uni-td>
				</uni-tr>
			</uni-table>
			</view>
		</view>
		<!-- 原材料超标率-->
		<view class="poursta">
			<view class="poursta-title"> <text class="biaoqian">1</text> 原材料超标率</view>
			<view class="poursta-pic">
				<view class="charts-box1">
					<qiun-data-charts type="pie" :opts="optsring" :chartData="PieA" />
				</view>
			</view>
		</view>
		<view style="height: 100upx;" class=""></view>
	</view>
</template>

<script>
	import api from "../../../api/smartbh.js"
	import timeSelector from "../../../components/wing-time-selector/wing-time-selector.vue"
	export default {
		data() {
			return {
				check:0,
				opts: {
					yAxis: {
						data: [{
							position: 'left',
							title: '柱状图'
						}]
					},
					enableScroll: true,
					xAxis:{itemCount:5,scrollShow: false,disableGrid: true,rotateLabel: true,fontSize: 10}
				},
				optss: {
					xAxis: {
						boundaryGap: 'justify'
					}
				},
				optsring: {
					legend: {
						position: 'bottom'
					},
					extra: {
						ring: {
							ringWidth: 20,
							linearType: 'custom'
						}
					}
				},
				materialsList: [],
				
				mix: {
					categories: [],
					series: [{
						name: "柱2",
						data: [],
						type: "column"
					}],
				},
				"Line": {
					"categories": ["2016", "2017", "2018", "2019", "2020", "2021"],
					"series": [{
						"name": "C30",
						"data": [35, 8, 25, 37, 4, 20]
					}, {
						"name": "C40",
						"data": [70, 40, 65, 100, 44, 68]
					}, {
						"name": "C50",
						"data": [100, 80, 95, 150, 112, 132]
					}]
				},
				PieA: {
				
					"series": [{
						"data": [
				      {
				      	"name": "",
				      	"value": []
				      }, {
				      	"name": "",
				      	"value": []
				      }, {
				      	"name": "",
				      	"value": []
				      }, {
				      	"name": "",
				      	"value": []
				      }, {
				      	"name": "",
				      	"value": []
				      }, {
				      	"name": "",
				      	"value": []
				      }
				    ]
					}]
				},
			}
		},
		created() {
			
		},
		onLoad() {
			this.check=1
			this.getstatisticsData();
			this.getcostM();
			this.getYuanCai();
		},
		methods: {
			checkW(){
				this.check=1
				console.log("checkW",this.check)
				this.getcostM();
				this.getstatisticsData();
				this.getYuanCai();
			},
			checkM(){
				this.check=2
				console.log("checkM",this.check)
				this.getcostM();
				this.getstatisticsData();
				this.getYuanCai();
			},
			checkQ(){
				this.check=3
				console.log("checkQ",this.check)
				this.getcostM();
				this.getstatisticsData();
				this.getYuanCai();
			},
			checkY(){
				this.check=4
				console.log("checkY",this.check)
				this.getcostM();
				this.getstatisticsData();
				this.getYuanCai();
			},
			getstatisticsData(){
				var that=this;
				api.materials({
					params: {
						result: this.check
					},
				}).then(res => {
					
					this.materialsList = res.data.result
					// if(res.data.result.length>0){
					// 	let data = res.data.result;
					// 	data.forEach(function(item,index){
					// 		that.choabiaoSum+=(item.chu+item.zhong+item.gao)
					// 	})
						
					// console.log("sssss123",this.choabiaoSum)	
						
					// } else {
					// 	uni.showToast({
					// 		title: "暂无数据",
					// 		icon: 'none'
					// 	})
					// 	console.log("暂无数据")
					// }
				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			getYuanCai(){
				var that = this;
				var choabiaoSum=0;
				that.PieA={
				
					series: [{
						data: [
				    ]
					}]
				},
				api.materials({
					params: {
						result: this.check
					},
				}).then(res => {
					console.log(res)
					
					if(res.data.result.length>0){
						let data = res.data.result;
						data.forEach(function(item,index){
							choabiaoSum+=(item.chu+item.zhong+item.gao)
						})
						
					console.log("sssss123",choabiaoSum)	
						
					}
					if(res.data.result.length>0){
						let data = res.data.result;
						data.forEach(function(item,index){
							if(choabiaoSum!=0){
								console.log("sssss456",choabiaoSum)	
								that.PieA.series[0].data.push({"name":item.cailiaoming,"value":(item.chu+item.zhong+item.gao)/choabiaoSum})
							}else{
								that.PieA.series[0].data.push({"name":item.cailiaoming,"value":0})
							}
						})	
						}else{
						uni.showToast({
							title: "暂无数据",
							icon: 'none'
						})
						}
						console.log("暂无数据")
					}).catch(e => {
					console.log("请求错误", e)
				})
			},
			getcostM() {
							var that = this;
							that.mix = {
									categories: [],
									series: [{
										name: "超标率%",
										data: [],
										type: "column"
									}],
								},
								api.bhzcbv({
									params: {
										result: this.check
									},
								}).then(res => {
									// console.log("resM", res)
									if (res.data.result.length > 0) {
										//console.log("resM", res)
										let data = res.data.result;
										data.forEach(function(item, index) {
											that.mix.categories.push(item.sbname);
											that.mix.series[0].data.push(item.chaobiaolv);
											
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
		}
	}
</script>

<style lang="scss" scoped>
	#mixzlfx {
		background-color: #F3F5FE;

		.top {
			width: 690upx;
			margin: 0 auto;
			margin-top: 30upx;
			display: flex;
			justify-content: space-between;
			align-items: center;

			&-item {
				width: 156upx;
				height: 180upx;

				&-imgclass {
					width: 100%;
					height: 100%;
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
					height: 560upx;
				}
			}

			&-pro {
				width: 94%;
				margin: 0 auto;
			}
		}

		.biaoqian {
			width: 12upx;
			height: 34upx;
			border-radius: 6upx;
			margin-right: 30upx;
			color: #4A7FFF;
			background-color: #4A7FFF;
		}
	}
</style>
