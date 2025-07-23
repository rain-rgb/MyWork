<template>
	<view id="home">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">材料统计分析</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<view class="section">
			<view style="display: flex;align-items: center;padding-top: 30upx;">
				<view class="biaoqian"></view>
				<view class="mainnew">材料数量统计</view>
				<view style="background-color: #FFFFFF;margin-left: 120px;font-size: 18px;width: 80px;">
					<uni-combox :candidates="candidates" placeholder="" v-model="datelable" @input="check"></uni-combox>
				</view>
			</view>
			<view style="margin-top: 30upx;">
				<qiun-data-charts type="column" :opts="opts" :chartData="mix" />
			</view>
		</view>
		<view class="section">
			<view style="display: flex;align-items: center;padding-top: 30upx;">
				<view class="biaoqian"></view>
				<view class="mainnew">材料合格/退场统计</view>
			</view>
			<view style="margin-top: 30upx;">
				<qiun-data-charts type="bar" :opts="optss" :chartData="bar" />
			</view>
		</view>
		<!--  筛选 -->
		<view class="screen" v-if="show" @confirm="confirm">
			<view class="screen-modal">
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">目的地：</view>
					<picker @change="PickerChange" :range="mddlist">
						<u--input placeholder="请选择目的地" border="surround" v-model="mdd" disabled suffixIcon="arrow-down">
						</u--input>
					</picker>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">供应商：</view>
					<picker @change="PickerChanges" :range="ghdwlist">
						<u--input placeholder="请选择供应商" border="surround" v-model="ghdw" disabled
							suffixIcon="arrow-down">
						</u--input>
					</picker>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">材料类型：</view>
					<picker @change="PickerChangess" :range="cailiaolist">
						<u--input placeholder="请选择材料类型" border="surround" v-model="cailiao" disabled
							suffixIcon="arrow-down">
						</u--input>
					</picker>
				</view>
				<view class="screen-modal-btn">
					<u-button text="取消" @click="confirm"></u-button>
					<u-button type="primary" text="确认" @click="handleOk"></u-button>
				</view>
			</view>
		</view>
	</view>
</template>
<script>
	export default {
		name: 'statistics',
		components: {},
		data() {
			return {
				show: false,
				mddlist: [],
				mddcode: [],
				mdd: '',
				index: -1,
				indexs: -1,
				indexss: -1,
				ghdwlist: [],
				cailiaolist: [],
				ghdw: '',
				cailiao: '',
				date: 0,
				datelable: '天',
				candidates: ['天', '周', '年'],
				opts: {
					yAxis: {
						data: [{
							position: 'left',
							title: '运输量(吨)',
							titleOffsetY: -5,
							titleOffsetX: 5
						}]
					}
				},
				mix: {
					"categories": [],
					"series": [{
						"name": "材料类型",
						"data": [],
					}]
				},
				optss: {
					extra: {
						bar: {
							type: 'stack'
						}
					}
				},
				bar: {
					"categories": [],
					"series": [{
							"name": "退场数量",
							"data": []
						},
						{
							"name": "合格数量",
							"data": []
						}
					]
				}
			}
		},
		onLoad() {

		},
		created() {
			this.getmddData()
			this.getghdwData()
			this.getcailiaoData()
			this.getStaData()
			this.getStaDatas()
		},
		methods: {
			screen() {
				this.mdd = ''
				this.ghdw = ''
				this.cailiao = ''
				this.date = ''
				this.show = true
			},
			confirm() {
				this.show = false
			},
			handleOk() {
				this.getStaData()
				this.getStaDatas()
				this.show = false
			},
			PickerChange(e) {
				// console.log("e", e)
				this.index = e.detail.value
				this.mdd = this.mddlist[this.index]
			},
			PickerChanges(e) {
				// console.log("e", e)
				this.indexs = e.detail.value
				this.ghdw = this.ghdwlist[this.indexs]
			},
			PickerChangess(e) {
				// console.log("e", e)
				this.indexss = e.detail.value
				this.cailiao = this.cailiaolist[this.indexss]
			},
			getmddData() {
				this.$http.get(`/wbdestination/wbDestination/list1`).then(res => {
					let data = res.data.result
					this.mddlist = []
					this.mddcode = []
					data.forEach(gysdata => {
						this.mddlist.push(gysdata.departname)
						this.mddcode.push(gysdata.sysOrgCode)
					})
				})
			},
			getghdwData() {
				this.$http.get(`/wzgongyingshang/wzgongyingshang/list1`).then(res => {
					let data = res.data.result
					this.ghdwlist = []
					data.forEach(gysdata => {
						this.ghdwlist.push(gysdata.gongyingshangname)
					})
				})
			},
			getcailiaoData() {
				this.$http.get(`/wzcailiaonamedict/wzcailiaonamedict/list2`).then(res => {
					// console.log(res.data.result, '材料')
					let data = res.data.result
					this.cailiaolist = []
					data.forEach(gysdata => {
						this.cailiaolist.push(gysdata.cailiaoname)
					})
				})
			},
			getStaData() {
				this.mix = {
					"categories": [],
					"series": [{
						"name": "材料类型",
						"data": []
					}]
				}
				let params = {
					cailiao: this.cailiao,
					mdd: this.mdd,
					ghdw: this.ghdw,
					date: this.date
				}
				this.$http.get(`/wbshebeidetail/wbshebeiDetail/cailiaolists`, {
					params
				}).then(re => {
					if (re.data.success) {
						let data = re.data.result
						data.forEach(item => {
							this.mix.categories.push(item.cailiao)
							this.mix.series[0].data.push(Number(Number(item.jcsl).toFixed(1)))
						})
						// console.log("re", re)
					}
				})
			},
			getStaDatas() {
				this.bar = {
					"categories": [],
					"series": [{
							"name": "退场数量",
							"data": []
						},
						{
							"name": "合格数量",
							"data": []
						}
					]
				}
				let params = {
					cailiao: this.cailiao,
					mdd: this.mdd,
					ghdw: this.ghdw
				}
				this.$http.get(`/wbshebeidetail/wbshebeiDetail/list6`, {
					params
				}).then(re => {
					if (re.data.success) {
						let data = re.data.result
						data.forEach(item => {
							this.bar.categories.push(item.cailiao)
							this.bar.series[0].data.push(Number(Number(item.data.buhege).toFixed(1)))
							this.bar.series[1].data.push(Number(Number(item.data.hege).toFixed(1)))
						})
						// console.log("this.bar", this.bar)
					}
				})
			},
			check(e) {
				if (e === "天") {
					this.date = 0
				} else if (e === "周") {
					this.date = 1
				}else{
					this.date = 2
				}
				this.getStaData()
			}
		}
	}
</script>

<style lang="scss" scoped>
	#home {
		width: 100%;
		// height: 100vh;
		padding-bottom: 20upx;
		background-color: #F2F5FE;

		.section {
			width: 700upx;
			height: 650upx;
			border-radius: 10px;
			margin: 15px auto;
			background-color: #FFFFFF;
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
				}
			}

			.fixstyle {
				width: 320px;
				height: 37px;
				border: 1px solid #c4c9d0;
				background-color: #f5f7fa;
				border-radius: 5px;
				line-height: 37px;
				display: flex;

			}

		}

		.mainnew {
			color: #333333;
			font-size: 30upx;
			font-weight: bold;
		}

		.biaoqian {
			width: 12upx;
			height: 34upx;
			border-radius: 6upx;
			margin: 0 30upx;
			background-color: #4A7FFF;
		}
	}
</style>
