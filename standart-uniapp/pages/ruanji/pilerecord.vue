<template>
	<view id="pilerecord">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">成桩记录</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<view class="top">
			<!-- <view class="left">
				<view class="left-img">
					<view class="img">

					</view>
				</view>
				<view class="left-text">
					累计注浆量
					<p>{{ (parseFloat(statistical.sumzjl)).toFixed(2) }} 方</p>
				</view>
			</view> -->
			<view class="">
				<view style="font-size: 17px;font-weight: bold;color:#000">组织机构:<span
						style="font-size:15px;color:#4C5971;font-family: cursive;">{{sys_depart_name !==''?sys_depart_name:departname}}</span>
				</view>
			</view>
			<view class="top-two">
				<view class="left">
					<view class="left-text">
						<p class="orange">{{ (parseFloat(statistical.zonglong || 0)).toFixed(2) }} 米</p>
						累计总长
					</view>
				</view>
				<view class="left" @click="change(0)">
					<view class="left-text">
						<p class="green">{{statistical.size - statistical.chaobiaocount}}</p>
						合格数
					</view>
				</view>
				<view class="left" @click="change(1)">
					<view class="left-text">
						<p class="red">{{statistical.chaobiaocount}}</p>
						不合格数
					</view>
				</view>
			</view>
			<view class="top-two">
				<view class="left">
					<view class="left-text">
						<p class="purple">{{ (parseFloat(statistical.exceededm || 0)).toFixed(2) }} 米</p>
						质量预警
					</view>
				</view>
				<view class="left">
					<view class="left-text">
						<p class="blue">{{statistical.size}}</p>
						已完工桩数
					</view>
				</view>
				<view class="left">
					<view class="left-text">
						<p class="black">{{ (parseFloat(statistical.sumzjl || 0)).toFixed(2) }} L</p>
						累计注浆量
					</view>
				</view>
			</view>
		</view>
		<u-loading-page :loading="loading" loading-text="加载中..."></u-loading-page>
		<view class="list">
			<view v-for="(item,index) in listdata" :key="index" @click="checkDetail(item)" class="list-item">
				<view class="list-item-sta" :class="item.chaobiaodengji == 0 ? 'green':'red'">
					<!-- <image v-if="item.chaobiaodengji == 0" src="../../static/pour/hege.png" mode=""/>
					<image v-if="item.chaobiaodengji == 1" src="../../static/pour/lv1.png" mode=""/> -->
					<view :style="item.chaobiaodengji == 0 ? 'color:#52C57C;':'color:#F32F45;'">
						{{ item.chaobiaodengji == 0 ? '合格':'不合格' }}
					</view>
				</view>
				<view v-if="item.overLevel != 0" class="list-item-sta1">
					<image v-if="item.overproofStatus == 0" src="../../static/pour/weichuzhi.png" mode=""/>
					<image v-if="item.overproofStatus == 10" src="../../static/pour/yichuzhi.png" mode=""/>
					<image v-if="item.overproofStatus == 20" src="../../static/pour/yishenhe.png" mode=""/>
					<image v-if="item.overproofStatus == 30" src="../../static/pour/bohui.png" mode=""/>
				</view>
				<view class="list-item-con">
					<view style="color: #333333; font-weight: 600;" class="list-item-con-name">
						{{ item.shebeino_dictText }}
					</view>
					<view class="list-item-con-name">
						里程：<span>{{ item.pileMileage }}</span>
					</view>
					<view class="list-item-con-name">
						桩号：<span style="font-weight: 600;">{{ item.pileNo }}</span>
					</view>
					<!-- <view class="list-item-con-name">
						开始时间：<span>{{ item.pileStarttime }}</span>
					</view>
					<view class="list-item-con-name">
						成桩时间：<span>{{ item.pileTime }}</span>
					</view> -->
					<view class="list-item-con-name">
						水泥用量：<span>{{ parseFloat(item.pileCement).toFixed(3) }}</span>
					</view>
					<view class="list-item-con-name">
						实际深度：<span>{{ parseFloat(item.pileRealdep).toFixed(3) }}</span>
					</view>
					<view v-if="item.chaobiaodengji != 0" class="list-item-con-name">
						预警原因：<span>{{ item.problem }}</span>
					</view>
					<view class="list-item-con-name"></view>
				</view>
			</view>
			<view style="height: 40upx;"></view>
		</view>
		<u-modal :show="show" @confirm="confirm" showCancelButton @cancel="cancel">
			<view class="screen">
				<view class="screen-modal">
					<view class="screen-modal-item">
						<view class="screen-modal-item-name">组织机构：</view>
						<view class="screen-modal-item-input">
							<org @choose="Chooseorg"></org>
						</view>
					</view>
					<view class="screen-modal-item">
						<view class="screen-modal-item-name">设备名称：</view>
						<view class="screen-modal-item-input">
							<equipment @choose="Choosedevice" :orgcode="sys_depart_orgcode" sbtype="16,19,53,54">
							</equipment>
						</view>
					</view>
					<view class="screen-modal-item">
						<view class="screen-modal-item-name">桩号：</view>
						<view class="screen-modal-item-input">
							<u--input placeholder="请输入桩号" border="surround" v-model="pileNo"></u--input>
						</view>
					</view>
					<!-- 	<view class="screen-modal-item">
						<view class="screen-modal-item-name">状态：</view>
						<view class="screen-modal-item-input">
							<picker @change="Changeover" :range="isover" range-key="name">
								<u--input placeholder="请选择状态" border="surround" v-model="isovername" disabled
									suffixIcon="arrow-down"></u--input>
							</picker>
						</view>
					</view> -->
					<view class="screen-modal-item">
						<view class="screen-modal-item-name">时间：</view>
						<view class="screen-modal-item-input" @click="dateshow = true">
							<u--input placeholder="请选择时间" border="surround" v-model="datetimevalue" disabled
								suffixIcon="arrow-down"></u--input>
						</view>
					</view>
				</view>
			</view>
		</u-modal>
		<!-- <view class="screen" v-if="show" @confirm="confirm">
			<view class="screen-modal">
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">组织机构：</view>
					<view class="screen-modal-item-input">
						<organization></organization>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">设备名称：</view>
					<view class="screen-modal-item-input">
						<equipment @choose="Choosedevice" sbtype="16,19"></equipment>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">桩号：</view>
					<view class="screen-modal-item-input">
						<u--input placeholder="请输入桩号" border="surround" v-model="pileNo"></u--input>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">时间：</view>
					<view class="screen-modal-item-input" @click="dateshow = true">
						<u--input placeholder="请选择时间" border="surround" v-model="datetimevalue" disabled
							suffixIcon="arrow-down"></u--input>
					</view>
				</view>
				<view class="screen-modal-btn">
					<u-button text="取消" @click="confirm"></u-button>
					<u-button type="primary" text="确认" @click="handleOk"></u-button>
				</view>
			</view>
		</view> -->
		<mx-date-picker :show="dateshow" :type="type" :show-tips="true" :show-seconds="true" @confirm="confirmdate"
			@cancel="confirmdate" />
	</view>
