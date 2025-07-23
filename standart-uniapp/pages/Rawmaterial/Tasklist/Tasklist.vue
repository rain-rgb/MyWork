<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">送货任务单</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<u-loading-page :loading="loading" loading-text="加载中请稍后..."></u-loading-page>
		<view v-for="(item,index) in rwdlist" :key="index">
			<view class="wrap-all">
				<view class="section" @click="select(item)">
					<view class="box1">
						<!-- <view>所属部门:<span>{{item.sysorgcode_dictText}}</span></view> -->
						<view>材料类型:<span>{{item.cailiaotype_dictText}}</span></view>
						<view>规格型号:<span>{{item.ggxh}}</span></view>
						<view>数量（吨）:<span>{{item.shuliang}}</span></view>
						<view>目的地:<span>{{item.mudidi_dictText}}</span></view>
						<view>要求送达时间:<span>{{item.endtime}}</span></view>
						<view>供应商:<span>{{item.gysguid_dictText}}</span></view>
						<view>送货任务单号:<span>{{item.shrwd}}</span></view>
						<view>状态:<span
								:style="{color:item.status==0?'orange':item.status==1?'#efef11':item.status==2?'blue':item.status==3?'green':item.status==4?'red':''}">{{item.status_dictText}}</span>
						</view>
					</view>
					<!-- <view class="box2">状态:<span
							:style="item.status ==0?'color:red':item.status==1?'color:green':item.status==2?'color:orange':item.status==3?'color:blue':''">{{item.status_dictText}}</span>
					</view> -->
				</view>
				<view class="box3">
					<u-button @click="handlecheck(item.id)" v-show="item.status ==0" v-has="'user:checks'"
						style="width: 200upx;background-color:#2c7dff;color:#fff;margin: 0 ;border-radius: 10px;"
						text="确认">
					</u-button>
				</view>
			</view>
		</view>
		<view>
			<u-modal :show="show" showConfirmButton showCancelButton @confirm="confirm" @cancel="cancel">
				<view class="select-box">
					<view class="Task">
						材料类型:
						<view class="Task-input">
							<dict dictCode="nodeType" title="请选择材料类型" @choose="Choose"></dict>
						</view>
					</view>
					<view class="Task">
						状态:
						<view class="Task-input">
							<dict dictCode="shStatus" title="请选择状态" @choose="Choose1"></dict>
						</view>
					</view>
				</view>
			</u-modal>
		</view>
		<view>
			<u-modal :show="detailsshow" showCancelButton :title="mantitle" @confirm="handleconfirm"
				@cancel="handlecancel">
				<view class="modal">
					<!-- <view class="modal-name">预计全部到货时间：</view> -->
					<!-- <view class="modal-input"> -->
					<!-- <u--input type="text" v-model="" placeholder="请选择预计全部到货时间" /> -->
					<view class="Task">
						预计全部到货时间:
						<view class="Task-input">
							<!-- <view class="screen-modal-item-name">时间：</view> -->
							<view class="screen-modal-item-input" @click="beginshow = true">
								<u--input placeholder="请选择预计全部到货时间" border="surround" v-model="endtime1" disabled
									suffixIcon="arrow-down"></u--input>
							</view>
						</view>
					</view>
					<!-- </view> -->
					<view class="modal-name">车次数：</view>
					<view class="modal-input">
						<u--input type="text" v-model="cartimes" placeholder="请输入车次数" />
					</view>
					<view class="modal-name">批次：</view>
					<view class="modal-input">
						<u--input type="text" v-model="picihao" placeholder="请输入批次" />
					</view>
				</view>
			</u-modal>

			<mx-date-picker :show="beginshow" :type="type" :value="value" :show-tips="true" :show-seconds="true"
				@confirm="confirmdate" @cancel="confirmdate" />
		</view>
	</view>
</template>

