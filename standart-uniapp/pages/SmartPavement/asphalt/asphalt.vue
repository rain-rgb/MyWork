<template>
	<view id="smartbh">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">沥青当日工作状态</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>

		<!-- 次级页面标签 -->
		<view class="tops">
			<view class="top-twos">
				<view class="lefts">
					<view class="left-texts">
						<p class="orange">{{ lqdata.middle_dish }} 个</p>
						中级超标盘数
					</view>
				</view>
				<view class="lefts" @click="change(0)">
					<view class="left-texts">
						<p class="green">{{ lqdata.all_dish}} 个</p>
						总盘数
					</view>
				</view>
				<view class="lefts" @click="change(1)">
					<view class="left-texts">
						<p class="red">{{ lqdata.estimate_number}} Kg</p>
						总产能
					</view>
				</view>
			</view>
			<view class="top-twos">
				<view class="lefts">
					<view class="left-texts">
						<p class="purple">{{ lqdata.primary_dish }} 个</p>
						初级超标盘数
					</view>
				</view>
				<view class="lefts">
					<view class="left-texts">
						<p class="blue">{{ lqdata.all_overproof_dish}} 个</p>
						总超标盘数
					</view>
				</view>
				<view class="lefts">
					<view class="left-texts">
						<p class="black">{{ lqdata.advanced_dish }} 个</p>
						高级超标盘数
					</view>
				</view>
			</view>
		</view>
		<view class="top">
			<view class="top-item" @click="handleNav(0)">
				<view class="top-item-img" style="background-image:url(../../../static/pour/tag1.png);"></view>
				<view class="top-item-name">沥青原材</view>
			</view>
			<view class="top-item" @click="handleNav(1)">
				<view class="top-item-img" style="background-image: url(../../../static/pour/tag2.png);"></view>
				<view class="top-item-name">沥青检测</view>
			</view>
			<view class="top-item" @click="handleNav(2)">
				<view class="top-item-img" style="background-image: url(../../../static/pour/tag3.png);"></view>
				<view class="top-item-name">沥青拌合</view>
			</view>
			<view class="top-item" @click="handleNav(3)">
				<view class="top-item-img" style="background-image: url(../../../static/pour/tag4.png);"></view>
				<view class="top-item-name">沥青运输</view>
			</view>
			<view class="top-item" @click="handleNav(4)">
				<view class="top-item-img" style="background-image: url(../../../static/pour/tag5.png);"></view>
				<view class="top-item-name">沥青摊铺</view>
			</view>
			<view class="top-item" @click="handleNav(5)">
				<view class="top-item-img" style="background-image: url(../../../static/pour/tag6.png);"></view>
				<view class="top-item-name">沥青碾压</view>
			</view>
		</view>
		<!-- 消息中心 -->
		<view class="news">
			<view class="news-title">
				<view class="news-title-name">消息中心</view>
				<view class="news-title-more"> <span>查看更多</span>
					<u-icon color="#999999" size="12" name="arrow-right"></u-icon>
				</view>
			</view>
			<view class="news-con" v-if="messagenum.length>0">
				<view class="news-con-item" v-for="(k,index) in messagenum" :key="index">
					<view class="news-con-item-but">精选</view>
					<view class="news-con-item-des">{{k.content}}</view>
				</view>
			</view>
			<view class="news-con" v-else>
				<view class="news-con-item">
					<u-notice-bar bgColor="#fff" fontSize=16 color="#91CB74" text="暂无新消息提醒"></u-notice-bar>
				</view>
				<view class="tiptitle"></view>
			</view>
		</view>
		<!-- 今日在线 -->
		<view class="tabcardall">
			<view class="tabcardall-title">今日在线</view>
			<view class="tabcard">
				<view class="tabcard-left">
					<view class="tabcard-left-title">
						水稳站:0
					</view>
				</view>
				<view class="tabcard-left">
					<view class="tabcard-left-title">
						地磅:0
					</view>
				</view>
				<view class="tabcard-left">
					<view class="tabcard-left-title">
						运输车:0
					</view>
				</view>
				<view class="tabcard-left">
					<view class="tabcard-left-title">
						摊铺机:0
					</view>
				</view>
				<view class="tabcard-left">
					<view class="tabcard-left-title">
						压路机:0
					</view>
				</view>
				<view class="tabcard-left">
					<view class="tabcard-left-title">
						环境仪:0
					</view>
				</view>
			</view>
		</view>
		<!-- 当日进场原材料 -->
		<view class="poursta">
			<view class="poursta-title">当日进场原材料</view>
			<view class="borderlist">
				<uni-table stripe emptyText="暂无更多数据">
					<!-- 表头行 -->
					<uni-tr>
						<uni-th width="90" align="center">规格类型</uni-th>
						<uni-th align="center">重量</uni-th>
						<uni-th width="110" align="center">车次数</uni-th>
					</uni-tr>
					<!-- 表格数据行 -->
					<uni-tr v-for="(item,index) in rawMaterial" :key="index">
						<uni-td align="center">{{item.sheibei}}</uni-td>
						<uni-td align="center">{{item.mileage}}</uni-td>
						<uni-td align="center">{{item.speed}}</uni-td>
					</uni-tr>


				</uni-table>
			</view>
		</view>
		<!-- 当日水稳拌合 -->
		<view class="poursta">
			<view class="poursta-title">当日沥青拌合</view>
			<view class="borderlist">
				<uni-table stripe emptyText="暂无更多数据">
					<!-- 表头行 -->
					<uni-tr>
						<uni-th width="90" align="center">类型</uni-th>
						<uni-th align="center">重量</uni-th>
						<uni-th width="110" align="center">盘数</uni-th>
						<uni-th width="110" align="center">超标率</uni-th>
					</uni-tr>
					<!-- 表格数据行 -->
					<uni-tr v-for="(item,index) in asphaltMixing" :key="index">
						<uni-td align="center">{{item.sheibei}}</uni-td>
						<uni-td align="center">{{item.mileage}}</uni-td>
						<uni-td align="center">{{item.speed}}</uni-td>
						<uni-td align="center">{{item.tem}}</uni-td>
					</uni-tr>


				</uni-table>
			</view>
		</view>
		<!-- 当日摊铺碾压 -->
		<view class="poursta">
			<view class="poursta-title">当日摊铺碾压</view>
			<view class="borderlist">
				<uni-table stripe emptyText="暂无更多数据">
					<!-- 表头行 -->
					<uni-tr>
						<uni-th width="90" align="center">设备编号</uni-th>
						<uni-th align="center">行驶里程</uni-th>
						<uni-th width="110" align="center">实时速度</uni-th>
						<uni-th width="110" align="center">实时温度</uni-th>
					</uni-tr>
					<!-- 表格数据行 -->
					<uni-tr v-for="(item,index) in Pavinglist" :key="index">
						<uni-td align="center">{{item.sheibei}}</uni-td>
						<uni-td align="center">{{item.mileage}}</uni-td>
						<uni-td align="center">{{item.speed}}</uni-td>
						<uni-td align="center">{{item.tem}}</uni-td>
					</uni-tr>


				</uni-table>
			</view>
		</view>
		<!-- 热门活动 -->
		<view class="news">
			<view class="news-title">
				<view class="news-title-name">消息中心</view>
				<view class="news-title-more"> <span>查看更多</span>
					<u-icon color="#999999" size="12" name="arrow-right"></u-icon>
				</view>
			</view>
			<view class="news-activity">
				<view class="news-activity-img"></view>
				<view class="news-activity-con">
					<view class="news-activity-con-title">多层面多维度谋划未来产展</view>
					<view class="news-activity-con-text">未来产业是以新一代信息技术、新材料、新能源、新装备等与现...</view>
					<view class="news-activity-con-time">
						<text>200</text>
						<text style="float: right; margin-right: 14upx;">2020-10-20</text>
					</view>
				</view>

			</view>
			<view class="news-activity">
				<view class="news-activity-img"></view>
				<view class="news-activity-con">
					<view class="news-activity-con-title">多层面多维度谋划未来产展</view>
					<view class="news-activity-con-text">未来产业是以新一代信息技术、新材料、新能源、新装备等与现...</view>
					<view class="news-activity-con-time">
						<text>200</text>
						<text style="float: right; margin-right: 14upx;">2020-10-20</text>
					</view>
				</view>

			</view>

			<view class="news-video">
				<view class="news-video-img"></view>
				<view class="news-video-title">多层面多维度谋划未来产展</view>
				<view class="news-video-time">
					<text>200</text>
					<text style="float: right; margin-right: 14upx;">2020-10-20</text>
				</view>
			</view>

		</view>
		<!--  -->
		<view style="height: 150upx;" class=""></view>
		<u-modal :show="show" showCancelButton @cancel="cancel" @confirm="confirm">
			<view class="boxmodal">
					<view class="boxmodal-time">时间：</view>
					<view class="screen-modal-item-input" @click="dateshow = true">
						<u--input placeholder="请选择时间" border="surround" v-model="datetimevalue" disabled
							suffixIcon="arrow-down"></u--input>
					</view>
					<view class="boxmodal-type">混合类型：</view>
						<dict dictCode="hhllx" title="请选择混合类型" @choose="Choose1">
						</dict>
			</view>

		</u-modal>
		<mx-date-picker :show="dateshow" :type="type" :show-tips="true" :show-seconds="true" @confirm="confirmdate"
			@cancel="confirmdate" />
	</view>
