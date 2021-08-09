package wtf.nucker.kitpvpplus.utils.github;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 09/08/2021
 */
public interface GithubRepo {

    GithubRelease getLatestRelease();
    String getName();
    String getOwnerName();
}
