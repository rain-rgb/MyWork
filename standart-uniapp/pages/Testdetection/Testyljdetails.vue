<template>
	<view>
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">压力机详情</block>
		</cu-custom>
		<view class="card">
			<scroll-view scroll-x class="nav bg-white">
				<view class="flex text-center">
					<view class="cu-item flex-sub" :class="0==TabCur?'text-blue cur':''" @tap="tabSelect" data-id="0">
						压力机详情信息
					</view>
					<view class="cu-item flex-sub" :class="1==TabCur?'text-blue cur':''" @tap="tabSelect" data-id="1">
						压力机详情折线图
					</view>
				</view>
			</scroll-view>
			<view v-if="TabCur==0" class="list-wrap">
				<view class="wrap" v-for="(item,index) in yljdata" :key="index">
					<view>试件序号:<span>{{item.sjxh}}</span></view>
					<view>抗压力值:<span>{{item.kylz}}</span></view>
					<view>抗压强度:<span>{{item.kyqd}}</span></view>
					<view>试验时间:<span>{{item.sysj}}</span></view>
					<view>完成时间:<span>{{item.wcsj}}</span></view>
					<!-- <view style="color: #0081FF;font-size: 1rem;font-weight: bold;">试件序号:<span
							style="color:#000000;">{{item.sjxh}}</span></view> -->
				</view>
			</view>

			<view v-if="TabCur==1" class="">
				<view class="charts-box" v-if="chartData.categories.length>0">
					<span class="xhlist">试件序号1</span>
					<qiun-data-charts type="line" :opts="opts" :chartData="chartData" :ontouch="true" :errorShow="false"
						background="#FFFFFF" />
				</view>
				<view class="charts-box" v-if="chartData1.categories.length>0">
					<span class="xhlist">试件序号2</span>
					<qiun-data-charts type="line" :opts="opts" :chartData="chartData1" :ontouch="true"
						:errorShow="false" background="#FFFFFF" />
				</view>
				<view class="charts-box" v-if="chartData2.categories.length>0">
					<span class="xhlist">试件序号3</span>
					<qiun-data-charts type="line" :opts="opts" :chartData="chartData2" :ontouch="true"
						:errorShow="false" background="#FFFFFF" />
				</view>
				<view class="charts-box" v-if="chartData3.categories.length>0">
					<span class="xhlist">试件序号4</span>
					<qiun-data-charts type="line" :opts="opts" :chartData="chartData3" :ontouch="true"
						:errorShow="false" background="#FFFFFF" />
				</view>
				<view class="charts-box" v-if="chartData4.categories.length>0">
					<span class="xhlist">试件序号5</span>
					<qiun-data-charts type="line" :opts="opts" :chartData="chartData4" :ontouch="true"
						:errorShow="false" background="#FFFFFF" />
				</view>
				<view class="charts-box" v-if="chartData5.categories.length>0">
					<span class="xhlist">试件序号6</span>
					<qiun-data-charts type="line" :opts="opts" :chartData="chartData5" :ontouch="true"
						:errorShow="false" background="#FFFFFF" />
				</view>
			</view>
		</view>
		<view class="button_all" v-show="items.pdjg !=='合格'">
			<view v-has="'wnj:handle'">
				<view class="button_cz" v-show="items.overproofStatus==0||items.overproofStatus==30">
					<u-button style="background-color: #F08322;
				color:#fff;
				border-radius: 10px;" @click="management" text="处置"></u-button>
				</view>
			</view>
			<view v-has="'wnj:reject'">
				<view class="button_bh" v-show="items.overproofStatus==10">
					<u-button style="
				   background: rgba(56, 127, 253, 0.2);
				color:#387FFD;
				border-radius: 10px;" @click="reject" text="驳回"></u-button>
				</view>
			</view>
			<view v-has="'wnj:check'">
				<view class="button_sh" v-show="items.overproofStatus==10">
					<u-button style="background-color: #387FFD;
				color:#fff;
				border-radius: 10px;" @click="check" text="审核"></u-button>
				</view>
			</view>
		</view>
		<view>
			<!-- over 30 驳回 20 审核 10 已处置 0 未处置 -->
			<u-modal :show="show" style="position: fixed;background-color: #FFFFFF;" showCancelButton @cancel="cancel"
				@confirm="confirm">
				<view class="table-wrap">
					<view class="table">
						<view class="table-cause">
							<text>问题原因:</text>
						</view>
						<view class="table-result">
							<u--input placeholder="请输入问题原因" border="surround" v-model="cause">
							</u--input>
						</view>
					</view>
					<view class="table">
						<view class="table-cause">
							<text>处理方式:</text>
						</view>
						<view class="table-result">
							<u--input placeholder="请输入处理方式" border="surround" v-model="mode">
							</u--input>
						</view>
					</view>
					<view class="table">
						<view class="table-cause">
							<text>处理结果:</text>
						</view>
						<view class="table-result">
							<u--input placeholder="请输入问题原因" border="surround" v-model="result">
							</u--input>
						</view>
					</view>
					<view class="table-upload">
						<view class="upimg">
							<my-image-upload :uploadFilenames="samPic" @input="ChooseImage" label="上传文件/图片：">
							</my-image-upload>
						</view>
						<!-- 上传图片 -->
						<!-- <view class="screen-modal-btn">
							<!-- <u-button text="取消" @click="confirm"></u-button> -->
						<!-- <u-button style="width:160px;border-radius: 10px;color: #fff;background-color: #387FFD;"
								text="取样" @click="submitqy"></u-button>
						</view> -->
					</view>
				</view>
			</u-modal>

			<!-- 驳回 -->
			<u-modal :show="showbh" style="position: fixed;background-color: #FFFFFF;" showCancelButton
				@cancel="cancelbh" @confirm="confirmbh">
				<view class="table-wrap">
					<view class="table">
						<view class="table-cause">
							<text>驳回原因:</text>
						</view>
						<view class="table-result">
							<u--input placeholder="请输入驳回原因" border="surround" v-model="refusal">
							</u--input>
						</view>
					</view>
				</view>
			</u-modal>
			<!-- 审核 -->
			<u-modal :show="showsh" style="position: fixed;background-color: #FFFFFF;" showCancelButton
				@cancel="cancelsh" @confirm="confirmsh">
				<view class="table-wrap">
					<view class="table">
						<view class="table-cause">
							<text>审核原因:</text>
						</view>
						<view class="table-result">
							<u--input placeholder="请输入审核原因" border="surround" v-model="refusal">
							</u--input>
						</view>
					</view>
					<view class="table">
						<view class="table-cause">
							<text>审批结果:</text>
						</view>
						<view class="table-result">
							<u--input placeholder="请输入审批结果" border="surround" v-model="refusals">
							</u--input>
						</view>
					</view>
					<view class="table-upload">
						<view class="upimg">
							<my-image-upload :uploadFilenames="samPic" @input="ChooseImage" label="上传文件/图片：">
							</my-image-upload>
						</view>
					</view>
				</view>
			</u-modal>
		</view>
		<view>
		</view>
	</view>
