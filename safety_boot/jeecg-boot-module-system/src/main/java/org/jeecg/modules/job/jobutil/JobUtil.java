package org.jeecg.modules.job.jobutil;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics;
import com.trtm.iot.TbhzcailiaoStatistics.service.IBhzCementDetailStatisticsService;
import com.trtm.iot.bhzMaterialCfg.entity.BhzMaterialCfg;
import com.trtm.iot.bhzMaterialCfg.service.IBhzMaterialCfgService;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.bhzStatistics.service.IBhzCementStatisticsService;
import com.trtm.iot.bhzSwJipeiFanwei.entity.BhzSwJipeiFanwei;
import com.trtm.iot.bhzSwJipeiFanwei.service.IBhzSwJipeiFanweiService;
import com.trtm.iot.bhzSwJipeiStatistics.entity.BhzSwJipeiStatistics;
import com.trtm.iot.bhzSwJipeiStatistics.service.IBhzSwJipeiStatisticsService;
import com.trtm.iot.bhzSwJipeiStatistics.vo.BhzSwJipei;
import com.trtm.iot.bhzSwPhbZibiao.entity.BhzSwPhbZibiao;
import com.trtm.iot.bhzSwPhbZibiao.service.IBhzSwPhbZibiaoService;
import com.trtm.iot.bhzSwRecipe.entity.BhzSwRecipe;
import com.trtm.iot.bhzSwRecipe.service.IBhzSwRecipeService;
import com.trtm.iot.bhzSwShaifenShiyan.entity.BhzSwShaifenShiyan;
import com.trtm.iot.bhzSwShaifenShiyan.service.IBhzSwShaifenShiyanService;
import com.trtm.iot.bhzcfg.entity.BhzChaobiaoCfg;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.cunliangprocedureconfig.entity.CunliangProcedureConfig;
import com.trtm.iot.cunliangprocedureconfig.service.ICunliangProcedureConfigService;
import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.service.IDeviceMixpileHistorydataOneService;
import com.trtm.iot.devicemixpilestatic.entity.DeviceMixpileStatic;
import com.trtm.iot.devicemixpilestatic.service.IDeviceMixpileStaticService;
import com.trtm.iot.devicepipepilehistorydataone.entity.DevicePipepileHistorydataOne;
import com.trtm.iot.devicepipepilehistorydataone.service.IDevicePipepileHistorydataOneService;
import com.trtm.iot.devicepipepilestatistics.entity.DevicePipepileStatistics;
import com.trtm.iot.devicepipepilestatistics.service.IDevicePipepileStatisticsService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.pipepileYujing.entity.PipepileYujing;
import com.trtm.iot.pipepileYujing.service.IPipepileYujingService;
import com.trtm.iot.swbhz.entity.BhzSwBases;
import com.trtm.iot.swbhz.entity.BhzSwCailiao;
import com.trtm.iot.swbhz.service.IBhzSwBasesService;
import com.trtm.iot.swbhz.service.IBhzSwCailiaoService;
import com.trtm.iot.swbhzStatistics.entity.BhzSwStatistics;
import com.trtm.iot.swbhzStatistics.service.IBhzSwStatisticsService;
import com.trtm.iot.swbhzcailiaoStatistics.entity.BhzSwCailiaoStatistics;
import com.trtm.iot.swbhzcailiaoStatistics.service.IBhzSwCailiaoStatisticsService;
import com.trtm.iot.switchmachinePhone.entity.SwitchmachinePhone;
import com.trtm.iot.switchmachinePhone.service.ISwitchmachinePhoneService;
import com.trtm.iot.switchmachineStatistics.entity.SwitchingmachineStatistics;
import com.trtm.iot.switchmachineStatistics.service.ISwitchingmachineStatisticsService;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.tsyjzbStatistics.entity.TSyjzbStatistics;
import com.trtm.iot.tsyjzbStatistics.service.ITSyjzbStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.jeecg.common.util.DateUtils.date2Str;
import static org.jeecg.common.util.DateUtils.differHours;

/**
 * @author xgd
 * @date 2021/3/17
 * 定时任务工具类 包含多个参数也就是代表值
 */
@Component
@Slf4j
public class JobUtil {
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ISysConfigService sysConfigService;//定时任务事务层
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private IBhzCementStatisticsService bhzCementStatisticsService;
    @Autowired
    private IBhzSwCailiaoService bhzSwCailiaoService;
    @Autowired
    private IBhzSwBasesService bhzswBasesService;
    @Autowired
    private IBhzSwStatisticsService bhzSwStatisticsService;
    @Autowired
    private IBhzMaterialCfgService bhzMaterialCfgService;
    @Autowired
    private IBhzSwCailiaoStatisticsService bhzSwCailiaoStatisticsService;
    @Autowired
    private IBhzCementDetailStatisticsService bhzCementDetailStatisticsService;
    @Autowired
    private ITSyjzbStatisticsService syjzbStatisticsService;
    @Autowired
    private IDeviceMixpileStaticService deviceMixpileStaticService;
    @Autowired
    private IDeviceMixpileHistorydataOneService deviceMixpileHistorydataOneService;
    @Autowired
    private ICunliangProcedureConfigService cunliangProcedureConfigService;
    @Autowired
    private RedisUtil redisUtil;
    private static JobUtil jobUtil;
    @Autowired
    private IPipepileYujingService pipepileYujingService;
    @Autowired
    private IDevicePipepileHistorydataOneService pipepileHistorydataOneService;
    @Autowired
    private IDevicePipepileStatisticsService pipepileStatisticsService;
    @Autowired
    private IBhzSwRecipeService bhzSwRecipeService;
    @Autowired
    private IBhzSwPhbZibiaoService bhzSwPhbZibiaoService;
    @Autowired
    private IBhzSwJipeiFanweiService bhzSwJipeiFanweiService;
    @Autowired
    private IBhzSwJipeiStatisticsService bhzSwJipeiStatisticsService;
    @Autowired
    private IBhzSwShaifenShiyanService bhzSwShaifenShiyanService;

    @Autowired
    private IBhzPhoneService bhzPhoneService;

    @Autowired
    private ISysMessageService sysMessageService;

    @Autowired
    private ISwitchingmachineStatisticsService switchingmachineStatisticsService;
    @Autowired
    private ISwitchmachinePhoneService switchmachinePhoneService;

    @PostConstruct
    public void init() {
        jobUtil = this;
    }

