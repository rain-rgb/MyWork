<template>
	<view class="">
		<view class="lxy-content">
			<scroll-view scroll-x="true" class="content-scroll" scroll-with-animation :scroll-left="scrollLeft">
				<view v-for="(item, index) in tabBars" :key="index" class="scroll-item" @click="tabtap(index)">
					<text class="item-text" :class="tabIndex == index? 'active' : ''">{{item.name}}</text>
				</view>
			</scroll-view>
		</view>
		<!-- <view class="uni-tab-bar">
			<scroll-view class="uni-swiper-tab" scroll-x>
				<block v-for="(tab,index) in tabBars" :key="tab.id" :style="scrollStyle">
					<view class="swiper-tab-list" :class="{'active' : tabIndex==index}" @tap="tabtap(index)"
						:style="scrollItemStyle">
						{{tab.name}} {{tab.num?tab.num:""}}
						<view class="swiper-tab-line"></view>
					</view>
				</block>
			</scroll-view>
		</view> -->
		<!-- <swiper style="width: 200px;" class="swiper" display-multiple-items="3" previous-margin="30px" next-margin="30px" :indicator-dots="indicatorDots" :autoplay="autoplay"
			:interval="interval" :duration="duration">
			<swiper-item v-for="(tab,index) in tabBars" :key="tab.id">
				<view :class="{'active' : tabIndex==index}" class="" @tap="tabtap(index)">{{tab.name}}</view>
			</swiper-item>
		</swiper> -->
	</view>

</template>

<script>
	export default {
		data() {
			return {
				contentScrollW: 0, // 导航区宽度
				curIndex: 0, // 当前选中
				scrollLeft: 0, // 横向滚动条位置
				list3: [
					'https://cdn.uviewui.com/uview/swiper/swiper3.png',
					'https://cdn.uviewui.com/uview/swiper/swiper1.png',
					'https://cdn.uviewui.com/uview/swiper/swiper3.png',
					'https://cdn.uviewui.com/uview/swiper/swiper2.png',
					'https://cdn.uviewui.com/uview/swiper/swiper3.png',
					'https://cdn.uviewui.com/uview/swiper/swiper1.png',
					'https://cdn.uviewui.com/uview/swiper/swiper3.png',
				],
			}
		},
		props: {
			tabBars: Array,
			tabIndex: Number,
			scrollStyle: {
				type: String,
				default: ""
			},
			scrollItemStyle: {
				type: String,
				default: ""
			}
		},
		mounted() {
			this.getScrollW()
		},
		methods: {
			getScrollW() {
				const query = uni.createSelectorQuery().in(this);
				query.select('.content-scroll').boundingClientRect(data => {
					// 拿到 scroll-view 组件宽度
					this.contentScrollW = data.width
				}).exec();
				console.log(this.contentScrollW)
				query.selectAll('.scroll-item').boundingClientRect(data => {
					let dataLen = data.length;
					for (let i = 0; i < dataLen; i++) {
						//  scroll-view 子元素组件距离左边栏的距离
						this.tabBars[i].left = data[i].left;
						//  scroll-view 子元素组件宽度
						this.tabBars[i].width = data[i].width
					}
				}).exec()
			},
			// 选择标题
			tabtap(index) {
				this.curIndex = index;
				this.scrollLeft = this.tabBars[index].left - this.contentScrollW / 2 + this.tabBars[index].width / 2;
				this.$emit('tabtap', index)
			},
			scroll: function(e) {
				console.log(e)
				this.old.scrollTop = e.detail.scrollTop
			},
			//点击切换导航
			// tabtap(index) {
			// 	// this.tabIndex = index;
			// 	this.$emit('tabtap', index)
			// }
		}
	}
</script>　

<style lang="scss" scoped>
	.lxy-content {
		width: 100%;
		height: 100rpx;
		// margin-top: 50rpx;
		box-sizing: border-box;

		.content-scroll {
			height: 100rpx;
			white-space: nowrap;

			.scroll-item {
				height: 100rpx;
				padding: 0 20rpx;
				display: inline-block;
				text-align: center;

				.item-text {
					font-size: 30rpx;
					line-height: 100rpx;

					&.active {
						color: #1468FF;
					}
				}
			}
		}
	}

	.nav .cu-item {
		height: 50upx;
		line-height: 50upx;
	}

	.cu-tag.sm {
		padding: 0px 10upx;
	}

	.trade {
		width: 100%;
		color: #007AFF;
		overflow: auto;
	}

	.trade view {
		text-align: center;
		padding-left: 25upx;
		width: 30%;
		float: left;
	}

	.trade .texts.active {
		border-bottom: 8upx solid #F0AD4E;
	}

	.uni-swiper-tab {
		border-bottom: 1upx solid #EEEEEE;
	}

	.swiper-tab-list {
		color: #969696;
		font-weight: bold;
	}

	.uni-tab-bar .active {
		color: #343434;
	}

	.active .swiper-tab-line {
		border-bottom: 6upx solid #FEDE33;
		width: 70upx;
		margin: auto;
		border-top: 6upx solid #FEDE33;
		border-radius: 20upx;
	}
</style>
