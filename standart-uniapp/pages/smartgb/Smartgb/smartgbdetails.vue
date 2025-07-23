<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">过磅详情</block>
		</cu-custom>
		<view class="wrap-section">
			<!-- <view class="wrap-section-name">毛重:<span>{{item.maozhong}}</span></view>
			<view class="wrap-section-name">皮重:<span>{{item.pizhong}}</span></view>
			<view class="wrap-section-name">净重:<span>{{item.jingzhong}}</span></view>
			<view class="wrap-section-name">扣重:<span>{{item.kouzhong}}</span></view>
			<view class="wrap-section-name">扣率:<span>{{item.koulv}}</span></view>
			<view class="wrap-section-name">称重偏差:<span>{{item.chengzhongpiancha}}</span></view> -->
			<view class="wrap-section-name">设备名称:<span>{{item.shebeibianhao_dictText}}</span></view>
			<view class="wrap-section-name">司磅员:<span>{{item.sibangyuan}}</span></view>
			<view class="wrap-section-name">供应商单位:<span>{{item.gongyingshangdanweibianma_dictText}}</span></view>
			<view class="wrap-section-name">毛重(吨):<span>{{item.maozhongt}}</span></view>
			<view class="wrap-section-name">皮重(吨):<span>{{item.pizhongt}}</span></view>
			<view class="wrap-section-name">运输单位:<span>{{item.yunshudanwei}}</span></view>
			<view class="wrap-section-name">料仓名称:<span>{{item.lcbianhao_dictText}}</span></view>
			<view class="wrap-section-name">净重(吨):<span>{{item.jingzhongt}}</span></view>
			<view class="wrap-section-name">是否合格:<span>{{item.shifouhege}}</span></view>
		</view>
		<view class="wrap-title">
			<view class="wrap-title-yx">
				进场影像
			</view>
			<view class="wrap-title-jcyx">
				<view class="wrap-title-jcyx-left">
					<view class="wrap-title-jcyx-left-jctj">
						<image class="image" :showLoading="true" v-if='item.ccbfpic !==""'
							:src="'http://web.traiot.cn/docs/wz/'+item.ccbfpic"
							@tap="previewImage('http://web.traiot.cn/docs/wz/'+item.ccbfpic)" mode="widthFix">
						</image>
						<view  class="zwlist" v-else>暂无数据</view>
					</view>
				</view>
				<view class="wrap-title-jcyx-left">
					<view class="wrap-title-jcyx-left-jctj">
						<image class="image" :showLoading="true" v-if='item.cccppic !==""'
							:src="'http://web.traiot.cn/docs/wz/'+item.cccppic"
							@tap="previewImage('http://web.traiot.cn/docs/wz/'+item.cccppic)" mode="widthFix">
						</image>
						<view class="zwlist" v-else>暂无数据</view>
					</view>
				</view>
				<view class="wrap-title-jcyx-left">
					<view class="wrap-title-jcyx-left-jctj">
						<image class="image" :showLoading="true" v-if='item.ccgkpic !==""'
							:src="'http://web.traiot.cn/docs/wz/'+item.ccgkpic"
							@tap="previewImage('http://web.traiot.cn/docs/wz/'+item.ccgkpic)" mode="widthFix">
						</image>
						<view class="zwlist" v-else>暂无数据</view>
					</view>
				</view>
				<view class="wrap-title-jcyx-left">
					<view class="wrap-title-jcyx-left-jctj">
						<image class="image" :showLoading="true" v-if='item.cchcppic !==""'
							:src="'http://web.traiot.cn/docs/wz/'+item.cchcppic"
							@tap="previewImage('http://web.traiot.cn/docs/wz/'+item.cchcppic)" mode="widthFix">
						</image>
						<view class="zwlist" v-else>暂无数据</view>
					</view>
				</view>

				<view class="wrap-title-jcyx-left">
					<view class="wrap-title-jcyx-left-jctj">
						<image class="image" :showLoading="true" v-if='item.jcbfpic !==""'
							:src="'http://web.traiot.cn/docs/wz/'+item.jcbfpic"
							@tap="previewImage('http://web.traiot.cn/docs/wz/'+item.jcbfpic) " mode="widthFix">
						</image>
						<view class="zwlist" v-else>暂无数据</view>
					</view>
				</view>
				<view class="wrap-title-jcyx-left">
					<view class="wrap-title-jcyx-left-jctj">
						<image class="image" :showLoading="true" v-if='item.jccppic !=="" '
							:src="'http://web.traiot.cn/docs/wz/'+item.jccppic"
							@tap="previewImage('http://web.traiot.cn/docs/wz/'+item.jccppic)" mode="widthFix">
						</image>
						<view class="zwlist" v-else>暂无数据</view>
					</view>
				</view>
				<view class="wrap-title-jcyx-left">
					<view class="wrap-title-jcyx-left-jctj">
						<image class="image" :showLoading="true" v-if='item.jcgkpic !==""'
							:src="'http://web.traiot.cn/docs/wz/'+item.jcgkpic"
							@tap="previewImage('http://web.traiot.cn/docs/wz/'+item.jcgkpic)" mode="widthFix">
						</image>
						<view class="zwlist" v-else>暂无数据</view>
					</view>
				</view>
				<view class="wrap-title-jcyx-left">
					<view class="wrap-title-jcyx-left-jctj">
						<image class="image" :showLoading="true" v-if='item.jchcppic !==""'
							:src="'http://web.traiot.cn/docs/wz/'+item.jchcppic"
							@tap="previewImage('http://web.traiot.cn/docs/wz/'+item.jchcppic)" mode="widthFix">
						</image>
						<view class="zwlist" v-else>暂无数据</view>
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
				item: {}
			}
		},
		onLoad(options) {
			this.item = JSON.parse(options.item)
			console.log(this.item)
		},
		methods: {
			previewImage(img) {
				let images = [];
				images.push(img);
				uni.previewImage({
					current: 0,
					urls: images,
				})
			},
		}
	}
</script>

<style scoped lang="scss">
	.wrap {
		width: 100%;
		height: auto;
		background-color: #f2f5fe;

		&-title {
			width: 700upx;
			height: auto;
			margin: 20px auto;

			&-yx {
				width: 700upx;
				height: 70upx;
				// border: 1px solid forestgreen;
				font-size: 18px;
				color: #000;
				line-height: 70upx;
				padding: 0 10px;
				font-weight: bold;
			}

			&-jcyx {
				width: 700upx;
				height: auto;
				height: auto;
				display: flex;
				flex-direction: row;
				justify-content: space-around;
				flex-wrap: wrap;

				&-left {
					width: 320upx;
					height: 180upx;
					border-radius: 10px;
					margin: 5px 0;
					// display: flex;
					// // flex-direction: column;
					// justify-content: center;
					// align-items: center;

					// border: 1px solid chartreuse;

					&-jctj {
						width: 315upx;
						height: 180upx;
						border: 1px solid #fff;
						border-radius: 10px;
						.image {
							width: 315upx !important;
							height: 180upx !important;
							border-radius: 10px;
						}
						.zwlist{
							text-align: center;
							line-height: 180upx;
						}
					}

					h2 {

						text-align: center;
					}
				}
			}
		}

		&-section {
			width: 700upx;
			height: auto;
			background-color: #fff;
			margin: 20px auto;
			border-radius: 10px;

			&-name {
				padding: 5px 15px;
				color: #9299bb;
				font-size: 15px;

				span {
					color: #4C5971;
					font-size: 15px;
					font-family: 'PingFang-SC-Medium';
					padding: 0 10px;
					// font-weight:bold;
				}
			}
		}
	}
</style>
