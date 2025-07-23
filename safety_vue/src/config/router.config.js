import { UserLayout, TabLayout, RouteView, BlankLayout, PageView } from '@/components/layouts'

/**
 * 走菜单，走权限控制
 * @type {[null,null]}
 */
export const asyncRouterMap = [

  {
    path: '/',
    name: 'dashboard',
    component: TabLayout,
    meta: { title: '首页' },
    redirect: '/dashboard/analysis',///dashboard/analysis
    children: [

      // // dashboard
      // {
      //   path: '/dashboard',
      //   name: 'dashboard',
      //   redirect: '/dashboard/workplace',
      //   component: RouteView,
      //   meta: { title: '仪表盘', icon: 'dashboard', permission: [ 'dashboard' ] },
      //   children: [
      //     {
      //       path: '/dashboard/analysis',
      //       name: 'Analysis',
      //       component: () => import('@/views/dashboard/Analysis'),
      //       meta: { title: '分析页', permission: [ 'dashboard' ] }
      //     },
      //     {
      //       path: '/dashboard/monitor',
      //       name: 'Monitor',
      //       hidden: true,
      //       component: () => import('@/views/dashboard/Monitor'),
      //       meta: { title: '监控页', permission: [ 'dashboard' ] }
      //     },
      //     {
      //       path: '/dashboard/workplace',
      //       name: 'Workplace',
      //       component: () => import('@/views/dashboard/Workplace'),
      //       meta: { title: '工作台', permission: [ 'dashboard' ] }
      //     }
      //   ]
      // },
      //
      // // forms
      // {
      //   path: '/form',
      //   redirect: '/form/basic-form',
      //   component: PageView,
      //   meta: { title: '表单页', icon: 'form', permission: [ 'form' ] },
      //   children: [
      //     {
      //       path: '/form/base-form',
      //       name: 'BaseForm',
      //       component: () => import('@/views/form/BasicForm'),
      //       meta: { title: '基础表单', permission: [ 'form' ] }
      //     },
      //     {
      //       path: '/form/step-form',
      //       name: 'StepForm',
      //       component: () => import('@/views/form/stepForm/StepForm'),
      //       meta: { title: '分步表单', permission: [ 'form' ] }
      //     },
      //     {
      //       path: '/form/advanced-form',
      //       name: 'AdvanceForm',
      //       component: () => import('@/views/form/advancedForm/AdvancedForm'),
      //       meta: { title: '高级表单', permission: [ 'form' ] }
      //     }
      //   ]
      // },
      //
      // // list
      // {
      //   path: '/list',
      //   name: 'list',
      //   component: PageView,
      //   redirect: '/list/query-list',
      //   meta: { title: '列表页', icon: 'table', permission: [ 'table' ] },
      //   children: [
      //     {
      //       path: '/list/query-list',
      //       name: 'QueryList',
      //       component: () => import('@/views/list/TableList'),
      //       meta: { title: '查询表格', permission: [ 'table' ] }
      //     },
      //     {
      //       path: '/list/edit-table',
      //       name: 'EditList',
      //       component: () => import('@/views/list/TableInnerEditList'),
      //       meta: { title: '内联编辑表格', permission: [ 'table' ] }
      //     },
      //     {
      //       path: '/list/user-list',
      //       name: 'UserList',
      //       component: () => import('@/views/list/UserList'),
      //       meta: { title: '用户列表', permission: [ 'table' ] }
      //     },
      //     {
      //       path: '/list/role-list',
      //       name: 'RoleList',
      //       component: () => import('@/views/list/RoleList'),
      //       meta: { title: '角色列表', permission: [ 'table' ] }
      //     },
      //     {
      //       path: '/list/permission-list',
      //       name: 'PermissionList',
      //       component: () => import('@/views/list/PermissionList'),
      //       meta: { title: '权限列表', permission: [ 'table' ] }
      //     },
      //     {
      //       path: '/list/basic-list',
      //       name: 'BasicList',
      //       component: () => import('@/views/list/StandardList'),
      //       meta: { title: '标准列表', permission: [ 'table' ] }
      //     },
      //     {
      //       path: '/list/card',
      //       name: 'CardList',
      //       component: () => import('@/views/list/CardList'),
      //       meta: { title: '卡片列表', permission: [ 'table' ] }
      //     },
      //     {
      //       path: '/list/search',
      //       name: 'SearchList',
      //       component: () => import('@/views/list/search/SearchLayout'),
      //       redirect: '/list/search/article',
      //       meta: { title: '搜索列表', permission: [ 'table' ] },
      //       children: [
      //         {
      //           path: '/list/search/article',
      //           name: 'SearchArticles',
      //           component: () => import('../views/list/TableList'),
      //           meta: { title: '搜索列表（文章）', permission: [ 'table' ] }
      //         },
      //         {
      //           path: '/list/search/project',
      //           name: 'SearchProjects',
      //           component: () => import('../views/list/TableList'),
      //           meta: { title: '搜索列表（项目）', permission: [ 'table' ] }
      //         },
      //         {
      //           path: '/list/search/application',
      //           name: 'SearchApplications',
      //           component: () => import('../views/list/TableList'),
      //           meta: { title: '搜索列表（应用）', permission: [ 'table' ] }
      //         },
      //       ]
      //     },
      //   ]
      // },
      //
      // // profile
      // {
      //   path: '/profile',
      //   name: 'profile',
      //   component: RouteView,
      //   redirect: '/profile/basic',
      //   meta: { title: '详情页', icon: 'profile', permission: [ 'profile' ] },
      //   children: [
      //     {
      //       path: '/profile/basic',
      //       name: 'ProfileBasic',
      //       component: () => import('@/views/profile/basic/Index'),
      //       meta: { title: '基础详情页', permission: [ 'profile' ] }
      //     },
      //     {
      //       path: '/profile/advanced',
      //       name: 'ProfileAdvanced',
      //       component: () => import('@/views/profile/advanced/Advanced'),
      //       meta: { title: '高级详情页', permission: [ 'profile' ] }
      //     }
      //   ]
      // },
      //
      // // result
      // {
      //   path: '/result',
      //   name: 'result',
      //   component: PageView,
      //   redirect: '/result/success',
      //   meta: { title: '结果页', icon: 'check-circle-o', permission: [ 'result' ] },
      //   children: [
      //     {
      //       path: '/result/success',
      //       name: 'ResultSuccess',
      //       component: () => import(/* webpackChunkName: "result" */ '@/views/result/Success'),
      //       meta: { title: '成功', hiddenHeaderContent: true, permission: [ 'result' ] }
      //     },
      //     {
      //       path: '/result/fail',
      //       name: 'ResultFail',
      //       component: () => import(/* webpackChunkName: "result" */ '@/views/result/Error'),
      //       meta: { title: '失败', hiddenHeaderContent: true, permission: [ 'result' ] }
      //     }
      //   ]
      // },
      //
      // // Exception
      // {
      //   path: '/exception',
      //   name: 'exception',
      //   component: RouteView,
      //   redirect: '/exception/403',
      //   meta: { title: '异常页', icon: 'warning', permission: [ 'exception' ] },
      //   children: [
      //     {
      //       path: '/exception/403',
      //       name: 'Exception403',
      //       component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/403'),
      //       meta: { title: '403', permission: [ 'exception' ] }
      //     },
      //     {
      //       path: '/exception/404',
      //       name: 'Exception404',
      //       component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404'),
      //       meta: { title: '404', permission: [ 'exception' ] }
      //     },
      //     {
      //       path: '/exception/500',
      //       name: 'Exception500',
      //       component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/500'),
      //       meta: { title: '500', permission: [ 'exception' ] }
      //     }
      //   ]
      // },
      //
      // // account
      // {
      //   path: '/account',
      //   component: RouteView,
      //   name: 'account',
      //   meta: { title: '个人页', icon: 'user', keepAlive: true, permission: [ 'user' ] },
      //   children: [
      //     {
      //       path: '/account/center',
      //       name: 'center',
      //       component: () => import('@/views/account/center/Index'),
      //       meta: { title: '个人中心', keepAlive: true, permission: [ 'user' ] }
      //     },
      //     {
      //       path: '/account/settings',
      //       name: 'settings',
      //       component: () => import('@/views/account/settings/Index'),
      //       meta: { title: '个人设置', hideHeader: true, keepAlive: true, permission: [ 'user' ]  },
      //       redirect: '/account/settings/base',
      //       alwaysShow: true,
      //       children: [
      //         {
      //           path: '/account/settings/base',
      //           name: 'BaseSettings',
      //           component: () => import('@/views/account/settings/BaseSetting'),
      //           meta: { title: '基本设置', hidden: true, keepAlive: true, permission: [ 'user' ]  }
      //         },
      //         {
      //           path: '/account/settings/security',
      //           name: 'SecuritySettings',
      //           component: () => import('@/views/account/settings/Security'),
      //           meta: { title: '安全设置', hidden: true, keepAlive: true, permission: [ 'user' ]  }
      //         },
      //         {
      //           path: '/account/settings/custom',
      //           name: 'CustomSettings',
      //           component: () => import('@/views/account/settings/Custom'),
      //           meta: { title: '个性化设置', hidden: true, keepAlive: true, permission: [ 'user' ]  }
      //         },
      //         {
      //           path: '/account/settings/binding',
      //           name: 'BindingSettings',
      //           component: () => import('@/views/account/settings/Binding'),
      //           meta: { title: '账户绑定', hidden: true, keepAlive: true, permission: [ 'user' ]  }
      //         },
      //         {
      //           path: '/account/settings/notification',
      //           name: 'NotificationSettings',
      //           component: () => import('@/views/account/settings/Notification'),
      //           meta: { title: '新消息通知', hidden: true, keepAlive: true, permission: [ 'user' ]  }
      //         },
      //       ]
      //     },
      //   ]
      // }
    ],
  },

  {
    path: '*', redirect: '/404', hidden: true
  }
]

