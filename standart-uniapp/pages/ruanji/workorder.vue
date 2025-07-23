<template>
	<view id="wordorder">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">搅拌桩工单管理</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<view class="top">
			<view class="top-item">
				<view class="top-item-img" style="background-image: url(../../static/ruanji/weikaishi.png);"></view>
				<view class="top-item-name">
					<view>未开始</view>
					<view style="font-size: 44upx; color: #FF233D;">{{datasta.ready||0}}</view>
				</view>
			</view>
			<view class="top-item">
				<view class="top-item-img" style="background-image: url(../../static/ruanji/yiwancheng.png);"></view>
				<view class="top-item-name">
					<view>已完成</view>
					<view style="font-size: 44upx; color: #0FBF6E;">{{datasta.finish||0}}</view>
				</view>
			</view>
			<view class="top-item">
				<view class="top-item-img" style="background-image: url(../../static/ruanji/shenchanz.png);"></view>
				<view class="top-item-name">
					<view>生产中</view>
					<view style="font-size: 44upx; color: #387FFD;">{{datasta.doing||0}}</view>
				</view>
			</view>
			<view class="top-item">
				<view class="top-item-img" style="background-image: url(../../static/ruanji/zhihou.png);"></view>
				<view class="top-item-name">
					<view>滞后</view>
					<view style="font-size: 44upx; color: #333333;">{{datasta.timeout||0}}</view>
				</view>
			</view>
		</view>
		<view class="list">
			<view v-for="(item,index) in listdata" :key="index" @click="checkDetail(item)" class="list-item">
				<view class="list-item-sta"
					:class="item.status == 0 ? 'red':item.status==1?'blue' :item.status==2?'green':'gery'">
					<view
						:style="item.status == 0 ? 'color:#FF233D;':item.status==1?'color:#387FFD;' :item.status==2?'color:#0FBF6E;':'color:#5E6E89;'">
						{{ item.status == 0 ? '未开始':(item.status==1?'生产中' :(item.status==2?'已完成':'滞后')) }}
					</view>
				</view>
				<view class="list-item-con">
					<view class="list-item-con-name">
						里程：<span>{{ item.mileage }}</span>
					</view>
					<view class="list-item-con-name">
						设计桩基数：<span>{{ item.descount }}</span>
					</view>
					<view class="list-item-con-name">
						工单号：<span>{{ item.rjrwd }}</span>
					</view>
					<view class="list-item-con-name">
						下发时间：<span>{{ item.starttime }}</span>
					</view>
					<view class="list-item-con-name">
						截止时间：<span>{{ item.endtime }}</span>
					</view>
					
					<view class="list-item-con-name">
						<u-line-progress height="18" :percentage="item.totalpro"></u-line-progress>
					</view>
					<view style="height: 30upx;"></view>
				</view>
			</view>
			<view style="height: 40upx;"></view>
		</view>
		<view v-has="'workorder:add'" class="add" @click="addHandle"></view>
		<u-modal :show="show" @confirm="confirm" showCancelButton @cancel="cancel">
			<view class="screen">
				<view class="screen-modal">
					<view class="screen-modal-item">
						<view class="screen-modal-item-name">组织机构：</view>
						<view class="screen-modal-item-input">
							<!-- <picker @change="Changeorg" :range="orglist" :range-key="'departName'"> -->
								<orgTree @choose="Chooseorg"></orgTree>
								<!-- <u--input placeholder="请选择组织机构" border="surround" v-model="orgname" disabled
									suffixIcon="arrow-down"></u--input> -->
							<!-- </picker> -->
						</view>
					</view>
					<view class="screen-modal-item">
						<view class="screen-modal-item-name">里程：</view>
						<view class="screen-modal-item-input">
							<!-- <orgTree @choose="Chooseorg"></orgTree> -->
							<u--input placeholder="请输入里程" border="surround" v-model="mileage"></u--input>
						</view>
					</view>
					<view class="screen-modal-item">
						<view class="screen-modal-item-name">状态：</view>
						<view class="screen-modal-item-input">
							<picker @change="Changestatus" :range="statusnames">
								<u--input placeholder="请选择状态" border="surround" v-model="statusname" disabled
									suffixIcon="arrow-down"></u--input>
							</picker>
						</view>
					</view>
				</view>
			</view>
		</u-modal>
	</view>
</template>