    public static int HNTBHZ_CFG_COPY = 1;//砼拌合站过滤表数据
    public static int LQBHZ_CFG_COPY = 2;//沥青拌合站过滤表数据
    public static int SWBHZ_CFG_COPY = 3;//水稳拌合站过滤表数据
    public static int HNTBHZ_CFG = 4;//砼拌合站超标检测 处理历史数据
    public static int LQBHZ_CFG = 5;//沥青拌合站超标检测
    public static int SWBHZ_CFG = 6;//水稳拌合站超标检测
    public static int ZHYD_CFG = 7;//智慧用电实时数据超标监测
    public static int ZHYDHISTORY_CFG = 8;//智慧用电历史数据超标监测
    public static int HNTBHZ_TUISONG_PROJECT = 9;//拌合站项目信息拌合站信息 设备信息推送山东陆科
    public static int HNTBHZ_TUISONG_SHANDONGLUKE = 10;//砼拌合站数据推送山东陆科
    public static int LQBHZ_TUISONG_SHANDONGLIKE = 11;//沥青拌合站数据推送山东陆科
    public static int HNTBHZ_TUISONG_SHANDONGLUKECB = 12;//砼拌合站超标数据推送山东陆科
    public static int LQBHZCHAOBIAO_TUISONG_SHANDONGLIKE = 13;//沥青拌合站数据推送山东陆科
    public static int SWBHZ_TUISONG_SHANDONGLUKE = 14;//水稳拌合站数据推送山东陆科
    public static int SWBHZ_TUISONG_SHANDONGLUKECB = 15;//水稳拌合站超标数据推送山东陆科
    public static int HNTBHZ_RENWUDAN = 16;//混凝土任务单进度
    public static int WZTAIZHANG = 17;//原材物资台账
    public static int SCHEDULING_CAR_ALL = 18;//查询所有的运输车信息判断发车状态
    public static int SCHEDULING_CAR_ONE = 19;//查询状态是运输中的运输车信息判断发车状态
    public static int FRONT_DEVICE_WEILAN_CREATE = 20;//创建圆形电子围栏
    public static int FRONT_DEVICE_WEILAN_YUJING = 21;//电子围栏预警
    public static int BYS_CFG = 22;//标养室预警
    public static int ZHONGJIAO_XIANGSHAN_SHUGANG = 23;//中交象山疏港高速wbs
    public static int BHZ_WORKSTATUA = 24;//砼拌合站工作状态
    public static int BHZ_JRTT = 25;//金仁铜高速拌合站一标数据推送-》贵州质监局
    public static int WZXHTAIZHANG = 26;//原材料物资消耗台账
    public static int YDDING_TALK = 28;//义东钉钉考勤用户信息 每天最多运行2次
    public static int WZJYP_YDTS = 27;//物资原材料检验批数据推送义东高速
    public static int LC_YDTS = 29;//料仓数据推送义东高速
    public static int NB_JIAQIAOJI = 30;//宁波市特检察院----通用门式起重机推送
    public static int NB_JIAQIAOJI31 = 31;//宁波市特检察院----通用门式起重机推送
    public static int NB_JIAQIAOJI32 = 32;//宁波市特检察院----通用门式起重机推送
    public static int JBZ_CFG = 33;//水泥搅拌桩定时任务
    public static int YD_ZHIHUIYONGDIAN = 34;//义东智慧用电
    public static int CRANEHISTORY_CFG = 35;//大型设备历史数据预警定时任务
    public static int MONITOR_CFG = 36;//摄像头离线判断定时任务
    private static String appId = "lVOaJ52X";   //通用门式起重机推送秘钥 测试
    private static String encryptedStr = "36d93f6c958bf2780bcb54c430056833d37a7db0";//通用门式起重机推送秘钥 测试
    private static String appIds = "0b38R6Ww";   //通用门式起重机推送秘钥 正式
    private static String encryptedStrs = "66f61d77ad8480ed8596e2845e1967b6de02dcf2";//通用门式起重机推送秘钥 正式
    public static int RUICANG_HNTHT = 37; //瑞仓高速推送混凝土回弹数据到试验平台上
    public static int ZHIJIAN_YDLQ = 44; //义东
    public static int RUICANG_GANGJINBAOHUCENG = 38; //瑞仓高速推送钢筋保护层数据到试验平台上
    public static int YIDONG_SUIDAOWEIYAN = 39; //义东隧道围岩数据
    public static int WBSHEBEI = 40;//获取电子锁实时和历史数据
    public static int RC_GJBHC = 41;//瑞仓钢筋保护层
    public static int RC_HNTHT = 43;//瑞仓钢筋保护层
    public static int RC_JCRWD = 44;//瑞仓钢检测任务单
    public static String username = "hzgx";//物镖平台登录名
    public static String password = "123456";//物镖平台登录密码
    public static int RUICANG_HNTBHZ = 42;//瑞仓内网拌合站数据推送
    public static int RUICANG_HNTBHZSTATICSTIC = 45;//瑞仓内网拌合站统计数据推送
    public static int SUTAI_HNTBHZ = 46;//苏台拌合站统计数据推送
    public static int ALL_DINGDING = 47;//钉钉考勤机获取数据
    public static int HNTBHZ_CFG_NEW = 48;//砼拌合站超标检测 处理实时数据
    public static int DEVICE = 49;//获取环境监测数据
    public static int AISB_ZHYD = 50;//AI识别  智慧义东
    public static int ZHYF_RCRUISONG = 52;//瑞仓内网智慧用电数据推送
    public static int ST_BYS = 51;//苏台推送标养室数据
    public static int ST_TBHZ = 53;//苏台推送砼拌合站数据
    public static int ST_YLJ = 54;//苏台推送压力机数据
    public static int RUICANG_WZJINCHANGGB = 55;//瑞仓高速物资进场过磅数据推送到试验平台
    public static int RC_ZHANGLA = 56;//瑞仓内网张拉数据推送
    public static int RC_YAJIANG = 57;//瑞仓内网压浆数据推送
    public static int RC_WZJINCHANGGB = 58;//瑞仓内网地磅数据推送
    public static int FRONTDEVICEDATA_ZHONGTIESHIWUJU = 59;//获取中铁十五局合新铁路车辆信息数据定时任务
    public static int RC_BHZ_CHAOBIAO_UPGRADE = 60;//瑞苍拌合站
    public static int LH_LONGMENDIAO = 61;//黎霍龙门吊
    public static int BHZ_STATISTICS = 62;//砼拌合站统计
    public static int DB_SFBDB = 63;//中铁十五局三分部地磅
    public static int BHZ_SFBDB = 64;//中铁十五局三分部拌合站
    public static int ST_HJJC = 65;//环境监测
    public static int SYJ_STATIXTICS = 66;//试验机统计
    public static int ZLM_HJJC = 67;//张拉任务单统计
    public static int YJ_HJJC = 68;//压浆任务单统计
    public static int YD_BHZ = 69;//义东拌合站超标数据推送
    public static int TBHZ_FILTER = 70;//砼拌合站数据过滤
    public static int JBZ_STA = 71;//搅拌桩统计
    public static int DEVICE_CHAOBIAO = 72;//环境监测数据超标监测
    public static int STTWO_LMD = 73;//苏台二期推送龙门吊数据
    public static int STTWO_ZHYD = 74;//苏台二期推送智慧用电
    public static int RC_BHZ = 75;//瑞苍拌合站
    public static int CHANZANG_WZGB = 76;//川藏过磅数据
    public static int RC_MJKQ = 77;//瑞苍门禁考勤
    public static int RC_ZJBHZTS = 78;//瑞苍拌合站质检资料推送
    public static int RC_QTJC = 79;//瑞苍气体检测推送
    public static int STTWO_THREEHNTBHZ = 80;//苏台二期三标混凝土拌合站数据推送
    public static int STTWO_ONESYJ = 81;//苏台二期一标试验机数据推送
    public static int STTWO_TWOHNTBHZ = 82;//苏台二期二标混凝土拌合站数据推送
    public static int STTWO_TWOSYJ = 83;//苏台二期二标试验机数据推送
    public static int TAICANG_DEVICE = 84;//太仓环境监测数据推送
    public static int TAICANG_TBHZDATA = 85;//太仓混凝土拌合站数据推送
    public static int TAICANG_MIXPLIE = 86;//太仓搅拌桩数据推送
    public static int STTWO_FIVEHNTBHZ = 87;//苏台二期五标混凝土拌合站数据推送
    public static int STTWO_FIVESYJ = 88;//苏台二期五标试验机数据推送
    public static int SG_BHZ = 89;//疏港超标数据推送浙路品质
    public static int RC_GZ = 90;//瑞苍管桩
    public static int RC_CLMJ = 91;//瑞苍车辆门禁
    public static int STTWO_ONESL = 92;//苏台二期一标原材料出料数据推送
    public static int ST_CLGJ = 93; //苏台车辆轨迹数据推送接口
    public static int YB_GTBHZ = 94;//永壁拌合站推送
    public static int YB_GTSYS = 95;//永壁试验室推送
    public static int YB_GTZL = 96;//永壁张拉推送
    public static int YB_GTYJ = 97;//永壁压浆推送
    public static int HSY_SYJ = 98;//杭绍甬试验机推送阳光工程
    public static int BH_YALIJI = 99;//滨淮压力机数据推送
    public static int BH_KZKY = 100;//滨淮抗压抗折机数据推送
    public static int BH_WANNJ = 101;//滨淮万能机数据推送
    public static int RC_ZJZLTS = 102;//瑞苍张拉质检资料推送
    public static int RC_ZJYJTS = 103;//瑞苍压浆质检资料推送
    public static int HSY_ZL = 104;//杭绍甬张拉推送阳光工程
    public static int HSY_YJ = 105;//杭绍甬压浆推送阳光工程
    public static int WZTAIZHANG_MAN = 106;//人工过磅原材物资台账
    public static int RC_ZJTS = 107;//瑞仓桩基数据推送
    public static int YGGC_CSB = 108;//超声波推送阳光工程
    public static int MEMLIST = 109;//获取义东在场人员名单数据
    public static int BHZ_KAIGUANJI = 110;//拌合站开关机
    public static int CUN_TAIZUO_SWITCH = 111;//存梁台座关灯定时任务
    public static int ZHILIANGGONGXU = 112;//制梁工序自动确认定时任务
    public static int GZSTAJOB = 113;//管桩预警统计
    public static int SHDXTX = 114;//送货确认短信提醒
    public static int BHZSTAHIS = 115;//砼拌合站统计（历史数据）
    public static int RCBHZTS = 116;//瑞仓拌合站推送
    public static int RCZHANGLA = 117;//瑞仓张拉推送
    public static int RCYAJIANG = 118;//瑞仓压浆推送
    public static int RCBHZCBCL = 119;//瑞仓拌合站超标处理推送
    public static int RCZLYJXX = 120;//瑞仓张拉压浆超标信息推送
    public static int RCYCLSL = 121;//瑞仓原材料收料推送
    public static int RCZHYL_HIS = 122;//瑞仓智慧用电历史数据推送
    public static int RCZHYL_REAL = 123;//瑞仓智慧用电实时数据推送
    public static int RCHNTHT = 124;//瑞仓混凝土回弹数据推送
    public static int RCGBTS = 125;//瑞仓钢保数据推送
    public static int BHZYCLSTASTIC = 126;//拌合站按照设备统计材料用量
    public static int RCBYS = 127;//瑞仓标养室数据推送
    public static int RCMJKQ_HIS = 128;//瑞仓门禁考勤数据推送
    public static int RCCLMJ_HIS = 129;//瑞仓车辆门禁历史数据推送
    public static int RCQTJC_REAL = 130;//瑞仓气体监测实时数据推送
    public static int RWDXUHAOJOB = 131;//制梁任务单工序统计
    public static int RCTOKEN = 132;//瑞仓获取TOKEN
    public static int RCGZTS = 133;//瑞仓管桩数据推送
    public static int RCBHZ_JZL = 134;//瑞仓拌合站浇筑令数据推送
    public static int RCBHZ_JZL_PHB = 135;//瑞仓拌合站浇筑令配合比数据推送
    public static int KZ_ZL = 136;//柯诸张拉
    public static int KZ_SYJ = 137;//柯诸试验机
    public static int RCWZTZ = 138;//瑞仓地磅wztaizhang推送
    public static int RCRYDW_REAL = 139;//瑞仓人员定位实时数据推送
    public static int RCRYDW_HIS = 140;//瑞仓人员定位历史数据推送
    public static int JTJT_LQLSSJ = 141;//沥青历史数据推送交投集团
    public static int JTJT_LQJP = 142;//沥青级配推送交投集团
    public static int JTJT_LQPHB = 143;//沥青混合料配方推送交投集团
    public static int RCCLDW = 144;//瑞仓车辆定位数据推送213
    public static int RCQTJC_HIS = 145;//瑞仓气体监测历史数据推送
    public static int RCSBTS = 146;//瑞仓设备数据推送
    public static int KZ_HJJC = 147;//柯诸环境监测
    public static int YB_GTBHZ2 = 148;//开投拌合站推送(永壁二号机)
    public static int YB_GTBHZ3 = 149;//开投拌合站推送(武道2标)
    public static int YB_GTBHZ4 = 150;//开投拌合站推送（巴彭四标）
    public static int YB_GTBHZ5 = 151;//开投拌合站推送（巴彭五标）
    public static int YB_GTBHZ6 = 152;//开投拌合站推送（巴彭三标）
    public static int YB_GTBHZ7 = 153;//开投拌合站推送（巴彭三标）
    public static int ZJ_BHZSC = 154;//拌合站生产（推送中交）
    public static int ZJ_SYJSC = 155;//拌合站生产（推送中交）
    public static int JX_DB = 156;//济新高速地磅
    public static int ZLPZ_LQ = 157;//浙路品质沥青
    public static int BHZ_CLCBTJ = 158;//拌合站材料超标次数统计
    public static int RC_JypWztz = 159;//检验批数据推送
    public static int PM_CSHLC = 160;//初始化梁场信息
    public static int PM_CSHCLTZ = 161;//初始化存梁台座信息
    public static int PM_CSHZLTZ = 162;//初始化制梁台座信息
    public static int PM_CSHQL = 163;//初始化桥梁信息
    public static int PM_CSHQK = 164;//初始化桥跨信息
    public static int PM_CSHL = 165;//初始化梁信息
    public static int PM_CSHZL = 166;//制梁
    public static int PM_CSHLLX = 167;//初始化梁类型信息
    public static int SYS_WYSY = 168;//委外试验推送 (品茗)
    public static int ZLPZ_ZL = 170;//浙路品质张拉推送
    public static int ZLPZ_YJ = 171;//浙路品质压浆推送
    public static int ZLPZ_SYJ = 172;//浙路品质试验机推送
    public final static int PM_INIT_LAB = 173; //初始化实验室 (品茗)
    public static int LH_BHZ = 174;//六横（疏港三标）拌合站推送
    public static int LH_ZL = 175;//六横（疏港三标）张拉推送
    public static int HY_BHZ = 176;//杭甬二标拌合站推送
    public final static int PM_INIT_CONCRETETASK = 177;//分部分项推送 品茗
    public final static int PM_INIT_MIXINGPLANT = 178;//拌合站初始化 品茗
    public final static int PM_INIT_CREW = 190; //机组初始化(设备初始化) 品茗
    public final static int PM_INIT_CONCRETE_USE = 191; //初始化混凝土用量 品茗
    public final static int PM_INIT_CONCRETE_USE_DETAIL = 192; //初始化混凝土用量详情 品茗
    public final static int PM_INIT_POT = 193; //初始化料仓 品茗
    public final static int PM_INIT_SAND_STORAGE = 194; //初始沙石料仓 品茗
    public static int HUN_NINGT = 180;//混凝土信息 抗折抗压水泥机（品茗重要）10014 压力机器
    public static int HUN_NINGTCOMPRESSION = 181;//抗折抗压实验
    public static int HUN_REBARTRIAL = 182;//钢筋试验 已改万能机 10047
    public static int HUN_LABTEMP = 183;//实验室温度
    public static int PMSJ_ZL = 184;//品茗张拉
    public static int PMSJ_YJ = 185;//品茗压浆
    public static int BZB_JZTTJ = 186;//标准版，净重T统计
    public static int BZB_GPDH = 187;//标准版，shigongphb表数据出料统计
    public static int BHZ_CB_TJ_RC = 188;//拌合站超标统计瑞仓
    public static int BHZ_BH_TJ_RC = 189;//拌合站处置统计瑞仓项目
    public final static int BH1_TO_BH6_BHZ = 200; //滨淮1-6标混凝土拌合站数据推送
    public static int KZ_BHZ = 201;//柯诸拌合站
    public static int KZ_BYS = 202;//柯诸标养室
    public static int KZ_BHZ1B = 203;//柯诸1标拌合站
    public static int BHZ_CSYJ = 204;//拌合站超时预警
    public static int KZ_GJY = 205;//柯诸钢筋仪
    public static int KZ_HTY = 206;//柯诸回弹仪
    public static int ZL_CBJC = 207;//张拉超标检测
    public static int STZ_TS = 208;//商砼站数据推送
    public static int SHYJ_ZL = 209;//上海有间张拉数据推送
    public static int SHYJ_YJ = 210;//上海有间压浆数据推送
    public static int SHYJ_HJJC = 211;//上海有间环境检测数据推送
    public static int SHYJ_LMD = 212;//上海有间龙门吊数据推送
    public static int SHYJ_ZHYD = 213;//上海有间智慧用电数据推送
    public static int SHYJ_BHZ = 216;//上海有间拌合站数据推送
    public static int ZLPZ_LQSG = 214;//浙路品质疏港沥青
    public static int ZLPZ_SWBHZ = 215;//浙路品质水稳
    public static int JXGS_AITS = 217;//济新高速#AI识别数据推送
    public static int ZJWZ_GONGYI = 218;//高信工艺新增接口
    public static int LQBHZ_CFGSG = 225; //疏港沥青拌合站超标检测
    public static int YJ_CBJC = 226;//压浆超标检测
    public static int ZLPZ_BHZ = 228;//浙路品质拌合站通用
    public static int KZ_YJ = 230;//柯诸压浆
    public static int DJ_LQ = 232;//浙高建东交沥青
    public static int ZLPZ_LQTY = 234;//浙路品质沥青通用接口
    public static int JZB_LQ = 235;//计之宝义东沥青
    public static int DJ_TPNY = 236;//东交（浙高建）摊铺碾压
    public static int DJ_YSC = 237;//东交（浙高建）运输车
    public static int GZ_ZBZL_SJTS = 240;//管桩质保资料数据推送
    public static int ZLPZ_BHBHZ = 241;//浙路品质拌合站（已闭合）
    public static int JTJT_LQCBLSSJ = 242;//沥青历史数据推送交投集团
    public static int HY_YSC = 243;//
    public static int JTJT_SWLSSJ = 251;//水稳历史数据推送交投集团
    public static int JTJT_HJJC = 252;//环境监测推送数据
    public static int JTJT_TPNY = 253;//义东摊铺碾压交投集团推送
    public static int JTJT_LSGJ = 254;//义东历史轨迹交投集团推送
    public static int JTJT_YSD = 256;//义东运输单交投集团推送
    public static int JTJT_SWCBLSSJ = 263;//水稳超标数据交投集团推送
    public static int HC_LQ = 266;//水稳超标数据交投集团推送
    public static int JTJT_SWPHB = 267;//水稳级配
    public static int JTJT_SWJP = 268;//水稳级配
    public static int XINCFG_SHDXTX = 269;//新智慧用电预警短信提示

    public static int ZJWZ_TOKEN = 997;//浙江网泽车辆位置第三方WEB调用接口
    public static int FKYSC_TOKEN = 998;//金途车联,车辆实时信息更新
    public static int FKYSC_XZDZWL = 1000;//新增电子围栏判断方法
    public static int BHZ_TOKEN = 999;//获取token
    public static int YTW_ZJBHZTS = 222;//甬台温拌合站质检资料推送
    public static int YTW_ZJYJTS = 223;//甬台温压浆质检资料推送
    public static int YTW_ZJZLTS = 224;//甬台温张拉质检资料推送
    public static int RC_JHJD_BHZ = 227;//47金华基地拌合站推送
    public static int YC_ONE_SYYLJ = 229;//中铁8局推送试验压力机数据
    public static int TJ_LIAOCANG_JC = 231;//统计料仓进场总数
    public static int TJ_LIAOCANG_JCMAN = 238;//统计料仓进场总数
    public static int TJ_LIAOCANG_SY = 233;//统计料仓累计使用
    public static int BHZ_UNIDIED_PROCESS = 239;//拌合站统一待办
    public static int LIDONG_TS=244;//黎东高速项目推送
    public static int DEVICE_BRIDGE_REAL=245;//集团标准版架桥机实时数据推送
    public static int DEVICE_BRIDGE_HIS=246;//集团标准版架桥机历史数据推送
    public static int DEVICE_CRANE_HIS=247;//集团标准版龙门吊历史数据推送
    public static int DEVICE_CRANE_REAL=248;//集团标准版龙门吊实时数据推送
    public static int DEVICE_TOWER_HIS=249;//集团标准版 塔吊历史数据推送
    public static int DEVICE_TOWER_REAL=250;//集团标准版 塔吊实时数据推送
    public static int BHZ_LQ_BASESTS=255;//拌合站沥青杭甬复线数据推送47平台
    public static int SW_UNIDIED_PROCESS = 257;//水稳拌合站统一待办
    public static int LQ_UNIDIED_PROCESS = 258;//沥青拌合站统一待办
    public static int ZL_UNIDIED_PROCESS = 259;//张拉统一待办
    public static int YJ_UNIDIED_PROCESS = 260;//压浆统一待办
    public static int STTWO_FOURHNTBHZ = 261;//苏台二期四标混凝土拌合站数据推送
    public static int LQBHZ_TUISONG_ROAD = 262;//拌合站沥青杭甬复线数据47平台推送杭甬复线
    public static int MQTT_ON_OFF = 264;//MQTT开关
    public static int GZ_UNIDIED_PROCESS = 265;//沥青拌合站统一待办
    public static int ST_BYS_TWO = 270;//苏台二标推送标养室数据
    public static int ZLPZ_SBZX = 271;//浙路品质设备在线情况
    public static int ZLPZ_ZLYJXQK = 272;//浙路品质张拉压浆当日情况

