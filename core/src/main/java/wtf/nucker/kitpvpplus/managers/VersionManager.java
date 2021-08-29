package wtf.nucker.kitpvpplus.managers;

import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.objects.Version;
import wtf.nucker.kitpvpplus.utils.ChatUtils;
import wtf.nucker.kitpvpplus.utils.ClockUtils;
import wtf.nucker.kitpvpplus.utils.Logger;
import wtf.nucker.kitpvpplus.utils.github.GithubClient;
import wtf.nucker.kitpvpplus.utils.github.GithubRepo;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 08/08/2021
 */
public class VersionManager {

    private final GithubClient gh;
    private final GithubRepo repo;
    private final Version currentVer;
    private Version latestVer;
    private final KitPvPPlus instance = KitPvPPlus.getInstance();

    private boolean updating = false;

    public VersionManager() throws IOException {
        this.gh = new GithubClient();
        this.repo = gh.getRepository("Nuckerr/KitPvPPlus");

        this.currentVer = Version.fromString(KitPvPPlus.getInstance().getDescription().getVersion());
        this.latestVer = Version.fromString(repo.getLatestRelease().getTagName());

        ClockUtils.runInterval((int) TimeUnit.MINUTES.toSeconds(instance.getConfig().getLong("update.re-check-alert")), runnable -> {
            Logger.debug("Checking version...");
            VersionManager.this.latestVer = Version.fromString(repo.getLatestRelease().getTagName());
            this.alertUpdate();
        });
        if(currentVer.equals(latestVer)) return;

        if(Version.compare(currentVer, latestVer)) {
            this.alertUpdate();
        }
    }

    public void alertUpdate() {
        if(!this.instance.getConfig().getBoolean("update.alert")) return;
        if(!Version.compare(currentVer, latestVer)) return;
        if(this.updating) return;

        this.updating = true;
        Logger.warn(new String[]{
                ChatUtils.CONSOLE_BAR,
                "Your plugin is out of date running v" + this.currentVer.buildVer() + ".",
                "The latest version is v" + this.latestVer.buildVer() + ".",
                "",
                "You can download it here: https://github.com/" + this.repo.getOwnerName() + "/" + this.repo.getName() + "/releases/tag/" + this.latestVer.buildVer(),
                ChatUtils.CONSOLE_BAR
        });
        if(this.latestVer.isHotfix()) {
            Logger.error("v" + latestVer.buildVer() + " is a hotfix. It is HIGHLY recommend that you update to this.");
        }
    }

    public boolean needsUpdating() {
        return this.updating;
    }

    public GithubRepo getRepo() {
        return repo;
    }

    public GithubClient getGitHubInstance() {
        return gh;
    }

    public Version getCurrentVer() {
        return currentVer;
    }

    public Version getLatestVer() {
        return latestVer;
    }
}
