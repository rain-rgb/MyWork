<template>
	<view>
		<picker @change="changeSilo" :value="index" :range="siloData" :disabled="disabled">
			<u--input :placeholder="this.title" border="none" v-model="siloData[index]" disabled
				suffixIcon="arrow-down">
			</u--input>
	<!-- 		<text>
				{{index>-1?siloData[index]:this.title}}
			</text>
			<text class="cuIcon cuIcon-unfold"></text> -->
		</picker>
	</view>
</template>

<script>
	import smartsyapi from '../../../api/smartsy.js'
	export default {
		props: {
			disabled: Boolean,
			shebeino: {
				type: String,
				default: ''
			},
			cailiaono: {
				type: String,
				default: ''
			},
			title: {
				type: String,
				default: ''
			},
			siloDatas:{
				type:String,
				default:''
			}
		},
		data() {
			return {
				siloData: [],
				siloValues: [],
				index: -1,
				siloValue: '',
				values: '',
				choosevalue: ''
			}
		},
		created() {
			this.getData()
			// console.log("cailiaono", this.cailiaono)
			// console.log("shebeino", this.shebeino)
		},
		methods: {
			changeSilo(e) {
				this.index = e.detail.value
				//console.log("this.index", this.index)
				this.siloValue = this.siloValues[this.index]
				this.choosevalue = this.siloValue
				this.Choose();
			},
			// 选择事件
			Choose() {
				this.$emit('choose', this.choosevalue)
			},
			getData() {
				this.siloData = []
				this.siloValues = []
				//console.log(this.values, "values")
				smartsyapi.liaocangChangeList({
					params: {
						shebeino: this.shebeino,
						cailiaono: this.cailiaono
					}
				}).then(res => {
					//console.log(res, "res000")
					if (res.data.success) {
						if (res.data.result.length > 0) {
							let data = res.data.result
							for (let i = 0; i < data.length; i++) {
								if (data[i].guid == this.siloDatas) {
									this.index = i
									//console.log("this.index",this.index)
								}
								if (data[i].cailiaoname != null && data[i].cailiaoname != '') {
									this.siloData.push(data[i].name + '(' + data[i].cailiaoname + ')')
								} else {
									this.siloData.push(data[i].name)
								}
								this.siloValues.push(data[i].guid)
							}
						} else {
							console.log("暂无数据")
						}
					}
				})
			}
		}
	}
</script>

<style>
</style>
