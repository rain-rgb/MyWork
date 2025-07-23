<template>
	<view id="dealDetail">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">处置详情</block>
		</cu-custom>
		<view class="pourlist">
			<view class="pourlist-item">
				<view class="pourlist-item-sta">
					<image v-if="chaobiaodengji == 0" src="../../../../static/pour/hege.png"></image>
					<image v-if="chaobiaodengji == 1" src="../../../../static/pour/lv1.png"></image>
					<image v-if="chaobiaodengji == 2" src="../../../../static/pour/lv2.png"></image>
					<image v-if="chaobiaodengji == 3" src="../../../../static/pour/lv3.png"></image>
				</view>
				<view class="pourlist-item-sta1">
					<image src="../../../../static/pour/handle7.png"></image>
				</view>
				<view class="pourlist-item-con">
					<view class="pourlist-item-con-name">
						问题原因：<span>{{dealInformation.problemReasons}}</span>
					</view>
					<view class="pourlist-item-con-name">
						处理方式：<span>{{dealInformation.handleWay}}</span>
					</view>
					<view class="pourlist-item-con-name">
						处理结果：<span>{{dealInformation.handleResult}}</span>
					</view>
					<view class="pourlist-item-con-name">
						处置人：<span>{{dealInformation.handlePerson}}</span>
					</view>
					<view class="pourlist-item-con-name">
						处理时间：<span>{{dealInformation.handleTime}}</span>
					</view>
					<view class="pourlist-item-con-name">
						附件：
					</view>
					<view>
						<image v-for="(img,i) in detailList1" :key="i" @click="ViewImage1(i)"
							style="width: 100px; height: 100px;" mode="aspectFill" :src="img">
						</image>
					</view>
				</view>
			</view>
		</view>
		<view class="pourlist">
			<view class="pourlist-item">
				<view class="pourlist-item-sta">
					<image v-if="chaobiaodengji == 0" src="../../../../static/pour/hege.png"></image>
					<image v-if="chaobiaodengji == 1" src="../../../../static/pour/lv1.png"></image>
					<image v-if="chaobiaodengji == 2" src="../../../../static/pour/lv2.png"></image>
					<image v-if="chaobiaodengji == 3" src="../../../../static/pour/lv3.png"></image>
				</view>
				<view class="pourlist-item-sta1">
					<image src="../../../../static/pour/handle9.png"></image>
				</view>
				<view class="pourlist-item-con">
					<view class="pourlist-item-con-name">
						监理审批：<span>{{dealInformation.supervisorApproval}}</span>
					</view>
					<view class="pourlist-item-con-name">
						监理结果：<span>{{dealInformation.supervisorResult}}</span>
					</view>
					<view class="pourlist-item-con-name">
						审核人：<span>{{dealInformation.approvalPerson}}</span>
					</view>
					<view class="pourlist-item-con-name">
						监理处理时间：<span>{{dealInformation.supervisorHandleTime}}</span>
					</view>
                    <view class="pourlist-item-con-name">
						附件：
					</view>
                    <view>
				<image v-for="(img,i) in detailList2" :key="i" @click="ViewImage2(i)"
					style="width: 100px; height: 100px;" mode="aspectFill" :src="img">
				</image>
			</view>
				</view>
			</view>
		</view>
		<view class="pourlist">
			<view class="pourlist-item">
				<view class="pourlist-item-sta">
					<image v-if="chaobiaodengji == 0" src="../../../../static/pour/hege.png"></image>
					<image v-if="chaobiaodengji == 1" src="../../../../static/pour/lv1.png"></image>
					<image v-if="chaobiaodengji == 2" src="../../../../static/pour/lv2.png"></image>
					<image v-if="chaobiaodengji == 3" src="../../../../static/pour/lv3.png"></image>
				</view>
				<view class="pourlist-item-sta1">
					<image src="../../../../static/pour/handle10.png"></image>
				</view>
				<view class="pourlist-item-con">
					<view class="pourlist-item-con-name">
						指挥部核查：<span>{{dealInformation.headquartersApproval}}</span>
					</view>
					<view class="pourlist-item-con-name">
						核查结果：<span>{{dealInformation.headquartersResult}}</span>
					</view>
					<view class="pourlist-item-con-name">
						核查人：<span>{{dealInformation.headquartersPerson}}</span>
					</view>
					<view class="pourlist-item-con-name">
						核查时间：<span>{{dealInformation.headquartersHandleTime}}</span>
					</view>
				</view>
			</view>
		</view>
		<view style="height: 30rpx;"></view>
	</view>
</template>
<script>
	export default {
		data() {
			return {
				dealInformation: {},
				detailList1: [],
				detailList2: [],
				chaobiaodengji: undefined,
			}
		},
		onLoad(options) {
			this.dealInformation = JSON.parse(options.data).bhzCementOverHandler
			this.chaobiaodengji = JSON.parse(options.data).chaobiaodengji
			this.detailList1 = this.dealInformation.filePath2 ? this.dealInformation.filePath2.split(',') : []
			this.detailList2 = this.dealInformation.filePath ? this.dealInformation.filePath.split(',') : []
		},
		methods: {
			//查看图片
			ViewImage1(i) {
				uni.previewImage({
					urls: this.detailList1,
					current: i,
				});
			},
			ViewImage2(i) {
				uni.previewImage({
					urls: this.detailList2,
					current: i,
				});
			},
		},
	}
</script>

<style lang="scss" scoped>
	#dealDetail {
		background-color: #F3F5FE;

		.pourlist {
			width: 100%;
			margin-top: 30rpx;

			&-item {
				position: relative;
				width: 690rpx;
				// height: 462rpx;
				margin: 0 auto;
				padding-bottom: 30rpx;
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
	}
</style>