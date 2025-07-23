<template>
	<view id="mixdatadetail">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">拌合站</block>
		</cu-custom>
		<view class="pourlist">
			<view class="pourlist-item">
				<view class="pourlist-item-sta">
					<image v-if="loaddata.overLevel == 0" src="../../../static/pour/hege.png" mode=""></image>
					<image v-if="loaddata.overLevel == 1" src="../../../static/pour/lv1.png" mode=""></image>
					<image v-if="loaddata.overLevel == 2" src="../../../static/pour/lv2.png" mode=""></image>
					<image v-if="loaddata.overLevel == 3" src="../../../static/pour/lv3.png" mode=""></image>
				</view>
				<view v-if="loaddata.overLevel != 0&&loaddata.overLevel != null" class="pourlist-item-sta1">
					<image v-if="loaddata.overproofStatus == 0" src="../../../static/pour/handle4.png" mode=""></image>
					<image v-if="loaddata.overproofStatus == 10" src="../../../static/pour/handle3.png" mode=""></image>
					<image v-if="loaddata.overproofStatus == 20" src="../../../static/pour/handle1.png" mode=""></image>
					<image v-if="loaddata.overproofStatus == 30" src="../../../static/pour/handle6.png" mode=""></image>
					<image v-if="loaddata.overproofStatus == 40" src="../../../static/pour/handle2.png" mode=""></image>
					<image v-if="loaddata.overproofStatus == 60" src="../../../static/pour/handle5.png" mode=""></image>
				</view>
				<view class="pourlist-item-con">
					<view class="pourlist-item-con-name">
						设备名称：<span>{{ loaddata.shebeiNo_dictText }}</span>
					</view>
					<view class="pourlist-item-con-name">
						工程名称：<span>{{ loaddata.projectName }}</span>
					</view>
					<view class="pourlist-item-con-name">
						施工地点：<span>{{ loaddata.jobLocation }}</span>
					</view>
					<view class="pourlist-item-con-name">
						浇筑部位：<span>{{ loaddata.poureLocation }}</span>
					</view>
					<view class="pourlist-item-con-name">
						出料时间：<span>{{ loaddata.productDatetime }}</span>
					</view>
					<view class="pourlist-item-con-name">
						强度等级：<span>{{ loaddata.strengthRank }}</span>
					</view>
					<view class="pourlist-item-con-name">
						操作者：<span>{{ loaddata.handlers }}</span>
					</view>
					<view class="pourlist-item-con-name">
						方量：<span>{{ loaddata.estimateNumber }}m³</span>
					</view>
					<view class="pourlist-item-con-name">
						塌落度：<span>{{ loaddata.slump }}</span>
					</view>
					<view class="pourlist-item-con-name"></view>
				</view>
			</view>
			<!-- <view style="height: 40upx;"></view> -->
		</view>
		<view v-if="loaddata.overLevel != 0&&loaddata.overLevel != null" class="reason">超标原因：{{ loaddata.overReason }}</view>
		<view class="cldetail">
			<view class="cldetail-title"> <text class="biaoqian">1</text> 材料详情</view>
			<uni-table stripe emptyText="暂无更多数据">
				<uni-tr>
					<uni-th width="60" align="center">材料名</uni-th>
					<uni-th width="60" align="center">实际用量</uni-th>
					<uni-th width="60" align="center">理论用量</uni-th>
					<uni-th width="60" align="center">误差值</uni-th>
					<uni-th width="60" align="center">超标值</uni-th>
					<uni-th width="60" align="center">超标等级</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in loaddetail" :key="index">
					<uni-td>
						<text
							:class="[item.materialeOverLevel == 1?'elementary':item.materialeOverLevel == 2?'intermediate':item.materialeOverLevel == 3?'senior':'']">{{item.materialeName}}</text>
					</uni-td>
					<uni-td><text
							:class="[item.materialeOverLevel == 1?'elementary':item.materialeOverLevel == 2?'intermediate':item.materialeOverLevel == 3?'senior':'']">{{item.realityNumber}}</text>
					</uni-td>
					<uni-td><text
							:class="[item.materialeOverLevel == 1?'elementary':item.materialeOverLevel == 2?'intermediate':item.materialeOverLevel == 3?'senior':'']">{{item.theoryNumber}}</text>
					</uni-td>
					<uni-td><text
							:class="[item.materialeOverLevel == 1?'elementary':item.materialeOverLevel == 2?'intermediate':item.materialeOverLevel == 3?'senior':'']">{{item.errorValue}}</text>
					</uni-td>
					<uni-td><text
							:class="[item.materialeOverLevel == 1?'elementary':item.materialeOverLevel == 2?'intermediate':item.materialeOverLevel == 3?'senior':'']">{{item.overValue}}</text>
					</uni-td>
					<uni-td>
						<view v-if="item.materialeOverLevel == 0" style="color: #0FBF6E;">合格</view>
						<view v-if="item.materialeOverLevel == 1" class="elementary">初级超标</view>
						<view v-if="item.materialeOverLevel == 2" class="intermediate">中级超标</view>
						<view v-if="item.materialeOverLevel == 3" class="senior">高级超标</view>
					</uni-td>
				</uni-tr>
			</uni-table>
			<view style="height: 50upx;" class=""></view>
		</view>
		<view v-if="loaddata.overLevel != 0&&loaddata.overLevel != null" class="progress">
			<u-steps direction="column">
				<u-steps-item v-for="(item,index) in handlelist" :key="index" :title="item.tile" :desc="item.desc">
					<u-icon :name="item.time==null?item.sta2:item.sta1" slot="icon" size='22'></u-icon>
					<view style="width: 95%; color: #9299A8;" slot="desc">
						<view style="height: 50upx;" v-if="item.time==null"></view>
						<view v-else>
							<view v-if="index == 0">完成时间{{item.time}}</view>
							<view v-if="index == 1">审核人{{item.person}},审核时间{{item.time}},审核意见{{item.way}}
							</view>
							<view v-if="index == 2">
								<view>处置人{{item.person}},处置时间{{item.time}},处理方式{{item.way}}</view>
								<image v-for="(img,i) in viewImageList" :key="i" @click="ViewImage(i)"
									style="width: 100px; height: 100px;" mode="aspectFill" :src="img"></image>
							</view>
							<view v-if="index == 3">预警时间{{item.time}}</view>
						</view>
					</view>
				</u-steps-item>
			</u-steps>
			<view style="height: 50upx;"></view>
		</view>
		<view v-if="loaddata.overLevel != 0 && loaddata.overLevel != null" class="btn">
			<u-button v-has="'mixdatalist:cz'" v-show="loaddata.overproofStatus === 0 || loaddata.overproofStatus === 30 || loaddata.overproofStatus === 60" class="btnclass" type="warning" @click="checkMan" text="施工处置"></u-button>
			<u-button v-has="'mixdatalist:sh'" v-show="loaddata.overproofStatus === 10" class="btnclass" type="primary" @click="checkExa" plain text="监理审核"></u-button>
			<u-button v-has="'mixdatalist:sp'" v-show="loaddata.overproofStatus === 40" class="btnclass" type="primary" @click="checkOver" text="指挥部审批"></u-button>
			<u-button v-show="loaddata.overproofStatus === 20" class="btnclass" type="primary" @click="checkDetail" text="详情"></u-button>
		</view>
		<view style="height: 40upx;"></view>
	</view>
