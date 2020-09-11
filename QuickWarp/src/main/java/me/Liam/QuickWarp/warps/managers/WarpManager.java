package me.Liam.QuickWarp.warps.managers;

import me.Liam.QuickWarp.WarpsMain;
import org.bukkit.Location;

public class WarpManager {
    
    private WarpsMain plugin;
    
    private Location warp;
    private String name;
    private Boolean isPrivate;
    private Boolean allowWorldTravel;
    private String creator;
    
    public WarpManager(String creator, Location warp, String name, Boolean isPrivate, Boolean allowWorldTravel, WarpsMain plugin){
        this.plugin = plugin;
        this.creator = creator;
        this.warp = warp;
        this.name = name;
        this.isPrivate = isPrivate;
        this.allowWorldTravel = allowWorldTravel;
        plugin.warpArgs.add(name);
    }
    
    public Location getWarp() { return this.warp; }
    public void setWarp(Location warp) { this.warp = warp; }
    
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    
    public Boolean getPrivate() { return this.isPrivate; }
    public void setPrivate(Boolean isPrivate) { this.isPrivate = isPrivate; }
    
    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }
    
    public Boolean getAllowWorldTravel() { return allowWorldTravel; }
    public void setAllowWorldTravel(Boolean allowWorldTravel) { this.allowWorldTravel = allowWorldTravel; }
    
    public void removeNameFromTab(String name){
        if (!(plugin.warpArgs.contains(name))){
            return;
        }
        plugin.warpArgs.remove(name);
    }
    
}