    public static int ZJJG_BHZ_PUSH = 273;//浙江交工推送拌合站数据
    public static int WZLC_WLRW = 274;//物资料仓
    public static int BHZ_CZDATA_QIANYI = 275;//拌合站曹宅生产数据迁移
    public static int SYJ_TUISONGSX = 276;//试验机数据z平台推送苏信平台
    public static int SYJ_TUISONG = 277;//曹宅试验机数据47推送z平台
    public static int DJ_SW = 278;//东交义东水稳
    public static int AI_WARN_MSG = 279;//AI预警信息推送
    public static int Z_DB = 280;//义东过磅数据
    public static int SUTAI_LMD_TUISONG = 281;//苏台高速龙门吊数据推送tim
    public static int JZB_ZJZL = 282;//计之宝
    public static int LIDONG_JQJ_TUISONG = 283;//黎东高速架桥机数据推送
    public static int LIDONG_JLSRW = 284;//605龙门吊历史数据
    public static int SLJBZ_SB = 285;//水泥搅拌桩大屏-接口:设备实时状态
    public static int SLJBZ_lsjl = 286;//水泥搅拌桩大屏-接口:历史记录
    public static int LARGEGLSB_YUJING = 287;//大型设备挂篮预警添加短信发送
    public static int HC_TPNY_YUJING = 288;//华测摊铺碾压预警添加短信发送
    public static int SLJBZ_ZLYJ = 289;//水泥搅拌桩大屏-接口:质量预警判断标准
    public static int SLJBZ_SSJL = 290;//水泥搅拌桩大屏-接口:实时记录
    public static int SLJBZ_MRCN = 291;//水泥搅拌桩大屏-接口:每日产能
    public static int SLJBZ_XMGK = 292;//水泥搅拌桩大屏-接口:各项目概况
    public static int SLJBZ_SJXQ = 293;//水泥搅拌桩大屏-接口:设备数据与详情
    public static int LARGEJQJSB_YUJING = 294;//大型设备架桥机预警添加短信发送
    public static int LARGELMDSB_YUJING = 295;//大型设备龙门吊预警添加短信发送
    public static int LARGETDSB_YUJING = 296;//大型设备塔吊预警添加短信发送
    public static int YD_ZL47 = 297;//义东47张拉推送老平台
    public static int YD_YJ47 = 298;//义东47压浆推送老平台
    public static int GX_GUALAN_TUISONG = 299;//挂篮数据推送高信平台
    public static int YD_ZJZLTS = 300;//义东张拉质检资料推送
    public static int YD_ZJYJTS = 301;//义东张拉质检资料推送
    public static int KZ_ZLBL = 302;//柯诸张拉补录
    public static int ST_SYJ = 303;//苏台三标试验机
    public static int ST_SYJ1 = 305;//苏台三标试验机
    public static int FCD_CSYJ = 304;//发车单超时预警
    public static int ST_BIMBHZ = 306;//苏台bim拌合站对接
    public static int ST_BIMBYS = 307;//苏台bim标养室对接
    public static int ST_BIMZHYD = 308;//苏台bim智慧用电对接
    public static int ST_BIMLMD = 309;//苏台bim龙门吊对接
    public static int YJQS_BHZ = 310;//甬金衢上拌合站
    public static int YJQS_SYJ = 311;//甬金衢上试验机
    public static int YJQS_SBLB = 312;//设备列表接口
    public static int ST_ZHANGLA = 313;//苏台张拉
    public static int ST_YAJIANG = 315;//苏台压浆
    public static int ST_TFYS = 314;//土方压实
    public static int SYJ_DXYJ = 316;//力学试验短信预警
    public static int YJQS_DB = 317;//甬金衢上地磅
    public static int KZ_YJBL = 318;//柯诸压浆补录
    public static int ZL_SX = 319;//苏台张拉筛选保存
    public static int YJ_SX = 320;//苏台压浆筛选保存
    public static int YJQS_ZJBHZTS = 321;//甬金衢上拌合站质检资料推送
    public static int SSZW = 323;//申述浙晚 水泥搅拌桩定时任务
    public static int SSZW_MIXPILEONE_HB = 324;//申苏浙皖水泥搅拌桩数据合并
    public static int YJQS_HJJC = 325;//甬金衢上环境监测
    public static int YJQS_MQTT_SNJBZ = 326;//甬台温北段mqtt水泥搅拌桩推送市站
    public static int CJGZ_RNYJ = 327;//锤击管桩预警任务
    public static int ST_BIMJQJ = 328;//苏台bim架桥机对接
    public static int ST_ZL = 329;//苏台中交路建张拉
    public static int ST_YJ = 330 ;//苏台中交路建压浆

    public static int YJQS_LC = 331;// 浙江交工甬金衢上三标 3标 数据推送
    public static int YTW_ND_BYS = 332;// 甬台温南段标养室 数据推送
    public static int ZTYJ_CLJC = 333;// 中铁一局，增量获取收料数据
    public static int ZTYJ_CLCC = 334;// 中铁一局，增量获取现场发料单据
    public static int ZTYJ_HWCL = 335;// 合武车辆调度数据对接
    public static int YJQS_TPNY = 336;//甬金衢上摊铺碾压
    public static int YTWND_BHZZJZL = 337;//甬台温南段拌合站质检资料
    public static int ZLXXBM_QIANYI = 338;//47张拉xxb,m表数据迁移

    public static int DXSB_ZXZT = 339;//大型设备在线状态判断
    public static int KZ_SWBHZ = 340;//47张拉xxb,m表数据迁移
    public static int MQTTSLJBZ_JOB = 341;//MQTT集团软基处理数据接入对接
    public static int YJQS_JOB = 342;//新增编辑母材收料
    public static int CKJL_JOB = 343;//新增出库记录
    public static int XZGB_JOB = 344;//新增过磅信息

    public static int ZJZ_KHCM_LQ = 345;//开化成明沥青推送衢州市质监站
    public static int JTJT_ZRD = 346;//交投集团针入度
    public static int JTJT_YD = 349;//交投集团延度
    public static int JTJT_WDD = 347;//交投集团稳定度
    public static int JTJT_RHD = 348;//交投集团软化点

    public static int KZ_SWBHZ_TCP = 372;//柯诸水稳数据推送tcp
    public static int SY_HJJC_JKY = 350;//上虞104环境数据推送交科院
    public static int SY_ZHYD_JKY = 351;//上虞104智慧用电数据推送交科院
    public static int ZLPZ_YCJYP= 352;//浙路品质原材检验批推送
    public static int JTJT_KZKY= 354;//交投集团抗折抗压

    public static int MJKQ_NEW_YF = 353;//门禁考勤数据对接（宇泛）
    public static int HFSN_MJKQ_SJG = 354;//淮阜枢纽数据对接省建管

    public static int HFSN_MJKQ_PULL = 355;//淮阜枢纽钉钉考勤用户信息 每天最多运行2次
    public static int LYG_MJKQ_PULL = 372;//连云港204钉钉考勤用户信息 每天最多运行2次
    public static int JHJD_YJQSBHZ = 356;//z平台金华基地推送47甬金衢上
    public static int PANAN_SYJ = 357;//磐安351试验推送
    public static int YJGS_JZLTS = 358;//甬金高速（绍兴段）浇筑令数据推送
    public static int YJGS_JYPTS = 359;//甬金高速（绍兴段）检验批数据推送
    public static int RYDW_GETDATA = 360;//人员定位数据获取(淮河入海水道)
    public static int ZJJG_LMD = 361;//龙门吊推送浙江交工
    public static int ZJJG_ZL = 362;//张拉推送浙江交工
    public static int ZJJG_YJ = 363;//压浆推送浙江交工
    public static int ZJJG_ZHYD = 364;//智慧用电推送浙江交工
    public static int ZJJG_LC = 365;//料仓推送浙江交工
    public static int WYCD_GETDATA = 366;//围堰测点数据获取

    public static int YTWND_ZJZLTS = 367;//甬台温南段张拉质检资料
    public static int YTWND_ZJYJTS = 368;//甬台温南段压浆质检资料

    public static int HFSN_MJKQ_DEVICE = 369;//淮阜枢纽门禁考勤用户信息 每天最多运行2次
    public static int CONTROL_LMD_DEVICE = 370;// 龙门吊控制服务端口和服务器
    public static int YLQ_HJJC = 371;// 义龙庆环境监测推送运检
    public static int RYDW_JDAKY_GETDATA = 373;//人员定位数据获取(交大安科院)




    /**
     * 砼拌合站超标检测
     */
    public List<BhzCementBase> Hntjiance(Integer cfgtype) {
        List<BhzCementBase> bhzCementBases = sysConfigService.selectListbai(cfgtype);
        return bhzCementBases;
    }

    /**
     * 砼拌合站搅拌时间判断是否超标
     */
    public Map HNTJboverproof(Integer stir_datetime, Integer stir_datetime_design, List<BhzChaobiaoCfg> selectchaobiaolist) {
        Map map = new HashMap();

        int TimeLevel = 0;
        int A = stir_datetime - stir_datetime_design;//看是否是正负值
        float Rimary = 0;//默认超标时间标准
        float Middle = 20;
        float Advanced = 40;
        if (null != selectchaobiaolist) {
            if (null != selectchaobiaolist.get(0).getRimary()) {
                Rimary = selectchaobiaolist.get(0).getRimary();
            }
            if (null != selectchaobiaolist.get(0).getMiddle()) {
                Middle = selectchaobiaolist.get(0).getMiddle();
            }
            if (null != selectchaobiaolist.get(0).getAdvanced()) {
                Advanced = selectchaobiaolist.get(0).getAdvanced();
            }

            if (A >= 0) {
                map.put("TimeLevel", TimeLevel);
                return map;
            } else {
                A = -A;// 把负值转为正值

                if (A > Rimary && A <= Rimary) {
                    TimeLevel = 1;
                } else if (A > Middle && A <= Middle) {
                    TimeLevel = 2;
                } else if (A > Advanced) {
                    TimeLevel = 3;
                }
                map.put("TimeLevel", TimeLevel);
                map.put("overnum", A);

                return map;
            }

        } else {
            if (A >= 0) {
                map.put("TimeLevel", TimeLevel);
                return map;
            } else {
                A = -A;// 把负值转为正值

                if (A > Rimary && A <= Rimary) {
                    TimeLevel = 1;
                } else if (A > Middle && A <= Middle) {
                    TimeLevel = 2;
                } else if (A > Advanced) {
                    TimeLevel = 3;
                }
                map.put("TimeLevel", TimeLevel);
                map.put("overnum", A);

                return map;
            }
        }

    }


    /**
     * 判断砼拌合站材料是否超标
     *
     * @param selectcementlist
     * @param selectchaobiaolist
     * @param is_call
     * @param shebeiNo
     * @return
     */
    public Map JudgeOverweight(List<BhzCementDetail> selectcementlist,
                               List<BhzChaobiaoCfg> selectchaobiaolist, int is_call, String shebeiNo) {
        Map result = new HashMap<>();
        StringBuilder final_content = new StringBuilder();
        int finalOverLevel = 0;
        float primary = 0;
        float middle = 0;
        float advanced = 0;
        Double realnum = 0.0;//实际用量
        Double theorynum = 0.0;//理论用量
        int materiale_type = 0;// 材料类别
        String materiale_name = "";//材料名称
        for (BhzCementDetail detail : selectcementlist) {
            if (null != detail.getRealityNumber()) {
                realnum = detail.getRealityNumber();//实际用量
            }
            if (null != detail.getTheoryNumber()) {
                theorynum = detail.getTheoryNumber();//理论用量
            }
            if (null != detail.getMaterialeType()) {
                materiale_type = detail.getMaterialeType();// 材料类别
            } else {
                materiale_type = 0;
            }
            if (null != detail.getMaterialeName()) {
                materiale_name = detail.getMaterialeName();//材料名称
                int i = LqJobUntil.lqCailiaotype(materiale_name);
                if (materiale_type != i) {
                    materiale_type = i;
                }
            }
            double errornum = realnum - theorynum;
            double overnum = 0;
            if (theorynum != 0) {
                overnum = errornum / theorynum * 100;
            }

            if (Double.isNaN(overnum)) {
                overnum = 0;
            }
            if (materiale_type < 5) {
                primary = 2;
                middle = 5;
                advanced = 10;
            } else if (materiale_type >= 5) {
                primary = 1;
                middle = 5;
                advanced = 10;
            }
            if (materiale_name.equals("瓜子片")) {
                primary = 2;
            }
            BhzCementDetail bhzCementDetail = new BhzCementDetail();
            BhzChaobiaoCfg bhzChaobiaoCfg = new BhzChaobiaoCfg();
            if (null != selectchaobiaolist) {
                //此循环是获取和当前材料类型相匹配的材料类型
                for (BhzChaobiaoCfg bhzChaobiaoCfgs : selectchaobiaolist) {
                    Integer materialType = bhzChaobiaoCfgs.getMaterialType();
                    if (null == materialType || materialType != materiale_type) {
                        continue;
                    } else if (materialType == materiale_type) {
                        bhzChaobiaoCfg = bhzChaobiaoCfgs;
                    }
                }
            }
            if (null != bhzChaobiaoCfg) {
                Object primary1 = bhzChaobiaoCfg.getRimary();
                Object middle1 = bhzChaobiaoCfg.getMiddle();
                Object advanced1 = bhzChaobiaoCfg.getAdvanced();
                if (null != primary1) {
                    primary = bhzChaobiaoCfg.getRimary();
                }
                if (null != middle1) {
                    middle = bhzChaobiaoCfg.getMiddle();
                }
                if (null != advanced1) {
                    advanced = bhzChaobiaoCfg.getAdvanced();
                }
            }
            int materiale_over_level = 0;

            //显示正负号
            double overnum1 = 0.0;
            if (overnum < 0) {// 如果小于0就把值变为正数
                overnum1 = -overnum;
            }else{
                overnum1 = overnum;
            }
            if (overnum1 > 0 && overnum1 <= primary) {
                materiale_over_level = 0;
            } else if (overnum1 > primary && overnum1 <= middle) {
                materiale_over_level = 1;
            } else if (overnum1 > middle && overnum1 < advanced) {
                materiale_over_level = 2;
            } else if (overnum1 > advanced) {
                materiale_over_level = 3;
            }
            if (materiale_over_level > finalOverLevel) {
                finalOverLevel = materiale_over_level;
            }
            bhzCementDetail.setMaterialeOverLevel(materiale_over_level);
            bhzCementDetail.setOverPrimarySetvalue(Double.valueOf(primary));
            bhzCementDetail.setOverMiddleSetvalue(Double.valueOf(middle));
            bhzCementDetail.setOverAdvancedSetvalue(Double.valueOf(advanced));
            bhzCementDetail.setErrorValue(errornum);
            bhzCementDetail.setOverValue(overnum);
            bhzCementDetail.setId(detail.getId());
            bhzCementDetail.setMaterialeType(materiale_type);
            bhzCementDetailService.updateBy(bhzCementDetail);//根据id修改拌合站材料表的数据
            if (materiale_over_level == 0) {
                continue;
            }
            final_content.append(String.format("%1$s超%2$.2f%%", materiale_name, overnum));
        }
        result.put("final_content", final_content.toString());
        result.put("level", finalOverLevel);
        return result;
    }

