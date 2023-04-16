package redstonedev.recipedumper;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class DumpCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> dumpCommand = Commands.literal("dump")
                .requires((commandSource) -> commandSource.hasPermissionLevel(1))
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
                );

        dispatcher.register(dumpCommand);
    }
}
