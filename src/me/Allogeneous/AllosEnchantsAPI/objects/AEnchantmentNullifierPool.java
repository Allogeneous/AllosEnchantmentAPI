package me.Allogeneous.AllosEnchantsAPI.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.Allogeneous.AllosEnchantsAPI.core.AEnchantment;

public class AEnchantmentNullifierPool {
	
	private final String[] nullifiedAEnchantmentConfigIDs;
	
	
	/**
	 * Creates an nullifier pool from a list of global IDs.
	 * 
	 * @param nullifiedAEnchantmentConfigIDs
	 */
	public AEnchantmentNullifierPool(String[] nullifiedAEnchantmentConfigIDs){
		this.nullifiedAEnchantmentConfigIDs =  nullifiedAEnchantmentConfigIDs;
	}
	
	/**
	 * Creates an nullifier pool from an AEnchantment.
	 * 
	 * @param nullifiedAEnchantment
	 */
	public AEnchantmentNullifierPool(AEnchantment nullifiedAEnchantment){
		nullifiedAEnchantmentConfigIDs = new String[1];
		nullifiedAEnchantmentConfigIDs[0] = nullifiedAEnchantment.getGlobalID();
	}
	
	/**
	 * @param Creates an nullifier pool from a global ID.
	 */
	public AEnchantmentNullifierPool(String nullifiedAEnchantment){
		nullifiedAEnchantmentConfigIDs = new String[1];
		nullifiedAEnchantmentConfigIDs[0] = nullifiedAEnchantment;
	}
	
	/**
	 * Used to stay uniform. Note that nullifier pools do not have a dynamic size.
	 * 
	 * @return
	 */
	public int size(){
		return nullifiedAEnchantmentConfigIDs.length;
	}
	
	/**
	 * Checks to see if the pool contains an AEnchantment global ID.
	 * 
	 * @param nullifiedAEnchantment
	 * @return
	 */
	public boolean contains(AEnchantment nullifiedAEnchantment){
		if(nullifiedAEnchantmentConfigIDs.length > 0){
			for(String s : nullifiedAEnchantmentConfigIDs){
				if(s.equals(nullifiedAEnchantment.getGlobalID())){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks to see if the pool contains an AEnchantment global ID from the given globalID.
	 * 
	 * @param nullifiedAEnchantment
	 * @return
	 */
	public boolean contains(String nullifiedAEnchantment){
		if(nullifiedAEnchantmentConfigIDs.length > 0){
			for(String s : nullifiedAEnchantmentConfigIDs){
				if(s.equals(nullifiedAEnchantment)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns a list of nullified AEnchantments.
	 * 
	 * @return
	 */
	public List<AEnchantment> nullifiedAEnchantments(){
		List<AEnchantment> nullifiedAEnchantments = new ArrayList<>();
		if(nullifiedAEnchantmentConfigIDs.length != 0){
			for(String id : nullifiedAEnchantmentConfigIDs){
				for(AEnchantment nullified : AEnchantment.viewRegisteredAEnchantments()){
					if(nullified.getGlobalID().equals(id)){
						nullifiedAEnchantments.add(nullified);
					}
				}
			}
		}
		return nullifiedAEnchantments;
	}

	@Override
	public String toString() {
		return "AEnchantmentNullifierPool [nullifiedAEnchantmentConfigIDs="
				+ Arrays.toString(nullifiedAEnchantmentConfigIDs) + "]";
	}

}