</template>

<script>
	import MyImageUpload from "../component/imgupload/my-image-upload.vue"
	export default {
		name: 'Testyljdetails',
		components: {
			MyImageUpload
		},
		data() {
			return {
				show: false,
				showbh: false,
				showsh: false,
				cause: '',
				mode: '',
				result: '',
				refusal: '',
				refusals: '',
				fileList: [],
				samPic: [],
				yljdata: [],
				items: [],
				kydata: [],
				// kydata1: [],
				// kydata2: [],
				// kydata3: [],
				kyvalue: [],
				TabCur: 0,
				sjxhdata: [],
				x: [],
				opts: {
					color: [
						"#1890ff",
					],
					dataPointShapeType: "hollow",
					enableScroll: true, //开启图表拖拽功能
					dataLabel: false,
					xAxis: {
						scrollShow: false,
						itemCount: 10,
						disableGrid: true
					},
				},
				chartData: {
					categories: [],
					series: [{
						name: "抗压力值",
						data: []
					}]
				},
				chartData1: {
					categories: [],
					series: [{
						name: "抗压力值",
						data: []
					}]
				},
				chartData2: {
					categories: [],
					series: [{
						name: "抗压力值",
						data: []
					}]
				},
				chartData3: {
					categories: [],
					series: [{
						name: "抗压力值",
						data: []
					}]
				},
				chartData4: {
					categories: [],
					series: [{
						name: "抗压力值",
						data: []
					}]
				},
				chartData5: {
					categories: [],
					series: [{
						name: "抗压力值",
						data: []
					}]
				},
			}
		},
		onLoad(options) {
			this.items = JSON.parse(options.item)
			// console.log(this.items, 'sssssssssss')
			this.listData()
			this.listDatas()
		},
		methods: {
			listData() {
				var that = this;
				this.$http.get("/syj/tSyjzb/selectById", {
					// api.samplingList({
					params: {
						syjid: this.items.syjid
					}
				}).then(res => {
					// console.log(res)
					this.yljdata = res.data.result
				})
			},
			listDatas() {
				var that = this;
				this.$http.get("/syj/tSyjzb/selectById", {
					// api.samplingList({
					params: {
						syjid: this.items.syjid
					}
				}).then(res => {
					// console.log(res)
					if (res.data.result.length > 0) {
						this.yljdata = res.data.result
						// this.yljdata.forEach(latr=>{
						// 	this.xpushlatr
						// })
						// console.log(this.x);
						let dataList = this.yljdata;
						dataList.forEach(res => {
							if(res !==''||res !==nul){
								this.kydata = res.sjgc.split(",");
								this.kyvalue = res.yskylz.split(",");
								if (res.sjxh == 1) {
									this.kyvalue.forEach(res => {
										this.chartData.categories.push(parseFloat(res)) // x轴
									})
									this.kydata.forEach(res => {
										that.chartData.series[0].data.push(parseFloat(res)) //y轴
									})
									// console.log(that.chartData.series[0].data, this.chartData.categories);
								} else if (res.sjxh == 2) {
									this.kydata.forEach(res => {
										this.chartData1.categories.push(parseFloat(res))
									})
									this.kydata.forEach(res => {
										that.chartData1.series[0].data.push(parseFloat(res))
									})
								} else if (res.sjxh == 3) {
									this.kydata.forEach(res => {
										this.chartData2.categories.push(parseFloat(res))
									})
									this.kydata.forEach(res => {
										that.chartData2.series[0].data.push(parseFloat(res))
									})
								} else if (res.sjxh == 4) {
									this.kydata.forEach(res => {
										this.chartData3.categories.push(parseFloat(res))
									})
									this.kydata.forEach(res => {
										that.chartData3.series[0].data.push(parseFloat(res))
									})
								} else if (res.sjxh == 5) {
									this.kydata.forEach(res => {
										this.chartData4.categories.push(parseFloat(res))
									})
									this.kydata.forEach(res => {
										that.chartData4.series[0].data.push(parseFloat(res))
									})
								} else if (res.jsxh == 6) {
									this.kydata.forEach(res => {
										this.chartData5.categories.push(parseFloat(res))
									})
									this.kydata.forEach(res => {
										that.chartData5.series[0].data.push(parseFloat(res))
									})
								}
							}
						})

					}
				})
			},
			tabSelect(e) {
				// console.log(e)
				this.TabCur = e.currentTarget.dataset.id;
				if (this.TabCur == 1) {
					this.listDatas();
				}
				this.scrollLeft = (e.currentTarget.dataset.id - 1) * 60
			},
			// 处置
			management() {

				this.show = true
			},
			//驳回
			reject() {
				this.showbh = true
			},
			check() {
				this.showsh = true
			},
			cancel() {
				this.show = false
			},
			cancelbh() {
				this.showbh = false
			},
			cancelsh() {
				this.showsh = false
			},
			ChooseImage(uploadFilenames) {
				// console.log(uploadFilenames)
				this.fileList = uploadFilenames.toString()
			},
			confirm() {
				let params = {
					baseid: this.items.syjid,
					flag: 2,
					problemReasons: this.cause,
					handleWay: this.mode,
					handleResult: this.result,
					filePath2: this.fileList
				}
				this.$http.get(`/syjoverhandler/syjOverHandler/deallist`, {
					params
				}).then(res => {
					uni.showToast({
						title: '处理成功'
					})
					setTimeout(function() {
						uni.navigateTo({
							url: "./Testwnj"
						})
					}, 300)
				})
				this.show = false
			},
			confirmbh() {
				let params = {
					baseid: this.items.syjid,
					flag: 3,
					remark: this.refusal
				}
				this.$http.get(`/syjoverhandler/syjOverHandler/deallist`, {
					params
				}).then(res => {
					uni.showToast({
						title: '处理成功'
					})
					setTimeout(function() {
						uni.navigateTo({
							url: "./Testwnj"
						})
					}, 300)
				})
				this.showbh = false
			},
			confirmsh() {
				let params = {
					baseid: this.items.syjid,
					flag: 1,
					supervisorApproval: this.refusal,
					supervisorResult: this.refusals,
					filePath: this.fileList
				}
				this.$http.get(`/syjoverhandler/syjOverHandler/deallist`, {
					params
				}).then(res => {
					uni.showToast({
						title: '处理成功'
					})
					setTimeout(function() {
						uni.navigateTo({
							url: "./Testwnj"
						})
					}, 300)
				})
				this.showbh = false
			}
		}
	}
