package me.necroliner.socialcreditsplugin.commands;

import me.necroliner.socialcreditsplugin.PlayersData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;

import javax.tools.Tool;

public class Repair implements CommandExecutor {

    PlayersData playersData;

    public Repair(PlayersData playersData){
        this.playersData = playersData;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This Command must be issued by players.");
            return true;
        }
        Player player = (Player) sender;

        if(!command.getName().equalsIgnoreCase("repair")){return true;}

        if(player.getInventory().getItemInMainHand().getAmount() == 0){
            player.sendMessage(ChatColor.GOLD + "nothing to repair");
            return true;
        }else if(!(player.getInventory().getItemInMainHand() instanceof Tool)){
            player.sendMessage(ChatColor.GOLD + "this is not a repairable item");
            return true;
        }

        org.bukkit.inventory.meta.Damageable damageable = (Damageable) player.getInventory().getItemInMainHand().getItemMeta();

        int currentDurability = damageable.getDamage();
        int maxDurability = player.getInventory().getItemInMainHand().getType().getMaxDurability();
        int toRepair = (maxDurability - currentDurability)/2;

        if(args.length == 0){
            player.sendMessage(ChatColor.GOLD + "if you fully repair your current item it will cost you " + ChatColor.WHITE + toRepair + ChatColor.GOLD + " type /repair confirm to continue");
        } else if (args.length == 1 && args[0].equalsIgnoreCase("confirm")) {
            damageable.setDamage(0);
            playersData.removeSocialCredit(player, toRepair);
        }
        return true;
    }
}
