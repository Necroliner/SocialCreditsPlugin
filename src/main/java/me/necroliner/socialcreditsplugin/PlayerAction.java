package me.necroliner.socialcreditsplugin;


import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class PlayerAction implements Listener {

    public static final int STONE_TRESHOLD = 128;
    public static final int IRON_TRESHOLD = 4;
    private final SocialCreditsManager scManager;
    private final HashMap<UUID,Integer> stoneCounter;
    private final HashMap<UUID,Integer> ironCounter;

    public PlayerAction(SocialCreditsManager scManager ){
        this.scManager = scManager;
        stoneCounter = new HashMap<>();
        ironCounter = new HashMap<>();
    }

    public boolean isSilkTouch( Player player){

        ItemStack itemUsedMainHand = player.getInventory().getItemInMainHand();
        ItemStack itemUsedOffHand = player.getInventory().getItemInOffHand();

        return (itemUsedMainHand.containsEnchantment(Enchantment.SILK_TOUCH) || itemUsedOffHand.containsEnchantment(Enchantment.SILK_TOUCH));
    }

    @EventHandler
    public void onBlockBroken(BlockBreakEvent e){
        Block brokenBlock = e.getBlock();
        Player player = e.getPlayer();

        if(!isSilkTouch(player)) {
            switch (brokenBlock.getType()) {
                case ANCIENT_DEBRIS:
                    scManager.addPoints(player, 15);
                    break;
                case EMERALD_ORE:
                case DEEPSLATE_EMERALD_ORE:
                    scManager.addPoints(player, 10);
                    break;
                case DIAMOND_ORE:
                case DEEPSLATE_DIAMOND_ORE:
                    scManager.addPoints(player, 5);
                    break;
                case GOLD_ORE:
                case DEEPSLATE_GOLD_ORE:
                    scManager.addPoints(player, 1);
                    break;
                case IRON_ORE:
                case DEEPSLATE_IRON_ORE:
                    if(handleIron(player)){
                        scManager.addPoints(player, 1);
                    }
                    break;
                case DEEPSLATE:
                case STONE:
                    if(handleStone(player)){
                        scManager.addPoints(player, 2);
                    }
                    break;
                default:
                    break;
            }

        }
    }

    private boolean handleIron(Player player) {
        if(!ironCounter.containsKey(player.getUniqueId())) {
            ironCounter.put(player.getUniqueId(), 1);
        }
        int ironBroken = ironCounter.get(player.getUniqueId());

        if(ironBroken >= IRON_TRESHOLD){
            ironCounter.replace(player.getUniqueId(), 1);
            player.sendMessage(ChatColor.GRAY + Integer.toString(ironBroken) + "/"+IRON_TRESHOLD+" iron");
            return true;
        }else {
            ironCounter.replace(player.getUniqueId(), ironBroken  + 1);
        }
        player.sendMessage(ChatColor.GRAY + Integer.toString(ironBroken) + "/"+IRON_TRESHOLD+" iron");
        return false;
    }

    private boolean handleStone(Player player) {
        if(!stoneCounter.containsKey(player.getUniqueId())) {
            stoneCounter.put(player.getUniqueId(), 1);
        }
        int stoneBroken = stoneCounter.get(player.getUniqueId());

        if(stoneBroken >= STONE_TRESHOLD){
            stoneCounter.replace(player.getUniqueId(), 0);
            player.sendMessage(ChatColor.GRAY + Integer.toString(stoneBroken) + "/"+STONE_TRESHOLD+" stone gathering complete !");
            return true;
        }else {
            stoneCounter.replace(player.getUniqueId(), stoneBroken  + 1);
        }
        if(stoneBroken % 8 == 0) {
            player.sendMessage(ChatColor.GRAY + Integer.toString(stoneBroken) + "/" + STONE_TRESHOLD + " stone");
        }
        return false;
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e){
        if(e.getDamager().getType() == EntityType.PLAYER) {
            Player damager = (Player) e.getDamager();
            if (e.getEntity().getType() == EntityType.PLAYER && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                scManager.removePoints(damager, (int) e.getFinalDamage() + 3);
            } else if (e.getEntity().getType() == EntityType.VILLAGER) {
                scManager.removePoints(damager, 2);
            }
        }
    }


}
