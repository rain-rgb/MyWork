<template>
	<!-- 整改 -->
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content" v-if="titleName">{{titleName}}</block>
			<!-- <block slot="content" v-else>整改</block> -->
		</cu-custom>
		<view class="section" >
			<view class="section-text">
				<view class="title">
					<view class="round"></view>
					<view class="title-font">问题隐患</view>
				</view>
				<view class="">风险源点:<span>{{query.manageLocation!==null?query.manageLocation:'暂无数据'}}</span></view>
				<view class="">责任部门:<span>{{query.departname!==null?query.departname:'暂无数据'}}</span></view>
				<view class="">问题级别：:<span>{{query.problemType!==null?query.problemType:'暂无数据'}}</span></view>
				<view class="">问题描述:<span>{{query.problems!==null?query.problems:'暂无数据'}}</span></view>
				<view class="">检查人:<span>{{query.checkPeople!==null?query.checkPeople:'暂无数据'}}</span></view>
				<view class="">检查年月日:<span>{{query.checkTime!==null?query.checkTime:'暂无数据'}}</span></view>
				<view class="">隐患图片:</view>
				<view class="imgs">
					<view @click="ViewImage(index)"  v-for="(item,index) in imgList" :key="index">
						<image style="width: 100px; height: 100px;" mode="aspectFill" :src="item" />
					</view>
				</view>
			</view>
		</view>

		<!-- 整改 开始 -->
		<view class="section" v-if="query.handlestatus !== 0">
			<view class="section-text">
				<view class="title">
					<view class="round"></view>
					<view class="title-font">整改</view>
				</view>
				<view class="">整改内容:<span>{{query.procedure2Reslut!==null?query.procedure2Reslut:'暂无数据'}}</span></view>
				<view class="">整改人:<span>{{query.procedure2People !==null?query.procedure2People:'暂无数据'}}</span></view>
				<view class="">整改日期:<span>{{query.procedure2Time!==null?query.procedure2Time:'暂无数据'}}</span></view>
				<view class="">整改图片:<span>{{query.zgfile!==null?``:'暂无数据'}}</span></view>
				<view class="imgs">
					<view @click="ViewImageZG(index)"  v-for="(item,index) in imgListZG" :key="index">
						<image style="width: 100px; height: 100px;" mode="aspectFill" :src="item" />
					</view>
				</view>
			</view>
		</view>
		<view class="section" v-if="query.handlestatus === 0 && titleName == `整改`">
			<view class="section-text">
				<view class="title">
					<view class="round"></view>
					<view class="title-font">进行整改</view>
				</view>
			<view class="wrap-nav">
				<view class="screen-modal-item-name">
					<span style="color: #E42424;">*</span>
					整改内容:</view>
				<view class="screen-picker">
					<view class="text-input">
						<u--input placeholder="请输入整改内容" border="surround" v-model="query.procedure2Reslut"
							style="background-color:#F6F9FC;"></u--input>
					</view>
				</view>
				<view class="wrap-img">
					<view class="upimg">
						<my-image-upload :uploadFilenames="samPics" @input="ChooseImaged" label="整改图片：">
						</my-image-upload>
					</view>
				</view>
			</view>
			</view>
		</view>
		<!-- 整改 结束 -->

		<!-- 审核 开始 -->
		<view class="section" v-if="query.handlestatus !== 0 && query.handlestatus !== 20">
			<view class="section-text">
				<view class="title">
					<view class="round"></view>
					<view class="title-font">审核</view>
				</view>
				<view class="">审核内容:<span>{{query.procedure1Reslut !==null?query.procedure1Reslut:'暂无数据'}}</span></view>
				<view class="">审核人:<span>{{query.procedure1People !==null?query.procedure1People:'暂无数据'}}</span></view>
				<view class="">审核日期:<span>{{query.procedure1Time !==null?query.procedure1Time:'暂无数据'}}</span></view>
				<!-- <view class="">审核图片:<span>{{query.zgfile!==null?``:'暂无数据'}}</span></view> -->
				<!-- <view class="imgs">
					<view @click="ViewImageZG(index)"  v-for="(item,index) in imgListZG" :key="index">
						<image style="width: 100px; height: 100px;" mode="aspectFill" :src="item" />
					</view>
				</view> -->
			</view>
		</view>
		<view class="section" v-if="query.handlestatus === 20 && titleName == `审核`">
			<view class="section-text">
				<view class="title">
					<view class="round"></view>
					<view class="title-font">进行审核</view>
				</view>
			<view class="wrap-nav">
				<view class="screen-modal-item-name">
					<span style="color: #E42424;">*</span>
					审核内容:</view>
				<view class="screen-picker">
					<view class="text-input">
						<u--input placeholder="请输入审核内容" border="surround" v-model="query.procedure1Reslut"
							style="background-color:#F6F9FC;"></u--input>
					</view>
				</view>
				<!-- <FileUpload
					:fileData="ypFiles"
					@changeFileList="changeFileList"
					:upload="true"
					label="点击上传"
				></FileUpload> -->
				<!-- <view class="wrap-img">
					<view class="upimg">
						<my-image-upload :uploadFilenames="samPics" @input="ChooseImaged" label="审核图片：">
						</my-image-upload>
					</view>
				</view> -->
			</view>
			</view>
		</view>
		<!-- 审核 结束 -->

		<!-- 销号 开始 -->
		<view class="section" v-if="query.handlestatus !== 0 && query.handlestatus !== 20 && query.handlestatus !== 30">
			<view class="section-text">
				<view class="title">
					<view class="round"></view>
					<view class="title-font">销号</view>
				</view>
				<view class="">销号内容:<span>{{query.procedure3Reslut!==null?query.procedure3Reslut:'暂无数据'}}</span></view>
				<view class="">销号人:<span>{{query.procedure3People !==null?query.procedure3People:'暂无数据'}}</span></view>
				<view class="">销号日期:<span>{{query.procedure3Time!==null?query.procedure3Time:'暂无数据'}}</span></view>
			</view>
		</view>
		<view class="section" v-if="query.handlestatus === 30 && titleName == `销号`">
			<view class="section-text">
				<view class="title">
					<view class="round"></view>
					<view class="title-font">进行销号</view>
				</view>
			<view class="wrap-nav">
				<view class="screen-modal-item-name">
					<span style="color: #E42424;">*</span>
					销号内容:</view>
				<view class="screen-picker">
					<view class="text-input">
						<u--input placeholder="请输入销号内容" border="surround" v-model="query.procedure3Reslut"
							style="background-color:#F6F9FC;"></u--input>
					</view>
				</view>
			</view>
			</view>
		</view>
		<!-- 销号 结束 -->

		<view class="screen-modal-btn" v-if="titleName !== `详情`">
			<u-button style="width:350px;border-radius: 10px;color: #fff;background-color: #387FFD;" :text="titleName"
				@click="submitfc"></u-button>
		</view>
		<view class="remind" v-if="titleName !== `详情`">
			请勿重复提交
		</view>
		<!-- </view> -->
		<mx-date-picker :show="datestashow" :type="type" :show-tips="true" :show-seconds="true"
			@confirm="confirmstadate" @cancel="confirmstadate" :isContainTime="true" />
	</view>
