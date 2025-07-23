<template>
	<view id="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">原材料批次详情</block>
		</cu-custom>
		<view class="section">
			<view class="section-text">
				<view class="title">
					<view class="round"></view>
					<view class="title-font">基础信息</view>
				</view>
				<view class="">
					工程名称:<span>{{this.entrustObj.gongchengmingchen!==null?this.entrustObj.gongchengmingchen:'暂无数据'}}</span>
				</view>
				<view class="">
					施工部位:<span>{{this.entrustObj.shigongbuwei!==null?this.entrustObj.shigongbuwei:'暂无数据'}}</span>
				</view>
				<view class="">
					材料类型:<span>{{this.entrustObj.state!==null?this.entrustObj.state:'暂无数据'}}</span>
				</view>
				<view class="">
					委托编号:<span>{{this.entrustObj.weituobianhao!==null?this.entrustObj.weituobianhao:'暂无数据'}}</span>
				</view>
				<view class="">
					样品名称:<span>{{this.entrustObj.yangpingname!==null?this.entrustObj.yangpingname:'暂无数据'}}</span>
				</view>
				<view class="">
					产地厂名:<span>{{this.entrustObj.chandichangming!==null?this.entrustObj.chandichangming:'暂无数据'}}</span>
				</view>
				<view class="">
					试样数量:<span>{{this.entrustObj.shiyangnum!==null?this.entrustObj.shiyangnum:'暂无数据'}}</span>
				</view>
				<view class="">
					规格种类:<span>{{this.entrustObj.guigezhonglei!==null?this.entrustObj.guigezhonglei:'暂无数据'}}</span>
				</view>
				<view class="">
					取样地点:<span>{{this.entrustObj.quyangdidian!==null?this.entrustObj.quyangdidian:'暂无数据'}}</span>
				</view>
				<view class="">
					代表数量(吨):<span>{{this.entrustObj.daibiaonum!==null?this.entrustObj.daibiaonum:'暂无数据'}}</span>
				</view>
				<view class="">
					样品描述:<span>{{this.entrustObj.yangpingmiaoshu!==null?this.entrustObj.yangpingmiaoshu:'暂无数据'}}</span>
				</view>
				<view class="">
					取样见证人:<span>{{this.entrustObj.quyangjianzhengren!==null?this.entrustObj.quyangjianzhengren:'暂无数据'}}</span>
				</view>
				<u-button @click="show = true" v-show="this.entrustObj.wtlc==0"
					style="width:270px;border-radius: 10px;color: #fff;background-color: #e9ac31;" text="见证取样">
				</u-button>
				<view class="imgs" v-show="imgList.length>0">
					<view @click="ViewImage(i)" v-for="(item,i) in imgList" :key="i">
						<image style="width: 100px; height: 100px;" mode="aspectFill" :src="item"></image>
					</view>
				</view>
			</view>

			<u-modal :show="show" title="请选择图片" confirmText="提交" showCancelButton @cancel="cancelsh"
				@confirm="confirmsh">
				<view class="wrap-img">
					<view class="upimg">
						<my-image-upload :uploadFilenames="samPic" @input="selectImg" label="取样实图：" :maxImg="maxImg">
						</my-image-upload>
					</view>
					<u-button v-show="!scantShow" type="warning" @click="onScant" text="扫码"></u-button>
					<u-button v-show="scantShow" type="success" text="已扫码"></u-button>
				</view>
			</u-modal>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				show: false,
				scantShow: false,
				maxImg: 1,
				entrustObj: {},
				samPic: [],
				smcode: [],
				imgList: [],
				trackList: ''
			};
		},
		onLoad(options) {
			if (options.item) {
				this.entrustObj = JSON.parse(options.item)
			}
		},
		mounted() {
			this.getImg()
		},
		methods: {
			//选择图片
			selectImg(uploadFilenames) {
				this.trackList = uploadFilenames.toString()
			},
			//获取见证取样图片
			getImg() {
				let params = {
					taizhangid: this.entrustObj.tzid,
					wtbh: this.entrustObj.weituobianhao
				}
				this.$http.get('/wzquyangsy/wzquyangsy/list', {
					params
				}).then(res => {
					if (res.data.code == 200) {
						this.imgList = res.data.result.records.map(item => {
							return item.qypic
						})
					}
				})
			},
			ViewImage(i) {
				uni.previewImage({
					urls: this.imgList,
					current: i,
				});
			},
			cancelsh() {
				this.samPic = []
				this.smcode = []
				this.show = false
				this.scantShow = false; //扫码状态
			},
			confirmsh() {
				if (this.samPic.length == 0) {
					uni.showToast({
						title: '请上传图片',
						icon: 'none'
					})
					return
				}
				if (this.smcode.length == 0) {
					uni.showToast({
						title: '请扫描二维码',
						icon: 'none'
					})
					return
				}
				let params = {
					taizhangid: this.entrustObj.tzid,
					qypic: this.trackList,
					qrcode: this.smcode[0],
					wtbh: this.entrustObj.weituobianhao
				}
				this.$http.post('/wzquyangsy/wzquyangsy/add1', params).then(res => {
					if (res.data.code == 200) {
						uni.showToast({
							title: "见证取样提交成功",
							icon: "none"
						})
						this.samPic = []
						this.smcode = []
						this.scantShow = false; //扫码状态
						// this.getImg()
						this.show = false
					} else {
						uni.showToast({
							title: "提交失败，请重新提交",
							icon: "none"
						})
					}
				})
			},
			onScant() {
				var that = this;
				// 允许从相机和相册扫码
				uni.scanCode({
					scanType: ['qrCode'],
					success: function(res) {
						// console.log('条码类型：' + res.scanType);
						// console.log('条码内容：' + res.result);
						let a = res.result
						that.$http.post('/hntconsign/hntConsign/qrcodeyanzheng?code=' + a).then(result => {
							if (!result.data.success) {
								uni.showToast({
									title: result.data.message,
									icon: "none"
								})
								return
							} else {
								that.smcode.push(a)
								that.scantShow = true;
							}

							// if (that.smcode.includes(a)) {
							// 	uni.showToast({
							// 		title: '请勿重复扫描此二维码！！！',
							// 		icon: 'none'
							// 	})
							// 	return
							// }
							// that.smcode.push(a)
						})
					}
				});
			},
		}
	}
