<template>
	<view class="margin-top">
		<view class="cu-bar bg-white ">
			<view class="">
				{{label}}
			</view>
			<!-- <view class="">
				{{imgList.length}}/{{maxImg}}
			</view> -->
		</view>
		<view class="cu-form-group">
			<view class="grid col-4 grid-square flex-sub">
				<view class="bg-img" v-for="(item,index) in imgList" :key="index" @tap="ViewImage" :data-url="imgList[index]">
					<image :src="imgList[index]" mode="aspectFill"></image>
					<view class="cu-tag bg-red" @tap.stop="DelImg" :data-index="index">
						<text class='cuIcon-close'></text>
					</view>
				</view>
				<view class="solids" @tap="ChooseImage" v-if="imgList.length<maxImg">
					<text class='cuIcon-cameraadd'></text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	
	export default {
		name: 'MyImageUpoad',
		props: {
			value: {type:String,default:''},
			label:{type:String,default:'图片上传'},
			maxImg: {
				type: Number,
				default: 4
			},
			uploadFilenames:{
				type: Array,
				default: () => []
			},
			//album 从相册选图，camera 使用相机，默认二者都有。如需直接开相机或直接选相册，请只使用一个选项
			sourceType:{
				type: Array,
				default: () => ['album','camera']
			}
			
		},
		mounted:function(){
			if (this.value.split(',')!=''){
				this.value.split(',').forEach(res=>{
					console.log(res)
					this.imgList.push(baseurl+res)
				})
			}
			if(this.uploadFilenames.length==0){
				let img11 = this.uploadFilenames.toString()
				this.imgList = this.uploadFilenames
			}
		},
		data() {
			return {
				uploadUrl: "/sys/common/upload",
				imgList: [],
				pathlist: [],
				baseurl: this.$config.apiUrl + '/sys/common/static/',
				// uploadFilenames:[]
				// deleteUrl: "/hidden/danger/plan/hiddenDangerFileDelete"
			}
		},
		methods: {
			ChooseImage() {
				uni.chooseImage({
					count: this.maxImg, //默认9
					sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
					sourceType: this.sourceType, //从相册选择
					success: (res) => {
						this.$http.upload(this.$config.apiUrl + this.uploadUrl,{
							filePath: res.tempFilePaths[0],
							name: 'file',
						}).then(res=>{
							if(res.data.success){
								let path =  res.data.message
								this.uploadFilenames.push(path)
								this.$emit('input', this.uploadFilenames, this.index)
							}
						}).catch(err=>{
							console.log("err",err)
						});
						if (this.imgList.length != 0) {
							this.imgList = this.imgList.concat(res.tempFilePaths)
						} else {
							this.imgList = res.tempFilePaths
						}
					}
				});
			},
			ViewImage(e) {
				uni.previewImage({
					urls: this.imgList,
					current: e.currentTarget.dataset.url,
				});
			},
			DelImg(e) {
				uni.showModal({
					title: '提示',
					content: '确认要删除吗',
					cancelText: '取消',
					confirmText: '确认',
					success: res => {
						if (res.confirm) {
							this.pathlist.splice(e.currentTarget.dataset.index,1)
							this.imgList.splice(e.currentTarget.dataset.index, 1)
							this.uploadFilenames.splice(e.currentTarget.dataset.index, 1)
							this.$emit('input', this.uploadFilenames, this.index)
						}
					}
				})
			},
		}
	}
</script>

<style>

</style>
