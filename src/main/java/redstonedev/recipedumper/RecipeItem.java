package redstonedev.recipedumper;

import com.google.gson.JsonElement;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeItem {
    public String id;
    public int count;

    public RecipeItem(String id, int count) {
        this.id = id;
        this.count = count;
    }

    public static RecipeItem fromItemStack(ItemStack stack) {
        return new RecipeItem(Objects.requireNonNull(stack.getItem().getRegistryName()).toString(), stack.getCount());
    }

    public static List<JsonElement> fromIngredients(NonNullList<Ingredient> items) {
        List<JsonElement> ingredients = new ArrayList<>();

        for (Ingredient ingredient : items) {
            ingredients.add(ingredient.serialize());
        }

        return ingredients;
    }
}
