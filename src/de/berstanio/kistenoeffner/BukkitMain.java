package de.berstanio.kistenoeffner;

import de.berstanio.lobby.bukkit.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BukkitMain extends JavaPlugin {

    private static BukkitMain instance;
    private int lastDay;

    @Override
    public void onEnable() {
        setInstance(this);
        System.out.println("Gestertet!");
        startChestTimer();
        getServer().getPluginManager().registerEvents(new Events(), this);
    }

    public void startChestTimer(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            int day = Integer.parseInt(new SimpleDateFormat().format(new Date()).substring(0, 2));
            if (day > getLastDay() || (day == 1 && day != getLastDay())){
                de.berstanio.lobby.bukkit.BukkitMain.getInstance().getLobbyPlayers().forEach(lobbyPlayer -> lobbyPlayer.setChests(lobbyPlayer.getChests() + 1));
                setLastDay(day);
            }

        },0,360000);
}

    public int getLastDay() {
        return lastDay;
    }

    public void setLastDay(int lastDay) {
        this.lastDay = lastDay;
    }

    public static BukkitMain getInstance() {
        return instance;
    }

    public void setInstance(BukkitMain instance) {
        BukkitMain.instance = instance;
    }
}
