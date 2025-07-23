<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">实体追溯</block>
		</cu-custom>
		<view class="header">
			<u--input placeholder="实体名称" border="surround" v-model="stname">
			</u--input>
			<view class="cuIcon-flex" @click="rightClick">
				<u-icon name="scan" size="28"></u-icon>
			</view>
		</view>
		<view class="article">
			<view class="section-text">
				<view class="title">
					<view class="round-dot">
						<view class="round"></view>
					</view>
					<view class="title-iu" v-if="show">
						<view class="title-font">分部分项工程进度(总体)</view>
					</view>
					<view class="title-iu" v-else>
						<view class="title-font">分部分项工程进度(具体)</view>
					</view>
				</view>
			</view>
		</view>
		<view class="list-item">
			<view class="tablelist">
				
			
			<uni-table stripe emptyText="暂无更多数据">
				<!-- 表头行 -->
				<uni-tr>
					<uni-th align="center" width="320">项目名称</uni-th>
					<uni-th align="center" width="320">施工部位</uni-th>
					<uni-th align="center">浇筑方式</uni-th>
					<uni-th align="center" width="150">开始生产时间</uni-th>
					<uni-th align="center" width="150">结束生产时间</uni-th>
					<uni-th align="center">强度等级</uni-th>
					<uni-th align="center" width="150">坍落度</uni-th>
					<uni-th align="center" width="150">任务方量(m³)</uni-th>
					<uni-th align="center" width="150">生产方量(m³)</uni-th>
					<uni-th align="center" width="150">进度(%)</uni-th>
				</uni-tr>
				<!-- 表格数据行 -->
				<uni-tr v-for="(item, index) in objectlist" :key="index">
					<uni-td align="center">{{ item.projectname }}</uni-td>
					<uni-td align="center">{{ item.conspos }}</uni-td>
					<uni-td align="center">{{ item.pour }}</uni-td>
					<uni-td align="center">{{ item.starttim }}</uni-td>
					<uni-td align="center">{{ item.endtim }}</uni-td>
					<uni-td align="center">{{ item.betlev }}</uni-td>
					<uni-td align="center">{{ item.lands }}</uni-td>
					<uni-td align="center">{{ item.mete }}</uni-td>
					<uni-td align="center">{{ item.metes }}</uni-td>
					<uni-td align="center">{{ item.bfb }}</uni-td>
				</uni-tr>
			</uni-table>
			</view>
		</view>
		<view class="article" v-show="nameid!==''">
			<view class="section-text">
				<view class="title">
					<view class="round-dot">
						<view class="round"></view>
					</view>
					<!-- <view class="title-iu" v-if="show">
						<view class="title-font">浇筑令(总体)</view>
					</view>
					<view class="title-iu" v-else>
						<view class="title-font">浇筑令(具体)</view>
					</view> -->
					<view class="title-iu">
						<view class="title-font">浇筑令(具体)</view>
					</view>
				</view>
			</view>
		</view>
		<view class="section" v-for="(item,index) in jzllist" :key="index">
			<!-- <navigator :url="'/pages/Entitydetection/Entitydetectiondetils?item='+JSON.stringify(item)"> -->
			<view class="section-texts">
				<view class="">工程名称:<span>{{item.projname}}</span></view>
				<view class="">开盘日期:<span>{{item.begtim}}</span></view>
				<view class="">部门名称:<span>{{item.sysOrgCode_dictText}}</span>
				</view>
				<view class="">任务单号:<span>{{item.rwdcode}}</span></view>
				<view class="">浇筑部位:<span>{{item.conspos}}</span></view>
				<view class="pourlist-item-con-name">
					<u-line-progress height="18" activeColor="#91CB74" :percentage="item.bfb"></u-line-progress>
				</view>
			</view>
			<!-- </navigator> -->
		</view>
		<view class="article">
			<view class="section-text">
				<view class="title">
					<view class="round-dot">
						<view class="round"></view>
					</view>
					<!-- <view class="title-iu" v-if="show">
						<view class="title-font">浇筑令(总体)</view>
					</view>
					<view class="title-iu" v-else>
						<view class="title-font">浇筑令(具体)</view>
					</view> -->
					<view class="title-iu">
						<view class="title-font">混泥土产能统计(总体)</view>
					</view>
				</view>
			</view>
		</view>
		<view class="charts">
			<view class="charts-box1">
				<!-- <qiun-data-charts type="pie"
				:opts="{legend:{show:false},dataLabel: false,extra:{ring:{ringWidth: 20,linearType:'custom'}}}"
				:chartData="chartsDataRing2" /> -->
				<qiun-data-charts type="pie"
					:opts="{legend:{show:false},extra:{ring:{ringWidth: 20,linearType:'custom'}}}"
					:chartData="chartsDataRing2" />
			</view>
		</view>
		<view class="article" v-show="nameid!==''">
			<view class="section-text">
				<view class="title">
					<view class="round-dot">
						<view class="round"></view>
					</view>
					<view class="title-iu">
						<view class="title-font">配料单基础信息(具体)</view>
					</view>
				</view>
			</view>
		</view>
		<view class="section" v-for="(item,index) in rwdlist" :key="index">
			<!-- <navigator :url="'/pages/Entitydetection/Entitydetectiondetils?item='+JSON.stringify(item)"> -->
			<view class="section-texts">
				<view class="">配料单:<span>{{item.code !==''&&item.code !==null?item.code:'暂无数据'}}</span></view>
				<view class="">浇筑令:<span>{{item.renwudan !==''&&item.renwudan !==null?item.renwudan:'暂无数据'}}</span>
				</view>
				<view class="">浇筑部位:<span>{{item.conspos !==''&&item.conspos !=null?item.conspos:'暂无数据'}}</span>
				</view>
				<view class="">计划方量:<span>{{item.mete !==''&&item.mete!==null?item.mete:'暂无数据'}}</span></view>
				<view class="">
					强度等级:<span>{{item.betlev_dictText !==''&& item.betlev_dictText!==null?item.betlev_dictText:'暂无数据'}}</span>
				</view>
			</view>
			<!-- </navigator> -->
		</view>
		<view class="article" v-show="nameid!==''">
			<view class="section-text">
				<view class="title">
					<view class="round-dot">
						<view class="round"></view>
					</view>
					<view class="title-iu">
						<view class="title-font">原材料批次(具体)</view>
					</view>
				</view>
			</view>
		</view>
		<view v-for="(item,index) in Materialdata" :key="index">
			<view class="section-model">
				<view class="model-img">
					<view>
						<image style="width: 45px;height: 45px;background-size: 100% 100%;"
							:src="item.jianyanstate == 0 ? imgthree: item.jianyanstate == 1?img: item.jianyanstate ==2?imgtwo:''"
							mode=""></image>
					</view>
				</view>
				<view class="model-text">
					<view class="tex-all">
						<view class="text-top">
							{{item.pici}}
						</view>
						<view class="text-bottom">
							{{item.guigexinghao}}
						</view>
					</view>
				</view>
				<u-icon @click="clickJump(item)" name="arrow-right" color="#c0c0c1" size="18"></u-icon>
			</view>
		</view>
	</view>
	</view>
