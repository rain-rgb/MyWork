<!-- 组织机构列表 -->
<template>
	<view>
		<view>
			<picker @change="PickerChange" :range="orglist" :range-key="'departName'">
				<u--input placeholder="请选择组织机构" border="surround" v-model="orgname" disabled suffixIcon="arrow-down"></u--input>
			</picker>
		</view>
	</view>
</template>

<script>
	import api from "@/api/api.js"
	export default {
		name: 'organizatonlist',
		components: {},
		props: {
			sbtype: {
				type: String,
				default: ''
			}
		},
		data() {
			return {
				choosevalue: '',
				deviceData: [],
				sbnames: '',
				orglist:[],
			}
		},
		created() {
			// console.log(this.sbtype, "设备类型")
			this.getOrgList()
		},
		methods: {
			// 选择设备
			// PickerChange(e) {
			// 	// console.log(e.detail.value)
			// 	this.sbnames = this.deviceData[e.detail.value].sbname
			// 	this.choosevalue = this.deviceData[e.detail.value].sbjno
			// 	this.Choose();
			// },
			// 获取组织机构列表
			getOrgList() {
				// console.log(this.$store.getters.orgcode)
				let params = { orgtype: "5,6",sysorgcode: this.$store.getters.orgcode }
				api.getOrgList({params}).then(res=>{
					// console.log(res,'组织机构列表')
					this.orglist = res.data.result
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

