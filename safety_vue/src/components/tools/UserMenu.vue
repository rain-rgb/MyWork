<template>
  <div class="user-wrapper" :class="theme">
    <!-- update_begin author:zhaoxin date:20191129 for: 做头部菜单栏导航 -->
    <!-- update-begin author:sunjianlei date:20191@20 for: 解决全局样式冲突的问题 -->
    <span class="action" @click="showClick">
      <a-icon type="search"></a-icon>
    </span>
    <!-- update-begin author:sunjianlei date:20200219 for: 菜单搜索改为动态组件，在手机端呈现出弹出框 -->
    <component :is="searchMenuComp" v-show="searchMenuVisible || isMobile()" class="borders" :visible="searchMenuVisible" title="搜索菜单" :footer="null" @cancel="searchMenuVisible=false">
      <a-select
        class="search-input"
        showSearch
        :showArrow="false"
        placeholder="搜索菜单"
        optionFilterProp="children"
        :filterOption="filterOption"
        :open="isMobile()?true:null"
        :getPopupContainer="(node) => node.parentNode"
        :style="isMobile()?{width: '100%',marginBottom:'50px'}:{}"
        @change="searchMethods"
        @blur="hiddenClick"
      >
        <a-select-option v-for="(site,index) in searchMenuOptions" :key="index" :value="site.id">{{site.meta.title}}</a-select-option>
      </a-select>
    </component>
    <!-- update-end author:sunjianlei date:20200219 for: 菜单搜索改为动态组件，在手机端呈现出弹出框 -->
    <!-- update-end author:sunjianlei date:20191220 for: 解决全局样式冲突的问题 -->
    <!-- update_end  author:zhaoxin date:20191129 for: 做头部菜单栏导航 -->
<!--    <span class="action">-->
<!--      <a class="logout_title" target="_blank" href="http://doc.jeecg.com">-->
<!--        <a-icon type="question-circle-o"></a-icon>-->
<!--      </a>-->
<!--    </span>-->
    <header-notice class="action"/>
    <a-dropdown>
      <span class="action action-full ant-dropdown-link user-dropdown-menu">
        <a-avatar class="avatar" size="small" :src="getAvatar()"/>
        <span v-if="isDesktop()">欢迎您，{{ nickname() }}</span>
      </span>
      <a-menu slot="overlay" class="user-dropdown-menu-wrapper">
<!--        <a-menu-item key="0">-->
<!--          <router-link :to="{ name: 'account-center' }">-->
<!--            <a-icon type="user"/>-->
<!--            <span>个人中心</span>-->
<!--          </router-link>-->
<!--        </a-menu-item>-->
<!--        <a-menu-item key="1">-->
<!--          <router-link :to="{ name: 'account-settings-base' }">-->
<!--            <a-icon type="setting"/>-->
<!--            <span>账户设置</span>-->
<!--          </router-link>-->
<!--        </a-menu-item>-->
<!--        <a-menu-item key="3"  @click="systemSetting">-->
<!--           <a-icon type="tool"/>-->
<!--           <span>系统设置</span>-->
<!--        </a-menu-item>-->
        <a-menu-item key="4" @click="updatePassword">
          <a-icon type="setting"/>
          <span>密码修改</span>
        </a-menu-item>
<!--        <a-menu-item key="5" @click="updateCurrentDepart">-->
<!--          <a-icon type="cluster"/>-->
<!--          <span>切换部门</span>-->
<!--        </a-menu-item>-->
        <a-menu-item key="6" @click="clearCache">
          <a-icon type="sync"/>
          <span>清理缓存</span>
        </a-menu-item>
       <!-- <a-menu-item key="2" disabled>
          <a-icon type="setting"/>
          <span>测试</span>
        </a-menu-item>
        <a-menu-divider/>
        <a-menu-item key="3">
          <a href="javascript:;" @click="handleLogout">
            <a-icon type="logout"/>
            <span>退出登录</span>
          </a>
        </a-menu-item>-->
      </a-menu>
    </a-dropdown>
    <span class="action">
      <a class="logout_title" href="javascript:;" @click="handleLogout">
        <a-icon type="logout"/>
        <span v-if="isDesktop()">&nbsp;退出登录</span>
      </a>
    </span>
    <user-password ref="userPassword"></user-password>
    <depart-select ref="departSelect" :closable="true" title="部门切换"></depart-select>
    <setting-drawer ref="settingDrawer" :closable="true" title="系统设置"></setting-drawer>
  </div>
