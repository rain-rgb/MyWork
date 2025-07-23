<template>
	<view id="home">
		<!-- <scroll-view scroll-y="true" class="scrolls-y" enable-flex="true"> -->
		<!-- <view class="bg">
				<view class="header"> -->
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">原材检验</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<!-- </view> -->
		<!-- </view> -->
		<view class="top">
			<view class="left">
				<view class="left-img">
					<view class="img">

					</view>
				</view>
				<view class="left-text">
					进场批次
					<p>0</p>
				</view>
			</view>
			<view class="right">
				<view class="rightall">
					<view class="right-img">

					</view>
				</view>
				<view class="right-text">
					取样数
					<p>0</p>
				</view>
			</view>
		</view>

		<view class="top-two">
			<view class="left">
				<view class="left-img">
					<view class="img">

					</view>
				</view>
				<view class="left-text">
					已检验
					<p>0</p>
				</view>
			</view>
			<view class="right">
				<view class="rightall">
					<view class="right-img">

					</view>
				</view>
				<view class="right-text">
					合格率
					<p>0%</p>
				</view>
			</view>
		</view>
		<view class="section" v-for="(item,index) in shitilist" :key="index" style="position: relative;">
			<navigator :url="'/pages/Rawmaterial/Statisticdetails?item='+JSON.stringify(item)">
				<view class="section-text">
					<view v-if="item.jianyanstate==0" class="wjc"></view>
					<view v-if="item.jianyanstate==1" class="hg"></view>
					<view v-if="item.jianyanstate==2" class="bhg"></view>
					<view class="">进场时间:<span>{{item.jinchangshijian!==null?item.jinchangshijian:'暂无数据'}}</span></view>
					<view class="">
						设备名称:<span>{{item.shebeibianhao_dictText!==null?item.shebeibianhao_dictText:'暂无数据'}}</span>
					</view>
					<view class="">检验批次:<span>{{item.pici !==null?item.pici:'暂无数据'}}</span></view>
					<view class="">
						规格类型:<span>{{item.guigexinghao !==null?item.guigexinghao:'暂无数据'}}</span></view>
					<view class="">净重(吨):<span>{{item.jingzhongt !==null?item.jingzhongt:'暂无数据'}}</span>
					</view>
					<view class="">料仓名称:<span>{{item.lcbianhao_dictText!==null?item.lcbianhao_dictText:'暂无数据'}}</span>
					</view>
					<view class="">材料名称:<span>{{item.cailiaono_dictText !==null?item.cailiaono_dictText:'暂无数据'}}</span>
					</view>
					<view class="">
						是否已取样:<span
							:style="item.isquyang ==0?'color:green;font-weight:bold;font-family:cursive;':item.isquyang==1?'color:orange;font-weight:bold;font-family:cursive':'color:red;font-weight:bold;font-family:cursive'">{{item.isquyang ==0?'未取样':item.isquyang ==1?'已取样':'其他'}}</span>
					</view>
					<!-- <view class="">是否合格:<span
						:style="item.jianyanstate == 0?'color:orange;font-weight:bold;font-family:cursive':item.jianyanstate ==1?'color:green;font-weight:bold;font-family:cursive':'color:red;font-weight:bold;font-family:cursive'">{{item.jianyanstate ==0?'未检验':item.jianyanstate ==1?'合格':'不合格'}}</span>
				</view> -->
				</view>
			</navigator>
			<u-button @click="sampling(item)"
				style="width:90px;border-radius: 10px;color: #fff;background-color: #387FFD; position: absolute;bottom: 20px;right: 30px;"
				text="取样委托"></u-button>
		</view>
		<!-- </scroll-view> -->
		<!-- <u-sticky :offset-top="topheight"  z-index="999" h5-nav-height="48"> -->
		<!-- 	<view class="increase-img" @click="increase">
          
		</view> -->
		<!-- </u-sticky> -->


		<view class="screen" v-if="show" :content='content' @confirm="confirm">
			<view class="screen-modal">
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">设备名称：</view>
					<picker @change="PickerChange" :range="deviceNames">
						<u--input placeholder="请选择设备名称" border="surround" v-model="deviceName" disabled></u--input>
					</picker>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">取样结果：</view>
					<view class="screen-modal-item-input">
						<view class="fixstyle">
							<picker @change="Cblevel" :value="index" :range="cblevelname">
								<text class="" style="color: rgb(192, 196, 204);">
									<!-- {{pdresult== ''?"请选择是否取样":pdresult}} -->
									{{cbindex>-1?cblevelname[cbindex]:'请选择是否取样'}}
								</text>
								<!-- <text class="cuIcon cuIcon-unfold"></text> -->
							</picker>
						</view>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">开始时间：</view>
					<view class="screen-modal-item-input">
						<view class="fixstyle">
							<timeSelector showType="dateToTime" @btnConfirm="beginTime">
								<text class="" style="color:#c0c4cc;">
									{{this.begintime==''?'开始时间':this.begintime}}
								</text>
								<!-- <text class="cuIcon cuIcon-unfold"></text> -->
							</timeSelector>
						</view>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">结束时间：</view>
					<view class="screen-modal-item-input">
						<view class="fixstyle">
							<timeSelector showType="dateToTime" @btnConfirm="endTime">
								<text class="" style="color:#c0c4cc;">
									{{this.endtime==''?'结束时间':this.endtime}}
								</text>
								<!-- <text class="cuIcon cuIcon-unfold"></text> -->
							</timeSelector>
						</view>
					</view>
				</view>
				<view class="screen-modal-btn">
					<u-button text="取消" @click="confirm"></u-button>
					<u-button type="primary" text="确认" @click="Choose"></u-button>
				</view>
			</view>
		</view>
	</view>
