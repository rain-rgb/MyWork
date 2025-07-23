<template>
	<!-- 发货申报 -->
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">发货申报</block>
		</cu-custom>
		<view class="nav">
			<view class="wrap-nav">
				<view class="screen-modal-item-name"><span style="color: #E42424;">*</span>送货任务单：</view>
				<view class="screen-picker">
					<picker @change="Pickersheibei" :range="deviceNames">
						<u--input placeholder="请选择送货任务单" border="surround" v-model="choosevalue" disabled
							suffixIcon="arrow-down"></u--input>
					</picker>
				</view>
			</view>
			<view class="wrap-nav">
				<view class="screen-modal-item-name"><span style="color: #E42424;">*</span>供应商：</view>
				<view class="screen-picker">
					<picker @change="Pickgys" :range="gys">
						<u--input placeholder="请选择供应商" border="surround" v-model="supplier" disabled
							suffixIcon="arrow-down"></u--input>
					</picker>
				</view>

			</view>

			<!-- <view class="wrap-nav">
				<view class="screen-modal-item-name">中转站：</view>
				<view class="screen-picker">
					<picker @change="Pickermdds" :range="mdd">
						<u--input placeholder="请选择中转站" border="surround" v-model="Endtos" disabled
							suffixIcon="arrow-down">
						</u--input>
					</picker>
				</view>

			</view> -->

			<view class="wrap-nav">
				<view class="screen-modal-item-name"><span style="color: #E42424;">*</span> 目的地：</view>

				<view class="screen-picker">
					<picker @change="Pickermdd" :range="mdd">
						<u--input placeholder="请选择目的地" border="surround" v-model="Endto" disabled
							suffixIcon="arrow-down">
						</u--input>
					</picker>
				</view>

			</view>

			<view class="wrap-nav">
				<view class="screen-modal-item-name"><span style="color: #E42424;">*</span>车牌号：</view>
				<view class="screen-picker">
					<picker @change="Pickercar" :range="cph">
						<u--input placeholder="请选择车牌号" border="surround" v-model="Carnumbers" disabled
							suffixIcon="arrow-down"></u--input>
					</picker>
				</view>
			</view>
			<view class="wrap-nav">
				<view class="screen-modal-item-name"><span style="color: #E42424;">*</span>材料名：</view>
				<view class="screen-picker">
					<picker @change="Pickmater" :range="maters">
						<u--input placeholder="请选择材料名" border="surround" v-model="materials" disabled
							suffixIcon="arrow-down"></u--input>
					</picker>
				</view>
			</view>
			<view class="wrap-nav">
				<view class="screen-modal-item-name"><span style="color: #E42424;">*</span>规格：</view>
				<view class="screen-picker">
					<picker @change="PickSpecifications" :range="Specifications">
						<u--input placeholder="请选择规格" border="surround" v-model="specs" disabled
							suffixIcon="arrow-down">
						</u--input>
					</picker>
				</view>
			</view>
			<view class="wrap-nav">
				<view class="screen-modal-item-name"><span style="color: #E42424;">*</span>数量:</view>
				<view class="screen-picker">
					<view class="text-input">
						<u--input placeholder="请输入数量" border="surround" v-model="amount"
							style="background-color: #F6F9FC;">
						</u--input>
					</view>
				</view>
			</view>
			<view class="wrap-nav">
				<view class="screen-modal-item-name"><span style="color: #E42424;">*</span>司机姓名:</view>
				<view class="screen-picker">
					<view class="text-input">
						<u--input placeholder="请输入司机姓名" border="surround" v-model="drivername"
							style="background-color:#F6F9FC;"></u--input>
					</view>
				</view>
			</view>
			<view class="wrap-nav">
				<view class="screen-modal-item-name"><span style="color: #E42424;">*</span>电子锁：</view>
				<view class="screen-picker">
					<picker @change="pickerLock" :range="electronicLockList">
						<u--input placeholder="请选择电子锁" border="surround" v-model="electronicLock" disabled
							suffixIcon="arrow-down"></u--input>
					</picker>
				</view>
			</view>
			<!-- <view class="wrap-nav">
				<view class="text">
					<span style="color: #E42424;">*</span> 铅封号:
				</view>
				<view class="screen-picker">
					<view class="text-input">
						<u--input placeholder="请输入铅封号" border="surround" v-model="lead"
							style="background-color: #F6F9FC;">
						</u--input>
					</view>
				</view>
			</view> -->
			<view class="wrap-img">
				<view class="upimg">
					<my-image-upload :uploadFilenames="samPicss" @input="ChooseImages" label="检验报告：">
					</my-image-upload>
				</view>
			</view>
			<view class="wrap-img">
				<view class="upimg">
					<my-image-upload :uploadFilenames="samPics" @input="ChooseImaged" label="上锁图：">
					</my-image-upload>
				</view>
			</view>
			<view class="wrap-nav"></view>
		</view>
		<!-- <view class=""> -->
		<view class="screen-modal-btn">
			<u-button style="width:350px;border-radius: 10px;color: #fff;background-color: #387FFD;" text="发车"
				@click="submitfc"></u-button>
		</view>
		<view class="remind">
			请勿重复提交
		</view>
		<!-- </view> -->
	</view>
</template>

<script>
	import api from '@/api/api.js'
	export default {
		name: 'DeliveryDeclaration',
		components: {},
		data() {
			return {
				sbtype: '55',
				sbNumber: '',
				gyslist: '',
				mdidata: '',
				cpdata: '',
				clmdata: '',
				clgg: '',

				deviceNames: [],
				devictype: [],
				electronicLockList: [],
				maters: [],
				gys: [],
				mdd: [],
				cph: [],
				Specifications: [],
				mddcode: [],
				samPics: [],
				samPicss: [],
				fileLists: '',
				fileListd: '',

				choosevalue: '',
				supplier: '',
				Endto: '',
				Carnumbers: '',
				materials: '',
				specs: '',
				amount: '',
				drivername: '',
				electronicLock: '',
				lead: '',
				index: 0,
				taskSelect: {}
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
			this.cphtype()
			this.clmtype()
		},
		methods: {
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
				//console.log(uploadFilenames)
				this.fileListd = uploadFilenames.toString()

			},
			submitfc() {
				if (!this.fileLists || !this.fileListd) {
					uni.showToast({
						title: "请添加检测报告和上锁图",
						icon: 'none'
					})
					return false
				}
				let listdata = {
					sbbh: this.electronicLock,
					shrwdh: this.choosevalue,
					ghdw: this.supplier,
					mdd: this.Endto,
					userdepartid: this.mdidata, //目的地
					cph: this.Carnumbers,
					cailiao: this.materials,
					guige: this.specs,
					jcsl: this.amount,
					fzr: this.drivername,
					qianfenghao: this.lead,
					jyimgfile: this.fileLists,
					imgfile: this.fileListd,
					fctype: 1
				}
				this.$http.post(`/wbshebeidetail/wbshebeiDetail/add1`, listdata).then(res => {
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