    public synchronized void ceshi(Integer i) {
        int x = 9;
        int c = x + i;
        System.out.println("111111111111111" + c);
    }

    /**
     * 砼统计表加信息（拌合机生产统计）
     *
     * @param selecthntbhzone
     * @param selectcementlist
     * @param level
     * @param sbno
     */
    public synchronized void Sattistics(BhzCementBase selecthntbhzone, List<BhzCementDetail> selectcementlist, int level, String sbno) {
        float estimate_number = NumberUtil.strToFloat(String.valueOf(selecthntbhzone.getEstimateNumber()));
        BhzCementStatistics bhzCementStatistics = new BhzCementStatistics();
        int SattisticsId = 0;
        Date productDatetime = selecthntbhzone.getProductDatetime();
        String datanyr = NumberUtil.Stringnyr(productDatetime);//格式化后的时间
        Date datanyr1 = NumberUtil.datanyr(datanyr);
        String projectName = selecthntbhzone.getProjectName();
        String poureLocation = selecthntbhzone.getPoureLocation();
        String jobLocation = selecthntbhzone.getJobLocation();
        String formulaNo = selecthntbhzone.getFormulaNo();
        String strengthRank = selecthntbhzone.getStrengthRank();
        String shebeiNo = selecthntbhzone.getShebeiNo();
        BhzCementStatistics selectlimit = bhzCementStatisticsService.selectlimit(datanyr1, projectName, poureLocation, jobLocation, formulaNo, strengthRank, shebeiNo);
        if (null != selectlimit) {
            log.info(String.format("拌合站统计累加获取统计数据" + selectlimit.getId()));
            count(selectlimit, level, estimate_number);//超标盘数以及方量累加
            SattisticsId = selectlimit.getId();
        } else {
            int addsattistics = addsattistics(selecthntbhzone);// 添加一条统计信息
            SattisticsId = addsattistics;
        }
        detailStatistics(selectcementlist, sbno, SattisticsId, datanyr1);
        /**
         * 料仓库存统计未写
         */
    }

    /**
     * 拌合站数据推送贵州质监局(金仁铜高速一标)
     *
     * @param jsonArray
     * @return
     */
    public Integer GETLeiRongTBHZ(JSONArray jsonArray) {//String Guid,String EquipCode,String Data
        Integer code = 0;
        String post = HttpRequest.post("https://gziot.21huayan.com/api/MSTUpload/WarningList")
                .header("Content-Type", "application/json")
                .body(String.valueOf(jsonArray))
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(post);
        Boolean success = (Boolean) jsonObject.get("Success");
        if (success) {
            code = 200;
        } else {
            code = 400;
        }
        return code;
    }

    /**
     * 物资检验批数据推送义东高速
     *
     * @param data
     * @return
     */
    public Integer GETYidongWzjyp(String data) {
        int code = 0;
        String post = HttpRequest.post("http://47.97.173.113:8081/yd/rest/YuanCaiDate/upload")
                .header("Content-Type", "application/json")
                .body(data)
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(post);
        Integer codes = (Integer) jsonObject.get("code");
        if (codes == 200) {
            code = 200;
        } else {
            code = 400;
        }
        return code;
    }

    /**
     * 物资料仓数据推送义东高速
     *
     * @param data
     * @return
     */
    public Integer GETYidongWzLC(String data) {
        int code = 0;
        String post = HttpRequest.post("http://47.97.173.113:8081/yd/rest/LiaoCangData/upload")
                .header("Content-Type", "application/json")
                .body(data)
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(post);
        Integer codes = (Integer) jsonObject.get("code");
        if (codes == 200) {
            code = 200;
        } else {
            code = 400;
        }
        return code;
    }

    /**
     * 宁波市特检察院通用门式起重机获取秘钥
     *
     * @param
     * @return
     */
    public String GETNingBoQzj() {
        String code = null;
        String post = HttpRequest.get("http://api210521.hold2025.com:9096/seomApi/getAccessToken?appId=" + appIds + "&encryptedStr=" + encryptedStrs + "")
                .header("Content-Type", "application/json")
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(post);
        JSONObject data1 = jsonObject.getJSONObject("data");
        String accessToken = String.valueOf(data1.get("accessToken"));
        Integer codes = (Integer) jsonObject.get("status");
        if (codes == 200) {
            code = accessToken;
            //redisUtil.set("NBaccessToken",accessToken,5400);//取消缓存 直接每次请求更新
        } else {
            code = null;
        }
        return code;
    }

    /**
     * 宁波市特检察院通用门式起重机推送
     */
    public Integer GETNingBoQzjTuiSong(JSONObject data) {
        Integer code = null;
        String post = HttpRequest.post("http://api210521.hold2025.com:9096/seomApi/submit")
                .header("Content-Type", "application/json")
                .body(String.valueOf(data))
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(post);
        Integer codes = (Integer) jsonObject.get("status");
        if (codes == 200) {
            code = 200;
        } else {
            code = 400;
        }
        return code;
    }

    /**
     * @param selectcementlist
     * @param sbno
     * @param cs_id
     * @param datanyr1
     */
    public void detailStatistics(List<BhzCementDetail> selectcementlist, String sbno, int cs_id, Date datanyr1) {
        float realnum = 0;
        float theorynum = 0;
        String cailiaoid = "";// 材料id
        String materiale_name = "";
        for (BhzCementDetail detail : selectcementlist) {
            realnum = Convert.toFloat(detail.getRealityNumber(), 0f);
            theorynum = Convert.toFloat(detail.getTheoryNumber(), 0f);
            cailiaoid = detail.getMaterialeId();
            materiale_name = detail.getMaterialeName();

            //如果材料id为空，则生成并写入材料配置表
            if (StrUtil.isBlank(cailiaoid) && StrUtil.isNotBlank(materiale_name)) {
                BhzMaterialCfg selectone = bhzMaterialCfgService.selectone(sbno, materiale_name);
                if (null == selectone) {
                    BhzMaterialCfg bhzMaterialCfg = new BhzMaterialCfg();
                    bhzMaterialCfg.setBhjno(sbno);
                    bhzMaterialCfg.setName(materiale_name);
                    if (StringUtils.isBlank(cailiaoid)) {
                        cailiaoid = UUID.randomUUID().toString();
                    }
                    bhzMaterialCfg.setMaterialid(cailiaoid);
                    bhzMaterialCfg.setSpecs(materiale_name);
                    bhzMaterialCfgService.save(bhzMaterialCfg);
                } else {
                    cailiaoid = selectone.getMaterialid();
                }
                detail.setMaterialeId(cailiaoid);
                bhzCementDetailService.updateById(detail);
            }
            // 查询材料统计表中是否有这样的数据，取出id，如果有就在这个数据的基础上把真实用量
            // ，理论用量累加到数据库中，如果没有就创建一条数据，取出id ，再把数据累加进去，
            BhzCementDetailStatistics selectone = bhzCementDetailStatisticsService.selectone(cs_id, cailiaoid);
            if (null == selectone) {
                BhzCementDetailStatistics bhzCementDetailStatistics = new BhzCementDetailStatistics();
                bhzCementDetailStatistics.setCsId(cs_id);
                bhzCementDetailStatistics.setMaterialeType(detail.getMaterialeType());
                bhzCementDetailStatistics.setMaterialeName(materiale_name);
                bhzCementDetailStatistics.setRealityNumber(Double.valueOf(realnum));
                bhzCementDetailStatistics.setTheoryNumber(Double.valueOf(theorynum));
                bhzCementDetailStatistics.setCailiaoid(cailiaoid);
                bhzCementDetailStatistics.setShebeiNo(sbno);
                bhzCementDetailStatistics.setStatisticsTime(datanyr1);
                bhzCementDetailStatisticsService.save(bhzCementDetailStatistics);
                selectone = bhzCementDetailStatisticsService.selectone(cs_id, cailiaoid);
            } else {
                bhzCementDetailStatisticsService.AddReality_numberOne(selectone.getId(), realnum);
                bhzCementDetailStatisticsService.AddTheory_numberOne(selectone.getId(), theorynum);
            }
            int chaobiaodengji = Convert.toInt(detail.getMaterialeOverLevel(), 0);//在主方法中已经计算过超标等级
            if (chaobiaodengji == 1 && selectone!=null) {// 初级超标
                bhzCementDetailStatisticsService.AddPrimary_numberOne(selectone.getId());
            } else if (chaobiaodengji == 2 && selectone!=null) {// 中级超标
                bhzCementDetailStatisticsService.AddMiddle_numberOne(selectone.getId());
            } else if (chaobiaodengji == 3 && selectone!=null) {// 高级超标
                bhzCementDetailStatisticsService.AddAdvanced_numberOne(selectone.getId());
            }
        }
    }

    /**
     * 添加一条统计信息
     *
     * @param selecthntbhzone
     */
    public int addsattistics(BhzCementBase selecthntbhzone) {
        String shebeiNo = selecthntbhzone.getShebeiNo();
        String projectName = selecthntbhzone.getProjectName();
        String jobLocation = selecthntbhzone.getJobLocation();
        String poureLocation = selecthntbhzone.getPoureLocation();
        String formulaNo = selecthntbhzone.getFormulaNo();
        String strengthRank = selecthntbhzone.getStrengthRank();
        Date productDatetime = selecthntbhzone.getProductDatetime();
        Integer overLevel = selecthntbhzone.getOverLevel();
        String stringnyr = NumberUtil.Stringnyr(productDatetime);
        Date datanyr = NumberUtil.datanyr(stringnyr);
        Double estimateNumber = selecthntbhzone.getEstimateNumber();
        BhzCementStatistics bhzCementStatistics = new BhzCementStatistics();
        bhzCementStatistics.setShebeiNo(shebeiNo);
        bhzCementStatistics.setProjectName(projectName);
        bhzCementStatistics.setJobLocation(jobLocation);
        bhzCementStatistics.setPoureLocation(poureLocation);
        bhzCementStatistics.setFormulaNo(formulaNo);
        bhzCementStatistics.setStrengthRank(strengthRank);
        bhzCementStatistics.setStatisticsTime(datanyr);
        if (overLevel == 1) {
            bhzCementStatistics.setAllOverproofDish(1);
            bhzCementStatistics.setPrimaryDish(1);
            bhzCementStatistics.setMiddleDish(0);
            bhzCementStatistics.setAdvancedDish(0);
        } else if (overLevel == 2) {
            bhzCementStatistics.setPrimaryDish(0);
            bhzCementStatistics.setMiddleDish(1);
            bhzCementStatistics.setAdvancedDish(0);
            bhzCementStatistics.setAllOverproofDish(1);
        } else if ((overLevel == 3)) {
            bhzCementStatistics.setPrimaryDish(0);
            bhzCementStatistics.setMiddleDish(0);
            bhzCementStatistics.setAdvancedDish(1);
            bhzCementStatistics.setAllOverproofDish(1);
        } else {
            bhzCementStatistics.setPrimaryDish(0);
            bhzCementStatistics.setMiddleDish(0);
            bhzCementStatistics.setAdvancedDish(0);
            bhzCementStatistics.setAllOverproofDish(0);
        }

//        if (overLevel == 2) {
//            bhzCementStatistics.setMiddleDish(1);
//            bhzCementStatistics.setAllOverproofDish(1);
//        } else {
//            bhzCementStatistics.setMiddleDish(0);
//            bhzCementStatistics.setAllOverproofDish(0);
//        }
//        if (overLevel == 3) {
//            bhzCementStatistics.setAdvancedDish(1);
//            bhzCementStatistics.setAllOverproofDish(1);
//        } else {
//            bhzCementStatistics.setAdvancedDish(0);
//            bhzCementStatistics.setAllOverproofDish(0);
//        }
        bhzCementStatistics.setPrimaryHandle(0);
        bhzCementStatistics.setPrimaryPercent(0.0);
        bhzCementStatistics.setMiddleHandle(0);
        bhzCementStatistics.setMiddlePercent(0.0);
        bhzCementStatistics.setAdvancedHandle(0);
        bhzCementStatistics.setAdvancedPercent(0.0);
        bhzCementStatistics.setAllDish(1);
        bhzCementStatistics.setAllHandleDish(0);
        bhzCementStatistics.setAllPercent(0.0);
        bhzCementStatistics.setEstimateNumber(estimateNumber);
        boolean save = bhzCementStatisticsService.save(bhzCementStatistics);
        if (save) {
            log.info(String.format("拌合站统计新添加数据" + bhzCementStatistics.toString()));
        }
        Integer id = bhzCementStatistics.getId();
        return id;
    }

    /**
     * 水稳添加一条统计信息
     *
     * @param swBases
     */
    public void addswsattistics(BhzSwBases swBases) {
        String shebeiNo = swBases.getShebeibianhao();
        String projectName = swBases.getProjectName();
        String jobLocation = swBases.getJobLocation();
        String poureLocation = swBases.getPoureLocation();
        String formulaNo = swBases.getFormulaNo();
        String strengthRank = swBases.getStrengthRank();
        String productDatetime = swBases.getChuliaoshijian();
        Date datanyr = NumberUtil.datanyr(productDatetime);
        Integer chaobiaodengji = swBases.getChaobiaodengji();
        BhzSwStatistics bhzSwStatistics = new BhzSwStatistics();
        bhzSwStatistics.setShebeibianhao(shebeiNo);
        bhzSwStatistics.setProjectName(projectName);
        bhzSwStatistics.setJobLocation(jobLocation);
        bhzSwStatistics.setPoureLocation(poureLocation);
        bhzSwStatistics.setFormulaNo(formulaNo);
        bhzSwStatistics.setStrengthRank(strengthRank);
        bhzSwStatistics.setStatisticsTime(datanyr);
        bhzSwStatistics.setPrimaryHandle(0);
        bhzSwStatistics.setPrimaryPercent(0.0);
        bhzSwStatistics.setMiddleHandle(0);
        bhzSwStatistics.setMiddlePercent(0.0);
        bhzSwStatistics.setAdvancedHandle(0);
        bhzSwStatistics.setAdvancedPercent(0.0);
        bhzSwStatistics.setAllDish(0);
        bhzSwStatistics.setPrimaryDish(0);
        bhzSwStatistics.setMiddleDish(0);
        bhzSwStatistics.setAdvancedDish(0);
        bhzSwStatistics.setAllOverproofDish(0);
        bhzSwStatistics.setAllHandleDish(0);
        bhzSwStatistics.setAllPercent(0.0);
        bhzSwStatistics.setEstimateNumber(0.0);
        bhzSwStatisticsService.save(bhzSwStatistics);
    }