</template>

<script>
	import smartbhapi from "../../../api/smartbh.js"
	export default {
		components: {},
		data() {
			return {
				batchNo: '',
				loaddata: {},
				loaddetail: [],
				handlelist: [],
				viewImageList: [],
				liucengimg: [{
						sta1: '../../../static/pour/finish1.png',
						sta2: '../../../static/pour/finish2.png',
					},
					{
						sta1: '../../../static/pour/audit1.png',
						sta2: '../../../static/pour/audit2.png',
					},
					{
						sta1: '../../../static/pour/chuzhi1.png',
						sta2: '../../../static/pour/chuzhi2.png',
					},
					{
						sta1: '../../../static/pour/yujin1.png',
						sta2: '../../../static/pour/yujin2.png',
					},
				]
			}
		},
		onLoad(options) {
			// console.log(options)
			this.loaddata = JSON.parse(options.item)
			this.batchNo = this.loaddata.batchNo
			// this.getloadlist()
			this.getloaddetail()
			this.getmanprocess()
		},
		methods: {
			// 施工处置
			checkMan() {
				uni.navigateTo({
					url: `/pages/smartbh/mix/handle/ChuZhi?batchNo=${this.batchNo}&bhz=0&level=${this.loaddata.overLevel}&reason=${this.loaddata.overReason}`
				})
			},
			// 监理审核
			checkExa() {
				uni.navigateTo({
					url: `/pages/smartbh/mix/handle/ShenHe?batchNo=${this.batchNo}&bhz=0&level=${this.loaddata.overLevel}`
				})
			},
			// 指挥部审批
			checkOver() {
				uni.navigateTo({
					url: `/pages/smartbh/mix/handle/ShenPi?batchNo=${this.batchNo}&bhz=0&level=${this.loaddata.overLevel}`
				})
			},
			//详情
			checkDetail(){
				uni.navigateTo({
					url: `/pages/smartbh/mix/handle/DealDetail?data=${JSON.stringify(this.loaddata)}`
				})
			},
			// 拌合站处置流程信息
			getmanprocess() {
				let params = {
					baseid: this.batchNo
				}
				this.$http.get('/czsh/bhzCementOverHandler/handlelist', {
					params
				}).then(res => {
					this.handlelist = res.data.result.map((item, index) => {
						return {
							...item,
							...this.liucengimg[index]
						};
					});
					this.handlelist.forEach(item => {
						if (item.tile == "处置") {
							this.viewImageList = item.file ? item.file.split(',') : []
						}
					});
				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			//查看图片
			ViewImage(i) {
				uni.previewImage({
					urls: this.viewImageList,
					current: i,
				});
			},
			// // 获取拌合站数据
			// getloadlist() {
			// 	let params = {
			// 		column: 'id',
			// 		order: 'desc',
			// 		batchNo: this.batchNo
			// 	}
			// 	smartbhapi.mixlist({
			// 		params
			// 	}).then(res => {
			// 		// console.log(res, "任务单列表")
			// 		this.loaddata = res.data.result.records[0]
			// 	}).catch(e => {
			// 		console.log("请求错误", e)
			// 	})
			// },
			// 获取拌合站详细信息
			getloaddetail() {
				let params = {
					batchNo: this.batchNo
				}
				smartbhapi.mixdetail({
					params
				}).then(res => {
					this.loaddetail = res.data.result.records
				}).catch(e => {
					console.log("请求错误", e)
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	#mixdatadetail {
		background-color: #F3F5FE;

		.pourlist {
			width: 100%;
			margin-top: 30upx;

			&-item {
				position: relative;
				width: 690upx;
				// height: 462upx;
				margin: 0 auto;
				margin-bottom: 30upx;
				border-radius: 16upx;
				background-color: white;

				&-sta {
					position: absolute;
					right: 0px;
					width: 165upx;
					height: 165upx;

					image {
						width: 100%;
						height: 100%;
					}
				}

				&-sta1 {
					position: absolute;
					right: 20upx;
					bottom: 20upx;
					width: 165upx;
					height: 165upx;

					image {
						width: 100%;
						height: 100%;
					}
				}

				&-con {
					width: 610upx;
					// height: 336upx;
					margin: 0 auto;

					// background-color: red;
					&-name {
						padding-top: 30upx;
						color: #9299A8;
						font-size: 28upx;

						span {
							color: #4C5971;
						}
					}
				}

			}
		}

		.reason {
			width: 690rpx;
			margin: 0 auto;
			background-color: rgb(255, 255, 255);
			color: rgb(239, 11, 11);
			line-height: 50rpx;
			text-align: center;
			border-radius: 8px;
			font-size: 32rpx;
		}

		.cldetail {
			width: 690upx;
			// height: 520upx;
			margin: 0 auto;
			margin-top: 30upx;
			border-radius: 16upx;
			background-color: #FFFFFF;

			&-title {
				padding-top: 25upx;
				margin-left: 30upx;
				font-size: 36upx;
				font-weight: 600;
				color: #333333;
			}

			.elementary {
				color: orange;
			}

			.intermediate {
				color: yellow;
			}

			.senior {
				color: red;
			}
		}

		.progress {
			width: 690upx;
			// height: 736upx;
			margin: 0 auto;
			margin-top: 30upx;
			border-radius: 16upx;
			background-color: #FFFFFF;
			padding-top: 30upx;
			padding-left: 30upx;
		}

		.biaoqian {
			width: 12upx;
			height: 34upx;
			border-radius: 6upx;
			margin-right: 30upx;
			color: #4A7FFF;
			background-color: #4A7FFF;
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

		.modal {
			width: 100%;

			// background-color: red;
			&-name {
				color: #4C5971;
				font-size: 30upx;
				margin: 20upx 0;
			}
		}
	}
</style>