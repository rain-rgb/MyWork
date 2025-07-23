<template>
	<view>
		<picker @change="changeDict" :value="index" :range="dictData">
			<text class="dict">
				{{index>-1?dictData[index]:this.title}}
			</text>
			<text class="cuIcon cuIcon-unfold"></text>
		</picker>
	</view>
</template>

<script>
	import api from "@/api/api.js"
	export default {
		name: 'selectshebei',
		components: {},
		props: {
			sbtype: {
				type: String,
				default: ''
			},
			title: {
				type: String,
				default: ''
			}

		},
		data() {
			return {
				dictData: [],
				index: -1,
				dictvalue: '',
				dictvalues: [],
				choosevalue: '',
				choosekey: '',
				values: '',
			}
		},
		created() {
			//console.log(this.dictCode, "字典值")
			//console.log(this.title, "tille")
			this.getdictData();
		},
		methods: {
			call(e) {
				this.values = e
				console.log(e, "sbjno")
				console.log(this.sbtype, "sbtype")
				//this.getdictData();
				this.deviceType();//需要传值 设备类型以及设备编号
			},
			changeDict(e) {
				//console.log(e, "e")
				this.index = e.detail.value
				//console.log("this.index", this.index)
				this.dictvalue = this.dictvalues[this.index]
				this.choosevalue = this.dictvalue
				this.Choose();
			},
			// 选择事件
			Choose() {
				this.$emit('choose', this.choosevalue)
			},
			deviceType() {
				api.deviceType({
					params: {
						//sys_depart_orgcode: this.orgcode,
						sbtypes: this.sbtype
					}
				}).then(res => {
					console.log(res.data.result)
					let data = res.data.result
					for (let i = 0; i < data.length; i++) {
						if (this.values != '') {
							if (data[i].sbname == this.values) {
								this.index = i
								this.dictvalues[this.index] = data[i].sbname
								//console.log("this.index", this.index);
								//console.log("this.dictvalues[this.index]", this.dictvalues[this.index])
							}
						}
						this.dictData.push(data[i].sbname)
						this.dictvalues.push(data[i].sbjno)
						//console.log("this.dictData", this.dictData)
					}
				}).catch(e => {
					console.log("请求错误", e)
				})
			
			},
			getdictData() {
				this.$http.get('/sys/dict/getDictItems/' + this.dictCode + '').then(res => {
					this.dictData = []
					this.dictvalues = []
					//console.log(res.data.result, "字典数组")
					let data = res.data.result
					for (let i = 0; i < data.length; i++) {
						if (this.values != '') {
							if (data[i].text == this.values) {
								this.index = i
								this.dictvalues[this.index] = data[i].text
								//console.log("this.index", this.index);
								//console.log("this.dictvalues[this.index]", this.dictvalues[this.index])
							}
						}
						this.dictData.push(data[i].text)
						this.dictvalues.push(data[i].value)
						//console.log("this.dictData", this.dictData)
					}
				}).catch(e => {
					console.log("请求错误", e)
				})
			}
		}
	}
</script>

<style>
	.dict {
		padding-left: 5px;
	}
</style>
