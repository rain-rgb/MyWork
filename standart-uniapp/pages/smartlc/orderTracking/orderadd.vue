<template>
	<view id="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">新增制梁任务单</block>
		</cu-custom>
		<view class="section">
			<view class="titles">
				<view class="round"></view>
				<view class="title-font">基础信息</view>
			</view>
			<view class="group" v-if="Siteid!==null">
				<view class="title">施工部位:</view>
				<view class="content">
					<!-- <view @click="change1">
						<u--input placeholder="请选择施工部位" border="surround" v-model="model.conspos"
							suffixIcon="arrow-down">
						</u--input>
						
					</view>
					<uni-popup ref="popup" type="bottom">
						<view style="background-color: #FFFFFF;">
							<scroll-view scroll-y="true" scroll-x="true">
								<projectname @choose="Choose5"></projectname>
							</scroll-view>
						</view>
					</uni-popup> -->
					<view class="screen-picker">
						<picker @change="Pickersgbu" :range="sgshebei">
							<u--input placeholder="请选择施工部位" border="surround" v-model="model.conspos" disabled
								suffixIcon="arrow-down">
							</u--input>
						</picker>
					</view>
				</view>
			</view>
			<view class="group" v-else>
				<view class="title">施工部位：</view>
				<view class="content">
					<input type="text" placeholder="请输入施工部位" v-model="model.conspos" />
				</view>
			</view>
			<view class="group">
				<view class="title">制梁台座:</view>
				<view class="content">
					<picker @change="Pickertz" :range="tzshebei">
						<u--input placeholder="请选择制梁台座" border="surround" v-model="taizuoname" disabled
							suffixIcon="arrow-down">
						</u--input>
					</picker>
				</view>
			</view>
			<!-- <view class="group">
				<view class="title">任务单编号：</view>
				<view class="content">
					<input type="number" placeholder="请输入任务单编号" v-model="model.code" />
				</view>
			</view>
			<view class="group">
				<view class="title">工程名称：</view>
				<view class="content">
					<input type="text" placeholder="请输入工程名称" v-model="model.projname" />
				</view>
			</view> -->
			<!-- <view class="group">
				<view class="title">施工部位：</view>
				<view class="content">
					<input type="text" placeholder="请输入施工部位" v-model="model.sgbu" />
				</view>
			</view> -->
			<!-- <view class="group">
				<view class="title">生产线：</view>
				<view class="content">
					<dict dictCode="station" title="请选择生产线" @choose="Choose" ref="dict2"></dict>
				</view>
			</view>
			<view class="group">
				<view class="title">浇筑方式：</view>
				<view class="content">
					<dict dictCode="pour" title="请选择浇筑方式" @choose="Choose6" ref="dict3">
					</dict>
				</view>
			</view> -->
			<!-- <view class="group">
				<view class="title">浇筑时间:</view>
				<view class="content">
					<!-- <view class="screen-modal-item-name">时间：</view> -->
			<!-- <view class="screen-modal-item-input" @click="beginshow = true">
						<u--input placeholder="请选择浇筑时间" border="surround" v-model="model.begtim"
							suffixIcon="arrow-down"></u--input>
					</view>
				</view>
			</view> -->
			<view class="group">
				<view class="title">计划生产时间:</view>
				<view class="content">
					<!-- <view class="screen-modal-item-name">时间：</view> -->
					<view class="screen-modal-item-input" @click="planshow = true">
						<u--input placeholder="请选择计划生产时间" border="surround" v-model="model.productiontime"
							suffixIcon="arrow-down"></u--input>
					</view>
				</view>
			</view>
			<!-- <view class="group">
				<view class="title">截至日期:</view>
				<view class="content">
					<!-- <view class="screen-modal-item-name">时间：</view> -->
			<!-- <view class="screen-modal-item-input" @click="dateshow = true">
						<u--input placeholder="请选择截至日期" border="surround" v-model="model.endtim"
							suffixIcon="arrow-down"></u--input>
					</view>
				</view>
			</view> -->
			<!-- <view class="group">
				<view class="title">任务方量:</view>
				<view class="content">
					<input type="text" placeholder="请输入任务方量" v-model="model.mete" />
				</view>
			</view> -->
			<!-- <view class="group">
				<view class="title">订单编号:</view>
				<view class="content">
					<input type="text" disabled v-model="model.oderno" />
				</view>
			</view> -->

			<!-- <view class="group">
				<view class="title">抗渗等级:</view>
				<view class="content">
					<input type="text" placeholder="请输入抗渗等级" v-model="model.filter" />
				</view>
			</view> -->
			<!-- <view class="group">
				<view class="title">抗冻等级:</view>
				<view class="content">
					<input type="text" placeholder="请输入抗冻等级" v-model="model.freeze" />
				</view>
			</view> -->
			<!-- <view class="group">
				<view class="title">强度等级：</view>
				<view class="content">
					<dict dictCode="betlev" title="请选择强度等级" @choose="Choose3">
					</dict>
				</view>
			</view>
			<view class="group">
				<view class="title">施工地址:</view>
				<view class="content">
					<input type="text" placeholder="请输入施工地址" v-model="model.projadr" />
				</view>
			</view> -->
			<!-- <view class="group">
				<view class="title">产品种类:</view>
				<view class="content">
					<input type="text" placeholder="请输入产品种类" v-model="model.variety" />
				</view>
			</view>
			<view class="group">
				<view class="title">坍落度：</view>
				<view class="content">
					<dict dictCode="lands" title="请选择坍落度" @choose="Choose2">
					</dict>
				</view>
			</view>
			<view class="group">
				<view class="title">搅拌时间:</view>
				<view class="content">
					<input type="number" placeholder="请输入搅拌时间" v-model="model.mixlast" />
				</view>
			</view>
			<view class="group">
				<view class="title">备注:</view>
				<view class="content">
					<input type="text" placeholder="请输入备注" v-model="model.note" />
				</view>
			</view> -->
		</view>
		<view class="sections">
			<view class="newfix">
				<view class="titletext">
					<view class="round"></view>
					<view class="title-font">制梁任务单工序</view>
				</view>
				<view class="buttonfix">
					<button class="mini-btn" style="color: #387FFD;background-color:#e3f2fd ;" size="mini"
						@click="increase"> 新增</button>
					<view class="imgurl" style="background-image: url(../../static/shiti/jia.png);width: 10px;height: 11px; background-size: 100% 100%;    position: relative;
					right: 57px;
					top: 8px;
					z-index: 2;"></view>
					<button class="mini-btn" style="color: #FF233D;background-color:#f7dbde;" size="mini"
						@click="reduce">删除</button>
					<view class="imgurl" style="background-image: url(../../static/shiti/chu.png);width: 10px;height: 2px; background-size: 100% 100%;position: relative;
					right: 57px;
					top: 13px;
					z-index: 2;"></view>
				</view>
			</view>
			<view class="addlist" v-for="(d,indexs) in model.zhiliangGongxuList" :key="indexs">
				<!-- <tbhzlist :tabindex="indexs" title="请选择材料类型" @choose7="choose7" @choose8="choose8"></tbhzlist> -->
				<view class="group">
					<view class="title">工序{{indexs+1}}:</view>
					<view class="content">
						<dict dictCode="xuhao" title="请选择工序" @choose="Choose4($event,indexs)">
						</dict>
					</view>
				</view>
				<view class="group">
					<view class="title">工序时长{{indexs+1}}:</view>
					<view class="content">
						<u--input placeholder="请输入工序时长" border="surround" @change="gxchange($event,indexs)">
						</u--input>
					</view>
				</view>

				<view class="group">
					<view class="title">时长单位:</view>
					<view class="content">
						<dict dictCode="unit" title="请选择时长单位" @choose="durationUnit($event,indexs)">
						</dict>
					</view>
				</view>
			</view>
		</view>
		<mx-date-picker :show="beginshow" :type="type" :value="values" :show-tips="true" :show-seconds="true"
			@confirm="confirmdate" @cancel="confirmdate" />

		<mx-date-picker :show="planshow" :type="type" :value="values" :show-tips="true" :show-seconds="true"
			@confirm="confirmplan" @cancel="confirmplan" />

		<mx-date-picker :show="dateshow" :type="type" :value="values" :show-tips="true" :show-seconds="true"
			@confirm="confirmEnd" @cancel="confirmEnd" />

		<view>
			<button style="width:350px; height: 40px; background-color:#387FFD;color: #fff;text-align: center;"
				@click="submit">提交</button>
		</view>
	</view>
