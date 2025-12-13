package de.larsensmods.mythocraft;

import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class MythocraftMod {

    public MythocraftMod() {
        Constants.LOG.info("Hello Forge world!");
        CommonClass.init();
    }
}
