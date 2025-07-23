<template>
	<view id="mixpile">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">搅拌桩信息</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<view class="top">
			<view class="left">
				<view class="left-img">
					<view class="img">

					</view>
				</view>
				<view class="left-text">
					在线数
					<p>{{opendevice}}</p>
				</view>
			</view>
			<view class="right">
				<view class="rightall">
					<view class="right-img">

					</view>
				</view>
				<view class="right-text">
					不在线数
					<p>{{alltotal-opendevice}}</p>
				</view>
			</view>


		</view>

		<view class="list">
			<view v-for="(item,index) in listdata" :key="index" @click="checkDetail(item)" class="list-item">
				<view class="list-item-con">
					<view style="color: #333333; font-weight: 600;" class="list-item-con-name">
						{{ item.shebeino_dictText }}
					</view>
					<view class="list-item-con-name">
						里程：<span>{{ item.mileage }}</span>
					</view>
					<view class="list-item-con-name">
						桩号：<span>{{ item.pileno }}</span>
					</view>
					<view class="list-item-con-name">
						状态：<span
							:style="{'color': item.worksta==3?'#EA0808':'#6DD400' }">{{ item.worksta==0?'在线':item.worksta==1?'下钻':item.worksta==2?'提钻':'停机中' }}</span>
					</view>
					<view class="list-item-con-name"></view>
				</view>
			</view>
			<view style="height: 40upx;"></view>
		</view>
		<u-modal :show="show" :title="title" showCancelButton @cancel="cancel" @confirm="confirm">
			<view class="screen-modal-item">
				<view class="screen-modal-item-name">组织机构：</view>
				<view class="screen-modal-item-input">
					<org @choose="Chooseorg"></org>
				</view>
			</view>
		</u-modal>
	</view>
</template>

<script>
	import org from '../component/organization/organization.vue'
	import ruanjiapi from '../../api/ruanji.js'
	export default {
		components: {
			org
		},
		data() {
			return {
				title: '筛选',
				pageNo: 1,
				listdata: [],
				opendevice: 0,
				statistical: '',
				sys_depart_orgcode: this.$store.getters.orgcode,
				show: false,
				total: 0,
				bhgtotal: 0,
				alltotal: 0,
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
			this.getDeviceOpen()
		},
		methods: {
			checkDetail(e) {
				if (e.worksta == '3') {
					uni.showToast({
						title: '此搅拌桩不在线，请选择其他设备！！',
						icon: 'none'
					})
				} else {
					uni.navigateTo({
						url: '/pages/ruanji/mixpilerealtime?shebeino=' + e.shebeino + '&id=' + e.id,
					})
				}
			},
			Chooseorg(choosevalue) {
				// 组织机构	
				this.sys_depart_orgcode = choosevalue
			},
			// 打开筛选框
			screen() {
				this.show = true
			},
			// 筛选框
			confirm() {
				this.pageNo = 1
				this.getloadlist()
				this.getDeviceOpen()
				this.show = false
			},
			cancel() {
				this.show = false
			},
			// 在线设备统计
			getDeviceOpen() {
				// console.log(this.$store.getters.orgcode)
				let params = {
					// sysOrgCode: this.sys_depart_orgcode
				}
				this.$http.get('devicemixpilerealdata/deviceMixpileRealdata/online', {
					params
				}).then(res => {
					this.opendevice = res.data.result.onlinecount
				})
			},
			// 获取搅拌桩数据列表
			getloadlist() {
				let params = {
					pageNo: this.pageNo,
					pageSize: 10,
					gropby: "shebeino",
					sys_depart_orgcode: this.sys_depart_orgcode
				}
				ruanjiapi.mixpilelist({
					params
				}).then(res => {
					this.alltotal = res.data.result.total
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
		}
	}
</script>

<style lang="scss" scoped>
	#mixpile {
		width: 100%;
		background-color: #F3F5FE;

		.top {
			width: 700upx;
			height: auto;
			margin: 10px auto;
			margin-top: 30upx;
			color: #727B8E;
			display: flex;
			flex-wrap: wrap;
			justify-content: space-between;

			.left {
				width: 345upx;
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
					background: #d6f5e7;
					border-radius: 10px;

					.img {
						width: 60upx;
						height: 60upx;
						background-image: url(../../static/shiti/two.png);
						// border: 1px solid #000;
						margin: 0 auto;
						line-height: 90upx;
						margin: 12px auto;
						background-size: 100% 92%;
						background-repeat: no-repeat;
					}
				}

				.left-text {
					width: 130upx;
					height: 145upx;
					// border: 1px solid #18BC37;
					color: #4C5971;
					text-align: center;
					font-size: 27upx;
					line-height: 60upx;
					font-family: 'DIN-Medium';

					p {
						color: #0FBF6E;
						font-size: 50upx;
					}

				}
			}

			.right {
				width: 345upx;
				height: 150upx;
				border-radius: 10px;
				background-color: #ffffff;
				display: flex;
				justify-content: space-evenly;
				align-items: center;

				.rightall {
					width: 120upx;
					height: 110upx;
					// border: 1px solid blue;
					background: #f5e0e2;
					border-radius: 10px;

					.right-img {
						width: 60upx;
						height: 60upx;
						background-image: url(../../static/shiti/one.png);
						// border: 1px solid #000;
						margin: 0 auto;
						line-height: 90upx;
						margin: 12px auto;
						background-size: 100% 92%;
						background-repeat: no-repeat;
					}
				}

				.right-text {
					width: 130upx;
					height: 145upx;
					// border: 1px solid #18BC37;
					color: #4C5971;
					text-align: center;
					font-size: 27upx;
					line-height: 60upx;
					font-family: 'DIN-Medium';

					p {
						color: #0FBF6E;
						font-size: 50upx;
					}

				}
			}
		}

		.list {
			width: 100%;

			// margin-top: 30upx;
			&-item {
				position: relative;
				width: 690upx;
				// height: 462upx;
				margin: 0 auto;
				margin-bottom: 30upx;
				border-radius: 16upx;
				background-color: white;

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
				border-radius: 12upx;
				padding: 20upx;

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
