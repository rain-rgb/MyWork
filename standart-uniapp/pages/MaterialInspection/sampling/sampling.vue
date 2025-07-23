<template>
	<view id="samplingadd">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">取样委托</block>
		</cu-custom>
		<view class="main">
			<view class="main-item">
				<view class="main-item-name">工程名称：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入工程名称" v-model="fromdata.gcmc"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">施工部位：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入施工部位" v-model="fromdata.sgbw"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">材料类型：</view>
				<view class="main-item-input">
					<dict dictCode="nodeType" title="请选择材料类型" @choose="ChooseType">
					</dict>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">委托编号：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入委托编号" v-model="fromdata.wtbh"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">样品名称：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入样品名称" v-model="fromdata.ypmc"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">产地厂名：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入产地厂名" v-model="fromdata.cdcm"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">试样数量：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入试样数量" v-model="fromdata.sysl"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">规格种类：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入规格种类" v-model="fromdata.ggzl"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">取样地点：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入取样地点" v-model="fromdata.qydd"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">代表数量(吨)：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入代表数量(吨)" v-model="fromdata.dbsl"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">样品描述：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入样品描述" v-model="fromdata.ypms"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">取样见证人：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入取样见证人" v-model="fromdata.jzr"></u--input>
				</view>
			</view>
			<view style="height: 30upx;"></view>
		</view>
		<view class="body-btn">
			<u-button @click="submitEntrust" :disabled="disabled"
				style="width:90px;border-radius: 10px;color: #fff;background-color: #387FFD;" text="提交委托">
			</u-button>
			<u-button @click="show = true" :disabled="!disabled"
				style="width:90px;border-radius: 10px;color: #fff;background-color: #e9ac31;" text="见证取样">
			</u-button>
		</view>
		<view class="imgs" v-show="imgList.length>0">
			<view @click="ViewImage(i)" v-for="(item,i) in imgList" :key="i">
				<image style="width: 100px; height: 100px;" mode="aspectFill" :src="item"></image>
			</view>
		</view>
		<view style="height: 40upx;"></view>

		<u-modal :show="show" title="请选择图片" confirmText="提交" showCancelButton @cancel="cancelsh" @confirm="confirmsh">
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
</template>

