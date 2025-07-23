<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">更多资讯</block>
		</cu-custom>
		<view class="news">
		</view>
		<u-loading-page :loading="loading" loading-text="加载中请稍后..."></u-loading-page>
		<hotlist :messagelist="messagelist" :messagevideo="messagevideo" @messageclick="details">
		</hotlist>
	</view>
	</view>
</template>

<script>
	import hotlist from '@/pages/component/hotlist/hotlist.vue'
	export default {
		name: 'Seemorelist',
		components:{
			hotlist
		},
		data() {
			return {
				pageNo: 1,
				messagelist: [],
				messagevideo: [],
				loading:true,
			}
		},
		created() {
			this.bhmessage()
		},
		methods: {
			bhmessage() {
				let params = {
					column: 'id',
					order: 'desc',
					topictype: 0,
					pageNo: this.pageNo,
					PageSize: 10,
				}
				this.$http.get(`/officialfile/officialFile/list6`, {
					params
				}).then(res => {
					this.messagelist = []
					this.messagevideo = []
					console.log(res, '智慧拌合消息中心')
					let data = res.data.result.records
				    this.loading = false
					if (data.length == 0) {
						uni.showToast({
							title: "别下拉啦 没有更多数据了",
							icon: "none",
						});
					}
					// if (this.pageNo !== 1) {
					// 	this.messagelist = this.messagelist.concat(res.data.result.records);
					// } else {
					// 	this.messagelist = res.data.result.records;
					// }
					// this.messagelist = res.data.result
					data.forEach(msg => {
						console.log(msg)
						if (msg.filetype == 0) {
							this.messagelist.push(msg)
						} else if (msg.filetype == 1) {
							this.messagevideo.push(msg)
						}
					})

				})
			},
			details(m) {
				console.log(m, '信息中心')
				uni.navigateTo({
					url: '/pages/smartbh/messageDetails?item=' + JSON.stringify(m)
				})
			},
		}
	}
</script>

<style>
</style>