    /**
     * 把总盘数加1  如果超标级别为1 在初级超标盘数中加1 如果超标级别为2 在中 级超标盘数中加1 如果超标级别为3 在高 级超标盘数中加1
     *
     * @param Sattistics
     * @param level
     */
    public void count(BhzCementStatistics Sattistics, int level, float estimateNumber) {
        BhzCementStatistics statistics1 = new BhzCementStatistics();
        int id = Sattistics.getId();
        Double estimateNumbers = Sattistics.getEstimateNumber();
        Integer allDish = Sattistics.getAllDish();
        Integer primaryDish = Sattistics.getPrimaryDish();//初级超标盘数
        Integer middleDish = Sattistics.getMiddleDish();//中级超标盘数
        Integer advancedDish = Sattistics.getAdvancedDish();//高级超标盘数
        Integer allOverproofDish = Sattistics.getAllOverproofDish();//总超标盘数
        Integer allsum = allDish + 1;
        double sumfl = estimateNumbers + estimateNumber;
        Integer cjsum = 0;
        Integer allsums = 0;
        Integer zjsum = 0;
        Integer gjsum = 0;
        if (level == 1) {
            cjsum = primaryDish + 1;
            allsums = allOverproofDish + 1;
            zjsum = middleDish;
            gjsum = advancedDish;
        } else if (level == 2) {
            allsums = allOverproofDish + 1;
            zjsum = middleDish + 1;
            cjsum = primaryDish;
            gjsum = advancedDish;
        } else if (level == 3) {
            allsums = allOverproofDish + 1;
            gjsum = advancedDish + 1;
            zjsum = middleDish;
            cjsum = primaryDish;
        } else {
            cjsum = primaryDish;
            allsums = allOverproofDish;
            zjsum = middleDish;
            gjsum = advancedDish;
        }
        boolean b = bhzCementStatisticsService.updatestatisticsone(id, allsum, sumfl, cjsum, allsums, zjsum, gjsum);//总盘数加1
        if (b) {
            log.info(String.format("拌合站统计累加成功！"));
        } else {
            log.info(String.format("拌合站统计累加失败！"));
        }

    }

    /**
     * 把总盘数加1  如果超标级别为1 在初级超标盘数中加1 如果超标级别为2 在中 级超标盘数中加1 如果超标级别为3 在高 级超标盘数中加1
     *
     * @param Sattistics
     * @param level
     */
    public void swcount(BhzSwStatistics Sattistics, int level) {
        BhzSwStatistics statistics1 = new BhzSwStatistics();
        int id = Sattistics.getId();
        Integer allDish = Sattistics.getAllDish();
        Integer allsum = allDish + 1;
        statistics1.setAllDish(allsum);
        statistics1.setId(id);
        bhzSwStatisticsService.updateById(statistics1);//总盘数加1
        if (level == 1) {
            swupdateprimaryDish(Sattistics);//初级超标盘数加1
            swupdatebhzCementStatistics(Sattistics);//总超标盘数加1
        } else if (level == 2) {
            swupdateMiddleDish(Sattistics);//中级超标盘数加1
            swupdatebhzCementStatistics(Sattistics);//总超标盘数加1
        } else if (level == 3) {
            swupdateAdvancedDish(Sattistics);//高级超标盘数加1
            swupdatebhzCementStatistics(Sattistics);//总超标盘数加1
        }
    }

    /**
     * 方量累加
     *
     * @param id
     * @param estimate_number
     * @param estimateNumber
     */
    public void updateestimatenumber(Integer id, Float estimate_number, Double estimateNumber) {
        BhzCementStatistics statistics = new BhzCementStatistics();
        double es = estimate_number + estimateNumber;
        statistics.setEstimateNumber(es);
        statistics.setId(id);
        bhzCementStatisticsService.updateById(statistics);//高级超标盘数加1
    }

    /**
     * 水稳方量累加
     *
     * @param id
     * @param estimate_number
     * @param estimateNumber
     */
    public void swupdateestimatenumber(Integer id, Double estimate_number, Double estimateNumber) {
        BhzSwStatistics statistics = new BhzSwStatistics();
        double es = estimate_number + estimateNumber;
        statistics.setEstimateNumber(es);
        statistics.setId(id);
        bhzSwStatisticsService.updateById(statistics);//高级超标盘数加1
    }

    /**
     * 高级超标盘数加1
     *
     * @param Sattistics
     */
    public void updateAdvancedDish(BhzCementStatistics Sattistics) {
        BhzCementStatistics statistics = new BhzCementStatistics();
        int id = Sattistics.getId();
        Integer advancedDish = Sattistics.getAdvancedDish();
        Integer advancedDish1 = advancedDish + 1;
        statistics.setAdvancedDish(advancedDish1);
        statistics.setId(id);
        bhzCementStatisticsService.updateById(statistics);//高级超标盘数加1
    }

    /**
     * 中级超标盘数加1
     *
     * @param Sattistics
     */
    public void updateMiddleDish(BhzCementStatistics Sattistics) {
        BhzCementStatistics statistics = new BhzCementStatistics();
        int id = Sattistics.getId();
        Integer middleDish = Sattistics.getMiddleDish();
        Integer middleDish1 = middleDish + 1;
        statistics.setMiddleDish(middleDish1);
        statistics.setId(id);
        bhzCementStatisticsService.updateById(statistics);//中级超标盘数加1
    }

    /**
     * 初级超标盘数加1
     *
     * @param Sattistics
     */
    public void updateprimaryDish(BhzCementStatistics Sattistics) {
        BhzCementStatistics statistics = new BhzCementStatistics();
        int id = Sattistics.getId();
        Integer primaryDish = Sattistics.getPrimaryDish();
        Integer primaryDish1 = primaryDish + 1;
        statistics.setPrimaryDish(primaryDish1);
        statistics.setId(id);
        bhzCementStatisticsService.updateById(statistics);//初级超标盘数加1
    }

    /**
     * 水稳初级超标盘数加1
     *
     * @param Sattistics
     */
    public void swupdateprimaryDish(BhzSwStatistics Sattistics) {
        BhzSwStatistics statistics = new BhzSwStatistics();
        int id = Sattistics.getId();
        Integer primaryDish = Sattistics.getPrimaryDish();
        Integer primaryDish1 = primaryDish + 1;
        statistics.setPrimaryDish(primaryDish1);
        statistics.setId(id);
        bhzSwStatisticsService.updateById(statistics);//初级超标盘数加1
    }

    /**
     * 水稳中级超标盘数加1
     *
     * @param Sattistics
     */
    public void swupdateMiddleDish(BhzSwStatistics Sattistics) {
        BhzSwStatistics statistics = new BhzSwStatistics();
        int id = Sattistics.getId();
        Integer middleDish = Sattistics.getMiddleDish();
        Integer middleDish1 = middleDish + 1;
        statistics.setMiddleDish(middleDish1);
        statistics.setId(id);
        bhzSwStatisticsService.updateById(statistics);//中级超标盘数加1
    }

    /**
     * 水稳高级超标盘数加1
     *
     * @param Sattistics
     */
    public void swupdateAdvancedDish(BhzSwStatistics Sattistics) {
        BhzSwStatistics statistics = new BhzSwStatistics();
        int id = Sattistics.getId();
        Integer advancedDish = Sattistics.getAdvancedDish();
        Integer advancedDish1 = advancedDish + 1;
        statistics.setAdvancedDish(advancedDish1);
        statistics.setId(id);
        bhzSwStatisticsService.updateById(statistics);//高级超标盘数加1
    }

    /**
     * 总超标盘数加1
     *
     * @param Sattistics
     */
    public void updatebhzCementStatistics(BhzCementStatistics Sattistics) {
        BhzCementStatistics statistics = new BhzCementStatistics();
        int id = Sattistics.getId();
        Integer allOverproofDish = Sattistics.getAllOverproofDish();
        Integer allOverproofDish1 = allOverproofDish + 1;
        statistics.setAllOverproofDish(allOverproofDish1);
        statistics.setId(id);
        bhzCementStatisticsService.updateById(statistics);//总超标盘数加1
    }

    /**
     * 水稳总超标盘数加1
     *
     * @param Sattistics
     */
    public void swupdatebhzCementStatistics(BhzSwStatistics Sattistics) {
        BhzSwStatistics statistics = new BhzSwStatistics();
        int id = Sattistics.getId();
        Integer allOverproofDish = Sattistics.getAllOverproofDish();
        Integer allOverproofDish1 = allOverproofDish + 1;
        statistics.setAllOverproofDish(allOverproofDish1);
        statistics.setId(id);
        bhzSwStatisticsService.updateById(statistics);//总超标盘数加1
    }

    /**
     * 水稳拌合站超标检测材料超标判断
     *
     * @param bhzSwBases
     * @param bhzSwCailiaos
     * @param bhzChaobiaoCfgs
     * @param is_call
     * @param shebeibianhao
     * @param guid
     * @param zongchanliang
     * @return
     */
    public Map JudgeMaterial(BhzSwBases bhzSwBases, List<BhzSwCailiao> bhzSwCailiaos, List<BhzChaobiaoCfg> bhzChaobiaoCfgs, Integer is_call, String shebeibianhao, String guid, float zongchanliang) {
        Map map = new HashMap();
        int final_over_level = 0;
        StringBuilder final_content = new StringBuilder();
        float primary = 1.0f;
        float middle = 3.0f;
        float advanced = 5.0f;
        //实际用量
        double realnum = 0;
        //理论用量
        float theorynum = 0;
        Double GuNum = 0.0; //骨料总重
        // 材料类别
        int cailiaoleixing = 0;
        //理论总重量
        double lilunzongliang = 0;
        String cailiaoming = "";
        float calzongchangliang = 0;
        for (BhzSwCailiao detail : bhzSwCailiaos) {
            if (null != detail.getShijiyongliang()) {
                if (null != detail.getCailiaoleixing() && detail.getCailiaoleixing() < 5) {
                    GuNum = GuNum + detail.getShijiyongliang();
                }
                calzongchangliang += detail.getShijiyongliang();
            }
            if (null != detail.getTheoryNumber()) {
                lilunzongliang += detail.getTheoryNumber();
            }
        }
        //未采集到则计算总产量
        if (zongchanliang == 0 && calzongchangliang > 0) {
            bhzswBasesService.updateswbhzongliang(guid, calzongchangliang);
        }
        if (GuNum > 0) {
            GuNum = GuNum;
        }
        //循环子表
        for (BhzSwCailiao detail1 : bhzSwCailiaos) {
            if (null != detail1.getShijiyongliang()) {
                realnum = detail1.getShijiyongliang();
            }
            if (null != detail1.getCailiaoleixing()) {
                // 材料类型
                cailiaoleixing = detail1.getCailiaoleixing();
            }
            if (null != detail1.getCailiaoming()) {
                cailiaoming = detail1.getCailiaoming();
            }
            float lilunpb = 0f;
            if (null != detail1.getLilunpb()) {
                lilunpb = Float.valueOf(detail1.getLilunpb());
            }

            // 创建一个实体用于修改每个表的
            BhzSwCailiao swCailiao = new BhzSwCailiao();

            float shijipb = 0f;
            if (null != detail1.getShijipb() && StrUtil.isNotBlank(detail1.getShijipb())) {
                shijipb = Convert.toFloat(detail1.getShijipb(), 0f);
            }
            if (shijipb == 0 && GuNum > 0) {
                shijipb = Convert.toFloat(cn.hutool.core.util.NumberUtil.round(realnum / GuNum * 100, 2));
                swCailiao.setShijipb(String.valueOf(shijipb));
            }
            if (lilunpb == 0) {
                BhzSwRecipe lilun = null;
                BhzSwPhbZibiao lilun2 = null;
                lilun2 = bhzSwPhbZibiaoService.selectTianjiaji(shebeibianhao, bhzSwBases.getFormulaNo(), cailiaoming);
                if (null == lilun2) {
                    BhzSwPhbZibiao bhzSwPhbZibiao = bhzSwPhbZibiaoService.selectTianjiajis(shebeibianhao, cailiaoming);
                    if (null == bhzSwPhbZibiao) {
                        log.info("水稳拌和站超标统计理论配比子表与主表的材料名：" + cailiaoming + "无匹配数据：" + bhzSwBases.getId());
                    } else {
                        lilunpb = Convert.toFloat(bhzSwPhbZibiao.getTianjiaji());
                    }
                } else {
                    lilunpb = Convert.toFloat(lilun2.getTianjiaji());
                }
            }
            String formulaNo = bhzSwBases.getFormulaNo();
            List<BhzSwJipei> list = getshaifenshiyan(shebeibianhao, detail1.getCailiaoleixing(),formulaNo);
            if (list.size() > 0) {
                double hechengjipei = 0.0;
                for (BhzSwJipei bhzSwJipei : list) {
                    BhzSwJipeiStatistics bhzSwJipeiStatistics = new BhzSwJipeiStatistics();
                    bhzSwJipeiStatistics.setZhongzhi(bhzSwJipei.getZhongzhi());
                    bhzSwJipeiStatistics.setXiaxian(bhzSwJipei.getXiaxian());
                    bhzSwJipeiStatistics.setShangxian(bhzSwJipei.getShangxian());
                    bhzSwJipeiStatistics.setShaikong(bhzSwJipei.getShaikong());
                    bhzSwJipeiStatistics.setBaseid(detail1.getBaseGuid());
                    bhzSwJipeiStatistics.setSbjno(shebeibianhao);
                    bhzSwJipeiStatistics.setChuliaoshijian(bhzSwBases.getChuliaoshijian());
                    BhzSwJipeiStatistics bhzSwJipeiStatistics1 = bhzSwJipeiStatisticsService.selectone(detail1.getBaseGuid(), shebeibianhao, bhzSwJipei.getShaikong());
                    if (bhzSwJipeiStatistics1 != null) {
                        hechengjipei = bhzSwJipeiStatistics1.getHechengjipei() + bhzSwJipei.getHechengjipei() * shijipb / 100;
                        if (hechengjipei > 100) {
                            hechengjipei = 100.00;
                        }
                        bhzSwJipeiStatistics.setHechengjipei(hechengjipei);
                        bhzSwJipeiStatistics.setId(bhzSwJipeiStatistics1.getId());
                        bhzSwJipeiStatistics.setUpdatetime(new Date());
                        bhzSwJipeiStatisticsService.updateById(bhzSwJipeiStatistics);
                    } else {
                        hechengjipei = bhzSwJipei.getHechengjipei() * shijipb / 100;
                        bhzSwJipeiStatistics.setHechengjipei(hechengjipei);
                        bhzSwJipeiStatistics.setCreatetime(new Date());
                        bhzSwJipeiStatisticsService.save(bhzSwJipeiStatistics);
                    }
                }
            }
            float overnum = 0;
            float chaobiao = 0;
            float chaobiao1 = 0f;
            if (shijipb > 0 && lilunpb > 0) {
                overnum = shijipb - lilunpb;
                theorynum = zongchanliang * lilunpb / 100;
                swCailiao.setTheoryNumber(Double.valueOf(theorynum));
                swCailiao.setLilunpb(Float.toString(lilunpb));
                swCailiao.setWucha(Double.valueOf(overnum));
                chaobiao = (shijipb - lilunpb) / lilunpb * 100;
                chaobiao1 = Convert.toFloat(cn.hutool.core.util.NumberUtil.round(chaobiao, 2));
                swCailiao.setChaobiao(Double.valueOf(chaobiao1));
            }
            BhzChaobiaoCfg bhzChaobiaoCfg = new BhzChaobiaoCfg();
            if (null != bhzChaobiaoCfgs) {
                //此循环是获取和当前材料类型相匹配的材料类型
                for (BhzChaobiaoCfg bhzChaobiaoCf : bhzChaobiaoCfgs) {
                    Integer materialType = bhzChaobiaoCf.getMaterialType();
                    if (null == materialType || materialType != cailiaoleixing) {
                        continue;
                    } else if (materialType == cailiaoleixing) {
                        bhzChaobiaoCfg = bhzChaobiaoCf;
                    }
                }
            }
            if (null != bhzChaobiaoCfg) {
                Object primary1 = bhzChaobiaoCfg.getRimary();
                Object middle1 = bhzChaobiaoCfg.getMiddle();
                Object advanced1 = bhzChaobiaoCfg.getAdvanced();
                if (null != primary1) {
                    primary = bhzChaobiaoCfg.getRimary();
                }
                if (null != middle1) {
                    middle = bhzChaobiaoCfg.getMiddle();
                }
                if (null != advanced1) {
                    advanced = bhzChaobiaoCfg.getAdvanced();
                }
            }
            int chaobiaodengji = 0;

            if (overnum < 0) {// 如果小于0就把值变为正数
                overnum = -overnum;
            }
            if (overnum > 0 && overnum <= primary) {
                chaobiaodengji = 0;
            } else if (overnum > primary && overnum <= middle) {
                chaobiaodengji = 1;
            } else if (overnum > middle && overnum <= advanced) {
                chaobiaodengji = 2;
            } else if (overnum > advanced) {
                chaobiaodengji = 3;
            }

            if (chaobiaodengji > final_over_level) {
                final_over_level = chaobiaodengji;
            }
            swCailiao.setChaobiaodengji(chaobiaodengji);
            swCailiao.setChujichaobiao(Double.valueOf(primary));
            swCailiao.setZhongjichaobiao(Double.valueOf(middle));
            swCailiao.setGaojichaobiao(Double.valueOf(advanced));
            swCailiao.setId(detail1.getId());
            bhzSwCailiaoService.updateById(swCailiao);
            if (chaobiaodengji == 0) {
                continue;
            }
            Double wucha = swCailiao.getWucha();
            final_content.append(String.format("%1$s超%2$.2f", cailiaoming, wucha));
        }
        map.put("level", final_over_level);
        map.put("guliao", GuNum);
        map.put("final_content", final_content.toString());
        return map;
    }

