<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">张拉详情信息</block>
			<!-- <view slot="right" @click="screen">筛选</view> -->
		</cu-custom>
		<view class="pourstas">
			<!-- <view class="poursta-title">材料使用情况</view> -->
			<view class="borderlist">
				<uni-table stripe emptyText="暂无更多数据">
					<!-- 表头行 -->
					<uni-tr>
						<uni-th width="90" align="center">孔号</uni-th>
						<uni-th align="center">张拉断面</uni-th>
						<uni-th width="110" align="center">阶段行程</uni-th>
						<uni-th width="90" align="center">张拉力</uni-th>
						<uni-th align="center">设计张力</uni-th>
						<uni-th width="110" align="center">超张百分比</uni-th>
					</uni-tr>
					<!-- 表格数据行 -->
					<uni-tr v-for="(tablelist,index) in materilstable" :key="index">
						<uni-td align="center"><span>{{tablelist.gsbh}}</span></uni-td>
						<uni-td align="center">
							<view v-for="(table,index) in tablelist.trZhanglaSList" :key="index">
								{{table.dh}}
							</view>
						</uni-td>
						<uni-td align="center">
							<view v-for="(table,index) in tablelist.trZhanglaSList" :key="index">
								{{table.jdbfb}}
							</view>
						</uni-td>
						<uni-td align="center">
							<view v-for="(table,index) in tablelist.trZhanglaSList" :key="index">
								{{table.jdzll}}
							</view>
						</uni-td>
						<uni-td align="center">{{tablelist.sjzll}}</uni-td>
						<uni-td align="center">{{tablelist.zzlb}}</uni-td>
					</uni-tr>


				</uni-table>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: 'Zldetails',
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
				this.$http.get(`/zhanglam/trZhanglaM/list1`, {
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
