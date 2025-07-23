<template>
	<view id="home">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">成孔质量检测</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<view class="section" v-for="(item,index) in shitilist" :key="index">
			<view class="section-text" @click="clickzl(item.id,item.testid)">
				<view class="">
					设备名称:<span>{{item.shebeino_dictText!==null?item.shebeino_dictText:'暂无数据'}}</span>
				</view>
				<view class="">流水号:<span>{{item.serialno!==null?item.serialno:'暂无数据'}}</span>
				</view>
				<view class="">测试仪编号:<span>{{item.machineid!==null?item.machineid:'暂无数据'}}</span>
				</view>
				<view class="">试桩编号:<span>{{item.pileno !==null?item.pileno:'暂无数据'}}</span>
				</view>
				<view class="">
					检测日期:<span>{{item.testpiledate !==null?item.testpiledate:'暂无数据'}}</span>
				</view>
				<view class="">
					检测时间:<span>{{item.testpiletime !==''?item.testpiletime:'未检测'}}</span>
				</view>
				<view class="">检测人员:<span>{{item.worker !==''?item.worker:'暂无数据'}}</span></view>
				<view class="">检测单位:<span>{{item.testunit !==''?item.testunit:'暂无数据'}}</span>
				</view>
				<view class="">施工单位:<span>{{item.constructionunit !==''?item.constructionunit:'暂无数据'}}</span>
				</view>
				<view class="">
					工程名称:<span>{{item.projectname !==''?item.projectname:'暂无数据'}}</span>
				</view>
				<view class="">
					孔深(m):<span>{{item.begindepth !==null?item.projectname:'暂无数据'}}</span>
				</view>
				<view class="">
					孔径(mm):<span>{{item.diameter !==''?item.diameter:'暂无数据'}}</span>
				</view>
				<view class="">始测深度(m):<span>{{item.begindepth !==''?item.begindepth:'暂无数据'}}</span>
				</view>
				<view class="">标高(m):<span>{{item.height !==''?item.height:'暂无数据'}}</span>
				</view>
				<view class="">管数:<span>{{item.pipenums !==null?item.pipenums:'暂无数据'}}</span>
				</view>
				<view class="">
					移距(mm):<span>{{item.shift !== ''?item.shift:'暂无数据'}}</span>
				</view>
				<view class="">偏移角:<span>{{item.angle !==''?item.angle:'暂无数据'}}</span>
				</view>
				<view class="">采集方式:<span
						:style="item.method ==0?'color:green;font-weight:bold;font-family: cursive;':item.method ==1?'color:blue;font-weight:bold;font-family: cursive;':item.method ==2?'color:orange;font-weight:bold;font-family: cursive;':''">{{item.method ==0?'自动提升0':item.method ==1?'手动提升':item.method ==2?'手动(4s/m)':'暂无数据'}}</span>
				</view>
				<view class="">数字滤波:<span
						:style="item.filter ==0?'color:red;font-family: cursive;font-weight:bold':item.filter ==1?'color:green;font-family: cursive;font-weight:bold':''">{{item.filter ==0?'未启动':item.filter ==1?'启动':'暂无数据'}}</span>
				</view>
				<view class="">泥浆补偿:<span
						:style="item.density ==0?'color:red;font-family: cursive;font-weight:bold':item.density>0?'color:green;font-family: cursive;font-weight:bold':''">{{item.density ==0?'未启动':item.density >0?'启动':'暂无数据'}}</span>
				</view>
				<view class="">波形增强:<span
						:style="item.waveenhance ==0?'color:red;font-family: cursive;font-weight:bold':item.waveenhance ==1?'color:green;font-family: cursive;font-weight:bold':''">{{item.waveenhance ==0?'未启动':item.waveenhance ==1?'启动':'暂无数据'}}</span>
				</view>
				<view class="">判读算法:<span
						:style="item.algorithm ==0?'color:green;font-family: cursive;font-weight:bold':item.algorithm ==1?'color:blue;font-family: cursive;font-weight:bold':'color:orange;font-family: cursive;font-weight:bold'">{{item.algorithm ==0?'自动判读':item.algorithm ==1?'幅值判读':'方差判读'}}</span>
				</view>
				<view class="">幅值参数:<span>{{item.amplitude !==''?item.amplitude:'暂无数据'}}</span>
				</view>
				<view class="">方差:<span>{{item.variance !==''?item.variance:'暂无数据'}}</span>
				</view>
				<view class="">
					主频:<span>{{item.frequency !==''?item.frequency:'暂无数据'}}</span>
				</view>
				<view class="">
					起点:<span
						:style="item.origin ==0?'color:green;font-family: cursive;font-weight:bold':item.origin ==1?'color:orange;font-family: cursive;font-weight:bold':''">{{item.origin ==0?'负':item.origin ==1?'正':'暂无数据'}}</span>
				</view>
				<view class="">
					首波方式:<span
						:style="item.mode ==0?'color:red;font-family: cursive;font-weight:bold':item.mode ==1?'color:green;font-family: cursive;font-weight:bold':''">{{item.mode ==0?'负':item.mode ==1?'正':'暂无数据'}}</span>
				</view>
				<view class="">检测标准:<span>{{item.standard !==''?item.standard:'暂无数据'}}</span>
				</view>
				<view class="">采集方向:<span
						:style="item.direction ==0?'color:green;font-family: cursive;font-weight:bold':item.direction ==1?'color:blue;font-family: cursive;font-weight:bold':''">{{item.direction ==0?'下放':item.direction ==1?'上拉':'暂无数据'}}</span>
				</view>
				<view class="">罗盘修正:<span
						:style="item.compass ==0?'color:red;font-family: cursive;font-weight:bold':item.compass ==1?'color:green;font-family: cursive;font-weight:bold':''">{{item.compass ==0?'未修正':item.compass ==1?'已修正':'暂无数据'}}</span>
				</view>
				<view class="">罗盘基准角:<span>{{item.compassangle !== ''?item.compassangle:'暂无数据'}}</span>
				</view>
				<view class="">采样间隔(us):<span>{{item.sampleinterval !==''?item.sampleinterval:'暂无数据'}}</span>
				</view>
				<view class="">采样长度:<span>{{item.samplelength !==''?item.samplelength:'暂无数据'}}</span>
				</view>
				<view class="">高通(kHz):<span>{{item.highfilter !==''?item.highfilter:'暂无数据'}}</span>
				</view>
				<view class="">低通(kHz):<span>{{item.lowfilter !==''?item.lowfilter:'暂无数据'}}</span>
				</view>
				<view class="">发射脉宽(us):<span>{{item.pulsewidth !==''?item.pulsewidth:'暂无数据'}}</span>
				</view>
				<view class="">发射电压:<span
						:style="item.voltage ==0?'color:green;font-family: cursive;font-weight:bold':item.voltage ==1?'color:red;font-family: cursive;font-weight:bold':''">{{item.voltage ==0?'低压':item.voltage ==1?'高压':'暂无数据'}}</span>
				</view>
				<view class="">滑轮直径(mm):<span>{{item.pulleydiameter !==''?item.pulleydiameter:'暂无数据'}}</span>
				</view>
				<view class="">电缆直径(mm):<span>{{item.cablediameter !==''?item.cablediameter:'暂无数据'}}</span>
				</view>
				<view class="">绞车速度:<span>{{item.winchspeed !==''?item.winchspeed:'暂无数据'}}</span>
				</view>
				<view class="">是否校系:<span
						:style="item.iscorrection ==0?'color:red;font-family: cursive;font-weight:bold':item.iscorrection ==1?'color:green;font-family: cursive;font-weight:bold':''">{{item.iscorrection ==0?'不启用':item.iscorrection ==1?'启用':'暂无数据'}}</span>
				</view>
				<view class="">波幅校系:<span>{{item.amplitudecorrection !==''?item.amplitudecorrection:'暂无数据'}}</span>
				</view>
				<!-- <view class="">
						<u-line-progress :percentage="item.sjxhsl" activeColor="#19be6b" height=20></u-line-progress>
					</view> -->
			</view>

		</view>
		<!-- 混凝土回弹弹框 -->
		<view class="tables">
			<u-modal :show="showck" closeOnClickOverlay @close="() => showck = false" :showConfirmButton="false"
				showCancelButton @cancel="cancelck" :title="title" cancelColor="#000">
				<view class="sectionwrap">
					<view class="section-all">
						<view class="section-text">
							<view class="title">
								<!-- <view class="round"></view> -->
								<view class="title-font">基础信息</view>
							</view>
							<view class="">孔深(m):<span>{{ckdata.depth}}</span></view>
							<view class="">孔径(mm):<span>{{ckdata.diameter}}</span></view>
							<view class="">始测深度(m):<span>{{ckdata.begindepth}}</span></view>
							<view class="">标高(m):<span>{{ckdata.height}}</span></view>
							<view class="">管数:<span>{{ckdata.pipenums}}</span></view>
							<view class="">移距(mm):<span>{{ckdata.shift}}</span></view>
							<view class="">偏移角:<span>{{ckdata.angle}}</span></view>
						</view>
					</view>
					<view class="" style="width: 300px;">
						<scroll-view scroll-x class="nav bg-white">
							<view class="flex text-center">
								<view class="cu-item flex-sub" :class="0==TabCur?'text-blue cur':''" @tap="tabSelect"
									data-id="0">
									通道参数
								</view>
								<view class="cu-item flex-sub" :class="1==TabCur?'text-blue cur':''" @tap="tabSelect"
									data-id="1">
									孔径通道采样数据
								</view>
								<view class="cu-item flex-sub" :class="2==TabCur?'text-blue cur':''" @tap="tabSelect"
									data-id="2">
									孔径分析数据
								</view>
							</view>
						</scroll-view>
						<view v-if="TabCur==0" class="tablelist section-all">
							<!-- <view class="title">
               					<!<view class="round"></view> -->
							<!-- 	<view class="title-fonts">通道参数</view>
               				</view> -->
							<uni-table stripe emptyText="暂无更多数据">
								<!-- <view class="section-text"> -->
								<!-- <view class=""> -->
								<!-- </view> -->
								<!-- </view> -->
								<!-- 表头行 -->
								<uni-tr>
									<uni-th align="center">通道号</uni-th>
									<uni-th align="center">状态</uni-th>
									<uni-th align="center">中心距(mm)</uni-th>
									<uni-th align="center">增益</uni-th>
									<uni-th align="center">延时(us)</uni-th>
									<uni-th align="center">校零时间(us)</uni-th>
								</uni-tr>
								<!-- 表格数据行 -->
								<uni-tr v-for="(tablelist,index) in ckvalue" :key="index">
									<uni-td align="center">{{tablelist.channelnum}}</uni-td>
									<uni-td align="center">
										<view
											:style="tablelist.status ==0?'color:red':tablelist.status ==1?'color:green':''">
											{{tablelist.status ==0?'未启用':tablelist.status ==1?'启用并显示':''}}
										</view>
									</uni-td>
									<uni-td align="center">{{tablelist.centerdistance}}</uni-td>
									<uni-td align="center">{{tablelist.gain}}</uni-td>
									<uni-td align="center">
										{{tablelist.delay}}
									</uni-td>
									<uni-td align="center">{{tablelist.zerotime}}</uni-td>
								</uni-tr>
							</uni-table>
							<view style="padding: 5px 13px;">
								<uni-pagination :total="totalck" title="标题文字" :current="currentck" @change="changeck" />
							</view>
						</view>

						<view v-if="TabCur==1" class="tablelist section-all">
							<!-- <view class="title">
               					<! <view class="round"></view> -->
							<!-- <view class="title-fonts">孔径通道采样数据</view>
               				</view> -->
							<uni-table stripe emptyText="暂无更多数据">
								<!-- <view class="section-text"> -->
								<!-- <view class=""> -->
								<!-- </view> -->
								<!-- </view> -->
								<!-- 表头行 -->
								<uni-tr>
									<uni-th align="center">通道号</uni-th>
									<uni-th align="center">深度(m)</uni-th>
									<uni-th align="center">声时(us)</uni-th>
									<uni-th align="center">泥浆声速(km/s)</uni-th>
									<uni-th align="center">波幅</uni-th>
									<uni-th align="center">方位角</uni-th>
									<uni-th align="center">半径(mm)</uni-th>
									<uni-th align="center">增益</uni-th>
									<uni-th align="center">延时(us)</uni-th>
									<uni-th align="center">增强声时(us)</uni-th>
									<uni-th align="center">修正距离(mm)</uni-th>
									<uni-th align="center">修正方向(us)</uni-th>
									<uni-th align="center">噪声档位</uni-th>
									<uni-th align="center">测点时间(s)</uni-th>
									<uni-th align="center">波形数值</uni-th>
								</uni-tr>
								<!-- 表格数据行 -->
								<uni-tr v-for="(tablelist,index) in cktongdao" :key="index">
									<uni-td align="center">{{tablelist.passway}}</uni-td>
									<uni-td align="center">{{tablelist.depth}}</uni-td>
									<uni-td align="center">{{tablelist.soundtime}}</uni-td>
									<uni-td align="center">{{tablelist.soundspeed}}</uni-td>
									<uni-td align="center">{{tablelist.amplitude}}</uni-td>
									<uni-td align="center">{{tablelist.azimuth}}</uni-td>
									<uni-td align="center">{{tablelist.radius}}</uni-td>
									<uni-td align="center">{{tablelist.gain}}</uni-td>
									<uni-td align="center">{{tablelist.delay}}</uni-td>
									<uni-td align="center">{{tablelist.enhancetime}}</uni-td>
									<uni-td align="center">{{tablelist.correctiondistance}}</uni-td>
									<uni-td align="center">{{tablelist.correctiondirection_dictText}}</uni-td>
									<uni-td align="center">{{tablelist.adjustinggear}}</uni-td>
									<uni-td align="center">{{tablelist.testtime}}</uni-td>
									<uni-td align="center">{{tablelist.wavedata}}</uni-td>
								</uni-tr>
							</uni-table>
							<view style="padding: 5px 13px;">
								<uni-pagination :total="totaldata" title="标题文字" :current="currentdata"
									@change="changeckdata" />
							</view>
						</view>


						<view v-if="TabCur==2" class="tablelist section-all">
							<!-- <view class="title">
               					<! <view class="round"></view> -->
							<!-- <view class="title-fonts">孔径分析数据</view>
               				</view> -->
							<uni-table stripe emptyText="暂无更多数据">
								<!-- <view class="section-text"> -->
								<!-- <view class=""> -->
								<!-- </view> -->
								<!-- </view> -->
								<!-- 表头行 -->
								<uni-tr>
									<uni-th align="center">设计井深(m)</uni-th>
									<uni-th align="center">设计孔径(mm)</uni-th>
									<uni-th align="center">最大孔径(mm)</uni-th>
									<uni-th align="center">最小孔径(mm)</uni-th>
									<uni-th align="center">平均孔径(mm)</uni-th>
									<uni-th align="center">偏心距(mm)</uni-th>
									<uni-th align="center">沉渣厚度(m)</uni-th>
									<uni-th align="center">垂直度(%)</uni-th>
								</uni-tr>
								<!-- 表格数据行 -->
								<uni-tr v-for="(tablelist,index) in ckanalysis" :key="index">
									<uni-td align="center">{{tablelist.designdepth}}</uni-td>
									<uni-td align="center">{{tablelist.designdiameter}}</uni-td>
									<uni-td align="center">{{tablelist.maxdiameter}}</uni-td>
									<uni-td align="center">{{tablelist.mindiameter}}</uni-td>
									<uni-td align="center">{{tablelist.avgdiameter}}</uni-td>
									<uni-td align="center">{{tablelist.eccentricity}}</uni-td>
									<uni-td align="center">{{tablelist.thickness}}</uni-td>
									<uni-td align="center">{{tablelist.verticality}}</uni-td>
								</uni-tr>
							</uni-table>
							<view style="padding: 5px 13px;">
								<uni-pagination :total="totalfx" title="标题文字" :current="currentfx" @change="changefx" />
							</view>
						</view>
					</view>
				</view>
			</u-modal>
		</view>
		<!-- 查询设备 -->
		<view class="screen" v-if="show" @confirm="confirm">
			<view class="screen-modal">
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">设备名称：</view>
					<picker @change="PickerChange" :range="deviceNames">
						<u--input placeholder="请选择设备名称" border="surround" v-model="deviceName" disabled
							suffixIcon="arrow-down"></u--input>
					</picker>
				</view>
				<view class="screen-modal-btn">
					<u-button text="取消" @click="confirm"></u-button>
					<u-button type="primary" text="确认" @click="Choose"></u-button>
				</view>

			</view>
		</view>
	</view>
