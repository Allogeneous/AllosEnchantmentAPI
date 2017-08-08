package me.Allogeneous.AllosEnchantsAPI.exceptions;

public class AEnchantmentInvalidAEnchantmentException extends Exception{

	private static final long serialVersionUID = -2892873135019743981L;
	
	/**
	 * Thrown when an AEnchantment is not valid.
	 * 
	 * @param msg
	 */
	public AEnchantmentInvalidAEnchantmentException(String msg){
		super(msg);
	}

}
