<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">订单管理</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<view>
			<u-loading-page :loading="loading" loading-text="加载中请稍后..."></u-loading-page>
			<view class="section" v-for="(item,index) in listdata" :key="index">
				<navigator :url="'/pages/smartlc/orderTracking/orderlist?item='+JSON.stringify(item)">
					<view class="section-text">
						<view>订单编号:<span>{{item.orderno !==null?item.orderno:'暂无数据'}}</span></view>
						<view>梁型:<span>{{item.beamType_dictText !==null?item.beamType_dictText:'暂无数据'}}</span></view>
						<view>施工部位:<span>{{item.constructionSite !==null?item.constructionSite:'暂无数据'}}</span></view>
						<view>交付日期:<span>{{item.deliveryDate !==null?item.deliveryDate:'暂无数据'}}</span></view>
						<view>梁数量:<span>{{item.beamNum !==null?item.beamNum:'暂无数据'}}</span></view>
						<view>梁场:<span>{{item.shebeino_dictText !==null?item.shebeino_dictText:'暂无数据'}}</span></view>
						<view>组织机构:<span>{{item.sysOrgCode_dictText !==null?item.sysOrgCode_dictText:'暂无数据'}}</span>
						</view>
						<view>制梁要求:<span>{{item.remark !==null?item.remark:'暂无数据'}}</span></view>
						<view>创建人:<span>{{item.createBy !==null?item.createBy:'暂无数据'}}</span></view>
						<view>创建日期:<span>{{item.createTime !==null?item.createTime:'暂无数据'}}</span></view>
						<view>更新人:<span>{{item.updateBy !==null?item.updateBy:"暂无数据"}}</span></view>
						<view>更新日期:<span>{{item.updateTime !==null?item.updateTime:'暂无数据'}}</span></view>
						<view>
							订单接收人:<span>{{item.orderRecipient_dictText !==null?item.orderRecipient_dictText:'暂无数据'}}</span>
						</view>
						<view>订单进度<u-line-progress :percentage="item.orderProgress" activeColor="#ff0000">
							</u-line-progress>
						</view>
						<!-- <view>顶板宽:<span>{{item.roofwid !==null?item.roofwid:'暂无数据'}}</span></view>
				<view>底板宽:<span>{{item.bottomwid !==null?item.bottomwid:"暂无数据"}}</span></view>
				<view>梁高:<span>{{item.beamhig !==null?item.beamhig:"暂无数据"}}</span></view> -->
					</view>
				</navigator>
			</view>
		</view>

		<view>
			<u-modal :show="show" showConfirmButton showCancelButton @confirm="confirm" @cancel="cancel">
				<view class="select-box">
					<view class="Task">
						设备名称:
						<view class="Task-input">
							<!-- <u--input placeholder="请选择设备名称" border="surround" v-model="Taskno" @change="change">
							</u--input> -->
							<eq :sbtype="sbtype" @choose="changevalue"></eq>
						</view>
					</view>
					<view class="Task">
						开始时间:
						<view class="Task-input">
							<!-- <view class="screen-modal-item-name">时间：</view> -->
							<view class="screen-modal-item-input" @click="beginshow = true">
								<u--input placeholder="请选择开始时间" border="surround" v-model="begintimevalue" disabled
									suffixIcon="arrow-down"></u--input>
							</view>
						</view>
					</view>
					<view class="Task">
						结束时间:
						<view class="Task-input">
							<!-- <view class="screen-modal-item-name">时间：</view> -->
							<view class="screen-modal-item-input" @click="endshow = true">
								<u--input placeholder="请选择结束时间" border="surround" v-model="endtimevalue" disabled
									suffixIcon="arrow-down"></u--input>
							</view>
						</view>
					</view>
				</view>
			</u-modal>
		</view>
		<mx-date-picker :show="beginshow" :type="type" :value="value" :show-tips="true" :show-seconds="true"
			@confirm="confirmdate" @cancel="confirmdate" />

		<mx-date-picker :show="endshow" :type="type" :value="value" :show-tips="true" :show-seconds="true"
			@confirm="confirmend" @cancel="confirmend" />
	</view>
</template>

<script>
	import eq from '../../component/equipment/equipment.vue'
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	export default {
		name: 'orderTracking',
		components: {
			eq,
			MxDatePicker
		},
		data() {
			return {
				loading: true,
				listdata: [],
				pageNo: 1,
				sbtype: '41',
				Number: '',
				show: false,
				beginshow: false,
				type: 'datetime',
				value: '',
				begintimevalue: '',
				endtimevalue: '',
				endshow: false,
				status: "",
				code: '',
			}
		},
		onLoad(options) {
			this.status = options.statuuss
			this.code = options.code
			console.log(options.shcode)
			this.selfhelpdata()
		},
		onReachBottom() {
			uni.showNavigationBarLoading()
			this.pageNo++
			this.selfhelpdata()
			uni.hideNavigationBarLoading()
		},
		// onShow() {
		// 	this.selfhelpdata()
		// },
		created() {
			this.selfhelpdata()
		},
		methods: {
			selfhelpdata() {
				this.$http.get(`/beamorder/beamOrder/list`, {
					params: {
						order: 'desc',
						column: 'id',
						pageNo: this.pageNo,
						pageSize: 10,
						statuuss: this.status,
						orderno: this.code
					}
				}).then(res => {
					console.log(res, '自助下单')
					this.listdata = res.data.result.records
					this.loading = false
					// if (res.data.result.records.length ==0) {
					// 	uni.showToast({
					// 		title: '没有更多数据',
					// 		icon: "loading"
					// 	})
					// }
					if (this.pageNo != 1) {
						this.listdata = this.listdata.concat(res.data.result.records)
					} else {
						this.listdata = res.data.result.records
					}
				})
			},
			select() {
				this.$http.get(`/beamorder/beamOrder/list`, {
					params: {
						shebeino: this.Number,
						createTime_begin: this.begintimevalue,
						createTime_end: this.endtimevalue
					}
				}).then(res => {
					if (res.data.result.records.length == 0) {
						this.listdata = [];
						uni.showToast({
							title: '暂无数据!',
							icon: "none"
						})
					} else {
						this.listdata = res.data.result.records;
					}
				})
			},
			changevalue(choosevalue) {
				this.Number = choosevalue
			},
			screen() {
				this.show = true
			},
			confirm() {
				this.select();
				this.show = false
				this.Number = ''
				this.begintimevalue = ''
				this.endtimevalue = ''
			},
			cancel() {
				this.show = false
				this.Number = ''
				this.begintimevalue = ''
				this.endtimevalue = ''
			},
			confirmdate(e) {
				this.begintimevalue = e.value
				this.beginshow = false
			},
			confirmend(e) {
				this.endtimevalue = e.value
				this.endshow = false
			},
		}
	}
</script>

<style lang="less" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FE;

		.section {
			width: 700upx;
			height: auto;
			background-color: #fff;
			border-radius: 10px;
			margin: 0 auto;

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
					padding: 0 15upx;
				}
			}
		}

		.select-box {
			width: 100%;
			height: auto;

			.Task {
				width: 100%;
				height: auto;

				.Task-input {
					width: 100%;
					height: 70upx;
					margin: 10px 0;
				}
			}
		}
	}
</style>
