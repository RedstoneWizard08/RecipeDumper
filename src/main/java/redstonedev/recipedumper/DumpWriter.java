package redstonedev.recipedumper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class DumpWriter {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void writeDump(Object obj, String name, boolean pretty) {
        MinecraftServer mc = ServerLifecycleHooks.getCurrentServer();
        Path path = new File(mc.getServerDirectory().getAbsolutePath()).toPath().resolve("dumps")
                .resolve(name + ".json");
        File dumpFile = path.toFile();

        if (!dumpFile.exists()) {
            try {
                path.getParent().toFile().mkdirs();
                dumpFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        GsonBuilder gsonBuilder = new GsonBuilder();

        if (pretty) {
            gsonBuilder.setPrettyPrinting();
        }

        Gson gson = gsonBuilder.create();
        String json = gson.toJson(obj);

        FileOutputStream fos;

        try {
            fos = new FileOutputStream(dumpFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        byte[] jsonBytes = json.getBytes();

        try {
            fos.write(jsonBytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeDump(Object obj, String name) {
        writeDump(obj, name, true);
    }

    public static <T> void writeTagDump(Map<TagKey<T>, List<String>> obj, String name) {
        for (Map.Entry<TagKey<T>, List<String>> entry : obj.entrySet()) {
            ResourceLocation key = entry.getKey().location();
            List<String> value = entry.getValue();

            writeDump(value, name + "/" + key.getNamespace() + "/" + key.getPath(), true);
        }
    }

    public static void writeRecipeDump(List<RecipeInfo> obj, String name) {
        for (RecipeInfo info : obj) {
            writeDump(info, name + "/" + info.type + "/" + info.output.id.getNamespace() + "/"
                    + info.output.id.getPath(), true);
        }
    }
}
