<script>
	import Vue from 'vue'
	import appUpdate from 'common/util/appUpdate.js'
	// import upAPP from "@/uni_modules/uni-upgrade-center-app/utils/check-update.js"
	export default {
		onLaunch: function() {
			// #ifdef APP-PLUS
			// upAPP()
			// #endif
			
			// #ifdef APP-PLUS
			appUpdate()
			// #endif
			uni.getSystemInfo({
				success: function(e) {
					// #ifndef MP
					Vue.prototype.StatusBar = e.statusBarHeight;
					if (e.platform == 'android') {
						Vue.prototype.CustomBar = e.statusBarHeight + 50;
					} else {
						Vue.prototype.CustomBar = e.statusBarHeight + 45;
					};
					// #endif

					// #ifdef MP-WEIXIN
					Vue.prototype.StatusBar = e.statusBarHeight;
					let custom = wx.getMenuButtonBoundingClientRect();
					Vue.prototype.Custom = custom;
					Vue.prototype.CustomBar = custom.bottom + custom.top - e.statusBarHeight;
					// #endif		

					// #ifdef MP-ALIPAY
					Vue.prototype.StatusBar = e.statusBarHeight;
					Vue.prototype.CustomBar = e.statusBarHeight + e.titleBarHeight;
					// #endif

					// #ifdef APP-PLUS
					//Vue.prototype.$api.listenTranMsg()
					// 		var info = plus.push.getClientInfo();

					// 		/* 5+  push 消息推送 ps:使用:H5+的方式监听，实现推送*/
					// 		plus.push.addEventListener("click", function(msg) {
					// 			console.log("click:" + JSON.stringify(msg));
					// 			console.log(msg.payload);
					// 			console.log(JSON.stringify(msg));
					// 			//这里可以写跳转业务代码
					// 		}, false);
					// 		// 监听在线消息事件    
					// 		plus.push.addEventListener("receive", function(msg) {
					// 			// plus.ui.alert(2);  
					// 			//这里可以写跳转业务代码
					// 			console.log("recevice:" + JSON.stringify(msg))
					// 		}, false);
					// #endif

					//Vue.prototype.$api.initLogin()

				}
			})
			Vue.prototype.NavBarColor = 'bg-gradual-blue'
			Vue.prototype.Radio_Check_Size = 'scale(0.7)'
			Vue.prototype.bannerList = [{
					id: 1,
					type: 'image',
					url: 'https://static.jeecg.com/upload/test/banner0_1595850438042.jpeg',
					link: ''
				},
				{
					id: 2,
					type: 'image',
					url: 'https://static.jeecg.com/upload/test/banner2_1595818081327.jpg',
					link: ''
				},
				{
					id: 3,
					type: 'image',
					url: 'https://static.jeecg.com/upload/test/oabanner-2_1595648520760.png',
					link: ''
				},
				{
					id: 4,
					type: 'image',
					url: 'https://static.jeecg.com/upload/test/banner5_1595818089013.jpeg',
					link: ''
				},
			]
			Vue.prototype.ColorList = [{
					title: '嫣红',
					name: 'red',
					color: '#e54d42'
				},
				{
					title: '桔橙',
					name: 'orange',
					color: '#f37b1d'
				},
				{
					title: '明黄',
					name: 'yellow',
					color: '#fbbd08'
				},
				{
					title: '橄榄',
					name: 'olive',
					color: '#8dc63f'
				},
				{
					title: '森绿',
					name: 'green',
					color: '#39b54a'
				},
				{
					title: '天青',
					name: 'cyan',
					color: '#1cbbb4'
				},
				{
					title: '海蓝',
					name: 'blue',
					color: '#0081ff'
				},
				{
					title: '姹紫',
					name: 'purple',
					color: '#6739b6'
				},
				{
					title: '木槿',
					name: 'mauve',
					color: '#9c26b0'
				},
				{
					title: '桃粉',
					name: 'pink',
					color: '#e03997'
				},
				{
					title: '棕褐',
					name: 'brown',
					color: '#a5673f'
				},
				{
					title: '玄灰',
					name: 'grey',
					color: '#8799a3'
				},
				{
					title: '草灰',
					name: 'gray',
					color: '#aaaaaa'
				},
				{
					title: '墨黑',
					name: 'black',
					color: '#333333'
				},
				{
					title: '雅白',
					name: 'white',
					color: '#ffffff'
				},
			]

		},
		onShow: function() {
			// console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}

	}
</script>

<style lang="scss">
	@import "plugin/colorui/main.css";
	@import "plugin/colorui/icon.css";
	@import "plugin/colorui/animation.css";
	@import "@/uni_modules/uview-ui/index.scss";

	.nav-list {
		display: flex;
		flex-wrap: wrap;
		padding: 0px 40upx 0px;
		justify-content: space-between;
	}

	.nav-li {
		padding: 30upx;
		border-radius: 12upx;
		width: 45%;
		margin: 0 2.5% 40upx;
		background-image: url(https://cdn.nlark.com/yuque/0/2019/png/280374/1552996358352-assets/web-upload/cc3b1807-c684-4b83-8f80-80e5b8a6b975.png);
		background-size: cover;
		background-position: center;
		position: relative;
		z-index: 1;
	}

	.nav-li::after {
		content: "";
		position: absolute;
		z-index: -1;
		background-color: inherit;
		width: 100%;
		height: 100%;
		left: 0;
		bottom: -10%;
		border-radius: 10upx;
		opacity: 0.2;
		transform: scale(0.9, 0.9);
	}

	.nav-li.cur {
		color: #fff;
		background: rgb(94, 185, 94);
		box-shadow: 4upx 4upx 6upx rgba(94, 185, 94, 0.4);
	}

	.nav-title {
		font-size: 32upx;
		font-weight: 300;
	}

	.nav-title::first-letter {
		font-size: 40upx;
		margin-right: 4upx;
	}

	.nav-name {
		font-size: 28upx;
		text-transform: Capitalize;
		margin-top: 20upx;
		position: relative;
	}

	.nav-name::before {
		content: "";
		position: absolute;
		display: block;
		width: 40upx;
		height: 6upx;
		background: #fff;
		bottom: 0;
		right: 0;
		opacity: 0.5;
	}

	.nav-name::after {
		content: "";
		position: absolute;
		display: block;
		width: 100upx;
		height: 1px;
		background: #fff;
		bottom: 0;
		right: 40upx;
		opacity: 0.3;
	}

	.nav-name::first-letter {
		font-weight: bold;
		font-size: 36upx;
		margin-right: 1px;
	}

	.nav-li text {
		position: absolute;
		right: 30upx;
		top: 30upx;
		font-size: 52upx;
		width: 60upx;
		height: 60upx;
		text-align: center;
		line-height: 60upx;
	}

	.text-light {
		font-weight: 300;
	}

	.uni-system-preview-image {
		background: #fff ;
	}

	@keyframes show {
		0% {
			transform: translateY(-50px);
		}

		60% {
			transform: translateY(40upx);
		}

		100% {
			transform: translateY(0px);
		}
	}

	@-webkit-keyframes show {
		0% {
			transform: translateY(-50px);
		}

		60% {
			transform: translateY(40upx);
		}

		100% {
			transform: translateY(0px);
		}
	}
</style>
