<template>
	<view id="home">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">混凝土发车单</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>


		<view>
			<view class="section" v-for="(item,index) in shitilist" :key="index">
				<view class="card" @click="shwoTab(item)">
					<!-- <view class="card" style="text-align: center;"> -->


					<view class="section-text">
						<!-- <view v-if="item.isjiesuo==0" class="zt"></view>
							<view v-if="item.isjiesuo==1 && item.sfhg==null" class="wsh"></view>
							<view v-if="item.isjiesuo==1 && item.sfhg=='不合格'" class="tc"></view>
							<view v-if="item.isjiesuo==1 && item.sfhg=='合格'" class="djs"></view>
							<view v-if="item.isjiesuo==2" class="ywc"></view> -->
						<!-- <view class="">GPS号:<span>{{item.sbbh!==null?item.sbbh:'暂无数据'}}</span></view> -->
						<view v-has="'car:time'" class="">
							票据号:<span>{{item.erpid !==0?formatErp(item.dattim,item.erpid):'暂无数据'}}</span>
						</view>
						<view class="">浇筑部位:<span>{{item.conspos !==null?item.conspos:'暂无数据'}}</span>
						</view>
						<view class="">施工地点:<span>{{item.projadr !==null?item.projadr:'暂无数据'}}</span>
						</view>
						<view class="">
							车号/驾驶员:<span>{{item.vehicle !==null?item.vehicle:'暂无数据'}}/{{item.driver !==null?item.driver:'暂无数据'}}</span>
						</view>
						<view class="">发车时间:<span>{{item.begtim !==null?item.begtim:''}}</span></view>
						<!-- <view class="">施工配合比:<span>{{item.recipe !==null?item.recipe:'暂无数据'}}</span></view> -->
						<view class="">运输方量:<span>{{item.prodmete !==null?item.prodmete+'m³':'暂无数据'}}</span>
						</view>
						<view v-if="item.yunju != null" class="">
							运距:<span>{{item.yunju !==null?item.yunju:'暂无数据'}}</span>
						</view>
						<view class=" ">签收:<span
								:style="item.status==1?'color:green;font-weight:bold;font-family:cursive;':'color:red;font-weight:bold;font-family:cursive;'">
								{{item.status !==null?item.status_dictText+' ':'暂无数据'}}
								{{item.qianshoutime !==null?item.qianshoutime:''}}</span></view>
						<view class="" v-if="item.zq != null">账期:<span>{{item.zq}}</span></view>
						<view class="" v-if="item.dgzmsj != null">
							到工作面/卸料/卸完:<span>{{item.dgzmsj.slice(11, 16)}}{{item.xlsj !==null?'/'+item.xlsj.slice(11, 16):''}}{{item.xwsj !==null?'/'+item.xwsj.slice(11, 16):''}}</span>
						</view>

						<view catchtouchmove class="btn" @tap.stop="testFn($event)">
							<u-button type="primary" class="btnclass" v-if="item.status == 0"
								@click="determine(item.id)" text="签收"></u-button>
							<u-button v-has="'car:time'" type="primary" class="btnclass" catchtouchmove
								@click="bashow1(item)" text="时间填报"></u-button>
						</view>

						<!-- <view class="">所属标段:<span>{{item.userdepartid !==null?item.userdepartid:'暂无数据'}}</span>
				</view> -->



					</view>


				</view>
			</view>

		</view>

		<view v-show="shows">
			<view class="card" style="text-align: center;margin: 10px 0;">
				<u-button loading loadingText="数据正在加载中"></u-button>
			</view>
		</view>

		<!-- <view class="">
			<view class="increase-img" @click="increase" v-has="'peihe:add'"></view>
		</view> -->
		<!-- 查询设备 -->

		<u-modal :title="'信息填报'" :show="bashow" @confirm="confirmtime" showCancelButton @cancel="infocancel">
			<view class="modal">
				<view class="screen-modal-item-input" style="display: flex">
					<span class="textfont">合同:</span>
					<u--input placeholder="请输入合同编号" v-model="taiz"></u--input>
				</view>
				<view class="screen-modal-item-input" @click="dateshow1(0)" style="display: flex;margin-top: 30upx;">
					<span class="textfont">账期:</span>
					<u--input placeholder="请选择账期" border="surround" v-model="zq" disabled suffixIcon="arrow-down">
					</u--input>
				</view>
				<view class="screen-modal-item-input" @click="dateshow1(3)" style="margin-top: 30upx;display: flex">
					<span class="textfont">到达:</span>
					<u--input placeholder="请选择到工作面时间" border="surround" v-model="dgzmsj1" disabled
						suffixIcon="arrow-down"></u--input>
				</view>
				<view class="screen-modal-item-input" @click="dateshow1(1)" style="margin-top: 30upx;display: flex">
					<span class="textfont">卸料:</span>
					<u--input placeholder="请选择卸料时间" border="surround" v-model="xlsj1" disabled suffixIcon="arrow-down">
					</u--input>
				</view>
				<view class="screen-modal-item-input" @click="dateshow1(2)" style="margin-top: 30upx;display: flex">
					<span class="textfont">卸完:</span>
					<u--input placeholder="请选择卸完时间" border="surround" v-model="xwsj1" disabled suffixIcon="arrow-down">
					</u--input>
				</view>

				<mx-date-picker :show="dateshow" :type="type" :value="value1" :show-tips="true" :show-seconds="false"
					@confirm="confirmdate" @cancel="confirmdate" />

				<!-- <uni-grid :column="5" @change="change">
						<uni-grid-item style="width: 20%;" v-for="(item,index) in huajiaNews" :index="index" :key='index'>
							<view class="grid-item-box" :style="{'background-color': item.bgindex==0?'#FFF':'#3c9cff' }">
								<text class="text">{{ item.huojianame }}</text>
							</view>
						</uni-grid-item>
					</uni-grid> -->
			</view>
		</u-modal>


		<view class="screen" v-if="show" @confirm="confirm">
			<view class="screen-modal">
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">车牌号：</view>
					<u--input placeholder="请输入车牌号" v-model="scvehicle"></u--input>
					<!-- <picker @change="PickerChange" :range="deviceNames">
						<u--input placeholder="请选择设备名称" border="surround" v-model="deviceName" disabled
							suffixIcon="arrow-down"></u--input>
					</picker> -->
				</view>
				<view class="screen-modal-item" v-has="'car:time'">
					<view class="screen-modal-item-name">是否进行时间填报：</view>
					<view class="screen-modal-item-input">
						<dict dictCode="tbtime" title="请选择" @choose="durationUnit">
						</dict>
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
	import timeSelector from '@/components/wing-time-selector/wing-time-selector.vue';
	// import Byspublic from '../../component/Byspublic/Byspublic.vue'
	import api from '@/api/apis.js'
	import nowtime from '../../../common/util/nowtime.js'
	import dict from '@/pages/component/dict/dict.vue'
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	import {
		ACCESS_TOKEN,
		USER_NAME,
		USER_INFO,
		SYS_BUTTON_AUTH
	} from "@/common/util/constants"
	import {
		mapActions
	} from "vuex"

	export default {
		name: 'arrivaledlist',
		components: {
			MxDatePicker,
			dict,
			timeSelector,
			// Byspublic
		},
		data() {
			return {
				tbStatus: null,
				scvehicle: '',
				tbid: '',
				type1: '0', // 0账期，1卸料时间，2卸完时间，3到工作面时间
				type: 'time',
				value1: '',
				dateshow: false,
				xlsj1: '',
				xwsj1: '',
				dgzmsj1: '',
				xlsj: '',
				xwsj: '',
				zq: '',
				dgzmsj: '',
				taiz: '',
				bashow: false,
				showqr: false,
				shows: true,
				fails: false,
				shitilist: '',
				listdatavalue: '',
				deviceNames: [],
				devictype: [],
				deviceName: '',
				choosekey: '',
				choosevalue: '',
				sbNumber: null,
				orgcode: '',
				sbtype: '55',
				index: -1,
				show: false,
				begintime: '',
				endtime: '',
				pageNo: 1,
				totaldata: {}
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
			this.deviceType()
		},

		methods: {
			dateshow1(a) {
				this.type1 = a
				if (a == 0) {
					this.type = 'date'
				} else {
					this.type = 'time'
				}

				this.dateshow = true
			},
			bashow1(car) {
				this.tbid = car.id
				this.taiz = car.taiz
				this.dgzmsj1 = car.dgzmsj != null && car.dgzmsj.length > 16 ? car.dgzmsj.slice(11, 16) : ''
				this.xlsj1 = car.xlsj != null && car.xlsj.length > 16 ? car.xlsj.slice(11, 16) : ''
				this.xwsj1 = car.xwsj != null && car.xwsj.length > 16 ? car.xwsj.slice(11, 16) : ''
				this.zq = car.zq != null ? car.zq : car.begtim.slice(0, 10)
				this.bashow = true


			},
			testFn(e) {
	            e.preventDefault();
			},
			formatErp(fctime, erpid) {
				let date = fctime.replace(/\D/g, "");
				let newDate = date.replace(/^(\d{4})(\d{2})(\d{2})$/, '$1 $2$3');
				return newDate + ' ' + erpid

			},
			confirmdate(e) {
				this.dateshow = false
				if (this.type1 == 1) {
					this.xlsj = e ? this.zq + ' ' + e.value + ':00' : ''
					this.xlsj1 = e ? e.value : ''
				} else if (this.type1 == 2) {
					this.xwsj = e ? this.zq + ' ' + e.value + ':00' : ''
					this.xwsj1 = e ? e.value : ''
				} else if (this.type1 == 3) {
					this.dgzmsj = e ? this.zq + ' ' + e.value + ':00' : ''
					this.dgzmsj1 = e ? e.value : ''
				} else {
					this.zq = e ? e.value : ''
				}


			},
			shwoTab(item) {
				let time = item.qianshoutime;
				console.log(item, 'item');
				let params = encodeURIComponent(JSON.stringify(item));
				uni.navigateTo({
					//保留当前页面，跳转到应用内的某个页面
					url: "/pages/CarDispatch/CarInfo/CarTrack1?params=" + params,
				});
			},
			infocancel() {
				this.bashow = false
			},
			cancelqr() {
				this.showqr = false
			},

			determine(carid) {
				let params = {}
				params = {
					id: carid,
					status: 1,
					status1: 2,
					qianshoutime: nowtime.date() + ' ' + nowtime.time()
				}
				this.$http.post(`/car/schedulingCar/edit`, params).then(res => {
					if (res.data.success) {
						this.textlist()
						uni.showToast({
							title: '签收成功'
						})

					} else {
						this.$tip.toast('签收失败')
					}

				})
			},
			confirmtime() {
				let params = {}
				params = {
					id: this.tbid,
					xlsj: this.xlsj,
					xwsj: this.xwsj,
					zq: this.zq,
					dgzmsj: this.dgzmsj,
					taiz: this.taiz,
					tbStatus: 2

				}
				this.$http.post(`/car/schedulingCar/edit`, params).then(res => {
					if (res.data.success) {
						this.textlist()
						uni.showToast({
							title: '填报成功'
						})

					} else {
						this.$tip.toast('填报失败')
					}

				})
				this.bashow = false
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
						this.deviceNames.push(e.sbjno)
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

				if (this.tbStatus == 0) {
					this.tbStatus = null
				}
				this.$http.get(`/car/schedulingCar/listyafapp`, {
					// this.$http.get(`/car/schedulingCar/list`, {
					params: {
						column: 'id',
						order: 'desc',
						isjiesuo: 0,
						tbStatus: this.tbStatus,
						vehicle: '*' + this.scvehicle + '*',
						pageNo: this.pageNo,
						pageSize: 10,
						sys_depart_orgcode: uni.getStorageSync(USER_INFO).orgCode,


						// sbbh: this.sbNumber,
						// shebeibianhao: this.sbNumber,
					}
				}).then(res => {
					if (res.data.success) {
						setTimeout(() => {
							this.shows = false
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

			// increase() {
			// 	uni.navigateTo({
			// 		url: '/pages/Rawmaterial/depart/departadd'
			// 	})
			// },
			screen() {
				this.deviceName = ''
				this.show = true
			},
			//填报状态
			durationUnit(val) {
				this.tbStatus = val
			},
			handleOk() {
				this.pageNo = 1
				this.listdata = []
				this.textlist()
				this.show = false
			},
			confirm() {
				this.show = false

			},
		}
	}
</script>

<style lang="scss" scoped>
	#home {
		height: auto;
		background-color: #F2F5FE;
		width: 100%;
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

	.textfont {
		margin-top: 5px;
		font-size: large
	}

	.btn {
		width: 690upx;
		margin: 0 auto;
		margin-top: 30upx;
		display: flex;

		.btnclass {
			width: 35%;
		}
	}

	// .increase-img {
	// 	width: 24%;
	// 	height: 87px;
	// 	background-image: url(../../../static/pour/add.png);
	// 	background-size: 100% 100%;
	// 	position: fixed;
	// 	bottom: 176px;
	// 	right: -18px;
	// }
</style>