</template>
<script>
	import timeSelector from '@/components/wing-time-selector/wing-time-selector.vue';
	import api from '@/api/apis.js'
	export default {
		name: 'departlist',
		components: {
			timeSelector,
		},
		data() {
			return {
				shitilist: [],
				// listdatavalue: '',
				deviceNames: [],
				devictype: [],
				deviceName: '',
				choosekey: '',
				choosevalue: '',
				sbNumber: null,
				// shows:true,
				orgcode: '',
				begintime: '',
				endtime: '',
				pageNo: 1,
				sbtype: 56,

				urls: '',
				TabCur: 0,
				TabCur1: 0,
				current: 1,
				currentck: 1,
				currentdata: 1,
				currentfx: 1,
				total: 50,
				totalck: 50,
				totaldata: 50,
				totalfx: 50,
				pageSize: 10,
				ckdata: '',
				guid: '',
				cktable: {},
				ckvalue: {},
				cktongdao: {},
				ckanalysis: {},
				zjdata: {},
				zjvalue: {},
				detailList: [],
				testid: '',
				htid: '',
				showck: false,
				show: false,
				// offmodal:true,
				title: '检测试验结果',
				detailslist: [],
				materilstable: [],
				gbilstable: [],
				id: '',
				chartData: {
					"categories": [

					],
					"series": [{
						"name": "厚度(mm)",
						"data": [

						]
					}]
				},
			}
		},
		onLoad() {
			this.textlist()
		},
		onReachBottom() {
			uni.showNavigationBarLoading()
			this.pageNo++
			this.textlist()
			uni.hideNavigationBarLoading()
		},
		created() {
			this.htlisttable()
			this.deviceType()
		},

		methods: {
			cancelck() {
				this.showck = false
			},
			//点击详情中弹出模态框
			clickzl(e, CKid) {
				this.showck = true
				this.$http.get(`/kongjingbasicinfo/kongjingBasicinfo/list`, {
					params: {
						id: e
					}
				}).then(res => {
					this.ckdata = res.data.result.records[0]
					console.log(this.ckdata, 'dddd')
					// this.guid = res.data.result.records[0].guid

				})
				setTimeout(() => {
					this.cklist()
					this.ckdatalist()
					this.ckfenxi()
				}, 500)
			},
			htlisttable() {
				this.$http.get(`/kongjingbasicinfo/kongjingBasicinfo/list`, {
					params: {
						serialno: this.id
					}
				}).then(res => {
					console.log(res.data.result.records, 'cccc')
					if (res.data.result.records.length > 0) {
						this.cktable = res.data.result.records
					}
				})
			},
			//通道参数
			cklist() {
				// console.log(this.ckdata.guid, 'ddddd')
				let currentck = this.currentck
				let data = {
					guid: this.ckdata.guid,
					column: 'id',
					order: 'desc',
					pageNo: currentck
				}
				this.$http.get(`/kongjingtongdao/kongjingTongdao/list`, {
					data
				}).then(res => {
					// console.log(res, '通道参数')
					this.ckvalue = res.data.result.records
					this.totalck = res.data.result.total
				})
			},
			//孔径通道采样数据
			ckdatalist() {
				let current = this.currentdata
				let data = {
					basicinfoid: this.ckdata.basicinfoid,
					column: 'id',
					order: 'desc',
					pageNo: current
				}
				// pageNo:this.ipagination2.current
				this.$http.get(`/kongjingpointsdata/kongjingPointsdata/list`, {
					data
				}).then(res => {
					// console.log(res, '孔径通道采样数据')
					this.cktongdao = res.data.result.records
					this.totaldata = res.data.result.total
					// console.log(this.totaldata, 'yhhhhhhhhh')
				})
			},
			//孔径分析数据
			ckfenxi() {
				let current = this.currentfx
				let data = {
					basicinfoid: this.ckdata.basicinfoid,
					column: 'id',
					order: 'desc',
					pageNo: current
				}
				this.$http.get(`/kongjinganalysisdata/kongjingAnalysisdata/list`, {
					data
				}).then(res => {
					console.log(res, '孔径分析数据')
					this.ckanalysis = res.data.result.records
					this.totalfx = res.data.result.total
					// console.log(this.ckanalysis, 'yhhhhhhhhh')
				})
			},
			changeck(e) {
				this.currentck = e.current
				this.cklist(this.currentck)
			},
			changeckdata(e) {
				this.currentdata = e.current
				this.ckdatalist(this.currentdata)
			},
			changefx(e) {
				this.currentfx = e.current
				this.ckfenxi(this.currentfx)
			},
			tabSelect(e) {
				console.log(e)
				this.TabCur = e.currentTarget.dataset.id;
				if (this.TabCur == 0) {
					this.cklist();
				} else if (this.TabCur == 1) {
					this.ckdatalist()
				} else if (this.TabCur == 2) {
					this.ckfenxi()
				}

				// this.scrollLeft = (e.currentTarget.dataset.id - 1) * 60
			},
			deviceType() {
				api.deviceType({
					params: {
						sys_depart_orgcode: this.orgcode,
						sbtypes: this.sbtype
					}
				}).then(res => {
					// console.log(res.data.result)
					let devicelist = res.data.result
					this.deviceNames = []
					this.devictype = []
					devicelist.forEach(e => {
						this.deviceNames.push(e.sbname)
						this.devictype.push(e.sbjno)
					})
				}).catch(e => {
					console.log("请求错误", e)
				})

			},
			// 设备名称
			PickerChange(e) {
				// console.log(e)
				// this.cbindex = -1
				this.index = e.detail.value
				this.imei = this.devictype[this.index]
				this.choosekey = 2
				this.choosevalue = this.devictype[this.index]
				this.deviceName = this.deviceNames[this.index]
				this.begintime = ''
				this.endtime = ''
				// this.Choose()
			},

			Choose(choosekey, choosevalue) {
				// console.log(this.choosekey,this.choosevalue);
				// 设备名称
				if (this.choosekey == 2) {
					this.sbNumber = this.choosevalue
					// console.log(this.sbNumber)
					this.begintime = ''
					this.endtime = ''
				}

				this.textlist()
				this.show = false
			},
			textlist() {
				this.$http.get(`/kongjingbasicinfo/kongjingBasicinfo/list`, {
					params: {
						column: 'id',
						order: 'desc',
						pageNo: this.pageNo,
						PageSize: 10,
						shebeino: this.sbNumber
						// sbbh: this.sbNumber,
						// shebeibianhao: this.sbNumber,
					}
				}).then(res => {
					console.log(res.data.result.records, 'kkkkkkkkkkkkkkkkkk')
					if (res.data.success) {
						setTimeout(() => {
							// this.shows = false
							if (res.data.result.records.length == 0) {
								uni.showToast({
									title: '别下拉啦 没有更多数据了',
									icon: 'none'
								})
							}
							if (this.pageNo != 1) {
								this.shitilist = this.shitilist.concat(res.data.result.records)
							} else {
								this.shitilist = res.data.result.records
							}
						}, 300)
					}

				})
			},
			screen() {
				this.deviceName = ''
				this.show = true
			},
			cancel() {
				this.show = false
			},
			// handleOk() {
			// 	this.textlist()
			// 	this.show = false
			// },
			confirm() {
				this.show = false
			},
		}
	}
