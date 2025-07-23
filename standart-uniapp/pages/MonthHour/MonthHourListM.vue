<template>
	<view id="home">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">考勤统计</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>


			<!-- 视图部分 -->
			<view class="attendance-container">
			  <view class="attendance-card" v-for="(item,index) in shitilist" :key="index">
				<view class="info-panel" @click="shwoTab(item)">
				  <!-- 头部信息 -->
				  <view class="info-header">
					<text class="name-style">{{item.xingming || '未登记'}}</text>
					<text class="position-style">{{ item.zhiwu || item.banzu || '未设职务' }}</text>
				  </view>

				  <!-- 分割线 -->
				  <view class="divider-line"></view>

				  <!-- 主体信息 -->
				  <view class="info-body">
					<view class="info-row">
					  <text class="info-label">考核月度</text>
					  <text class="info-value accent-blue">{{item.note || '未设置周期'}}</text>
					</view>
					<view class="stats-row">
					  <text class="stat-item">出勤 {{item.shifoubixudaogang || 0}} 天</text>
					  <text class="divider">|</text>
					  <text class="stat-item highlight-value">工时 {{item.jintuichangzhuangtai || 0}}h</text>
					</view>
				  </view>
				</view>
			  </view>
			</view>

		<view v-show="shows">
			<view class="card" style="text-align: center;margin: 10px 0;">
				<u-button loading loadingText="数据正在加载中"></u-button>
			</view>
		</view>




		<view class="screen" v-if="show">
		  <view class="screen-modal">
		    <!-- 姓名筛选 -->
		    <view class="screen-modal-item">
		      <view class="screen-modal-item-name">姓名：</view>
		      <u--input 
		        placeholder="请输入姓名" 
		        v-model="xingming"
		      ></u--input>
		    </view>
			
			<!-- 月度筛选 -->
			<view class="screen-modal-item">
			  <view class="screen-modal-item-name">月度：</view>
			  <u--input 
			    placeholder="请输入月度(例如2025-01)" 
			    v-model="note"
			  ></u--input>
			</view>
		
		    <!-- 人员类型筛选 -->
		    <view class="screen-modal-item">
		      <view class="screen-modal-item-name">人员类型：</view>
		      <view class="screen-modal-item-input">
		        <dict 
		          dictCode="renyuantype"
		          title="请选择类型" 
		          @choose="typeUnit"
		        ></dict>
		      </view>
		    </view>
		
		    <!-- 操作按钮 -->
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
	import nowtime from '@/common/util/nowtime.js'
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
				xingming: '',
				renyuantype: '',
				biaoduan: '' , 
				note:'',
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
			  // 人员类型选择
			  typeUnit(val) {
			    this.renyuantype = val
				console.log('Received:', val); // 验证参数是否传递
			  },
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
			  if (!item || typeof item !== 'object') return;
			    
			    // 类型判断前置（使用原始数据）
			    let targetUrl = item.renyuantype === '管理人员' 
			      ? "/pages/personManage/StaffInfo" 
			      : "/pages/personManage/StaffInfo1";
			  
			    // 参数编码处理
			    const enhancedEncode = str => str.replace(/[%?#&]/g, match => 
			      `%${match.charCodeAt(0).toString(16).toUpperCase()}`);
			    const serializedData = JSON.stringify(item);
			    const encodedParams = enhancedEncode(encodeURIComponent(serializedData));
			  
			    // 标准参数传递
			    uni.navigateTo({
			      url: `${targetUrl}?params=${encodedParams}` // ✅ 统一参数命名
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
		

				this.textlist()
				this.show = false
			},
			textlist() {

				if (this.tbStatus == 0) {
					this.tbStatus = null
				}
					this.$http.get(`/staffbase/staffBaseWork/MothonHours`, {
					params: {
						sys_depart_orgcode: uni.getStorageSync(USER_INFO).orgCode,
						column: 'id',
						order: 'desc',
						isjiesuo: 0,
						xingming: this.xingming,
						renyuantype: this.renyuantype,
						note:this.note,
						pageNo: this.pageNo,
						pageSize: 10,
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
					background-image: url(../../static/shiti/five.png);
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
/* 容器布局 */
.attendance-container {
  display: grid;
  gap: 12px;
  padding: 16px;
  background: #f8f9fa;
}

/* 卡片主体 */
.attendance-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
  transition: transform 0.2s ease;
}

/* 信息面板 */
.info-panel {
  padding: 20px;
}

/* 头部信息 */
.info-header {
  display: flex;
  align-items: baseline;
  margin-bottom: 16px;
}

.name-style {
  font-size: 18px;
  font-weight: 600;
  color: #2d3748;
  margin-right: 12px;
}

.position-style {
  font-size: 14px;
  color: #4a5568;
  position: relative;
  padding-left: 12px;
}

.position-style::before {
  content: "·";
  position: absolute;
  left: 0;
  color: #4299e1;
  font-weight: 700;
}

/* 分割线 */
.divider-line {
  height: 1px;
  background: #edf2f7;
  margin: 16px 0;
}

/* 主体信息 */
.info-body {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-label {
  font-size: 14px;
  color: #718096;
}

.info-value {
  font-size: 14px;
  color: #2d3748;
  font-weight: 500;
}

/* 统计行 */
.stats-row {
  display: flex;
  justify-content: space-around;
  align-items: center;
  background: #f7fafc;
  border-radius: 8px;
  padding: 12px;
  margin-top: 8px;
}

.stat-item {
  font-size: 15px;
  color: #2d3748;
}

.highlight-value {
  color: #2b6cb0;
  font-weight: 600;
}

.divider {
  color: #cbd5e0;
  font-weight: 300;
}

.accent-blue {
  color: #2b6cb0;
}

/* 移动端适配 */
@media (max-width: 480px) {
  .attendance-container {
    padding: 12px;
  }
  
  .info-panel {
    padding: 16px;
  }
  
  .name-style {
    font-size: 16px;
  }
  
  .position-style {
    font-size: 13px;
  }
  
  .stats-row {
    padding: 10px;
  }
  
  .stat-item {
    font-size: 14px;
  }
}
</style>
