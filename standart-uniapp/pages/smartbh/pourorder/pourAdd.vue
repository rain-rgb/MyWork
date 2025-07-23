<template>
	<view id="pouradd">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">{{title}}</block>
		</cu-custom>
		<view class="main">
			<view class="main-item">
				<view class="biaoqian"></view>
				<view class="mainnew">主要信息</view>
			</view>
			<view class="main-item" v-if="!model.orderno">
				<view class="main-item-name">施工部位：</view>
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
			<view class="main-item" v-else>
				<view class="main-item-name">施工部位：</view>
				<view class="main-item-input">
					<!-- <picker @change="Pickersgbu" :range="sgshebei">
						<u--input placeholder="请选择施工部位" border="surround" v-model="model.conspos" disabled
							suffixIcon="arrow-down">
						</u--input>
					</picker> -->
					<u--input placeholder="请输入施工部位" border="surround" v-model="model.conspos"></u--input>
				</view>
			</view>

			<view style="color: red;padding-left: 170upx;">*若无选择信息，请在辅助信息中填写</view>
			<view class="main-item">
				<view class="main-item-name">强度等级：</view>
				<view class="main-item-input">
					<dict dictCode="betlev" title="请选择强度等级" @choose="Choose4" ref="dict1">
					</dict>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">任务方量：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入任务方量" border="surround" v-model="model.mete"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">浇注日期：</view>
				<view class="main-item-input" @click="handlebegtim">
					<u--input placeholder="请选择浇注日期" border="surround" v-model="model.begtim" suffixIcon="calendar"
						disabled suffixIconStyle="color:#4A7FFF;"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">截止日期：</view>
				<view class="main-item-input" @click="handleendtim">
					<u--input placeholder="请选择截止日期" border="surround" v-model="model.endtim" disabled
						suffixIcon="calendar" suffixIconStyle="color:#4A7FFF;"></u--input>
				</view>
			</view>

			<view class="main-item">
				<u-collapse style="width: 100%;">
					<u-collapse-item title="辅助信息" name="Docs guide">
						<view class="main-item">
							<view class="main-item-name">施工部位：</view>
							<view class="main-item-input">
								<u--input placeholder="请输入施工部位" border="surround" v-model="model.conspos"></u--input>
							</view>
						</view>
						<view style="color: red;padding-left: 170upx;">*若已选择施工部位请不要重复填写</view>
						<view class="main-item">
							<view class="main-item-name">工程名称：</view>
							<view class="main-item-input">
								<u--input placeholder="请输入工程名称" border="surround" v-model="model.projname"></u--input>
							</view>
						</view>
						<view style="color: red;padding-left: 170upx;">*若已选择施工部位请不要重复填写</view>
						<view class="main-item">
							<view class="main-item-name">施工地址：</view>
							<view class="main-item-input">
								<u--input placeholder="请输入施工地址" border="surround" v-model="model.projadr">
								</u--input>
							</view>
						</view>
						<view class="main-item">
							<view class="main-item-name">生产线：</view>
							<view class="main-item-input">
								<dict dictCode="station" title="请选择生产线" @choose="Choose" ref="dict2"></dict>
							</view>
						</view>
						<view class="main-item">
							<view class="main-item-name">任务性质：</view>
							<view class="main-item-input">
								<u--input placeholder="请输入任务性质" border="surround" v-model="model.attribute">
								</u--input>
							</view>
						</view>
						<view class="main-item">
							<view class="main-item-name">浇筑方式：</view>
							<view class="main-item-input">
								<dict dictCode="pour" title="请选择浇筑方式" @choose="Choose3" ref="dict3">
								</dict>
							</view>
						</view>
						<view class="main-item">
							<view class="main-item-name">抗渗等级：</view>
							<view class="main-item-input">
								<u--input placeholder="请输入抗渗等级" border="surround" v-model="model.filter"></u--input>
							</view>
						</view>
						<view class="main-item">
							<view class="main-item-name">抗冻等级：</view>
							<view class="main-item-input">
								<u--input placeholder="请输入抗冻等级" border="surround" v-model="model.freeze"></u--input>
							</view>
						</view>
						<view class="main-item">
							<view class="main-item-name">坍落度：</view>
							<view class="main-item-input">
								<dict dictCode="lands" title="请选择坍落度" @choose="Choose5" ref="dict4">
								</dict>
							</view>
						</view>
						<view class="main-item">
							<view class="main-item-name">调度人员：</view>
							<view class="main-item-input">
								<u--input placeholder="请输入调度人员" border="surround" v-model="model.attamper"></u--input>
							</view>
						</view>
						<view class="main-item">
							<view class="main-item-name">客户名称：</view>
							<view class="main-item-input">
								<u--input placeholder="请输入客户名称" border="surround" v-model="model.customer"></u--input>
							</view>
						</view>
						<view class="main-item">
							<view class="main-item-name">联系方式：</view>
							<view class="main-item-input">
								<u--input placeholder="请输入联系方式" border="surround" v-model="model.phone"></u--input>
							</view>
						</view>
						<view class="main-item">
							<view class="main-item-name">搅拌时间：</view>
							<view class="main-item-input">
								<u--input type="Number" placeholder="请输入搅拌时间" border="surround" v-model="model.mixlast"></u--input>
							</view>
						</view>
						<view class="main-item">
							<view class="main-item-name">备注：</view>
							<view class="main-item-input">
								<u--input placeholder="请输入备注" border="surround" v-model="model.note"></u--input>
							</view>
						</view>
					</u-collapse-item>
				</u-collapse>
			</view>

		</view>
		<view class="btn">
			<u-button text="提交" type="primary" @click="sumit"></u-button>
		</view>
		<mx-date-picker :show="datestashow" :type="type" :show-tips="true" :show-seconds="true"
			@confirm="confirmstadate" @cancel="confirmstadate" :isContainTime="true" />
		<mx-date-picker :show="dateendshow" :type="type" :show-tips="true" :show-seconds="true"
			@confirm="confirmenddate" @cancel="confirmenddate" :isContainTime="true" />
	</view>
