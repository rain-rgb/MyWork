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
				<input type="text" placeholder="请输入内容" v-model="beamweizhi.remark" disabled />
			</view>
		</view>
		<view class="group">
			<view class="title">梁场台座：</view>
			<view class="content">
				<!-- <picker @change="ChangeBeamzuo" :range="liangzuoname"> -->
				<input type="text" placeholder="请输入内容" v-model="beamweizhi.taizuoname" disabled />
				<!-- </picker> -->
			</view>
		</view>
		<view class="group">
			<view class="title">存梁层：</view>
			<view class="content">
				<input type="text" placeholder="请输入内容" v-model="beamweizhi.liangceng" disabled />
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
	import api from '@/api/api.js'
	export default {
		components: {
			MxDatePicker
		},
		data() {
			return {
				createBy: '',
				uuid: '',
				listdata: '',
				beamweizhi: '',
				id: '',
				endtimevalue: '',
				endshow: false,
				type: 'datetime',
				value: ''
			}
		},
		onLoad(options) {
			this.uuid = options.uuid
			this.id = options.id
			this.loadData()
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
				let params = {
					liangceng: this.beamweizhi.liangceng,
					taizuoname: this.beamweizhi.taizuoname,
					shebeino: this.beamweizhi.shebeino,
					status: 1,
					uuid: this.uuid,
					xuhao: 7
				}
				// 取梁前验证接口
				this.$http.get('/cunliang/beammanagementProcedure/list2', {
					params
				}).then(res => {
					console.log(res, '验证')
					if (res.data.success) {
						// 取梁接口
						let paramsadd = {
							taizuoname: this.beamweizhi.taizuoname,
							liangceng: this.beamweizhi.liangceng,
							shebeino: this.beamweizhi.shebeino,
							uuid: this.uuid,
							xuhao: 7,
							id: this.id,
							chutime: this.endtimevalue,
							responsible: this.createBy,
						}
						// console.log(paramsadd)
						// return
						this.$http.post('/cunliang/beammanagementProcedure/add2', paramsadd).then(res => {
							console.log(res, '取梁')
							if (res.data.success) {
								uni.showToast({
									title: "取梁成功",
									icon: 'none'
								})
								setTimeout(() => {
									uni.navigateBack({
										delta: 1
									});
								}, 1000)
							}
						})
					}
					uni.showToast({
						title: res.data.message,
						icon: "none"
					})
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
					console.log(res)
					this.listdata = res.data.result.records[0]
				})
				this.$http.get('/cunliang/beammanagementProcedure/list4', {
					params: {
						sys_depart_orgcode: this.$store.getters.orgcode,
						uuid: this.uuid,
						xuhao: 7
					}
				}).then(res => {
					console.log(res, '存梁的位置')
					this.beamweizhi = res.data.result
				})
				// let liangparams = {id: this.uuid}
				// this.$http.get('/zhiliangrenwudan/zhiliangrenwudan/queryZhiliangGongxuByMainId',{params:{id:this.uuid}}).then(res=>{
				// 	res.data.result.forEach(e=>{
				// 		if(e.xuhao === 7){
				// 			this.liangdata = e
				// 		}
				// 	})
				// })
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
