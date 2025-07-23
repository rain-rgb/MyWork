package com.trtm.sy.enumutil;



public class MaterialTypeTest {
//    @Test
//    public  void MaterialTypeTest2() {
//
//        // 遍历输出所有材料类型及对应的代码
//        for (MaterialType materialType : MaterialType.values()) {
//            System.out.println(materialType.name() + ": " + materialType.getCode());
//        }
//
//        // 获取特定材料类型的代码
//        String name = "水泥";
//        MaterialType material = MaterialType.valueOf(name);
//        System.out.println("水泥的代码是：" + material.getCode());
//
//        // 检查某个代码对应的材料类型
//        String code = "SNJ";
//        MaterialType materialType = getMaterialTypeByCode(code);
//        if (materialType != null) {
//            System.out.println("代码 " + code + " 对应的材料类型是：" + materialType.name());
//        } else {
//            System.out.println("找不到代码为 " + code + " 的材料类型");
//        }
//    }

    // 根据代码查找对应的材料类型
    public  MaterialType getMaterialTypeByCode(String code) {
        for (MaterialType materialType : MaterialType.values()) {
            if (materialType.getCode().equals(code)) {
                return materialType;
            }
        }
        return null;
    }
}
