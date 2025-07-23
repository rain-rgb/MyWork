<template>
	<view id="home">
		<scroll-view scroll-y="true" class="scrolls-y" enable-flex="true">
			<view class="bg">
				<view class="header">
					<cu-custom bgColor="" class="newcolor" :isBack="true">
						<block slot="content">全部任务</block>
						<image slot="right" class="header-left-screen" src="../../static/pour/screen.png" mode="">
						</image>
						<image slot="right" class="header-left-tagpage" src="../../static/pour/phone.png" mode="">
						</image>
					</cu-custom>
				</view>
				<view class="top">
					<view>
						<view class="top-img" style="background-image: url(../../static/label/zh.png);">
							<view class="top-img-p">智慧拌合</view>
						</view>
					</view>
					<view>
						<view class="top-img" style="background-image: url(../../static/label/zhsy.png);">
							<view class="top-img-p">智慧试验</view>
						</view>
					</view>
					<view>
						<view class="top-img" style="background-image: url(../../static/label/rj.png);">
							<view class="top-img-p">软基监测</view>
						</view>
					</view>
					<view>
						<view class="top-img" style="background-image: url(../../static/label/clsy.png);">
							<view class="top-img-p">材料溯源</view>
						</view>
					</view>
				</view>
			</view>
<!-- 			<view class="article">
				<labeldata></labeldata>
			</view>
			<view class="article">
				<Softdata></Softdata>
			</view>
			<view class="article">
				<processdata></processdata>
			</view>
			<view class="article">
				<smartscoubt></smartscoubt>
			</view>
			<view class="article">
				<labelmodel></labelmodel>
			</view> -->
			<view class="article">
				<labeldatas></labeldatas>
			</view>
		</scroll-view>

	</view>
</template>
<script>
	import labeldata from '../labeldata/labeldata.vue'
	import Softdata from '../labeldata/Softdata.vue'
	import processdata from '../labeldata/processdata.vue'
	import smartscoubt from '../labeldata/smartscount.vue'
	import labelmodel from '../labeldata/labelmodel.vue'
	import labeldatas from '../labeldata/labeldatas.vue'
	export default {
		components: {
			labeldata,
			Softdata,
			processdata,
			smartscoubt,
			labelmodel,
			labeldatas
		},
		data() {
			return {

			}
		},
		methods: {
		}
	}
</script>

<style lang="scss" scoped>
	#home {
		background-color: #F2F5FE;
		// width: 100%;
		// white-space: nowrap;
	}

	.bg {
		background-image: url(../../static/home/bg-header.png);
		background-repeat: no-repeat;
		background-color: #fff;
		background-size: 100%;
	}

	.header {
		width: 100%;
		height: 100upx;
		display: flex;

		// background-color: #506CE2;
		&-left {
			width: 20%;
			display: flex;
			justify-content: center;
			align-items: flex-end;

			&-tagpage {
				width: 34upx;
				height: 37upx;
				margin-left: 30upx;
			}

			&-screen {
				width: 34upx;
				height: 37upx;
				margin-left: 30upx;
			}

			// background-color: red;
		}

		&-center {
			width: 60%;
			// background-color: blue;
			display: flex;
			align-items: flex-end;
			overflow: hidden;
			white-space: nowrap;

			.center {
				width: 40%;
				text-align: center;
				font-size: 38upx;
				color: #FFFFFF;
			}

			.side {
				width: 30%;
				text-align: center;
				color: rgba(255, 255, 255, 0.4);
				// overflow: hidden;
				// white-space: nowrap;
			}
		}

		&-right {
			width: 20%;
			display: flex;
			justify-content: center;
			align-items: flex-end;

			&-tagpage {
				width: 34upx;
				height: 37upx;
				margin-left: 30upx;
			}

			&-screen {
				width: 34upx;
				height: 37upx;
				margin-left: 30upx;
			}

			// background-color: red;
		}
	}

	.newcolor {
		color: #fff;
		font-size: 44upx;
	}

	.top {
		width: 700upx;
		height: 214upx;
		margin: 0 auto;
		margin-top: 80upx;
		border-radius: 16upx;
		// border: 1px solid #F37B1D;
		display: flex;
		justify-content: space-between;
		align-items: center;

		&-img {
			width: 170upx;
			height: 123upx;
			// border: 1px soliod #aa55ff;
			background-repeat: no-repeat;
			background-size: 100% 100%;

			&-p {
				font-size: 15px;
				text-align: center;
				line-height: 123upx;
				font-weight: bold;
				font-family: "PingFang-SC-Bold";

			}
		}
	}

	.newcolor/deep/.cu-bar.fixed {
		position: fixed;
		width: 100%;
		top: 22px;
		z-index: 1024;
	}

	.scrolls-y {
		width: 100%;
		height:auto;
		// white-space: nowrap;
	}
</style>
