<template>
	<view id="mixdatadetail">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">超标处理</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<u-loading-page :loading="loading" loading-text="加载中..."></u-loading-page>
		<view class="pourlist">
			<view v-for="(item,index) in listdata" :key="index" @click="checkDetail(item)" class="pourlist-item">
				<view class="pourlist-item-sta">
					<image v-if="item.overLevel == 0" src="../../../static/pour/hege.png" mode=""></image>
					<image v-if="item.overLevel == 1" src="../../../static/pour/lv1.png" mode=""></image>
					<image v-if="item.overLevel == 2" src="../../../static/pour/lv2.png" mode=""></image>
					<image v-if="item.overLevel == 3" src="../../../static/pour/lv3.png" mode=""></image>
				</view>
				<view v-if="item.overLevel != 0 && item.overLevel != null" class="pourlist-item-sta1">
					<image v-if="item.overproofStatus == 0" src="../../../static/pour/handle4.png" mode=""></image>
					<image v-if="item.overproofStatus == 10" src="../../../static/pour/handle3.png" mode=""></image>
					<image v-if="item.overproofStatus == 20" src="../../../static/pour/handle1.png" mode=""></image>
					<image v-if="item.overproofStatus == 30" src="../../../static/pour/handle6.png" mode=""></image>
					<image v-if="item.overproofStatus == 40" src="../../../static/pour/handle2.png" mode=""></image>
					<image v-if="item.overproofStatus == 60" src="../../../static/pour/handle5.png" mode=""></image>
				</view>
				<view class="pourlist-item-con">
					<view class="pourlist-item-con-name">
						设备名称：<span>{{ item.shebeiNo }}</span>
					</view>
					<view class="pourlist-item-con-name">
						浇筑部位：<span>{{ item.poureLocation }}</span>
					</view>
					<view class="pourlist-item-con-name">
						最高超限率：<span>{{ item.additiveVariety }}</span>
					</view>
					<view class="pourlist-item-con-name">
						出料时间：<span>{{ item.productDatetime }}</span>
					</view>
					<view class="pourlist-item-con-name">
						方量：<span>{{ item.estimateNumber }}</span>
					</view>
					<view class="pourlist-item-con-name">
						强度等级：<span>{{ item.strengthRank }}</span>
					</view>
					<view class="pourlist-item-con-name"></view>
				</view>
			</view>
			<view style="height: 40upx;"></view>
		</view>
		<view class="screen" v-if="show">
			<view class="screen-modal">
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">设备名称：</view>
					<view class="screen-modal-item-input">
						<picker @change="Changedevice" :range="devicenames">
							<u--input placeholder="请选择设备名称" border="surround" v-model="devicename" disabled
								suffixIcon="arrow-down"></u--input>
						</picker>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">超标等级：</view>
					<view class="screen-modal-item-input">
						<picker @change="ChangeoverLV" :range="overLevels">
							<u--input placeholder="请选择超标等级" border="surround" v-model="overlevelname" disabled
								suffixIcon="arrow-down"></u--input>
						</picker>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">时间：</view>
					<view class="screen-modal-item-input" @click="dateshow = true">
						<u--input placeholder="请选择时间" border="surround" v-model="datetimevalue" disabled
							suffixIcon="arrow-down"></u--input>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">处置状态：</view>
					<view class="screen-modal-item-input">
						<dict dictCode="overproof_status" title="请选择处置状态" @choose="durationUnit">
						</dict>
					</view>
				</view>
				<view class="screen-modal-btn">
					<u-button text="取消" @click="confirm"></u-button>
					<u-button type="primary" text="确认" @click="handleOk"></u-button>
				</view>
			</view>
		</view>
		<mx-date-picker :show="dateshow" :type="type" :value="value" :show-tips="true" :show-seconds="true"
			@confirm="confirmdate" @cancel="confirmdate" />
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
				devicenames: [],
				devicename: '',
				dateshow: false,
				datetimevalue: '',
				type: 'rangetime',
				shebeiNo: undefined,
				overLevel: undefined,
				overLevels: ['合格', '初级超标', '中级超标', '高级超标'],
				overlevelname: '',
				value: '',
				productDatetime_begin: undefined,
				productDatetime_end: undefined,
				overStatus: undefined
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
			checkDetail(e) {
				uni.navigateTo({
					url: '/pages/smartbh/mix/mixdatadetail?item=' + JSON.stringify(e)
				})
			},
			// 打开筛选框
			screen() {
				this.overStatus = undefined
				this.show = true
			},
			// 确认筛选
			handleOk() {
				this.pageNo = 1
				this.listdata = []
				this.getloadlist()
				this.show = false
			},
			// 关闭筛选框
			confirm() {
				// this.getloadlist()
				this.show = false
			},
			// 时间选择
			confirmdate(e) {
				this.datetimevalue = e ? e.value.toString() : ''
				this.productDatetime_begin = e ? e.value[0] : undefined
				this.productDatetime_end = e ? e.value[1] : undefined
				this.dateshow = false
			},
			// 超标等级选择
			ChangeoverLV(e) {
				this.overlevelname = this.overLevels[e.detail.value]
				this.overLevel = e.detail.value
			},
			//处置状态
			durationUnit(val) {
				this.overStatus = val
			},
			// 设备选择
			Changedevice(e) {
				this.devicename = this.devicenames[e.detail.value]
				this.shebeiNo = this.devicedata[e.detail.value].sbjno
			},
			// 获取拌合站数据
			getloadlist() {
				this.loading = true
				let params = {
					column: 'id',
					order: 'desc',
					pageNo: this.pageNo,
					pageSize: 10,
					shebeiNo: this.shebeiNo,
					overLevel: this.overLevel,
					productDatetime_begin: this.productDatetime_begin,
					productDatetime_end: this.productDatetime_end,
					overproofStatus: this.overStatus,
				}
				smartbhapi.bhzOverHandle({
					params
				}).then(res => {
					if(res.data.success){
						if (res.data.result.records.length == 0) {
							uni.showToast({
								title: '没有更多数据了',
								icon: 'none'
							})
						}else{
							this.listdata = [...this.listdata, ...res.data.result.records]
						}
					}
				}).finally(() => {
					this.loading = false
				})
			},
			// 获取设备列表
			getdevicelist() {
				let params = {
					sbtypes: 0
				}
				smartbhapi.deviceList({
					params
				}).then(res => {
					this.devicedata = res.data.result
					this.devicenames = []
					this.devicedata.forEach(item => {
						this.devicenames.push(item.sbname)
					})
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	#mixdatadetail {
		background-color: #F3F5FE;

		.pourlist {
			width: 100%;
			margin-top: 30upx;

			&-item {
				position: relative;
				width: 690upx;
				// height: 462upx;
				margin: 0 auto;
				margin-bottom: 30upx;
				border-radius: 16upx;
				background-color: white;

				.hege {
					background-image: url(../../../static/pour/hege.png);
					background-repeat: no-repeat;
					background-size: 100% 100%;
				}

				&-sta {
					position: absolute;
					right: 0px;
					width: 165upx;
					height: 165upx;

					image {
						width: 100%;
						height: 100%;
					}
				}

				&-sta1 {
					position: absolute;
					right: 20upx;
					bottom: 20upx;
					width: 165upx;
					height: 165upx;

					image {
						width: 100%;
						height: 100%;
					}
				}

				&-con {
					width: 610upx;
					// height: 336upx;
					margin: 0 auto;

					// background-color: red;
					&-name {
						padding-top: 30upx;
						color: #9299A8;
						font-size: 28upx;

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
				left: (750upx - 690upx) / 2;
				width: 690upx;
				transform: translateY(-50%);
				box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.1);
				border-radius: 12upx;
				padding: 20upx;

				&-item {
					text-align: left;
					// width: 90%;
					margin-bottom: 30upx;

					&-name {
						color: #4C5971;
						font-size: 30upx;
						margin-bottom: 30upx;
					}
				}

				&-btn {
					display: flex;
				}
			}
		}

	}
</style>