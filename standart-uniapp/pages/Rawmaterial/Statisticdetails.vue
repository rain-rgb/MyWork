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
					进场时间:<span>{{detailslist.jinchangshijian!==null?detailslist.jinchangshijian:'暂无数据'}}</span></view>
				<view class="">
					设备名称:<span>{{detailslist.shebeibianhao_dictText!==null?detailslist.shebeibianhao_dictText:'暂无数据'}}</span>
				</view>
				<view class="">
					料仓名称:<span>{{detailslist.lcbianhao_dictText!==null?detailslist.lcbianhao_dictText:'暂无数据'}}</span>
				</view>
				<view class="">
					材料名称:<span>{{detailslist.cailiaono_dictText !==null?detailslist.cailiaono_dictText:'暂无数据'}}</span>
				</view>
				<view class="">净重(吨):<span>{{detailslist.jingzhongt !==null?detailslist.jingzhongt:'暂无数据'}}</span>
				</view>
				<view class="">检验批次:<span>{{detailslist.pici !==null?detailslist.pici:'暂无数据'}}</span></view>
				<view class="">
					规格类型:<span>{{detailslist.guigexinghao !==null?detailslist.guigexinghao:'暂无数据'}}</span></view>
				<view class="">
					是否已取样:<span
						:style="detailslist.isquyang ==0?'color:green;font-weight:bold;font-family:cursive;':detailslist.isquyang==1?'color:orange;font-weight:bold;font-family:cursive':'color:red;font-weight:bold;font-family:cursive'">{{detailslist.isquyang ==0?'未取样':detailslist.isquyang ==1?'已取样':'其他'}}</span>
				</view>
				<!-- <view class="">是否合格:<span
						:style="detailslist.jianyanstate == 0?'color:orange;font-weight:bold;font-family:cursive':detailslist.jianyanstate ==1?'color:green;font-weight:bold;font-family:cursive':'color:red;font-weight:bold;font-family:cursive'">{{detailslist.jianyanstate ==0?'未检验':detailslist.jianyanstate ==1?'合格':'不合格'}}</span>
				</view> -->
			</view>
		</view>
		<view class="section">
			<view class="section-table">
				<view class="title">
					<view class="round"></view>
					<view class="title-font">原材料进场明细</view>
				</view>
				<uni-table stripe emptyText="暂无更多数据">
					<!-- 表头行 -->
					<uni-tr>
						<uni-th align="center">供应商单位</uni-th>
						<uni-th align="center">进场时间</uni-th>
						<uni-th align="center">出场时间</uni-th>
						<uni-th align="center">车牌号</uni-th>
						<uni-th align="center">净重(吨)</uni-th>
					</uni-tr>
					<!-- 表格数据行 -->
					<uni-tr v-for="(tablelist,index) in listdata" :key="index">
						<uni-td align="center">{{tablelist.gongyingshangdanweibianma_dictText}}</uni-td>
						<uni-td align="center">{{tablelist.jinchangshijian}}</uni-td>
						<uni-td align="center">{{tablelist.chuchangshijian}}</uni-td>
						<uni-td align="center">{{tablelist.qianchepai}}</uni-td>
						<uni-td align="center">{{tablelist.jingzhongt}}</uni-td>
					</uni-tr>
				</uni-table>
			</view>
		</view>
		<view class="section">
			<view class="section-text">
				<view class="title" style="width: 212rpx;">
					<view class="round"></view>
					<view class="title-font">原材料批次</view>
				</view>
				<view class="yc-content" v-for="(item,index) in ycList" :key="index" @click="entrust(item)">
					<view :class="[item.wtlc==4?'yihg':item.wtlc==5?'bhgy':'wjcy']"></view>
					<view class="yc-msg">
						<view style="color: #333333;">{{item.weituobianhao}}</view>
						<view>{{item.cailiaono_dictText + item.guigezhonglei}}</view>
					</view>
					<u-icon color="#999999" size="16" name="arrow-right"></u-icon>
				</view>
			</view>
		</view>

		<view class="button-anniu">
			<!-- <button @click="openmodal"
				style="width:160px;height: 40px;border-radius: 10px;background-color:#FFF;line-height: 39px;">取样</button>
			<button @click="openmodal"
				style="width: 160px;height: 40px;border-radius: 10px;background-color:#387FFD;line-height: 39px;">检测报告</button> -->
			<button @click="openquyang" v-show="detailslist.isquyang == 0 && detailslist.jianyanstate == 0 "
				style="width:160px;height: 40px;border-radius: 10px;background-color:#FFF;line-height: 39px;color: #000;">取样</button>
			<button @click="openjiancha" v-show="detailslist.isquyang == 1"
				style="width: 160px;height: 40px;border-radius: 10px;background-color:#387FFD;line-height: 39px;color:#fff;">检测报告</button>
			<!-- 取样 -->
			<view class="screen" v-if="show" @confirm="confirm">
				<view class="screen-modal">
					<view class="model-title">
						原材料取样
						<view @click="closewindow" class=""
							style="background-image: url(../../static/shiti/cha.png); width: 13px;height: 13px;background-size: 100% 100%;position: relative;left: 300px;bottom:20px;">
						</view>
					</view>
					<!-- 上传图片 -->
					<view class="upimg">
						<my-image-upload :uploadFilenames="samPic" @input="ChooseImage" label="取样图上传：">
						</my-image-upload>
					</view>
					<!-- 上传图片 -->
					<view class="screen-modal-btn">
						<!-- <u-button text="取消" @click="confirm"></u-button> -->
						<u-button style="width:160px;border-radius: 10px;color: #fff;background-color: #387FFD;"
							text="取样" @click="submitqy"></u-button>
					</view>
				</view>
			</view>
			<!-- 检测报告 -->
			<view class="screen" v-if="showjiancha" @confirms="confirms">
				<view class="screen-modal">
					<view class="model-title">
						检测报告
						<view @click="closewindows" class=""
							style="background-image: url(../../static/shiti/cha.png); width: 13px;height: 13px;background-size: 100% 100%;position: relative;left: 300px;bottom:20px;">
						</view>
					</view>
					<view class="group">
						<text class="jielun">报告结论:</text>
						<view class="content">
							<picker @change="Cblevel" :value="index" :range="cblevelname">
								<text class="">
									<!-- {{pdresult== ''?"判定结果":pdresult}} -->
									{{index>-1?cblevelname[index]:'请选择判定条件'}}
								</text>
							</picker>
						</view>
						<!-- <view class="btn" @click="Choose">搜索</view> -->
					</view>
					<!-- 上传图片 -->
					<view class="upimg">
						<my-image-upload :uploadFilenames="samPic" @input="ChooseImage" label="鉴定条件：">
						</my-image-upload>
					</view>
					<!-- 上传图片 -->
					<view class="screen-modal-btn">
						<u-button style="width:160px;border-radius: 10px;color: #fff;background-color: #387FFD;"
							text="检测报告" @click="submitjc"></u-button>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>
