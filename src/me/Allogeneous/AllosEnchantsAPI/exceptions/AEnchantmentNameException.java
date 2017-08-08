package me.Allogeneous.AllosEnchantsAPI.exceptions;

public class AEnchantmentNameException extends Exception{

	private static final long serialVersionUID = -669277275740259286L;
	
	/**
	 * Thrown when an AEnchantment has an invalid name.
	 * 
	 * @param msg
	 */
	public AEnchantmentNameException(String msg){
		super(msg);
	}
	
}
