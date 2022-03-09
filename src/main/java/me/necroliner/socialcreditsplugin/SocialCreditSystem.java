package me.necroliner.socialcreditsplugin;

import me.necroliner.socialcreditsplugin.commands.SCS;
import me.necroliner.socialcreditsplugin.data.NameManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;

public final class SocialCreditSystem extends JavaPlugin {

    public static final String LOGGER_NAME = "[SocialCreditsSystem-1.0-SNAPSHOT] : ";

    private static SocialCreditSystem instance;
    private static PlayersData playersData;
    @Override
    public void onEnable() {
        this.instance = this;
        this.playersData = new PlayersData();
        StatsDisplayManager sdManager = new StatsDisplayManager(playersData);
        NameManager nameManager = new NameManager();
        sdManager.startLoop();
        nameManager.startLoop();

        getServer().getPluginManager().registerEvents(new PlayerAction(playersData), instance);
        getServer().getPluginManager().registerEvents(sdManager, instance);

        Objects.requireNonNull(getCommand("scs")).setExecutor(new SCS(playersData));

        System.out.println(SocialCreditSystem.LOGGER_NAME + "Finished plugin startup.");
    }

    @Override
    public void onDisable() {
        System.out.println("Saving plugin data...");
        playersData.saveSocialCredits();
        System.out.println("SocialCreditSystem unloaded.");
    }

    public static SocialCreditSystem getInstance() {
        return instance;
    }
}
