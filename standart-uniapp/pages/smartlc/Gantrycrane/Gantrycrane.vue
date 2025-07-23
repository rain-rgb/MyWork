<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">龙门吊</block>
			<!-- <view slot="right" @click="screen">筛选</view> -->
		</cu-custom>
		<view class="card" v-for="(item,index) in listdata" :key="index">
			<view class="article">
				<view class="section-text">
					<view class="title">
						<view class="round-dot">
							<view class="round"></view>
						</view>
						<view class="title-iu">
							<view class="title-font">{{item.deviceCode_dictText}}</view>
						</view>
					</view>
				</view>
			</view>
			<view class="box1">
				<view class="gou">
					<view class="item">主钩高度：<span>{{item.mainHookheight}}</span></view>
					<view class="item">主钩吊重：<span>{{item.mainHookload}}</span></view>
					<view class="item">副钩高度：<span>{{item.reservedVicehookheight}}</span></view>
					<view class="item">副钩吊重：<span>{{item.reservedVicehookload}}</span></view>
				</view>
				<view class="gou">
					<view class="item1">数据上传时间：<span>{{item.cranedate}}</span></view>
					<view class="item">风速：<span>{{item.windSpeed}}</span></view>
					<view class="item">累计时间：<span>{{item.allTime}}</span></view>
					<view class="item">循环使用次数：<span>{{item.allTimes}}</span></view>
				</view>
				<view class="gou">
					<view class="item">主钩载荷状态：<span
							:style="item.mainHookstatus==0?'color:green;font-size:30upx':'color:red;font-size:30upx'">{{ item.mainHookstatus==0 ? '正常' : '预警'}}</span>
					</view>
					<view class="item">副钩载荷状态：<span
							:style="item.reservedVicehookstatus==0?'color:green;font-size:30upx':'color:red;font-size:30upx'">{{ item.reservedVicehookstatus==0 ? '正常' : '预警'}}</span>
					</view>
					<view class="item">抗风防滑状态：<span
							:style="item.windAntiskidstatus==0?'color:green;font-size:30upx':'color:red;font-size:30upx'">{{ item.windAntiskidstatus==0 ? '正常' : '限位'}}</span>
					</view>
					<view class="item">电缆卷筒状态：<span
							:style="item.wireDrumstatus==0?'color:green;font-size:30upx':'color:red;font-size:30upx'">{{ item.wireDrumstatus==0 ? '正常' : '预警' }}</span>
					</view>
					<view class="item">风速报警：<span
							:style="item.windSpeedalarm==0?'color:green;font-size:30upx':'color:red;font-size:30upx'">{{ item.windSpeedalarm==0 ? '正常' : '预警' }}</span>
					</view>
					<view class="item">支腿偏差报警：<span
							:style="item.bigcarAlm==0?'color:green;font-size:30upx':'color:red;font-size:30upx'">{{ item.bigcarAlm==0 ? '正常' : '预警' }}</span>
					</view>
				</view>
			</view>
			<view class="box1">
				<view class="gou">
					<view class="item">小车行程：<span>{{item.smallCarroute}}</span></view>
					<view class="item"></view>
					<view class="item">小车前限位：<span>{{item.smallCarfrontlimit}}</span></view>
					<view class="item">小车后限位：<span>{{item.smallCarbacklimit}}</span></view>
				</view>
				<view class="gou">
					<view class="item">小车2行程：<span>{{item.reservedSmallcarroute}}</span></view>
					<view class="item"></view>
					<view class="item">小车2前限位：<span>{{item.reservedSmallcarfrontlimit}}</span></view>
					<view class="item">小车2后限位：<span>{{item.reservedSmallcarbacklimit}}</span></view>
				</view>
				<view class="gou">
					<view class="item">大车行程：<span>{{item.bigCarroute}}</span></view>
					<view class="item">大车支腿偏差：<span>{{item.bigCarlegdeviation}}</span></view>
					<view class="item">大车左限位：<span>{{item.bigCarleftlimit}}</span></view>
					<view class="item">大车右限位：<span>{{item.bigCarrightlimit}}</span></view>
				</view>
				<view class="gou">
					<view class="item">小车1制动器1：<span>{{item.smallCar1brake1}}</span></view>
					<view class="item">小车1制动器2：<span>{{item.smallCar1brake2}}</span></view>
					<view class="item">小车2制动器1：<span>{{item.smallCar2brake1}}</span></view>
					<view class="item">小车2制动器2：<span>{{item.smallCar2brake2}}</span></view>
				</view>
			</view>
			<view style="height: 10upx;"></view>
		</view>
	</view>
</template>
<script>
	export default {
		name: 'Gantrycrane',
		data() {
			return {
				listdata: []
			}
		},
		onReady() {
			this.loadData()
		},
		onPullDownRefresh() {
			uni.showNavigationBarLoading()
			this.loadData()
			uni.stopPullDownRefresh()
			uni.hideNavigationBarLoading()
		},
		methods: {
			loadData() {
				this.$http.get('/devicecranerealdata/deviceCraneRealdata/list').then(res => {
					if (res.data.success) {
						this.listdata = res.data.result.records
						// console.log(this.listdata,'龙门吊实时数据')
					}
				}).catch(e => {
					console.log("请求错误", e)
				})
			},
		}
	}
</script>
<style lang="less" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FE;

		.card {
			width: 700upx;
			margin: 0 auto;
			margin-top: 10upx;
			border-radius: 10px;
			background-color: white;

			.article {
				width: 700upx;
				height: auto;
				background-color: #fff;
				margin: 10px auto;
				border-radius: 10px;

				.section-text {
					width: 700upx;
					height: auto;

					display: flex;
					flex-direction: row;
					justify-content: space-between;
					flex: 1;

					.title {
						width: 100%;
						height: 25px;
						// border: 1px solid #18BC37;
						display: flex;
						flex-direction: row;
						justify-content: space-around;
						align-items: center;

						.round-dot {
							flex: 1;

							.round {
								width: 6px;
								height: 17px;
								background: #387ffd;
								border-radius: 6px;
								margin: 0 auto;
							}
						}

						.title-iu {
							flex: 10;

							.title-font {
								font-size: 18px;
								font-family: " PingFang SC";
								font-weight: bold;
								color: #333333;
							}
						}
					}
				}

			}
		}

		.box1 {
			width: 700upx;
			height: auto;
			box-shadow: 1px 4px 7px #387FFD;
			margin: 10px 0;
			line-height: 27px;
			padding: 8px 25px;
			.gou {
				width: 96%;
				margin: 10px auto;
				border-radius: 10upx;
				background-color: #ffffff;
				display: flex;
				flex-wrap: wrap;

				.item {
					width: 50%;
					color: #000;
					font-size: 29upx;

					span {
						color: #959595;
						font-size: 28upx;
					}
				}

				.item1 {
					width: 100%;
				}
			}
		}
	}
</style>
