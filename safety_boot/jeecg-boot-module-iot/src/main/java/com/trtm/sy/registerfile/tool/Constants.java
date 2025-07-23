package com.trtm.sy.registerfile.tool;

import java.text.SimpleDateFormat;

/**
 * 通用常量信息
 *
 * @author zww
 */
public class Constants
{
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";


    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";
    /**
     * 资源映射路径 前缀
     */
    public static final String PREVIEW_APP = "/previewapp";

    public static final String SEPRATOR = ",";
    public static final String IS_VIRTUAL = "1";
    public static final String NOT_VIRTUAL = "0";

    /**
     * 用于评优
     */
    public static final Integer maxscore = 95;

    /**
     * 用于评良
     */
    public static final Integer minscore = 85;

    /**
     * 用于评中
     */
    public static final Integer zhong = 75;

    /**
     * 月度计划form
     */
    public static final String MonthlyPlan = "MonthlyPlan";

    /**
     * 月进度form
     */
    public static final String MonthProgress = "MonthProgress";

    public static final SimpleDateFormat FORMAT_YMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final String DATE_PATTERNS_COMMON = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_PATTERNS = "yyyy-MM-dd";

    /**
     * 党建工作创建
     */
    public static final String BJL_PARTY_BUILD = "bjl_party_build";

    /**
     * 党支部设置
     */
    public static final String PARTY_BRANCH_SETTING = "party_branch_setting";

    /**
     * 党支部调整
     */
    public static final String PARTY_BRANCH_ADJUSTMENT = "party_branch_adjustment";

    /**
     * 党组织隶属关系
     */
    public static final String PARTY_ORGANIZATION_AFFILIATION = "party_organization_affiliation";

    /**
     * 党员名单
     */
    public static final String PARTY_MEMBER_LIST = "party_member_list";

    /**
     * 党小组划分
     */
    public static final String PARTY_GROUP_DIVISION = "party_group_division";

    /**
     * 支部委员会设立
     */
    public static final String BRANCH_COMMITTEE_ESTABLISHED = "branch_committee_established";

    /**
     * 任期换届
     */
    public static final String TERM_CHANGE = "term_change";

    /**
     * 支委会分工
     */
    public static final String SUB_COMMITTEE_DIVISION_OF_LABOR = "sub_committee_division_of_labor";

    /**
     * 目标管理制度系
     */
    public static final String OBJECTIVE_MANAGEMENT_SYSTEM = "objective_management_system";

    /**
     * 特色工作汇报
     */
    public static final String FEATURED_WORK_REPORT = "featured_work_report";

    /**
     * 议事决策
     */
    public static final String DECISION_MAKING = "decision_making";

    /**
     * 学习培训
     */
    public static final String LEARNING_TRAINING = "learning_training";

    /**
     * 组织生活
     */
    public static final String BJL_ORGANIZE_LIFE = "bjl_organize_life";

    /**
     * 谈心谈话
     */
    public static final String BJL_CONVERSATION = "bjl_conversation";

    /**
     * 民主评议党员
     */
    public static final String BJL_DEMOCRATIC_APPRAISAL = "bjl_democratic_appraisal";

    /**
     * 申请入党名册
     */
    public static final String BJL_APPLY_FOR_MEMBERSHIP = "bjl_apply_for_membership";

    /**
     * 入党积极分子名册
     */
    public static final String BJL_PARTY_ACTIVISTS = "bjl_party_activists";

    /**
     * 入党发展对象名册
     */
    public static final String BJL_DEVELOPMENT_OBJECT = "bjl_development_object";

    /**
     * 党员学习培训
     */
    public static final String BJL_PARTY_TRAIN = "bjl_party_train";

    /**
     * 党费缴纳
     */
    public static final String BJL_PARTY_DUES_PAYMENT = "bjl_party_dues_payment";

    /**
     * 组织关系转接
     */
    public static final String BJL_ORG_TRANSFER = "bjl_org_transfer";

    /**
     * 组织关系转接
     */
    public static final String BJL_PUBLIC_PARTY = "bjl_public_party";

    /**
     * 党内关怀激励
     */
    public static final String BJL_PARTY_CARE = "bjl_party_care";

    /**
     * 党员日常监督
     */
    public static final String BJL_DAILY_SUPERVISION = "bjl_daily_supervision";

    /**
     * 党员设岗定责
     */
    public static final String BJL_FIX_DUTY = "bjl_fix_duty";

