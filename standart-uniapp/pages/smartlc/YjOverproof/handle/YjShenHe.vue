<template>
	<!-- 抽检审核 -->
	<view id="home">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">监理审核</block>
		</cu-custom>
		<view class="section">
			<view class="section-title"><text class="biaoqian"></text><text>施工单位处理</text></view>
			<view class="section-text">问题原因：<span>{{dealInformation.problemReasons}}</span></view>
			<view class="section-text">处理方式：<span>{{dealInformation.handleWay}}</span></view>
			<view class="section-text">处理结果：<span>{{dealInformation.handleResult}}</span></view>
			<view class="section-text">处置人：<span>{{dealInformation.handlePerson}}</span></view>
			<view class="section-text">处理时间：<span>{{dealInformation.handleTime}}</span></view>
			<view class="section-text">附件：</view>
			<view>
				<image v-for="(img,i) in detailList" :key="i" @click="ViewImage(i)" style="width: 100px; height: 100px;"
					mode="aspectFill" :src="img">
				</image>
			</view>
		</view>
		<view class="deal">
			<view class="deal-name">孔道：</view>
			<view class="deal-input">
				<view style="display: flex">
					<picker @change="handleHole" :range="holeidList" range-key="kongdao">
						<u--input type="text" v-model="holeName" placeholder="请选择孔道" disabled suffixIcon="arrow-down" />
					</picker>
				</view>
			</view>
			<view class="deal-examine">
				<view class="deal-examine-title">
					是否同意：
				</view>
				<u-radio-group v-model="isAgree">
					<u-radio label="是" name="是"></u-radio>
					<u-radio :customStyle="{ marginLeft: '15px' }" label="否" name="否"></u-radio>
				</u-radio-group>
			</view>
			<view v-show="isAgree=='否'">
				<view class="deal-name">监理驳回原因：</view>
				<view class="deal-input">
					<u--input type="text" v-model="deal.jlbh" placeholder="请输入监理驳回原因" />
				</view>
			</view>
			<view v-show="isAgree=='是'">
				<view class="deal-name">监理审批：</view>
				<view class="deal-input">
					<view style="display: flex">
						<u--input type="text" v-model="deal.spyj" placeholder="请选择或输入监理审批" />
						<picker @change="handleOpinion" :range="opinionList" range-key="describeinfo">
							<u-button size="small" icon="arrow-down" :custom-style="btnStyle"></u-button>
						</picker>
					</view>
				</view>
				<view class="deal-name">监理结果：</view>
				<view class="deal-input">
					<view style="display: flex">
						<u--input type="text" v-model="deal.spjg" placeholder="请选择或输入监理结果" />
						<picker @change="handleRes" :range="resList" range-key="describeinfo">
							<u-button size="small" icon="arrow-down" :custom-style="btnStyle"></u-button>
						</picker>
					</view>
				</view>
				<view class="deal-input">
					<my-image-upload :uploadFilenames="trackImg" @input="selectImg" label="上传图片：" :maxImg="maxImg" :sourceType="sourceType">
					</my-image-upload>
				</view>
			</view>
			<view class="deal-btn">
				<u-button type="primary" @click="confirm" style="width: 200px; border-radius: 10px" text="确认">
				</u-button>
			</view>
		</view>
	</view>
