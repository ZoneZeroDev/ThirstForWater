package kiinse.plugin.thirstforwater.recipes.interfaces;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public interface ThirstRecipes {

    @NotNull ItemStack getCleanWater();

    @NotNull ItemStack getWaterBottle();

    @NotNull ItemStack getBottle();

    @NotNull ItemStack getSnowBall();

    @NotNull ItemStack getFilter();

    void addFilterRecipes();

    void addCleanBottleRecipes();

    void addBottleRecipes();

    void removeRecipe();

    @NotNull ShapedRecipe getFilterRecipe(@NotNull String key, @NotNull ItemStack result, @NotNull ItemStack ingredient1,
                                          @NotNull ItemStack ingredient2);
}