<script>
	import dict from '../../component/dict/dict.vue'
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	export default {
		components: {
			dict,
			MxDatePicker
		},
		data() {
			return {
				rwdlist: [],
				pageNo: 1,
				loading: true,
				show: false,
				rwdstatus: '',
				cailiaoType: '',
				detailsshow: false,
				mantitle: '详情确认',
				endtime1: '', //预计全部到货时间
				cartimes: '', //车次数
				picihao: '', // 批次
				beginshow: false,
				value: '',
				type: 'datetime',
				id: ''
			}
		},
		created() {
			this.shrwdlist()
		},
		onReachBottom() {
			uni.showNavigationBarLoading()
			this.pageNo++
			this.shrwdlist()
			uni.hideNavigationBarLoading()
		},
		methods: {
			shrwdlist() {
				let params = {
					order: 'desc',
					column: 'id',
					pageNo: this.pageNo,
					pageSize: 10,
					status: this.rwdstatus,
					cailiaotype: this.cailiaoType
				}
				this.$http.get(`/syshrwd/syshrwd/list`, {
					params
				}).then(res => {
					this.loading = false
					if (res.data.result.records.length == 0) {
						uni.showToast({
							title: '没有更多数据啦',
							icon: "loading"
						})
					}
					if (this.pageNo != 1) {
						this.rwdlist = this.rwdlist.concat(res.data.result.records)
					} else {
						this.rwdlist = res.data.result.records
					}
				})
			},
			// 筛选框
			Choose(e) {
				this.cailiaoType = e
			},
			Choose1(e) {
				this.rwdstatus = e
			},

			select(item) {
				if (item.status != 0 && item.status != 4) {
					uni.navigateTo({
						url: '/pages/MaterialProcurement/DeliveryManagement/DeliveryManagement?item=' + JSON.stringify(item)
					})
				}
			},
			screen() {
				this.show = true
			},
			confirm() {
				this.pageNo = 1
				this.shrwdlist()
				this.show = false
				this.rwdstatus = ''
				this.cailiaoType = ''
			},
			cancel() {
				this.show = false
				this.rwdstatus = ''
				this.cailiaoType = ''
			},
			handlecheck(e) {
				this.id = e
				this.detailsshow = true
			},
			confirmdate(e) {
				this.endtime1 = e.value
				this.beginshow = false
			},
			handleconfirm() {
				this.detailsshow = false
				this.cartimes
				let params = {
					cartimes: this.cartimes,
					picihao: this.picihao,
					endtime1: this.endtime1,
					id: this.id
				}
				this.$http.put(`/syshrwd/syshrwd/cjedit`, params).then(res => {
					console.log(res)
					if (res.data.success) {
						uni.showToast({
							title: res.data.result,
							icon: 'success'
						})
						this.shrwdlist()
					}
				})
			},
			handlecancel() {
				this.detailsshow = false
			},

		}
	}
</script>

<style lang="less" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FE;

		.wrap-all {
			width: 700upx;
			height: 480upx;
			background-color: #fff;
			margin: 10px auto;
			padding: 10px 10px;
			border-radius: 10px;
			color: #6a6a6a;
			font-size: 15px;
			line-height: 27px;


			.section {
				width: 680upx;
				height: 485upx;
				// display: flex;
				// flex-direction: row;

				// border: 1px solid red;
				span {
					color: #000;
					font-size: 15px;
					padding: 0 10px;
				}

				.box1 {
					// width: 560upx;
					// width: 440upx;
					// border: 1px solid darkcyan;
				}

				.box2 {
					width: 260upx;
					// border: 1px solid gold;
				}
			}

			.box3 {
				width: 210upx;
				// border: 1px solid deepskyblue;
				height: auto;
				margin: 8px 10px 8px 200px;
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

		.modal {
			width: 100%;

			// background-color: red;
			&-name {
				color: #4C5971;
				font-size: 30upx;
				margin: 30upx 0;
			}

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
