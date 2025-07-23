<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">沥青拌合详情</block>
		</cu-custom>
		<view class="lqtable">
			<view class="title">
				<view class="round-dot">
					<view class="round"></view>
				</view>
				<view class="title-iu">
					<view class="title-font">沥青拌合站子表材料信息</view>
				</view>
			</view>
			<uni-table stripe emptyText="暂无更多数据">
				<uni-tr>
					<uni-th width="70" align="center">材料名</uni-th>
					<uni-th width="10" align="center">实际用量</uni-th>
					<uni-th width="10" align="center">理论用量</uni-th>
					<uni-th width="10" align="center">误差值</uni-th>
					<uni-th width="10" align="center">超标值</uni-th>
					<uni-th width="60" align="center">超标等级</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in lqdata" :key="index">
					<uni-td align="center">{{item.cailiaoming}}</uni-td>
					<uni-td align="center">{{item.shijiyongliang}}</uni-td>
					<uni-td align="center">{{item.theoryNumber}}</uni-td>
					<uni-td align="center">{{item.wucha}}</uni-td>
					<uni-td align="center">{{item.chaobiao}}</uni-td>
					<uni-td align="center"
						:style="item.chaobiaodengji ==0?'color:green':item.chaobiaodengji ==1?'color:blue':item.chaobiaodengji ==2?'color:orange':item.chaobiaodengji ==3?'color:red':''">
						{{item.chaobiaodengji ==0?'合格':item.chaobiaodengji ==1?'初级超标':item.chaobiaodengji ==2?'中级超标':item.chaobiaodengji ==3?'高级超标':'暂无数据'}}
					</uni-td>
				</uni-tr>
			</uni-table>
		</view>
		<view v-show="loaddata.chaobiaodengji !==0" class="btn">
			<u-button class="btnclass" type="warning" v-show="loaddata.overproofStatus==0||loaddata.overproofStatus==30" @click="checkMan" text="处置"></u-button>
			<u-button class="btnclass" type="primary" v-show="loaddata.overproofStatus==10" @click="checkOver" plain text="驳回">
			</u-button>
			<u-button class="btnclass" type="primary" v-show="loaddata.overproofStatus==10"  @click="checkExa" text="审核"></u-button>
		</view>
		<!-- 处置 -->
		<u-modal :show="manshow" showCancelButton :title="mantitle" @confirm="confirmman" @cancel="cancel">
			<view class="modal">
				<view class="modal-name">问题原因：</view>
				<view class="modal-input">
					<u--input type="text" v-model="mancontent.wtyy" placeholder="请输入问题原因" />
				</view>
				<view class="modal-name">处理方式：</view>
				<view class="modal-input">
					<u--input type="text" v-model="mancontent.clfs" placeholder="请输入处理方式" />
				</view>
				<view class="modal-name">处理结果：</view>
				<view class="modal-input">
					<u--input type="text" v-model="mancontent.cljg" placeholder="请输入处理结果" />
				</view>
				<!-- <view class="modal-name">上传文件/图片：</view> -->
				<view class="modal-input">
					<my-image-upload :uploadFilenames="samPic" @input="ChooseImage" label="上传文件/图片" :maxImg="maxImg">
					</my-image-upload>
				</view>
			</view>
		</u-modal>
		<!-- 驳回 -->
		<u-modal :show="overshow" :title="overtitle" @confirm="confirmover" showCancelButton @cancel="cancel">
			<view class="modal">
				<view class="modal-name">驳回意见：</view>
				<view class="modal-input">
					<u--input type="text" v-model="overcontent.remark" placeholder="请输入驳回意见" />
				</view>
			</view>
		</u-modal>
		<!-- 审核 -->
		<u-modal :show="exashow" :title="exatitle" @confirm="confirmexa" showCancelButton @cancel="cancel">
			<view class="modal">
				<view class="modal-name">审批意见：</view>
				<view class="modal-input">
					<u--input type="text" v-model="exacontent.spyj" placeholder="请输入审批意见" />
				</view>
				<view class="modal-name">审批结果：</view>
				<view class="modal-input">
					<u--input type="text" v-model="exacontent.spjg" placeholder="请输入审批结果" />
				</view>
				<view class="modal-input">
					<my-image-upload :uploadFilenames="samPic" @input="ChooseImage" label="上传文件/图片" :maxImg="maxImg">
					</my-image-upload>
					<!-- <u-upload :fileList="fileList1" @afterRead="afterRead" @delete="deletePic" name="1" multiple
						:maxCount="10"></u-upload> -->
				</view>
			</view>
		</u-modal>
	</view>
</template>

