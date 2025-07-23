<template>
	<view id="home">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">混凝土回弹</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<view class="section" v-for="(item,index) in shitilist" :key="index">
				<view class="section-text" @click="clickht(item.id,item.testid)">
					<view class="">
						设备名称:<span>{{item.shebeiNo_dictText !== '' && item.shebeiNo_dictText!== null?item.shebeiNo_dictText:'暂无数据'}}</span>
					</view>
					<view class="">项目名称:<span>{{item.projectname !== ''&&item.projectname!== null?item.projectname:'暂无数据'}}</span>
					</view>
					<view class="">施工部位:<span>{{item.sgbw !== ' '&&item.sgbw!== null?item.sgbw:'暂无数据'}}</span>
					</view>
					<view class="">检测时间:<span>{{item.checktime  !== ''&&item.checktime!== null?item.checktime:'暂无数据'}}</span>
					</view>
					<view class="">
						检测位置:<span
							>{{item.checklocation  !== ''&&item.checklocation!== null?item.checklocation:'暂无数据'}}</span>
					</view>
					<view class="">
						构件部位:<span>{{item.place  !== ''&&item.place!== null?item.place:'未检测'}}</span>
					</view>
					<view class="">强度等级:<span>{{item.strength  !== ''&&item.strength!== null?item.strength:'暂无数据'}}</span></view>
					<view class="">任务单编号:<span>{{item.code  !== ''&&item.code!== null?item.code:'暂无数据'}}</span>
					</view>
					<view class="">测区数量:<span>{{item.testingcount  !== ''&&item.testingcount!== null?item.testingcount:'暂无数据'}}</span>
					</view>
					<view class="">
						检测面:<span>{{item.detectionsurface  !== ''&&item.detectionsurface!== null?item.detectionsurface:'暂无数据'}}</span>
					</view>
					<view class="">
						浇筑面:<span :style="item.pouringsurface ==0?'color:#8bc34a;font-family: cursive;font-weight:bold':item.pouringsurface ==1?'color:orange;font-family: cursive;font-weight:bold':''">{{item.pouringsurface ==0?'浇筑侧面':item.pouringsurface ==1?'浇筑底面':'暂无数据'}}</span>
					</view>
					<view class="">
						标准碳化深度值:<span>{{item.carbonizedepth  !== ''&&item.carbonizedepth!== null?item.carbonizedepth:'暂无数据'}}</span>
					</view>
					<view class="">检测角度:<span>{{item.detectionangle  !== ''&&item.detectionangle!== null?item.detectionangle:'暂无数据'}}</span>
					</view>
					<view class="">是否泵送:<span>{{item.ispumping  !== ''&&item.ispumping!== null?item.ispumping:'暂无数据'}}</span>
					</view>
					<view class="">浇筑日期:<span>{{item.pouringdate  !== ''&&item.pouringdate!== null?item.pouringdate:'暂无数据'}}</span>
					</view>
					<view class="">
						测试最小数值:<span
						>{{item.testingmin !== ''&&item.testingmin!== null ?item.testingmin:'暂无数据'}}</span>
					</view>
					<view class="">测区平均数值:<span>{{item.testingaverage  !== ''&&item.testingaverage!== null?item.testingaverage:'暂无数据'}}</span>
					</view>
					<view class="">标准差:<span>{{item.standarddeviation  !== ''&&item.standarddeviation!== null?item.standarddeviation:'暂无数据'}}</span>
					</view>
					<view class="">推定值:<span>{{item.estimatedvalue  !== ''&&item.estimatedvalue!== null?item.estimatedvalue:'暂无数据'}}</span>
					</view>
					<view class="">测区最小换算值:<span>{{item.showmin  !== ''&&item.showmin!== null?item.showmin:'暂无数据'}}</span>
					</view>
					<view class="">测区平均换算值:<span>{{item.showaverage  !== ''&&item.showaverage!== null?item.showaverage:'暂无数据'}}</span>
					</view>
					<view class="">标准差:<span>{{item.showstandarddev  !== ''&&item.showstandarddev!== null?item.showstandarddev:'暂无数据'}}</span>
					</view>
					<view class="">推定值:<span>{{item.showestimatedval  !== ''&&item.showestimatedval!== null?item.showestimatedval:'暂无数据'}}</span>
					</view>
					<view class="">检测人:<span>{{item.testerpeople  !== ''&&item.testerpeople!== null?item.testerpeople:'暂无数据'}}</span>
					</view>
					<!-- <view class="">
						<u-line-progress :percentage="item.sjxhsl" activeColor="#19be6b" height=20></u-line-progress>
					</view> -->
				</view>

		</view>
		<!-- 混凝土回弹弹框 -->
	<view  class="tables">
		<u-modal :show="showht" closeOnClickOverlay @close="() => showht = false" :showConfirmButton="false"
			showCancelButton @cancel="cancelht" :title="title" cancelColor="#000">
			<view class="sectionwrap">
				<view class="section-all">
					<view class="section-text">
						<view class="title">
							<!-- <view class="round"></view> -->
							<view class="title-font">基础信息</view>
						</view>
						<view class="">构件部位:<span>{{htdata.place}}</span></view>
						<view class="">强度等级:<span>{{htdata.strength}}</span></view>
						<view class="">测区数量:<span>{{htdata.testingcount}}</span></view>
						<view class="">检测面:<span>{{htdata.detectionsurface}}</span></view>
						<view class="">标准碳化深度值:<span>{{htdata.carbonizedepth}}</span></view>
						<view class="">检测角度:<span>{{htdata.detectionangle}}</span></view>
					</view>
				</view>
				<view class="tablelist section-all">
					<view class="title">
						<!-- <view class="round"></view> -->
						<view class="title-fonts">混凝土回弹检测数据分析</view>
					</view>
					<uni-table border stripe emptyText="暂无更多数据">
						<!-- <view class="section-text"> -->
						<!-- <view class=""> -->
						<!-- </view> -->
						<!-- </view> -->
						<!-- 表头行 -->
						<uni-tr>
							<uni-th align="center">回弹平均值</uni-th>
							<uni-th align="center">测区</uni-th>
							<uni-th align="center">强度值</uni-th>
							<uni-th align="center">强度值</uni-th>
							<uni-th align="center">面修正值</uni-th>
							<uni-th align="center">角度修正值</uni-th>
							<uni-th align="center">浇筑面</uni-th>
							<uni-th align="center">浇筑面</uni-th>
							<uni-th align="center">碳化值</uni-th>
						</uni-tr>
						<!-- 表格数据行 -->
						<uni-tr v-for="(tablelist,index) in htlistdata" :key="index">
							<uni-td align="center">{{tablelist.average}}</uni-td>
							<uni-td align="center">
								<view v-for="(items,index) in tablelist.cequlist" :key="index">
									{{items}}
								</view>
							</uni-td>
							<uni-td align="center">{{tablelist.carbonize}}</uni-td>
							<uni-td align="center">{{tablelist.strcar}}</uni-td>
							<uni-td align="center">
								{{tablelist.calsurface}}
							</uni-td>
							<uni-td align="center">{{tablelist.calangle}}</uni-td>
							<uni-td align="center"
								:style="tablelist.pouringsurface ==0?'color:green':tablelist.pouringsurface ==1?'color:blue':'color:orange'">
								{{tablelist.pouringsurface ==0?'浇筑侧面':tablelist.pouringsurface ==1?'浇筑地面':''}}
							</uni-td>
							<uni-td align="center"
								:style="tablelist.surface ==1?'color:orange':tablelist.surface ==2?'color:blue':tablelist.surface ==3 ?'color:green':''">
								{{tablelist.surface == 1?'表面':tablelist.surface ==2?'底面':tablelist.surface==3?'侧面':''}}
							</uni-td>
							<uni-td align="center">{{tablelist.carbonization}}</uni-td>
						</uni-tr>
					</uni-table>
					<view style="padding: 5px 13px;">
						<uni-pagination :total="totalht" title="标题文字" :current="currentht" @change="changeht" />
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
	// import dict from '../../component/dict/dict.vue'
	import timeSelector from '@/components/wing-time-selector/wing-time-selector.vue';
	// import Byspublic from '../../component/Byspublic/Byspublic.vue'
	import api from '@/api/apis.js'
	export default {
		name: 'departlist',
		components: {
			// dict,
			timeSelector,
			// Byspublic
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
				sbtype:43,
				
				urls: '',
				TabCur: 0,
				TabCur1: 0,
				current: 1,
				currentht: 1,
				total: 50,
				totalht: 50,			
				pageSize: 10,
				basicdata: '',
				htdata: '',
				htlistdata: {},
				cktable: {},
				ckvalue: {},
				cktongdao: {},
				ckanalysis: {},
				zjdata: {},
				zjvalue: {},
				detailList: [],
				testid: '',
				htid: '',
				showht:false,
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
			cancelht() {
				this.showht = false
			},
			//点击详情中弹出模态框
			clickht(e, htid) {
				this.htid = htid
				console.log(e, htid, '混凝土回弹')
				this.showht = true
				this.$http.get(`/trhnthtm/trHnthtM/list`, {
					params: {
						id: e
					}
				}).then(res => {
					console.log(res.data.result.records[0], 'ddddd')
					this.htdata = res.data.result.records[0]
				})
				this.hntlist()
			},
			htlisttable() {
					this.$http.get(`/trhnthtm/trHnthtM/list`, {
						params: {
							code: this.id
						}
					}).then(res => {
						console.log(res, '混凝土回弹')
						if (res.data.result.records.length > 0) {
							this.gbilstable = res.data.result.records
						}
					})
				},
			// 混凝土回弹
			hntlist() {
				let currentht = this.currentht
				let ht = this.htid
				let data = {
					testid: ht,
					pageNo: currentht
				}
				this.$http.get(`/trhnthts/trHnthtS/list1`, {
					data
				}).then(res => {
					// console.log(res.data.result.records, '回弹表格数据')
					this.htlistdata = res.data.result.records
					this.totalht = res.data.result.total
				})
			},
			changeht(e) {
				this.currentht = e.current
				this.hntlist(this.currentht)
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
				}

				this.textlist()
				this.show = false
			},
			textlist() {
				this.$http.get(`/trhnthtm/trHnthtM/list`, {
					params: {
						column: 'id',
						order: 'desc',
						pageNo: this.pageNo,
						PageSize: 10,
						shebeiNo:this.sbNumber
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
