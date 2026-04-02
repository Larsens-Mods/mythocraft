package de.larsensmods.mythocraft.data;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.block.MythBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MythocraftBlockStateProvider extends BlockStateProvider {

    public MythocraftBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(MythBlocks.LABYRINTH_BARRIER_ROCK.get());
    }
}
