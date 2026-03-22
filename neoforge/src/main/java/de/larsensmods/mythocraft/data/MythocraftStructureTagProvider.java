package de.larsensmods.mythocraft.data;

import de.larsensmods.mythocraft.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.StructureTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class MythocraftStructureTagProvider extends StructureTagsProvider {

    public MythocraftStructureTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, ExistingFileHelper existingFileHelper) {
        super(output, registriesFuture, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        tag(MythocraftStructureTags.IS_LABYRINTH_PORTAL)
                .add(MythocraftStructures.LABYRINTH_PORTAL, MythocraftStructures.LABYRINTH_PORTAL_SANDSTONE);
    }

}