<script>
	import dict from '../../component/dict/dict.vue'
	import nowtime from '../../../common/util/nowtime.js'
	export default {
		components: {
			dict
		},
		data() {
			return {
				show: false,
				scantShow: false,
				disabled: false,
				imgList: [],
				samPic: [],
				trackList: '',
				maxImg: 1,
				nowdate: nowtime.date(),
				materialsObj: {},
				fromdata: {
					nodeType: '', //试验类型
					wtbh: '', //委托编号
					ypmc: '', //样品名称
					cdcm: '', //产地厂名
					sysl: '', //试样数量
					ggzl: '', //规格种类
					qydd: '', //取样地点
					dbsl: '', //代表数量(吨)
					ypms: '', //样品描述
					jzr: '', //取样见证人
					gcmc: '', //工程名称
					sgbw: '', //施工部位
				},
				smcode: [],
			};
		},
		onLoad(options) {
			if (options.item) {
				this.materialsObj = JSON.parse(options.item)
			}
		},
		mounted() {
			// console.log(this.$config.apiUrl)
			let a = nowtime.date().split("-").join('')
			let b = nowtime.time().split(":").join('')
			this.fromdata.wtbh = 'WT-' + a + b
			this.getImg()
		},
		methods: {
			//获取见证取样图片
			getImg() {
				let params = {
					taizhangid: this.materialsObj.id,
					wtbh: this.fromdata.wtbh,
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
			submitEntrust() {
				if (!this.fromdata.gcmc) {
					uni.showToast({
						title: '请输入工程名称',
						icon: 'none'
					})
					return
				}
				if (!this.fromdata.sgbw) {
					uni.showToast({
						title: '请输入施工部位',
						icon: 'none'
					})
					return
				}
				if (!this.fromdata.nodeType) {
					uni.showToast({
						title: '请选择材料类型',
						icon: 'none'
					})
					return
				}
				if (!this.fromdata.wtbh) {
					uni.showToast({
						title: '请输入委托编号',
						icon: 'none'
					})
					return
				}
				if (!this.fromdata.ypmc) {
					uni.showToast({
						title: '请输入样品名称',
						icon: 'none'
					})
					return
				}
				if (!this.fromdata.cdcm) {
					uni.showToast({
						title: '请输入产地厂名',
						icon: 'none'
					})
					return
				}
				if (!this.fromdata.sysl) {
					uni.showToast({
						title: '请输入试样数量',
						icon: 'none'
					})
					return
				}
				if (!this.fromdata.ggzl) {
					uni.showToast({
						title: '请输入规格种类',
						icon: 'none'
					})
					return
				}
				if (!this.fromdata.qydd) {
					uni.showToast({
						title: '请输入取样地点',
						icon: 'none'
					})
					return
				}
				if (!this.fromdata.dbsl) {
					uni.showToast({
						title: '请输入代表数量',
						icon: 'none'
					})
					return
				}
				if (!this.fromdata.ypms) {
					uni.showToast({
						title: '请输入样品描述',
						icon: 'none'
					})
					return
				}
				if (!this.fromdata.jzr) {
					uni.showToast({
						title: '请输入取样见证人',
						icon: 'none'
					})
					return
				}
				let params = {
					gongchengmingchen: this.fromdata.gcmc,
					shigongbuwei: this.fromdata.sgbw,
					state: this.fromdata.nodeType,
					weituobianhao: this.fromdata.wtbh,
					yangpingname: this.fromdata.ypmc,
					chandichangming: this.fromdata.cdcm,
					shiyangnum: this.fromdata.sysl,
					guigezhonglei: this.fromdata.ggzl,
					quyangdidian: this.fromdata.qydd,
					daibiaonum: this.fromdata.dbsl,
					yangpingmiaoshu: this.fromdata.ypms,
					quyangjianzhengren: this.fromdata.jzr,
					tzid: this.materialsObj.id,
					pici: this.materialsObj.pici,
					createtime: nowtime.date() + ' ' + nowtime.time(),
					cailiaono: this.materialsObj.cailiaono,
					jinchangshijian: this.materialsObj.jinchangshijian,
					jingzhongt: this.materialsObj.jingzhongt,
					shebeibianhao: this.materialsObj.shebeibianhao
				}
				this.$http.post('/wztaizhangweituodan/wztaizhangweituodan/add', params).then(res => {
					if (res.data.code == 200) {
						uni.showToast({
							title: "提交委托成功",
							icon: "none"
						})
						this.disabled = true;
					}
				})
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
					taizhangid: this.materialsObj.id,
					qypic: this.trackList,
					qrcode: this.smcode[0],
					wtbh: this.fromdata.wtbh,
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
						this.getImg()
						this.show = false
					} else {
						uni.showToast({
							title: "提交失败，请重新提交",
							icon: "none"
						})
					}
				})
			},
			//材料类型
			ChooseType(choosevalue) {
				// console.log(choosevalue, "材料类型")
				this.fromdata.nodeType = choosevalue
			},
			//选择图片
			selectImg(uploadFilenames) {
				this.trackList = uploadFilenames.toString()
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
	#samplingadd {
		width: 100%;
		// height: 100vh;
		background-color: #F3F5FE;

		.main {
			width: 690upx;
			// height: 754upx;
			margin: 0 auto;
			margin-top: 30upx;
			background-color: white;
			border-radius: 16upx;

			.tags {
				display: flex;
				flex-wrap: wrap;
			}

			.mainnew {
				color: #333333;
				font-size: 30upx;
				font-weight: bold;
			}

			&-item {
				display: flex;
				align-items: center;
				padding-top: 30upx;

				&-name {
					flex: 4;
					font-size: 30upx;
					text-align: right;
					color: #4C5971;
				}

				&-input {
					flex: 8;
					margin-right: 30upx;
					background-color: #F6F9FC;
					border-radius: 10upx;
				}
			}
		}

		.body-btn {
			width: 690rpx;
			margin: 15rpx auto;
			padding: 15rpx 0;
			background-color: white;
			border-radius: 16rpx;
			display: flex;
		}

		.btn {
			width: 690upx;
			margin: 0 auto;
			margin-top: 30upx;
			display: flex;

			.btnclass {
				width: 30%;
			}
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

		.imgs {
			width: 690rpx;
			margin: 15rpx auto;
			padding: 15rpx 0;
			background-color: white;
			border-radius: 16rpx;
			display: flex;
			justify-content: space-evenly;
			flex-wrap: wrap;
		}
	}
</style>
