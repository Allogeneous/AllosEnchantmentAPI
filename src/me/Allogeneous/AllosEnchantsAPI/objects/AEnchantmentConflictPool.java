package me.Allogeneous.AllosEnchantsAPI.objects;

import java.util.ArrayList;
import java.util.List;

import me.Allogeneous.AllosEnchantsAPI.core.AEnchantment;

public class AEnchantmentConflictPool {
	
	private List<String> conflictIDs;
	
	/**
	 * Base conflict pool, can add or remove conflicts.
	 */
	public AEnchantmentConflictPool(){
		conflictIDs = new ArrayList<>();
	}
	
	/**
	 * Creates a conflict pool based off off an exixting list of AEnchantment global IDs.
	 * 
	 * @param configID
	 */
	public AEnchantmentConflictPool(List<String> configID){
		this.conflictIDs = configID;
	}
	
	/**
	 * Creates a conflict pool based on an AEnchantment
	 * 
	 * @param conflict
	 */
	public AEnchantmentConflictPool(AEnchantment conflict){
		conflictIDs = new ArrayList<>();
		conflictIDs.add(conflict.getGlobalID());
	}
	
	/**
	 * Creates a conflict pool based off a global ID.
	 * 
	 * @param configID
	 */
	public AEnchantmentConflictPool(String configID){
		conflictIDs = new ArrayList<>();
		conflictIDs.add(configID);
	}
	
	public int size(){
		return conflictIDs.size();
	}
	
	public void add(AEnchantment conflict){
		conflictIDs.add(conflict.getGlobalID());
	}
	
	public void remove(AEnchantment conflict){
		conflictIDs.remove(conflict.getGlobalID());
	}
	
	public boolean contains(AEnchantment conflict){
		return conflictIDs.contains(conflict.getGlobalID());
	}
	
	public void add(String conflict){
		conflictIDs.add(conflict);
	}
	
	public void remove(String conflict){
		conflictIDs.remove(conflict);
	}
	
	public boolean contains(String conflict){
		return conflictIDs.contains(conflict);
	}
	
	/**
	 * Returns a List of AEnchantments in the conflict pool
	 * 
	 * @return
	 */
	public List<AEnchantment> conflicts(){
		List<AEnchantment> conflicts = new ArrayList<>();
		if(!conflictIDs.isEmpty()){
			for(String id : conflictIDs){
				for(AEnchantment conflict : AEnchantment.viewRegisteredAEnchantments()){
					if(conflict.getGlobalID().equals(id)){
						conflicts.add(conflict);
					}
				}
			}
		}
		return conflicts;
	}
	
	/**
	 * Makes sure that both the AEnchantment with the conflict and the conflicting AEnchamtment are in each other's conflict pools.
	 */
	public static void refreshConflicts(){
		for(AEnchantment aenchantment : AEnchantment.viewRegisteredAEnchantments()){
			for(AEnchantment conflict : aenchantment.getConflicts().conflicts()){
				if(!conflict.getConflicts().contains(aenchantment.getGlobalID())){
					conflict.getConflicts().add(aenchantment);
				}
			}
		}
	}

	@Override
	public String toString() {
		return "AEnchantmentConflictPool [conflictIDs=" + String.join(", ", conflictIDs) + "]";
	}
	
}
