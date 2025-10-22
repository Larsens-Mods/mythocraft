package de.larsensmods.mythocraft.entity.friendly;

import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum SatyrVariant {
    VARIANT_0(0),
    VARIANT_1(1);

    private static final IntFunction<SatyrVariant> BY_ID = ByIdMap.continuous(SatyrVariant::getId, values(), ByIdMap.OutOfBoundsStrategy.WRAP);

    private final int id;

    SatyrVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static SatyrVariant byId(int id) {
        return BY_ID.apply(id);
    }
}