</template>
<script>
	import dict from '../component/dict/dict.vue'
	import timeSelector from '@/components/wing-time-selector/wing-time-selector.vue';
	import Byspublic from '../component/Byspublic/Byspublic.vue'
	import api from '@/api/apis.js'
	export default {
		components: {
			dict,
			timeSelector,
			Byspublic
		},
		onLoad() {
			this.textlist()
		},
		onReachBottom() {
			uni.showNavigationBarLoading()
			this.pageNo++
			this.textlist()
			uni.hideNavigationBarLoading()
		},
		created() {
			this.deviceType()
		},
		data() {
			return {
				deviceName: [],
				devictype: [],
				choosekey: '',
				choosevalue: '',
				sbNumber: '',
				deviceNames: [],
				deviceName: '',
				orgcode: '',
				index: -1,
				cbindex: -1,
				shitilist: [],
				pageNo: 1,
				show: false,
				// shows: false,
				// table: [],
				// index:0,
				// jg:'',
				trytype: '',
				begintime: '',
				endtime: '',
				sbtype: '27',
				content: 'uView的目标是成为uni-app生态最优秀的UI框架',
				cblevelname: [
					"未取样",
					"已取样",
				],
				pdresult: '',
				pdjg: null,
			}
		},
		methods: {
			deviceType() {
				api.deviceType({
					params: {
						sys_depart_orgcode: this.orgcode,
						sbtypes: this.sbtype
					}
				}).then(res => {
					// console.log(res.data.result)
					let devicelist = res.data.result
					this.deviceNames = []
					this.devictype = []
					devicelist.forEach(e => {
						this.deviceNames.push(e.sbname)
						this.devictype.push(e.sbjno)
					})
				}).catch(e => {
					// console.log("请求错误", e)
				})

			},
			// 设备名称
			PickerChange(e) {
				// console.log(e)
				// this.cbindex = -1
				this.index = e.detail.value
				this.choosekey = 2
				this.choosevalue = this.devictype[this.index]
				this.deviceName = this.deviceNames[this.index]
				// console.log(this.deviceName,'sss')
				this.sbNumber = this.choosevalue
				// console.log(this.sbNumber, 'jjjjjjjjjjjj');

				this.begintime = ''
				this.endtime = ''
				// this.Choose()
			},
			Cblevel(e) {
				console.log(e)
				// if (e.detail.value == 0) {
				// 	e.detail.value = '未取样'
				// 	this.pdresult = e.detail.value
				// } else if (e.detail.value == 1) {
				// 	e.detail.value = '已取样'
				// 	this.pdresult = e.detail.value
				// }
				this.cbindex = e.detail.value
				this.choosekey = 3
				// this.choosevalue = e.detail.value
				// this.Choose()
			},
			Choose(choosekey, choosevalue) {
				this.pdjg = ''
				// console.log(this.choosekey,this.choosevalue);
				// 设备名称
				if (this.choosekey == 2) {
					this.sbNumber = this.choosevalue
					// console.log(this.sbNumber)
					this.begintime = ''
					this.endtime = ''
				}
				if (this.choosekey == 3) {
					this.pdjg = this.cbindex
				}
				this.textlist()
				this.show = false
			},
			textlist() {
				this.$http.get(`/wztaizhang/wztaizhang/list1`, {
					params: {
						column: 'id',
						order: 'desc',
						pageNo: this.pageNo,
						PageSize: 10,
						shebeibianhao: this.sbNumber,
						// sys_depart_orgcode: this.orgcode,
						// bhjno: this.sbNumber,
						isquyang: this.pdjg,
						syrq_begin: this.begintime,
						syrq_end: this.endtime,
					}
				}).then(res => {
					// let ng = res.data.result.records
					// ng.forEach((mnj => {
					// 	this.table = mnj.cailiaono_dictText
					// 	console.log(this.table,'gggggggggggg');
					// }))
					if (res.data.result.records.length == 0) {
						uni.showToast({
							title: '别下拉啦 没有更多数据了',
							icon: 'none'
						})
					}
					if (this.pageNo != 1) {
						this.shitilist = this.shitilist.concat(res.data.result.records)
					} else {
						this.shitilist = res.data.result.records
					}
				})
			},
			// bindPickerChange(e){
			// 	console.log(e,'ggggggggggggg');
			// 	//改变的事件名
			// 	//console.log('picker发送选择改变，携带值为', e.target.value)   用于输出改变索引值
			// 	this.index = e.target.value			//将数组改变索引赋给定义的index变量
			// 	this.jg=this.table[this.index]		//将array【改变索引】的值赋给定义的jg变量
			// 	console.log("材料名称：",this.jg)		//输出获取的籍贯值，例如：中国
			// },
			screen() {
				this.cbindex = '请选择是否取样'
				this.show = true
				this.begintime = ''
				this.endtime = ''
				this.sbNumber = ''
				this.deviceName = ''
			},
			beginTime(e) {
				console.log('确定时间为：', e);
				this.begintime = e.key
				this.choosevalue = e.key
				this.choosekey = 4
				// this.Choose()
			},
			// 结束时间
			endTime(e) {
				console.log('确定时间为：', e);
				this.endtime = e.key
				this.choosevalue = e.key
				this.choosekey = 5
				// this.Choose()
			},
			handleOk() {
				this.textlist()
				this.show = false
			},
			confirm() {
				this.show = false
			},
			sampling(item) {
				uni.navigateTo({
					url: '/pages/MaterialInspection/sampling/sampling?item=' + JSON.stringify(item)
				})
			},
			// increase() {
			// 	uni.navigateTo({
			// 		url: '/pages/Entitydetection/Entitydetectionadd'
			// 	})
			// }
		}
	}
