import axios from 'axios'
import store from '../store/index'

const services = axios.create({        
//   baseURL: 'http://10.8.0.86:8089/jeecg-boot/',  // 本地
  baseURL: 'http://z.traiot.cn/jeecg-boot/',  // 线上
//   baseURL: 'http://121.89.166.108:9034/show', //本地  
    
  timeout: 80000
})
// axios拦截器
services.interceptors.request.use(function (config) {
	var token = store.state.key || ''
	if(token){
        config.headers['X-Access-Token']= token; 
        // console.log(config.headers['X-Access-Token']) 
        return config;
	}else{
        console.log('token为空')
        return config;
	}
}, 
error => {
    return Promise.reject(error);
});
// 响应拦截
services.interceptors.response.use(response => {
	return response.data;
}, 
error => {
	return Promise.reject(error)
	// if(error.status==404){
	// 	console.log('请求超时')
	// }
})
export default services