    /**
     * 水稳拌合站统计信息
     *
     * @param swBases
     * @param selectswbhzcailiao
     * @param level
     * @param shebeibianhao
     */
    public void swSattistics(BhzSwBases swBases, List<BhzSwCailiao> selectswbhzcailiao, int level, String shebeibianhao) {
        Double zongchanliang = swBases.getZongchanliang();
        BhzSwStatistics swStatistics = new BhzSwStatistics();
        int SattisticsId = 0;

        String chuliaoshijian = swBases.getChuliaoshijian();
        Date datanyr1 = NumberUtil.datanyr(chuliaoshijian);//格式化后的时间
        BhzSwStatistics selectone = bhzSwStatisticsService.selectone(datanyr1,shebeibianhao);
        if (null != selectone) {
            swcount(selectone, level);
            SattisticsId = selectone.getId();
            Double estimateNumber = selectone.getEstimateNumber();
            swupdateestimatenumber(SattisticsId, zongchanliang, estimateNumber);//方量累加
        } else {
            addswsattistics(swBases);
            BhzSwStatistics selectone1 = bhzSwStatisticsService.selectone(datanyr1,shebeibianhao);
            swcount(selectone1, level);
            SattisticsId = selectone1.getId();
            Double estimateNumber = selectone1.getEstimateNumber();
            swupdateestimatenumber(SattisticsId, zongchanliang, estimateNumber);//方量累加
        }
        swdetailStatistics(selectswbhzcailiao, shebeibianhao, SattisticsId);
        /**
         * 料仓库存统计未写
         */
    }

    /**
     * 水稳统计材料表信息
     * 材料的初中高超标统计，每种类型的材料的理论用量、实际用量累加，
     *
     * @param selectswbhzcailiao
     * @param shebeibianhao
     * @param cs_id
     */
    public void swdetailStatistics(List<BhzSwCailiao> selectswbhzcailiao, String shebeibianhao, int cs_id) {
        float realnum = 0;
        float theorynum = 0;
        String cailiaoid = "";// 材料id
        String materiale_name = "";
        for (BhzSwCailiao detail : selectswbhzcailiao) {
            //实际用量
            realnum = Convert.toFloat(detail.getShijiyongliang(), 0f);
            //理论用量
            theorynum = Convert.toFloat(detail.getTheoryNumber(), 0f);
            cailiaoid = detail.getCailiaoid();
            //材料名字
            materiale_name = detail.getCailiaoming();
            if (StrUtil.isBlank(cailiaoid) && StrUtil.isNotBlank(materiale_name)) {
                BhzMaterialCfg selectone = bhzMaterialCfgService.selectone(shebeibianhao, materiale_name);
                if (null == selectone) {
                    BhzMaterialCfg bhzMaterialCfg = new BhzMaterialCfg();
                    bhzMaterialCfg.setBhjno(shebeibianhao);
                    bhzMaterialCfg.setName(materiale_name);
                    bhzMaterialCfg.setMaterialid(cailiaoid);
                    bhzMaterialCfg.setSpecs(materiale_name);
                    bhzMaterialCfgService.save(bhzMaterialCfg);
                } else {
                    cailiaoid = selectone.getMaterialid();
                }
                detail.setCailiaoid(cailiaoid);
                bhzSwCailiaoService.updateById(detail);
            }
            int materiale_type = Convert.toInt(detail.getCailiaoleixing(), 0);
            // 查询材料统计表中是否有这样的数据，取出id，如果有就在这个数据的基础上把真实用量
            // ，理论用量累加到数据库中，如果没有就创建一条数据，取出id ，再把数据累加进去，
            BhzSwCailiaoStatistics selectone1 = bhzSwCailiaoStatisticsService.selectone(cs_id, cailiaoid);
            if (null == selectone1) {
                BhzSwCailiaoStatistics bhzSwCailiaoStatistics = new BhzSwCailiaoStatistics();
                bhzSwCailiaoStatistics.setCsId(cs_id);
                bhzSwCailiaoStatistics.setMaterialeType(materiale_type);
                bhzSwCailiaoStatistics.setMaterialeName(materiale_name);
                bhzSwCailiaoStatistics.setRealityNumber(Double.valueOf(realnum));
                bhzSwCailiaoStatistics.setTheoryNumber(Double.valueOf(theorynum));
                bhzSwCailiaoStatistics.setCailiaoid(cailiaoid);
                bhzSwCailiaoStatisticsService.save(bhzSwCailiaoStatistics);
                selectone1 = bhzSwCailiaoStatisticsService.selectone(cs_id, cailiaoid);
            } else {
                bhzSwCailiaoStatisticsService.AddReality_numberOne(selectone1.getId(), realnum);
                bhzSwCailiaoStatisticsService.AddTheory_numberOne(selectone1.getId(), theorynum);
            }

            int chaobiaodengji = detail.getChaobiaodengji();//在主方法中已经计算过超标等级
            // 初级超标
            Integer id = selectone1.getId();
            if (chaobiaodengji == 1) {
                bhzSwCailiaoStatisticsService.AddPrimary_numberOne(id);
                // 中级超标
            } else if (chaobiaodengji == 2) {
                bhzSwCailiaoStatisticsService.AddMiddle_numberOne(id);
                // 高级超标
            } else if (chaobiaodengji == 3) {
                bhzSwCailiaoStatisticsService.AddAdvanced_numberOne(id);
            }
        }
    }

    public String getWbshebeiToken() {
        String code = null;
        String post = HttpRequest.get("http://gpselock.vip:80/user/login?username=" + username + "&password=" + password + "")
                .header("Content-Type", "application/json")
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(post);
        String token = String.valueOf(jsonObject.get("token"));
        Integer codes = (Integer) jsonObject.get("code");
        if (codes == 0) {
            code = token;
            redisUtil.set("token", token, 1800);//token缓存
        }
        return code;
    }

    public String getDeviceToken() {
        String username = "h190425hzgx";
        String password = "h190425hzgx";
        String code = null;
        String post = HttpRequest.get("http://www.0531yun.com/api/getToken?loginName=" + username + "&password=" + password)
                .header("Content-Type", "application/json")
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(post);
        JSONObject data1 = jsonObject.getJSONObject("data");
        String token = String.valueOf(data1.get("token"));
        Integer codes = (Integer) jsonObject.get("code");
        if (codes == 1000) {
            code = token;
        }
        return code;
    }

    /**
     * 试验机统计信息
     *
     * @param selectsyjone
     * @param shebeiInfo
     * @param pdjg
     * @param shebeiNo
     */
    public synchronized void SyjSatistics(TSyjzb selectsyjone, ShebeiInfo shebeiInfo, String pdjg, String shebeiNo) {

        TSyjzbStatistics tSyjzbStatistics = new TSyjzbStatistics();

        Date datanyr = NumberUtil.datanyr(selectsyjone.getSyrq());//格式化后的时间
//        String stringnyr = NumberUtil.Stringnyr(datanyr);
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        String sjqd = formatter.format(stringnyr);
        String sbbh = selectsyjone.getSbbh();
        String sylx = selectsyjone.getSylx();
        String wtbh = selectsyjone.getWtbh();
        String sjqd = selectsyjone.getSjqd();


        TSyjzbStatistics selectLimit = syjzbStatisticsService.selectLimit(sbbh, sylx, wtbh, sjqd, datanyr);
        if (null != selectLimit) {
            log.info(String.format("试验机统计累加获取统计数据" + selectLimit.toString()));
            syjCount(selectLimit, pdjg);
        } else {
            tSyjzbStatistics.setSbbh(sbbh);
            tSyjzbStatistics.setSylx(sylx);
            tSyjzbStatistics.setWtbh(wtbh);
            tSyjzbStatistics.setSjqd(sjqd);
            tSyjzbStatistics.setSyrq(datanyr);
            tSyjzbStatistics.setSysOrgCode(shebeiInfo.getSysOrgCode());
            if (pdjg.equals("合格")) {
                tSyjzbStatistics.setAllDish(1);
                tSyjzbStatistics.setAllOverproofDish(0);
            } else if (pdjg.equals("有效")) {
                tSyjzbStatistics.setAllDish(1);
                tSyjzbStatistics.setAllOverproofDish(0);
            } else {
                tSyjzbStatistics.setAllDish(1);
                tSyjzbStatistics.setAllOverproofDish(1);
            }
            boolean save = syjzbStatisticsService.save(tSyjzbStatistics);
            if (save) {
                log.info(String.format("试验机统计新添加数据" + tSyjzbStatistics.toString()));
            }

        }
    }

    private void syjCount(TSyjzbStatistics selectLimit, String pdjg) {
        int id = selectLimit.getId();
        Integer allDish = selectLimit.getAllDish();
        Integer allOverproofDish = selectLimit.getAllOverproofDish();
        Integer allsums = 0;
        Integer allOversums = 0;
        if (pdjg.equals("合格")) {
            allsums = allDish + 1;
            allOversums = allOverproofDish;
        } else if (pdjg.equals("有效")) {
            allsums = allDish + 1;
            allOversums = allOverproofDish;
        } else {
            allsums = allDish + 1;
            allOversums = allOverproofDish + 1;
        }
        boolean b = syjzbStatisticsService.updatestatisticsone(id, allsums, allOversums);
        if (b) {
            log.info(String.format("试验机统计累加成功！"));
        } else {
            log.info(String.format("试验机统计累加失败！"));
        }
    }

