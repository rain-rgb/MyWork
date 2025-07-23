<template>
	<view>
		<picker @change="changeDict" :value="index" :range="dictData" :disabled="disabled">
			<u--input :placeholder="this.title" border="surround" v-model="dictData[index]" disabled
				suffixIcon="arrow-down">
			</u--input>
			<!-- <text class="dict">
				{{index>-1?dictData[index]:this.title}}
			</text>
			<text class="cuIcon cuIcon-unfold"></text> -->
		</picker>
	</view>
</template>

<script>
	import api from "@/api/api.js"
	export default {
		name: 'dict',
		components: {},
		props: {
			dictCode: {
				type: String,
				default: ''
			},
			title: {
				type: String,
				default: ''
			},
			dictCodeArr: {
				type: Array,
				default: () => []
			},
			disabled:Boolean

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
				//console.log(this.values, "values")
				this.getdictData();
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
			getdictData() {
				if(this.dictCode == 9 || this.dictCode == 10){
					this.$http.get('/sys/user/list3', {
						params: {
							sys_depart_orgcode: this.$store.getters.orgcode,
							sbtypes: this.dictCode
						}
					}).then(res => {
						this.dictData = []
						this.dictvalues = []
						//console.log(res.data.result, "字典数组")
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
												let devicelist = res.data.result
						// this.deviceName = []
						// this.devictype = []
						// devicelist.forEach(e => {
						// 	this.deviceName.push(e.sbname)
						// 	this.devictype.push(e.sbjno)
						// })
						}
					}).catch(e => {
						console.log("请求错误", e)
					})
				}else{
					this.dictData = [];
					this.dictvalues = [];
					//console.log(res.data.result, "字典数组")
					let data = this.dictCodeArr;
					// let data = [{
					// 	text: '箱型梁',
					// 	value: '0'
					// 	}, {
					// 	text: 'T型梁',
					// 	value: '1'
					// 	}, {
					// 	text: '槽形梁',
					// 	value: '2'
					// 	}, {
					// 	text: '空心板梁',
					// 	value: '3'
					// 	}, {
					// 	text: '其他',
					// 	value: '10'
					// 	}];
					for (let i = 0; i < data.length; i++) {
						if (this.values != "") {
						if (data[i].text == this.values) {
							this.index = i;
							this.dictvalues[this.index] = data[i].text;
						}
						}
						this.dictData.push(data[i].text);
						this.dictvalues.push(data[i].value);
					}
				}
			}
		}
	}
</script>

<style>
	.dict {
		padding-left: 5px;
	}
</style>
