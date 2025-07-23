<template>
		<view>
	<view class="smartbh">
		<!--  工艺工序监测-->
		<view>
			<view class="smartbh-message">
				工艺工序监测
			</view>
		</view>
		<view class="smartbh-label">
			<view class="smartbh-label-all">
				<view class="smartbh-label-all-img" style="background-image: url(../../static/label/zljc.png);"></view>
				<view class="smartbh-label-all-title">智能张拉</view>
			</view>
			<view class="smartbh-label-all">
				<view class="smartbh-label-all-img" style="background-image: url(../../static/label/yj.png);"></view>
				<view class="smartbh-label-all-title">智能压浆</view>
			</view>
			<view class="smartbh-label-all">
				<view class="smartbh-label-all-img" style="background-image: url(../../static/label/dzgl.png);"></view>
				<view class="smartbh-label-all-title">墩柱盖梁养生</view>
			</view>
			<view class="smartbh-label-all">
				<view class="smartbh-label-all-img" style="background-image: url(../../static/label/yzgj.png);"></view>
				<view class="smartbh-label-all-title">预制构件养生</view>
			</view>
			<view class="smartbh-label-all">
				<view class="smartbh-label-all-img" style="background-image: url(../../static/label/snjb.png);"></view>
				<view class="smartbh-label-all-title">水泥搅拌桩</view>
			</view>
			<view class="smartbh-label-all">
				<view class="smartbh-label-all-img" style="background-image: url(../../static/label/gyxp.png);"></view>
				<view class="smartbh-label-all-title">高压旋喷桩</view>
			</view>
			<view class="smartbh-label-all">
				<view class="smartbh-label-all-img" style="background-image: url(../../static/label/lj.png);"></view>
				<view class="smartbh-label-all-title">路基填筑压实</view>
			</view>
			<view class="smartbh-label-all">
				<view class="smartbh-label-all-img" style="background-image: url(../../static/label/cjgc.png);"></view>
				<view class="smartbh-label-all-title">沉降观测</view>
			</view>
			<view class="smartbh-label-all">
				<view class="smartbh-label-all-img" style="background-image: url(../../static/label/lxlx.png);"></view>
				<view class="smartbh-label-all-title">连续梁线性监测</view>
			</view>
			
			<view class="after-add">
				<view class="smartbh-label-all-img"></view>
				<view class="smartbh-label-all-title"></view>
			</view>
			<view class="after-add">
				<view class="smartbh-label-all-img"></view>
				<view class="smartbh-label-all-title"></view>
			</view>
			<view class="after-add">
				<view class="smartbh-label-all-img"></view>
				<view class="smartbh-label-all-title"></view>
			</view>
			<view class="after-add">
				<view class="smartbh-label-all-img"></view>
				<view class="smartbh-label-all-title"></view>
			</view>
			<view class="after-add">
				<view class="smartbh-label-all-img"></view>
				<view class="smartbh-label-all-title"></view>
			</view>
			<view class="after-add">
				<view class="smartbh-label-all-img"></view>
				<view class="smartbh-label-all-title"></view>
			</view>
		</view>

		
	</view>
	<view class="smartbh">
				<!-- 智慧路面 -->
		<view>
			<view class="smartbh-message">
				智慧路面
			</view>
		</view>
		<view class="smartbh-label">
			<view class="smartbh-label-all">
				<view class="smartbh-label-all-img" style="background-image: url(../../static/label/sytj.png);"></view>
				<view class="smartbh-label-all-title">首页统计</view>
			</view>
			<view class="after-add">
				<view class="smartbh-label-all-img"></view>
				<view class="smartbh-label-all-title"></view>
			</view>
			<view class="after-add">
				<view class="smartbh-label-all-img"></view>
				<view class="smartbh-label-all-title"></view>
			</view>
			<view class="after-add">
				<view class="smartbh-label-all-img"></view>
				<view class="smartbh-label-all-title"></view>
			</view>
			<view class="after-add">
				<view class="smartbh-label-all-img"></view>
				<view class="smartbh-label-all-title"></view>
			</view>
			<view class="after-add">
				<view class="smartbh-label-all-img"></view>
				<view class="smartbh-label-all-title"></view>
			</view>
			<view class="after-add">
				<view class="smartbh-label-all-img"></view>
				<view class="smartbh-label-all-title"></view>
			</view>
		</view>
	</view>

		
	</view>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		methods: {

		}
	}
</script>

<style lang="scss" scoped>
	.smartbh {
		width: 99%;
		margin: 0 auto;
		/* height: 50%; */
		// flex: 1;
		// border: 1px solid #fff;
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
			margin:8px auto;
			display: flex;
			flex-wrap: wrap;
			justify-content: space-around;

			&-label:not(:nth-child(4n)) {
				margin-right: calc(4% / 3);
			}

			&-all {
				width: 150upx;
				height: 130upx;
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
			height: 0;
			padding: 0;
			margin: 0  //之前li设定值 0 之前li设定值;
		}
	}
</style>
