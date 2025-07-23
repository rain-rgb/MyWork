<template>
	<view id="home">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">钢筋保护层</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<view class="section" v-for="(item,index) in shitilist" :key="index">
			<navigator>
				<view class="section-text" @click="clickJump(item.id,item.testid)">
					<view class="">
						设备名称:<span>{{item.shebeiNo_dictText!==null?item.shebeiNo_dictText:'暂无数据'}}</span>
					</view>
					<view class="">
						任务单编号:<span>{{item.code!==null?item.code:'暂无数据'}}</span>
					</view>
					<view class="">项目名称:<span>{{item.projectname!==null?item.projectname:'暂无数据'}}</span>
					</view>
					<view class="">具体部位:<span>{{item.sgbw !==null?item.sgbw:'暂无数据'}}</span>
					</view>
					<view class="">检测时间:<span>{{item.checktime !==null?item.checktime:'暂无数据'}}</span>
					</view>
					<view class="">
						检测类型:<span
							:style="item.type ==1?'color:#8bc34a;font-family: cursive;font-weight:bold':item.type ==2?'color:orange;font-family: cursive;font-weight:bold':item.type ==3?'color:#ffc107;font-family: cursive;font-weight:bold':item.type ==4?'color:#e8fb47a6;font-family: cursive;font-weight:bold':item.type ==5?'color:#69aaff;font-family: cursive;font-weight:bold':''">{{item.type ==1?'厚度':item.type ==2?'波形':item.type ==3?'剖面':item.type ==4?'网格':item.type ==5?'规范':''}}</span>
					</view>
					<view class="">
						检测对象类型:<span>{{item.targettype !==null?item.targettype:'暂无数据'}}</span>
					</view>
					<view class="">测点总数:<span>{{item.zonecount !==null?item.zonecount:'暂无数据'}}</span></view>
					<view class="">合格率(%):<span>{{item.passrate !==null?item.passrate:'暂无数据'}}</span>
					</view>
					<view class="">检测位置:<span>{{item.checklocation !==""?item.checklocation:'暂无数据'}}</span>
					</view>
					<view class="">
						主筋直径(mm):<span>{{item.masterdiameter !==null?item.masterdiameter:'暂无数据'}}</span>
					</view>
					<view class="">
						主筋间距(mm):<span>{{item.masterspacing !==null?item.masterspacing:'暂无数据'}}</span>
					</view>
					<view class="">
						设计厚度(mm):<span>{{item.designthickness !==null?item.designthickness:'暂无数据'}}</span>
					</view>
					<view class="">
						纵向设计厚度:<span>{{item.zdesignthickness !==null?item.zdesignthickness:'暂无数据'}}</span>
					</view>
					<!-- <view class="">所属标段:<span>{{item.userdepartid !==null?item.userdepartid:'暂无数据'}}</span>
					</view> -->
					<view class="">箍筋直径(mm):<span>{{item.subdiameter !==null?item.subdiameter:'暂无数据'}}</span>
					</view>
					<view class="">箍筋间距(mm):<span>{{item.subspacing !==null?item.subspacing:'暂无数据'}}</span>
					</view>
					<view class="">
						曲面直径:<span
						>{{item.curveddiameter !== null?item.curveddiameter:'暂无数据'}}</span>
					</view>
					<view class="">检测人:<span>{{item.testerid !==""?item.testerid:'暂无数据'}}</span>
					</view>
					<!-- <view class="">
						<u-line-progress :percentage="item.sjxhsl" activeColor="#19be6b" height=20></u-line-progress>
					</view> -->
				</view>
			</navigator>
		</view>
		<!-- 钢筋保护层弹框 -->
		<view class="tables">
			<u-modal :show="showgb" closeOnClickOverlay @close="() => show = false" :showConfirmButton="false"
				showCancelButton @cancel="cancelgb" :title="title" cancelColor="#000">
				<view class="sectionwrap">
					<view class="section-all">
						<view class="section-text">
							<view class="title">
								<!-- <view class="round"></view> -->
								<view class="title-font">基础信息</view>
							</view>
							<view class="">测点总数:<span>{{basicdata.zonecount}}</span></view>
							<view class="">合格率:<span>{{basicdata.passrate}}%</span></view>
							<view class="">检测位置:<span>{{basicdata.checklocation}}</span></view>
						</view>
					</view>
					<view class="tablelist section-all">
						<view class="title">
							<!-- <view class="round"></view> -->
							<view class="title-fonts">钢保检测数据分析</view>
						</view>
						<uni-table stripe emptyText="暂无更多数据">
							<!-- <view class="section-text"> -->
							<!-- <view class=""> -->
							<!-- </view> -->
							<!-- </view> -->
							<!-- 表头行 -->
							<uni-tr>
								<uni-th align="center">厚度</uni-th>
								<uni-th width="110" align="center">偏差</uni-th>
								<uni-th width="90" align="center">复测点数值</uni-th>
								<uni-th align="center">距离</uni-th>
								<uni-th width="110" align="center">是否合格</uni-th>
							</uni-tr>
							<!-- 表格数据行 -->
							<uni-tr v-for="(tablelist,index) in gbdata" :key="index">
								<uni-td align="center">{{tablelist.thickness}}</uni-td>
								<uni-td align="center">{{tablelist.thickness-tablelist.designthickness}}</uni-td>
								<uni-td align="center">{{tablelist.strthickness}}</uni-td>
								<uni-td align="center">{{tablelist.distance}}</uni-td>
								<uni-td align="center"
									:style="tablelist.flagpassrate == 1?'color:green':tablelist.flagpassrate == 0?'color:red':'color:orange'">
									{{tablelist.flagpassrate == 1?'合格':tablelist.flagpassrate == 0?'不合格':''}}
								</uni-td>
							</uni-tr>
						</uni-table>
		
						<view style="padding: 5px 13px;">
							<uni-pagination :total="total" title="标题文字" :current="current" @change="change" />
						</view>
					</view>
					<view class="charts-box section-all" style="width:300px;">
		
						<view class="title">
		
							<view class="title-fonts">钢保检测数据分析厚度</view>
						</view>
						<qiun-data-charts type="demotype" :chartData="chartData" :loadingType="10"
							:disableScroll="true" canvasId="" background="none" />
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
				sbtype:42,
				
				urls: '',
				TabCur: 0,
				TabCur1: 0,
				current: 1,
				total: 50,
				pageSize: 10,
				basicdata: '',
				gbdata: {},
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
				show: false,
				showgb:false,
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
			clickJump(e, testid) {
				// console.log(e,testid)
				this.testid = testid
				this.$http.get(`/trgangjinbhcm/trGangjinbhcM/list`, {
					params: {
						id: e
					}
				}).then(res => {
					console.log(res.data.result.records[0], 'ssss')
					this.basicdata = res.data.result.records[0]
				})
				this.showgb = true
				this.gblist()
			},
			htlisttable() {
				// if (this.detailslist.component == '钢筋保护层') {
					// gjlisttable() {
					this.$http.get(`/trgangjinbhcm/trGangjinbhcM/list`, {
						params: {
							code: this.id
						}
					}).then(res => {
						console.log(res.data.result.records, 'ssss')
						if (res.data.result.records.length > 0) {
							this.materilstable = res.data.result.records
						}
					})
					// },
				// }
			},
            gblist() {
            	// console.log(pageindex,'pageindex')
            	this.chartData.categories = []
            	this.chartData.series[0].data = []
            	let current = this.current
            	let test = this.testid
            	let data = {
            		testid: test,
            		pageNo: current
            	}
            	this.$http.get(`trgangjinbhcs/trGangjinbhcS/list`, {
            		data
            	}).then(res => {
            		console.log(res, 'ffffffffff')
            		this.gbdata = res.data.result.records
            		this.gbdata.forEach((gblist, index) => {
            			// console.log(gblist, index, 'huyuyyy')
            			this.chartData.categories.push(index)
            			this.chartData.series[0].data.push(gblist.thickness)
            		})
            		this.total = res.data.result.total
            		// this.pageSize = res.data.result.size
            	})
            },
			change(e) {
				this.current = e.current
				this.gblist(this.current)
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
				this.$http.get(`/trgangjinbhcm/trGangjinbhcM/list`, {
					params: {
						column: 'id',
						order: 'desc',
						pageNo: this.pageNo,
						PageSize: 10,
						shebeiNo:this.sbNumber
					}
				}).then(res => {
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
			cancelgb() {
				this.showgb = false
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
