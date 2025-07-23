<template>
	<view id="home">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">人员门禁</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>


			   <view class="access-card-list">
			     <view 
			       class="access-card" 
			       v-for="(item,index) in shitilist" 
			       :key="index" 
			       @click="shwoTab(item)"
			     >
			       <view class="card-wrapper">
			         <!-- 左侧信息区 -->
			         <view class="info-section">
			           <view class="card-header">
			             <text class="device-label">{{item.shebeino_dictText || '未命名设备'}}</text>
			             <text class="group-info">{{item.departname || '未知班组'}}</text>
			           </view>
			   
			           <view class="detail-info">
			             <view class="info-line">
			               <text class="info-tag">操作人</text>
			               <text class="info-content">{{item.names || '未登记'}}</text>
			             </view>
			             <view class="info-line time-info">
			               <text class="info-tag">开门时间</text>
			               <text class="time-stamp">{{item.opentime || '无记录'}}</text>
			             </view>
			           </view>
			         </view>
			   
			         <!-- 右侧图片区 -->
			         <view class="image-section">
			           <view class="image-container">
			             <image 
			               class="square-img"
			               :src="item.pic || '/static/default-image.png'"
			               mode="aspectFill"
			               @error="handleImageError"
			               :lazy-load="true"
			             />
			             <text v-if="!item.pic" class="img-placeholder">无打卡照片</text>
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
		        v-model="names"
		      ></u--input>
		    </view>
		
		   <view class="screen-modal-item">
		     <view class="screen-modal-item-name">设备名称：</view>
		     <view class="screen-modal-item-input">
		       <u-button 
		         @click="devicePickerVisible = true"
		         :custom-style="{
		           width: '100%',
		           textAlign: 'left',
		           border: '1px solid #dcdfe6',
		           borderRadius: '4px',
		           padding: '8px 12px'
		         }"
		       >
		         {{ selectedDevice || '点击选择设备' }}
		         <text style="float:right;color:#909399">▼</text>
		       </u-button>
		       
		       <u-picker
		         v-if="deviceOptions.length > 0"
		         :show="devicePickerVisible"
		         :columns="[deviceOptions]"
		         keyName="label"
		         valueName="value"
		         @confirm="handleDeviceConfirm"
		         @cancel="devicePickerVisible = false"
		       />
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
				devicePickerVisible: false,
				deviceOptions: [],       // 设备选项列表
				selectedDevice: '',      // 选中设备名称
				shebeino_dictText: '' ,   // 实际存储设备ID
				names: '',
				shebeino_dictText: [],
				shebeino: [],
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
			this.deviceType(); // 初始化加载设备列表
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
		// 设备确认方法
		handleDeviceConfirm(e) {
		  const [selected] = e.value;
		  this.shebeino_dictText = selected.value;
		  this.showDevicePicker = false;
		  console.log('当前选择设备:', selected);
		},

		methods: {
			  // 设备名称选择
			  typeUnit(val) {
			    this.shebeino_dictText = val.value
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
			  return new Promise((resolve) => {
			    // 使用正确的API端点获取设备列表
			    this.$http.get(`/sys/user/list3`, {
			      params: {
			        sys_depart_orgcode: uni.getStorageSync(USER_INFO)?.orgCode,
			        sbtypes: 18 // 根据实际业务需求调整类型参数
			      }
			    }).then(res => {
			      if (res.data?.success) {
			        // 按新数据结构转换选项
			        this.deviceOptions = res.data.result.map(item => ({
			          label: item.sbname,    // 显示名称用sbname
			          value: item.sbjno      // 实际值用sbjno
			        }));
			        resolve();
			      }
			    })
			  })
			},
				handleDeviceConfirm(e) {
				  if (e.value && e.value.length > 0) {
				    const selected = e.value[0];
				    // 正确绑定字段
				    this.selectedDevice = selected.label; // sbname显示
				    this.shebeino_dictText = selected.value; // sbjno传值
				    
				    // 立即执行查询
				    this.Choose();
				  }
				  this.devicePickerVisible = false;
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
					this.$http.get(`/entranceGuardRecord/entranceGuardRecord/list`, {
					params: {
						sys_depart_orgcode: uni.getStorageSync(USER_INFO).orgCode,
						column: 'id',
						order: 'desc',
						pageNo: this.pageNo,
						pageSize: 10,
						names: this.names, // 添加姓名筛选
						shebeino: this.shebeino_dictText // 添加设备ID筛选
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
/* 卡片列表容器 */
.access-card-list {
  padding: 20rpx;
  background: #f8fafe;
}

/* 整体卡片效果 */
.access-card {
  background: #ffffff;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 8rpx 24rpx rgba(43, 108, 176, 0.12);
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  
  &:active {
    transform: scale(0.98);
    box-shadow: 0 4rpx 12rpx rgba(43, 108, 176, 0.2);
  }
}

/* 卡片内容容器 */
.card-wrapper {
  display: flex;
  padding: 24rpx;
}

/* 信息区域 */
.info-section {
  flex: 1;
  margin-right: 24rpx;
}

/* 头部信息 */
.card-header {
  border-left: 6rpx solid #2b6cb0;
  padding-left: 20rpx;
  margin-bottom: 32rpx;
  
  .device-label {
    display: block;
    font-size: 34rpx;
    color: #2c3e50;
    font-weight: 600;
    line-height: 1.4;
  }
  
  .group-info {
    font-size: 26rpx;
    color: #7f8c8d;
    &::before {
      content: "班组：";
      color: #2b6cb0;
      font-weight: 500;
    }
  }
}

/* 详细信息 */
.detail-info {
  .info-line {
    margin-bottom: 20rpx;
    display: flex;
    align-items: baseline;
    
    &.time-info {
      align-items: center;
    }
  }
  
  .info-tag {
    width: 120rpx;
    font-size: 28rpx;
    color: #2b6cb0;
    font-weight: 500;
    padding-right: 16rpx;
  }
  
  .info-content {
    flex: 1;
    font-size: 28rpx;
    color: #34495e;
  }
  
  .time-stamp {
    font-size: 30rpx;
    color: #2b6cb0;
    font-weight: 500;
    background: rgba(43, 108, 176, 0.1);
    padding: 8rpx 16rpx;
    border-radius: 8rpx;
  }
}

/* 图片区域 */
.image-section {
  width: 200rpx;
  height: 200rpx;
  
  .image-container {
    width: 100%;
    height: 100%;
    border-radius: 12rpx;
    overflow: hidden;
    position: relative;
    background: #f0f4f8;
    border: 2rpx solid rgba(43, 108, 176, 0.1);
  }
  
  .square-img {
    width: 100%;
    height: 100%;
  }
  
  .img-placeholder {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    color: #95a5a6;
    font-size: 24rpx;
    white-space: nowrap;
  }
}
/* 在原有样式后追加 */
.screen-modal-item-input .u-button {
  border: 1px solid #c0c4cc;
  border-radius: 4px;
  padding: 8px 12px;
  color: #606266;
}

.screen-modal-item-input .u-button::after {
  content: "▼";
  float: right;
  font-size: 12px;
  color: #909399;
}

</style>
