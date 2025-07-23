<template>
	<view id="workorderdetail">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">工单详情</block>
		</cu-custom>
		<view class="top">
			<view class="top-item" style="background-color: rgba(126, 158, 254, .1);" @click="handleNav(0)">
				<view class="top-item-img" style="background-image: url(../../static/ruanji/liceng.png);"></view>
				<view class="top-item-name">
					<view>里程</view>
					<view>{{loaddata.mileage}}</view>
				</view>
			</view>
			<!-- <view class="top-item" style="background-color: rgba(47, 174, 255, .1);" @click="handleNav(1)">
				<view class="top-item-img" style="background-image: url(../../static/ruanji/shigongdui.png);"></view>
				<view class="top-item-name">施工队</view>
			</view> -->
		</view>
		<view class="title">统计信息</view>
		<view class="newstj">
			<view class="newstj-item">
				<view class="newstj-item-num"><span style="color: #333333; font-size: 38upx;" >{{loaddata.descount}}</span></view>
				<view class="newstj-item-name">设计桩基数</view>
			</view>
			<view class="newstj-item">
				<view class="newstj-item-num"><span style="color: #333333; font-size: 38upx;" >{{loaddata.piletotal}}</span></view>
				<view class="newstj-item-name">完成桩基数</view>
			</view>
			<view class="newstj-item">
				<view class="newstj-item-num"><span style="color: #333333; font-size: 38upx;" >{{loaddata.mileagetotal}}</span>m</view>
				<view class="newstj-item-name">完成里程数</view>
			</view>
			<view class="newstj-item">
				<view class="newstj-item-num"><span style="color: #333333; font-size: 38upx;" >{{loaddata.chaobiaototal}}</span></view>
				<view class="newstj-item-name">超标数</view>
			</view>
			<view class="newstj-item">
				<view class="newstj-item-num"><span style="color: #333333; font-size: 38upx;" >{{loaddata.handled}}</span></view>
				<view class="newstj-item-name">处置数</view>
			</view>
			<view class="newstj-item">
				<view class="newstj-item-num"><span style="color: #333333; font-size: 38upx;" >{{loaddata.checked}}</span></view>
				<view class="newstj-item-name">闭合数</view>
			</view>
			<u-divider style="width: 95%; margin: 30upx auto 0;"></u-divider>
			<view class="newstj-pro">
				<u-line-progress height="18" :percentage="(loaddata.piletotal/loaddata.descount)*100"></u-line-progress>
			</view>
		</view>
		<view class="title">详细信息</view>
		<view class="list">
			<view v-for="(item,index) in loaddetail" :key="index" @click="checkDetail" class="list-item">
				<view class="list-item-sta" :class="item.chaobiaodengji == 1 ? 'red':'green'">
					<view :style="item.chaobiaodengji == 1 ? 'color:#F32F45;':'color:#52C57C;'">
						{{ item.chaobiaodengji == 1 ? '不合格':'合格' }}
					</view>
				</view>
				<view class="list-item-con">
					<view class="list-item-con-name">
						桩号：<span>{{ item.pileNo }}</span>
					</view>
					<view class="list-item-con-name">
						开始时间：<span>{{ item.pileStarttime }}</span>
					</view>
					<view class="list-item-con-name">
						结束时间：<span>{{ item.pileTime }}</span>
					</view>
					<view class="list-item-con-name">
						水泥用量：<span>{{ item.pileCement }}</span>
					</view>
					<view style="height: 30upx;"></view>
				</view>
			</view>
			<view style="height: 40upx;"></view>
		</view>
		<view class="btn">
			<u-button class="btnclass" v-has="'workorder:xf'" type="primary"  @click="show = true" text="下发"></u-button>
			<u-button v-if="loaddata.status==0" class="btnclass" v-has="'workorder:edit'" type="primary" plain @click="handleEdit" text="编辑"></u-button>
			<u-button v-if="loaddata.status==0" class="btnclass" v-has="'workorder:del'" type="error"  @click="handlerDel" text="删除"></u-button>
		</view>
		<view style="height: 40upx;"></view>
		<u-modal :show="show" :title="title" showCancelButton @cancel="show = false" @confirm="handleXf" >
			<view class="screen-modal-item">
				是否确认下发工单！！！
			</view>
		</u-modal>
	</view>
</template>

