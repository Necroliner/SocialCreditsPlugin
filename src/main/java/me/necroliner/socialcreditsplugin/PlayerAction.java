package me.necroliner.socialcreditsplugin;


import me.necroliner.socialcreditsplugin.data.Datasets;
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
import org.bukkit.event.entity.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import java.util.*;

public class PlayerAction implements Listener {

    private final Datasets datas;
    private final EnumMap<Material, HashMap<UUID, Integer>> materialCounter;
    private final PlayersData playersData;

    public PlayerAction(PlayersData playersData){

        this.materialCounter = playersData.getMaterialsCounterMap();
        this.playersData = playersData;
        this.datas = Datasets.getDataset();
    }

    public boolean isSilkTouch( Player player){

        ItemStack itemUsedMainHand = player.getInventory().getItemInMainHand();
        ItemStack itemUsedOffHand = player.getInventory().getItemInOffHand();

        return (itemUsedMainHand.containsEnchantment(Enchantment.SILK_TOUCH) || itemUsedOffHand.containsEnchantment(Enchantment.SILK_TOUCH));
    }

    private void handleCropDrop(ItemStack k, Player player) {
        Material material = k.getType();
        if(!materialCounter.containsKey(material) && !datas.cropReward.containsKey(material)){
            return;
        }

        if(!materialCounter.get(material).containsKey(player.getUniqueId())){
            materialCounter.get(material).put(player.getUniqueId(), k.getAmount()-1);
        }

        int lootedTotal = materialCounter.get(material).get(player.getUniqueId()) + k.getAmount();
        int threshold = datas.cropThresholds.get(material);

        if(lootedTotal >= threshold){
            materialCounter.get(material).replace(player.getUniqueId(), lootedTotal - threshold );
            playersData.addSocialCredit(player, datas.cropReward.get(material));
        }else {
            materialCounter.get(material).replace(player.getUniqueId(), lootedTotal);
        }
        notifyPlayer(player,material);
    }

    private void handleOre(Player player, Block block) {
        Material material = block.getType();

        if(!materialCounter.get(material).containsKey(player.getUniqueId())){
            materialCounter.get(material).put(player.getUniqueId(), 1);
        }else{
            int brokenBlocks = materialCounter.get(material).get(player.getUniqueId());
            int threshold = datas.oreThresholds.get(material);

            if (brokenBlocks+1 >= threshold) {
                materialCounter.get(material).replace(player.getUniqueId(), 0);
                playersData.addSocialCredit(player, datas.oreReward.get(material));
            } else {
                materialCounter.get(material).replace(player.getUniqueId(), brokenBlocks + 1);
            }
        }
        notifyPlayer(player,material);
    }

    private void notifyPlayer(Player player, Material material){
        String message;
        if(datas.oreThresholds.containsKey(material)){
            message = "§6" + materialCounter.get(material).get(player.getUniqueId()) + "§f/" + datas.oreThresholds.get(material) + " " + Datasets.getPrettyName(material);
        }else if(datas.cropThresholds.containsKey(material)){
            message = "§6" + materialCounter.get(material).get(player.getUniqueId()) + "§f/" + datas.cropThresholds.get(material) + " " + Datasets.getPrettyName(material);
        }else{
            return;
        }

        Datasets.sendActionBar(player,message);
    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setFormat("%s: %s");
    }

    @EventHandler
    public void onBlockBroken(BlockBreakEvent e){
        Block brokenBlock = e.getBlock();
        Player player = e.getPlayer();

        if(Datasets.isHarvestableRightClick(brokenBlock.getType())){
            Ageable blockData = (Ageable) brokenBlock.getBlockData();
            if(blockData.getAge() == blockData.getMaximumAge()) {
                Datasets.sendActionBar(player,ChatColor.GOLD + "You can earn social credits by harvesting these crops with right click (empty handed)");
            }
        }

        if(isSilkTouch(player)) {
            return;
        }

        if(datas.oreReward.containsKey(brokenBlock.getType())){
            if(datas.oreThresholds.containsKey(brokenBlock.getType())){
                handleOre(player, brokenBlock);
            }else{
                playersData.addSocialCredit(player, datas.oreReward.get(brokenBlock.getType()));
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
                    Datasets.sendActionBar(player,ChatColor.GRAY + "This crop isn't fully grown yet !");
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamagedByEntity(EntityDamageByEntityEvent e){
        if(e.getDamager().getType() == EntityType.PLAYER) {
            Player damager = (Player) e.getDamager();
            if (e.getEntity().getType() == EntityType.PLAYER && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                playersData.removeSocialCredit(damager,(int) (e.getFinalDamage() + 2));
            } else if (e.getEntity().getType() == EntityType.VILLAGER) {
                playersData.removeSocialCredit(damager,  2);
            }
        }
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(event.getEntity() instanceof Creeper){
            if(event.getEntity().getKiller() != null) {
                playersData.addSocialCredit(event.getEntity().getKiller(), 2);
            }
        }
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event){
        if(event.getEntity().getType() == EntityType.CREEPER){
            Creeper creeper = (Creeper) event.getEntity();
            if(creeper.getTarget().getType() == EntityType.PLAYER){
                Player player = (Player) creeper.getTarget();
                playersData.removeSocialCredit(player, 10);
            }
        }
    }
}

