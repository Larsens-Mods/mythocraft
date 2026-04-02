package de.larsensmods.mythocraft.mixin;

import com.mojang.serialization.Lifecycle;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = WorldOpenFlows.class)
public class MixinWorldOpenFlows {

    @ModifyVariable(method = "confirmWorldCreation", at = @At("HEAD"), argsOnly = true, index = 4)
    private static boolean mythocraft$skipWarnings(boolean value){
        return true;
    }

    @Redirect(method = "openWorldCheckWorldStemCompatibility", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/WorldData;worldGenSettingsLifecycle()Lcom/mojang/serialization/Lifecycle;"))
    public Lifecycle mythocraft$makeAlwaysStable(WorldData instance){
        return Lifecycle.stable();
    }

}
