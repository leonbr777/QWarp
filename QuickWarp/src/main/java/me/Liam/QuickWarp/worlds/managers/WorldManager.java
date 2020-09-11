package me.Liam.QuickWarp.worlds.managers;

import me.Liam.QuickWarp.WarpsMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class WorldManager {

    private WarpsMain plugin;
    
    private String name;
    private Boolean isPVP;
    private Boolean isHardcore;
    private World world;
    private Boolean isUnloaded;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Boolean getPVP() { return isPVP; }
    public void setPVP(Boolean PVP) { isPVP = PVP; }
    
    public Boolean getHardcore() { return isHardcore; }
    public void setHardcore(Boolean hardcore) { isHardcore = hardcore; }
    
    public World getWorld() { return world; }
    public void setWorld(World world) { this.world = world; }
    
    public Boolean getUnloaded() { return isUnloaded; }
    public void setUnloaded(Boolean unloaded) { isUnloaded = unloaded; }
    
    public WorldManager(WarpsMain plugin, String name, Boolean isPVP, Boolean isHardcore){
        this.plugin = plugin;
        this.name = name;
        this.isPVP = isPVP;
        this.isHardcore = isHardcore;
        createWorld();
    }
    
    public void createWorld(){
        try {
            Bukkit.broadcastMessage(ChatColor.AQUA + "Creating world...");
            WorldCreator creator = new WorldCreator(name);
            World w = creator.createWorld();
            this.world = w;
            world.setPVP(isPVP);
            world.setHardcore(isHardcore);
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Created world " + ChatColor.GOLD + "\"" + name + "\"" + ChatColor.GREEN + "!");
            return;
        } catch (Exception o){
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not create world! Error: " + o.getCause());
        }
        
    }
    
    public void unloadWorld(){
        if (!(getWorld() == null)){
            if (getWorld().getPlayers().isEmpty()){
                Bukkit.getServer().unloadWorld(getWorld(), true);
                Bukkit.getConsoleSender().sendMessage(plugin.chatUtil.colorize("&aThe world &6\"" + getName() + "\" &ahas been unloaded!"));
                setUnloaded(true);
                return;
            }
            Bukkit.getConsoleSender().sendMessage(plugin.chatUtil.colorize("&cThe world &6\"" + getName() + "\" &ccould not be unloaded because players were found on that world."));
            return;
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "An error occured while trying to unload a world! (Null error, World does not exist)");
        return;
    }
    
    public void deleteFiles(File file) throws IOException {
        if (file.isDirectory()) {
            File[] entries = file.listFiles();
            if (entries != null) {
                for (File entry : entries) {
                    deleteFiles(entry);
                }
            }
        }
        if (!file.delete()) {
            throw new IOException("Failed to delete " + file);
        }
    }
    
    public void deleteWorld(File file){
        if (getUnloaded()){
            if (file.exists()){
                File world = getWorld().getWorldFolder();
                Arrays.stream(world.listFiles()).forEach(file1 -> {
                    file1.deleteOnExit();
                });
                world.getAbsoluteFile().delete();
                world.delete();
                world.deleteOnExit();
                Bukkit.getConsoleSender().sendMessage(plugin.chatUtil.colorize("&aThe world &6\"" + getName() + "\" &ahas been deleted!"));
                return;
            }
        }
        
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "An error occured! Either the world file could not be found, or the world is not yet unloaded.");
        return;
    }
    
}