</template>

<script>
	import org from '../component/organization/organization.vue'
	import equipment from '../component/equipment/equipment.vue'
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	import ruanjiapi from "../../api/ruanji.js"
	export default {
		components: {
			equipment,
			MxDatePicker,
			org,
		},
		data() {
			return {
				loading: true,
				total: 0,
				bhgtotal: 0,
				type: 'range',
				pageNo: 1,
				listdata: [],
				show: false,
				dateshow: false,
				datetimevalue: '',
				pileNo: '',
				shebeiNo: '',
				datatime_begin: '',
				datatime_end: '',
				statistical: '',
				isover: [{
						name: '合格',
						value: 0
					},
					{
						name: '超标',
						value: 1
					}
				],
				chaobiaodengji: null,
				isovername: '',
				sys_depart_orgcode: '',
				sysorgcode: '',
				sys_depart_name: '',
				departname: this.$store.getters.departname,
			};
		},
		onReachBottom() {
			uni.showNavigationBarLoading()
			this.pageNo++
			this.getloadlist()
			uni.hideNavigationBarLoading()
		},
		mounted() {
			this.getloadlist()
			this.staticdata()
		},
		onLoad() {
			console.log(this.$store.getters, 'kkkkkkk')
		},
		methods: {
			Chooseorg(choosevalue, choosekey) {
				// 组织机构
				// console.log(choosevalue,choosekey)
				this.sys_depart_name = choosekey
				this.sys_depart_orgcode = choosevalue
				this.shebeiNo = null
				console.log(this.$store, '组织机构')

			},
			URLencode(sStr) {
				return escape(sStr).replace(/\+/g, '%2B').replace(/\ /g, '%20').replace(/\'/g, '%27').replace(/\//g, '%2F')
			},
			checkDetail(e) {
				var s = this.URLencode(e.pileNo)
				var mileage = this.URLencode(e.pileMileage)
				uni.navigateTo({
					url: '/pages/ruanji/pilerecorddetail?pileno=' + s + '&shebeino=' + e.shebeino + '&id=' +
						e.id + '&mileage=' + mileage + '&pileTime=' + e.pileTime,
				})
			},

			// 打开筛选框
			screen() {
				this.show = true
			},
			cancel() {
				this.show = false
			},
			// 关闭筛选框
			confirm() {
				this.pageNo = 1
				this.listdata = []
				this.getloadlist()
				this.staticdata()
				this.show = false
			},
			change(e) {
				this.chaobiaodengji === e ? this.chaobiaodengji = '' : this.chaobiaodengji = e
				this.pageNo = 1
				this.listdata = []
				this.getloadlist();
			},
			Changeover(e) {
				console.log(e)
				this.isovername = this.isover[e.detail.value].name
				this.chaobiaodengji = e.detail.value
			},
			confirmdate(e) {
				console.log(e, '确认时间')
				// this.formData.starttime = e.value
				if (e) {
					this.datetimevalue = e.value.toString()
					this.datatime_begin = e.value[0] + " 00:00:00"
					this.datatime_end = e.value[1] + " 23:59:59"
				}
				this.dateshow = false
			},
			Choosedevice(choosevalue) {
				// console.log(choosevalue, "当前设备编号")
				this.shebeiNo = choosevalue
			},
			// 获取统计信息
			staticdata() {
				let params = {
					column: 'id',
					order: 'desc',
					// shebeino: this.shebeiNo,
					pileTime_begin: this.datatime_begin,
					pileTime_end: this.datatime_end,
				}
				if (this.shebeiNo !== "") {
					let params2 = {
						shebeino: this.shebeiNo
					}
					params = {
						...params,
						...params2
					}
				}
				this.$http.get('/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/list1', {
					params
				}).then(res => {
					this.statistical = res.data.result
				})
			},
			// 获取成桩记录数据
			getloadlist() {
				this.loading = true
				let params = {
					column: 'id',
					order: 'desc',
					pageNo: this.pageNo,
					pageSize: 10,
					pileTime_begin: this.datatime_begin,
					pileTime_end: this.datatime_end,
					pileNo: this.pileNo,
					chaobiaodengji: this.chaobiaodengji
				}
				if (this.shebeiNo !== "") {
					let params2 = {
						shebeino: this.shebeiNo
					}
					params = {
						...params,
						...params2
					}
				}
				ruanjiapi.pilerecordlist({
					params
				}).then(res => {
					// console.log(res, "任务单列表")
					this.loading = false
					let datas = res.data.result.records
					// pileDowntime   pileUptime
					// datas.forEach(e => {
					// 	let startTime = new Date(e.pileTime.replace(/-/g, '/')).getTime() - ((Number(e
					// 		.pileDowntime) + Number(e.pileUptime)) * 1000)
					// 	e.pileStarttime = this.timeapi(new Date(startTime), "yyyy-MM-dd hh:mm:ss")
					// })
					if (res.data.result.records.length == 0) {
						uni.showToast({
							title: '没有更多数据了',
							icon: 'none'
						})
					}
					if (this.pageNo != 1) {
						this.listdata = this.listdata.concat(datas)
					} else {
						this.listdata = datas
					}
				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			// 时间获取
			timeapi(time, fmt) {
				var o = {
					"M+": time.getMonth() + 1, //月份
					"d+": time.getDate(), //日
					"h+": time.getHours(), //小时
					"m+": time.getMinutes(), //分
					"s+": time.getSeconds(), //秒
					"q+": Math.floor((time.getMonth() + 3) / 3), //季度
					S: time.getMilliseconds(), //毫秒
				};
				if (/(y+)/.test(fmt)) {
					fmt = fmt.replace(
						RegExp.$1,
						(time.getFullYear() + "").substr(4 - RegExp.$1.length)
					);
				}
				for (var k in o) {
					if (new RegExp("(" + k + ")").test(fmt)) {
						fmt = fmt.replace(
							RegExp.$1,
							RegExp.$1.length == 1 ?
							o[k] :
							("00" + o[k]).substr(("" + o[k]).length)
						);
					}
				}
				return fmt;
			},
		}
	}
</script>

<style lang="scss" scoped>
	#pilerecord {
		width: 100%;
		background-color: #F3F5FE;

		.top {
			width: 690upx;
			height: auto;
			margin: 0 auto;
			margin-top: 30upx;
			color: #727B8E;
			display: flex;
			flex-wrap: wrap;
			justify-content: space-between;

			.left {
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
						background-image: url(../../static/ruanji/zhujiangliang.png);
						// border: 1px solid #000;
						margin: 0 auto;
						line-height: 90upx;
						// margin: 12px auto;
						background-size: 100% 100%;
						background-repeat: no-repeat;
					}
				}

				.left-text {
					color: #4C5971;
					font-size: 28upx;

					p {
						color: #387FFD;
						font-size: 44upx;
					}

				}
			}

			.top-two {
				width: 700upx;
				height: 180upx;
				margin: 0 auto;
				// margin-top: 80upx;
				// border-radius: 16upx;
				// border:1px solid #1785FF;
				display: flex;
				justify-content: space-between;
				align-items: center;

				.left {
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
							background-image: url(../../static/shiti/five.png);
							// border: 1px solid #000;
							margin: 0 auto;
							line-height: 90upx;
							margin: 12px auto;
							background-size: 100% 92%;
							background-repeat: no-repeat;
						}
					}

					.left-text {
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

			.itemsmall {
				width: 210upx;
				height: 150upx;
			}

			.itembig {
				width: 330upx;
				height: 190upx;
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

		.list {
			width: 100%;
			margin-top: 30upx;

			&-item {
				position: relative;
				width: 690upx;
				// height: 462upx;
				margin: 0 auto;
				margin-bottom: 30upx;
				border-radius: 16upx;
				background-color: white;

				.red {
					background-image: url(../../static/pour/sta1.png);
					background-repeat: no-repeat;
					background-size: 100% 100%;
				}

				.green {
					background-image: url(../../static/pour/sta4.png);
					background-repeat: no-repeat;
					background-size: 100% 100%;
				}

				&-sta {
					position: absolute;
					right: 0px;
					width: 165upx;
					height: 165upx;

					view {
						position: absolute;
						transform: rotate(45deg);
						top: 40upx;
						left: 65upx;
					}
				}

				&-sta1 {
					position: absolute;
					right: 20upx;
					bottom: 20upx;
					width: 165upx;
					height: 165upx;

					image {
						width: 100%;
						height: 100%;
					}
				}

				&-con {
					width: 610upx;
					// height: 336upx;
					margin: 0 auto;

					// background-color: red;
					&-name {
						padding-top: 15upx;
						color: #9299A8;
						font-size: 28upx;

						span {
							color: #4C5971;
						}
					}
				}

			}
		}

		.screen {
			// position: fixed;
			// background: rgba(000, 000, 000, .3);
			// left: 0;
			// top: 0;
			// width: 100%;
			// height: 100%;

			&-modal {
				// background-color: #FFFFFF;
				// position: absolute;
				// top: 50%;
				// left: (750upx - 690upx) / 2;
				// width: 690upx;
				// transform: translateY(-50%);
				// box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.1);
				// border-radius: 12upx;
				// padding: 20upx;

				&-item {
					text-align: left;
					// width: 90%;
					margin-bottom: 30upx;

					&-name {
						color: #4C5971;
						font-size: 30upx;
						margin-bottom: 30upx;
					}

					&-input {}
				}

				&-btn {
					display: flex;
				}
			}


		}
	}
</style>
