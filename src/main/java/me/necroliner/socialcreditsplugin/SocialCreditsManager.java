package me.necroliner.socialcreditsplugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class SocialCreditsManager {
    private final String OBJECTIVE_SOCIAL_CREDIT_NAME = "SocialCredit";

    private final ScoreboardManager sbManager;
    private final Scoreboard board;
    private final Objective objective;


    public SocialCreditsManager(ScoreboardManager sbManager){
        this.sbManager = sbManager;
        this.board = sbManager.getMainScoreboard();

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
