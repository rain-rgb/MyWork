<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">抗压抗折</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<view v-if="listdatavalue.length != 0">
			<view class="card" v-for="(item,index) in listdatavalue" :key="index">
				<!-- 路面跳转 -->
				<navigator :url="'/pages/Testdetection/Testkykzdetails?item='+JSON.stringify(item)">
					<view class="" v-if="item!==null?item:'暂无数据'">
						<view v-if="item.pdjg=='合格'" class="standard"></view>
						<view v-if="item.pdjg=='不合格'" class="standards"></view>

						<view v-show="item.pdjg!=='合格'">
							<view v-if="item.overproofStatus==30" class="bh"></view>
							<view v-if="item.overproofStatus==20" class="sh"></view>
							<view v-if="item.overproofStatus==10" class="ycz"></view>
							<view v-if="item.overproofStatus==0" class="wcz"></view>
						</view>

						<view>设备名称:<span>{{item.sbbh_dictText!==null?item.sbbh_dictText:'暂无数据'}}</span></view>
						<view>试验类型:<span>{{item.sylx_dictText !==null?item.sylx_dictText:'暂无数据'}}</span></view>
						<view>工程名称:<span>{{item.cjmc !==null?item.cjmc:'暂无数据'}}</span></view>
						<view>龄期:<span>{{item.lq !==null?item.lq:'暂无数据'}}</span></view>
						<view>试件尺寸:<span>{{item.sjcc !==null?item.sjcc:'暂无数据'}}</span></view>
						<view>试件数量:<span>{{item.sjsl !==null?item.sjsl:'暂无数据'}}</span></view>
						<view>设计强度:<span>{{item.sjqd !==null?item.sjqd:'暂无数据'}}</span></view>
						<view>强度代表值:<span>{{item.qddbz !==null?item.qddbz:'暂无数据'}}</span></view>
						<view>试验日期:<span>{{item.syrq !==null?item.syrq:'暂无数据'}}</span></view>
						<view>判定结果:<span
								:style="item.pdjg =='合格'?'color:green;font-weight:bold':item.pdjg =='不合格'?'color:red;font-weight:bold':'color:orange;font-weight:bold'">{{item.pdjg}}</span>
						</view>
						<view>操作人员:<span>{{item.czry!==null?item.czry:'暂无数据'}}</span></view>
					</view>
				</navigator>
			</view>
		</view>
		<view v-else>
			<view class="card" style="text-align: center;">
				<!-- <h2 ></h2> -->
				<u-button loading loadingText="数据正在加载中">

				</u-button>
			</view>
		</view>
		<view class="screen" v-if="show">
			<view class="screen-modal">
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">设备名称：</view>
					<picker @change="PickerChange" :range="deviceNames">
						<u--input placeholder="请选择设备名称" border="surround" v-model="deviceName" disabled></u--input>
					</picker>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">试验类型：</view>
					<!-- <picker @change="Pickertype" :range="typename">
						<u--input placeholder="试验类型" border="surround" v-model="typenames" disabled></u--input>
					</picker> -->
					<dict dictCode="SYLX" @choose="choose" title="请选择试验类型">
					</dict>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">判定结果：</view>
					<view class="screen-modal-item-input">
						<view class="fixstyle">
							<picker @change="Cblevel" :value="index" :range="cblevelname">
								<text class="" style="color: rgb(192, 196, 204);">
									{{pdresult== ''?"请选择判定结果":pdresult}}
									<!-- {{cbindex>-1?cblevelname[cbindex]:'判定结果'}} -->
								</text>
								<!-- <text class="cuIcon cuIcon-unfold"></text> -->
							</picker>
						</view>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">开始时间：</view>
					<view class="screen-modal-item-input">
						<view class="fixstyle">
							<timeSelector showType="dateToTime" @btnConfirm="beginTime">
								<text class="" style="color: rgb(192, 196, 204);">
									{{this.begintime==''?'请选择开始时间':this.begintime}}
								</text>
								<!-- <text class="cuIcon cuIcon-unfold"></text> -->
							</timeSelector>
						</view>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">结束时间：</view>
					<view class="screen-modal-item-input">
						<view class="fixstyle">
							<timeSelector showType="dateToTime" @btnConfirm="endTime">
								<text class="" style="color: rgb(192, 196, 204);">
									{{this.endtime==''?'请选择结束时间':this.endtime}}
								</text>
								<!-- <text class="cuIcon cuIcon-unfold"></text> -->
							</timeSelector>
						</view>
					</view>
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
	import timeSelector from '@/components/wing-time-selector/wing-time-selector.vue'
	import smartbhapi from "../../api/smartbh.js"
	import dict from "../../pages/component/dict/dict.vue"
	import api from '@/api/api.js'
	export default {
		name: 'Testkykz',
		components: {
			timeSelector,
			dict
		},
		data() {
			return {
				show: false,
				cblevelname: [
					"合格",
					"不合格",
				],
				sbtype: '12',

				// cblevel:true,
				// zzjg:false,
				typename: [],
				deviceNames: [],
				devictype: [],
				deviceName: '',
				typenames: '',
				// shebeiNo:'',/
				choosekey: '',
				choosevalue: '',
				index: -1,
				pdresult: '',
				listData: [],
				listdatavalue: [],
				pageNo: 1,
				cbindex: -1,
				orgcode: null,
				sbNumber: null,
				begintime: '',
				endtime: '',
				pdjg: null,
				syjid: '',
				sylxvalue: null,
			}
		},
		onLoad() {
			this.listdata();
		},
		onReachBottom() {
			uni.showNavigationBarLoading()
			this.pageNo++
			this.listdata()
			uni.hideNavigationBarLoading()
		},
		created() {
			// console.log(this.sbtype)
			// this.queryMyDepartTree()
			this.deviceType()
			// this.Typetest()
		},
		mounted() {
			// this.getdevicelist()
		},
		methods: {
			//设备选择
			// Changedevice(e) {
			// 	// console.log(e)
			// 	this.devicename = this.devicenames[e.detail.value]
			// 	this.shebeiNo = this.devictype[e.detail.value].sbjno
			// },
			// getdevicelist() {
			// 	smartbhapi.deviceList({
			// 		params:{
			// 			sbtypes: this.sbtype
			// 		}
			// 	}).then(res => {
			// 		console.log(res)
			// 		this.devictype = res.data.result
			// 		this.devicenames = []
			// 		this.devictype.forEach(item => {
			// 			this.devicenames.push(item.sbname)
			// 		})
			// 	})
			// },
			deviceType() {
				api.deviceType({
					params: {
						sys_depart_orgcode: this.orgcode,
						sbtypes: this.sbtype
					}
				}).then(res => {
					console.log(res.data.result)
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
			// Pickertype() {
			// 	this.index = e.detail.value
			// 	this.choosekey = 2
			// 	this.choosevalue = this.devictype[this.index]
			// 	this.deviceName = this.deviceNames[this.index]
			// 	// console.log(this.deviceName,'sss')
			// 	this.sbNumber = this.choosevalue
			// 	console.log(this.sbNumber, 'jjjjjjjjjjjj');

			// 	this.begintime = ''
			// 	this.endtime = ''
			// },
			// 设备名称
			PickerChange(e) {
				console.log(e)
				// this.cbindex = -1
				this.index = e.detail.value
				this.choosekey = 2
				this.choosevalue = this.devictype[this.index]
				this.deviceName = this.deviceNames[this.index]
				// console.log(this.deviceName,'sss')
				this.sbNumber = this.choosevalue
				console.log(this.sbNumber, 'jjjjjjjjjjjj');

				this.begintime = ''
				this.endtime = ''
				// this.Choose()
			},
			// Typetest() {
			// 	api.deviceType({
			// 		params: {
			// 			sys_depart_orgcode: this.orgcode,
			// 			sbtypes: this.sbtype
			// 		}
			// 	}).then(res => {
			// 		console.log(res.data.result)
			// 		let devicelist = res.data.result
			// 		this.deviceNames = []
			// 		this.devictype = []
			// 		devicelist.forEach(e => {
			// 			this.deviceNames.push(e.sbname)
			// 			this.devictype.push(e.sbjno)
			// 		})
			// 	}).catch(e => {
			// 		console.log("请求错误", e)
			// 	})
			// },
			// 判定结果
			Cblevel(e) {
				// console.log(e)
				if (e.detail.value == 0) {
					e.detail.value = '合格'
					this.pdresult = e.detail.value
				} else if (e.detail.value == 1) {
					e.detail.value = '不合格'
					this.pdresult = e.detail.value
				}
				// this.cbindex = e.detail.value
				this.choosekey = 3
				this.choosevalue = e.detail.value
				// this.Choose()
			},
			// 开始时间
			beginTime(e) {
				// console.log('确定时间为：', e);
				this.begintime = e.key
				this.choosevalue = e.key
				this.choosekey = 4
				// this.Choose()
			},
			// 结束时间
			endTime(e) {
				// console.log('确定时间为：', e);
				this.endtime = e.key
				this.choosevalue = e.key
				this.choosekey = 5
				// this.Choose()
			},
			Choose(choosekey, choosevalue) {
				// console.log(this.choosevalue)
				// 设备名称
				if (this.choosekey == 2) {
					this.sbNumber = this.choosevalue

				}
				// 判定结果
				if (this.choosekey == 3) {
					this.pdjg = this.choosevalue
				}
				// 开始时间
				if (this.choosekey == 4) {
					this.begintime = this.choosevalue
				}
				// 结束时间
				if (this.choosekey == 5) {
					this.endtime = this.choosevalue
				}
				this.listdata()

				this.show = false
			},
			choose(e) {
				// console.log(e, 'ddddddddd')
				this.sylxvalue = e
			},
			listdata() {
				this.$http.get("/syj/tSyjzb/list3", {
					params: {
						column: 'id',
						order: 'desc',
						sylx: this.sylxvalue,
						pageNo: this.pageNo,
						PageSize: 10,
						sys_depart_orgcode: this.orgcode,
						sbbh: this.sbNumber,
						pdjg: this.pdjg,
						syrq_begin: this.begintime,
						syrq_end: this.endtime,
					}
				}).then(res => {
					// this.listdatavalue = res.data.result.records
					// console.log(this.listdatavalue,'ddddddddddd')
					if (res.data.result.records.length == 0) {
						uni.showToast({
							title: '别下拉啦，没数据啦',
							icon: 'none'
						})
					}
					if (this.pageNo != 1) {
						this.listdatavalue = this.listdatavalue.concat(res.data.result.records)
					} else {
						this.listdatavalue = res.data.result.records
					}
				})
			},
			screen() {
				this.pdresult = ''
				this.begintime = '',
					this.endtime = ''
				this.deviceName = ''
				this.show = true
			},
			confirm() {
				this.listdata()
				this.show = false
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

	.card {
		/* width: 100%; */
		background-color: white;
		margin: 10px 5px 0px 5px;
		border-radius: 5px;
		color: #909399;
	}

	.card uni-view {
		padding: 5px 0px 0px 10px;
		font-size: .9rem;
		font-weight: bold;
		color: #000;
	}

	.card span {
		color: #606266;
		font-size: .9rem;
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

				// &-input {}
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

	.standard {
		width: 197px;
		height: 145px;
		background-image: url(../../static/shiti/hg.png);
		background-size: 44% 62%;
		background-repeat: no-repeat;
		position: absolute;
		right: -105px;
		margin-top: -5px;
		-webkit-transform: rotate(30deg);
		transform: rotate(0deg);
	}

	.standards {
		width: 197px;
		height: 145px;
		background-image: url(../../static/shiti/bhg.png);
		background-size: 44% 62%;
		background-repeat: no-repeat;
		position: absolute;
		right: -105px;
		margin-top: -5px;
		-webkit-transform: rotate(30deg);
		transform: rotate(0deg);
	}

	.bh {
		width: 82px;
		height: 82px;
		background-image: url(../../static/pour/bohui.png);
		background-size: 100% 100%;
		background-repeat: no-repeat;
		position: absolute;
		right: 26px;
		margin-top: 175px;
		-webkit-transform: rotate(30deg);
		transform: rotate(0deg);
	}

	.sh {
		width: 82px;
		height: 82px;
		background-image: url(../../static/pour/yishenhe.png);
		background-size: 100% 100%;
		background-repeat: no-repeat;
		position: absolute;
		right: 26px;
		margin-top: 175px;
		-webkit-transform: rotate(30deg);
		transform: rotate(0deg);
	}

	.ycz {
		width: 82px;
		height: 82px;
		background-image: url(../../static/pour/yichuzhi.png);
		background-size: 100% 100%;
		background-repeat: no-repeat;
		position: absolute;
		right: 26px;
		margin-top: 175px;
		-webkit-transform: rotate(30deg);
		transform: rotate(0deg);
	}

	.wcz {
		width: 82px;
		height: 82px;
		background-image: url(../../static/pour/weichuzhi.png);
		background-size: 100% 100%;
		background-repeat: no-repeat;
		position: absolute;
		right: 26px;
		margin-top: 175px;
		-webkit-transform: rotate(30deg);
		transform: rotate(0deg);
	}
</style>
