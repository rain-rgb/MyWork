<template>
	<view id="smartsy">
		<view style="height: 150upx;" class=""></view>
		<!-- 次级页面标签 -->
		<!-- <view class="top">
			<view class="top-item" @click="handleNav(0)">
				<view class="top-item-img" style="background-image: url(../../static/smartsy/llphb_logo.png);">
				</view>
				<view class="top-item-name">理论配比</view>
			</view>
			<view class="top-item" @click="handleNav(1)">
				<view class="top-item-img" style="background-image: url(../../static/smartsy/sgpb_logo.png);">
				</view>
				<view class="top-item-name">施工配比</view>
			</view>
			<view class="top-item" @click="handleNav(2)">
				<view class="top-item-img" style="background-image: url(../../static/smartsy/ypgl_logo.png);">
				</view>
				<view class="top-item-name">样品管理</view>
			</view>
			<view class="top-item" @click="handleNav(3)">
				<view class="top-item-img" style="background-image: url(../../static/smartsy/syj_logo.png);">
				</view>
				<view class="top-item-name">试验机</view>
			</view>
			<view class="top-item" @click="handleNav(4)">
				<view class="top-item-img" style="background-image: url(../../static/smartsy/stjc_logo.png);">
				</view>
				<view class="top-item-name">实体检测</view>
			</view>
			<view class="top-item" @click="handleNav(5)">
				<view class="top-item-img" style="background-image: url(../../static/smartsy/ycjy_logo.png);">
				</view>
				<view class="top-item-name">原材检验</view>
			</view>
		</view> -->
		<view class="top">
			<view class="top-item" v-for="(item,i) in menu" :key="i" @click="handleNav(item.url)">
				<view class="top-item-img" :style="item.indexpic"></view>
				<view class="top-item-name">{{ item.title }}</view>
			</view>
		</view>
		<!-- 任务单统计 -->
		<view class="daycn">
			<view class="daycn-title">任务单统计</view>
			<view class="daycn-sc">
				<view class="daycn-sc-scs">
					<view class="daycn-sc-scs-item">
						<view class="daycn-sc-scs-item-img">
							<image class="" src="../../static/smartsy/sgphb1.png" mode=""></image>
							<span>配比通知单</span>
						</view>
						<view class="daycn-sc-scs-item-num">{{ sgphbsum || 0 }}</view>
					</view>
					<view class="daycn-sc-scs-item">
						<view class="daycn-sc-scs-item-img">
							<image class="" src="../../static/smartsy/llphb1.png" mode=""></image>
							<span>理论配合比</span>
						</view>
						<view class="daycn-sc-scs-item-num">{{ llphbsum || 0 }}</view>
					</view>
				</view>
			</view>
			<view class="daycn-sc">
				<view class="daycn-titles">取样检测任务</view>
				<view class="daycn-sc-scs">
					<view class="daycn-sc-scs-item">
						<view class="daycn-sc-scs-item-img">
							<image class="" src="../../static/smartsy/jlc1.png" mode=""></image>
							<span>监理抽检</span>
						</view>
						<view class="daycn-sc-scs-item-num">{{ jlsum || 0 }}</view>
					</view>
					<view class="daycn-sc-scs-item">
						<view class="daycn-sc-scs-item-img">
							<image class="" src="../../static/smartsy/sgzj1.png" mode=""></image>
							<span>施工自检</span>
						</view>
						<view class="daycn-sc-scs-item-num">{{ sgsum || 0 }}</view>
					</view>
				</view>
			</view>
			<view style="height: 30upx;"></view>
		</view>
		<!-- 检验批监测 -->
		<view class="poursta">
			<view class="poursta-title">检验批监测</view>
			<view class="poursta-pic">
				<view style="display: flex;">
					<view class="charts-box1" style="width: 50%;">
						<qiun-data-charts type="ring"
							:opts="{legend:{position: 'bottom'},dataLabel: false,extra:{ring:{ringWidth: 15,linearType:'custom'}}}"
							:chartData="chartsDataRing1" />
					</view>
					<view class="charts-box1" style="width: 50%;">
						<qiun-data-charts type="ring"
							:opts="{legend:{position: 'bottom',float:'bottom'},dataLabel: false,extra:{ring:{ringWidth: 15,linearType:'custom'}}}"
							:chartData="chartsDataRing3" />
					</view>
				</view>
			</view>
		</view>
		<!-- 钢筋保护层厚度合格率 -->
		<view class="poursta">
			<view class="poursta-title">钢筋保护层厚度合格率</view>
			<view class="poursta-pic">
				<view class="charts-box">
					<qiun-data-charts type="column" background="none" :ontouch="true"
						:opts="{legend:{position: 'top',float:'right'},enableScroll: true,xAxis:{itemCount:5,scrollShow: false,disableGrid: true}}"
						:chartData="mix" />
				</view>
			</view>
		</view>
		<!-- 标养室温湿度检测 -->
		<view class="poursta">
			<view class="poursta-title">标养室温湿度检测</view>
			<view class="poursta-pic">
				<view class="charts-box1">
					<qiun-data-charts type="ring"
						:opts="{legend:{position: 'right'},dataLabel: false,extra:{ring:{ringWidth: 10,linearType:'custom'}}}"
						:chartData="chartsDataRing1" />
				</view>
				
			</view>
		</view>
		<!-- 试块智管 -->
		<view class="daycn">
			<view class="daycn-title">试块智管</view>
			<view class="daycn-bp">
				<view class="daycn-bp-item">
					<view class="daycn-bp-item-name">已过期</view>
					<view class="daycn-bp-item-num" style="color: #E8124F;">{{expiredsum}}</view>
				</view>
				<view class="daycn-bp-item">
					<view class="daycn-bp-item-name">可试验</view>
					<view class="daycn-bp-item-num" style="color: #31ECE5;">{{testablesum}}</view>
				</view>
				<view class="daycn-bp-item">
					<view class="daycn-bp-item-name">养护中</view>
					<view class="daycn-bp-item-num" style="color: #B931EC;">{{maintenancesum}}</view>
				</view>
				<view class="daycn-bp-item">
					<view class="daycn-bp-item-name">已出库</view>
					<view class="daycn-bp-item-num" style="color: #2C9D09;">{{outmaintenancesum}}</view>
				</view>
			</view>

		</view>
		<view class="poursta">
			<view class="poursta-title">试验室合格率/总数</view>
			<view class="poursta-pic">
				<view>
					<view class="charts-box1">
						<qiun-data-charts type="ring" :opts="opts" :chartData="chartsDataRing2" @getIndex="getdata" />
					</view>
				</view>
				<view style="width: 100%;padding: 20upx;">
					<uni-table :stripe="true">
						<uni-tr>
							<uni-th width="50%" align="center"></uni-th>
							<uni-th width="50%" align="center">总数</uni-th>
							<uni-th align="center">合格</uni-th>
							<uni-th align="center">合格率</uni-th>
						</uni-tr>
						<uni-tr>
							<uni-td align="center">压力机</uni-td>
							<uni-td align="center">{{lists.yljsums}}</uni-td>
							<uni-td align="center">{{lists.yljsumstrue}}</uni-td>
							<uni-td align="center">{{lists.yljsumsfalse}}</uni-td>
						</uni-tr>
						<uni-tr>
							<uni-td align="center">万能机</uni-td>
							<uni-td align="center">{{lists.wnjsums}}</uni-td>
							<uni-td align="center">{{lists.wnjsumstrue}}</uni-td>
							<uni-td align="center">{{lists.wnjsumsfalse}}</uni-td>
						</uni-tr>
						<uni-tr>
							<uni-td align="center">抗压抗折机</uni-td>
							<uni-td align="center">{{lists.kykzsums}}</uni-td>
							<uni-td align="center">{{lists.kykzsumstrue}}</uni-td>
							<uni-td align="center">{{lists.kykzsumsfalse}}</uni-td>
						</uni-tr>
						<uni-tr>
							<uni-td align="center">标养室</uni-td>
							<uni-td align="center">{{lists.byssums}}</uni-td>
							<uni-td align="center">{{lists.byssumstrue}}</uni-td>
							<uni-td align="center">{{lists.kykzsumsfalse}}</uni-td>
						</uni-tr>
					</uni-table>
				</view>
			</view>
		</view>
		<!-- 底部消息中心 -->
		<view class="news">
			<view class="news-title">
				<view class="news-title-name">热门资讯</view>
				<view class="news-title-more"> <span>查看更多</span>
					<u-icon color="#999999" size="12" name="arrow-right"></u-icon>
				</view>
			</view>
			<view class="news-activity" v-for="(item,index) in messagelist" :key="index" @click="messageclick(item)">
				<view class="news-activity-img">
					<u--image :showLoading="true" :src="item.url" width="90px" height="96px" shape="square"
						radius="10px"></u--image>
				</view>
				<view class="news-activity-con">
					<view class="news-activity-con-title">{{item.name}}</view>
					<!-- <view class="news-activity-con-text">
					<u-read-more :toggle="true" closeText="更多" openText="收起" :showHeight="50">
						<rich-text style="font-size: 13px;" :nodes="item.description"></rich-text>
					</u-read-more>
				   </view> -->
					<view class="news-activity-con-time" >
						<!-- <text>200</text> -->
						<view class="img">
							{{item.sysOrgCode_dictText}}
						</view>
						<view class="wrap-font">
							<view>{{item.createTime.substring(5)}}</view>
						</view>
					</view>
				</view>
			</view>
			<view class="news-video" v-for="(video,index) in messagevideo" :key="index">
				<view class="news-video-img">
					<video id="myVideo" object-fit="cover" muted autoplay
						style="width: 342px;height: 137px;border-radius: 10px;" :src="video.url"
						controls="true"></video>
				</view>
				<view class="news-video-title">{{video.name}}</view>
				<view class="news-activity-con-text">{{video.description}}</view>
				<view class="news-video-time">
					<!-- <text>200</text> -->
					<text style="float: right; margin-right: 14upx;padding:10px 0;padding: 40px 0;">{{video.createTime}}</text>
				</view>
			</view>
		
		</view>
		<view style="height: 150upx;" class=""></view>
	</view>
