package redstonedev.recipedumper.fabric;

import redstonedev.recipedumper.RecipeDumper;
import net.fabricmc.api.ModInitializer;

public class RecipeDumperFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        RecipeDumper.init();
    }
}