<script>
	import orgTree from "../component/organization/organization.vue"
	import ruanjiapi from "../../api/ruanji.js"
	import api from "../../api/api.js"
	export default {
		components: {
			orgTree
		},
		data() {
			return {
				listdata: [],
				datasta: '',
				pageNo: 1,
				orglist: [],
				orgname: '',
				show: false,
				mileage: '',
				statusnames: ['未开始', '生产中', '已完成', '滞后'],
				statusname: '',
				status: '',
				orgcode: ''
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
			this.getOrgList()
		},
		methods: {

			checkDetail(e) {
				uni.navigateTo({
					url: '/pages/ruanji/workorderdetail?rjrwd=' + e.rjrwd
				})
			},
			addHandle() {
				uni.navigateTo({
					url: '/pages/ruanji/workorderadd'
				})
			},
			// 选择状态
			Changestatus(e) {
				this.statusname = this.statusnames[e.detail.value]
				this.status = e.detail.value
			},
			Chooseorg(choosevalue) {
				// 组织机构
				console.log(choosevalue)
				this.orgcode = choosevalue
			},
			// 打开筛选框
			screen() {
				this.show = true
			},
			// 确认筛选
			handleOk() {
				this.listdata = []
				this.pageNo = 1
				this.$nextTick(function() {
					this.getloadlist()
				})
				this.show = false
			},
			cancel() {
				this.show = false
			},
			// 关闭筛选框
			confirm() {
				// this.getloadlist()
				this.listdata = []
				this.$nextTick(function() {
					this.getloadlist()
				})
				this.show = false
			},
			// 获取软基数据列表
			getloadlist() {
				let params = {
					column: 'id',
					order: 'desc',
					pageNo: this.pageNo,
					pageSize: 10,
					mileage: this.mileage,
					status: this.status,
					sys_depart_orgcode: this.orgcode
				}
				ruanjiapi.workorderlist({
					params
				}).then(res => {
					// console.log(res, "软基工单列表")
					this.datasta = res.data.data
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
			// 获取组织机构列表
			getOrgList() {
				// console.log(this.$store.getters.orgcode)
				let params = {
					orgtype: "5,6",
					sysorgcode: this.$store.getters.orgcode
				}
				api.getOrgList({
					params
				}).then(res => {
					// console.log(res,'组织机构列表')
					this.orglist = res.data.result
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	#wordorder {
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
				width: 330upx;
				height: 150upx;
				border-radius: 16upx;
				background-color: white;
				display: flex;
				// flex-direction: column;
				// justify-content: center;
				align-items: center;

				&-img {
					width: 100upx;
					height: 100upx;
					background-repeat: no-repeat;
					background-size: 100% 100%;
					margin-left: 30upx;
					margin-right: 18upx;
				}

				&-name {
					font-size: 28upx;
					color: #4C5971;
					// margin-top: 24upx;
				}
			}
		}

		.list {
			width: 100%;

			&-item {
				position: relative;
				width: 690upx;
				margin: 0 auto;
				margin-bottom: 30upx;
				border-radius: 16upx;
				background-color: white;

				.red {
					background-image: url(../../static/pour/sta1.png);
					background-repeat: no-repeat;
					background-size: 100% 100%;
				}

				.green {
					background-image: url(../../static/pour/sta4.png);
					background-repeat: no-repeat;
					background-size: 100% 100%;
				}

				.blue {
					background-image: url(../../static/pour/sta2.png);
					background-repeat: no-repeat;
					background-size: 100% 100%;
				}

				.gery {
					background-image: url(../../static/pour/sta5.png);
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
			background-image: url(../../static/pour/add.png);
			background-repeat: no-repeat;
			background-size: 100% 100%;
		}

		.screen {
			// position: fixed;
			// background: rgba(000, 000, 000, .3);
			// left: 0;
			// top: 0;
			// width: 100%;
			// height: 100%;

			&-modal {
				// background-color: #FFFFFF;
				// position: absolute;
				// top: 50%;
				// left: (750upx - 690upx) / 2;
				// width: 690upx;
				// transform: translateY(-50%);
				// box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.1);
				// border-radius: 12upx;
				// padding: 20upx;

				&-item {
					text-align: left;
					// width: 90%;
					margin-bottom: 30upx;

					&-name {
						color: #4C5971;
						font-size: 30upx;
						margin-bottom: 30upx;
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
