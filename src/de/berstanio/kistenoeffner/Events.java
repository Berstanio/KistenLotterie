package de.berstanio.kistenoeffner;

import de.berstanio.lobby.bukkit.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class Events implements Listener {

    @EventHandler
    public void onChestClick(PlayerInteractEvent e) {
        LobbyPlayer.getLobbyPlayer(e.getPlayer()).setChests(100);
        if (e.getAction().name().contains("BLOCK")) {
            // TODO: 10.12.17 Richtige Locations
            if (e.getClickedBlock().getType() == Material.CHEST && e.getClickedBlock().getLocation().equals(new Location(Bukkit.getWorld("world"),0,4,0))) {
                e.setCancelled(true);
                try {
                    if (LobbyPlayer.getLobbyPlayer(e.getPlayer()).getChests() != 0) {
                        LobbyPlayer.getLobbyPlayer(e.getPlayer()).setChests(LobbyPlayer.getLobbyPlayer(e.getPlayer()).getChests() - 1);
                        new LobbyChest(LobbyPlayer.getLobbyPlayer(e.getPlayer())).open();
                    }else {
                        e.getPlayer().sendMessage(ChatColor.RED + "Du hast keine Kisten!");
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        }
    }
}
