<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">审核</block>
		</cu-custom>
		<!-- <view class="section"> -->
		<view class="radio">
			<view class="result">
				<view style="padding: 30upx 10upx;font-size: 28upx;">
					审批结果：
				</view>
		
				<view style="margin: -70upx 120upx;">
					<u-radio-group v-model="value" placement="row">
						<u-radio :customStyle="{marginLeft: '15px'}" v-for="(item, index) in radiolist1" :key="index"
							:label="item.name" :name="item.status" @change="radioChange">
						</u-radio>
					</u-radio-group>
				</view>
			</view>
			<view class="opinion">
				<view style="padding: 30upx 10upx;font-size: 28upx;">
					审批意见：
				</view>
				<view style="margin:-85upx 150upx;width:430upx;">
					<u--input placeholder="请输入审批意见" border="surround" v-model="opinion" @change="change1"></u--input>
					</view>
				</view>
			</view>
			<!-- </view> -->
		<view class="">
			<u-button type="primary" style="width: 100px;border-radius: 10px;" @click="checkconfirm" text="确定"></u-button>
		</view>
	</view>
</template>

<script>
	export default {
		name: 'Zlcheck',
		data() {
			return {
				value: 1,
				opinion: '', //
				showcheck: false,
				radiolist1: [{
						name: '同意',
						status: 1,
						disabled: false
					},
					{
						name: '不同意',
						status: 0,
						disabled: false
					}
				],
				item:'',
				ids:'',
				ddcode:''
			}
		},
		onLoad(options){
			console.log(options.item,options.id,'fffffff')
			let data = 	options.item
			let id = options.id
			this.item = data
			this.ids = id
			this.ddcode = options.ddcode
		},
		methods: {
			radioChange(n) {
				console.log('radioChange', n);
				this.value = n
			},
			// 审批意见
			change1(e) {
				console.log(e, '审批意见')
				this.opinion = e
			},
			//审核
			checkconfirm() {
				let params = {
					status: this.value,
					note: this.opinion,
					id:this.ids
				}
				if(this.item ==0){
					this.$http.put(`/zhiliangrenwudan/zhiliangrenwudan/edit`, params).then(res => {
						console.log(res, '审核')
						if (res.data.success == true) {
							uni.showToast({
								title: "编辑成功",
								icon: 'success'
							})
							setTimeout(function() {
								uni.navigateBack({
									url: "/pages/smartlc/orderTracking/orderlist",
								})
							}, 500)
						}
					})
				}
				if(this.item ==1){
					this.$http.put(`/system/bhzrenwudan/edit`, params).then(res => {
						console.log(res, '审核')
						if (res.data.success == true) {
							uni.showToast({
								title: "编辑成功",
								icon: 'success'
							})
							setTimeout(function() {
								uni.navigateBack({
									url: "/pages/smartlc/orderTracking/orderlist",
								})
							}, 500)
						}
					})
				}
			},
		}
	}
</script>

<style lang="less" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FE;
// .section{
	
	.radio {
		width:350px;
		height: auto;
		border-radius: 10px;
		margin: 20px auto;
		background: #fff;
		// border: 1px solid #8DC63F;
	}
	
	.result {
		width: 300px;
		height: 100upx;
		// padding:10upx 10upx;
		// border: 1px solid #1785FF;
	}
	
	.opinion {
		width: 300px;
		height: 100upx;
		// padding:10upx 10upx;
		// border: 1px solid #55aa7f;
	}
// }
		
	}
</style>
