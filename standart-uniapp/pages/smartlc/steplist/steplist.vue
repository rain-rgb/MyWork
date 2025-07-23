<template>
	<view id="home" >
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">梁信息列表</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<!-- <view class="top-two">
			<view class="left" @click="change(3)">
				<view class="left-text">
					<p class="red">{{totaldata.arrivesum}}</p>
					到场车次
				</view>
			</view>
			<view class="left" @click="change(1)">
				<view class="left-text">
					<p class="orange">{{totaldata.hege}}</p>
					合格数
				</view>
			</view>
			<view class="left" @click="change(2)">
				<view class="left-text">
					<p class="green">{{totaldata.finish}}</p>
					已完成
				</view>
			</view>
		</view> -->
		<!-- <view class="top-two">
			<view class="left" @click="change(0)">
				<view class="left-text">
					<p class="purple">{{totaldata.noarrive}}</p>
					在途车次
				</view>
			</view>
			<view class="left" @click="change(4)">
				<view class="left-text">
					<p class="blue">{{totaldata.failed}}</p>
					退场数
				</view>
			</view>
			<view class="left" @click="change(5)">
				<view class="left-text">
					<p class="black">{{totaldata.arrivednosh}}</p>
					总数量
				</view>
			</view>
		</view> -->
		<view class="section" v-for="(item,index) in shitilist" :key="index">
			<navigator :url="'/pages/smartlc/steplist/stepdetails?uuid='+ item.uuid">
				<view class="section-text">
					<view class="standard" style="color: #FF0000;">{{item.active}}</view>
					<view>工程名称:<span>{{item.projname}}</span></view>
					<view>所属组织机构:<span>{{item.sysOrgCode_dictText}}</span></view>
					<view>任务单编号:<span>{{item.code}}</span></view>
					<view>制梁台座:<span>{{item.taizuono}}</span></view>
					<view>创建时间:<span>{{item.dattim}}</span></view>
					<view>浇注日期:<span>{{item.begtim}}</span></view>
					<view>截止日期:<span>{{item.endtim}}</span></view>
					<view>计划生产时间:<span>{{item.productiontime}}</span></view>
					<view>施工部位:<span>{{item.conspos}}</span></view>
					<view>生产线:<span>{{item.station_dictText}}</span></view>
					<view>坍落度:<span>{{item.lands}}</span></view>
					<view>搅拌时间(分钟):<span>{{item.mixlast}}</span></view>
					<view>强度等级:<span>{{item.betlev}}</span></view>
					<view>任务方量(方):<span>{{item.mete}}</span></view>
					<view>浇筑方式:<span>{{item.pour}}</span></view>
					<view>创建人:<span>{{item.createBy}}</span></view>
				</view>
			</navigator>
		</view>
		<view v-has="'gongxu:list'">
			<u-modal :show="show" showConfirmButton showCancelButton @confirm="confirm" @cancel="cancel">
				<view class="select-box">
					<view class="Task">
						任务单号:
						<view class="Task-input">
						<u--input placeholder="请输入任务单号" border="surround" v-model="Rwudan" @change="change"></u--input>
						</view>
					</view>
					<view class="Task">
						台座名:
						<view class="Task-input">
						<u--input placeholder="请输入制梁台座" border="surround" v-model="Sgtzuo" @change="changeTz"></u--input>
						</view>
					</view>
					<view class="Task">
						施工部位:
						<view class="Task-input">
						<u--input placeholder="请输入施工部位" border="surround" v-model="Sgbuwei" @change="changesg"></u--input>
						</view>
					</view>
					<view class="Task">
						开始时间:
						<view class="Task-input">
							<!-- <view class="screen-modal-item-name">时间：</view> -->
							<view class="screen-modal-item-input" @click="beginshow = true">
								<u--input placeholder="请选择时间" border="surround" v-model="begintimevalue" 
									suffixIcon="arrow-down"></u--input>
							</view>
						</view>
					</view>
					<view class="Task">
						结束时间:
						<view class="Task-input">
							 <!-- <view class="screen-modal-item-name">时间：</view> -->
							 <view class="screen-modal-item-input" @click="endshow = true">
								<u--input placeholder="请选择时间" border="surround" v-model="endtimevalue" 
									suffixIcon="arrow-down"></u--input>
							</view>
						</view>
					</view>
				</view>
			</u-modal>
		</view>
		<mx-date-picker :show="beginshow" :type="type" :value="value" :show-tips="true" :show-seconds="true"
			@confirm="confirmdate" @cancel="confirmdate" />

		<mx-date-picker :show="endshow" :type="type" :value="value" :show-tips="true" :show-seconds="true"
			@confirm="confirmend" @cancel="confirmend" />
	</view>
