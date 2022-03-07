package me.necroliner.socialcreditsplugin;

import me.necroliner.socialcreditsplugin.commands.SCS;
import me.necroliner.socialcreditsplugin.data.PlayersData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SocialCreditSystem extends JavaPlugin {

    public static final String LOGGER_NAME = "[SocialCreditsSystem-1.0-SNAPSHOT] : ";

    @Override
    public void onEnable() {
        PlayersData playersData = new PlayersData();
        StatsDisplayManager sdManager = new StatsDisplayManager(getServer().getScoreboardManager(), playersData);
        SocialCreditsManager scManager = new SocialCreditsManager(getServer().getScoreboardManager(), sdManager);



        getServer().getPluginManager().registerEvents(new PlayerAction(scManager, playersData), this);
        getServer().getPluginManager().registerEvents(sdManager, this);

        Objects.requireNonNull(getCommand("scs")).setExecutor(new SCS(scManager));

        System.out.println(SocialCreditSystem.LOGGER_NAME + "Finished plugin startup.");
    }

    @Override
    public void onDisable() {
        System.out.println("SocialCreditSystem unloaded.");
    }
}
