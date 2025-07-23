<template>
	<view id="pipedata">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">预应力管桩生产记录</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<u-loading-page :loading="loading" loading-text="加载中..."></u-loading-page>
		<view class="top">
			<view class="top-content">
				<view class="num yellow">{{ this.pileNum.zjs || 0 }}根</view>
				<view class="title">总桩数</view>
			</view>
			<view class="top-content">
				<view class="num blue">{{ this.pileNum.sbs || 0 }}台</view>
				<view class="title">总台数</view>
			</view>
			<view class="top-content">
				<view class="num green">{{ this.pileNum.zms ? parseFloat(this.pileNum.zms).toFixed(2) : 0 }}米</view>
				<view class="title">总桩长</view>
			</view>
		</view>
		<view class="pourlist">
			<view v-for="(item,index) in listdata" :key="index" @click="checkDetail(item)" class="pourlist-item">
				<view class="pourlist-item-sta">
					<image v-if="item.chaobiaodengji === 0 && item.istongji === 1" src="../../static/pour/hege.png">
					</image>
					<image v-if="item.chaobiaodengji === 1 && item.istongji === 1" src="../../static/pour/bhege.png">
					</image>
					<image v-if="item.chaobiaodengji === 0 && item.istongji === 0" src="../../static/pour/wjc.png">
					</image>
					<image v-if="item.istongji === 40" src="../../static/pour/jcyc.png"></image>
				</view>
				<view class="pourlist-item-con">
					<view class="pourlist-item-con-name">
						设备名称：<span>{{ item.shebeino_dictText }}</span>
					</view>
					<view class="pourlist-item-con-name">
						里程：<span>{{ item.pileMileage }}</span>
					</view>
					<view class="pourlist-item-con-name">
						桩号：<span>{{ item.pileNo }}</span>
					</view>
					<view class="pourlist-item-con-name">
						设计桩长：<span>{{ item.pileDesigndep }}</span>
					</view>
					<view class="pourlist-item-con-name">
						施工长度(m)：<span>{{ parseFloat(item.pileRealdep).toFixed(2) }}</span>
					</view>
					<view class="pourlist-item-con-name">
						开始时间：<span>{{ item.pileStarttime }}</span>
					</view>
					<view class="pourlist-item-con-name">
						成桩时间(s)：<span>{{ item.pileWorktime }}</span>
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
					<view class="screen-modal-item-name">桩号：</view>
					<view class="screen-modal-item-input">
						<u--input placeholder="请输入桩号" border="surround" v-model="pileNo"></u--input>
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
	import smartbhapi from "../../api/smartbh.js"
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
				pileTime_begin: null,
				pileTime_end: null,
				//设备
				devicedata: [],
				devicename: '',
				shebeino: null,
				//桩号
				pileNo: '',
				pileNum: {}
			}
		},
		onLoad() {
			this.getdevicelist()
			this.getpilelist()
			this.getloadlist()
		},
		onReachBottom() {
			this.pageNo++
			this.getloadlist()
		},
		methods: {
			checkDetail(e) {
				uni.navigateTo({
					url: '/pages/ruanji/PipeProductionDetail?item=' + encodeURIComponent(JSON.stringify(e))
				})
			},
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
					this.pileTime_begin = e.value[0]
					this.pileTime_end = e.value[1]
				}
				this.dateshow = false
			},
			// 设备选择
			Changedevice(e) {
				this.devicename = this.devicedata[e.detail.value].sbname
				this.shebeino = this.devicedata[e.detail.value].sbjno
			},
			// 获取管桩生产记录
			getloadlist() {
				this.loading = true
				let params = {
					column: 'id',
					order: 'desc',
					pageNo: this.pageNo,
					pageSize: 10,
					sys_depart_orgcode: this.$store.getters.orgcode,
					pileTime_begin: this.pileTime_begin,
					pileTime_end: this.pileTime_end,
					shebeino: this.shebeino,
					pileNo: this.pileNo,
				}
				this.$http.get('/devicepipepilehistorydataone/devicePipepileHistorydataOne/list', {
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
			// 获取桩数
			getpilelist() {
				this.$http.get('/devicepipepilehistorydataone/devicePipepileHistorydataOne/listYm').then(res => {
					if (res.data.success) {
						this.pileNum = res.data.result[0]
					}
				})
			},
			// 获取设备列表
			getdevicelist() {
				let params = {
					sys_depart_orgcode: this.$store.getters.orgcode,
					sbtypes: '61'
				}
				smartbhapi.deviceList({
					params
				}).then(res => {
					this.devicedata = res.data.result
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	#pipedata {
		background-color: #F3F5FE;

		.top {
			width: 690rpx;
			margin: 15px auto 0;
			display: flex;
			color: #727B8E;
			justify-content: space-between;

			&-content {
				width: 30%;
				height: 140rpx;
				display: flex;
				justify-content: space-evenly;
				align-items: center;
				flex-direction: column;
				border-radius: 16rpx;
				background-color: white;

				.num {
					font-size: 34rpx;
					word-break: break-word;
				}

				.yellow {
					color: rgba(255, 164, 48, 1)
				}

				.blue {
					color: rgba(12, 138, 255, 1)
				}

				.green {
					color: rgba(78, 201, 143, 1)
				}
			}
		}

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