</script>

<style lang="scss" scoped>
	.card {
		/* width: 100%; */
		// background-color: white;
		height: auto;
		/* margin: 10px 5px 0px 5px; */
		/* border-radius: 5px; */
		color: #909399;
	}

	.card uni-view {
		padding: 5px 0px 0px 10px;
		position: relative;
		right: 3px;
	}

	.card span {
		padding-left: 10px;
		color: #606266;
	}

	.card .box1 {
		display: flex;
		flex-wrap: wrap;
		align-items: center;
		justify-content: center;
	}

	.card .box1 .box1-title {

		padding: 0;
		flex: 2;
	}

	.card .box1 .box1-content {
		padding: 0;
		flex: 7;
	}


	.card .box1 .box1-content uni-view {
		padding: 2px 0 0 2px;
	}

	.list-wrap {
		width: 700upx;
		height: auto;
		margin: 0 auto;

	}

	.wrap {
		// box-shadow: 2px 4px 7px #0081FF;
		// padding: .7rem 0 !important;
		// border: 1px solid #000;
		background-color: #fff;
		margin: 10px 0;
		border-radius: 10px;
	}

	.chartsview {
		width: 100%;
		height: 100%;
		display: flex;
		flex: 1;
		justify-content: end;
		align-items: stretch;
	}

	.xhlist {
		font-size: 15px;
		font-weight: bold;
		color: #000000;
	}

	.button_all {
		width: 350px;
		display: flex;
		margin: 10px auto;
		// border:1px solid red;
		justify-content: center;
	}

	.button_cz {
		width: 110px;
		background-color: #F08322;
		color: cornsilk;
		border-radius: 22px;
	}

	.button_sh {
		width: 110px;
	}

	.button_bh {
		width: 110px;
	}

	.table-wrap {
		width: 300px;
		height: auto;

		// border: 1px solid #E1D7F0;
		.table {
			width: 300px;
			height: auto;
			// border: 1px solid pink;
			display: flex;
			flex-direction: row;
			align-items: center;
			margin: 10px 0;

			.table-cause {
				width: 90px;
				height: auto;
			}

			.table-result {
				width: 100%;
				height: auto;
			}
		}
	}

	.table-upload {
		width: 300px;
		height: auto;
		// border: 1px solid #18BC37;
	}

	.upimg {
		// border-bottom: 1px solid #DDDDDD;
		width: 326px;
		height: auto;
		// margin-top: 25px;

		// border:1px solid blue;
		// display:flex;
		.uptext {
			width: 200px;
			height: 20px;
			// border:1px solid blue;
			font-size: 15px;
			font-family: "PingFang SC";
			font-weight: bold;
			color: #4C5971;
		}
	}
</style>
