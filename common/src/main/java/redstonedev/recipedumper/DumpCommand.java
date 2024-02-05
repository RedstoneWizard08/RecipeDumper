package redstonedev.recipedumper;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class DumpCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> dumpCommand = Commands.literal("dump")
            .requires((commandSource) -> commandSource.hasPermission(1))
                .then(
                    Commands.literal("recipes")
                        .executes(Dumper::dumpRecipes)
                )
                .then(
                    Commands.literal("tags")
                        .then(
                            Commands.literal("items")
                                .executes(Dumper::dumpItemTags)
                        )
                        .then(
                            Commands.literal("blocks")
                                .executes(Dumper::dumpBlockTags)
                        )
                        .then(
                            Commands.literal("fluids")
                                .executes(Dumper::dumpFluidTags)
                        )
                        .then(
                            Commands.literal("all")
                                .executes(Dumper::dumpAllTags)
                        )
                )
                .then(
                    Commands.literal("all")
                        .executes(Dumper::dumpAll)
                );

        dispatcher.register(dumpCommand);
    }
}
