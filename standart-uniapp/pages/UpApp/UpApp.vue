<template>
	<view class="mask flex-center">
		<view class="content botton-radius">
			<view class="content-top">
				<text class="content-top-text">{{title}}</text>
				<image class="content-top" style="top: 0;" width="100%" height="100%"
					src="../../static/upapp/bg_top.png">
				</image>
			</view>
			<view class="content-header"></view>
			<view class="content-body">
				<view class="title">
					<text>{{subTitle}}</text>
					<!-- <text style="padding-left:20rpx;font-size: 0.5em;color: #666;">v.{{version}}</text> -->
				</view>
				<view class="body">
					<scroll-view class="box-des-scroll" scroll-y="true">
						<text class="box-des">
							{{contents}}
						</text>
					</scroll-view>
				</view>
				<view class="footer flex-center">
					<template>
						<template v-if="!downloadSuccess">
							<view class="progress-box flex-column" v-if="downloading">
								<progress class="progress" border-radius="35" :percent="downLoadPercent"
									activeColor="#3DA7FF" show-info stroke-width="10" />
								<view style="width:100%;font-size: 28rpx;display: flex;justify-content: space-around;">
									<text>{{downLoadingText}}</text>
									<text>({{downloadedSize}}/{{packageFileSize}}M)</text>
								</view>
							</view>

							<button v-else class="content-button" style="border: none;color: #fff;" plain
								@click="downloadPackage">
								{{downLoadBtnText}}
							</button>
						</template>
						<button v-else class="content-button" style="border: none;color: #fff;" plain
							@click="installPackage">
							下载完成，立即安装
						</button>
					</template>
				</view>
			</view>

			<image class="close-img" src="../../static/upapp/app_update_close.png" @click.stop="closeUpdate"></image>
		</view>
	</view>
</template>

<script>
	let downloadTask = null;

	export default {
		data() {
			return {
				appSrc: '',

				// 下载
				downloadSuccess: false,
				downloading: false,

				downLoadPercent: 0,
				downloadedSize: 0,
				packageFileSize: 0,

				tempFilePath: '', // 要安装的本地包地址

				// 默认安装包信息
				title: '更新日志',
				contents: '发现新版本！',

				// 可自定义属性
				subTitle: '发现新版本！',
				downLoadBtnText: '立即下载更新',
				downLoadingText: '安装包下载中，请稍后'
			}
		},
		onLoad({
			appSrc,
			contents
		}) {
			this.appSrc = appSrc;
			this.contents = contents;
		},
		methods: {
			async closeUpdate() {
				if (this.downloading) {
					uni.showModal({
						title: '是否取消下载？',
						cancelText: '否',
						confirmText: '是',
						success: res => {
							if (res.confirm) {
								downloadTask && downloadTask.abort()
								uni.navigateBack()
							}
						}
					});
					return;
				}

				// if (this.downloadSuccess && this.tempFilePath) {
				// 	// 包已经下载完毕，稍后安装，将包保存在本地
				// 	await this.saveFile(this.tempFilePath, this.version)
				// 	uni.navigateBack()
				// 	return;
				// }

				uni.navigateBack()
			},
			downloadPackage() {
				this.downloading = true;

				//下载包
				downloadTask = uni.downloadFile({
					url: this.appSrc,
					success: res => {
						if (res.statusCode == 200) {
							this.downloadSuccess = true;
							this.tempFilePath = res.tempFilePath
						}
					},
					complete: () => {
						this.downloading = false;

						this.downLoadPercent = 0
						this.downloadedSize = 0
						this.packageFileSize = 0

						downloadTask = null;
					}
				});

				downloadTask.onProgressUpdate(res => {
					this.downLoadPercent = res.progress;
					this.downloadedSize = (res.totalBytesWritten / Math.pow(1024, 2)).toFixed(2);
					this.packageFileSize = (res.totalBytesExpectedToWrite / Math.pow(1024, 2)).toFixed(2);
				});
			},
			installPackage() {
				// #ifdef APP-PLUS
				plus.runtime.install(
					this.tempFilePath, {
						force: false
					}, () => {
						setTimeout(() => {
							uni.navigateBack()
						}, 1000)
					}, err => {
						uni.showModal({
							title: '更新失败',
							content: err.message,
							showCancel: false
						});
					}
				)
				// #endif
			},
			// async saveFile(tempFilePath, version) {
			// 	const [err, res] = await uni.saveFile({
			// 		tempFilePath
			// 	})
			// 	if (err) {
			// 		return;
			// 	}
			// 	uni.setStorageSync(localFilePathKey, {
			// 		version,
			// 		savedFilePath: res.savedFilePath
			// 	})
			// },
			// deleteSavedFile(filePath) {
			// 	uni.removeStorageSync(localFilePathKey)
			// 	return uni.removeSavedFile({
			// 		filePath
			// 	})
			// },
		}
	}
</script>

<style>
	page {
		background: transparent;
	}

	.flex-center {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		justify-content: center;
		align-items: center;
	}

	.mask {
		position: fixed;
		left: 0;
		top: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(0, 0, 0, .65);
	}

	.botton-radius {
		border-bottom-left-radius: 30rpx;
		border-bottom-right-radius: 30rpx;
	}

	.content {
		position: relative;
		top: 0;
		width: 600rpx;
		background-color: #fff;
		box-sizing: border-box;
		padding: 0 50rpx;
		font-family: Source Han Sans CN;
	}

	.text {
		/* #ifndef APP-NVUE */
		display: block;
		/* #endif */
		line-height: 200px;
		text-align: center;
		color: #FFFFFF;
	}

	.content-top {
		position: absolute;
		top: -195rpx;
		left: 0;
		width: 600rpx;
		height: 270rpx;
	}

	.content-top-text {
		font-size: 45rpx;
		font-weight: bold;
		color: #F8F8FA;
		position: absolute;
		top: 120rpx;
		left: 50rpx;
		z-index: 1;
	}

	.content-header {
		height: 70rpx;
	}

	.title {
		font-size: 33rpx;
		font-weight: bold;
		color: #3DA7FF;
		line-height: 38px;
	}

	.footer {
		height: 150rpx;
		display: flex;
		align-items: center;
		justify-content: space-around;
	}

	.box-des-scroll {
		box-sizing: border-box;
		padding: 0 40rpx;
		height: 200rpx;
		text-align: left;
	}

	.box-des {
		font-size: 26rpx;
		color: #000000;
		line-height: 50rpx;
	}

	.progress-box {
		width: 100%;
	}

	.progress {
		width: 90%;
		height: 40rpx;
		border-radius: 35px;
	}

	.close-img {
		width: 70rpx;
		height: 70rpx;
		z-index: 1000;
		position: absolute;
		bottom: -120rpx;
		left: calc(50% - 70rpx / 2);
	}

	.content-button {
		text-align: center;
		flex: 1;
		font-size: 30rpx;
		font-weight: 400;
		color: #FFFFFF;
		border-radius: 40rpx;
		margin: 0 18rpx;

		height: 80rpx;
		line-height: 80rpx;

		background: linear-gradient(to right, #1785ff, #3DA7FF);
	}

	.flex-column {
		display: flex;
		flex-direction: column;
		align-items: center;
	}
</style>