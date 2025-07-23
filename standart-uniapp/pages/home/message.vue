<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">消息中心</block>
		</cu-custom>
		<view class="tabs">
			<scroll-view scroll-x class="nav bg-white">
				<view class="flex text-center">
					<view class="cu-item flex-sub" :class="0==TabCur?'text-blue cur':''" @tap="tabSelect" data-id="0">
						通知公告
					</view>
					<view class="cu-item flex-sub" :class="1==TabCur?'text-blue cur':''" @tap="tabSelect" data-id="1">
						系统消息
					</view>
				</view>
			</scroll-view>
		</view>
		<view v-if="TabCur==0" class="list-wrap">
			<view class="tabs-wrap" v-for="(item,index) in notice" :key="index">
				<view>标题:<span>{{item.titile}}</span></view>
				<view>发布时间:<span>{{item.sendTime}} 发布</span></view>
				<view
					>
					优先级:<span :style="item.priority =='L'?'color:green;font-weight:bold':item.priority =='M'?'color:orange;font-weight:bold':item.priority =='H'?'color:red;font-weight:bold':''">{{item.priority =='L'?'一般消息':item.priority =='M'?'重要消息':item.priority =='H'?'紧急消息':'其他消息'}}</span>
				</view>
			</view>
		</view>
		<view v-if="TabCur==1" class="list-wrap">
			<view class="tabs-wrap" v-for="(sysitem,index) in system">
				<view>标题:<span>{{sysitem.titile}}</span></view>
				<view>发布时间:<span>{{sysitem.sendTime}} 发布</span></view>
				<view
					>
					优先级:<span :style="sysitem.priority =='L'?'color:green;font-weight:bold':sysitem.priority =='M'?'color:orange;font-weight:bold':sysitem.priority =='H'?'color:red;font-weight:bold':''">{{sysitem.priority =='L'?'一般消息':sysitem.priority =='M'?'重要消息':sysitem.priority =='H'?'紧急消息':'其他消息'}}</span>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: 'message',
		data() {
			return {
				TabCur: 0,
				notice: {},
				system: {}
			}
		},
		created() {
			this.messagelist()
		},
		methods: {
			messagelist() {
				this.$http.get(`/sys/annountCement/listByUser`).then(res => {
					// console.log(res.data.result.anntMsgList, '系统信息')
					this.notice = res.data.result.anntMsgList
					this.system = res.data.result.sysMsgList
				})
			},
			tabSelect(e) {
				// console.log(e)
				this.TabCur = e.currentTarget.dataset.id;
				if (this.TabCur == 1) {
					this.messagelist();
				}
				this.scrollLeft = (e.currentTarget.dataset.id - 1) * 60
			},
		}
	}
</script>

<style lang="less" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FE;

		.tabs {
			width: 100%;
			height:105upx;
			background-color: #F2F5FE;
			// border: 1px solid gold;
		}

		.tabs-wrap {
			width: 700upx;
			border-radius: 10px;
			margin: 10px auto;
			background-color: #fff;
			padding: 20upx 10upx;
			   span{
				   color: #868789;
			   }
		}


	}
</style>
