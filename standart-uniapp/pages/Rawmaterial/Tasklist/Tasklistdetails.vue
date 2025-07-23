<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">送货任务单详情</block>
		</cu-custom>
		<view class="section">
			<view>采购人:<span>{{item.createby_dictText}}</span></view>
			<view>采购时间:<span>{{item.caeatetime}}</span></view>
			<view>备注:<span>{{item.beizhu}}</span></view>
			<view>供应商联系人:<span>{{item.lianxr}}</span></view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				item: {}
			}
		},
		onLoad(options) {
			this.item = JSON.parse(options.item)
		},
		methods: {

		}
	}
</script>

<style lang="less" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FE;

		.section {
			width: 700upx;
			height: 200upx;
			background-color: #fff;
			margin: 10px auto;
			padding: 10px 10px;
			border-radius: 10px;
			color: #6a6a6a;
			// border: 1px solid deeppink;
			font-size: 15px;
			line-height: 23px;
			
		}
	}
</style>
