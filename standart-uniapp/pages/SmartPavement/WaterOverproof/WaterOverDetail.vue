<template>
	<view id="waterOverDetail">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">水稳详情</block>
		</cu-custom>
		<view class="pourlist">
			<view class="pourlist-item">
				<view class="pourlist-item-sta">
					<image v-if="loaddata.chaobiaodengji == 0" src="../../../static/pour/hege.png"></image>
					<image v-if="loaddata.chaobiaodengji == 1" src="../../../static/pour/lv1.png"></image>
					<image v-if="loaddata.chaobiaodengji == 2" src="../../../static/pour/lv2.png"></image>
					<image v-if="loaddata.chaobiaodengji == 3" src="../../../static/pour/lv3.png"></image>
				</view>
				<view v-if="loaddata.chaobiaodengji != 0 && loaddata.chaobiaodengji != null" class="pourlist-item-sta1">
					<image v-if="loaddata.bhzCementOverHandler.overproofStatus == 0" src="../../../static/pour/handle4.png"></image>
					<image v-if="loaddata.bhzCementOverHandler.overproofStatus == 10" src="../../../static/pour/handle3.png"></image>
					<image v-if="loaddata.bhzCementOverHandler.overproofStatus == 20" src="../../../static/pour/handle1.png"></image>
					<image v-if="loaddata.bhzCementOverHandler.overproofStatus == 30" src="../../../static/pour/handle6.png"></image>
					<image v-if="loaddata.bhzCementOverHandler.overproofStatus == 40" src="../../../static/pour/handle2.png"></image>
					<image v-if="loaddata.bhzCementOverHandler.overproofStatus == 60" src="../../../static/pour/handle5.png"></image>
				</view>
				<view class="pourlist-item-con">
					<view class="pourlist-item-con-name">
						设备名称：<span>{{ loaddata.shebeibianhao }}</span>
					</view>
					<view class="pourlist-item-con-name">
						出料时间：<span>{{ loaddata.chuliaoshijian }}</span>
					</view>
					<view class="pourlist-item-con-name">
						总产量：<span>{{ parseInt(loaddata.zongchanliang) }}</span>
					</view>
					<view class="pourlist-item-con-name">
						配方号：<span>{{ loaddata.formulaNo }}</span>
					</view>
					<view class="pourlist-item-con-name"></view>
				</view>
			</view>
		</view>
		<!-- <view v-if="loaddata.chaobiaodengji != 0 && loaddata.chaobiaodengji != null" class="reason">超标原因：{{ loaddata.overReason }}</view> -->
		<view class="cldetail">
			<view class="cldetail-title"><text class="biaoqian"></text><text>材料详情</text></view>
			<uni-table stripe emptyText="暂无更多数据">
				<uni-tr>
					<uni-th width="80" align="center">材料名</uni-th>
					<uni-th width="80" align="center">实际用量</uni-th>
					<uni-th width="80" align="center">实际配比</uni-th>
					<uni-th width="80" align="center">理论配比</uni-th>
					<uni-th width="80" align="center">误差值</uni-th>
					<uni-th width="80" align="center">超标等级</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in loaddetail" :key="index">
					<uni-td align="center">
						<text
							:class="[item.chaobiaodengji == 1?'elementary':item.chaobiaodengji == 2?'intermediate':item.chaobiaodengji == 3?'senior':'']">{{item.cailiaoming}}</text>
					</uni-td>
					<uni-td align="center"><text
							:class="[item.chaobiaodengji == 1?'elementary':item.chaobiaodengji == 2?'intermediate':item.chaobiaodengji == 3?'senior':'']">{{item.shijiyongliang}}</text>
					</uni-td>
					<uni-td align="center"><text
							:class="[item.chaobiaodengji == 1?'elementary':item.chaobiaodengji == 2?'intermediate':item.chaobiaodengji == 3?'senior':'']">{{item.shijipb}}</text>
					</uni-td>
					<uni-td align="center"><text
							:class="[item.chaobiaodengji == 1?'elementary':item.chaobiaodengji == 2?'intermediate':item.chaobiaodengji == 3?'senior':'']">{{item.lilunpb}}</text>
					</uni-td>
					<uni-td align="center"><text
							:class="[item.chaobiaodengji == 1?'elementary':item.chaobiaodengji == 2?'intermediate':item.chaobiaodengji == 3?'senior':'']">{{item.wucha}}</text>
					</uni-td>
					<uni-td align="center">
						<view v-if="item.chaobiaodengji == 0" style="color: #0FBF6E;">合格</view>
						<view v-if="item.chaobiaodengji == 1" class="elementary">初级超标</view>
						<view v-if="item.chaobiaodengji == 2" class="intermediate">中级超标</view>
						<view v-if="item.chaobiaodengji == 3" class="senior">高级超标</view>
					</uni-td>
				</uni-tr>
			</uni-table>
			<view style="height: 30rpx;"></view>
		</view>
		<!-- <view v-if="loaddata.chaobiaodengji != 0 && loaddata.chaobiaodengji != null" class="progress">
			<u-steps direction="column">
				<u-steps-item v-for="(item,index) in handlelist" :key="index" :title="item.tile" :desc="item.desc">
					<u-icon :name="item.time==null?item.sta2:item.sta1" slot="icon" size='22'></u-icon>
					<view style="width: 95%; color: #9299A8;" slot="desc">
						<view style="height: 50rpx;" v-if="item.time==null"></view>
						<view v-else>
							<view v-if="index == 0">完成时间{{item.time}}</view>
							<view v-if="index == 1">审核人{{item.person}},审核时间{{item.time}},审核意见{{item.way}}
							</view>
							<view v-if="index == 2">
								<view>处置人{{item.person}},处置时间{{item.time}},处理方式{{item.way}}</view>
								<image v-for="(img,i) in viewImageList" :key="i" @click="ViewImage(i)"
									style="width: 100px; height: 100px;" mode="aspectFill" :src="img"></image>
							</view>
							<view v-if="index == 3">预警时间{{item.time}}</view>
						</view>
					</view>
				</u-steps-item>
			</u-steps>
			<view style="height: 50rpx;"></view>
		</view> -->
		<view v-if="loaddata.chaobiaodengji != 0 && loaddata.chaobiaodengji != null" class="btn">
			<u-button v-has="'WaterOverDetail:cz'"
				v-show="loaddata.bhzCementOverHandler.overproofStatus === 0 || loaddata.bhzCementOverHandler.overproofStatus === 30 || loaddata.bhzCementOverHandler.overproofStatus === 60"
				class="btnclass" type="warning" @click="checkMan" text="施工处置"></u-button>
			<u-button v-has="'WaterOverDetail:sh'" v-show="loaddata.bhzCementOverHandler.overproofStatus === 10"
				class="btnclass" type="primary" @click="checkExa" plain text="监理审核"></u-button>
			<u-button v-has="'WaterOverDetail:sp'" v-show="loaddata.bhzCementOverHandler.overproofStatus === 40"
				class="btnclass" type="primary" @click="checkOver" text="指挥部审批"></u-button>
			<u-button v-show="loaddata.bhzCementOverHandler.overproofStatus === 20" class="btnclass" type="primary"
				@click="checkDetail" text="详情"></u-button>
		</view>
		<view style="height: 40rpx;"></view>
	</view>