    public synchronized void sattistics(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne, ShebeiInfo selectshebeione) {
        int pilecount = 0;//桩基数（根）
        int chaobiaototal = 0;//超标数
        double worklength = 0.0;//施工总长度（m）
        double allcement = 0.0;//水泥总用量（kg）
        double allbeton = 0.0;//水泥浆总量
        try {
            Integer overLevel = deviceMixpileHistorydataOne.getChaobiaodengji();
            String pileTime = deviceMixpileHistorydataOne.getPileTime();
            Date datanyr = NumberUtil.datanyr(pileTime);//格式化后的时间
            String datanyr1 = NumberUtil.Stringnyr(datanyr);//格式化后的时间
            DeviceMixpileStatic deviceMixpileStatic = null;

            if (!StrUtil.isBlank(deviceMixpileHistorydataOne.getPileMileage())) {
                deviceMixpileStatic = deviceMixpileStaticService.selectones(datanyr1, deviceMixpileHistorydataOne.getShebeino(), deviceMixpileHistorydataOne.getPileMileage());
            } else {
                deviceMixpileStatic = deviceMixpileStaticService.selectone(datanyr1, deviceMixpileHistorydataOne.getShebeino());
            }
            DeviceMixpileStatic deviceMixpileStatic1 = new DeviceMixpileStatic();
            deviceMixpileStatic1.setShebeino(deviceMixpileHistorydataOne.getShebeino());
            if (selectshebeione.getSbtype() == 16) {
                deviceMixpileStatic1.setPiletype(0);
            } else if (selectshebeione.getSbtype() == 19) {
                deviceMixpileStatic1.setPiletype(1);
            } else if (selectshebeione.getSbtype() == 53) {
                deviceMixpileStatic1.setPiletype(2);
            } else if (selectshebeione.getSbtype() == 54) {
                deviceMixpileStatic1.setPiletype(3);
            }
            deviceMixpileStatic1.setMileage(deviceMixpileHistorydataOne.getPileMileage());
            deviceMixpileStatic1.setStadate(datanyr1);
            deviceMixpileStatic1.setOrgcode(selectshebeione.getSysOrgCode());
            if (deviceMixpileStatic == null) {
                if (!StrUtil.isBlank(deviceMixpileHistorydataOne.getPileRealdep())) {
                    worklength = Double.parseDouble(deviceMixpileHistorydataOne.getPileRealdep());
                }
                if (!StrUtil.isBlank(deviceMixpileHistorydataOne.getPileCement())) {
                    allcement = Double.parseDouble(deviceMixpileHistorydataOne.getPileCement());
                }
                if (!StrUtil.isBlank(deviceMixpileHistorydataOne.getPileDownbeton()) && !StrUtil.isBlank(deviceMixpileHistorydataOne.getPileUobeton())) {
                    allbeton = Double.parseDouble(deviceMixpileHistorydataOne.getPileDownbeton()) +
                            Double.parseDouble(deviceMixpileHistorydataOne.getPileUobeton());
                } else if (!StrUtil.isBlank(deviceMixpileHistorydataOne.getPileDownbeton())) {
                    allbeton = Double.parseDouble(deviceMixpileHistorydataOne.getPileDownbeton());
                } else if (!StrUtil.isBlank(deviceMixpileHistorydataOne.getPileUobeton())) {
                    allbeton = Double.parseDouble(deviceMixpileHistorydataOne.getPileUobeton());
                }
                pilecount += 1;
                if (overLevel == 1) {
                    chaobiaototal += 1;
                }
                deviceMixpileStatic1.setPilecount(String.valueOf(pilecount));
                deviceMixpileStatic1.setChaobiaototal(chaobiaototal);
                deviceMixpileStatic1.setWorklength(String.format("%.3f", worklength));
                deviceMixpileStatic1.setAllbeton(String.format("%.3f", allbeton));
                deviceMixpileStatic1.setAllcement(String.format("%.3f", allcement));
                deviceMixpileStaticService.save(deviceMixpileStatic1);
            } else {
                pilecount = Integer.parseInt(deviceMixpileStatic.getPilecount()) + 1;
                if (overLevel == 1) {
                    chaobiaototal = deviceMixpileStatic.getChaobiaototal() + 1;
                }
                deviceMixpileStatic1.setPilecount(String.valueOf(pilecount));
                deviceMixpileStatic1.setChaobiaototal(chaobiaototal);
                if (!StrUtil.isBlank(deviceMixpileHistorydataOne.getPileRealdep())) {
                    worklength = Double.parseDouble(deviceMixpileHistorydataOne.getPileRealdep()) + Double.parseDouble(deviceMixpileStatic.getWorklength());
                }
                if (!StrUtil.isBlank(deviceMixpileHistorydataOne.getPileCement())) {
                    allcement = Double.parseDouble(deviceMixpileHistorydataOne.getPileCement()) + Double.parseDouble(deviceMixpileStatic.getAllcement());
                }
                if (!StrUtil.isBlank(deviceMixpileHistorydataOne.getPileDownbeton()) && !StrUtil.isBlank(deviceMixpileHistorydataOne.getPileUobeton())) {
                    allbeton = Double.parseDouble(deviceMixpileHistorydataOne.getPileDownbeton()) +
                            Double.parseDouble(deviceMixpileHistorydataOne.getPileUobeton()) + Double.parseDouble(deviceMixpileStatic.getAllbeton());
                } else if (!StrUtil.isBlank(deviceMixpileHistorydataOne.getPileDownbeton())) {
                    allbeton = Double.parseDouble(deviceMixpileHistorydataOne.getPileDownbeton()) + Double.parseDouble(deviceMixpileStatic.getAllbeton());
                } else if (!StrUtil.isBlank(deviceMixpileHistorydataOne.getPileUobeton())) {
                    allbeton = Double.parseDouble(deviceMixpileHistorydataOne.getPileUobeton()) + Double.parseDouble(deviceMixpileStatic.getAllbeton());
                }
                deviceMixpileStatic1.setId(deviceMixpileStatic.getId());
                deviceMixpileStatic1.setWorklength(String.format("%.3f", worklength));
                deviceMixpileStatic1.setAllbeton(String.format("%.3f", allbeton));
                deviceMixpileStatic1.setAllcement(String.format("%.3f", allcement));
                deviceMixpileStaticService.updateById(deviceMixpileStatic1);
                DeviceMixpileHistorydataOne deviceMixpileHistorydataOne1 = new DeviceMixpileHistorydataOne();
                deviceMixpileHistorydataOne1.setId(deviceMixpileHistorydataOne.getId());
                deviceMixpileHistorydataOne1.setIstongji(1);
                deviceMixpileHistorydataOneService.updateById(deviceMixpileHistorydataOne1);
            }
        } catch (Exception e) {
            DeviceMixpileHistorydataOne deviceMixpileHistorydataOne1 = new DeviceMixpileHistorydataOne();
            deviceMixpileHistorydataOne1.setId(deviceMixpileHistorydataOne.getId());
            deviceMixpileHistorydataOne1.setIstongji(40);
            deviceMixpileHistorydataOneService.updateById(deviceMixpileHistorydataOne1);

        }
    }

