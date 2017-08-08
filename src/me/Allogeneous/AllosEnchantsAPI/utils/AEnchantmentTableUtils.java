package me.Allogeneous.AllosEnchantsAPI.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Allogeneous.AllosEnchantsAPI.core.AEnchantment;
import me.Allogeneous.AllosEnchantsAPI.objects.AEnchantmentNullifier;

public class AEnchantmentTableUtils {

	/**
	 * Resolves any conflicting AEnchantments. Looks through AEnchantment conflict pools and making sure none match up. 
	 * 
	 * @param aenchantment
	 * @return
	 */
	public static List<AEnchantment> resolveConflictsTable(List<AEnchantment> aenchantment){
		List<AEnchantment> resolved = new ArrayList<>(aenchantment);
		for(AEnchantment d : aenchantment){
			if(d.getConflicts() != null || d.getConflicts().size() != 0){
				for(AEnchantment c : d.getConflicts().conflicts()){
					if(!c.getConflicts().contains(d)){
						c.getConflicts().add(d);
					}
					if(resolved.contains(c)){
						boolean r = false;
						for(AEnchantment cc : c.getConflicts().conflicts()){
							if(resolved.contains(cc)){
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

	/**
	 * Returns AEnchantments safe for the item.
	 * 
	 * @param item
	 * @return
	 */
	public static List<AEnchantment> getAllowedTableEnchantments(ItemStack item){
		if(item == null || item.getType() == Material.AIR){
			return null;
		}
		List<AEnchantment> allowedTableEnchantments = new ArrayList<>();
		for(AEnchantment ench : AEnchantment.viewRegisteredAEnchantments()){
			if(ench.isSafeAEnchantment(item, ench.getSafeLevel()) && ench.getEnchantmentTableWeight() > 0 &&!(ench instanceof AEnchantmentNullifier)){
				allowedTableEnchantments.add(ench);
			}
		}
		return allowedTableEnchantments;
	}

	/**
	 * Returns AEnchantments safe for the item and checks player permission.
	 * 
	 * @param player
	 * @param item
	 * @return
	 */
	public static List<AEnchantment> getAllowedTableEnchantments(Player player, ItemStack item){
		if(item == null || item.getType() == Material.AIR){
			return null;
		}
		List<AEnchantment> allowedTableEnchantments = new ArrayList<>();
		for(AEnchantment ench : AEnchantment.viewRegisteredAEnchantments()){
			if(ench.isSafeAEnchantment(item, ench.getSafeLevel()) && ench.getEnchantmentTableWeight() > 0 && (!(ench instanceof AEnchantmentNullifier))){
				if(ench.hasPermission(player)){
					allowedTableEnchantments.add(ench);
				}
			}
		}
		return allowedTableEnchantments;
	}

	/**
	 * Returns list of AEnchantments sorted by AEnchantment Table weight.
	 * 
	 * @param sort
	 * @return
	 */
	public static List<AEnchantment> getEnchantmentTableWeightedList(List<AEnchantment> sort){
		Collections.sort(sort);
		List<AEnchantment> newData = new ArrayList<>();
			while(!sort.isEmpty()){
				double totalWeight = 0.0d;
				for (AEnchantment ench : sort){
					totalWeight += ench.getEnchantmentTableWeight();
				}
		
				int randomIndex = -1;
				double random = Math.random() * totalWeight;
				for (int i = 0; i < sort.size(); i++){
					random -= sort.get(i).getEnchantmentTableWeight();
					if (random <= 0.0d){
						randomIndex = i;
						break;
					}
				}
			AEnchantment r = sort.get(randomIndex);
			sort.remove(randomIndex);
			newData.add(r);
		}
			return resolveConflictsTable(newData);
	}

}
