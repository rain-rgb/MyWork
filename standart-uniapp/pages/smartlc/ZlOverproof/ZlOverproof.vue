<template>
	<view id="zlOverproof">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">超标处理</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<u-loading-page :loading="loading" loading-text="加载中..."></u-loading-page>
		<view class="pourlist">
			<view v-for="(item,index) in listdata" :key="index" @click="checkDetail(item)" class="pourlist-item">
				<view class="pourlist-item-sta">
					<image v-if="item.hege == 0" src="../../../static/pour/hege.png"></image>
					<image v-if="item.hege == 1" src="../../../static/pour/cjyj.png"></image>
					<image v-if="item.hege == 2" src="../../../static/pour/zjyj.png"></image>
					<image v-if="item.hege == 3" src="../../../static/pour/gjyj.png"></image>
				</view>
				<view class="pourlist-item-sta1">
					<image v-if="item.zhanglaYajiangOverHandler.overproofStatus == 0"
						src="../../../static/pour/handle4.png">
					</image>
					<image v-if="item.zhanglaYajiangOverHandler.overproofStatus == 10"
						src="../../../static/pour/handle3.png"></image>
					<image v-if="item.zhanglaYajiangOverHandler.overproofStatus == 40"
						src="../../../static/pour/handle2.png"></image>
					<image v-if="item.zhanglaYajiangOverHandler.overproofStatus == 30"
						src="../../../static/pour/handle8.png"></image>
					<image v-if="item.zhanglaYajiangOverHandler.overproofStatus == 20"
						src="../../../static/pour/handle1.png"></image>
					<!-- <image v-if="item.zhanglaYajiangOverHandler.overproofStatus == 50"
						src="../../../static/pour/handle10.png"></image> -->
					<image v-if="item.zhanglaYajiangOverHandler.overproofStatus == 60"
						src="../../../static/pour/handle11.png"></image>
				</view>
				<view class="pourlist-item-con">
					<view class="pourlist-item-con-name">
						工程部位：<span>{{ item.trZhanglaXxbList[0].gjbh }}</span>
					</view>
					<view class="pourlist-item-con-name">
						张拉梁型：<span>{{ item.trZhanglaXxbList[0].gjmc }}</span>
					</view>
					<view class="pourlist-item-con-name">
						设备名称：<span>{{ item.shebeibianhao }}</span>
					</view>
					<view class="pourlist-item-con-name">
						延伸量误差：<span>{{ item.sclper }}</span>
					</view>
					<view class="pourlist-item-con-name">
						施工时间：<span>{{ item.zhanglaMList[0].zlsj }}</span>
					</view>
					<view class="pourlist-item-con-name">
						超标原因：<span style="color: rgb(239, 11, 11);">{{ item.trZhanglaXxbList[0].overReason }}</span>
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
						<picker @change="Changedevice" :range="devicedata" range-key="sbname">
							<u--input placeholder="请选择设备名称" border="surround" v-model="devicename" disabled
								suffixIcon="arrow-down"></u--input>
						</picker>
					</view>
				</view>
				<view class="screen-modal-btn">
					<u-button text="取消" @click="confirm"></u-button>
					<u-button type="primary" text="确认" @click="handleOk"></u-button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	import smartbhapi from "@/api/smartbh.js"
	import dict from '@/pages/component/dict/dict.vue'
	export default {
		components: {
			dict,
			MxDatePicker
		},
		data() {
			return {
				loading: true,
				listdata: [],
				pageNo: 1,
				show: false,
				devicedata: [],
				devicename: '',
				shebeiNo: undefined,
			}
		},
		onLoad() {
			this.getdevicelist()
		},
		onShow(){
			this.pageNo = 1
			this.listdata = []
			this.getloadlist()
		},
		onReachBottom() {
			uni.showNavigationBarLoading()
			this.pageNo++
			this.getloadlist()
			uni.hideNavigationBarLoading()
		},
		methods: {
			// 获取设备列表
			getdevicelist() {
				let params = {
					sys_depart_orgcode: this.$store.getters.orgcode,
					sbtypes: '9'
				}
				smartbhapi.deviceList({
					params
				}).then(res => {
					this.devicedata = res.data.result
				})
			},
			// 设备选择
			Changedevice(e) {
				this.devicename = this.devicedata[e.detail.value].sbname
				this.shebeiNo = this.devicedata[e.detail.value].sbjno
			},
			checkDetail(e) {
				uni.navigateTo({
					url: '/pages/smartlc/ZlOverproof/ZlOverDetail?data=' + JSON.stringify(e)
				})
			},
			// 获取拌合站数据
			getloadlist() {
				this.loading = true
				let params = {
					column: 'id',
					order: 'desc',
					pageNo: this.pageNo,
					pageSize: 10,
					sys_depart_orgcode: this.$store.getters.orgcode,
					shebeibianhao: this.shebeiNo,
				}
				this.$http.get('/zhanglam/trZhanglaM/list6', {
					params
				}).then(res => {
					if (res.data.success) {
						if (res.data.result.records.length == 0) {
							uni.showToast({
								title: '没有更多数据了',
								icon: 'none'
							})
						} else {
							this.listdata = [...this.listdata, ...res.data.result.records]
						}
					}
				}).finally(() => {
					this.loading = false
				})
			},
			// 确认筛选
			handleOk() {
				this.pageNo = 1
				this.listdata = []
				this.getloadlist()
				this.show = false
			},
			// 打开筛选框
			screen() {
				this.show = true
			},
			// 关闭筛选框
			confirm() {
				this.show = false
			},
		}
	}
</script>

<style lang="scss" scoped>
	#zlOverproof {
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
					background-image: url(../../../static/pour/hege.png);
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
					margin-bottom: 30rpx;

					&-name {
						color: #4C5971;
						font-size: 30rpx;
						margin-bottom: 30rpx;
					}

				}

				&-btn {
					display: flex;
				}
			}
		}

	}
</style>