<script>
	export default {
		name: 'asphaltmixdetails',
		data() {
			return {
				batchNo: '',
				guid: '',
				loaddata: '',
				lqdata: {},
				// bhzCementOverHandler: {},
				detailList: [],
				detailList1: [],
				manshow: false,
				mantitle: "处置",
				mancontent: {
					fileList: '', //上传图片地址
					bhz: 1, //
					hntbatch:'', //
					cljg: '', //处理结果
					wtyy: '', //问题原因
					clfs: '' //处理方式
				},
				overshow: false,
				overtitle: "驳回",
				overcontent: {
					remark: '', //驳回原因
					baseid: '',
					bhz: 1
				},
				exashow: false,
				exatitle: "审核",
				exacontent: { //审核内容
					fileList: '', //上传图片地址
					bhz: 1, //
					hntbatch: '', //
					spjg: '', //审核结果
					spyj: '' //审核意见
				},
				fileList: [],
				samPic: [],
				maxImg: 4,
			}
		},
		onLoad(options) {

			let item = JSON.parse(options.item)
			this.guid = item.guid
			this.loaddata = item
			console.log(this.loaddata, 'ssss')
		},
		created() {
			this.lqdetails()
			// this.lqinfo()
		},
		methods: {
			lqdetails() {
				this.$http.get(`/lqbhz/bhzLqBases/queryBhzLqCailiaoByMainId`, {
					params: {
						baseGuid: this.guid
					}
				}).then(res => {
					console.log(res, '沥青详情上')
					this.lqdata = res.data.result
				})
			},
			ChooseImage(uploadFilenames) {
				this.fileList = uploadFilenames.toString()
			},
			cancel() {
				this.manshow = false
				this.exashow = false
				this.overshow = false
			},
			// 处置
			checkMan() {
				this.manshow = true
			},
			confirmman() {
				this.mancontent.hntbatch = this.loaddata.guid
				console.log(this.mancontent)
				if (!this.mancontent.wtyy) {
					uni.showToast({
						title: '请输入问题原因',
						icon: 'none'
					})
					return
				}
				if (!this.mancontent.clfs) {
					uni.showToast({
						title: '请输入处理方式',
						icon: 'none'
					})
					return
				}
				if (!this.mancontent.cljg) {
					uni.showToast({
						title: '请输入处理结果',
						icon: 'none'
					})
					return
				}
				this.mancontent.fileList = this.fileList
				let params = this.mancontent
		
				this.$http.get('/czsh/bhzCementOverHandler/CZAddOrUpdate', {
					params
				}).then(res => {
					if (res.data.success) {
						this.manshow = false
						uni.showToast({
							title: '处理成功'
						})
						setTimeout(function() {
							uni.navigateTo({
								url: "/pages/SmartPavement/asphalt/asphaltmix"
							})
						}, 300)
					} else {
						this.$tip.toast('提交失败')
					}
				})

			},
			// 驳回
			checkOver() {
				this.overshow = true
			},
			confirmover() {
				this.overcontent.baseid = this.loaddata.guid
				if (!this.overcontent.remark) {
					uni.showToast({
						title: '请输入驳回原因',
						icon: 'none'
					})
					return
				}
				let params = this.overcontent
				this.$http.get('/czsh/bhzCementOverHandler/bohuiedit', {
					params
				}).then(res => {
					if (res.data.success) {
						this.overshow = false
						uni.showToast({
							title: '处理成功'
						})
						setTimeout(function() {
							uni.navigateTo({
								url: "/pages/SmartPavement/asphalt/asphaltmix"
							})
						}, 300)
					} else {
						this.$tip.toast('提交失败')
					}
				})

			},
			// 审核
			checkExa() {
				this.exashow = true
			},
			confirmexa() {
				this.exacontent.hntbatch = this.loaddata.guid
				console.log(this.exacontent)
				if (!this.exacontent.spyj) {
					uni.showToast({
						title: '请输入审核意见',
						icon: 'none'
					})
					return
				}
				if (!this.exacontent.spjg) {
					uni.showToast({
						title: '请输入审核结果',
						icon: 'none'
					})
					return
				}
				this.exacontent.fileList = this.fileList
				let params = this.exacontent
				this.$http.get('/czsh/bhzCementOverHandler/SHAddOrUpdate', {
					params
				}).then(res => {
					if (res.data.success) {
						this.$tip.toast('审核成功')
						this.exashow = false
						setTimeout(function() {
							uni.navigateTo({
								url: "/pages/SmartPavement/asphalt/asphaltmix"
							})
						}, 300)
					} else {
						this.$tip.toast('提交失败')
					}
				})

			},
		},

	}
</script>

<style lang="less" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FF;

		.lqtable {
			width: 700upx;
			height: auto;
			background-color: #fff;
			border-radius: 10px;
			margin: 10px auto;

			.title {
				width: 490rpx;
				height: 25px;
				// border: 1px solid #18BC37;
				display: flex;
				flex-direction: row;
				justify-content: space-around;
				align-items: center;

				.round-dot {
					flex: 2;

					.round {
						width: 6px;
						height: 17px;
						background: #387ffd;
						border-radius: 6px;
						margin: 0 auto;
					}
				}

				.title-iu {
					flex: 12;

					.title-font {
						font-size: 18px;
						font-family: " PingFang SC";
						font-weight: bold;
						color: #333333;
					}
				}
			}

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
			}
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
				margin: 30upx 0;
			}
		}
	}
</style>
