package com.trtm.iot.util;

import cn.hutool.core.util.StrUtil;

/**
 * \* @author: zml
 * \* Date: 2022/9/27
 * \* Time: 15:56
 * \* Description:
 * \
 */
public class materialeUtil {
    //水泥或沥青
    private static String[] cementstrs = { "水泥","沥青","SN","华新" };
    //粉煤灰
    private static String[] fastrs = { "煤" };
    //矿粉
    private static String[] mpstrs = { "矿", "粉", "KF" };
    //外加剂
    private static String[] adtstrs = { "液", "外", "低标", "高标", "剂","W" };
    //细骨料
    private static String[] finestrs = { "砂", "沙", "细" };
    //水
    private static String[] waterstrs = { "水" };
    //粗骨料
    private static String[] stonestrs = { "粗", "骨料", "料", "石", "mm", "-", "GL", "分子" };
    private static String[] bigstonestrs = { "大", "26.5","31.5","17" };
    private static String[] mstonestrs = { "中", "16","20","11","9.5","13" };
    private static String[] sstonestrs = { "小", "5", "4.75","3" };

    public enum MaterialTypeEnum {
        Fine(1) , BigStone(2) , MediumStone(3), SmallStone(4),Water(5), Cement(6), MineralPowder(7), FlyAsh(8), Additive(9), Other(10);

        private int material_type;
        private MaterialTypeEnum(int material_type) {
            this.material_type = material_type;
        }

        public int getMaterialType() {
            return material_type;
        }
    }

    public static boolean isMatch(String materialname, String[] strs) {
        boolean match = false;
        for (int i = 0; i < strs.length; i++)
        {
            if (materialname.contains(strs[i])) {
                match = true;
                break;
            }
        }
        return match;
    }
    /**
     * 根据材料名字判断材料类型
     */
    public static int lqCailiaotype(String materialname){
        int materialType = MaterialTypeEnum.Other.getMaterialType();
        if (StrUtil.isNotBlank(materialname)){
            if (isMatch(materialname,cementstrs)){
                materialType = MaterialTypeEnum.Cement.getMaterialType();
            }else if (isMatch(materialname,fastrs)){
                materialType = MaterialTypeEnum.FlyAsh.getMaterialType();
            }else if (isMatch(materialname,mpstrs)){
                materialType = MaterialTypeEnum.MineralPowder.getMaterialType();
            }else if (isMatch(materialname,adtstrs)){
                materialType = MaterialTypeEnum.Additive.getMaterialType();
            }else if (isMatch(materialname,finestrs)){
                materialType = MaterialTypeEnum.Fine.getMaterialType();
            }else if (isMatch(materialname,waterstrs)){
                materialType = MaterialTypeEnum.Water.getMaterialType();
            }else if (isMatch(materialname,stonestrs)){
                if (isMatch(materialname,bigstonestrs)){
                    materialType = MaterialTypeEnum.BigStone.getMaterialType();
                }else if (isMatch(materialname,sstonestrs)){
                    materialType = MaterialTypeEnum.SmallStone.getMaterialType();
                }else if (isMatch(materialname,mstonestrs)){
                    materialType = MaterialTypeEnum.MediumStone.getMaterialType();
                }else {
                    materialType = MaterialTypeEnum.MediumStone.getMaterialType();
                }
            }else {
                materialType = MaterialTypeEnum.Other.getMaterialType();
            }
        }
        return materialType;
    }
}
