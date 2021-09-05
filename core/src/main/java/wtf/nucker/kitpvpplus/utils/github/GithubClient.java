package wtf.nucker.kitpvpplus.utils.github;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import jodd.io.FileUtil;
import org.bukkit.Bukkit;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.managers.VersionManager;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 09/08/2021
 */
public class GithubClient {

    private final String url;

    public GithubClient() {
        this.url = "https://api.github.com/";
    }

    /**
     * @param name For KitPvPPlus by Nuckerr it would be "Nuckerr/KitPvPPlus"
     * @return Instance of repo
     */
    public GithubRepo getRepository(String name) {
        String url = this.url + "repos/" + name;
        JsonElement element = this.get(url);

        return new GithubRepo() {
            @Override
            public GithubRelease getLatestRelease() {
                JsonObject releaseObj;
                if(KitPvPPlus.getInstance().getConfig().getBoolean("update.notify-beta")) {
                    JsonElement releaseEl = GithubClient.this.get(url + "/" + "releases/");
                    releaseObj = releaseEl.getAsJsonArray().get(0).getAsJsonObject();
                }else {
                    JsonElement releaseEl = GithubClient.this.get(url + "/" + "releases/latest");
                    releaseObj = releaseEl.getAsJsonObject();
                }
                return new GithubRelease() {
                    @Override
                    public String getTagName() {
                        return releaseObj.get("tag_name").getAsString();
                    }

                    @Override
                    public GithubReleaseAsset getAssetByName(String name) {
                        AtomicReference<GithubReleaseAsset> res = new AtomicReference<>();
                        releaseObj.getAsJsonArray("assets").forEach(el -> {
                            if(el.getAsJsonObject().get("name").getAsString().equalsIgnoreCase(name)) {
                                GithubReleaseAsset asset = new GithubReleaseAsset() {
                                    @Override
                                    public String getName() {
                                        return el.getAsJsonObject().get("name").getAsString();
                                    }

                                    @Override
                                    public void download(File path) throws IOException {
                                        VersionManager manager = KitPvPPlus.getInstance().getVerManager();
                                        GithubClient.this.download("https://github.com/Nuckerr/KitPvPPlus/releases/download/" +
                                                manager.getLatestVer().buildVer() + "/KitPvPPlus-" + manager.getLatestVer().buildVer() + ".jar",
                                                KitPvPPlus.getInstance().getDataFolder().getParentFile().getAbsoluteFile());
                                        KitPvPPlus.getInstance().getDataManager().getDataYaml().set("old-ver", manager.getCurrentVer().buildVer());
                                        KitPvPPlus.getInstance().getDataManager().getDataConfig().save();

                                        Bukkit.getServer().shutdown();
                                    }
                                };

                                res.set(asset);
                            }
                        });

                        return res.get();
                    }

                    @Override
                    public GithubReleaseAsset getAssetById(String id) {
                        return null;
                    }
                };
            }

            @Override
            public String getName() {
                return element.getAsJsonObject().get("name").getAsString();
            }

            @Override
            public String getOwnerName() {
                return element.getAsJsonObject().get("owner").getAsJsonObject().get("login").getAsString();
            }
        };
    }

    private JsonElement get(String stringUrl) {
        HttpResponse req = HttpRequest.get(stringUrl).accept("application/vnd.github.v3+json").send();
        JsonReader reader = new JsonReader(new StringReader(req.bodyText()));
        reader.setLenient(true);
        return new JsonParser().parse(reader);
    }

    private void download(String url, File path) throws IOException {
        HttpResponse res = HttpRequest.get(url).send();

        byte[] bytes = res.bodyBytes();

        FileUtil.writeBytes(path, bytes);
    }
}
