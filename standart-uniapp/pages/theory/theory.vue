<template>
	<view>
		<view id="home">
			<cu-custom bgColor="bg-white" :isBack="true">
				<block slot="content">理论配合比</block>
				<view slot="right" @click="screen">筛选</view>
			</cu-custom>
			<view class="top">
				<view class="left">
					<view class="">
						0
						<p>已使用</p>
					</view>
				</view>
				<view class="right">
					<view class="">
						0
						<p>理论配合总数</p>
					</view>
				</view>
			</view>
				<view class="section" v-for="(item,index) in listdatavalue" :key="index">
			<navigator :url="'/pages/theory/theorydetails?item='+JSON.stringify(item)">
					<view class="section-text">
						<view class="">配合比编号:<span>{{item.code!==null?item.code:'暂无数据'}}</span></view>
						<view class="">强度等级:<span>{{item.betlevel!==null?item.betlevel:'暂无数据'}}</span></view>
						<view class="">抗渗等级:<span>{{item.filters!==null?item.filters:'暂无数据'}}</span></view>
						<view class="">抗冻等级:<span>{{item.freeze !==null?item.freeze:'暂无数据'}}</span></view>
						<view class="">抗折等级:<span>{{item.bend !==null?item.bend:'暂无数据'}}</span></view>
						<view class="">坍落度:<span>{{item.lands !==null?item.lands:'暂无数据'}}</span></view>
						<view class="">搅拌时长:<span>{{item.mixlast !==null?item.mixlast:'暂无数据'}}</span></view>
						<view class="">建立时间:<span>{{item.createdate !==null?item.createdate:'暂无数据'}}</span></view>
						<view class="">拌合机名称:<span>{{item.bhjno_dictText !==null?item.bhjno_dictText:'暂无数据'}}</span></view>
						<view class="">水胶比:<span>{{item.waterbindratio !==null?item.waterbindratio:'暂无数据'}}</span></view>
					</view>
			</navigator>
				</view>
			<!-- 添加按钮 -->
			
			<view class="">
				<view class="increase-img" @click="increase" v-has="'peihe:add'"></view>
			</view>
			<!-- 查询设备 -->
			<view class="screen" v-if="show" @confirm="confirm" >
				<view class="screen-modal">
					<view class="screen-modal-item">
						<view class="screen-modal-item-name">设备名称：</view>
						<picker @change="PickerChange" :range="deviceNames">
							<u--input placeholder="请选择设备名称" border="surround" v-model="deviceName" disabled></u--input>
						</picker>
					</view>
					<view class="screen-modal-btn">
						<u-button text="取消" @click="confirm"></u-button>
						<u-button type="primary" text="确认" @click="Choose"></u-button>
					</view>

				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import timeSelector from '@/components/wing-time-selector/wing-time-selector.vue';
	import api from "@/api/apis.js"
	export default {
		data() {
			return {
				listdatavalue: '',
				deviceName: [],
				devictype: [],
				choosekey: '',
				choosevalue: '',
				sbNumber: null,
				deviceNames: [],
				deviceName: '',
				orgcode: '',
				sbtype: '0',
				index: -1,
				show: false,
				begintime: '',
				endtime: '',
				pageNo: 1,
			}
		},
		onLoad() {
			this.listdata()
		},
		onReachBottom() {
			uni.showNavigationBarLoading()
			this.pageNo++
			this.listdata()
			uni.hideNavigationBarLoading()
		},
		created() {
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
					// console.log(res.data.result)
					let devicelist = res.data.result
					this.deviceNames = []
					this.devictype = []
					devicelist.forEach(e => {
						this.deviceNames.push(e.sbname)
						this.devictype.push(e.sbjno)
					})
				}).catch(e => {
					// console.log("请求错误", e)
				})
			
			},
			// 设备名称
			PickerChange(e) {
				// console.log(e)
				// this.cbindex = -1
				this.index = e.detail.value
				this.choosekey = 2
				this.choosevalue = this.devictype[this.index]
				this.deviceName = this.deviceNames[this.index]
				// console.log(this.deviceName,'sss')
				this.sbNumber = this.choosevalue
				// console.log(this.sbNumber, 'jjjjjjjjjjjj');
			
				this.begintime = ''
				this.endtime = ''
				// this.Choose()
			},
			Choose(choosekey, choosevalue) {
				// this.deviceName = []
				// console.log(this.choosekey,this.choosevalue);
				// 设备名称
				if (this.choosekey == 2) {
					this.sbNumber = this.choosevalue
					// console.log(this.sbNumber)
					this.begintime = ''
					this.endtime = ''
				}

				this.listdata()
				this.show = false
				
			},
			listdata() {
				this.$http.get(`/bhzrecipe/bhzRecipe/list`, {
					params: {
						column: 'id',
						order: 'desc',
						pageNo: this.pageNo,
						PageSize: 10,
						// sys_depart_orgcode: this.orgcode,
						bhjno: this.sbNumber,
					}
				}).then(res => {
					if (res.data.result.records.length == 0) {
						uni.showToast({
							title: '别下拉啦 没有更多数据了',
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
			increase() {
				uni.navigateTo({
					url: '/pages/theory/theoryadd'
				})
			},
			screen() {
				this.deviceName = ''
				this.show = true
			},
			handleOk() {
				this.listdata()
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
		width: 100%;
		height: auto;

		.top {
			width: 700rpx;
			height: 120px;
			background-color: #fff;
			margin: 15px auto;
			border-radius: 10px;
			display: flex;
			justify-content: space-around;
			align-items: center;

			.left {
				width: 300rpx;
				height: 80px;
				background-color: #fff1e4;
				border-radius: 10px;

				view {
					font-size: 19px;
					font-weight: 500;
					text-align: center;
					color: #FF8712;
					width: 100px;
					margin: 18px auto;


					p {
						font-size: 14px;
						font-weight: 500;
						text-align: center;
						color: #000;
					}
				}
			}

			.right {
				width: 300rpx;
				height: 80px;
				background-color: #e9fbf3;
				border-radius: 10px;

				view {
					font-size: 19px;
					font-weight: 500;
					text-align: center;
					color: #0FBF6E;
					width: 100px;
					margin: 18px auto;


					p {
						font-size: 14px;
						font-weight: 500;
						text-align: center;
						color: #000;
					}
				}
			}
		}

		.section {
			width: 700rpx;
			height: auto;
			background-color: #fff;
			margin: 0 auto;
			border-radius: 10px;

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

		.increase-img {
			width: 77px;
			height: 77px;
			background-image: url(../../static/pour/add.png);
			background-size: 100% 100%;
			position: fixed;
			bottom: 176px;
			right: -2px;
		}
	}
</style>
