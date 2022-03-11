package me.necroliner.socialcreditsplugin;

import me.necroliner.socialcreditsplugin.data.Datasets;
import me.necroliner.socialcreditsplugin.data.NameManager;
import me.necroliner.socialcreditsplugin.utils.LightBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import java.util.*;

public class StatsDisplayManager implements Listener {

    private final Datasets datas;
    private final EnumMap<Material, HashMap<UUID, Integer>> materialCounter;
    private final PlayersData playersData;

    private final Map<UUID, LightBoard> lightBoardMap = new HashMap<>();

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
        lines.add(ChatColor.GOLD + "Alliance §f: " + NameManager.teamData.get(player));
        lines.add(ChatColor.GOLD + "Name §f: " + player.getName());
        lines.add(ChatColor.GOLD + "Social Credits §f: " + playersData.getScore(player));
        lines.add(ChatColor.WHITE + "");

        if (playersData.getIgnoreBoard().contains(player.getUniqueId())) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            return;
        }

        // if player doesn't have scoreboard, create on
        if (board == null) {
            board = new LightBoard(player, lines.size());
        }


        board.setTitle(title);
        int lineNum = lines.size();
        for (String line : lines) {
            board.setLine(lineNum, line);
            lineNum--;
        }

        //setting credits for each players in the tab
        Objective objective = board.board.getObjective("pl1");
        if (objective == null) {
            objective = board.board.registerNewObjective("pl1", "dummy", "pl1");
            objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        }
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Score score = objective.getScore(onlinePlayer.getName());
            score.setScore(playersData.getScore(onlinePlayer));
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
            if (Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline() || playersData.getIgnoreBoard().contains(uuid)) {
                removeList.add(uuid);
            }
        }
        removeList.forEach(lightBoardMap::remove);
    }


}