</template>

<script>
	import nowtime from '../../common/util/nowtime.js'
	import smartsyapi from '../../api/smartsy.js'
	import testinterface from '@/api/testinterface.js'
	export default {
		// mixins: [nowtime],
		components: {},
		props:["menuList"],
		watch: {
			menuList: {
				handler() {
					this.handlerMenu();
					// console.log(this.menuList,'menuList----------------------------------------------智慧拌合');
				},
			}
		},
		data() {
			return {
				menu: [],
				messagelist:[],
				messagevideo:[],
				dataList: [],
				// syjStatiscshow:false,
				expiredsum: 0,
				testablesum: 0,
				maintenancesum: 0,
				outmaintenancesum: 0,
				llphbsum: 0,
				sgphbsum: 0,
				jlsum: 66,
				sgsum: 9,
				newslist: [],
				todaycn: '',
				pourtjjd: 0, //浇筑令统计进度
				nowdate: nowtime.date(),
				lists: {
					wnjsums: 0,
					wnjsumstrue: 0,
					wnjsumsfalse: 0,

					yljsums: 0,
					yljsumstrue: 0,
					yljsumsfalse: 0,

					byssums: 0,
					byssumstrue: 0,
					byssumsfalse: 0,

					kykzsums: 0,
					kykzsumstrue: 0,
					kykzsumsfalse: 0,

				},
				chartsDataRing1: {
					"series": [{
						"data": [{
							"name": "已形成检验批次数",
							"value": 0
						}, {
							"name": "未形成检验批次数",
							"value": 0
						}]
					}]
				},
				chartsDataRing3: {
					"series": [{
						"data": [{
							"name": "合格批次数",
							"value": 0
						}, {
							"name": "退场批次数",
							"value": 0
						}]
					}]
				},
				chartsDataRing2: {
					"series": [{
						"data": [
							// 	{
							// 	"name": "压力机",
							// 	"value": 50,
							// 	"color": "#387FFD"
							// }, {
							// 	"name": "万能机",
							// 	"value": 8,
							// 	"color": "#B493FD"
							// }, {
							// 	"name": "抗压抗折机",
							// 	"value": 8,
							// 	"color": "#FE9B96"
							// },
						]
					}]
				},
				opts: {
					"legend": {
						"position": 'right'
					},
					"title": {
						"name": "0",
						"fontSize": 25,
						"color": "#000000",
						"offsetX": 0,
						"offsetY": 0
					},
					"dataLabel": false,
					"extra": {
						"ring": {
							"ringWidth": 10,
							"linearType": 'custom'
						}
					},
					"subtitle": {
						"name": "总量",
						"fontSize": 15,
						"color": "#666666",
						"offsetX": 0,
						"offsetY": 0
					}
				},
				mix: {
					"categories": [],
					"series": [{
							"name": "其他",
							"data": []
						},
						{
							"name": "现浇柱",
							"data": []
						},
						{
							"name": "现浇梁",
							"data": []
						},
						{
							"name": "预制梁",
							"data": []
						},
						{
							"name": "现浇板",
							"data": []
						},
						{
							"name": "预制板",
							"data": []
						},
						{
							"name": "预制柱",
							"data": []
						},
					]
				}
			}
		},
		mounted() {
			this.getsksta()
			this.getbhzrecipesta()
			this.getsgphb()
			this.testStatics()
			this.getgangjinbhcSta()
			this.getwztaizhangSta()
			this.slmessage()
		},
		methods: {
			handlerMenu(){
				this.menu = this.menuList.mate.filter((e)=>{
					return e.indexpic.indexOf("http") != -1;
				});
				if(this.menu.length > 6 ){
					this.menu.length = 6;
				};
				// console.log(this.menu,this.menuList.mate,'handlerMenu----------------------------------------------智慧试验');
			},
			handleNav(url) {
				if (url) {
					uni.navigateTo({
						url: url
					})
				} else {
					uni.navigateTo({
						url: '/pages/Labellist/Labellist'
					})
				}
			},
			// handleNav(e) {
			// 	if (e == 0) {
			// 		uni.navigateTo({
			// 			url: '/pages/theory/theory'
			// 		})
			// 	}
			// 	if (e == 1) {
			// 		uni.navigateTo({
			// 			url: '/pages/smartsy/shigongphb/shigongphbManage'
			// 		})
			// 	}
			// 	if (e == 2) {
			// 		uni.navigateTo({
			// 			url: '/pages/smartsy/sampling/samplinglist'
			// 		})
			// 	}
			// 	if (e == 3) {
			// 		uni.navigateTo({
			// 			url: '/pages/smartsy/testingmachine/testingmachinehome'
			// 		})
			// 	}
			// 	if (e == 4) {
			// 		uni.navigateTo({
			// 			url: '/pages/Entitydetection/Entitydetection'
			// 		})
			// 	}
			// 	if (e == 5) {
			// 		uni.navigateTo({
			// 			url: '/pages/Rawmaterial/Statistics'
			// 		})
			// 	}
			// },
			getsksta() {
				smartsyapi.hntconsignsta({
					params: {
						orgcode: this.$store.getters.orgcode
					}
				}).then(res => {
					if (res.data.success) {
						this.expiredsum = res.data.result.expiredsum
						this.testablesum = res.data.result.testablesum
						this.maintenancesum = res.data.result.maintenancesum
						this.outmaintenancesum = res.data.result.outmaintenancesum
					}
				})
			},
			getbhzrecipesta() {
				smartsyapi.bhzrecipesta().then(res => {
					if (res.data.success) {
						this.llphbsum = res.data.result.total
					}
				})
			},
			getsgphb() {
				smartsyapi.sgphbsta({
					params: {
						sysOrgCode: this.$store.getters.orgcode
					}
				}).then(res => {
					if (res.data.success) {
						this.sgphbsum = res.data.result.total
					}
				})
			},
			getdata(e) {
				let datac = e.opts.series[e.legendIndex].data
				console.log("datac1", datac)
				console.log("e", e)
			},
			testStatics() {
				console.log(this.lists.byssums)
				testinterface.syjStatisc().then(res => {
					//console.log(res)
					if (res.data.success) {
						// this.syjStatiscshow = true
						var data = res.data.result;
						this.lists.byssums = data.bys.sums;
						this.lists.byssumstrue = data.bys.sumtrue;
						if (data.bys.sums != 0) {
							this.lists.byssumsfalse = ((data.bys.sumtrue / data.bys.sums) * 100).toFixed(2);
						}
						this.lists.kykzsums = data.kykz.sums;
						this.lists.kykzsumstrue = data.kykz.sumtrue;
						if (data.kykz.sums != 0) {
							this.lists.kykzsumsfalse = ((data.kykz.sumtrue / data.kykz.sums) * 100).toFixed(2);
						}
						this.lists.yljsums = data.ylj.sums;
						this.lists.yljsumstrue = data.ylj.sumtrue;
						if (data.ylj.sums != 0) {
							this.lists.yljsumsfalse = ((data.ylj.sumtrue / data.ylj.sums) * 100).toFixed(2);
						}
						this.lists.wnjsums = data.wnj.sums;
						this.lists.wnjsumstrue = data.wnj.sumtrue;
						if (data.wnj.sums != 0) {
							this.lists.wnjsumsfalse = ((data.wnj.sumtrue / data.wnj.sums) * 100).toFixed(2);
						}
						this.opts.title.name = data.bys.sums + data.kykz.sums + data.ylj.sums + data.wnj.sums;
						this.chartsDataRing2.series[0].data.push({
							name: '压力机',
							value: data.ylj.sums
						}, {
							name: '万能机',
							value: data.wnj.sums
						}, {
							name: '标养室',
							value: data.bys.sums
						}, {
							name: '抗压抗折',
							value: data.kykz.sums
						})
					}
				})
			},
			getgangjinbhcSta() {
				this.dataList = []
				smartsyapi.gangjinbhcSta().then(res => {
					console.log("res", res)
					if (res.data.success) {
						let datacate = res.data.result.orgCode
						let data = res.data.result.data
						for (let re of datacate) {
							this.mix.categories.push(re.departName)
						}
						//console.log("this.mix.categories",this.mix.categories)
						for (let i = 0; i < data.length; i++) {
							if(data[i].data.length>0){
								for (let j = 0; j < data[i].data.length; j++) {
									if (data[i].data[j].zonecount != 0) {
										this.mix.series[i].data.push(Number((data[i].data[j].passrate / data[i]
											.data[j]
											.zonecount * 100).toFixed()))
									} else {
										this.mix.series[i].data.push(0)
									}
								}
							}else{
								this.mix.series[i].data.push(0)
							}
						}   
						console.log("this.mix", this.mix)
					}
				})
			},
			getwztaizhangSta(){
				smartsyapi.wztaizhangSta().then(res=>{
					if(res.data.success){
						//console.log("检验批",res)
						this.chartsDataRing1.series[0].data[0].value=res.data.result.finish
						this.chartsDataRing1.series[0].data[1].value=res.data.result.unfinish
						this.chartsDataRing3.series[0].data[0].value=res.data.result.hege
						this.chartsDataRing3.series[0].data[1].value=res.data.result.buhege
					}
				})
			},
			slmessage() {
				let params = {
					column: 'id',
					order: 'desc',
					topictype:1,
				}
				this.$http.get(`/officialfile/officialFile/list7`, {
					params
				}).then(res => {
					this.messagelist = []
					this.messagevideo = []
					console.log(res, '软基消息中心')
					let data = res.data.result
					// this.messagelist = res.data.result
					data.forEach(msg => {
						console.log(msg)
						if (msg.filetype == 0) {
							this.messagelist.push(msg)
						} else if (msg.filetype == 1) {
							this.messagevideo.push(msg)
						}
					})
			
				})
			},
			messageclick(m){
				uni.navigateTo({
					url:'/pages/smartbh/messageDetails?item='+JSON.stringify(m)
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	#smartsy {
		background-image: url(../../static/smartsy/bg_head.png);
		background-repeat: no-repeat;
		background-size: 100%;

		.top {
			width: 690upx;
			height: 334upx;
			margin: 0 auto;
			// margin-top: 30upx;
			border-radius: 16upx;
			background-color: white;
			display: flex;
			flex-wrap: wrap;
			// justify-content: space-between;
			align-items: center;

			&-item {
				width: 210upx;
				height: 150upx;
				border-radius: 16upx;
				background-color: rgb(255, 255, 255);
				display: flex;
				flex-direction: column;
				justify-content: center;
				align-items: center;
				margin: 0 5px;

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
					// background-color: #1CBBB4;
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
						text-align: left;
						width: 239px;
						height: auto;
					}
		
					&-text {
						margin-top: 16upx;
						font-size: 24upx;
						display: -webkit-box;
						overflow: hidden;
						/*超出隐藏*/
						text-overflow: ellipsis;
						/*隐藏后添加省略号*/
						-webkit-box-orient: vertical;
						-webkit-line-clamp: 2; //想显示多少行
					}
				}
			}
		
			&-video {
				height:500upx;
				border-radius: 16upx;
				margin-top: 30upx;
				background-color: white;
				color: #333333;
		
				&-img {
					height: 280upx;
					// background-color: #1CBBB4;
					border-radius: 16upx;
					// border: 1px solid navajowhite;
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
		.daycn {
			width: 690upx;
			// height: 500upx;
			margin: 0 auto;
			margin-top: 30upx;
			border-radius: 16upx;
			background-color: white;

			&-title {
				padding-top: 25upx;
				margin-left: 30upx;
				font-size: 36upx;
				font-weight: 600;
				color: #333333;
			}

			&-bp {
				width: 620upx;
				margin: 0 auto;
				margin-top: 35upx;
				display: flex;
				flex-wrap: wrap;
				justify-content: space-between;

				&-item {
					width: 260upx;
					height: 160upx;
					border-radius: 19upx;
					background-color: #F2F5FE;
					display: flex;
					flex-direction: column;
					justify-content: center;
					align-items: center;
					margin-bottom: 35upx;

					&-num {
						font-size: 38upx;
						margin-bottom: 20upx;
						margin-top: 14upx;
					}

					&-name {
						font-size: 28upx;
						color: #4C5971;
						margin-top: 24upx;
					}
				}
			}

			&-sc {
				width: 620upx;
				margin: 0 auto;
				margin-top: 50upx;

				&-titles {
					padding-top: 25upx;
					margin-left: 30upx;
					font-size: 30upx;
					font-weight: 400;
				}

				&-scs {
					margin-top: 25upx;
					display: flex;
					flex-wrap: wrap;
					justify-content: space-between;

					&-item {
						width: 290upx;
						height: 70upx;
						border-radius: 19upx;
						border: 1upx solid #D0D2DA;
						display: flex;
						justify-content: space-between;
						align-items: center;

						&-img {
							margin-left: 20upx;
							font-size: 24upx;

							image {
								width: 26upx;
								height: 26upx;
								margin-right: 16upx;
							}

							span {
								color: #4C5971;
							}
						}

						&-num {
							margin-right: 20upx;
						}

					}
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
	}
</style>
