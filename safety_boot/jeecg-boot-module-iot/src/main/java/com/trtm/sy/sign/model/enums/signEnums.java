package com.trtm.sy.sign.model.enums;



/**
 *
 * @author wcx
 */
public class signEnums {
    public static String baseServiceUrl = "https://openapi.qiyuesuo.cn"; // 替换为实际的【契约锁私有云开放平台】地址
    public static String appToken = "8C3C5Cs04s"; // 【契约锁私有云控制台】创建应用后生成的 AppToken
    public static String appSecret = "T41pDdKcu7Yt6afqJOLBWf1hJOMv3k"; // 【契约锁私有云控制台】创建应用后生成的 AppSecret

    public static String categoryId = "3114135844814213392"; // 用印流程 ID，需要在[契约锁电子签署平台]-[印控]-[用印流程]中新增并启用用印流程。
    public static Long sealId = 3126811685789315860L; // 印章 ID，需要在[契约锁电子签署平台]-[印控]-[电子印章]中制作印章，并进行「自动盖章授权」。
    public static Long waterSealId = 3123453558376997631L; // 水运丙级蓝章印章 ID，需要在[契约锁电子签署平台]-[印控]-[电子印章]中制作印章，并进行「自动盖章授权」。
    public static Long synthesizeSealId = 3123453497320514287L; // 综合乙级蓝章印章 ID，需要在[契约锁电子签署平台]-[印控]-[电子印章]中制作印章，并进行「自动盖章授权」。
    public static Long cmaSealId = 3123453362108736217L; // CMA印章 ID，需要在[契约锁电子签署平台]-[印控]-[电子印章]中制作印章，并进行「自动盖章授权」。
    public static String companyName = "江苏省苏信工程咨询有限公司"; // 组织名称
    public static String createrMobile = "15195880106"; // 创建人手机号
    public static String createrName = "周巍巍"; // 创建人姓名
    public static String leaderMobile = "15957140410"; // 领导手机号
}
