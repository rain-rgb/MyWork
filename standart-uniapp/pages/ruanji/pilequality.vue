<template>
	<view id="pilequality">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">桩质量分析</block>
		</cu-custom>
		<!-- 软基设备超标排名 -->
		<view class="block">
			<view class="block-title"><text class="biaoqian">1</text>软基设备超标排名</view>
			<view class="block-pic">
				<view class="block-pic-item" v-for="(item,index) in devicover" :key="index" :style="item.color">
					<view class="block-pic-item-name">{{item.sbname}}</view>
					<view class="block-pic-item-cbl">超标率：{{item.overlv}}</view>
				</view>
			</view>
		</view>
		<!-- 工单超标排名 -->
		<view class="block">
			<view class="block-title"><text class="biaoqian">1</text>工单超标排名</view>
			<view class="block-pic">
				<view class="block-pic-item" v-for="(item,index) in workorder" :key="index" :style="item.color" @click="workclick(item)">
					<view class="block-pic-item-name">里程：{{item.mileage}} <text style="float: right;">{{item.orgcode_dictText}}</text></view>
					<view class="block-pic-item-cbl">超标率：{{item.chaobiaolv}}</view>
					<view class="block-pic-item-cbl">完成数：{{item.piletotal}}</view>
					<view class="block-pic-item-cbl">设计数：{{item.descount}}</view>
				</view>
			</view>
		</view>
		<!-- 单位生产统计 -->
		<view class="block">
			<view class="block-title"><text class="biaoqian">1</text>单位生产统计</view>
			<view class="block-pic">
				<view class="charts-box1">
					<qiun-data-charts type="mix"
						:opts="{yAxis:{data:[{position: 'left',title: '折线'},{position: 'right',title: '柱状图',textAlign: 'left'}]},xAxis:{disableGrid: true,rotateLabel: true,fontSize: 10}}"
						:chartData="mix" />
				</view>
			</view>
		</view>
		
		
		<!-- 产能统计 -->
		<view class="block">
			<view class="block-title">
				<text class="biaoqian">1</text>产能统计
				<view class="block-title-item" :class="date==0?'item1':'item2'" @click="checkdate(0)">年</view>
				<view class="block-title-item" :class="date==1?'item1':'item2'" @click="checkdate(1)">月</view>
				<view class="block-title-item" :class="date==2?'item1':'item2'" @click="checkdate(2)">日</view>
			</view>
			<!-- <view class="block-title"><text class="biaoqian">1</text>产能统计</view> -->
			<view class="block-pic">
				<view class="charts-box1">
					<qiun-data-charts type="column" :opts="opts" :chartData="chartsDataRing1" />
				</view>
			</view>
		</view>
		<view style="height: 60upx;"></view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				date:2,
				workorder:[],
				devicover:[],
				bgcolor:[
					{
						color:'background-color: rgba(255,0,0,.3);'
					},
					// {
					// 	color:'background-color: rgba(255,85,0,.3);'
					// },
					{
						color:'background-color: rgba(255,255,0,.3);'
					},
					{
						color:'background-color: rgba(85, 170, 255, 0.3);'
					},
				],
				mix: {
					categories: [],
					series: [{
						name: "总数",
						index: 1,
						data: [],
						type: "column"
					}, {
						name: "超标率",
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
					padding: [25,5,15,5],
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
				chartsDataRing1: {
					categories: [],
					series: [{
						name: "产能(m)",
						data: []
					},
					{
						name: "数量(根)",
						data: []
					}]
				},
			};
		},
		onLoad() {
			// this.getMixcntj()
			this.getdwsctj()
			this.getDeviceOver()
			this.getGongtj()
			this.getloadlist()
		},
		methods: {
			workclick(e) {
				uni.navigateTo({
					url: '/pages/ruanji/workorderdetail?rjrwd=' + e.rjrwd
				})
			},
			checkdate(e) {
				this.date = e
				this.getGongtj()
			},
			// 获取软基数据列表
			getloadlist() {
				let params = {
					column: 'chaobiaolv',
					order: 'desc',
				}
				this.$http.get('/devicemixpilerwdsta/deviceMixpileRwdsta/list',{params}).then(res => {
					// console.log(res, "软基工单列表")
					let datas = res.data.result.records.splice(0,3)
					this.workorder = datas.map((item, index) => {
					    return {...item, ...this.bgcolor[index]}
					})
				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			// 功效统计
			getGongtj() {
				let params ={
					date: this.date
				}
				this.$http.get('/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/stalists',{params}).then(res => {
					// console.log(res)
					let datas = res.data.result
					this.chartsDataRing1.categories=[]
					this.chartsDataRing1.series[0].data=[]
					this.chartsDataRing1.series[1].data=[]
					datas.forEach(e => {
						// console.log(e)
						this.chartsDataRing1.categories.push(e.pileTime)
						this.chartsDataRing1.series[0].data.push(e.pileLength)
						this.chartsDataRing1.series[1].data.push(e.pileNum)
					})
					// let datas = res.data.result
					// this.mix.categories = []
					// this.mix.series[0].data = []
					// this.mix.series[1].data = []
					// datas.forEach(e => {
					// 	console.log(e)
					// 	this.mix.categories.push(e.departName)
					// 	this.mix.series[0].data.push(e.zong)
					// 	let cbl = parseFloat(((e.buhege/e.zong)*100).toFixed(2)) ||0
					// 	console.log(cbl,typeof(cbl))
					// 	this.mix.series[1].data.push(cbl)
					// })
				})
			},
			// 设备超标
			getDeviceOver() {
				this.$http.get('/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/stalvlists').then(res => {
					// console.log(res)
					let datas = res.data.result.splice(0,3)
					this.devicover = datas.map((item, index) => {
					    return {...item, ...this.bgcolor[index]}
					})
					// console.log(this.devicover)
					// console.log(datas)
					
					// let datas = res.data.result
					// this.mix.categories = []
					// this.mix.series[0].data = []
					// this.mix.series[1].data = []
					// datas.forEach(e => {
					// 	console.log(e)
					// 	this.mix.categories.push(e.departName)
					// 	this.mix.series[0].data.push(e.zong)
					// 	let cbl = parseFloat(((e.buhege/e.zong)*100).toFixed(2)) ||0
					// 	console.log(cbl,typeof(cbl))
					// 	this.mix.series[1].data.push(cbl)
					// })
				})
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
						let cbl = parseFloat(((e.buhege/e.zong)*100).toFixed(2)) ||0
						// console.log(cbl,typeof(cbl))
						this.mix.series[1].data.push(cbl)
					})
				})
			},
			getMixcntj() {
				let params = {
					type: 1
				}
				this.$http.get('/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/outputSta', {
					params
				}).then(res => {
					console.log(res)
					let datas = res.data.result
					datas.forEach(e => {
						// console.log(e)
						this.chartsDataRing1.categories.push(e.daydate)
						this.chartsDataRing1.series[0].data.push(e.deep)
					})
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	#pilequality {
		width: 100%;
		// height: 100vh;
		background-color: #F3F5FE;

		.block {
			width: 690upx;
			height: 520upx;
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
				
				.item1{
					background-color: rgba(56, 127, 253, .5);
					border-radius: 8upx;
				}
				.item2{
					background-color: rgba(51, 51, 51, .5);
					border-radius: 8upx;
				}
				
				&-item {
					width: 60upx;
					margin-right: 10upx;
					text-align: center;
					float: right;
					font-weight: normal;
					font-size: 30upx;
				}
			}

			&-pic {
				width: 100%;
				// height: ;
				// background-color: red;
				&-item{
					width: 94%;
					height: 120upx;
					margin: 20upx auto;
					border-radius: 16upx;
					&-name{
						padding: 10upx;
					}
					&-cbl{
						padding: 10upx;
						float: right;
					}
				}
				.charts-box {
					width: 90%;
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
