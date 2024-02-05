package redstonedev.recipedumper;

import com.google.gson.JsonElement;

import java.util.List;

public class RecipeInfo {
    public String type;
    public RecipeItem output;
    public List<JsonElement> ingredients;
}
