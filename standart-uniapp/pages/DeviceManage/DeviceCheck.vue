<template>
	<view id="home">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">设备巡检</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>

<!-- 新增悬浮按钮 -->
		<view class="float-button" @click="addCheck">
			<u-icon name="plus" color="#fff" size="28"></u-icon>
			<text class="btn-text">检查</text>
		</view>

  <!-- 设备列表容器 -->
  <view class="rw-list-container">
    <!-- 每个设备项独立循环 -->
    <view 
      v-for="(item, index) in shitilist" 
      :key="index"
      class="rw-item-wrapper"
    >
      <!-- 可点击卡片 -->
      <view 
        class="rw-interactive-card"
        :class="item.zhengchang === 0 ? 'normal-state' : 'alert-state'"
        @click="shwoTab(item)"
      >
        <!-- 状态指示器 -->
        <view class="status-indicator">
          <view class="state-marker">
            {{ item.zhengchang === 0 ? '✓' : '!' }}
          </view>
          <text class="state-text">
            {{ item.zhengchang === 0 ? '运行正常' : '需要维护' }}
          </text>
        </view>

        <!-- 设备信息主体 -->
        <view class="info-content">
          <view class="info-row">
            <text class="label">设备编号：</text>
            <text class="value">{{ item.shebeino || '暂无数据' }}</text>
          </view>

          <view class="info-row">
            <text class="label">设备名称：</text>
            <text class="value">{{ item.shebeino_dictText || '暂无数据' }}</text>
          </view>

          <view class="info-block">
            <text class="label">状况描述：</text>
            <text class="detail">{{ item.miaoshu || '无异常报告' }}</text>
          </view>

          <view class="info-block">
            <text class="label">检查内容：</text>
            <text class="detail">{{ item.checkcontent || '无检查记录' }}</text>
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




		<!-- 筛选弹窗部分 -->
		<view class="screen" v-if="show">
		  <view class="screen-modal">
		    <!-- 设备编号 -->
		    <view class="screen-modal-item">
		      <view class="screen-modal-item-name">设备编号：</view>
		      <u-input 
		        placeholder="请输入设备编号" 
		        v-model="shebeino"
		      ></u-input>
		    </view>
		
		    <!-- 设备状态（修正版） -->
		    <view class="screen-modal-item">
		      <view class="screen-modal-item-name">设备状态：</view>
		      <u-radio-group v-model="zhengchang">
		        <u-radio 
		          :name="0" 
		          label="正常"
		        ></u-radio>
		        <u-radio 
		          :name="1" 
		          label="故障"
		        ></u-radio>
		      </u-radio-group>
		    </view>
		
		    <!-- 操作按钮 -->
		    <view class="screen-modal-btn">
		      <u-button text="重置" @click="confirm"></u-button>
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
				shebeino: '',
				shebeino_dictText: '',
				zhengchang: null, // 初始状态为null表示不限
				tbStatus: null,
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
				// 新增跳转方法
					addCheck() {
						uni.navigateTo({
							url: '/pages/DeviceManage/AddDeviceCheck',
							fail: (err) => {
								console.error('跳转失败:', err)
								uni.showToast({ title: '无法打开页面', icon: 'none' })
							}
						})
					},
			shwoTab(item) {
			    try {
			      // 1. 参数有效性验证
			      if (!item || typeof item !== 'object') {
			        console.error('无效的项目数据');
			        return;
			      }
			
			      // 2. 路径配置常量
			      const TARGET_PAGE = "/pages/DeviceManage/DeviceCheckInfo"; 
			
			      // 3. 安全参数序列化
			      const safeStringify = (obj) => {
			        try {
			          return JSON.stringify(obj);
			        } catch (e) {
			          console.error('对象序列化失败:', e);
			          return '{}';
			        }
			      };
			
			      // 4. 标准化编码流程
			      const encodedParams = encodeURIComponent(safeStringify(item));
			      
			      // 5. 带错误处理的路由跳转
			      uni.navigateTo({
			        url: `${TARGET_PAGE}?payload=${encodedParams}`,
			        fail: (err) => {
			          console.error('路由跳转失败:', err);
			          uni.showToast({
			            title: '页面打开失败',
			            icon: 'none'
			          });
			        }
			      });
			
			    } catch (error) {
			      console.error('跳转过程异常:', error);
			    }
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
			
			

			Choose(choosekey, choosevalue) {
				// console.log(this.choosekey,this.choosevalue);
		

				this.textlist()
				this.show = false
			},
			textlist() {

				if (this.tbStatus == 0) {
					this.tbStatus = null
				}
				this.$http.get(`shebeiinfo/shebeiOverhaul/listsx`, {
					// this.$http.get(`/car/schedulingCar/list`, {
					params: {
						column: 'id',
						order: 'desc',
						pageSize: 10,
						sys_depart_orgcode: uni.getStorageSync(USER_INFO).orgCode,
						shebeino:this.shebeino,
						zhengchang:this.zhengchang,
						shebeino_dictText:this.shebeino_dictText,
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
		
			handleOk() {
				this.pageNo = 1
				this.listdata = []
				this.textlist()
				this.show = false
			},
			confirm() {
				this.show = false
				this.shebeino = ''
				this.shebeino_dictText = ''
				this.zhengchang = null
				this.Choose()

			},
		},
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

	.status-radio-group {
	  display: flex;
	  padding: 12rpx 0;
	  
	  .status-radio {
	    margin-right: 40rpx;
	    
	    /deep/ .u-radio__label {
	      font-size: 28rpx;
	      color: #606266;
	    }
	  }
	}
	
	.screen-modal-btn {
	  margin-top: 40rpx;
	  display: flex;
	  justify-content: space-between;
	  
	  /deep/ .u-button {
	    flex: 0.48;
	  }
	}
	.rw-list-container {
	  padding: 24rpx;
	  background: #f8f9fa;
	}
	
	.rw-item-wrapper {
	  margin-bottom: 24rpx;
	}
	
	.rw-interactive-card {
	  border-radius: 16rpx;
	  overflow: hidden;
	  transition: all 0.2s ease;
	  
	  /* 点击反馈效果 */
	  &:active {
	    opacity: 0.85;
	    transform: scale(0.98);
	  }
	
	  /* 正常状态 */
	  &.normal-state {
	    border-left: 8rpx solid #34d399;
	    .status-indicator {
	      background: #ecfdf5;
	      color: #059669;
	    }
	    .state-marker {
	      background: #34d399;
	    }
	  }
	
	  /* 异常状态 */
	  &.alert-state {
	    border-left: 8rpx solid #f87171;
	    .status-indicator {
	      background: #fef2f2;
	      color: #dc2626;
	    }
	    .state-marker {
	      background: #f87171;
	    }
	  }
	}
	
	.status-indicator {
	  padding: 20rpx;
	  display: flex;
	  align-items: center;
	  font-size: 28rpx;
	}
	
	.state-marker {
	  width: 40rpx;
	  height: 40rpx;
	  border-radius: 50%;
	  color: white;
	  display: flex;
	  align-items: center;
	  justify-content: center;
	  margin-right: 16rpx;
	  font-weight: 700;
	}
	
	.info-content {
	  padding: 20rpx;
	  background: white;
	}
	
	.info-row {
	  display: flex;
	  justify-content: space-between;
	  margin-bottom: 18rpx;
	}
	
	.info-block {
	  margin: 24rpx 0;
	}
	
	.label {
	  color: #6b7280;
	  font-size: 28rpx;
	  flex-shrink: 0;
	}
	
	.value {
	  color: #1f2937;
	  font-size: 28rpx;
	  text-align: right;
	  flex: 1;
	  padding-left: 20rpx;
	}
	
	.detail {
	  color: #374151;
	  font-size: 28rpx;
	  line-height: 1.5;
	  display: block;
	  margin-top: 8rpx;
	}
	// 新增按钮样式
	.float-button {
		position: fixed;
		right: 30rpx;
		bottom: 120rpx;
		z-index: 999;
		display: flex;
		align-items: center;
		padding: 20rpx 30rpx;
		background: #2979ff;
		border-radius: 50rpx;
		box-shadow: 0 6rpx 18rpx rgba(41, 121, 255, 0.4);
		transition: all 0.2s;
		
		&:active {
			transform: scale(0.95);
			opacity: 0.9;
		}
		
		.btn-text {
			color: #fff;
			font-size: 30rpx;
			margin-left: 10rpx;
		}
		
		.u-icon {
			filter: drop-shadow(0 2rpx 4rpx rgba(0,0,0,0.1));
		}
	}
</style>
