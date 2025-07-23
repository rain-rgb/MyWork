<template>
	<!-- 安全检查 -->
	<view id="home">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">安全检查</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<!-- <view class="top-two">
			<view class="left" @click="change(3)">
				<view class="left-text">
					<p class="red">{{totaldata.arrivesum || 0}}</p>
					到场车次
				</view>
			</view>
			<view class="left" @click="change(1)">
				<view class="left-text">
					<p class="orange">{{totaldata.hege || 0}}</p>
					合格数
				</view>
			</view>
			<view class="left" @click="change(2)">
				<view class="left-text">
					<p class="green">{{totaldata.finish || 0}}</p>
					已完成
				</view>
			</view>
		</view> -->
		<!-- <view class="top-two">
			<view class="left" @click="change(0)">
				<view class="left-text">
					<p class="purple">{{totaldata.noarrive || 0}}</p>
					在途车次
				</view>
			</view>
			<view class="left" @click="change(4)">
				<view class="left-text">
					<p class="blue">{{totaldata.failed || 0}}</p>
					退场数
				</view>
			</view>
			<view class="left" @click="change(5)">
				<view class="left-text">
					<p class="black">{{totaldata.arrivednosh || 0}}</p>
					总数量
				</view>
			</view>
		</view> -->
		<view class="section" v-for="(item,index) in shitilist" :key="index" @tap.stop="handelclick(item)">
			<!-- <navigator :url="'/pages/Rawmaterial/depart/departdetails?item='+JSON.stringify(item)"> -->
			<view class="section-text">
				<!-- <view v-if="item.isjiesuo==0" class="zt"></view>
				<view v-if="item.isjiesuo==1 && item.sfhg==null" class="wsh"></view>
				<view v-if="item.isjiesuo==1 && item.sfhg=='不合格'" class="tc"></view>
				<view v-if="item.isjiesuo==1 && item.sfhg=='合格'" class="djs"></view>
				<view v-if="item.isjiesuo==2" class="ywc"></view> -->
				<view class="">风险源点:<span>{{item.manageLocation!==null?item.manageLocation:'暂无数据'}}</span></view>
				<view class="">
					责任部门:<span>{{item.departname!==null?item.departname:'暂无数据'}}</span>
				</view>
				<view class="">问题级别:<span :style="
											item.problemType == '⼀般问题'
												? 'color:#e7e700;font-weight:bold;font-family:cursive;'
												: item.problemType == '⼀般隐患'
												? 'color:orange;font-weight:bold;font-family:cursive;'
												: 'color:red;font-weight:bold;font-family:cursive;'"
											>{{ item.problemType !== null ? item.problemType : "暂无数据" }}</span>
				</view>
				<view class="">问题描述:<span>{{item.problems !==null?item.problems:'暂无数据'}}</span>
				</view>
				<view class="">隐患类别:<span>{{item.yhlb !==null?item.yhlb:'暂无数据'}}</span>
				</view>
				<view class="">检查人:<span>{{item.checkPeople !==null?item.checkPeople:'暂无数据'}}</span>
				</view>
				<view class="">检查年月日:<span>{{item.checkTime !==null?item.checkTime:'暂无数据'}}</span></view>
				<view class="">整改状态:
					<!-- 状态0；未整改；20已整改；30已审核； 40已销号 -->
					<span v-if="item.handlestatus === 0">{{ `未整改` }}</span>
					<span v-if="item.handlestatus === 20">{{ `已整改` }}</span>
					<span v-if="item.handlestatus === 30">{{ `已审核` }}</span>
					<span v-if="item.handlestatus === 40">{{ `已销号` }}</span>
					<!-- <span v-if="item.handlestatus === 0">{{item.problems !==null?`未整改`:'暂无数据'}}</span> -->
					<!-- <span v-else>{{item.problems !==null?`已整改`:'暂无数据'}}</span> -->
				</view>
				<view class="screen-modal-btn" @tap.stop="edit($event,item)">
					<u-button style="width:350px;border-radius: 10px;color: #fff;background-color: #387FFD;" text="整改" v-if="item.handlestatus === 0"
					></u-button>
					<u-button style="width:350px;border-radius: 10px;color: #fff;background-color: #387FFD;" text="审核" v-if="item.handlestatus === 20 && (item.problemType == `⼀般隐患` || item.problemType == `重⼤隐患`)"
					></u-button>
					<u-button style="width:350px;border-radius: 10px;color: #fff;background-color: #387FFD;" text="销号" v-if="item.handlestatus === 30 && item.problemType == `重⼤隐患`"
					></u-button>
				</view>
			</view>
			<!-- </navigator> -->
		</view>
		<view class="">
			<view class="increase-img" @click="increase()" v-has="'peihe:add'"></view>
		</view>
		<!-- 查询设备 -->
		<view class="screen" v-if="show" @confirm="confirm">
			<view class="screen-modal">
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">风险源点:</view>
					<view class="text-input">
						<u--input placeholder="请输入风险源点" border="surround" v-model="query.manageLocation"
							style="background-color:#F6F9FC;"></u--input>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">问题级别:</view>
					<view class="screen-picker" v-has="'car:time'">
						<view class="screen-modal-item-input">
							<dict dictCode="problem_type" title="请选择" @choose="durationUnit">
							</dict>
						</view>
					</view>
				</view>
				<view class="screen-modal-btn">
					<u-button text="取消" @click="confirm"></u-button>
					<u-button text="重置" @click="Reset"></u-button>
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
	import dict from '@/pages/component/dict/dict.vue'
	export default {
		name: 'DeliveryManagement',
		components: {
			dict,
			timeSelector,
			// Byspublic
		},
		data() {
			return {
				taskSelect: {},
				shitilist: [],
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
				totaldata: {},
				jiesuo: '',
				sh: '',
				urls: `/anquanfxgk/anquanFxaqjcInfo/list`,
				query:{
					manageLocation:"",
					problemType:"",
				}
			}
		},
		onLoad(options) {
			if (options.item) {
				this.taskSelect = JSON.parse(options.item)
				this.sbNumber = this.taskSelect.shrwd
			}
		},
		onShow() {
			this.pageNo = 1;
			this.getDatalist()
			// this.tabledata()
		},
		onReachBottom() {
			console.log(`2222222222222222222222233333`);
			
			uni.showNavigationBarLoading()
			this.pageNo++
			this.getDatalist()
			uni.hideNavigationBarLoading()
		},
		created() {
			this.deviceType()
		},

		methods: {
			//问题级别
			durationUnit(val) {
				this.query.problemType = val
			},
			deviceType() {
				let params = {
					status: 1,
					pageSize: 50
				}
				this.$http.get('/syshrwd/syshrwd/list', {
					params
				}).then(res => {
					if (res.data.code == 200) {
						let arr = []
						res.data.result.records.forEach(item => {
							arr.push(item.shrwd)
						})
						this.deviceNames = arr.slice(0);
						this.devictype = arr.slice(0);
					}
				})
			},
			// 发货单号
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
			change(e) {
				console.log("this.jiesuo", this.jiesuo)
				this.shitilist = []
				this.pageNo = 1
				this.urls = `/wbshebeidetail/wbshebeiDetail/list`
				// console.log(e, 'ffffffffffff')
				if (e == 0) {
					this.sh = ''
					if (this.jiesuo == 0) {
						this.jiesuo = ''
					} else {
						this.jiesuo = 0
					}
				} else if (e == 3) {
					this.sh = ''
					if (this.jiesuo == 1) {
						this.jiesuo = ''
					} else {
						this.urls = `/wbshebeidetail/wbshebeiDetail/list1`
						this.jiesuo = 1
					}
				} else if (e == 2) {
					this.sh = ''
					if (this.jiesuo == 2) {
						this.jiesuo = ''
					} else {
						this.jiesuo = 2
					}
				} else if (e == 4) {
					if (this.jiesuo == 1 && this.sh == '不合格') {
						this.jiesuo = ''
						this.sh = ''
					} else {
						this.jiesuo = 1
						this.sh = '不合格'
					}
				} else if (e == 1) {
					if (this.jiesuo == 1 && this.sh == '合格') {
						this.jiesuo = ''
						this.sh = ''
					} else {
						this.jiesuo = 1
						this.sh = '合格'
					}
				}
				this.getDatalist()
			},
			getDatalist() {
				console.log("this.jiesuoeed", this.jiesuo)
				this.$http.get(this.urls, {
					params: {
						column: 'id',
						order: 'desc',
						pageNo: this.pageNo,
						PageSize: 10,
						manageLocation: this.query.manageLocation,
						problemType: this.query.problemType,
						// isjiesuo: this.jiesuo,
						// sfhg: this.sh,
						// fctype: 1
						// shebeibianhao: this.sbNumber,
					}
				}).then(res => {
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
					console.log(this.shitilist.length ,`this.shitilist.length`);
					
				})
			},
			tabledata() {
				let params = {
					fctype: 1,
					shrwdh: this.sbNumber
				}
				this.$http.get(`/wbshebeidetail/wbshebeiDetail/list3`, {
					params
				}).then(res => {
					console.log(res.data.result, '标签统计')
					this.totaldata = res.data.result
				})
			},

			increase() {
				if (Object.keys(this.taskSelect).length > 0) {
					uni.navigateTo({
						url: '/pages/smartgb/SafeCheck/handle/AddList?taskSelect=' +JSON.stringify(this.taskSelect)
					})
				} else {
					uni.navigateTo({
						url: '/pages/smartgb/SafeCheck/handle/AddList'
					})
				}
			},			
			edit(e,item) {
				e.preventDefault();
				let title = item.handlestatus === 0 ? `整改` : item.handlestatus === 20 ? `审核` :item.handlestatus === 30 ? `销号` : "";
				uni.navigateTo({
					url: '/pages/smartgb/SafeCheck/handle/edit?item=' +JSON.stringify(item)+`&title=${title}`
				})
			},
			screen() {
				this.deviceName = ''
				this.show = true
			},
			handleOk() {
				this.getDatalist()
				this.show = false
			},

			Choose(choosekey, choosevalue) {
				// console.log(this.choosekey,this.choosevalue);
				// 发货单号
				// if (this.choosekey == 2) {
				// 	this.sbNumber = this.choosevalue
				// 	this.pageNo = 1
				// 	// console.log(this.sbNumber)
				// 	this.begintime = ''
				// 	this.endtime = ''
				// }

				this.getDatalist()
				this.show = false
			},
			confirm() {
				this.show = false
			},
			Reset(){
				this.query = {
					manageLocation: ``,
					problemType: ``,
				}
				this.getDatalist()
				this.show = false
			},
			handelclick(item){
				uni.navigateTo({
					url: '/pages/smartgb/SafeCheck/handle/edit?item=' +JSON.stringify(item)+`&title=详情`
				})
				console.log(`handelclick===========`);
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
		// border:1px solid #1785FF;
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

	.zt {
		width: 197px;
		height: 145px;
		background-image: url(../../../static/shiti/zt.png);
		background-size: 44% 62%;
		background-repeat: no-repeat;
		position: absolute;
		right: -98px;
		margin-top: -15px;
		-webkit-transform: rotate(30deg);
		transform: rotate(0deg);
	}

	.wsh {
		width: 197px;
		height: 145px;
		background-image: url(../../../static/shiti/dsh.png);
		background-size: 44% 62%;
		background-repeat: no-repeat;
		position: absolute;
		right: -98px;
		margin-top: -15px;
		-webkit-transform: rotate(30deg);
		transform: rotate(0deg);
	}

	.tc {
		width: 197px;
		height: 145px;
		background-image: url(../../../static/shiti/tc.png);
		background-size: 44% 62%;
		background-repeat: no-repeat;
		position: absolute;
		right: -98px;
		margin-top: -15px;
		-webkit-transform: rotate(30deg);
		transform: rotate(0deg);
	}

	.djs {
		width: 197px;
		height: 145px;
		background-image: url(../../../static/shiti/djs.png);
		background-size: 44% 62%;
		background-repeat: no-repeat;
		position: absolute;
		right: -98px;
		margin-top: -15px;
		-webkit-transform: rotate(30deg);
		transform: rotate(0deg);
	}

	.ywc {
		width: 197px;
		height: 145px;
		background-image: url(../../../static/shiti/ywc.png);
		background-size: 44% 62%;
		background-repeat: no-repeat;
		position: absolute;
		right: -98px;
		margin-top: -15px;
		-webkit-transform: rotate(30deg);
		transform: rotate(0deg);
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

	.increase-img {
		width: 24%;
		height: 87px;
		background-image: url(../../../static/pour/add.png);
		background-size: 100% 100%;
		position: fixed;
		bottom: 176px;
		right: -18px;
		color: #c9c924;
	}
</style>