</template>

<script>
  import HeaderNotice from './HeaderNotice'
  import UserPassword from './UserPassword'
  import SettingDrawer from "@/components/setting/SettingDrawer";
  import DepartSelect from './DepartSelect'
  import { mapActions, mapGetters,mapState } from 'vuex'
  import { mixinDevice } from '@/utils/mixin.js'
  import { getFileAccessHttpUrl,getAction } from "@/api/manage"
  import Vue from 'vue'
  import { UI_CACHE_DB_DICT_DATA,SYS_DEPART_ORGCODE,SAFETY_PARTIAL_BASIC} from "@/store/mutation-types"

  export default {
    name: "UserMenu",
    mixins: [mixinDevice],
    data(){
      return{
        // update-begin author:sunjianlei date:20200219 for: 头部菜单搜索规范命名 --------------
        searchMenuOptions:[],
        searchMenuComp: 'span',
        searchMenuVisible: false,
        // update-begin author:sunjianlei date:20200219 for: 头部菜单搜索规范命名 --------------
      }
    },
    components: {
      HeaderNotice,
      UserPassword,
      DepartSelect,
      SettingDrawer
    },
    props: {
      theme: {
        type: String,
        required: false,
        default: 'dark'
      }
    },
    /* update_begin author:zhaoxin date:20191129 for: 做头部菜单栏导航*/
    created() {
      let lists = []
      this.searchMenus(lists,this.permissionMenuList)
      this.searchMenuOptions=[...lists]
    },
    mounted() {
      //如果是单点登录模式
      if (process.env.VUE_APP_SSO == 'true') {
        let depart = this.userInfo().orgCode
        if (!depart) {
          this.updateCurrentDepart()
        }
      }
    },
    computed: {
      ...mapState({
        // 后台菜单
        permissionMenuList: state => state.user.permissionList

      })
    },
    /* update_end author:zhaoxin date:20191129 for: 做头部菜单栏导航*/
    watch: {
      // update-begin author:sunjianlei date:20200219 for: 菜单搜索改为动态组件，在手机端呈现出弹出框
      device: {
        immediate: true,
        handler() {
          this.searchMenuVisible = false
          this.searchMenuComp = this.isMobile() ? 'a-modal' : 'span'
        },
      },
      // update-end author:sunjianlei date:20200219 for: 菜单搜索改为动态组件，在手机端呈现出弹出框
    },
    methods: {
      /* update_begin author:zhaoxin date:20191129 for: 做头部菜单栏导航*/
      showClick() {
        this.searchMenuVisible = true
      },
      hiddenClick(){
        this.shows = false
      },
      /* update_end author:zhaoxin date:20191129 for: 做头部菜单栏导航*/
      ...mapActions(["Logout"]),
      ...mapGetters(["nickname", "avatar","userInfo"]),
      getAvatar(){
        return getFileAccessHttpUrl(this.avatar())
      },
      handleLogout() {
        const that = this

        this.$confirm({
          title: '提示',
          content: '真的要注销登录吗 ?',
          onOk() {
            return that.Logout({}).then(() => {
              Vue.ls.remove('SYS_DEPART_ORGCODE');
              Vue.ls.set('SYS_DEPART_ORGCODE', '');
              Vue.ls.remove('SAFETY_PARTIAL_BASIC');
              Vue.ls.set('SAFETY_PARTIAL_BASIC','');
              // update-begin author:wangshuai date:20200601 for: 退出登录跳转登录页面
              that.$router.push({ path: '/user/login' });
              // update-end author:wangshuai date:20200601 for: 退出登录跳转登录页面
              //window.location.reload()
            }).catch(err => {
              that.$message.error({
                title: '错误',
                description: err.message
              })
            })
          },
          onCancel() {
          },
        });
      },
      updatePassword(){
        let username = this.userInfo().username
        this.$refs.userPassword.show(username)
      },
      updateCurrentDepart(){
        this.$refs.departSelect.show()
      },
      systemSetting(){
        this.$refs.settingDrawer.showDrawer()
      },
      /* update_begin author:zhaoxin date:20191129 for: 做头部菜单栏导航*/
      searchMenus(arr,menus){
        for(let i of menus){
          if(!i.hidden && "layouts/RouteView"!==i.component){
           arr.push(i)
          }
          if(i.children&& i.children.length>0){
            this.searchMenus(arr,i.children)
          }
        }
      },
      filterOption(input, option) {
        return option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      },
      // update_begin author:sunjianlei date:20191230 for: 解决外部链接打开失败的问题
     searchMethods(value) {
        let route = this.searchMenuOptions.filter(item => item.id === value)[0]
        //update-begin-author:taoyan date:20210528 for: 【菜单问题】配置一个iframe地址的菜单，内部打开，在搜索菜单上打开却新开了一个窗口
        if (route.meta.internalOrExternal === true) {
          window.open(route.meta.url, '_blank')
        } else {
          if(route.component.includes('layouts/IframePageView')){
            this.$router.push(route)
          }else{
            this.$router.push({ path: route.path })
          }
        }
        //update-end-author:taoyan date:20210528 for: 【菜单问题】配置一个iframe地址的菜单，内部打开，在搜索菜单上打开却新开了一个窗口
        this.searchMenuVisible = false
      },
      // update_end author:sunjianlei date:20191230 for: 解决外部链接打开失败的问题
      /*update_end author:zhaoxin date:20191129 for: 做头部菜单栏导航*/
      /*update_begin author:liushaoqian date:20200507 for: 刷新缓存*/
      clearCache(){
        getAction("sys/dict/refleshCache").then((res) => {
          if (res.success) {
            //重新加载缓存
            getAction("sys/dict/queryAllDictItems").then((res) => {
              if (res.success) {
                Vue.ls.remove('SAFETY_PARTIAL_BASIC')
                Vue.ls.set('SAFETY_PARTIAL_BASIC','')
                Vue.ls.remove(UI_CACHE_DB_DICT_DATA)
                Vue.ls.set(UI_CACHE_DB_DICT_DATA, res.result, 7 * 24 * 60 * 60 * 1000)
              }
            })
            this.$message.success("刷新缓存完成！");
          }
        }).catch(e=>{
          this.$message.warn("刷新缓存失败！");
          console.log("刷新失败",e)
        })
      }
      /*update_end author:liushaoqian date:20200507 for: 刷新缓存*/
    }
  }
</script>

<style lang="less" scoped>
  /* update_begin author:zhaoxin date:20191129 for: 让搜索框颜色能随主题颜色变换*/
  /* update-begin author:sunjianlei date:20191220 for: 解决全局样式冲突问题 */
  .user-wrapper .search-input {
    width: 180px;
    color: inherit;

    /deep/ .ant-select-selection {
      background-color: inherit;
      border: 0;
      border-bottom: 1px solid white;
      &__placeholder, &__field__placeholder {
        color: inherit;
      }
    }
  }
  /* update-end author:sunjianlei date:20191220 for: 解决全局样式冲突问题 */
  /* update_end author:zhaoxin date:20191129 for: 让搜索框颜色能随主题颜色变换*/
</style>

<style scoped>
  .logout_title {
    color: inherit;
    text-decoration: none;
  }
</style>