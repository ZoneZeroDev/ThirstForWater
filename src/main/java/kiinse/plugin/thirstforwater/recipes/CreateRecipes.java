package kiinse.plugin.thirstforwater.recipes;

import kiinse.plugin.thirstforwater.ThirstForWater;
import kiinse.plugin.thirstforwater.enums.Config;

public class CreateRecipes {

    public CreateRecipes(ThirstForWater thirstForWater) {
        thirstForWater.sendLog("Creating recipes...");
        if (thirstForWater.getConfiguration().getBoolean(Config.RECIPE_BOTTLE_CLEAN))
            thirstForWater.getRecipes().addCleanBottleRecipes();
        if (thirstForWater.getConfiguration().getBoolean(Config.RECIPE_BOTTLE_SNOW))
            thirstForWater.getRecipes().addBottleRecipes();
        if (thirstForWater.getConfiguration().getBoolean(Config.RECIPE_FILTER))
            thirstForWater.getRecipes().addFilterRecipes();
        thirstForWater.sendLog("Recipes created!");
    }
}
