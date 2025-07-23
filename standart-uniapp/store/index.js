import Vue from 'vue'
import Vuex from 'vuex'
import api from "@/api/api"
import MinCache from '@/common/util/MinCache.js'
import {
	ACCESS_TOKEN,
	USER_NAME,
	USER_INFO,
	SYS_BUTTON_AUTH,
	SET_PERMISSIONLIST
} from "@/common/util/constants"

Vue.use(Vuex)

export default new Vuex.Store({
	state: {
		token: '',
		userid: '',
		username: '',
		realname: '',
		welcome: '',
		avatar: '',
		orgcode: '',
		departname:'',
	},
	mutations: {
		SET_TOKEN: (state, token) => {
			state.token = token
		},
		SET_NAME: (state, {
			username,
			realname,
			welcome,
			orgcode,
			departname,
			userid
		}) => {
			state.userid = userid
			state.username = username
			state.realname = realname
			state.welcome = welcome
			state.orgcode = orgcode
			state.departname = departname
		},
		SET_AVATAR: (state, avatar) => {
			state.avatar = avatar
		}
	},
	actions: {
		// 认证登录
		 		mLoginss({
		 			commit
		 		}, userInfo) {
		 			return new Promise((resolve, reject) => {
		 				api.login12(userInfo).then(response => {
		 					if (response.data.code == 200) {
		 						const result = response.data.result
		 						const userInfo = result.userInfo
		 						uni.setStorageSync(ACCESS_TOKEN, result.token);
		 						uni.setStorageSync(USER_INFO, userInfo);
		 						commit('SET_TOKEN', result.token)
		 						commit('SET_AVATAR', userInfo.avatar)
		 						commit('SET_NAME', {
		 							username: userInfo.username,
		 							realname: userInfo.realname,
		 							orgcode: userInfo.orgCode,
		 							departname:userInfo.telephone,
		 							userid: userInfo.id,
		 						})
		 						//console.log(userInfo.id,"用户id")
		 						resolve(response)
		 					} else {
		 						resolve(response)
		 					}
		 				}).catch(error => {
		 					console.log("catch===>response", response)
		 					reject(error)
		 				})
		 			})
		 		},

		// 登录
		mLogin({
			commit
		}, userInfo) {
			return new Promise((resolve, reject) => {
				api.login(userInfo).then(response => {
					if (response.data.code == 200) {
						const result = response.data.result
						const userInfo = result.userInfo
						uni.setStorageSync(ACCESS_TOKEN, result.token);
						uni.setStorageSync(USER_INFO, userInfo);
						commit('SET_TOKEN', result.token)
						commit('SET_AVATAR', userInfo.avatar)
						commit('SET_NAME', {
							username: userInfo.username,
							realname: userInfo.realname,
							orgcode: userInfo.orgCode,
							departname:userInfo.telephone,
							userid: userInfo.id,
						})
						//console.log(userInfo.id,"用户id")
						resolve(response)
					} else {
						resolve(response)
					}
				}).catch(error => {
					console.log("catch===>response", response)
					reject(error)
				})
			})
		},
		//获取用户信息
		GetPermissionList({
			commit
		}) {
			return new Promise((resolve, reject) => {
				api.permission().then(response => {
					console.log(response, "当前权限信息")
					if (response.data.code == 200) {
						const menuData = response.data.result.menu;
						const authData = response.data.result.auth;
						//const allAuthData = response.result.allAuth;
						uni.setStorageSync(SYS_BUTTON_AUTH, authData);
						uni.setStorageSync(SET_PERMISSIONLIST, menuData);
						//commit('SYS_BUTTON_AUTH', authData);
						//commit('SET_PERMISSIONLIST', menuData)
						resolve(response)
					} else {
						reject(response)
					}
				}).catch(error => {
					reject(error)
				})
			})
		},
		//手机号登录
		PhoneLogin({
			commit
		}, userInfo) {
			return new Promise((resolve, reject) => {
				api.phoneNoLogin(userInfo).then(response => {
					if (response.data.code == 200) {
						const result = response.data.result
						const userInfo = result.userInfo
						uni.setStorageSync(ACCESS_TOKEN, result.token);
						uni.setStorageSync(USER_INFO, userInfo);
						commit('SET_TOKEN', result.token)
						commit('SET_NAME', {
							username: userInfo.username,
							realname: userInfo.realname
						})
						commit('SET_AVATAR', userInfo.avatar)
						resolve(response)
					} else {
						reject(response)
					}
				}).catch(error => {
					reject(error)
				})
			})
		},
		// 登出
		Logout({
			commit,
			state
		}) {
			return new Promise((resolve) => {
				let logoutToken = state.token;
				commit('SET_TOKEN', '')
				uni.removeStorageSync(ACCESS_TOKEN)
				uni.removeStorageSync(USER_INFO)
				api.logout(logoutToken).then(() => {
					resolve()
				}).catch(() => {
					resolve()
				})
			})
		},

	},
	getters: {
		token: state => state.token,
		username: state => {
			state.username = uni.getStorageSync(USER_INFO).username;
			return state.username
		},
		nickname: state => {
			state.realname = uni.getStorageSync(USER_INFO).realname;
			return state.user.realname
		},
		avatar: state => {
			state.avatar = uni.getStorageSync(USER_INFO).avatar;
			return state.user.avatar
		},
		userid: state => {
			state.userid = uni.getStorageSync(USER_INFO).id;
			return state.userid
		},
		orgcode: state => {
			state.orgcode = uni.getStorageSync(USER_INFO).orgCode;
			return state.orgcode
		},
		departname: state => {
			state.departname = uni.getStorageSync(USER_INFO).telephone;
			return state.departname
		},
	}
})
