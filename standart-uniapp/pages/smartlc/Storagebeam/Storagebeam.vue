<template>
	<view class="wrap" v-has="'save:liang'">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">存梁区</block>
			<!-- <view slot="right" @click="screen">筛选</view> -->
		</cu-custom>
		<view class="top-two">
			<view class="left" @tap="tabSeltai" data-id="0">
				<view class="left-text">
					<p class="red">{{qtaizuo}}</p>
					全部台座
				</view>
			</view>
			<view class="left" @tap="tabSeltai" data-id="1">
				<view class="left-text">
					<p class="orange">{{ktaizuo}}</p>
					空闲台座
				</view>
			</view>
			<view class="left" @tap="tabSeltai" data-id="2">
				<view class="left-text">
					<p class="green">{{ztaizuo}}</p>
					占用台座
				</view>
			</view>
		</view>
		<view class="">
			<u--input placeholder="请输入台座名称" placeholderStyle=black style="width: 700upx;margin: 0 auto;"
				border="surround" @change="tzuochange"></u--input>
		</view>
		<view class="taizuo shadow-warp" v-for="(item,index) in liangc" :key="index">
			<view class="taizuo-item" @click="touchclick(item)" :class="active"
				:style="item.switchsta ==0?'background-color:#f93e3e':item.switchsta ==1?'background-color:#bbedbb':'background-color: #F1F9FF;'">
				<view class="taizuo-left">
					<view class="padding-left-sm">
						<view style="font-size: 24px;font-weight: 600;font-family: cursive;">
							{{item.liangzuoname}}
						</view>
						<!-- <view class="" >存梁台座</view> -->
					</view>
				</view>
				<view class="taizuo-right">
					<!-- 只有一层有梁 -->
					<!-- <view class="" v-if="item.zhiliangGongxuList.length==1">
						<view class="" v-if="item.zhiliangGongxuList[0].liangceng==1">
							<view class="liang1">
								<view class=""></view>
								<view class=""></view>
							</view>
							<view class="liang1" @click="beamnews(item)">
								<view style="font-weight: 600;" class="">
									{{item.zhiliangGongxuList[0].taizuoname}}</view>
								<view class="">{{item.zhiliangGongxuList[0].remark}}</view>
							</view>
						</view>
						<view class="" v-if="item.zhiliangGongxuList[0].liangceng==2">
							<view class="liang1" @click="beamnews(item)">
								<view style="font-weight: 600;" class="">
									{{item.zhiliangGongxuList[0].taizuoname}}</view>
								<view class="">{{item.zhiliangGongxuList[0].remark}}</view>
							</view>
							<view class="liang1">
								<view class=""></view>
								<view class=""></view>
							</view>
						</view>
					</view> -->
					<!-- 二层都有梁 -->
					<view class="" v-if="item.zhiliangGongxuList.length!==0">
						<view v-if="item.zhiliangGongxuList[0].liangceng==1">
							<view class="liang1" @click="beamnews(item)">
								<view style="font-weight: 600;font-family: cursive;font-size:30upx;" class="">
									{{item.zhiliangGongxuList.length == 1 ? '' : item.zhiliangGongxuList[1].taizuoname}}
								</view>
								<view class="" style="font-family:cursive;font-size:29upx;padding: 5px 0;">
									{{item.zhiliangGongxuList.length == 1 ? '' : item.zhiliangGongxuList[1].remark}}
								</view>
							</view>
							<view class="liang1" @click="beamnews(item)">
								<view style="font-weight: 600;font-family: cursive;font-size:30upx;" class="">
									{{item.zhiliangGongxuList[0].taizuoname}}
								</view>
								<view class="" style="font-family:cursive;font-size:29upx;padding: 5px 0;">
									{{item.zhiliangGongxuList[0].remark}}
								</view>
							</view>
						</view>
						<view v-else>
							<view class="liang1" @click="beamnews(item)">
								<view style="font-weight: 600;font-family: cursive;font-size:30upx;" class="">
									{{item.zhiliangGongxuList[0].taizuoname}}
								</view>
								<view class="" style="font-family:cursive;font-size:29upx;padding: 5px 0;">
									{{item.zhiliangGongxuList[0].remark}}
								</view>
							</view>
							<view class="liang1" @click="beamnews(item)">
								<view style="font-weight: 600;font-family: cursive;font-size:30upx;" class="">
									{{item.zhiliangGongxuList[1].taizuoname}}
								</view>
								<view class="" style="font-family:cursive;font-size:29upx;padding: 5px 0;">
									{{item.zhiliangGongxuList[1].remark}}
								</view>
							</view>
						</view>
					</view>

					<!-- 两层都没有梁 -->
					<view class="" v-if="item.zhiliangGongxuList.length==0">
						<view class="liang1">
							<view class=""></view>
							<view class=""></view>
						</view>
						<view class="liang1">
							<view class=""></view>
							<view class=""></view>
						</view>
					</view>

				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				liangc: [],
				qtaizuo: 0,
				ztaizuo: 0,
				ktaizuo: 0,
				lzname: '',
				pageNo: 1,
				status: '',
				status1: '',
				lname: '',
				Touchnum: 0,
				bgshow: false,
				active: ''
			}
		},
		onLoad() {
			this.loadData()
			this.gettaizuo()
			console.log("data", this.liangc)
		},
		onReachBottom() {
			uni.showNavigationBarLoading()
			this.pageNo++
			this.gettaizuo()
			uni.hideNavigationBarLoading()
		},
		// srtaizuo(e) {
		// 	console.log(e.detail.value)
		// 	this.lzname = e.detail.value
		// 	this.pageNo = 1
		// 	this.gettaizuo()
		// },
		methods: {
			gettaizuo() {
				let params = {}
				if (this.status == 1 || this.status1 == 1) {
					params = {
						sta: this.status,
						sta1: this.status1,
						pageNo: this.pageNo,
						pageSize: 10,
						// liangzuoname: this.lname,
					}
				} else {
					console.log(this.lname)
					params = {
						sta: this.status,
						sta1: this.status1,
						pageNo: this.pageNo,
						pageSize: 10,
						column: 'id',
						order: 'asc',
						liangzuoname: this.lname,
					}
				}
				this.$http.get('/cunliang/beammanagementProcedure/list6', {
					params
				}).then(res => {
					console.log(res, "=========================")
					if (res.data.result.length == 0) {
						uni.showToast({
							title: '没有更多数据了',
							icon: 'none'
						})
					}
					if (this.pageNo != 1) {
						this.liangc = this.liangc.concat(res.data.result)
					} else {
						this.liangc = res.data.result
					}
				})
			},
			loadData() {
				let params = {
					sys_depart_orgcode: this.$store.getters.orgcode,
					column: 'id',
					order: 'desc',
					// status: 1,
					// tiliangstatus: 2
				}
				this.$http.get('/zhiliangrenwudan/zhiliangrenwudan/list', {
					params
				}).then(res => {
					let data = res.data.result.records
					console.log(data)
					this.listdata = []
					data.forEach(e => {
						if (e.cunliangstatus !== 2) {
							this.listdata.push(e)
						}
					})
				})
				// /cunliang/beammanagementProcedure/list3
				this.$http.get('/cunliang/beammanagementProcedure/list5').then(res => {
					// this.liangc = res.data.result
					this.qtaizuo = res.data.result.length
					console.log(res)
				})
				this.$http.get('/cunliang/beammanagementProcedure/list5?sta=0&sta1=0').then(res => {
					// this.liangc = res.data.result
					this.ktaizuo = res.data.result.length
					console.log(res)
				})
				this.$http.get('/cunliang/beammanagementProcedure/list5?sta=1&sta1=1').then(res => {
					// this.liangc = res.data.result
					this.ztaizuo = res.data.result.length
					console.log(res)
				})

			},
			tabSeltai(e) {
				this.TabTaizuo = e.currentTarget.dataset.id;
				if (e.currentTarget.dataset.id == 0) {
					this.status = ''
					this.status1 = ''
					this.pageNo = 1
					this.gettaizuo()
				}
				if (e.currentTarget.dataset.id == 1) {
					this.status = 0
					this.status1 = 0
					this.pageNo = 1
					this.gettaizuo()
				}
				if (e.currentTarget.dataset.id == 2) {
					this.status = 1
					this.status1 = 1
					this.pageNo = 1
					this.gettaizuo()
				}
			},
			beamnews(e) {
				console.log(e)
				uni.navigateTo({
					url: '/pages/smartlc/Storagebeam/Storagedetails?uuid=' + e.zhiliangGongxuList[0].uuid
				})
			},
			tzuochange(e) {
				console.log(e, 'ddd')
				this.lname = e
				this.gettaizuo()
			},
			// Beamlist(){

			// },
			// gettouch(num){
			// 	return num % 2 == 0
			// },
			touchclick(e) {
				this.lname = ''
				this.lname = e.liangzuoname
				let params = {
					uuid: e.uuid,
					switchsta: e.switchsta
				}
				this.$http.put(`/sys/systems/sysMessages/switchEdit`, params).then(res => {
					console.log(res, '关灯成功')
					if (res.data.success) {
						this.selectdata()
						uni.showToast({
							title: res.data.result,
							icon: 'success'
						})
						
						
					}
				})
			},
			selectdata(){
				this.$http.get(`/cunliang/beammanagementProcedure/list6`, {
					params: {
						liangzuoname: this.lname,
					}
				}).then(res => {
					// console.log(res, '再次渲染页面')
					this.liangc = res.data.result
				})
			},
		}
	}