</template>
<script>
	import timeSelector from '@/components/wing-time-selector/wing-time-selector.vue';
	import api from '@/api/api.js'
	import dict from '@/pages/component/dict/dict.vue'
	import Byspublic from '@/pages/component/Byspublic/Byspublic.vue'
	import tbhzlist from '@/pages/component/tbhzlist/tbhzlist.vue'
	import projectname from '../../component/projectname/projectname.vue'
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	export default {
		components: {
			dict,
			Byspublic,
			tbhzlist,
			timeSelector,
			projectname,
			MxDatePicker
		},
		data() {
			return {
				sgshebei: [],
				sgshebeicode: [],
				tzshebei: [],
				tzshebeicode: [],
				sbtype: '0',
				typelist: [],
				typecode: [],
				typename: [],
				typenamecode: [],
				index: -1,
				Tindex: -1,
				tabindex: 1,
				show: false,
				dateshow: false,
				type: 'datetime',
				values: '',
				beginshow: false,
				planshow: false,
				num: 1,
				taizuoname: '', // 台座
				Siteid: '',
				remark: '',
				gxadd: '',
				gxdata: [],
				model: {
					// filter: '', //抗渗等级
					// betlev: '', // 强度等级
					// freeze: '', //抗冻等级
					// mixlast: '', //搅拌时间
					// lands: '', //坍落度
					zhiliangGongxuList: [],
					conspos: '', //施工部位
					projgrade: null, //施工部位code
					// code: '', //任务单编号
					projname: '', //工程名称
					// mete: "", //任务方量
					// projadr: '', //施工地址
					// variety: "", //产品种类
					// note: '', //备注
					// begtim: '', //浇筑日期
					productiontime: '', //计划生产时间
					// endtim: '', //截至日期
					// pour: '', //浇筑方式
					station: '', //生产线
					// gongxuNo:'',//工序选择
					oderno: "", //订单编号
					taizuono: '',
					// qcode: '',
				},
				choosevalue: '',
				choosevalues: '',
			}
		},
		onLoad(options) {
			// console.log(JSON.parse(options.items), 'dddd')
			let add = JSON.parse(options.items)
			this.model.oderno = add.orderno
			this.Siteid = add.constructionSiteid
			// console.log(options.uuid)
			// this.model.qcode = options.uuid
		},


		created() {
			this.sgbutype()
			this.tztype()
		},
		methods: {
			increase() {
				this.model.zhiliangGongxuList.push({})
			},
			reduce() {
				this.model.zhiliangGongxuList.pop()
			},
			//日期选择
			confirmdate(e) {
				console.log(e, '')
				this.model.begtim = e.value
				this.beginshow = false
			},
			confirmplan(e) {
				console.log(e, '')
				this.model.productiontime = e.value
				this.planshow = false
			},
			confirmEnd(e) {
				console.log(e, '')
				this.model.endtim = e.value
				this.dateshow = false
			},
			Choose(choosevalue) {
				//console.log(choosevalue, "生产线")
				this.model.station = choosevalue
			},
			Choose6(choosevalue) {
				//console.log(choosevalue, "浇筑方式")
				this.model.pour = choosevalue
			},
			change1() {
				// 通过组件定义的ref调用uni-popup方法 ,如果传入参数 ，type 属性将失效 ，仅支持 ['top','left','bottom','right','center']
				this.$refs.popup.open('bottom')
			},
			Choose4(choosevalue, i) {
				if (this.model.zhiliangGongxuList[i].xuhao !== undefined) {
					this.model.zhiliangGongxuList[i].xuhao = choosevalue
				} else {
					this.$set(this.model.zhiliangGongxuList[i], 'xuhao', choosevalue)
				}
			},
			gxchange(e, i) {
				if (this.model.zhiliangGongxuList[i].remark !== undefined) {
					this.model.zhiliangGongxuList[i].remark = e
				} else {
					this.$set(this.model.zhiliangGongxuList[i], 'remark', e)
				}
			},
			durationUnit(e, i) {
				if (this.model.zhiliangGongxuList[i].unit !== undefined) {
					this.model.zhiliangGongxuList[i].unit = e
				} else {
					this.$set(this.model.zhiliangGongxuList[i], 'unit', e)
				}
			},
			Choose5(choosevalue) {
				console.log(choosevalue, "当前施工部位")
				this.model.conspos = choosevalue.departName
				this.model.treeid = choosevalue.id
				// this.model.projname = 
				setTimeout(() => {
					this.$refs.popup.close()
				}, 500);

			},
			Choose3(choosevalue) {
				// console.log(choosevalue, "强度等级")
				this.model.betlev = choosevalue
			},
			Choose2(choosevalue) {
				// console.log(choosevalue, "坍落度")
				this.model.lands = choosevalue
			},
			confirm() {
				this.show = false
			},
			sgbutype() {
				this.$http.get(`/sys/sysDepartproject/getDepartNames`, {
					params: {
						orgCode: this.Siteid,
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

				// console.log(this.model.conspos,this.model.projgrade)
			},
			// 制梁台座
			tztype() {
				this.$http.get(`/zhiliangtaizuocfg/zhiliangTaizuoCfg/list1`).then(res => {
					console.log(res.data.result, '制梁台座')
					let data = res.data.result
					this.tzshebei = []
					this.tzshebeicode = []
					data.forEach(tzdata => {
						this.tzshebei.push(tzdata.taizuoname)
						this.tzshebeicode.push(tzdata.taizuono)
					})
				})

			},
			Pickertz(e) {
				console.log(e)
				this.Tindex = e.detail.value
				// this.choosekey = 4
				this.taizuoname = this.tzshebei[this.Tindex]
				this.model.taizuono = this.tzshebeicode[this.Tindex]
			},
			submit() {
				if (!this.model.productiontime) {
					// console.log("111111111111");
					uni.showToast({
						title: '请选择计划生产时间',
						icon: 'none'
					})
					return
				}
				// if(!this.model.projgrade){
				let params = {
					projgrade: this.model.projgrade
				}
				this.$http.get(`/zhiliangrenwudan/zhiliangrenwudan/applist`, {
					params
				}).then(res => {
					// console.log(res.data.result.records.length, '选择施工部位')
					let sglist = res.data.result.records.length
					if (sglist > 0) {
						this.$tip.toast('施工部位已占用,请重新选择');
					} else {
						this.$http.post(`/sys/sysDepartproject/zlrenwudanadd`, this.model).then(res => {
							console.log(res, 'pppppp');
							if (res.data.success == true) {
								uni.showToast({
									title: "添加成功",
									icon: 'none'
								})
								setTimeout(function() {
									uni.navigateBack({
										url: "/pages/smartlc/orderTracking/orderlist",
									})
								}, 500)
							}
						})
					}
					// let sgbwdata = res.data.result.records
					// for (let item of sgbwdata) {
					// 	let prono = item.projgrade
					// 	console.log(prono, this.model.projgrade, 'lllll')
					// 	if (this.model.projgrade == prono) {
					// 		this.$tip.toast('施工部位已占用,请重新选择');
					// 	}
					// }
				})
				// }

			}
		}
	}
</script>
<style lang="scss" scoped>
	#wrap {
		width: 100%;
	}

	.section {
		width: 700rpx;
		height: auto;
		background-color: #fff;
		margin: 15px auto;
		border-radius: 10px;
	}

	.group {
		margin-top: 1px;
		display: flex;
		// background: #FFFFFF;
		padding: 5px 10px;
		height: 47px;
		line-height: 50px;
		font-size: 14px;
	}

	.group .title {
		flex: 4;
		line-height: 30px;
		height: 30px;
		text-align: center;

	}

	.group .content {
		height: 38px;
		line-height: 38px;
		background-color: #f5f7fa;
		flex: 9;
		border-radius: 7px;
		border: 1px solid #dadbde;
	}

	.group .content input {
		padding-top: 8px;
		padding-left: 10px;
		font-size: 15px;
		height: 30px;
	}

	.uni-input-placeholder {
		color: #c0c4cc;
	}

	.group .content button {
		text-align: left;
		padding-left: 5px;
	}

	.sgbw {
		height: 30px;
		line-height: 30px;
		background-color: #f1f1f1;
		flex: 9;
		border-radius: 3px;
	}

	.sgbw text {
		color: inherit;
		font-size: 14px;
	}

	.time {
		padding-left: 5px;
	}



	.titles {
		width: 200rpx;
		height: 34px;
		// border: 1px solid #18BC37;
		display: flex;
		flex-direction: row;
		justify-content: space-around;
		align-items: center;

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
		}

		// .mini-btn {
		// 	float: right;
		// }
	}



	.sections {
		width: 700rpx;
		height: auto;
		background-color: #fff;
		margin: 15px auto;
		border-radius: 10px;
		// border: 1px solid deepskyblue;
		// display: flex;
		// justify-content: space-between;

		.newfix {
			width: 350px;
			display: flex;
			flex-direction: row;
			justify-content: space-between;
			align-items: center;

			.titletext {
				width: 152px;
				height: 34px;
				// border: 1px solid #18BC37;
				display: flex;
				flex-direction: row;
				justify-content: space-around;
				align-items: center;

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
				}

			}

			.buttonfix {
				// width: 130px;
				// border: 1px solid red;
				heigh: 40px;
				display: flex;

				.imgurl {
					width: 10px;
					height: 10px;
					// border:1px solid blue;
				}

				// .mini-btn {
				// 	// flex-direction: row;
				// }
			}

		}

		.addlist {
			// border: 1px solid brown;
			background-color: #fff;
			margin: 10px auto;
			border-radius: 10px;
			height: 150px;
			// border-bottom: 4px solid #dadbde;
			box-shadow: 0px 5px 4px #8aaeec;

		}
	}
</style>
