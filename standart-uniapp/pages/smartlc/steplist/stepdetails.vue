<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">梁详情信息</block>
			<!-- <view slot="right" @click="screen">筛选</view> -->
		</cu-custom>
		<view class="section">
			<view class="section-text">
				<view>工程名称:<span>{{listdata.projname}}</span></view>
				<view>任务单编号:<span>{{listdata.code}}</span></view>
				<view>台座:<span>{{listdata.taizuono}}</span></view>
				<view>创建时间:<span>{{listdata.dattim}}</span></view>
				<view>施工部位:<span>{{listdata.conspos}}</span></view>
			</view>
		</view>
		<view class="grid col-1 padding-sm">
			<view v-for="(item,index) in gongxu" :key="index">
				<view class="padding-sm">
					<view class="padding radius text-center shadow-blur"
						:class="item.status === 2 ? 'bg-blue' : 'bg-white' ">
						<view class="text-lg" @click="onHandle(item,index)">{{item.title}}</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: 'stepconfirm',
		data() {
			return {
				// item: '',
				gongxu: [],
				uuid: '',
				// active:-1,
				listdata: [],
				gongxuzd: [], //工序字典值
				rwdgongxu: [], //工序字典值
			}
		},
		onLoad(options) {
			// this.item = JSON.parse(options.item)
			// console.log(this.item, 'item')
			this.uuid = options.uuid
			console.log(this.uuid, 'uuid')
		},
		onShow() {
			this.loadData()
			this.getGX()
		},
		methods: {
			getGXZD(data) {
				// console.log(data,'data')
				// let sign = signMd5Utils.getSign('/sys/dict/getDictItems/xuhao');
				// let signHeader = {"X-Sign": sign, "X-TIMESTAMP": signMd5Utils.getDateTimeToString()};
				// let params = {
				// 	header:signHeader
				// }
				this.$http.get('/sys/dict/getDictItems/xuhao').then(res => {
					if (res.data.success) {
						console.log(res.data.result)
						this.gongxuzd = []
						let gxzdshuju = res.data.result
						let xuhao = []
						for (let i = 0; i < data.length; i++) {
							// console.log(data[i])
							for (let j = 0; j < gxzdshuju.length; j++) {
								// console.log(gxzdshuju[j])
								if (data[i] == gxzdshuju[j].value) {
									// console.log(gxzdshuju[j])
									this.gongxuzd.push(gxzdshuju[j])
								}
							}
						}
						this.gongxu = this.gongxuzd.map((item, index) => {
							return {
								...item,
								...this.rwdgongxu[index]
							}
						})
						console.log(this.gongxu)
					}
				})
			},

			// 工序查询
			getGX() {
				let params = {
					id: this.uuid
				}
				let gxxh = []
				let rwdgx = []
				this.$http.get('/zhiliangrenwudan/zhiliangrenwudan/queryZhiliangGongxuByMainId', {
					params
				}).then(res => {
					console.log(res, 'gongxuid')
					res.data.result.forEach(e => {
						let obj = {
							status: e.status,
							xuhao: e.xuhao,
							id: e.id
						}
						gxxh.push(e.xuhao.toLocaleString())
						rwdgx.push(obj)
					})
					this.rwdgongxu = rwdgx;
					this.getGXZD(gxxh)
				})

			},

			onHandle(e, index) {
				// console.log(e)
				// console.log(index)
				// console.log(this.active)
				if (e.status === 2) {
					uni.showToast({
						title: "该工序已完成，请勿操作",
						icon: "none"
					})
					return
				}
				if (index > 0) {
					if (this.gongxu[index - 1].status !== 2) {
						uni.showToast({
							title: "请先完成前面的工序",
							icon: "none"
						})
						return
					}
				}
				if (this.gongxu[index].xuhao === 7) {
					if (this.gongxu[index].status == 1) {
						uni.showToast({
							title: '已存梁，请取走梁'
						})
						setTimeout(() => {
							uni.navigateTo({
								url: '/pages/smartlc/steplist/stepTake?uuid=' + this.uuid + '&id=' + e.id
							})
						}, 500)

						return
					}
					if (this.gongxu[index].status == 0) {
						uni.navigateTo({
							url: '/pages/smartlc/steplist/stepSave?uuid=' + this.uuid + '&id=' + e.id
						})
					}
				} else {
					uni.navigateTo({
						url: '/pages/smartlc/steplist/stepConfirm?uuid=' + this.uuid + '&xuhao=' + e.xuhao +
							'&id=' + e.id
					})
				}

			},
			loadData() {
				let params = {
					sys_depart_orgcode: this.$store.getters.orgcode,
					uuid: this.uuid,
				}
				this.$http.get('/zhiliangrenwudan/zhiliangrenwudan/list', {
					params
				}).then(res => {
					this.listdata = res.data.result.records[0]
					// console.log(this.listdata)
				})
			}
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
			}
		}

		// .section-button{
		// 	width:100%;
		// 	margin: 0 auto;
		// 	height: auto;
		//     border: 1px solid forestgreen;
		// 	.button-line{
		// 		width:700upx;
		// 		margin: 0 auto;
		// 		height: auto;
		// 		padding: 10px 0;
		// 	}
		// }
	}
</style>
