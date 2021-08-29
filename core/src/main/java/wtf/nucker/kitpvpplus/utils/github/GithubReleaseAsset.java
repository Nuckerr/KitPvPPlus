package wtf.nucker.kitpvpplus.utils.github;

import java.io.File;
import java.io.IOException;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 10/08/2021
 */
public interface GithubReleaseAsset {

    String getName();

    void download(File path) throws IOException;
}
