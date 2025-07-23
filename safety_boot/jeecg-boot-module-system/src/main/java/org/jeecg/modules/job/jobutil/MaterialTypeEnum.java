package org.jeecg.modules.job.jobutil;

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
