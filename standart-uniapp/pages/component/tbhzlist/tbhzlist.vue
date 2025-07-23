<template>
	<view>
		<view class="group">
			<view class="title">材料类型：</view>
			<view class="content">
				<picker @change="Materialtype" :value="index" :range="typelist">
					<text class="">
						{{index>-1?typelist[index]:'请选择材料类型'}}
					</text>
				</picker>
			</view>
		</view>
		<view class="group">
			<view class="title">材料名称：</view>
			<view class="content">
				<picker @change="Materialname" :value="index" :range="typename">
					<text class="">
						{{Mindex>-1?typename[Mindex]:'请选择材料名称'}}
					</text>
				</picker>
			</view>
		</view>
		<view class="group">
			<view class="title">用量：</view>
			<view class="content">
				<!-- <u--input type="number" @input="Choose1"  placeholder="请输入用量"
					v-model="model.Materialcount" /> -->
					<u--input placeholder="请输入用量" border="none" v-model="model.Materialcount"
						type="number" @input="Choose1">
					</u--input>
			</view>
		</view>
	</view>
</template>

<script>
	import tkiTree from '@/components/tki-tree/tki-tree.vue'
	import timeSelector from '@/components/wing-time-selector/wing-time-selector.vue';
	import api from "@/api/api.js"
	export default {
		name: 'tbhzlist',
		components: {
			tkiTree,
			timeSelector
		},
		props: {
			materialtype: {
				type: String,
				default: ''
			},
			materialname: {
				type: String,
				default: ''
			},
			materialcount: {
				type: String,
				default: ''
			},
			tabindex: {
				type: Number,
				default: 1
			}
		},
		data() {
			return {
				dictData: [],
				index: -1,
				Mindex: -1,
				dictvalue: '',
				dictvalues: [],
				choosekey: '',
				deviceName: [],
				devictype: [],
				typelist: [],
				typecode: [],
				typename: [],
				typenamecode: [],
				model: {
					Materialcount: ''
				},
				choosevalue: '',
				choosedata: '',
				choosevalues: '',
			}
		},
		created() {
			// console.log(this.dengji, "字典值")
			// console.log(this.title, "tille")
			this.Materlistdata();
		},
		methods: {
			// 设备名称
			Materlistdata() {
				console.log(this.tabindex, 'jjjjjjjjjjjjjj');
				this.$http.get(`/sys/dict/getDictItems/material_types`, ).then(res => {
					// console.log(res.data.result,'wwwwwwwwwwwwwwww');
					let Materdata = res.data.result
					this.typelist = []
					this.typecode = []
					Materdata.forEach(e => {
						this.typelist.push(e.title)
						this.typecode.push(e.value)
					})
					console.log(this.typecode, 'wwwwwwwwwwwwwwwww');
				})
			},
			Materialtype(e) {
				console.log(e);
				// this.cbindex = -1
				this.index = e.detail.value
				this.imei = this.typecode
				this.choosekey = 2
				this.choosevalue = this.typecode[this.index]
				this.choosedata = this.typelist[this.index]
				// console.log(this.choosedata,this.choosevalue);
				this.Materlist(this.choosevalue,this.choosedata)
				//this.Choose()
				// this.Choose()
			},
			// 材料名称
			Materlist(aa) {
				console.log(aa,'材料名称')
				this.$http.get(`/sys/dictItem/cailiaonamelist`, {
					params: {
						itemValue: aa
					}
				}).then(res => {
					// console.log(res,'dddddddddddddd');
					let materdatalist = res.data.result
					this.typename = []
					this.typenamecode = []
					materdatalist.forEach(e => {
						this.typename.push(e.itemText)
						this.typenamecode.push(e.itemValue)
						// console.log(this.typename,);
						// console.log(this.typenamecode);
					})
				})
			},
			Materialname(e) {
				console.log(e);
				this.Mindex = e.detail.value
				this.Mcode = this.typenamecode
				this.choosekey = 3
				this.choosevalues = this.typenamecode[this.Mindex]
				//this.Choose()
				// this.Choose()
			},
			// 选择事件
			Choose1(e) {
				 this.model.Materialcount = e
				 console.log(this.model.Materialcount,'this.model.Materialcount');
				this.Choose()
	
				// this.$emit('choose7', this.choosevalue,this.choosedata,this.choosevalues, this.model.Materialcount, this.tabindex)
			},
			// 选择事件
			Choose() {
				console.log("names",this.choosevalue,this.choosedata,this.choosevalues, this.model.Materialcount, this.tabindex)
				this.$emit('choose7', this.choosevalue,this.choosedata,this.choosevalues, this.model.Materialcount, this.tabindex)
			},
			// choose8() {
			// 	this.$emit('choose8', this.choosevalue,this.choosedata,this.choosevalues,  this.model.Materialcount, this.tabindex)
			// }
		}
	}
</script>

<style>
	.group {
		margin-top: 1px;
		display: flex;
		/* background: #FFFFFF; */
		padding: 5px 10px;
		height: 42px;
		line-height: 42px;
		font-size: 14px;
	}

	.title {
		flex: 4;
		line-height: 30px;
		height: 30px;
		text-align: center;
	}

	.content {
		height: 30px;
		line-height: 30px;
		background-color: #f5f7fa;
		color: #c0c4cc;
		flex: 9;
		border-radius: 7px;
	
	}
</style>
