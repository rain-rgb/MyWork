import Vue from 'vue'
import router from './router'
import store from './store'
import {handertoken} from "@/mixins/getHanderToken";
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import notification from 'ant-design-vue/es/notification'
import { ACCESS_TOKEN, INDEX_MAIN_PAGE_PATH } from '@/store/mutation-types'
import { generateIndexRouter } from '@/utils/util'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/user/login1' ,'/user/login', '/user/register', '/user/register-result', '/user/alteration',
  '/sygl/hnthtconsign/HnthtConsignList','/zlgl/tpny/FrontDeviceHistorydataList', '/bhz/swbhz/BhzSwBasesList'
  , '/bhz/lqbhz/BhzLqBasesList','/bhz/lqbhz/BhzLqBasesCBClList','/bhz/lqbhz/BhzLqBasesCbList','/bhz/lqbhz/BhzLqBaseSta'
  ,'/bhz/swbhz/BhzSwBasesCbClList','/bhz/swbhz/BhzSwBasesCBList','/bhz/swbhz/BhzSwBaseSta','/td/TadiaoList'
  ,'/td/tadiaoHis/TadiaoHisList','/zjjc/zjjccx/ZhJinJiCeBaseList','/syj/kongjing/KongjingBasicinfoList'
  ,'/zlyj/TrZhanglaXxbList','/zlyj/TrYajiangMList','/bhz/hntbhz/BhzHntCbCxtBaseList',
   '/zhangla/ZhangLaList',
  '/zlyj/yj/yjBasesList', '/bys/byssj/BysSjjkBasesList', '/syj/ylj/SyyLjBasesList', '/syj/wnj/WnjList', '/kykzj/kykz/KyKzJBasesList','/kykzj/kykz/KyKzJBasesCBList','/kykzj/kykz/kykzBaseCBCZList',
  '/sthb/hjjc/DevicehistoryList','/zlgl/snjbz/SmwsegmentataBaseList','/anquan/zhyd/realdata/DeviceElectricRealdataList',
  '/anquan/weiyan/WeiyanBaseList','/lmd/DeviceCraneHistorydataList','/anquan/gualan/GualanBaseList','/anquan/sdhj/DeviceTunnelEnvironmentdataList',
  '/jqj/jqjsjjc/JqjShujubaseList','/zlgl/tpny/FrontDeviceRealdataList','/zlgl/yclsl/WzycljinchanggbList','/syj/hylytj/HylytjList','/zlgl/wztaizhang/WztaizhangList'
  ,'/system/modules/shebeiMap','/anquan/safetyhelmet/SafetyHelmetAlertList','/syj/wnj/WnjCBCZList','/lumian/swlumian/swshebeiMap'
  ,'/bhz/lqbhz/BhzLqBasesList','/bhz/lqbhz/FrontDischargeholeDataList'
  ,'/bhz/lqbhz/FrontDischargeholeRealdataList','/bhz/swbhz/BhzSwBasesList'
  ,'/bhz/lqbhz/modules/BhzLqBaseCBCXForm','/bhz/lqbhz/modules/BhzLqBasesForm','/bhz/lqbhz/modules/FrontDischargeholeDataForm','/bhz/lqbhz/modules/FrontDischargeholeRealdataForm'
  ,'/bhz/swbhz/modules/BhzSwBasesCBCXForm','/bhz/swbhz/modules/BhzSwBasesForm','/syj/hntht/TrHnthtMList'
  ,'/anquan/jiaqiaoji/jiaqiaojireal/DeviceCraneRealdataList',
  '/anquan/gualan/gualanreal/GualanBaseRealList','/anquan/gualan/GualanBaseList','/lmd/lmdrealdata/DeviceCraneRealdataList','/anquan/aiwarn/aiwarnmsg/AiWarnMsgList','/officialfile/dashijifile/OfficialFileList'
,'/bhz/entitycheak/EntityProgresscheckList','/anquan/mjkq/EntranceGuardRecordList','/anquan/sdhj/DeviceTunnelEnvironmentdataList','/zlgl/yclcl/WzyclchuchanggbList'
,'/checklist/invlist/InvestmentChecklistList','/checklist/prolist/ProductionValuelistList','/checklist/record/onboarding/OnboardingRecordList','/checklist/record/comparison/ComparisonRecordList','/checklist/innovation/AchievementsListList',
'/checklist/salary/SalaryListList','/checklist/benefit/biangengpifu/ChangeSpprovalList','/checklist/benefit/biangengzaojia/ChangeCostControlList','/checklist/benefit/jiliangzhifu/MeteredPaymentList',
'/checklist/benefit/jiliangzhixing/MeasurementExecutionRateList','/checklist/quality/MaterialTestingList','/snjbz/devicepipepilehistorydataone/DevicePipepileHistorydataOneList',
'/anquan/mjkq/carmjkq/SafetyTunnelCarHistoryList','/car/vehicle/VehicleManagementList','/car/wbdestination/WbDestinationList','/car/wbshebeidetail/WbshebeiDetailList','/zlgl/wztaizhang/wztaizhangnew/WztaizhangLists',
  '/dashboard/CMindex','/anquan/zhyd/historydata/DeviceElectricHistorydataList','/zlyj/TrZhanglachaobiaoList','/zlyj/TrZhanglachaobiaoCLList','/zlyj/TrYajiangchaobiaoList','/zlyj/TrYajiangchaobiaoCLList','/anquan/zhyd/historydata/DeviceElectricHistorydataListCB'
  ,"/zlyj/TrZhanglaXxbList/detail","/zlyj/TrZhanglaXxbListTwo/detail","/yj/TrYajiangMList/detail","/bhz/hntbhz/BhzCementBaseCBFormTwo",'/system/modules/ShigongphbModalTwo','/bhz/czsh/ChuZhiTwoLone','/bhz/czsh/ShenHeTwoLone','/bhz/czsh/ShenPiLone',
  "/bhz/lqbhz/BhzLqBaseCBCXModalLone","/bhz/swbhz/BhzSwBasesCBCXModalLone","/zl/czsh/zlChuZhiLone","/zl/czsh/zlShenHeLone","/zl/czsh/zlShenPiLone","/yj/czsh/yjChuZhiLone","/yj/czsh/yjShenHeLone","/yj/czsh/yjShenPiLone","/bhz/bhzSupervisionOrder/InstructModelLone",
  '/lmd/lmdrealdata/DeviceCraneRealdataModalDetail','/lumianxx/modules/reportDetail'
] // no redirect whitelist

