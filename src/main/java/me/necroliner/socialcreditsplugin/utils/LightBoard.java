package me.necroliner.socialcreditsplugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;

/**
    Code written and created by Lightcaster5 (Lightcaster5#0001 @ discord.com)
    https://github.com/Lightcaster5
 */

public class LightBoard {

    public Scoreboard board;
    private final Objective objective;

    private final HashMap<Integer, String> cache = new HashMap<>();

    /**
     * Create board
     */
    public LightBoard(Player player, int linecount) {
        this.board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        this.objective = this.board.registerNewObjective("sb1", "sb2", "sb3");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName("...");

        int score = linecount;
        for (int i = linecount; i > 0; i--)
        {
            Team t = this.board.registerNewTeam(i + "");
            t.addEntry(ChatColor.values()[i] + "");

            this.objective.getScore(ChatColor.values()[i] + "").setScore(score);

            score--;
        }

        player.setScoreboard(this.board);
    }

    /**
     * Set the board title
     */
    public void setTitle(String string) {
        if (string == null) string = "";

        if (cache.containsKey(-1) && cache.get(-1).equals(string)) return;
        cache.put(-1, string);
        objective.setDisplayName(string);
    }

    /**
     * Set a specific line
     */
    public void setLine(int index, String line) {
        Team t = board.getTeam((index) + "");
        if (line == null) line = "";

        if (cache.containsKey(index) && cache.get(index).equals(line)) return;
        cache.put(index, line);

        line = prepLine(line);
        ArrayList<String> parts;
        parts = splitString(line);

        t.setPrefix(trimToMax(parts.get(0)));
        t.setSuffix(trimToMax(parts.get(1)));
    }

    private String trimToMax(String string) {
        if (string.length() > 64) {
            return string.substring(0, 64);
        } else {
            return string;
        }
    }

    private String prepLine(String color) {
        if (color.length() > 64) {
            ArrayList<String> pieces = splitString(color);
            return pieces.get(0) + "§f" + getLastColor(pieces.get(0)) + pieces.get(1);
        }
        return color;
    }

    private ArrayList<String> splitString(String line) {
        ArrayList<String> parts = new ArrayList<>();
        StringBuilder prefix = new StringBuilder(line.substring(0, Math.min(line.length(), 64)));
        StringBuilder suffix = new StringBuilder(line.length() > 64 ? line.substring(64) : "");
        if (line.length() > 64) {
            if (prefix.toString().length() > 2 && prefix.charAt(prefix.length() - 2) == '§') {
                String color = String.valueOf(prefix.charAt(prefix.length() - 1));
                prefix.deleteCharAt(prefix.length() - 2).deleteCharAt(prefix.length() - 1);
                suffix.insert(0, '§' + color);
            } else if (prefix.toString().length() > 1 && prefix.charAt(prefix.length() - 1) == '§') {
                prefix.deleteCharAt(prefix.length() - 1);
                suffix.insert(0, '§');
            }
            parts.add(prefix.toString());
            parts.add(suffix.toString());
        } else {
            parts.add(line);
            parts.add("");
        }
        return parts;
    }

    private String getLastColor(String s) {
        String last = ChatColor.getLastColors(s);
        if (last == null)
            return "";
        return last;
    }
}
