<template>
	<view>
		<view>
			<ly-tree ref="tree" :tree-data="treeData" node-key="id" lazy :load="loadNode" :showRadio="true"
				:props="defaultprops" @node-expand="handleNodeExpand" @node-click="handleNodeClick"
				@radio-change="handleRadioChange"/>
		</view>
	</view>

</template>

<script>
	import LyTree from '@/components/ly-tree/ly-tree.vue'
	import api from '@/api/api.js'
	export default {
		name: 'projectname',
		components: {
			LyTree
		},
		props: {},
		data() {
			return {
				conspos: '',
				departTree: [],
				selectParent: true,
				flod: true,
				departName: [],
				index: -1,
				orgcode: '',
				treeData: [],
				treeSonData: [],
				dataList: [],
				defaultprops: {
					label: 'departName',
					children: 'childs'
				},
				choosevalue: '',
				choosekey: ''
			}
		},
		created() {
			this.queryMyDepartprojectTreeList()
		},
		methods: {
			queryMyDepartprojectTreeList() {
				this.treeData = []
				api.projectTree({
					params: {
						parentId: ''
					}
				}).then(res => {
					let data = res.data.result
					data.forEach(e => {
						this.treeData.push({
							id: e.id,
							departName: e.departName,
							childs: e.children,
							orgcode: e.orgCode
						})
					})
					//console.log(res.data.result, "施工部位")
				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			// uni-app中emit触发的方法只能接受一个参数，所以会回传一个对象，打印对象即可见到其中的内容
			handleNodeClick(obj) {
				// console.log('handleNodeClick', obj);
			},
			handleNodeExpand(obj) {
				// console.log('handleNodeExpand', obj);
				this.parentId = obj.data.id
				//console.log("this.parentId", this.parentId)
				//console.log("handleNodeExpand", obj)
				// this.choosevalue =currentNode[0]
				// this.Choose()
				// console.log(this.choosevalue, "选中的wbs")
			},
			handleRadioChange(data) {
				// console.log(data, "handleRadioChange");
				this.choosevalue = data.data
				this.Choose()
				//console.log(this.choosevalue, "选中的wbs")
				// this.parentId = data.data.id
				// this.queryDepartTreeprojectListSon(parentIds)
				// console.log("parentIds", parentIds)
			},
			reWriterWithSlot(arr) {
				this.dataList = []
				for (let item of arr) {
					if (item.childs && item.childs.length > 0) {
						this.reWriterWithSlot(item.childs)
						let temp = Object.assign({}, item)
						temp.childs = {}
						this.dataList.push(temp)
						// console.log("temp", temp, item.childs)
					} else {
						this.dataList.push(item)
						// console.log("temp2", this.dataList)
					}
				}
			},
			// 选择事件
			Choose() {
				// const currentNode = this.$refs.tree.getCheckedNodes()
				// this.choosevalue =currentNode[0]
				this.$emit('choose', this.choosevalue)
			},
			loadNode(node, resolve) {
				// console.log("resolve", resolve)
				// console.log("node1", node)
				this.treeSonData = []
				this.dataList = []
				setTimeout(() => {
					api.projectTreeSon({
						params: {
							parentId: this.parentId
						}
					}).then(res => {
						let data = res.data.result
						data.forEach(e => {
							this.treeSonData.push({
								id: e.id,
								departName: e.departName,
								childs: e.children,
								orgcode: e.orgCode
							})
						})
						node.data.childs = this.treeSonData
						this.reWriterWithSlot(this.treeSonData)
						resolve(this.dataList)
						//console.log(data, "子节点数据")
					}).catch(e => {
						console.log("请求错误", e)
					})
				}, 500);
			},
		}
	}
</script>

<style>
</style>
