<template>
	<view>
		<u-loading-page :loading="loading" loading-text="加载中请稍后..."></u-loading-page>
		<view v-for="(item,index) in lablelist" :key="index">
			<view class="smartbh">
				<view>
					<view class="smartbh-message">
						{{item.title}}
					</view>
				</view>
				<view class="smartbh-label">
					<view class="after-add" v-for="(item1,index1) in item.mate" :key="index1">
						<view class="smartbh-label-all" @click="handleNav(item1.url)">
							<view class="smartbh-label-all-img" :style="item1.style"></view>
							<view class="smartbh-label-all-title">{{item1.title}}</view>
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import api from "../../api/api.js"
	export default {
		data() {
			return {
				lablelist: [],
				nameid: '',
				loading: true,
			}
		},
		created() {
			this.getlableData()
		},
		methods: {
			// handleNav(e) {
			// 	uni.navigateTo({
			// 		url: e
			// 	})
			// },

			handleNav(e) {
				if (e == "/pages/smartlc/beamInfo/beamInfo") {
					var that = this;
					that.nameid = '';
					// 允许从相机和相册扫码
					uni.scanCode({
						scanType: ['qrCode'],
						success: function(res) {
							if (res.scanType == 'QR_CODE') {
								that.nameid = res.result;
								setTimeout(() => {
									uni.showToast({
										title: "正在加载中，请稍侯",
										icon: 'loading'
									})
									setTimeout(() => {
										if (that.nameid != '') {
											uni.navigateTo({
												url: '/pages/smartlc/beamInfo/beamInfo?id=' +
													that.nameid
											})
										} else {
											this.$tip.error('请重新扫码');
										}
									}, 500)
								}, 1000)
							} else {
								uni.showToast({
									title: "请重新扫描二维码",
									icon: 'none'
								})
							}
							console.log('条码类型：' + res.scanType);
							console.log('条码内容：' + res.result);
						}
					})
				} else if (e == "/pages/smartlc/infolist/infolist") {
					var that = this;
					that.nameid = ''
					// 允许从相机和相册扫码
					uni.scanCode({
						success: (res) => {
							if (res.scanType == 'QR_CODE') {
								that.nameid = res.result;
								setTimeout(() => {
									uni.showToast({
										title: "正在加载中，请稍侯",
										icon: 'loading'
									})
									setTimeout(() => {
										if (that.nameid != '') {
											uni.navigateTo({
												url: '/pages/smartlc/infolist/infolist?id=' +
													that.nameid
											})
										} else {
											this.$tip.error('请重新扫码');
										}
									}, 500)
								}, 1000)
							} else {
								uni.showToast({
									title: "请重新扫描二维码",
									icon: 'none'
								})
							}
							console.log("条码类型：" + res.scanType);
							console.log("条码内容：" + res.result);
						}
						// ,
						// fail(err) {
						// 	if (that.nameid = "") {
						// 		uni.showToast({
						// 			title: "扫码失败,请重新扫码",
						// 			icon: 'none'
						// 		})
						// 	}
						// }
					});
				} else if (e == "/pages/WebView/Iroadw") {
					uni.navigateTo({
						url: e + '?url=https://road.iroadw.com/admin/login/auth.html?token=650e66a270ae6c9d392ec287c55994bd',
					})
				} else {
					uni.navigateTo({
						url: e,
					})
				}
			},

			getlableData() {
				api.permission().then(res => {
					this.loading = false
					this.lablelist = res.data.result.lableAuth
					console.log(this.lablelist, 'sss')
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.smartbh {
		width: 99%;
		margin: 0 auto;
		/* height: 50%; */
		// flex: 1;
		// border: 1px solid #ff557f;
		background-color: #fff;

		// margin-top: 1px;
		&-message {
			color: #000000;
			font-size: 21px;
			margin-left: 17px;
			font-weight: Bold;
			font-family: "PingFang-SC-Bold";
		}

		&-label {
			width: 100%;
			// height: 400upx;
			// background-color: #E45656;
			// border: 1px solid #91CB74;
			margin: 8px auto;
			display: flex;
			flex-wrap: wrap;
			// justify-content: space-around;

			&-label:not(:nth-child(4n)) {
				margin-right: calc(4% / 3);
			}

			&-all {
				width: 140upx;
				// height: 130upx;
				// border: 1px solid pink;

				&-img {
					width: 60upx;
					height: 60upx;
					margin: 0 auto;
					background-size: 100% 100%;
					background-repeat: no-repeat;
					// margin: 0 auto;
				}

				&-title {
					// height: 27px;
					text-align: center;
					font-size: 10px;
					padding-top: 5px;
					font-family: "PingFang SC";
					font-weight: 500;
					color: #333333;
					// line-height: 32px;
				}
			}
		}

		.after-add {
			// height: 0;
			padding: 10upx;
			margin: 10upx //之前li设定值 0 之前li设定值;
		}
	}
</style>