<template>
	<view id="ProductionDetail">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">管桩生产记录详情</block>
		</cu-custom>
		<view class="pourlist">
			<view class="pourlist-item">
				<view class="pourlist-item-sta">
					<image v-if="listData.chaobiaodengji === 0 && listData.istongji === 1"
						src="../../static/pour/hege.png">
					</image>
					<image v-if="listData.chaobiaodengji === 1 && listData.istongji === 1"
						src="../../static/pour/bhege.png">
					</image>
					<image v-if="listData.chaobiaodengji === 0 && listData.istongji === 0"
						src="../../static/pour/wjc.png">
					</image>
					<image v-if="listData.istongji === 40" src="../../static/pour/jcyc.png"></image>
				</view>
				<view class="pourlist-item-con">
					<view class="pourlist-item-con">
						<view class="pourlist-item-con-name">
							设备名称：<span>{{ listData.shebeino_dictText }}</span>
						</view>
						<view class="pourlist-item-con-name">
							桩号：<span>{{ listData.pileNo }}</span>
						</view>
						<view class="pourlist-item-con-name">
							里程：<span>{{ listData.pileMileage }}</span>
						</view>
						<view class="pourlist-item-con-name">
							设计桩长：<span>{{ listData.pileDesigndep }}</span>
						</view>
						<view class="pourlist-item-con-name">
							施工长度(m)：<span>{{ parseFloat(listData.pileRealdep).toFixed(2) }}</span>
						</view>
						<view class="pourlist-item-con-name">
							最大垂直度(%)：<span>{{ listData.pileY }}</span>
						</view>
						<view class="pourlist-item-con-name">
							开始时间：<span>{{ listData.pileStarttime }}</span>
						</view>
						<view class="pourlist-item-con-name">
							成桩时间(s)：<span>{{ listData.pileWorktime }}</span>
						</view>
						<view class="pourlist-item-con-name">
							数据时间：<span>{{ listData.datatime }}</span>
						</view>
						<view class="pourlist-item-con-name"></view>
					</view>
				</view>
			</view>
			<view style="height: 40rpx;"></view>
		</view>
		<view class="graph">
			<view class="graph-title"><text class="biaoqian"></text><text>速度/深度变化曲线</text></view>
			<view class="graph-charts">
				<qiun-data-charts type="mix" :opts="graphOptsOne" :chartData="graphDataOne" />
			</view>
		</view>
		<view class="graph">
			<view class="graph-title"><text class="biaoqian"></text><text>压桩力变化曲线</text></view>
			<view class="graph-charts">
				<qiun-data-charts type="line" :opts="graphOptsTwo" :chartData="graphDataTwo" />
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				listData: {},
				//曲线图
				graphDataOne: {},
				graphDataTwo: {},
				graphOptsOne: {
					padding: [0, 10, 15, 20],
					legend: {
						position: "top"
					},
					xAxis: {
						// labelCount:5,//数据点文字（刻度点）单屏幕限制显示的数量
						disableGrid: true
					},
					yAxis: {
						gridType: "dash",
						dashLength: 2,
						data: [{
								position: "left",
								title: "速度(cm/min)"
							},
							{
								position: "right",
								title: "深度(m)",
								textAlign: "left"
							}
						]
					},
					extra: {
						mix: {
							line: {
								width: 2,
							}
						}
					}
				},
				graphOptsTwo: {
					padding: [0, 10, 15, 20],
					legend: {
						position: "top"
					},
					xAxis: {
						// labelCount:5,//数据点文字（刻度点）单屏幕限制显示的数量
						disableGrid: true
					},
					yAxis: {
						gridType: "dash",
						dashLength: 2,
						showTitle: true,
						data: [{
							title: "压桩力(Mpa)"
						}]
					},
					extra: {
						line: {
							type: "curve",
							width: 2,
							activeType: "hollow"
						}
					}
				},
			}
		},
		onLoad(options) {
			this.listData = JSON.parse(decodeURIComponent(options.item))
			console.log(this.listData, "listdata")
			this.getData()
		},
		methods: {
			getData() {
				let params = {
					shebeino: this.listData.shebeino,
					pileNo: this.listData.pileNo,
				}
				//管桩曲线图数据
				this.$http.get('/devicepipepilehistorydatapart/devicePipepileHistorydataPart/listqxt', {
					params
				}).then(res => {
					if (res.data.success) {
						let data = res.data.result
						let partDepList = []
						let partSpeedList = []
						let partUpressList = []
						let datatimeList = []
						data.forEach((item) => {
							partDepList.push(item.partDep)
							partSpeedList.push(item.partSpeed)
							partUpressList.push(item.partUpress)
							datatimeList.push(item.datatime)
						})
						this.graphDataOne = {
							categories: datatimeList,
							series: [{
									name: "速度",
									type: "line",
									style: "curve",
									data: partSpeedList
								},
								{
									name: "深度",
									index: 1,
									type: "line",
									style: "curve",
									data: partDepList
								}
							]
						}
						this.graphDataTwo = {
							categories: datatimeList,
							series: [{
								name: "压桩力",
								data: partUpressList
							}]
						}
					}
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	#ProductionDetail {
		background-color: #F3F5FE;

		.pourlist {
			width: 100%;
			margin-top: 30rpx;

			&-item {
				position: relative;
				width: 690rpx;
				margin: 0 auto;
				margin-bottom: 30rpx;
				border-radius: 16rpx;
				background-color: white;

				&-sta {
					position: absolute;
					right: 0px;
					width: 165rpx;
					height: 165rpx;

					image {
						width: 100%;
						height: 100%;
					}
				}

				&-sta1 {
					position: absolute;
					right: 20rpx;
					bottom: 20rpx;
					width: 165rpx;
					height: 165rpx;

					image {
						width: 100%;
						height: 100%;
					}
				}

				&-con {
					width: 610rpx;
					margin: 0 auto;

					&-name {
						padding-top: 30rpx;
						color: #9299A8;
						font-size: 28rpx;

						span {
							color: #4C5971;
						}
					}
				}
			}
		}

		.graph {
			margin-top: 20rpx;

			&-title {
				display: flex;
				align-items: center;
				font-size: 36rpx;
				font-weight: 600;
				color: #333333;
				padding-top: 10px;
				margin-left: 20rpx;

				.biaoqian {
					width: 12rpx;
					height: 34rpx;
					border-radius: 6rpx;
					margin-right: 20rpx;
					color: #4A7FFF;
					background-color: #4A7FFF;
				}
			}

			&-charts {
				width: 100%;
				height: 350rpx;
			}
		}
	}
</style>