package me.Liam.QuickWarp.worlds.commands;

import me.Liam.QuickWarp.WarpsMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class WorldTabComplete implements TabCompleter {
    
    private WarpsMain plugin;
    
    public WorldTabComplete(WarpsMain plugin){
        this.plugin = plugin;
    }
    
    public ArrayList<String> autoFix(List<String> arrayList, String args){
        ArrayList<String> result = new ArrayList<>();
        for (String s : arrayList){
            if (s.toLowerCase().startsWith(args.toLowerCase())){
                result.add(s);
            }
        }
        return result;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        
        if (args.length == 1 && cmd.getName().equalsIgnoreCase("qworld")){
            return autoFix(plugin.worldArgs, args[0]);
        }
        if (args.length == 2 && cmd.getName().equalsIgnoreCase("qworld") && args[0].equalsIgnoreCase("remove")){
            return autoFix(plugin.worldRemoveArgs, args[1]);
        }
        return null;
    }
    
}
