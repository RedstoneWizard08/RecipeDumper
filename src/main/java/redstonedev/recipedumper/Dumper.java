package redstonedev.recipedumper;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.resources.DataPackRegistries;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class Dumper {
    public static int dumpRecipes(CommandContext<CommandSource> ctx) {
        Minecraft mc = Minecraft.getInstance();
        IntegratedServer server = Objects.requireNonNull(mc.getIntegratedServer());
        DataPackRegistries registries = server.getDataPackRegistries();
        RecipeManager recipeManager = registries.getRecipeManager();

        List<RecipeInfo> recipeInfos = new ArrayList<>();

        for (IRecipe<?> recipe : recipeManager.getRecipes()) {
            IRecipeType<?> type = recipe.getType();
            ItemStack out = recipe.getRecipeOutput();
            NonNullList<Ingredient> ingredients = recipe.getIngredients();

            RecipeInfo info = new RecipeInfo();

            info.type = type.toString();
            info.output = RecipeItem.fromItemStack(out);
            info.ingredients = RecipeItem.fromIngredients(ingredients);

            recipeInfos.add(info);
        }

        DumpWriter.writeDump(recipeInfos, "recipes");

        ctx.getSource().sendFeedback(new StringTextComponent("Successfully dumped recipes!"), true);

        return 1;
    }

    public static int dumpItemTags(CommandContext<CommandSource> ctx) {
        Set<Map.Entry<RegistryKey<Item>, Item>> items = ForgeRegistries.ITEMS.getEntries();
        Map<ResourceLocation, List<String>> tags = new HashMap<>();

        for (Map.Entry<RegistryKey<Item>, Item> entry : items) {
            Item item = entry.getValue();

            processTags(tags, item.getRegistryName(), item.getTags());
        }

        DumpWriter.writeDump(tags, "tags/items");

        ctx.getSource().sendFeedback(new StringTextComponent("Successfully dumped item tags!"), true);

        return 1;
    }

    public static int dumpBlockTags(CommandContext<CommandSource> ctx) {
        Set<Map.Entry<RegistryKey<Block>, Block>> blocks = ForgeRegistries.BLOCKS.getEntries();
        Map<ResourceLocation, List<String>> tags = new HashMap<>();

        for (Map.Entry<RegistryKey<Block>, Block> entry : blocks) {
            Block block = entry.getValue();

            processTags(tags, block.getRegistryName(), block.getTags());
        }

        DumpWriter.writeDump(tags, "tags/blocks");

        ctx.getSource().sendFeedback(new StringTextComponent("Successfully dumped block tags!"), true);

        return 1;
    }

    public static int dumpFluidTags(CommandContext<CommandSource> ctx) {
        Set<Map.Entry<RegistryKey<Fluid>, Fluid>> fluids = ForgeRegistries.FLUIDS.getEntries();
        Map<ResourceLocation, List<String>> tags = new HashMap<>();

        for (Map.Entry<RegistryKey<Fluid>, Fluid> entry : fluids) {
            Fluid fluid = entry.getValue();

            processTags(tags, fluid.getRegistryName(), fluid.getTags());
        }

        DumpWriter.writeDump(tags, "tags/fluids");

        ctx.getSource().sendFeedback(new StringTextComponent("Successfully dumped fluid tags!"), true);

        return 1;
    }

    public static void processTags(Map<ResourceLocation, List<String>> resourceTags, ResourceLocation registryName, Set<ResourceLocation> tags) {
        ResourceLocation id = Objects.requireNonNull(registryName);

        for (ResourceLocation tag : tags) {
            if (!resourceTags.containsKey(tag))
                resourceTags.put(tag, new ArrayList<>());

            resourceTags.get(tag).add(id.toString());
        }
    }

    public static int dumpAllTags(CommandContext<CommandSource> ctx) {
        dumpItemTags(ctx);
        dumpBlockTags(ctx);
        dumpFluidTags(ctx);

        return 1;
    }
}