    // 管桩统计
    public synchronized void gzSattistics(DevicePipepileHistorydataOne devicePipepileHistorydataOne, ShebeiInfo selectshebeione) {
        int pilecount = 0;//桩基数（根）
        int chaobiaototal = 0;//超标数
        double worklength = 0.0;//施工总长度（m）

        String pileRealdep = devicePipepileHistorydataOne.getPileRealdep();
        Integer pileDesigndep = devicePipepileHistorydataOne.getPileDesigndep();
        float i = 0;
        if (pileRealdep != null){
            if (Float.parseFloat(pileRealdep) < (float) pileDesigndep) {
                i = Math.abs(Float.parseFloat(pileRealdep) - (float) pileDesigndep);
            }
            int i1 = 0;
            if (!StrUtil.isBlank(devicePipepileHistorydataOne.getPileY())) {
                i1 = (int) Float.parseFloat(devicePipepileHistorydataOne.getPileY());
            } else {
                log.info("该管桩数据没有倾角" + DateUtils.now());
            }

            String pileTime = devicePipepileHistorydataOne.getPileTime();
            Date datanyr = NumberUtil.datanyr(pileTime);//格式化后的时间
            String datanyr1 = NumberUtil.Stringnyr(datanyr);//格式化后的时间

            DevicePipepileStatistics pipepileStatistics = null;
            try {
                if (!StrUtil.isBlank(devicePipepileHistorydataOne.getPileMileage())) {
                    pipepileStatistics = pipepileStatisticsService.selectones(datanyr1, devicePipepileHistorydataOne.getShebeino(), devicePipepileHistorydataOne.getPileMileage());
                } else {
                    pipepileStatistics = pipepileStatisticsService.selectone(datanyr1, devicePipepileHistorydataOne.getShebeino());
                }
                // 管桩统计表
                DevicePipepileStatistics pipepileStatistics1 = new DevicePipepileStatistics();
                pipepileStatistics1.setOrgcode(selectshebeione.getSysOrgCode());
                pipepileStatistics1.setMileage(devicePipepileHistorydataOne.getPileMileage());
                pipepileStatistics1.setShebeino(devicePipepileHistorydataOne.getShebeino());
                pipepileStatistics1.setStadate(datanyr1);
                if (pipepileStatistics == null) {
                    if (!StrUtil.isBlank(devicePipepileHistorydataOne.getPileRealdep())) {
                        worklength = Double.parseDouble(devicePipepileHistorydataOne.getPileRealdep());
                    }
                    PipepileYujing pipepileYujing = pipepileYujingService.selectones(devicePipepileHistorydataOne.getShebeino());
                    pilecount += 1;
                    if (pipepileYujing == null) {
                        if (i > 1 || i1 > 1 || i1 < -1) {
                            if (i > 1) {
                                devicePipepileHistorydataOne.setYcyy("设计桩长："+pileDesigndep+"，当前桩长："+pileRealdep);
                                pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                            } else if (i1 > 1 || i1 < -1) {
                                devicePipepileHistorydataOne.setYcyy("倾角异常");
                                pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                            }
                            chaobiaototal += 1;
                            devicePipepileHistorydataOne.setChaobiaodengji(1);
                            pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                        }
                    } else {
                        if (pipepileYujing.getPileRealdep() != null && pipepileYujing.getPileY() != null && pipepileYujing.getPileYx() != null) {
                            int i2 = Math.abs(Integer.parseInt(pileRealdep) - pipepileYujing.getPileRealdep());
                            if (i2 > 1 || i1 < pipepileYujing.getPileYx() || i1 > pipepileYujing.getPileY()) {
                                if (i2 > 1) {
                                    devicePipepileHistorydataOne.setYcyy("设计桩长："+pileDesigndep+"，当前桩长："+pileRealdep);
                                    pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                                } else if (i1 < pipepileYujing.getPileYx() || i1 > pipepileYujing.getPileY()) {
                                    devicePipepileHistorydataOne.setYcyy("倾角异常");
                                    pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                                }
                                chaobiaototal += 1;
                                devicePipepileHistorydataOne.setChaobiaodengji(1);
                                pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                            }
                        } else if (pipepileYujing.getPileRealdep() == null && pipepileYujing.getPileY() != null && pipepileYujing.getPileYx() != null) {
                            if (i > 1 || i1 < pipepileYujing.getPileYx() || i1 > pipepileYujing.getPileY()) {
                                if (i > 1) {
                                    devicePipepileHistorydataOne.setYcyy("设计桩长："+pileDesigndep+"，当前桩长："+pileRealdep);
                                    pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                                } else if (i1 < pipepileYujing.getPileYx() || i1 > pipepileYujing.getPileY()) {
                                    devicePipepileHistorydataOne.setYcyy("倾角异常");
                                    pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                                }
                                chaobiaototal += 1;
                                devicePipepileHistorydataOne.setChaobiaodengji(1);
                                pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                            }
                        } else if (pipepileYujing.getPileRealdep() != null && pipepileYujing.getPileY() == null && pipepileYujing.getPileYx() == null) {
                            int i2 = Math.abs(Integer.parseInt(pileRealdep) - pipepileYujing.getPileRealdep());
                            if (i2 > 1 || i1 < -1 || i1 > 1) {
                                if (i2 > 1) {
                                    devicePipepileHistorydataOne.setYcyy("设计桩长："+pileDesigndep+"，当前桩长："+pileRealdep);
                                    pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                                } else if (i1 < -1 || i1 > 1) {
                                    devicePipepileHistorydataOne.setYcyy("倾角异常");
                                    pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                                }
                                chaobiaototal += 1;
                                devicePipepileHistorydataOne.setChaobiaodengji(1);
                                pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                            }
                        }
                    }
                    pipepileStatistics1.setPilecount(String.valueOf(pilecount));
                    pipepileStatistics1.setChaobiaototal(chaobiaototal);
                    pipepileStatistics1.setWorklength(String.format("%.3f", worklength));
                    pipepileStatisticsService.save(pipepileStatistics1);
                } else {
                    pilecount = Integer.parseInt(pipepileStatistics.getPilecount()) + 1;
                    if (!StrUtil.isBlank(devicePipepileHistorydataOne.getPileRealdep())) {
                        worklength = Double.parseDouble(devicePipepileHistorydataOne.getPileRealdep()) + Double.parseDouble(pipepileStatistics.getWorklength());
                    }
                    PipepileYujing pipepileYujing = pipepileYujingService.selectones(devicePipepileHistorydataOne.getShebeino());
                    if (pipepileYujing == null) {
                        if (i > 1 || i1 < -1 || i1 > 1) {
                            if (i > 1) {
                                devicePipepileHistorydataOne.setYcyy("设计桩长："+pileDesigndep+"，当前桩长："+pileRealdep);
                                pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                            } else if (i1 < -1 || i1 > 1) {
                                devicePipepileHistorydataOne.setYcyy("倾角异常");
                                pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                            }
                            chaobiaototal += 1;
                            devicePipepileHistorydataOne.setChaobiaodengji(1);
                            pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                        }
                    } else {
                        if (pipepileYujing.getPileRealdep() != null && pipepileYujing.getPileY() != null && pipepileYujing.getPileYx() != null) {
                            int i2 = Math.abs(Integer.parseInt(pileRealdep) - pipepileYujing.getPileRealdep());
                            if (i2 > 1 || i1 < pipepileYujing.getPileYx() || i1 > pipepileYujing.getPileY()) {
                                if (i2 > 1) {
                                    devicePipepileHistorydataOne.setYcyy("设计桩长："+pileDesigndep+"，当前桩长："+pileRealdep);
                                    pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                                } else if (i1 < pipepileYujing.getPileYx() || i1 > pipepileYujing.getPileY()) {
                                    devicePipepileHistorydataOne.setYcyy("倾角异常");
                                    pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                                }
                                chaobiaototal += 1;
                                devicePipepileHistorydataOne.setChaobiaodengji(1);
                                pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                            }
                        } else if (pipepileYujing.getPileRealdep() == null && pipepileYujing.getPileY() != null && pipepileYujing.getPileYx() != null) {
                            if (i > 1 || i1 < pipepileYujing.getPileYx() || i1 > pipepileYujing.getPileY()) {
                                if (i > 1) {
                                    devicePipepileHistorydataOne.setYcyy("设计桩长："+pileDesigndep+"，当前桩长："+pileRealdep);
                                    pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                                } else if (i1 < pipepileYujing.getPileYx() || i1 > pipepileYujing.getPileY()) {
                                    devicePipepileHistorydataOne.setYcyy("倾角异常");
                                    pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                                }
                                chaobiaototal += 1;
                                devicePipepileHistorydataOne.setChaobiaodengji(1);
                                pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                            }
                        } else if (pipepileYujing.getPileRealdep() != null && pipepileYujing.getPileY() == null && pipepileYujing.getPileYx() == null) {
                            int i2 = Math.abs(Integer.parseInt(pileRealdep) - pipepileYujing.getPileRealdep());
                            if (i2 > 1 || i1 < -1 || i1 > 1) {
                                if (i2 > 1) {
                                    devicePipepileHistorydataOne.setYcyy("设计桩长："+pileDesigndep+"，当前桩长："+pileRealdep);
                                    pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                                } else if (i1 < -1 || i1 > 1) {
                                    devicePipepileHistorydataOne.setYcyy("倾角异常");
                                    pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                                }
                                chaobiaototal += 1;
                                devicePipepileHistorydataOne.setChaobiaodengji(1);
                                pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
                            }
                        }
                    }
                    pipepileStatistics1.setId(pipepileStatistics.getId());
                    pipepileStatistics1.setPilecount(String.valueOf(pilecount));
                    pipepileStatistics1.setChaobiaototal(chaobiaototal);
                    pipepileStatistics1.setWorklength(String.format("%.3f", worklength));
                    pipepileStatisticsService.updateById(pipepileStatistics1);
                }
            } catch (NumberFormatException e) {
                DevicePipepileHistorydataOne devicePipepileHistorydataOne1 = new DevicePipepileHistorydataOne();
                devicePipepileHistorydataOne1.setId(devicePipepileHistorydataOne.getId());
                devicePipepileHistorydataOne1.setIstongji(40);
                pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne1);
                e.printStackTrace();
            }
        }else {
            devicePipepileHistorydataOne.setChaobiaodengji(30);
            devicePipepileHistorydataOne.setIstongji(30);//施工长度为null
            pipepileHistorydataOneService.updateById(devicePipepileHistorydataOne);
        }
    }

    public synchronized int getUpdateStatus(Date time, String id) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        int status = 0;
        int df = differHours(date2Str(time, format), date2Str(date, format));
        if (df < 2) {
            status = 3;
        }
        if (df < 24 && df >= 2) {
            status = 2;
        }
        if (df >= 24) {
            status = 1;
        }
        return status;
    }

    public void getupdate(List<CunliangProcedureConfig> lists, int i) {
        for (CunliangProcedureConfig cunliangProcedureConfig : lists) {
            cunliangProcedureConfig.setSwitchtime(new Date());
            cunliangProcedureConfig.setSwitchsta(i);
            cunliangProcedureConfigService.updateById(cunliangProcedureConfig);
        }
    }

    public int getcode(String token, String s, int switchsta) {
        String url = "http://www.0531yun.com/api/device/setRelay?deviceAddr=" + s + "&opt=" + switchsta + "&relayNo=1";
        String body = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .header("authorization", token)
                .execute()
                .body();
        System.out.println(body);
        JSONObject jsonObject1 = new JSONObject(body);
        Integer codes = (Integer) jsonObject1.get("code");
        if (codes == 1000) {
            return 0;
        }
        return 1;
    }

    private List<BhzSwJipei> getshaifenshiyan(String shebeibianhao, Integer cailiaoid, String formulaNo) {
        List<BhzSwJipei> list = new ArrayList<>();
        BhzSwJipeiFanwei bhzSwJipeiFanwei = bhzSwJipeiFanweiService.selectone(shebeibianhao, formulaNo);
        if (bhzSwJipeiFanwei != null) {
            BhzSwShaifenShiyan bhzSwShaifenShiyan = bhzSwShaifenShiyanService.selectone(shebeibianhao, cailiaoid,bhzSwJipeiFanwei.getUuid());
            if (bhzSwShaifenShiyan != null) {
                BhzSwJipei bhzSwJipei1 = new BhzSwJipei();
                bhzSwJipei1.setShaikong("0.075");
                bhzSwJipei1.setHechengjipei(Double.parseDouble(bhzSwShaifenShiyan.getShaikong0075()));
                bhzSwJipei1.setShangxian(bhzSwJipeiFanwei.getSk0075shangxian());
                bhzSwJipei1.setXiaxian(bhzSwJipeiFanwei.getSk0075xiaxian());
                bhzSwJipei1.setZhongzhi(bhzSwJipeiFanwei.getSk0075zhongxian());
                list.add(bhzSwJipei1);
                BhzSwJipei bhzSwJipei2 = new BhzSwJipei();
                bhzSwJipei2.setShaikong("0.15");
                bhzSwJipei2.setHechengjipei(Double.parseDouble(bhzSwShaifenShiyan.getShaikong015()));
                bhzSwJipei2.setShangxian(bhzSwJipeiFanwei.getSk015shangxian());
                bhzSwJipei2.setXiaxian(bhzSwJipeiFanwei.getSk015xiaxian());
                bhzSwJipei2.setZhongzhi(bhzSwJipeiFanwei.getSk015zhongxian());
                list.add(bhzSwJipei2);
                BhzSwJipei bhzSwJipei3 = new BhzSwJipei();
                bhzSwJipei3.setShaikong("0.3");
                bhzSwJipei3.setHechengjipei(Double.parseDouble(bhzSwShaifenShiyan.getShaikong03()));
                bhzSwJipei3.setShangxian(bhzSwJipeiFanwei.getSk015shangxian());
                bhzSwJipei3.setXiaxian(bhzSwJipeiFanwei.getSk015xiaxian());
                bhzSwJipei3.setZhongzhi(bhzSwJipeiFanwei.getSk015zhongxian());
                list.add(bhzSwJipei3);
                BhzSwJipei bhzSwJipei4 = new BhzSwJipei();
                bhzSwJipei4.setShaikong("0.6");
                bhzSwJipei4.setHechengjipei(Double.parseDouble(bhzSwShaifenShiyan.getShaikong06()));
                bhzSwJipei4.setShangxian(bhzSwJipeiFanwei.getSk06shangxian());
                bhzSwJipei4.setXiaxian(bhzSwJipeiFanwei.getSk06xiaxian());
                bhzSwJipei4.setZhongzhi(bhzSwJipeiFanwei.getSk06zhongxian());
                list.add(bhzSwJipei4);
                BhzSwJipei bhzSwJipei5 = new BhzSwJipei();
                bhzSwJipei5.setShaikong("1.18");
                bhzSwJipei5.setHechengjipei(Double.parseDouble(bhzSwShaifenShiyan.getShaikong118()));
                bhzSwJipei5.setShangxian(bhzSwJipeiFanwei.getSk118shangxian());
                bhzSwJipei5.setXiaxian(bhzSwJipeiFanwei.getSk118xiaxian());
                bhzSwJipei5.setZhongzhi(bhzSwJipeiFanwei.getSk118zhongxian());
                list.add(bhzSwJipei5);
                BhzSwJipei bhzSwJipei6 = new BhzSwJipei();
                bhzSwJipei6.setShaikong("2.36");
                bhzSwJipei6.setHechengjipei(Double.parseDouble(bhzSwShaifenShiyan.getShaikong236()));
                bhzSwJipei6.setShangxian(bhzSwJipeiFanwei.getSk236shangxian());
                bhzSwJipei6.setXiaxian(bhzSwJipeiFanwei.getSk236xiaxian());
                bhzSwJipei6.setZhongzhi(bhzSwJipeiFanwei.getSk236zhongxian());
                list.add(bhzSwJipei6);
                BhzSwJipei bhzSwJipei7 = new BhzSwJipei();
                bhzSwJipei7.setShaikong("4.75");
                bhzSwJipei7.setHechengjipei(Double.parseDouble(bhzSwShaifenShiyan.getShaikong475()));
                bhzSwJipei7.setShangxian(bhzSwJipeiFanwei.getSk475shangxian());
                bhzSwJipei7.setXiaxian(bhzSwJipeiFanwei.getSk475xiaxian());
                bhzSwJipei7.setZhongzhi(bhzSwJipeiFanwei.getSk475zhongxian());
                list.add(bhzSwJipei7);
                BhzSwJipei bhzSwJipei8 = new BhzSwJipei();
                bhzSwJipei8.setShaikong("9.5");
                bhzSwJipei8.setHechengjipei(Double.parseDouble(bhzSwShaifenShiyan.getShaikong95()));
                bhzSwJipei8.setShangxian(bhzSwJipeiFanwei.getSk95shangxian());
                bhzSwJipei8.setXiaxian(bhzSwJipeiFanwei.getSk95xiaxian());
                bhzSwJipei8.setZhongzhi(bhzSwJipeiFanwei.getSk95zhongxian());
                list.add(bhzSwJipei8);
                BhzSwJipei bhzSwJipei9 = new BhzSwJipei();
                bhzSwJipei9.setShaikong("13.2");
                bhzSwJipei9.setHechengjipei(Double.parseDouble(bhzSwShaifenShiyan.getShaikong132()));
                bhzSwJipei9.setShangxian(bhzSwJipeiFanwei.getSk132shangxian());
                bhzSwJipei9.setXiaxian(bhzSwJipeiFanwei.getSk132xiaxian());
                bhzSwJipei9.setZhongzhi(bhzSwJipeiFanwei.getSk132zhongxian());
                list.add(bhzSwJipei9);
                BhzSwJipei bhzSwJipei10 = new BhzSwJipei();
                bhzSwJipei10.setShaikong("16");
                bhzSwJipei10.setHechengjipei(Double.parseDouble(bhzSwShaifenShiyan.getShaikong16()));
                bhzSwJipei10.setShangxian(bhzSwJipeiFanwei.getSk16shangxian());
                bhzSwJipei10.setXiaxian(bhzSwJipeiFanwei.getSk16xiaxian());
                bhzSwJipei10.setZhongzhi(bhzSwJipeiFanwei.getSk16zhongxian());
                list.add(bhzSwJipei10);
                BhzSwJipei bhzSwJipei11 = new BhzSwJipei();
                bhzSwJipei11.setShaikong("19");
                bhzSwJipei11.setHechengjipei(Double.parseDouble(bhzSwShaifenShiyan.getShaikong19()));
                bhzSwJipei11.setShangxian(bhzSwJipeiFanwei.getSk19shangxian());
                bhzSwJipei11.setXiaxian(bhzSwJipeiFanwei.getSk19xiaxian());
                bhzSwJipei11.setZhongzhi(bhzSwJipeiFanwei.getSk19zhongxian());
                list.add(bhzSwJipei11);
                BhzSwJipei bhzSwJipei12 = new BhzSwJipei();
                bhzSwJipei12.setShaikong("26.5");
                bhzSwJipei12.setHechengjipei(Double.parseDouble(bhzSwShaifenShiyan.getShaikong265()));
                bhzSwJipei12.setShangxian(bhzSwJipeiFanwei.getSk265shangxian());
                bhzSwJipei12.setXiaxian(bhzSwJipeiFanwei.getSk265xiaxian());
                bhzSwJipei12.setZhongzhi(bhzSwJipeiFanwei.getSk265zhongxian());
                list.add(bhzSwJipei12);
                BhzSwJipei bhzSwJipei13 = new BhzSwJipei();
                bhzSwJipei13.setShaikong("37.5");
                bhzSwJipei13.setHechengjipei(Double.parseDouble(bhzSwShaifenShiyan.getShaikong375()));
                bhzSwJipei13.setShangxian(bhzSwJipeiFanwei.getSk375shangxian());
                bhzSwJipei13.setXiaxian(bhzSwJipeiFanwei.getSk375xiaxian());
                bhzSwJipei13.setZhongzhi(bhzSwJipeiFanwei.getSk375zhongxian());
                list.add(bhzSwJipei13);
                BhzSwJipei bhzSwJipei14 = new BhzSwJipei();
                bhzSwJipei14.setShaikong("53");
                bhzSwJipei14.setHechengjipei(Double.parseDouble(bhzSwShaifenShiyan.getShaikong53()));
                bhzSwJipei14.setShangxian(bhzSwJipeiFanwei.getSk53shangxian());
                bhzSwJipei14.setXiaxian(bhzSwJipeiFanwei.getSk53xiaxian());
                bhzSwJipei14.setZhongzhi(bhzSwJipeiFanwei.getSk53zhongxian());
                list.add(bhzSwJipei14);
            }
        }
        return list;
    }

    // 开关机短信发送判断
    public void switchMessage(String chuliaoshijian , String shebeibianhao,String sbname) throws ParseException {

        // 开机提醒 --- 查询当前设备 是否存在状态表
        SwitchingmachineStatistics statistics = switchingmachineStatisticsService.getones(shebeibianhao);
        // 开机提醒 --- 查询当前设备 是否存在开关机短信提醒预警号码
        List<SwitchmachinePhone> switchmachinePhone = switchmachinePhoneService.getons(shebeibianhao);
        int iscall = 0;
        String phones = "";
        String phonenum = "";
        if (switchmachinePhone.size() > 0) {
            // 开机提醒 --- 查询当前设备 设置手机号和预警状态
            iscall = switchmachinePhone.get(0).getIsCall();
            phones = switchmachinePhone.get(0).getYujingPhones();
        }
        if (StrUtil.isNotBlank(phones)) {
            BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(phones);
            if (null != bhzPhone) {
                phonenum = bhzPhone.getPhones();
            }
        }
        if (null == statistics) {
            // 状态不为空
            SwitchingmachineStatistics statistics1 = new SwitchingmachineStatistics();
            statistics1.setChuliaoshijian(chuliaoshijian);
            statistics1.setShebeiNo(shebeibianhao);
            statistics1.setStatus(1);
            statistics1.setKaijishijian(chuliaoshijian);
            switchingmachineStatisticsService.save(statistics1);
            if (null != switchmachinePhone) {
                com.alibaba.fastjson.JSONObject obj = new com.alibaba.fastjson.JSONObject();
                obj.put("sbname", sbname);
                obj.put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                obj.put("content", "该设备已开机");
                if (!"".equals(phonenum)) {
                    send(iscall, phonenum, obj, shebeibianhao);
                }
            }
        } else {
            int status = statistics.getStatus();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String chuliaoshijian1 = statistics.getChuliaoshijian();
            Date date = format.parse(chuliaoshijian1);
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, 10);
            date = cal.getTime();
            String format1 = format.format(date);
            Date times = format.parse(format1);//开关机的出料时间加间隔时间（10分钟）
            Date parse = format.parse(chuliaoshijian);//最新一条数据的出料时间

            if (status == 2) {//关机
                com.alibaba.fastjson.JSONObject obj = new com.alibaba.fastjson.JSONObject();
                obj.put("sbname",sbname);
                obj.put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                obj.put("content", "该设备已开机");
                if (parse.after(times)) {//最新一条数据的时间大于开关机表中的时间+上间隔时间那么久发送开机短信
                    statistics.setStatus(1);
                    statistics.setChuliaoshijian(chuliaoshijian);
                    statistics.setKaijishijian(chuliaoshijian);
                    statistics.setJieshushijian("");
                    switchingmachineStatisticsService.updateById(statistics);
                    if (!"".equals(phonenum)) {
                        send(iscall, phonenum, obj, shebeibianhao);
                    }
                }
            }
            if (status == 1) {//开机
                statistics.setChuliaoshijian(chuliaoshijian);
                switchingmachineStatisticsService.updateById(statistics);
            }
        }

    }

    private void send(int iscall, String phonenum, com.alibaba.fastjson.JSONObject obj, String shebeibianhao) {
        SysMessage sysMessage = new SysMessage();
        sysMessage.setEsTitle("拌合站开关机提醒");
        sysMessage.setEsType("1");
        sysMessage.setRemark(shebeibianhao);
        sysMessage.setEsReceiver(phonenum);
        sysMessage.setEsSendNum(0);
        sysMessage.setEsContent(obj.toString());
        if (iscall == 0) {
            sysMessage.setEsSendStatus("0");//推送状态0未推送
        } else {
            sysMessage.setEsSendStatus("-1");//推送状态-1 不需要推送
            sysMessage.setEsResult("不需要推送");
        }
        sysMessage.setCreateTime(new Date());
        sysMessageService.save(sysMessage);
    }

}


