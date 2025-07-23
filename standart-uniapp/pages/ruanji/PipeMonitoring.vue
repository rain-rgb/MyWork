<template>
	<view id="pipedata">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">预应力管桩设备监控</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<u-loading-page :loading="loading" loading-text="加载中..."></u-loading-page>
		<view class="pourlist">
			<view v-for="(item,index) in listdata" :key="index" @click="checkDetail(item)" class="pourlist-item">
				<view class="pourlist-item-sta">
					<image v-if="item.worksta == '上钻' || item.worksta == '下钻'" src="../../static/pour/zaixian.png">
					</image>
					<image v-else src="../../static/pour/yilixian.png"></image>
				</view>
				<view class="pourlist-item-con">
					<view class="pourlist-item-con-name">
						设备名称：<span>{{ item.shebeino_dictText }}</span>
					</view>
					<view class="pourlist-item-con-name">
						设备编号：<span>{{ item.shebeino }}</span>
					</view>
					<view class="pourlist-item-con-name">
						里程：<span>{{ item.mileage }}</span>
					</view>
					<view class="pourlist-item-con-name">
						桩号：<span>{{ item.pileno }}</span>
					</view>
					<view class="pourlist-item-con-name"></view>
				</view>
			</view>
			<view style="height: 40rpx;"></view>
		</view>
		<view class="screen" v-if="show">
			<view class="screen-modal">
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">设备名称：</view>
					<view class="screen-modal-item-input">
						<picker @change="Changedevice" :range="devicedata" range-key="shebeino_dictText">
							<u--input placeholder="请选择设备名称" border="surround" v-model="devicename" disabled
								suffixIcon="arrow-down"></u--input>
						</picker>
					</view>
				</view>
				<view class="screen-modal-btn">
					<u-button text="取消" @click="cancel"></u-button>
					<u-button type="primary" text="确认" @click="confirm"></u-button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue"
	export default {
		components: {
			MxDatePicker
		},
		data() {
			return {
				show: false,
				loading: true,
				listdata: [],
				//设备
				devicedata: [],
				devicename: '',
				shebeino: null,
			}
		},
		onLoad() {
			this.getdevicelist()
			this.getloadlist()
		},
		methods: {
			checkDetail(e) {
				uni.navigateTo({
					url: '/pages/ruanji/PipeMonitoringDetail?item=' + JSON.stringify(e)
				})
			},
			// 打开筛选框
			screen() {
				this.show = true
			},
			// 确认筛选
			confirm() {
				this.listdata = []
				this.getloadlist()
				this.show = false
			},
			// 关闭筛选框
			cancel() {
				this.show = false
			},
			// 设备选择
			Changedevice(e) {
				this.devicename = this.devicedata[e.detail.value].shebeino_dictText
				this.shebeino = this.devicedata[e.detail.value].shebeino
			},
			// 获取管桩设备
			getloadlist() {
				this.loading = true
				let params = {
					shebeino: this.shebeino,
				}
				this.$http.get('/devicepipepilerealdata/devicePipepileRealdata/list', {
					params
				}).then(res => {
					if (res.data.success) {
						this.listdata = res.data.result.records
					}
				}).finally(() => {
					this.loading = false
				})
			},
			// 获取设备列表
			getdevicelist() {
				let params = {
					sys_depart_orgcode: this.$store.getters.orgcode,
					sbtypes: '61'
				}
				this.$http.get('/devicepipepilerealdata/devicePipepileRealdata/list', {
					params
				}).then(res => {
					if (res.data.success) {
						this.devicedata = res.data.result.records
					}
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	#pipedata {
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

				.hege {
					background-image: url(../../static/pour/hege.png);
					background-repeat: no-repeat;
					background-size: 100% 100%;
				}

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

		.screen {
			position: fixed;
			background: rgba(000, 000, 000, .3);
			left: 0;
			top: 0;
			width: 100%;
			height: 100%;

			&-modal {
				background-color: #FFFFFF;
				position: absolute;
				top: 50%;
				left: (750rpx - 690rpx) / 2;
				width: 690rpx;
				transform: translateY(-50%);
				box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.1);
				border-radius: 12rpx;
				padding: 20rpx;

				&-item {
					text-align: left;
					// width: 90%;
					margin-bottom: 30rpx;

					&-name {
						color: #4C5971;
						font-size: 30rpx;
						margin-bottom: 30rpx;
					}

					&-input {}
				}

				&-btn {
					display: flex;
				}
			}
		}

	}
</style>