</template>
<script>
	export default {
		name: "ShenHe",
		components: {},
		data() {
			return {
				isOverLevel: undefined,
				syjid: undefined,
				deal: {
					jlbh: "", //监理驳回
					spyj: "", //监理审批意见
					spjg: "", //监理结果
					fileList: "", //上传图片地址
				},
				dealInformation: {},
				detailList: [],
				opinionList: [],
				resList: [],
				btnStyle: {
					height: "76rpx",
					minWidth: "0",
				},
				maxImg: 3,
				sourceType: ['camera'],
				trackImg: [],
				isAgree: '是',
				holeidList: [],
				holeName: '',
				holeid: ''
			};
		},
		onLoad(options) {
			this.syjid = options.syjid
			this.getHoleidList()
			this.getHandleList()
		},
		methods: {
			//获取孔道
			getHoleidList() {
				let params = {
					syjid: this.syjid,
					type: 1,
				}
				this.$http.get('/zhanglam/zhanglaYajiangOverHandler/holeidList', {
					params
				}).then(res => {
					if (res.data.success) {
						this.holeidList = res.data.result
					}
				})
			},
			//选择孔道
			handleHole(e) {
				this.holeName = this.holeidList[e.detail.value].kongdao;
				this.holeid = this.holeidList[e.detail.value].holeid;
				this.isOverLevel = this.holeidList[e.detail.value].isOverLevel;
				this.getInformation()
			},
			getInformation() {
				this.$http.get('/zhanglam/zhanglaYajiangOverHandler/list', {
					params: {
						baseId: this.syjid,
						holeid: this.holeid
					}
				}).then(res => {
					if (res.data.success) {
						if (res.data.result.records.length > 0) {
							this.dealInformation = res.data.result.records[0]
							this.detailList = this.dealInformation.filePath2 ? this.dealInformation.filePath2.split(',') : []
						}
					}
				})
			},
			getHandleList() {
				this.$http.get('/bhzTerminology/bhzTerminology/list', {
					params: {
						typeinfo: '监理审批'
					}
				}).then(res => {
					if (res.data.success) {
						this.opinionList = res.data.result.records
					}
				})
				this.$http.get('/bhzTerminology/bhzTerminology/list', {
					params: {
						typeinfo: '监理结果'
					}
				}).then(res => {
					if (res.data.success) {
						this.resList = res.data.result.records
					}
				})
			},
			// 监理审批
			handleOpinion(e) {
				this.deal.spyj = this.opinionList[e.detail.value].describeinfo;
			},
			//监理结果
			handleRes(e) {
				this.deal.spjg = this.resList[e.detail.value].describeinfo;
			},
			//查看图片
			ViewImage(i) {
				uni.previewImage({
					urls: this.detailList,
					current: i,
				});
			},
			selectImg(uploadFilenames) {
				this.deal.fileList = uploadFilenames.join();
			},
			confirm() {
				if (!this.holeid) {
					this.$tip.toast('请选择孔道')
					return
				}
				let params
				if (this.isAgree == '是') {
					if (!this.deal.spyj) {
						this.$tip.toast('请选择或输入监理审批')
						return
					}
					if (!this.deal.spjg) {
						this.$tip.toast('请选择或输入监理结果')
						return
					}
					params = {
						spyj: this.deal.spyj,
						spjg: this.deal.spjg,
						baseid: this.syjid,
						type: 0,
						status: 40,
						holeid: this.holeid,
						level: this.isOverLevel,
						fileList: this.deal.fileList,
					}
				} else {
					if (!this.deal.jlbh) {
						this.$tip.toast('请输入监理驳回原因')
						return
					}
					params = {
						jlbh: this.deal.jlbh,
						baseid: this.syjid,
						type: 0,
						status: 30,
						holeid: this.holeid,
						level: this.isOverLevel,
					}
				}
				this.$http.get('/zhanglam/zhanglaYajiangOverHandler/HBZCZAddOrUpdate', {
					params
				}).then(res => {
					if (res.data.success) {
						this.isAgree == '是' ? this.$tip.success('审核成功！') : this.$tip.success('驳回成功！')
						setTimeout(() => {
							uni.navigateBack(2)
						}, 500)
					} else {
						this.isAgree == '是' ? this.$tip.toast('审核失败！') : this.$tip.toast('驳回失败！')
					}
				})
			}
		},
	};
</script>

<style lang="scss" scoped>
	#home {
		width: 100%;
		height: auto;
		background-color: #f2f5fe;
		padding-bottom: 30rpx;

		.section {
			width: 700rpx;
			border-radius: 20rpx;
			margin: 20rpx auto;
			padding: 30rpx;
			background-color: #fff;

			&-title {
				display: flex;
				align-items: center;
				font-size: 36rpx;
				font-weight: 600;
				color: #333333;
				margin-bottom: 10rpx;

				.biaoqian {
					width: 12rpx;
					height: 34rpx;
					border-radius: 6rpx;
					margin-right: 20rpx;
					color: #4A7FFF;
					background-color: #4A7FFF;
				}
			}

			&-text {
				// height: 55rpx;
				color: #9299A8;
				font-size: 30rpx;
				line-height: 55rpx;

				span {
					color: #4C5971;
					font-size: 30rpx;
					font-family: 'PingFang-SC-Medium';
					padding: 0 15rpx;
				}
			}
		}

		.deal {
			width: 700rpx;
			border-radius: 20rpx;
			background-color: #fff;
			margin: 30rpx auto 0;
			padding: 50rpx;

			&-examine {
				display: flex;

				&-title {
					padding: 30rpx 10rpx;
					font-size: 28rpx;
				}
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