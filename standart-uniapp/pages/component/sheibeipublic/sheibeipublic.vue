<!-- 设备名称 -->
<template>
	<view>
		<view>
			<picker @change="PickerChange" :range="deviceData" :range-key="'sbname'">
				<u--input placeholder="请选择设备名称" border="surround" v-model="sbnames"  suffixIcon="arrow-down">
				</u--input>
			</picker>
		</view>
	</view>
</template>

<script>
	import api from "@/api/api.js"
	export default {
		name: 'equipment',
		components: {},
		watch: {
			orgcode: {
				handler(n, o) {
					console.log(n, o)
					this.deviceType()
				},
				immediate: true

			},
		},
		props: {
			sbtype: {
				type: String,
				default: ''
			},
			// sbnames: {
			// 	type: String,
			// 	default: ''
			// },
			orgcode: {
				type: String,
				default: ''
			}
		},
		data() {
			return {
				choosevalue: '',
				deviceData: [],
				sbnames: ''
			}
		},
		created() {
			// console.log(this.sbtype, "设备类型")
			this.deviceType()
		},
		methods: {
			// 选择设备
			PickerChange(e) {
				console.log(e.detail.value)
				this.sbnames = this.deviceData[e.detail.value].sbname
				this.choosevalue = this.deviceData[e.detail.value].sbjno
				this.Choose();
			},
			// 获取设备信息列表
			deviceType() {
				api.deviceType({
					params: {
						sys_depart_orgcode: this.orgcode,
						sbtypes: this.sbtype
					}
				}).then(res => {
					//console.log(res.data.result)
					this.deviceData = res.data.result
				}).catch(e => {
					console.log("请求错误", e)
				})

			},
			// 选择事件
			Choose() {
				this.$emit('choose', this.choosevalue)
			},
		},
	}
</script>

<style>
	.shebei {
		padding-left: 5px;
	}
</style>
