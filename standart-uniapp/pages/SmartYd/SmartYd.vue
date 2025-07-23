<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">智慧用电</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<view v-if="listData.length != 0">
			<view class="card" v-for="(item,index) in listData" :key="index">
				<!-- <navigator :url="'/pages/anquan/zhyongdian/zhydhistory?imei='+item.imei"> -->
				<view v-if="item.status==0" class="standard"
					style="color: orange;font-weight: bold;font-family: cursive;">未检测</view>
				<view v-if="item.status==1" class="standard"
					style="color: green;font-weight: bold;font-family: cursive;">合格</view>
				<view v-if="item.status==2" class="standard" style="color: red;font-weight: bold;font-family: cursive;">
					不合格</view>
				<view class="card_title">
					<view class="name">设备名称:<span>{{ item.imei_dictText }}</span></view>
				</view>
				<view class="box">
					<view class="box1">电流A:<span>{{item.electricitya}}</span></view>
					<view class="box1">电压A:<span>{{item.voltagea}}</span></view>
					<view class="box1">电能A:<span>{{item.energya}}</span></view>
					<view class="box1">频率A:<span>{{item.frequencya}}</span></view>
					<view class="box1">温度A:<span>{{item.tempa}}</span></view>
					<view class="box1" v-if="item.electricBoxA==0"
						style="color:orange;font-weight: bold;font-family: cursive;">未检测</view>
					<view class="box1" v-if="item.electricBoxA==1"
						style="color: green;font-weight: bold;font-family: cursive;">合格</view>
					<view class="box1" v-if="item.electricBoxA==2"
						style="color: red;font-weight: bold;font-family: cursive;">不合格</view>
				</view>
				<view class="box">
					<view class="box1">电流B:<span>{{item.electricityb}}</span></view>
					<view class="box1">电压B:<span>{{item.voltageb}}</span></view>
					<view class="box1">电能B:<span>{{item.energyb}}</span></view>
					<view class="box1">频率B:<span>{{item.frequencyb}}</span></view>
					<view class="box1">温度B:<span>{{item.tempb}}</span></view>
					<view class="box1" v-if="item.electricBoxB==0"
						style="color: orange;font-weight: bold;font-family: cursive;">未检测</view>
					<view class="box1" v-if="item.electricBoxB==1"
						style="color: green;font-weight: bold;font-family: cursive;">合格</view>
					<view class="box1" v-if="item.electricBoxB==2"
						style="color: red;font-weight: bold;font-family: cursive;">不合格</view>
				</view>
				<view class="box">
					<view class="box1">电流C:<span>{{item.electricityc}}</span></view>
					<view class="box1">电压C:<span>{{item.voltagec}}</span></view>
					<view class="box1">电能C:<span>{{item.energyc}}</span></view>
					<view class="box1">频率C:<span>{{item.frequencyc}}</span></view>
					<view class="box1">温度C:<span>{{item.tempc}}</span></view>
					<view class="box1" v-if="item.electricBoxC==0"
						style="color: orange;font-weight: bold;font-family: cursive;">未检测</view>
					<view class="box1" v-if="item.electricBoxC==1"
						style="color: green;font-weight: bold;font-family: cursive;">合格</view>
					<view class="box1" v-if="item.electricBoxC==2"
						style="color: red;font-weight: bold;font-family: cursive;">不合格</view>
				</view>
				<view class="content1">
					<view class="">温度N:<span>{{item.tempn}}</span></view>
					<view class="">时间:<span>{{ item.elecdate }}</span></view>
					<view class="">漏电流:<span>{{item.seepelectricity}}</span></view>
				</view>
				<!-- </navigator> -->
			</view>
		</view>
		<view class="">
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
		</view>
		<mx-date-picker :show="beginshow" :type="type" :value="value" :show-tips="true" :show-seconds="true"
			@confirm="confirmdate" @cancel="confirmdate" />

		<mx-date-picker :show="endshow" :type="type" :value="value" :show-tips="true" :show-seconds="true"
			@confirm="confirmend" @cancel="confirmend" />
	</view>
</template>

