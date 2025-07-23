<template>
	<view class="wrap">
		<view class="news">
		</view>
		<view class="news-activity" v-for="(item,index) in messagelist" :key="index" @click="messageclick(item)">
			<view class="news-activity-img">
				<u--image :showLoading="true" :src="item.url" width="90px" height="96px" shape="square" radius="10px">
				</u--image>
			</view>
			<view class="news-activity-con">
				<view class="news-activity-con-title">{{item.name}}</view>
				<view class="news-activity-con-time">
					<view class="img">
						{{item.sysOrgCode_dictText}}
					</view>
					<view class="wrap-font">
						<view>{{item.createTime.substring(5)}}</view>
					</view>
				</view>
			</view>
		</view>
		<view class="news-video" v-for="(video,index) in messagevideo" :key="index">
			<view class="news-video-img">
				<video id="myVideo" object-fit="cover" muted autoplay
					style="width: 342px;height: 137px;border-radius: 10px;" :src="video.url" controls="true"></video>
			</view>
			<view class="news-video-title">{{video.name}}</view>
			<view class="news-activity-con-text">{{video.description}}</view>
			<view class="news-video-time">
				<text style="float: right; margin-right: 14upx;">{{video.createTime}}</text>
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
		props: [
			"messagelist",
			"messagevideo",
		],
		methods: {
			messageclick(m) {
				console.log(m, '信息中心')
				this.$emit('messageclick', m);
			},
		}
	}
</script>

<style lang="less" scoped>
	.wrap {
		width: 100%;
		margin: 0 auto;
		margin-top: 30upx;

		.news {
			// width: 690upx;
			// margin: 0 auto;
			// margin-top: 30upx;

			&-title {
				display: flex;
				justify-content: space-between;
				align-items: center;

				&-name {
					color: #333333;
					font-size: 36upx;
					font-weight: 600;
				}

				&-more {
					display: flex;
					align-items: center;
					font-size: 24upx;
					color: #999999;
				}
			}

			&-con {
				margin-top: 30upx;

				&-item {
					margin-top: 30upx;
					display: flex;

					&-but {
						width: 130upx;
						padding: 0 10upx;
						height: 35upx;
						background-color: rgba(74, 127, 255, .1);
						font-size: 22upx;
						color: #387FFD;
						text-align: center;
					}

					&-des {
						width: 500upx;
						margin-left: 18upx;
						font-size: 26upx;
						color: #333333;
					}
				}
			}

			&-activity {
				height: 230upx;
				width: 700upx;
				border-radius: 16upx;
				margin: 0 auto;
				margin-top: 30upx;
				background-color: white;
				display: flex;
				align-items: center;
				justify-content: space-around;

				&-img {
					width: 180upx;
					height: 200upx;
					// margin-left: 16upx;
					// background-color: #1CBBB4;
					border-radius: 16upx;
				}

				&-con {
					height: 200upx;
					// margin-left: 20upx;
					color: #333333;

					// background-color: red;
					&-title {
						font-size: 28upx;
						font-weight: 600;
						text-align: left;
						width: 239px;
						height: 70px;
					}

					&-time {
						width: 500upx;
						// border: 1px solid darkgoldenrod;
						display: flex;
						flex-direction: row;
						justify-content: start;
						align-items: center;

						.img {
							width: 100px;
							height: 30px;
							line-height: 30px;
							padding: 0 10px;
							// border: 1px solid forestgreen;
						}

						.wrap-font {
							width: 230px;
							height: 30px;
							line-height: 30px;
							padding: 0 30px;
							// border: 1px solid deeppink;
						}
					}

					&-text {
						margin-top: 16upx;
						font-size: 24upx;
						display: -webkit-box;
						overflow: hidden;
						/*超出隐藏*/
						text-overflow: ellipsis;
						/*隐藏后添加省略号*/
						-webkit-box-orient: vertical;
						-webkit-line-clamp: 2; //想显示多少行
					}
				}
			}

			&-video {
				height: 500upx;
				border-radius: 16upx;
				margin-top: 30upx;
				background-color: white;
				color: #333333;

				&-img {
					height: 280upx;
					// background-color: #1CBBB4;
					border-radius: 16upx;
					// border: 1px solid navajowhite;
				}

				&-title {
					margin-top: 20upx;
					font-size: 28upx;
					font-weight: 600;
				}

				&-text {
					margin-top: 16upx;
					font-size: 24upx;
				}
			}
		}
	}
</style>
