package me.Allogeneous.AllosEnchantsAPI.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

public class AEnchantmentPlugin extends JavaPlugin{
	
	private boolean generateGlobalIDs;
	private final List<AEnchantment> localAEnchantments;
	
	public AEnchantmentPlugin(){
		localAEnchantments = new ArrayList<>();
		generateGlobalIDs = false;
	}

	/**
	 * Returns a list of all the the AEnchantments in the AEnchantmentPlugin.
	 * 
	 * @return
	 */
	public List<AEnchantment> getLocalAEnchantments() {
		return localAEnchantments;
	}

	/**
	 * Returns true if the API will attempt to generate global IDs as they're registered.
	 * 
	 * @return
	 */
	public boolean isGenerateGlobalIDs() {
		return generateGlobalIDs;
	}

	/**
	 * Sets if the API will attempt to generate global IDs as they're registered.
	 * 
	 * @param generateGlobalIDs
	 */
	public void setGenerateGlobalIDs(boolean generateGlobalIDs) {
		this.generateGlobalIDs = generateGlobalIDs;
	}

}