</template>
<script>
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	import api from '@/api/apis.js'
	export default {
		name: 'infoconfirm',
		components: {
			MxDatePicker
		},
		data() {
			return {
				shitilist: [],
				sbtype: '55',
				pageNo: 1,
				urls: `/zhiliangrenwudan/zhiliangrenwudan/list`,
				show: false,
				Taskno: '',
				Tzname:'',
				Sgposition:'',
				Rwudan:'',
				Sgtzuo:'',
				Sgbuwei:'',
				beginshow: false,
				endshow:false,
				begintimevalue:'',
				endtimevalue:'',
				type:'datetime',
				value:''
			}
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
		created() {},

		methods: {
			textlist() {
				console.log("this.jiesuoeed", this.jiesuo)
				this.$http.get(this.urls, {
					params: {
						sys_depart_orgcode: this.$store.getters.orgcode,
						column: 'id',
						order: 'desc',
						status: 1,
						taizuono: this.Sgtzuo, 
						code: this.Rwudan,
						conspos: this.Sgbuwei,
						pageNo: this.pageNo,
						pageSize: 10,
						dattim_begin:this.begintimevalue,
						dattim_end:this.endtimevalue
					}
				}).then(res => {
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
			
			change(e) {
				console.log(e, '任务单号')
				this.Rwudan = e
			},
			changeTz(e){
				console.log(e,'施工台座')
				this.Sgtzuo = e
			},
			changesg(e){
				console.log(e,'施工部位')
				this.Sgbuwei = e
			},
			// 开始时间
			confirmdate(e) {
				this.begintimevalue = e.value
				this.beginshow = false
			},
			// 结束时间
			confirmend(e) {
				this.endtimevalue = e.value
				this.endshow = false
			},
			screen() {
				this.show = true
			},
			confirm() {
				this.textlist()
				this.show = false
				this.Rwudan = ''
				this.Sgtzuo = ''
				this.Sgbuwei = ''
				this.begintimevalue = ''
				this.endtimevalue = ''
			},
			cancel() {
				this.show = false
				this.Rwudan = ''
				this.Sgtzuo = ''
				this.Sgbuwei = ''
				this.begintimevalue = ''
				this.endtimevalue = ''
			},
			
		}
	}
</script>

<style lang="scss" scoped>
	#home {
		// background-color: #F2F5FE;
		width: 100%;
		height: auto;
		background-color: #F2F5FE;
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
					background-image: url(../../../static/shiti/five.png);
					// border: 1px solid #000;
					margin: 0 auto;
					line-height: 90upx;
					margin: 12px auto;
					background-size: 100% 92%;
					background-repeat: no-repeat;
				}
			}

			.left-text {
				width: 140upx;
				height: 100upx;
				// border: 1px solid #18BC37;
				color: #4C5971;
				text-align: center;
				font-size: 27upx;
				line-height: 50upx;
				font-family: 'DIN-Medium';

				.red {
					color: #FF233D;
					font-size: 50upx;
				}

				.orange {
					color: #FF8712;
					font-size: 50upx;
				}

				.green {
					color: #0FBF6E;
					font-size: 50upx;
				}

				.purple {
					color: #8333FA;
					font-size: 50upx;
				}

				.blue {
					color: #387FFD;
					font-size: 50upx;
				}

				.black {
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
    .select-box{
		width: 100%;
		height:auto;
		// border: 1px solid seagreen;
		.Task{
			width: 100%;
			height:auto;
			// border: 1px solid hotpink;
			.Task-input{
				width:100%;
				height:70upx;
				margin: 10px 0;
				// border: 1px solid forestgreen;
			}
		}
	}
	.zt {
		width: 197px;
		height: 145px;
		background-image: url(../../../static/shiti/zt.png);
		background-size: 44% 62%;
		background-repeat: no-repeat;
		position: absolute;
		right: -98px;
		margin-top: -15px;
		-webkit-transform: rotate(30deg);
		transform: rotate(0deg);
	}

	.wsh {
		width: 197px;
		height: 145px;
		background-image: url(../../../static/shiti/dsh.png);
		background-size: 44% 62%;
		background-repeat: no-repeat;
		position: absolute;
		right: -98px;
		margin-top: -15px;
		-webkit-transform: rotate(30deg);
		transform: rotate(0deg);
	}

	.tc {
		width: 197px;
		height: 145px;
		background-image: url(../../../static/shiti/tc.png);
		background-size: 44% 62%;
		background-repeat: no-repeat;
		position: absolute;
		right: -98px;
		margin-top: -15px;
		-webkit-transform: rotate(30deg);
		transform: rotate(0deg);
	}

	.djs {
		width: 197px;
		height: 145px;
		background-image: url(../../../static/shiti/djs.png);
		background-size: 44% 62%;
		background-repeat: no-repeat;
		position: absolute;
		right: -98px;
		margin-top: -15px;
		-webkit-transform: rotate(30deg);
		transform: rotate(0deg);
	}

	.ywc {
		width: 197px;
		height: 145px;
		background-image: url(../../../static/shiti/ywc.png);
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
		background-image: url(../../../static/pour/add.png);
		background-size: 100% 100%;
		position: fixed;
		bottom: 176px;
		right: -18px;
	}
</style>
