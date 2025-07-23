<template>
	<view class="wrap" v-has="'selfhelp:add'">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">自助下单</block>
		</cu-custom>
		<view class="section">
			<view class="section-text">
				<view class="group">
					<view class="title">订单编号：</view>
					<view class="c
					ontent">
						<uni-forms ref="models" :modelValue="model" :rules="customRules">
							<uni-forms-item name="orderno">
								<u--input class="input" v-model="model.orderno" type="text" placeholder="请输入任务单编号"
									@input="input" />
								</u--input>
							</uni-forms-item>
						</uni-forms>
					</view>
				</view>
				<view class="group">
					<view class="title">粱型:</view>
					<view class="content">
						<dict dictCode="beamType" title="请选择粱型" @choose="Choose3"></dict>
					</view>
				</view>
				<view class="group">
					<view class="title"><span style="color:#f00;">*</span>施工部位:</view>
					<view class="content">
						<view @click="change1">
							<u--input placeholder="请选择施工部位" border="surround" v-model="model.constructionSite"
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
				<view class="group">
					<view class="title"><span style="color:#f00;">*</span>交付日期:</view>
					<view class="content">
						<!-- <u--input  type="text" placeholder="请输入抗渗等级" v-model="model.filters" /></u--input> -->
						<view class="screen-modal-item-input" @click="dateshow = true">
							<u--input placeholder="请选择时间" border="surround" v-model="model.deliveryDate"
								suffixIcon="arrow-down"></u--input>
						</view>
					</view>
				</view>
				<view class="group">
					<view class="title">梁数量:</view>
					<view class="content">
						<u--input type="text" placeholder="请输入梁数量" v-model="model.beamNum" />
						</u--input>
					</view>
				</view>
				<view class="group">
					<view class="title"><span style="color:#f00;">*</span>梁场(设备名称):</view>
					<view class="content">
						<sbp :sbtype="sbtype" @choose="changevalue"></sbp>
					</view>
				</view>
				<view class="group">
					<view class="title">制梁要求:</view>
					<view class="content">
						<!-- <u--input  border="surround" >
						</u--input> -->
						<u--textarea v-model="model.remark" placeholder="请输入制梁要求" autoHeight></u--textarea>
					</view>
				</view>
				<view class="group">
					<view class="title">顶板宽:</view>
					<view class="content">
						<u--input type="text" placeholder="请输入顶板宽" v-model="model.roofwid" />
						</u--input>
					</view>
				</view>
				<view class="group">
					<view class="title">底板宽:</view>
					<view class="content">
						<u--input type="text" placeholder="请输入底板宽" v-model="model.bottomwid" />
						</u--input>
					</view>
				</view>
				<view class="group">
					<view class="title">梁高:</view>
					<view class="content">
						<u--input type="text" placeholder="请输入梁高" v-model="model.beamhig" />
						</u--input>
					</view>
				</view>
				<!-- <view class="group">
					<view class="title">订单接收人:</view>
					<view class="">
						<u--input type="text" placeholder="请输入订单接收人" v-model="model.names" />
						</u--input>
					</view>
				</view> -->

				<view class="group">
					<view class="title">订单接收人:</view>
					<view class="content">
						<picker @change="Pickerorder" :range="orderman">
							<u--input placeholder="请选择订单接收人" border="surround" v-model="names" suffixIcon="arrow-down">
							</u--input>
						</picker>
					</view>
				</view>

				<view class="group">
					<view class="title">是否通知接收人:</view>
					<view class="content">
						<dict dictCode="yn" title="请选择是否通知接收人" @choose="Choose1"></dict>
					</view>
				</view>
			</view>
		</view>
		<button style="width:200upx;color: #fff;background-color:#387FFD;" @click="submit('models')">提交</button>
		<mx-date-picker :show="dateshow" :type="type" :value="values" :show-tips="true" :show-seconds="true"
			@confirm="confirmdate" @cancel="confirmdate" />
	</view>
</template>