</template>

<script>
	import api from "../../api/smartbh.js"
	export default {
		name: "Entitytracing",
		data() {
			return {
				stname: "",
				objectlist: {},
				pageNo: 1,
				nameid: '',
				show: true,
				jzllist: {},
				rwdlist: {},
				img: '../../static/shiti/bhgy.png',
				imgtwo: '../../static/shiti/yihg.png',
				imgthree: '../../static/shiti/wjcy.png',
				Materialdata: {},
				chartsDataRing2: {
					series: [{
						data: []
					}]
				},
			};
		},
		created() {
			this.fbfxdata();
		},
		mounted() {
			this.getbhzcapacity()
		},
		methods: {
			fbfxdata() {
				this.$http
					.get(`/renwudan/renwudanSchedule/list`, {
						params: {
							column: "id",
							order: "desc",
							conspos: this.nameid
						},
					})
					.then((res) => {
						if (res.data.result.records.length == 0) {
							uni.showToast({
								title: "别下拉啦 没有更多数据了",
								icon: "none",
								pageNo: this.pageNo,
								PageSize: 10,
							});
						}
						if (this.pageNo != 1) {
							this.objectlist = this.objectlist.concat(res.data.result.records);
						} else {
							this.objectlist = res.data.result.records;
						}
					});
			},
			jzldata() {
				this.$http.get(`/bhzrwdxx/bhzrwdxx/list`, {
					params: {
						conspos: this.nameid
					}
				}).then(res => {
					console.log(res.data.result.records, '浇筑令管理')
					this.jzllist = res.data.result.records
				})
			},
			renwudanlist() {
				this.$http.get(`/system/shigongphb/list5`, {
					params: {
						renwudan: this.jzllist.rwdcode
					}
				}).then(res => {
					console.log(res.data.result.records, '配料单数据')
					this.rwdlist = res.data.result.records
					//原材料批次数据
					this.Materialdata = res.data.data
					console.log(this.Materialdata, '原材料批次数据')

				})
			},
			//产能统计
			getServerData(e) {
				setTimeout(() => {
					//饼图格式化示例
					//使用format属性指定config-ucharts.js里事先定义好的formatter的key值，组件会自动匹配与其对应的function 
					let pieFormatDemo = JSON.parse(JSON.stringify(e))
					// console.log(pieFormatDemo)
					for (var i = 0; i < pieFormatDemo.series.length; i++) {
						pieFormatDemo.series[i].format = "pieDemo"
					}
					// console.log(pieFormatDemo)
					this.chartsDataRing2 = pieFormatDemo

				}, 1500);
			},
			getbhzcapacity() {
				api.bhzcapacity().then(res => {
					if (res.data.result.length > 0) {
						// console.log("resM", res)
						let data = res.data.result;
						let datas = []
						data.forEach(function(item, index) {
							console.log(item)
							if (item.estimateNumber > 1000) {
								let num = parseFloat(item.estimateNumber.toFixed(2))
								datas.push({
									name: item.strengthRank,
									value: num
								})
							}
							// that.mix.categories.push(item.projectName);
							// that.mix.series[0].data.push(item.estimateNumber);
						})
						this.chartsDataRing2.series[0].data = datas
						// console.log(datas)
						this.getServerData(this.chartsDataRing2)
						//console.log("this.chartData++", that.chartData)
					} else {
						uni.showToast({
							title: "暂无数据",
							icon: 'none'
						})
					}
				})
			},
			clickJump(e) {
				// let id = id
				uni.navigateTo({
					url: "/pages/Rawmaterial/Statisticdetails?item=" + JSON.stringify(e),
				})
			},
			rightClick() {
				var that = this;
				// 允许从相机和相册扫码
				uni.scanCode({
					success: (res) => {
						console.log("条码类型：" + res.scanType);
						console.log("条码内容：" + res.result);
						that.nameid = res.result;
						that.show = false
						// uni.navigateTo({
						// 	url: "./caroperation?id=" + that.id,
						// });
						that.fbfxdata()
						that.jzldata()
						that.renwudanlist()
					}
				});

			},
		}
	};
