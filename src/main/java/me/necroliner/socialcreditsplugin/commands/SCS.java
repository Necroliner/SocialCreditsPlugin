package me.necroliner.socialcreditsplugin.commands;

import me.necroliner.socialcreditsplugin.SocialCreditsManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.management.PlatformLoggingMXBean;

public class SCS implements CommandExecutor {

    private SocialCreditsManager scManager;

    public SCS(SocialCreditsManager scManager) {
        this.scManager = scManager;
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
                player.sendMessage(ChatColor.GOLD + "Social Credits : " + ChatColor.WHITE +  scManager.getScore(player.getName()));
                player.sendMessage(ChatColor.WHITE + "-----------------------------------------------------");
                player.sendMessage(ChatColor.GOLD + "Type /scs help for more infos");
                player.sendMessage(ChatColor.GRAY + "we know everything, we see everything.");
            }else{
                if( args[0].equalsIgnoreCase("help")){
                    player.sendMessage(ChatColor.GOLD + "This server is running the glorious " +ChatColor.RED+ "Social Credits System ");
                    player.sendMessage(ChatColor.WHITE + "This plugin is still a WIP, do not hesitate to make suggestions on discord" );
                    player.sendMessage( ChatColor.GRAY + "${project.version} by Necroliner");

                }else if(args[0].equalsIgnoreCase("")){

                }
            }

        }
        return true;
    }
}
