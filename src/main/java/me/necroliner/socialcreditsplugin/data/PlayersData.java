package me.necroliner.socialcreditsplugin.data;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.UUID;

public class PlayersData{

    private final EnumMap<Material, HashMap<UUID, Integer>> materialsCounter;

    public PlayersData(){
        materialsCounter = new EnumMap<>(Material.class);
        Datasets data = new Datasets();
        data.cropThresholds.forEach((k, v) -> materialsCounter.put(k, new HashMap<>()));
        data.oreThresholds.forEach((k, v) -> materialsCounter.put(k, new HashMap<>()));
        System.out.println("playerData initialised, materials : " + Arrays.toString(materialsCounter.entrySet().toArray()));
    }

    public EnumMap<Material, HashMap<UUID, Integer>> getMap(){
        return materialsCounter;
    }


}
