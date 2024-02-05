package redstonedev.recipedumper;

import dev.architectury.event.events.common.CommandRegistrationEvent;

public class RecipeDumper {
    public static final String MOD_ID = "recipedumper";

    public static void init() {
        CommandRegistrationEvent.EVENT.register((dispatcher, _reg, _sel) -> {
            DumpCommand.register(dispatcher);
        });
    }
}
