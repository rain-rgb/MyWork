<template>
	<view>
		<!-- <view class="screen" >
			<view class="zzjg" @tap="showTree" v-if="zzjg">
				<text class="zzjg-title">
					{{orgname==''?"组织机构":orgname}}
				</text>
				<text class="cuIcon cuIcon-unfold"></text>
			</view>
		</view> -->
		<view class="" @click="showTree">
			<u--input placeholder="请选择组织机构" border="surround" v-model="orgname" disabled
				suffixIcon="arrow-down">
			</u--input>
		</view>
		<tki-tree ref="tkitree" @confirm="treeConfirm" @cancel="treeCancel" :selectParent="selectParent"
			:range="departTree" idKey='id' :foldAll="flod" rangeKey="departName" confirmColor="#4e8af7" />
	</view>
</template>

<script>
	import tkiTree from '@/components/tki-tree/tki-tree.vue'
	import api from '@/api/api.js'
	
	export default {
		name: 'organization',
		components: {
			tkiTree,
		},
		data() {
			return {
				departTree: [],
				selectParent: true,
				flod: false,
				orgname: '',
				orgcode: '',
				choosevalue: '',
				choosekey: ''
			}
		},
		created() {
			const tree = uni.getStorageSync('departTree');
			if(!tree){
				this.queryMyDepartTree()
			}else{
				if(Date.now()-tree.time>60000*5){
					this.queryMyDepartTree()
				}else{
					this.departTree = tree.data
				}
			}
		},
		methods: {
			// 选择组织机构
			treeConfirm(e) {
				console.log(e)
				this.orgname = e[0].title
				this.orgcode = e[0].orgCode
				this.choosevalue = e[0].orgCode
				this.choosekey = 1
				this.Choose()
			},
			
			// 选择事件
			Choose() {
				this.$emit('choose', this.choosevalue,this.orgname)
				this.deviceList()
			},
			// 取消回调事件
			treeCancel(e) {
				console.log(e)
			},
			// 显示树形组件
			showTree() {
				this.$refs.tkitree._show();
			},
			// 组织机构树
			queryMyDepartTree() {
				api.orgTree().then(res => {
					this.departTree = res.data.result
					uni.setStorageSync('departTree', {time:Date.now(),data:this.departTree});
				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			// 设备信息列表
			deviceList() {
				api.deviceList({
					params: {
						sys_depart_orgcode: this.choosevalue
					}
				}).then(res => {
					// console.log(res)
					if(res.data.success){
						// this.loadData()
						// this.getDeviceOpen()
					}
				})
				// this.loadData()
			},
		}
	}
</script>

<style scoped>
	
</style>
