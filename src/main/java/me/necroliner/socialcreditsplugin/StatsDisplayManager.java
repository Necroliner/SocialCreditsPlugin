package me.necroliner.socialcreditsplugin;

import me.necroliner.socialcreditsplugin.data.Datasets;
import me.necroliner.socialcreditsplugin.data.PlayersData;
import me.necroliner.socialcreditsplugin.toDelete.MapHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.*;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.UUID;

public class StatsDisplayManager implements Listener {

    private static final String STATS_SCOREBOARD_NAME = "StatsDisplay";
    private static final String STATS_SCOREBOARD_DISPLAY_NAME = ChatColor.GOLD + "Citizen stats";

    private final Datasets datas;
    private final ScoreboardManager sbManager;
    private EnumMap<Material, HashMap<UUID, Integer>> playersData;

    private final MapHandler map = new MapHandler();

    public StatsDisplayManager(ScoreboardManager sbManager, PlayersData playersData){
        this.sbManager = sbManager;
        this.playersData = playersData.getMap();
        this.datas = Datasets.getDataset();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        createScoreboard(e.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        map.removePlayerScoreboard(e.getPlayer());
    }

    public void createScoreboard(Player player) {
        String entryVoidString = "§7§7";
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(ChatColor.GOLD + "Citizen Stats", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        map.addPlayerScoreboard(player, scoreboard);

        Score blankLine = objective.getScore("§0");
        blankLine.setScore(99);

        if (scoreboard.getTeam("name") == null) {
            scoreboard.registerNewTeam("name");
        }
        scoreboard.getTeam("name").addEntry("§7");
        scoreboard.getTeam("name").setPrefix(ChatColor.GOLD + "Citizen : ");
        scoreboard.getTeam("name").setSuffix(ChatColor.WHITE + player.getName());
        objective.getScore("§7").setScore(98);

        if (scoreboard.getTeam("Social Credits") == null) {
            scoreboard.registerNewTeam("Social Credits");
        }
        scoreboard.getTeam("Social Credits").addEntry("§7§7");
        scoreboard.getTeam("Social Credits").setPrefix(ChatColor.GOLD + "Social Credits: ");
        scoreboard.getTeam("Social Credits").setSuffix(ChatColor.WHITE + "" + player.getScoreboard().getObjective(SocialCreditsManager.OBJECTIVE_SOCIAL_CREDIT_NAME).getScore(player.getName()).getScore());
        objective.getScore("§7§7").setScore(97);

        Score separator1 = objective.getScore("======================");
        separator1.setScore(95);

        if (scoreboard.getTeam("Stone") == null) {
            scoreboard.registerNewTeam("Social Credits");
        }
        scoreboard.getTeam("Social Credits").addEntry("§7§7§7");
        scoreboard.getTeam("Social Credits").setPrefix( "Stone : ");
        scoreboard.getTeam("Social Credits").setSuffix("?/" + datas.oreThresholds.get(Material.STONE));
        objective.getScore("§7§7§7").setScore(94);

        for (HashMap.Entry<Material, Integer> entry : datas.oreThresholds.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());

            entryVoidString = entryVoidString + "§7";
            String currentMaterial = Datasets.getPrettyName(entry.getKey());
            if (scoreboard.getTeam(currentMaterial) == null) {
                scoreboard.registerNewTeam(currentMaterial);
            }
            scoreboard.getTeam(currentMaterial).addEntry(entryVoidString);
            scoreboard.getTeam(currentMaterial).setPrefix( "Stone : ");
            scoreboard.getTeam(currentMaterial).setSuffix("?/" + datas.oreThresholds.get(Material.STONE));
            objective.getScore(entryVoidString).setScore(94);
        }

        //map.addOnlinePlayer(player);
        player.setScoreboard(scoreboard);
    }
    public void updateScoreboardCredits(Player player) {

            Scoreboard scoreboard = map.getPlayerScoreboard(player);

            if (scoreboard.getTeam("Social Credits") == null) {
                scoreboard.registerNewTeam("Social Credits");
            }
            scoreboard.getTeam("Social Credits").setPrefix(ChatColor.GOLD + "Social Credits: ");
            scoreboard.getTeam("Social Credits").setSuffix(ChatColor.WHITE + "" + sbManager.getMainScoreboard().getObjective(SocialCreditsManager.OBJECTIVE_SOCIAL_CREDIT_NAME).getScore(player.getName()).getScore());


    }

    public void updateScoreboardRessources(Player player) {

        Scoreboard scoreboard = map.getPlayerScoreboard(player);
        //EnumMap<Material, HashMap<UUID, Integer>> playersData
        playersData.forEach((key,value) ->{
            if(value.containsKey(player.getUniqueId())){

            }
        });


    }





}