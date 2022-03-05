package me.necroliner.socialcreditsplugin;

import me.necroliner.socialcreditsplugin.commands.SCS;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class SocialCreditSystem extends JavaPlugin {

    public static final String LOGGER_NAME = "[SocialCreditsSystem-1.0-SNAPSHOT] : ";

    @Override
    public void onEnable() {
        SocialCreditsManager scManager = new SocialCreditsManager(getServer().getScoreboardManager());

        getServer().getPluginManager().registerEvents(new PlayerAction(scManager), this);

        getCommand("scs").setExecutor(new SCS(scManager));

        System.out.println(SocialCreditSystem.LOGGER_NAME + "Finished plugin startup.");
    }

    @Override
    public void onDisable() {

    }
}
