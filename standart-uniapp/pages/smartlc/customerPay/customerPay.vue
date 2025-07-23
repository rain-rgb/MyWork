<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">交付进度</block>
			<!-- <view slot="right" @click="screen">筛选</view> -->
		</cu-custom>

		<!-- 交付进度统计 -->
		<view class="deliver">
			<view class="deliver-title">交付进度统计</view>
			<view class="table">
				<uni-table stripe emptyText="暂无更多数据">
					<uni-tr>
						<uni-th width="100" align="center">序号</uni-th>
						<uni-th align="center">客户名称</uni-th>
						<uni-th align="center">订单号</uni-th>
						<uni-th width="100" align="center">梁板类型</uni-th>
						<uni-th width="100" align="center">订单数量</uni-th>
						<uni-th width="100" align="center">已交付数量</uni-th>
						<uni-th width="100" align="center">百分比</uni-th>
					</uni-tr>

					<uni-tr v-for="(item, index) in deliverTableData" :key="index">
						<uni-td align="center">{{item.xuaho}}</uni-td>
						<uni-td align="center"><text style="color: #1E90FF;text-decoration: underline;"
								@click="rowclick(item)">{{item.material}}</text></uni-td>
						<uni-td align="center">{{item.hao}}</uni-td>
						<uni-td align="center">{{item.liang}}</uni-td>
						<uni-td align="center">{{item.type}}</uni-td>
						<uni-td align="center">{{item.amount}}</uni-td>
						<uni-td align="center">{{item.stock}}</uni-td>
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
				deliverTableData: [],
			}
		},
		created() {},
		methods: {
			rowclick(e) {
				console.log(e, "行列表信息")
				uni.navigateTo({
					url: '/pages/smartlc/customerPay/customerList?id=' + e.hao
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FE;
		padding-bottom: 40rpx;
	}

	.deliver {
		width: 690rpx;
		margin: 30rpx auto 0;
		padding-bottom: 30rpx;
		border-radius: 16rpx;
		background-color: #FFFFFF;

		&-title {
			padding-top: 25rpx;
			margin-left: 30rpx;
			font-size: 36rpx;
			font-weight: 600;
			color: #333333;
		}

		.table {
			width: 95%;
			margin: 10rpx auto;
		}
	}
</style>
