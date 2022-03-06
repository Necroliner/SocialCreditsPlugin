package me.necroliner.socialcreditsplugin;


import me.necroliner.socialcreditsplugin.data.Datasets;
import me.necroliner.socialcreditsplugin.data.PlayersData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.UUID;

public class PlayerAction implements Listener {

    private final SocialCreditsManager scManager;
    private final Datasets datas;
    private EnumMap<Material, HashMap<UUID, Integer>> playersData;

    public PlayerAction(SocialCreditsManager scManager, PlayersData playerData){
        this.scManager = scManager;
        this.playersData = playerData.getMap();
        this.datas = new Datasets();
    }

    public boolean isSilkTouch( Player player){

        ItemStack itemUsedMainHand = player.getInventory().getItemInMainHand();
        ItemStack itemUsedOffHand = player.getInventory().getItemInOffHand();

        return (itemUsedMainHand.containsEnchantment(Enchantment.SILK_TOUCH) || itemUsedOffHand.containsEnchantment(Enchantment.SILK_TOUCH));
    }

    private void handleCropDrop(ItemStack k, Player player) {
        Material material = k.getType();
        player.sendMessage(ChatColor.RED + "item processed : " + material);
        if(!playersData.containsKey(material) && !datas.cropReward.containsKey(material)){
            return;
        }

        if(!playersData.get(material).containsKey(player.getUniqueId())){
            playersData.get(material).put(player.getUniqueId(), 1);
        }

        int lootedTotal = playersData.get(material).get(player.getUniqueId());
        int threshold = datas.cropThresholds.get(material);

        if(lootedTotal >= threshold){
            playersData.get(material).replace(player.getUniqueId(), k.getAmount());
            player.sendMessage(ChatColor.GRAY + Integer.toString(lootedTotal) + "/"+threshold + " " + Datasets.getPrettyName(material));
            scManager.addPoints(player, datas.cropReward.get(material));
            return;
        }else {
            player.sendMessage(ChatColor.RED + "current total : " + lootedTotal + " | amount just looted : " + k.getAmount() );
            lootedTotal = lootedTotal + k.getAmount();
            playersData.get(material).replace(player.getUniqueId(), lootedTotal);

        }
        player.sendMessage(ChatColor.GRAY + Integer.toString(lootedTotal) + "/"+threshold + " " + Datasets.getPrettyName(material));
    }

    private void handleOre(Player player, Block block) {
        Material material = block.getType();

        if(!playersData.get(material).containsKey(player.getUniqueId())){
            playersData.get(material).put(player.getUniqueId(), 1);
        }
        int brokenBlocks = playersData.get(material).get(player.getUniqueId());
        int threshold = datas.oreThresholds.get(material);

        if(brokenBlocks >= threshold){
            playersData.get(material).replace(player.getUniqueId(), 1);
            player.sendMessage(ChatColor.GRAY + Integer.toString(brokenBlocks) + "/"+threshold + " " + Datasets.getPrettyName(material));
            scManager.addPoints(player, datas.oreReward.get(material));
            return;
        }else {
            playersData.get(material).replace(player.getUniqueId(), brokenBlocks  + 1);
        }
        player.sendMessage(ChatColor.GRAY + Integer.toString(brokenBlocks) + "/"+threshold + " " + Datasets.getPrettyName(material));
    }

    @EventHandler
    public void onBlockBroken(BlockBreakEvent e){
        Block brokenBlock = e.getBlock();
        Player player = e.getPlayer();

        if(Datasets.isHarvestableRightClick(brokenBlock.getType()) && Math.random() > 0.7){
            player.sendMessage(ChatColor.GOLD + "You can earn social credits by harvesting these crops with right click (empty handed)");
        }

        if(isSilkTouch(player)) {
            return;
        }

        if(datas.oreReward.containsKey(brokenBlock.getType())){
            if(datas.oreThresholds.containsKey(brokenBlock.getType())){
                handleOre(player, brokenBlock);
            }else{
                scManager.addPoints(player, datas.oreReward.get(brokenBlock.getType()));
            }
        }

    }
    @EventHandler
    public void onBlockClick(PlayerInteractEvent e){

        if(e.getAction() != Action.RIGHT_CLICK_BLOCK){
            return;
        }
        if(e.getItem() == null && e.getHand().equals(EquipmentSlot.HAND)){
            Player player = e.getPlayer();
            Block clickedBlock = e.getClickedBlock();
            assert clickedBlock != null;
            if(clickedBlock.getBlockData() instanceof Ageable){
                Ageable blockData = (Ageable) clickedBlock.getBlockData();
                if(blockData.getAge() == blockData.getMaximumAge()){
                    Collection<ItemStack> drops = clickedBlock.getDrops();
                    drops.forEach(k -> handleCropDrop(k, player));
                    drops.forEach(k -> player.getWorld().dropItem(clickedBlock.getLocation(), k ));
                    blockData.setAge(0);
                    clickedBlock.setBlockData(blockData);
                }else{
                    player.sendMessage(ChatColor.GRAY + "This crop isn't fully grown yet !");
                }
            }
        }
    }



    @EventHandler
    public void onEntityDamagedByEntity(EntityDamageByEntityEvent e){
        if(e.getDamager().getType() == EntityType.PLAYER) {
            Player damager = (Player) e.getDamager();
            if (e.getEntity().getType() == EntityType.PLAYER && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                scManager.removePoints(damager, (int) e.getFinalDamage() + 2);
            } else if (e.getEntity().getType() == EntityType.VILLAGER) {
                scManager.removePoints(damager, 2);
            }
        }
    }


}
