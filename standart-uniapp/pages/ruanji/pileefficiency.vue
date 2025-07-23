<template>
	<view id="pileefficiency">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">功效分析</block>
		</cu-custom>
		<!-- 每周明星钻机榜 -->
		<view class="block" v-if="qualifieddata.length!=0">
			<view class="block-title"><text class="biaoqian">1</text>每周明星钻机榜</view>
			<view class="block-pic">
				<view class="block-pic-item" v-for="(item,index) in 3" :key="index" :style="bgcolor[index].color">
					<view class="block-pic-item-name">{{qualifieddata[index].shebeino_dictText}}</view>
					<view class="block-pic-item-cbl">合格率：{{qualifieddata[index].allash}}%</view>
				</view>
			</view>
			<view style="height: 10upx;"></view>
		</view>
		<!-- 每周工程量明星 -->
		<view class="block" v-if="startsdata.length!=0">
			<view class="block-title"><text class="biaoqian">1</text>每周工程量明星</view>
			<view class="block-pic">
				<view class="block-pic-item" v-for="(item,index) in 3" :key="index" :style="bgcolor[index].color">
					<view class="block-pic-item-name">{{startsdata[index].shebeino_dictText}}</view>
					<view class="block-pic-item-cbl">桩数量：{{(parseFloat(startsdata[index].pilecount)).toFixed(0)}}
						&nbsp;&nbsp;总米数：{{ (parseFloat(startsdata[index].worklength)).toFixed(2)}} </view>
				</view>
			</view>
			<view style="height: 10upx;"></view>
		</view>
		<!-- 单机功效分析 -->
		<view class="block">
			<view class="block-title1">
				<picker @change="Changesbname" :range="listdata" :range-key="'shebeino_dictText'">
					<view>
						<text class="biaoqian">1</text>
						<text class="onclick">单机功效分析</text>
					</view>
				</picker>
				<view @click="dateshow = true" style="display: flex; align-items: center;">
					<u-icon color="#FFFFFF" size="28" name="calendar"></u-icon><span>{{nowdate}}</span>
				</view>
			</view>

			<view class="block-devicetitle">{{sbname}}</view>
			<view class="block-main">
				<view class="block-main-item">
					<text>总根数</text>
					<text>{{ devcntoday.finishcount || 0 }}</text>
				</view>
				<view class="block-main-item">
					<text>总浆量</text>
					<text>{{ (devcntoday.downcement || 0 + devcntoday.upcement || 0).toFixed(2) }}L</text>
				</view>
				<view class="block-main-item">
					<text>超标数</text>
					<text>{{ devcntoday.chaobiao || 0 }}</text>
				</view>
				<view class="block-main-item">
					<text>超标率</text>
					<text>{{ devcntoday.chaobiao || 0/devcntoday.finishcount || 0 }}%</text>
				</view>
				<view class="block-main-item1">
					<text>总桩长</text>
					<text>{{ devcntoday.longlen || 0 }}m</text>
				</view>
				<view class="block-main-item1">
					<text>总灰量</text>
					<text>{{ devcntoday.pilecement || 0 }}KG</text>
				</view>
			</view>
			<view style="height: 18upx;"></view>
		</view>
		<!-- 近七天产能统计 -->
		<view class="block">
			<view class="block-title"><text class="biaoqian">1</text>近七天产能统计</view>
			<view class="block-pic">
				<view class="averagebyday">日均功效 <span>{{rjm}}米</span> <span>{{rjg}}根</span> </view>
				<view class="charts-box1">
					<qiun-data-charts type="column" :opts="opts" :chartData="chartsDataRing1" />
				</view>
			</view>
			<view style="height: 10upx;"></view>
		</view>
		<view style="height: 40upx;"></view>
		<mx-date-picker :show="dateshow" :type="type" :show-tips="true" :show-seconds="true" @confirm="confirmdate"
			@cancel="confirmdate" />
	</view>
</template>

