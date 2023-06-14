package redstonedev.recipedumper;

import com.google.gson.JsonElement;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class RecipeItem {
    public ResourceLocation id;
    public int count;

    public RecipeItem(ResourceLocation id, int count) {
        this.id = id;
        this.count = count;
    }

    public static RecipeItem fromItemStack(ItemStack stack) {
        ResourceLocation loc = ForgeRegistries.ITEMS.getKey(stack.getItem());

        return new RecipeItem(loc, stack.getCount());
    }

    public static List<JsonElement> fromIngredients(NonNullList<Ingredient> items) {
        List<JsonElement> ingredients = new ArrayList<>();

        for (Ingredient ingredient : items) {
            ingredients.add(ingredient.toJson());
        }

        return ingredients;
    }
}