/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/login',
    hidden: true,
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/Login')
      },
      {
        path: 'login1',
        name: 'login1',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/Login1')
      },
      {
        path: 'register',
        name: 'register',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/register/Register')
      },
      {
        path: 'register-result',
        name: 'registerResult',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/register/RegisterResult')
      },
      {
        path: 'alteration',
        name: 'alteration',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/alteration/Alteration')
      },

    ]
  },
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/login1',
    hidden: true,
    children: [
      {
        path: 'login1',
        name: 'login1',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/Login1')
      },
      {
        path: 'register',
        name: 'register',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/register/Register')
      },
      {
        path: 'register-result',
        name: 'registerResult',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/register/RegisterResult')
      },
      {
        path: 'alteration',
        name: 'alteration',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/alteration/Alteration')
      },

    ]
  },

  // {
  //   path: '/',
  //   name: 'index',
  //   component: TabLayout,
  //   meta: {title: '首页'},
  //   redirect: '/dashboard/workplace',
  //   children: [
  //     {
  //       path: '/online',

  //       name: 'online',
  //       redirect: '/online',
  //       component: RouteView,
  //       meta: {title: '在线开发', icon: 'dashboard', permission: ['dashboard']},
  //       children: [
  //         {
  //           path: '/online/auto/:code',
  //           name: 'report',
  //           component: () => import('@/views/modules/online/cgreport/OnlCgreportAutoList')
  //         },
  //       ]
  //     },
  //   ]
  // },
   {
    // OAuth2 APP页面路由
    path: '/oauth2-app',
    component: BlankLayout,
    redirect: '/oauth2-app/login',
    children: [
      {
        // OAuth2 登录路由
        path: 'login',
        name: 'login',
        component: () => import(/* webpackChunkName: "oauth2-app.login" */ '@/views/user/oauth2/OAuth2Login')
      },
    ]
  },

  {
    path: '/test',
    component: BlankLayout,
    redirect: '/test/home',
    children: [
      {
        path: 'home',
        name: 'TestHome',
        component: () => import('@/views/Home')
      }
    ]
  },
  {
    path: '/dashboard',
    component: BlankLayout,
    redirect: '/dashboard/CMindex',
    children: [
      {
        path: 'CMindex',
        name: 'CMindex',
        component: () => import('@/views/dashboard/CMindex')
      }
    ]
  },
  // {
  //   path: '/bhz/hntbhz',
  //   component: BlankLayout,
  //   redirect: '/bhz/hntbhz/BhzCementBaseList',
  //   children: [
  //     {
  //       path: 'BhzCementBaseList',
  //       name: 'BhzCementBaseList',
  //       component: () => import('@/views/bhz/hntbhz/BhzCementBaseList')
  //     }
  //   ]
  // },
  // {
  //   path: '/bhz/hntbhz',
  //   component: BlankLayout,
  //   redirect: '/bhz/hntbhz/BhzCementBaseYLList',
  //   children: [
  //     {
  //       path: 'BhzCementBaseYLList',
  //       name: 'BhzCementBaseYLList',
  //       component: () => import('@/views/bhz/hntbhz/BhzCementBaseYLList')
  //     }
  //   ]
  // },

  {
    path: '/bhz/lqbhz',
    component: BlankLayout,
    redirect: '/bhz/lqbhz/BhzLqBasesList',
    children: [
      {
        path: 'BhzLqBasesList',
        name: 'BhzLqBasesList',
        component: () => import('@/views/bhz/lqbhz/BhzLqBasesList')
      },
      {
        path: 'BhzLqBaseCBCXModalLone',
        name: 'BhzLqBaseCBCXModalLone',
        component: () => import('@/views/bhz/lqbhz/modules/BhzLqBaseCBCXModalLone')
      }
    ]
  },
  {
    path: '/bhz/swbhz',
    component: BlankLayout,
    redirect: '/bhz/swbhz/BhzSwBasesList',
    children: [
      {
        path: 'BhzSwBasesList',
        name: 'BhzSwBasesList',
        component: () => import('@/views/bhz/swbhz/BhzSwBasesList')
      },
      {
        path: 'BhzSwBasesCBCXModalLone',
        name: 'BhzSwBasesCBCXModalLone',
        component: () => import('@/views/bhz/swbhz/modules/BhzSwBasesCBCXModalLone')
      }
    ]
  },
  {
    path: '/bhz/bhzSupervisionOrder',//物联网指令单
    component: BlankLayout,
    redirect: '/bhz/bhzSupervisionOrder/InstructModelLone',
    children: [
      {
        path: 'InstructModelLone',
        name: 'InstructModelLone',
        component: () => import('@/views/bhz/bhzSupervisionOrder/modules/InstructModelLone')
      }
    ]
  },
  {
    path: '/bys/byssj',
    component: BlankLayout,
    redirect: '/bys/byssj/BysSjjkBasesList',
    children: [
      {
        path: 'BysSjjkBasesList',
        name: 'BysSjjkBasesList',
        component: () => import('@/views/bys/byssj/BysSjjkBasesList')
      }
    ]
  },
  {
    path: '/syj/ylj',
    component: BlankLayout,
    redirect: '/syj/ylj/SyyLjBasesList',
    children: [
      {
        path: 'SyyLjBasesList',
        name: 'SyyLjBasesList',
        component: () => import('@/views/syj/ylj/SyyLjBasesList')
      }
    ]
  },
  // {
  //   path: '/syj/ylj',
  //   component: BlankLayout,
  //   redirect: '/syj/ylj/SyyLjBasesCBList',
  //   children: [
  //     {
  //       path: 'SyyLjBasesCBList',
  //       name: 'SyyLjBasesCBList',
  //       component: () => import('@/views/syj/ylj/SyyLjBasesCBList')
  //     }
  //   ]
  // },
  // {
  //   path: '/bhz/hntbhz',
  //   component: BlankLayout,
  //   redirect: '/bhz/hntbhz/BhzCementBaseCBList',
  //   children: [
  //     {
  //       path: 'BhzCementBaseCBList',
  //       name: 'BhzCementBaseCBList',
  //       component: () => import('@/views/bhz/hntbhz/BhzCementBaseCBList')
  //     }
  //   ]
  // },
  {
    path: '/syj/wnj',
    component: BlankLayout,
    redirect: '/syj/wnj/WnjList',
    children: [
      {
        path: 'WnjList',
        name: 'WnjList',
        component: () => import('@/views/syj/wnj/WnjList')
      }
    ]
  },
  {
    path: '/kykzj/kykz',
    component: BlankLayout,
    redirect: '/kykzj/kykz/KyKzJBasesList',
    children: [
      {
        path: 'KyKzJBasesList',
        name: 'KyKzJBasesList',
        component: () => import('@/views/kykzj/kykz/KyKzJBasesList')
      }
    ]
  },


  {
    path: '/kykzj/kykz',
    component: BlankLayout,
    redirect: '/kykzj/kykz/KyKzJBasesCBList',
    children: [
      {
        path: 'KyKzJBasesCBList',
        name: 'KyKzJBasesCBList',
        component: () => import('@/views/kykzj/kykz/KyKzJBasesCBList')
      }
    ]
  },
  {
    path: '/kykzj/kykz',
    component: BlankLayout,
    redirect: '/kykzj/kykz/kykzBaseCBCZList',
    children: [
      {
        path: 'KyKzJBasesList',
        name: 'KyKzJBasesList',
        component: () => import('@/views/kykzj/kykz/kykzBaseCBCZList')
      }
    ]
  },
  {
    path: '/sthb/hjjc',
    component: BlankLayout,
    redirect: '/sthb/hjjc/DevicehistoryList',
    children: [
      {
        path: 'DevicehistoryList',
        name: 'DevicehistoryList',
        component: () => import('@/views/sthb/hjjc/DevicehistoryList')
      }
    ]
  },
  {
    path: '/zlgl/snjbz',
    component: BlankLayout,
    redirect: '/zlgl/snjbz/SmwsegmentataBaseList',
    children: [
      {
        path: 'SmwsegmentataBaseList',
        name: 'SmwsegmentataBaseList',
        component: () => import('@/views/zlgl/snjbz/SmwsegmentataBaseList')
      }
    ]
  },
  {
    path: '/anquan/zhyd/realdata', // 智慧用电实时数据
    component: BlankLayout,
    redirect: '/anquan/zhyd/realdata/DeviceElectricRealdataList',
    children: [
      {
        path: 'DeviceElectricRealdataList',
        name: 'DeviceElectricRealdataList',
        component: () => import('@/views/anquan/zhyd/realdata/DeviceElectricRealdataList')
      }
    ]
  },
  {
    path: '/anquan/zhyd/historydata', // 智慧用电历史数据
    component: BlankLayout,
    redirect: '/anquan/zhyd/historydata/DeviceElectricHistorydataList',
    children: [
      {
        path: 'DeviceElectricHistorydataList',
        name: 'DeviceElectricHistorydataList',
        component: () => import('@/views/anquan/zhyd/historydata/DeviceElectricHistorydataList')
      }
    ]
  },
  {
    path: '/anquan/zhyd/historydata', // 智慧用电超标数据
    component: BlankLayout,
    redirect: '/anquan/zhyd/historydata/DeviceElectricHistorydataListCB',
    children: [
      {
        path: 'DeviceElectricHistorydataListCB',
        name: 'DeviceElectricHistorydataListCB',
        component: () => import('@/views/anquan/zhyd/historydata/DeviceElectricHistorydataListCB')
      }
    ]
  },
  {
    path: '/anquan/weiyan',
    component: BlankLayout,
    redirect: '/anquan/weiyan/WeiyanBaseList',
    children: [
      {
        path: 'WeiyanBaseList',
        name: 'WeiyanBaseList',
        component: () => import('@/views/anquan/weiyan/WeiyanBaseList')
      }
    ]
  },
  {
    path: '/lmd',//龙门吊
    component: BlankLayout,
    redirect: '/lmd/DeviceCraneHistorydataList',
    children: [
      {
        path: 'DeviceCraneHistorydataList',
        name: 'DeviceCraneHistorydataList',
        component: () => import('@/views/lmd/DeviceCraneHistorydataList')
      }
    ]
  },
  {
    path: '/anquan/gualan',
    component: BlankLayout,
    redirect: '/anquan/gualan/GualanBaseList',
    children: [
      {
        path: 'GualanBaseList',
        name: 'GualanBaseList',
        component: () => import('@/views/anquan/gualan/GualanBaseList')
      }
    ]
  },
  {
    path: '/anquan/sdhj',
    component: BlankLayout,
    redirect: '/anquan/sdhj/DeviceTunnelEnvironmentdataList',
    children: [
      {
        path: 'DeviceTunnelEnvironmentdataList',
        name: 'DeviceTunnelEnvironmentdataList',
        component: () => import('@/views/anquan/sdhj/DeviceTunnelEnvironmentdataList')
      }
    ]
  },
  {
    path: '/jqj/jqjsjjc',
    component: BlankLayout,
    redirect: '/jqj/jqjsjjc/JqjShujubaseList',
    children: [
      {
        path: 'JqjShujubaseList',
        name: 'JqjShujubaseList',
        component: () => import('@/views/jqj/jqjsjjc/JqjShujubaseList')
      }
    ]
  },
  {
    path: '/zlgl/tpny',
    component: BlankLayout,
    redirect: '/zlgl/tpny/FrontDeviceRealdataList',
    children: [
      {
        path: 'FrontDeviceRealdataList',
        name: 'FrontDeviceRealdataList',
        component: () => import('@/views/zlgl/tpny/FrontDeviceRealdataList')
      }
    ]
  },
  {
    path: '/zlgl/yclsl',
    component: BlankLayout,
    redirect: '/zlgl/yclsl/WzycljinchanggbList',
    children: [
      {
        path: 'WzycljinchanggbList',
        name: 'WzycljinchanggbList',
        component: () => import('@/views/zlgl/yclsl/WzycljinchanggbList')
      }
    ]
  },
  {
    path: '/syj/hylytj',
    component: BlankLayout,
    redirect: '/syj/hylytj/HylytjList',//恒应力一体机
    children: [
      {
        path: 'HylytjList',
        name: 'HylytjList',
        component: () => import('@/views/syj/hylytj/HylytjList')
      }
    ]
  },
  {
    path: '/zlgl/wztaizhang',
    component: BlankLayout,
    redirect: '/zlgl/wztaizhang/WztaizhangList',
    children: [
      {
        path: 'WztaizhangList',
        name: 'WztaizhangList',
        component: () => import('@/views/zlgl/wztaizhang/WztaizhangList')
      }
    ]
  },
  {
    path: '/system/modules',//设备实时定位
    component: BlankLayout,
    redirect: '/system/modules/shebeiMap',
    children: [
      {
        path: 'shebeiMap',
        name: 'shebeiMap',
        component: () => import('@/views/system/modules/shebeiMap')
      },
      {
        path: 'ShigongphbModalTwo',//配料单
        name: 'ShigongphbModalTwo',
        component: () => import('@/views/system/modules/ShigongphbModalTwo')
      }
    ]
  },
  {
    path: '/anquan/safetyhelmet',//安全帽监测
    component: BlankLayout,
    redirect: '/anquan/safetyhelmet/SafetyHelmetAlertList',
    children: [
      {
        path: 'SafetyHelmetAlertList',
        name: 'SafetyHelmetAlertList',
        component: () => import('@/views/anquan/safetyhelmet/SafetyHelmetAlertList')
      }
    ]
  },
  {
    path: '/system',//安全帽监测
    component: BlankLayout,
    redirect: '/system/ProjectInfoList',
    children: [
      {
        path: 'ProjectInfoList',
        name: 'ProjectInfoList',
        component: () => import('@/views/system/ProjectInfoList')
      }
    ]
  },
  {
    path: '/syj/wnj',//万能机超标闭合
    component: BlankLayout,
    redirect: '/syj/wnj/WnjCBCZList',
    children: [
      {
        path: 'WnjCBCZList',
        name: 'WnjCBCZList',
        component: () => import('@/views/syj/wnj/WnjCBCZList')
      }
    ]
  },
  // {
  //   path: '/syj/wnj',//万能机超标
  //   component: BlankLayout,
  //   redirect: '/syj/wnj/WnjCBList',
  //   children: [
  //     {
  //       path: 'WnjCBList',
  //       name: 'WnjCBList',
  //       component: () => import('@/views/syj/wnj/WnjCBList')
  //     }
  //   ]
  // },
  {
    path: '/lumian/swlumian',//路面摊铺碾压设备查询
    component: BlankLayout,
    redirect: '/lumian/swlumian/swshebeiMap',
    children: [
      {
        path: 'swshebeiMap',
        name: 'swshebeiMap',
        component: () => import('@/views/lumian/swlumian/swshebeiMap')
      }
    ]
  },

  {
  path: '/zlgl/tpny',//路面摊铺碾压历史数据查询
  component: BlankLayout,
  redirect: '/zlgl/tpny/FrontDeviceHistorydataList',
  children: [
  {
    path: 'FrontDeviceHistorydataList',
    name: 'FrontDeviceHistorydataList',
    component: () => import('@/views/zlgl/tpny/FrontDeviceHistorydataList')
  }]
  },
  {
    path: '/404',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404')
  },
  {
    path: '/bhz/lqbhz',//沥青超标处理 画面
    component: BlankLayout,
    redirect: '/bhz/lqbhz/BhzLqBasesCBClList',
    children: [
      {
        path: 'BhzLqBasesCBClList',
        name: 'BhzLqBasesCBClList',
        component: () => import('@/views/bhz/lqbhz/BhzLqBasesCBClList')
      }
    ]
  },
  {
    path: '/bhz/lqbhz',//BhzLqBasesCbList 画面
    component: BlankLayout,
    redirect: '/bhz/lqbhz/BhzLqBasesCbList',
    children: [
      {
        path: 'BhzLqBasesCbList',
        name: 'BhzLqBasesCbList',
        component: () => import('@/views/bhz/lqbhz/BhzLqBasesCbList')
      }
    ]
  },

  {
    path: '/bhz/lqbhz',//BhzLqBaseSta 画面
    component: BlankLayout,
    redirect: '/bhz/lqbhz/BhzLqBaseSta',
    children: [
      {
        path: 'BhzLqBaseSta',
        name: 'BhzLqBaseSta',
        component: () => import('@/views/bhz/lqbhz/BhzLqBaseSta')
      }
    ]
  },
  {
    path: '/bhz/lqbhz',//FrontDischargeholeDataList 画面
    component: BlankLayout,
    redirect: '/bhz/lqbhz/FrontDischargeholeDataList',
    children: [
      {
        path: 'FrontDischargeholeDataList',
        name: 'FrontDischargeholeDataList',
        component: () => import('@/views/bhz/lqbhz/FrontDischargeholeDataList')
      }
    ]
  },
  {
    path: '/bhz/lqbhz',//FrontDischargeholeRealdataList 画面
    component: BlankLayout,
    redirect: '/bhz/lqbhz/FrontDischargeholeRealdataList',
    children: [
      {
        path: 'FrontDischargeholeRealdataList',
        name: 'FrontDischargeholeRealdataList',
        component: () => import('@/views/bhz/lqbhz/FrontDischargeholeRealdataList')
      }
    ]
  },
  {
    path: '/bhz/swbhz',//BhzSwBasesCbClList 画面
    component: BlankLayout,
    redirect: '/bhz/swbhz/BhzSwBasesCbClList',
    children: [
      {
        path: 'BhzSwBasesCbClList',
        name: 'BhzSwBasesCbClList',
        component: () => import('@/views/bhz/swbhz/BhzSwBasesCbClList')
      }
    ]
  },
  {
    path: '/bhz/swbhz',//BhzSwBasesCBList 画面
    component: BlankLayout,
    redirect: '/bhz/swbhz/BhzSwBasesCBList',
    children: [
      {
        path: 'BhzSwBasesCBList',
        name: 'BhzSwBasesCBList',
        component: () => import('@/views/bhz/swbhz/BhzSwBasesCBList')
      }
    ]
  },

  {
    path: '/bhz/swbhz',//BhzSwBaseSta 画面
    component: BlankLayout,
    redirect: '/bhz/swbhz/BhzSwBaseSta',
    children: [
      {
        path: 'BhzSwBaseSta',
        name: 'BhzSwBaseSta',
        component: () => import('@/views/bhz/swbhz/BhzSwBaseSta')
      }
    ]
  },

  {
    path: '/bhz/czsh',//拌合站施工处置、监理审核、指挥部审批单独页面
    component: BlankLayout,
    redirect: '/bhz/czsh/ChuZhiTwoLone',
    children: [
      {
        path: 'ChuZhiTwoLone',
        name: 'ChuZhiTwoLone',
        component: () => import('@/views/bhz/czsh/ChuZhiTwoLone')
      },
      {
        path: 'ShenHeTwoLone',
        name: 'ShenHeTwoLone',
        component: () => import('@/views/bhz/czsh/ShenHeTwoLone')
      },
      {
        path: 'ShenPiLone',
        name: 'ShenPiLone',
        component: () => import('@/views/bhz/czsh/ShenPiLone')
      }
    ]
  },

  {
    path: '/td',// 塔吊
    component: BlankLayout,
    redirect: '/td/TadiaoList',
    children: [
      {
        path: 'TadiaoList',
        name: 'TadiaoList',
        component: () => import('@/views/td/TadiaoList')
      }
    ]
  },
  {
    path: '/td/tadiaoHis',//BhzSwBasesForm 画面
    component: BlankLayout,
    redirect: '/td/tadiaoHis/TadiaoHisList',
    children: [
      {
        path: 'TadiaoHisList',
        name: 'TadiaoHisList',
        component: () => import('@/views/td/tadiaoHis/TadiaoHisList')
      }
    ]
  },

  {
    path: '/syj/hntht',//混凝土回弹
    component: BlankLayout,
    redirect: '/syj/hntht/TrHnthtMList',
    children: [
      {
        path: 'TrHnthtMList',
        name: 'TrHnthtMList',
        component: () => import('@/views/syj/hntht/TrHnthtMList')
      }
    ]
  },
  // {
  //   path: '/syj/gangbao',//钢筋保护层
  //   component: BlankLayout,
  //   redirect: '/syj/gangbao/TrGangjinbhcMList',
  //   children: [
  //     {
  //       path: 'TrGangjinbhcMList',
  //       name: 'TrGangjinbhcMList',
  //       component: () => import('@/views/syj/gangbao/TrGangjinbhcMList')
  //     }
  //   ]
  // },
  {
    path: '/anquan/weiyan',//围岩量测保护层
    component: BlankLayout,
    redirect: '/anquan/weiyan/WeiyanBaseList',
    children: [
      {
        path: 'WeiyanBaseList',
        name: 'WeiyanBaseList',
        component: () => import('@/views/anquan/weiyan/WeiyanBaseList')
      }
    ]
  },
  {
    path: '/zjjc/zjjccx',//超声波
    component: BlankLayout,
    redirect: '/zjjc/zjjccx/ZhJinJiCeBaseList',
    children: [
      {
        path: 'ZhJinJiCeBaseList',
        name: 'ZhJinJiCeBaseList',
        component: () => import('@/views/zjjc/zjjccx/ZhJinJiCeBaseList')
      }
    ]
  },
  {
    path: '/zlyj',//张拉
    component: BlankLayout,
    redirect: '/zlyj/TrZhanglaXxbList',
    children: [
      {
        path: 'TrZhanglaXxbList',
        name: 'TrZhanglaXxbList',
        component: () => import('@/views/zlyj/TrZhanglaXxbList')
      },
      {
        path: 'TrZhanglaXxbList/detail',
        name: 'TrZhanglaXxbList/detail',
        component: () => import('@/views/zlyj/modules/TrZhanglaMModalDetail')
      },
      {
        path: 'TrZhanglaXxbListTwo/detail',
        name: 'TrZhanglaXxbListTwo/detail',
        component: () => import('@/views/zlyj/modules/TrZhanglaMModalTwoDetail')
      }
    ]
  },
  {
    path: '/zlyj',//张拉超标
    component: BlankLayout,
    redirect: '/zlyj/TrZhanglachaobiaoList',
    children: [
      {
        path: 'TrZhanglachaobiaoList',
        name: 'TrZhanglachaobiaoList',
        component: () => import('@/views/zlyj/TrZhanglachaobiaoList')
      }
    ]
  },
  {
    path: '/zlyj',//张拉超标处置
    component: BlankLayout,
    redirect: '/zlyj/TrZhanglachaobiaoCLList',
    children: [
      {
        path: 'TrZhanglachaobiaoCLList',
        name: 'TrZhanglachaobiaoCLList',
        component: () => import('@/views/zlyj/TrZhanglachaobiaoCLList')
      }
    ]
  },
  {
    path: '/zl/czsh',//张拉施工处置、监理审核、指挥部审批单独页面
    component: BlankLayout,
    redirect: '/zl/czsh/zlChuZhiLone',
    children: [
      {
        path: 'zlChuZhiLone',
        name: 'zlChuZhiLone',
        component: () => import('@/views/zl/czsh/ChuZhiLone')
      },
      {
        path: 'zlShenHeLone',
        name: 'zlShenHeLone',
        component: () => import('@/views/zl/czsh/ShenHeLone')
      },
      {
        path: 'zlShenPiLone',
        name: 'zlShenPiLone',
        component: () => import('@/views/zl/czsh/ShenPiLone')
      }
    ]
  },
  {
    path: '/yj/czsh',//压浆施工处置、监理审核、指挥部审批单独页面
    component: BlankLayout,
    redirect: '/yj/czsh/yjChuZhiLone',
    children: [
      {
        path: 'yjChuZhiLone',
        name: 'yjChuZhiLone',
        component: () => import('@/views/yj/czsh/ChuZhiLone')
      },
      {
        path: 'yjShenHeLone',
        name: 'yjShenHeLone',
        component: () => import('@/views/yj/czsh/ShenHeLone')
      },
      {
        path: 'yjShenPiLone',
        name: 'yjShenPiLone',
        component: () => import('@/views/yj/czsh/ShenPiLone')
      }
    ]
  },

  {
    path: '/zlyj',//压浆
    component: BlankLayout,
    redirect: '/zlyj/TrYajiangMList',
    children: [
      {
        path: 'TrYajiangMList',
        name: 'TrYajiangMList',
        component: () => import('@/views/zlyj/TrYajiangMList')
      },
      // {
      //   path: 'TrYajiangMList/detail',
      //   name: 'TrYajiangMList/detail',
      //   component: () => import('@/views/zlyj/TrYajiangMSModalDetail')
      // }
    ]
  },
  {
    path: '/yj',//压浆
    component: BlankLayout,
    redirect: '/yj/TrYajiangMList',
    children: [
      {
        path: 'TrYajiangMList',
        name: 'TrYajiangMList',
        component: () => import('@/views/zlyj/TrYajiangMList')
      },
      {
        path: 'TrYajiangMList/detail',
        name: 'TrYajiangMList/detail',
        component: () => import('@/views/yj/modules/TrYajiangMSModalDetail')
      }
    ]
  },
  {
    path: '/zlyj',//压浆超标
    component: BlankLayout,
    redirect: '/zlyj/TrYajiangchaobiaoList',
    children: [
      {
        path: 'TrYajiangchaobiaoList',
        name: 'TrYajiangchaobiaoList',
        component: () => import('@/views/zlyj/TrYajiangchaobiaoList')
      }
    ]
  },
  {
    path: '/zlyj',//压浆超标处理
    component: BlankLayout,
    redirect: '/zlyj/TrYajiangchaobiaoCLList',
    children: [
      {
        path: 'TrYajiangchaobiaoCLList',
        name: 'TrYajiangchaobiaoCLList',
        component: () => import('@/views/zlyj/TrYajiangchaobiaoCLList')
      }
    ]
  },

  {
    path: '/anquan/jiaqiaoji/jiaqiaojireal',//架桥机
    component: BlankLayout,
    redirect: '/anquan/jiaqiaoji/jiaqiaojireal/DeviceCraneRealdataList',
    children: [
      {
        path: 'DeviceCraneRealdataList',
        name: 'DeviceCraneRealdataList',
        component: () => import('@/views/anquan/jiaqiaoji/jiaqiaojireal/DeviceCraneRealdataList')
      }
    ]
  },
  {
    path: '/anquan/gualan/gualanreal',//挂篮
    component: BlankLayout,
    redirect: '/anquan/gualan/gualanreal/GualanBaseRealList',
    children: [
      {
        path: 'GualanBaseRealList',
        name: 'GualanBaseRealList',
        component: () => import('@/views/anquan/gualan/gualanreal/GualanBaseRealList')
      }
    ]
  },
  {
    path: '/anquan/gualan',//挂篮
    component: BlankLayout,
    redirect: '/anquan/gualan/GualanBaseList',
    children: [
      {
        path: 'GualanBaseList',
        name: 'GualanBaseList',
        component: () => import('@/views/anquan/gualan/GualanBaseList')
      }
    ]
  },
  {
    path: '/lmd/lmdrealdata',//龙门吊
    component: BlankLayout,
    redirect: '/lmd/lmdrealdata/DeviceCraneRealdataList',
    children: [
      {
        path: 'DeviceCraneRealdataList',
        name: 'DeviceCraneRealdataList',
        component: () => import('@/views/lmd/lmdrealdata/DeviceCraneRealdataList')
      },
      {
        path: 'DeviceCraneRealdataModalDetail',
        name: 'DeviceCraneRealdataModalDetail',
        component: () => import('@/views/lmd/lmdrealdata/modules/DeviceCraneRealdataModalDetail')
      }
    ]
  },
  {
    path: '/expType/expType',//实验类型
    component: BlankLayout,
    redirect: '/expType/expType/expTypeTree',
    children: [
      {
        path: 'expTypeTree',
        name: 'expTypeTree',
        component: () => import('@/views/expType/expType/expTypeTree')
      }
    ]
  },
  {
    path: '/anquan/aiwarn/aiwarnmsg',//AI识别
    component: BlankLayout,
    redirect: '/anquan/aiwarn/aiwarnmsg/AiWarnMsgList',
    children: [
      {
        path: 'AiWarnMsgList',
        name: 'AiWarnMsgList',
        component: () => import('@/views/anquan/aiwarn/aiwarnmsg/AiWarnMsgList')
      }
    ]
  },
  {
    path: '/syj/kongjing',//成孔质量检测
    component: BlankLayout,
    redirect: '/syj/kongjing/KongjingBasicinfoList',
    children: [
      {
        path: 'KongjingBasicinfoList',
        name: 'KongjingBasicinfoList',
        component: () => import('@/views/syj/kongjing/KongjingBasicinfoList')
      }
    ]
  },
  {
    path: '/zjjc/zjjccx',
    component: BlankLayout,
    redirect: '/zjjc/zjjccx/ZhJinJiCeBaseList',
    children: [
      {
        path: 'KongjingBasicinfoList',
        name: 'KongjingBasicinfoList',
        component: () => import('@/views/zjjc/zjjccx/ZhJinJiCeBaseList')
      }
    ]
  },

  {
    path: '/sygl/hnthtconsign',//试验监测任务单下发
    component: BlankLayout,
    redirect: '/sygl/hnthtconsign/HnthtConsignList',
    children: [
      {
        path: 'HnthtConsignList',
        name: 'HnthtConsignList',
        component: () => import('@/views/sygl/hnthtconsign/HnthtConsignList')
      }
    ]
  },
  {
    path: '/bhz/hntbhz',//拌合站超标处理
    component: BlankLayout,
    redirect: '/bhz/hntbhz/BhzHntCbCxtBaseList',
    children: [
      {
        path: 'BhzHntCbCxtBaseList',
        name: 'BhzHntCbCxtBaseList',
        component: () => import('@/views/bhz/hntbhz/BhzHntCbCxtBaseList')
      },
      {
        path: 'BhzCementBaseCBFormTwo',
        name: 'BhzCementBaseCBFormTwo',
        component: () => import('@/views/bhz/hntbhz/modules/BhzCementBaseCBFormTwo')
      }
    ]
  },
  {
    path: '/officialfile/dashijifile',//大事记登录
    component: BlankLayout,
    redirect: '/officialfile/dashijifile/OfficialFileList',
    children: [
      {
        path: 'OfficialFileList',
        name: 'OfficialFileList',
        component: () => import('@/views/officialfile/dashijifile/OfficialFileList')
      }
    ]
  },
  {
    path: '/bhz/entitycheak',//实体进度清单
    component: BlankLayout,
    redirect: '/bhz/entitycheak/EntityProgresscheckList',
    children: [
      {
        path: 'EntityProgresscheckList',
        name: 'EntityProgresscheckList',
        component: () => import('@/views/bhz/entitycheak/EntityProgresscheckList')
      }
    ]
  },
  {
    path: '/anquan/mjkq',//实体进度清单
    component: BlankLayout,
    redirect: '/anquan/mjkq/EntranceGuardRecordList',
    children: [
      {
        path: 'EntranceGuardRecordList',
        name: 'EntranceGuardRecordList',
        component: () => import('@/views/anquan/mjkq/EntranceGuardRecordList')
      }
    ]
  },
  {
    path: '/anquan/sdhj',//实体进度清单
    component: BlankLayout,
    redirect: '/anquan/sdhj/DeviceTunnelEnvironmentdataList',
    children: [
      {
        path: 'DeviceTunnelEnvironmentdataList',
        name: 'DeviceTunnelEnvironmentdataList',
        component: () => import('@/views/anquan/sdhj/DeviceTunnelEnvironmentdataList')
      }
    ]
  },//
  {
    path: '/zlgl/yclcl',//原材料出料
    component: BlankLayout,
    redirect: '/zlgl/yclcl/WzyclchuchanggbList',
    children: [
      {
        path: 'WzyclchuchanggbList',
        name: 'WzyclchuchanggbList',
        component: () => import('@/views/zlgl/yclcl/WzyclchuchanggbList')
      }
    ]
  },
  {
    path: '/checklist/invlist',//投资清单
    component: BlankLayout,
    redirect: '/checklist/invlist/InvestmentChecklistList',
    children: [
      {
        path: 'InvestmentChecklistList',
        name: 'InvestmentChecklistList',
        component: () => import('@/views/checklist/invlist/InvestmentChecklistList')
      }
    ]
  },
  {
    path: '/checklist/prolist',//产值清单
    component: BlankLayout,
    redirect: '/checklist/prolist/ProductionValuelistList',
    children: [
      {
        path: 'ProductionValuelistList',
        name: 'ProductionValuelistList',
        component: () => import('@/views/checklist/prolist/ProductionValuelistList')
      }
    ]
  },
  {
    path: '/checklist/prolist',//产值清单
    component: BlankLayout,
    redirect: '/checklist/prolist/ProductionValuelistList',
    children: [
      {
        path: 'ProductionValuelistList',
        name: 'ProductionValuelistList',
        component: () => import('@/views/checklist/prolist/ProductionValuelistList')
      }
    ]
  },
  {
    path: '/checklist/record/onboarding',//入职培训记录
    component: BlankLayout,
    redirect: '/checklist/record/onboarding/OnboardingRecordList',
    children: [
      {
        path: 'OnboardingRecordList',
        name: 'OnboardingRecordList',
        component: () => import('@/views/checklist/record/onboarding/OnboardingRecordList')
      }
    ]
  },
  {
    path: '/checklist/record/comparison',//评比记录
    component: BlankLayout,
    redirect: '/checklist/record/comparison/ComparisonRecordList',
    children: [
      {
        path: 'ComparisonRecordList',
        name: 'ComparisonRecordList',
        component: () => import('@/views/checklist/record/comparison/ComparisonRecordList')
      }
    ]
  },
  {
    path: '/checklist/innovation',//成果清单
    component: BlankLayout,
    redirect: '/checklist/innovation/AchievementsListList',
    children: [
      {
        path: 'AchievementsListList',
        name: 'AchievementsListList',
        component: () => import('@/views/checklist/innovation/AchievementsListList')
      }
    ]
  },
  {
    path: '/checklist/salary',//工资清单
    component: BlankLayout,
    redirect: '/checklist/salary/SalaryListList',
    children: [
      {
        path: 'SalaryListList',
        name: 'SalaryListList',
        component: () => import('@/views/checklist/salary/SalaryListList')
      }
    ]
  },
  {
    path: '/checklist/benefit/biangengpifu',//变更复批
    component: BlankLayout,
    redirect: '/checklist/benefit/biangengpifu/ChangeSpprovalList',
    children: [
      {
        path: 'ChangeSpprovalList',
        name: 'ChangeSpprovalList',
        component: () => import('@/views/checklist/benefit/biangengpifu/ChangeSpprovalList')
      }
    ]
  },
  {
    path: '/checklist/benefit/biangengzaojia',//变更造价控制
    component: BlankLayout,
    redirect: '/checklist/benefit/biangengzaojia/ChangeCostControlList',
    children: [
      {
        path: 'ChangeCostControlList',
        name: 'ChangeCostControlList',
        component: () => import('@/views/checklist/benefit/biangengzaojia/ChangeCostControlList')
      }
    ]
  },
  {
    path: '/checklist/benefit/jiliangzhifu',//计量支付
    component: BlankLayout,
    redirect: '/checklist/benefit/jiliangzhifu/MeteredPaymentList',
    children: [
      {
        path: 'MeteredPaymentList',
        name: 'MeteredPaymentList',
        component: () => import('@/views/checklist/benefit/jiliangzhifu/MeteredPaymentList')
      }
    ]
  },
  {
    path: '/checklist/benefit/jiliangzhixing',//计量执行
    component: BlankLayout,
    redirect: '/checklist/benefit/jiliangzhixing/MeasurementExecutionRateList',
    children: [
      {
        path: 'MeasurementExecutionRateList',
        name: 'MeasurementExecutionRateList',
        component: () => import('@/views/checklist/benefit/jiliangzhixing/MeasurementExecutionRateList')
      }
    ]
  },
  {
    path: '/checklist/quality',//重点材料检测清单
    component: BlankLayout,
    redirect: '/checklist/quality/MaterialTestingList',
    children: [
      {
        path: 'MaterialTestingList',
        name: 'MaterialTestingList',
        component: () => import('@/views/checklist/quality/MaterialTestingList')
      }
    ]
  },
  {
    path: '/snjbz/devicepipepilehistorydataone',//管桩
    component: BlankLayout,
    redirect: '/snjbz/devicepipepilehistorydataone/DevicePipepileHistorydataOneList',
    children: [
      {
        path: 'DevicePipepileHistorydataOneList',
        name: 'DevicePipepileHistorydataOneList',
        component: () => import('@/views/snjbz/devicepipepilehistorydataone/DevicePipepileHistorydataOneList')
      }
    ]
  },
  {
    path: '/anquan/mjkq/carmjkq',//车辆门禁
    component: BlankLayout,
    redirect: '/anquan/mjkq/carmjkq/SafetyTunnelCarHistoryList',
    children: [
      {
        path: 'SafetyTunnelCarHistoryList',
        name: 'SafetyTunnelCarHistoryList',
        component: () => import('@/views/anquan/mjkq/carmjkq/SafetyTunnelCarHistoryList')
      }
    ]
  },
  {
    path: '/car/vehicle',//电子锁车牌查询
    component: BlankLayout,
    redirect: '/car/vehicle/VehicleManagementList',
    children: [
      {
        path: 'VehicleManagementList',
        name: 'VehicleManagementList',
        component: () => import('@/views/car/vehicle/VehicleManagementList')
      }
    ]
  },
  {
    path: '/car/wbdestination',//电子锁目的地查询
    component: BlankLayout,
    redirect: '/car/wbdestination/WbDestinationList',
    children: [
      {
        path: 'WbDestinationList',
        name: 'WbDestinationList',
        component: () => import('@/views/car/wbdestination/WbDestinationList')
      }
    ]
  },
  {
    path: '/car/wbshebeidetail',//电子锁发车单查询
    component: BlankLayout,
    redirect: '/car/wbshebeidetail/WbshebeiDetailList',
    children: [
      {
        path: 'WbshebeiDetailList',
        name: 'WbshebeiDetailList',
        component: () => import('@/views/car/wbshebeidetail/WbshebeiDetailList')
      }
    ]
  },
  /////******************************/////
  {
    path: '/car/wbshebeidetail',//车辆到达次数查询
    component: BlankLayout,
    redirect: '/car/wbshebeidetail/WbshebeiDetailToList',
    children: [
      {
        path: 'WbshebeiDetailToList',
        name: 'WbshebeiDetailToList',
        component: () => import('@views/car/wbshebeidetail/WbshebeiDetailToList')
      }
    ]
  },
  {
    path: '/lumianxx/modules',//车辆到达次数查询
    component: BlankLayout,
    redirect: '/lumianxx/modules/reportDetail',
    children: [
      {
        path: 'reportDetail',
        name: 'reportDetail',
        component: () => import('@views/lumianxx/modules/reportDetail')
      }
    ]
  },
  /////******************************/////
  {
    path: '/zlgl/wztaizhang/wztaizhangnew',//物资检验批（新规则）
    component: BlankLayout,
    redirect: '/zlgl/wztaizhang/wztaizhangnew/WztaizhangLists',
    children: [
      {
        path: 'WztaizhangLists',
        name: 'WztaizhangLists',
        component: () => import('@views/zlgl/wztaizhang/wztaizhangnew/WztaizhangLists')
      }
    ]
  },
]