</script>

<style lang="less" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FE;

		.top-two {
			width: 700upx;
			height: 180upx;
			margin: 0 auto;
			// margin-top: 80upx;
			// border-radius: 16upx;
			// border:1px solid #1785FF;
			display: flex;
			justify-content: space-between;
			align-items: center;

			.left {
				width: 225upx;
				height: 150upx;
				border-radius: 10px;
				background-color: #ffffff;
				display: flex;
				justify-content: space-evenly;
				align-items: center;

				.left-img {
					width: 120upx;
					height: 110upx;
					// border: 1px solid blue;
					background: #cfe0fd;
					border-radius: 10px;

					.img {
						width: 60upx;
						height: 60upx;
						background-image: url(../../../static/shiti/five.png);
						// border: 1px solid #000;
						margin: 0 auto;
						line-height: 90upx;
						margin: 12px auto;
						background-size: 100% 92%;
						background-repeat: no-repeat;
					}
				}

				.left-text {
					width: 140upx;
					height: 100upx;
					// border: 1px solid #18BC37;
					color: #4C5971;
					text-align: center;
					font-size: 27upx;
					line-height: 50upx;
					font-family: 'DIN-Medium';

					.red {
						color: #5A92FD;
						font-size: 50upx;
					}

					.orange {
						color: #FF8712;
						font-size: 50upx;
					}

					.green {
						color: #0FBF6E;
						font-size: 50upx;
					}

				}
			}
		}

		.taizuo {
			margin: 10px 5px 0px 5px;
			border-radius: 10px;
			height: 360rpx;
			background-color: #F1F9FF;

			// background-color: red;
			.taizuo-item {
				margin: 2px;
				border-radius: 10px;
				height: 100%;
				display: flex;

				.taizuo-left {
					flex: 1;
					display: flex;
					align-items: center;
				}

				.taizuo-right {
					padding: 1px 0;
					flex: 2;
					display: flex;
					flex-direction: column;
					justify-content: space-around;

					.liang1 {
						margin-top: 1px;
						margin-bottom: 1px;
						border-top-right-radius: 10px;
						border-bottom-right-radius: 10px;
						height: 170rpx;
						background-color: #ffffff;
						padding: 5px;
					}
				}
			}

			// .Originalcolr {
			// 	background-color: #f1f9ff;
			// }

			.changecolr {
				background-color: #ff4639;
			}
		}

	}
</style>