    /**
     * 联系服务群众
     */
    public static final String BJL_CONTACT_SERVICE = "bjl_contact_service";

    /**
     * 创新活动方式
     */
    public static final String BJL_BASIC_GUARANTEE = "bjl_basic_guarantee";

    /**
     * 监理
     */
    public static final String JYBJLJL = "JYBJL-JL";
    public static final String JYBJLDYJL = "JYBJL-DYJL";

    /**
     * 施工
     */
    public static final String JYBJLSG = "JYBJL-SG";
    public static final String JYBJLDYSG = "JYBJL-DYSG";

    /**
     * 上级文件（张家港项目）
     */
    public static final String ZHGD_PARTY_MASSES_FILE = "zhgd_party_masses_file";

    /**
     * 党员资料（张家港项目）
     */
    public static final String ZHGD_ZJG_PARTY_MEMBERS = "zhgd_zjg_party_members";

    /**
     * 党员风采（张家港项目）
     */
    public static final String ZHGD_ZJG_PARTY_MEMBER_STYLE = "zhgd_zjg_party_member_style";

    /**
     * 发展党员情况（张家港项目）
     */
    public static final String ZHGD_ZJG_RECRUIT_PARTY = "zhgd_zjg_recruit_party";

    /**
     * 发展党员情况（张家港项目）
     */
    public static final String ZHGD_ZJG_RECRUIT_PARTY_IMG = "zhgd_zjg_recruit_party_img";

    /**
     * 党群活动 封面（张家港项目）
     */
    public static final String ZHGD_ZJG_PARTY_ACTIVITIES_FM = "zhgd_zjg_party_activities_fm";

    /**
     * 党群活动 附件（张家港项目）
     */
    public static final String ZHGD_ZJG_PARTY_ACTIVITIES_FJ = "zhgd_zjg_party_activities_fj";

    /**
     * 廉洁体系（张家港项目）
     */
    public static final String ZHGD_ZJG_INTEGRITY_SYSTEM = "zhgd_zjg_integrity_system";

    /**
     *  字符串0
     */
    public static final String STRING_ZERO = "0";

    /**
     *  数字0
     */
    public static final int NUMBER_ZERO = 0;

    /**
     *  数字1
     */
    public static final int NUMBER_ONE = 1;

    /**
     *  字符串1
     */
    public static final String STRING_ONE = "1";

    /**
     *  数字2
     */
    public static final int NUMBER_TWO = 2;

    /**
     *  数字9
     */
    public static final int NUMBER_NINE = 9;

    /**
     *  数字16
     */
    public static final int NUMBER_SIXTEEN = 16;

    /**
     *  数字21
     */
    public static final int NUMBER_TWENTY_ONE = 21;

    /**
     *  double数字10.0
     */
    public static final double DOUBLE_TEN = 10.0;

    /**
     *  double数字20
     */
    public static final double DOUBLE_TWENTY = 20.0;

    /**
     *  double数字50
     */
    public static final double DOUBLE_FIFTY = 50.0;

    /**
     *  double数字0
     */
    public static final double DOUBLE_ZERO = 0.0;

    /**
     *  double数字0.05
     */
    public static final double DOUBLE_ZERO_ZERO_ONE = 0.05;

    /**
     *  double数字0.10
     */
    public static final double DOUBLE_ZERO_ONE = 0.10;

    /**
     *  double数字0.20
     */
    public static final double DOUBLE_ZERO_TWO = 0.20;

    /**
     *  double数字0.30
     */
    public static final double DOUBLE_ZERO_THREE = 0.30;

    /**
     *  double数字0.40
     */
    public static final double DOUBLE_ZERO_FOUR = 0.40;

    /**
     *  double数字0.50
     */
    public static final double DOUBLE_ZERO_FIVE = 0.50;

    /**
     *  double数字100
     */
    public static final double DOUBLE_ONE_HUNDRED = 100.0;

    /**
     * 安全巡查 视频
     */
    public static final String ZHGD_PATROL_VIDEO = "ZHGD_PATROL_VIDEO";

    /**
     * 潜在事故控制措施管理 图片
     */
    public static final String ZHGD_ACTION_MANAGEMENT = "ZHGD_ACTION_MANAGEMENT";

    /**
     * 未知
     */
    public static final String WEI_ZHI_STRING = "未知";
}

