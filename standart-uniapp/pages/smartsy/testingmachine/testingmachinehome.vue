<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">试验机</block>
		</cu-custom>
		<view class="titletop">
			<view class="u-demo-block__content">
				<u-row justify="space-between" gutter="10">
					<u-col span="6">
						<view class="demo-layout bg-purple-light" @click="Testclick(0)">
							<view class="top-item-img"
								style="background-image: url(../../../static/smartsy/machine.png);"></view>
							<view class="top-item-name">
								<view>万能机</view>
							</view>
						</view>
					</u-col>
					<u-col span="6">
						<view class="demo-layout bg-purple-light" @click="Testclick(1)">
							<view class="top-item-img"
								style="background-image: url(../../../static/smartsy/machine1.png);"></view>
							<view class="top-item-name">
								<view>压力机</view>
							</view>
						</view>
					</u-col>
				</u-row>
				<u-row justify="space-between" gutter="10" class="magentop">
					<u-col span="6">
						<view class="demo-layout bg-purple-light" @click="Testclick(2)">
							<view class="top-item-img"
								style="background-image: url(../../../static/smartsy/machine2.png);"></view>
							<view class="top-item-name">
								<view>标养室</view>
							</view>
						</view>
					</u-col>
					<u-col span="6">
						<view class="demo-layout bg-purple-light" @click="Testclick(3)">
							<view class="top-item-img"
								style="background-image: url(../../../static/smartsy/machine3.png);"></view>
							<view class="top-item-name">
								<view>抗压抗折</view>
							</view>
						</view>
					</u-col>
				</u-row>
			</view>
		</view>
		<view class="titlecenter">
			<view style="display: flex;">
				<view class="biaoqian"></view>
				<text>检测结果</text>
			</view>

			<view class="charts-box">
				<qiun-data-charts type="ring" :opts="opts" :chartData="chartData" background="none"
					@getIndex="getdata" />
			</view>
			<uni-table stripe emptyText="暂无更多数据">
				<!-- 表头行 -->
				<uni-tr>
					<uni-th width="100upx" align="center"></uni-th>
					<uni-th align="center">总数</uni-th>
					<uni-th align="center">合格</uni-th>
					<uni-th align="center">不合格</uni-th>
				</uni-tr>
				<!-- 表格数据行 -->
				<uni-tr>
					<uni-td>万能机</uni-td>
					<uni-td align="center">{{lists.wnjsums}}</uni-td>
					<uni-td align="center">{{lists.wnjsumstrue}}</uni-td>
					<uni-td align="center">{{lists.wnjsumsfalse}}</uni-td>
				</uni-tr>
				<uni-tr>
					<uni-td>压力机</uni-td>
					<uni-td align="center">{{lists.yljsums}}</uni-td>
					<uni-td align="center">{{lists.yljsumstrue}}</uni-td>
					<uni-td align="center">{{lists.yljsumsfalse}}</uni-td>
				</uni-tr>
				<uni-tr>
					<uni-td>标养室</uni-td>
					<uni-td align="center">{{lists.byssums}}</uni-td>
					<uni-td align="center">{{lists.byssumstrue}}</uni-td>
					<uni-td align="center">{{lists.byssumsfalse}}</uni-td>
				</uni-tr>
				<uni-tr>
					<uni-td>抗压抗折</uni-td>
					<uni-td align="center">{{lists.kykzsums}}</uni-td>
					<uni-td align="center">{{lists.kykzsumstrue}}</uni-td>
					<uni-td align="center">{{lists.kykzsumsfalse}}</uni-td>
				</uni-tr>

			</uni-table>
		</view>
		<view class="titleboween">
			<view style="display: flex;">
				<view class="biaoqian"></view>
				<text>预警闭合统计</text>
			</view>
			<view class="charts-box1">
				<!-- <qiun-data-charts type="column" :chartData="chartData1" background="none"
					:opts="{legend:{position: 'top',float:'right'}}" /> -->
			</view>

		</view>
	</view>
</template>

