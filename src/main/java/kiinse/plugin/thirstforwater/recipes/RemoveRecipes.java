package kiinse.plugin.thirstforwater.recipes;

import kiinse.plugin.thirstforwater.ThirstForWater;
import org.jetbrains.annotations.NotNull;

public class RemoveRecipes {

    public RemoveRecipes(@NotNull ThirstForWater thirstForWater) {
        thirstForWater.sendLog("Removing recipes...");
        thirstForWater.getRecipes().removeRecipe();
        thirstForWater.sendLog("Recipes removed");
    }
}
