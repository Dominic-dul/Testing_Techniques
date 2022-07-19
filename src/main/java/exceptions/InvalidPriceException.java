package exceptions;

public class InvalidPriceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "The price is not valid";
	}
}