<script>
	import MyImageUpload from "../component/imgupload/my-image-upload.vue"
	import dict from '../component/dict/dict.vue'
	import timeSelector from '@/components/wing-time-selector/wing-time-selector.vue';
	import Byspublic from '../component/Byspublic/Byspublic.vue'
	import api from '@/api/apis.js'
	export default {
		components: {
			dict,
			timeSelector,
			Byspublic,
			MyImageUpload
		},
		data() {
			return {
				detailslist: '',
				listdata: '',
				show: false,
				showjiancha: false,
				fileList1: [],
				fileList: [],
				samPic: [],
				ycList: [],
				id: '',
				index: -1,
				pdjg: null,
				pdresult: '',
				cblevelname: [
					"合格",
					"不合格",
				],
			}
		},
		onLoad(options) {
			this.detailslist = JSON.parse(options.item)
			console.log(this.detailslist, 'yyyyyyyyyy');
			this.tableleist()
			// this.Choose()
			this.getYC()
		},
		created() {

		},
		methods: {
			ChooseImage(uploadFilenames) {
				console.log(uploadFilenames)
				this.fileList = uploadFilenames.toString()
			},
			submitqy() {
				let params = this.fileList
				this.$http.post('/wzquyangsy/wzquyangsy/add1', {
					params
				}).then(res => {
					if (res.data.success) {
						uni.showToast({
							title: '上传成功'
						})
						this.show = false
					} else {
						this.$tip.toast('上传失败')
					}
				})
			},

			Cblevel(e) {
				console.log(e)
				// if (e.detail.value ==0) {
				// 	e.detail.value='合格'
				// 	this.pdresult = e.detail.value
				// } else if(e.detail.value ==1){
				// 	e.detail.value = '不合格'
				// 	this.pdresult =e.detail.value
				// }
				this.index = e.detail.value
				console.log(this.index, 'kkkkkkkkkk');
				this.choosekey = 3
				this.choosevalue = e.detail.value
			},
			submitjc() {
				let params = this.fileList
				let jianyanstate = this.index
				this.$http.put('/wztaizhang/wztaizhang/edit', {
					params,
					jianyanstate
				}).then(res => {
					if (res.data.success) {
						uni.showToast({
							title: '上传成功'
						})
						this.showjiancha = false
					} else {
						this.$tip.toast('上传失败')
					}
				})

			},
			tableleist() {
				let numid = this.detailslist.id
				this.$http.get(`yclsl/wzycljinchanggb/list`, {
					params: {
						taizhangid: numid
					}
				}).then(res => {
					// console.log(res, 'ffffff');
					this.listdata = res.data.result.records
				})
			},
			getYC() {
				let numid = this.detailslist.id
				this.$http.get(`/wztaizhangweituodan/wztaizhangweituodan/list`, {
					params: {
						tzid: numid
					}
				}).then(res => {
					if (res.data.code == 200) {
						this.ycList = res.data.result.records
						console.log(this.ycList, 'getYC');
					}
				})
			},
			entrust(item) {
				uni.navigateTo({
					url: '/pages/MaterialInspection/sampling/entrustDetails?item=' + JSON.stringify(item)
				})
			},
			// 删除图片
			// deletePic(event) {
			// 	this[`fileList${event.name}`].splice(event.index, 1)
			// },
			// // 新增图片
			// async afterRead(event) {
			// 	// 当设置 mutiple 为 true 时, file 为数组格式，否则为对象格式
			// 	let lists = [].concat(event.file)
			// 	let fileListLen = this[`fileList${event.name}`].length
			// 	lists.map((item) => {
			// 		this[`fileList${event.name}`].push({
			// 			...item,
			// 			status: 'uploading',
			// 			message: '上传中'
			// 		})
			// 	})
			// 	for (let i = 0; i < lists.length; i++) {
			// 		const result = await this.uploadFilePromise(lists[i].url)
			// 		let item = this[`fileList${event.name}`][fileListLen]
			// 		this[`fileList${event.name}`].splice(fileListLen, 1, Object.assign(item, {
			// 			status: 'success',
			// 			message: '',
			// 			url: result
			// 		}))
			// 		fileListLen++
			// 	}
			// },
			// uploadFilePromise(url) {
			// 				return new Promise((resolve, reject) => {
			// 					let a = uni.uploadFile({
			// 						// method: "post",
			// 						url: 'http://z.traiot.cn/jeecg-boot/sys/common/upload', // 仅为示例，非真实的接口地址
			// 						filePath: url,
			// 						name: 'file',
			// 						formData: {
			// 							user: 'test'
			// 						},
			// 						success: (res) => {
			// 							console.log(res,'图片上传');
			// 							setTimeout(() => {
			// 								resolve(res.data.data)
			// 							}, 1000)
			// 						}
			// 					});
			// 				})
			// 			},
			openquyang() {
				this.show = true
			},
			openjiancha() {
				this.showjiancha = true
			},
			confirm() {
				this.show = false
			},
			confirms() {
				this.showjiancha = false
			},
			closewindow() {
				this.show = false
			},
			closewindows() {
				this.showjiancha = false
			}
		}
	}
