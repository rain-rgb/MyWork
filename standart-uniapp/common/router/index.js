import modules from './modules'
import Vue from 'vue'
import Router from '@/plugin/uni-simple-router/index.js'
import {ACCESS_TOKEN} from '@/common/util/constants.js'

Vue.use(Router)
//初始化
const router = new Router({
	encodeURI:true,  
    routes: [...modules]//路由表
});

const whiteList = ['/pages/login/login','/pages/login/JTlogin'] 
const whiteList2 = ['/pages/login/ssologin'] 
//全局路由前置守卫
router.beforeEach((to, from, next) => {

	let token=uni.getStorageSync(ACCESS_TOKEN);
	// let local = location.href;
	// let index = local.lastIndexOf("=");
	// let tokenUrl = local.substring(index + 1, local.length);
    console.log(to.path)
	
	if(token){
		next()
	}else{
		if (whiteList.indexOf(to.path) !== -1) {
			next()
		}else{
			if(whiteList2.indexOf(to.path) !== -1  ){
				// console.log("11111",tokenUrl)
				// next({ path: '/pages/login/JTlogin',query:{"token": tokenUrl} })
				next({ path: '/pages/login/JTlogin' })
			}else{
				// console.log("22222",tokenUrl)
				next({ path: '/pages/login/login'})
			}
		  // next({ path: '/pages/login/login'})
		}
	} 
})
// 全局路由后置守卫
router.afterEach((to, from) => {
	console.log("afterEach")
})
export default router;