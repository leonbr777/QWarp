package me.Liam.QuickWarp.warps.commands;

import me.Liam.QuickWarp.warps.managers.WarpManager;
import me.Liam.QuickWarp.WarpsMain;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;


public class WarpCommands implements CommandExecutor {
    
    
    private WarpsMain plugin;
    
    public WarpCommands(WarpsMain plugin){
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        if (cmd.getName().equalsIgnoreCase("qwarp")) {
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
                                p.sendMessage(ChatColor.RED + "A warp with that name has already been created!");
                                return true;
                            }
                            Boolean isPrivate = Boolean.parseBoolean(args[2]);
                            Boolean allowWorldTravel = Boolean.parseBoolean(args[3]);
                            Location loc = p.getLocation();
                            plugin.warps.put(name, new WarpManager(p.getName(), loc, name, isPrivate, allowWorldTravel, plugin));
                            plugin.addWarpArgs();
                            plugin.addWarpRemoveArgs();
                            
                            if (isPrivate == true){
                                if (allowWorldTravel == true){
                                    p.sendMessage(plugin.chatUtil.colorize("&aThe warp &6&l" + name + " &ahas been successfully created with &9&lprivacy &aset to &3&ltrue &aand " +
                                            "&5&lAllow World Travel &aset to &3&ltrue!"));
                                    return true;
                                }
                                p.sendMessage(plugin.chatUtil.colorize("&aThe warp &6&l" + name + " &ahas been successfully created with &9&lprivacy &aset to &3&ltrue &aand " +
                                        "&5&lAllow World Travel &aset to &4&lfalse!"));
                                return true;
                                
                            }
                            if (allowWorldTravel == true){
                                p.sendMessage(plugin.chatUtil.colorize("&aThe warp &6&l" + name + " &ahas been successfully created with &9&lprivacy &aset to &4&lfalse &aand " +
                                        "&5&lAllow World Travel &aset to &3&ltrue!"));
                                return true;
                            }
                            p.sendMessage(plugin.chatUtil.colorize("&aThe warp &6&l" + name + " &ahas been successfully created with &9&lprivacy &aset to &4&lfalse &aand " +
                                    "&5&lAllow World Travel &aset to &4&lfalse!"));
                            return true;
                        } catch (Exception o){
                            Bukkit.getServer().getLogger().warning("Exception: " + o.toString());
                        }
                    }
                }
                else if (args[0].equalsIgnoreCase("remove")){
                    if (args.length == 2){
                        String name = args[1];
                        if (!(plugin.warps.containsKey(name))){
                            p.sendMessage(plugin.chatUtil.colorize("&cThe warp &6&l" + name + " &cdoes not exist!"));
                            return true;
                        }
                        plugin.warps.remove(name);
                        plugin.addWarpArgs();
                        plugin.addWarpRemoveArgs();
                        p.sendMessage(ChatColor.GREEN + "Warp removed successfully!");
                        return true;
                    }
                }
                else if (args[0].equalsIgnoreCase("clear")){
                    if (plugin.warps.keySet().isEmpty()){
                        p.sendMessage(ChatColor.RED + "There are no warps! Unable to clear.");
                        return true;
                    }
                    plugin.warps.clear();
                    p.sendMessage(ChatColor.GREEN + "All warps have been cleared!");
                    return true;
                }
                else if (args[0].equalsIgnoreCase("list")){
                    if (args.length == 1){
                        try {
                            if (plugin.warps.keySet().isEmpty()){
                                p.sendMessage(ChatColor.RED + "There are currently no warps!");
                                return true;
                            }
                            p.sendMessage("");
                            p.sendMessage("");
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "    &5☰&9☰&3☰&b☰&a☰&e☰&6☰&c☰&d☰&5☰&9☰&3☰&b☰&a☰&e☰     &5&lWarps List     &e☰&a☰&b☰&3☰&9☰&5☰&d☰&c☰&6☰&e☰&a☰&b☰&3☰&9☰&5☰"));
                            p.sendMessage("");
                            p.sendMessage(ChatColor.GRAY + "-——————————————————————————————————-");
                            
                            ArrayList<String> warps = new ArrayList<>();
                            for (String name : plugin.warps.keySet()){
                                warps.add(name);
                            }
                            for (int i = 0; i < plugin.warps.keySet().size(); i++) {
                                p.sendMessage("");
                                p.sendMessage(plugin.chatUtil.colorize("&a&l" + (i + 1) + "&e&l: &6" + warps.get(i)));
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
                    
                    if (!(plugin.warps.containsKey(name))){
                        p.sendMessage(plugin.chatUtil.colorize("&cThe warp &6&l" + name + " &cdoes not exist!"));
                        return true;
                    }
                    
                    if (plugin.warps.get(name).getPrivate() == true && !(p.getName().equalsIgnoreCase(plugin.warps.get(name).getCreator()))){
                        p.sendMessage(ChatColor.RED + "That warp is available only through private access!");
                        return true;
                    }
                    if (plugin.warps.get(name).getWarp().getWorld() != p.getWorld()){
                        if (plugin.warps.get(name).getAllowWorldTravel() == false){
                            p.sendMessage(plugin.chatUtil.colorize("&cThe warp &6&l" + name + " &cdoes not allow multi-dimensional travel!"));
                            return true;
                        }
                    }
                    try {
                        Location warp = plugin.warps.get(name).getWarp();
                        p.teleport(warp);
                        p.getLocation().setPitch(warp.getPitch());
                        p.getLocation().setYaw(warp.getYaw());
                        p.playSound(warp, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1F);
                        return true;
                    } catch (Exception o){
                        p.sendMessage(ChatColor.RED + "An error occured! Does that warp exist?");
                        return true;
                    }
                    
                }
                
               /* if (args[1].equalsIgnoreCase("world")) {
                    Location loc = Bukkit.getWorld("world").getSpawnLocation();
                    p.teleport(loc);
                }
                if (args[1].equalsIgnoreCase("nether")) {
                    Location loc = Bukkit.getWorld("world_nether").getSpawnLocation();
                    p.teleport(loc);
                }
                if (args[1].equalsIgnoreCase("end")) {
                    Location loc = Bukkit.getWorld("world_the_end").getSpawnLocation();
                    p.teleport(loc);
                }
                
                if (args[2] != null) {
                    Player pl = Bukkit.getPlayer("args[0]");
                    if (args[2].equalsIgnoreCase("world")) {
                        Location loc = Bukkit.getWorld("world").getSpawnLocation();
                        pl.teleport(loc);
                    }
                    if (args[2].equalsIgnoreCase("nether")) {
                        Location loc = Bukkit.getWorld("world_nether").getSpawnLocation();
                        pl.teleport(loc);
                    }
                    if (args[2].equalsIgnoreCase("end")) {
                        Location loc = Bukkit.getWorld("world_the_end").getSpawnLocation();
                        pl.teleport(loc);
                    }
                }*/
            }
        }
        return false;
    }
}