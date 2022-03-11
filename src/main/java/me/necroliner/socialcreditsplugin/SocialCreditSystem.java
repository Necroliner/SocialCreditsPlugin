package me.necroliner.socialcreditsplugin;

import me.necroliner.socialcreditsplugin.commands.SCS;
import me.necroliner.socialcreditsplugin.data.NameManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SocialCreditSystem extends JavaPlugin {

    private static SocialCreditSystem instance;
    private static PlayersData playersData;
    public static Logger LOGGER;

    @Override
    public void onEnable() {
        LOGGER = this.getLogger();
        instance = this;
        playersData = new PlayersData();
        StatsDisplayManager sdManager = new StatsDisplayManager(playersData);
        NameManager nameManager = new NameManager();
        sdManager.startLoop();
        nameManager.startLoop();

        getServer().getPluginManager().registerEvents(new PlayerAction(playersData), instance);
        getServer().getPluginManager().registerEvents(sdManager, instance);

        Objects.requireNonNull(getCommand("scs")).setExecutor(new SCS(playersData));

        SocialCreditSystem.LOGGER.log(Level.INFO, " Finished plugin startup");
    }

    @Override
    public void onDisable() {
        SocialCreditSystem.LOGGER.log(Level.INFO,"Saving plugin data...");
        playersData.saveSocialCredits();
        SocialCreditSystem.LOGGER.log(Level.INFO,"SocialCreditSystem unloaded.");
    }

    public static SocialCreditSystem getInstance() {
        return instance;
    }
}
