<template>
	<view id="samplinglist">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">试块管理</block>
			<!-- <view slot="right" @click="screen">筛选</view> -->
		</cu-custom>
		<!-- <view class="top">
			<view class="top-item">
				<view class="top-item-num" style="color: #FF233D;">66</view>
				<view class="top-item-name">见证取样</view>
			</view>
			<view class="top-item">
				<view class="top-item-num" style="color: #FF8712;">999</view>
				<view class="top-item-name">已审核未配料</view>
			</view>
			<view class="top-item">
				<view class="top-item-num" style="color: #0FBF6E;">120</view>
				<view class="top-item-name">试块上架</view>
			</view>
		</view> -->
		<u-loading-page :loading="loading" loading-text="加载中..."></u-loading-page>
		<view class="list">
			<view v-for="(item,index) in listdata" :key="index" @click="checkDetail(item)" class="list-item">
				<!-- <view class="list-item-sta" :class="item.chaobiaodengji == 0 ? 'green':'red'">
					<image v-if="item.yhstatus == 0" src="../../../static/pour/sta1.png" mode=""></image>
					<image v-if="item.yhstatus == 1" src="../../../static/pour/sta2.png" mode=""></image>
					<image v-if="item.yhstatus == 2" src="../../../static/pour/sta4.png" mode=""></image>
					<view :style="item.yhstatus == 0 ? 'color:#F32F45;':'color:#52C57C;'">
						{{ item.yhstatus == 0 ? '已过期':item.yhstatus == 1 ? '未过期' : '可试验' }}
					</view>
				</view> -->
				<view class="list-item-con">
					<view class="list-item-con-name">
						样品名称：<span>{{ item.ypmc }}</span>
					</view>
					<view class="list-item-con-name">
						取样地点：<span>{{ item.sgbw }}</span>
					</view>
					<view class="list-item-con-name">
						取样时间：<span>{{ item.qyrq }}</span>
					</view>
					<view class="list-item-con-name">
						取样人：<span>{{ item.qyr }}</span>
					</view>
					<view class="list-item-con-name">
						数量：<span>{{ item.sysuliang }}</span>
					</view>
					<view class="list-item-con-name">
						试验类型：<span>{{ item.syxmmc }}</span>
					</view>
					<view class="list-item-con-name"></view>
				</view>
			</view>
			<view style="height: 40upx;"></view>
		</view>
		<view class="add" @click="popshow==false?popshow=true:popshow=false">
			<u-icon name="more-dot-fill" color="#2979ff" size="28"></u-icon>
		</view>
		<view class="popconfirm" v-if="popshow">
			<view class="popconfirm-item" @click="addHandle">
				<image class="popconfirm-item-img" src="../../../static/smartsy/sampling.png" mode=""></image>
				<p>见证取样</p>
			</view>
			<view class="popconfirm-item" @click="saveHandle">
				<image class="popconfirm-item-img" src="../../../static/smartsy/save.png" mode=""></image>
				<p>试块上架</p>
			</view>
			<view class="popconfirm-item" @click="downHandle">
				<image class="popconfirm-item-img" src="../../../static/smartsy/lower.png" mode=""></image>
				<p>试块下架</p>
			</view>
			<view class="popconfirm-item"></view>
		</view>
	</view>
</template>