</script>

<style lang="scss" scoped>
	.wrap {
		width: 100%;
		height: auto;
	}

	.wrap-img {
		width: 540upx;
		height: 370rpx;
		margin: 0 auto;

		.upimg {
			width: 540upx;
			height: 240rpx;
		}
	}

	.section {
		width: 700rpx;
		height: auto;
		margin: 15px auto;

		.section-text {
			background-color: #fff;
			border-radius: 10px;
			color: #9299A8;
			font-size: 30upx;
			padding: 15px 15px;
			line-height: 55upx;
			margin: 10px 0;

			span {
				color: #4C5971;
				font-size: 30upx;
				font-family: 'PingFang-SC-Medium';
				padding: 10upx 15upx;
			}

			.yc-content {
				width: 100%;
				height: 140rpx;
				background-color: #F6F7FA;
				display: flex;
				align-items: center;
				padding-right: 20rpx;

			}

			.title {
				width: 176rpx;
				height: 34px;
				// border: 1px solid #18BC37;
				display: flex;
				flex-direction: row;
				justify-content: space-between;
				align-items: center;

				.round {
					width: 6px;
					height: 17px;
					background: #387FFD;
					border-radius: 6px;
				}

				.title-font {
					font-size: 18px;
					font-family: ' PingFang SC';
					font-weight: bold;
					color: #333333;
				}
			}

			.imgs {
				width: 100%;
				margin: 15rpx auto;
				padding: 15rpx 0;
				display: flex;
				justify-content: space-evenly;
				flex-wrap: wrap;
			}
		}
	}
</style>
