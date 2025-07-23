<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">标养室</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<view v-if="listdatavalue.length != 0">
			<view class="card" v-for="(item,index) in listdatavalue" :key="index">
				<!-- 路面跳转 -->
				<!-- <navigator :url="'/pages/anquan/zhyongdian/zhydhistory?imei='+item.imei"> -->
				<view>设备名称:<span>{{item.shebeino_dictText !==null?item.shebeino_dictText:'暂无数据'}}</span></view>
				<view>上传时间:<span>{{item.gatherdate !==null?item.gatherdate:'暂无数据'}}</span></view>
				<view>温度:<span>{{item.temperature !==null?item.temperature:'暂无数据'}}&#8451</span></view>
				<view>温度超标状态:<span
						:style="item.temperature >= 18 || item.temperature <= 22?'color:green;font-weight:bold;font-family:cursive':'color:red;font-weight:bold;font-family:cursive'">{{item.temperature >= 18 || item.temperature <= 22?'正常':'异常'}}</span>
				</view>
				<view>湿度:<span>{{item.humidity}}%</span></view>
				<view>湿度超标状态:<span
						:style="item.humidity >= 95?'color:green;font-weight:bold;font-family:cursive':'color:red;font-weight:bold;font-family:cursive'">{{item.humidity >= 95?'正常':'异常'}}</span>
				</view>
				<!-- </navigator> -->
			</view>
		</view>
		<view v-else>
			<view class="card" style="text-align: center;">
				<u-button loading loadingText="数据正在加载中"></u-button>
			</view>
		</view>
		<view class="screen" v-if="show"  @confirm="confirm">
			<view class="screen-modal">
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">设备名称：</view>
					<view class="screen-modal-item-input">
						<!-- <picker @change="ChangeTestType" :range="testType"> -->
						<view class="fixstyle">
							<picker @change="PickerChange" :value="index" :range="deviceName">
								<text class="">
									{{index>-1?deviceName[index]:'设备名称'}}
								</text>
								<text class="cuIcon cuIcon-unfold"></text>
							</picker>
						</view>
						<!-- </picker> -->
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">温度超标状态：</view>
					<view class="screen-modal-item-input">
						<view class="fixstyle">
							<picker @change="Cblevel" :value="index" :range="cblevelname">
								<text class="">
									<!-- {{pdresult== ''?"温度超标状态":pdresult}} -->
									{{cbindex>-1?cblevelname[cbindex]:'温度超标状态'}}
								</text>
								<text class="cuIcon cuIcon-unfold"></text>
							</picker>
						</view>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">湿度超标状态：</view>
					<view class="screen-modal-item-input">
						<view class="fixstyle">
						<picker @change="hCblevel" :value="index" :range="hcblevelname">
							<text class="">
								<!-- {{pdresult== ''?"湿度超标状态":pdresult}} -->
								{{hcbindex>-1?cblevelname[hcbindex]:'湿度超标状态'}}
							</text>
							<text class="cuIcon cuIcon-unfold"></text>
						</picker>
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
	import timeSelector from '@/components/wing-time-selector/wing-time-selector.vue';
	import api from '@/api/api.js'
	export default {
		name: 'Testdetection',
		components: {
				timeSelector
		},
		data() {
			return {
				cblevelname: [
					"未检测",
					"正常",
					"异常",
				],
				hcblevelname: [
					"未检测",
					"正常",
					"异常",
				],
				deviceName: [],
				devictype: [],
				choosekey: '',
				choosevalue: '',
				index: -1,
				show: false,
				// pdresult: '',
				hcbindex: -1,
				cbindex: -1,
				sbtype: '11',
				cblevel: true,
				listData: [],
				jblevel: true,
				listdatavalue: [],
				pageNo: 1,
				temstatus: null,
				humstatus: null,
				shNumber: null
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
		},
		methods: {
			deviceType() {
				api.deviceType({
					params: {
						sys_depart_orgcode: this.orgcode,
						sbtypes: this.sbtype
					}
				}).then(res => {
					console.log(res.data.result)
					let devicelist = res.data.result
					this.deviceName = []
					this.devictype = []
					devicelist.forEach(e => {
						this.deviceName.push(e.sbname)
						this.devictype.push(e.sbjno)
					})
				}).catch(e => {
					console.log("请求错误", e)
				})

			},
			// 设备名称
			PickerChange(e) {
				console.log(e)
				// this.cbindex = -1
				this.index = e.detail.value
				this.imei = this.devictype[this.index]
				this.choosekey = 2
				this.choosevalue = this.devictype[this.index]
				console.log(this.choosevalue, 'jjjjjjjjjjjj');
				this.begintime = ''
				this.endtime = ''
				// this.Choose()
			},
			// 判定结果
			Cblevel(e) {
				this.cbindex = e.detail.value
				this.choosekey = 3
				this.choosevalue = e.detail.value
			},
			hCblevel(e) {
				this.hcbindex = e.detail.value
				this.choosekey = 4
				this.choosevalue = e.detail.value
			},
			Choose(choosekey, choosevalue) {
				console.log(this.choosevalue)
				// 设备名称
				if (this.choosekey == 2) {
					this.sbNumber = this.choosevalue
					this.begintime = ''
					this.endtime = ''
					// this.listdata()
				}
				// 温度超标状态
				if (this.choosekey == 3) {
					this.temstatus = this.choosevalue
				}
				// // 湿度超标状态
				if (this.choosekey == 4) {
					this.humstatus = this.choosevalue
				}
				this.listdata()
				this.show = false
			},
			listdata() {
				this.$http.get("/bys/bysReal/list", {
					params: {
						column: 'id',
						order: 'desc',
						pageNo: this.pageNo,
						PageSize: 10,
						shebeino: this.sbNumber,
						temstatus: this.temstatus,
						humstatus: this.humstatus,
					}
				}).then(res => {
					// this.listdatavalue = res.data.result.records
					if (res.data.result.records.length == 0) {
						uni.showToast({
							title: '没有更多数据了',
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
				this.show = true
			},
			confirm() {
				this.show = false
			},
		}
	}
</script>

<style lang="scss" scoped>
	.wrap{
		width:100%;
		height:auto;
		background-color:#F2F5FE;
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
</style>