</template>

<script>
	import equipment from '../../component/equipment/equipment.vue'
	import projectname from '../../component/projectname/projectname.vue'
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	import dict from '../../component/dict/dict.vue'
	import smartbh from '../../../api/smartbh.js'
	import nowtime from '../../../common/util/nowtime.js'
	export default {
		components: {
			equipment,
			projectname,
			dict,
			MxDatePicker
		},
		data() {
			return {
				title: '',
				code: null,
				datestashow: false,
				dateendshow: false,
				type: 'datetime',
				sgshebei: [],
				sgshebeicode: [],
				sgbu: '',
				ddcode:'',
				// sgbuw:'',
				model: {
					attribute: '',
					dattim: '',
					conspos: '',
					projgrade: '',
					projname: '',
					pour: '',
					filter: '',
					betlev: '',
					freeze: '',
					lands: '',
					mete: '',
					begtim: '',
					endtim: '',
					attamper: '',
					note: '',
					treeid: '',
					projadr: '',
					orderno: '',
					sgbworderno:'',
				},
				urls: {}
			}
		},
		onLoad(options) {
			this.model.begtim = nowtime.date() + ' ' + nowtime.time()
			this.model.endtim = nowtime.getNextPreDate(nowtime.date(), 1) + ' ' + nowtime.time()
			this.urls = {}
			this.code = options.code
			if (this.code != null) {
				console.log("this.code", this.code)
				this.getcodedata(this.code)
			}
			if (options.titlesta == 1) {
				this.title = '浇筑令编辑'
				this.urls = smartbh.bhzrenwudanedit
			} else {
			    // console.log(options,'hhhh')
					this.model.conspos = options.sgbu
					this.model.sgbworderno = options.projgrade
					this.model.orderno  = options.orderno
					this.ddcode = options.ddcode
				this.title = '浇筑令新增'
				this.urls = smartbh.bhzrenwudanadd
				console.log("options.titlesta", options.titlesta)
			}
			
		},
		created() {
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
				this.model.treeid = choosevalue.id
				// this.model.projname = 
				setTimeout(() => {
					this.$refs.popup.close()
				}, 500);

			},
			sgbutype() {
				this.$http.get(`/sys/sysDepartproject/getDepartNames`, {
					params: {
						orgCode: this.sgbu,
						orgCategory: '8'
					}
				}).then(res => {
					console.log(res.data.result, '施工部位')
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
			Choose4(choosevalue) {
				//console.log(choosevalue, "强度等级")
				this.model.betlev = choosevalue
			},
			Choose3(choosevalue) {
				//console.log(choosevalue, "浇筑方式")
				this.model.pour = choosevalue
			},
			Choose5(choosevalue) {
				//console.log(choosevalue, "坍落度")
				this.model.lands = choosevalue
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
				//console.log("this.url",this.urls)
				if (!this.model.conspos) {
					this.$tip.toast('请选择施工部位')
					return
				}
				if (!this.model.betlev) {
					this.$tip.toast('请选择强度等级')
					return
				}
				if (!this.model.mete) {
					this.$tip.toast('请输入任务方量')
					return
				}
				if (!this.model.begtim) {
					this.$tip.toast('请选择浇注日期')
					return
				}
				this.urls(this.model).then(res => {
					if (res.data.success) {
						uni.showToast({
							title: this.title + '成功',
							icon: 'none'
						})
						setTimeout(() => {
							uni.redirectTo({
								url: '/pages/smartbh/pourorder/pourManage'
							});
						}, 500);
						if (this.model.orderno) {
							console.log(this.ddcode,'dddd')
							setTimeout(() => {
								uni.navigateTo({
									url: '/pages/smartlc/orderTracking/orderTracking?code='+ this.ddcode
								});
							}, 500);
						}
					}
				})
			},
			getcodedata(e) {
				smartbh.pourlist({
					params: {
						rwdcode: e
					}
				}).then(res => {
					if (res.data.success) {
						this.model = res.data.result.records[0]
						this.$refs.dict1.call(this.model.betlev);
						this.$refs.dict2.call(this.model.station);
						this.$refs.dict3.call(this.model.pour);
						this.$refs.dict4.call(this.model.lands);
						//console.log("res",res)
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
			background-color: white;
			border-radius: 16upx;

			.mainnew {
				color: #333333;
				font-size: 30upx;
				font-weight: bold;
			}

			&-item {
				display: flex;
				align-items: center;
				padding-top: 30upx;

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

		.btn {
			width: 690upx;
			height: 100upx;
			margin: 0 auto;
			margin-top: 30upx;
		}
	}
</style>