<script>
	import sbp from '../../component/sheibeipublic/sheibeipublic.vue'
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	import projectname from '../../component/projectname/projectname.vue'
	import dict from '@/pages/component/dict/theory.vue'
	export default {
		name: 'selfhelp',
		components: {
			sbp,
			MxDatePicker,
			projectname,
			dict
		},
		data() {
			return {
				sbtype: '41',
				dateshow: false,
				type: 'datetime',
				idcreat: "",
				values: '',
				names: '', //订单接收人
				orderman: [],
				uid: '', //订单接收人传值
				model: {
					beamType: '', //梁型
					constructionSite: '', //施工部位
					deliveryDate: '', //交付日期
					beamNum: '', //梁数量
					shebeino: '', //设备名称
					remark: '', //制梁要求
					roofwid: '', //顶板宽
					bottomwid: '', //底板宽
					beamhig: '', //梁高
					orderRecipient: '', //订单接收人
					issend: '', //是否短信通知人
					orderno: '',
					treeid: '',

				},
				// baseFormData: {
				// 	: '',
				// },
				customRules: {
					// 对name字段进行必填验证
					orderno: {
						rules: [{
							required: true,
							errorMessage: '编号不能为空',
						}, ]
					},
				}
			}
		},
		onReady() {
			// this.selfhelpdata()
			this.ordertype()
		},

		methods: {
			input(e) {
				// console.log(e, '------------')
				// this.model.orderno = e
				this.$http.get(`/beamorder/beamOrder/list`).then(res => {
					// console.log(res, '自助下单')
					this.listdata = res.data.result.records
					for (let idname of this.listdata) {
						this.idcreat = idname.orderno
						console.log(e, this.idcreat, 'ffffff')
						if (e == this.idcreat) {
							this.$tip.toast('订单编号已占用,请重新输入');
						}
					}
				})

			},
			changevalue(choosevalue) {
				console.log(choosevalue, 'jkkkk')
				this.model.shebeino = choosevalue
			},
			//施工部位
			change1() {
				// 通过组件定义的ref调用uni-popup方法 ,如果传入参数 ，type 属性将失效 ，仅支持 ['top','left','bottom','right','center']
				this.$refs.popup.open('bottom')
			},
			// 是否发送短信通知人
			Choose1(choosevalue) {
				console.log(choosevalue, '是否发送短信通知人')
				this.model.issend = choosevalue
			},
			//梁型
			Choose3(choosevalue) {
				console.log(choosevalue, '选择梁型')
				this.model.beamType = choosevalue
			},
			// 订单接收人
			ordertype() {
				this.$http.get(`/bhzcfg/bhzPhone/list1`, {
					params: {
						phonesname: '12'
					}
				}).then(res => {
					console.log(res.data.result, '订单接收人')
					let data = res.data.result
					this.orderman = []
					this.uid = []
					data.forEach(orderdata => {
						this.orderman.push(orderdata.names)
						this.uid.push(orderdata.uid)
					})
				})

			},
			Pickerorder(e) {
				console.log(e)
				this.index = e.detail.value
				// this.choosekey = 5
				this.names = this.orderman[this.index]
				this.model.orderRecipient = this.uid[this.index]
				// this.cpdata = this.Carnumbers
				//console.log(this.cpdata, 'jjjjjjjjjjjj');
				// this.begintime = ''
				// this.endtime = ''
			},
			Choose2(choosevalue) {
				console.log(choosevalue, "当前施工部位")
				this.model.constructionSite = choosevalue.departName
				this.model.treeid = choosevalue.id
				// this.model.projname = 
				setTimeout(() => {
					this.$refs.popup.close()
				}, 500);

			},
			//日期选择
			confirmdate(e) {
				console.log(e, '')
				this.model.deliveryDate = e.value
				this.dateshow = false
			},
			submit() {
				if (!this.model.constructionSite && !this.model.shebeino && !this.model.deliveryDate) {
					uni.showToast({
						title: '请选择施工部位,设备名称和交付日期',
						icon: 'none'
					})
					return
				}
				this.$http.post(`/sys/sysDepartproject//beamorderadd`, this.model).then(res => {
					uni.showToast({
						title: '数据添加成功',
						icon: "success"
					})
					setTimeout(() => {
						uni.navigateTo({
							url: "/pages/smartlc/orderTracking/orderTracking"
						})
					}, 2000)
				})
			},
		},
	}
</script>

<style lang="less" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FE;

		.section {
			width: 700upx;
			height: auto;
			background-color: #fff;
			border-radius: 10px;
			margin: 0 auto;

			.section-text {

				color: #9299A8;
				font-size: 30upx;
				padding: 15px 15px;
				line-height: 55upx;
				margin: 10px 0;

				.group {
					margin-top: 1px;
					display: flex;
					// background: #FFFFFF;
					padding: 5px 10px;
					height: 56px;
					line-height: 50px;
					font-size: 14px;
				}

				.group .title {
					flex: 7;
					line-height: 30px;
					height: 30px;
					color: #000;
					text-align: center;
				}

				.group .content {
					height: 34px;
					line-height: 34px;
					background-color: #fff;
					flex: 12;
					border-radius: 7px;
					// border: 1px solid #dadbde;
				}

				.group .content input {
					padding-top: 8px;
					padding-left: 5px;
					font-size: 14px;
				}

				.uni-input-placeholder {
					color: #c0c4cc;
				}

				.group .content button {
					text-align: left;
					padding-left: 5px;
				}
			}
		}
	}
</style>
