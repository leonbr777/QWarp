package me.Liam.QuickWarp.chatUtil;

import org.bukkit.ChatColor;

public class ChatUtil {
    
    public String colorize(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    
}
