package wtf.nucker.kitpvpplus.utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import wtf.nucker.kitpvpplus.KitPvPPlus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class has a bunch of useful methods and variables
 *
 * @author Nucker
 */
public class ChatUtils {
    /**
     * Good sized bar for scoreboards
     */
    public static final String SB_BAR = ChatColor.STRIKETHROUGH + "----------------------";
    /**
     * Good sized bar for guis
     */
    public static final String MENU_BAR = ChatColor.STRIKETHROUGH + "------------------------";
    /**
     * Good sized bar for information messages
     */
    public static final String CHAT_BAR = ChatColor.STRIKETHROUGH + "------------------------------------------------";
    /**
     * Good sized bar for console messages
     */
    public static final String CONSOLE_BAR = ChatColor.STRIKETHROUGH + "--------------------------------------";
    /**
     * No text in the message, adds a bit of space between a message
     */
    public static final String BLANK_MESSAGE = String.join("", Collections.nCopies(150, " \n"));

    public static final Pattern HEX_PATTERN = Pattern.compile("&#(\\w{5}[0-9a-f])");

    /**
     * A shorter method for the bukkit method ChatColor#translateAlternateColorCodes.
     * Dosent require a altColorChar param, this is provided in the method as "and symbol"
     *
     * @param string the string you want translating
     * @return the translated string
     */
    public static String translate(String string) {
        if(string == null) return null;
        string = ChatUtils.translateHexCodes(string);
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * A shorter method for the bukkit method ChatColor#translateAlternateColorCodes.
     * Dosent require a altColorChar param, this is provided in the method as "and symbol"
     * This methods supports lists
     *
     * @param lines the list of strings you want to translate
     * @return each string in your list being translated
     */
    public static List<String> translate(List<String> lines) {
        List<String> strings = new ArrayList<>();

        for (String line : lines) {
            strings.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        return strings;
    }

    public static String[] translate(String[] lines) {
        List<String> resAsList = new ArrayList<>();
        for (String line : lines) {
            resAsList.add(ChatUtils.translate(line));
        }
        return resAsList.toArray(new String[0]);
    }

    public static List<String> replaceInList(List<String> list, String beforeChar, String afterChar) {
        List<String> res = new ArrayList<>();
        for (String line : list) {
            res.add(line.replace(beforeChar, afterChar));
        }
        return res;
    }

    /**
     * A useful enum with a bunch of symbols you may want to access
     */
    public enum Symbols {
        HEALTH(StringEscapeUtils.unescapeJava("\u2764")),
        ARROW_LEFT(StringEscapeUtils.unescapeJava("\u00AB")),
        ARROW_RIGHT(StringEscapeUtils.unescapeJava("\u00BB")),
        CROSS(StringEscapeUtils.unescapeJava("\u2716")),
        WARNING(StringEscapeUtils.unescapeJava("\u26A0"));

        private final String symbol;

        /**
         * The constructor for the symbol enum
         *
         * @param symbol the symbol unicode
         */
        Symbols(String symbol) {
            this.symbol = symbol;
        }

        /**
         * A method to get the unicode symbol
         *
         * @return the symbol
         */
        public String getSymbol() {
            return symbol;
        }
    }

    public static String translateHexCodes (String textToTranslate) {
        if(!KitPvPPlus.getInstance().isHexEnabled()) return textToTranslate;

        Matcher matcher = HEX_PATTERN.matcher(textToTranslate);
        StringBuffer buffer = new StringBuffer();

        while(matcher.find()) {
            matcher.appendReplacement(buffer, net.md_5.bungee.api.ChatColor.of("#" + matcher.group(1)).toString());
        }

        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());

    }
}
