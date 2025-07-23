<template>
	<view id="samplingadd">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">见证取样</block>
		</cu-custom>
		<view class="main">
			<view class="main-item">
				<view class="main-item-name">试验类型：</view>
				<view class="main-item-input">
					<picker :range="testType" @change="ChangetestType">
						<u--input placeholder="请选择试验类型" disabled suffixIcon="arrow-down" v-model="fromdata.syxmmc"></u--input>
					</picker>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">配比通知单：</view>
				<view class="main-item-input" @click="pbtzdshows">
					<!-- <picker :range="pbtzdData" range-key="code" @change="changePbtzd">
						<u--input @click="pbtzdshows" placeholder="请选择配比通知单" disabled suffixIcon="arrow-down" v-model="fromdata.phbtzdbh"></u--input>
					</picker> -->
					<u-picker :search="true" :show="pbtzdshow" @confirm="pbtzdconfirm" @cancel="pbtzdcancel" :columns="pbtzdData" keyName="code" @search="picksearch">
					</u-picker>
						<u--input placeholder="请选择配比通知单" disabled suffixIcon="arrow-down" v-model="fromdata.phbtzdbh"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">委托编号：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入委托编号" v-model="fromdata.wtbh"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">工程名称：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入工程名称" v-model="fromdata.gcmc"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">浇筑部位：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入浇筑部位" v-model="fromdata.sgbw"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">样品名称：</view>
				<view class="main-item-input">
					<picker :range="sampling" @change="ChangetestType">
						<u--input placeholder="请选择样品名称" disabled suffixIcon="arrow-down" v-model="fromdata.ypmc"></u--input>
					</picker>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">取样日期：</view>
				<view class="main-item-input">
					<u--input disabled placeholder="请选择取样日期" v-model="fromdata.qyrq"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">试验尺寸：</view>
				<view class="main-item-input">
					<picker :range="testSize" @change="ChangeTestSize">
						<u--input placeholder="请选择试验尺寸" disabled suffixIcon="arrow-down" v-model="fromdata.chicun"></u--input>
					</picker>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">试验组数：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入试验组数" v-model="fromdata.sysuliang"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">强度等级：</view>
				<view class="main-item-input">
					<u--input placeholder="请输入强度等级" v-model="fromdata.qddj"></u--input>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">砼的种类：</view>
				<view class="main-item-input">
					<picker :range="concreteType" @change="ChangeConcrete">
						<u--input placeholder="请选择砼的种类" disabled suffixIcon="arrow-down" v-model="fromdata.hntzl"></u--input>
					</picker>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">龄期：</view>
				<view class="main-item-input">
					<picker :range="age" @change="changeage">
						<u--input placeholder="请选择龄期" disabled suffixIcon="arrow-down" v-model="fromdata.yplq"></u--input>
					</picker>
				</view>
			</view>
			<view style="height: 30upx;"></view>
		</view>
		<view class="main">
			<view class="" style="margin-left: 20upx;">
				<my-image-upload :uploadFilenames="samPic" @input="ChooseImage" label="取样实图" :maxImg="maxImg">
				</my-image-upload>
				<view class="tags">
					<u-tag  v-for="(item,index) in smcode" :key='index' :text="item" closable size="large" @close="closecode(index)"></u-tag>
				</view>
				<view class="btn">
					<u-button class="btnclass" type="warning" @click="onScan" text="扫码"></u-button>
					<u-button class="btnclass" type="primary" :loading="btnloading" @click="submit" plain text="提交"></u-button>
				</view>
				
				<!-- <button type="default" @click="onScan">扫码</button> -->
				<!-- <button type="default" @click="submit">提交</button> -->
			</view>
			<view style="height: 30upx;"></view>
		</view>
		<view style="height: 40upx;"></view>
	</view>
</template>