</template>

<script>
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	import dict from '@/pages/component/dict/theory.vue'
	export default {
		components:{
			MxDatePicker,
			dict
		},
		data() {
			return {
				messagenum: {},
				asphaltMixing:[],//沥青拌合
				rawMaterial:[],//当日进场原材料
				Pavinglist: [
					// {
					// 	sheibei: '一号压路机',
					// 	mileage: 110,
					// 	speed: '0.8M/min',
					// 	tem: 23
					// },
					// {
					// 	sheibei: '三号压路机',
					// 	mileage: 120,
					// 	speed: '0.3M/min',
					// 	tem: 20
					// },
					// {
					// 	sheibei: '五号压路机',
					// 	mileage: 130,
					// 	speed: '0.5M/min',
					// 	tem: 24
					// },
				],
				show: false,
				type: 'range',
				dateshow:false,
				datetimevalue: '',
				datatime_begin:null,
				datatime_end:null,
				hhtype:'',
				lqdata:{}
			}

		},
		mounted() {

		},
		created() {
			this.counttitle()
		},
		methods: {
			handleNav(e) {
				if (e == 2) {
					uni.navigateTo({
						url: '/pages/SmartPavement/asphalt/asphaltmix'
					})
				}
			},
			counttitle() {
				let params = {
                  statisticsTime_begin:this.datatime_begin,
				  statisticsTime_end:this.datatime_end,
				  strengthRank:this.hhtype
				}
				this.$http.get(`/lqbhzStatistics/bhzLqStatistics/liststaData`,{params}).then(res => {
					// console.log(res.data.result, '沥青标签统计接口')
					this.lqdata = res.data.result
				})
			},
			screen() {
				this.show = true
			},
			cancel(){
				this.datetimevalue = '',
				this.hhtype = '',
				this.show = false
			},
			confirm(){
				this.counttitle()
				this.datetimevalue = '',
				this.hhtype = '',
				this.show = false
			},
			confirmdate(e) {
				console.log(e, '确认时间')
				// this.formData.starttime = e.value
				if (e) {
					// console.log(e)
					this.datetimevalue = e.value.toString()
					// console.log(this.datetimevalue)
					this.datatime_begin = e.value[0] 
					// console.log(this.datatime_begin)
					this.datatime_end = e.value[1] 
					// console.log(this.datatime_end)
				}
				this.dateshow = false
			},
			Choose1(e){
				// console.log(e)
				this.hhtype = e
			}
		}
	}
