<template>
	<view id="mixdatadetail">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">拌合站</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<u-loading-page :loading="loading" loading-text="加载中..."></u-loading-page>
		<view class="pourlist">
			<view v-for="(item,index) in listdata" :key="index" @click="checkDetail(item)" class="pourlist-item">
				<view class="pourlist-item-sta" >
					<image v-if="item.overLevel == 0" src="../../../static/pour/hege.png" mode=""></image>
					<image v-if="item.overLevel == 1" src="../../../static/pour/lv1.png" mode=""></image>
					<image v-if="item.overLevel == 2" src="../../../static/pour/lv2.png" mode=""></image>
					<image v-if="item.overLevel == 3" src="../../../static/pour/lv3.png" mode=""></image>
					<!-- <view :style="overLevel == 0 ? 'color:#F32F45;':'color:#52C57C;'">
						{{ item.overLevel == 0 ? '合格':'已审核' }}
					</view> -->
				</view>
				<view v-if="item.overLevel != 0 && item.overLevel!=null" class="pourlist-item-sta1">
					<image v-if="item.overproofStatus == 0" src="../../../static/pour/weichuzhi.png" mode=""></image>
					<image v-if="item.overproofStatus == 10" src="../../../static/pour/yichuzhi.png" mode=""></image>
					<image v-if="item.overproofStatus == 20" src="../../../static/pour/yishenhe.png" mode=""></image>
					<image v-if="item.overproofStatus == 30" src="../../../static/pour/bohui.png" mode=""></image>
				</view>
				<view class="pourlist-item-con">
					<view class="pourlist-item-con-name">
						设备名称：<span>{{ item.shebeiNo_dictText }}</span>
					</view>
					<view class="pourlist-item-con-name">
						工程名称：<span>{{ item.projectName }}</span>
					</view>
					<view class="pourlist-item-con-name">
						施工地点：<span>{{ item.jobLocation }}</span>
					</view>
					<view class="pourlist-item-con-name">
						浇筑部位：<span>{{ item.poureLocation }}</span>
					</view>
					<view class="pourlist-item-con-name">
						出料时间：<span>{{ item.productDatetime }}</span>
					</view>
					<view class="pourlist-item-con-name">
						强度等级：<span>{{ item.strengthRank }}</span>
					</view>
					<view class="pourlist-item-con-name">
						操作者：<span>{{ item.handlers }}</span>
					</view>
					<view class="pourlist-item-con-name">
						方量：<span>{{ item.estimateNumber }}</span>
					</view>
					<view class="pourlist-item-con-name">
						塌落度：<span>{{ item.slump }}</span>
					</view>
					<!-- <view class="pourlist-item-con-name">
						超标等级：<span>{{ item.overLevel }}</span>
					</view> -->
					<view class="pourlist-item-con-name"></view>
				</view>
			</view>
			<view style="height: 40upx;"></view>
		</view>
		<view class="screen" v-if="show" @confirm="confirm">
			<view class="screen-modal">
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">设备名称：</view>
					<view class="screen-modal-item-input">
						<picker @change="Changedevice" :range="devicenames">
							<u--input placeholder="请选择设备名称" border="surround" v-model="devicename" disabled suffixIcon="arrow-down"></u--input>
						</picker>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">超标等级：</view>
					<view class="screen-modal-item-input">
						<picker @change="ChangeoverLV" :range="overLevels">
							<u--input placeholder="请选择超标等级" border="surround" v-model="overlevelname" disabled suffixIcon="arrow-down"></u--input>
						</picker>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">时间：</view>
					<view class="screen-modal-item-input" @click="dateshow = true">
						<u--input placeholder="请选择时间" border="surround" v-model="datetimevalue" disabled
							suffixIcon="arrow-down"></u--input>
					</view>
				</view>
				<view class="screen-modal-btn">
					<u-button text="取消" @click="confirm"></u-button>
					<u-button type="primary" text="确认" @click="handleOk"></u-button>
				</view>
			</view>
		</view>
		<mx-date-picker :show="dateshow" :type="type" :value="value" :show-tips="true" :show-seconds="true"
			@confirm="confirmdate" @cancel="confirmdate" />
	</view>
</template>

