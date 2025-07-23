<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">水稳管控</block>
			<!-- <view slot="right" @click="screen">筛选</view> -->
		</cu-custom>

		<view class="top">
			<view class="top-item" @click="handleNav(0)">
				<view class="top-item-img" style="background-image: url(../../../static/pour/tag1.png);"></view>
				<view class="top-item-name">水稳施工</view>
			</view>
			<view class="top-item" @click="handleNav(1)">
				<view class="top-item-img" style="background-image: url(../../../static/pour/tag2.png);"></view>
				<view class="top-item-name">水稳原材</view>
			</view>
			<view class="top-item" @click="handleNav(2)">
				<view class="top-item-img" style="background-image: url(../../../static/pour/tag3.png);"></view>
				<view class="top-item-name">水稳拌和</view>
			</view>
			<view class="top-item" @click="handleNav(3)">
				<view class="top-item-img" style="background-image: url(../../../static/pour/tag4.png);"></view>
				<view class="top-item-name">水稳检测</view>
			</view>
			<view class="top-item" @click="handleNav(4)">
				<view class="top-item-img" style="background-image: url(../../../static/pour/tag5.png);"></view>
				<view class="top-item-name">水稳运输</view>
			</view>
			<view class="top-item" @click="handleNav(5)">
				<view class="top-item-img" style="background-image: url(../../../static/pour/tag6.png);"></view>
				<view class="top-item-name">水稳摊铺</view>
			</view>
		</view>
		<!-- 消息中心 -->
		<view class="news">
			<view class="news-title">
				<view class="news-title-name">当日实时预警播报消息</view>
				<view class="news-title-more"> <span>查看更多</span>
					<u-icon color="#999999" size="12" name="arrow-right"></u-icon>
				</view>
			</view>
			<view class="news-con" v-if="newslist.length > 0">
				<view class="news-con-item" v-for="(item,index) in newslist" :key="index">
					<view class="news-con-item-but">{{ item.overLevel_dictText }}</view>
					<view class="news-con-item-des">{{ item.overReason }}{{ item.poureLocation }}</view>
				</view>
			</view>
			<view class="news-con" v-else>
				<view class="news-con-item">
					<u-notice-bar bgColor="#fff" fontSize=16 color="#91CB74" text="暂无新消息提醒"></u-notice-bar>
				</view>
				<view class="tiptitle"></view>
			</view>
		</view>
		<!-- 今日在线 -->
		<view class="online">
			<view class="online-title">今日在线</view>
			<view class="online-equipment">
				<view class="equipment" @click="onlineRun()">水稳站:{{onlineList.type || 0}}</view>
				<view class="equipment" @click="onlineRun()">地磅:{{onlineList.type || 0}}</view>
				<view class="equipment" @click="onlineRun()">运输车:{{onlineList.type || 0}}</view>
				<view class="equipment" @click="onlineRun()">摊铺机:{{onlineList.type || 0}}</view>
				<view class="equipment" @click="onlineRun()">压路机:{{onlineList.type || 0}}</view>
				<view class="equipment" @click="onlineRun()">环境仪:{{onlineList.type || 0}}</view>
			</view>
		</view>
		<!-- 当日进场原材料 -->
		<view class="tableStyle">
			<view class="tableStyle-title">当日进场原材料</view>
			<view class="table">
				<uni-table stripe emptyText="暂无更多数据">
					<!-- 表头行 -->
					<uni-tr>
						<uni-th width="80" align="center">序号</uni-th>
						<uni-th width="80" align="center">规格类型</uni-th>
						<uni-th width="80" align="center">重量</uni-th>
						<uni-th width="80" align="center">车次数</uni-th>
					</uni-tr>
					<!-- 表格数据行 -->
					<uni-tr v-for="(item, index) in materialTableData" :key="index">
						<uni-td align="center">{{index > 8 ? index + 1 : '0' + (index + 1)}}</uni-td>
						<uni-td align="center">{{item.betLev}}</uni-td>
						<uni-td align="center">{{item.betlevsum}}</uni-td>
						<uni-td align="center">{{item.betlevsum}}</uni-td>
					</uni-tr>
				</uni-table>
			</view>
		</view>
		<!-- 当日水稳拌合 -->
		<view class="tableStyle">
			<view class="tableStyle-title">当日水稳拌合</view>
			<view class="table">
				<uni-table stripe emptyText="暂无更多数据">
					<!-- 表头行 -->
					<uni-tr>
						<uni-th width="60" align="center">序号</uni-th>
						<uni-th width="60" align="center">类型</uni-th>
						<uni-th width="60" align="center">重量</uni-th>
						<uni-th width="60" align="center">盘数</uni-th>
						<uni-th width="60" align="center">超标率</uni-th>
					</uni-tr>
					<!-- 表格数据行 -->
					<uni-tr v-for="(item, index) in watermixTableData" :key="index">
						<uni-td align="center">{{index > 8 ? index + 1 : '0' + (index + 1)}}</uni-td>
						<uni-td align="center">{{item.betLev}}</uni-td>
						<uni-td align="center">{{item.betlevsum}}</uni-td>
						<uni-td align="center">{{item.betlevsum}}</uni-td>
						<uni-td align="center">{{item.betlevsum}}</uni-td>
					</uni-tr>
				</uni-table>
			</view>
		</view>
		<!-- 当日摊铺碾压 -->
		<view class="tableStyle">
			<view class="tableStyle-title">当日摊铺碾压</view>
			<view class="table">
				<uni-table stripe emptyText="暂无更多数据">
					<!-- 表头行 -->
					<uni-tr>
						<uni-th width="80" align="center">设备编号</uni-th>
						<uni-th width="80" align="center">行驶里程</uni-th>
						<uni-th width="80" align="center">实时速度</uni-th>
						<uni-th width="80" align="center">今日超标</uni-th>
					</uni-tr>
					<!-- 表格数据行 -->
					<uni-tr v-for="(item, index) in rollingTableData" :key="index">
						<uni-td align="center">{{item.betLev}}</uni-td>
						<uni-td align="center">{{item.betlevsum}}</uni-td>
						<uni-td align="center">{{item.betlevsum}}</uni-td>
						<uni-td align="center">{{item.betlevsum}}</uni-td>
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
				newslist: [],
				onlineList: {},
				materialTableData: [],
				watermixTableData: [],
				rollingTableData: []
			}
		},
		mounted() {
			// this.getMixcblist();
		},
		methods: {
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
					this.newslist = res.data.result.records
				})
			},
			onlineRun(e) {
				// console.log(e)
			},
			handleNav(e) {

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

	.top {
		width: 690rpx;
		height: 334rpx;
		margin: 30rpx auto 0;
		border-radius: 16rpx;
		background-color: white;
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
		align-items: center;


		&-item {
			width: 210rpx;
			height: 150rpx;
			border-radius: 16rpx;
			background-color: white;
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;

			&-img {
				width: 75rpx;
				height: 75rpx;
				background-repeat: no-repeat;
				background-size: 100% 100%;
				margin-bottom: 24rpx;
			}

			&-name {
				font-size: 28rpx;
				color: #333333;
			}
		}
	}

	.news {
		width: 690rpx;
		margin: 0 auto;
		margin-top: 30rpx;

		&-title {
			display: flex;
			justify-content: space-between;
			align-items: center;

			&-name {
				color: #333333;
				font-size: 36rpx;
				font-weight: 600;
			}

			&-more {
				display: flex;
				align-items: center;
				font-size: 24rpx;
				color: #999999;
			}
		}

		&-con {
			margin-top: 30rpx;

			&-item {
				margin-top: 30rpx;
				display: flex;

				&-but {
					width: 130rpx;
					padding: 0 10rpx;
					height: 35rpx;
					background-color: rgba(74, 127, 255, .1);
					font-size: 22rpx;
					color: #387FFD;
					text-align: center;
				}

				&-des {
					width: 500rpx;
					margin-left: 18rpx;
					font-size: 26rpx;
					color: #333333;
				}
			}
		}
	}

	.tableStyle {
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

	.online {
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

		&-equipment {
			display: flex;
			flex-wrap: wrap;
			justify-content: space-evenly;

			.equipment {
				width: 30%;
				height: 100rpx;
				margin-top: 20rpx;
				border-radius: 20rpx;
				background-color: #E8E8FF;
				line-height: 100rpx;
				text-align: center;
			}
		}
	}
</style>
