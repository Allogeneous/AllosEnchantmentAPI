package me.Allogeneous.AllosEnchantsAPI.objects;

import java.util.Arrays;

import org.bukkit.Material;

import me.Allogeneous.AllosEnchantsAPI.core.AEnchantment;
import me.Allogeneous.AllosEnchantsAPI.core.AEnchantmentPlugin;

public class AEnchantmentNullifier extends AEnchantment{
	
	private AEnchantmentNullifierPool nullifiedEnchantments;
	
	/**
	 * Special kind of AEnchantment that can be used to remove other AEnchantments when applied in the AEnchantment Anvil.
	 * 
	 * @param globalID
	 * @param STORAGE_NAME
	 * @param displayName
	 * @param safeMaterials
	 * @param safeLevel
	 * @param conflicts
	 * @param permission
	 * @param baseEffectOdds
	 * @param enchantmentTableWeight
	 * @param host
	 */
	public AEnchantmentNullifier(String globalID, String STORAGE_NAME, String displayName, Material[] safeMaterials, int safeLevel, AEnchantmentConflictPool conflicts, String permission, float baseEffectOdds, float enchantmentTableWeight, AEnchantmentPlugin host) {
		super(globalID, STORAGE_NAME, displayName, safeMaterials, safeLevel, conflicts ,permission, baseEffectOdds, enchantmentTableWeight, host);
	}
	
	/**
	 * Special kind of AEnchantment that can be used to remove other AEnchantments when applied in the AEnchantment Anvil.
	 * 
	 * @param globalID
	 * @param STORAGE_NAME
	 * @param displayName
	 * @param safeMaterials
	 * @param conflicts
	 * @param safeLevel
	 * @param permission
	 * @param baseEffectOdds
	 * @param enchantmentTableWeight
	 * @param nullifiedEnchantments
	 * @param host
	 */
	public AEnchantmentNullifier(String globalID, String STORAGE_NAME, String displayName, Material[] safeMaterials, AEnchantmentConflictPool conflicts, int safeLevel, String permission, float baseEffectOdds, float enchantmentTableWeight, AEnchantmentNullifierPool nullifiedEnchantments, AEnchantmentPlugin host) {
		super(globalID, STORAGE_NAME, displayName, safeMaterials, safeLevel, conflicts ,permission, baseEffectOdds, enchantmentTableWeight, host);
		this.nullifiedEnchantments = nullifiedEnchantments;
	}

	public AEnchantmentNullifierPool getNullifiedEnchantments() {
		return nullifiedEnchantments;
	}

	public void setNullifiedEnchantments(AEnchantmentNullifierPool nullifiedEnchantments) {
		this.nullifiedEnchantments = nullifiedEnchantments;
	}
	
	@Override
	public String toString() {
		return "AEnchantment [globalID=" + super.getGlobalID() + ", localID=" + super.getLocalID() + ", STORAGE_NAME=" + super.getSTORAGE_NAME()
				+ ", displayName=" + super.getDisplayName() + ", safeMaterials=" + Arrays.toString(super.getSafeMaterials()) + ", safeLevel="
				+ super.getSafeLevel() + ", conflicts=" + super.getConflicts() + ", permission=" + super.getPermission() + ", baseEffectOdds="
				+ super.getBaseEffectOdds() + ", enchantmentTableWeight=" + super.getEnchantmentTableWeight() + ", nullifiedEnchantments=" + getNullifiedEnchantments() + ", host=" + super.getHost().getName() + "]";
	}

}
