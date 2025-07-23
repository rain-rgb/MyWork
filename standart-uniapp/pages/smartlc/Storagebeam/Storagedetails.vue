<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">工序信息</block>
			<!-- <view slot="right" @click="screen">筛选</view> -->
		</cu-custom>
		<view class="process">
			<uni-steps :options="list2" active-color="#387FFD" :active="active" direction="column">
			</uni-steps>
		</view>
	</view>
</template>

<script>
	export default {
		components: {},
		data() {
			return {
				active: -1,
				list2: [],
				gongxuzd: [],
				rwdgongxu: [],
				listdata: [],
				lclistdata: [],
				// bangzadata: [],
				// jiaozhudata: [],
				// jingzhidata: [],
				// zhengyangdata: [],
				// zhangladata: [],
				// tiliangdata: [],
				// cunliangdata: [],
				uuid: '',
			}
		},
		onLoad(options) {
			this.uuid = options.uuid
			// this.loadData()
			this.liangnews()
			// this.bangzalist()
			// this.jiaozhulist()
			// this.jingzhilist()
			// this.zhengyanglist()
			// this.zhanglalist()
			// this.tilianglist()
			// this.cunlianglist()
			console.log(this.active)
			console.log(this.list2)
		},
		methods: {
			// 工序字典查询
			getGXZD(data) {
				console.log(data)
				this.$http.get('/sys/dict/getDictItems/xuhao').then(res => {
					if (res.data.success) {
						console.log(res.data.result)
						this.gongxuzd = []
						let gxzdshuju = res.data.result
						let xuhao = []
						for (let i = 0; i < data.length; i++) {
							console.log(data[i])
							for (let j = 0; j < gxzdshuju.length; j++) {
								// console.log(gxzdshuju[j])
								if (data[i] == gxzdshuju[j].value) {
									console.log(gxzdshuju[j])
									this.gongxuzd.push(gxzdshuju[j])
								}
							}
						}
						// console.log(this.gongxuzd,'gongxuzd============')
						this.list2 = this.gongxuzd.map((item, index) => {
							return {
								...item,
								...this.rwdgongxu[index]
							}
						})

						let data5 = this.list2.filter(function(item) {
							return item.status === 2
						})
						let data6 = this.list2.filter(function(item) {
							return item.status === 1
						})
						if (data6.length != 0) {
							this.active = data5.length
						} else {
							this.active = data5.length - 1
						}

					}
				})
			},
			liangnews() {
				let params = {
					id: this.uuid
				}
				let gxxh = []
				this.$http.get('/zhiliangrenwudan/zhiliangrenwudan/queryZhiliangGongxuByMainId', {
					params
				}).then(res => {
					console.log(res, 'gongxuid')
					res.data.result.forEach(e => {
						if (e.xuhao === 7) {
							let obj = {
								status: e.status,
								desc: `
												存梁时间:${e.cuntime || '--' }
												责任人:${e.responsible || '--'}
												梁所存层:${e.liangceng || '--'}
												存梁行:${e.cunlianghang || '--'}
												存梁列:${e.cunlianglie || '--'}
											 `
							}
							this.rwdgongxu.push(obj)
							console.log(this.rwdgongxu, '工序')
						} else {
							let obj = {
								status: e.status,
								desc: `
												 时间:${e.finishtime || '--'}
												 责任人:${e.responsible || '--'}
											`
							}
							this.rwdgongxu.push(obj)
						}
						gxxh.push(e.xuhao.toLocaleString())

					})
					console.log(this.rwdgongxu, '========================')
					this.getGXZD(gxxh)
				})
			},

			// loadData() {
			// 	let params = {
			// 		sys_depart_orgcode: this.$store.getters.orgcode,
			// 		uuid: this.uuid
			// 	}
			// 	this.$http.get('/zhiliangrenwudan/zhiliangrenwudan/list', {
			// 		params
			// 	}).then(res => {
			// 		this.listdata = res.data.result.records
			// 		console.log(this.listdata)
			// 		// if(this.listdata !== undefined){
			// 		// 	this.list2[0].desc = `<div class="container">
			// 		// 												 <p>创建人:${this.listdata[0].createBy}</p>
			// 		// 												 <p>任务单编号:${this.listdata[0].code}</p>
			// 		// 												 <p>创建时间:${this.listdata[0].dattim}</p>
			// 		// 											 </div>`
			// 		// }
			// 		// if (this.listdata[0].wiretiestatus === 2) {
			// 		// 	this.active = 0
			// 		// }
			// 		// if (this.listdata[0].jiaozhustatus === 2) {
			// 		// 	this.active = 1
			// 		// }
			// 		// if (this.listdata[0].jingzhistatus === 2) {
			// 		// 	this.active = 2
			// 		// }
			// 		// if (this.listdata[0].zhengyangstatus1 === 2 || this.listdata[0].zhengyangstatus2 === 2) {
			// 		// 	this.active = 3
			// 		// }
			// 		// if (this.listdata[0].zhanglastatus === 2) {
			// 		// 	this.active = 4
			// 		// }
			// 		// if (this.listdata[0].tiliangstatus === 2) {
			// 		// 	this.active = 5
			// 		// }
			// 		// if (this.listdata[0].cunliangstatus === 1 || this.listdata[0].cunliangstatus === 2) {
			// 		// 	this.active = 8
			// 		// }
			// 		console.log(this.active)
			// 	})
			// }
		}
	}
</script>

<style lang="scss" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FE;

		.process {
			width: 690upx;
			height: auto;
			margin: 10px auto;
			// margin-top: 30upx;
			border-radius: 16upx;
			background-color: #fff;
			border-radius: 10px;
			// border: 1px solid #5555ff;
			padding-top: 30upx;
			padding-left: 30upx;
			// position: absolute;
			// top: 450upx;
			// left: 30upx;
			// z-index:0;
			box-shadow: 0px 5px 5px #f2f3f3;
		}
	}
</style>
