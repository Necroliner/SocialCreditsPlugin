package me.necroliner.socialcreditsplugin.toDelete;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;

public class MapHandler {

    public HashMap<Player, Scoreboard> playerScoreboards = new HashMap<Player, Scoreboard>();

    public boolean checkPlayerScoreboard(Player player) {
        if (playerScoreboards.containsKey(player)) {
            return true;
        }
        return false;
    }

    public void addPlayerScoreboard(Player player, Scoreboard scoreboard) {
        if (checkPlayerScoreboard(player)) {
            playerScoreboards.remove(player);
        }
        playerScoreboards.put(player, scoreboard);

    }

    public void removePlayerScoreboard(Player player) {
        if (checkPlayerScoreboard(player)) {
            playerScoreboards.remove(player);
        }
    }

    public Scoreboard getPlayerScoreboard(Player player) {
        if (!checkPlayerScoreboard(player)) {
            Bukkit.getServer().getConsoleSender()
                    .sendMessage("§cThere was an error getting " + player.getName() + "'s scoreboard!");
            return null;
        }
        return playerScoreboards.get(player);
    }


    public void addOnlinePlayer(Player player) {

    }
}