const whiteList2 = ['/yj/TrYajiangMList','/zl/TrZhanglaXxbList','/syj/wnj/WnjCBList','/syj/gangbao/TrGangjinbhcMList','/zl/TrZhanglachaobiaoList','/yj/TrYajiangchaobiaoList','/bhz/hntbhz/BhzCementBaseCBList','/syj/ylj/SyyLjBasesCBList','/bhz/hntbhz/BhzCementBaseList','/bhz/hntbhz/BhzCementBaseYLList']



router.beforeEach((to, from, next) => {
  NProgress.start() // start progress bar

  let reg = new RegExp('(^|&)' + 'token' + '=([^&]*)(&|$)')
  let r = window.location.search.substr(1).match(reg)
  if (r != null && !unescape(r[2]).startsWith("Bearer") ) Vue.ls.set(ACCESS_TOKEN, unescape(r[2]), 14 * 60 * 60 * 1000)

  if (Vue.ls.get(ACCESS_TOKEN) ) {
    /* has token */
    if (to.path === '/user/login') {
      next({ path: INDEX_MAIN_PAGE_PATH })
      NProgress.done()
    } else if(to.path === '/user/login1'){
      localStorage.clear()
      sessionStorage.clear()
      // next({ path: INDEX_MAIN_PAGE_PATH })
      // NProgress.done()
      next()

    }else {
      if (store.getters.permissionList.length === 0) {
        store.dispatch('GetPermissionList').then(res => {
          const menuData = res.result.menu
          //console.log(res.message)
          if (menuData === null || menuData === '' || menuData === undefined) {
            return
          }
          let constRoutes = []
          constRoutes = generateIndexRouter(menuData)
          // 添加主界面路由
          store.dispatch('UpdateAppRouter', { constRoutes }).then(() => {
            // 根据roles权限生成可访问的路由表
            // 动态添加可访问路由表
            router.addRoutes(store.getters.addRouters)
            const redirect = decodeURIComponent(from.query.redirect || to.path)
            if (to.path === redirect) {
              // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
              next({ ...to, replace: true })
            } else {
              // 跳转到目的路由
              next({ path: redirect })
            }
          })
        })
          .catch(() => {
            /* notification.error({
               message: '系统提示',
               description: '请求用户信息失败，请重试！'
             })*/
            store.dispatch('Logout').then(() => {
              next({ path: '/user/login', query: { redirect: to.fullPath } })
            })
          })
      } else {
        next()
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    }
    // else if(to.path === ){
    //
    // }
    else if(whiteList2.indexOf(to.path) !== -1){
      // handertoken.methods.getToken();
      if (store.getters.permissionList.length === 0) {
        store.dispatch('GetPermissionList').then(res => {
          const menuData = res.result.menu
          //console.log(res.message)
          if (menuData === null || menuData === '' || menuData === undefined) {
            return
          }
          let constRoutes = []
          constRoutes = generateIndexRouter(menuData)
          // 添加主界面路由
          store.dispatch('UpdateAppRouter', { constRoutes }).then(() => {
            // 根据roles权限生成可访问的路由表
            // 动态添加可访问路由表
            router.addRoutes(store.getters.addRouters)
            const redirect = decodeURIComponent(from.query.redirect || to.path)
            if (to.path === redirect) {
              // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
              next({ ...to, replace: true })
            } else {
              // 跳转到目的路由
              next({ path: redirect })
            }
          })
        })
          .catch(() => {
            /* notification.error({
               message: '系统提示',
               description: '请求用户信息失败，请重试！'
             })*/
            store.dispatch('Logout').then(() => {
              next({ path: '/user/login', query: { redirect: to.fullPath } })
            })
          })
      } else {
        next()
      }


    } else {
      next({ path: '/user/login', query: { redirect: to.fullPath } })
      NProgress.done() // if current page is login will not trigger afterEach hook, so manually handle it
    }
  }
})

router.afterEach(() => {
  NProgress.done() // finish progress bar
})
