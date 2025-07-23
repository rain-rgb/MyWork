<template>
	<view class="">
		<home1 v-if="PageCur=='/home'"></home1>
		<bhz v-if="PageCur=='/smartbh'" :menuList="menuList.bhz"></bhz>
		<ruanji v-if="PageCur=='/ruanji'" :parentId="parentId" :menuList="menuList.ruanji"></ruanji>
		<Rawmaterial v-if="PageCur=='/pages/Rawmaterial/Rawmaterial'" :menuList="menuList.rawmaterial"></Rawmaterial>
		<smartsy v-if="PageCur=='/pages/smartsy/smartsy'" :menuList="menuList.smartsy"></smartsy>
		<smartlc v-if="PageCur=='/pages/smartlc/smartlc'" :menuList="menuList.smartlc"></smartlc>
		<smartlm v-if="PageCur=='/pages/SmartPavement/SmartPavement'" :menuList="menuList.smartlm"></smartlm>
		<MaterialProcurement v-if="PageCur=='/pages/MaterialProcurement/MaterialProcurement'" :menuList="menuList.materialProcurement"></MaterialProcurement>
		<smartgb v-if="PageCur=='/pages/smartgb/smartgb'"></smartgb>
		<!-- <smartenv v-if="PageCur=='/Smartenv'" :menuList="menuList.smartenv"></smartenv> -->
	</view>
</template>

<script>
	import home1 from "./homes.vue"
	import bhz from "../smartbh/smartbh.vue"
	import ruanji from "../ruanji/ruanji.vue"
	import Rawmaterial from "../Rawmaterial/Rawmaterial.vue"
	import smartsy from "../smartsy/smartsy.vue"
	import smartlc from "../smartlc/smartlc.vue"
	import Smartlm from "../SmartPavement/SmartPavement.vue"
	import MaterialProcurement from "../MaterialProcurement/MaterialProcurement.vue"
	import smartgb from "../smartgb/smartgb.vue"
	import smartenv from "../smartgb/Smartenv/smartenv.vue"
	export default {
		name: 'home',
				data() {
			return {
				menuList: {
					bhz: {},
					ruanji: {},
					rawmaterial: {},
					smartsy: {},
					smartlc: {},
					smartlm: {},
					materialProcurement: {},
					smartenv: {},
				}
			}
		},
		components: {
			home1,
			bhz,
			ruanji,
			Rawmaterial,
			smartsy,
			smartlc,
			Smartlm,
			MaterialProcurement,
			smartgb,
			smartenv,
		},
		props:['PageCur','parentId','lableAuth'],
		watch: {
			PageCur: {
				// immediate: true,
				
				handler:function(val,oldVal){
					// console.log(this.PageCur)
					// console.log('PageCur',val,oldVal)
				    // this.initMenu()
				},
			},
		},
		mounted() {
			this.lableAuth.forEach(item => {
				if(item.url == '/smartbh'){
					this.menuList.bhz = item
				}else if(item.url == '/ruanji'){
					this.menuList.ruanji = item
				}else if(item.url == '/pages/Rawmaterial/Rawmaterial'){
					this.menuList.rawmaterial = item
				}else if(item.url == '/pages/smartsy/smartsy'){
					this.menuList.smartsy = item
				}else if(item.url == '/pages/smartlc/smartlc'){
					this.menuList.smartlc = item
				}else if(item.url == '/pages/SmartPavement/SmartPavement'){
					this.menuList.smartlm = item
				}else if(item.url == '/pages/MaterialProcurement/MaterialProcurement'){
					this.menuList.materialProcurement = item
				}else if(item.url == '/Smartenv'){
					this.menuList.smartenv = item
				}
			});
			// console.log(this.yemiandata[0],'yemiandata[0]----------------------------------');
			// console.log(this.PageCur)
		}
	}
</script>

<style>
</style>
