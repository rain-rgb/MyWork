<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">工序确认</block>
			<!-- <view slot="right" @click="screen">筛选</view> -->
		</cu-custom>
		<view class="group">
			<view class="title">工程名称：</view>
			<view class="content">
				<input type="text" placeholder="请输入内容" v-model="listdata.projname" disabled />
			</view>
		</view>
		<view class="group">
			<view class="title">任务单编号：</view>
			<view class="content">
				<input type="text" placeholder="请输入内容" v-model="listdata.code" disabled />
			</view>
		</view>
		<!-- <view class="group">
			<view class="title">配合比编号：</view>
			<view class="content">
				<input type="text" placeholder="请输入内容" v-model="listdata.recipe" disabled />
			</view>
		</view> -->
		<view class="group">
			<view class="title">台座：</view>
			<view class="content">
				<input type="text" placeholder="请输入内容" v-model="listdata.taizuono_dictText" disabled />
			</view>
		</view>
		<view class="group">
			<view class="title">创建时间：</view>
			<view class="content">
				<input type="text" placeholder="请输入内容" v-model="listdata.dattim" disabled />
			</view>
		</view>
		<view class="group">
			<view class="title">结束时间：</view>
			<view class="content">
				<view class="Task-input">
					<view class="screen-modal-item-input" @click="endshow = true">
						<u--input placeholder="请选择时间" border="surround" v-model="endtimevalue" suffixIcon="arrow-down">
						</u--input>
					</view>
				</view>
			</view>
		</view>
		<view class="group">
			<view class="title">施工部位：</view>
			<view class="content">
				<input type="text" placeholder="请输入内容" v-model="listdata.conspos" disabled />
			</view>
		</view>
		<view class="group">
			<view class="title">梁场：</view>
			<view class="content">
				<picker @change="ChangeBeam" :range="deviceName">
					<input type="text" placeholder="请选择梁场" v-model="deviceName1" disabled />
				</picker>
			</view>
		</view>
		<view class="group">
			<view class="title">梁场台座：</view>
			<view class="content">
				<picker @change="ChangeBeamzuo" :range="liangzuoname">
					<input type="text" placeholder="请选择梁场台座" v-model="liangzuoname1" disabled />
				</picker>
			</view>
		</view>
		<view class="group">
			<view class="title">存梁层：</view>
			<view class="content">
				<picker @change="ChangeBeamceng" :range="liangceng">
					<input type="text" placeholder="请选择存梁层" v-model="liangceng1" disabled />
				</picker>
			</view>
		</view>
		<view class="group">
			<view class="title">责任人：</view>
			<view class="content">
				<input type="text" placeholder="请输入内容" v-model="createBy" />
			</view>
		</view>
		<view class="">
			<button @click='onConfirm'
				style="width:200upx;background-color:#387FFD;color:#fff;margin:10px auto;">确认</button>
		</view>
		<mx-date-picker :show="endshow" :type="type" :value="value" :show-tips="true" :show-seconds="true"
			@confirm="confirmend" @cancel="confirmend" />
	</view>
</template>

