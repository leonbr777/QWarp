package me.Liam.QuickWarp;

import me.Liam.QuickWarp.warps.managers.WarpManager;
import me.Liam.QuickWarp.chatUtil.ChatUtil;
import me.Liam.QuickWarp.warps.commands.WarpCommands;
import me.Liam.QuickWarp.warps.commands.WarpTabComplete;
import me.Liam.QuickWarp.worlds.commands.WorldCommands;
import me.Liam.QuickWarp.worlds.commands.WorldTabComplete;
import me.Liam.QuickWarp.worlds.managers.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarpsMain extends JavaPlugin{
    
    private PluginManager pm;
    private ConsoleCommandSender sender;
    
    public HashMap<String, WarpManager> warps;
    public HashMap<String, WorldManager> worlds;
    public List<String> warpArgs;
    public List<String> warpCreateArgs;
    public List<String> warpRemoveArgs;
    public List<String> warpAllArgs;
    
    public List<String> worldArgs;
    public List<String> worldCreateArgs;
    public List<String> worldRemoveArgs;
    public List<String> worldAllArgs;
    
    public ChatUtil chatUtil;
    
    public void getCommands(){
        getCommand("qwarp").setExecutor(new WarpCommands(this));
        getCommand("qwarp").setTabCompleter(new WarpTabComplete(this));
        getCommand("qworld").setExecutor(new WorldCommands());
        getCommand("qworld").setTabCompleter(new WorldTabComplete(this));
    }
    public void registerEvents(){
    
    }
    public void enableMessage(){
        sender = Bukkit.getConsoleSender();
        sender.sendMessage(ChatColor.DARK_AQUA + getPlugin(this.getClass()).toString() + " Enabled!");
    }
    
    public void disableMessage(){
        sender.sendMessage(ChatColor.DARK_PURPLE + getPlugin(this.getClass()).toString() + " Disabled!");
    }
    public void initialize(){
        pm = this.getServer().getPluginManager();
        warps = new HashMap<>();
        worlds = new HashMap<>();
        chatUtil = new ChatUtil();
        warpArgs = new ArrayList<>();
        warpRemoveArgs = new ArrayList<>();
        warpCreateArgs = new ArrayList<>();
        warpAllArgs = new ArrayList<>();
        worldArgs = new ArrayList<>();
        worldCreateArgs = new ArrayList<>();
        worldRemoveArgs = new ArrayList<>();
        worldAllArgs = new ArrayList<>();
    }
    
    public void addWarpArgs(){
        if (!(warpArgs.isEmpty())){
            warpArgs.clear();
        }
        warpArgs.add("create");
        warpArgs.add("remove");
        warpArgs.add("list");
        for (String name : warps.keySet()){
            warpArgs.add(name);
        }
    }
    
    public void addWarpRemoveArgs(){
        if (!(warpRemoveArgs.isEmpty())){
            warpRemoveArgs.clear();
        }
        for (String name : warps.keySet()){
            warpRemoveArgs.add(name);
        }
    }
    
    public void addWorldArgs(){
        if (!(worldArgs.isEmpty())){
            worldArgs.clear();
        }
        worldArgs.add("create");
        worldArgs.add("remove");
        worldArgs.add("list");
        for (String name : worlds.keySet()){
            worldArgs.add(name);
        }
    }
    
    public void addWorldRemoveArgs(){
        if (!(worldRemoveArgs.isEmpty())){
            worldRemoveArgs.clear();
        }
        for (String name : worlds.keySet()){
            worldRemoveArgs.add(name);
        }
    }
    
    public void saveWarps(){
        if (!(warps.isEmpty())) {
            for (Map.Entry<String, WarpManager> entry : warps.entrySet()) {
                getConfig().set("warps." + entry.getKey() + ".name", entry.getValue().getName());
                getConfig().set("warps." + entry.getKey() + ".private", entry.getValue().getPrivate());
                getConfig().set("warps." + entry.getKey() + ".creator", entry.getValue().getCreator());
                getConfig().set("warps." + entry.getKey() + ".warp", entry.getValue().getWarp());
                getConfig().set("warps." + entry.getKey() + ".allow-world-travel", entry.getValue().getAllowWorldTravel());
            }
            saveConfig();
        }
        return;
    }
    public void loadWarps(){ // Same as restoreInvs in TinyGames plugin
        if (getConfig().contains("warps")){
            if (warps.isEmpty()) {
                getConfig().getConfigurationSection("warps").getKeys(false).forEach(key -> {
                    String creator = getConfig().getString("warps." + key + ".creator");
                    Location location = (Location) getConfig().get("warps." + key + ".warp");
                    String name = getConfig().getString("warps." + key + ".name");
                    Boolean isPrivate = getConfig().getBoolean("warps." + key + ".private");
                    Boolean allowWorldTravel = getConfig().getBoolean("warps." + key + ".allow-world-travel");
                    warps.put(name, new WarpManager(creator, location, name, isPrivate, allowWorldTravel, this));
                    getConfig().set("warps." + key, null);
                    saveConfig();
                });
                
            }
            return;
        }
        return;
    }
    
    public void loadWorlds(){
        for (World w : Bukkit.getWorlds()){
            Boolean pvpAllowed = w.getPVP();
            Boolean isHardcore = w.isHardcore();
            if (!(w.getName().equalsIgnoreCase("world") || w.getName().equalsIgnoreCase("world_nether") ||
                    w.getName().equalsIgnoreCase("world_the_end"))){
                worlds.put(w.getName(), new WorldManager(this, w.getName(), pvpAllowed, isHardcore));
                continue;
            }
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Default world " + ChatColor.AQUA + "\"" + w.getName() + "\" " +
                    ChatColor.GREEN + "found; World Load ignored.");
            continue;
        }
        return;
    }
    
    public void onEnable(){
        initialize();
        getCommands();
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&eRemember that all warps must have &cunique &enames!"));
        enableMessage();
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();
        loadWarps();
        loadWorlds();
        if (!(warps.keySet().isEmpty())){ // Adds all the warps to the TabComplete
            for (Map.Entry<String, WarpManager> entry : warps.entrySet()){
                warpArgs.add(entry.getKey());
            }
        }
        if (!(warps.keySet().isEmpty())){ // Adds all the worlds to the TabComplete
            for (Map.Entry<String, WorldManager> entry : worlds.entrySet()){
                worldArgs.add(entry.getKey());
            }
        }
        addWarpArgs();
        addWarpRemoveArgs();
        addWorldArgs();
        addWorldRemoveArgs();
    }
    
    public void onDisable(){
        disableMessage();
        this.saveConfig();
        saveWarps();
    }
    
}
