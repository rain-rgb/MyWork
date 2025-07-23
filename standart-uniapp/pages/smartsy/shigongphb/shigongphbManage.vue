<template>
	<view id="shigongphbmangage">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">配料单管理</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<!-- 		<view class="top">
			<view class="top-item">
				<view class="top-item-num" style="color: #FF233D;">0</view>
				<view class="top-item-name">未审核</view>
			</view>
			<view class="top-item">
				<view class="top-item-num" style="color: #FF8712;">10</view>
				<view class="top-item-name">配料单总数</view>
			</view>
		</view> -->
		<view class="pourlist">
			<view class="pourlist-item "></view>
		</view>
		<view class="pourlist">
			<view v-for="(item,index) in listdata" :key="index" @click="checkDetail(JSON.stringify(item))"
				class="pourlist-item">
				<view class="pourlist-item-con">
					<view class="pourlist-item-con-name">
						配料单：<span>{{ item.code }}</span>
					</view>
					<view class="pourlist-item-con-name">
						浇筑令：<span>{{ item.renwudan }}</span>
					</view>
					<view class="pourlist-item-con-name">
						浇筑时间：<span>{{ item.dattim }}</span>
					</view>
					<view class="pourlist-item-con-name">
						浇筑部位：<span>{{ item.conspos }}</span>
					</view>
					<view class="pourlist-item-con-name">
						计划方量：<span>{{ item.mete }}</span>
					</view>
					<view class="pourlist-item-con-name">
						强度等级：<span>{{ item.betlev_dictText }}</span>
					</view>
					<view class="pourlist-item-con-name">

					</view>
				</view>
				<view class="qrCode" @click.stop="amplifyQR(item.code)">
					<uqrcode ref="uqrcode" size="60" :canvas-id="'qrcode'+index" :value="item.code" :options="options">
					</uqrcode>
				</view>
			</view>
			<view style="height: 40upx;"></view>
		</view>
		<view class="add" @click="addHandle" v-has="'shigongphb:add'"></view>
		<view class="screen" v-if="show" @confirm="confirm">
			<view class="screen-modal">
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">设备名称：</view>
					<view class="screen-modal-item-input">
						<equipment sbtype="0" @choose="Changedevice"></equipment>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">施工部位：</view>
					<view class="screen-modal-item-input">
						<u--input placeholder="请输入施工部位" border="surround" v-model="conspos"></u--input>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">配料单号：</view>
					<view class="screen-modal-item-input">
						<u--input placeholder="请输入配料单号" border="surround" v-model="code"></u--input>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">浇筑令：</view>
					<view class="screen-modal-item-input">
						<u--input placeholder="请输入浇筑令" border="surround" v-model="renwudan"></u--input>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">开始时间：</view>
					<view class="screen-modal-item-input" @click="beginshow = true">
						<u--input placeholder="请选择开始时间" border="surround" v-model="begintimevalue" disabled
							suffixIcon="arrow-down"></u--input>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">结束时间：</view>
					<view class="screen-modal-item-input" @click="endshow = true">
						<u--input placeholder="请选择结束时间" border="surround" v-model="endtimevalue" disabled
							suffixIcon="arrow-down"></u--input>
					</view>
				</view>
				<view class="screen-modal-btn">
					<u-button text="取消" type="primary" plain @click="confirm"></u-button>
					<u-button type="primary" text="确认" @click="handleOk"></u-button>
				</view>
			</view>
		</view>
		<mx-date-picker :show="beginshow" :type="type" :value="value" :show-tips="true" :show-seconds="true"
			@confirm="confirmdate" @cancel="confirmdate" />
		<mx-date-picker :show="endshow" :type="type" :value="value" :show-tips="true" :show-seconds="true"
			@confirm="confirmend" @cancel="confirmend" />
		<u-modal :show="showQr" @confirm="confirmQr" confirmText="关闭">
			<uqrcode size="150" canvas-id="qrcode" :value="qrcode" :options="options"></uqrcode>
		</u-modal>
	</view>
</template>

