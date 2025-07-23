<template>
	<view id="testblockdown">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">试块下架</block>
		</cu-custom>
		<view class="main">
			<view class="main-item">
				<view class="main-item-name">试验类型：</view>
				<view class="main-item-input">
					<u--input disabled v-model="endData.syxmmc"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">工程名称：</view>
				<view class="main-item-input">
					<u--input disabled v-model="endData.gcmc"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">浇筑部位：</view>
				<view class="main-item-input">
					<u--input disabled v-model="endData.sgbw"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">样品名称：</view>
				<view class="main-item-input">
					<u--input disabled v-model="endData.ypmc"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">取样日期：</view>
				<view class="main-item-input">
					<u--input disabled v-model="endData.qyrq"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">试验数组：</view>
				<view class="main-item-input">
					<u--input disabled v-model="endData.sysuliang"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">强度等级：</view>
				<view class="main-item-input">
					<u--input disabled v-model="endData.qddj"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">龄期：</view>
				<view class="main-item-input">
					<u--input disabled v-model="endData.yplq"></u--input>
				</view>
			</view>
			<view class="main-item">
				<u-button class="btnclass" type="primary" @click="onScan" text="扫码"></u-button>
				<u-button class="btnclass" plain type="primary" @click="bashow = true" text="输入"></u-button>
			</view>
			<view class="main-item">
				<u-button class="btnclass" type="warning" @click="onConfirm" text="确认下架"></u-button>
			</view>
			<view class="main-item"></view>
		</view>
		<!-- 输入编码 -->
		<u-modal :title="'输入编码'" :show="bashow" @confirm="confirm1" showCancelButton @cancel="cancel">
			<view class="modal">
				<u--input placeholder="请输入编码" v-model="code"></u--input>
				<!-- <uni-grid :column="5" @change="change">
					<uni-grid-item style="width: 20%;" v-for="(item,index) in huajiaNews" :index="index" :key='index'>
						<view class="grid-item-box" :style="{'background-color': item.bgindex==0?'#FFF':'#3c9cff' }">
							<text class="text">{{ item.huojianame }}</text>
						</view>
					</uni-grid-item>
				</uni-grid> -->
			</view>
		</u-modal>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				endData:{
					syxmmc:'',
					gcmc:'',
					sgbw:'',
					ypmc:'',
					qyrq:'',
					sysuliang:'',
					qddj:'',
					yplq:''
				},
				codeno:'',
				code:'',
				bashow:false
			};
		},
		methods:{
			// 下架
			onConfirm() {
				// consol e.log(1111)
				// console.log(this.codeno)
				// console.log(this.code)
				// return
				// /hntconsigncode/hntConsignCode/edityhdowns
				if(this.code){
					let params = {
						code:this.code
					}
					this.$http.put('/hntconsigncode/hntConsignCode/edityhdown',params).then(res=>{
						if (res.data.success) {
							uni.showToast({
								title: "操作成功",
								icon: 'none'
							})
							uni.navigateTo({
								url: '/pages/smartsy/sampling/samplinglist',
							})
						}
					})
				}
				if(this.codeno){
					let params = {
						codeno:this.codeno
					}
					this.$http.put('/hntconsigncode/hntConsignCode/edityhdown',params).then(res=>{
						if (res.data.success) {
							uni.showToast({
								title: "操作成功",
								icon: 'none'
							})
							uni.navigateTo({
								url: '/pages/smartsy/sampling/samplinglist',
							})
						}
					})
				}
			},
			confirm1(){
				// console.log(this.code)
				this.$http.post('/hntconsign/hntConsign/yhdownqueryOne?code='+this.code).then(result=>{
					console.log(result)
					if(!result.data.success){
						uni.showToast({
							title:result.data.message,
							icon:"none"
						})
						return
					}
					if(result.data.success){
						this.endData = result.data.result
						this.bashow = false
					}
				})
			},
			cancel() {
				this.code = ''
				this.bashow = false
			},
			// 二维码扫描
			onScan(){
				var that = this
				uni.scanCode({
					success: function(res) {
						// console.log('条码类型：' + res.scanType);
						// console.log('条码内容：' + res.result);
						let a = res.result.split('#')
						that.codeno = a[1]
						that.$http.post('/hntconsign/hntConsign/yhdownqueryOne?codeno='+a[1]).then(result=>{
							console.log(result)
							if(!result.data.success){
								uni.showToast({
									title:result.data.message,
									icon:"none"
								})
								return
							}
							if(result.data.success){
								that.endData = result.data.result
								// that.endCuring = false
								// that.modalNumber = null
							}
						})
					},
				});
			},
			
		}
	}
</script>

<style lang="scss" scoped>
	#testblockdown {
		width: 100%;
		height: 100vh;
		background-color: #F3F5FE;

		.main {
			width: 690upx;
			// height: 754upx;
			margin: 0 auto;
			margin-top: 30upx;
			background-color: white;
			border-radius: 16upx;

			.tags {
				display: flex;
				flex-wrap: wrap;
				margin-top: 20upx;
			}

			.mainnew {
				color: #333333;
				font-size: 30upx;
				font-weight: bold;
			}

			&-item {
				display: flex;
				align-items: center;
				padding-top: 30upx;

				.btnclass {
					margin: 0 10upx;
					// width: 30%;
				}

				&-name {
					flex: 3;
					font-size: 30upx;
					text-align: right;
					color: #4C5971;
				}

				&-input {
					flex: 8;
					margin-right: 30upx;
					background-color: #F6F9FC;
					border-radius: 10upx;
				}
			}
		}
		.modal {
			// background-color: red;
			// height: 300upx;
			width: 100%;
		}
	}
</style>