</script>

<style lang="scss" scoped>
	#smartbh {
		width: 100%;
		height: auto;

		.tops {
			width: 690upx;
			height: auto;
			margin: 5px auto;
			margin-top: 30upx;
			color: #727B8E;
			display: flex;
			flex-wrap: wrap;
			justify-content: space-between;

			.lefts {
				width: 745upx;
				height: 150upx;
				border-radius: 10px;
				background-color: #ffffff;
				display: flex;
				justify-content: space-evenly;
				align-items: center;

				// border: 1px solid #1CBBB4;
				.left-img {
					width: 120upx;
					height: 110upx;
					margin: 30px 0;
					// border: 1px solid blue;
					// background: #d6f5e7;
					// border-radius: 10px;

					.img {
						width: 120upx;
						height: 100upx;
						background-image: url(../../../static/ruanji/zhujiangliang.png);
						// border: 1px solid #000;
						margin: 0 auto;
						line-height: 90upx;
						// margin: 12px auto;
						background-size: 100% 100%;
						background-repeat: no-repeat;
					}
				}

				.left-texts {
					color: #4C5971;
					font-size: 28upx;

					p {
						color: #387FFD;
						font-size: 44upx;
					}

				}
			}

			.top-twos {
				width: 700upx;
				height: 180upx;
				margin: 0 auto;
				// margin-top: 80upx;
				// border-radius: 16upx;
				// border:1px solid #1785FF;
				display: flex;
				justify-content: space-between;
				align-items: center;

				.lefts {
					width: 225upx;
					height: 150upx;
					border-radius: 10px;
					background-color: #ffffff;
					display: flex;
					justify-content: space-evenly;
					align-items: center;

					.left-img {
						width: 120upx;
						height: 110upx;
						// border: 1px solid blue;
						background: #cfe0fd;
						border-radius: 10px;

						.img {
							width: 60upx;
							height: 60upx;
							background-image: url(../../../static/shiti/five.png);
							// border: 1px solid #000;
							margin: 0 auto;
							line-height: 90upx;
							margin: 12px auto;
							background-size: 100% 92%;
							background-repeat: no-repeat;
						}
					}

					.left-texts {
						width: 210upx;
						height: 100upx;
						// border: 1px solid #18BC37;
						color: #4C5971;
						text-align: center;
						font-size: 20upx;
						line-height: 50upx;
						font-family: 'DIN-Medium';

						.red {
							color: #FF233D;
							font-size: 30upx;
						}

						.orange {
							color: #FF8712;
							font-size: 30upx;
						}

						.green {
							color: #0FBF6E;
							font-size: 30upx;
						}

						.purple {
							color: #8333FA;
							font-size: 30upx;
						}

						.blue {
							color: #387FFD;
							font-size: 30upx;
						}

						.black {
							color: #333333;
							font-size: 30upx;
						}

					}
				}
			}

			&-item {
				// width: 330upx;
				// height: 190upx;
				border-radius: 16upx;
				background-color: white;
				display: flex;
				flex-direction: column;
				justify-content: center;
				align-items: center;

				&-img {
					// width: 100upx;
					// height: 36upx;
					background-repeat: no-repeat;
					background-size: 100% 100%;
					// margin-left: 30upx;
					// margin-right: 18upx;
					margin-bottom: 24upx;
				}

				&-num {
					font-size: 44upx;
				}

				&-name {
					font-size: 28upx;
					color: #4C5971;
					text-align: center;
					// margin-top: 24upx;
				}
			}
		}

		.top {
			width: 690upx;
			height: 334upx;
			margin: 0 auto;
			// margin-top: 30upx;
			border-radius: 16upx;
			background-color: white;
			display: flex;
			flex-wrap: wrap;
			justify-content: space-between;
			align-items: center;

			&-item {
				width: 210upx;
				height: 150upx;
				border-radius: 16upx;
				background-color: white;
				display: flex;
				flex-direction: column;
				justify-content: center;
				align-items: center;

				&-img {
					width: 75upx;
					height: 75upx;
					background-repeat: no-repeat;
					background-size: 100% 100%;
					margin-bottom: 24upx;
				}

				&-num {
					font-size: 38upx;
					color: #333333;
					margin-bottom: 10upx;
					// background-image: url(../../../static/pour/add.png);

					span {
						color: #4C5971;
						font-size: 28upx;
					}
				}

				&-name {
					font-size: 28upx;
					color: #333333;
					// margin-top: 24upx;
				}
			}
		}

		.tabcardall {
			width: 345px;
			height: auto;
			margin: 20px auto;
			background: #fff;
			border-radius: 10px;

			&-title {
				padding-top: 25upx;
				margin-left: 30upx;
				font-size: 36upx;
				font-weight: 600;
				color: #333333;
			}

			.tabcard {
				width: 345px;
				background-color: #fff;
				height: 120px;
				display: flex;
				flex-wrap: wrap;
				// margin: 15px auto;
				border-radius: 10px;
				justify-content: space-around;
				align-items: center;

				&-title {
					padding-top: 25upx;
					margin-left: 30upx;
					font-size: 18upx;
					font-weight: 600;
					color: #333333;
				}

				&-left {

					&-title {
						width: 200upx;
						height: 87upx;
						font-size: 13px;
						font-family: PingFang SC;
						border-radius: 10px;
						// font-weight: bold;
						color: #333333;
						text-align: center;
						line-height: 87upx;
						background: #c9ebff;

					}

					// border: 1px solid #ff0000;
				}

				&-right {
					width: 150upx;
					height: 50px;

					&-title {
						font-size: 17px;
						font-family: PingFang SC;
						font-weight: bold;
						color: #333333;

					}

					// border: 1px solid firebrick;
				}
			}

		}

		.news {
			width: 690upx;
			margin: 0 auto;
			margin-top: 30upx;

			&-title {
				display: flex;
				justify-content: space-between;
				align-items: center;

				&-name {
					color: #333333;
					font-size: 36upx;
					font-weight: 600;
				}

				&-more {
					display: flex;
					align-items: center;
					font-size: 24upx;
					color: #999999;
				}
			}

			&-con {
				margin-top: 30upx;

				&-item {
					margin-top: 30upx;
					display: flex;

					&-but {
						width: 130upx;
						padding: 0 10upx;
						height: 35upx;
						background-color: rgba(74, 127, 255, .1);
						font-size: 22upx;
						color: #387FFD;
						text-align: center;
					}

					&-des {
						width: 500upx;
						margin-left: 18upx;
						font-size: 26upx;
						color: #333333;
					}
				}
			}

			&-activity {
				height: 230upx;
				border-radius: 16upx;
				margin-top: 30upx;
				background-color: white;
				display: flex;
				align-items: center;

				&-img {
					width: 260upx;
					height: 200upx;
					margin-left: 16upx;
					background-color: #1CBBB4;
					border-radius: 16upx;
				}

				&-con {
					height: 200upx;
					margin-left: 20upx;
					color: #333333;

					// background-color: red;
					&-title {
						font-size: 28upx;
						font-weight: 600;
					}

					&-text {
						margin-top: 16upx;
						font-size: 24upx;
					}
				}
			}

			&-video {
				height: 405upx;
				border-radius: 16upx;
				margin-top: 30upx;
				background-color: white;
				color: #333333;

				&-img {
					height: 280upx;
					background-color: #1CBBB4;
					border-radius: 16upx;
				}

				&-title {
					margin-top: 20upx;
					font-size: 28upx;
					font-weight: 600;
				}

				&-text {
					margin-top: 16upx;
					font-size: 24upx;
				}
			}
		}

		.poursta {
			width: 690upx;
			// height: 520upx;
			margin: 0 auto;
			margin-top: 30upx;
			border-radius: 16upx;
			background-color: #FFFFFF;

			&-title {
				padding-top: 25upx;
				margin-left: 30upx;
				font-size: 36upx;
				font-weight: 600;
				color: #333333;

			}

			&-pic {
				width: 100%;
				// height: ;
				// background-color: red;

				.charts-box {
					width: 100%;
					height: 400upx;
				}

				.charts-box1 {
					width: 100%;
					height: 420upx;
				}
			}

			&-pro {
				width: 94%;
				margin: 0 auto;
			}

			&-titles {
				padding-top: 25upx;
				margin-left: 30upx;
				font-size: 30upx;
				font-weight: 400;
			}
		}
		.boxmodal{
			width: 100%;
			height: 150px;
			// border: 1px solid deeppink;
			&-time{
				padding: 10px 0;
			}
			&-type{
				padding: 10px 0 ;
			}
		}
	}
</style>