</script>

<style lang="scss" scoped>
	.wrap {
		width: 100%;
		height: auto;
	}

	.section {
		width: 700rpx;
		height: auto;
		background-color: #fff;
		margin: 15px auto;
		border-radius: 10px;

		// display: flex;
		// flex-direction: row;
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
				padding: 10upx 15upx;
				// width:300rpx;
				// height:70rpx;
				//                background: #F6F9FC;
				// border-radius: 12px;
				// display: inline-block;

			}

			.yc-content {
				width: 100%;
				height: 140rpx;
				background-color: #F6F7FA;
				display: flex;
				align-items: center;
				padding-right: 20rpx;

				.yihg {
					background: url(../../static/shiti/yihg.png) no-repeat;
					background-size: 100%;
					width: 108rpx;
					height: 108rpx;
					margin: 20rpx 30rpx;
				}

				.wjcy {
					background: url(../../static/shiti/wjcy.png) no-repeat;
					background-size: 100%;
					width: 108rpx;
					height: 108rpx;
					margin: 20rpx 30rpx;
				}

				.bhgy {
					background: url(../../static/shiti/bhgy.png) no-repeat;
					background-size: 100%;
					width: 108rpx;
					height: 108rpx;
					margin: 20rpx 30rpx;
				}

				.yc-msg {
					flex: 1;
				}
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
		}

		.section-table {
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
				// width:300rpx;
				// height:70rpx;
				//                background: #F6F9FC;
				// border-radius: 12px;
				// display: inline-block;

			}

			.title {
				width: 280rpx;
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
		}
	}

	.button-anniu {
		display: flex;

	}

	.screen {
		position: fixed;
		background: rgba(000, 000, 000, .3);
		left: 0;
		top: 0;
		width: 100%;
		height: 100%;

		&-modal {
			background-color: #FFFFFF;
			position: absolute;
			top: 50%;
			left: (750upx - 690upx) / 2;
			width: 690upx;
			transform: translateY(-50%);
			box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.1);
			border-radius: 12upx;
			padding: 20upx;

			&-item {
				text-align: left;
				// width: 90%;
				margin-bottom: 30upx;

				&-name {
					color: #4C5971;
					font-size: 30upx;
					margin-bottom: 30upx;
				}

				&-input {}
			}

			&-btn {
				display: flex;
				margin-top: 10px;
			}
		}
	}

	.model-title {
		border-bottom: 1px solid #DDDDDD;
		width: 326px;
		height: 29px;
		font-size: 17px;
		font-weight: bold;
		color: #4C5971;
		font-family: PingFang SC;
	}

	.upimg {
		// border-bottom: 1px solid #DDDDDD;
		width: 326px;
		height: auto;
		margin-top: 25px;

		// border:1px solid blue;
		// display:flex;
		.uptext {
			width: 200px;
			height: 20px;
			// border:1px solid blue;
			font-size: 15px;
			font-family: "PingFang SC";
			font-weight: bold;
			color: #4C5971;
		}
	}

	.group {
		margin-top: 1px;
		display: flex;
		background: #FFFFFF;
		padding: 15px 8px;
		height: 40px;
		line-height: 29px;
		font-size: 14px;
	}

	.group .content {
		height: 30px;
		line-height: 30px;
		background-color: #F6F9FC;
		flex: 9;
		border-radius: 3px;
		padding-left: 5px;

	}

	.group .jielun {
		width: 67px;
		height: 10px;
		margin: 0 -7px;
		color: #4C5971;
		font-size: 13px;
		font-family: PingFang SC;
		font-weight: 500;
	}

	.group .content input {
		height: 30px;
		font-size: 16px;
	}

	.group .btn {
		flex: 2;
		height: 30px;
		line-height: 30px;
		margin-left: 5px;
		text-align: center;
		font-size: 16px;
		border: 1px solid #0081FF;
		background-color: #0081FF;
		border-radius: 5px;
	}

	.card {
		/* width: 100%; */
		background-color: white;
		margin: 10px 5px 0px 5px;
		border-radius: 5px;
		color: #909399;
	}

	.card uni-view {
		padding: 5px 0px 0px 10px;
		font-size: .9rem;
		font-weight: bold;
		color: #000;
	}

	.card span {
		color: #606266;
		font-size: .9rem;
	}
</style>
