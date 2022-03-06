package me.necroliner.socialcreditsplugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scoreboard.*;

public class StatsDisplayManager implements Listener {

    private static final String STATS_SCOREBOARD_NAME = "StatsDisplay";
    private static final String STATS_SCOREBOARD_DISPLAY_NAME = ChatColor.GOLD + "Citizen stats";

    public StatsDisplayManager(ScoreboardManager sbManager){
        Scoreboard currentScoreboard = sbManager.getMainScoreboard();

        if(currentScoreboard.getObjective(STATS_SCOREBOARD_NAME) == null){
            System.out.println("scoreboard inexistant, initialising..");

            Scoreboard scoreboard = sbManager.getMainScoreboard();
            Objective objective = scoreboard.registerNewObjective(STATS_SCOREBOARD_NAME, "dummy", STATS_SCOREBOARD_DISPLAY_NAME);
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
    }

    @EventHandler
    public void onPlayerConnection(PlayerLoginEvent e){
        Player player = e.getPlayer();

        refreshData(player);
    }

    public void refreshData(Player player){
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective(STATS_SCOREBOARD_NAME);
        scoreboard.resetScores(STATS_SCOREBOARD_NAME);
        Score line1 = objective.getScore("Social Credits : " + player.getScoreboard().getObjective(SocialCreditsManager.OBJECTIVE_SOCIAL_CREDIT_NAME).getScore(player.getName()));
        line1.setScore(3);
    }
}
