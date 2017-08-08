package me.Allogeneous.AllosEnchantsAPI.GUIs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Allogeneous.AllosEnchantsAPI.core.AEnchantmentConfigData;
import net.md_5.bungee.api.ChatColor;

public class EnchantmentTableGUI {
	
	/**
	 * Displays the AEnchantment enchantment table GUI to a player.
	 * 
	 * @param player
	 */
	public static void displayGUI(Player player){
		Inventory etable = Bukkit.createInventory(null, 27, "AEnchantments Enchantment Table");
		for(int i = 0; i < etable.getSize(); i++){
			if(i < 10 || i > 16){
				etable.setItem(i, new ItemStack(AEnchantmentConfigData.getEnchantmentTableBorder(), 1));
			}
		}
		etable.setItem(12, new ItemStack(AEnchantmentConfigData.getEnchantmentTableDecorationItem1(), 1));
		ItemStack is1 = new ItemStack(AEnchantmentConfigData.getEnchantmentTableActivator(), 1);
		ItemMeta im1 = is1.getItemMeta();
		im1.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Click here to activate!");
		is1.setItemMeta(im1);
		etable.setItem(13, is1);
		ItemStack is2 = new ItemStack(AEnchantmentConfigData.getEnchantmentTableCostIndicator(), 1);
		ItemMeta im2 = is2.getItemMeta();
		im2.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Cost:");
		is2.setItemMeta(im2);
		etable.setItem(14, is2);
		etable.setItem(15, new ItemStack(AEnchantmentConfigData.getEnchantmentTableDecorationItem2(), 1));
		
		player.openInventory(etable);
		
		
	}
	
}