<script>
	import api from '@/api/api.js'
	import eq from '../component/equipment/equipment.vue'
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	export default {
		name: 'SmartYd',
		components: {
			eq,
			MxDatePicker
		},
		data() {
			return {
				sbtype: '17',
				index: -1,
				orgcode: null,
				listData: [],
				departTree: [],
				pageNo: 1,
				imei: null,
				begintime: '',
				endtime: '',
				show: false,
				begintimevalue: '',
				endtimevalue: '',
				sheibei: '',
				type: 'datetime',
				value: '',
				beginshow: false,
				endshow: false
			}
		},
		name: 'queryzhyd',
		onLoad() {
			this.listdata();
		},

		// 上拉加载更多
		onReachBottom() {
			uni.showNavigationBarLoading()
			this.pageNo++
			this.listdata()
			uni.hideNavigationBarLoading()
		},
		methods: {

			// 智慧用电信息列表
			listdata() {
				this.$http.get(`/zhydreal/deviceElectricRealdata/list`, {
					params: {
						column: 'id',
						order: 'desc',
						elecdate_begin: this.begintimevalue,
						elecdate_end: this.endtimevalue,
						sys_depart_orgcode: this.orgcode,
						pageNo: this.pageNo,
						pageSize: 10,
						imei: this.sheibei
					}
				}).then(res => {
					if (res.data.result.records.length == 0) {
						uni.showToast({
							title: '没有更多数据了',
							icon: 'none'
						})
					}
					if (this.pageNo != 1) {
						this.listData = this.listData.concat(res.data.result.records)
					} else {
						this.listData = res.data.result.records
					}
					this.listData.forEach(e => {
						// 判断A电箱状态
						if (e.vaStatus == 0 || e.faStatus == 0 || e.eaStatus == 0 || e.taStatus == 0) {
							e.electricBoxA = 0
						} else if (e.vaStatus == 1 && e.faStatus == 1 && e.eaStatus == 1 && e.taStatus ==
							1) {
							e.electricBoxA = 1
						} else {
							e.electricBoxA = 2
						}
						// 判断B电箱状态
						if (e.vbStatus == 0 || e.fbStatus == 0 || e.ebStatus == 0 || e.tbStatus == 0) {
							e.electricBoxB = 0
						} else if (e.vbStatus == 1 && e.fbStatus == 1 && e.ebStatus == 1 && e.tbStatus ==
							1) {
							e.electricBoxB = 1
						} else {
							e.electricBoxB = 2
						}
						// 判断C电箱状态
						if (e.vcStatus == 0 || e.fcStatus == 0 || e.ecStatus == 0 || e.tcStatus == 0) {
							e.electricBoxC = 0
						} else if (e.vcStatus == 1 && e.fcStatus == 1 && e.ecStatus == 1 && e.tcStatus ==
							1) {
							e.electricBoxC = 1
						} else {
							e.electricBoxC = 2
						}
					})
				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			//开始时间
			confirmdate(e) {
				this.begintimevalue = e.value
				this.beginshow = false
			},
			// 结束时间
			confirmend(e) {
				this.endtimevalue = e.value
				this.endshow = false
			},
			//设备名称
			changevalue(e) {
				console.log(e, 'dddd')
				this.sheibei = e
			},
			screen() {
				this.show = true
			},
			confirm() {
				this.listdata()
				this.begintimevalue = ''
				this.endtimevalue = ''
				this.show = false
			},
			cancel() {
				this.begintimevalue = ''
				this.endtimevalue = ''
				this.show = false
			},
		}
	}
</script>

<style lang="less" scoped>
	.wrap {
		background-color: #F2F5FE;
		margin: 10px 0px;
		border-radius: 5px;
		// color: #909399;
	}

	.card {
		width: 700upx;
		height: auto;
		border-radius: 10px;
		margin: 10px auto;
		background: #fff;
	}

	.standard {
		width: 50px;
		height: 20px;
		position: absolute;
		right: 8px;
		margin-top: 10px;
		transform: rotate(30deg);
		opacity: 0.8;
	}

	.card_title {
		padding-top: 10px;
		/* display: flex; */
		justify-content: right;
		padding-left: 10px;
	}

	.box {
		margin: 5px;
		background-color: #fff;
		display: flex;
		flex-wrap: wrap;
	}

	.box1 {
		padding: 5px 0px 0px 10px;
		flex: 1;
		width: 33.33%;
		min-width: 33.33%;
		max-width: 33.33%;
	}

	.content1 {
		display: flex;
		justify-content: space-around;
		margin-top: 5px;
		padding-bottom: 5px;
	}

	span {
		color: #000;
		font-size: 15px;
		font-weight: bold;
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
</style>