</script>

<style lang="scss" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #f2f5fe;

		.header {
			width: 700upx;
			margin: 10px auto;
			// border: 1px solid #18BC37;

			.u-input {
				width: 640upx;
				background-color: #fff;
			}

			.cuIcon-flex {
				float: right;
				margin-top: -33px;
			}
		}

		.article {
			width: 700upx;
			height: auto;
			background-color: #fff;
			margin: 10px auto;
			border-radius: 4px;

			.section-text {
				width: 700upx;
				height: auto;

				display: flex;
				flex-direction: row;
				justify-content: space-between;
				flex: 1;

				.title {
					width: 490rpx;
					height: 25px;
					// border: 1px solid #18BC37;
					display: flex;
					flex-direction: row;
					justify-content: space-around;
					align-items: center;

					.round-dot {
						flex: 2;

						.round {
							width: 6px;
							height: 17px;
							background: #387ffd;
							border-radius: 6px;
							margin: 0 auto;
						}
					}

					.title-iu {
						flex: 12;

						.title-font {
							font-size: 18px;
							font-family: " PingFang SC";
							font-weight: bold;
							color: #333333;
						}
					}
				}
			}

		}

		.list-item {
			width: 700upx;
			height: auto;
			border-radius: 10px;
			margin: 0 auto;
			background-color: #fff;
			// border: 1px solid #1890FF;
			   .tablelist {
			   	width: 90%;
			   	height: auto;
			   	border-radius: 10px;
			   	// overflow-y: auto;
			   	margin: 0 auto;
			   	// border: 1px solid #ffaaff;
			   
			   }
			.list-item-data {
				color: #9299a8;
				font-size: 30upx;
				padding: 15px 15px;
				line-height: 55upx;
				margin: 10px 0;

				span {
					color: #4c5971;
					font-size: 30upx;
					font-family: "PingFang-SC-Medium";
					padding: 0 15upx;
				}
			}
		}

		.section {
			width: 700upx;
			height: auto;
			border-radius: 10px;
			margin: 0 auto;
			background-color: #fff;

			.section-texts {
				color: #9299A8;
				font-size: 30upx;
				padding: 15px 15px;
				line-height: 55upx;
				margin: 10px 0;

				span {
					color: #4C5971;
					font-size: 30upx;
					font-family: 'PingFang-SC-Medium';
					padding: 0 15upx;
				}
			}
		}

		.charts {
			width: 100%;
			height: 900upx;

			// border:1px solid #1785FF;
			.charts-box1 {
				height: 900upx;
				// border:1px solid #55aa7f;
			}
		}

		.section-model {
			width: 670upx;
			height: 70px;
			margin: 10px auto;
			border-radius: 4px;
			background-color: #F6F7FA;
			display: flex;
			flex-direction: row;
			justify-content: space-around;
			align-items: center;

			.model-img {
				width: 45px;
				height: 45px;
				// border: 1px solid #1CBBB4;
			}

			.model-text {
				width: 470upx;
				height: 42px;
				// border: 1px solid #F1A532;

				.tex-all {
					width: 470upx;
					height: 25px;
					line-height: 25px;

					.text-top {
						font-size: 15px;
						font-family: PingFang SC;
						font-weight: bold;
						color: #333333;
					}

					.text-bottom {
						color: #dcdddf;

					}
				}

			}
		}

	}
</style>
