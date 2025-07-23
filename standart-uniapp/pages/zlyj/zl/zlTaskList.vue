<template>
	<view id="pourmangage">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">张拉任务单</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<!-- <view class="top">
			<view class="top-item" @click="changes(0)">
				<view class="top-item-num" style="color: #FF233D;">{{sta.unreviewed}}</view>
				<view class="top-item-name">未审核</view>
			</view>
			<view class="top-item" @click="changes(1)">
				<view class="top-item-num" style="color: #FF8712;">{{sta.reviewNotIngredients}}</view>
				<view class="top-item-name">已审核未配料</view>
			</view>
			<view class="top-item" @click="changes(5)">
				<view class="top-item-num" style="color: #0FBF6E;">{{sta.done}}</view>
				<view class="top-item-name">已完成</view>
			</view>
			<view class="top-item" @click="changes(3)">
				<view class="top-item-num" style="color: #8333FA;">{{sta.ingredientsNotProduced}}</view>
				<view class="top-item-name">已配料未生产</view>
			</view>
			<view class="top-item" @click="changes(4)">
				<view class="top-item-num" style="color: #387FFD;">{{sta.inProduction}}</view>
				<view class="top-item-name">生产中</view>
			</view>
			<view class="top-item" @click="changes(6)">
				<view class="top-item-num" style="color: #333333;">{{sta.lag}}</view>
				<view class="top-item-name">滞后任务</view>
			</view>
		</view> -->
		<view class="pourlist">
			<view v-for="(item,index) in listdata" :key="index" @click="checkDetail(item)"
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
				<view class="pourlist-item-sta purple" v-if="item.jzlsts == 3">
					<view style="color: #8333FA;">
						已配料
					</view>
				</view>
				<view class="pourlist-item-sta blue" v-if="item.jzlsts == 4">
					<view style="color: #387FFD;">
						生产中
					</view>
				</view>
				<view class="pourlist-item-sta grey" v-if="item.jzlsts == 6">
					<view style="color: #333333;">
						滞&nbsp;&nbsp;后
					</view>
				</view>
				<view class="pourlist-item-con">
					<view class="pourlist-item-con-name">
						任务单号：<span>{{ item.uuid }}</span>
					</view>
					<view class="pourlist-item-con-name">
						设备名称：<span>{{ item.shebeibianh_dictText }}</span>
					</view>
					<view class="pourlist-item-con-name">
						任务单状态：<span>
							<span style="color:red" v-if="item.wcstatus == '0'">未完成</span>
							<span style="color:green" v-if="item.wcstatus == '1'">已完成</span>
						</span>
					</view>
					<view class="pourlist-item-con-name">
						施工部位名称：<span>{{ item.sgbwname }}</span>
					</view>
					<view class="pourlist-item-con-name">
						创建时间：<span>{{ item.createTime }}</span>
					</view>
					<view class="pourlist-item-con-name">
						创建人：<span>{{ item.createBy_dictText }}</span>
					</view>
					<!-- <view class="pourlist-item-con-name">
						<u-line-progress height="18" :percentage="item.bfb"></u-line-progress>
					</view> -->
					<view class="pourlist-item-con-name">

					</view>
				</view>
			</view>
			<view style="height: 40upx;"></view>
		</view>
		<view class="add" @click="addHandle" v-has="'bhzrwd:add'"></view>
		<view class="screen" v-if="show" @confirm="confirm">
			<view class="screen-modal">
				<!-- <view class="screen-modal-item">
					<view class="screen-modal-item-name">设备名称：</view>
					<view class="screen-modal-item-input">
						<equipment sbtype="0" @choose="Changedevice"></equipment>
					</view>
				</view> -->
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">浇筑部位：</view>
					<view class="screen-modal-item-input">
						<u--input placeholder="请输入浇筑部位" border="surround" v-model="conspos"></u--input>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">开始时间：</view>
					<view class="screen-modal-item-input" @click="beginshow = true">
						<u--input placeholder="请选择时间" border="surround" v-model="begintimevalue" disabled
							suffixIcon="arrow-down"></u--input>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">结束时间：</view>
					<view class="screen-modal-item-input" @click="endshow = true">
						<u--input placeholder="请选择时间" border="surround" v-model="endtimevalue" disabled
							suffixIcon="arrow-down"></u--input>
					</view>
				</view>
				<view class="screen-modal-btn">
					<u-button style="width: 120px;" text="取消" type="primary" plain @click="confirm"></u-button>
					<u-button style="width: 120px;" type="primary" text="确认" @click="handleOk"></u-button>
				</view>
			</view>
		</view>
		<mx-date-picker :show="beginshow" :type="type" :value="value" :show-tips="true" :show-seconds="true"
			@confirm="confirmdate" @cancel="confirmdate" />
		<mx-date-picker :show="endshow" :type="type" :value="value" :show-tips="true" :show-seconds="true"
			@confirm="confirmend" @cancel="confirmend" />
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
				begintimevalue: '',
				endtimevalue: '',
				orgCode: '',
				conspos: '',
				type: 'datetime',
				beginshow: false,
				endshow: false,
				show: false,
				listdata: [],
				pageNo: 1,
				// shebeiNo: '',
				value: '',
				sta: {
					unreviewed: 0,
					reviewNotIngredients: 0,
					done: 0,
					ingredientsNotProduced: 0,
					inProduction: 0,
					lag: 0
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
		onPullDownRefresh() {
			this.pageNo = 1
			this.jzlsts = ''
			this.begintimevalue = ''
			this.endtimevalue = ''
			this.conspos = ''
			this.listdata = []
			this.getloadlist()
			this.getstalist()
			uni.stopPullDownRefresh();
		},
		mounted() {
			this.orgCode = this.$store.getters.orgcode
			console.log(this.orgCode,'this.orgCode-------------');
			this.getloadlist()
			this.getstalist()
		},
		methods: {
			addHandle() {
				uni.navigateTo({
					url: '/pages/zlyj/zl/zlAdd'
				})
			},
			checkDetail(e) {
				// uni.navigateTo({
				// 	url: '/pages/smartbh/pourorder/pourDetail?item=' + JSON.stringify(e)
				// })
			},
			// 打开筛选框
			screen() {
				this.show = true
			},
			// 确认筛选
			handleOk() {
				this.pageNo = 1
				this.listdata = []
				this.getloadlist()
				this.getstalist()
				this.show = false
			},
			// 关闭筛选框
			confirm() {
				// this.shebeiNo = ''
				// this.jzlsts = ''
				// this.getloadlist()
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
			// Changedevice(choosevalue) {
			// 	this.shebeiNo = choosevalue
			// },
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
					sys_depart_orgcode: this.orgCode,
					// shebeiNo: this.shebeiNo,
					dattim_begin: this.begintimevalue,
					dattim_end: this.endtimevalue,
					conspos: this.conspos,
					status: this.jzlsts
				}
				smartbhapi.zhanglaRenwudan({
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
					// shebeiNo: this.shebeiNo,
					orgCode: this.orgCode,
					dattim_begin: this.begintimevalue,
					dattim_end: this.endtimevalue,
					conspos: this.conspos,
				}
				smartbhapi.bhzrwdsta2({
					params
				}).then(res => {
					if (res.data.result != null) {
						this.sta = res.data.result
					} else {
						this.sta = {
							unreviewed: 0,
							reviewNotIngredients: 0,
							done: 0,
							ingredientsNotProduced: 0,
							inProduction: 0,
							lag: 0
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
			margin-top: 30upx;
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

				}

				&-btn {
					display: flex;
				}
			}


		}

	}
</style>
