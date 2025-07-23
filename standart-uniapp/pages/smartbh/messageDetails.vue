<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">资讯详情</block>
		</cu-custom>
		<view class="section">
			<h2>{{item.name}}</h2>
			<view class="wrap-float">
				<view class="img">
					<u--image :showLoading="true" :src="item.url" width="50px" height="50px" shape="circle"
						radius="10px"></u--image>
				</view>

				<view class="wrap-font">
					<view>{{item.sysOrgCode_dictText}}</view>
					<view>{{item.createTime.substring(5,11)}}</view>
				</view>

			</view>
			<view class="section-news">
				{{item.description}}
			</view>
			<view class="bg">
				<image :src="item.url" ></image>
			</view>
		</view>
		<!-- <view class="article">
			<u-skeleton
				    rows="1"
				    title='false'
					loading
					rowsHeight='100'
				></u-skeleton>
		</view> -->
	</view>
</template>

<script>
	export default {
		name: 'messageDetails',
		data() {
			return {
				item: {}
			}
		},
		onLoad(options) {
			this.item = JSON.parse(options.item)
			console.log(this.item)
			console.log(options.id)
		},
		methods:{
		}
	}
</script>

<style lang="scss" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FE;

		.section {
			width: 100%;
			height: auto;

			.wrap-float {
				width: 100%;
				// border: 1px solid firebrick;
				display: flex;
				flex-direction: row;
				justify-content: start;
                align-items:center;
				padding: 15px 0;
				.img {
					width:50px;
					height:50px;
					padding: 0 10px;
					// border: 1px solid forestgreen;
				}

				.wrap-font {
					width: 230px;
					height:50px;
					line-height: 25px;
					padding: 0 30px;
					// border: 1px solid deeppink;
				}
			}

			h2 {
				text-align: center;
				font-family: cursive;
				font-weight: bold;
				padding: 10px 0;
			}

			&-news {
				color: #000;
				font-size: 28upx;
				text-align: center;
				padding: 25upx 15upx;
				line-height: 60upx;
			}
		}

		.article {
			margin: 0 auto;
			height: 100px;
			width: 100%;
			border: 1px solid red;
		}
		.bg{
			width: 86%;
			margin: 0 auto;
			// border: 1px solid darkgoldenrod;
		}
	}
</style>