<script>
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	import nowtime from '../../common/util/nowtime.js'
	import ruanjiapi from '../../api/ruanji.js'
	export default {
		components: {
			MxDatePicker,
		},
		data() {
			return {
				dateshow: false,
				type: 'date',
				rjm: 0,
				rjg: 0,
				nowdate: nowtime.date(),
				bgcolor: [{
						name: "滨淮5标1-1号机",
						hgl: "99.8%",
						color: 'background-color: rgba(255,215,0,.3);'
					},
					{
						name: "滨淮5标1-2号机",
						hgl: "98.8%",
						color: 'background-color: rgba(192,192,192,.3);'
					},
					{
						name: "滨淮5标1-3号机",
						hgl: "95.8%",
						color: 'background-color: rgba(184,115,51, 0.3);'
					},
				],
				listdata: [],
				sbname: '',
				sbno:'',
				devcntoday: '',
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
				chartsDataRing1: {
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
				qualifieddata: [],
				startsdata: []
			}
		},
		mounted() {
			this.getloadlist()
			this.getGongtj()
			this.getqualifiedlist()
			this.getstartslist()
		},
		methods: {
			// 时间选择
			confirmdate(e) {
				console.log(e, '确认时间')
				// this.formData.starttime = e.value
				if (e) {
					this.nowdate = e.value
					this.mixpiledevToday()
				}
				this.dateshow = false
			},
			// 设备选择
			Changesbname(e) {
				this.sbname = this.listdata[e.detail.value].shebeino_dictText
				this.sbno = this.listdata[e.detail.value].shebeino
				this.mixpiledevToday()
			},
			// 获取明星桩基排名列表
			getstartslist() {
				this.$http.get('/devicemixpilestatic/deviceMixpileStatic/list3').then(res => {
					console.log(res, "明星桩基排名")
					this.startsdata = res.data.result.records
				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			// 获取合格率排名列表
			getqualifiedlist() {
				this.$http.get('/devicemixpilestatic/deviceMixpileStatic/list2').then(res => {
					console.log(res, "合格率排名")
					this.qualifieddata = res.data.result.records
				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			// 功效统计
			getGongtj() {
				let params = {
					date: 2
				}
				this.$http.get('/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/stalists', {
					params
				}).then(res => {
					// console.log(res)
					let datas = res.data.result
					this.chartsDataRing1.categories = []
					this.chartsDataRing1.series[0].data = []
					this.chartsDataRing1.series[1].data = []
					let numg = 0
					let numM = 0
					datas.forEach(e => {
						// console.log(e)
						this.chartsDataRing1.categories.push(e.pileTime)
						this.chartsDataRing1.series[0].data.push(e.pileLength)
						this.chartsDataRing1.series[1].data.push(e.pileNum)
						numg += e.pileNum
						numM += e.pileLength
					})
					console.log(datas.length)
					this.rjg = (numg / datas.length).toFixed(2)
					this.rjm = (numM / datas.length).toFixed(2)
					console.log(numg, numM)
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
					this.listdata = res.data.result.records
				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			mixpiledevToday() {
				let params = {
					shebeino: this.sbno,
					date: this.nowdate
				}
				ruanjiapi.mixpiledevToday({
					params
				}).then(res => {
					this.devcntoday = res.data.result
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	#pileefficiency {
		width: 100%;
		// height: 100vh;
		background-color: #F3F5FE;

		.block {
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

			&-title1 {
				width: 93%;
				padding-top: 25upx;
				margin-left: 30upx;
				font-size: 36upx;
				font-weight: 600;
				color: #333333;
				display: flex;
				justify-content: space-between;
			}

			&-devicetitle {
				margin-top: 10upx;
				margin-left: 30upx;
				font-size: 30upx;
				font-weight: 600;
				color: #333333;
			}

			&-pic {
				width: 100%;

				.averagebyday {
					width: 96%;
					margin: 10upx auto;
					padding: 8upx;
					color: #FFFFFF;
					border-radius: 12upx;
					background-color: rgba(26, 144, 255, .8);

					span {
						margin: 0 10upx;
					}
				}

				.charts-box1 {
					width: 100%;
					height: 420upx;
				}

				&-item {
					width: 94%;
					height: 120upx;
					margin: 20upx auto;
					border-radius: 16upx;

					&-name {
						padding: 10upx;
					}

					&-cbl {
						padding: 10upx;
						float: right;
					}
				}
			}

			&-main {
				width: 97%;
				margin: 0 auto;
				display: flex;
				flex-wrap: wrap;
				justify-content: space-around;

				&-item {
					width: 24%;
					height: 90upx;
					margin-top: 20upx;
					border-radius: 8upx;
					display: flex;
					flex-direction: column;
					align-items: center;
					justify-content: center;
					background-color: rgba(208, 210, 218, .5);
				}

				&-item1 {
					width: 49%;
					height: 90upx;
					background-color: rgba(208, 210, 218, .5);
					margin-top: 10upx;
					border-radius: 8upx;
					display: flex;
					flex-direction: column;
					align-items: center;
					justify-content: center;
				}
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
		
		.onclick {
			background-color: rgba(51,51,51,.3);
			padding: 8upx;
			border-radius: 10upx;
		}
	}
</style>