</script>

<style lang="scss" scoped>
	#home {
		// background-color: #F2F5FE;
		width: 100%;
		height: auto;
		background-color: #F2F5FE;
		// white-space: nowrap;
	}

	.bg {
		width: 100%;
		height: 150upx;
		background-color: #ffffff;
		position: fixed;
	}

	.header {
		width: 100%;
		height: 100upx;
		display: flex;

		// background-color: #506CE2;
		&-left {

			width: 20%;
			display: flex;
			justify-content: center;
			align-items: flex-end;

			&-tagpage {
				width: 34upx;
				height: 37upx;
				margin-left: 30upx;
			}

			&-screen {
				width: 34upx;
				height: 37upx;
				margin-left: 30upx;
			}

			// background-color: red;
		}

		&-center {
			width: 60%;
			// background-color: blue;
			display: flex;
			align-items: flex-end;
			overflow: hidden;
			white-space: nowrap;

			.center {
				width: 40%;
				text-align: center;
				font-size: 38upx;
				color: #FFFFFF;
			}

			.side {
				width: 30%;
				text-align: center;
				color: rgba(255, 255, 255, 0.4);
				// overflow: hidden;
				// white-space: nowrap;
			}
		}

		&-right {
			width: 20%;
			display: flex;
			justify-content: center;
			align-items: flex-end;

			&-tagpage {
				width: 34upx;
				height: 37upx;
				margin-left: 30upx;
			}

			&-screen {
				width: 34upx;
				height: 37upx;
				margin-left: 30upx;
			}

			// background-color: red;
		}
	}

	.newcolor {
		color: #000;
		font-size: 30upx;
	}

	.top-two {
		width: 700upx;
		height: 180upx;
		margin: 0 auto;
		// margin-top: 80upx;
		// border-radius: 16upx;
		// border:1px solid #333333;
		display: flex;
		justify-content: space-between;
		align-items: center;

		.left {
			width: 225upx;
			height: 150upx;
			border-radius: 10px;
			background-color: #ffffff;
			display: flex;
			justify-content: space-evenly;
			align-items: center;

			.left-img {
				width: 120upx;
				height: 110upx;
				// border: 1px solid blue;
				background: #cfe0fd;
				border-radius: 10px;

				.img {
					width: 60upx;
					height: 60upx;
					background-image: url(../../../static/shiti/five.png);
					// border: 1px solid #000;
					margin: 0 auto;
					line-height: 90upx;
					margin: 12px auto;
					background-size: 100% 92%;
					background-repeat: no-repeat;
				}
			}

			.left-text {
				width: 140upx;
				height: 100upx;
				// border: 1px solid #18BC37;
				color: #4C5971;
				text-align: center;
				font-size: 27upx;
				line-height: 50upx;
				font-family: 'DIN-Medium';

				.red {
					color: #FF233D;
					font-size: 50upx;
				}

				.orange {
					color: #FF8712;
					font-size: 50upx;
				}

				.green {
					color: #0FBF6E;
					font-size: 50upx;
				}

				.purple {
					color: #8333FA;
					font-size: 50upx;
				}

				.blue {
					color: #387FFD;
					font-size: 50upx;
				}

				.black {
					color: #333333;
					font-size: 50upx;
				}

			}
		}
	}

	.newcolor/deep/.cu-bar.fixed {
		position: fixed;
		width: 100%;
		top: 22px;
		z-index: 1024;
	}

	.scrolls-y {
		width: 100%;
		height: auto;
		// white-space: nowrap;
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

	.sectionwrap {
		width: 900px;
		height: auto;
		// overflow-y: auto;
		// border: 1px solid #ffaa00;

		.section-all {
			width: 630rpx;
			height: auto;
			background-color: #fff;
			margin: 10px 0;
			// border: 1px solid #ED1C24;
			// margin: 15px auto;
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
					padding: 10upx 15upx;
					// width:300rpx;
					// height:70rpx;
					//                background: #F6F9FC;
					// border-radius: 12px;
					// display: inline-block;

				}

				.title {
					width: 200rpx;
					height: 34px;
					// border: 1px solid #18BC37;
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

					.title-font {
						font-size: 18px;
						font-family: ' PingFang SC';
						font-weight: bold;
						color: #333333;
					}
				}
			}
		}

	}

	.tablelist {
		width: 600rpx;
		height: auto;
		// overflow-y: auto;
		// border: 1px solid #5500ff;

	}

	.tables/deep/.u-modal {
		overflow-y: auto;
		height: 560px;
	}

	.tables/deep/.u-popup__content {
		background-color: #F2F5FE;
	}

	.title-fonts {
		font-size: 18px;
		font-family: ' PingFang SC';
		font-weight: bold;
		color: #333333;
		padding: 0 15px;
	}
</style>
