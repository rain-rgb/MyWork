package org.jeecg.config.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisClusterManager;
import org.crazycake.shiro.RedisManager;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.shiro.filters.CustomShiroFilterFactoryBean;
import org.jeecg.config.shiro.filters.JwtFilter;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.util.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.*;

/**
 * @author: Scott
 * @date: 2018/2/7
 * @description: shiro 配置类
 */

@Slf4j
@Configuration
public class ShiroConfig {

    @Value("${jeecg.shiro.excludeUrls}")
    private String excludeUrls;
    @Resource
    LettuceConnectionFactory lettuceConnectionFactory;
    @Autowired
    private Environment env;


    /**
     * Filter Chain定义说明
     *
     * 1、一个URL可以配置多个Filter，使用逗号分隔
     * 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        CustomShiroFilterFactoryBean shiroFilterFactoryBean = new CustomShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        if(oConvertUtils.isNotEmpty(excludeUrls)){
            String[] permissionUrl = excludeUrls.split(",");
            for(String url : permissionUrl){
                filterChainDefinitionMap.put(url,"anon");
            }
        }
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/system/shigongphb/getphb","anon");//查询配料单和施工配合比
        filterChainDefinitionMap.put("/qrcode/qrcode/getQrcod","anon");//获取二维码
        filterChainDefinitionMap.put("/wzyclHandler/wzyclHandler/addRectifyInfo","anon");//获取净重
        filterChainDefinitionMap.put("/wztaizhang/wztaizhang/listJz","anon");//获取净重
        filterChainDefinitionMap.put("/wzliaocang/wzliaocangChange/wzliaocangChange","anon");//料位数据更新
        filterChainDefinitionMap.put("/jikeng/**/add","anon");//基坑数据对接
        filterChainDefinitionMap.put("/clgl/clxxRealdata/updatebycardnumber","anon");//睿矩基站对接，修改状态
        filterChainDefinitionMap.put("/devicemixgrouted/deviceMixpileGroutedReal/updateGroutedReal","anon");//灌浆桩实时数据
        filterChainDefinitionMap.put("/grouted/deviceMixpileGroutedOne/addGroutedOpen","anon");//灌浆桩数据
        filterChainDefinitionMap.put("/grouted/deviceMixpileGroutedPart/addGroutedPartOpen","anon");//灌浆桩数据
        filterChainDefinitionMap.put("/grouted/deviceMixpileGroutedOne/addGrouteds","anon");//灌浆桩数据
        filterChainDefinitionMap.put("/devicehammeringhistorydata/deviceHammeringHistorydata/add","anon");//锤击桩实时数据
        filterChainDefinitionMap.put("/devicehammeringhistorydataone/deviceHammeringHistorydataOne/addother","anon");//锤击桩成桩数据
        filterChainDefinitionMap.put("/devicehammeringhistorydatapart/deviceHammeringHistorydataPart/add","anon");//锤击桩段数据
        filterChainDefinitionMap.put("/wzliaocang/wzliaocang/editWeight","anon");//AI监测料位数据
        filterChainDefinitionMap.put("/sys/common/upload","anon");//文件统一上传
        filterChainDefinitionMap.put("/devicehighformwork/deviceHighFormworkReal/addOpen","anon");//高支模接口数据
        filterChainDefinitionMap.put("/system/bhzrenwudan/list0","anon");//试验平台任务单数据对接
        filterChainDefinitionMap.put("/aiwarnmsg/aiWarnMsg/BoShiAiWarn","anon");//ai接收预警接口
        filterChainDefinitionMap.put("/wzliaocang/wzliaocang/searchLCStatus","anon");//高信新增工艺查询
        filterChainDefinitionMap.put("/devicepipepilehistorydataone/devicePipepileHistorydataOne/findgxYear*","anon");//高信新增工艺查询
        filterChainDefinitionMap.put("/gongyistatistic/gongyiStatistic/listGx*","anon");//高信新增工艺查询
        filterChainDefinitionMap.put("/sys/dict/getDictItemsGx","anon");//高信重点工艺查询
        filterChainDefinitionMap.put("/sys/sysDepart/querySysDepartInfoList","anon");//code下的项目，标段
        filterChainDefinitionMap.put("/sys/sysDepart/queryList13","anon");//code下的项目，标段
        filterChainDefinitionMap.put("/devicepipepilehistorydataone/devicePipepileHistorydataOne/findgxBySysOrgCode","anon");//高信看板一级页面
        filterChainDefinitionMap.put("/devicepipepilehistorydataone/devicePipepileHistorydataOne/findgxYear","anon");//高信看板二级页面
        filterChainDefinitionMap.put("/devicepipepilehistorydataone/devicePipepileHistorydataOne/findgxYearXq","anon");//高信看板二级页面
        filterChainDefinitionMap.put("/devicepipepilehistorydataone/devicePipepileHistorydataOne/findgxYearXqtime","anon");//高信看板二级页面
        filterChainDefinitionMap.put("/wztaizhang/wztaizhang/queryList*","anon");//原材看板接口
        filterChainDefinitionMap.put("/hntbhz/bhzCementBase/titleDataStaZt","anon");//瑞苍项目拌合站首页超标接口
        filterChainDefinitionMap.put("/hntbhz/bhzCementBase/titleDataStaFb","anon");//瑞苍项目拌合站首页超标接口
        filterChainDefinitionMap.put("/ycltd/yclTestDetail/list1","anon");//瑞苍项目原材检验详情接口
        filterChainDefinitionMap.put("/hntbhz/bhzCementBase/listKf","anon");//瑞苍项目拌合站查询开放接口
        filterChainDefinitionMap.put("/wzliaocang/wzliaocang/getliaocangList","anon");//根据材料名获取料仓名
        filterChainDefinitionMap.put("/zhanglam/trZhanglaM/listFb","anon");//zhangla
        filterChainDefinitionMap.put("/yajiangm/trYajiangM/listFb","anon");//yajiang
        filterChainDefinitionMap.put("/zhanglam/trZhanglaM/listZt","anon");//总zhangla
        filterChainDefinitionMap.put("/yajiangm/trYajiangM/listZt","anon");//总yajiang
        filterChainDefinitionMap.put("/sys/systems/ssoredirect/**","anon");
        filterChainDefinitionMap.put("/sys/systems/api//wbshebei*","anon");// 地榜端对接运输车
        filterChainDefinitionMap.put("/rcjob/rsautil/*","anon");
        filterChainDefinitionMap.put("/swbhz/bhzSwBases/phbList","anon");//义东拌合站配合比
        filterChainDefinitionMap.put("/hntbhz/bhzCementBase/queryList","anon");//拌合站
        filterChainDefinitionMap.put("/hntbhz/bhzCementBase/queryxqList","anon");//杭甬拌合站
        filterChainDefinitionMap.put("/zhanglaxxb/trZhanglaXxb/queryList","anon");//张拉
        filterChainDefinitionMap.put("/zhanglaxxb/trZhanglaXxb/queryListrc","anon");//张拉
        filterChainDefinitionMap.put("/yajiangm/trYajiangM/queryList","anon");//压浆
        filterChainDefinitionMap.put("/trgangjinbhcm/trGangjinbhcM/getgbhgl","anon");//根据projectid 缓存 redis 设备编号
        filterChainDefinitionMap.put("/hntconsign/hntConsign/getlist3","anon");//根据projectid 缓存 redis 设备编号
        filterChainDefinitionMap.put("/hntconsign/hntConsign/dataCollect","anon");//甬台温北段试验统计
        filterChainDefinitionMap.put("/wztaizhang/wztaizhang/getlist5","anon");//根据projectid 缓存 redis 设备编号
        filterChainDefinitionMap.put("/syj/tSyjzb/getstalists","anon");//根据projectid 缓存 redis 设备编号
        filterChainDefinitionMap.put("/sys/user/getShebiCache","anon");//根据projectid 缓存 redis 设备编号
        filterChainDefinitionMap.put("/zhiliangrenwudan/zhiliangrenwudan/hlist","anon");//任务单（制梁）表信息-分页列表查询(微信扫码)
        filterChainDefinitionMap.put("/zhiliangrenwudan/zhiliangrenwudan/zyhhlist","anon");//任务单（制梁）表信息-分页列表查询(微信扫码)2.0
        filterChainDefinitionMap.put("/devicepipepilehistorydataone/devicePipepileHistorydataOne/addOther", "anon");//管桩成桩（对外开放）
        filterChainDefinitionMap.put("/devicepipepilehistorydatapart/devicePipepileHistorydataPart/addOther", "anon");//管桩段数据（对外开放）
        filterChainDefinitionMap.put("/devicepipepilerealdata/devicePipepileRealdata/addOther", "anon");//管桩实时（对外开放）
        filterChainDefinitionMap.put("/devicepipepilereport/devicePipepileReport/addOther", "anon");//管桩报表（对外开放）
        filterChainDefinitionMap.put("/device_mixer_trucks/deviceMixerTrucks/addOpen", "anon");//搅拌车
        filterChainDefinitionMap.put("/device_mixer_trucks/deviceMixerTrucks/addOpen1", "anon");//搅拌车
        filterChainDefinitionMap.put("/weiyanalarm/weiyanAlarm/list1", "anon");//监控量测报警查询数据接口（对外开放）
        filterChainDefinitionMap.put("/weiyanalarm/weiyanAlarm/addOpen", "anon");//监控量测报警数据（对外开放）
        filterChainDefinitionMap.put("/gualan/gualanBase/listOpen", "anon");//挂篮接口（对外开放）
        filterChainDefinitionMap.put("/syjoverhandler/syjOverHandler/addOpen", "anon");//试验机处置（对外开放）
        filterChainDefinitionMap.put("/sydpssysample/syDpsSySample/addOpen", "anon");//委托单（对外开放）
        filterChainDefinitionMap.put("/skip_car/skipCar/getTem", "anon");//运输车温度
        filterChainDefinitionMap.put("/hc_pavement_stationdata/hcPavementStationdata/getData", "anon");//逐桩温度，碾压遍数
        filterChainDefinitionMap.put("/hc_pavement_stationdata/hcPavementStationdata/getData1", "anon");//逐桩温度，碾压遍数
        filterChainDefinitionMap.put("/hc_pavement_stationdata/hcPavementStationdata/getDay", "anon");//逐桩温度，碾压遍数
        filterChainDefinitionMap.put("/lqbhzrecipe/bhzLqRecipe/getPhb", "anon");//获取配比
        filterChainDefinitionMap.put("/lqbhz/bhzLqBases/getDosage", "anon");//获取每日材料及配比
        filterChainDefinitionMap.put("/lqbhz/bhzLqBases/getDay", "anon");//获取每日材料及配比
        filterChainDefinitionMap.put("/lqbhz/bhzLqBases/getYSB", "anon");//获取油石比
        filterChainDefinitionMap.put("/devicemixpilerwd/deviceMixpileRwd/editOpen", "anon");//修改软基工单状态接口（对外开放）
        filterChainDefinitionMap.put("/yclsl/wzycljinchanggb/addOpens", "anon");//原材料进场过磅对外开放添加数据接口(瑞苍内网)
        filterChainDefinitionMap.put("/yajiangs/trYajiangSS/addList", "anon");//压浆数据过程值 上传 对外接口实时上传（瑞仓内网）
        filterChainDefinitionMap.put("/zhanglass/trZhanglaSS/addList", "anon");//张拉数据过程值 上传 对外接口实时上传（瑞仓内网）
        filterChainDefinitionMap.put("/zhydreal/deviceElectricRealdata/addOpen","anon");//智慧用电实时数据添加对开放接口
        filterChainDefinitionMap.put("/chaoshengbo/chaoshengboSybltsj/list2","anon");//桩基图片接口对前端不加秘钥开放
        filterChainDefinitionMap.put("/lqbhz/bhzLqBases/liststa","anon");//拌合站统计对外开放 瑞苍专用接口
        filterChainDefinitionMap.put("/system/bhzrenwudan/addOpen","anon");//浇筑令对外开放接口
        filterChainDefinitionMap.put("/kongjingbasicinfo/kongjingBasicinfo/addOpen","anon");//孔径数据添加对外开放接口
        filterChainDefinitionMap.put("/yclsl/wzycljinchanggb/cailiaolist","anon");//原材料根据省/市/项目统计总净重对外开放接口
        filterChainDefinitionMap.put("/devicemixpilerwd/deviceMixpileRwd/zdlist","anon");//软基工单信息查询
        filterChainDefinitionMap.put("/sys/common/uploadLock","anon");//电子锁上传图片
        filterChainDefinitionMap.put("/sys/common/uploadLockAI","anon");//AI识别上传图片
        filterChainDefinitionMap.put("/sys/common/uploadLockAI2","anon");//AI2识别上传图片
        filterChainDefinitionMap.put("/sys/common/uploadLockMG","anon");//锚杆上传文件
        filterChainDefinitionMap.put("/sys/common/uploadWeiyan","anon");//监控量测上传文件
        filterChainDefinitionMap.put("/wzcailiaonamedict/wzcailiaonamedict/addOpen", "anon");//物资材料类型对外开放添加数据接口
        filterChainDefinitionMap.put("/wzgongyingshang/wzgongyingshang/addOpen", "anon");//物资供应商对外开放添加数据接口
        filterChainDefinitionMap.put("/wzliaocang/wzliaocang/addOpen", "anon");//料仓对外开放添加数据接口
        filterChainDefinitionMap.put("/yclcl/wzyclchuchanggb/addOpen", "anon");//原材料出场过磅对外开放添加数据接口
        filterChainDefinitionMap.put("/yclsl/wzycljinchanggb/addOpen", "anon");//原材料进场过磅对外开放添加数据接口
//        filterChainDefinitionMap.put("/yajiangs/trYajiangS/list4", "anon");//压浆根据省/市/项目统计预警数/不合格率/闭合率对外接口
//        filterChainDefinitionMap.put("/chaoshengbo/chaoshengboSyqxsj/addOpen", "anon");//桩基chaoshengbo_syqxs添加数据对外开放
//        filterChainDefinitionMap.put("/chaoshengbo/chaoshengboSyljzs/addOpen", "anon");//桩基chaoshengbo_syljzs添加数据对外开放
//        filterChainDefinitionMap.put("chaoshengbo/chaoshengboSyjsb/addOpen", "anon");//桩基chaoshengbo_syjsb添加数据对外开放
        filterChainDefinitionMap.put("/chaoshengbo/chaoshengboSyjbsj/addOpen", "anon");//桩基基本数据对外开放添加接口(chaoshengbo_syjbsj)
//        filterChainDefinitionMap.put("/chaoshengbo/chaoshengboSydsj/addOpen", "anon");//桩基chaoshengbo_sydsj添加数据对外开放
//        filterChainDefinitionMap.put("/chaoshengbo/chaoshengboSybsj/addOpen", "anon");//桩基chaoshengbo_sybsj对外开放添加数据接口
//        filterChainDefinitionMap.put("/chaoshengbo/chaoshengboSybltsj/addOpen", "anon");//桩基波列图对外开放添加数据接口(chaoshengbo_sybltsj)
        filterChainDefinitionMap.put("/zhanglam/trZhanglaM/list7", "anon");//张拉压浆根据省/市/项目统计预警数/不合格率/闭合率对外接口（张拉压浆混凝土拌合站合在一起计算）
//        filterChainDefinitionMap.put("/hntbhz/bhzCementBase/departtbhzlist", "anon");//混凝土拌合站根据省/市/项目统计盘数/合格数/不合格数对外接口
        filterChainDefinitionMap.put("/yajiangrenwudan/trYajiangRenwudan/addOpen", "anon");//压浆任务单对外接口对接
        filterChainDefinitionMap.put("/trzhanglarenwudan/trZhanglaRenwudan/addOpen", "anon");//张拉任务单对外接口对接
        filterChainDefinitionMap.put("/hnthtconsign/hnthtConsign/addOpen", "anon");//检测任务单下发对外接口对接
        filterChainDefinitionMap.put("/devicemixpilerwd/deviceMixpileRwd/addOpen","anon");//软基任务单
        filterChainDefinitionMap.put("/devicemixpilerwd/deviceMixpileRwd/editRwd","anon");//软基任务单状态修改
        filterChainDefinitionMap.put("/bys/bysReal/addOpen", "anon");//标养室对外接口对接
        filterChainDefinitionMap.put("/zhenru/wZhenruduM/addOpen", "anon");//针入度对外接口对接
        filterChainDefinitionMap.put("/mxe/wendingD/addOpen", "anon");//稳定度对外接口对接
        filterChainDefinitionMap.put("/mxe/wendingD/addOpen1", "anon");//稳定度对外接口对接
        filterChainDefinitionMap.put("/yd/wYanduM/addOpen", "anon");//延度对外接口对接
        filterChainDefinitionMap.put("/ruanhuadu/wRuanhuadianM/addOpen", "anon");//软化点对外接口对接
        filterChainDefinitionMap.put("/whwgpy/wHwgpy/addOpen", "anon");//红外光谱对外接口对接
        filterChainDefinitionMap.put("/skip_car/skipCar/addOpen", "anon");//运输温度对外接口对接
        filterChainDefinitionMap.put("/aiwarnmsg/aiWarnMsg/addOpen", "anon");//AI数据识别预警
        filterChainDefinitionMap.put("/entranceGuardRecord/entranceGuardRecord/addOpen", "anon");//门禁考勤
        filterChainDefinitionMap.put("/devicepipepilehistorydataone/devicePipepileHistorydataOne/addOpen", "anon");//管桩
        filterChainDefinitionMap.put("/safetyTunnelCarReal/safetyTunnelCarReal/addOpen", "anon");//隧道车辆门禁
        filterChainDefinitionMap.put("/safetyTunnelCarHistory/safetyTunnelCarHistory/addOpen", "anon");//隧道车辆门禁历史
        filterChainDefinitionMap.put("/devicetunnelenvironmentdata/deviceTunnelEnvironmentdata/addOpen", "anon");//隧道气体监测
        filterChainDefinitionMap.put("/devicetunnelenvironmentdata/deviceTunnelEnvironmentdata/listOpen", "anon");//隧道环境（有害气体）（对外开放）
        filterChainDefinitionMap.put("/devicetunnelpositiondatareal/deviceTunnelPositiondataReal/addOpen", "anon");//人员定位（对外开放）
        filterChainDefinitionMap.put("/devicetunnelpositiondatareal/deviceTunnelPositiondataReal/addOpenList", "anon");//批量添加人员定位（对外开放）
        filterChainDefinitionMap.put("/entranceguardrecordreal/entranceGuardRecordReal/addOpen", "anon");//门禁考勤(对外)
        filterChainDefinitionMap.put("/weiyan/weiyanBase/wallRockBase", "anon");//围岩对外接口对接
        filterChainDefinitionMap.put("/trhnthtm/trHnthtM/add", "anon");//混凝土回弹试验对外接口对接
        filterChainDefinitionMap.put("/trhnthtm/trHnthtM/update", "anon");//混凝土回弹试验对外接口修改
        filterChainDefinitionMap.put("/hntbhz/bhzCementBase/addOpen", "anon");//混凝土拌合站对外接口
        filterChainDefinitionMap.put("/bhzStatistics/bhzCementStatistics/addOpen","anon");//混凝土拌合站统计信息表以及统计材料表对外开放接口
        filterChainDefinitionMap.put("/syj/tSyjzb/addWNJOpen", "anon");//万能机对外接口
        filterChainDefinitionMap.put("/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/addOther", "anon");//水泥搅拌桩成桩记录数据上传接口
        filterChainDefinitionMap.put("/devicemixpilehistorydata/deviceMixpileHistorydata/addOther", "anon");//软基历史数据上传接口
        filterChainDefinitionMap.put("/devicemixpilerealdata/deviceMixpileRealdata/addOther", "anon");//软基实时数据上传接口
        filterChainDefinitionMap.put("/devicemixpilehistorydatapart/deviceMixpileHistorydataPart/addOther", "anon");//软基段数据数据上传接口
        filterChainDefinitionMap.put("/devicemixpileupdata/deviceMixpileUpdata/addOther", "anon");//软基水泥配料上传接口
        filterChainDefinitionMap.put("/trgangjinbhcm/trGangjinbhcM/add", "anon");//钢筋保护层试验对外接口对接
        filterChainDefinitionMap.put("/trgangjinbhcm/trGangjinbhcM/update", "anon");//钢筋保护层试验对外修改
        filterChainDefinitionMap.put("/trzhanglarenwudan/trZhanglaRenwudan/rwdlist", "anon");//张拉任务单 对外接口（合作厂家）
        filterChainDefinitionMap.put("/trzhanglarenwudan/trZhanglaRenwudan/edit", "anon");//张拉任务单完成标记 对外接口（合作厂家）
        filterChainDefinitionMap.put("/zhanglaxxb/trZhanglaXxb/addZhangla", "anon");//张拉数据上传 对外接口（合作厂家）
        filterChainDefinitionMap.put("/zhanglas/trZhanglaS/addList", "anon");//张拉数据表三 上传 对外接口实时上传（合作厂家）
        filterChainDefinitionMap.put("/zhanglass/trZhanglaSS/add", "anon");//张拉数据过程值 上传 对外接口实时上传（合作厂家）
        filterChainDefinitionMap.put("/yajiangrenwudan/trYajiangRenwudan/rwdlist", "anon");//压浆任务单 对外接口（合作厂家）
        filterChainDefinitionMap.put("/yajiangrenwudan/trYajiangRenwudan/edit", "anon");//压浆任务单完成标记 对外接口（合作厂家）
        filterChainDefinitionMap.put("/yajiangm/trYajiangM/addYaJiang", "anon");//压浆数据上传 对外接口（合作厂家）
        filterChainDefinitionMap.put("/yajiangs/trYajiangSS/add", "anon");//压浆数据过程值上传 对外接口实时上传（合作厂家）
        filterChainDefinitionMap.put("/sys/cas/client/validateLogin", "anon"); //cas验证登录
        filterChainDefinitionMap.put("/tadiao/tadiao/add", "anon"); //塔吊
        filterChainDefinitionMap.put("/devicetiebeamrealdata/deviceTiebeamRealdata/add1", "anon"); //提梁机/架桥机设备接口
        filterChainDefinitionMap.put("/sys/randomImage/**", "anon"); //登录验证码接口排除
        filterChainDefinitionMap.put("/sys/checkCaptcha", "anon"); //登录验证码接口排除
        filterChainDefinitionMap.put("/system/shigongphb/list2", "anon");//根据配合比号
        filterChainDefinitionMap.put("/system/shigongphb/list2s", "anon");//根据配合比号
        filterChainDefinitionMap.put("/hnthtconsign/hnthtConsign/list1", "anon");//检测试验任务单下发数据接口
        filterChainDefinitionMap.put("/hnthtconsign/hnthtConsign/edit", "anon");//厂家修改检测试验任务单完成状态
        filterChainDefinitionMap.put("/syj/tSyjzb/yljadd", "anon"); //压力机对外开放添加数据接口
        filterChainDefinitionMap.put("/sys/loginsso", "anon"); //演示用登录接口不传密码的登录
        filterChainDefinitionMap.put("/sys/loginsso12", "anon"); //演示用登录接口不传密码的登录
        filterChainDefinitionMap.put("/sys/loginsso13", "anon"); //演示用登录接口不传密码的登录
        filterChainDefinitionMap.put("/sys/jtbzbtz", "anon"); //演示用登录接口不传密码的登录
        filterChainDefinitionMap.put("/sys/user/addUser", "anon");//传输用户信息(因其他项目调用本平台的页面连接需要传输用户)此接口仅提供外面使用 //暂时废除替换成下面的
        filterChainDefinitionMap.put("/sys/user/addUserDocking", "anon");//传输用户信息(因其他项目调用本平台的页面连接需要传输用户)此接口仅提供外面使用
        filterChainDefinitionMap.put("/sys/login", "anon"); //登录接口排除
        filterChainDefinitionMap.put("/sys/mLogin", "anon"); //登录接口排除
        filterChainDefinitionMap.put("/sys/logout", "anon"); //登出接口排除
        filterChainDefinitionMap.put("/sys/mLogins", "anon"); //登录接口排除
        filterChainDefinitionMap.put("/sys/mLoginss", "anon"); //app登录接口排除
        filterChainDefinitionMap.put("/sys/selectDeparts", "anon"); //登录接口排除
        filterChainDefinitionMap.put("/sys/thirdLogin/**", "anon"); //第三方登录
        filterChainDefinitionMap.put("/sys/getToken", "anon"); //获取永久token
        filterChainDefinitionMap.put("/sys/getEncryptedString", "anon"); //获取加密串
        filterChainDefinitionMap.put("/sys/sms", "anon");//短信验证码 /getSmsCode
        filterChainDefinitionMap.put("/sys/getSmsCode", "anon");//短信验证码
        filterChainDefinitionMap.put("/sys/loginSms", "anon");//短信验证登录
        filterChainDefinitionMap.put("/sys/phoneLogin", "anon");//手机登录
        filterChainDefinitionMap.put("/sys/user/checkOnlyUser", "anon");//校验用户是否存在
        filterChainDefinitionMap.put("/sys/user/register", "anon");//用户注册
        filterChainDefinitionMap.put("/sys/sysDepart/querySysDepartList", "anon");//注册查询当前所有的项目列表
        filterChainDefinitionMap.put("/sys/user/WXregister", "anon");//小程序司机用户注册
        filterChainDefinitionMap.put("/sys/user/querySysUser", "anon");//根据手机号获取用户信息
        filterChainDefinitionMap.put("/sys/user/phoneVerification", "anon");//用户忘记密码验证手机号
        filterChainDefinitionMap.put("/sys/user/passwordChange", "anon");//用户更改密码
        filterChainDefinitionMap.put("/auth/2step-code", "anon");//登录验证码
        filterChainDefinitionMap.put("/appmessage/appmessage/list1", "anon");//登录页面展示APP二维码
//        filterChainDefinitionMap.put("/sys/systems/sysMessages/yhlist","anon");//养护大屏接口(需要权限)
        filterChainDefinitionMap.put("/sys/common/static/**", "anon");//图片预览 &下载文件不限制token
        filterChainDefinitionMap.put("/sys/common/pdf/**", "anon");//pdf预览
        filterChainDefinitionMap.put("/generic/**", "anon");//pdf预览需要文件
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/doc.html", "anon");
        filterChainDefinitionMap.put("/**/*.js", "anon");
        filterChainDefinitionMap.put("/**/*.css", "anon");
        filterChainDefinitionMap.put("/**/*.html", "anon");
        filterChainDefinitionMap.put("/**/*.svg", "anon");
        filterChainDefinitionMap.put("/**/*.pdf", "anon");
        filterChainDefinitionMap.put("/**/*.jpg", "anon");
        filterChainDefinitionMap.put("/**/*.png", "anon");
        filterChainDefinitionMap.put("/**/*.ico", "anon");
        filterChainDefinitionMap.put("/zlpz/ZLPZTestController/posttest", "anon");
        filterChainDefinitionMap.put("/zlpz/ZLPZTestController/posttestold", "anon");
        filterChainDefinitionMap.put("/zlpz/ZLPZTestController/gettest", "anon");
        filterChainDefinitionMap.put("/zlpz/ZLPZTestController/getxmid", "anon");
        filterChainDefinitionMap.put("/zlpz/ZLPZTestController/getbdid", "anon");
        filterChainDefinitionMap.put("/zlpz/ZLPZTestController/getsbid", "anon");
        filterChainDefinitionMap.put("/zlpz/ZLPZTestController/sbzc", "anon");
        filterChainDefinitionMap.put("/zlpz/ZLPZTestController/sbzcold", "anon");
        filterChainDefinitionMap.put("/zjjg/zjjgTestController/posttest", "anon");
        filterChainDefinitionMap.put("/openapigpsdatavo/openapigpsdatavo/recvGpsData", "anon");
        filterChainDefinitionMap.put("/openapigpsdatavo/openapigpsdatavo/recvGpsData1", "anon");
        filterChainDefinitionMap.put("/pfdj/registration/getNewData", "anon");//瑞苍大屏实验室评分
        filterChainDefinitionMap.put("/devicepipepilerealdata/devicePipepileRealdata/addOther1", "anon");//管桩处理实时数据接口
        filterChainDefinitionMap.put("/devicepipepilehistorydataone/devicePipepileHistorydataOne/addOther1", "anon");//管桩处理成桩数据接口
        filterChainDefinitionMap.put("/devicepipepilehistorydatapart/devicePipepileHistorydataPart/addOther1", "anon");//管桩处理生产数据接口
        filterChainDefinitionMap.put("/devicepipepilereport/devicePipepileReport/addOther1", "anon");//管桩处理报表数据接口
        filterChainDefinitionMap.put("/yajiangssrealdata/trYajiangSSRealdata/add", "anon");//智能压浆子表实时数据表
        filterChainDefinitionMap.put("/zhanglassrealdata/trZhanglaSSRealdata/add", "anon");//智能张拉子表实时数据表
        filterChainDefinitionMap.put("/virtualgateway/virtualGateway/add", "anon");//红外栅栏数据表
        filterChainDefinitionMap.put("/virtualgateway/virtualGatewayDoor/add", "anon");//水泥仓门禁数据表
        filterChainDefinitionMap.put("/lqbhz/bhzLqBases/add", "anon");//沥青拌合站数据推送47平台
        filterChainDefinitionMap.put("/hntbhz/bhzCementBase/addCZ", "anon");//曹宅项目拌合站数据接收
        filterChainDefinitionMap.put("/tadiao/deviceTowerHistorydata/insertSyncData", "anon");//塔吊历史表数据接收，根据设备编号同步更新到实时表
        filterChainDefinitionMap.put("/vehicleinfraredtemperature/vehicleInfraredTemperature/add", "anon");//车载红外测温
        filterChainDefinitionMap.put("/sydpssysample/syDpsSySample/getSyRenWu", "anon");//试验任务单下发接口
        filterChainDefinitionMap.put("/czsh/bhzCementOverHandler/add", "anon");//拌合站处置
        filterChainDefinitionMap.put("/largesbdoorswitch/largesbDoorSwitch/add", "anon");//大型设备门开关数据接收
        filterChainDefinitionMap.put("/trmaoxiayuyinglim/trMaoxiayuyingliM/addOpen", "anon");//锚下预应力张拉
        filterChainDefinitionMap.put("/trkongdaogjdwm/trKongdaogjDwm/addOpen", "anon");//孔道灌浆(定位)
        filterChainDefinitionMap.put("/trkongdaogjdxm/trKongdaogjDxm/addOpen", "anon");//孔道灌浆（定性）
        filterChainDefinitionMap.put("/device_concrete_realdata/deviceConcreteRealdata/addOpen", "anon");//混凝土强度监测
        filterChainDefinitionMap.put("/lowstrain_m/lowstrainM/addOpen", "anon");//低应变

