<template>
	<view class="wrap">
		<!-- <cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">环保监测</block>
		</cu-custom> -->
		<!-- <view class="Envbox">
			<h2>环境监测设备</h2>
			<view class="charts-box">
				<qiun-data-charts type="gauge" :opts="opts" :chartData="chartData" />
			</view>
		</view>
		<view class="Envdt">
			<h2>环境监测数据</h2>
			<view class="Envdt-list" v-for="(item,index) in envdata" :key="index">
				<view class="Envdt-list-monitor">PM2.5:<span>{{item.pm25}}(ug/m&sup3)</span></view>
				<view class="Envdt-list-monitor">PM10:<span>{{item.pm10}}(ug/m&sup3)</span></view>
				<view class="Envdt-list-monitor">温度:<span>{{item.temperature}}&#8451</span></view>
				<view class="Envdt-list-monitor">湿度:<span>{{item.humidity}}%</span></view>
				<view class="Envdt-list-monitor">噪声:<span>{{item.noise}}dB</span></view>
			</view>
		</view> -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
				envdata:[],
				chartData: {
					
					
				},
				//您可以通过修改 config-ucharts.js 文件中下标为 ['gauge'] 的节点来配置全局默认参数，如都是默认参数，此处可以不传 opts 。实际应用过程中 opts 只需传入与全局默认参数中不一致的【某一个属性】即可实现同类型的图表显示不同的样式，达到页面简洁的需求。
				opts: {
					color: ["#1890FF", "#91CB74", "#FAC858", "#EE6666", "#73C0DE", "#3CA272", "#FC8452", "#9A60B4",
						"#ea7ccc"
					],
					padding: undefined,
					title: {
						name: "60Km/H",
						fontSize: 25,
						color: "#2fc25b",
						offsetY: 50
					},
					subtitle: {
						name: "实时速度",
						fontSize: 15,
						color: "#666666",
						offsetY: -50
					},
					extra: {
						gauge: {
							type: "default",
							width: 30,
							labelColor: "#666666",
							startAngle: 0.75,
							endAngle: 0.25,
							startNumber: 0,
							endNumber: 100,
							labelFormat: "",
							splitLine: {
								fixRadius: 0,
								splitNumber: 10,
								width: 30,
								color: "#FFFFFF",
								childNumber: 5,
								childWidth: 12
							},
							pointer: {
								width: 24,
								color: "auto"
							}
						}
					}
				}
			}
		},
		onReady() {
			this.getServerData();
		},
		created() {
			this.envlist()
		},
		methods: {
		  envlist(){
			  this.$http.get(`devicehistory/devicehistory/lists`).then(res => {
				  // console.log(res.data.result.records)
				  this.envdata = res.data.result.records
			  })
		  },
			getServerData() {
				//模拟从服务器获取数据时的延时
				setTimeout(() => {
					//模拟服务器返回数据，如果数据格式和标准格式不同，需自行按下面的格式拼接
					let res = {
						categories: [{
							"value": 0.2,
							"color": "#1890ff"
						}, {
							"value": 0.8,
							"color": "#2fc25b"
						}, {
							"value": 1,
							"color": "#f04864"
						}],
						series: [{
							name: "完成率",
							data: 0.51
						}]
					};
					this.chartData = JSON.parse(JSON.stringify(res));
				}, 500);
			},
		}
	}
</script>


<style lang="scss" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #f2f5fe;

		.Envbox {
			width: 700upx;
			height: auto;
                 // border: 1px solid cornflowerblue;
				 margin: 10 auto;
			h2 {
				padding: 10px 10px;

			}

			.charts-box {
				width: 70%;
				height: 300px;
				margin: 0 auto;
			}
		}

		.Envdt {
			width: 700upx;
			height: auto;
            // border: 1px solid forestgreen;
			margin: 0 auto;
			h2 {
				padding: 0 10px;
			}

			&-list {
				width: 700upx;
				height: auto;
				background-color:#fff;
				border-radius: 10px;
                margin: 10px 0;
				&-monitor {
					padding: 5px 15px;
					font-size: 15px;
					color: #000;
					font-weight: bold;
                    // border: 1px solid greenyellow;
					span {
						color: #878887;
						font-size: 15px;
						font-family: 'PingFang-SC-Medium';
						padding: 0 10px;
						font-weight: normal;
					}
				} 	 	      
			}
		}
	}
</style>