<script>
	import smartsyapi from "../../../api/smartsy.js"
	export default {
		name: "samplinglist",
		data() {
			return {
				loading: true,
				pageNo: 1,
				listdata: [],
				popshow: false,
				nowdate: ''
			};
		},
		onReachBottom() {
			uni.showNavigationBarLoading()
			this.pageNo++
			this.getloadlist()
			uni.hideNavigationBarLoading()
		},
		mounted() {
			this.getloadlist()
			this.getDate()
		},
		methods: {
			checkDetail(e) {
				// console.log(e)
				let item = JSON.stringify(e)
				uni.navigateTo({
					url: '/pages/smartsy/sampling/samplingdetail?item=' + item
				})
			},
			addHandle() {
				uni.navigateTo({
					url: '/pages/smartsy/sampling/samplingadd'
				})
			},
			saveHandle() {
				uni.navigateTo({
					url: '/pages/smartsy/sampling/testblocksave'
				})
			},
			downHandle() {
				uni.navigateTo({
					url: '/pages/smartsy/sampling/testblockdown'
				})
			},
			// 获取搅拌桩数据列表
			getloadlist() {
				let params = {
					pageNo: this.pageNo,
					pageSize: 10,
					column: 'id',
					order: 'desc'
				}
				smartsyapi.samplinglist({
					params
				}).then(res => {
					// console.log(res, "搅拌桩数据列表")
					this.loading = false
					if (res.data.result.length == 0) {
						uni.showToast({
							title: '没有更多数据了',
							icon: 'none'
						})
					}
					let datas = res.data.result
					// datas.forEach(e=>{
					// 	if(this.nowdate>e.starttime){
					// 		e.yhstatus = 0
					// 		// console.log("已过期")
					// 	}
					// 	if(this.nowdate<e.starttime){
					// 		e.yhstatus = 1
					// 		// console.log("未过期")
					// 	}
					// 	if(this.nowdate<e.starttime && this.nowdate>e.endtime){
					// 		e.yhstatus = 2
					// 		// console.log("可试验")
					// 	}
					// })
					if (this.pageNo != 1) {
						this.listdata = this.listdata.concat(datas)
					} else {
						this.listdata = datas
					}
					console.log(this.listdata)
				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			// 获取当前时间
			getDate() {
				var nowDate = new Date();
				let year = nowDate.getFullYear()
				let month = nowDate.getMonth() + 1 > 10 ? nowDate.getMonth() + 1 : this.addzero(nowDate.getMonth() + 1)
				let date = nowDate.getDate() > 10 ? nowDate.getDate() : this.addzero(nowDate.getDate())
				let hours = nowDate.getHours() > 10 ? nowDate.getHours() : this.addzero(nowDate.getHours())
				let minutes = nowDate.getMinutes() > 10 ? nowDate.getMinutes() : this.addzero(nowDate.getMinutes())
				let seconds = nowDate.getSeconds() > 10 ? nowDate.getSeconds() : this.addzero(nowDate.getSeconds())
				this.nowdate = year + '-' + month + '-' + date + ' ' + hours + ':' + minutes + ':' + seconds;

			},
			addzero(obj) {
				if (obj < 10) {
					return '0' + obj
				} else {
					return obj
				}
			},
		}
	}
</script>

<style lang="scss" scoped>
	#samplinglist {
		width: 100%;
		background-color: #F3F5FE;

		.top {
			width: 690upx;
			// height: 360upx;
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

		.list {
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

				&-sta {
					position: absolute;
					right: 0px;
					width: 165upx;
					height: 165upx;

					image {
						width: 100%;
						height: 100%;
					}

					view {
						position: absolute;
						transform: rotate(45deg);
						top: 40upx;
						left: 65upx;
					}
				}

				&-sta1 {
					position: absolute;
					right: 20upx;
					bottom: 20upx;
					width: 165upx;
					height: 165upx;

					image {
						width: 100%;
						height: 100%;
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
			bottom: 120upx;
			right: 20upx;
			width: 100upx;
			height: 100upx;
			background-color: #FFFFFF;
			border-radius: 50upx;
			display: flex;
			justify-content: center;
			align-items: center;
			// background-image: url(../../../static/pour/add.png);
			// background-repeat: no-repeat;
			// background-size: 100% 100%;
		}

		.popconfirm {
			position: fixed;
			bottom: 200upx;
			right: 20upx;
			width: 200upx;
			// height: 155upx;
			border-radius: 16upx;
			background-color: white;
			border: 1px solid #E6E8ED;
			box-shadow: 3px 5px 13px 0px rgba(197, 207, 227, 0.46);

			&-item {
				display: flex;
				justify-content: center;
				align-items: center;
				margin-top: 20upx;

				&-img {
					width: 30upx;
					height: 30upx;
					margin-right: 10upx;
				}
			}
		}
	}
</style>
