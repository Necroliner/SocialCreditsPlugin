package me.necroliner.socialcreditsplugin;

import me.necroliner.socialcreditsplugin.commands.SCS;
import me.necroliner.socialcreditsplugin.data.PlayersData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SocialCreditSystem extends JavaPlugin {

    public static final String LOGGER_NAME = "[SocialCreditsSystem-1.0-SNAPSHOT] : ";

    @Override
    public void onEnable() {

        StatsDisplayManager sdManager = new StatsDisplayManager(getServer().getScoreboardManager());
        SocialCreditsManager scManager = new SocialCreditsManager(getServer().getScoreboardManager());

        PlayersData playersData = new PlayersData();

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
