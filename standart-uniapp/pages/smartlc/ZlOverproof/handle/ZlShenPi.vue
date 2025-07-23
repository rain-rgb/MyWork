<template>
	<!-- 抽检审核 -->
	<view id="home">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">指挥部审批</block>
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
				<image v-for="(img,i) in detailList1" :key="i" @click="ViewImage1(i)"
					style="width: 100px; height: 100px;" mode="aspectFill" :src="img">
				</image>
			</view>
		</view>
		<view class="section">
			<view class="section-title"><text class="biaoqian"></text><text>监理审核</text></view>
			<view class="section-text">监理审批：<span>{{dealInformation.supervisorApproval}}</span></view>
			<view class="section-text">监理结果：<span>{{dealInformation.supervisorResult}}</span></view>
			<view class="section-text">审核人：<span>{{dealInformation.approvalPerson}}</span></view>
			<view class="section-text">监理处理时间：<span>{{dealInformation.supervisorHandleTime}}</span></view>
			<view class="section-text">附件：</view>
			<view>
				<image v-for="(img,i) in detailList2" :key="i" @click="ViewImage2(i)"
					style="width: 100px; height: 100px;" mode="aspectFill" :src="img">
				</image>
			</view>
		</view>
		<view class="deal">
			<view class="deal-name">孔道：</view>
			<view class="deal-input">
				<view style="display: flex">
					<picker @change="handleHole" :range="holeidList" range-key="gsbh">
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
				<view class="deal-name">指挥部驳回原因：</view>
				<view class="deal-input">
					<u--input type="text" v-model="deal.zhbbh" placeholder="请输入指挥部驳回原因" />
				</view>
			</view>
			<view v-show="isAgree=='是'">
				<view class="deal-name">指挥部审批：</view>
				<view class="deal-input">
					<view style="display: flex">
						<u--input type="text" v-model="deal.zhbyj" placeholder="请选择或输入指挥部审批" />
						<picker @change="handleOpinion" :range="opinionList" range-key="describeinfo">
							<u-button size="small" icon="arrow-down" :custom-style="btnStyle"></u-button>
						</picker>
					</view>
				</view>
				<view class="deal-name">指挥部结果：</view>
				<view class="deal-input">
					<view style="display: flex">
						<u--input type="text" v-model="deal.zhbjg" placeholder="请选择或输入指挥部结果" />
						<picker @change="handleRes" :range="resList" range-key="describeinfo">
							<u-button size="small" icon="arrow-down" :custom-style="btnStyle"></u-button>
						</picker>
					</view>
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
		name: "ShenPi",
		components: {},
		data() {
			return {
				overLevel: undefined,
				syjid: undefined,
				deal: {
					zhbbh: "", //指挥部驳回原因
					zhbyj: "", //指挥部意见
					zhbjg: "", //指挥部结果
				},
				dealInformation: {},
				detailList1: [],
				detailList2: [],
				opinionList: [],
				resList: [],
				btnStyle: {
					height: "76rpx",
					minWidth: "0",
				},
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
					type: 0,
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
				this.holeName = this.holeidList[e.detail.value].gsbh;
				this.holeid = this.holeidList[e.detail.value].holeid;
				this.overLevel = this.holeidList[e.detail.value].overLevel;
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
						if(res.data.result.records.length > 0){
							this.dealInformation = res.data.result.records[0]
							this.detailList1 = this.dealInformation.filePath2 ? this.dealInformation.filePath2.split(',') : []
							this.detailList2 = this.dealInformation.filePath ? this.dealInformation.filePath.split(',') : []
						}
					}
				})
			},
			getHandleList() {
				this.$http.get('/bhzTerminology/bhzTerminology/list', {
					params: {
						typeinfo: '指挥部审批'
					}
				}).then(res => {
					if (res.data.success) {
						this.opinionList = res.data.result.records
					}
				})
				this.$http.get('/bhzTerminology/bhzTerminology/list', {
					params: {
						typeinfo: '指挥部结果'
					}
				}).then(res => {
					if (res.data.success) {
						this.resList = res.data.result.records
					}
				})
			},
			//查看图片
			ViewImage1(i) {
				uni.previewImage({
					urls: this.detailList1,
					current: i,
				});
			},
			ViewImage2(i) {
				uni.previewImage({
					urls: this.detailList2,
					current: i,
				});
			},
			// 指挥部意见
			handleOpinion(e) {
				this.deal.zhbyj = this.opinionList[e.detail.value].describeinfo;
			},
			//指挥部结果
			handleRes(e) {
				this.deal.zhbjg = this.resList[e.detail.value].describeinfo;
			},
			confirm() {
				if (!this.holeid) {
					this.$tip.toast('请选择孔道')
					return
				}
				let params
				if (this.isAgree == '是') {
					if (!this.deal.zhbyj) {
						this.$tip.toast('请选择或输入指挥部审批')
						return
					}
					if (!this.deal.zhbjg) {
						this.$tip.toast('请选择或输入指挥部结果')
						return
					}
					params = {
						zhbyj: this.deal.zhbyj,
						zhbjg: this.deal.zhbjg,
						baseid: this.syjid,
						type: 0,
						status: 50,
						holeid: this.holeid,
						level: this.overLevel,
					}
				} else {
					if (!this.deal.zhbbh) {
						this.$tip.toast('请输入指挥部驳回原因')
						return
					}
					params = {
						zhbbh: this.deal.zhbbh,
						baseid: this.syjid,
						type: 0,
						status: 60,
						holeid: this.holeid,
						level: this.overLevel,
					}
				}
				this.$http.get('zhanglam/zhanglaYajiangOverHandler/HBZCZAddOrUpdate', {
					params
				}).then(res => {
					if (res.data.success) {
						this.isAgree == '是' ? this.$tip.success('审批成功！') : this.$tip.success('驳回成功！')
						setTimeout(() => {
							uni.navigateBack(2)
						}, 500)
					} else {
						this.isAgree == '是' ? this.$tip.toast('审批失败！') : this.$tip.toast('驳回失败！')
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