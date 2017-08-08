package me.Allogeneous.AllosEnchantsAPI.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Allogeneous.AllosEnchantsAPI.core.AEnchantment;
import me.Allogeneous.AllosEnchantsAPI.core.AEnchantmentConfigData;
import me.Allogeneous.AllosEnchantsAPI.objects.AEnchantmentNullifier;

public class AEnchantmentAnvilUtils {

	/**
	 * Checks to see what AEnchantments on the book are safe to use on the item.
	 * 
	 * @param item
	 * @param book
	 * @return
	 */
	public static HashMap<AEnchantment, Integer> getApplicableAnvilAEnchantments(ItemStack item, ItemStack book){
		if(book == null || book.getType() == Material.AIR){
			return null;
		}
		if(item == null || item.getType() == Material.AIR){
			return null;
		}
		ItemMeta im = book.getItemMeta();
		List<String> lore = im.getLore();
		if(lore == null){
			return null;
		}
		HashMap<AEnchantment, Integer> applicableEnchantments = new HashMap<>();
		for(String loreLine : lore){
			String[] words = loreLine.split(" ");
			innerLoop:
			for(AEnchantment ench : AEnchantment.viewRegisteredAEnchantments()){
				if(loreLine.startsWith(ench.getDisplayName()) && AEnchantmentUtils.isIntValue(words[words.length - 2])){
					if(ench.isSafeAEnchantment(item, AEnchantmentUtils.translateIntValue(words[words.length - 2])) || ench instanceof AEnchantmentNullifier){
						applicableEnchantments.put(ench, AEnchantmentUtils.translateIntValue(words[words.length - 2]));
						break innerLoop;
					}
				}
			}
			
		}
		return AEnchantmentAnvilUtils.resolveConflictsAnvil(applicableEnchantments);
	}

	/**
	 * Checks to see what AEnchantments on the book are safe to use on the item and checks the player's permission.
	 * 
	 * @param player
	 * @param item
	 * @param book
	 * @return
	 */
	public static HashMap<AEnchantment, Integer> getApplicableAnvilAEnchantments(Player player, ItemStack item, ItemStack book){
		if(book == null || book.getType() == Material.AIR){
			return null;
		}
		if(item == null || item.getType() == Material.AIR){
			return null;
		}
		ItemMeta im = book.getItemMeta();
		List<String> lore = im.getLore();
		if(lore == null){
			return null;
		}
		HashMap<AEnchantment, Integer> applicableEnchantments = new HashMap<>();
		for(String loreLine : lore){
			String[] words = loreLine.split(" ");
			innerLoop:
			for(AEnchantment ench : AEnchantment.viewRegisteredAEnchantments()){
				if(loreLine.startsWith(ench.getDisplayName()) && AEnchantmentUtils.isIntValue(words[words.length - 2])){
					if(ench.isSafeAEnchantment(item, AEnchantmentUtils.translateIntValue(words[words.length - 2])) || ench instanceof AEnchantmentNullifier){
						if(ench.hasPermission(player) || AEnchantmentConfigData.isLoadedCanEnchantItemsWithNoEnchantmentUsePermission()){
							applicableEnchantments.put(ench, AEnchantmentUtils.translateIntValue(words[words.length - 2]));
						}
						break innerLoop;
					}
				}
			}
			
		}
		return AEnchantmentAnvilUtils.resolveConflictsAnvil(applicableEnchantments);
	}

	/**
	 * Resolves any conflicting AEnchantments. Looks through AEnchantment conflict pools and making sure none match up. 
	 * 
	 * @param aenchantmentSet
	 * @return
	 */
	public static HashMap<AEnchantment, Integer> resolveConflictsAnvil(HashMap<AEnchantment, Integer> aenchantmentSet){
		HashMap<AEnchantment, Integer> resolved = new HashMap<>(aenchantmentSet);
		for(Entry<AEnchantment, Integer> d : aenchantmentSet.entrySet()){
			if(d.getKey().getConflicts() != null || d.getKey().getConflicts().size() != 0){
				for(AEnchantment c : d.getKey().getConflicts().conflicts()){
					if(!c.getConflicts().contains(d.getKey())){
						c.getConflicts().add(d.getKey());
					}
					if(resolved.containsKey(c)){
						boolean r = false;
						for(AEnchantment cc : c.getConflicts().conflicts()){
							if(resolved.containsKey(cc)){
								r = true;
								break;
							}
						}
						if(r == true){
							resolved.remove(c);
						}
					}
				}
			}
		}
		return resolved;
	}

}
