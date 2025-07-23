<template>
	<view id="pouradd">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">{{title}}</block>
		</cu-custom>
		<view class="main">
			<view class="main-item-top">
				<view class="biaoqian"></view>
				<view class="mainnew">主要信息</view>
			</view>
			<view class="main-item">
				<view class="main-item-name"><span style="color: #E42424;">*</span>设备名称：</view>
				<view class="main-item-input">
					<dict1 dictCode="9" title="请选择设备名称" @choose="Choose1">
					</dict1>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">一次张拉日期：</view>
				<view class="main-item-input" @click="handlezldate">
					<u--input placeholder="请选择浇注日期" border="surround" v-model="model.zldate" suffixIcon="calendar"
						disabled suffixIconStyle="color:#4A7FFF;"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">混凝土的设计强度<!-- 强度等级 -->：</view>
				<view class="main-item-input">
					<dict dictCode="betlev" title="请选择强度等级" @choose="Choose4">
					</dict>
				</view>
			</view>
			<!-- <view class="main-item">
				<view class="main-item-name">任务单号：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入任务单号" border="surround" v-model="model.code"></u--input>
				</view>
			</view> -->
			<!-- <view class="main-item" v-if="!model.orderno"> -->
			<view class="main-item" >
				<view class="main-item-name"><span style="color: #E42424;">*</span>施工部位：</view>
				<view class="main-item-input">
					<view @click="change1">
						<u--input placeholder="请选择施工部位" border="surround" v-model="model.conspos" disabled
							suffixIcon="arrow-down">
						</u--input>
					</view>
					<uni-popup ref="popup" type="bottom">
						<view style="background-color: #FFFFFF;">
							<scroll-view scroll-y="true" scroll-x="true">
								<projectname @choose="Choose2"></projectname>
							</scroll-view>
						</view>
					</uni-popup>
				</view>
			</view>
			<!-- <view class="main-item" v-else>
				<view class="main-item-name">施工部位：</view>
				<view class="main-item-input"> -->
					<!-- <picker @change="Pickersgbu" :range="sgshebei">
						<u--input placeholder="请选择施工部位" border="surround" v-model="model.conspos" disabled
							suffixIcon="arrow-down">
						</u--input>
					</picker> -->
					<!-- <u--input placeholder="请输入施工部位" border="surround" v-model="model.conspos"></u--input>
				</view>
			</view> -->

			<!-- <view style="color: red;padding-left: 170upx;">*若无选择信息，请在辅助信息中填写</view> -->
			<view class="main-item">
				<view class="main-item-name">一次张拉力：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入一次张拉力" border="surround" v-model="model.sjzll"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">两端千斤顶的张拉力误差指标：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入两端千斤顶的张拉力误差指标" border="surround" v-model="model.zllwc"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">梁型：</view>
				<view class="main-item-input">
					<dict1 dictCode="lx" :dictCodeArr="dictCodeArr" title="请选择梁型" @choose="Choose5">
					</dict1>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">孔道数：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入孔道数" border="surround" v-model="model.kds"></u--input>
				</view>
			</view>

		</view>
		<view class="btn">
			<u-button text="提交" type="primary" @click="sumit"></u-button>
		</view>
		<mx-date-picker :show="datestashow1" :type="type" :show-tips="true" :show-seconds="true"
			@confirm="confirmstadate1" @cancel="confirmstadate1" :isContainTime="true" />
	</view>
</template>