<script>
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	import smartbhapi from "../../../api/smartbh.js"
	export default {
		components: {
			MxDatePicker
		},
		data() {
			return {
				loading: true,
				listdata:[],
				pageNo:1,
				show:false,
				devicedata:[],
				devicenames:[],
				devicename:'',
				dateshow: false,
				datetimevalue: '',
				type: 'rangetime',
				shebeiNo:'',
				overLevel:'',
				overLevels: ['合格', '初级超标', '中级超标', '高级超标'],
				overlevelname:'',
				value:'',
				productDatetime_begin:'',
				productDatetime_end:''
			}
		},
		mounted() {
			this.getloadlist()
			this.getdevicelist()
		},
		onReachBottom() {
			uni.showNavigationBarLoading()
			this.pageNo++
			this.getloadlist()
			uni.hideNavigationBarLoading()
		},
		methods: {
			checkDetail(e) {
				uni.navigateTo({
					url:'/pages/smartbh/mix/mixdatadetail?item=' + JSON.stringify(e)
				})
			},
			// 打开筛选框
			screen() {
				this.show = true
			},
			// 确认筛选
			handleOk() {
				this.pageNo = 1
				this.listdata = []
				this.getloadlist()
				this.show = false
			},
			// 关闭筛选框
			confirm() {
				// this.getloadlist()
				this.show = false
			},
			// 时间选择
			confirmdate(e) {
				console.log(e)
				if(e){
					this.datetimevalue = e.value.toString()
					this.productDatetime_begin = e.value[0]
					this.productDatetime_end = e.value[1]
				}
				this.dateshow = false
			},
			// 超标等级选择
			ChangeoverLV(e) {
				// console.log(e.detail.value)
				this.overlevelname = this.overLevels[e.detail.value]
				this.overLevel = e.detail.value
			},
			// 设备选择
			Changedevice(e) {
				// console.log(e.detail.value)
				this.devicename = this.devicenames[e.detail.value]
				this.shebeiNo = this.devicedata[e.detail.value].sbjno
			},
			// 获取拌合站数据
			getloadlist() {
				this.loading = true
				let params = {
					column: 'id',
					order: 'desc',
					pageNo: this.pageNo,
					pageSize: 10,
					// shebeiNo: this.shebeiNo,
					overLevel: this.overLevel,
					productDatetime_begin: this.productDatetime_begin,
					productDatetime_end: this.productDatetime_end,
				}
				if (this.shebeiNo !== "") {
					let params2 = {
						shebeiNo: this.shebeiNo
					}
					params = {
						...params,
						...params2
					}
				}
				smartbhapi.mixlist({
					params
				}).then(res => {
					// console.log(res, "任务单列表")
					this.loading = false
					if (res.data.result.records.length == 0) {
						uni.showToast({
							title: '没有更多数据了',
							icon: 'none'
						})
					}
					if (this.pageNo != 1) {
						this.listdata = this.listdata.concat(res.data.result.records)
					} else {
						this.listdata = res.data.result.records
					}
				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			// 获取设备列表
			getdevicelist() {
				let params = {
					sbtypes: 0
				}
				smartbhapi.deviceList({
					params
				}).then(res => {
					console.log(res)
					this.devicedata = res.data.result
					this.devicenames = []
					this.devicedata.forEach(item => {
						this.devicenames.push(item.sbname)
					})
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	#mixdatadetail{
		background-color: #F3F5FE;
		.pourlist{
			width: 100%;
			margin-top: 30upx;
			&-item{
				position: relative;
				width: 690upx;
				// height: 462upx;
				margin: 0 auto;
				margin-bottom: 30upx;
				border-radius: 16upx;
				background-color: white;
				
				.hege {
					background-image: url(../../../static/pour/hege.png);
					background-repeat: no-repeat;
					background-size: 100% 100%;
				}
				
				&-sta{
					position: absolute;
					right: 0px;
					width: 165upx;
					height: 165upx;
					
					image{
						width: 100%;
						height: 100%;
					}
				}
				&-sta1{
					position: absolute;
					right: 20upx;
					bottom: 20upx;
					width: 165upx;
					height: 165upx;
					image{
						width: 100%;
						height: 100%;
					}
				}
				
				&-con{
					width: 610upx;
					// height: 336upx;
					margin: 0 auto;
					// background-color: red;
					&-name{
						padding-top: 30upx;
						color: #9299A8;
						font-size: 28upx;
						span{
							color: #4C5971;
						}
					}
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
		}
		
	}
</style>
