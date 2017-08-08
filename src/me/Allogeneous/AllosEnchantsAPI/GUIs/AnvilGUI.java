package me.Allogeneous.AllosEnchantsAPI.GUIs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Allogeneous.AllosEnchantsAPI.core.AEnchantmentConfigData;
import net.md_5.bungee.api.ChatColor;

public class AnvilGUI {
	
	/**
	 * Displays the AEnchantment anvil GUI to a player.
	 * 
	 * @param player
	 */
	public static void displayGUI(Player player){
		Inventory anvil = Bukkit.createInventory(null, 27, "AEnchantments Anvil");
		for(int i = 0; i < anvil.getSize(); i++){
			if(i < 10 || i > 16){
				anvil.setItem(i, new ItemStack(AEnchantmentConfigData.getAnvilBorder(), 1));
			}
		}
		anvil.setItem(12, new ItemStack(AEnchantmentConfigData.getAnvilDecorationItem1(), 1));
		ItemStack is1 = new ItemStack(AEnchantmentConfigData.getAnvilActivator(), 1);
		ItemMeta im1 = is1.getItemMeta();
		im1.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Click here to activate!");
		is1.setItemMeta(im1);
		anvil.setItem(13, is1);
		ItemStack is2 = new ItemStack(AEnchantmentConfigData.getAnvilCostIndicator(), 1);
		ItemMeta im2 = is2.getItemMeta();
		im2.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Cost:");
		is2.setItemMeta(im2);
		anvil.setItem(14, is2);
		anvil.setItem(15, new ItemStack(AEnchantmentConfigData.getAnvilDecorationItem2(), 1));
		player.openInventory(anvil);
	}

}
