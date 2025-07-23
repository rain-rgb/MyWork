<template>
	<!-- 到场审核详情 -->
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">到场审核详情</block>
		</cu-custom>
		<view class="section">
			<view class="section-text">
				<view>送货任务单:<span>{{listdata.shrwdh}}</span></view>
				<view>供应商:<span>{{listdata.ghdw}}</span></view>
				<view>目的地:<span>{{listdata.mdd}}</span></view>
				<view>车牌号:<span>{{listdata.cph}}</span></view>
				<view>材料名:<span>{{listdata.cailiao}}</span></view>
				<view>规格:<span>{{listdata.guige}}</span></view>
				<view>数量:<span>{{listdata.jcsl}}</span></view>
				<view>司机名称:<span>{{listdata.fzr}}</span></view>
				<view class="imgs">
					<view @click="ViewImage(0)">
						<image style="width: 100px; height: 100px;" mode="aspectFill" :src="listdata.jyimgfile"></image>
						<view class="image-title">检验报告</view>
					</view>
					<view @click="ViewImage(1)">
						<image style="width: 100px; height: 100px;" mode="aspectFill" :src="listdata.imgfile"></image>
						<view class="image-title">上锁图</view>
					</view>
				</view>
				<u-button @click="check" style="width:200px;border-radius: 10px;color: #fff;background-color: #F08322;"
					text="审核">
				</u-button>
			</view>
		</view>
		<u-modal :show="show" title="请选择审核" showCancelButton @cancel="cancelsh" @confirm="confirmsh">
			<view class="wrap-img">
				<view class="upimg">
					<my-image-upload :uploadFilenames="trackImg" @input="selectImg" label="轨迹图片：">
					</my-image-upload>
				</view>
				<view class="examine">
					<view class="examine-title">
						审批结果：
					</view>
					<u-radio-group v-model="value">
						<u-radio :customStyle="{marginLeft: '15px'}" v-for="(item, index) in radiolist" :key="index"
							:label="item.name" :name="item.name" @change="radioChange">
						</u-radio>
					</u-radio-group>
				</view>
			</view>

		</u-modal>
	</view>
</template>

<script>
	import rawmaterialapi from '../../../api/rawmaterial.js'
	import nowtime from '../../../common/util/nowtime.js'
	export default {
		name: 'checkdetails',
		components: {},
		data() {
			return {
				show: false,
				listdata: {},
				imgList: [],
				trackImg: [],
				trackList: '',
				value: '合格',
				radiolist: [{
						name: '合格',
						disabled: false
					},
					{
						name: '不合格',
						disabled: false
					}
				],
				times: nowtime.date() + ' ' + nowtime.time()
			}
		},
		onLoad(options) {
			this.listdata = JSON.parse(options.item)
			this.imgList.push(this.listdata.jyimgfile, this.listdata.imgfile)
		},
		methods: {
			//审核
			check() {
				this.show = true
			},
			ViewImage(index) {
				uni.previewImage({
					urls: this.imgList,
					current: index,
				});
			},
			selectImg(uploadFilenames) {
				this.trackList = uploadFilenames.toString()
			},
			cancelsh() {
				this.trackImg = []
				this.show = false
			},
			confirmsh() {
				if (!this.trackList) {
					uni.showToast({
						title: "请添加轨迹图片",
						icon: 'none'
					})
					return false
				}
				let params = {
					reviewtime: this.times,
					id: this.listdata.id,
					reviewer: this.$store.getters.username,
					sfhg: this.value,
					gjtp: this.trackList
				}
				rawmaterialapi.elockEdit(params).then(res => {
					if (res.data.success) {
						uni.showToast({
							title: "审核成功",
							icon: 'none'
						})
						setTimeout(() => {
							this.show = false
							uni.navigateBack({
								delta: 1
							});
						}, 500);
					}
				})
			},
			radioChange(n) {
				this.value = n
			},
		}
	}
</script>

<style lang="less" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FE;

		.section {
			width: 700upx;
			height: auto;
			border-radius: 10px;
			margin: 0 auto;
			background-color: #fff;

			.section-text {
				color: #9299A8;
				font-size: 30upx;
				padding: 15px 15px;
				line-height: 55upx;
				margin: 10px 0;

				span {
					color: #4C5971;
					font-size: 30upx;
					font-family: 'PingFang-SC-Medium';
					padding: 0 15upx;
				}

				.imgs {
					display: flex;
					justify-content: space-evenly;

					.image-title {
						text-align: center;
					}
				}
			}
		}
	}

	.wrap-img {
		width: 540upx;
		height: 180px;
		margin: 0 auto;

		.upimg {
			width: 540upx;
			height: 120px;
		}

		.examine {
			display: flex;

			.examine-title {
				padding: 30rpx 10rpx;
				font-size: 28rpx;
			}
		}
	}
</style>
