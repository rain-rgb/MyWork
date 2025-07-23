<template>
	<view id="samplingdetail">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">取样详情</block>
			<!-- <view slot="right" @click="screen">筛选</view> -->
		</cu-custom>
		<view class="main">
			<view class="main-item">
				<view class="biaoqian"></view>
				<view class="mainnew">主要信息</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					试验项目名称：<span>{{loaddata.syxmmc}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					配比通知单：<span>{{loaddata.phbtzdbh}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					委托编号：<span>{{loaddata.wtbh}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					工程名称：<span>{{loaddata.gcmc}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					浇筑部位：<span>{{loaddata.sgbw}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					样品名称：<span>{{loaddata.ypmc}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					取样日期：<span>{{loaddata.qyrq}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					试验尺寸：<span>{{loaddata.chicun}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					试验组数：<span>{{loaddata.sysuliang}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					强度等级：<span>{{loaddata.qddj}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					砼的种类：<span>{{loaddata.hntzl}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					龄期：<span>{{loaddata.yplq}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					取样人：<span>{{loaddata.qyr}}</span>
				</view>
			</view>
			<view class="main-items">
				<view class="main-items-name">
					取样实图：
					
				</view>
				<view class="main-items-img grid col-4 grid-square flex-sub">
					<view class="img" v-for="(item,index) in imglist" :key="index" @tap="ViewImage" :data-url="imglist[index]">
						<image :src="imglist[index]" mode="aspectFill"></image>
					</view>
				</view>
				
			</view>
			<view class="main-item"></view>
		</view>
		<view style="height: 40upx;"></view>
	</view>
</template>

<script>
	import smartsyapi from "../../../api/smartsy.js"
	export default {
		data() {
			return {
				loaddata:'',
				imglist:[]
			};
		},
		onLoad(options) {
			this.loaddata = JSON.parse(options.item)
			this.getSampingPic()
		},
		methods:{
			ViewImage(e) {
				uni.previewImage({
					urls: this.imglist,
					current: e.currentTarget.dataset.url
				});
			},
			getSampingPic() {
				smartsyapi.samplingPic({
					params:{
						wtbh:this.loaddata.wtbh
					}
				}).then(res=>{
					console.log(res)
					if(res.data.result.length!=0){
						let imgarr = []
						let samplingPic = res.data.result[0]
						imgarr.push(samplingPic.wtpic1)
						imgarr.push(samplingPic.wtpic2)
						imgarr.push(samplingPic.wtpic3)
						imgarr.push(samplingPic.wtpic4)
						this.imglist = imgarr.filter(function(item){
							return item
						})
					}
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	#samplingdetail {
		width: 100%;
		// height: 136vh;
		background-color: #F3F5FE;

		.main {
			width: 690upx;
			margin: 0 auto;
			margin-top: 30upx;
			background-color: white;
			border-radius: 16upx;

			.mainnew {
				color: #333333;
				font-size: 30upx;
				font-weight: bold;
			}
			
			&-items {
				font-size: 28upx;
				padding-top: 30upx;
				
				&-name {
					margin-left: 30upx;
					text-align: left;
					color: #9299A8;
				}
				&-img{
					width: 90%;
					margin: 0 auto;
					margin-top: 20upx;
				}
			}
			
			&-item {
				display: flex;
				font-size: 28upx;
				padding-top: 30upx;

				&-name {
					margin-left: 30upx;
					text-align: left;
					color: #9299A8;
					
					.img{
						width: 100upx;
					}
					span {
						margin-right: 30upx;
						color: #4C5971;
					}
				}
			}
		}

		.biaoqian {
			width: 12upx;
			height: 34upx;
			border-radius: 6upx;
			margin: 0 30upx;
			background-color: #4A7FFF;
		}
	}
</style>
