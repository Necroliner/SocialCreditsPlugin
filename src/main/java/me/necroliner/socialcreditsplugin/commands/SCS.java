package me.necroliner.socialcreditsplugin.commands;

import me.necroliner.socialcreditsplugin.SocialCreditSystem;
import me.necroliner.socialcreditsplugin.PlayersData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SCS implements CommandExecutor {

    private PlayersData playersData;

    public SCS(PlayersData playersData) {
        this.playersData = playersData;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("This Command must be issued by players.");
            return true;
        }
        Player player = (Player) sender;

        if(command.getName().equalsIgnoreCase("scs")){



            if(args.length == 0){
                player.sendMessage(ChatColor.WHITE + "-----------------------------------------------------");
                player.sendMessage(ChatColor.GOLD + "Citizen : " + ChatColor.WHITE + player.getName());
                player.sendMessage(ChatColor.GOLD + "IP Address : " + ChatColor.WHITE + (player.getAddress()));
                player.sendMessage(ChatColor.GOLD + "Level : " + ChatColor.WHITE + (player.getLevel()));
                player.sendMessage(ChatColor.GOLD + "Social Credits : " + ChatColor.WHITE +  playersData.getScore(player));
                player.sendMessage(ChatColor.WHITE + "-----------------------------------------------------");
                player.sendMessage(ChatColor.GOLD + "Type /scs help for more infos");
                player.sendMessage(ChatColor.GRAY + "we know everything, we see everything.");
                return true;
            }else if(args.length == 1){
                if( args[0].equalsIgnoreCase("help")){
                    player.sendMessage(ChatColor.GOLD + "This server is running the glorious " +ChatColor.RED+ "Social Credits System ");
                    player.sendMessage(ChatColor.WHITE + "This plugin is still a WIP, do not hesitate to make suggestions on discord" );
                    player.sendMessage( ChatColor.GRAY + SocialCreditSystem.getInstance().getDescription().getVersion() + " by Necroliner");

                }else if(args[0].equalsIgnoreCase("toggle")){
                    if(playersData.getIgnoreBoard().contains(player.getUniqueId())){
                        playersData.removePlayerHiddenStats(player);
                    }else{
                        playersData.addPlayerHiddenStats(player);
                    }
                }/*else if(args[0].equalsIgnoreCase("top")){
                    player.sendMessage("Top 25 players :");

                    sortHashMap(this.playersData.getSocialCreditsCounter())
                            .forEach((k, v) -> player.sendMessage(ChatColor.GOLD + k + " : " + ChatColor.WHITE + v));
                    return true;
                }*/
            }else if(args.length == 2){
                if(args[0].equalsIgnoreCase("get") ){
                    Player target = Bukkit.getPlayer(args[1]);
                    if(target == null){
                        sender.sendMessage(ChatColor.RED + "Player inexistant or offline");
                    }else {
                        sender.sendMessage(ChatColor.GOLD + target.getName() + "'s Social credits : " + ChatColor.WHITE + playersData.getScore(target));
                    }
                }
            }else if(args.length == 3){
                if(args[0].equalsIgnoreCase("set") ){
                    if(!sender.isOp()){
                        sender.sendMessage(ChatColor.RED + "You do not have enough permissions to perform this action");
                        return true;
                    }
                    Player target = Bukkit.getPlayer(args[1]);
                    int points = Integer.parseInt(args[2]);
                    if(target == null){
                        sender.sendMessage(ChatColor.RED + "Player inexistant or offline");
                    }else{
                        playersData.setSocialCredit(target, points);
                        sender.sendMessage(ChatColor.GREEN + "Done ! " + target.getName() + "'s Social credits set to " + points );
                    }
                }
            }

        }
        return true;
    }


}
