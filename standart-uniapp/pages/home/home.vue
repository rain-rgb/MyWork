<template name="home">
	<view id="home">
		<view class="bg">
			<!-- <swiper circular class="swipers" :style="{height:swiperheight+'px'}" :current="tabIndex" @change="tabChange"> -->
			<swiper circular class="swipers" :style="{height:swiperheight+'px'}" :current="tabIndex"
				@change="tabChange">
				<swiper-item v-for="(items,index) in yemiandata" :key="index">
					<scroll-view scroll-y class="scrolls">
						<view class="header">
							<view class="header-left">
								<image class="header-left-tagpage" src="../../static/pour/biaoqian.png"
									@click="label('/pages/Labellist/Labellist')" mode=""></image>
								<image class="header-left-screen" src="../../static/pour/screen.png" mode=""></image>
							</view>
							<view class="header-center">
								<view class="side">{{ title1 }}</view>
								<view class="center">{{ items.title }}</view>
								<view class="side">{{ title2 }}</view>
							</view>
							<view class="header-right">
								<image class="header-left-tagpage" src="../../static/pour/phone.png" mode=""></image>
								<!-- <image class="header-left-screen" @click="popshow==false?popshow=true:popshow=false"
									src="../../static/pour/firstpageadd.png" mode="">
								</image> -->
							</view>
							<view class="add" :class="[popshows?'go':'aa']"
								@click="popshow">
								<u-icon name="plus-circle" color="#fff" size="20"></u-icon>
							</view>
							<view class="popconfirm" v-if="popshows">
								<view class="popconfirm-item" @click="MessageTip">
									<p>消息提示</p>
									<view class="hd">
										<view class="box">
											<u-badge :type="type" :value="value"></u-badge>
										</view>
									</view>
								</view>
								<u-line color="#707070" length="150rpx" margin="20rpx"></u-line>
								<view class="popconfirm-item" @click="signout">
									<p>退出登录</p>
								</view>
								<view class="popconfirm-item"></view>

					
							</view>
						</view>
						<view class="content">
							<!-- <sss></sss> -->
							<!-- <home1 v-if="PageCur=='1494208097087643650'"></home1>
							<bhz v-if="PageCur=='1494208220333072386'"></bhz>
							<ruanji v-if="PageCur=='1496738460326305793'"></ruanji>
							<Rawmaterial v-if="PageCur=='1498132227663040513'"></Rawmaterial> -->
							<!-- <home1 v-if="PageCur=='/home'"></home1>
							<bhz v-if="PageCur=='/smartbh'"></bhz>
							<ruanji v-if="PageCur=='/ruanji'"></ruanji>
							<Rawmaterial v-if="PageCur=='pages/Rawmaterial/Rawmaterial'"></Rawmaterial> -->
							<leveronetag :PageCur="items.url" :parentId="itemid" :lableAuth="lableAuth"></leveronetag>
						</view>
					</scroll-view>
				</swiper-item>
			</swiper>
		</view>
	</view>
</template>

