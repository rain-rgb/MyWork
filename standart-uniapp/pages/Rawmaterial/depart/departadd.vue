<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">发车单申报</block>
		</cu-custom>
		<view class="nav" v-has="'fcsb:add'">
			<view class="wrap-nav">
				<view class="screen-modal-item-name"><span style="color: #E42424;">*</span>出料锁：</view>
				<view class="screen-picker">
					<picker @change="Pickersheibei" :range="deviceNames">
						<u--input placeholder="请选择出料口电子锁" border="surround" v-model="choosevalue" disabled
							suffixIcon="arrow-down"></u--input>
					</picker>
				</view>
			</view>
			<view class="wrap-nav">
				<view class="screen-modal-item-name">进料锁：</view>
				<view class="screen-picker">
					<picker @change="PickersheibeiUp" :range="deviceNames">
						<u--input placeholder="请选择进料口电子锁" border="surround" v-model="choosevalueUp" disabled
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

			<view class="wrap-nav">
				<view class="screen-modal-item-name">中转站：</view>
				<view class="screen-picker">
					<picker @change="Pickermdds" :range="mdd">
						<u--input placeholder="请选择中转站" border="surround" v-model="Endtos" disabled
							suffixIcon="arrow-down">
						</u--input>
					</picker>
				</view>

			</view>

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
					<!-- <picker @change="Pickercar" :range="cph">
						<u--input placeholder="请选择车牌号" border="surround" v-model="Carnumbers" disabled
							suffixIcon="arrow-down"></u--input>
					</picker> -->
					<appSelect v-model="Carnumbers" @change="Pickercar" :range="cph"
					:placeholder="`请选择车牌号`"></appSelect>
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
				<view class="text">
					<span style="color: #E42424;">*</span> 数量:
				</view>
				<view class="screen-picker">
					<view class="text-input">
						<u--input placeholder="请输入数量" border="surround" v-model="amount"
							style="background-color: #F6F9FC;">
						</u--input>
					</view>
				</view>
			</view>
			<view class="wrap-nav">
				<view class="text">
					<span style="color: #E42424;">*</span> 司机姓名:
				</view>
				<view class="screen-picker">
					<view class="text-input">
						<u--input placeholder="请输入司机姓名" border="surround" v-model="drivername"
							style="background-color:#F6F9FC;"></u--input>
					</view>
				</view>
			</view>
			<view class="wrap-nav">
				<view class="text">
					 铅封号:
				</view>
				<view class="screen-picker">
					<view class="text-input">
						<u--input placeholder="请输入铅封号" border="surround" v-model="lead"
							style="background-color: #F6F9FC;">
						</u--input>
					</view>
				</view>
			</view>
			<view class="wrap-img">
				<view class="upimg">
					<my-image-upload :uploadFilenames="samPicss" @input="ChooseImages" label="质保单：">
					<!-- <my-image-upload :uploadFilenames="samPicss" @input="ChooseImages" label="检验报告："> -->
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
	import appSelect from '@/components/my-componets/appSelect1.vue'
	import MyImageUpload from "../../component/imgupload/my-image-upload.vue"
	import api from '@/api/api.js'
	export default {
		name: 'departadd',
		components: {
			MyImageUpload,
			appSelect
		},
		data() {
			return {
				// deviceName: '',
				sbtype: '55',
				sbNumber: '',
				sbNumberUp: '',
				gyslist: '',
				mdidata: '',
				cpdata: '',
				clmdata: '',
				clgg: '',

				deviceNames: [],
				devictype: [],
				maters: [],
				gys: [],
				mdd: [],
				cph: [],
				Specifications: [],
				mddcode: [],
				samPics: [],
				samPicss: [],
				fileLists: [],
				fileListd: [],

				choosevalue: '',
				choosevalueUp: '',
				supplier: '',
				Endto: '',
				Endtos: '',
				Carnumbers: '',
				materials: '',
				specs: '',
				amount: '',
				drivername: '',
				lead: '',
				orgcode:'',
			}
		},
		created() {
			// this.orgcode = this.$store.getters.orgcode//当前用户的组织机构
			// this.orgcode = this.$store.getters.orgcode +`*`
			this.deviceType()
			this.gystype()
			this.mddtype()
			this.cphtype()
			this.clmtype()
			console.log(this.orgcode,'this.orgcode');
		},
		mounted() {
			
		},
		methods: {
			deviceType() {
				api.deviceType({
					params: {
						sys_depart_orgcode: this.orgcode,
						// sysOrgCode: this.orgcode,
						sbtypes: this.sbtype
					}
				}).then(res => {
					//console.log(res.data.result)
					let devicelist = res.data.result
					this.deviceNames = []
					this.devictype = []
					devicelist.forEach(e => {
						this.deviceNames.push(e.sbjno)
						this.devictype.push(e.sbjno)
					})
				}).catch(e => {
					console.log("请求错误", e)
				})

			},
			// 设备名称
			Pickersheibei(e) {
				//console.log(e)
				this.index = e.detail.value
				this.choosekey = 2
				this.choosevalue = this.devictype[this.index]
				// this.deviceName = this.deviceNames[this.index]
				this.sbNumber = this.choosevalue
				//console.log(this.sbNumber, 'jjjjjjjjjjjj');

				this.begintime = ''
				this.endtime = ''
			},
			// 设备名称
			PickersheibeiUp(e) {
				//console.log(e)
				this.index = e.detail.value
				this.choosekey = 2
				this.choosevalueUp = this.devictype[this.index]
				// this.deviceName = this.deviceNames[this.index]
				this.sbNumberUp = this.choosevalueUp
				//console.log(this.sbNumber, 'jjjjjjjjjjjj');

				this.begintime = ''
				this.endtime = ''
			},
			gystype() {
				// let params ={
				// 	sysOrgCode: this.orgcode,
				// }
				this.$http.get(`/wzgongyingshang/wzgongyingshang/list1`).then(res => {
					console.log(res.data.result, '供应商信息')
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
				// let params ={
				// 	sysOrgCode: this.orgcode,
				// }
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
			Pickermdds(e) {
				console.log(e, "e__")
				this.indexs = e.detail.value
				this.Endtos = this.mdd[this.indexs]
				console.log("this.Endtos", this.Endtos)
				// this.mdcode = this.mddcode[this.index]
				// this.mdidata = this.mdcode
				// console.log(this.mdidata, 'jjjjjjjjjjjj');
				// this.begintime = ''
				// this.endtime = ''
			},
			cphtype() {
				// let params ={
				// 	sysOrgCode: this.orgcode,
				// }
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
				// console.log(e)
				// this.index = e.detail.value
				this.choosekey = 5;
				// this.Carnumbers = this.cph[this.index]
				this.Carnumbers = e;
				this.cpdata = this.Carnumbers
				console.log(this.Carnumbers)
				//console.log(this.cpdata, 'jjjjjjjjjjjj');
				// this.begintime = ''
				// this.endtime = ''
			},
			clmtype() {
				// let params ={
				// 	sysOrgCode: this.orgcode,
				// }
				this.$http.get(`/wzcailiaonamedict/wzcailiaonamedict/list2`).then(res => {
					//console.log(res.data.result, '材料名')
					let data = res.data.result
					this.maters = []
					this.Specifications = []
					data.forEach(gysdata => {
						this.maters.push(gysdata.cailiaoname)
						this.Specifications.push(gysdata.guigexinghao)
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
			// Choose(choosekey, choosevalue) {
			// 	console.log(this.choosevalue)
			// 	// 电子锁
			// 	if (this.choosekey == 2) {
			// 		this.sbNumber = this.choosevalue
			// 		this.begintime = ''
			// 		this.endtime = ''
			// 	}
			// 	if (this.choosekey == 3) {
			// 		this.gyslist = this.choosevalue
			// 		this.begintime = ''
			// 		this.endtime = ''
			// 	}
			// 	if (this.choosekey == 4) {
			// 		this.mdidata = this.choosevalue
			// 		this.begintime = ''
			// 		this.endtime = ''
			// 	}
			// 	if (this.choosekey == 5) {
			// 		this.cpdata = this.choosevalue
			// 		this.begintime = ''
			// 		this.endtime = ''
			// 	}
			// 	if (this.choosekey == 6) {
			// 		this.clmdata = this.choosevalue
			// 		this.begintime = ''
			// 		this.endtime = ''
			// 	}
			// 	if (this.choosekey == 7) {
			// 		this.clgg = this.choosevalue
			// 		this.begintime = ''
			// 		this.endtime = ''
			// 	}
			//        this.submitfc()
			// },
			ChooseImages(uploadFilenames) {
				//console.log(uploadFilenames)
				this.fileLists = uploadFilenames.toString()

			},
			ChooseImaged(uploadFilenames) {
				//console.log(uploadFilenames)
				this.fileListd = uploadFilenames.toString()

			},
			submitfc() {
				if (!this.fileLists.length > 0) {
					uni.showToast({
						title: "上传检验报告",
						icon: 'none'
					})
					return false
				}
				if (!this.fileListd.length > 0 ) {
					uni.showToast({
						title: "请上传上锁图",
						icon: 'none'
					})
					return false
				}
				let listdata = {
					sbbh: this.choosevalue,
					upsbbh: this.choosevalueUp,
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
					zzmdd: this.Endtos
				}
				this.$http.post(`/wbshebeidetail/wbshebeiDetail/add1`, listdata).then(res => {
					// console.log(res, '发车单新增')
					if (res.data.success) {
						uni.showToast({
							title: "添加成功",
							icon: 'none'
						})
						setTimeout(() => {
							uni.redirectTo({
								url: '/pages/Rawmaterial/depart/departlist'
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
