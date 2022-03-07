package me.necroliner.socialcreditsplugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class SocialCreditsManager {
    public static final String OBJECTIVE_SOCIAL_CREDIT_NAME = "SocialCredit";

    private final Scoreboard board;
    private final Objective objective;
    private final StatsDisplayManager sdManager;


    public SocialCreditsManager(ScoreboardManager sbManager, StatsDisplayManager sdManager){
        this.board = sbManager.getMainScoreboard();
        this.sdManager = sdManager;
        if(this.board.getObjective(OBJECTIVE_SOCIAL_CREDIT_NAME) == null) {
            System.out.println(SocialCreditSystem.LOGGER_NAME +"initialising new scoreboard");

            this.objective = board.registerNewObjective(OBJECTIVE_SOCIAL_CREDIT_NAME, "dummy");
            this.objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
            this.objective.setDisplayName(OBJECTIVE_SOCIAL_CREDIT_NAME);

        }else{
            System.out.println(SocialCreditSystem.LOGGER_NAME +"Scoreboard already initialised");
            objective = this.board.getObjective(OBJECTIVE_SOCIAL_CREDIT_NAME);
        }
    }

    public void addPoints (Player player, int points){
        Score score = objective.getScore(player.getName());
        score.setScore(score.getScore() + points);

        if(points<2) {
            player.sendMessage(ChatColor.GREEN + "+" + points + " Social Credit !");
        }else{
            player.sendMessage(ChatColor.GREEN + "+" + points + " Social Credits !");
        }
    }

    public void removePoints (Player player, int points){
        Score score = objective.getScore(player.getName());
        score.setScore(score.getScore() - points);

        if(points<2) {
            player.sendMessage(ChatColor.RED + "-" + points + " Social Credit !");
        }else{
            player.sendMessage(ChatColor.RED + "-" + points + " Social Credits !");
        }
    }

    public int getScore(String playerName){
        return objective.getScore(playerName).getScore();
    }

}
