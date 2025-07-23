<template>
	<view id="ZlOverDetail">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">张拉详情</block>
		</cu-custom>
		<view class="pourlist">
			<view class="pourlist-item">
				<view class="pourlist-item-sta">
					<image v-if="loaddata.hege == 0" src="../../../static/pour/hege.png"></image>
					<image v-if="loaddata.hege == 1" src="../../../static/pour/cjyj.png"></image>
					<image v-if="loaddata.hege == 2" src="../../../static/pour/zjyj.png"></image>
					<image v-if="loaddata.hege == 3" src="../../../static/pour/gjyj.png"></image>
				</view>
				<view class="pourlist-item-sta1">
					<image v-if="loaddata.zhanglaYajiangOverHandler.overproofStatus == 0"
						src="../../../static/pour/handle4.png">
					</image>
					<image v-if="loaddata.zhanglaYajiangOverHandler.overproofStatus == 10"
						src="../../../static/pour/handle3.png"></image>
					<image v-if="loaddata.zhanglaYajiangOverHandler.overproofStatus == 40"
						src="../../../static/pour/handle2.png"></image>
					<image v-if="loaddata.zhanglaYajiangOverHandler.overproofStatus == 30"
						src="../../../static/pour/handle8.png"></image>
					<image v-if="loaddata.zhanglaYajiangOverHandler.overproofStatus == 20"
						src="../../../static/pour/handle1.png"></image>
					<!-- <image v-if="loaddata.zhanglaYajiangOverHandler.overproofStatus == 50"
						src="../../../static/pour/handle10.png"></image> -->
					<image v-if="loaddata.zhanglaYajiangOverHandler.overproofStatus == 60"
						src="../../../static/pour/handle11.png"></image>
				</view>
				<view class="pourlist-item-con">
					<view class="pourlist-item-con-name">
						工程部位：<span>{{ loaddata.trZhanglaXxbList[0].gjbh }}</span>
					</view>
					<view class="pourlist-item-con-name">
						张拉梁型：<span>{{ loaddata.trZhanglaXxbList[0].gjmc }}</span>
					</view>
					<view class="pourlist-item-con-name">
						设备名称：<span>{{ loaddata.shebeibianhao }}</span>
					</view>
					<view class="pourlist-item-con-name">
						延伸量误差：<span>{{ loaddata.sclper }}</span>
					</view>
					<view class="pourlist-item-con-name">
						施工时间：<span>{{ loaddata.zhanglaMList[0].zlsj }}</span>
					</view>
					<view class="pourlist-item-con-name">
						超标原因：<span style="color: rgb(239, 11, 11);">{{ loaddata.trZhanglaXxbList[0].overReason }}</span>
					</view>
					<view class="pourlist-item-con-name"></view>
				</view>
			</view>
		</view>
		<view class="section">
			<view class="section-text">
				<view class="title" style="width: 212rpx;">
					<view class="round"></view>
					<view class="title-font">预警孔道</view>
				</view>
				<view class="kd-content" v-for="(item,index) in holeidList" :key="index">
					<view class="yjkd"></view>
					<view class="kd-msg">
						<view style="color: #333333;">{{ item.gsbh }}</view>
						<view>{{ item.overReason }}</view>
					</view>
					<!-- <u-icon color="#999999" size="16" name="arrow-right"></u-icon> -->
				</view>
			</view>
		</view>
		<view class="btn">
			<u-button v-has="'ZlOverDetail:cz'"
				v-show="loaddata.zhanglaYajiangOverHandler.overproofStatus === 0 || loaddata.zhanglaYajiangOverHandler.overproofStatus === 30 || loaddata.zhanglaYajiangOverHandler.overproofStatus === 60"
				class="btnclass" type="warning" @click="checkMan" text="施工处置"></u-button>
			<u-button v-has="'ZlOverDetail:sh'" v-show="loaddata.zhanglaYajiangOverHandler.overproofStatus === 10"
				class="btnclass" type="primary" @click="checkExa" plain text="监理审核"></u-button>
			<u-button v-has="'ZlOverDetail:sp'" v-show="loaddata.zhanglaYajiangOverHandler.overproofStatus === 40"
				class="btnclass" type="primary" @click="checkOver" text="指挥部审批"></u-button>
			<!-- <u-button v-show="loaddata.zhanglaYajiangOverHandler.overproofStatus === 20" class="btnclass" type="primary"
				@click="checkDetail" text="详情"></u-button> -->
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
				holeidList: []
			}
		},
		onLoad(options) {
			this.loaddata = JSON.parse(options.data)
			// console.log(this.loaddata, "loaddata");
			this.getHoleidList()
		},
		methods: {
			// 施工处置
			checkMan() {
				uni.navigateTo({
					url: `/pages/smartlc/ZlOverproof/handle/ZlChuZhi?syjid=${this.loaddata.syjid}`
				})
			},
			// 监理审核
			checkExa() {
				uni.navigateTo({
					url: `/pages/smartlc/ZlOverproof/handle/ZlShenHe?syjid=${this.loaddata.syjid}`
				})
			},
			// 指挥部审批
			checkOver() {
				uni.navigateTo({
					url: `/pages/smartlc/ZlOverproof/handle/ZlShenPi?syjid=${this.loaddata.syjid}`
				})
			},
			//详情
			// checkDetail() {
			// 	uni.navigateTo({
			// 		url: `/pages/smartbh/mix/handle/DealDetail?data=${JSON.stringify(this.loaddata)}`
			// 	})
			// },
			//获取孔道
			getHoleidList() {
				let params = {
					syjid: this.loaddata.syjid,
					type: 0,
				}
				this.$http.get('/zhanglam/zhanglaYajiangOverHandler/holeidList', {
					params
				}).then(res => {
					if (res.data.success) {
						this.holeidList = res.data.result
					}
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	#ZlOverDetail {
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

		.section {
			width: 700rpx;
			height: auto;
			background-color: #fff;
			margin: 15px auto;
			border-radius: 10px;

			.section-text {
				color: #9299A8;
				font-size: 30upx;
				padding: 15px 15px;
				line-height: 55upx;
				margin: 10px 0;

				span {
					color: #4C5971;
					font-size: 30upx;
					font-family: 'PingFang-SC-Medium';
					padding: 10upx 15upx;

				}

				.kd-content {
					width: 100%;
					height: 140rpx;
					background-color: #F6F7FA;
					display: flex;
					align-items: center;
					padding-right: 20rpx;

					.yjkd {
						background: url('../../../static/shiti/one.png') no-repeat;
						background-size: 100%;
						width: 108rpx;
						height: 108rpx;
						margin: 20rpx 30rpx;
					}

					.kd-msg {
						flex: 1;
					}
				}

				.title {
					width: 176rpx;
					height: 34px;
					// border: 1px solid #18BC37;
					display: flex;
					flex-direction: row;
					justify-content: space-between;
					align-items: center;

					.round {
						width: 6px;
						height: 17px;
						background: #387FFD;
						border-radius: 6px;
					}

					.title-font {
						font-size: 18px;
						font-family: ' PingFang SC';
						font-weight: bold;
						color: #333333;
					}
				}
			}

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

	}
</style>