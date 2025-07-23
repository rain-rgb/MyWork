<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">压浆详情信息</block>
			<!-- <view slot="right" @click="screen">筛选</view> -->
		</cu-custom>
		<view class="pourstas">
			<view class="borderlist">
				<uni-table stripe emptyText="暂无更多数据">
					<uni-tr>
						<uni-th width="150" align="center">压浆时间</uni-th>
						<uni-th align="center">孔道</uni-th>
						<uni-th width="120" align="center">配合比</uni-th>
						<uni-th width="90" align="center">水胶比</uni-th>
						<uni-th align="center">搅拌时间</uni-th>
						<uni-th width="110" align="center">压浆次数</uni-th>
						<uni-th width="110" align="center">是否合格</uni-th>
					</uni-tr>
					<uni-tr v-for="(tablelist,index) in materilstable" :key="index">
						<uni-td align="center"><span>{{tablelist.yajiangsj}}</span></uni-td>
						<uni-td align="center">{{tablelist.kongdao}}</uni-td>
						<uni-td align="center">{{tablelist.peihebi}}</uni-td>
						<uni-td align="center"><span>{{tablelist.shuijiaobi}}</span></uni-td>
						<uni-td align="center">{{tablelist.jiaobansj}}</uni-td>
						<uni-td align="center">{{tablelist.yjcs}}</uni-td>
						<uni-td align="center" :style="tablelist.hege ==1?'color:green;font-weight:bold':tablelist.hege ==0?'color:red;font-weight:bold':'color:blue;font-weight:bold'">{{tablelist.hege ==0?'不合格':tablelist.hege ==1?'合格':'暂无数据'}}</uni-td>
					</uni-tr>

				</uni-table>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: 'Yjdetails',
		data() {
			return {
				syjid: "",
				materilstable: {}
			}
		},
		onLoad(options) {
			this.syjid = options.item
		},
		created() {
			this.Ldata()
		},
		methods: {
			Ldata() {
				this.$http.get(`/yajiangs/trYajiangS/list1`, {
					params: {
						syjid: this.syjid
					}
				}).then(res => {
					console.log(res, '梁信息详情')
					this.materilstable = res.data.result
				})
			}
		}
	}
</script>

<style lang="less" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FE;

		.borderlist {
			border-radius: 10px;
			width: 94%;
			margin: 0 auto;
		}

		.pourstas {
			width: 690upx;
			height: auto;
			margin: 0 auto;
			margin-top: 30upx;
			border-radius: 16upx;
			background-color: #FFFFFF;
		}
	}
</style>
