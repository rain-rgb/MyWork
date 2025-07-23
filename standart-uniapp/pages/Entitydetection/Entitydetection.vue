<template>
	<view id="home">
		<!-- <scroll-view scroll-y="true" class="scrolls-y" enable-flex="true"> -->
		<!-- <view class="bg">
				<view class="header"> -->
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">实体检测</block>
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
					检测完成
					<p>0</p>
				</view>
			</view>
			<view class="right">
				<view class="rightall">
					<view class="right-img">

					</view>
				</view>
				<view class="right-text">
					检测未完成
					<p>0</p>
				</view>
			</view>
		</view>
		<view class="section" v-for="(item,index) in shitilist" :key="index">
			<navigator :url="'/pages/Entitydetection/Entitydetectiondetils?item='+JSON.stringify(item)">
				<view class="section-text">
					<view v-if="item.status==0" class="standard"></view>
					<view v-if="item.status==1" class="standards"></view>
					<view class="">任务单号:<span>{{item.uuid!==null?item.uuid:'暂无数据'}}</span></view>
					<view class="">工程名称:<span>{{item.projectname!==null?item.projectname:'暂无数据'}}</span></view>
					<view class="">构件名称:<span>{{item.component_dictText!==null?item.component_dictText:'暂无数据'}}</span>
					</view>
					<view class="">施工部位:<span>{{item.sgbwname !==null?item.sgbwname:'暂无数据'}}</span></view>
					<view class="">任务单状态:<span
							:style="item.status ==1?'color:green;font-weight:bold;font-family: cursive;':item.status ==0?'color:red;font-weight:bold;font-family: cursive;':'color:orange;font-weight:bold;font-family: cursive;'">{{item.status ==1?'已完成':item.status ==0?'未完成':'暂无数据'}}</span>
					</view>
					<view class="">试验日期:<span>{{item.zldate !==null?item.zldate:'暂无数据'}}</span></view>
					<view class="">
						所属部门:<span>{{item.sysOrgCode_dictText !==null?item.sysOrgCode_dictText:'暂无数据'}}</span></view>
					<view class="">
						试验设备厂家:<span>{{item.shebeichangjia_dictText !==null?item.shebeichangjia_dictText:'暂无数据'}}</span>
					</view>
					<view class="">创建人:<span>{{item.createBy !==null?item.createBy:'暂无数据'}}</span>
					</view>
					<view class="">创建日期:<span>{{item.createTime !==null?item.createTime:'暂无数据'}}</span></view>
					<view class="">更新日期:<span>{{item.updateTime !==null?item.updateTime:'暂无数据'}}</span></view>
				</view>
			</navigator>
		</view>
		<!-- </scroll-view> -->
		<!-- <u-sticky :offset-top="topheight"  z-index="999" h5-nav-height="48"> -->
		<view class="increase-img" @click="increase">

		</view>
		<!-- </u-sticky> -->
		<view class="screen" v-if="show" :content='content' @confirm="confirm">
			<view class="screen-modal">
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">试验类型：</view>
					<view class="screen-modal-item-input">
						<!-- <picker @change="ChangeTestType" :range="testType"> -->
						<view class="fixstyle">
							<dict dictCode="component" title="请选择试验类型" @choose="Choose2">
							</dict>
						</view>
						<!-- </picker> --> 
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">开始时间：</view>
					<view class="screen-modal-item-input">
						<view class="fixstyle">
							<timeSelector showType="dateToTime" @btnConfirm="beginTime">
								<text class=""  style="color: #c6c8cb;">
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
								<text class="" style="color: #c6c8cb;">
									{{this.endtime==''?'结束时间':this.endtime}}
								</text>
								<!-- <text class="cuIcon cuIcon-unfold"></text> -->
							</timeSelector>
						</view>
					</view>
				</view>
				<view class="screen-modal-btn">
					<u-button text="取消" @click="confirm"></u-button>
					<u-button type="primary" text="确认" @click="handleOk"></u-button>
				</view>
			</view>
		</view>
	</view>
</template>
<script>
	import dict from '../component/dict/theory.vue'
	import timeSelector from '@/components/wing-time-selector/wing-time-selector.vue';
	export default {
		components: {
			dict,
			timeSelector,
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
		data() {
			return {
				shitilist: [],
				pageNo: 1,
				show: false,
				trytype: '',
				begintime: '',
				endtime: '',
				content: 'uView的目标是成为uni-app生态最优秀的UI框架',
			}
		},

		methods: {

			textlist() {
				this.$http.get(`/hnthtconsign/hnthtConsign/list`, {
					params: {
						column: 'id',
						order: 'desc',
						pageNo: this.pageNo,
						PageSize: 10,
						component: this.trytype,
						sys_depart_orgcode: this.orgcode,
						bhjno: this.sbNumber,
						pdjg: this.pdjg,
						syrq_begin: this.begintime,
						syrq_end: this.endtime,
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
			screen() {
				this.show = true
			},
			Choose2(choosevalue) {
				console.log(choosevalue, 'sssssssssssssss');
				this.trytype = choosevalue
			},
			Choose(choosekey, choosevalue) {
				console.log(this.choosevalue)
				// 开始时间
				if (this.choosekey == 4) {
					this.begintime = this.choosevalue
				}
				// 结束时间
				if (this.choosekey == 5) {
					this.endtime = this.choosevalue
				}

				this.listdata()
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
			increase() {
				uni.navigateTo({
					url: '/pages/Entitydetection/Entitydetectionadd'
				})
			}
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
		height: 214upx;
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
				background: #d6f5e7;
				border-radius: 10px;

				.img {
					width: 60upx;
					height: 60upx;
					background-image: url(../../static/shiti/two.png);
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
					color: #0FBF6E;
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
				background: #f5e0e2;
				border-radius: 10px;

				.right-img {
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

	.standard {
		width: 197px;
		height: 145px;
		background-image: url(../../static/shiti/the.png);
		background-size: 44% 62%;
		background-repeat: no-repeat;
		position: absolute;
		right: -98px;
		margin-top: -15px;
		-webkit-transform: rotate(30deg);
		transform: rotate(0deg);
	}

	.standards {
		width: 197px;
		height: 145px;
		background-image: url(../../static/shiti/four.png);
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
		}

	}

	.increase-img {
		width: 77px;
		height: 77px;
		background-image: url(../../static/pour/add.png);
		background-size: 100% 100%;
		position: fixed;
		bottom: 176px;
		right: -2px;
	}
</style>