<script>
	import leveronetag from "./leveronetag.vue"
	import home1 from "./homes.vue"
	import bhz from "../smartbh/smartbh.vue"
	import ruanji from "../ruanji/ruanji.vue"
	import Rawmaterial from "../Rawmaterial/Rawmaterial.vue"
	import smartlc from "../smartlc/smartlc.vue"
	import smartlm from "../SmartPavement/SmartPavement.vue"
	import smartgb from "../smartgb/smartgb.vue"
	import personManage from "../personManage/personManage.vue"
	import api from "../../api/api.js"
	import {
		mapState,
		mapActions
	} from 'vuex'
	export default {
		name: 'home',
		components: {
			home1,
			bhz,
			ruanji,
			Rawmaterial,
			leveronetag,
			smartlc,
			smartlm,
			smartgb,
			personManage,
		},
		data() {
			return {
				swiperheight: 0, //高度
				yemiandata: [],
				lableAuth: [],
				tabIndex: 0, // 选中的
				title1: '',
				title2: '',
				PageCur: '',
				popshows: false,
				type: "error",
				value: '',
				itemid:'',
				showsign:1,
			}
		},
		computed: {
			...mapState(['token'])
		},
		mounted() {

			// this.getGXZD()
			this.getdata()
			// console.log(1111111111)
			// 注意，这里要用个变量存this，不然进到getSystemInfo后this指向会变化，找不到data变量
			var _this = this
			uni.getSystemInfo({
				success: function(res) {
					_this.swiperheight = res.windowHeight + 150
				}
			});
		},
		// onLoad() {
		// 	// this.getdata()
		// 	// console.log(111)
		// 	uni.getSystemInfo({
		// 		success: (res) => {
		// 			let height = res.windowHeight - uni.upx2px(100)
		// 			this.swiperheight = height;
		// 		}
		// 	})
		// },
		methods: {
			doUpApp() {
				upAPP();
			},
			getdata() {
				api.permission().then(res => {
					// console.log(res)
					this.yemiandata = res.data.result.menu
					this.yemiandata = this.yemiandata.filter((e)=>{ 
						return e.title != `安全环保` ||  e.title != `人员管理` || e.title != `材料管理` ||  e.title != `设备管理` ||  e.title != `原材取样检验` })
					this.lableAuth = res.data.result.lableAuth
					console.log("yemiandata---------------------------------------------",this.yemiandata)
					if(this.yemiandata.length > 0){
						if(this.yemiandata.length != 1 ){
							let num = this.yemiandata.length - 1
							this.title1 = this.yemiandata[num].title
							this.title2 = this.yemiandata[1].title
						}
						this.PageCur = this.yemiandata[0].url
						this.itemid = this.yemiandata[0].id
					}
				})
			},
			tabChange(e) {
				this.tabIndex = e.detail.current;
				if (this.tabIndex == 0) {
					let num = this.yemiandata.length - 1
					this.title1 = this.yemiandata[num].title
					this.title2 = this.yemiandata[this.tabIndex + 1].title
				} else if (this.tabIndex == this.yemiandata.length - 1) {
					this.title1 = this.yemiandata[this.tabIndex - 1].title
					this.title2 = this.yemiandata[0].title
				} else {
					this.title1 = this.yemiandata[this.tabIndex - 1].title
					this.title2 = this.yemiandata[this.tabIndex + 1].title
				}
				this.PageCur = this.yemiandata[this.tabIndex].url
				this.itemid = this.yemiandata[this.tabIndex].id
				console.log(this.PageCur)
				// console.log(this.yemiandata[this.tabIndex])
			},
			tiao() {
				uni.navigateTo({
					url: '/pages/pourorder/pourManage'
				})
			},
			// getGXZD() {
			// 	this.$http.get('/sys/dict/getDictItems/xuhao').then(res => {
			// 		if (res.data.success) {
			// 			console.log(res.data.result)
			// 		}
			// 	})
			// },
			getmessage(num){
				return num % 2 ==0
			},
			popshow() {
                this.showsign ++
				console.log(this.showsign)
				let newmessge = this.getmessage(this.showsign)
				if(newmessge){
					let a = ''
					let b = ''
					this.$http.get(`/sys/annountCement/listByUser`).then(res => {
						console.log(res.data.result.anntMsgTotal, '系统信息')
						a = res.data.result.anntMsgTotal
						b = res.data.result.sysMsgTotal
						this.value = a + b
						console.log(this.value)
					})
				}
				this.popshows = !this.popshows
				// console.log(this.popshows)
				
			},
			MessageTip() {
				uni.navigateTo({
					url: '/pages/home/message'
				})
			},

			signout() {
				this.$store.dispatch('Logout')
				uni.navigateTo({
					url: '/pages/login/login'
				})
			},
			label(url) {
				uni.navigateTo({
					url: url
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	#home {
		background-color: #F2F5FE;
	}

	.scrolls {
		height: 100%;
	}

	.bg {
		// background-color: red;
		// background-image: url(../../static/home/bg-header.png);
		// background-repeat: no-repeat;
	}

	.add {
		position: fixed;
		top: 60upx;
		right: 10upx;
		width: 50upx;
		height: 50upx;
		// background-color: #aaaaff;
		border-radius: 50upx;
		display: flex;
		justify-content: center;
		align-items: center;

		// background-image: url(../../../static/pour/add.png);
		// background-repeat: no-repeat;
		// background-size: 100% 100%;
	}

	.popconfirm {
		position: fixed;
		top: 140rpx;
		left: 540rpx;
		width: 200upx;
		// height: 155upx;
		border-radius: 16upx;
		background-color: #000;
		border: 1px solid #000;
		box-shadow: 3px 5px 13px 0px rgba(197, 207, 227, 0.46);

		&-item {
			display: flex;
			justify-content: center;
			align-items: center;
			margin-top: 20upx;
			color: #fff;

			&-img {
				width: 30upx;
				height: 30upx;
				margin-right: 10upx;
			}
		}
	}

	.popconfirm:before,
	.popconfirm:after {
		width: 0;
		height: 0;
		border: transparent solid;
		position: absolute;
		bottom: 96%;
		content: ""
	}

	.popconfirm:before {
		border-width: 10px;
		border-bottom-color: #000;
		right: 10px;
	}

	.popconfirm:after {
		border-width: 8px;
		border-bottom-color: #000;
		right: 10px;
	}

	.content {
		width: 100%;
		// height: 500px;
		// padding-top: 100upx;
		// background-color: red;
	}

	.header {
		width: 100%;
		height: 120upx;
		background-image: url(../../static/home/bg-header.png);
		background-repeat: no-repeat;
		background-size: 100% 200%;
		position: fixed;
		display: flex;
		z-index: 1000;

		// background-color: #506CE2;
		&-left {
			width: 20%;
			display: flex;
			justify-content: center;
			align-items: flex-end;
			margin-bottom: 15upx;

			&-tagpage {
				width: 34upx;
				height: 37upx;
				// margin-right: 30upx;
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
			margin-bottom: 15upx;
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
			width: 10%;
			display: flex;
			justify-content: center;
			align-items: flex-end;
			margin-bottom: 15upx;

			&-tagpage {
				width: 34upx;
				height: 37upx;
				// margin-right: 30upx;
			}

			&-screen {
				width: 34upx;
				height: 37upx;
				margin-left: 30upx;
			}

			// background-color: red;
		}

		.aa {
			transition: all 1s;
		}

		.go {
			transform: rotate(-170deg);
			transition: all 1s;
		}

		.hd {
			position: relative;
			// width: 20px;
			height: 20px;

			// border: 1px solid lawngreen;
			.box {
				width: 17px;
				height: 20px;
				// background-color: #909193;
				border-radius: 15px;
				position: absolute;
				top: -5px;
				right:-15px;
			}
		}
	}
</style>