        // update-begin--Author:sunjianlei Date:20190813 for：排除字体格式的后缀
        filterChainDefinitionMap.put("/**/*.ttf", "anon");
        filterChainDefinitionMap.put("/**/*.woff", "anon");
        filterChainDefinitionMap.put("/**/*.woff2", "anon");
        // update-begin--Author:sunjianlei Date:20190813 for：排除字体格式的后缀

        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/swagger**/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");

        filterChainDefinitionMap.put("/sys/annountCement/show/**", "anon");

        //积木报表排除
        filterChainDefinitionMap.put("/jmreport/**", "anon");
        filterChainDefinitionMap.put("/**/*.js.map", "anon");
        filterChainDefinitionMap.put("/**/*.css.map", "anon");
        //大屏模板例子
        filterChainDefinitionMap.put("/test/bigScreen/**", "anon");

        //websocket排除
        filterChainDefinitionMap.put("/websocket/**", "anon");//系统通知和公告
        filterChainDefinitionMap.put("/newsWebsocket/**", "anon");//CMS模块
        filterChainDefinitionMap.put("/vxeSocket/**", "anon");//JVxeTable无痕刷新示例

        //性能监控  TODO 存在安全漏洞泄露TOEKN（durid连接池也有）
       // filterChainDefinitionMap.put("/actuator/**", "anon"); // 二级等保隐藏

        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<String, Filter>(1);
        //如果cloudServer为空 则说明是单体 需要加载跨域配置【微服务跨域切换】
        Object cloudServer = env.getProperty(CommonConstant.CLOUD_SERVER_KEY);
        filterMap.put("jwt", new JwtFilter(cloudServer==null));
        shiroFilterFactoryBean.setFilters(filterMap);
        // <!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边
        filterChainDefinitionMap.put("/**", "jwt");

