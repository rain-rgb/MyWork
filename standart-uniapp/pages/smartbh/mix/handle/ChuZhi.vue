<template>
	<!-- 抽检审核 -->
	<view id="home">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">施工处置</block>
		</cu-custom>
		<view class="deal">
			<view v-show="reason != ''" class="deal-reason">超标原因：{{ reason }}</view>
			<view class="deal-name">问题原因：</view>
			<view class="deal-input">
				<u--textarea type="text" v-model="deal.wtyy" placeholder="请输入问题原因" />
			</view>
			<view class="deal-name">处理方式：</view>
			<view class="deal-input">
				<view style="display: flex">
					<u--input type="text" v-model="deal.clfs" placeholder="请选择或输入处理方式" />
					<picker @change="handleWay" :range="wayList" range-key="describeinfo">
						<u-button size="small" icon="arrow-down" :custom-style="btnStyle"></u-button>
					</picker>
				</view>
			</view>
			<view class="deal-name">处理结果：</view>
			<view class="deal-input">
				<view style="display: flex">
					<u--input type="text" v-model="deal.cljg" placeholder="请选择或输入处理结果" />
					<picker @change="handleRes" :range="resList" range-key="describeinfo">
						<u-button size="small" icon="arrow-down" :custom-style="btnStyle"></u-button>
					</picker>
				</view>
			</view>
			<view class="deal-input">
				<my-image-upload :uploadFilenames="trackImg" @input="selectImg" label="上传图片：" :maxImg="maxImg" :sourceType="sourceType">
				</my-image-upload>
			</view>
			<view class="deal-btn">
				<u-button type="primary" @click="confirm" style="width:200px;border-radius: 10px;" text="确认">
				</u-button>
			</view>
		</view>
	</view>
</template>
<script>
	export default {
		name: "ChuZhi",
		components: {},
		data() {
			return {
				level: undefined,
				bhz: undefined,
				batchNo: undefined,
				reason: '',
				deal: {
					wtyy: "", //问题原因
					clfs: "", //处理方式
					cljg: "", //处理结果
					fileList: "", //上传图片地址
				},
				wayList: [],
				resList: [],
				btnStyle: {
					height: "76rpx",
					minWidth: "0",
				},
				maxImg: 3,
				sourceType: ['camera'],
				trackImg: [],
			};
		},
		onLoad(options) {
			this.batchNo = options.batchNo
			this.reason = options.reason ? options.reason : ''
			this.bhz = options.bhz
			this.level = options.level
			this.getHandleList()
		},
		methods: {
			getHandleList() {
				this.$http.get('/bhzTerminology/bhzTerminology/list', {
					params: {
						typeinfo: '处理方式'
					}
				}).then(res => {
					if (res.data.success) {
						this.wayList = res.data.result.records
					}
				})
				this.$http.get('/bhzTerminology/bhzTerminology/list', {
					params: {
						typeinfo: '处理结果'
					}
				}).then(res => {
					if (res.data.success) {
						this.resList = res.data.result.records
					}
				})
			},
			// 处理方式
			handleWay(e) {
				this.deal.clfs = this.wayList[e.detail.value].describeinfo;
			},
			//处理结果
			handleRes(e) {
				this.deal.cljg = this.resList[e.detail.value].describeinfo;
			},
			selectImg(uploadFilenames) {
				this.deal.fileList = uploadFilenames.join();
			},
			confirm() {
				if (!this.deal.wtyy) {
					this.$tip.toast('请输入问题原因')
					return
				}
				if (!this.deal.clfs) {
					this.$tip.toast('请选择或输入处理方式')
					return
				}
				if (!this.deal.cljg) {
					this.$tip.toast('请选择或输入处理结果')
					return
				}
				let params = {
					cljg: this.deal.cljg,
					clfs: this.deal.clfs,
					wtyy: this.deal.wtyy,
					fileList: this.deal.fileList,
					hntbatch: this.batchNo,
					bhz: this.bhz,
					level: this.level,
					status: 10,
				}
				this.$http.get('/czsh/bhzCementOverHandler/HBZCZAddOrUpdate', {
					params
				}).then(res => {
					if (res.data.success) {
						this.$tip.success('处置成功！')
						setTimeout(() => {
							uni.navigateBack(2)
						}, 500)
					} else {
						this.$tip.toast('处置失败！')
					}
				})
			},
		},
	};
</script>

<style lang="scss" scoped>
	#home {
		width: 100%;
		height: auto;
		background-color: #f2f5fe;
		padding-bottom: 30rpx;

		.deal {
			width: 700rpx;
			border-radius: 20rpx;
			background-color: #fff;
			margin: 30rpx auto 0;
			padding: 50rpx;

			&-reason {
				height: 50rpx;
				color: rgb(239, 11, 11);
				line-height: 50rpx;
				text-align: center;
				font-size: 36rpx;
			}

			&-name {
				color: #4c5971;
				font-size: 30rpx;
				margin: 20rpx 0;
			}

			&-btn {
				margin-top: 30rpx;
			}
		}
	}
</style>