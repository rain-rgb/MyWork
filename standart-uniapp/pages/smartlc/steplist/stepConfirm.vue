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
				<input type="text" placeholder="" v-model="listdata.taizuono" disabled />
			</view>
		</view>
		<view class="group">
			<view class="title">创建时间：</view>
			<view class="content">
				<input type="text" placeholder="请输入内容" v-model="listdata.dattim" disabled />
			</view>
		</view>
		<view class="group">
			<view class="title">施工部位：</view>
			<view class="content">
				<input type="text" placeholder="请输入内容" v-model="listdata.conspos" disabled />
			</view>
		</view>
		<view class="group">
			<view class="title">确认结束时间：</view>
			<view class="content">
				<!-- <input type="text" placeholder="请输入内容" v-model="time" disabled /> -->
				<!-- <view class="Task">
					开始时间: -->
					<view class="Task-input">
						<view class="screen-modal-item-input" @click="endshow = true">
							<u--input placeholder="请选择时间" border="surround" v-model="endtimevalue" suffixIcon="arrow-down">
							</u--input>
						</view>
					</view>
			</view>
		</view>
		<view class="group" v-if="zhengyang">
			<view class="title">蒸养室：</view>
			<view class="content">
				<picker @change="ChangeRoom" :range="zyroom">
					<input type="text" placeholder="请选择蒸养室" v-model="zyRoom" disabled />
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
				style="width:200upx;background-color:#387FFD;color:#fff;margin: 10px auto;">确认</button>
		</view>
		<mx-date-picker :show="endshow" :type="type" :value="value" :show-tips="true" :show-seconds="true"
			@confirm="confirmend" @cancel="confirmend" />

	</view>
</template>

<script>
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	export default {
		components: {
			MxDatePicker
		},
		data() {
			return {
				listdata: '',
				uuid: '',
				time: '',
				createBy: '',
				xuhao: '',
				zyroom: ['蒸养室一号', '蒸养室二号'],
				zyRoom: '',
				status1: '',
				status2: '',
				zhengyang: false,
				id: '',
				endtimevalue:'',
				endshow:false,
				type: 'datetime',
				value:''
			}
		},
		onLoad(options) {
			// console.log(options)
			console.log(this.$store.getters.username)
			this.createBy = this.$store.getters.username
			this.uuid = options.uuid
			console.log(this.uuid, 'llllll')
			this.xuhao = options.xuhao
			this.id = options.id
			// if (options.xuhao == '9') {
			// 	this.zhengyang = true
			// }
			this.loadData()
			this.getDate()
			this.endtimevalue = this.time
		},
		methods: {
			ChangeRoom(e) {
				this.zyRoom = this.zyroom[e.detail.value]
				if (e.detail.value == 0) {
					this.status1 = 2
				}
				if (e.detail.value == 1) {
					this.status2 = 2
				}
			},
			onConfirm() {
				console.log(this.createBy)
				if (!this.createBy) {
					uni.showToast({
						title: "请输入责任人",
						icon: "none"
					})
					return
				}
				let params = {
					uuid: this.uuid,
					finishtime: this.endtimevalue,
					responsible: this.createBy,
					xuhao: this.xuhao,
					id: this.id
				}
				console.log(params, 'params')
				this.$http.post('/zhilianggongxu/zhiliangGongxu/gongxuedit', params).then(res => {
					// console.log(res)
					if (res.data.success) {
						uni.showToast({
							title: "操作成功",
							icon: 'none'
						})
						setTimeout(() => {
							uni.navigateBack({
								delta: 1
							});
						}, 1000)
					}
				})

			},
			loadData() {
				let params = {
					sys_depart_orgcode: this.$store.getters.orgcode,
					uuid: this.uuid
				}
				this.$http.get('/zhiliangrenwudan/zhiliangrenwudan/list', {
					params
				}).then(res => {
					console.log(res, 'fffff')
					this.listdata = res.data.result.records[0]
					console.log(this.listdata, 'lllll')
				})
			},
		
			// 获取当前时间
			getDate() {
				var nowDate = new Date();
				let year = nowDate.getFullYear()
				let month = nowDate.getMonth() + 1 > 10 ? nowDate.getMonth() + 1 : this.addzero(nowDate.getMonth() + 1)
				let date = nowDate.getDate() > 10 ? nowDate.getDate() : this.addzero(nowDate.getDate())
				let hours = nowDate.getHours() > 10 ? nowDate.getHours() : this.addzero(nowDate.getHours())
				let minutes = nowDate.getMinutes() > 10 ? nowDate.getMinutes() : this.addzero(nowDate.getMinutes())
				let seconds = nowDate.getSeconds() > 10 ? nowDate.getSeconds() : this.addzero(nowDate.getSeconds())
				this.time = year + '-' + month + '-' + date + ' ' + hours + ':' + minutes + ':' + seconds;
				// this.fromdata.wtbh = 'WT-'+year+month+date+hours+minutes+seconds
			},
			addzero(obj) {
				if (obj < 10) {
					return '0' + obj
				} else {
					return obj
				}
			},
			confirmend(e) {
				this.endtimevalue = e.value
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