</script>

<style lang="scss" scoped>
	#home {
		// background-color: #F2F5FE;
		width: 100%;
		// white-space: nowrap;
	}

	.bg {
		width: 100%;
		height: 150upx;
		background-color: #ffffff;
		position: fixed;
	}

	.header {
		width: 100%;
		height: 100upx;
		display: flex;

		// background-color: #506CE2;
		&-left {

			width: 20%;
			display: flex;
			justify-content: center;
			align-items: flex-end;

			&-tagpage {
				width: 34upx;
				height: 37upx;
				margin-left: 30upx;
			}

			&-screen {
				width: 34upx;
				height: 37upx;
				margin-left: 30upx;
			}

			// background-color: red;
		}

		&-center {
			width: 60%;
			// background-color: blue;
			display: flex;
			align-items: flex-end;
			overflow: hidden;
			white-space: nowrap;

			.center {
				width: 40%;
				text-align: center;
				font-size: 38upx;
				color: #FFFFFF;
			}

			.side {
				width: 30%;
				text-align: center;
				color: rgba(255, 255, 255, 0.4);
				// overflow: hidden;
				// white-space: nowrap;
			}
		}

		&-right {
			width: 20%;
			display: flex;
			justify-content: center;
			align-items: flex-end;

			&-tagpage {
				width: 34upx;
				height: 37upx;
				margin-left: 30upx;
			}

			&-screen {
				width: 34upx;
				height: 37upx;
				margin-left: 30upx;
			}

			// background-color: red;
		}
	}

	.newcolor {
		color: #000;
		font-size: 30upx;
	}

	.top {
		width: 700upx;
		height: 180upx;
		margin: 0 auto;
		// margin-top: 80upx;
		// border-radius: 16upx;
		// border:1px solid #333333;
		display: flex;
		justify-content: space-between;
		align-items: center;

		.left {
			width: 345upx;
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
				background: #f7d0d4;
				border-radius: 10px;

				.img {
					width: 60upx;
					height: 60upx;
					background-image: url(../../static/shiti/one.png);
					// border: 1px solid #000;
					margin: 0 auto;
					line-height: 90upx;
					margin: 12px auto;
					background-size: 100% 92%;
					background-repeat: no-repeat;
				}
			}

			.left-text {
				width: 130upx;
				height: 145upx;
				// border: 1px solid #18BC37;
				color: #4C5971;
				text-align: center;
				font-size: 27upx;
				line-height: 60upx;
				font-family: 'DIN-Medium';

				p {
					color: #FF233D;
					font-size: 50upx;
				}

			}
		}

		.right {
			width: 345upx;
			height: 150upx;
			border-radius: 10px;
			background-color: #ffffff;
			display: flex;
			justify-content: space-evenly;
			align-items: center;

			.rightall {
				width: 120upx;
				height: 110upx;
				// border: 1px solid blue;
				background: #e5f7ef;
				border-radius: 10px;

				.right-img {
					width: 60upx;
					height: 60upx;
					background-image: url(../../static/shiti/seven.png);
					// border: 1px solid #000;
					margin: 0 auto;
					line-height: 90upx;
					margin: 12px auto;
					background-size: 100% 92%;
					background-repeat: no-repeat;
				}
			}

			.right-text {
				width: 130upx;
				height: 145upx;
				// border: 1px solid #18BC37;
				color: #4C5971;
				text-align: center;
				font-size: 27upx;
				line-height: 60upx;
				font-family: 'DIN-Medium';

				p {
					color: #0FBF6E;
					font-size: 50upx;
				}

			}
		}
	}

	.top-two {
		width: 700upx;
		height: 180upx;
		margin: 0 auto;
		// margin-top: 80upx;
		// border-radius: 16upx;
		// border:1px solid #333333;
		display: flex;
		justify-content: space-between;
		align-items: center;

		.left {
			width: 345upx;
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
				width: 130upx;
				height: 145upx;
				// border: 1px solid #18BC37;
				color: #4C5971;
				text-align: center;
				font-size: 27upx;
				line-height: 60upx;
				font-family: 'DIN-Medium';

				p {
					color: #387FFD;
					font-size: 50upx;
				}

			}
		}

		.right {
			width: 345upx;
			height: 150upx;
			border-radius: 10px;
			background-color: #ffffff;
			display: flex;
			justify-content: space-evenly;
			align-items: center;

			.rightall {
				width: 120upx;
				height: 110upx;
				// border: 1px solid blue;
				background: #ebf0f9;
				border-radius: 10px;

				.right-img {
					width: 60upx;
					height: 60upx;
					background-image: url(../../static/shiti/six.png);
					// border: 1px solid #000;
					margin: 0 auto;
					line-height: 90upx;
					margin: 12px auto;
					background-size: 100% 92%;
					background-repeat: no-repeat;
				}
			}

			.right-text {
				width: 130upx;
				height: 145upx;
				// border: 1px solid #18BC37;
				color: #4C5971;
				text-align: center;
				font-size: 27upx;
				line-height: 60upx;
				font-family: 'DIN-Medium';

				p {
					color: #333333;
					font-size: 50upx;
				}

			}
		}
	}

	.newcolor/deep/.cu-bar.fixed {
		position: fixed;
		width: 100%;
		top: 22px;
		z-index: 1024;
	}

	.scrolls-y {
		width: 100%;
		height: auto;
		// white-space: nowrap;
	}

	.section {
		width: 700upx;
		height: auto;
		border-radius: 10px;
		margin: 0 auto;
		background-color: #fff;

		.section-text {
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

	.wjc {
		width: 197px;
		height: 145px;
		background-image: url(../../static/shiti/wjc.png);
		background-size: 44% 62%;
		background-repeat: no-repeat;
		position: absolute;
		right: -98px;
		margin-top: -15px;
		-webkit-transform: rotate(30deg);
		transform: rotate(0deg);
	}

	.hg {
		width: 197px;
		height: 145px;
		background-image: url(../../static/shiti/hg.png);
		background-size: 44% 62%;
		background-repeat: no-repeat;
		position: absolute;
		right: -98px;
		margin-top: -15px;
		-webkit-transform: rotate(30deg);
		transform: rotate(0deg);
	}

	.bhg {
		width: 197px;
		height: 145px;
		background-image: url(../../static/shiti/bhg.png);
		background-size: 44% 62%;
		background-repeat: no-repeat;
		position: absolute;
		right: -98px;
		margin-top: -15px;
		-webkit-transform: rotate(30deg);
		transform: rotate(0deg);
	}

	.screen {
		position: fixed;
		background: rgba(000, 000, 000, .3);
		left: 0;
		top: 0;
		width: 100%;
		height: 100%;

		&-modal {
			background-color: #FFFFFF;
			position: absolute;
			top: 50%;
			left: (750upx - 690upx) / 2;
			width: 690upx;
			transform: translateY(-50%);
			box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.1);
			border-radius: 12upx;
			padding: 20upx;

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

		.fixstyle {
			width: 320px;
			height: 37px;
			border: 1px solid #c4c9d0;
			background-color: #f5f7fa;
			border-radius: 5px;
			line-height: 37px;
			display: flex;

		}

	}

	.increase-img {
		width: 24%;
		height: 87px;
		// background-image: url(/static/img/add.075111b7.png);
		background-size: 100% 100%;
		position: fixed;
		bottom: 176px;
		right: -18px;
	}
</style>