<script>
	import equipment from '../../component/equipment/equipment.vue'
	import projectname from '../../component/projectname/projectname.vue'
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	import dict from '../../component/dict/dict.vue'
	import dict1 from '../../component/dict/dict1.vue'
	import smartbh from '../../../api/smartbh.js'
	import nowtime from '../../../common/util/nowtime.js'
	import Byspublic from '../../component/Byspublic/Byspublic.vue'
	export default {
		components: {
			equipment,
			projectname,
			dict,
			dict1,
			MxDatePicker,
			Byspublic
		},
		data() {
			return {
				title: '',
				code: null,
				datestashow1: false,
				datestashow: false,
				dateendshow: false,
				type: 'datetime',
				sgshebei: [],
				sgshebeicode: [],
				sgbu: '',
				ddcode:'',
				// sgbuw:'',
				model: {
					zllwc:"",
					zldate:"",
					sgbwuuid:"",
					sjzll:"",
					shebeibianh:"",
					conspos:"",
					// code:"",
					betlev:"",
					lx:"",
					kds:"",
					// attribute: '',
					// dattim: '',
					// conspos: '',
					// projgrade: '',
					// projname: '',
					// pour: '',
					// filter: '',
					// betlev: '',
					// freeze: '',
					// lands: '',
					// mete: '',
					// begtim: '',
					// endtim: '',
					// attamper: '',
					// note: '',
					// treeid: '',
					// projadr: '',
					// orderno: '',
					// sgbworderno:'',
				},
				urls: {},
				dictCodeArr:[{
					text: '箱型梁',
					value: '0'
					}, {
					text: 'T型梁',
					value: '1'
					}, {
					text: '槽形梁',
					value: '2'
					}, {
					text: '空心板梁',
					value: '3'
					}, {
					text: '其他',
					value: '10'
					}
				],
			}
		},
		onLoad(options) {
			// this.model.begtim = nowtime.date() + ' ' + nowtime.time()
			// this.model.endtim = nowtime.getNextPreDate(nowtime.date(), 1) + ' ' + nowtime.time()
			this.urls = {}
			this.code = options.code
			console.log(options,'options-----------------');
			// if (this.code != null) {
			// 	console.log("this.code", this.code)
			// 	this.getcodedata(this.code)
			// }
			if (options.titlesta == 1) {
				this.title = '张拉任务单编辑'
				this.urls = smartbh.zhanglaRenwudanedit
			} else {
			    // console.log(options,'hhhh')
					// this.model.conspos = options.sgbu
					// this.model.sgbworderno = options.projgrade
					// this.model.orderno  = options.orderno
					this.ddcode = options.ddcode
				this.title = '张拉任务单新增'
				this.urls = smartbh.zhanglaRenwudanadd
				console.log("options.titlesta", options.titlesta)
			}
			
		},
		created() {
			this.orgCode = this.$store.getters.orgcode
			console.log(this.orgCode,'this.orgCode-------------');
			this.sgbutype()
		},
		methods: {
			Choose1(choosevalue) {
				//console.log(choosevalue, "当前设备名称")
				this.model.shebeibianh = choosevalue
			},
			change1() {
				// 通过组件定义的ref调用uni-popup方法 ,如果传入参数 ，type 属性将失效 ，仅支持 ['top','left','bottom','right','center']
				this.$refs.popup.open('bottom')
			},
			Choose2(choosevalue) {
				//console.log(choosevalue, "当前施工部位")
				this.model.conspos = choosevalue.departName
				this.model.sgbwuuid = choosevalue.orgcode
				// this.model.sgbwuuid = choosevalue.id
				// this.model.treeid = choosevalue.id
				// this.model.projname = 
				setTimeout(() => {
					this.$refs.popup.close()
				}, 500);

			},
			sgbutype() {
				this.$http.get(`/sys/sysDepartproject/getDepartNames`, {
					params: {
						orgCode: this.sgbu,
						orgCategory: '9'
					}
				}).then(res => {
					// console.log(res.data.result, '施工部位')
					let data = res.data.result
					this.sgshebei = []
					this.sgshebeicode = []
					data.forEach(gysdata => {
						this.sgshebei.push(gysdata.departName)
						this.sgshebeicode.push(gysdata.orgCode)
					})
				})
			},
			Pickersgbu(e) {
				console.log(e)
				this.index = e.detail.value
				// this.choosekey = 4
				this.model.conspos = this.sgshebei[this.index]
				this.model.projgrade = this.sgshebeicode[this.index]
			},
			Choose(choosevalue) {
				//console.log(choosevalue, "生产线")
				this.model.station = choosevalue
			},
			Choose5(choosevalue) {
				//console.log(choosevalue, "强度等级")
				this.model.lx = choosevalue
			},
			Choose4(choosevalue) {
				//console.log(choosevalue, "强度等级")
				this.model.betlev = choosevalue
			},
			Choose3(choosevalue) {
				//console.log(choosevalue, "浇筑方式")
				this.model.pour = choosevalue
			},
			// Choose5(choosevalue) {
			// 	//console.log(choosevalue, "坍落度")
			// 	this.model.lands = choosevalue
			// },
			handlezldate() {
				this.datestashow1 = true
			},
			confirmstadate1(e) {
				// console.log(e, '确认开始时间')
				this.model.zldate = e.value
				this.datestashow1 = false
			},
			handlebegtim() {
				this.datestashow = true
			},
			confirmstadate(e) {
				console.log(e, '确认开始时间')
				this.model.begtim = e.value
				this.model.endtim = nowtime.getNextPreDate(e.value, 1) + ' ' + nowtime.time(e.value)
				this.datestashow = false
			},
			handleendtim() {
				this.dateendshow = true
			},
			confirmenddate(e) {
				// console.log(e,'确认截止时间') 
				this.model.endtim = e.value
				this.dateendshow = false
			},
			sumit() {
				// console.log("this.model",this.model)
				if (!this.model.shebeibianh) {
					this.$tip.toast('请选择设备名称')
					return
				}
				if (!this.model.sgbwuuid) {
					this.$tip.toast('请选择施工部位')
					return
				}
				// if (!this.model.betlev) {
				// 	this.$tip.toast('请选择强度等级')
				// 	return
				// }
				// if (!this.model.mete) {
				// 	this.$tip.toast('请输入任务方量')
				// 	return
				// }
				// if (!this.model.begtim) {
				// 	this.$tip.toast('请选择浇注日期')
				// 	return
				// }
				this.urls(this.model).then(res => {
					if (res.data.success) {
						uni.showToast({
							title: this.title + '成功',
							icon: 'none'
						})
						setTimeout(() => {
							uni.redirectTo({
								url: '/pages/zlyj/zl/zlTaskList'
							});
						}, 500);
					}
				})
			},

		}
	}
