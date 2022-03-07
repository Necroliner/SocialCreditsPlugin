package me.necroliner.socialcreditsplugin;

import me.necroliner.socialcreditsplugin.data.Datasets;
import me.necroliner.socialcreditsplugin.data.PlayersData;
import me.necroliner.socialcreditsplugin.utils.LightBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.*;

import java.util.*;

public class StatsDisplayManager implements Listener {

    private static final String STATS_SCOREBOARD_NAME = "StatsDisplay";
    private static final String STATS_SCOREBOARD_DISPLAY_NAME = ChatColor.GOLD + "Citizen stats";

    private final Datasets datas;
    private final ScoreboardManager sbManager;
    private EnumMap<Material, HashMap<UUID, Integer>> playersData;

    private final Map<UUID, LightBoard> lightBoardMap = new HashMap<>();
    private final Map<UUID, Integer> blockMap = new HashMap<>();

    private long updateTime;

    public StatsDisplayManager(ScoreboardManager sbManager, PlayersData playersData){
        this.sbManager = sbManager;
        this.playersData = playersData.getMap();
        this.datas = Datasets.getDataset();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
    }

    public void scoreboard(Player player) {
        LightBoard board = lightBoardMap.get(player.getUniqueId());

        // Normally load this at start and pull from config
        String title = ChatColor.GOLD + "Citizen Stats";
        List<String> lines = Arrays.asList(
                ChatColor.GOLD +"Name : " + ChatColor.WHITE + player.getName(),
                ChatColor.GOLD +"Social Credits : " + ChatColor.WHITE + sbManager.getMainScoreboard().getObjective(SocialCreditsManager.OBJECTIVE_SOCIAL_CREDIT_NAME).getScore(player.getName()).getScore(),
                ChatColor.WHITE +"======================"
        );


        // if player doesn't have scorebaord, create one
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