        // 未授权界面返回JSON
        shiroFilterFactoryBean.setUnauthorizedUrl("/sys/common/403");
        shiroFilterFactoryBean.setLoginUrl("/sys/common/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(ShiroRealm myRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm);

        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-
         * StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        //自定义缓存实现,使用redis
        securityManager.setCacheManager(redisCacheManager());
        return securityManager;
    }

    /**
     * 下面的代码是添加注解支持
     * @return
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        /**
         * 解决重复代理问题 github#994
         * 添加前缀判断 不匹配 任何Advisor
         */
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        defaultAdvisorAutoProxyCreator.setAdvisorBeanNamePrefix("_no_advisor");
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisCacheManager redisCacheManager() {
        log.info("===============(1)创建缓存管理器RedisCacheManager");
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        //redis中针对不同用户缓存(此处的id需要对应user实体中的id字段,用于唯一标识)
        redisCacheManager.setPrincipalIdFieldName("id");
        //用户权限信息缓存时间
        redisCacheManager.setExpire(200000);
        return redisCacheManager;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public IRedisManager redisManager() {
        log.info("===============(2)创建RedisManager,连接Redis..");
        IRedisManager manager;
        // redis 单机支持，在集群为空，或者集群无机器时候使用 add by jzyadmin@163.com
        if (lettuceConnectionFactory.getClusterConfiguration() == null || lettuceConnectionFactory.getClusterConfiguration().getClusterNodes().isEmpty()) {
            RedisManager redisManager = new RedisManager();
            redisManager.setHost(lettuceConnectionFactory.getHostName());
            redisManager.setPort(lettuceConnectionFactory.getPort());
            redisManager.setDatabase(lettuceConnectionFactory.getDatabase());
            redisManager.setTimeout(0);
            if (!StringUtils.isEmpty(lettuceConnectionFactory.getPassword())) {
                redisManager.setPassword(lettuceConnectionFactory.getPassword());
            }
            manager = redisManager;
        }else{
            // redis集群支持，优先使用集群配置
            RedisClusterManager redisManager = new RedisClusterManager();
            Set<HostAndPort> portSet = new HashSet<>();
            lettuceConnectionFactory.getClusterConfiguration().getClusterNodes().forEach(node -> portSet.add(new HostAndPort(node.getHost() , node.getPort())));
            //update-begin--Author:scott Date:20210531 for：修改集群模式下未设置redis密码的bug issues/I3QNIC
            if (oConvertUtils.isNotEmpty(lettuceConnectionFactory.getPassword())) {
                JedisCluster jedisCluster = new JedisCluster(portSet, 2000, 2000, 5,
                    lettuceConnectionFactory.getPassword(), new GenericObjectPoolConfig());
                redisManager.setPassword(lettuceConnectionFactory.getPassword());
                redisManager.setJedisCluster(jedisCluster);
            } else {
                JedisCluster jedisCluster = new JedisCluster(portSet);
                redisManager.setJedisCluster(jedisCluster);
            }
            //update-end--Author:scott Date:20210531 for：修改集群模式下未设置redis密码的bug issues/I3QNIC
            manager = redisManager;
        }
        return manager;
    }

}
