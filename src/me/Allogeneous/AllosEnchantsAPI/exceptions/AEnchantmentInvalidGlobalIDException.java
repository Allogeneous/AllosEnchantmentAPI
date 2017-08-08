package me.Allogeneous.AllosEnchantsAPI.exceptions;

public class AEnchantmentInvalidGlobalIDException extends Exception{

	private static final long serialVersionUID = 7977512184935076720L;

	/**
	 * Thrown when an AEnchantment has an invalid global ID.
	 * 
	 * @param msg
	 */
	public AEnchantmentInvalidGlobalIDException(String msg){
		super(msg);
	}
}
