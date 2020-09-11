package me.Liam.QuickWarp.worlds.commands;

import me.Liam.QuickWarp.WarpsMain;
import me.Liam.QuickWarp.worlds.managers.WorldManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WorldCommands implements CommandExecutor {
    
    private WarpsMain plugin;
    
    public WorldCommands(){
        this.plugin = WarpsMain.getPlugin(WarpsMain.class);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        if (cmd.getName().equalsIgnoreCase("qworld")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Player-only command!");
                return true;
            }
            Player p = (Player) sender;
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("create")){
                    if (args.length == 4){
                        try {
                            String name = args[1];
                            if (plugin.warps.containsKey(name)){
                                p.sendMessage(ChatColor.RED + "A world with that name has already been created!");
                                return true;
                            }
                            Boolean isPVP = Boolean.parseBoolean(args[2]);
                            Boolean isHardcore = Boolean.parseBoolean(args[3]);
                            plugin.worlds.put(name, new WorldManager(plugin, name, isPVP, isHardcore));
                            plugin.addWorldArgs();
                            plugin.addWorldRemoveArgs();
                            // Add the tab-complete args in this area later
                            
                            if (isPVP == true){
                                p.sendMessage(isHardcore == true ? plugin.chatUtil.colorize("&aThe world &6&l" + name + " &ahas been successfully created with &9&lPVP &aset to &3&lon &aand " +
                                        "&5&lHardcore Mode &aset to &3&lon!") : plugin.chatUtil.colorize("&aThe world &6&l" + name + " &ahas been successfully created with &9&lPVP &aset to &3&lon &aand " +
                                        "&5&lHardcore Mode &aset to &3&loff!"));
                                return true;
                            }
                            p.sendMessage(isHardcore == true ? plugin.chatUtil.colorize("&aThe world &6&l" + name + " &ahas been successfully created with &9&lPVP &aset to &3&loff &aand " +
                                    "&5&lHardcore Mode &aset to &3&lon!") : plugin.chatUtil.colorize("&aThe world &6&l" + name + " &ahas been successfully created with &9&lPVP &aset to &3&loff &aand " +
                                    "&5&lHardcore Mode &aset to &3&loff!"));
                            return true;
                        } catch (Exception o){
                            Bukkit.getServer().getLogger().warning("Exception: " + o.toString());
                        }
                    }
                }
                else if (args[0].equalsIgnoreCase("remove")){
                    if (args.length == 2){
                        String name = args[1];
                        if (!(plugin.worlds.containsKey(name))){
                            p.sendMessage(plugin.chatUtil.colorize("&cThe world &6&l" + name + " &cdoes not exist!"));
                            return true;
                        }
                        if (!(plugin.worlds.get(name).getWorld().getPlayers().isEmpty())){
                            Bukkit.broadcastMessage(ChatColor.RED + "Players were found in the world " + ChatColor.AQUA + "\"" + name + "\"" +
                                    ChatColor.RED + ", so the world was not deleted.");
                            return true;
                        }
                        
                        
                        File file = plugin.worlds.get(name).getWorld().getWorldFolder();
                        plugin.worlds.get(name).unloadWorld();
                        //plugin.worlds.get(name).deleteWorld(file);
                        try {
                            plugin.worlds.get(name).deleteFiles(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        plugin.worlds.remove(name);
                        plugin.addWorldArgs();
                        plugin.addWorldRemoveArgs();
                        p.sendMessage(ChatColor.DARK_AQUA + "Successfully removed world " + ChatColor.AQUA + "\"" + name + "\"!");
                        // Re-use the adding and removing ARGS functions for the future tab-complete.
                        //p.sendMessage(ChatColor.GREEN + "World removed successfully!");
                        return true;
                    }
                }
                else if (args[0].equalsIgnoreCase("clear")){
                    if (plugin.warps.isEmpty()){
                        p.sendMessage(ChatColor.RED + "There are no worlds! Unable to clear.");
                        return true;
                    }
                    try {
                        for (WorldManager worldManager : plugin.worlds.values()){
                            File file = worldManager.getWorld().getWorldFolder();
                            worldManager.unloadWorld();
                            worldManager.deleteWorld(file);
                        }
                        plugin.worlds.clear();
                        p.sendMessage(ChatColor.GREEN + "All warps have been cleared!");
                    } catch (Exception o){
                        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "An error occured while trying to clear all worlds: " + o.getCause());
                        return true;
                    }
                    
                    return true;
                }
                else if (args[0].equalsIgnoreCase("list")){
                    if (args.length == 1){
                        try {
                            if (plugin.worlds.isEmpty()){
                                p.sendMessage(ChatColor.RED + "There are currently no worlds!");
                                return true;
                            }
                            p.sendMessage("");
                            p.sendMessage("");
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "    &5☰&9☰&3☰&b☰&a☰&e☰&6☰&c☰&d☰&5☰&9☰&3☰&b☰&a☰&e☰     &5&lWorld List     &e☰&a☰&b☰&3☰&9☰&5☰&d☰&c☰&6☰&e☰&a☰&b☰&3☰&9☰&5☰"));
                            p.sendMessage("");
                            p.sendMessage(ChatColor.GRAY + "-——————————————————————————————————-");
                            
                            ArrayList<String> worlds = new ArrayList<>();
                            for (String name : plugin.worlds.keySet()){
                                worlds.add(name);
                            }
                            for (int i = 0; i < plugin.worlds.keySet().size(); i++) {
                                p.sendMessage("");
                                p.sendMessage(plugin.chatUtil.colorize("&9&l" + (i + 1) + "&e&l: &e" + worlds.get(i)));
                                p.sendMessage("");
                                p.sendMessage(ChatColor.GRAY + "-——————————————————————————————————-");
                            }
                            p.sendMessage("");
                            return true;
                        } catch (Exception o){
                            Bukkit.getServer().getLogger().warning("Exception: " + o.toString());
                        }
                    }
                }
                // All code after this means that the Player wants to teleport to a specified warp.
                else {
                    String name = args[0];
                    
                    if (!(plugin.worlds.containsKey(name))){
                        p.sendMessage(plugin.chatUtil.colorize("&cThe world &6&l" + name + " &cdoes not exist!"));
                        return true;
                    }
                    
                    try {
                        Location worldSpawn = plugin.worlds.get(name).getWorld().getSpawnLocation();
                        p.teleport(worldSpawn);
                        p.getLocation().setPitch(worldSpawn.getPitch());
                        p.getLocation().setYaw(worldSpawn.getYaw());
                        p.playSound(worldSpawn, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1F);
                        return true;
                    } catch (Exception o){
                        p.sendMessage(ChatColor.RED + "An error occured! Does that world exist?");
                        return true;
                    }
                    
                }
            }
        }
        return false;
    }
}
