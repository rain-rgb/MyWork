<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">进场材料</block>
		</cu-custom>
		<view class="jc_material">
			<uni-table border stripe emptyText="暂无更多数据">
				<!-- 表头行 -->
				<uni-tr>
					<uni-th align="center">材料名称</uni-th>
					<uni-th align="center">规格</uni-th>
					<uni-th align="center">数量:吨</uni-th>
				</uni-tr>
				<!-- 表格数据行 -->
				<uni-tr v-for="(item,index) in jclistdata" :key="index">
					<uni-td align="center">{{item.cailiaono}}</uni-td>
					<uni-td align="center">{{item.beizhu}}</uni-td>
					<uni-td align="center">{{item.jingzhongt}}</uni-td>
				</uni-tr>

			</uni-table>
		</view>
		<view style="padding: 5px 13px;">
			<uni-pagination :total="totaljc" title="标题文字" :current="currentjc" @change="changejc" />
		</view>
		<view class="jc_material-jd">
			<h2>进度详情</h2>
		</view>
		<view class="charts-box1">
			<qiun-data-charts type="ring" :opts="opts" :chartData="chartData1"/>
		</view>
	</view>
</template>


<script>
	export default {
		data() {
			return {
				chartData1: {

				},
				jclistdata: [],
				opts: {
					rotate: false,
					rotateLock: false,
					color: ["#1890FF", "#91CB74", "#FAC858", "#EE6666", "#73C0DE", "#3CA272", "#FC8452", "#9A60B4",
						"#ea7ccc"
					],
					padding: [5, 5, 5, 5],
					dataLabel: true,
					legend: {
						show: true,
						position: "right",
						lineHeight: 25
					},
					// title: {
					// 	name: "收益率",
					// 	fontSize: 15,
					// 	color: "#666666"
					// },
					// subtitle: {
					// 	name: "70%",
					// 	fontSize: 25,
					// 	color: "#7cb5ec"
					// },
					extra: {
						ring: {
							ringWidth: 60,
							activeOpacity: 0.5,
							activeRadius: 10,
							offsetAngle: 0,
							labelWidth: 15,
							border: false,
							borderWidth: 3,
							borderColor: "#FFFFFF"
						}
					}
				},
				totaljc: 50,
				currentjc: 1,
			}
		},
		created() {},
		mounted() {
			this.gettongji()
		},
		methods: {
			gettongji() {
				let materialdata = {
					series: [{
						data: []
					}]
				}
				let current = this.currentjc
				let data = {
					pageNo: current
				}
				this.$http.get("/yclsl/wzycljinchanggb/list", {
					data
				}).then((res) => {
					console.log(res.data.result, "材料统计");
					this.jclistdata = res.data.result.records;
					this.totaljc = res.data.result.total
					this.jclistdata.forEach(item => {
						let num = Number(item.jingzhongt)
						materialdata.series[0].data.push({
							name: item.cailiaono_dictText,
							value: num
						})
					})
					this.chartData1 = JSON.parse(JSON.stringify(materialdata));
					console.log(this.chartData1)

				});
			},
			changejc(e) {
				console.log(e)
				this.currentjc = e.current
				this.gettongji(this.currentjc)
			},
		}
	}
</script>

<style lang="scss" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #f2f5fe;

		.jc_material {
			width: 700upx;
			height: auto;
			margin: 10px auto;

			&-jd {
				width: 700upx;
				height: auto;
				margin: 0 auto;

				h2 {
					padding: 0 10px;
					height: 70upx;
					line-height: 70upx;
				}

				.charts-box1 {
					width: 100%;
					height: 400upx;
				}
			}
		}
	}
</style>
