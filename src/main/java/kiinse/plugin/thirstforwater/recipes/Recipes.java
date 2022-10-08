package kiinse.plugin.thirstforwater.recipes;

import kiinse.plugin.thirstforwater.enums.Config;
import kiinse.plugin.thirstforwater.enums.File;
import kiinse.plugin.thirstforwater.recipes.interfaces.ThirstRecipes;
import kiinse.plugins.darkwaterapi.api.DarkWaterJavaPlugin;
import kiinse.plugins.darkwaterapi.api.files.filemanager.YamlFile;
import kiinse.plugins.darkwaterapi.api.utilities.ItemStackUtils;
import kiinse.plugins.darkwaterapi.core.utilities.DarkItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Recipes extends YamlFile implements ThirstRecipes {

    private final DarkWaterJavaPlugin plugin;
    private final ItemStackUtils utils;

    public Recipes(@NotNull DarkWaterJavaPlugin plugin) {
        super(plugin, File.ITEMS_YML);
        this.plugin = plugin;
        utils = new DarkItemUtils(plugin);
    }

    @Override
    public @NotNull ItemStack getCleanWater() {
        return Objects.requireNonNull(getItemStack(Config.WATER));
    }

    @Override
    public @NotNull ItemStack getWaterBottle() {
        return utils.getPotionItemStack(null, null, PotionType.WATER, 1);
    }

    @Override
    public @NotNull ItemStack getBottle() {
        return utils.getItemStack(Material.GLASS_BOTTLE, null, null, 1);
    }

    @Override
    public @NotNull ItemStack getSnowBall() {
        return utils.getItemStack(Material.SNOWBALL, null, null, 1);
    }

    @Override
    public @NotNull ItemStack getFilter() {
        return Objects.requireNonNull(getItemStack(Config.FILTER));
    }

    @Override
    public void addFilterRecipes() {
        Bukkit.getServer().addRecipe(getFilterRecipe("filter_coal", getFilter(), new ItemStack(Material.COAL), new ItemStack(Material.PAPER)));
        Bukkit.getServer().addRecipe(getFilterRecipe("filter_charcoal",
                                                     getFilter(),
                                                     new ItemStack(Material.CHARCOAL),
                                                     new ItemStack(Material.PAPER)));
        plugin.sendLog("Filter recipes created");
    }

    @Override
    public void addCleanBottleRecipes() {
        Bukkit.getServer().addRecipe(utils.getShapelessRecipe("clean_water_table", getCleanWater(), getFilter(), getWaterBottle()));
        Bukkit.getServer().addRecipe(utils.getFurnaceRecipe("clear_water_furnace",
                                                            getCleanWater(),
                                                            (float) plugin.getConfiguration().getDouble(Config.COOKING_EXPERIENCE),
                                                            plugin.getConfiguration().getInt(Config.COOKING_TIME)));
        plugin.sendLog("Clear water bottle recipes created");
    }

    @Override
    public void addBottleRecipes() {
        Bukkit.getServer().addRecipe(utils.getShapelessRecipe("water_snow_table", getWaterBottle(), getSnowBall(), getBottle()));
        plugin.sendLog("Water bottle recipes created");
    }

    @Override
    public void removeRecipe() {
        Bukkit.getServer().removeRecipe(Objects.requireNonNull(NamespacedKey.fromString("clear_water_furnace")));
        Bukkit.getServer().removeRecipe(Objects.requireNonNull(NamespacedKey.fromString("clear_water_table")));
        Bukkit.getServer().removeRecipe(Objects.requireNonNull(NamespacedKey.fromString("water_snow_table")));
        Bukkit.getServer().removeRecipe(Objects.requireNonNull(NamespacedKey.fromString("filter_charcoal")));
        Bukkit.getServer().removeRecipe(Objects.requireNonNull(NamespacedKey.fromString("filter_coal")));
    }

    @Override
    public @NotNull ShapedRecipe getFilterRecipe(@NotNull String key, @NotNull ItemStack result, @NotNull ItemStack ingredient1,
                                                 @NotNull ItemStack ingredient2) {
        var recipe = new ShapedRecipe(new NamespacedKey(plugin, key), result);
        recipe.shape("CCC", "PPP");
        recipe.setIngredient('C', new RecipeChoice.ExactChoice(ingredient1));
        recipe.setIngredient('P', new RecipeChoice.ExactChoice(ingredient2));
        return recipe;
    }
}
