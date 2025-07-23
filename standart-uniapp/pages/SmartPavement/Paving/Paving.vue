<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">摊铺碾压</block>
		</cu-custom>
		<view class="poursta">
			<!-- <view class="poursta-title">路面检测试验指标</view> -->
			<view class="borderlist" style="width:98vw;">
				<uni-table stripe emptyText="暂无更多数据">
					<!-- 表头行 -->
					<uni-tr>
						<uni-th align="center">开始时间</uni-th>
						<uni-th align="center">结束时间</uni-th>
						<uni-th align="center">摊铺层级</uni-th>
						<uni-th align="center">开始里程</uni-th>
						<uni-th align="center">结束里程</uni-th>
						<uni-th align="center">摊铺碾压</uni-th>
					</uni-tr>
					<!-- 表格数据行 -->
					<uni-tr>
						<uni-td align="center">{{4}}</uni-td>
						<uni-td align="center">{{5}}</uni-td>
						<uni-td align="center">{{3}}</uni-td>
						<uni-td align="center">{{21}}</uni-td>
						<uni-td align="center">{{3}}</uni-td>
						<uni-td align="center">
							<view>
								<span @click="ckeck" style="color:orange;padding: 0 20upx;">查看</span>
								<span @click="playback" style="color: orange;">数据整理</span>
							</view>
						</uni-td>
					</uni-tr>


				</uni-table>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		onLoad() {
			plus.screen.lockOrientation('default');
		},
		onUnload() {
			plus.screen.lockOrientation('portrait-primary');
		},
		methods: {
			ckeck() {
				console.log('查看')
			},
			playback() {
				console.log('数据整理')
			}
		}
	}
</script>

<style lang="scss" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FE;

		.poursta {
			width: 690vw;
			height: 82vh;
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
	}
</style>
