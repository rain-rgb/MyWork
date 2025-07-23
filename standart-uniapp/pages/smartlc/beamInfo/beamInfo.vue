<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">梁板二维码</block>
			<!-- <view slot="right" @click="screen">筛选</view> -->
		</cu-custom>


		<view class="section">
			<view class="section-text">
				<view class="standard" style="color: #FF0000;">{{shitilist.active}}</view>
				<view>工程名称:<span>{{shitilist.projname}}</span></view>
				<view>所属组织机构:<span>{{shitilist.sysOrgCode_dictText}}</span></view>
				<view>任务单编号:<span>{{shitilist.code}}</span></view>
				<view>台座:<span>{{shitilist.taizuono}}</span></view>
				<view>创建时间:<span>{{shitilist.dattim}}</span></view>
				<view>浇注日期:<span>{{shitilist.begtim}}</span></view>
				<view>截止日期:<span>{{shitilist.endtim}}</span></view>
				<view>计划生产时间:<span>{{shitilist.productiontime}}</span></view>
				<view>施工部位:<span>{{shitilist.conspos}}</span></view>
				<view>生产线:<span>{{shitilist.station_dictText}}</span></view>
				<view>坍落度:<span>{{shitilist.lands}}</span></view>
				<view>搅拌时间:<span>{{shitilist.mixlast}}</span></view>
				<view>强度等级:<span>{{shitilist.betlev}}</span></view>
				<view>任务方量:<span>{{shitilist.mete}}</span></view>
				<view>浇筑方式:<span>{{shitilist.pour}}</span></view>
				<view>创建人:<span>{{shitilist.createBy}}</span></view>
			</view>
		</view>

		<view class=" section-all">
			<view class="title">
				<view class="title-fonts">梁工序信息</view>
			</view>
			<view class="tablelist">
				<view class="process">
					<uni-steps :options="list2" active-color="#387FFD" :active="active"
						direction="column">
					</uni-steps>
				</view>
			</view>

		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				shitilist: {},
				id: '',
				datalist: '',
				xuhaoList: [],
				liuchengList: [], //流程列表
				list: [], //存筛选后的流程
				list2: [], //展示的数据
				list3: [], //存序号
				active: -1,
			}
		},
		onLoad(options) {
			//扫码获取的任务单编号
			this.id = options.id;
			this.textList();
		},
		created() {},
		methods: {
			//根据任务单编号获取信息
			textList() {
				this.$http.get(`/zhiliangrenwudan/zhiliangrenwudan/list`, {
					params: {
						code: this.id
					}
				}).then(res => {
					this.shitilist = res.data.result.records[0]
					// console.log(res.data.result.records[0].uuid, "res.data.result.records[0].uuid")
					this.liangNews();
				})
			},
			//流程序号
			getGXZD() {
				this.$http.get('/sys/dict/getDictItems/xuhao').then(res => {
					this.xuhaoList = res.data.result;
					this.xuhaoList.forEach((item, index) => {
						this.liuchengList.forEach(cItem => {
							if (item.value == cItem.xuhao)
								this.list.push(item)
						})
					})
					this.list2 = this.list3.map((item, index) => {
						return {
							...item,
							...this.xuhaoList[index]
						}
					})

					let status2 = this.list2.filter(function(item) {
						return item.status === 2
					})
					let status1 = this.list2.filter(function(item) {
						return item.status === 1
					})
					if (status1.length != 0) {
						this.active = status2.length
					} else {
						this.active = status2.length - 1
					}
					// console.log(this.list, "this.list")
					// console.log(this.list3, "this.list3")
					// console.log(this.xuhaoList, "this.xuhaoList")
					// console.log(this.list2, "this.list2")
				})
			},
			//获取工序
			liangNews() {
				let gxArr = []
				this.$http.get('/zhilianggongxu/zhiliangGongxu/list', {
					params: {
						uuid: this.shitilist.uuid,
					}
				}).then(res => {
					console.log(res.data, "制梁工序")
					this.liuchengList = res.data.result.records;
					this.liuchengList.forEach((item, i) => {
						let obj = {};
						if (item.xuhao === 7) {
							obj = {
								status: item.status,
								desc: `
										存梁时间:${item.cuntime || '--' }
										责任人:${item.responsible || '--'}
										梁所存层:${item.liangceng || '--'}
										存梁行:${item.cunlianghang || '--'}
										存梁列:${item.cunlianglie || '--'}
										`
							}
						} else {
							obj = {
								status: item.status,
								desc: `
										存梁时间:${item.finishtime || '--' }
										责任人:${item.responsible || '--'}
										`
							}
						}
						gxArr.push(obj)
					});
					this.list3 = gxArr;
					this.getGXZD();
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FE;

	}

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

	.section-all {
		width: 700rpx;
		height: auto;
		background-color: #fff;
		margin: 0 auto;
		border-radius: 10px;

		.section-text {
			color: #9299A8;
			font-size: 30upx;
			padding: 0 15px;
			line-height: 55upx;
			margin: 10px 0;

			span {
				color: #4C5971;
				font-size: 30upx;
				font-family: 'PingFang-SC-Medium';
				padding: 10upx 0px;
			}

			.title {
				width: 200rpx;
				height: 34px;
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

			}
		}
	}

	.title-fonts {
		font-size: 18px;
		font-family: ' PingFang SC';
		font-weight: bold;
		color: #333333;
		padding: 0 15px;
	}
	
</style>