</script>

<style lang="scss" scoped>
	#pouradd {
		width: 100%;
		// height: 100vh;
		background-color: #F3F5FE;

		.main {
			width: 690upx;
			// height: 754upx;
			margin: 0 auto;
			margin-top: 30upx;
			padding-bottom: 30upx;
			background-color: white;
			border-radius: 16upx;


			&-item {
				&-top{
					display: flex; 
					padding-top: 30upx;
				}
				display: flex; 
				flex-direction: column;
				// align-items: center;
				justify-content: start;
				padding-top: 30upx;
				padding-left: 30upx;
				&-name {
					flex: 3;
					font-size: 30upx;
					// text-align: right;
					color: #4C5971;
				}

				&-input {
					flex: 8;
					margin-right: 30upx;
					background-color: #F6F9FC;
					border-radius: 10upx;

					&-button {
						background-color: #F6F9FC;
					}

					&-text {
						padding: 6px 9px;
						font-size: 32rpx;
						margin-right: 30upx;
						background-color: #F6F9FC;
					}
				}
			}
		}

		.biaoqian {
			width: 12upx;
			height: 34upx;
			border-radius: 6upx;
			margin: 0 30upx;
			background-color: #4A7FFF;
		}
		.mainnew {
			color: #333333;
			font-size: 30upx;
			font-weight: bold;
		}
		.btn {
			width: 690upx;
			height: 100upx;
			margin: 0 auto;
			margin-top: 30upx;
		}
	}
</style>
