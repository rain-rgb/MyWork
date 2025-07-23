<template>
	<view id="pilerecorddetail">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">成桩记录详情</block>
		</cu-custom>
		<view class="list">
			<view class="list-item">
				<view class="list-item-con">
					<view style="color: #333333; font-weight: 600;" class="list-item-con-name">
						{{ listdata.shebeino_dictText }}
					</view>
					<view style="color: #333333; font-weight: 600;" class="list-item-con-name">
						桩号：<span>{{ listdata.pileNo }}</span>
					</view>
					<view class="list-item-con-name">
						桩长(m)：<span>{{ parseFloat(listdata.pileRealdep).toFixed(3) }}</span>
					</view>
					<view class="list-item-con-name">
						下钻平均电流(A)：<span>{{ parseFloat(listdata.pileDelectr).toFixed(3) }}</span>
					</view>
					<view class="list-item-con-name">
						下钻平均速度(m/min)：<span>{{ parseFloat(listdata.pileDspeed).toFixed(3) }}</span>
					</view>
					<view class="list-item-con-name">
						下钻浆量(L)：<span>{{ parseFloat(listdata.pileDownbeton).toFixed(3) }}</span>
					</view>
					<view class="list-item-con-name">
						提钻平均电流(A)：<span>{{ parseFloat(listdata.pileUelectr).toFixed(3) }}</span>
					</view>
					<view class="list-item-con-name">
						提钻平均速度(m/min)：<span>{{ parseFloat(listdata.pileUspeed).toFixed(3) }}</span>
					</view>
					<view class="list-item-con-name">
						提钻浆量(L)：<span>{{ parseFloat(listdata.pileUobeton).toFixed(3) }}</span>
					</view>
					<view class="list-item-con-name">
						注浆量(L)：<span>{{ (parseFloat(listdata.pileUobeton) + parseFloat(listdata.pileDownbeton)).toFixed(3) }}</span>
					</view>
					<view class="list-item-con-name">
						水泥浆比重：<span>{{ parseFloat(listdata.pileDensity).toFixed(3) }}</span>
					</view>
					<view class="list-item-con-name">
						水泥用量(kg)：<span>{{ parseFloat(listdata.pileCement).toFixed(3) }}</span>
					</view>
					<view class="list-item-con-name">
						每米水泥用量(kg/m)：<span>{{ parseFloat(listdata.pjdep).toFixed(3) }}</span>
					</view>
					<view class="list-item-con-name">
						水灰比(%)：<span>{{ parseFloat(listdata.pileRatio).toFixed(2) }}</span>
					</view>
					<view class="list-item-con-name">
						喷浆时长(min)：<span>{{ (parseFloat(listdata.pileWorktime)/60).toFixed(0) }}</span>
					</view>
					<view class="list-item-con-name">
						倾角：<span>{{ listdata.pileX }}</span>
					</view>
					<!-- <view class="list-item-con-name">
						开始时间：<span>{{ listdata.pileStarttime }}</span>
					</view>
					<view class="list-item-con-name">
						成桩时间：<span>{{ listdata.pileTime }}</span>
					</view> -->
					<view v-if="listdata.chaobiaodengji != 0" class="list-item-con-name warn">
						预警原因：<span class="warn">{{ listdata.problem }}</span>
					</view>
					<view class="list-item-con-name"></view>
				</view>
			</view>
		</view>
		<view class="poursta">
			<view class="poursta-title"> <text class="biaoqian">1</text> 桩记录曲线</view>
			<view class="poursta-pic">
				<view class="charts-box1">
					<qiun-data-charts type="mix" :opts="quxian" :chartData="quxiandata" :ontouch="true"
						:errorShow="false" />
				</view>
			</view>
		</view>
		<!-- <view class="poursta">
			<view class="poursta-title"> <text class="biaoqian">1</text> 瞬时流量曲线</view>
			<view class="poursta-pic">
				<view class="charts-box1">
					<qiun-data-charts type="line" :opts="opts" :chartData="chartData" :ontouch="true"
						:errorShow="false" />
				</view>
			</view>
		</view> -->
		<!-- <view class="poursta">
			<view class="poursta-title"> <text class="biaoqian">1</text> 速度曲线</view>
			<view class="poursta-pic">
				<view class="charts-box1">
					<qiun-data-charts type="line" :opts="opts1" :chartData="chartData1" :ontouch="true"
						:errorShow="false" />
				</view>
			</view>
		</view> -->
		<view class="poursta">
			<view class="poursta-title"> <text class="biaoqian">1</text> 角度偏差曲线</view>
			<view class="poursta-pic">
				<view class="charts-box1">
					<qiun-data-charts type="line" :opts="opts2" :chartData="chartData2" :ontouch="true"
						:errorShow="false" />
				</view>
			</view>
		</view>
		<view v-if="listdata.chaobiaodengji!=0" class="progress">
			<u-steps direction="column">
				<u-steps-item v-for="(item,index) in handlelist" :key="index" :title="item.tile" :desc="item.desc">
					<u-icon :name="item.time==null?item.sta2:item.sta1" slot="icon" size='22'></u-icon>
					<view class="" style="width: 95%; color: #9299A8;" slot="desc">
						<view style="height: 50upx;" v-if="item.time==null"></view>
						<view v-else class="">
							<view v-if="index == 0" class="">完成时间{{item.time}}</view>
							<view v-if="index == 1" class="">审核人{{item.person}},审核时间{{item.time}},审核意见{{item.way}}
							</view>
							<view v-if="index == 2" class="">处置人{{item.person}},处置时间{{item.time}},处理方式{{item.way}}
							</view>
							<view v-if="index == 3" class="">预警时间{{item.time}}</view>
						</view>
					</view>
				</u-steps-item>
			</u-steps>
			<view style="height: 50upx;"></view>
		</view>
		<view v-if="listdata.chaobiaodengji!=0" class="btn">
			<u-button v-has="'pilerecord:cz'" class="btnclass" type="warning" @click="checkMan" text="处置"></u-button>
			<u-button v-has="'pilerecord:bh'" class="btnclass" type="primary" @click="checkOver" plain text="驳回">
			</u-button>
			<u-button v-has="'pilerecord:sh'" class="btnclass" type="primary" @click="checkExa" text="审核"></u-button>
			<!-- <button type="default"></button>
				<button type="default"></button> -->
		</view>
		<view style="height: 40upx;"></view>
		<!-- 处置 -->
		<u-modal :show="manshow" showCancelButton :title="mantitle" @confirm="confirmman" @cancel="cancel">
			<view class="modal">
				<view class="modal-name">问题原因：</view>
				<view class="modal-input">
					<u--input type="text" v-model="mancontent.problemReasons" placeholder="请输入问题原因" />
				</view>
				<view class="modal-name">处理方式：</view>
				<view class="modal-input">
					<u--input type="text" v-model="mancontent.handleWay" placeholder="请输入处理方式" />
				</view>
				<view class="modal-name">处理结果：</view>
				<view class="modal-input">
					<u--input type="text" v-model="mancontent.handleResult" placeholder="请输入处理结果" />
				</view>
				<!-- <view class="modal-name">上传文件/图片：</view> -->
				<view class="modal-input">
					<my-image-upload :uploadFilenames="samPic" @input="ChooseImage" label="上传文件/图片" :maxImg="maxImg">
					</my-image-upload>
					<!-- <u-upload :fileList="fileList1" @afterRead="afterRead" @delete="deletePic" name="1" multiple
						:maxCount="10"></u-upload> -->
				</view>
				<view class="modal-name">备注：</view>
				<view class="modal-input">
					<u--input type="text" v-model="mancontent.remark" placeholder="请输入备注" />
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
					<u--input type="text" v-model="exacontent.supervisorApproval" placeholder="请输入审批意见" />
				</view>
				<view class="modal-name">审批结果：</view>
				<view class="modal-input">
					<u--input type="text" v-model="exacontent.supervisorResult" placeholder="请输入审批结果" />
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
	import MyImageUpload from "../component/imgupload/my-image-upload.vue"
	import ruanjiapi from "../../api/ruanji.js"
	export default {
		components: {
			MyImageUpload
		},
		data() {
			return {
				manshow: false,
				mantitle: "处置",
				mancontent: {
					filePath2: '', //上传图片地址
					overproofStatus: 10, //
					baseid: '', //
					handleResult: '', //处理结果
					problemReasons: '', //问题原因
					handleWay: '', //处理方式
					remark: '', //备注
					pileno: '', //桩号
					sbbh: '', //设备编号
				},
				overshow: false,
				overtitle: "驳回",
				overcontent: {
					remark: '', //驳回原因
					id: '', //处置表id
					baseid: '',
					overproofStatus: 30
				},
				exashow: false,
				exatitle: "审核",
				exacontent: { //审核内容
					filePath: '', //上传图片地址
					overproofStatus: 20, //
					supervisorResult: '', //审核结果
					supervisorApproval: '', //审核意见
					id: '', //处置表id
					baseid: ''
				},
				samPic: [],
				maxImg: 4,
				fileList: [],
				liucengimg: [{
						sta1: '../../static/pour/finish1.png', //完成
						sta2: '../../static/pour/finish2.png', //未完成
					},
					{
						sta1: '../../static/pour/audit1.png',
						sta2: '../../static/pour/audit2.png',
					},
					{
						sta1: '../../static/pour/chuzhi1.png',
						sta2: '../../static/pour/chuzhi2.png',
					},
					{
						sta1: '../../static/pour/yujin1.png',
						sta2: '../../static/pour/yujin2.png',
					},
				],
				handlelist: [],
				listdata: [],
				pileno: '',
				shebeino: '',
				partEndtime: '',
				// mileage: '',
				chartData: {
					categories: [],
					series: [{
						name: "瞬时流量(L/h)",
						data: []
					}]
				},
				opts: {
					dataPointShapeType: "hollow",
					enableScroll: true, //开启图表拖拽功能
					xAxis: {
						scrollShow: true,
						itemCount: 4,
						disableGrid: true,
						rotateLabel: true,
						fontSize: 10
					},
					yAxis: {
						disabled: false,
						splitNumber: 4,
						data: [{
							type: "value",
							position: "left",
							min: 0,
							max: 0,
							tofix: 2
						}]
					},
					extra: {
						line: {
							type: "curve",
							width: 2
						},
					},
				},
				chartData1: {
					categories: [],
					series: [{
						name: "速度",
						data: []
					}]
				},
				opts1: {
					dataPointShapeType: "hollow",
					enableScroll: true, //开启图表拖拽功能
					xAxis: {
						scrollShow: true,
						itemCount: 4,
						disableGrid: true,
						rotateLabel: true,
						fontSize: 10
					},
					extra: {
						line: {
							type: "curve",
							width: 2
						},
					},
				},
				chartData2: {
					categories: [],
					series: [{
							name: "x(度)",
							data: []
						},
						{
							name: "y(度)",
							data: []
						}
					]
				},
				opts2: {
					dataPointShapeType: "hollow",
					enableScroll: true, //开启图表拖拽功能
					xAxis: {
						scrollShow: true,
						itemCount: 4,
						disableGrid: true,
						rotateLabel: true,
						fontSize: 10
					},
					yAxis: {
						disabled: false,
						splitNumber: 4,
						data: [{
							type: "value",
							position: "left",
							min: 0,
							max: 0,
							tofix: 2
						}]
					},
					extra: {
						line: {
							type: "curve",
							width: 2
						},
					},
				},
				quxian: {
					enableScroll: true,
					yAxis: {
						data: [{
								position: 'left',
								title: '流量/深度',
							},
							{
								position: 'right',
								title: '电流',
								textAlign: 'left'
							},
							{
								position: 'right',
								title: '速度',
								textAlign: 'left'
						}]
					},
					xAxis: {
						scrollShow: true,
						itemCount: 4,
						disableGrid: true,
						rotateLabel: true,
						fontSize: 10
					}
				},
				quxiandata: {
					categories: [],
					series: [{
							name: "当前深度",
							data: [],
							type: "line",
							color: "#ff0000",

						},
						{
							name: "当前电流",
							index: 1,
							data: [],
							type: "line",
						},
						{
							name: "瞬时流量",
							data: [],
							type: "line",
						},
						{
							name: "速度",
							index: 2,
							data: [],
							type: "line",
						}
					]
				}
			}
		},
		// mounted() {
		// 	this.getloadlist()
		// },
		onLoad(options) {
			this.pileno = options.pileno
			this.shebeino = options.shebeino
			this.id = options.id
			this.partEndtime = options.pileTime
			// this.mileage = options.mileage
			this.getloadlist()
			this.loadData()
			this.getmanprocess()
			this.getHistorydata()
			// this.loadData1()
		},
		methods: {
			//子组件传递的数据
			ChooseImage(uploadFilenames) {
				console.log(uploadFilenames)
				this.fileList = uploadFilenames.toString()
				console.log(this.fileList)
				// this.imglist = uploadFilenames
				// console.log(this.imglist)
			},
			// 取消
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
				this.mancontent.pileno = this.pileno
				this.mancontent.sbbh = this.shebeino
				this.mancontent.baseid = this.id
				console.log(this.mancontent)
				if (!this.mancontent.problemReasons) {
					uni.showToast({
						title: '请输入问题原因',
						icon: 'none'
					})
					return
				}
				if (!this.mancontent.handleWay) {
					uni.showToast({
						title: '请输入处理方式',
						icon: 'none'
					})
					return
				}
				if (!this.mancontent.handleResult) {
					uni.showToast({
						title: '请输入处理结果',
						icon: 'none'
					})
					return
				}
				if (this.fileList.length != 0) {
					this.mancontent.filePath2 = this.fileList
				}
				console.log(this.mancontent)
				// return
				let params = this.mancontent
				this.$http.post('/mixpileoneoverhandler/mixpileOneOverHandler/handle', params).then(res => {
					if (res.data.success) {
						this.manshow = false
						// this.$Router.replace({
						// 	name: 'workorder'
						// })
						this.$nextTick(function() {
							this.loadData()
							this.getmanprocess()
						})
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
				this.overcontent.baseid = this.id
				this.overcontent.id = this.listdata.handler.id
				// console.log(this.mancontent)
				if (!this.overcontent.remark) {
					uni.showToast({
						title: '请输入驳回原因',
						icon: 'none'
					})
					return
				}
				let params = this.overcontent
				this.$http.put('/mixpileoneoverhandler/mixpileOneOverHandler/editNext', params).then(res => {
					if (res.data.success) {
						// this.$tip.toast('审核成功')
						this.$nextTick(function() {
							this.loadData()
							this.getmanprocess()
						})
						this.overshow = false
						// this.$Router.replace({
						// 	name: 'workorder'
						// })
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
				this.exacontent.baseid = this.id
				this.exacontent.id = this.listdata.handler.id
				console.log(this.exacontent)
				if (!this.exacontent.supervisorApproval) {
					uni.showToast({
						title: '请输入审核意见',
						icon: 'none'
					})
					return
				}
				if (!this.exacontent.supervisorResult) {
					uni.showToast({
						title: '请输入审核结果',
						icon: 'none'
					})
					return
				}
				if (this.fileList.length != 0) {
					this.exacontent.filePath = this.fileList
				}

				let params = this.exacontent
				this.$http.put('/mixpileoneoverhandler/mixpileOneOverHandler/editNext', params).then(res => {
					if (res.data.success) {
						this.$tip.toast('审核成功')
						this.$nextTick(function() {
							this.loadData()
							this.getmanprocess()
						})
						this.exashow = false
						// this.$Router.replace({
						// 	name: 'workorder'
						// })
					} else {
						this.$tip.toast('提交失败')
					}
				})

			},
			// 获取成桩记录数据
			getloadlist() {
				let params = {
					column: 'id',
					order: 'desc',
					id: this.id
				}
				ruanjiapi.pilerecordlist({
					params
				}).then(res => {
					this.listdata = res.data.result.records[0]
				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			// 成桩记录处置流程信息
			getmanprocess() {
				let params = {
					baseid: this.id
				}
				this.$http.get('deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/handlelist', {
					params
				}).then(res => {
					this.handlelist = res.data.result.map((item, index) => {
						return {
							...item,
							...this.liucengimg[index]
						};
					});
					// console.log(this.handlelist)
				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			URLencode(sStr) {
			  return escape(sStr).replace(/\+/g, '%2B').replace(/\ /g,'%20').replace(/\'/g, '%27').replace(/\//g,'%2F')
			},
			// 软基历史数据查询 曲线使用
			getHistorydata() {
				// console.log(this.pileno)
				// console.log(this.mileage)
				let params = {
					shebeino: this.shebeino,
					pileno: this.pileno,
					// mileage: this.mileage
				}
				this.$http.get('/devicemixpilehistorydata/deviceMixpileHistorydata/list1', {
					params
				}).then(res => {
					// console.log(res,"===================")
					let datas = res.data.result
					let datatime = []
					let curdep = []
					let elec = []
					let flowlst = []
					let speed = []
					datas.forEach(e => {
						datatime.push(e.datatime.split(' ')[1])
						curdep.push(parseFloat(e.curdep).toFixed(2))
						elec.push(parseFloat(e.elec).toFixed(2))
						flowlst.push(parseFloat(e.flowlst).toFixed(2))
						speed.push(parseFloat(e.speed).toFixed(2))
					})
					this.quxiandata.categories = datatime
					this.quxiandata.series[0].data = curdep
					this.quxiandata.series[1].data = elec
					this.quxiandata.series[2].data = flowlst
					this.quxiandata.series[3].data = speed
				})
			},
			// 曲线
			loadData() {
				let params = {
					pageNo: 1,
					pageSize: 500,
					shebeino: this.shebeino,
					pileNo: this.pileno,
					partEndtime: this.partEndtime,
				}
				this.$http.get('/devicemixpilehistorydatapart/deviceMixpileHistorydataPart/list', {
					params
				}).then(res => {
					// console.log(res)
					let datas = res.data.result.records
					let datatime = []
					let penjianliang = []
					let paixu = []
					let paixu1 = []
					let duansudu = []
					let x = []
					let y = []
					datas.forEach(e => {
						datatime.push(e.datatime.split(' ')[1])
						penjianliang.push(Math.round((parseFloat(e.partBeton)) * 100) / 100)
						duansudu.push(Math.round((parseFloat(e.partSpeed)) * 100) / 100)
						paixu.push(Math.round((parseFloat(e.partBeton)) * 100) / 100)
						paixu1.push(Math.round((parseFloat(e.partX)) * 100) / 100)
						x.push(Math.round((parseFloat(e.partX)) * 100) / 100)
						y.push(Math.round((parseFloat(e.partY)) * 100) / 100)
					})

					// console.log(datatime)
					// console.log(duansudu)
					this.opts.yAxis.data[0].min = Math.floor(paixu.sort(this.compare)[0])
					this.opts.yAxis.data[0].max = Math.ceil(paixu.sort(this.compare)[paixu.length - 1])
					// console.log(penjianliang)
					this.chartData.categories = datatime.reverse()
					this.chartData1.categories = datatime.reverse()
					this.chartData2.categories = datatime.reverse()
					this.chartData.series[0].data = penjianliang.reverse()
					this.chartData1.series[0].data = duansudu.reverse()
					this.opts2.yAxis.data[0].min = Math.floor(paixu1.sort(this.compare)[0])
					this.opts2.yAxis.data[0].max = Math.ceil(paixu1.sort(this.compare)[paixu1.length - 1])
					this.chartData2.series[0].data = x.reverse()
					this.chartData2.series[1].data = y.reverse()
				})
			},
			compare(val1, val2) {
				return val1 - val2;
			},
			// 时间获取
			timeapi(time, fmt) {
				var o = {
					"M+": time.getMonth() + 1, //月份
					"d+": time.getDate(), //日
					"h+": time.getHours(), //小时
					"m+": time.getMinutes(), //分
					"s+": time.getSeconds(), //秒
					"q+": Math.floor((time.getMonth() + 3) / 3), //季度
					S: time.getMilliseconds(), //毫秒
				};
				if (/(y+)/.test(fmt)) {
					fmt = fmt.replace(
						RegExp.$1,
						(time.getFullYear() + "").substr(4 - RegExp.$1.length)
					);
				}
				for (var k in o) {
					if (new RegExp("(" + k + ")").test(fmt)) {
						fmt = fmt.replace(
							RegExp.$1,
							RegExp.$1.length == 1 ?
							o[k] :
							("00" + o[k]).substr(("" + o[k]).length)
						);
					}
				}
				return fmt;
			},
		},



	}
</script>

<style lang="scss" scoped>
	#pilerecorddetail {
		.slot-icon {
			width: 21px;
			height: 21px;
			border: 1px solid #CBD0D9;
			border-radius: 100px;
			font-size: 12px;
			line-height: 21px;
			text-align: center;
		}

		.list {
			width: 100%;
			margin-top: 30upx;

			&-item {
				position: relative;
				width: 690upx;
				// height: 462upx;
				margin: 0 auto;
				margin-bottom: 30upx;
				border-radius: 16upx;
				background-color: white;

				.red {
					background-image: url(../../static/pour/sta1.png);
					background-repeat: no-repeat;
					background-size: 100% 100%;
				}

				.green {
					background-image: url(../../static/pour/sta4.png);
					background-repeat: no-repeat;
					background-size: 100% 100%;
				}

				&-sta {
					position: absolute;
					right: 0px;
					width: 165upx;
					height: 165upx;

					view {
						position: absolute;
						transform: rotate(45deg);
						top: 40upx;
						left: 65upx;
					}
				}

				&-sta1 {
					position: absolute;
					right: 20upx;
					bottom: 20upx;
					width: 165upx;
					height: 165upx;

					image {
						width: 100%;
						height: 100%;
					}
				}

				&-con {
					width: 610upx;
					// height: 336upx;
					margin: 0 auto;

					// background-color: red;
					&-name {
						padding-top: 30upx;
						color: #9299A8;
						font-size: 28upx;

						span {
							color: #4C5971;
						}
					}

					.warn {
						color: red;
					}
				}

			}
		}

		.poursta {
			width: 690upx;
			height: 520upx;
			margin: 0 auto;
			margin-top: 30upx;
			border-radius: 16upx;
			background-color: #FFFFFF;

			&-title {
				padding-top: 25upx;
				margin-left: 30upx;
				font-size: 36upx;
				font-weight: 600;
				color: #333333;

			}

			&-pic {
				width: 100%;
				// height: ;
				// background-color: red;

				.charts-box {
					width: 100%;
					height: 320upx;
				}

				.charts-box1 {
					width: 100%;
					height: 420upx;
				}
			}

			&-pro {
				width: 94%;
				margin: 0 auto;
			}
		}

		.biaoqian {
			width: 12upx;
			height: 34upx;
			border-radius: 6upx;
			margin-right: 30upx;
			color: #4A7FFF;
			background-color: #4A7FFF;
		}

		.modal {
			width: 100%;

			// background-color: red;
			&-name {
				color: #4C5971;
				font-size: 30upx;
				margin: 15upx 0;
			}
		}

		.progress {
			width: 690upx;
			// height: 736upx;
			margin: 0 auto;
			margin-top: 30upx;
			border-radius: 16upx;
			background-color: #FFFFFF;
			padding-top: 30upx;
			padding-left: 30upx;
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
	}
</style>
