package me.necroliner.socialcreditsplugin.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.necroliner.socialcreditsplugin.SocialCreditSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import java.io.File;
import java.util.*;

public class PlayersData{

    private final EnumMap<Material, HashMap<UUID, Integer>> materialsCounter;
    private final HashMap<String, Integer> socialCreditCounter;
    private long updateTime;

    public PlayersData(){
        materialsCounter = new EnumMap<>(Material.class);
        Datasets data = Datasets.getDataset();
        data.cropThresholds.forEach((k, v) -> materialsCounter.put(k, new HashMap<>()));
        data.oreThresholds.forEach((k, v) -> materialsCounter.put(k, new HashMap<>()));
        socialCreditCounter = getSocialCreditMapFromJSON();
        this.startLoop();
    }

    public EnumMap<Material, HashMap<UUID, Integer>> getMaterialsCounterMap(){
        return materialsCounter;
    }

    public void saveSocialCredits() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("socialCreditsMap.json"), this.socialCreditCounter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private HashMap<String, Integer> getSocialCreditMapFromJSON() {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Integer> map = new HashMap<>();
        try {
            map = mapper.readValue(new File("socialCreditsMap.json"), HashMap.class);
        }catch (Exception e){
            saveSocialCredits();
            e.printStackTrace();
        }
        return map;
    }

    public HashMap<String, Integer> getSocialCreditsCounter() {
        return socialCreditCounter;
    }

    public void addSocialCredit(Player player, int points){

        if(!socialCreditCounter.containsKey(player.getName())){
            socialCreditCounter.put(player.getName(), points);
        }else{
            int currentScore = this.getScore(player);
            socialCreditCounter.replace(player.getName(), currentScore + points);
        }

        if(points<2) {
            player.sendMessage(ChatColor.GREEN + "+" + points + " Social Credit !");
        }else{
            player.sendMessage(ChatColor.GREEN + "+" + points + " Social Credits !");
        }
    }

    public void removeSocialCredit(Player player, int points){

        if(!socialCreditCounter.containsKey(player.getName())){
            socialCreditCounter.put(player.getName(), points);
        }else{
            int currentScore = this.getScore(player);
            socialCreditCounter.replace(player.getName(), currentScore - points);
        }
        if(points<2) {
            player.sendMessage(ChatColor.RED + "-" + points + " Social Credit !");
        }else{
            player.sendMessage(ChatColor.RED + "-" + points + " Social Credits !");
        }
    }

    public void setSocialCredit(Player player, int points){
        socialCreditCounter.put(player.getName(), points);
    }

    public int getScore(Player player){
        if(!socialCreditCounter.containsKey(player.getName())){
            socialCreditCounter.put(player.getName(), 0);
        }
        return socialCreditCounter.get(player.getName());
    }

    private void startLoop(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SocialCreditSystem.getInstance(), this::tick, 10L, 2L);
    }

    private void tick() {
        long current = System.currentTimeMillis();
        if (updateTime <= current) {
            saveSocialCredits();
            System.out.println("saved social credits.");
            updateTime = current + 60000L;
        }
    }
}
