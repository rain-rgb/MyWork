<template>
	<view>
		<view class="cu-custom" :style="[{height:CustomBar + 'px',zIndex:zIndex}]">
			<view class="cu-bar fixed" :style="style" :class="[bgImage!=''?'none-bg text-white bg-img':'',bgColor]">
				<view class="action" @tap="BackPage" v-if="isBack">
					<text class="cuIcon-back"></text>
					<slot name="backText"></slot>
				</view>
				<view class="content" :style="[{top:StatusBar + 'px'}]">
					<slot name="content"></slot>
				</view>
				<view class="action">
					<slot name="right"></slot>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				StatusBar: this.StatusBar,
				CustomBar: this.CustomBar
			};
		},
		name: 'cu-custom',
		computed: {
			style() {
				var StatusBar= this.StatusBar;
				var CustomBar= this.CustomBar;
				var bgImage = this.bgImage;
				var style = `height:${CustomBar}px;padding-top:${StatusBar}px;`;
				if (this.bgImage) {
					style = `${style}background-image:url(${bgImage});`;
				}
				return style
			}
		},
		props: {
			bgColor: {
				type: String,
				default: ''
			},
			isBack: {
				type: [Boolean, String],
				default: false
			},
			bgImage: {
				type: String,
				default: ''
			},
			zIndex:{
				type: String,
				default: '10'
			},
			backRouterName:{
				type: String,
				default: ''
			}
		},
		methods: {
			BackPage() {
				console.log(this.backRouterName,'this.backRouterName=================');
				
				if(!this.backRouterName){
					uni.navigateBack({
						delta: 1
					});	
				}else{
					uni.redirectTo({
						url: this.backRouterName
					});
				}
			}
		}
	}
</script>

<style>

</style>
