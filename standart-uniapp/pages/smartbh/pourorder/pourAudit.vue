<template>
	<view id="pourmangage">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">浇筑令审核</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
<!-- 		<view class="top">
			<view class="top-item" @click="changes(0)">
				<view class="top-item-num" style="color: #FF233D;">{{sta.weishenhe==null?0:sta.weishenhe}}</view>
				<view class="top-item-name">未审核</view>
			</view>
			<view class="top-item" @click="changes(1)">
				<view class="top-item-num" style="color: #FF8712;">{{sta.shenhe==null?0:sta.shenhe}}</view>
				<view class="top-item-name">已审核未配料</view>
			</view>
			<view class="top-item" @click="changes(5)">
				<view class="top-item-num" style="color: #0FBF6E;">{{sta.wancheng==null?0:sta.wancheng}}</view>
				<view class="top-item-name">已完成</view>
			</view>
			<view class="top-item" @click="changes(2)">
				<view class="top-item-num" style="color: #8333FA;">{{sta.peiliao==null?0:sta.peiliao}}</view>
				<view class="top-item-name">已配料未生产</view>
			</view>
			<view class="top-item" @click="changes(3)">
				<view class="top-item-num" style="color: #387FFD;">{{sta.shenchan==null?0:sta.shenchan}}</view>
				<view class="top-item-name">生产中</view>
			</view>
			<view class="top-item" @click="changes(4)">
				<view class="top-item-num" style="color: #333333;">{{sta.zhihou==null?0:sta.zhihou}}</view>
				<view class="top-item-name">滞后任务</view>
			</view>
		</view> -->
		<view class="pourlist">
			<view v-for="(item,index) in listdata" :key="index" @click="checkDetail(item.rwdcode)"
				class="pourlist-item">
				<view class="pourlist-item-sta red" v-if="item.jzlsts == 0">
					<view style="color:#FF233D;">
						未审核
					</view>
				</view>
				<view class="pourlist-item-sta orange" v-if="item.jzlsts == 1">
					<view style="color: #FF8712;">
						未配料
					</view>
				</view>
				<view class="pourlist-item-sta green" v-if="item.jzlsts == 5">
					<view style="color: #0FBF6E;">
						已完成
					</view>
				</view>
				<view class="pourlist-item-sta purple" v-if="item.jzlsts == 2">
					<view style="color: #8333FA;">
						已配料
					</view>
				</view>
				<view class="pourlist-item-sta blue" v-if="item.jzlsts == 3">
					<view style="color: #387FFD;">
						生产中
					</view>
				</view>
				<view class="pourlist-item-sta grey" v-if="item.jzlsts == 4">
					<view style="color: #333333;">
						滞&nbsp;&nbsp;后
					</view>
				</view>
				<view class="pourlist-item-con">
					<view class="pourlist-item-con-name">
						工程名称：<span>{{ item.projname }}</span>
					</view>
					<view class="pourlist-item-con-name">
						开盘日期：<span>{{ item.begtim }}</span>
					</view>
					<view class="pourlist-item-con-name">
						部门名称：<span>{{ item.sysOrgCode_dictText }}</span>
					</view>
					<view class="pourlist-item-con-name">
						任务单号：<span>{{ item.rwdcode }}</span>
					</view>
					<view class="pourlist-item-con-name">
						浇筑部位：<span>{{ item.conspos }}</span>
					</view>
					<view class="pourlist-item-con-name">
						<u-line-progress height="18" :percentage="item.bfb"></u-line-progress>
					</view>
					<view class="pourlist-item-con-name">

					</view>
				</view>
			</view>
			<view style="height: 40upx;"></view>
		</view>
		<!-- <view class="add" @click="addHandle" v-has="'bhzrwd:add'"></view> -->
		<view class="screen" v-if="show" @confirm="confirm">
			<view class="screen-modal">
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">设备名称：</view>
					<view class="screen-modal-item-input">
						<equipment sbtype="0" @choose="Changedevice"></equipment>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">时间：</view>
					<view class="screen-modal-item-input" @click="dateshow = true">
						<u--input placeholder="请选择时间" border="surround" v-model="datetimevalue" disabled
							suffixIcon="arrow-down"></u--input>
					</view>
				</view>
				<view class="screen-modal-btn">
					<u-button text="取消" type="primary" plain @click="confirm"></u-button>
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
	import smartbhapi from "../../../api/smartbh.js"
	import equipment from '../../component/equipment/equipment.vue'
	export default {
		components: {
			MxDatePicker,
			equipment
		},
		data() {
			return {
				datetimevalue: '',
				type: 'datetime',
				dateshow: false,
				show: false,
				listdata: [],
				pageNo: 1,
				shebeiNo: '',
				value: '',
				sta: {
					peiliao: 0,
					wancheng: 0,
					shenchan: 0,
					zhihou: 0,
					weishenhe: 0,
					shenhe: 0
				},
				jzlsts: ''
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
			this.getstalist()
		},
		methods: {
			// addHandle() {
			// 	uni.navigateTo({
			// 		url: '/pages/smartbh/pourorder/pourAdd'
			// 	})
			// },
			checkDetail(e) {
				console.log(e, "e")
				uni.navigateTo({
					url: '/pages/smartbh/pourorder/pourDetail?code=' + e
				})
			},
			// 打开筛选框
			screen() {
				this.show = true
			},
			// 确认筛选
			handleOk() {
				this.getloadlist()
				this.getstalist()
				this.show = false
			},
			// 关闭筛选框
			confirm() {
				this.shebeiNo = ''
				this.datetimevalue = ''
				this.jzlsts = ''
				this.getloadlist()
				this.show = false
			},
			// 时间选择
			confirmdate(e) {
				this.datetimevalue = e.value
				this.dateshow = false
			},
			// 设备选择
			Changedevice(choosevalue) {
				this.shebeiNo = choosevalue
			},
			changes(e) {
				// this.jzlsts = e
				
				this.jzlsts===e ? this.jzlsts='' : this.jzlsts = e
				this.pageNo = 1
				this.listdata =[]
				this.getloadlist();
			},
			// 获取任务单数据
			getloadlist() {
				
				let params = {
					column: 'id',
					order: 'desc',
					pageNo: this.pageNo,
					pageSize: 10,
					shebeiNo: this.shebeiNo,
					starttim_begin: this.datetimevalue,
					jzlsts: 0,
				}
				smartbhapi.pourlist({
					params
				}).then(res => {
					// console.log(res, "任务单列表")
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
			},
			getstalist() {
				let params = {
					shebeiNo: this.shebeiNo,
					starttim_begin: this.datetimevalue
				}
				smartbhapi.bhzrwdsta({
					params
				}).then(res => {
					console.log(res)
					if (res.data.result != null) {
						this.sta = res.data.result
					} else {
						this.sta = {
							peiliao: 0,
							wancheng: 0,
							shenchan: 0,
							zhihou: 0,
							weishenhe: 0,
							shenhe: 0
						}
					}

				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	#pourmangage {
		width: 100%;
		background-color: #F3F5FE;

		.top {
			width: 690upx;
			height: 360upx;
			margin: 0 auto;
			margin-top: 30upx;
			color: #727B8E;
			display: flex;
			flex-wrap: wrap;
			justify-content: space-between;

			&-item {
				width: 210upx;
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
					margin-bottom: 40upx;

					&-name {
						color: #4C5971;
						font-size: 34upx;
						margin-bottom: 40upx;
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