<script>
	import testinterface from '@/api/testinterface.js'
	export default {
		data() {
			return {
				num: 0,
				title: "总数",
				chartData: {
					categories: [],
					"series": [{
						"data": [],
					}]
				},
				opts: {
					"legend": {
						"position": 'right'
					},
					"title": {
						"name": '0',
						"fontSize": 25,
						"color": "#000000",
						"offsetX": 0,
						"offsetY": 0
					},
					"dataLabel": false,
					"extra": {
						"ring": {
							"ringWidth": 10,
							"linearType": 'custom'
						}
					},
					"subtitle": {
						"name": '总量',
						"fontSize": 15,
						"color": "#666666",
						"offsetX": 0,
						"offsetY": 0
					}
				},
				chartData1: {
					"categories": [
						"压力机",
						"万能机",
						"标养室",
						"抗压抗折"
					],
					"series": [{
							"name": "超标率",
							"data": [
								35,
								36,
								31,
								33
							]
						},
						{
							"name": "处置率",
							"data": [
								18,
								27,
								21,
								24
							]
						}

					]
				},
				lists: {
					wnjsums: 0,
					wnjsumstrue: 0,
					wnjsumsfalse: 0,

					yljsums: 0,
					yljsumstrue: 0,
					yljsumsfalse: 0,

					byssums: 0,
					byssumstrue: 0,
					byssumsfalse: 0,

					kykzsums: 0,
					kykzsumstrue: 0,
					kykzsumsfalse: 0,

				},

			};
		},
		created() {
			this.testStatics();
		},
		mounted() {
			// this.opts.subtitle.name = "总量"
		},
		methods: {
			testStatics() {
				testinterface.syjStatisc().then(res => {
					console.log(res)
					if (res.data.success) {
						var data = res.data.result;
						this.lists.byssums = data.bys.sums;
						this.lists.byssumstrue = data.bys.sumtrue;
						this.lists.byssumsfalse = data.bys.sumsfalse;
						this.lists.kykzsums = data.kykz.sums;
						this.lists.kykzsumstrue = data.kykz.sumtrue;
						this.lists.kykzsumsfalse = data.kykz.sumsfalse;
						this.lists.yljsums = data.ylj.sums;
						this.lists.yljsumstrue = data.ylj.sumtrue;
						this.lists.yljsumsfalse = data.ylj.sumsfalse;
						this.lists.wnjsums = data.wnj.sums;
						this.lists.wnjsumstrue = data.wnj.sumtrue;
						this.lists.wnjsumsfalse = data.wnj.sumsfalse;
						this.opts.title.name = data.bys.sums + data.kykz.sums + data.ylj.sums + data.wnj.sums;
						this.chartData.series[0].data.push({
							name: '压力机',
							value: data.ylj.sums
						}, {
							name: '万能机',
							value: data.wnj.sums
						}, {
							name: '标养室',
							value: data.bys.sums
						}, {
							name: '抗压抗折',
							value: data.kykz.sums
						})
					}
				})
			},
			getdata(e) {
				let datac = e.opts.series[e.legendIndex].data;
				// var da=this.opts.title.name-datac;
				// this.opts.title.name=da
				//this.opts.title.name=this.opts.title.name-datac;

				console.log("datac", this.datac)
				console.log("e", e)
			},
			Testclick(e) {
				if (e == 0) {
					uni.navigateTo({
						url: '/pages/Testdetection/Testwnj'
					})
				}
				if (e == 1) {
					uni.navigateTo({
						url: '/pages/Testdetection/Testylj',
					})
				}
				if (e == 2) {
					uni.navigateTo({
						url: '/pages/Testdetection/Testdetection'
					})
				}
				if (e == 3) {
					uni.navigateTo({
						url: '/pages/Testdetection/Testkykz'
					})
				}
			},
		}

	}
</script>

<style lang="scss" scoped>
	/* 请根据需求修改图表容器尺寸，如果父容器没有高度图表则会显示异常 */
	.wrap{
		width: 100%;
		height:auto;
		background-color: #F2F5FE;
	}
	.charts-box {
		width: 100%;
		height: 400upx;
	}

	.charts-box1 {
		margin-top: 30upx;
		width: 100%;
		height: 500upx;
	}

	.biaoqian {
		width: 12upx;
		height: 34upx;
		border-radius: 6upx;
		margin: 0 30upx;
		background-color: #4A7FFF;
	}

	.magentop {
		margin-top: 20upx;
	}

	.demo-layout {
		height: 130upx;
		border-radius: 16upx;
	}

	.bg-purple-light {
		background: rgba(124, 160, 255, 0.1);
		display: flex;
		justify-content: space-evenly;
		align-items: center;

		.top-item-img {
			width: 75upx;
			height: 75upx;
			background-repeat: no-repeat;
			background-size: 100% 100%;
			margin-left: 10upx;
			margin-right: 1upx;
		}

		.top-item {
			font-size: 28upx;
			color: #4C5971;
			// margin-top: 24upx;
		}

	}

	.titletop {
		padding: 30upx;
		width: 690upx;
		// height: 520upx;
		margin: 0 auto;
		margin-top: 30upx;
		border-radius: 16upx;
		background-color: #FFFFFF;
	}

	.titlecenter {
		padding: 30upx;
		width: 690upx;
		// height: 520upx;
		margin: 0 auto;
		margin-top: 30upx;
		border-radius: 16upx;
		background-color: #FFFFFF;
	}

	.titleboween {
		padding: 30upx;
		width: 690upx;
		// height: 520upx;
		margin: 0 auto;
		margin-top: 30upx;
		border-radius: 16upx;
		background-color: #FFFFFF;
	}
</style>
