<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">更多消息</block>
		</cu-custom>
		<u-loading-page :loading="loading" loading-text="加载中请稍后..."></u-loading-page>
		<view class="Rank" v-for="(item,index) in newslist" :key="index">
			<view class="Rank-all">
				<view class="Rank-all-project">
					{{item.projectName}}
				</view>
				<view class="Rank-all-excess" :style="item.overLevel_dictText =='高级超标'?'color:red;font-weight:bold;font-family: cursive;':item.overLevel_dictText=='中级超标'?'color:blue;font-weight:bold;font-family: cursive;':item.overLevel_dictText =='初级超标'?'color:orange;font-weight:bold;font-family: cursive;':item.overLevel_dictText =='合格'?'color:green;font-weight:bold;font-family: cursive;':''">
					{{item.overLevel_dictText}}
				</view>
			</view>
			<view class="Rank-describe">
				{{ item.overReason }}--{{ item.poureLocation }}
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: 'messagelook',
		data() {
			return {
				newslist: [],
				loading: true
			}
		},
		created() {
			this.getMixcblist()
		},
		methods: {
			getMixcblist() {
				let params = {
					column: 'id',
					order: 'desc',
					pageSize: 3,
					overLevel: 3
				}
				this.$http.get('/hntbhz/bhzCementBase/list', {
					params
				}).then(res => {
					this.loading = false
					console.log(res, 'yyyyyy')
					this.newslist = res.data.result.records
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	.wrap {
		width: 100%;
		height: 500upx;
		background-color: #f2f5fe;

		.Rank {
			width: 700upx;
			height: 300upx;
			margin: 10px auto;
			padding: 10px 0;
			background-color: #fff;
			border-radius: 10px;

			&-all {
				width: 700upx;
				height: auto;
				display: flex;
				flex-direction: row;
				justify-content: space-between;
				padding:0 10px;
				&-project {
					width: 570upx;
					height: 60upx;
					line-height: 60upx;
					// border: 1px solid mediumseagreen;
					white-space: normal;
					white-space: nowrap;
					overflow: hidden;
					text-overflow: ellipsis;
					// color: darkblue;
				}
				
				&-excess {
					width: 120upx;
					height: 60upx;
					line-height: 60upx;
					// color: darkcyan;
					// border: 1px solid mediumseagreen;
				}
			}
			&-describe {
				width: 700upx;
				height: auto;
				// border: 1px solid mediumseagreen;
				font-size: 30upx;
				font-weight: bold;
				padding: 30px  10px;
			}
		}
	}
</style>
