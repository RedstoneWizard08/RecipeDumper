package redstonedev.recipedumper.forge;

import dev.architectury.platform.forge.EventBuses;
import redstonedev.recipedumper.RecipeDumper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RecipeDumper.MOD_ID)
public class RecipeDumperForge {
    public RecipeDumperForge() {
        EventBuses.registerModEventBus(RecipeDumper.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        RecipeDumper.init();
    }
}
