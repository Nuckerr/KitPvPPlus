package wtf.nucker.kitpvpplus.objects;

import wtf.nucker.kitpvpplus.exceptions.InvalidVersionSyntaxException;

import java.util.Objects;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 08/08/2021
 */
public class Version {

    private final String main, sub, fix;
    private final boolean hotfix;

    public Version(String main, String sub, String fix, boolean hotfix) {
        this.main = main;
        this.sub = sub;
        this.fix = fix;
        this.hotfix = hotfix;
    }

    public String buildVer() {
        return main + "." + sub + "." + fix + (hotfix ? "-HOTFIX" : "");
    }


    public String getFix() {
        return fix;
    }

    public String getMain() {
        return main;
    }

    public String getSub() {
        return sub;
    }

    public int getMainAsInt() {
        return Integer.parseInt(main);
    }

    public int getSubAsInt() {
        return Integer.parseInt(sub);
    }

    public int getFixAsInt() {
        return Integer.parseInt(fix);
    }

    public boolean isHotfix() {
        return hotfix;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Version)) return false;
        if (this == o) return true;
        return ((Version) o).buildVer().equals(this.buildVer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(main, sub, fix, hotfix);
    }

    public static Version fromString(String str) {
        String[] versions = str.split("\\.");
        if(versions.length == 0) throw new InvalidVersionSyntaxException(str + " is an invalid version. Follow `main.sub.fix-HOTFIX`");
        String main, sub, fix;
        boolean hotfix;
        main = versions[0];
        if(versions.length <= 1) sub = "0"; else sub = versions[1];
        if(versions.length <= 2) fix = "0"; else fix = versions[2];
        hotfix = str.endsWith("-HOTFIX");

        return new Version(main, sub, fix, hotfix);
    }

    /**
     * @return true if v2 is greater
     */
    public static boolean compare(Version v1, Version v2) {
        if(v2.getMainAsInt() > v1.getMainAsInt()) return true;
        if(v2.getSubAsInt() > v1.getSubAsInt()) return true;
        if(v2.getFixAsInt() > v1.getSubAsInt()) return true;
        return v2.isHotfix();
    }
}