<template>
	<view id="testblocksave">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">试块上架</block>
			<!-- <view slot="right" @click="screen">筛选</view> -->
		</cu-custom>
		<view class="main" >
			<view class="main-item">
				<view class="main-item-name">养护架号：</view>
				<view class="main-item-input" @click="show = true">
					<u--input placeholder="请选择养护架号" disabled suffixIcon="arrow-down" v-model="huojianame"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">储存层数：</view>
				<view class="main-item-input">
					<picker :range="layerNo" @change="ChangelayerNo">
						<u--input placeholder="请选择储存层数" disabled suffixIcon="arrow-down" v-model="huojiacenshu"></u--input>
					</picker>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">序号：</view>
				<view class="main-item-input">
					<picker :range="storeNo" @change="ChangeStoreNo">
						<u--input placeholder="请选择序号" disabled suffixIcon="arrow-down" v-model="no"></u--input>
					</picker>
				</view>
			</view>
			<view class="tags">
				<u-tag v-show="smcode !== ''" :text="smcode" size="large"></u-tag>
			</view>
			<view class="main-item">
				<u-button class="btnclass" type="primary" @click="onScan" text="扫码"></u-button>
				<u-button class="btnclass" plain type="primary" @click="handleSrbm" text="输入"></u-button>
			</view>
			<view class="main-item">
				<u-button class="btnclass" type="warning" @click="onConfirm" text="确认上架"></u-button>
			</view>
			<view class="main-item"></view>
		</view>
		<!-- 养护架modal -->
		<u-modal :title="'选择养护架号'" :show="show" @confirm="confirm" showCancelButton @cancel="cancel">
			<view class="modal">
				<uni-grid :column="5" @change="change">
					<uni-grid-item style="width: 20%;" v-for="(item,index) in huajiaNews" :index="index" :key='index'>
						<view class="grid-item-box" :style="{'background-color': item.bgindex==0?'#FFF':'#3c9cff' }">
							<text class="text">{{ item.huojianame }}</text>
						</view>
					</uni-grid-item>
				</uni-grid>
			</view>
		</u-modal>
		<!-- 输入编码 -->
		<u-modal :title="'输入编码'" :show="bashow" @confirm="confirm1" showCancelButton @cancel="cancel">
			<view class="modal">
				<u--input placeholder="请输入编码" v-model="code"></u--input>
				<!-- <uni-grid :column="5" @change="change">
					<uni-grid-item style="width: 20%;" v-for="(item,index) in huajiaNews" :index="index" :key='index'>
						<view class="grid-item-box" :style="{'background-color': item.bgindex==0?'#FFF':'#3c9cff' }">
							<text class="text">{{ item.huojianame }}</text>
						</view>
					</uni-grid-item>
				</uni-grid> -->
			</view>
		</u-modal>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				value: '',
				show: false,
				bashow:false,
				huajiaNews: [],
				huojianame: '',
				huojiacenshu:'',
				layerNo:[],
				storeNo: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10'],
				no: '',
				code:'',
				smcode:'',
				codeno:'',
				huojiano:''
			};
		},
		mounted() {
			this.getCuringNo()
		},
		methods: {
			handleSrbm() {
				this.bashow = true
			},
			// 确认上架
			onConfirm() {
				if (!this.huojianame) {
					uni.showToast({
						title: "请选择养护架号",
						icon: "none"
					})
					return
				}
				if (!this.huojiacenshu) {
					uni.showToast({
						title: "请选择储存层数",
						icon: "none"
					})
					return
				}
				if (!this.no) {
					uni.showToast({
						title: "请选择储存序号",
						icon: "none"
					})
					return
				}
				
				console.log(this.code,this.codeno)
				if (!this.code && !this.codeno) {
					uni.showToast({
						title: "请扫描二维码或填写二维码信息",
						icon: "none"
					})
					return
				}
				let huojiayz = {
					no: this.no,
					huojiacenshu: this.huojiacenshu,
					huojianname: this.huojiano,
				}
							
				// console.log("货架上已经存在")
				this.$http.put('/hntconsigncode/hntConsignCode/upyanzhenghuojia', huojiayz).then(res => {
					console.log(res)
					if (!res.data.success) {
						uni.showToast({
							title: res.data.message,
							icon: "none"
						})
					}
					if (res.data.message == "货架上已经存在") {
						return
					} else {
						this.shangjia()
					}
				})
			
			},
			// 二维码扫描
			onScan() {
				var that = this
				uni.scanCode({
					success: function(res) {
						console.log('条码类型：' + res.scanType);
						console.log('条码内容：' + res.result);
						console.log(res)
						let a = res.result.split('#')
						console.log(a[1])
						that.$http.post('/hntconsign/hntConsign/yhyanzheng?code=' + a[1]).then(result => {
							console.log(result)
							if (!result.data.success) {
								uni.showToast({
									title: result.data.message,
									icon: "none"
								})
								return
							}
							that.codeno = a[1]
							// console.log(that.codeno)
							that.smcode = a[0]
						})
					},
				});
			},
			// 上架接口调用
			shangjia() {
				if (this.code) {
					let params = {
						huojianname: this.huojiano,
						huojiacenshu: this.huojiacenshu,
						no: this.no,
						code: this.code,
					}
					this.$http.put('/hntconsigncode/hntConsignCode/edityhup',params).then(res => {
					// api.startCuring(params).then(res => {
						if (res.data.success) {
							uni.showToast({
								title: "操作成功",
								icon: 'none'
							})
							uni.navigateTo({
								url: '/pages/smartsy/sampling/samplinglist',
							})
						}
					})
				}
				if (this.codeno) {
					let params = {
						huojianname: this.huojiano,
						huojiacenshu: this.huojiacenshu,
						no: this.no,
						codeno: this.codeno,
					}
					this.$http.put('/hntconsigncode/hntConsignCode/edityhup',params).then(res => {
						if (res.data.success) {
							uni.showToast({
								title: "操作成功",
								icon: 'none'
							})
							uni.navigateTo({
								url: '/pages/smartsy/sampling/samplinglist',
							})
						}
					})
				}
			},
			// 选择养护架
			change(e) {
				console.log(e.detail)
				let ceng = []
				ceng = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
						'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
					]
				let cengsu = this.huajiaNews[e.detail.index].huojiacenshu
				this.huajiaNews.forEach(e => {
					e.bgindex = 0
				})
				this.huojianame = this.huajiaNews[e.detail.index].huojianame
				this.huajiaNews[e.detail.index].bgindex = 1
				this.layerNo = [],
				this.layerNo = ceng.splice(0, cengsu)
				this.huojiano = this.huajiaNews[e.detail.index].id
				// this.layerNo = []
				// this.cengshu = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
				// 		'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
				// 	],
				// 	console.log(this.cengshu)
				// this.huojianame = this.huajiaNews[e.detail.index].huojianame
				// this.huojiano = this.huajiaNews[e.detail.index].id
				// let cengsu = this.huajiaNews[e.detail.index].huojiacenshu
				// this.layerNo = this.cengshu.splice(0, cengsu)
				this.modalName = null
			},
			// 选择层数
			ChangelayerNo(e) {
				this.huojiacenshu = this.layerNo[e.detail.value]
			},
			// 选择编号
			ChangeStoreNo(e) {
				this.no = this.storeNo[e.detail.value]
			},
			// modal 确认
			confirm() {
				this.show = false
			},
			confirm1() {
				this.$http.post('/hntconsign/hntConsign/yhyanzhengNo?code=' + this.code).then(result => {
					console.log(result)
					if (!result.data.success) {
						uni.showToast({
							title: result.data.message,
							icon: "none"
						})
						this.smcode = ''
						this.code = ''
						return
					}
					this.smcode = this.code
				})
				this.bashow = false
			},
			cancel() {
				this.code = ''
				this.huojianame = ''
				this.huajiaNews.forEach(e => {
					e.bgindex = 0
				})
				this.bashow = false
				this.show = false
			},
			getCuringNo() {
				let params = {
					sys_depart_orgcode: this.$store.getters.orgcode
				}
				this.$http.get('/hntconsignsamplepos/hntConsignSamplePos/list1', {
					params
				}).then(res => {
					console.log(res)
					this.huajiaNews = []
					res.data.result.forEach(e => {
						e.bgindex = 0
						this.huajiaNews.push(e)
					})
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	#testblocksave {
		width: 100%;
		height: 100vh;
		background-color: #F3F5FE;

		.grid-item-box {
			flex: 1;
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			padding: 15px 0;
		}

		.tags{
			display: flex;
			flex-wrap: wrap;
			justify-content: center;
		}

		.main {
			width: 690upx;
			// height: 754upx;
			margin: 0 auto;
			margin-top: 30upx;
			background-color: white;
			border-radius: 16upx;

			.tags {
				display: flex;
				flex-wrap: wrap;
				margin-top: 20upx;
			}

			.mainnew {
				color: #333333;
				font-size: 30upx;
				font-weight: bold;
			}

			&-item {
				display: flex;
				align-items: center;
				padding-top: 30upx;

				.btnclass {
					margin: 0 10upx;
					// width: 30%;
				}

				&-name {
					flex: 3;
					font-size: 30upx;
					text-align: right;
					color: #4C5971;
				}

				&-input {
					flex: 8;
					margin-right: 30upx;
					background-color: #F6F9FC;
					border-radius: 10upx;
				}
			}
		}

		.modal {
			// background-color: red;
			// height: 300upx;
			width: 100%;
		}
	}
</style>
