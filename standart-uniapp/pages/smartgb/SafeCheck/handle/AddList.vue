<template>
	<!-- 新增 -->
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">新增</block>
		</cu-custom>
		<view class="nav">
			<view class="wrap-nav">
				<view class="screen-modal-item-name">
					<!-- <span style="color: #E42424;">*</span> -->
					风险源点:</view>
				<view class="screen-picker">
					<view class="text-input">
						<u--input placeholder="请输入风险源点" border="surround" v-model="manageLocation"
							style="background-color:#F6F9FC;"></u--input>
					</view>
				</view>
			</view>
			<view class="wrap-nav">
				<view class="screen-modal-item-name">
					<!-- <span style="color: #E42424;">*</span> -->
				责任部门:</view>
				<view class="screen-picker">
					<view class="text-input">
						<u--input placeholder="请输入责任部门" border="surround" v-model="departname"
							style="background-color:#F6F9FC;"></u--input>
					</view>
				</view>
			</view>
			<view class="wrap-nav">
				<view class="screen-modal-item-name">
					<!-- <span style="color: #E42424;">*</span> -->
					问题级别：</view>
				<view class="screen-picker" v-has="'car:time'">
					<view class="screen-modal-item-input">
						<dict dictCode="problem_type" title="请选择" @choose="durationUnit">
						</dict>
					</view>
				</view>
			</view>
			<view class="wrap-nav">
				<view class="screen-modal-item-name">
					<!-- <span style="color: #E42424;">*</span> -->
					隐患类别：</view>
				<view class="screen-picker" v-has="'car:time'">
					<view class="screen-modal-item-input">
						<dict dictCode="yhlb" title="请选择" @choose="durationUnit1">
						</dict>
					</view>
				</view>
			</view>
			<view class="wrap-nav">
				<view class="screen-modal-item-name">
					<!-- <span style="color: #E42424;">*</span> -->
					问题描述:</view>
				<view class="screen-picker">
					<view class="text-input">
						<u--input placeholder="请输入问题描述" border="surround" v-model="problems"
							style="background-color:#F6F9FC;"></u--input>
					</view>
				</view>
			</view>
			<!-- <view class="wrap-nav">
				<view class="screen-modal-item-name">
					<span style="color: #E42424;">*</span>
					检查人:</view>
				<view class="screen-picker">
					<view class="text-input">
						<u--input placeholder="请输入检查人" border="surround" v-model="checkPeople"
							style="background-color:#F6F9FC;"></u--input>
					</view>
				</view>
			</view> -->
			<!-- <view class="wrap-nav">
				<view class="screen-modal-item-name"><span style="color: #E42424;">*</span>检查年月日:</view>
				<view class="screen-picker">
					<view class="text-input">
						<u--input placeholder="请输入检查年月日" border="surround" v-model="checkTime"
							style="background-color:#F6F9FC;"></u--input>
					</view>
				</view>
			</view> -->
			<!-- <view class="wrap-nav">
				<view class="screen-modal-item-name">
					<span style="color: #E42424;">*</span>
				检查年月日:</view>
				<view class="screen-picker">
					<view class="text-input" @click="handlebegtim">
						<u--input placeholder="请选择检查年月日:" border="surround" v-model="checkTime" suffixIcon="calendar"
							disabled suffixIconStyle="color:#4A7FFF;"></u--input>
					</view>
				</view>
			</view> -->
			<!-- <view class="wrap-img">
				<view class="upimg">
					<my-image-upload :uploadFilenames="samPicss" @input="ChooseImages" label="检验报告：">
					</my-image-upload>
				</view>
			</view> -->
			<view class="wrap-img">
				<view class="upimg">
					<my-image-upload :uploadFilenames="samPics" @input="ChooseImaged" label="隐患图片：">
					</my-image-upload>
				</view>
			</view>
			<view class="wrap-nav"></view>
		</view>
		<!-- <view class=""> -->
		<view class="screen-modal-btn">
			<u-button style="width:350px;border-radius: 10px;color: #fff;background-color: #387FFD;" text="新增"
				@click="submitfc"></u-button>
		</view>
		<view class="remind">
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
				sbtype: '55',

				manageLocation:"",//风险源点
				departname:"",//责任部门
				problemType:"",//问题级别
				yhlb:"",//隐患类别
				problems:"",//问题描述
				checkPeople:"",//检查人
				checkTime:"",//检查年月日
				datestashow: false,
				type: 'datetime',

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
				// taskSelect: {}
			}
		},
		onLoad(options) {
			if (options.taskSelect) {
				this.taskSelect = JSON.parse(options.taskSelect)
				this.choosevalue = this.taskSelect.shrwd
				this.materials = this.taskSelect.cailiaotype_dictText
				this.specs = this.taskSelect.ggxh
				this.Endto = this.taskSelect.mudidi_dictText
				this.supplier = this.taskSelect.gysguid_dictText
				this.mdidata = this.taskSelect.mudidi
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
			//问题级别
			durationUnit(val) {
				this.problemType = val
			},
			//隐患类别
			durationUnit1(val) {
				this.yhlb = val
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
				// if (!manageLocation) {
				// 	this.$tip.toast('请输入风险源点')
				// 	return
				// }
				let listdata = {
					manageLocation:this.manageLocation,//风险源点
					departname:this.departname,//责任部门
					problemType:this.problemType,//问题级别
					yhlb:this.yhlb,//隐患类别
					problems:this.problems,//问题描述
					// checkPeople: this.checkPeople,//检查人
					// checkTime: this.checkTime,//检查年月日
					yhfile: this.fileListd,
				}
				this.$http.post(`/anquanfxgk/anquanFxaqjcInfo/add`, listdata).then(res => {
				// this.$http.post(`/wbshebeidetail/wbshebeiDetail/add1`, listdata).then(res => {
					if (res.data.success) {
						uni.showToast({
							title: "添加成功",
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
			width: 143px;
			height: 30px;
			margin: 0 auto;
			text-align: center;
			font-size: 18px;
			font-family: PingFang SC;
			font-weight: bold;
			color: #FF233D;
			font-family: cursive;
		}
	}
</style>
