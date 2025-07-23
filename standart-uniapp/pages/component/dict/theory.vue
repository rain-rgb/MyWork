<template>
	<view>
		<picker @change="changeDict" :value="index" :range="dictData" :disabled="disabled">
			<u--input :placeholder="this.title" border="surround" v-model="dictData[index]" 
				>
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
