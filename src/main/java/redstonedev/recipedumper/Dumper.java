package redstonedev.recipedumper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.mojang.brigadier.context.CommandContext;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

public class Dumper {
    public static int dumpRecipes(CommandContext<CommandSourceStack> ctx) {
        MinecraftServer mc = ctx.getSource().getServer();
        RecipeManager recipeManager = mc.getRecipeManager();

        List<RecipeInfo> recipeInfos = new ArrayList<>();

        for (Recipe<?> recipe : recipeManager.getRecipes()) {
            RecipeType<?> type = recipe.getType();
            ItemStack out = recipe.getResultItem(mc.registryAccess());
            NonNullList<Ingredient> ingredients = recipe.getIngredients();

            RecipeInfo info = new RecipeInfo();

            info.type = type.toString();
            info.output = RecipeItem.fromItemStack(out);
            info.ingredients = RecipeItem.fromIngredients(ingredients);

            recipeInfos.add(info);
        }

        DumpWriter.writeRecipeDump(recipeInfos, "recipes");

        ctx.getSource().sendSystemMessage(Component.literal("Successfully dumped recipes!"));

        return 1;
    }

    public static int dumpItemTags(CommandContext<CommandSourceStack> ctx) {
        Set<Map.Entry<ResourceKey<Item>, Item>> items = ForgeRegistries.ITEMS.getEntries();
        Map<TagKey<Item>, List<String>> tags = new HashMap<>();

        for (Map.Entry<ResourceKey<Item>, Item> entry : items) {
            Item item = entry.getValue();
            ResourceLocation loc = entry.getKey().location();
            List<TagKey<Item>> itemTags = ForgeRegistries.ITEMS.tags().getReverseTag(item).get().getTagKeys().toList();

            processTags(tags, loc, itemTags);
        }

        DumpWriter.writeTagDump(tags, "tags/items");

        ctx.getSource().sendSystemMessage(Component.literal("Successfully dumped item tags!"));

        return 1;
    }

    public static int dumpBlockTags(CommandContext<CommandSourceStack> ctx) {
        Set<Map.Entry<ResourceKey<Block>, Block>> blocks = ForgeRegistries.BLOCKS.getEntries();
        Map<TagKey<Block>, List<String>> tags = new HashMap<>();

        for (Map.Entry<ResourceKey<Block>, Block> entry : blocks) {
            Block block = entry.getValue();
            ResourceLocation loc = entry.getKey().location();
            List<TagKey<Block>> blockTags = ForgeRegistries.BLOCKS.tags().getReverseTag(block).get().getTagKeys()
                    .toList();

            processTags(tags, loc, blockTags);
        }

        DumpWriter.writeTagDump(tags, "tags/blocks");

        ctx.getSource().sendSystemMessage(Component.literal("Successfully dumped block tags!"));

        return 1;
    }

    public static int dumpFluidTags(CommandContext<CommandSourceStack> ctx) {
        Set<Map.Entry<ResourceKey<Fluid>, Fluid>> fluids = ForgeRegistries.FLUIDS.getEntries();
        Map<TagKey<Fluid>, List<String>> tags = new HashMap<>();

        for (Map.Entry<ResourceKey<Fluid>, Fluid> entry : fluids) {
            Fluid fluid = entry.getValue();
            ResourceLocation loc = entry.getKey().location();
            List<TagKey<Fluid>> fluidTags = ForgeRegistries.FLUIDS.tags().getReverseTag(fluid).get().getTagKeys()
                    .toList();

            processTags(tags, loc, fluidTags);
        }

        DumpWriter.writeTagDump(tags, "tags/fluids");

        ctx.getSource().sendSystemMessage(Component.literal("Successfully dumped fluid tags!"));

        return 1;
    }

    public static <T> void processTags(Map<TagKey<T>, List<String>> resourceTags, ResourceLocation registryName,
            List<TagKey<T>> tags) {
        ResourceLocation id = Objects.requireNonNull(registryName);

        for (TagKey<T> tag : tags) {
            if (!resourceTags.containsKey(tag))
                resourceTags.put(tag, new ArrayList<>());

            resourceTags.get(tag).add(id.toString());
        }
    }

    public static int dumpAllTags(CommandContext<CommandSourceStack> ctx) {
        dumpItemTags(ctx);
        dumpBlockTags(ctx);
        dumpFluidTags(ctx);

        return 1;
    }

    public static int dumpAll(CommandContext<CommandSourceStack> ctx) {
        dumpRecipes(ctx);
        dumpAllTags(ctx);

        return 1;
    }
}
