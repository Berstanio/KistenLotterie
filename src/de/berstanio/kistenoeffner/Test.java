package de.berstanio.kistenoeffner;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.UUID;

public class Test {

    public static Class<?> skullMetaClass;
    public static Class<?> tileEntityClass;
    public static Class<?> blockPositionClass;

    static {
        String version = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            skullMetaClass = Class.forName("org.bukkit.craftbukkit." + version + ".inventory.CraftMetaSkull");

            tileEntityClass = Class.forName("net.minecraft.server." + version + ".TileEntitySkull");

            blockPositionClass = Class.forName("net.minecraft.server." + version + ".BlockPosition");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ItemStack getSkull(Player player ) {
        return getSkull( getSignature( player ), getValue( player ), 1 );
    }

    public static ItemStack getSkull(String signature, String value)
    {
        return getSkull(signature, value, 1);
    }

    public static ItemStack getSkull(String signature, String value, int amount) {
        ItemStack skull = new ItemStack( Material.SKULL_ITEM, amount, (short)3);
        SkullMeta meta = (SkullMeta)skull.getItemMeta();
        try {
            Field profileField = skullMetaClass.getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, getProfile(signature, value));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        skull.setItemMeta(meta);
        return skull;
    }

    public static  boolean setBlock(Location loc, String signature, String value) {
        return setBlock(loc.getBlock(), signature, value);
    }

    public static boolean setBlock(Block block, String signature, String value) {
        if ((block.getType() != Material.SKULL) && (block.getType() != Material.SKULL_ITEM)) {
            block.setType(Material.SKULL);
        }
        try {
            Object nmsWorld = block.getWorld().getClass().getMethod("getHandle").invoke(block.getWorld());

            Object tileEntity = null;

            Method getTileEntity = nmsWorld.getClass().getMethod("getTileEntity", blockPositionClass);

            tileEntity = tileEntityClass.cast(getTileEntity.invoke(nmsWorld, getBlockPositionFor(block.getX(), block.getY(), block.getZ())));




            tileEntityClass.getMethod("setGameProfile", GameProfile.class).invoke(tileEntity, getProfile(signature, value));
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getSignature(Player p) {
        String value = null;
        Collection<Property> pro = ((CraftPlayer)p).getHandle().getProfile().getProperties().get("textures");
        for(Property prop : pro) {
            value = prop.getSignature();
        }
        return value;
    }

    public static String getValue(Player p) {
        String value = null;
        Collection<Property> pro = ((CraftPlayer)p).getHandle().getProfile().getProperties().get("textures");
        for(Property prop : pro) {
            value = prop.getValue();
        }
        return value;
    }

    private static GameProfile getProfile(String signature, String value) {
        GameProfile profile = new GameProfile( UUID.randomUUID( ), null);
        Property property = new Property("textures", value, signature);
        profile.getProperties().put("textures", property);
        return profile;
    }

    private static Object getBlockPositionFor(int x, int y, int z) {
        Object blockPosition = null;
        try {
            Constructor<?> cons = blockPositionClass.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE);

            blockPosition = cons.newInstance(x, y, z);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blockPosition;
    }
}
