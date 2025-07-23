<template>
	<view id="pourdetail">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">详情</block>
		</cu-custom>
		<view class="main">
			<view class="main-item">
				<view class="biaoqian"></view>
				<view class="mainnew">主要信息</view>
			</view>
			<!-- <view class="main-item">
				<view class="main-item-name">
					组织机构：<span>{{loaddata.sysOrgCode_dictText}}</span>
				</view>
			</view> -->
			<view class="main-item">
				<view class="main-item-name">
					开盘日期：<span>{{loaddata.begtim}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					任务编号：<span>{{loaddata.rwdcode}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					工程名称：<span>{{loaddata.projname}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					坍落度(mm)/扩展度(mm)：<span>{{loaddata.lands}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					强度等级：<span>{{loaddata.betlev}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					浇筑方式：<span>{{loaddata.pour}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					任务方量：<span>{{loaddata.mete}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					浇筑部位：<span>{{loaddata.conspos}}</span>
				</view>
			</view>
			<view class="main-item"></view>
		</view>
		<view class="progress">
			<u-steps direction="column">
				<view v-for="(item,index) in stelist" :key="index">
					<u-steps-item :title="item.tile">
						<view style="height: 50upx;font-size: 26upx;color: #9299A8;padding: 10upx 20upx;" slot="desc">
							{{item.content}}</view>
						<u-icon :name="item.name" slot="icon" @click="change(item)" size="22"></u-icon>
					</u-steps-item>
				</view>
			</u-steps>
		</view>
		<view
			style="display: flex; justify-content: center; margin-top: 60upx;padding-left: 4%;padding-right: 4%;margin-bottom: 50upx;">
			<view style="padding-right: 3%;width: 40%;" v-has="'bhzrwd:sh'" v-if="this.loaddata.jzlsts==0">
				<u-button text="审核" type="warning" @click="shenhesumit"></u-button>
			</view>
			<view style="padding-right: 3%;width: 40%;" v-if="this.loaddata.jzlsts==0" v-has="'bhzrwd:edit'">
				<u-button text="编辑" type="primary" @click="editsumit"></u-button>
			</view>
			<view style="width: 40%;" v-if="this.loaddata.jzlsts==0" v-has="'bhzrwd:del'">
				<u-button text="删除" type="primary" plain @click="delsumit"></u-button>
			</view>
		</view>
		<view>
			<u-modal :show="shshow" style="position: fixed;background-color: #FFFFFF;" showCancelButton
				@cancel="shcancel" @confirm="shconfirm">
				<view>
					<view>
						<view style="padding: 20upx;">
							<view style="padding: 20upx;font-size: 18px;">
								审批结果：
							</view>
							<view
								style="padding-top: 6%;background-color: #F6F9FC;width: 100%;height: 40%;padding-bottom: 6%;border-radius: 14rpx;">
								<u-radio-group v-model="radiovalue1" placement="row">
									<u-radio :customStyle="{marginLeft: '15px'}" v-for="(item, index) in radiolist1"
										:key="index" :label="item.name" :name="item.status" @change="radioChange">
									</u-radio>
								</u-radio-group>
							</view>
						</view>
						<view style="padding: 20upx;">
							<view style="padding: 20upx;font-size: 18px;">
								审批意见：
							</view>
							<view style="margin-top: 20rpx;background-color: #F6F9FC;border-radius: 14rpx;">
								<u--input placeholder="请输入审批意见" border="surround" v-model="note"></u--input>
							</view>
						</view>
					</view>
				</view>
			</u-modal>
		</view>
		<view>
			<u-modal style="text-align: center;font-size: 20px;" :show="delshow" content="是否确认删除" showCancelButton
				@cancel="delcancel" @confirm="delconfirm">
			</u-modal>
		</view>

	</view>
</template>

<script>
	import smartbhapi from '../../../api/smartbh.js'
	import dict from '../../component/dict/dict.vue'
	export default {
		components: {
			dict
		},
		data() {
			return {
				name: '',
				code: '',
				loaddata: {},
				stelist: [],
				shshow: false,
				delshow: false,
				note: '',
				radiolist1: [{
						name: '同意',
						status: 1,
						disabled: false
					},
					{
						name: '不同意',
						status: 0,
						disabled: false
					}
				],
				radiovalue1: 1,
			}
		},
		onLoad(options) {
			//console.log(options.code, "options.code")
			this.loaddata = JSON.parse(options.item)
			this.code = this.loaddata.rwdcode
			// this.getloadlist()
			this.getstedata()
		},
		methods: {
			// 获取任务单数据
			// getloadlist() {
			// 	this.loaddata = {}
			// 	smartbhapi.pourlist({
			// 		params: {
			// 			rwdcode: this.code
			// 		}
			// 	}).then(res => {
			// 		console.log(res, "任务单列表")
			// 		this.loaddata = res.data.result.records[0]
			// 	}).catch(e => {
			// 		console.log("请求错误", e)
			// 	})
			// },
			getstedata() {
				this.stelist = []
				smartbhapi.bhzrwdSte2({
					params: {
						code: this.code
					}
				}).then(res => {
					//console.log(res, "任务单proste")
					let data = res.data.result
					if (data.length > 0) {
						for (let i = 0; i < data.length; i++) {
							if (data[i].tile == '创建') {
								if (data[i].status == 0) {
									this.stelist.push({
										tile: data[i].tile,
										content: '',
										name: '../../../static/pour/creates2.png',
										status: data[i].status
									})
								} else {
									this.stelist.push({
										tile: data[i].tile,
										content: '创建人' + data[i].person + ',创建时间' + data[i].time,
										name: '../../../static/pour/creates1.png',
										status: data[i].status
									})
								}
							} else if (data[i].tile == '审核') {
								if (data[i].status == 0) {
									this.stelist.push({
										tile: data[i].tile,
										content: '',
										name: '../../../static/pour/audit2.png',
										status: data[i].status
									})
								} else {
									this.stelist.push({
										tile: data[i].tile,
										content: data[i].content,
										name: '../../../static/pour/audit1.png',
										status: data[i].status
									})
								}
							} else if (data[i].tile == '配料') {
								if (data[i].status == 0) {
									this.stelist.push({
										tile: data[i].tile,
										content: '',
										name: '../../../static/pour/Ingredients2.png',
										status: data[i].status
									})
								} else {
									this.stelist.push({
										tile: data[i].tile,
										content: '配料人' + data[i].person + ',配料时间' + data[i].time,
										name: '../../../static/pour/Ingredients1.png',
										status: data[i].status
									})
								}
							} else if (data[i].tile == '生产中' || data[i].tile == '生产已滞后') {
								if (data[i].status == 0) {
									this.stelist.push({
										tile: data[i].tile,
										content: '',
										name: '../../../static/pour/production2.png',
										status: data[i].status
									})
								} else {
									this.stelist.push({
										tile: data[i].tile,
										content: data[i].person + '正在生产中,开始时间' + data[i].time,
										name: '../../../static/pour/production1.png',
										status: data[i].status
									})
								}
							} else {
								if (data[i].status == 0) {
									this.stelist.push({
										tile: data[i].tile,
										content: '',
										name: '../../../static/pour/finish2.png',
										status: data[i].status
									})
								} else {
									this.stelist.push({
										tile: data[i].tile,
										content: '您的工程已完成，完成度100%,完成时间' + data[i].time,
										name: '../../../static/pour/finish1.png',
										status: data[i].status
									})
								}
							}
						}
					}

				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			shenhesumit() {
				this.shshow = true
			},
			shcancel() {
				this.shshow = false
			},
			shconfirm() {
				let params = {
					id: this.loaddata.id,
					status: this.radiovalue1,
					note: this.note
				}
				smartbhapi.bhzrwdedit(
					params
				).then(res => {
					if (res.data.success) {
						uni.showToast({
							title: "审核成功",
							icon: 'none'
						})
						setTimeout(() => {
							this.shshow = false
							uni.navigateTo({
								url: '/pages/smartbh/pourorder/pourManage'
							});
						}, 500);
					}
				})

			},
			radioChange(n) {
				this.radiovalue1 = n
				//console.log('radioChange', n);
			},
			editsumit() {
				uni.navigateTo({
					url: '/pages/smartbh/pourorder/pourAdd?code=' + this.loaddata.rwdcode + '&titlesta=1'
				})
			},
			delsumit() {
				this.delshow = true
			},
			delcancel() {
				this.delshow = false
			},
			delconfirm() {
				this.$http.delete('/system/bhzrenwudan/delete?id=' + this.loaddata.id + '').then(res => {
					if (res.data.success) {
						uni.showToast({
							title: "删除成功",
							icon: 'none'
						})
						setTimeout(() => {
							this.delshow = false
							uni.navigateTo({
								url: '/pages/smartbh/pourorder/pourManage'
							});
						}, 500);
					}
				})
			},
			change(e) {
				console.log("e", e)
				if (e.tile == "审核" && e.status == 0) {
					this.shshow = true
				}
				if (e.tile == "配料" && e.status == 0 && this.loaddata.jzlsts > 0) {
					uni.navigateTo({
						url: '/pages/smartsy/shigongphb/shigongphbAdd?code=' + this.loaddata.rwdcode
					})
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	#pourdetail {
		width: 100%;
		// height: 136vh;
		background-color: #F3F5FE;

		.main {
			width: 690upx;
			margin: 0 auto;
			margin-top: 30upx;
			background-color: white;
			border-radius: 16upx;

			.mainnew {
				color: #333333;
				font-size: 30upx;
				font-weight: bold;
			}

			&-item {
				display: flex;
				font-size: 28upx;
				padding-top: 30upx;

				&-name {
					margin-left: 30upx;
					text-align: left;
					color: #9299A8;

					span {
						margin-right: 30upx;
						color: #4C5971;
					}
				}
			}
		}

		.biaoqian {
			width: 12upx;
			height: 34upx;
			border-radius: 6upx;
			margin: 0 30upx;
			background-color: #4A7FFF;
		}

		.progress {
			width: 690upx;
			height: 736upx;
			margin: 0 auto;
			margin-top: 30upx;
			border-radius: 16upx;
			background-color: #FFFFFF;
			padding-top: 30upx;
			padding-left: 30upx;
		}
	}
</style>
