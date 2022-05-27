package me.necroliner.socialcreditsplugin.data;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Locale;

public class Datasets {

    public final EnumMap<Material, Integer> oreThresholds;
    public final EnumMap<Material, Integer> oreReward;
    public final EnumMap<Material, Integer> cropReward;
    public final EnumMap<Material, Integer> cropThresholds;

    private static Datasets datasets = null;

    private Datasets(){
        oreThresholds = getOreThresholds();
        oreReward = getOreReward();
        cropReward = getCropReward();
        cropThresholds = getCropThresholds();
    }

    public static Datasets getDataset(){
        if(datasets == null){
            datasets = new Datasets();
        }
        return datasets;
    }


    private EnumMap<Material, Integer> getOreThresholds(){
        EnumMap<Material, Integer> map = new EnumMap<>(Material.class);
        map.put(Material.STONE, 64);
        map.put(Material.IRON_ORE, 4);
        map.put(Material.DEEPSLATE_IRON_ORE, 4);
        map.put(Material.COPPER_ORE, 8);
        map.put(Material.DEEPSLATE_COPPER_ORE, 8);
        return map;
    }

    private EnumMap<Material, Integer> getOreReward() {
        EnumMap<Material, Integer> map = new EnumMap<>(Material.class);
        map.put(Material.STONE, 1);
        map.put(Material.COPPER_ORE, 1);
        map.put(Material.DEEPSLATE_COPPER_ORE, 2);
        map.put(Material.IRON_ORE, 1);
        map.put(Material.DEEPSLATE_IRON_ORE, 2);
        map.put(Material.GOLD_ORE, 2);
        map.put(Material.DEEPSLATE_GOLD_ORE, 3);
        map.put(Material.DIAMOND_ORE, 5);
        map.put(Material.DEEPSLATE_DIAMOND_ORE, 6);
        map.put(Material.EMERALD_ORE, 25);
        map.put(Material.DEEPSLATE_EMERALD_ORE, 30);
        map.put(Material.ANCIENT_DEBRIS, 15);
        return map;
    }
    private EnumMap<Material, Integer> getCropThresholds(){
        EnumMap<Material, Integer> map = new EnumMap<>(Material.class);
        map.put(Material.WHEAT, 48);
        map.put(Material.CARROT, 48);
        map.put(Material.POTATO, 48);
        map.put(Material.BEETROOT, 48);
        map.put(Material.NETHER_WART, 48);
        map.put(Material.COCOA_BEANS, 48);
        return map;
    }

    private EnumMap<Material, Integer> getCropReward(){
        EnumMap<Material, Integer> map = new EnumMap<>(Material.class);
        map.put(Material.WHEAT, 1);
        map.put(Material.CARROT, 1);
        map.put(Material.POTATO, 1);
        map.put(Material.BEETROOT, 1);
        map.put(Material.NETHER_WART, 1);
        map.put(Material.COCOA_BEANS, 1);
        return map;
    }

    public static boolean isHarvestableRightClick(Material material){
        ArrayList<Material> harvestableList = new ArrayList<>();
        harvestableList.add(Material.WHEAT);
        harvestableList.add(Material.CARROTS);
        harvestableList.add(Material.POTATOES);
        harvestableList.add(Material.BEETROOTS);
        harvestableList.add(Material.NETHER_WART);
        harvestableList.add(Material.COCOA_BEANS);
        return harvestableList.contains(material);
    }

    public static String getPrettyName(Material material){
        String name = material.name().toLowerCase(Locale.ROOT).replace("_", " ");

        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static void sendActionBar(Player player, String message){
        TextComponent textComponent;
        textComponent = Component.text(message);
        player.sendActionBar(textComponent);
    }
}
