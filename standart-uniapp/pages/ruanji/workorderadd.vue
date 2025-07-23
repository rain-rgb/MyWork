<template>
	<view id="wordorderadd">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">{{id==undefined?'工单新增':'工单编辑'}}</block>
		</cu-custom>
		<view class="main">
			<view class="main-item">
				<view class="biaoqian"></view>
				<view class="mainnew">主要信息</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">组织机构：</view>
				<view class="main-item-input">
					<organization :orgname="formData.orgcode_dictText" @choose="Chooseorg"></organization>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">设备名称：</view>
				<view class="main-item-input">
					<equipment :sbnames="formData.shebeino_dictText" @choose="Choosedevice" sbtype="16,19"></equipment>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">里程：</view>
				<view class="main-item-input">
					<view @click="change1">
						<u--input placeholder="请选择里程" border="surround" v-model="formData.mileage" disabled
							suffixIcon="arrow-down">
						</u--input>
					</view>
					<uni-popup ref="popup" type="bottom">
						<view style="background-color: #FFFFFF;">
							<scroll-view scroll-y="true" scroll-x="true">
								<projectname @choose="Choose"></projectname>
							</scroll-view>
						</view>
					</uni-popup>
					<!-- <u--input placeholder="请输入里程" border="surround" v-model="formData.mileage"></u--input> -->
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">设计桩基数：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入设计桩基数" border="surround" v-model="formData.descount"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">开始日期：</view>
				<view class="main-item-input" @click="handlestatime">
					<u--input placeholder="请选择开始日期" border="surround" v-model="formData.starttime"
					  disabled	suffixIcon="calendar" suffixIconStyle="color:#4A7FFF;"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">截止日期：</view>
				<view class="main-item-input" @click="handleendtime">
					<u--input placeholder="请选择截止日期" border="surround" v-model="formData.endtime"
					  disabled	suffixIcon="calendar" suffixIconStyle="color:#4A7FFF;"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">备注：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入备注" border="surround" v-model="formData.other"></u--input>
				</view>
			</view>
			<view style="height: 30upx;"></view>
		</view>
		<!-- <view class=""> -->
			<u-button type="primary" :loading="btnloading" text="确定" @click="submit"></u-button>
		<!-- </view> -->
		<mx-date-picker :show="datestashow" :type="type" :show-tips="true" :show-seconds="true"
			@confirm="confirmstadate" @cancel="confirmstadate" />
		<mx-date-picker :show="dateendshow" :type="type" :show-tips="true" :show-seconds="true"
			@confirm="confirmenddate" @cancel="confirmenddate" />
	</view>
</template>

<script>
	import ruanjiapi from "../../api/ruanji.js"
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	import equipment from '../component/equipment/equipment.vue'
	import organization from '../component/organization/organization1.vue'
	import projectname from '../component/projectname/projectname.vue'
	export default {
		components: {
			equipment,
			MxDatePicker,
			organization,
			projectname
		},
		data() {
			return {
				btnloading: false,
				datestashow: false,
				dateendshow: false,
				type: 'datetime',
				id:null,
				formData: {
					// orgcode_dictText:"",
					other: "", //其他备注
					orgcode: "", //组织机构（选择）
					endtime: "", //截止日期（时间选择控件）
					shebeino: "", //设备编号（选择）
					starttime: "", //开始时间（时间选择控件）
					descount: null, //设计桩基数（手输）
					mileage: "" ,//里程（输入）->选择
					treeid:""//里程节点id
				}
			}
		},
		onLoad(options) {
			this.id = options.id
			if(this.id!=undefined){
				this.getLoadData()
			}
		},
		methods: {
			Choose(choosevalue){
				//console.log(choosevalue, "当前施工部位")
				this.formData.mileage = choosevalue.departName
				this.formData.treeid = choosevalue.id
				// this.model.projname = 
				setTimeout(() => {
					this.$refs.popup.close()
				}, 500);
			},
			change1() {
				// 通过组件定义的ref调用uni-popup方法 ,如果传入参数 ，type 属性将失效 ，仅支持 ['top','left','bottom','right','center']
				this.$refs.popup.open('bottom')
			},
			// 软基工单新增
			submit() {
				console.log(this.formData)
				if(!this.formData.orgcode){
					uni.showToast({
						title:'请选择组织机构',
						icon:'none'
					})
					return
				}
				if(!this.formData.shebeino){
					uni.showToast({
						title:'请选择设备',
						icon:'none'
					})
					return
				}
				if(!this.formData.endtime){
					uni.showToast({
						title:'请选择截止时间',
						icon:'none'
					})
					return
				}
				if(!this.formData.starttime){
					this.formData.starttime = this.getDate()
				}
				if(!this.formData.mileage){
					uni.showToast({
						title:'请输入里程',
						icon:'none'
					})
					return
				}
				if(!this.formData.descount){
					uni.showToast({
						title:'请输入桩基数',
						icon:'none'
					})
					return
				}
				if(this.id==null){
					this.dataAdd()
				}else{
					this.dataEdit()
				}
			},
			dataEdit() {
				this.btnloading = true
				ruanjiapi.workorderedit(this.formData).then(res=>{
					this.btnloading = false
					if (res.data.success) {
						this.$tip.toast('提交成功')
						this.$Router.replace({
							name: 'workorder'
						})
					}else{
						this.$tip.toast('提交失败')
					}
				})
			},
			dataAdd() {
				this.btnloading = true
				ruanjiapi.workorderadd(this.formData).then(res=>{
					this.btnloading = false
					if (res.data.success) {
						this.$tip.toast('提交成功')
						this.$Router.replace({
							name: 'workorder'
						})
					}else{
						this.$tip.toast('提交失败')
					}
				})
			},
			handlestatime(){
				this.datestashow = true
			},
			confirmstadate(e) {
				// console.log(e,'确认开始时间')
				this.formData.starttime = e.value
				this.datestashow = false
			},
			handleendtime() {
				this.dateendshow = true
			},
			confirmenddate(e) {
				// console.log(e,'确认截止时间') 
				this.formData.endtime = e.value
				this.dateendshow = false
			},
			Choosedevice(choosevalue) {
				// console.log(choosevalue, "当前设备编号")
				this.formData.shebeino = choosevalue
			},
			Chooseorg(choosevalue) {
				// 组织机构
				console.log(choosevalue)
				this.formData.orgcode = choosevalue
			},
			getLoadData() {
				let params = {
					id:this.id
				}
				ruanjiapi.workorderlist({
					params
				}).then(res => {
					console.log(res, "软基工单列表")
					this.formData = res.data.result.records[0]
				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			// 获取当前时间
			getDate(){
				var nowDate = new Date();
				let year = nowDate.getFullYear()
				let month = nowDate.getMonth()+1>10 ? nowDate.getMonth()+1:this.addzero(nowDate.getMonth()+1)
				let date = nowDate.getDate()>10 ? nowDate.getDate():this.addzero(nowDate.getDate())
				let hours = nowDate.getHours()>10 ? nowDate.getHours():this.addzero(nowDate.getHours())
				let minutes = nowDate.getMinutes()>10 ? nowDate.getMinutes():this.addzero(nowDate.getMinutes())
				let seconds = nowDate.getSeconds()>10 ? nowDate.getSeconds():this.addzero(nowDate.getSeconds())
				let time = year+'-'+month+'-'+date+' '+hours+':'+minutes+':'+seconds;
				return time
			},
			addzero(obj){
				if(obj<10){
					return '0' + obj
				} else {
					return obj
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	#wordorderadd {
		width: 100%;
		height: 100vh;
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
	}
</style>