<script>
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	import api from '@/api/api.js'
	export default {
		components: {
			MxDatePicker
		},
		data() {
			return {
				createBy: '',
				uuid: '',
				sbtype: '41',
				listdata: '',
				deviceName: [],
				deviceName1: '',
				devicetype: [],
				shebename: '',
				liangceng: [],
				cunlianghang: [],
				cunlianglie: [],
				liangceng1: '',
				cunlianghang1: '',
				cunlianglie1: '',
				cunpeople: '',
				liangzuoname: [],
				liangzuoname1: '',
				id: '',
				endtimevalue: '',
				endshow: false,
				type: 'datetime',
				value: ''
			}
		},
		onLoad(options) {
			console.log(options)
			this.uuid = options.uuid
			this.id = options.id
			this.loadData()
			this.deviceType()
		},
		methods: {
			onConfirm() {
				if (!this.createBy) {
					uni.showToast({
						title: "请输入责任人",
						icon: "none"
					})
					return
				}
				if (!this.deviceName1) {
					uni.showToast({
						title: "请选择梁场",
						icon: "none"
					})
					return
				}
				let params = {
					liangceng: this.liangceng1.toString(),
					taizuoname: this.liangzuoname1,
					shebeino: this.shebename,
					status: 1,
					uuid: this.uuid
				}
				// 存梁前检验接口
				this.$http.get('/cunliang/beammanagementProcedure/list1', {
					params
				}).then(res => {
					console.log(res, '验证')
					if (res.data.success) {
						// console.log(params)
						// 存梁接口
						let paramsadd = {
							taizuoname: this.liangzuoname1,
							liangceng: this.liangceng1.toString(),
							shebeino: this.shebename,
							uuid: this.uuid,
							id: this.id,
							cuntime: this.endtimevalue,
							responsible: this.createBy,
						}
						// return
						this.$http.post('/cunliang/beammanagementProcedure/add1', paramsadd).then(res => {
							console.log(res, '存梁')
							if (res.data.success) {
								uni.showToast({
									title: "存梁成功",
									icon: 'none'
								})
								setTimeout(() => {
									uni.navigateBack({
										delta: 1
									});
								}, 1000)
							}
						}).catch(e => {
							console.log("请求错误", e)
						})
					}
					uni.showToast({
						title: res.data.message,
						icon: 'none'
					})
				})
			},
			// 选择梁场
			ChangeBeam(e) {
				this.deviceName1 = this.deviceName[e.detail.value]
				this.shebename = this.devictype[e.detail.value]
				this.gettaizuo()
			},
			// 选择台座
			ChangeBeamzuo(e) {
				this.liangzuoname1 = this.liangzuoname[e.detail.value]
				this.getchl()
			},
			// 选择层
			ChangeBeamceng(e) {
				this.liangceng1 = this.liangceng[e.detail.value]
			},
			// 选择行
			ChangeBeamhang(e) {
				this.cunlianghang1 = this.cunlianghang[e.detail.value]
			},
			// 选择列
			ChangeBeamlie(e) {
				this.cunlianglie1 = this.cunlianglie[e.detail.value]
			},
			// 获取台座
			gettaizuo() {
				let params = {
					shebeino: this.shebename
				}
				this.$http.get('/cunliangprocedureconfig/cunliangProcedureConfig/list2', {
					params
				}).then(res => {
					if (res.data.success) {
						this.liangzuoname = []
						res.data.result.forEach(e => {
							this.liangzuoname.push(e.liangzuoname)
						})
						console.log(this.liangzuoname)
					}
				})
			},
			// 获取层行列
			getchl() {
				let params = {
					shebeino: this.shebename,
					liangzuoname: this.liangzuoname1
				}
				this.$http.get('/cunliangprocedureconfig/cunliangProcedureConfig/list2', {
					params
				}).then(res => {
					if (res.data.success) {
						let data = res.data.result[0]
						this.liangceng = []
						this.cunlianghang = []
						this.cunlianglie = []
						for (let a = 1; a <= data.cengshu; a++) {
							this.liangceng.push(a)
						}
						for (let b = 1; b <= data.lianghang; b++) {
							this.cunlianghang.push(b)
						}
						for (let c = 1; c <= data.lianglie; c++) {
							this.cunlianglie.push(c)
						}
					}
				})
			},
			// 梁信息
			loadData() {
				let params = {
					sys_depart_orgcode: this.$store.getters.orgcode,
					uuid: this.uuid
				}
				this.$http.get('/zhiliangrenwudan/zhiliangrenwudan/list', {
					params
				}).then(res => {
					this.listdata = res.data.result.records[0]
				})
			},
			// 获取设备信息列表
			deviceType() {
				api.deviceType({
					params: {
						sys_depart_orgcode: this.$store.getters.orgcode,
						sbtypes: this.sbtype
					}
				}).then(res => {
					console.log(res.data.result)
					let devicelist = res.data.result
					this.deviceName = []
					this.devictype = []
					devicelist.forEach(e => {
						this.deviceName.push(e.sbname)
						this.devictype.push(e.sbjno)
					})
				}).catch(e => {
					console.log("请求错误", e)
				})

			},
			confirmend(e) {
				if (e) this.endtimevalue = e.value
				this.endshow = false
			},
		}
	}
</script>

<style scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #FFF;
	}

	.group {
		margin-top: 1px;
		display: flex;
		background: #FFFFFF;
		padding: 5px 10px;
		height: 46px;
		line-height: 46px;
		font-size: 14px;
	}

	.group .title {
		flex: 4;
		line-height: 37px;
		height: 37px;
		text-align: center;
	}

	.group .content {
		height: 37px;
		line-height: 37px;
		background-color: #F6F9FC;
		flex: 9;
		border-radius: 10px;
		padding-left: 5px;
	}

	.group .content input {
		height: 30px;
		font-size: 16px;
	}

	.btn {
		margin-top: 1px;
		background-color: #FFFFFF;
		display: flex;
		padding: 30upx 20px;
	}
</style>