<script>
	import MyImageUpload from "../../component/imgupload/my-image-upload.vue"
	import smartsyapi from "../../../api/smartsy.js"
	import nowtime from '../../../common/util/nowtime.js'
	export default {
		components: {
			MyImageUpload
		},
		data() {
			return {
				btnloading: false,
				pbtzdshow:false,
				samPic: [],
				maxImg: 4,
				nowdate: nowtime.date(),
				testType:['混凝土试件抗压强度试验','水泥砂浆立方体抗压强度试验','水泥砂浆立方体抗压强度试验(建筑)'],
				sampling:['水泥混凝土试块'],
				testSize:['100*100*100','150*150*150','200*200*200'],
				concreteType:['泵送混凝土','碾压混凝土','普通混凝土','水下混凝土','抗渗混凝土'],
				age:['1','3','7','28','56'],
				pbtzdData:[],
				fromdata: {
					syxmmc: '', //试验类型
					syxm: "0503", //试验类型编号
					phbtzdbh: '', //配比通知单
					wtbh: '', //委托编号
					gcmc: '', //工程名称
					sgbw: '', //浇筑部位
					ypmc: '水泥混凝土试块', //样品名称
					qyrq: '', //取样日期
					chicun: '', //试验尺寸
					sysuliang: '', //试验组数
					qddj: '', //强度等级
					hntzl: '', //砼的种类
					yplq: '', //龄期
					qrcode: '', //二维码
					cdcm: '', //二维码编号
					qyr: '', //取样人
					testtype: '', //试验类型：1 压力试验  2 万能试验
					orgcode: ''
				},
				smcode:[],
				// smcode:["2578-93","2578-93","2578-93","2578-93","2578-93","2578-93","2578-93","2578-93"],
				sancarr:[],
				pbtzdcode:'',
				imglist:[],
				imgupload:{
					wtid:'',
					wtbh:'',
					wtpic1:'',
					wtpic2:'',
					wtpic3:'',
					wtpic4:'',
				},
			};
		},
		mounted() {
			console.log(this.$config.apiUrl)
			this.fromdata.qyrq = nowtime.date() +' '+ nowtime.time()
			let a = nowtime.date().split("-").join('')
			let b = nowtime.time().split(":").join('')
			this.fromdata.wtbh = 'WT-'+a+b
			this.getMixList()
			// console.log(a)
			// console.log(this.fromdata.qyrq,typeof(this.fromdata.qyrq))
		},
		methods:{
			pbtzdshows(){
				// console.log(11111)
				this.pbtzdshow = true
			},
			submit(){
				// this.btnloading = true
				if(!this.fromdata.syxmmc){
					uni.showToast({
						title:'请选择试验类型',
						icon:'none'
					})
					return
				}
				if(this.fromdata.phbtzdbh==0){
					uni.showToast({
						title:'请选择配合比通知单',
						icon:'none'
					})
					return
				}
				if(!this.fromdata.wtbh){
					uni.showToast({
						title:'请输入委托编号',
						icon:'none'
					})
					return
				}
				if(!this.fromdata.gcmc){
					uni.showToast({
						title:'请输入工程名称',
						icon:'none'
					})
					return
				}
				if(!this.fromdata.sgbw){
					uni.showToast({
						title:'请输入浇筑部位',
						icon:'none'
					})
					return
				}
				if(!this.fromdata.ypmc){
					uni.showToast({
						title:'请输入样品名称',
						icon:'none'
					})
					return
				}
				if(!this.fromdata.qyrq){
					uni.showToast({
						title:'请输入取样日期',
						icon:'none'
					})
					return
				}
				if(!this.fromdata.chicun){
					uni.showToast({
						title:'请选择试验尺寸',
						icon:'none'
					})
					return
				}
				if(!this.fromdata.sysuliang){
					uni.showToast({
						title:'请选择试验组数',
						icon:'none'
					})
					return
				}
				if(!this.fromdata.qddj){
					uni.showToast({
						title:'请选择强度等级',
						icon:'none'
					})
					return
				}
				if(!this.fromdata.hntzl){
					uni.showToast({
						title:'请选择砼的种类',
						icon:'none'
					})
					return
				}
				if(!this.fromdata.yplq){
					uni.showToast({
						title:'请选择龄期',
						icon:'none'
					})
					return
				}
				if(this.smcode.length !== this.fromdata.sysuliang*3){
					uni.showToast({
						title:'二维码数量不对',
						icon:'none'
					})
					return
				}
				// if(this.fromdata.syxm == '100014'){
				// 	this.fromdata.testtype = 1
				// }
				this.fromdata.testtype = 1
				this.fromdata.qrcode = this.sancarr.join(',')
				this.fromdata.cdcm = this.smcode.join(',')
				this.fromdata.qyr = this.$store.getters.username
				this.fromdata.orgcode = this.$store.getters.orgcode
				// console.log(this.fromdata,'this.fromdata')
				this.btnloading = true
				smartsyapi.samplingAdd(this.fromdata).then(res=>{
					// console.log(res)
					if(res.data.success){
						// console.log(this.imglist.length)
						if(this.imglist.length!=0){
							// console.log(1111)
							this.imgupload.wtid = res.data.result
							this.imgupload.wtbh = this.fromdata.wtbh
							switch(this.imglist.length){
								case 1:
								this.imgupload.wtpic1 = this.imglist[0]
								// console.log(this.imgupload)
								break;
								case 2:
								this.imgupload.wtpic1 = this.imglist[0]
								this.imgupload.wtpic2 = this.imglist[1]
								// console.log(this.imgupload)
								break;
								case 3:
								this.imgupload.wtpic1 = this.imglist[0]
								this.imgupload.wtpic2 = this.imglist[1]
								this.imgupload.wtpic3 = this.imglist[2]
								// console.log(this.imgupload)
								break;
								case 4:
								this.imgupload.wtpic1 = this.imglist[0]
								this.imgupload.wtpic2 = this.imglist[1]
								this.imgupload.wtpic3 = this.imglist[2]
								this.imgupload.wtpic4 = this.imglist[3]
								// console.log(this.imgupload)
							}
						}
						// console.log(this.imgupload)
						smartsyapi.samplingPicAdd(this.imgupload).then(res=>{
							// console.log(res)
							this.btnloading = false
							if (res.data.success) {
								uni.showToast({
									title: "操作成功",
									icon: 'none'
								})
								uni.navigateTo({
									url: '/pages/smartsy/sampling/samplinglist',
								})
							}
						})
					}
				})
			},
			// 删除二维码编号
			closecode(e){
				console.log(e)
				this.smcode.splice(e, 1)
				this.sancarr.splice(e, 1)
				console.log(this.smcode)
			},
			ChooseImage(uploadFilenames) {
				this.imglist = uploadFilenames
				console.log(this.imglist)
			},
			// 二维码扫描
			onScan(){
				var that = this
				// that.modalName = 'DialogModal1'
				// return
				uni.scanCode({
					success: function(res) {
						console.log('条码类型：' + res.scanType);
						console.log('条码内容：' + res.result);
						
						console.log(res.result.indexOf('#'))
						if(res.result.indexOf('#')==-1){
							let a = res.result
							console.log(a,typeof(a	))
							that.$http.post('/hntconsign/hntConsign/qrcodeyanzheng?code='+a).then(result=>{
								if(!result.data.success){
									uni.showToast({
										title:result.data.message,
										icon:"none"
									})
									return
								}
								if(that.smcode.includes(a)){
									uni.showToast({
										title:'请勿重复扫描此二维码！！！',
										icon:'none'
									})
									return
								}
								that.sancarr.push(a)
								that.smcode.push(a)
							})
						}else{
							let a = res.result.split('#')
							that.$http.post('/hntconsign/hntConsign/qrcodeyanzheng?code='+a[1]).then(result=>{
								if(!result.data.success){
									uni.showToast({
										title:result.data.message,
										icon:"none"
									})
									return
								}
								if(that.smcode.includes(a[0])){
									uni.showToast({
										title:'请勿重复扫描此二维码！！！',
										icon:'none'
									})
									return
								}
								that.sancarr.push(a[1])
								that.smcode.push(a[0])
							})
						}
						
					},
				});
			},
			// 选择砼的种类
			ChangeConcrete(e){
				this.fromdata.hntzl = this.concreteType[e.detail.value]
			},
			// 选择尺寸
			ChangeTestSize(e){
				this.fromdata.chicun = this.testSize[e.detail.value]
			},
			// 选择龄期
			changeage(e) {
				this.fromdata.yplq = this.age[e.detail.value]
			},
			// 选择试验类型
			ChangetestType(e) {
				this.fromdata.syxmmc = this.testType[e.detail.value]
				if(this.fromdata.syxmmc.indexOf("水泥砂浆")>-1){
					this.fromdata.syxm = "0506"
				}
				this.fromdata.ypmc = this.sampling[e.detail.value]
				this.fromdata.sysuliang = "1"
			},
			// 选择配料单编号
			// changePbtzd(e) {
			// 	this.fromdata.phbtzdbh = this.pbtzdData[e.detail.value].code
			// 	this.fromdata.qddj = this.pbtzdData[e.detail.value].betlev
			// 	this.taskList()
			// },
			pbtzdshows(){
				// console.log(11111)
				this.pbtzdshow = true
			},
			pbtzdconfirm(e){
				let value = e.value[0]
				// console.log(value,typeof(value))
				this.fromdata.phbtzdbh = value.code
				this.fromdata.qddj = value.betlev
				this.fromdata.gcmc = value.projname
				this.fromdata.sgbw = value.conspos
				this.taskList()
				this.pbtzdshow = false
				// console.log(e)
			},
			pbtzdcancel(){
				this.pbtzdshow = false
			},
			picksearch(e){
				console.log(e)
				this.pbtzdcode = e
				this.getMixList()
			},
			// 获取配比通知单编号
			getMixList(){
				smartsyapi.samplingMixList({params:{column: 'id',order: 'desc',code:this.pbtzdcode}}).then(res=>{
					this.pbtzdData = []
					// this.pbtzdData = res.data.result
					this.pbtzdData.push(res.data.result.records)
					// console.log(this.pbtzdData)
				})
			},
			// 任务单数据
			taskList(){
				smartsyapi.taskListNo({
					params: {
						code: this.fromdata.phbtzdbh
					}
				}).then(res=>{
					this.taskdata =res.data.result.records[0]
					this.fromdata.gcmc = this.taskdata.projname
					this.fromdata.sgbw = this.taskdata.conspos
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	#samplingadd {
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
			
			.tags{
				display: flex;
				flex-wrap: wrap;
			}
			
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
		
		.btn {
			width: 690upx;
			margin: 0 auto;
			margin-top: 30upx;
			display: flex;
		
			.btnclass {
				width: 30%;
			}
		}
		
	}
</style>
