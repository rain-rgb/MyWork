<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">压浆任务单新增</block>
		</cu-custom>
		<view class="nav">
			<view class="wrap-nav">
				<view class="screen-modal-item-name"><span style="color: #E42424;">*</span>设备名称:</view>
				<view class="screen-picker">
					<picker @change="equipmentPicker" :range="deviceName">
						<u--input placeholder="请选择设备名称" border="surround" v-model="chooseValue" disabled
							suffixIcon="arrow-down"></u--input>
					</picker>
				</view>
			</view>
			<view class="wrap-nav">
				<view class="screen-modal-item-name">施工部位:</view>
				<view class="screen-picker">
					<view @click="change1">
						<u--input placeholder="请选择施工部位" border="surround" v-model="construction" disabled
							suffixIcon="arrow-down">
						</u--input>
					</view>
					<uni-popup ref="popup" type="bottom">
						<view style="background-color: #FFFFFF;">
							<scroll-view scroll-y="true" scroll-x="true">
								<projectname @choose="Choose1"></projectname>
							</scroll-view>
						</view>
					</uni-popup>
				</view>
			</view>
			<view class="wrap-nav">
				<view class="screen-modal-item-name">工程名称:</view>
				<view class="screen-picker">
					<view class="text-input">
						<u--input placeholder="请输入工程名称" border="surround" v-model="projectName"
							style="background-color: #F6F9FC;">
						</u--input>
					</view>
				</view>
			</view>
			<view class="wrap-nav">
				<view class="screen-modal-item-name">压浆日期:</view>
				<view class="screen-picker">
					<view class="screen-modal-item-input" @click="timeShow = true">
						<u--input placeholder="请选择一次压浆日期" border="surround" v-model="timeValue" disabled
							suffixIcon="arrow-down"></u--input>
					</view>
				</view>
			</view>
		</view>
		<view class="screen-modal-btn">
			<u-button style="width:170px;border-radius: 10px;color: #fff;background-color: #387FFD;" text="新增"
				@click="addTask"></u-button>
		</view>
		<mx-date-picker :show="timeShow" :type="type" :show-tips="true" :show-seconds="true" @confirm="confirmdate"
			@cancel="confirmdate" />
	</view>
</template>

<script>
	import api from '@/api/api.js'
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	import projectname from '../../component/projectname/projectname.vue'
	import dict from '@/pages/component/dict/dict.vue'

	export default {
		name: 'YjTaskList',
		components: {
			MxDatePicker,
			dict,
			projectname
		},
		data() {
			return {
				orgcode: this.$store.getters.orgcode,
				type: 'datetime',
				timeShow: false,
				sbtype: '9',
				deviceName: [],
				devictype: [],
				chooseValue: '',
				timeValue: '', //压浆日期
				equipmentName: '', //设备名称
				construction: '', //施工部位
				treeId: '', //施工部位ID
				projectName: '', //工程名称
			}
		},
		onLoad() {
			this.deviceType()
		},
		methods: {
			// 获取设备信息列表
			deviceType() {
				let params = {
					sys_depart_orgcode: this.orgcode,
					sbtypes: this.sbtype
				}
				api.deviceType({
					params
				}).then(res => {
					res.data.result.forEach(e => {
						this.deviceName.push(e.sbname)
						this.devictype.push(e.sbjno)
					})
				})
			},
			// 施工部位选择
			change1() {
				// 通过组件定义的ref调用uni-popup方法 ,如果传入参数 ，type 属性将失效 ，仅支持 ['top','left','bottom','right','center']
				this.$refs.popup.open('bottom')
			},
			Choose1(choosevalue) {
				// console.log(choosevalue, "当前施工部位")
				this.construction = choosevalue.departName
				this.treeId = choosevalue.orgcode
				setTimeout(() => {
					this.$refs.popup.close()
				}, 500);
			},
			//设备名称
			equipmentPicker(e) {
				this.chooseValue = this.deviceName[e.detail.value]
				this.equipmentName = this.devictype[e.detail.value]
			},
			//日期选择
			confirmdate(e) {
				this.timeValue = e.value
				this.timeShow = false
			},
			addTask() {
				if (!this.equipmentName) {timeValue
					uni.showToast({
						title: '请选择设备名称',
						icon: 'none'
					})
					return
				}
				let params = {
					shebeibianhao: this.equipmentName,
					sgbwuuid: this.treeId,
					sgbwname: this.construction,
					projectname: this.projectName,
					yjdate: this.timeValue
				}
				this.$http.post('/sys/sysDepartproject/yjrenwudanadd', params).then(res => {
					if (res.data.code == 200) {
						uni.showToast({
							title: '操作成功！',
							icon: 'none'
						})
					} else {
						uni.showToast({
							title: '操作失败！',
							icon: 'none'
						})
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
				width: 590upx;
				height: 116rpx;
				margin: 0 auto;
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
			}
		}

		.screen-modal-btn {
			padding-bottom: 30rpx;
		}
	}
</style>
