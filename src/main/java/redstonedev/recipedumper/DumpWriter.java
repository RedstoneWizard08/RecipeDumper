package redstonedev.recipedumper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class DumpWriter {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void writeDump(Object obj, String name, boolean pretty) {
        Path path = new File(Minecraft.getInstance().gameDir.getAbsolutePath()).toPath().resolve("dumps").resolve(name + ".json");
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
}
