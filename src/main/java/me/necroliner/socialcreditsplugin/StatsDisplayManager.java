package me.necroliner.socialcreditsplugin;

import me.necroliner.socialcreditsplugin.data.Datasets;
import me.necroliner.socialcreditsplugin.data.PlayersData;
import me.necroliner.socialcreditsplugin.utils.LightBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import java.util.*;

public class StatsDisplayManager implements Listener {

    private final Datasets datas;
    private final EnumMap<Material, HashMap<UUID, Integer>> materialCounter;
    private final PlayersData playersData;

    private final Map<UUID, LightBoard> lightBoardMap = new HashMap<>();
    private final Map<UUID, Integer> blockMap = new HashMap<>();

    private long updateTime;

    public StatsDisplayManager(PlayersData playersData){
        this.materialCounter = playersData.getMaterialsCounterMap();
        this.playersData = playersData;
        this.datas = Datasets.getDataset();
    }

    public void scoreboard(Player player) {
        LightBoard board = lightBoardMap.get(player.getUniqueId());

        // Normally load this at start and pull from config
        String title = ChatColor.GOLD + "Citizen Stats";
        ArrayList<String> lines = new ArrayList<>();
        lines.add(ChatColor.WHITE + "");
        lines.add(ChatColor.GOLD + "Name : " + ChatColor.WHITE + player.getName());
        lines.add(ChatColor.GOLD + "Social Credits : " + ChatColor.WHITE + playersData.getScore(player));
        lines.add(ChatColor.WHITE + "");
        lines.add(ChatColor.WHITE + "======================");

        datas.oreThresholds.forEach((k, v) -> {
            HashMap<UUID, Integer> map = materialCounter.get(k);
            if(map.containsKey(player.getUniqueId())) {
                lines.add(ChatColor.WHITE + map.get(player.getUniqueId()).toString() + "/" + ChatColor.GRAY  + v  + " " + ChatColor.GOLD + Datasets.getPrettyName(k));
            }else{
                lines.add(ChatColor.WHITE + "0/" + ChatColor.GRAY  + v  + " " + ChatColor.GOLD + Datasets.getPrettyName(k));
            }
        });

        datas.cropThresholds.forEach((k, v) -> {
            HashMap<UUID, Integer> map = materialCounter.get(k);
            if(map.containsKey(player.getUniqueId())) {
                lines.add(ChatColor.WHITE + map.get(player.getUniqueId()).toString() + "/" + ChatColor.GRAY  + v  + " " + ChatColor.GOLD + Datasets.getPrettyName(k));
            }else{
                lines.add(ChatColor.WHITE + "0/" + ChatColor.GRAY  + v  + " " + ChatColor.GOLD + Datasets.getPrettyName(k));
            }
        });

        // if player doesn't have scoreboard, create one
        if (board == null) {
            board = new LightBoard(player, lines.size());
        }


        board.setTitle(title);
        int lineNum = lines.size();
        for (String line : lines) {
            board.setLine(lineNum, line);
            lineNum--;
        }

        lightBoardMap.put(player.getUniqueId(), board);
        player.setScoreboard(board.board);

    }
    public void startLoop() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SocialCreditSystem.getInstance(), this::tick, 10L, 2L);
    }

    private void tick() {
        long current = System.currentTimeMillis();
        if (updateTime <= current) {
            Bukkit.getOnlinePlayers().forEach(this::scoreboard);
            clean();
            updateTime = current + 250L;
        }
    }

    private void clean() {
        List<UUID> removeList = new ArrayList<>();
        for (UUID uuid : lightBoardMap.keySet()) {
            if (Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline()) {
                removeList.add(uuid);
            }
        }
        removeList.forEach(lightBoardMap::remove);
        removeList.forEach(blockMap::remove);
    }


}