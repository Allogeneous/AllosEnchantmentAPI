package me.Allogeneous.AllosEnchantsAPI.core;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import me.Allogeneous.AllosEnchantsAPI.listeners.command.AEnchantCommandListener;
import me.Allogeneous.AllosEnchantsAPI.listeners.command.AEnchantmentTabCompleter;
import me.Allogeneous.AllosEnchantsAPI.listeners.event.AnvilListener;
import me.Allogeneous.AllosEnchantsAPI.listeners.event.EnchantmentTableListener;


public class AllosEnchantsMain extends JavaPlugin{
	
	private AnvilListener al;
	private EnchantmentTableListener el;
	public static AllosEnchantsMain instance;
	public static String commandTag;
	
	@Override
	public void onEnable(){
		load();
		getLogger().info("AllosEnchantsAPI has been enabled!");
	}
	
	@Override
	public void onDisable(){
		getLogger().info("AllosEnchantsAPI has been disabled!");
	}
	
	private void load(){ 
		createConfig();
		loadTag();
		registerCommands();
		loadEnables();
		registerListeners();
		instance = this;
	}
	
	/**
	 * Reloads the config file. 
	 */
	public void reload(){
		this.reloadConfig();
	}
	
	private void createConfig(){
	   try{
	      if (!getDataFolder().exists()) {
	        getDataFolder().mkdirs();
	      }
	      File file = new File(getDataFolder(), "config.yml");
	      if (!file.exists()){
	        saveDefaultConfig();
	      }
	    }
	    catch (Exception ex) {}
	  }
	
	private void registerCommands(){
		getCommand("aenchants").setExecutor(new AEnchantCommandListener());
		getCommand("aenchants").setTabCompleter(new AEnchantmentTabCompleter());
	}
	
	private void registerListeners(){
		al = new AnvilListener(this);
		Bukkit.getPluginManager().registerEvents(al, this);
		el = new EnchantmentTableListener(this);
		Bukkit.getPluginManager().registerEvents(el, this);
	}
	
	private void loadEnables(){

			AEnchantmentConfigData.setLoadedEnableAnvilShiftClick(getConfig().getBoolean("enableAnvilShiftClick", true));
			AEnchantmentConfigData.setLoadedEnchantmentTableShiftClick(getConfig().getBoolean("enableEnchantmentTableShiftClick", true));
			AEnchantmentConfigData.setLoadedtMaxSafeEnchantmentsOnItem(getConfig().getInt("maxSafeEnchantmentsOnItem", 3));
			AEnchantmentConfigData.setLoadedCanEnchantItemsWithNoEnchantmentUsePermission(getConfig().getBoolean("canEnchantItemsWithNoEnchantmentUsePermission", false));
			AEnchantmentConfigData.setNo0Weights(getConfig().getBoolean("n0Weights", false));
	
			AEnchantmentConfigData.setAnvilBlock(getBlockFromString(getConfig().getString("anvilBlock", "ANVIL")));
			AEnchantmentConfigData.setAnvilBorder(getMaterialFromString(getConfig().getString("anvilBorder", "IRON_BLOCK")));
			AEnchantmentConfigData.setAnvilActivator(getMaterialFromString(getConfig().getString("anvilActivator", "EMERLAD_BLOCK")));
			AEnchantmentConfigData.setAnvilCostIndicator(getMaterialFromString(getConfig().getString("anvilCostIndicator", "EXP_BOTTLE")));
			AEnchantmentConfigData.setAnvilDecorationItem1(getMaterialFromString(getConfig().getString("anvilDecorationItem1", "REDSTONE")));
			AEnchantmentConfigData.setAnvilDecorationItem2(getMaterialFromString(getConfig().getString("anvilDecorationItem2", "REDSTONE")));
			
			AEnchantmentConfigData.setEnchantmentTableBlock(getBlockFromString(getConfig().getString("enchantmentTableBlock", "ENCHANTMENT_TABLE")));
			AEnchantmentConfigData.setEnchantmentTableBorder(getMaterialFromString(getConfig().getString("enchantmentTableBorder", "OBSIDIAN")));
			AEnchantmentConfigData.setEnchantmentTableActivator(getMaterialFromString(getConfig().getString("enchantmentTableActivator", "EMERLAD_BLOCK")));
			AEnchantmentConfigData.setEnchantmentTableCostIndicator(getMaterialFromString(getConfig().getString("enchantmentTableCostIndicator", "EXP_BOTTLE")));
			AEnchantmentConfigData.setEnchantmentTableDecorationItem1(getMaterialFromString(getConfig().getString("enchantmentTableDecorationItem1", "BOOK")));
			AEnchantmentConfigData.setEnchantmentTableDecorationItem2(getMaterialFromString(getConfig().getString("enchantmentTableDecorationItem2", "BOOK")));
	}
	
	
	
	private void loadTag(){
		commandTag = getConfig().getString("tag", "&5[&cA&dEnchantments&5] ");
	}
	
	private Material getMaterialFromString(String material){
		for(Material m : Material.values()){
			if(m.name().equals(material)){
				return m;
			}
		}
		return null;
	}
	
	private Material getBlockFromString(String material){
		for(Material m : Material.values()){
			if(m.name().equals(material) && m.isBlock()){
				return m;
			}
		}
		return null;
	}
	
}
