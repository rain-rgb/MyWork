<template>
	<view id="pipedata">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">物联网指令单</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<u-loading-page :loading="loading" loading-text="加载中..."></u-loading-page>
		<view class="pourlist">
			<view v-for="(item,index) in listdata" :key="index" @click="instructDetail(item.id,item)" class="pourlist-item">
				<view class="pourlist-item-sta">
					<!-- <image v-if="item.status == 0" src="../../../static/pour/wqs.png">
					</image> -->
					<!-- <image v-if="item.status == 1" src="../../../static/pour/zhjqs.png">
					</image>
					<image v-if="item.status == 2" src="../../../static/pour/zjqs.png">
					</image>
					<image v-if="item.status == 3" src="../../../static/pour/sgdwqs.png">
					</image> -->
				</view>
				<view class="pourlist-item-con">
					<view class="pourlist-item-con-name">
						编号：<span>{{ item.batchNo }}</span>
					</view>
					<view class="pourlist-item-con-name">
						标段：<span>{{ item.departName }}</span>
					</view>
					<view class="pourlist-item-con-name">
						设备编号：<span>{{ item.shebeiNo_dictText }}</span>
					</view>
					<view class="pourlist-item-con-name">
						出料时间：<span>{{ item.productDatetime }}</span>
					</view>
					<view class="pourlist-item-con-name">
						超标原因：<span>{{ item.overReason }}</span>
					</view>
					<view class="pourlist-item-con-name">
						签收人：<span>{{ item.receiver }}</span>
					</view>
					<view class="pourlist-item-con-name">
						预计处置时间：<span>{{ item.expectedDatetime }}</span>
					</view>
					<view class="pourlist-item-con-name">
						监理单位：<span>{{ item.supervisionUnit }}</span>
					</view>
					<view class="pourlist-item-con-name">
						施工单位：<span>{{ item.constructionUnit }}</span>
					</view>
					<view class="pourlist-item-con-name">
						保存时间：<span>{{ item.saveDatetime }}</span>
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
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">时间范围：</view>
					<view class="screen-modal-item-input" @click="dateshow = true">
						<u--input placeholder="请选择时间" border="surround" v-model="datetimevalue" disabled
							suffixIcon="arrow-down"></u--input>
					</view>
				</view>
				<view class="screen-modal-btn">
					<u-button text="取消" @click="cancel"></u-button>
					<u-button type="primary" text="确认" @click="confirm"></u-button>
				</view>
			</view>
		</view>
		<mx-date-picker :show="dateshow" :type="type" :show-tips="true" :show-seconds="true" @confirm="confirmdate"
			@cancel="confirmdate" />
	</view>
</template>

<script>
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue"
	import smartbhapi from "@/api/smartbh.js"
	export default {
		components: {
			MxDatePicker
		},
		data() {
			return {
				show: false,
				loading: true,
				listdata: [],
				pageNo: 1,
				//时间
				dateshow: false,
				datetimevalue: '',
				type: 'rangetime',
				productDatetime_begin: null,
				productDatetime_end: null,
				//设备
				devicedata: [],
				devicename: '',
				shebeino: null,
				pathUrl:'',
			}
		},
		onLoad() {
			this.getdevicelist()
			this.getloadlist()
		},
		onReachBottom() {
			this.pageNo++
			this.getloadlist()
		},
		methods: {
			// 打开筛选框
			screen() {
				this.show = true
			},
			// 确认筛选
			confirm() {
				this.pageNo = 1
				this.listdata = []
				this.getloadlist()
				this.show = false
			},
			// 关闭筛选框
			cancel() {
				this.show = false
			},
			// 时间选择
			confirmdate(e) {
				if (e) {
					this.datetimevalue = e.value.join()
					this.productDatetime_begin = e.value[0]
					this.productDatetime_end = e.value[1]
				}
				this.dateshow = false
			},
			// 设备选择
			Changedevice(e) {
				this.devicename = this.devicedata[e.detail.value].sbname
				this.shebeino = this.devicedata[e.detail.value].sbjno
			},
			instructDetail(id,item) {
				this.getpathUrl(item);
			},
			// 获取物联网指令单列表
			getloadlist() {
				this.loading = true
				let params = {
					column: 'id',
					order: 'desc',
					pageNo: this.pageNo,
					pageSize: 10,
					sys_depart_orgcode: this.$store.getters.orgcode,
					productDatetime_begin: this.productDatetime_begin,
					productDatetime_end: this.productDatetime_end,
					shebeiNo: this.shebeino,
				}
				this.$http.get('/bhzSupervisionOrder/bhzSupervisionOrder/list1', {
					params
				}).then(res => {
					if (res.data.result.records.length == 0) {
						uni.showToast({
							title: '没有更多数据了',
							icon: 'none'
						})
					} else {
						this.listdata = [...this.listdata, ...res.data.result.records]
					}
				}).finally(() => {
					this.loading = false
				})
			},
			// 获取设备列表
			getdevicelist() {
				let params = {
					sys_depart_orgcode: this.$store.getters.orgcode,
					sbtypes: '0'
				}
				smartbhapi.deviceList({
					params
				}).then(res => {
					this.devicedata = res.data.result
				})
			},
			// 获取跳转地址
			getpathUrl(item) {
				let params = {
					status: item.status,
					batchNo: item.batchNo
				}
				this.$http.get('/bhzSupervisionOrder/bhzSupervisionOrder/sign', {
					params
				}).then(res => {
					this.pathUrl = res.data.result;
					uni.navigateTo({
						url:`/pages/WebView/Iroadw?url=${this.pathUrl}`,
						// url:`/pages/WebView/Iroadw?url=http://47.111.9.185:9180/sign?viewToken=2b437cda-0047-4818-9a02-76af752ec190&themeColor=%232489F2&lang=zh_CN`,
						// url:`/pages/WebView/Iroadw?url=http://47.96.161.157/bhz/bhzSupervisionOrder/InstructModelLone?id=${id}`,
					})
					// console.log(this.pathUrl,res,'this.pathUrl this.pathUrl');
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

				}

				&-btn {
					display: flex;
				}
			}
		}

	}
</style>