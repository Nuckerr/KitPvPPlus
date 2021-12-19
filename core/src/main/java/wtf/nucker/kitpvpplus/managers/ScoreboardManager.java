package wtf.nucker.kitpvpplus.managers;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.dataHandlers.PlayerData;
import wtf.nucker.kitpvpplus.utils.ChatUtils;
import wtf.nucker.kitpvpplus.utils.ClockUtils;

import java.util.HashMap;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class ScoreboardManager {

    public static boolean ENABLED = KitPvPPlus.getInstance().getConfig().getBoolean("scoreboard.enabled");

    private HashMap<Player, FastBoard> boards;

    public ScoreboardManager() {
        if(!ScoreboardManager.ENABLED) return;
        boards = new HashMap<>();
        ClockUtils.runIntervalAsync(5, runnable -> {
            if (Bukkit.getServer().getOnlinePlayers().size() > 0) {
                Bukkit.getServer().getOnlinePlayers().forEach(this::updateBoard);
            }
        });
    }

    public FastBoard getSpawnBoard(Player player) {
        if(!ScoreboardManager.ENABLED) return null;
        if (KitPvPPlus.getInstance().getConfig().getStringList("scoreboard.disabled-worlds").contains(player.getWorld().getName())) {
            FastBoard board = new FastBoard(player);
            board.updateTitle("null");
            board.updateLine(0, "null");
            board.delete();
            return board;
        }
        ConfigurationSection section = KitPvPPlus.getInstance().getConfig().getConfigurationSection("scoreboard.spawn");
        return buildBoard(player, section);
    }

    private FastBoard buildBoard(Player player, ConfigurationSection section) {
        PlayerData data = KitPvPPlus.getInstance().getDataManager().getPlayerData(player);

        FastBoard board = new FastBoard(player);
        board.updateTitle(ChatUtils.translate(section.getString("title"))
                .replace("%player%", player.getName())
                .replace("%kills%", String.valueOf(data.getKills()))
                .replace("%deaths%", String.valueOf(data.getDeaths()))
                .replace("%exp%", String.valueOf(data.getExp()))
                .replace("%bar%", ChatUtils.SB_BAR)
                .replace("%level%", String.valueOf(data.getLevel()))
                .replace("%killstreak%", String.valueOf(data.getKillStreak()))
                .replace("%top_killstreak%", String.valueOf(data.getTopKillStreak()))
                .replace("%kdr%", String.valueOf(data.getKDR()))
                .replace("%bal%", String.valueOf(new PlayerBank(player).getBal()))
        );

        for (int i = 0; i < section.getStringList("board").size(); i++) {
            board.updateLine(i, ChatUtils.translate(section.getStringList("board").get(i))
                    .replace("%player%", player.getName())
                    .replace("%kills%", String.valueOf(data.getKills()))
                    .replace("%deaths%", String.valueOf(data.getDeaths()))
                    .replace("%exp%", String.valueOf(data.getExp()))
                    .replace("%bar%", ChatUtils.SB_BAR)
                    .replace("%level%", String.valueOf(data.getLevel()))
                    .replace("%killstreak%", String.valueOf(data.getKillStreak()))
                    .replace("%top_killstreak%", String.valueOf(data.getTopKillStreak()))
                    .replace("%kdr%", String.valueOf(data.getKDR()))
                    .replace("%bal%", String.valueOf(new PlayerBank(player).getBal()))
            );
        }
        this.getBoards().put(player, board);
        return board;
    }

    public FastBoard getArenaBoard(Player player) {
        if(!ScoreboardManager.ENABLED) return null;
        if (KitPvPPlus.getInstance().getConfig().getStringList("scoreboard.disabled-worlds").contains(player.getWorld().getName())) {
            FastBoard board = new FastBoard(player);
            board.updateTitle("null");
            board.updateLine(0, "null");
            board.delete();
            return board;
        }
        ConfigurationSection section = KitPvPPlus.getInstance().getConfig().getConfigurationSection("scoreboard.arena");
        return buildBoard(player, section);
    }

    public void updateBoard(Player player) {
        if(!ScoreboardManager.ENABLED) return;
        PlayerData data = KitPvPPlus.getInstance().getDataManager().getPlayerData(player);
        FastBoard board = this.getBoardByPlayer(player);
        switch (data.getState()) {
            case ARENA:
                for (int i = 0; i < KitPvPPlus.getInstance().getConfig().getStringList("scoreboard.arena.board").size(); i++) {
                    board.updateLine(i, ChatUtils.translate(KitPvPPlus.getInstance().getConfig().getStringList("scoreboard.arena.board").get(i))
                            .replace("%player%", player.getName())
                            .replace("%kills%", String.valueOf(data.getKills()))
                            .replace("%deaths%", String.valueOf(data.getDeaths()))
                            .replace("%exp%", String.valueOf(data.getExp()))
                            .replace("%bar%", ChatUtils.SB_BAR)
                            .replace("%level%", String.valueOf(data.getLevel()))
                            .replace("%killstreak%", String.valueOf(data.getKillStreak()))
                            .replace("%top_killstreak%", String.valueOf(data.getTopKillStreak()))
                            .replace("%kdr%", String.valueOf(data.getKDR()))
                    );
                }

                if(KitPvPPlus.getInstance().getConfig().getStringList("scoreboard.arena.board").size() < board.getLines().size()) {
                    for (int i = KitPvPPlus.getInstance().getConfig().getStringList("scoreboard.arena.board").size() - 1; i < board.getLines().size(); i++) {
                        board.removeLine(i);
                    }
                }
                break;
            case SPAWN:
                for (int i = 0; i < KitPvPPlus.getInstance().getConfig().getStringList("scoreboard.spawn.board").size(); i++) {
                    board.updateLine(i, ChatUtils.translate(KitPvPPlus.getInstance().getConfig().getStringList("scoreboard.spawn.board").get(i))
                            .replace("%player%", player.getName())
                            .replace("%kills%", String.valueOf(data.getKills()))
                            .replace("%deaths%", String.valueOf(data.getDeaths()))
                            .replace("%exp%", String.valueOf(data.getExp()))
                            .replace("%bar%", ChatUtils.SB_BAR)
                            .replace("%level%", String.valueOf(data.getLevel()))
                            .replace("%killstreak%", String.valueOf(data.getKillStreak()))
                            .replace("%top_killstreak%", String.valueOf(data.getTopKillStreak()))
                            .replace("%kdr%", String.valueOf(data.getKDR()))
                    );
                }

                if(KitPvPPlus.getInstance().getConfig().getStringList("scoreboard.spawn.board").size() < board.getLines().size()) {
                    for (int i = KitPvPPlus.getInstance().getConfig().getStringList("scoreboard.spawn.board").size() - 1; i < board.getLines().size(); i++) {
                        board.removeLine(i);
                    }
                }
                break;
        }
    }

    public Scoreboard getHealthDisplay(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("showhealth", "health");
        obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
        obj.setDisplayName(KitPvPPlus.getInstance().getConfig().getString("health-display").replace("%health%", String.valueOf(player.getHealth())));

        return board;
    }

    public void clearBoard(Player player) {
        if(!ScoreboardManager.ENABLED) return;
        this.getSpawnBoard(player).delete();
        this.getArenaBoard(player).delete();
    }

    private HashMap<Player, FastBoard> getBoards() {
        return boards;
    }

    public FastBoard getBoardByPlayer(Player player) {
        if(!this.getBoards().containsKey(player)) {
            return null;
        }

        return this.getBoards().get(player);
    }
}