<script>
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	import smartsyapi from "../../../api/smartsy.js"
	import equipment from '../../component/equipment/equipment.vue'
	export default {
		components: {
			MxDatePicker,
			equipment
		},
		data() {
			return {
				qrcode: '',
				options: {
					margin: 10
				},
				begintimevalue: '',
				endtimevalue: '',
				type: 'datetime',
				beginshow: false,
				endshow: false,
				show: false,
				showQr: false,
				listdata: [],
				pageNo: 1,
				shebeiNo: null,
				value: '',
				conspos: '',
				code: '',
				renwudan: '',
			}
		},
		onReachBottom() {
			uni.showNavigationBarLoading()
			this.pageNo++
			this.getloadlist()
			uni.hideNavigationBarLoading()
		},
		mounted() {
			this.getloadlist()
		},
		methods: {
			addHandle() {
				uni.navigateTo({
					url: '/pages/smartsy/shigongphb/shigongphbAdd'
				})
			},
			checkDetail(e) {
				//console.log(e, "e")
				uni.navigateTo({
					url: '/pages/smartsy/shigongphb/shigongphbDetail?json=' + e
				})
			},
			// 打开筛选框
			screen() {
				this.shebeiNo = null
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
				this.show = false
			},
			// 时间选择
			confirmdate(e) {
				this.begintimevalue = e.value
				this.beginshow = false
			},
			confirmend(e) {
				this.endtimevalue = e.value
				this.endshow = false
			},
			// 设备选择
			Changedevice(choosevalue) {
				this.shebeiNo = choosevalue
			},
			amplifyQR(val) {
				this.qrcode = val
				this.showQr = true
			},
			confirmQr() {
				this.showQr = false
			},
			// 获取配料单数据
			getloadlist() {
				let params = {
					column: 'id',
					order: 'desc',
					pageNo: this.pageNo,
					pageSize: 10,
					shebeibianhao: this.shebeiNo,
					dattim_begin: this.begintimevalue,
					dattim_end: this.endtimevalue,
					conspos: this.conspos ? `*${this.conspos}*` : '',
					code: this.code ? `*${this.code}*` : '',
					renwudan: this.renwudan ? `*${this.renwudan}*` : '',
				}
				// if (this.shebeiNo !== "") {
				// 	let params2 = {
				// 		shebeibianhao: this.shebeiNo
				// 	}
				// 	params = {
				// 		...params,
				// 		...params2
				// 	}
				// }
				smartsyapi.shigongphblist({
					params
				}).then(res => {
					//console.log(res, "配料单列表")
					if (res.data.result.records.length == 0) {
						uni.showToast({
							title: '没有更多数据了',
							icon: 'none'
						})
					}
					if (this.pageNo != 1) {
						this.listdata = this.listdata.concat(res.data.result.records)
					} else {
						this.listdata = res.data.result.records
					}
				}).catch(e => {
					console.log("请求错误", e)
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	#shigongphbmangage {
		width: 100%;
		background-color: #F3F5FE;

		.top {
			background-color: #FFFFFF;
			width: 690upx;
			height: 180upx;
			margin: 0 auto;
			margin-top: 30upx;
			margin-bottom: 30upx;
			color: #727B8E;
			display: flex;
			flex-wrap: wrap;
			justify-content: space-between;

			&-item {
				width: 310upx;
				height: 150upx;
				border-radius: 16upx;
				background-color: white;
				display: flex;
				flex-direction: column;
				justify-content: center;
				align-items: center;

				&-num {
					font-size: 44upx;
					// font-family: PingFang-SC-Medium;
				}

				&name {
					font-size: 28upx;
				}
			}
		}

		.pourlist {
			width: 100%;

			&-item {
				position: relative;
				width: 690upx;
				// height: 462upx;
				margin: 0 auto;
				margin-bottom: 30upx;
				border-radius: 16upx;
				background-color: white;

				.qrCode {
					width: 120rpx;
					height: 120rpx;
					position: absolute;
					bottom: 30rpx;
					right: 70rpx;
				}

				.red {
					background-image: url(../../../static/pour/sta1.png);
					background-repeat: no-repeat;
					background-size: 100% 100%;
				}

				.green {
					background-image: url(../../../static/pour/sta4.png);
					background-repeat: no-repeat;
					background-size: 100% 100%;
				}

				.orange {
					background-image: url(../../../static/pour/sta3.png);
					background-repeat: no-repeat;
					background-size: 100% 100%;
				}

				.grey {
					background-image: url(../../../static/pour/sta5.png);
					background-repeat: no-repeat;
					background-size: 100% 100%;
				}

				.blue {
					background-image: url(../../../static/pour/sta2.png);
					background-repeat: no-repeat;
					background-size: 100% 100%;
				}

				.purple {
					background-image: url(../../../static/pour/sta6.png);
					background-repeat: no-repeat;
					background-size: 100% 100%;
				}

				&-sta {
					position: absolute;
					right: 0px;
					width: 165upx;
					height: 165upx;

					view {
						position: absolute;
						transform: rotate(45deg);
						top: 40upx;
						left: 65upx;
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

		.add {
			position: fixed;
			bottom: 80upx;
			right: 0;
			width: 155upx;
			height: 155upx;
			background-image: url(../../../static/pour/add.png);
			background-repeat: no-repeat;
			background-size: 100% 100%;
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
				border-radius: 14upx;
				padding: 40upx;

				&-item {
					text-align: left;
					// width: 90%;
					margin-bottom: 20upx;

					&-name {
						color: #4C5971;
						font-size: 34upx;
						margin-bottom: 20upx;
					}

					// &-input {}
				}

				&-btn {
					display: flex;
				}
			}


		}

	}
</style>