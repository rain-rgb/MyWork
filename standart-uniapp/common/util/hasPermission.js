import Vue from 'vue';
const hasPermission = {
    install (Vue, options) {
          //console.log(options);
          Vue.directive('has', {
            inserted: (el, binding, vnode)=>{
                //console.log("页面权限控制----");
                //console.time()
                //节点权限处理，如果命中则不进行全局权限处理
                // if(!filterNodePermission(el, binding, vnode)){
                  filterGlobalPermission(el, binding, vnode);
                // }
                //console.timeEnd() //计时结束并输出时长
            }
          });
    }
};
/**
 * 全局权限控制
 */
export const filterGlobalPermission = (el, binding, vnode) => {
    let permissionList = [];
    let authList = uni.getStorageSync("SYS_BUTTON_AUTH" || "[]");//
	// console.log(authList,"获取到的按钮权限数据")
    for (let auth of authList) {
        permissionList.push(auth);
    }
    if (!permissionList.length) {
        el.parentNode.removeChild(el);
        return;
    }
    let permissions = [];
    for (let item of permissionList) {
        permissions.push(item.action);
    }
    if (!permissions.includes(binding.value)) {
        el.parentNode.removeChild(el);
    }
}

export default hasPermission;
