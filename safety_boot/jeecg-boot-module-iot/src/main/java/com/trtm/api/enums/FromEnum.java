package com.trtm.api.enums;

public enum FromEnum implements DataBaseTableEnum{

    JL_JJ0231("JJ0231", "宕渣试验检测记录表", "宕渣试验（一）"),
    BG_JB010246("JB010246", "宕渣试验检测报告", "宕渣试验检测报告"),
    SPB_BZSYSHB("BZSYSHB", "标准试验（综合毛体积密度）审核表", "标准试验审核表"),

    JL_JJ0232("JJ0232", "宕渣试验检测记录表", "宕渣试验（二）"),
    JL_JJ0105A("JJ0105a", "土的CBR试验检测记录表（三）", "承载比(CBR)(三)"),


    JL_JJ1001A("JJ1001a", "钢筋原材试验检测记录表", "钢筋原材试验"),
    JL_JJ1001D("JJ1001d", "钢筋重量偏差试验检测记录表", "钢筋重量偏差试验"),
    BG_JB011001("JB011001", "钢筋原材料试验检测报告", "钢筋原材料报告"),
    BYD_GJYC("BYDGJJ", "进场材料(含永久工程构配件或设备)报验单", "报验单"),

    JL_JJ1001C("JJ1001c", "钢筋机械连接试验检测记录表", "钢筋机械连接试验"),
    BG_JB011003("JB011003", "钢筋机械连接试验检测报告", "钢筋机械连接报告"),
    BYD_GJLJ("BYDGHJ", "进场材料(含永久工程构配件或设备)报验单", "报验单"),

    JL_JJ1001B("JJ1001b", "钢筋接头试验检测记录表", "钢筋接头试验"),
    BG_JB011002("JB011002", "钢筋接头试验检测报告", "钢筋接头报告"),
    BYD_GJHJ("BYDGLJ", "进场材料(含永久工程构配件或设备)报验单", "报验单"),

    JL_JJ1001B_GJ("JJ1001b_GJ", "钢筋接头试验检测记录表", "钢筋接头试验"),
    BG_JB011002_GJ("JB011002_GJ", "钢筋接头试验检测报告", "钢筋接头报告"),
    BYD_GJHJ_FJ("BYD_JGHJ_FJ", "进场材料(含永久工程构配件或设备)报验单", "报验单"),

    JL_JJ1412("JJ1412", "钢筋机械连接试验检测记录表", "钢筋机械连接试验"),
    BG_JB021415("JB021415", "钢筋机械连接试验检测报告", "钢筋机械连接报告"),
    BYD_GJLJ_FJ("BYD_GJLJ_FJ", "进场材料(含永久工程构配件或设备)报验单", "报验单")
    ;

    private String tableNum;

    private String tableName;

    private String projectName;

    FromEnum(String tableNum, String tableName, String projectName) {
        this.tableNum = tableNum;
        this.tableName = tableName;
        this.projectName = projectName;
    }


    @Override
    public String getTableNum() {
        return this.tableNum;
    }

    @Override
    public String getTableName() {
        return this.tableName;
    }

    @Override
    public String getProjectName() {
        return this.projectName;
    }
}
