package de.berstanio.kistenoeffner;

import de.berstanio.lobby.bukkit.BukkitMain;
import de.berstanio.lobby.bukkit.LobbyPlayer;
import de.berstanio.lobby.bukkit.gadgets.Gadget;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class LobbyChest {

    private LobbyPlayer lobbyPlayer;

    public LobbyChest(LobbyPlayer lobbyPlayer) {
        setLobbyPlayer(lobbyPlayer);
    }

    public void open(){
        if (new Random().nextInt(20) == 0) {
            Gadget gadget = BukkitMain.getInstance().getGadgets().stream().findAny().get();
            if (getLobbyPlayer().getBoughtGadgets().contains(gadget)) {
                open();
                return;
            }
            switch (gadget.getRarity().name()) {
                case "LEGENDARY":
                    if (new Random().nextInt(20) == 0) {
                        startAnimation(gadget);
                    } else {
                        open();
                        return;
                    }
                    break;
                case "EPIC":
                    if (new Random().nextInt(15) == 0) {
                        startAnimation(gadget);
                    } else {
                        open();
                        return;
                    }
                    break;
                case "RARE":
                    if (new Random().nextInt(5) == 0) {
                        startAnimation(gadget);
                    } else {
                        open();
                        return;
                    }
                    break;
                case "COMMON":
                    if (new Random().nextInt(1) == 0) {
                        startAnimation(gadget);
                    } else {
                        open();
                        return;
                    }
                    break;
            }
        }else {
            startAnimation(randomMoney());
        }
    }

    public int randomMoney(){
        Random random = new Random();
        int randomNr = random.nextInt(1000) + random.nextInt(1000) + random.nextInt(1000) + random.nextInt(1000) + random.nextInt(1000);
        int transformedRandomNr = (Math.abs(randomNr - 2500) * 2);
        if (transformedRandomNr < 100){
            transformedRandomNr = 100;
        }
        return transformedRandomNr;
    }

    public Material transformIntToMaterial(int number){
        if (number < 1000 ) {
            return Material.GOLD_NUGGET;
        }else if (number < 2000) {
            return Material.GOLD_INGOT;
        }else if (number >= 4900){
            return Material.GOLD_ORE;
        } else {
            return Material.GOLD_BLOCK;
        }
    }

    public void startAnimation(Object award) {
        ArrayList<Object> items = new ArrayList<>(BukkitMain.getInstance().getGadgets());
        for (int i = 0; i < BukkitMain.getInstance().getGadgets().size() + getLobbyPlayer().getBoughtGadgets().size(); i++) {
            items.add(randomMoney());
        }
        items.removeAll(getLobbyPlayer().getBoughtGadgets());
        Collections.shuffle(items);
        items.add(award);
        items.forEach(o -> {
            if (o instanceof Gadget){
                items.set(items.indexOf(o), ((Gadget) o).getItemStack());
            }else {
                items.set(items.indexOf(o), BukkitMain.getInstance().renameItem(new ItemStack(transformIntToMaterial((int) o)), ChatColor.GOLD + "Gold", ChatColor.GOLD + "Das enthält " + o + " Coins"));
            }
        });
        ArrayList<ItemStack> stacks = new ArrayList<>();
        items.forEach(o -> stacks.add((ItemStack) o));
        Inventory inventory = Bukkit.createInventory(null, 45, "Lotterie");
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        BukkitMain.getInstance().renameItem(itemStack, " ", "");
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, itemStack);
        }
        inventory.getItem(13).setDurability((short) 5);
        inventory.getItem(31).setDurability((short) 5);
        inventory.setItem(20, stacks.get(0));
        inventory.setItem(21, stacks.get(1));
        inventory.setItem(22, stacks.get(2));
        inventory.setItem(23, stacks.get(3));
        inventory.setItem(24, stacks.get(4));
        getLobbyPlayer().getPlayer().openInventory(inventory);
        AtomicInteger taskID = new AtomicInteger(0);
        AtomicInteger currentItem = new AtomicInteger(0);
        AtomicInteger delay = new AtomicInteger(2);
        AtomicInteger overDelay = new AtomicInteger(1);
        taskID.set(Bukkit.getScheduler().scheduleSyncRepeatingTask(de.berstanio.kistenoeffner.BukkitMain.getInstance(), () -> {
            overDelay.set(overDelay.get() + 1);
            if (overDelay.get() == delay.get()) {
                if (stacks.size() == currentItem.get() + 4){
                    delay.set(delay.get() + 1);
                    overDelay.set(0);
                    inventory.setItem(20, stacks.get(currentItem.get()));
                    inventory.setItem(21, stacks.get(currentItem.get() + 1));
                    inventory.setItem(22, stacks.get(currentItem.get() + 2));
                    inventory.setItem(23, stacks.get(currentItem.get() + 3));
                    inventory.setItem(24, stacks.get(0));
                    currentItem.set(currentItem.get() + 1);
                    return;
                }else if (stacks.size() == currentItem.get() + 3){
                    delay.set(delay.get() + 1);
                    overDelay.set(0);
                    inventory.setItem(20, stacks.get(currentItem.get()));
                    inventory.setItem(21, stacks.get(currentItem.get() + 1));
                    inventory.setItem(22, stacks.get(currentItem.get() + 2));
                    inventory.setItem(23, stacks.get(0));
                    inventory.setItem(24, stacks.get(1));
                    currentItem.set(currentItem.get() + 1);
                    return;
                }else if (stacks.size() == currentItem.get() + 2){
                    delay.set(delay.get() + 1);
                    overDelay.set(0);
                    inventory.setItem(20, stacks.get(currentItem.get()));
                    inventory.setItem(21, stacks.get(currentItem.get() + 1));
                    inventory.setItem(22, stacks.get(0));
                    inventory.setItem(23, stacks.get(1));
                    inventory.setItem(24, stacks.get(2));
                    currentItem.set(currentItem.get() + 1);
                    return;
                }else if (stacks.size() == currentItem.get() + 1){
                    delay.set(delay.get() + 1);
                    overDelay.set(0);
                    inventory.setItem(20, stacks.get(currentItem.get()));
                    inventory.setItem(21, stacks.get(0));
                    inventory.setItem(22, stacks.get(1));
                    inventory.setItem(23, stacks.get(2));
                    inventory.setItem(24, stacks.get(3));
                    currentItem.set(currentItem.get() + 1);
                    return;
                }else if (stacks.size() == currentItem.get()){
                    delay.set(delay.get() + 1);
                    overDelay.set(0);
                    inventory.setItem(20, stacks.get(0));
                    inventory.setItem(21, stacks.get(1));
                    inventory.setItem(22, stacks.get(2));
                    inventory.setItem(23, stacks.get(3));
                    inventory.setItem(24, stacks.get(4));
                    currentItem.set(1);
                    return;
                }
                delay.set(delay.get() + 1);
                overDelay.set(0);
                inventory.setItem(20, stacks.get(currentItem.get()));
                inventory.setItem(21, stacks.get(currentItem.get() + 1));
                inventory.setItem(22, stacks.get(currentItem.get() + 2));
                inventory.setItem(23, stacks.get(currentItem.get() + 3));
                inventory.setItem(24, stacks.get(currentItem.get() + 4));
                currentItem.set(currentItem.get() + 1);
            }else if (delay.get() == (stacks.size() * 2)){
                if (award instanceof Gadget) {
                    getLobbyPlayer().getBoughtGadgets().add((Gadget) award);
                    getLobbyPlayer().getPlayer().sendMessage(ChatColor.GREEN + "Du hast das Gadget namens " + ((Gadget) award).getName() + " erhalten!");
                }else {
                    getLobbyPlayer().setCoins(getLobbyPlayer().getCoins() + (int) award);
                    getLobbyPlayer().getPlayer().sendMessage(ChatColor.GREEN + "Du hast " + ChatColor.GOLD + award + ChatColor.GREEN +  " Coins erhalten!");
                }
                Bukkit.getScheduler().cancelTask(taskID.get());
            }
        },0, 1));
    }


    public LobbyPlayer getLobbyPlayer() {
        return lobbyPlayer;
    }

    public void setLobbyPlayer(LobbyPlayer lobbyPlayer) {
        this.lobbyPlayer = lobbyPlayer;
    }
}
