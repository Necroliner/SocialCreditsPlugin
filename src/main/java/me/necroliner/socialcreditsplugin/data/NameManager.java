package me.necroliner.socialcreditsplugin.data;

import me.necroliner.socialcreditsplugin.SocialCreditSystem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import java.util.HashMap;

public class NameManager {

    private long updateTime;
    public static HashMap<Player, String> teamData = new HashMap<>();

    public static void update(Player player) {
        String playerName = player.getName();
        if(player.isOp()){
            playerName = "§c" + playerName;
        }
        String playerTeam = getPlayerTeam(player);
        String prefix = managePrefix(playerTeam);
        player.setPlayerListName(prefix + "§r" + playerName + "§r");
        player.setDisplayName(prefix + "§7" + playerName + "§r");


        teamData.put(player, prefix);
    }

    public static String getPlayerTeam(Player player){
        String playerTeam = "None";
        for(Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()){
            if(team.getEntries().contains(player.getName())){
                playerTeam = team.getName();
                break;
            }
        }
        return playerTeam;
    }

    private static String managePrefix(String teamName){
        String prefix = "§8[No Faction] ";
        if(teamName.contains("South")){
            prefix =  "§8[§2South§8] ";
        }else if(teamName.contains("North")){
            prefix =  "§8[§9North§8] ";
        }else if(teamName.contains("East")){
            prefix =  "§8[§eEast§8] ";
        }else if(teamName.contains("West")){
            prefix =  "§8[§5West§8] ";
        }
        return prefix;
    }

    private void tick() {
        long current = System.currentTimeMillis();
        if (updateTime <= current) {
            Bukkit.getOnlinePlayers().forEach(NameManager::update);
            updateTime = current + 1000L;
        }
    }

    public void startLoop() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SocialCreditSystem.getInstance(), this::tick, 10L, 2L);
    }

}
