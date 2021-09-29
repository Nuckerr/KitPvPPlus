package wtf.nucker.kitpvpplus.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.bukkit.Bukkit;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.managers.KitManager;
import wtf.nucker.kitpvpplus.managers.Locations;
import wtf.nucker.kitpvpplus.managers.PlayerBank;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nucker
 * @project KitPvPPlus
 * @date 28/09/2021
 */
public class Debugger {

    private static final String LINE = "===================================================";

    private static final String HASTE_URL = "https://paste.nucker.me";

    public String dumpContents() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String res = LINE + "\n DUMP CONTENTS - " + dtf.format(now) + "\n" + LINE + "\n";

        res = res + this.dumpConfig(KitPvPPlus.getInstance().getMessagesConfig());
        res = res + this.dumpConfig(KitPvPPlus.getInstance().getDataManager().getDataConfig());
        res = res + this.dumpConfig(Locations.getConfig());
        res = res + this.dumpConfig(KitManager.getConfigInstance());
        res = res + this.dumpConfig(KitPvPPlus.getInstance().getSignManager().getConfigInstance());

        res = res + "PLUGIN DEBUG DATA: \n" + LINE + "\n";

        res = res + "Version: " + KitPvPPlus.getInstance().getDescription().getVersion() + "\n";
        res = res + "Sending metrics: " + KitPvPPlus.getInstance().getMetrics().isEnabled() + "\n";
        res = res + "World guard integration: " + KitPvPPlus.getInstance().isWGEnabled() + "\n";
        res = res + "Placeholder API integration: " + (Bukkit.getServer().getPluginManager().getPlugin("PlaceHolderAPI") != null) + "\n";
        res = res + "Storage system: " + KitPvPPlus.getInstance().getDataManager().getStorageType() + "\n";
        res = res + "Bank Storage type: " + PlayerBank.getStorageType() + "\n";
        res = res + "Providing vault: " + PlayerBank.providingVault() + "\n";
        res = res + "Debug mode: " + KitPvPPlus.DEBUG + "\n";
        res = res + "Legacy version: " + KitPvPPlus.getInstance().getVerManager().needsUpdating() + "\n";

        res = res + LINE + "\n";

        res = res + "SERVER DEBUG DATA: \n" + LINE + "\n";

        res = res + "Full version: " + Bukkit.getVersion() + "\n";
        res = res + "Bukkit version: " + Bukkit.getBukkitVersion() + "\n";
        res = res + "Sub version: " + KitPvPPlus.getInstance().getSubVersion() + "\n";

        res = res + LINE + "\n";

        res = res + "Please send this in #kitpvpplus-help";

        return res;
    }

    public String dumpToPasteServer() {
        HttpRequest req = HttpRequest.post(HASTE_URL + "/documents").header("data", this.dumpContents());
        HttpResponse response = req.send();

        JsonReader reader = new JsonReader(new StringReader(response.bodyText()));
        reader.setLenient(true);
        JsonObject obj = new JsonParser().parse(reader).getAsJsonObject();
        return HASTE_URL + "/" + obj.get("key").getAsString();
    }

    private String dumpConfig(Config config)  {
        StringBuilder res = new StringBuilder(LINE + "\n" + config.getName() + "\n" + LINE + "\n");

        try {
            for (String line : this.getLines(config.getFile())) {
                res.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        res.append(LINE).append("\n");
        return res.toString();
    }


    private List<String> getLines(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;

        List<String> res = new ArrayList<>();
        while((line = reader.readLine()) != null){
            res.add(line);
        }

        return res;
    }
}