</template>

<script>
	export default {
		components: {},
		data() {
			return {
				loaddata: {},
				loaddetail: [],
				handlelist: [],
				viewImageList: [],
				liucengimg: [{
						sta1: '../../../static/pour/finish1.png',
						sta2: '../../../static/pour/finish2.png',
					},
					{
						sta1: '../../../static/pour/audit1.png',
						sta2: '../../../static/pour/audit2.png',
					},
					{
						sta1: '../../../static/pour/chuzhi1.png',
						sta2: '../../../static/pour/chuzhi2.png',
					},
					{
						sta1: '../../../static/pour/yujin1.png',
						sta2: '../../../static/pour/yujin2.png',
					},
				]
			}
		},
		onLoad(options) {
			this.loaddata = JSON.parse(options.wDetail)
			this.getloaddetail()
			// this.getmanprocess()
		},
		methods: {
			// 施工处置
			checkMan() {
				uni.navigateTo({
					url: `/pages/smartbh/mix/handle/ChuZhi?batchNo=${this.loaddata.guid}&bhz=2&level=${this.loaddata.chaobiaodengji}`
				})
			},
			// 监理审核
			checkExa() {
				uni.navigateTo({
					url: `/pages/smartbh/mix/handle/ShenHe?batchNo=${this.loaddata.guid}&bhz=2&level=${this.loaddata.chaobiaodengji}`
				})
			},
			// 指挥部审批
			checkOver() {
				uni.navigateTo({
					url: `/pages/smartbh/mix/handle/ShenPi?batchNo=${this.loaddata.guid}&bhz=2&level=${this.loaddata.chaobiaodengji}`
				})
			},
			//详情
			checkDetail() {
				uni.navigateTo({
					url: `/pages/smartbh/mix/handle/DealDetail?data=${JSON.stringify(this.loaddata)}`
				})
			},
			// // 拌合站处置流程信息
			// getmanprocess() {
			// 	let params = {
			// 		baseid: this.loaddata.guid
			// 	}
			// 	this.$http.get('/czsh/bhzCementOverHandler/handlelist', {
			// 		params
			// 	}).then(res => {
			// 		this.handlelist = res.data.result.map((item, index) => {
			// 			return {
			// 				...item,
			// 				...this.liucengimg[index]
			// 			};
			// 		});
			// 		this.handlelist.forEach(item => {
			// 			if (item.tile == "处置") {
			// 				this.viewImageList = item.file ? item.file.split(',') : []
			// 			}
			// 		});
			// 	})
			// },
			// //查看图片
			// ViewImage(i) {
			// 	uni.previewImage({
			// 		urls: this.viewImageList,
			// 		current: i,
			// 	});
			// },
			// 获取拌合站材料详细信息
			getloaddetail() {
				let params = {
					baseGuid: this.loaddata.guid
				}
				this.$http.get('/swbhz/bhzSwBases/queryBhzSwCailiaoByMainId', {
					params
				}).then(res => {
					this.loaddetail = res.data.result
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	#waterOverDetail {
		background-color: #F3F5FE;

		.pourlist {
			width: 100%;
			margin-top: 30rpx;

			&-item {
				position: relative;
				width: 690rpx;
				// height: 462rpx;
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
					// height: 336rpx;
					margin: 0 auto;

					// background-color: red;
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

		// .reason {
		// 	width: 690rpx;
		// 	margin: 0 auto;
		// 	background-color: rgb(255, 255, 255);
		// 	color: rgb(239, 11, 11);
		// 	line-height: 50rpx;
		// 	text-align: center;
		// 	border-radius: 8px;
		// 	font-size: 32rpx;
		// }

		.cldetail {
			width: 690rpx;
			// height: 520rpx;
			margin: 0 auto;
			margin-top: 30rpx;
			border-radius: 16rpx;
			background-color: #FFFFFF;

			&-title {
				display: flex;
				align-items: center;
				font-size: 36rpx;
				font-weight: 600;
				color: #333333;
				padding-top: 10px;
				margin-left: 30rpx;

				.biaoqian {
					width: 12rpx;
					height: 34rpx;
					border-radius: 6rpx;
					margin-right: 20rpx;
					color: #4A7FFF;
					background-color: #4A7FFF;
				}
			}

			.elementary {
				color: orange;
			}

			.intermediate {
				color: yellow;
			}

			.senior {
				color: red;
			}
		}

		.progress {
			width: 690rpx;
			// height: 736rpx;
			margin: 0 auto;
			margin-top: 30rpx;
			border-radius: 16rpx;
			background-color: #FFFFFF;
			padding-top: 30rpx;
			padding-left: 30rpx;
		}

		.btn {
			width: 690rpx;
			margin: 0 auto;
			margin-top: 30rpx;
			display: flex;

			.btnclass {
				width: 30%;
			}
		}

		.modal {
			width: 100%;

			// background-color: red;
			&-name {
				color: #4C5971;
				font-size: 30rpx;
				margin: 20rpx 0;
			}
		}
	}
</style>