<script>
	import ruanjiapi from "../../api/ruanji.js"
	export default {
		data() {
			return {
				show:false,
				rjrwd:'',
				loaddata:'',
				loaddetail:[],
				pageNo:1,
				
			}
		},
		onLoad(options) {
			// console.log(options)
			this.rjrwd = options.rjrwd
			this.getloadlist()
			this.getloaddetail()
		},
		onReachBottom() {
			uni.showNavigationBarLoading()
			this.pageNo++
			this.getloaddetail()
			uni.hideNavigationBarLoading()
		},
		methods: {
			// 下发
			handleXf() {
				let params = {rjrwd:this.loaddata.rjrwd}
				ruanjiapi.workorderxf({params}).then(res=>{
					if (res.data.success) {
						this.$tip.toast(res.data.result)
						this.$Router.replace({
							name: 'workorder'
						})
					}else{
						this.$tip.toast('下发失败')
					}
					this.show = false
				})
			},
			// 编辑工单数据
			handleEdit() {
				uni.navigateTo({
					url: '/pages/ruanji/workorderadd?id='+this.loaddata.id
				})
			},
			// 删除软基工单数据
			handlerDel() {
				let params = {id:this.loaddata.id}
				ruanjiapi.workorderdel('',{params}).then(res=>{
					if (res.data.success) {
						this.$tip.toast('提交成功')
						this.$Router.replace({
							name: 'workorder'
						})
					}else{
						this.$tip.toast('提交失败')
					}
				})
			},
			// 获取软基工单数据
			getloadlist() {
				let params = {
					rjrwd: this.rjrwd
				}
				ruanjiapi.workorderlist({
					params
				}).then(res => {
					// console.log(res, "软基工单数据")
					this.loaddata = res.data.result.records[0]
				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			// 获取软基工单详细信息
			getloaddetail() {
				let params = {
					rjrwd: this.rjrwd,
					pageNo: this.pageNo,
					pageSize: 10
				}
				ruanjiapi.workorderdetail({params}).then(res=>{
					// console.log(res,'软基工单详情')
					if (res.data.result.records.length == 0) {
						uni.showToast({
							title: '没有更多数据了',
							icon: 'none'
						})
					}
					if (this.pageNo != 1) {
						this.loaddetail = this.loaddetail.concat(res.data.result.records)
					} else {
						this.loaddetail = res.data.result.records
					}
				}).catch(e => {
					console.log("请求错误", e)
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
#workorderdetail{
	width: 100%;
	background-color: #F3F5FE;
	.title {
		font-size: 36upx;
		color: #333333;
		font-weight: 600;
		margin-top: 30upx;
		margin-left: 30upx;
	}
	.top {
		width: 690upx;
		height: 170upx;
		margin: 0 auto;
		margin-top: 30upx;
		border-radius: 16upx;
		background-color: white;
		display: flex;
		flex-wrap: wrap;
		justify-content: space-around;
		align-items: center;
	
	
		&-item {
			width: 650upx;
			height: 130upx;
			border-radius: 16upx;
			background-color: white;
			display: flex;
			// flex-direction: column;
			// justify-content: center;
			align-items: center;
	
			&-img {
				width: 75upx;
				height: 75upx;
				background-repeat: no-repeat;
				background-size: 100% 100%;
				margin-left: 30upx;
				margin-right: 18upx;
			}
	
			&-name {
				font-size: 28upx;
				color: #333333;
				// margin-top: 24upx;
			}
		}
	}
	.newstj{
		width: 690upx;
		// height: 360upx;
		margin: 0 auto;
		margin-top: 30upx;
		color: #727B8E;
		border-radius: 16upx;
		background-color: white;
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
		
		&-pro{
			width: 610upx;
			margin: 30upx auto;
		}
		
		&-item{
			width: 210upx;
			height: 130upx;
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			
			&-num{
				font-size: 28upx;
				color: #4C5971;
				// font-family: PingFang-SC-Medium;
			}
			
			&name{
				font-size: 28upx;
			}
		}
	}
	.list{
		width: 100%;
		margin-top: 30upx;
		&-item{
			position: relative;
			width: 690upx;
			margin: 0 auto;
			margin-bottom: 30upx;
			border-radius: 16upx;
			background-color: white;
			
			.red {
				background-image: url(../../static/pour/sta1.png);
				background-repeat: no-repeat;
				background-size: 100% 100%;
			}
			
			.green {
				background-image: url(../../static/pour/sta4.png);
				background-repeat: no-repeat;
				background-size: 100% 100%;
			}
			
			&-sta{
				position: absolute;
				right: 0px;
				width: 165upx;
				height: 165upx;
				
				view{
					position: absolute;
					transform: rotate(45deg);
					top: 40upx;
					left: 65upx;
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
	.btn {
		width: 690upx;
		margin: 0 auto;
		margin-top: 30upx;
		display: flex;
	
		.btnclass {
			margin: 0 10upx; 
			// width: 30%;
		}
	}
}
</style>
