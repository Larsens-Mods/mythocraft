package de.larsensmods.mythocraft.entity.friendly;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.item.MythItems;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Map;
import java.util.Set;

public record BlacksmithCyclopsRecipe(Map<Item, Integer> ingredients, Map<Item, Integer> outputs, int craftingTicks) {

    public static final BlacksmithCyclopsRecipe HADES_HELMET_RECIPE = new BlacksmithCyclopsRecipe(
            Map.of(
                    Items.IRON_INGOT, 12,
                    Items.NETHERITE_INGOT, 2
            ),
            Map.of(
                    MythItems.HADES_HELM.get(), 1
            ),
            200
    );

    public static final Set<BlacksmithCyclopsRecipe> RECIPES = Set.of(
            HADES_HELMET_RECIPE
    );

    public boolean canUse(InventoryCarrier carrier) {
        SimpleContainer inventory = carrier.getInventory();
        for (Map.Entry<Item, Integer> ingredient : this.ingredients.entrySet()) {
            if (inventory.countItem(ingredient.getKey()) < ingredient.getValue()) {
                return false;
            }
        }
        int requiredOutputSlots = outputs.size();
        int availableOutputSlots = 0;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (inventory.getItem(i).isEmpty()) {
                availableOutputSlots++;
                if (availableOutputSlots >= requiredOutputSlots) {
                    return true;
                }
            }
        }
        return false;
    }

    public void changeItems(InventoryCarrier carrier) {
        SimpleContainer inventory = carrier.getInventory();
        for (Map.Entry<Item, Integer> ingredient : this.ingredients.entrySet()) {
            ItemStack stack = inventory.removeItemType(ingredient.getKey(), ingredient.getValue());
            if (stack.getCount() < ingredient.getValue()) {
                inventory.addItem(stack);
                Constants.LOG.error("Tried to remove more items than available in BlacksmithCyclopsRecipe.changeItems, this should not have happened.");
                return;
            }
        }
        for (Map.Entry<Item, Integer> output : this.outputs.entrySet()) {
            ItemStack outputStack = new ItemStack(output.getKey(), output.getValue());
            if (!inventory.canAddItem(outputStack)) {
                Constants.LOG.error("Tried to add more items than available slots in BlacksmithCyclopsRecipe.changeItems, this should not have happened.");
                return;
            }
            inventory.addItem(outputStack);
        }
    }
}
