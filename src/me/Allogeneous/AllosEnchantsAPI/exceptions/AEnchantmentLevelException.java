package me.Allogeneous.AllosEnchantsAPI.exceptions;

public class AEnchantmentLevelException extends Exception{
	
	private static final long serialVersionUID = -1038093076063428268L;

	/**
	 * Thrown when an there is a negative AEnchantment level.
	 * 
	 * @param msg
	 */
	public AEnchantmentLevelException(String msg){
		super(msg);
	}
}