</template>

<script>
	import api from '@/api/api.js'
	import dict from '@/pages/component/dict/dict.vue'
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	export default {
		name: 'DeliveryDeclaration',
		components: {
			dict,
			MxDatePicker,
		},
		data() {
			return {
				titleName:"",
				sbtype: '55',

				manageLocation:"",//风险源点
				departname:"",//责任部门
				problemType:"",//问题级别
				problems:"",//问题描述
				checkPeople:"",//检查人
				checkTime:"",//检查年月日
				datestashow: false,
				type: 'datetime',
				imgList: [],
				imgListZG: [],

				procedure2Reslut:"",

				samPics: [],
				samPicss: [],
				fileLists: '',
				fileListd: '',

				// sbNumber: '',
				// gyslist: '',
				// mdidata: '',
				// cpdata: '',
				// clmdata: '',
				// clgg: '',

				// deviceNames: [],
				// devictype: [],
				// electronicLockList: [],
				// maters: [],
				// gys: [],
				// mdd: [],
				// cph: [],
				// Specifications: [],
				// mddcode: [],

				// choosevalue: '',
				// supplier: '',
				// Endto: '',
				// Carnumbers: '',
				// materials: '',
				// specs: '',
				// amount: '',
				// drivername: '',
				// electronicLock: '',
				// lead: '',
				// index: 0,
				query: {},
				ypFiles: null,

			}
		},
		onLoad(options) {
			console.log(`options.item==`,options.item);
			console.log(`options.title==`,options.title);
			if (options.title) {
				this.titleName = options.title;
			}
			if (options.item) {
				this.query = JSON.parse(options.item)
				console.log(`this.query.yhfile==`,this.query.yhfile);
				if (this.query.yhfile) {
					this.imgList = this.query.yhfile.split(/[;,]/);
				}
				if (this.query.zgfile) {
					this.imgListZG = this.query.zgfile.split(/[;,]/);
				}
				console.log(`his.imgList==`,this.imgList);
				// this.manageLocation = this.query.manageLocation
				// this.materials = this.query.cailiaotype_dictText
				// this.specs = this.query.ggxh
				// this.Endto = this.query.mudidi_dictText
			}
		},
		created() {
			this.deviceType()
			this.lock()
			this.gystype()
			this.mddtype()
			// this.cphtype()
			// this.clmtype()
		},
		methods: {
			ViewImage(index) {
				uni.previewImage({
					urls: this.imgList,
					current: index,
				});
			},
			ViewImageZG(index) {
				uni.previewImage({
					urls: this.imgListZG,
					current: index,
				});
			},
			//问题级别
			durationUnit(val) {
				this.problemType = val
			},
			deviceType() {
				let params = {
					status: 1,
					pageSize: 50
				}
				this.$http.get('/syshrwd/syshrwd/list', {
					params
				}).then(res => {
					if (res.data.code == 200) {
						let arr = []
						res.data.result.records.forEach(item => {
							arr.push(item.shrwd)
						})
						this.deviceNames = arr.slice(0);
						this.devictype = arr.slice(0);
					}
				})
			},
			lock() {
				api.deviceType({
					params: {
						// sys_depart_orgcode: this.orgcode,
						sbtypes: this.sbtype
					}
				}).then(res => {
					let arr = []
					res.data.result.forEach(item => {
						arr.push(item.sbjno)
					})
					this.electronicLockList = arr
				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			// 设备名称
			Pickersheibei(e) {
				this.index = e.detail.value
				this.choosekey = 2
				this.choosevalue = this.devictype[this.index]
				// this.sbNumber = this.choosevalue

				// this.begintime = ''
				// this.endtime = ''
			},
			//电子锁
			pickerLock(e) {
				this.electronicLock = this.electronicLockList[e.detail.value]
			},
			gystype() {
				this.$http.get(`/wzgongyingshang/wzgongyingshang/list1`).then(res => {
					// console.log(res.data.result, '供应商信息')
					let data = res.data.result
					this.gys = []
					data.forEach(gysdata => {
						this.gys.push(gysdata.gongyingshangname)
					})
				})

			},
			Pickgys(e) {
				//console.log(e)
				this.index = e.detail.value
				this.choosekey = 3
				this.supplier = this.gys[this.index]
				this.gyslist = this.supplier
				//console.log(this.gyslist, 'jjjjjjjjjjjj');
				// this.begintime = ''
				// this.endtime = ''
			},
			mddtype() {
				this.$http.get(`/wbdestination/wbDestination/list1`).then(res => {
					//console.log(res.data.result, '目的地')
					let data = res.data.result
					this.mdd = []
					this.mddcode = []
					data.forEach(gysdata => {
						this.mdd.push(gysdata.departname)
						this.mddcode.push(gysdata.sysOrgCode)
					})
				})

			},
			Pickermdd(e) {
				//console.log(e)
				this.index = e.detail.value
				this.choosekey = 4
				this.Endto = this.mdd[this.index]
				this.mdcode = this.mddcode[this.index]
				this.mdidata = this.mdcode
				//console.log(this.mdidata, 'jjjjjjjjjjjj');
				// this.begintime = ''
				// this.endtime = ''
			},
			cphtype() {
				this.$http.get(`/vehiclemanagement/vehicleManagement/list1`).then(res => {
					//console.log(res.data.result, '车牌号')
					let data = res.data.result
					this.cph = []
					data.forEach(gysdata => {
						this.cph.push(gysdata.numberPlate)
					})
				})

			},
			Pickercar(e) {
				console.log(e)
				this.index = e.detail.value
				this.choosekey = 5
				this.Carnumbers = this.cph[this.index]
				this.cpdata = this.Carnumbers
				//console.log(this.cpdata, 'jjjjjjjjjjjj');
				// this.begintime = ''
				// this.endtime = ''
			},
			clmtype() {
				let params = {
					iselocks: 0
				}
				this.$http.get(`/wzcailiaonamedict/wzcailiaonamedict/list2`, {
					params
				}).then(res => {
					//console.log(res.data.result, '材料名')
					let data = res.data.result
					this.maters = []
					this.Specifications = []
					data.forEach(gysdata => {
						if (gysdata.cailiaoname && gysdata.guigexinghao) {
							this.maters.push(gysdata.cailiaoname)
							this.Specifications.push(gysdata.guigexinghao)
						}
					})
				})

			},
			Pickmater(e) {
				//console.log(e)
				this.index = e.detail.value
				this.choosekey = 6
				this.materials = this.maters[this.index]
				this.clmdata = this.Carnumbers
				//console.log(this.clmdata, 'jjjjjjjjjjjj');
				// this.begintime = ''
				// this.endtime = ''
			},
			PickSpecifications(e) {
				//console.log(e)
				this.index = e.detail.value
				this.choosekey = 7
				this.specs = this.Specifications[this.index]
				this.clgg = this.Carnumbers
				//console.log(this.clgg, 'jjjjjjjjjjjj');
				// this.begintime = ''
				// this.endtime = ''
			},
			ChooseImages(uploadFilenames) {
				//console.log(uploadFilenames)
				this.fileLists = uploadFilenames.toString()

			},
			ChooseImaged(uploadFilenames) {
				// this.fileListd = uploadFilenames.toString()
				this.fileListd = uploadFilenames.join(`,`)
				console.log(`uploadFilenames.toString()===`,uploadFilenames.toString())
				console.log(`this.fileListd===`,this.fileListd)

			},
			handlebegtim() {
				console.log(`this.checkTime===`,this.checkTime);
				this.datestashow = true
			},
			confirmstadate(e) {
				console.log(e, '确认开始时间')
				this.checkTime = e.value
				// this.model.endtim = nowtime.getNextPreDate(e.value, 1) + ' ' + nowtime.time(e.value)
				this.datestashow = false
			},
			submitfc() {
				let handlestatus = this.titleName == "整改" ? 20 : this.titleName == "审核" ? 30 : 40;
	  			if (!this.query.procedure2Reslut && this.titleName == `整改`) {
					this.$tip.toast('请输入整改内容')
					return
				}
				if (!this.fileListd && this.titleName == `整改`) {
					uni.showToast({
						title: "请添加整改图片",
						icon: 'none'
					})
					return false
				}
				if (!this.query.procedure1Reslut && this.titleName == `审核`) {
					this.$tip.toast('请输入审核内容')
					return
				}
				if (!this.query.procedure3Reslut && this.titleName == `销号`) {
					this.$tip.toast('请输入销号内容')
					return
				}
				let listdata = {
					procedure2Reslut:this.procedure2Reslut,//整改内容
					// id: this.query.id,
					...this.query,
					handlestatus,//整改状态
				}
				if (this.titleName == "整改") {
					listdata.zgfile = this.fileListd;
				}
				this.$http.put(`/anquanfxgk/anquanFxaqjcInfo/edit`, listdata).then(res => {
					if (res.data.success) {
						uni.showToast({
							title: res.data.message,
							icon: 'none'
						})
						setTimeout(() => {
							uni.navigateBack({
								delta: 1
							});
						}, 500);
					}
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FE;
		padding-bottom: 20upx;

		.nav {
			width: 700upx;
			border-radius: 10px;
			background-color: #fff;
			margin: 15px auto;

			.wrap-nav {
				width: 550upx;
				height: 40px;
				margin: 0 auto;
				padding: 29px 0;
				display: flex;
				flex-direction: row;
				justify-content: space-around;
				align-items: center;

				.screen-modal-item-name {
					flex: 3;
				}

				.screen-picker {
					flex: 8;
				}

				.text {
					font-size: 13px;
					font-family: PingFang SC;
					font-weight: 500;
					color: #333333;
					flex: 3;
				}
			}

			.wrap-img {
				width: 540upx;
				height: 95px;
				margin: 0 auto;

				.upimg {
					width: 540upx;
					height: 95px;

					.uptext {
						width: 200px;
						height: 20px;
						font-size: 15px;
						font-family: "PingFang SC";
						font-weight: bold;
						color: #4C5971;
					}
				}
			}
		}

		.remind {
			width: 243upx;
			height: 60upx;
			margin: 20upx auto;
			text-align: center;
			font-size: 18px;
			font-family: PingFang SC;
			font-weight: bold;
			color: #FF233D;
			font-family: cursive;
		}
		.section {
			width: 700rpx;
			height: auto;
			background-color: #fff;
			margin: 15px auto;
			border-radius: 10px;

			// display: flex;
			// flex-direction: row;
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
					padding: 10upx 15upx;
					// width:300rpx;
					// height:70rpx;
					// border-radius: 12px;
					// display: inline-block;
					// background: #F6F9FC;

				}

				.title {
					// width: 400rpx;
					height: 34px;
					// border: 1px solid #18BC37;
					display: flex;
					flex-direction: row;
					justify-content: start;
					align-items: center;
					// background: #38fdc2;

					.round {
						width: 6px;
						height: 17px;
						background: #387FFD;
						border-radius: 6px;
					}

					.title-font {
						font-size: 18px;
						font-family: ' PingFang SC';
						font-weight: bold;
						color: #333333;
						margin-left: 20upx;
					}
				}
			}
		}
		.imgs{
			display: flex;
			flex-wrap: wrap;
		}
	}
</style>
